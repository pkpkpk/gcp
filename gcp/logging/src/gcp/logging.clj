(ns ^{:url "https://cloud.google.com/java/docs/reference/google-cloud-logging/latest/com.google.cloud.logging.Logging"}
  gcp.logging
  (:refer-clojure :exclude [flush])
  (:require [gcp.core.MonitoredResource :as MonitoredResource]
            [gcp.global :as g]
            [gcp.logging.Exclusion :as Exclusion]
            [gcp.logging.LogDestinationName :as LogDestinationName]
            [gcp.logging.LogEntry :as LogEntry]
            [gcp.logging.Logging :as L]
            [gcp.logging.LoggingOptions :as LO]
            [gcp.logging.Metric :as Metric]
            [gcp.logging.MetricInfo :as MetricInfo]
            [gcp.logging.Severity :as Severity]
            [gcp.logging.Sink :as Sink]
            [gcp.logging.SinkInfo :as SinkInfo]
            [gcp.logging.Synchronicity :as Synchronicity])
  (:import (com.google.api LabelDescriptor)
           (com.google.cloud MonitoredResourceDescriptor MonitoredResourceDescriptor$LabelDescriptor)
           [com.google.cloud.logging LogEntryServerStream Logging Logging$EntryListOption Logging$ListOption Logging$TailOption Logging$WriteOption]))

;;; TODO
;;;  --> convert overload opts to singular map arg

(defonce ^:dynamic *client* nil)

(defn ^Logging client
  ([]
   (client nil))
  ([arg]
   (or *client*
       (if (instance? Logging arg)
         arg
         (LO/get-service arg)))))

(defn create-exclusion
  "create a log exclusion."
  ([exclusion]
   (create-exclusion nil exclusion))
  ([clientable exclusion]
   (Exclusion/to-edn (.create (client clientable) (Exclusion/from-edn exclusion)))))

(defn create-metric
  "create a logs-based metric."
  ([metric]
   (create-metric nil metric))
  ([clientable metric]
   (MetricInfo/to-edn (.create (client clientable) (MetricInfo/from-edn metric)))))

(defn create-sink
  "create a sink."
  ([sink]
   (create-sink nil sink))
  ([clientable sink]
   (SinkInfo/to-edn (.create (client clientable) (SinkInfo/from-edn sink)))))

(defn delete-exclusion
  "delete an exclusion by name."
  ([exclusion] (delete-exclusion nil exclusion))
  ([clientable exclusion] (.deleteExclusion (client clientable) exclusion)))

(defn delete-log
  "delete a log by name (default destination)."
  ([log] (delete-log nil log))
  ([clientable log] (.deleteLog (client clientable) log)))

(defn delete-log-in-destination
  "delete a log in a specific destination."
  ([arg0]
   (delete-log-in-destination nil arg0 nil))
  ([arg0 arg1]
   (if (g/valid? LO/clientable-schema arg0)
     (delete-log-in-destination arg0 arg1 nil)
     (delete-log-in-destination nil arg0 arg1)))
  ([clientable log destination]
   (.deleteLog (client clientable) (g/coerce :string log) (LogDestinationName/from-edn destination))))

(defn delete-metric
  "delete a metric by name."
  ([metric] (delete-metric nil metric))
  ([clientable metric] (.deleteMetric (client clientable) metric)))

(defn delete-sink
  "delete a sink by name."
  ([sink] (delete-sink nil sink))
  ([clientable sink] (.deleteSink (client clientable) sink)))

(defn flush
  "force-flush buffered writes."
  ([]
   (flush nil))
  ([clientable]
   (.flush (client clientable))))

(defn get-exclusion
  "get an exclusion by name"
  ([exclusion-name]
   (get-exclusion nil exclusion-name))
  ([clientable exclusion-name]
   (Exclusion/to-edn (.getExclusion (client clientable) exclusion-name))))

(defn get-flush-severity
  "current flush threshold `Severity`"
  ([]
   (get-flush-severity nil))
  ([clientable]
   (Severity/to-edn (.getFlushSeverity (client clientable)))))

(defn get-metric
  "get a metric by name"
  ([metric-name]
   (get-metric nil metric-name))
  ([clientable metric-name]
   (.getMetric (client clientable) metric-name)))

(defn get-sink
  "get a sink by name"
  ([sink-name]
   (get-sink nil sink-name))
  ([clientable sink-name]
   (.getSink (client clientable) sink-name)))

(defn get-write-synchronicity
  "current `Synchronicity` for writes."
  ([]
   (get-write-synchronicity nil))
  ([clientable]
   (.name (.getWriteSynchronicity (client clientable)))))

(defn list-exclusions [& args]
  (let [xs (if (and (some? (first args)) (g/valid? LO/clientable-schema (first args)))
             (.listExclusions (client (first args)) (into-array Logging$ListOption (map L/ListOption:from-edn (rest args))))
             (.listExclusions (client) (into-array Logging$ListOption (map L/ListOption:from-edn args))))]
    (map Exclusion/to-edn (seq (.iterateAll xs)))))

(defn list-log-entries [& args]
  (let [xs (if (and (some? (first args)) (g/valid? LO/clientable-schema (first args)))
             (.listMetrics (client (first args)) (into-array Logging$EntryListOption (map L/EntryListOption:from-edn (rest args))))
             (.listMetrics (client) (into-array Logging$EntryListOption (map L/EntryListOption:from-edn args))))]
    (map LogEntry/to-edn (seq (.iterateAll xs)))))

(defn list-logs [& args]
  (if (and (some? (first args)) (g/valid? LO/clientable-schema (first args)))
    (seq (.iterateAll (.listLogs (client (first args)) (into-array Logging$ListOption (map L/ListOption:from-edn (rest args))))))
    (seq (.iterateAll (.listLogs (client) (into-array Logging$ListOption (map L/ListOption:from-edn args)))))))

(defn list-metrics [& args]
  (let [xs (if (and (some? (first args)) (g/valid? LO/clientable-schema (first args)))
             (.listMetrics (client (first args)) (into-array Logging$ListOption (map L/ListOption:from-edn (rest args))))
             (.listMetrics (client) (into-array Logging$ListOption (map L/ListOption:from-edn args))))]
    (map Metric/to-edn (seq (.iterateAll xs)))))

(defn list-MRDs [& args]
  (let [xs (if (and (some? (first args)) (g/valid? LO/clientable-schema (first args)))
             (.listMonitoredResourceDescriptors (client (first args)) (into-array Logging$ListOption (map L/ListOption:from-edn (rest args))))
             (.listMonitoredResourceDescriptors (client) (into-array Logging$ListOption (map L/ListOption:from-edn args))))]
    (map
      (fn [^MonitoredResourceDescriptor mrd]
        (cond-> {:type (.getType mrd)
                 :displayName (.getDisplayName mrd)
                 :name (.getName mrd)}
                (.getDescription mrd) (assoc :description (.getDescription mrd))
                (.getLabels mrd) (assoc :labels
                                        (map
                                          (fn [^MonitoredResourceDescriptor$LabelDescriptor ld]
                                            {:description (.getDescription ld)
                                             :key         (.getKey ld)
                                             :valueType   (.name (.getValueType ld))})
                                          (.getLabels mrd)))))
      (seq (.iterateAll xs)))))

(defn list-sinks [& args]
  (let [xs (if (and (some? (first args)) (g/valid? LO/clientable-schema (first args)))
             (.listSinks (client (first args)) (into-array Logging$ListOption (map L/ListOption:from-edn (rest args))))
             (.listSinks (client) (into-array Logging$ListOption (map L/ListOption:from-edn args))))]
    (map Sink/to-edn (seq (.iterateAll xs)))))

(defn populate-metadata
  "enrich entries with resource/labels."
  ([logEntries customResource exclusionPaths]
   (populate-metadata nil logEntries customResource exclusionPaths))
  ([clientable logEntries customResource exclusionPaths]
   (.populateMetadata (client clientable)
                      (map LogEntry/from-edn logEntries)
                      (MonitoredResource/from-edn customResource)
                      (into-array String exclusionPaths))))

(defn set-flush-severity
  "set flush threshold."
  ([severity]
   (set-flush-severity nil severity))
  ([clientable severity]
   (.setFlushSeverity (client clientable) (Severity/from-edn severity))))

(defn set-write-synchronicity
  "set write mode"
  ([synchronicity]
   (set-write-synchronicity nil synchronicity))
  ([clientable synchronicity]
   (.setWriteSynchronicity (client clientable) (Synchronicity/from-edn synchronicity))))

(defn ^LogEntryServerStream tail-log-entries
  "stream/tail entries
   https://cloud.google.com/java/docs/reference/google-cloud-logging/latest/com.google.cloud.logging.LogEntryServerStream"
  [& args]
  (if (and (some? (first args)) (g/valid? LO/clientable-schema (first args)))
    (.tailLogEntries (client (first args)) (into-array Logging$TailOption (map L/TailOption:from-edn (rest args))))
    (.tailLogEntries (client) (into-array Logging$TailOption (map L/TailOption:from-edn args)))))

(defn update-exclusion
  ([exclusion]
   (update-exclusion nil exclusion))
  ([clientable exclusion]
   (Exclusion/to-edn (.update (client clientable) (Exclusion/from-edn exclusion)))))

(defn update-metric
  ([metric]
   (update-metric nil metric))
  ([clientable metric]
   (MetricInfo/to-edn (.update (client clientable) (MetricInfo/from-edn metric)))))

(defn update-sink
  ([sink]
   (update-sink nil sink))
  ([clientable sink]
   (SinkInfo/to-edn (.update (client clientable) (SinkInfo/from-edn sink)))))

(defn- do-write [client logEntries opts]
  (.write client
          (map LogEntry/from-edn logEntries)
          (into-array Logging$WriteOption (map L/WriteOption:from-edn opts))))

(defn write [& args]
  (if (g/valid? LO/clientable-schema (first args))
    (do-write (client (first args)) (second args) (nthrest args 2))
    (do-write (client nil) (first args) (rest args))))
