(ns gcp.pubsub.v1.BigQueryConfig
  (:require [clojure.string :as string]
            [gcp.global :as global])
  (:import [com.google.pubsub.v1 BigQueryConfig BigQueryConfig$Builder]))

;https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.pubsub.v1.BigQueryConfig.Builder

(defn to-edn [^BigQueryConfig arg]
  {:post [(global/strict! :gcp.pubsub.v1/BigQueryConfig %)]}
  (cond-> {:writeMetadata (.getWriteMetadata arg)
           :table (.getTable arg)}
          (.getUseTableSchema arg)
          (assoc :useTableSchema (.getUseTableSchema arg))

          (.getUseTopicSchema arg)
          (assoc :useTopicSchema (.getUseTopicSchema arg))

          (and (some? (.getServiceAccountEmail arg))
               (not (string/blank? (.getServiceAccountEmail arg))))
          (assoc :serviceAccountEmail (.getServiceAccountEmail arg))))

(defn ^BigQueryConfig from-edn [{:keys [table] :as arg}]
  (global/strict! :gcp.pubsub.v1/BigQueryConfig arg)
  (let [builder (BigQueryConfig/newBuilder)]
    (if-let [{:keys [project dataset table]} (and (map? table) table)]
      (.setTable builder (str project "." dataset "." table))
      (.setTable builder table))
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

(def schemas
  {:gcp.pubsub.v1/synth.TablePath
   [:and
    :string
    [:fn
     {:error/message "string representations of tables must be in form '{project}.{dataset}.{table}'"}
     '(fn [s] (= 3 (count (clojure.string/split s #"\."))))]]
   :gcp.pubsub.v1/BigQueryConfig
   [:map
    [:writeMetadata :boolean]
    [:table [:or
             :gcp.pubsub.v1/synth.TablePath
             [:map {:doc "effectively :gcp/bigquery.TableId"}
              [:project :string]
              [:dataset :string]
              [:table :string]]]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))