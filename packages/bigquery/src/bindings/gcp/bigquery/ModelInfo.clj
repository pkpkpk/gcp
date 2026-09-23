;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ModelInfo
  {:doc
     "Google BigQuery ML model information. Models are not created directly via the API, but by issuing\na CREATE MODEL query.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/standard-sql/bigqueryml-syntax-create\">CREATE\n    MODEL statement</a>"
   :file-git-sha "9b6aa80452ca4b123ad92df10545db8bfca2e622"
   :fqcn "com.google.cloud.bigquery.ModelInfo"
   :gcp.dev/certification
     {:base-seed 1790034194504
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034194504 :standard 1790034194505 :stress 1790034194506}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:43:15.764034328Z"}}
  (:require [gcp.api.services.bigquery.model.TrainingRun :as TrainingRun]
            [gcp.bigquery.EncryptionConfiguration :as EncryptionConfiguration]
            [gcp.bigquery.ModelId :as ModelId]
            [gcp.bigquery.custom.StandardSQL :as StandardSQL]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery ModelInfo ModelInfo$Builder]))

(declare from-edn to-edn)

(defn ^ModelInfo from-edn
  [arg]
  (global/strict! :gcp.bigquery/ModelInfo arg)
  (let [builder (ModelInfo/newBuilder (ModelId/from-edn (get arg :modelId)))]
    (when (some? (get arg :description))
      (.setDescription builder (get arg :description)))
    (when (some? (get arg :encryptionConfiguration))
      (.setEncryptionConfiguration builder
                                   (EncryptionConfiguration/from-edn
                                     (get arg :encryptionConfiguration))))
    (when (some? (get arg :expirationTime))
      (.setExpirationTime builder (long (get arg :expirationTime))))
    (when (some? (get arg :friendlyName))
      (.setFriendlyName builder (get arg :friendlyName)))
    (when (seq (get arg :labels))
      (.setLabels builder
                  (into {} (map (fn [[k v]] [(name k) v])) (get arg :labels))))
    (.build builder)))

(defn to-edn
  [^ModelInfo arg]
  {:post [(global/strict! :gcp.bigquery/ModelInfo %)]}
  (when arg
    (cond-> {:modelId (ModelId/to-edn (.getModelId arg))}
      (.getCreationTime arg) (assoc :creationTime (.getCreationTime arg))
      (some->> (.getDescription arg)
               (not= ""))
        (assoc :description (.getDescription arg))
      (.getEncryptionConfiguration arg) (assoc :encryptionConfiguration
                                          (EncryptionConfiguration/to-edn
                                            (.getEncryptionConfiguration arg)))
      (some->> (.getEtag arg)
               (not= ""))
        (assoc :etag (.getEtag arg))
      (.getExpirationTime arg) (assoc :expirationTime (.getExpirationTime arg))
      (seq (.getFeatureColumns arg)) (assoc :featureColumnList
                                       (mapv StandardSQL/StandardSQLField-to-edn
                                         (.getFeatureColumns arg)))
      (some->> (.getFriendlyName arg)
               (not= ""))
        (assoc :friendlyName (.getFriendlyName arg))
      (seq (.getLabelColumns arg)) (assoc :labelColumnList
                                     (mapv StandardSQL/StandardSQLField-to-edn
                                       (.getLabelColumns arg)))
      (seq (.getLabels arg))
        (assoc :labels
          (into {} (map (fn [[k v]] [(keyword k) v])) (.getLabels arg)))
      (.getLastModifiedTime arg) (assoc :lastModifiedTime
                                   (.getLastModifiedTime arg))
      (some->> (.getLocation arg)
               (not= ""))
        (assoc :location (.getLocation arg))
      (some->> (.getModelType arg)
               (not= ""))
        (assoc :modelType (.getModelType arg))
      (seq (.getTrainingRuns arg)) (assoc :trainingRunList
                                     (mapv TrainingRun/to-edn
                                       (.getTrainingRuns arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery ML model information. Models are not created directly via the API, but by issuing\na CREATE MODEL query.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/standard-sql/bigqueryml-syntax-create\">CREATE\n    MODEL statement</a>",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/ModelInfo}
   [:creationTime
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the time when this model was created, in milliseconds since the epoch."}
    :i64]
   [:description
    {:optional true,
     :getter-doc "Returns the user description of the model.",
     :setter-doc "Sets the user description for this model."}
    [:string {:min 1, :gen/max 1}]]
   [:encryptionConfiguration {:optional true, :setter-doc nil}
    :gcp.bigquery/EncryptionConfiguration]
   [:etag
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the hash of the model resource."}
    [:string {:min 1, :gen/max 1}]]
   [:expirationTime
    {:optional true,
     :getter-doc
       "Returns this this model expires, in milliseconds since the epoch. If not present, the model\nwill persist indefinitely. Expired models will be deleted.",
     :setter-doc
       "Set the time when this model expires, in milliseconds since the epoch. If not present, the\nmodel persists indefinitely. Expired models will be deleted."}
    :i64]
   [:featureColumnList
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns information about the feature columns for this model."}
    [:sequential {:min 1, :gen/max 2} :gcp.bigquery/StandardSQLField]]
   [:friendlyName
    {:optional true,
     :getter-doc "Returns the user-friendly name for the model.",
     :setter-doc "Sets the user-friendly name for this model."}
    [:string {:min 1, :gen/max 1}]]
   [:labelColumnList
    {:optional true,
     :read-only? true,
     :getter-doc "Returns information about the label columns for this model."}
    [:sequential {:min 1, :gen/max 2} :gcp.bigquery/StandardSQLField]]
   [:labels
    {:optional true,
     :getter-doc "Returns a map for labels applied to the model.",
     :setter-doc
       "Set the labels applied to this model.\n\n<p>When used with {@link BigQuery#update(ModelInfo, ModelOption...)}, setting {@code labels}\nto {@code null} removes all labels; otherwise all keys that are mapped to {@code null} values\nare removed and other keys are updated to their respective values."}
    [:map-of [:or simple-keyword? [:string {:min 1}]]
     [:string {:min 1, :gen/max 1}]]]
   [:lastModifiedTime
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the time when this table was last modified, in milliseconds since the epoch."}
    :i64]
   [:location
    {:optional true,
     :read-only? true,
     :getter-doc "Returns a location of the model."}
    [:string {:min 1, :gen/max 1}]]
   [:modelId {:getter-doc "Returns the model identity."} :gcp.bigquery/ModelId]
   [:modelType
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the type of the ML model."}
    [:string {:min 1, :gen/max 1}]]
   [:trainingRunList
    {:optional true,
     :read-only? true,
     :getter-doc "Returns metadata about each training run iteration."}
    [:sequential :gcp.api.services.bigquery.model/TrainingRun]]])

(global/include-registry! "gcp.bigquery.ModelInfo"
                          {:gcp.bigquery/ModelInfo schema})