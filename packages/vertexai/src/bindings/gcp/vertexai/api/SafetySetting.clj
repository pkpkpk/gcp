;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.SafetySetting
  {:doc
     "<pre>\nSafety settings.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.SafetySetting}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.SafetySetting"
   :gcp.dev/certification
     {:base-seed 1775465479352
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465479352 :standard 1775465479353 :stress 1775465479354}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:51:20.245798327Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api HarmCategory SafetySetting
            SafetySetting$Builder SafetySetting$HarmBlockMethod
            SafetySetting$HarmBlockThreshold]))

(declare from-edn
         to-edn
         HarmBlockThreshold-from-edn
         HarmBlockThreshold-to-edn
         HarmBlockMethod-from-edn
         HarmBlockMethod-to-edn)

(def HarmBlockThreshold-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nProbability based thresholds levels for blocking.\n</pre>\n\nProtobuf enum {@code google.cloud.vertexai.v1.SafetySetting.HarmBlockThreshold}",
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/SafetySetting.HarmBlockThreshold}
   "HARM_BLOCK_THRESHOLD_UNSPECIFIED" "BLOCK_LOW_AND_ABOVE"
   "BLOCK_MEDIUM_AND_ABOVE" "BLOCK_ONLY_HIGH" "BLOCK_NONE" "OFF"])

(def HarmBlockMethod-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nProbability vs severity.\n</pre>\n\nProtobuf enum {@code google.cloud.vertexai.v1.SafetySetting.HarmBlockMethod}",
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/SafetySetting.HarmBlockMethod}
   "HARM_BLOCK_METHOD_UNSPECIFIED" "SEVERITY" "PROBABILITY"])

(defn ^SafetySetting from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/SafetySetting arg)
  (let [builder (SafetySetting/newBuilder)]
    (when (some? (get arg :category))
      (.setCategory builder (HarmCategory/valueOf (get arg :category))))
    (when (some? (get arg :method))
      (.setMethod builder
                  (SafetySetting$HarmBlockMethod/valueOf (get arg :method))))
    (when (some? (get arg :threshold))
      (.setThreshold builder
                     (SafetySetting$HarmBlockThreshold/valueOf
                       (get arg :threshold))))
    (.build builder)))

(defn to-edn
  [^SafetySetting arg]
  {:post [(global/strict! :gcp.vertexai.api/SafetySetting %)]}
  (when arg
    (cond-> {:category (.name (.getCategory arg)),
             :threshold (.name (.getThreshold arg))}
      (.getMethod arg) (assoc :method (.name (.getMethod arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nSafety settings.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.SafetySetting}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/SafetySetting}
   [:category
    {:getter-doc
       "<pre>\nRequired. Harm category.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.HarmCategory category = 1 [(.google.api.field_behavior) = REQUIRED];\n</code>\n\n@return The category.",
     :setter-doc
       "<pre>\nRequired. Harm category.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.HarmCategory category = 1 [(.google.api.field_behavior) = REQUIRED];\n</code>\n\n@param value The category to set.\n@return This builder for chaining."}
    [:enum {:closed true} "HARM_CATEGORY_UNSPECIFIED"
     "HARM_CATEGORY_HATE_SPEECH" "HARM_CATEGORY_DANGEROUS_CONTENT"
     "HARM_CATEGORY_HARASSMENT" "HARM_CATEGORY_SEXUALLY_EXPLICIT"
     "HARM_CATEGORY_CIVIC_INTEGRITY" "HARM_CATEGORY_JAILBREAK"]]
   [:method
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Specify if the threshold is used for probability or severity\nscore. If not specified, the threshold is used for probability score.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.SafetySetting.HarmBlockMethod method = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The method.",
     :setter-doc
       "<pre>\nOptional. Specify if the threshold is used for probability or severity\nscore. If not specified, the threshold is used for probability score.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.SafetySetting.HarmBlockMethod method = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param value The method to set.\n@return This builder for chaining."}
    [:enum {:closed true} "HARM_BLOCK_METHOD_UNSPECIFIED" "SEVERITY"
     "PROBABILITY"]]
   [:threshold
    {:getter-doc
       "<pre>\nRequired. The harm block threshold.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.SafetySetting.HarmBlockThreshold threshold = 2 [(.google.api.field_behavior) = REQUIRED];\n</code>\n\n@return The threshold.",
     :setter-doc
       "<pre>\nRequired. The harm block threshold.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.SafetySetting.HarmBlockThreshold threshold = 2 [(.google.api.field_behavior) = REQUIRED];\n</code>\n\n@param value The threshold to set.\n@return This builder for chaining."}
    [:enum {:closed true} "HARM_BLOCK_THRESHOLD_UNSPECIFIED"
     "BLOCK_LOW_AND_ABOVE" "BLOCK_MEDIUM_AND_ABOVE" "BLOCK_ONLY_HIGH"
     "BLOCK_NONE" "OFF"]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/SafetySetting schema,
              :gcp.vertexai.api/SafetySetting.HarmBlockMethod
                HarmBlockMethod-schema,
              :gcp.vertexai.api/SafetySetting.HarmBlockThreshold
                HarmBlockThreshold-schema}
    {:gcp.global/name "gcp.vertexai.api.SafetySetting"}))