;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.GoogleMaps
  {:doc
     "<pre>\nTool to retrieve public maps data for grounding, powered by Google.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GoogleMaps}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.GoogleMaps"
   :gcp.dev/certification
     {:base-seed 1775465562668
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465562668 :standard 1775465562669 :stress 1775465562670}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:52:43.612620651Z"}}
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