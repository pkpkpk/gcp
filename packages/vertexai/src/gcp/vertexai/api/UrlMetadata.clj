;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.UrlMetadata
  {:doc
     "<pre>\nContext of the a single url retrieval.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.UrlMetadata}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.UrlMetadata"
   :gcp.dev/certification
     {:base-seed 1774824747088
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824747088 :standard 1774824747089 :stress 1774824747090}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:52:28.130599613Z"}}
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
  (cond-> {}
    (some->> (.getRetrievedUrl arg)
             (not= ""))
      (assoc :retrievedUrl (.getRetrievedUrl arg))
    (.getUrlRetrievalStatus arg) (assoc :urlRetrievalStatus
                                   (.name (.getUrlRetrievalStatus arg)))))

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
    [:string {:min 1}]]
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