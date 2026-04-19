;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.GoogleMaps
  {:doc
     "<pre>\nTool to retrieve public maps data for grounding, powered by Google.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GoogleMaps}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.GoogleMaps"
   :gcp.dev/certification
     {:base-seed 1776627489211
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627489211 :standard 1776627489212 :stress 1776627489213}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:38:10.079640296Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api GoogleMaps GoogleMaps$Builder]))

(declare from-edn to-edn)

(defn ^GoogleMaps from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/GoogleMaps arg)
  (let [builder (GoogleMaps/newBuilder)]
    (when (some? (get arg :enableWidget))
      (.setEnableWidget builder (get arg :enableWidget)))
    (.build builder)))

(defn to-edn
  [^GoogleMaps arg]
  {:post [(global/strict! :gcp.vertexai.api/GoogleMaps %)]}
  (when arg
    (cond-> {}
      (.getEnableWidget arg) (assoc :enableWidget (.getEnableWidget arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nTool to retrieve public maps data for grounding, powered by Google.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GoogleMaps}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/GoogleMaps}
   [:enableWidget
    {:optional true,
     :getter-doc
       "<pre>\nIf true, include the widget context token in the response.\n</pre>\n\n<code>bool enable_widget = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The enableWidget.",
     :setter-doc
       "<pre>\nIf true, include the widget context token in the response.\n</pre>\n\n<code>bool enable_widget = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The enableWidget to set.\n@return This builder for chaining."}
    :boolean]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/GoogleMaps schema}
    {:gcp.global/name "gcp.vertexai.api.GoogleMaps"}))