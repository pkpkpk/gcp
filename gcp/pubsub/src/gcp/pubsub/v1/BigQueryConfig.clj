(ns gcp.pubsub.v1.BigQueryConfig
  (:require [clojure.string :as string]
            [gcp.global :as g])
  (:import [com.google.pubsub.v1 BigQueryConfig BigQueryConfig$Builder]))

;https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.pubsub.v1.BigQueryConfig.Builder

(defn to-edn [^BigQueryConfig arg]
  {:post [(g/strict! :gcp/pubsub.BigQueryConfig %)]}
  (cond-> {:writeMetadata (.getWriteMetadata arg)
           :table (.getTable arg)}
          (.getUseTableSchema arg)
          (assoc :useTableSchema (.getUseTableSchema arg))

          (.getUseTopicSchema arg)
          (assoc :useTopicSchema (.getUseTopicSchema arg))

          (and (some? (.getServiceAccountEmail arg))
               (not (string/blank? (.getServiceAccountEmail arg))))
          (assoc :serviceAccountEmail (.getServiceAccountEmail arg))))

(defn ^BigQueryConfig from-edn
  [{:keys [table] :as arg}]
  (g/strict! :gcp/pubsub.BigQueryConfig arg)
  (let [builder (BigQueryConfig/newBuilder)]
    (.setTable table)
    ;; TODO setState setServiceAccountEmail
    (when (contains? arg :serviceAccountEmail)
      (.setServiceAccountEmail builder (get arg :serviceAccountEmail)))
    (when (contains? arg :writeMetadata)
      (.setWriteMetadata builder (:writeMetadata arg)))
    (when (contains? arg :useTableSchema)
      (.setUseTableSchema builder (:useTableSchema arg)))
    (when (contains? arg :useTopicSchema)
      (.setUseTopicSchema builder (:useTopicSchema arg)))
    (.build builder)))