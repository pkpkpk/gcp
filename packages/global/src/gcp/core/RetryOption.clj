(ns gcp.core.RetryOption
  (:require [gcp.global :as global]))

;https://cloud.google.com/java/docs/reference/google-cloud-core/latest/com.google.cloud.RetryOption

(defn from-edn [arg]
  (throw (Exception. "unimplemented")))

(global/include-schema-registry! (with-meta {:gcp.core/RetryOption :any} {:gcp.global/name (str *ns*)}))