;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.GoogleMaps
  {:doc
     "<pre>\nTool to retrieve public maps data for grounding, powered by Google.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GoogleMaps}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.GoogleMaps"
   :gcp.dev/certification
     {:base-seed 1774824664751
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824664751 :standard 1774824664752 :stress 1774824664753}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:51:05.725720035Z"}}
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
  (cond-> {}
    (.getEnableWidget arg) (assoc :enableWidget (.getEnableWidget arg))))

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