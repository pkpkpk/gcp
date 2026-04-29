;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.ExpirationPolicy
  {:doc
     "<pre>\nA policy that specifies the conditions for resource expiration (i.e.,\nautomatic resource deletion).\n</pre>\n\nProtobuf type {@code google.pubsub.v1.ExpirationPolicy}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.ExpirationPolicy"
   :gcp.dev/certification
     {:base-seed 1777403445238
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777403445238 :standard 1777403445239 :stress 1777403445240}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-28T19:10:46.198144273Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global])
  (:import [com.google.protobuf Duration]
           [com.google.pubsub.v1 ExpirationPolicy ExpirationPolicy$Builder]))

(declare from-edn to-edn)

(defn ^ExpirationPolicy from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/ExpirationPolicy arg)
  (let [builder (ExpirationPolicy/newBuilder)]
    (when (some? (get arg :ttl))
      (.setTtl builder (protobuf/Duration-from-edn (get arg :ttl))))
    (.build builder)))

(defn to-edn
  [^ExpirationPolicy arg]
  {:post [(global/strict! :gcp.pubsub.v1/ExpirationPolicy %)]}
  (when arg
    (cond-> {}
      (.hasTtl arg) (assoc :ttl (protobuf/Duration-to-edn (.getTtl arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nA policy that specifies the conditions for resource expiration (i.e.,\nautomatic resource deletion).\n</pre>\n\nProtobuf type {@code google.pubsub.v1.ExpirationPolicy}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.pubsub.v1/ExpirationPolicy}
   [:ttl
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Specifies the \"time-to-live\" duration for an associated resource.\nThe resource expires if it is not active for a period of `ttl`. The\ndefinition of \"activity\" depends on the type of the associated resource.\nThe minimum and maximum allowed values for `ttl` depend on the type of the\nassociated resource, as well. If `ttl` is not set, the associated resource\nnever expires.\n</pre>\n\n<code>.google.protobuf.Duration ttl = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The ttl.",
     :setter-doc
       "<pre>\nOptional. Specifies the \"time-to-live\" duration for an associated resource.\nThe resource expires if it is not active for a period of `ttl`. The\ndefinition of \"activity\" depends on the type of the associated resource.\nThe minimum and maximum allowed values for `ttl` depend on the type of the\nassociated resource, as well. If `ttl` is not set, the associated resource\nnever expires.\n</pre>\n\n<code>.google.protobuf.Duration ttl = 1 [(.google.api.field_behavior) = OPTIONAL];</code>"}
    :gcp.foreign.com.google.protobuf/Duration]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/ExpirationPolicy schema}
    {:gcp.global/name "gcp.pubsub.v1.ExpirationPolicy"}))