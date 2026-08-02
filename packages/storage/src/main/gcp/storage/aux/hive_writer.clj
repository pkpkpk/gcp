(ns gcp.storage.aux.hive-writer
  "A non-blocking, Hive-partitioned GCS writer for high-volume telemetry.
   - Non-blocking 'offer!' API using a bounded queue.
   - Background thread for GCS I/O and session rotation.
   - Automatic Hive-path resolution (dt=.../hr=...).
   - Soft OTel metrics for dropped events and session failures."
  (:require
   [gcp.storage :as storage]
   [jsonista.core :as j])
  (:import
   (io.opentelemetry.api GlobalOpenTelemetry)
   (java.nio ByteBuffer)
   (java.nio.charset StandardCharsets)
   (java.time Instant ZoneOffset)
   (java.time.format DateTimeFormatter)
   (java.util.concurrent ArrayBlockingQueue TimeUnit)))

(def ^:private date-formatter
  (-> (DateTimeFormatter/ofPattern "yyyy-MM-dd")
      (.withZone ZoneOffset/UTC)))

(def ^:private hour-formatter
  (-> (DateTimeFormatter/ofPattern "HH")
      (.withZone ZoneOffset/UTC)))

(defn- current-hive-prefix
  "Returns a Hive-partitioned path prefix based on current UTC time.
   Example: dt=2026-05-18/hr=14"
  []
  (let [now (Instant/now)]
    (str "dt=" (.format date-formatter now)
         "/hr=" (.format hour-formatter now))))

(def ^:private json-mapper (j/object-mapper {:encode-key-fn name}))

#!=============================================================================
#! OTel

(defn- inc-metric!
  "Increments a counter using the GlobalOpenTelemetry API.
   Fails gracefully if OTel is not initialized or available."
  [name description]
  (try
    (-> (GlobalOpenTelemetry/getMeter "gcp.storage.aux.hive-writer")
        (.counterBuilder name)
        (.setDescription description)
        (.build)
        (.add 1))
    (catch Throwable _)))

(defn- register-queue-gauge!
  "Registers an asynchronous Gauge to observe the current size of the queue.
   This provides early-warning visibility before dropped events occur."
  [queue service-prefix]
  (try
    (-> (GlobalOpenTelemetry/getMeter "gcp.storage.aux.hive-writer")
        (.gaugeBuilder (str "telemetry.queue_depth." (clojure.string/replace service-prefix #"/" "_")))
        (.setDescription "Current number of telemetry events buffered in the Hive-Writer queue")
        (.setUnit "events")
        (.ofLongs)
        (.buildWithCallback #(.record % (long (.size queue)))))
    (catch Throwable _)))

#!=============================================================================

(defn- open-session
  "Opens a new BlobWriteSession for the specified bucket and path using idiomatic gcp.storage."
  [storage-client bucketName path]
  (let [blob-info {:blobId {:bucket bucketName
                            :name path}
                   :contentType "application/x-jsonlines"}]
    (storage/blob-write-session storage-client blob-info {})))

(defn- rotate?
  "Checks if the current session should be rotated based on time or volume."
  [session-start session-bytes max-age-ms max-bytes]
  (or (> (- (System/currentTimeMillis) session-start) max-age-ms)
      (>= session-bytes max-bytes)))

(defn- finalize-session!
  "Closes the WritableByteChannel and blocks until the BlobWriteSession future resolves,
   ensuring the file is fully committed to GCS."
  [session channel]
  (when channel
    (try
      (.close channel)
      (.. session getResult get) ; block until flushed
      (catch Exception e
        (inc-metric! "telemetry.session_failures" "Count of Hive-Writer GCS write failures")
        (throw e)))))

(defn run-loop!
  "Executes the background I/O loop. 
   This function blocks and runs indefinitely until `stop!` is called.
   Consumers should execute this within their preferred thread management system."
  [{:keys [queue storage bucketName servicePrefix replicaId
           maxSessionMs maxSessionBytes state]}]
  (loop [session nil
         channel nil
         session-start 0
         session-bytes 0
         stream-id (System/currentTimeMillis)]
    (let [event (.poll queue 100 TimeUnit/MILLISECONDS)]
      (if (and (nil? event) (= :stopping @state))
        (do (when session (try (finalize-session! session channel) (catch Exception _)))
            (reset! state :stopped)
            :stopped)
        (let [should-rotate (and session (rotate? session-start session-bytes maxSessionMs maxSessionBytes))
              _ (when (and session should-rotate)
                  (try (finalize-session! session channel) (catch Exception _)))
              new-session (if (or (nil? session) should-rotate)
                            (let [new-stream-id (System/currentTimeMillis)
                                  path (str servicePrefix "/" (current-hive-prefix) "/"
                                            replicaId "-" new-stream-id ".jsonl")]
                              (inc-metric! "telemetry.session_rotations" "Count of Hive-Writer session rotations")
                              (open-session storage bucketName path))
                            session)
              new-channel (if (or (nil? session) should-rotate)
                            (.open new-session)
                            channel)
              new-start (if (or (nil? session) should-rotate) (System/currentTimeMillis) session-start)
              new-stream (if (or (nil? session) should-rotate) (System/currentTimeMillis) stream-id)]
          (let [[next-s next-c next-start next-bytes next-stream]
                (if event
                  (try
                    (let [json-bytes (j/write-value-as-bytes event json-mapper)]
                      (.write new-channel (ByteBuffer/wrap json-bytes))
                      (.write new-channel (ByteBuffer/wrap (.getBytes "\n" StandardCharsets/UTF_8)))
                      [new-session new-channel new-start (+ session-bytes (count json-bytes) 1) new-stream])
                    (catch Exception e
                      (inc-metric! "telemetry.session_failures" "Count of Hive-Writer GCS write failures")
                      ;; On error, drop the session and try to recover in the next loop
                      (when new-session (try (finalize-session! new-session new-channel) (catch Exception _)))
                      [nil nil 0 0 (System/currentTimeMillis)]))
                  [new-session new-channel new-start session-bytes new-stream])]
            (recur next-s next-c next-start next-bytes next-stream)))))))

(defn create-hive-writer
  "Options:
   - :bucketName (Required)
   - :servicePrefix (Required) e.g. 'my-service/events'
   - :replicaId (Required) e.g. pod name. 
      WARNING: If creating multiple writers on the same node/pod for the same servicePrefix, 
      ensure the replicaId is strictly unique (e.g., append a UUID). Target files must be 
      unique to prevent BlobWriteSession collisions across threads.
   - :batchingSettings (Required) A (syntactically) GAX BatchingSettings configuration map.
    see (gcp.global/get-schema :gcp.foreign.com.google.api.gax.batching/BatchingSettings)"
  [client {:keys [bucketName servicePrefix replicaId batchingSettings]}]
  (let [fc-settings (:flowControlSettings batchingSettings)
        queue-size (or (:maxOutstandingElementCount fc-settings) 10000)
        limit-behavior (or (:limitExceededBehavior fc-settings) :Ignore)
        _ (when (= limit-behavior :Block) ;; Ensure we are strictly dropping elements to prevent OOM
            (throw (ex-info "Hive-Writer requires limitExceededBehavior to be :Ignore (drop) to prevent blocking the serving thread." {})))
        max-session-ms (if-let [delay-secs (:delayThreshold batchingSettings)]
                         (* delay-secs 1000)
                         300000)                            ;; default 5 mins
        max-session-bytes (or (:requestByteThreshold batchingSettings)
                              67108864)                     ;; default 64MB
        queue (ArrayBlockingQueue. queue-size)]
    (register-queue-gauge! queue servicePrefix)
    {:queue queue
     :storage client
     :bucketName bucketName
     :servicePrefix servicePrefix
     :replicaId replicaId
     :maxSessionMs max-session-ms
     :maxSessionBytes max-session-bytes
     :state (atom :running)}))

(defn offer!
  "Attempts to add an event to the writer.
   Returns true if accepted, false (and increments dropped metric) if queue is full."
  [writer event]
  (if (.offer (:queue writer) event)
    true
    (do
      (inc-metric! "telemetry.dropped_events" "Count of telemetry events dropped due to buffer overflow")
      false)))

(defn stop!
  "Signals the writer loop to gracefully flush pending events and stop.
   Note: If the loop is running in a managed executor, the consumer must 
   await termination of the executor task after calling this."
  [writer]
  (reset! (:state writer) :stopping))
