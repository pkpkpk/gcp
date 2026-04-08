;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.Content
  {:doc
     "<pre>\nThe base structured datatype containing multi-part content of a message.\n\nA `Content` includes a `role` field designating the producer of the `Content`\nand a `parts` field containing multi-part data that contains the content of\nthe message turn.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Content}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.Content"
   :gcp.dev/certification
     {:base-seed 1775465708894
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465708894 :standard 1775465708895 :stress 1775465708896}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:55:12.105837733Z"}}
  (:require [gcp.global :as global]
            [gcp.vertexai.api.Part :as Part])
  (:import [com.google.cloud.vertexai.api Content Content$Builder]))

(declare from-edn to-edn)

(defn ^Content from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/Content arg)
  (let [builder (Content/newBuilder)]
    (when (seq (get arg :parts))
      (.addAllParts builder (map Part/from-edn (get arg :parts))))
    (when (some? (get arg :role)) (.setRole builder (get arg :role)))
    (.build builder)))

(defn to-edn
  [^Content arg]
  {:post [(global/strict! :gcp.vertexai.api/Content %)]}
  (when arg
    (cond-> {:parts (map Part/to-edn (.getPartsList arg))}
      (some->> (.getRole arg)
               (not= ""))
        (assoc :role (.getRole arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nThe base structured datatype containing multi-part content of a message.\n\nA `Content` includes a `role` field designating the producer of the `Content`\nand a `parts` field containing multi-part data that contains the content of\nthe message turn.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Content}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/Content}
   [:parts
    {:getter-doc
       "<pre>\nRequired. Ordered `Parts` that constitute a single message. Parts may have\ndifferent IANA MIME types.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.Part parts = 2 [(.google.api.field_behavior) = REQUIRED];\n</code>",
     :setter-doc
       "<pre>\nRequired. Ordered `Parts` that constitute a single message. Parts may have\ndifferent IANA MIME types.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.Part parts = 2 [(.google.api.field_behavior) = REQUIRED];\n</code>"}
    [:sequential {:min 1} :gcp.vertexai.api/Part]]
   [:role
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The producer of the content. Must be either 'user' or 'model'.\n\nUseful to set for multi-turn conversations, otherwise can be left blank\nor unset.\n</pre>\n\n<code>string role = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The role.",
     :setter-doc
       "<pre>\nOptional. The producer of the content. Must be either 'user' or 'model'.\n\nUseful to set for multi-turn conversations, otherwise can be left blank\nor unset.\n</pre>\n\n<code>string role = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The role to set.\n@return This builder for chaining."}
    [:string {:min 1}]]])

(global/include-schema-registry! (with-meta {:gcp.vertexai.api/Content schema}
                                   {:gcp.global/name
                                      "gcp.vertexai.api.Content"}))