(ns gcp.pubsub.v1.TopicAdminSettings
  (:require [gcp.global :as global])
  (:import (com.google.api.gax.core NoCredentialsProvider)
           (com.google.api.gax.grpc GrpcTransportChannel)
           (com.google.api.gax.rpc FixedTransportChannelProvider TransportChannelProvider)
           [com.google.cloud.pubsub.v1 TopicAdminSettings]
           (io.grpc ManagedChannel ManagedChannelBuilder)))

(defn to-edn [^TopicAdminSettings arg] (throw (Exception. "unimplemented")))

(defn ^TopicAdminSettings from-edn [arg]
  (global/strict! :gcp.pubsub.v1/TopicAdminSettings arg)
  (let [builder (TopicAdminSettings/newBuilder)]
    (when-let [emulator-host (System/getenv "PUBSUB_EMULATOR_HOST")]
      (let [channel            ^ManagedChannel (-> (ManagedChannelBuilder/forTarget emulator-host)
                                                   (.usePlaintext)
                                                   (.build))
            transport-provider ^TransportChannelProvider (FixedTransportChannelProvider/create (GrpcTransportChannel/create channel))]
        (.shouldAutoClose transport-provider)
        (.setTransportChannelProvider builder transport-provider)
        (.setCredentialsProvider builder (NoCredentialsProvider/create))))
    (.build builder)))

(global/include-schema-registry! (with-meta {:gcp.pubsub.v1/TopicAdminSettings :any} {:gcp.global/name (str *ns*)}))
