;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.ModelInfo
  {:doc
     "Google BigQuery ML model information. Models are not created directly via the API, but by issuing\na CREATE MODEL query.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/standard-sql/bigqueryml-syntax-create\">CREATE\n    MODEL statement</a>"
   :file-git-sha "6e3e07a22b8397e1e9d5b567589e44abc55961f2"
   :fqcn "com.google.cloud.bigquery.ModelInfo"
   :gcp.dev/certification
     {:base-seed 1772392608388
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772392608388 :standard 1772392608389 :stress 1772392608390}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T19:17:18.340505537Z"}}
  (:require [gcp.bigquery.custom.StandardSQL :as StandardSQL]
            [gcp.bindings.bigquery.EncryptionConfiguration :as
             EncryptionConfiguration]
            [gcp.bindings.bigquery.ModelId :as ModelId]
            [gcp.bindings.services.bigquery.model.TrainingRun :as TrainingRun]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery ModelInfo ModelInfo$Builder]))

(defn ^ModelInfo from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/ModelInfo arg)
  (let [builder (ModelInfo/newBuilder (ModelId/from-edn (get arg :modelId)))]
    (when (some? (get arg :description))
      (.setDescription builder (get arg :description)))
    (when (some? (get arg :encryptionConfiguration))
      (.setEncryptionConfiguration builder
                                   (EncryptionConfiguration/from-edn
                                     (get arg :encryptionConfiguration))))
    (when (some? (get arg :expirationTime))
      (.setExpirationTime builder (get arg :expirationTime)))
    (when (some? (get arg :friendlyName))
      (.setFriendlyName builder (get arg :friendlyName)))
    (when (some? (get arg :labels))
      (.setLabels builder
                  (into {} (map (fn [[k v]] [(name k) v])) (get arg :labels))))
    (.build builder)))

(defn to-edn
  [^ModelInfo arg]
  {:post [(global/strict! :gcp.bindings.bigquery/ModelInfo %)]}
  (cond-> {:modelId (ModelId/to-edn (.getModelId arg))}
    (.getCreationTime arg) (assoc :creationTime (.getCreationTime arg))
    (.getDescription arg) (assoc :description (.getDescription arg))
    (.getEncryptionConfiguration arg) (assoc :encryptionConfiguration
                                        (EncryptionConfiguration/to-edn
                                          (.getEncryptionConfiguration arg)))
    (.getEtag arg) (assoc :etag (.getEtag arg))
    (.getExpirationTime arg) (assoc :expirationTime (.getExpirationTime arg))
    (.getFeatureColumns arg) (assoc :featureColumnList
                               (map StandardSQL/StandardSQLField-to-edn
                                 (.getFeatureColumns arg)))
    (.getFriendlyName arg) (assoc :friendlyName (.getFriendlyName arg))
    (.getLabelColumns arg) (assoc :labelColumnList
                             (map StandardSQL/StandardSQLField-to-edn
                               (.getLabelColumns arg)))
    (.getLabels arg)
      (assoc :labels
        (into {} (map (fn [[k v]] [(keyword k) v])) (.getLabels arg)))
    (.getLastModifiedTime arg) (assoc :lastModifiedTime
                                 (.getLastModifiedTime arg))
    (.getLocation arg) (assoc :location (.getLocation arg))
    (.getModelType arg) (assoc :modelType (.getModelType arg))
    (.getTrainingRuns arg) (assoc :trainingRunList
                             (map TrainingRun/to-edn (.getTrainingRuns arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery ML model information. Models are not created directly via the API, but by issuing\na CREATE MODEL query.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/standard-sql/bigqueryml-syntax-create\">CREATE\n    MODEL statement</a>",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/ModelInfo}
   [:creationTime
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the time when this model was created, in milliseconds since the epoch."}
    :int]
   [:description
    {:optional true,
     :getter-doc "Returns the user description of the model.",
     :setter-doc "Sets the user description for this model."}
    [:string {:min 1}]]
   [:encryptionConfiguration {:optional true}
    :gcp.bindings.bigquery/EncryptionConfiguration]
   [:etag
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the hash of the model resource."} [:string {:min 1}]]
   [:expirationTime
    {:optional true,
     :getter-doc
       "Returns this this model expires, in milliseconds since the epoch. If not present, the model\nwill persist indefinitely. Expired models will be deleted.",
     :setter-doc
       "Set the time when this model expires, in milliseconds since the epoch. If not present, the\nmodel persists indefinitely. Expired models will be deleted."}
    :int]
   [:featureColumnList
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns information about the feature columns for this model."}
    [:sequential :gcp.bigquery.custom.StandardSQL/StandardSQLField]]
   [:friendlyName
    {:optional true,
     :getter-doc "Returns the user-friendly name for the model.",
     :setter-doc "Sets the user-friendly name for this model."}
    [:string {:min 1}]]
   [:labelColumnList
    {:optional true,
     :read-only? true,
     :getter-doc "Returns information about the label columns for this model."}
    [:sequential :gcp.bigquery.custom.StandardSQL/StandardSQLField]]
   [:labels
    {:optional true,
     :getter-doc "Returns a map for labels applied to the model.",
     :setter-doc
       "Set the labels applied to this model.\n\n<p>When used with {@link BigQuery#update(ModelInfo, ModelOption...)}, setting {@code labels}\nto {@code null} removes all labels; otherwise all keys that are mapped to {@code null} values\nare removed and other keys are updated to their respective values."}
    [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
   [:lastModifiedTime
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the time when this table was last modified, in milliseconds since the epoch."}
    :int]
   [:location
    {:optional true,
     :read-only? true,
     :getter-doc "Returns a location of the model."} [:string {:min 1}]]
   [:modelId {:getter-doc "Returns the model identity."}
    :gcp.bindings.bigquery/ModelId]
   [:modelType
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the type of the ML model."} [:string {:min 1}]]
   [:trainingRunList
    {:optional true,
     :read-only? true,
     :getter-doc "Returns metadata about each training run iteration."}
    [:sequential :gcp.bindings.services.bigquery.model/TrainingRun]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/ModelInfo schema}
    {:gcp.global/name "gcp.bindings.bigquery.ModelInfo"}))