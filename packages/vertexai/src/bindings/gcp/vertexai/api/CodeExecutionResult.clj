;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.CodeExecutionResult
  {:doc
     "<pre>\nResult of executing the [ExecutableCode].\n\nAlways follows a `part` containing the [ExecutableCode].\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.CodeExecutionResult}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.CodeExecutionResult"
   :gcp.dev/certification
     {:base-seed 1775465691800
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465691800 :standard 1775465691801 :stress 1775465691802}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:54:52.756029422Z"}}
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
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/CodeExecutionResult schema,
              :gcp.vertexai.api/CodeExecutionResult.Outcome Outcome-schema}
    {:gcp.global/name "gcp.vertexai.api.CodeExecutionResult"}))