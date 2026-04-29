;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.MessageTransform
  {:doc
     "<pre>\nAll supported message transforms types.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.MessageTransform}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.MessageTransform"
   :gcp.dev/certification
     {:base-seed 1777403449641
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777403449641 :standard 1777403449642 :stress 1777403449643}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-28T19:10:50.861116117Z"}}
  (:require [gcp.global :as global]
            [gcp.pubsub.v1.AIInference :as AIInference]
            [gcp.pubsub.v1.JavaScriptUDF :as JavaScriptUDF])
  (:import [com.google.pubsub.v1 MessageTransform MessageTransform$Builder
            MessageTransform$TransformCase]))

(declare from-edn to-edn TransformCase-from-edn TransformCase-to-edn)

(def TransformCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.pubsub.v1/MessageTransform.TransformCase} "JAVASCRIPT_UDF"
   "AI_INFERENCE" "TRANSFORM_NOT_SET"])

(defn ^MessageTransform from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/MessageTransform arg)
  (let [builder (MessageTransform/newBuilder)]
    (when (some? (get arg :disabled))
      (.setDisabled builder (get arg :disabled)))
    (cond (contains? arg :aiInference)
            (.setAiInference builder
                             (AIInference/from-edn (get arg :aiInference)))
          (contains? arg :javascriptUdf) (.setJavascriptUdf
                                           builder
                                           (JavaScriptUDF/from-edn
                                             (get arg :javascriptUdf))))
    (.build builder)))

(defn to-edn
  [^MessageTransform arg]
  {:post [(global/strict! :gcp.pubsub.v1/MessageTransform %)]}
  (when arg
    (let [res (cond-> {}
                (.getDisabled arg) (assoc :disabled (.getDisabled arg)))
          res (case (.name (.getTransformCase arg))
                "AI_INFERENCE" (assoc res
                                 :aiInference (AIInference/to-edn
                                                (.getAiInference arg)))
                "JAVASCRIPT_UDF" (assoc res
                                   :javascriptUdf (JavaScriptUDF/to-edn
                                                    (.getJavascriptUdf arg)))
                res)]
      res)))

(def schema
  [:and
   {:closed true,
    :doc
      "<pre>\nAll supported message transforms types.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.MessageTransform}",
    :gcp/category :union-protobuf-oneof,
    :gcp/key :gcp.pubsub.v1/MessageTransform}
   [:map {:closed true}
    [:aiInference
     {:optional true,
      :getter-doc
        "<pre>\nOptional. AI Inference. Specifies the Vertex AI endpoint that inference\nrequests built from the Pub/Sub message data and provided parameters will\nbe sent to.\n</pre>\n\n<code>.google.pubsub.v1.AIInference ai_inference = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The aiInference.",
      :setter-doc
        "<pre>\nOptional. AI Inference. Specifies the Vertex AI endpoint that inference\nrequests built from the Pub/Sub message data and provided parameters will\nbe sent to.\n</pre>\n\n<code>\n.google.pubsub.v1.AIInference ai_inference = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     :gcp.pubsub.v1/AIInference]
    [:disabled
     {:optional true,
      :getter-doc
        "<pre>\nOptional. If true, the transform is disabled and will not be applied to\nmessages. Defaults to `false`.\n</pre>\n\n<code>bool disabled = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The disabled.",
      :setter-doc
        "<pre>\nOptional. If true, the transform is disabled and will not be applied to\nmessages. Defaults to `false`.\n</pre>\n\n<code>bool disabled = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The disabled to set.\n@return This builder for chaining."}
     :boolean]
    [:javascriptUdf
     {:optional true,
      :getter-doc
        "<pre>\nOptional. JavaScript User Defined Function. If multiple JavaScriptUDF's\nare specified on a resource, each must have a unique `function_name`.\n</pre>\n\n<code>\n.google.pubsub.v1.JavaScriptUDF javascript_udf = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The javascriptUdf.",
      :setter-doc
        "<pre>\nOptional. JavaScript User Defined Function. If multiple JavaScriptUDF's\nare specified on a resource, each must have a unique `function_name`.\n</pre>\n\n<code>\n.google.pubsub.v1.JavaScriptUDF javascript_udf = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     :gcp.pubsub.v1/JavaScriptUDF]]
   [:fn
    {:error/message
       "Only one of these keys may be present: #{:javascriptUdf :aiInference}"}
    (quote (fn [m]
             (<= (count (filter (set (keys m)) #{:javascriptUdf :aiInference}))
                 1)))]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/MessageTransform schema,
              :gcp.pubsub.v1/MessageTransform.TransformCase
                TransformCase-schema}
    {:gcp.global/name "gcp.pubsub.v1.MessageTransform"}))