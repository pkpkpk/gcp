(ns gcp.core.MonitoredResource
  (:import
   (com.google.cloud MonitoredResource)))

; https://cloud.google.com/java/docs/reference/google-cloud-core/latest/com.google.cloud.MonitoredResource

(def schema any?)

(defn ^MonitoredResource from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^MonitoredResource arg]
  (throw (Exception. "unimplemented")))
