(ns gcp.pubsub.v1.Topic
  (:require [clojure.string :as string])
  (:import (com.google.pubsub.v1 Topic)))

;https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.pubsub.v1.Topic

(defn ^Topic from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^Topic arg]
  (cond-> {:name (.getName arg)}

          (not= "STATE_UNSPECIFIED" (.name (.getState arg)))
          (assoc :state (.name (.getState arg)))

          (pos? (.getLabelsCount arg))
          (assoc :labels (.getLabelsMap arg))

          (not (string/blank? (.getKmsKeyName arg)))
          (assoc :kmsKeyName (.getKmsKeyName arg))

          (.hasSchemaSettings arg)
          (assoc :schemaSettings (.getSchemaSettings arg))

          (.hasMessageRetentionDuration arg)
          (assoc :messageRetentionDuration (.getMessageRetentionDuration arg))

          (.hasIngestionDataSourceSettings arg)
          (assoc :ingestionDataSourceSettings (.getIngestionDataSourceSettings arg))

          (.hasMessageStoragePolicy arg)
          (assoc :messageStoragePolicy (.getMessageStoragePolicy arg))

          (pos? (.getMessageTransformsCount arg))
          (assoc :messageTransforms (.getMessageTransformsList arg))))