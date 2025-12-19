(ns gcp.pubsub.aux.subscriber
  (:require [clojure.core.async :as a]
            [clojure.core.async.impl.protocols :as impl :refer [ReadPort]]
            [gcp.global :as g]
            [gcp.pubsub.v1.SubscriptionName :as SubscriptionName])
  (:import (com.google.api.gax.core NoCredentialsProvider)
           (com.google.api.gax.grpc GrpcTransportChannel)
           (com.google.api.gax.rpc FixedTransportChannelProvider TransportChannelProvider)
           [com.google.cloud.pubsub.v1 AckReplyConsumer
                                       MessageReceiver
                                       Subscriber]
           (com.google.pubsub.v1 Subscription)
           (io.grpc ManagedChannel ManagedChannelBuilder)))

(defrecord ReadportSubscriber [chan subscriber mc]
  ReadPort
  (take! [_ handler] (impl/take! chan handler)))

(defn subscribe-channel
  [{:keys [channel-buffer xform subscription] :or {channel-buffer 10}}]
  (let [chan (if xform
               (a/chan channel-buffer xform)
               (a/chan channel-buffer))
        sub-name (if (instance? Subscription subscription)
                   (.getName subscription)
                   (if (map? subscription)
                     (if (g/valid? :gcp/pubsub.SubscriptionName subscription)
                       (SubscriptionName/from-edn subscription)
                       (g/coerce :string (:name subscription)))
                     (g/coerce :string subscription)))
        receiver (reify MessageReceiver
                   (receiveMessage [_ message consumer]
                     (if (a/offer! chan message)
                       (.ack ^AckReplyConsumer consumer)
                       (.nack ^AckReplyConsumer consumer))))
        subscriber-builder (Subscriber/newBuilder ^String sub-name ^MessageReceiver receiver)
        ?mc (when (System/getenv "PUBSUB_EMULATOR_HOST")
              (let [mc ^ManagedChannel (-> (ManagedChannelBuilder/forTarget (System/getenv "PUBSUB_EMULATOR_HOST"))
                                           (.usePlaintext)
                                           (.build))
                    transport-provider ^TransportChannelProvider (FixedTransportChannelProvider/create (GrpcTransportChannel/create mc))]
                (.shouldAutoClose transport-provider)
                (.setCredentialsProvider subscriber-builder (NoCredentialsProvider/create))
                (.setChannelProvider subscriber-builder ^TransportChannelProvider transport-provider)
                mc))
        subscriber (.build subscriber-builder)]
    (.startAsync subscriber)
    (.awaitRunning subscriber)
    (ReadportSubscriber. chan subscriber ?mc)))

(defn shutdown [{:keys [chan subscriber mc]}]
  (.stopAsync subscriber)
  (when mc (.shutdownNow ^ManagedChannel mc))
  (.awaitTerminated subscriber)
  (a/close! chan))
