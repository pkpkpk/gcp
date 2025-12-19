(ns gcp.logging.SinkInfo
  (:require [gcp.global :as g])
  (:import (com.google.cloud.logging SinkInfo SinkInfo$Destination SinkInfo$Destination$BucketDestination SinkInfo$Destination$DatasetDestination SinkInfo$Destination$LoggingBucketDestination SinkInfo$Destination$TopicDestination SinkInfo$VersionFormat)))

(def SinkInfo$Destination:schema
  [:or
   [:map {:closed true}
    [:type [:= "BUCKET"]]
    [:bucket :string]]
   [:map {:closed true}
    [:type [:= "DATASET"]]
    [:dataset :string]
    [:project {:optional true} :string]]
   [:map {:closed true}
    [:type [:= "LOGGING_BUCKET"]]
    [:bucket :string]
    [:location :string]
    [:project {:optional true} :string]]
   [:map {:closed true}
    [:type [:= "TOPIC"]]
    [:topic :string]
    [:project {:optional true} :string]]])

(defn ^SinkInfo$Destination SinkInfo$Destination:from-edn
  [{:keys [type project] :as arg}]
  (g/strict! SinkInfo$Destination:schema arg)
  (case type
    "BUCKET" (SinkInfo$Destination$BucketDestination/of (:bucket arg))
    "DATASET" (if project
                (SinkInfo$Destination$DatasetDestination/of project (:dataset arg))
                (SinkInfo$Destination$DatasetDestination/of (:dataset arg)))
    "LOGGING_BUCKET" (if project
                       (SinkInfo$Destination$LoggingBucketDestination/of project (:location arg) (:bucket arg))
                       (SinkInfo$Destination$LoggingBucketDestination/of (:location arg) (:bucket arg)))
    "TOPIC" (if project
              (SinkInfo$Destination$TopicDestination/of project (:topic arg))
              (SinkInfo$Destination$TopicDestination/of (:topic arg)))))

(defn SinkInfo$Destination:to-edn [^SinkInfo$Destination arg]
  (let [T (.name (.getType arg))]
    (case T
      "BUCKET" {:type T
                :bucket (.getBucket ^SinkInfo$Destination$BucketDestination arg)}
      "DATASET" (let [arg ^SinkInfo$Destination$DatasetDestination arg]
                  (cond-> {:type    T
                           :dataset (.getDataset  arg)}
                          (.getProject arg) (assoc :project (.getProject arg))))
      "LOGGING_BUCKET" (let [arg ^SinkInfo$Destination$LoggingBucketDestination arg]
                         (cond-> {:type    T
                                  :location (.getLocation  arg)}
                                 (.getProject arg) (assoc :project (.getProject arg))))
      "TOPIC" (let [arg ^SinkInfo$Destination$TopicDestination arg]
                (cond-> {:type    T
                         :topic (.getTopic  arg)}
                        (.getProject arg) (assoc :project (.getProject arg)))))))

(def schema
  [:map
   {:url "https://cloud.google.com/java/docs/reference/google-cloud-logging/latest/com.google.cloud.logging.SinkInfo"}
   [:name :string]
   [:destination SinkInfo$Destination:schema]
   [:filter {:optional true} :string]
   [:versionFormat {:optional true} [:enum "V1" "V2"]]])

(defn ^SinkInfo from-edn
  [{:keys [name destination] :as arg}]
  (g/strict! schema arg)
  (let [builder (SinkInfo/newBuilder name (SinkInfo$Destination:from-edn destination))]
    (some->> (:filter arg) (.setFilter builder))
    (some->> (:versionFormat arg)
             (SinkInfo$VersionFormat/valueOf)
             (.setVersionFormat builder))
    (.build builder)))

(defn to-edn [^SinkInfo arg]
  (cond->
    {:type          (.getName arg)
     :destination   (SinkInfo$Destination:to-edn (.getDestination arg))
     :versionFormat (str (.getVersionFormat arg))}
    (.getFilter arg) (assoc :filter (.getFilter arg))))