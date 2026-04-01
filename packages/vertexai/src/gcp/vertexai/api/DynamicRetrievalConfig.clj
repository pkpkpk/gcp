;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.DynamicRetrievalConfig
  {:doc
     "<pre>\nDescribes the options to customize dynamic retrieval.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.DynamicRetrievalConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.DynamicRetrievalConfig"
   :gcp.dev/certification
     {:base-seed 1774824666707
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824666707 :standard 1774824666708 :stress 1774824666709}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:51:07.791310733Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api DynamicRetrievalConfig
            DynamicRetrievalConfig$Builder DynamicRetrievalConfig$Mode]))

(declare from-edn to-edn Mode-from-edn Mode-to-edn)

(def Mode-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nThe mode of the predictor to be used in dynamic retrieval.\n</pre>\n\nProtobuf enum {@code google.cloud.vertexai.v1.DynamicRetrievalConfig.Mode}",
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/DynamicRetrievalConfig.Mode} "MODE_UNSPECIFIED"
   "MODE_DYNAMIC"])

(defn ^DynamicRetrievalConfig from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/DynamicRetrievalConfig arg)
  (let [builder (DynamicRetrievalConfig/newBuilder)]
    (when (some? (get arg :dynamicThreshold))
      (.setDynamicThreshold builder (float (get arg :dynamicThreshold))))
    (when (some? (get arg :mode))
      (.setMode builder (DynamicRetrievalConfig$Mode/valueOf (get arg :mode))))
    (.build builder)))

(defn to-edn
  [^DynamicRetrievalConfig arg]
  {:post [(global/strict! :gcp.vertexai.api/DynamicRetrievalConfig %)]}
  (cond-> {}
    (.hasDynamicThreshold arg) (assoc :dynamicThreshold
                                 (.getDynamicThreshold arg))
    (.getMode arg) (assoc :mode (.name (.getMode arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nDescribes the options to customize dynamic retrieval.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.DynamicRetrievalConfig}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/DynamicRetrievalConfig}
   [:dynamicThreshold
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The threshold to be used in dynamic retrieval.\nIf not set, a system default value is used.\n</pre>\n\n<code>optional float dynamic_threshold = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The dynamicThreshold.",
     :setter-doc
       "<pre>\nOptional. The threshold to be used in dynamic retrieval.\nIf not set, a system default value is used.\n</pre>\n\n<code>optional float dynamic_threshold = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The dynamicThreshold to set.\n@return This builder for chaining."}
    :f32]
   [:mode
    {:optional true,
     :getter-doc
       "<pre>\nThe mode of the predictor to be used in dynamic retrieval.\n</pre>\n\n<code>.google.cloud.vertexai.v1.DynamicRetrievalConfig.Mode mode = 1;</code>\n\n@return The mode.",
     :setter-doc
       "<pre>\nThe mode of the predictor to be used in dynamic retrieval.\n</pre>\n\n<code>.google.cloud.vertexai.v1.DynamicRetrievalConfig.Mode mode = 1;</code>\n\n@param value The mode to set.\n@return This builder for chaining."}
    [:enum {:closed true} "MODE_UNSPECIFIED" "MODE_DYNAMIC"]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/DynamicRetrievalConfig schema,
              :gcp.vertexai.api/DynamicRetrievalConfig.Mode Mode-schema}
    {:gcp.global/name "gcp.vertexai.api.DynamicRetrievalConfig"}))