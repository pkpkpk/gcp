(ns gcp.bigquery.v2.BigQueryRetryConfig
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery BigQueryRetryConfig)))

(defn ^BigQueryRetryConfig from-edn
  [{:keys [errorMessages regExPatterns] :as arg}]
  (global/strict! :gcp.bigquery.v2/BigQueryRetryConfig arg)
  (let [builder (BigQueryRetryConfig/newBuilder)]
    (when (seq errorMessages)
      (.retryOnMessage builder (into-array String errorMessages)))
    (when (seq regExPatterns)
      (.retryOnRegEx builder (into-array String regExPatterns)))
    (.build builder)))

(def schemas
  {:gcp.bigquery.v2/BigQueryRetryConfig
   [:and
    {:doc "part of JobOption"}
    [:map
     [:errorMessages {:optional true} [:sequential :string]]
     [:regExPatterns {:optional true} [:sequential :string]]]
    [:fn
     {:error/message "must be one of :errorMessages or :regExPatterns"}
     '(fn [m] (or (contains? m :errorMessages) (contains? m :regExPatterns)))]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))