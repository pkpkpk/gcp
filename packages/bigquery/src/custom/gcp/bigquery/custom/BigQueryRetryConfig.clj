(ns gcp.bigquery.custom.BigQueryRetryConfig
  {:file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.BigQueryRetryConfig"
   :gcp.dev/certification {:base-seed 1772513313509
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772513313509 :standard 1772513313510 :stress 1772513313511}
      :protocol-hash
        "0466eb62b07facf617af7b849b8db776a488b4f3168236d1280ba10263c81f91"
      :timestamp "2026-03-03T04:48:33.540727872Z"}}
  (:require
   [gcp.global :as g])
  (:import
   (com.google.cloud.bigquery BigQueryRetryConfig)))

(declare from-edn to-edn)

(defn ^BigQueryRetryConfig from-edn
  [arg]
  (g/strict! :gcp.bigquery/BigQueryRetryConfig arg)
  (let [builder (BigQueryRetryConfig/newBuilder)]
    (when-let [messages (:retriableErrorMessages arg)]
      (.retryOnMessage builder (into-array String messages)))
    (when-let [regexes (:retriableRegExes arg)]
      (.retryOnRegEx builder (into-array String regexes)))
    (.build builder)))

(defn to-edn
  [^BigQueryRetryConfig arg]
  {:post [(g/strict! :gcp.bigquery/BigQueryRetryConfig %)]}
  (cond-> {}
          (.getRetriableErrorMessages arg) (assoc :retriableErrorMessages
                                                  (set (.getRetriableErrorMessages arg)))
          (.getRetriableRegExes arg) (assoc :retriableRegExes
                                            (set (.getRetriableRegExes arg)))))

(def schema
  [:map
   {:closed true
    :doc nil
    :gcp/category :accessor-with-builder
    :gcp/key :gcp.bigquery/BigQueryRetryConfig}
   [:retriableErrorMessages {:optional true}
    [:set [:string {:min 1}]]]
   [:retriableRegExes {:optional true}
    [:set [:string {:min 1}]]]])

(g/include-schema-registry!
  (with-meta {:gcp.bigquery/BigQueryRetryConfig schema}
             {:global/name "gcp.bigquery.custom.BigQueryRetryConfig"}))
