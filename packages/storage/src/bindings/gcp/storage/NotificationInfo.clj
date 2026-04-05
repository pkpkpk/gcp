;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.storage.NotificationInfo
  {:doc "The class representing Pub/Sub Notification metadata for the Storage.",
   :file-git-sha "8f9f5ec4506bde58fbf2351c99f0d67cdcfcd88e",
   :fqcn "com.google.cloud.storage.NotificationInfo"}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.storage NotificationInfo NotificationInfo$Builder
            NotificationInfo$EventType NotificationInfo$PayloadFormat]))

(declare from-edn
         to-edn
         PayloadFormat-from-edn
         PayloadFormat-to-edn
         EventType-from-edn
         EventType-to-edn)

(def PayloadFormat-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.storage/NotificationInfo.PayloadFormat} "JSON_API_V1" "NONE"])

(def EventType-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.storage/NotificationInfo.EventType} "OBJECT_FINALIZE"
   "OBJECT_METADATA_UPDATE" "OBJECT_DELETE" "OBJECT_ARCHIVE"])

(defn ^NotificationInfo from-edn
  [arg]
  (global/strict! :gcp.storage/NotificationInfo arg)
  (let [builder (NotificationInfo/newBuilder (get arg :topic))]
    (when (seq (get arg :customAttributes))
      (.setCustomAttributes
        builder
        (into {} (map (fn [[k v]] [(name k) v])) (get arg :customAttributes))))
    (when (some? (get arg :etag)) (.setEtag builder (get arg :etag)))
    (when (some? (get arg :objectNamePrefix))
      (.setObjectNamePrefix builder (get arg :objectNamePrefix)))
    (when (some? (get arg :payloadFormat))
      (.setPayloadFormat builder
                         (NotificationInfo$PayloadFormat/valueOf
                           (get arg :payloadFormat))))
    (when (some? (get arg :selfLink))
      (.setSelfLink builder (get arg :selfLink)))
    (.build builder)))

(defn to-edn
  [^NotificationInfo arg]
  {:post [(global/strict! :gcp.storage/NotificationInfo %)]}
  (when arg
    (cond-> {:topic (.getTopic arg)}
      (seq (.getCustomAttributes arg)) (assoc :customAttributes
                                         (into {}
                                               (map (fn [[k v]] [(keyword k)
                                                                 v]))
                                               (.getCustomAttributes arg)))
      (some->> (.getEtag arg)
               (not= ""))
        (assoc :etag (.getEtag arg))
      (seq (.getEventTypes arg))
        (assoc :eventTypes (map (fn [e] (.name e)) (.getEventTypes arg)))
      (some->> (.getNotificationId arg)
               (not= ""))
        (assoc :notificationId (.getNotificationId arg))
      (some->> (.getObjectNamePrefix arg)
               (not= ""))
        (assoc :objectNamePrefix (.getObjectNamePrefix arg))
      (.getPayloadFormat arg) (assoc :payloadFormat
                                (.name (.getPayloadFormat arg)))
      (some->> (.getSelfLink arg)
               (not= ""))
        (assoc :selfLink (.getSelfLink arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "The class representing Pub/Sub Notification metadata for the Storage.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.storage/NotificationInfo}
   [:customAttributes
    {:optional true,
     :getter-doc
       "Returns the list of additional attributes to attach to each Cloud PubSub message published for\nthis notification subscription.",
     :setter-doc nil}
    [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
   [:etag
    {:optional true,
     :getter-doc
       "Returns HTTP 1.1 Entity tag for the notification. See <a\nhref=\"http://tools.ietf.org/html/rfc2616#section-3.11\">Entity Tags</a>",
     :setter-doc nil} [:string {:min 1}]]
   [:eventTypes
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the events that trigger a notification to be sent. If empty, notifications are\ntriggered by any event. See <a\nhref=\"https://cloud.google.com/storage/docs/pubsub-notifications#events\">Event types</a> to get\nlist of available events."}
    [:sequential {:min 1}
     [:enum {:closed true} "OBJECT_FINALIZE" "OBJECT_METADATA_UPDATE"
      "OBJECT_DELETE" "OBJECT_ARCHIVE"]]]
   [:notificationId
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the service-generated id for the notification."}
    [:string {:min 1}]]
   [:objectNamePrefix
    {:optional true,
     :getter-doc
       "Returns the object name prefix for which this notification configuration applies.",
     :setter-doc nil} [:string {:min 1}]]
   [:payloadFormat
    {:optional true,
     :getter-doc "Returns the desired content of the Payload.",
     :setter-doc nil} [:enum {:closed true} "JSON_API_V1" "NONE"]]
   [:selfLink
    {:optional true,
     :getter-doc "Returns the canonical URI of this topic as a string.",
     :setter-doc nil} [:string {:min 1}]]
   [:topic
    {:getter-doc "Returns the topic in Pub/Sub that receives notifications."}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.storage/NotificationInfo schema,
              :gcp.storage/NotificationInfo.EventType EventType-schema,
              :gcp.storage/NotificationInfo.PayloadFormat PayloadFormat-schema}
    {:gcp.global/name "gcp.storage.NotificationInfo"}))