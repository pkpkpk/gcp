;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.UrlContext
  {:doc
     "<pre>\nTool to support URL context.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.UrlContext}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.UrlContext"
   :gcp.dev/certification
     {:base-seed 1774824584994
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824584994 :standard 1774824584995 :stress 1774824584996}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:49:45.893524514Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api UrlContext UrlContext$Builder]))

(declare from-edn to-edn)

(defn ^UrlContext from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/UrlContext arg)
  (let [builder (UrlContext/newBuilder)] (.build builder)))

(defn to-edn
  [^UrlContext arg]
  {:post [(global/strict! :gcp.vertexai.api/UrlContext %)]}
  (cond-> {}))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nTool to support URL context.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.UrlContext}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/UrlContext}])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/UrlContext schema}
    {:gcp.global/name "gcp.vertexai.api.UrlContext"}))