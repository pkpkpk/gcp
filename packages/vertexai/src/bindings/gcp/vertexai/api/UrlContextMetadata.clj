;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.UrlContextMetadata
  {:doc
     "<pre>\nMetadata related to url context retrieval tool.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.UrlContextMetadata}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.UrlContextMetadata"
   :gcp.dev/certification
     {:base-seed 1775465658912
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465658912 :standard 1775465658913 :stress 1775465658914}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:54:20.084949173Z"}}
  (:require [gcp.global :as global]
            [gcp.vertexai.api.UrlMetadata :as UrlMetadata])
  (:import [com.google.cloud.vertexai.api UrlContextMetadata
            UrlContextMetadata$Builder]))

(declare from-edn to-edn)

(defn ^UrlContextMetadata from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/UrlContextMetadata arg)
  (let [builder (UrlContextMetadata/newBuilder)]
    (when (seq (get arg :urlMetadata))
      (.addAllUrlMetadata builder
                          (map UrlMetadata/from-edn (get arg :urlMetadata))))
    (.build builder)))

(defn to-edn
  [^UrlContextMetadata arg]
  {:post [(global/strict! :gcp.vertexai.api/UrlContextMetadata %)]}
  (when arg
    (cond-> {}
      (seq (.getUrlMetadataList arg)) (assoc :urlMetadata
                                        (map UrlMetadata/to-edn
                                          (.getUrlMetadataList arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nMetadata related to url context retrieval tool.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.UrlContextMetadata}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/UrlContextMetadata}
   [:urlMetadata
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. List of url context.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.UrlMetadata url_metadata = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>"}
    [:sequential {:min 1} :gcp.vertexai.api/UrlMetadata]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/UrlContextMetadata schema}
    {:gcp.global/name "gcp.vertexai.api.UrlContextMetadata"}))