;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.UrlContextMetadata
  {:doc
     "<pre>\nMetadata related to url context retrieval tool.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.UrlContextMetadata}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.UrlContextMetadata"
   :gcp.dev/certification
     {:base-seed 1776627411833
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627411833 :standard 1776627411834 :stress 1776627411835}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:36:52.711838833Z"}}
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
                          (mapv UrlMetadata/from-edn (get arg :urlMetadata))))
    (.build builder)))

(defn to-edn
  [^UrlContextMetadata arg]
  {:post [(global/strict! :gcp.vertexai.api/UrlContextMetadata %)]}
  (when arg
    (cond-> {}
      (seq (.getUrlMetadataList arg)) (assoc :urlMetadata
                                        (mapv UrlMetadata/to-edn
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
    [:sequential {:min 1, :gen/max 2} :gcp.vertexai.api/UrlMetadata]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/UrlContextMetadata schema}
    {:gcp.global/name "gcp.vertexai.api.UrlContextMetadata"}))