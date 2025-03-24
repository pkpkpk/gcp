(ns gcp.pubsub.v1.SubscriptionAdminSettings
  (:require [gcp.global :as g])
  (:import (com.google.api.gax.core NoCredentialsProvider)
           (com.google.api.gax.grpc GrpcTransportChannel)
           (com.google.api.gax.rpc FixedTransportChannelProvider TransportChannelProvider)
           (com.google.cloud.pubsub.v1 SubscriptionAdminSettings)
           (io.grpc ManagedChannel ManagedChannelBuilder)))

; https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.cloud.pubsub.v1.SubscriptionAdminSettings
; https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/com.google.cloud.pubsub.v1.SubscriptionAdminSettings.Builder

(defn ^SubscriptionAdminSettings from-edn [arg]
  (g/strict! :gcp/pubsub.SubscriptionAdminSettings arg)
  (let [builder (SubscriptionAdminSettings/newBuilder)]
    (when-let [emulator-host (System/getenv "PUBSUB_EMULATOR_HOST")]
      (let [channel            ^ManagedChannel (-> (ManagedChannelBuilder/forTarget emulator-host)
                                                   (.usePlaintext)
                                                   (.build))
            transport-provider ^TransportChannelProvider (FixedTransportChannelProvider/create (GrpcTransportChannel/create channel))]
        (.shouldAutoClose transport-provider)
        (.setTransportChannelProvider builder transport-provider)
        (.setCredentialsProvider builder (NoCredentialsProvider/create))))
    (.build builder)))
