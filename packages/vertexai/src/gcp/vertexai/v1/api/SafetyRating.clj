(ns gcp.vertexai.v1.api.SafetyRating
  (:require [gcp.vertexai.v1.api.HarmCategory :as HarmCategory]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api SafetyRating
                                          SafetyRating$HarmSeverity
                                          SafetyRating$HarmProbability]))

(defn HarmProbability-from-edn [arg]
  (if (number? arg)
    (SafetyRating$HarmProbability/forNumber (int arg))
    (SafetyRating$HarmProbability/valueOf ^String arg)))

(defn HarmProbability-to-edn [arg]
  (if (int? arg)
    (.name (SafetyRating$HarmProbability/forNumber arg))
    (if (string? arg)
      arg
      (if (instance? SafetyRating$HarmProbability arg)
        (.name arg)
        (throw (ex-info "unsupported arg" {:arg arg}))))))

(defn HarmSeverity-from-edn [arg]
  (if (number? arg)
    (SafetyRating$HarmSeverity/forNumber (int arg))
    (SafetyRating$HarmSeverity/valueOf ^String arg)))

(defn HarmSeverity-to-edn [arg]
  (if (int? arg)
    (.name (SafetyRating$HarmSeverity/forNumber arg))
    (if (string? arg)
      arg
      (if (instance? SafetyRating$HarmSeverity arg)
        (.name arg)
        (throw (ex-info "unsupported arg" {:arg arg}))))))

(defn ^SafetyRating from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/SafetyRating arg)
  (let [builder (SafetyRating/newBuilder)]
    (some->> (:category arg) HarmCategory/from-edn (.setCategory builder))
    (some->> (:probability arg) HarmProbability-from-edn (.setProbability builder))
    (some->> (:probabilityScore arg) (.setProbabilityScore builder))
    (some->> (:severity arg) HarmSeverity-from-edn (.setSeverity builder))
    (some->> (:severityScore arg) (.setSeverityScore builder))
    (some->> (:blocked arg) (.setBlocked builder))
    (.build builder)))

(defn to-edn [^SafetyRating arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/SafetyRating %)]}
  {:blocked          (.getBlocked arg)
   :category         (HarmCategory/to-edn (.getCategory arg))
   :probability      (HarmProbability-to-edn (.getProbability arg))
   :probabilityScore (.getProbabilityScore arg)
   :severity         (HarmSeverity-to-edn (.getSeverity arg))
   :severityScore    (.getSeverityScore arg)})

(def schema
  [:map
   {:ns               'gcp.vertexai.v1.api.SafetyRating
    :from-edn         'gcp.vertexai.v1.api.SafetyRating/from-edn
    :to-edn           'gcp.vertexai.v1.api.SafetyRating/to-edn
    :doc              "The safety rating contains the category of harm and the harm probability level in that category for a piece of content. Content is classified for safety across a number of harm categories and the probability of the harm classification is included here."
    :generativeai/url "https://ai.google.dev/api/generate-content#safetyrating"
    :protobuf/type    "google.cloud.vertexai.v1.SafetyRating"
    :class            'com.google.cloud.vertexai.api.SafetyRating
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.SafetyRating"}
   [:category {:optional true} :gcp.vertexai.v1.api/HarmCategory]
   [:blocked {:optional true} :boolean]
   [:probability {:optional true} [:enum
                                  "HARM_PROBABILITY_UNSPECIFIED"
                                  "HIGH"
                                  "LOW"
                                  "MEDIUM"
                                  "NEGLIGIBLE"
                                  "UNRECOGNIZED"]]
   [:probabilityScore {:optional true} :float]
   [:severity {:optional true} [:enum
                               "HARM_SEVERITY_HIGH"
                               "HARM_SEVERITY_LOW"
                               "HARM_SEVERITY_MEDIUM"
                               "HARM_SEVERITY_NEGLIGIBLE"
                               "HARM_SEVERITY_UNSPECIFIED"]]
   [:severityScore {:optional true} :float]])

(global/register-schema! :gcp.vertexai.v1.api/SafetyRating schema)
