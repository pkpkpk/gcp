(ns gcp.vertexai.v1.api.SafetyRating
  (:require [gcp.vertexai.v1.api.HarmCategory :as hc]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api SafetyRating
                                          SafetyRating$HarmSeverity
                                          SafetyRating$HarmProbability]))

(def ^{:class SafetyRating$HarmProbability}
  HarmProbability-schema
  [:enum "HARM_PROBABILITY_UNSPECIFIED"
         "HIGH"
         "LOW"
         "MEDIUM"
         "NEGLIGIBLE"
         "UNRECOGNIZED"])

(defn HarmProbability-from-edn [arg] (throw (Exception. "unimplemented")))

(defn HarmProbability-to-edn [arg]
  {:post [(global/strict! HarmProbability-schema %)]}
  (if (int? arg)
    (.name (SafetyRating$HarmProbability/forNumber arg))
    (if (string? arg)
      arg
      (if (instance? SafetyRating$HarmProbability arg)
        (.name arg)
        (throw (ex-info "unsupported arg" {:arg arg}))))))

#!---------------------------------------------------------------------------------

(def ^{:class SafetyRating$HarmSeverity}
  HarmSeverity-schema
  [:enum "HARM_SEVERITY_HIGH"
         "HARM_SEVERITY_LOW"
         "HARM_SEVERITY_MEDIUM"
         "HARM_SEVERITY_NEGLIGIBLE"
         "HARM_SEVERITY_UNSPECIFIED"])

(defn HarmSeverity-from-edn [arg] (throw (Exception. "unimplemented")))

(defn HarmSeverity-to-edn [arg]
  {:post [(global/strict! HarmSeverity-schema %)]}
  (if (int? arg)
    (.name (SafetyRating$HarmSeverity/forNumber arg))
    (if (string? arg)
      arg
      (if (instance? SafetyRating$HarmSeverity arg)
        (.name arg)
        (throw (ex-info "unsupported arg" {:arg arg}))))))

#!---------------------------------------------------------------------------------

(def ^{:class SafetyRating} schema
  [:map
   [:category hc/schema]
   [:blocked :boolean]
   [:probability HarmProbability-schema]
   [:probabilityScore :float]
   [:severity HarmSeverity-schema]
   [:severityScore :float]])

(defn from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^SafetyRating arg]
  {:post [(global/strict! schema %)]}
  {:blocked          (.getBlocked arg)
   :category         (hc/to-edn (.getCategory arg))
   :probability      (HarmProbability-to-edn (.getProbability arg))
   :probabilityScore (.getProbabilityScore arg)
   :severity         (HarmSeverity-to-edn (.getSeverity arg))
   :severityScore    (.getSeverityScore arg)})