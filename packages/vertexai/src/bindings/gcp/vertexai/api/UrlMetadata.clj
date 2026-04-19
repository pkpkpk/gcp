;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.UrlMetadata
  {:doc
     "<pre>\nContext of the a single url retrieval.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.UrlMetadata}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.UrlMetadata"
   :gcp.dev/certification
     {:base-seed 1776627409914
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627409914 :standard 1776627409915 :stress 1776627409916}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:36:50.752885318Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api UrlMetadata UrlMetadata$Builder
            UrlMetadata$UrlRetrievalStatus]))

(declare from-edn to-edn UrlRetrievalStatus-from-edn UrlRetrievalStatus-to-edn)

(def UrlRetrievalStatus-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nStatus of the url retrieval.\n</pre>\n\nProtobuf enum {@code google.cloud.vertexai.v1.UrlMetadata.UrlRetrievalStatus}",
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/UrlMetadata.UrlRetrievalStatus}
   "URL_RETRIEVAL_STATUS_UNSPECIFIED" "URL_RETRIEVAL_STATUS_SUCCESS"
   "URL_RETRIEVAL_STATUS_ERROR"])

(defn ^UrlMetadata from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/UrlMetadata arg)
  (let [builder (UrlMetadata/newBuilder)]
    (when (some? (get arg :retrievedUrl))
      (.setRetrievedUrl builder (get arg :retrievedUrl)))
    (when (some? (get arg :urlRetrievalStatus))
      (.setUrlRetrievalStatus builder
                              (UrlMetadata$UrlRetrievalStatus/valueOf
                                (get arg :urlRetrievalStatus))))
    (.build builder)))

(defn to-edn
  [^UrlMetadata arg]
  {:post [(global/strict! :gcp.vertexai.api/UrlMetadata %)]}
  (when arg
    (cond-> {}
      (some->> (.getRetrievedUrl arg)
               (not= ""))
        (assoc :retrievedUrl (.getRetrievedUrl arg))
      (.getUrlRetrievalStatus arg) (assoc :urlRetrievalStatus
                                     (.name (.getUrlRetrievalStatus arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nContext of the a single url retrieval.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.UrlMetadata}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/UrlMetadata}
   [:retrievedUrl
    {:optional true,
     :getter-doc
       "<pre>\nRetrieved url by the tool.\n</pre>\n\n<code>string retrieved_url = 1;</code>\n\n@return The retrievedUrl.",
     :setter-doc
       "<pre>\nRetrieved url by the tool.\n</pre>\n\n<code>string retrieved_url = 1;</code>\n\n@param value The retrievedUrl to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:urlRetrievalStatus
    {:optional true,
     :getter-doc
       "<pre>\nStatus of the url retrieval.\n</pre>\n\n<code>.google.cloud.vertexai.v1.UrlMetadata.UrlRetrievalStatus url_retrieval_status = 2;</code>\n\n@return The urlRetrievalStatus.",
     :setter-doc
       "<pre>\nStatus of the url retrieval.\n</pre>\n\n<code>.google.cloud.vertexai.v1.UrlMetadata.UrlRetrievalStatus url_retrieval_status = 2;\n</code>\n\n@param value The urlRetrievalStatus to set.\n@return This builder for chaining."}
    [:enum {:closed true} "URL_RETRIEVAL_STATUS_UNSPECIFIED"
     "URL_RETRIEVAL_STATUS_SUCCESS" "URL_RETRIEVAL_STATUS_ERROR"]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/UrlMetadata schema,
              :gcp.vertexai.api/UrlMetadata.UrlRetrievalStatus
                UrlRetrievalStatus-schema}
    {:gcp.global/name "gcp.vertexai.api.UrlMetadata"}))