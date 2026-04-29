;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.PushConfig
  {:doc
     "<pre>\nConfiguration for a push delivery endpoint.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.PushConfig}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.PushConfig"
   :gcp.dev/certification
     {:base-seed 1777403451455
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777403451455 :standard 1777403451456 :stress 1777403451457}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-28T19:10:52.637828773Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.pubsub.v1 PushConfig PushConfig$AuthenticationMethodCase
            PushConfig$Builder PushConfig$NoWrapper PushConfig$NoWrapper$Builder
            PushConfig$OidcToken PushConfig$OidcToken$Builder
            PushConfig$PubsubWrapper PushConfig$PubsubWrapper$Builder
            PushConfig$WrapperCase]))

(declare from-edn
         to-edn
         OidcToken-from-edn
         OidcToken-to-edn
         PubsubWrapper-from-edn
         PubsubWrapper-to-edn
         NoWrapper-from-edn
         NoWrapper-to-edn
         AuthenticationMethodCase-from-edn
         AuthenticationMethodCase-to-edn
         WrapperCase-from-edn
         WrapperCase-to-edn)

(defn ^PushConfig$OidcToken OidcToken-from-edn
  [arg]
  (let [builder (PushConfig$OidcToken/newBuilder)]
    (when (some? (get arg :audience))
      (.setAudience builder (get arg :audience)))
    (when (some? (get arg :serviceAccountEmail))
      (.setServiceAccountEmail builder (get arg :serviceAccountEmail)))
    (.build builder)))

(defn OidcToken-to-edn
  [^PushConfig$OidcToken arg]
  (when arg
    (cond-> {}
      (some->> (.getAudience arg)
               (not= ""))
        (assoc :audience (.getAudience arg))
      (some->> (.getServiceAccountEmail arg)
               (not= ""))
        (assoc :serviceAccountEmail (.getServiceAccountEmail arg)))))

(def OidcToken-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nContains information needed for generating an\n[OpenID Connect\ntoken](https://developers.google.com/identity/protocols/OpenIDConnect).\n</pre>\n\nProtobuf type {@code google.pubsub.v1.PushConfig.OidcToken}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.pubsub.v1/PushConfig.OidcToken}
   [:audience
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Audience to be used when generating OIDC token. The audience\nclaim identifies the recipients that the JWT is intended for. The\naudience value is a single case-sensitive string. Having multiple values\n(array) for the audience field is not supported. More info about the OIDC\nJWT token audience here:\nhttps://tools.ietf.org/html/rfc7519#section-4.1.3 Note: if not specified,\nthe Push endpoint URL will be used.\n</pre>\n\n<code>string audience = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The audience.",
     :setter-doc
       "<pre>\nOptional. Audience to be used when generating OIDC token. The audience\nclaim identifies the recipients that the JWT is intended for. The\naudience value is a single case-sensitive string. Having multiple values\n(array) for the audience field is not supported. More info about the OIDC\nJWT token audience here:\nhttps://tools.ietf.org/html/rfc7519#section-4.1.3 Note: if not specified,\nthe Push endpoint URL will be used.\n</pre>\n\n<code>string audience = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The audience to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:serviceAccountEmail
    {:optional true,
     :getter-doc
       "<pre>\nOptional. [Service account\nemail](https://cloud.google.com/iam/docs/service-accounts)\nused for generating the OIDC token. For more information\non setting up authentication, see\n[Push subscriptions](https://cloud.google.com/pubsub/docs/push).\n</pre>\n\n<code>string service_account_email = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The serviceAccountEmail.",
     :setter-doc
       "<pre>\nOptional. [Service account\nemail](https://cloud.google.com/iam/docs/service-accounts)\nused for generating the OIDC token. For more information\non setting up authentication, see\n[Push subscriptions](https://cloud.google.com/pubsub/docs/push).\n</pre>\n\n<code>string service_account_email = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The serviceAccountEmail to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(defn ^PushConfig$PubsubWrapper PubsubWrapper-from-edn
  [arg]
  (let [builder (PushConfig$PubsubWrapper/newBuilder)] (.build builder)))

(defn PubsubWrapper-to-edn
  [^PushConfig$PubsubWrapper arg]
  (when arg (cond-> {})))

(def PubsubWrapper-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nThe payload to the push endpoint is in the form of the JSON representation\nof a PubsubMessage\n(https://cloud.google.com/pubsub/docs/reference/rpc/google.pubsub.v1#pubsubmessage).\n</pre>\n\nProtobuf type {@code google.pubsub.v1.PushConfig.PubsubWrapper}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.pubsub.v1/PushConfig.PubsubWrapper}])

(defn ^PushConfig$NoWrapper NoWrapper-from-edn
  [arg]
  (let [builder (PushConfig$NoWrapper/newBuilder)]
    (when (some? (get arg :writeMetadata))
      (.setWriteMetadata builder (get arg :writeMetadata)))
    (.build builder)))

(defn NoWrapper-to-edn
  [^PushConfig$NoWrapper arg]
  (when arg
    (cond-> {}
      (.getWriteMetadata arg) (assoc :writeMetadata (.getWriteMetadata arg)))))

(def NoWrapper-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nSets the `data` field as the HTTP body for delivery.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.PushConfig.NoWrapper}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.pubsub.v1/PushConfig.NoWrapper}
   [:writeMetadata
    {:optional true,
     :getter-doc
       "<pre>\nOptional. When true, writes the Pub/Sub message metadata to\n`x-goog-pubsub-&lt;KEY&gt;:&lt;VAL&gt;` headers of the HTTP request. Writes the\nPub/Sub message attributes to `&lt;KEY&gt;:&lt;VAL&gt;` headers of the HTTP request.\n</pre>\n\n<code>bool write_metadata = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The writeMetadata.",
     :setter-doc
       "<pre>\nOptional. When true, writes the Pub/Sub message metadata to\n`x-goog-pubsub-&lt;KEY&gt;:&lt;VAL&gt;` headers of the HTTP request. Writes the\nPub/Sub message attributes to `&lt;KEY&gt;:&lt;VAL&gt;` headers of the HTTP request.\n</pre>\n\n<code>bool write_metadata = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The writeMetadata to set.\n@return This builder for chaining."}
    :boolean]])

(def AuthenticationMethodCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.pubsub.v1/PushConfig.AuthenticationMethodCase} "OIDC_TOKEN"
   "AUTHENTICATIONMETHOD_NOT_SET"])

(def WrapperCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.pubsub.v1/PushConfig.WrapperCase} "PUBSUB_WRAPPER"
   "NO_WRAPPER" "WRAPPER_NOT_SET"])

(defn ^PushConfig from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/PushConfig arg)
  (let [builder (PushConfig/newBuilder)]
    (when (seq (get arg :attributes))
      (.putAllAttributes
        builder
        (into {} (map (fn [[k v]] [(name k) v])) (get arg :putAllAttributes))))
    (when (some? (get arg :pushEndpoint))
      (.setPushEndpoint builder (get arg :pushEndpoint)))
    (cond (contains? arg :oidcToken)
            (.setOidcToken builder (OidcToken-from-edn (get arg :oidcToken))))
    (cond (contains? arg :noWrapper)
            (.setNoWrapper builder (NoWrapper-from-edn (get arg :noWrapper)))
          (contains? arg :pubsubWrapper) (.setPubsubWrapper
                                           builder
                                           (PubsubWrapper-from-edn
                                             (get arg :pubsubWrapper))))
    (.build builder)))

(defn to-edn
  [^PushConfig arg]
  {:post [(global/strict! :gcp.pubsub.v1/PushConfig %)]}
  (when arg
    (let [res (cond-> {}
                (seq (.getAttributesMap arg))
                  (assoc :attributes
                    (into {}
                          (map (fn [[k v]] [(keyword k) v]))
                          (.getAttributesMap arg)))
                (some->> (.getPushEndpoint arg)
                         (not= ""))
                  (assoc :pushEndpoint (.getPushEndpoint arg)))
          res (case (.name (.getAuthenticationMethodCase arg))
                "OIDC_TOKEN"
                  (assoc res :oidcToken (OidcToken-to-edn (.getOidcToken arg)))
                res)
          res (case (.name (.getWrapperCase arg))
                "NO_WRAPPER"
                  (assoc res :noWrapper (NoWrapper-to-edn (.getNoWrapper arg)))
                "PUBSUB_WRAPPER" (assoc res
                                   :pubsubWrapper (PubsubWrapper-to-edn
                                                    (.getPubsubWrapper arg)))
                res)]
      res)))

(def schema
  [:and
   {:closed true,
    :doc
      "<pre>\nConfiguration for a push delivery endpoint.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.PushConfig}",
    :gcp/category :union-protobuf-oneof,
    :gcp/key :gcp.pubsub.v1/PushConfig}
   [:map {:closed true}
    [:attributes
     {:optional true,
      :getter-doc
        "<pre>\nOptional. Endpoint configuration attributes that can be used to control\ndifferent aspects of the message delivery.\n\nThe only currently supported attribute is `x-goog-version`, which you can\nuse to change the format of the pushed message. This attribute\nindicates the version of the data expected by the endpoint. This\ncontrols the shape of the pushed message (i.e., its fields and metadata).\n\nIf not present during the `CreateSubscription` call, it will default to\nthe version of the Pub/Sub API used to make such call. If not present in a\n`ModifyPushConfig` call, its value will not be changed. `GetSubscription`\ncalls will always return a valid version, even if the subscription was\ncreated without this attribute.\n\nThe only supported values for the `x-goog-version` attribute are:\n\n* `v1beta1`: uses the push format defined in the v1beta1 Pub/Sub API.\n* `v1` or `v1beta2`: uses the push format defined in the v1 Pub/Sub API.\n\nFor example:\n`attributes { \"x-goog-version\": \"v1\" }`\n</pre>\n\n<code>map&lt;string, string&gt; attributes = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>",
      :setter-doc
        "<pre>\nOptional. Endpoint configuration attributes that can be used to control\ndifferent aspects of the message delivery.\n\nThe only currently supported attribute is `x-goog-version`, which you can\nuse to change the format of the pushed message. This attribute\nindicates the version of the data expected by the endpoint. This\ncontrols the shape of the pushed message (i.e., its fields and metadata).\n\nIf not present during the `CreateSubscription` call, it will default to\nthe version of the Pub/Sub API used to make such call. If not present in a\n`ModifyPushConfig` call, its value will not be changed. `GetSubscription`\ncalls will always return a valid version, even if the subscription was\ncreated without this attribute.\n\nThe only supported values for the `x-goog-version` attribute are:\n\n* `v1beta1`: uses the push format defined in the v1beta1 Pub/Sub API.\n* `v1` or `v1beta2`: uses the push format defined in the v1 Pub/Sub API.\n\nFor example:\n`attributes { \"x-goog-version\": \"v1\" }`\n</pre>\n\n<code>map&lt;string, string&gt; attributes = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:map-of [:or simple-keyword? [:string {:min 1}]]
      [:string {:min 1, :gen/max 1}]]]
    [:noWrapper
     {:optional true,
      :getter-doc
        "<pre>\nOptional. When set, the payload to the push endpoint is not wrapped.\n</pre>\n\n<code>\n.google.pubsub.v1.PushConfig.NoWrapper no_wrapper = 5 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The noWrapper.",
      :setter-doc
        "<pre>\nOptional. When set, the payload to the push endpoint is not wrapped.\n</pre>\n\n<code>\n.google.pubsub.v1.PushConfig.NoWrapper no_wrapper = 5 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:ref :gcp.pubsub.v1/PushConfig.NoWrapper]]
    [:oidcToken
     {:optional true,
      :getter-doc
        "<pre>\nOptional. If specified, Pub/Sub will generate and attach an OIDC JWT\ntoken as an `Authorization` header in the HTTP request for every pushed\nmessage.\n</pre>\n\n<code>\n.google.pubsub.v1.PushConfig.OidcToken oidc_token = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The oidcToken.",
      :setter-doc
        "<pre>\nOptional. If specified, Pub/Sub will generate and attach an OIDC JWT\ntoken as an `Authorization` header in the HTTP request for every pushed\nmessage.\n</pre>\n\n<code>\n.google.pubsub.v1.PushConfig.OidcToken oidc_token = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:ref :gcp.pubsub.v1/PushConfig.OidcToken]]
    [:pubsubWrapper
     {:optional true,
      :getter-doc
        "<pre>\nOptional. When set, the payload to the push endpoint is in the form of\nthe JSON representation of a PubsubMessage\n(https://cloud.google.com/pubsub/docs/reference/rpc/google.pubsub.v1#pubsubmessage).\n</pre>\n\n<code>\n.google.pubsub.v1.PushConfig.PubsubWrapper pubsub_wrapper = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The pubsubWrapper.",
      :setter-doc
        "<pre>\nOptional. When set, the payload to the push endpoint is in the form of\nthe JSON representation of a PubsubMessage\n(https://cloud.google.com/pubsub/docs/reference/rpc/google.pubsub.v1#pubsubmessage).\n</pre>\n\n<code>\n.google.pubsub.v1.PushConfig.PubsubWrapper pubsub_wrapper = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:ref :gcp.pubsub.v1/PushConfig.PubsubWrapper]]
    [:pushEndpoint
     {:optional true,
      :getter-doc
        "<pre>\nOptional. A URL locating the endpoint to which messages should be pushed.\nFor example, a Webhook endpoint might use `https://example.com/push`.\n</pre>\n\n<code>string push_endpoint = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The pushEndpoint.",
      :setter-doc
        "<pre>\nOptional. A URL locating the endpoint to which messages should be pushed.\nFor example, a Webhook endpoint might use `https://example.com/push`.\n</pre>\n\n<code>string push_endpoint = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The pushEndpoint to set.\n@return This builder for chaining."}
     [:string {:min 1, :gen/max 1}]]]
   [:fn {:error/message "Only one of these keys may be present: #{:oidcToken}"}
    (quote (fn [m] (<= (count (filter (set (keys m)) #{:oidcToken})) 1)))]
   [:fn
    {:error/message
       "Only one of these keys may be present: #{:noWrapper :pubsubWrapper}"}
    (quote (fn [m]
             (<= (count (filter (set (keys m)) #{:noWrapper :pubsubWrapper}))
                 1)))]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/PushConfig schema,
              :gcp.pubsub.v1/PushConfig.AuthenticationMethodCase
                AuthenticationMethodCase-schema,
              :gcp.pubsub.v1/PushConfig.NoWrapper NoWrapper-schema,
              :gcp.pubsub.v1/PushConfig.OidcToken OidcToken-schema,
              :gcp.pubsub.v1/PushConfig.PubsubWrapper PubsubWrapper-schema,
              :gcp.pubsub.v1/PushConfig.WrapperCase WrapperCase-schema}
    {:gcp.global/name "gcp.pubsub.v1.PushConfig"}))