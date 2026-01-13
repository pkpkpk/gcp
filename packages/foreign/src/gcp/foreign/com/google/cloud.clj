(ns gcp.foreign.com.google.cloud
  {:gcp.dev/certification
   {:MonitoredResource
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767557871664
       :timestamp "2026-01-04T20:17:51.703621525Z"
       :passed-stages
         {:smoke 1767557871664 :standard 1767557871665 :stress 1767557871666}
       :source-hash
         "19a033f51c5782388dad10259d9088c4c8d358fc0df767170278e8dd7186b770"}
    :MonitoredResourceDescriptor
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767557871704
       :timestamp "2026-01-04T20:17:51.750784586Z"
       :passed-stages
         {:smoke 1767557871704 :standard 1767557871705 :stress 1767557871706}
       :source-hash
         "19a033f51c5782388dad10259d9088c4c8d358fc0df767170278e8dd7186b770"}
    :RetryOption
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767557871752
       :timestamp "2026-01-04T20:17:51.759657220Z"
       :passed-stages
         {:smoke 1767557871752 :standard 1767557871753 :stress 1767557871754}
       :source-hash
         "19a033f51c5782388dad10259d9088c4c8d358fc0df767170278e8dd7186b770"}}}
  (:require [gcp.global :as global]
            [gcp.foreign.com.google.api :as api]
            [gcp.foreign.com.google.protobuf :as protobuf])
  (:import
   (com.google.cloud MonitoredResource MonitoredResourceDescriptor RetryOption RetryOption$OptionType)
   (java.time Duration)))

(defn- edn->java-duration [arg]
  (if (number? arg)
    (Duration/ofSeconds arg)
    (let [{:keys [seconds nanos] :or {seconds 0 nanos 0}} arg]
      (Duration/ofSeconds seconds nanos))))

(defn- java-duration->edn [^Duration d]
  {:seconds (.getSeconds d)
   :nanos (.getNano d)})

;; MonitoredResource
(defn ^MonitoredResource MonitoredResource-from-edn [arg]
  (MonitoredResource/fromPb (api/MonitoredResource-from-edn arg)))

(defn MonitoredResource-to-edn [^MonitoredResource arg]
  (api/MonitoredResource-to-edn (.toPb arg)))

;; MonitoredResourceDescriptor
(defn ^MonitoredResourceDescriptor MonitoredResourceDescriptor-from-edn [arg]
  (MonitoredResourceDescriptor/fromPb (api/MonitoredResourceDescriptor-from-edn arg)))

(defn MonitoredResourceDescriptor-to-edn [^MonitoredResourceDescriptor arg]
  (api/MonitoredResourceDescriptor-to-edn (.toPb arg)))

;; RetryOption
(defn RetryOption-from-edn [arg]
  (cond
    (:totalTimeout arg) (RetryOption/totalTimeoutDuration (edn->java-duration (:totalTimeout arg)))
    (:initialRetryDelay arg) (RetryOption/initialRetryDelayDuration (edn->java-duration (:initialRetryDelay arg)))
    (:retryDelayMultiplier arg) (RetryOption/retryDelayMultiplier (double (:retryDelayMultiplier arg)))
    (:maxRetryDelay arg) (RetryOption/maxRetryDelayDuration (edn->java-duration (:maxRetryDelay arg)))
    (:maxAttempts arg) (RetryOption/maxAttempts (int (:maxAttempts arg)))
    (contains? arg :jittered) (RetryOption/jittered (boolean (:jittered arg)))
    :else (throw (ex-info "Unknown RetryOption" {:arg arg}))))

(defn RetryOption-to-edn [^RetryOption arg]
  ;; Use reflection to get type and value
  (let [f-type (.getDeclaredField RetryOption "type")
        f-value (.getDeclaredField RetryOption "value")]
    (.setAccessible f-type true)
    (.setAccessible f-value true)
    (let [type (.get f-type arg)
          value (.get f-value arg)]
      (case (str type)
        "TOTAL_TIMEOUT" {:totalTimeout (java-duration->edn value)}
        "INITIAL_RETRY_DELAY" {:initialRetryDelay (java-duration->edn value)}
        "RETRY_DELAY_MULTIPLIER" {:retryDelayMultiplier value}
        "MAX_RETRY_DELAY" {:maxRetryDelay (java-duration->edn value)}
        "MAX_ATTEMPTS" {:maxAttempts value}
        "JITTERED" {:jittered value}
        (throw (ex-info "Unknown RetryOption type" {:type type}))))))

(global/include-schema-registry!
 (with-meta
   {::RetryOption [:or
                   [:map {:closed true} [:totalTimeout ::protobuf/Duration]]
                   [:map {:closed true} [:initialRetryDelay ::protobuf/Duration]]
                   [:map {:closed true} [:retryDelayMultiplier :double]]
                   [:map {:closed true} [:maxRetryDelay ::protobuf/Duration]]
                   [:map {:closed true} [:maxAttempts :int]]
                   [:map {:closed true} [:jittered :boolean]]]
    ::MonitoredResource [:map
                         [:type :string]
                         [:labels [:map-of :string :string]]]
    ::MonitoredResourceDescriptor [:map
                                   [:type :string]
                                   [:displayName :string]
                                   [:description :string]
                                   [:labels [:sequential :gcp.foreign.com.google.api/LabelDescriptor]]]}
   {::global/name ::registry}))
