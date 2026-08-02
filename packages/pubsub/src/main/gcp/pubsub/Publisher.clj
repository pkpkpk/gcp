(ns gcp.pubsub.Publisher
  (:require
   [gcp.foreign.com.google.api.gax.batching :as batching]
   [gcp.foreign.com.google.api.gax.retrying :as retrying]
   [gcp.global :as g]
   [gcp.pubsub.v1.TopicName :as TopicName]
   [jsonista.core :as j])
  (:import
   (com.google.api.core ApiFuture ApiFutureCallback ApiFutures)
   (com.google.api.gax.core NoCredentialsProvider)
   (com.google.api.gax.grpc GrpcTransportChannel)
   (com.google.api.gax.rpc FixedTransportChannelProvider TransportChannelProvider)
   (com.google.cloud.pubsub.v1 Publisher)
   (com.google.common.util.concurrent MoreExecutors)
   (com.google.protobuf ByteString)
   (com.google.pubsub.v1 PubsubMessage)
   (io.grpc ManagedChannel ManagedChannelBuilder)
   (java.util.concurrent TimeUnit)))

(def schema
  [:map {:closed true}
   [:topicName :gcp.pubsub.v1/TopicName]
   [:batchingSettings {:optional true} ::batching/BatchingSettings]
   [:retrySettings {:optional true} ::retrying/RetrySettings]
   [:enableOpenTelemetryTracing {:optional true
                                 :doc "Gives the ability to enable Open Telemetry Tracing"} :boolean]
   [:openTelemetry {:optional true
                    :doc "Sets the instance of OpenTelemetry for the Publisher class."}
    (g/instance-schema io.opentelemetry.api.OpenTelemetry)]])

(defn ^Publisher from-edn [arg]
  (g/strict! :gcp.pubsub/Publisher arg)
  (let [topicName (TopicName/from-edn (:topicName arg))
        builder   (Publisher/newBuilder topicName)]
    ;; ----------------------------------------------------------------------------------
    (when-let [batchingSettings (:batchingSettings arg)]
      (.setBatchingSettings builder (batching/BatchingSettings-from-edn batchingSettings)))
    (when-let [retrySettings (:retrySettings arg)]
      (.setRetrySettings builder (retrying/RetrySettings-from-edn retrySettings)))
    (some->> (:enableOpenTelemetryTracing arg) (.setEnableOpenTelemetryTracing builder))
    (some->> (:openTelemetry arg) (.setOpenTelemetry builder))
    ;; ----------------------------------------------------------------------------------
    (when-let [host (System/getenv "PUBSUB_EMULATOR_HOST")]
      (println "Using emulator to create Publisher for topic " (str topicName))
      (let [channel            ^ManagedChannel (-> (ManagedChannelBuilder/forTarget host)
                                                   (.usePlaintext)
                                                   (.build))
            transport-provider ^TransportChannelProvider (FixedTransportChannelProvider/create (GrpcTransportChannel/create channel))
            _                  (.shouldAutoClose transport-provider)]
        (println "configuring publisher to use emulator for topic " (str topicName))
        (.setCredentialsProvider builder (NoCredentialsProvider/create))
        (.setChannelProvider builder transport-provider)))
    (.build builder)))

(defn ^boolean shutdown [^Publisher publisher]
  ;(.publishAllOutstanding publisher)
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

(g/include-schema-registry! (with-meta {:gcp.pubsub/Publisher schema} {::g/name "gcp.pubsub.Publisher"}))

(comment
  {:name "setChannelProvider"
   :doc "{@code ChannelProvider} to use to create Channels, which must point at Cloud Pub/Sub
        endpoint.

        <p>For performance, this client benefits from having multiple underlying connections. See
        {@link com.google.api.gax.grpc.InstantiatingGrpcChannelProvider.Builder#setPoolSize(int)}."
   :parameters [{:name "channelProvider", :type com.google.api.gax.rpc.TransportChannelProvider}]}
  {:name "setCompressionBytesThreshold"
   :parameters [{:name "compressionBytesThreshold", :type long}]
   :doc "Sets the threshold (in bytes) above which messages are compressed for transport. Only takes
        effect if setEnableCompression(true) is also called.\""}
  {:name "setCredentialsProvider"
   :doc "{@code CredentialsProvider} to use to create Credentials to authenticate calls."
   :parameters [{:name "credentialsProvider", :type com.google.api.gax.core.CredentialsProvider}]}
  {:name "setEnableCompression"
   :parameters [{:name "enableCompression", :type boolean}]
   :doc "Gives the ability to enable transport compression."}
  {:name "setEnableMessageOrdering"
   :parameters [{:name "enableMessageOrdering", :type boolean}]
   :doc "Sets the message ordering option."}
  {:name "setEndpoint"
   :parameters [{:name "endpoint", :type java.lang.String}]
   :doc "Gives the ability to override the gRPC endpoint."}
  {:name "setExecutorProvider"
   :doc "Gives the ability to set a custom executor to be used by the library."
   :parameters [{:name "executorProvider", :type com.google.api.gax.core.ExecutorProvider}]}
  {:name "setHeaderProvider"
   :doc "Sets the static header provider. The header provider will be called during client
        construction only once. The headers returned by the provider will be cached and supplied as
        is for each request issued by the constructed client. Some reserved headers can be overridden
        (e.g. Content-Type) or merged with the default value (e.g. User-Agent) by the underlying
        transport layer.

        @param headerProvider the header provider
        @return the builder"
   :parameters [{:name "headerProvider", :type com.google.api.gax.rpc.HeaderProvider}]}
  {:name "setInternalHeaderProvider"
   :doc "Sets the static header provider for getting internal (library-defined) headers. The header
        provider will be called during client construction only once. The headers returned by the
        provider will be cached and supplied as is for each request issued by the constructed client.
        Some reserved headers can be overridden (e.g. Content-Type) or merged with the default value
        (e.g. User-Agent) by the underlying transport layer.

        @param internalHeaderProvider the internal header provider
        @return the builder"
   :parameters [{:name "internalHeaderProvider", :type com.google.api.gax.rpc.HeaderProvider}]}

  {:name "setTransform"
   :doc "Gives the ability to set an {@link ApiFunction} that will transform the {@link PubsubMessage}
        before it is sent"
   :parameters [{:name "messageTransform"
                 :type [com.google.api.core.ApiFunction
                        com.google.pubsub.v1.PubsubMessage
                        com.google.pubsub.v1.PubsubMessage]}]}
  {:name "setUniverseDomain"
   :parameters [{:name "universeDomain", :type java.lang.String}]
   :doc "Gives the ability to override the universe domain."})
