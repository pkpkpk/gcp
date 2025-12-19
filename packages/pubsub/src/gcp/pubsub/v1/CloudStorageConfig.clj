(ns gcp.pubsub.v1.CloudStorageConfig
  (:require [clojure.string :as string]
            [gcp.global :as global])
  (:import [com.google.pubsub.v1 CloudStorageConfig CloudStorageConfig$Builder]))

;;https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.pubsub.v1.CloudStorageConfig

(defn to-edn [^CloudStorageConfig arg]
  {:post [(global/strict! :gcp.pubsub.v1/CloudStorageConfig %)]}
  (cond-> {:bucket (.getBucket arg)}

          (not (string/blank? (.getFilenameDatetimeFormat arg)))
          (assoc :filenameDatetimeFormat (.getFilenameDatetimeFormat arg))

          (not (string/blank? (.getFilenameSuffix arg)))
          (assoc :filenameSuffix (.getFilenameSuffix arg))

          (not (string/blank? (.getFilenamePrefix arg)))
          (assoc :filenamePrefix (.getFilenamePrefix arg))

          (pos? (.getMaxMessages arg))
          (assoc :maxMessages (.getMaxMessages arg))

          (.hasAvroConfig arg)
          (assoc :avroConfig (.getAvroConfig arg))

          (.hasTextConfig arg)
          (assoc :textConfig (.getTextConfig arg))

          (.hasMaxDuration arg)
          (assoc :maxDuration (.getMaxDuration arg))))

(defn ^CloudStorageConfig from-edn
  [{:keys [bucket] :as arg}]
  (global/strict! :gcp.pubsub.v1/CloudStorageConfig arg)
  (let [builder (CloudStorageConfig/newBuilder)]
    (.setBucket builder bucket)
    (.build builder)))

(def schemas
  {:gcp.pubsub.v1/CloudStorageConfig
   [:map
    [:bucket :string]
    [:filenameDatetimeFormat {:optional true} :string]
    [:filenameSuffix {:optional true} :string]
    [:filenamePrefix {:optional true} :string]
    [:maxMessages {:optional true} :int]
    [:avroConfig {:optional true} :any]
    [:textConfig {:optional true} :any]
    [:maxDuration {:optional true} :any]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
