(ns gcp.vertexai.v1.api.SafetyRating
  (:require [gcp.vertexai.v1.api.HarmCategory :as hc]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api SafetyRating
                                          SafetyRating$HarmSeverity
                                          SafetyRating$HarmProbability]))

(defn HarmProbability-from-edn [arg] (throw (Exception. "unimplemented")))

(defn HarmProbability-to-edn [arg]
  (if (int? arg)
    (.name (SafetyRating$HarmProbability/forNumber arg))
    (if (string? arg)
      arg
      (if (instance? SafetyRating$HarmProbability arg)
        (.name arg)
        (throw (ex-info "unsupported arg" {:arg arg}))))))

(defn HarmSeverity-from-edn [arg] (throw (Exception. "unimplemented")))

(defn HarmSeverity-to-edn [arg]
  (if (int? arg)
    (.name (SafetyRating$HarmSeverity/forNumber arg))
    (if (string? arg)
      arg
      (if (instance? SafetyRating$HarmSeverity arg)
        (.name arg)
        (throw (ex-info "unsupported arg" {:arg arg}))))))

(defn from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^SafetyRating arg]
  {:post [(global/strict! :gcp/vertexai.api.SafetyRating %)]}
  {:blocked          (.getBlocked arg)
   :category         (hc/to-edn (.getCategory arg))
   :probability      (HarmProbability-to-edn (.getProbability arg))
   :probabilityScore (.getProbabilityScore arg)
   :severity         (HarmSeverity-to-edn (.getSeverity arg))
   :severityScore    (.getSeverityScore arg)})