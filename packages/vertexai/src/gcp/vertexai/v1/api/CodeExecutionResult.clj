(ns gcp.vertexai.v1.api.CodeExecutionResult
  (:require [gcp.global :as global])
  (:import (com.google.cloud.vertexai.api CodeExecutionResult CodeExecutionResult$Outcome)))

(defn- ^CodeExecutionResult$Outcome Outcome-from-edn [arg]
  (if (number? arg)
    (CodeExecutionResult$Outcome/forNumber (int arg))
    (CodeExecutionResult$Outcome/valueOf ^String arg)))

(defn- Outcome-to-edn [arg]
  (if (int? arg)
    (.name (CodeExecutionResult$Outcome/forNumber arg))
    (if (string? arg)
      arg
      (if (instance? CodeExecutionResult$Outcome arg)
        (.name arg)
        (throw (ex-info "unsupported arg" {:arg arg}))))))

(defn ^CodeExecutionResult from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/CodeExecutionResult arg)
  (let [builder (CodeExecutionResult/newBuilder)]
    (some->> (:outcome arg) Outcome-from-edn (.setOutcome builder))
    (some->> (:output arg) (.setOutput builder))
    (.build builder)))

(defn to-edn [^CodeExecutionResult arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/CodeExecutionResult %)]}
  {:outcome (Outcome-to-edn (.getOutcome arg))
   :output  (.getOutput arg)})

(def schema
  [:map
   {:protobuf/type "google.cloud.vertexai.v1.CodeExecutionResult"
    :class         'com.google.cloud.vertexai.api.CodeExecutionResult}
   [:outcome {:optional true} [:or :string :int]]
   [:output {:optional true} :string]])

(global/register-schema! :gcp.vertexai.v1.api/CodeExecutionResult schema)
