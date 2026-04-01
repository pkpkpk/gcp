;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.SearchEntryPoint
  {:doc
     "<pre>\nGoogle search entry point.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.SearchEntryPoint}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.SearchEntryPoint"
   :gcp.dev/certification
     {:base-seed 1774824742781
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824742781 :standard 1774824742782 :stress 1774824742783}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:52:23.790692660Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api SearchEntryPoint
            SearchEntryPoint$Builder]))

(declare from-edn to-edn)

(defn ^SearchEntryPoint from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/SearchEntryPoint arg)
  (let [builder (SearchEntryPoint/newBuilder)]
    (when (some? (get arg :renderedContent))
      (.setRenderedContent builder (get arg :renderedContent)))
    (.build builder)))

(defn to-edn
  [^SearchEntryPoint arg]
  {:post [(global/strict! :gcp.vertexai.api/SearchEntryPoint %)]}
  (cond-> {}
    (some->> (.getRenderedContent arg)
             (not= ""))
      (assoc :renderedContent (.getRenderedContent arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nGoogle search entry point.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.SearchEntryPoint}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/SearchEntryPoint}
   [:renderedContent
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Web content snippet that can be embedded in a web page or an app\nwebview.\n</pre>\n\n<code>string rendered_content = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The renderedContent.",
     :setter-doc
       "<pre>\nOptional. Web content snippet that can be embedded in a web page or an app\nwebview.\n</pre>\n\n<code>string rendered_content = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The renderedContent to set.\n@return This builder for chaining."}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/SearchEntryPoint schema}
    {:gcp.global/name "gcp.vertexai.api.SearchEntryPoint"}))