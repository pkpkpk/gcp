(ns gcp.logging.v2.LogName
  (:import (com.google.logging.v2 LogName)))

;https://cloud.google.com/java/docs/reference/google-cloud-logging/latest/com.google.logging.v2.LogName

(def schema any?)

(defn ^LogName from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^LogName arg]
  (throw (Exception. "unimplemented")))
