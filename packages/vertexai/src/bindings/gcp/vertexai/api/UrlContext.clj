;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.UrlContext
  {:doc
     "<pre>\nTool to support URL context.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.UrlContext}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.UrlContext"
   :gcp.dev/certification
     {:base-seed 1776627408038
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627408038 :standard 1776627408039 :stress 1776627408040}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:36:48.893854495Z"}}
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
  (when arg (cond-> {})))

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