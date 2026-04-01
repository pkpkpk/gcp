;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.SafetyRating
  {:doc
     "<pre>\nSafety rating corresponding to the generated content.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.SafetyRating}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.SafetyRating"
   :gcp.dev/certification
     {:base-seed 1774824774648
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824774648 :standard 1774824774649 :stress 1774824774650}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:52:55.786486161Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api HarmCategory SafetyRating
            SafetyRating$Builder SafetyRating$HarmProbability
            SafetyRating$HarmSeverity]))

(declare from-edn
         to-edn
         HarmProbability-from-edn
         HarmProbability-to-edn
         HarmSeverity-from-edn
         HarmSeverity-to-edn)

(def HarmProbability-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nHarm probability levels in the content.\n</pre>\n\nProtobuf enum {@code google.cloud.vertexai.v1.SafetyRating.HarmProbability}",
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/SafetyRating.HarmProbability}
   "HARM_PROBABILITY_UNSPECIFIED" "NEGLIGIBLE" "LOW" "MEDIUM" "HIGH"])

(def HarmSeverity-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nHarm severity levels.\n</pre>\n\nProtobuf enum {@code google.cloud.vertexai.v1.SafetyRating.HarmSeverity}",
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/SafetyRating.HarmSeverity}
   "HARM_SEVERITY_UNSPECIFIED" "HARM_SEVERITY_NEGLIGIBLE" "HARM_SEVERITY_LOW"
   "HARM_SEVERITY_MEDIUM" "HARM_SEVERITY_HIGH"])

(defn ^SafetyRating from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/SafetyRating arg)
  (let [builder (SafetyRating/newBuilder)]
    (when (some? (get arg :blocked)) (.setBlocked builder (get arg :blocked)))
    (when (some? (get arg :category))
      (.setCategory builder (HarmCategory/valueOf (get arg :category))))
    (when (some? (get arg :probability))
      (.setProbability builder
                       (SafetyRating$HarmProbability/valueOf
                         (get arg :probability))))
    (when (some? (get arg :probabilityScore))
      (.setProbabilityScore builder (float (get arg :probabilityScore))))
    (when (some? (get arg :severity))
      (.setSeverity builder
                    (SafetyRating$HarmSeverity/valueOf (get arg :severity))))
    (when (some? (get arg :severityScore))
      (.setSeverityScore builder (float (get arg :severityScore))))
    (.build builder)))

(defn to-edn
  [^SafetyRating arg]
  {:post [(global/strict! :gcp.vertexai.api/SafetyRating %)]}
  (cond-> {}
    (.getBlocked arg) (assoc :blocked (.getBlocked arg))
    (.getCategory arg) (assoc :category (.name (.getCategory arg)))
    (.getProbability arg) (assoc :probability (.name (.getProbability arg)))
    (.getProbabilityScore arg) (assoc :probabilityScore
                                 (.getProbabilityScore arg))
    (.getSeverity arg) (assoc :severity (.name (.getSeverity arg)))
    (.getSeverityScore arg) (assoc :severityScore (.getSeverityScore arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nSafety rating corresponding to the generated content.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.SafetyRating}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/SafetyRating}
   [:blocked
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Indicates whether the content was filtered out because of this\nrating.\n</pre>\n\n<code>bool blocked = 3 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The blocked."}
    :boolean]
   [:category
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Harm category.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.HarmCategory category = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The category."}
    [:enum {:closed true} "HARM_CATEGORY_UNSPECIFIED"
     "HARM_CATEGORY_HATE_SPEECH" "HARM_CATEGORY_DANGEROUS_CONTENT"
     "HARM_CATEGORY_HARASSMENT" "HARM_CATEGORY_SEXUALLY_EXPLICIT"
     "HARM_CATEGORY_CIVIC_INTEGRITY" "HARM_CATEGORY_JAILBREAK"]]
   [:probability
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Harm probability levels in the content.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.SafetyRating.HarmProbability probability = 2 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The probability."}
    [:enum {:closed true} "HARM_PROBABILITY_UNSPECIFIED" "NEGLIGIBLE" "LOW"
     "MEDIUM" "HIGH"]]
   [:probabilityScore
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Harm probability score.\n</pre>\n\n<code>float probability_score = 5 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The probabilityScore."}
    :f32]
   [:severity
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Harm severity levels in the content.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.SafetyRating.HarmSeverity severity = 6 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The severity."}
    [:enum {:closed true} "HARM_SEVERITY_UNSPECIFIED" "HARM_SEVERITY_NEGLIGIBLE"
     "HARM_SEVERITY_LOW" "HARM_SEVERITY_MEDIUM" "HARM_SEVERITY_HIGH"]]
   [:severityScore
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Harm severity score.\n</pre>\n\n<code>float severity_score = 7 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The severityScore."}
    :f32]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/SafetyRating schema,
              :gcp.vertexai.api/SafetyRating.HarmProbability
                HarmProbability-schema,
              :gcp.vertexai.api/SafetyRating.HarmSeverity HarmSeverity-schema}
    {:gcp.global/name "gcp.vertexai.api.SafetyRating"}))