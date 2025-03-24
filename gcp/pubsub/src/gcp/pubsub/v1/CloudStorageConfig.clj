(ns gcp.pubsub.v1.CloudStorageConfig
  (:require [clojure.string :as string]
            [gcp.global :as g])
  (:import [com.google.pubsub.v1 CloudStorageConfig CloudStorageConfig$Builder]))

;;https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.pubsub.v1.CloudStorageConfig

(defn to-edn [^CloudStorageConfig arg]
  {:post [(g/strict! :gcp/pubsub.CloudStorageConfig %)]}
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
  (g/strict! :gcp/pubsub.CloudStorageConfig arg)
  (let [builder (CloudStorageConfig/newBuilder)]
    (.setBucket builder bucket)
    (.build builder)))
