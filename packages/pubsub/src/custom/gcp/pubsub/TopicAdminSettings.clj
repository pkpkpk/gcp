(ns gcp.pubsub.TopicAdminSettings
  (:require
   [gcp.global :as g])
  (:import
   (com.google.api.gax.core NoCredentialsProvider)
   (com.google.api.gax.grpc GrpcTransportChannel)
   (com.google.api.gax.rpc FixedTransportChannelProvider TransportChannelProvider)
   (com.google.cloud.pubsub.v1 TopicAdminSettings)
   (io.grpc ManagedChannel ManagedChannelBuilder)))

(def schema :any)

(defn ^TopicAdminSettings from-edn
  ([] (from-edn nil))
  ([arg]
   (g/strict! :gcp.pubsub/TopicAdminSettings arg)
   (let [builder (TopicAdminSettings/newBuilder)]
     (when-let [emulator-host (System/getenv "PUBSUB_EMULATOR_HOST")]
       (let [channel            ^ManagedChannel (-> (ManagedChannelBuilder/forTarget emulator-host)
                                                    (.usePlaintext)
                                                    (.build))
             transport-provider ^TransportChannelProvider (FixedTransportChannelProvider/create (GrpcTransportChannel/create channel))]
         (.shouldAutoClose transport-provider)
         (.setTransportChannelProvider builder transport-provider)
         (.setCredentialsProvider builder (NoCredentialsProvider/create))))
     (.build builder))))

(g/include-schema-registry! (with-meta {:gcp.pubsub/TopicAdminSettings schema}
                                       {::g/name "gcp.pubsub.TopicAdminSettings"}))
