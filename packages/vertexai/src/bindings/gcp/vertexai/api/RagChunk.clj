;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.RagChunk
  {:doc
     "<pre>\nA RagChunk includes the content of a chunk of a RagFile, and associated\nmetadata.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.RagChunk}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.RagChunk"
   :gcp.dev/certification
     {:base-seed 1776627513604
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627513604 :standard 1776627513605 :stress 1776627513606}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:38:34.508148022Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api RagChunk RagChunk$Builder
            RagChunk$PageSpan RagChunk$PageSpan$Builder]))

(declare from-edn to-edn PageSpan-from-edn PageSpan-to-edn)

(defn ^RagChunk$PageSpan PageSpan-from-edn
  [arg]
  (let [builder (RagChunk$PageSpan/newBuilder)]
    (when (some? (get arg :firstPage))
      (.setFirstPage builder (int (get arg :firstPage))))
    (when (some? (get arg :lastPage))
      (.setLastPage builder (int (get arg :lastPage))))
    (.build builder)))

(defn PageSpan-to-edn
  [^RagChunk$PageSpan arg]
  (when arg
    (cond-> {}
      (.getFirstPage arg) (assoc :firstPage (.getFirstPage arg))
      (.getLastPage arg) (assoc :lastPage (.getLastPage arg)))))

(def PageSpan-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nRepresents where the chunk starts and ends in the document.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.RagChunk.PageSpan}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/RagChunk.PageSpan}
   [:firstPage
    {:optional true,
     :getter-doc
       "<pre>\nPage where chunk starts in the document. Inclusive. 1-indexed.\n</pre>\n\n<code>int32 first_page = 1;</code>\n\n@return The firstPage.",
     :setter-doc
       "<pre>\nPage where chunk starts in the document. Inclusive. 1-indexed.\n</pre>\n\n<code>int32 first_page = 1;</code>\n\n@param value The firstPage to set.\n@return This builder for chaining."}
    :i32]
   [:lastPage
    {:optional true,
     :getter-doc
       "<pre>\nPage where chunk ends in the document. Inclusive. 1-indexed.\n</pre>\n\n<code>int32 last_page = 2;</code>\n\n@return The lastPage.",
     :setter-doc
       "<pre>\nPage where chunk ends in the document. Inclusive. 1-indexed.\n</pre>\n\n<code>int32 last_page = 2;</code>\n\n@param value The lastPage to set.\n@return This builder for chaining."}
    :i32]])

(defn ^RagChunk from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/RagChunk arg)
  (let [builder (RagChunk/newBuilder)]
    (when (some? (get arg :pageSpan))
      (.setPageSpan builder (PageSpan-from-edn (get arg :pageSpan))))
    (when (some? (get arg :text)) (.setText builder (get arg :text)))
    (.build builder)))

(defn to-edn
  [^RagChunk arg]
  {:post [(global/strict! :gcp.vertexai.api/RagChunk %)]}
  (when arg
    (cond-> {}
      (.hasPageSpan arg) (assoc :pageSpan (PageSpan-to-edn (.getPageSpan arg)))
      (some->> (.getText arg)
               (not= ""))
        (assoc :text (.getText arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nA RagChunk includes the content of a chunk of a RagFile, and associated\nmetadata.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.RagChunk}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/RagChunk}
   [:pageSpan
    {:optional true,
     :getter-doc
       "<pre>\nIf populated, represents where the chunk starts and ends in the document.\n</pre>\n\n<code>optional .google.cloud.vertexai.v1.RagChunk.PageSpan page_span = 2;</code>\n\n@return The pageSpan.",
     :setter-doc
       "<pre>\nIf populated, represents where the chunk starts and ends in the document.\n</pre>\n\n<code>optional .google.cloud.vertexai.v1.RagChunk.PageSpan page_span = 2;</code>"}
    [:ref :gcp.vertexai.api/RagChunk.PageSpan]]
   [:text
    {:optional true,
     :getter-doc
       "<pre>\nThe content of the chunk.\n</pre>\n\n<code>string text = 1;</code>\n\n@return The text.",
     :setter-doc
       "<pre>\nThe content of the chunk.\n</pre>\n\n<code>string text = 1;</code>\n\n@param value The text to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/RagChunk schema,
              :gcp.vertexai.api/RagChunk.PageSpan PageSpan-schema}
    {:gcp.global/name "gcp.vertexai.api.RagChunk"}))