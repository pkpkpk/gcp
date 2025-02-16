(ns gcp.dev
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [gcp.dev.analyzer :as ana :refer [analyze]]
            [gcp.dev.compiler :as c :refer [emit-to-edn emit-from-edn emit-ns-form]]
            [gcp.dev.malli :refer [malli]]
            [gcp.global :as g]
            [gcp.vertexai.v1]
            [gcp.vertexai.generativeai :as genai]
            [malli.dev]))

;; TODO
;;  - kill enum bindings in vertexai in favor of inlining
;;  - com.google.cloud.ServiceOptions
;;  - genai response-schemas should accept named in properties slots, automatically transform them to string
;;  - investigate response-schemas from malli

;(defn missing-files [package src-root]
;  (let [expected-binding-names (into (sorted-set) (map first) (map util/class-parts (:classes package)))
;        expected-files (map #(io/file src-root (str % ".clj")) expected-binding-names)]
;    (remove #(.exists %) expected-files)))

;; singlefile dst, prompts?

(comment
  (do (require :reload 'gcp.dev.analyzer) (in-ns 'gcp.dev.analyzer))

  (analyze bigquery "com.google.cloud.bigquery.LoadJobConfiguration") ;=> accessor
  (malli bigquery "com.google.cloud.bigquery.LoadJobConfiguration")

  (analyze bigquery "com.google.cloud.bigquery.WriteChannelConfiguration")
  (malli bigquery "com.google.cloud.bigquery.WriteChannelConfiguration")

  ;; TODO this is a union type!
  (analyze bigquery "com.google.cloud.bigquery.FormatOptions") ;=> static-factory
  (malli bigquery "com.google.cloud.bigquery.FormatOptions")

  (analyze bigquery "com.google.cloud.bigquery.Acl.Entity.Type")
  (malli bigquery "com.google.cloud.bigquery.Acl.Entity.Type")
  ;(get-in @bigquery [:discovery :schemas :JobConfigurationQuery :properties :writeDisposition])
  ;{:type "string" :description "Optional. Specifies the action that occurs if the destination table already exists. The following values are supported: * WRITE_TRUNCATE: If the table already exists, BigQuery overwrites the data, removes the constraints, and uses the schema from the query result. * WRITE_APPEND: If the table already exists, BigQuery appends the data to the table. * WRITE_EMPTY: If the table already exists and contains data, a 'duplicate' error is returned in the job result. The default value is WRITE_EMPTY. Each action is atomic and only occurs if BigQuery is able to complete the job successfully. Creation, truncation and append actions occur as one atomic update upon job completion."}
  )