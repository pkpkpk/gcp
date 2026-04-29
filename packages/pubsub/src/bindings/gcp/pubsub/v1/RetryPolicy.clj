;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.RetryPolicy
  {:doc
     "<pre>\nA policy that specifies how Pub/Sub retries message delivery.\n\nRetry delay will be exponential based on provided minimum and maximum\nbackoffs. https://en.wikipedia.org/wiki/Exponential_backoff.\n\nRetryPolicy will be triggered on NACKs or acknowledgment deadline exceeded\nevents for a given message.\n\nRetry Policy is implemented on a best effort basis. At times, the delay\nbetween consecutive deliveries may not match the configuration. That is,\ndelay can be more or less than configured backoff.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.RetryPolicy}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.RetryPolicy"
   :gcp.dev/certification
     {:base-seed 1777403453144
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777403453144 :standard 1777403453145 :stress 1777403453146}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-28T19:10:54.041263583Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global])
  (:import [com.google.protobuf Duration]
           [com.google.pubsub.v1 RetryPolicy RetryPolicy$Builder]))

(declare from-edn to-edn)

(defn ^RetryPolicy from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/RetryPolicy arg)
  (let [builder (RetryPolicy/newBuilder)]
    (when (some? (get arg :maximumBackoff))
      (.setMaximumBackoff builder
                          (protobuf/Duration-from-edn (get arg
                                                           :maximumBackoff))))
    (when (some? (get arg :minimumBackoff))
      (.setMinimumBackoff builder
                          (protobuf/Duration-from-edn (get arg
                                                           :minimumBackoff))))
    (.build builder)))

(defn to-edn
  [^RetryPolicy arg]
  {:post [(global/strict! :gcp.pubsub.v1/RetryPolicy %)]}
  (when arg
    (cond-> {}
      (.hasMaximumBackoff arg) (assoc :maximumBackoff
                                 (protobuf/Duration-to-edn (.getMaximumBackoff
                                                             arg)))
      (.hasMinimumBackoff arg) (assoc :minimumBackoff
                                 (protobuf/Duration-to-edn (.getMinimumBackoff
                                                             arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nA policy that specifies how Pub/Sub retries message delivery.\n\nRetry delay will be exponential based on provided minimum and maximum\nbackoffs. https://en.wikipedia.org/wiki/Exponential_backoff.\n\nRetryPolicy will be triggered on NACKs or acknowledgment deadline exceeded\nevents for a given message.\n\nRetry Policy is implemented on a best effort basis. At times, the delay\nbetween consecutive deliveries may not match the configuration. That is,\ndelay can be more or less than configured backoff.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.RetryPolicy}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.pubsub.v1/RetryPolicy}
   [:maximumBackoff
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The maximum delay between consecutive deliveries of a given\nmessage. Value should be between 0 and 600 seconds. Defaults to 600\nseconds.\n</pre>\n\n<code>.google.protobuf.Duration maximum_backoff = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The maximumBackoff.",
     :setter-doc
       "<pre>\nOptional. The maximum delay between consecutive deliveries of a given\nmessage. Value should be between 0 and 600 seconds. Defaults to 600\nseconds.\n</pre>\n\n<code>\n.google.protobuf.Duration maximum_backoff = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.foreign.com.google.protobuf/Duration]
   [:minimumBackoff
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The minimum delay between consecutive deliveries of a given\nmessage. Value should be between 0 and 600 seconds. Defaults to 10 seconds.\n</pre>\n\n<code>.google.protobuf.Duration minimum_backoff = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The minimumBackoff.",
     :setter-doc
       "<pre>\nOptional. The minimum delay between consecutive deliveries of a given\nmessage. Value should be between 0 and 600 seconds. Defaults to 10 seconds.\n</pre>\n\n<code>\n.google.protobuf.Duration minimum_backoff = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.foreign.com.google.protobuf/Duration]])

(global/include-schema-registry! (with-meta {:gcp.pubsub.v1/RetryPolicy schema}
                                   {:gcp.global/name
                                      "gcp.pubsub.v1.RetryPolicy"}))