(ns gcp.dev
  (:require [clojure.java.io :as io]
            [clojure.repl :refer :all]
            [clojure.string :as string]
            [gcp.dev.analyzer :as ana :refer [analyze]]
            [gcp.dev.compiler :as c :refer [emit-to-edn emit-from-edn emit-ns-form]]
            [gcp.dev.malli :refer [malli]]
            [gcp.dev.packages :as packages]
            [gcp.dev.util :refer :all]
            [gcp.global :as g]
            [gcp.vertexai.v1]
            [gcp.vertexai.generativeai :as genai]
            [malli.dev]))

(set! *print-namespace-maps* false)

;; TODO
;;  - kill enum bindings in vertexai in favor of inlining
;;  - com.google.cloud.ServiceOptions
;;  - genai response-schemas should accept named in properties slots, automatically transform them to string
;;  - singlefile dst, prompts?
;;  - investigate response-schemas from malli
;;  - index samples + repositories + bookmarks
;;  - configuration inference -> instead of looking at :type, check if schema can be matched unambiguously
;;  - enum for FormatOptions (& ExportJobConfiguration)... says JSON in docstrings but is actually NEWLINE_DELIMITED_JSON

(defn into-registry
  ([package key]
   (into-registry (sorted-map) package key))
  ([registry package key]
   (into registry
         (map
           (fn [className]
             (Thread/sleep 1000)
             (let [schema (malli package className)]
               [(:gcp/key (second schema)) schema])))
         (g/coerce set? (get package key)))))

(comment
  (do (require :reload 'gcp.dev) (in-ns 'gcp.dev))
  (def bigquery (packages/bigquery))

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