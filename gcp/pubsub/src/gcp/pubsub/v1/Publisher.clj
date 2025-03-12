(ns gcp.pubsub.v1.Publisher
  (:require [gcp.global :as g]
            [gcp.pubsub.v1.TopicName :as TopicName]
            [jsonista.core :as j])
  (:import
    [com.google.api.core ApiFuture ApiFutureCallback ApiFutures]
    [com.google.api.gax.batching BatchingSettings]
    [com.google.api.gax.core NoCredentialsProvider]
    [com.google.api.gax.grpc GrpcTransportChannel]
    [com.google.api.gax.rpc FixedTransportChannelProvider TransportChannelProvider]
    [com.google.cloud.pubsub.v1 Publisher]
    [com.google.common.util.concurrent MoreExecutors]
    [com.google.protobuf ByteString]
    [com.google.pubsub.v1 PubsubMessage]
    [io.grpc ManagedChannel ManagedChannelBuilder]
    [java.util.concurrent TimeUnit]
    [org.threeten.bp Duration]
    [org.threeten.bp.temporal ChronoUnit]))

#!----------------------------------------------------------------------------------------------------------------------
#!
#! gax.batching
#!

(defn FlowControlSettings-from-edn [arg] (throw (Exception. "unimplemented")))

;;https://cloud.google.com/java/docs/reference/gax/latest/com.google.api.gax.batching.BatchingSettings.Builder
(defn BatchSettings-from-edn
  [{:keys [byteThreshold
           delayThreshold
           elementCountThreshold
           enabled
           flowControlSettings]}]
  (let [builder (BatchingSettings/newBuilder)]
    (when byteThreshold
      (.setRequestByteThreshold builder (long byteThreshold)))
    (when delayThreshold
      (.setDelayThreshold builder (Duration/of delayThreshold ChronoUnit/MILLIS)))
    (when elementCountThreshold
      (.setElementCountThreshold builder (long elementCountThreshold)))
    (when flowControlSettings
      (.setFlowControlSettings builder (FlowControlSettings-from-edn flowControlSettings)))
    (when (some? enabled)
      (.setIsEnabled builder (boolean enabled)))
    (.build builder)))

#!----------------------------------------------------------------------------------------------------------------------

(defn ^Publisher from-edn
  [{:keys [batchSettings
           topicName] :as arg}]
  (g/strict! :gcp/pubsub.Publisher arg)
  (let [topic (TopicName/from-edn topicName)
        builder (Publisher/newBuilder topic)]
    (when-let [host (System/getenv "PUBSUB_EMULATOR_HOST")]
      (println "Using emulator to create Publisher for topic " (str topic))
      (let [channel ^ManagedChannel (-> (ManagedChannelBuilder/forTarget host)
                                        (.usePlaintext)
                                        (.build))
            transport-provider ^TransportChannelProvider (FixedTransportChannelProvider/create (GrpcTransportChannel/create channel))
            _  (.shouldAutoClose transport-provider)]
        (println "configuring publisher to use emulator for topic " (str topic))
        (.setCredentialsProvider builder (NoCredentialsProvider/create))
        (.setChannelProvider builder transport-provider)))
    (when batchSettings
      (.setBatchingSettings builder (BatchSettings-from-edn batchSettings)))
    (.build builder)))

(defn ^boolean shutdown [^Publisher publisher]
  (.shutdown publisher)
  (.awaitTermination publisher 30 TimeUnit/SECONDS))

(defn default-on-failure [throwable msg]
  (throw (ex-info (str "publish-message error: " (.getMessage throwable))
                  {:cause throwable
                   :msg   msg})))

(defonce ^:dynamic *on-error* default-on-failure)

(defn publish-message
  "(fn on-failure [throwable edn-map] ...)"
  ([publisher edn-map]
   (publish-message publisher edn-map *on-error*))
  ([publisher edn-map on-failure]
   (let [message-data (ByteString/copyFromUtf8 (j/write-value-as-string edn-map))
         msg-builder (PubsubMessage/newBuilder)
         _ (.setData msg-builder message-data)
         future (.publish publisher ^PubsubMessage (.build msg-builder))]
     (ApiFutures/addCallback ^ApiFuture future
                             (reify ApiFutureCallback
                               (onSuccess [_ _])
                               (onFailure [_ throwable]
                                 (on-failure throwable edn-map)))
                             (MoreExecutors/directExecutor)))))
