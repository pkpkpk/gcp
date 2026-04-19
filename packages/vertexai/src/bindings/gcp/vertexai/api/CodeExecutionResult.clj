;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.CodeExecutionResult
  {:doc
     "<pre>\nResult of executing the [ExecutableCode].\n\nAlways follows a `part` containing the [ExecutableCode].\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.CodeExecutionResult}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.CodeExecutionResult"
   :gcp.dev/certification
     {:base-seed 1776627459551
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627459551 :standard 1776627459552 :stress 1776627459553}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:37:40.380786658Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api CodeExecutionResult
            CodeExecutionResult$Builder CodeExecutionResult$Outcome]))

(declare from-edn to-edn Outcome-from-edn Outcome-to-edn)

(def Outcome-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nEnumeration of possible outcomes of the code execution.\n</pre>\n\nProtobuf enum {@code google.cloud.vertexai.v1.CodeExecutionResult.Outcome}",
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/CodeExecutionResult.Outcome}
   "OUTCOME_UNSPECIFIED" "OUTCOME_OK" "OUTCOME_FAILED"
   "OUTCOME_DEADLINE_EXCEEDED"])

(defn ^CodeExecutionResult from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/CodeExecutionResult arg)
  (let [builder (CodeExecutionResult/newBuilder)]
    (when (some? (get arg :outcome))
      (.setOutcome builder
                   (CodeExecutionResult$Outcome/valueOf (get arg :outcome))))
    (when (some? (get arg :output)) (.setOutput builder (get arg :output)))
    (.build builder)))

(defn to-edn
  [^CodeExecutionResult arg]
  {:post [(global/strict! :gcp.vertexai.api/CodeExecutionResult %)]}
  (when arg
    (cond-> {:outcome (.name (.getOutcome arg))}
      (some->> (.getOutput arg)
               (not= ""))
        (assoc :output (.getOutput arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nResult of executing the [ExecutableCode].\n\nAlways follows a `part` containing the [ExecutableCode].\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.CodeExecutionResult}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/CodeExecutionResult}
   [:outcome
    {:getter-doc
       "<pre>\nRequired. Outcome of the code execution.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.CodeExecutionResult.Outcome outcome = 1 [(.google.api.field_behavior) = REQUIRED];\n</code>\n\n@return The outcome.",
     :setter-doc
       "<pre>\nRequired. Outcome of the code execution.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.CodeExecutionResult.Outcome outcome = 1 [(.google.api.field_behavior) = REQUIRED];\n</code>\n\n@param value The outcome to set.\n@return This builder for chaining."}
    [:enum {:closed true} "OUTCOME_UNSPECIFIED" "OUTCOME_OK" "OUTCOME_FAILED"
     "OUTCOME_DEADLINE_EXCEEDED"]]
   [:output
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Contains stdout when code execution is successful, stderr or\nother description otherwise.\n</pre>\n\n<code>string output = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The output.",
     :setter-doc
       "<pre>\nOptional. Contains stdout when code execution is successful, stderr or\nother description otherwise.\n</pre>\n\n<code>string output = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The output to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/CodeExecutionResult schema,
              :gcp.vertexai.api/CodeExecutionResult.Outcome Outcome-schema}
    {:gcp.global/name "gcp.vertexai.api.CodeExecutionResult"}))