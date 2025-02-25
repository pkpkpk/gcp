(ns gcp.dev
  (:require [clojure.java.io :as io]
            [clojure.repl :refer :all]
            [clojure.string :as string]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [gcp.bigquery.v2.TableId :as TableId]
            [gcp.dev.asm :as asm]
            [gcp.dev.analyzer :as ana :refer [analyze]]
            [gcp.dev.analyzer.extract :as extract]
            [gcp.dev.compiler :as c :refer [emit-to-edn emit-from-edn emit-ns-form]]
            [gcp.dev.malli :refer [malli]]
            [gcp.dev.models :as models]
            [gcp.dev.packages :as packages]
            [gcp.dev.packages.bigquery :as bq]
            [gcp.dev.util :refer :all]
            [gcp.global :as g]
            [gcp.vertexai.generativeai :as genai]
            [gcp.vertexai.v1]
            [malli.dev]))

#_(do (require :reload 'gcp.dev) (in-ns 'gcp.dev))
#_(def bigquery (bq/bigquery))

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


;; TODO gcp.global. priorities
;;;    0) schemas need to-edn/from-edn in option map as part of malli emitter
;;;       such that they can be resolved and tested here
;;;    1) all generated schemas should produce valid instances via from-edn
;;;    2) edn can be recovered from instances and required fields compared
;;;     ---  not all types will rt identically, java sdk will backfill values (which we exploit)
;;;     ---  full equivalence via :readOnly flags?
;;; #_
;(def prop
;  (prop/for-all [t (generator :gcp/bigquery.TableId)]
;    (= t (TableId/to-edn (TableId/from-edn t)))))
;
;#_(tc/quick-check 100 prop)
;
;(def prop
;  (prop/for-all [ljc (generator :gcp/bigquery.LoadJobConfiguration)]
;    (gcp.bigquery.v2.LoadJobConfiguration/from-edn ljc)))
;
;;(tc/quick-check 100 prop)
;
;(defn quick-check [])
;;;

;(reduce
;  (fn [_ className]
;    (when (some #(= % 'byte<>) (map :returnType (instance-methods className)))
;      (reduced className)))
;  (sorted-set)
;  (:types/all bigquery))
;=> "com.google.cloud.bigquery.FieldValue"

; (def instance-method-return-types
;   (reduce
;       (fn [acc ms]
;         (into acc (map :returnType) ms))
;       (sorted-set)
;       (map instance-methods (:types/all bigquery))))

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

  ;EncryptionConfiguration

  ;; TODO
  ;;  -- emitted newBuilder incorrect, :optionalSourceUris in malli incorrect
  ;;  -- LoadJobConfiguration/to-edn
  (extract bigquery "com.google.cloud.bigquery.LoadJobConfiguration")
  (analyze bigquery "com.google.cloud.bigquery.LoadJobConfiguration") ;=> accessor
  (malli bigquery "com.google.cloud.bigquery.LoadJobConfiguration")
  ;(check bigquery "com.google.cloud.bigquery.LoadJobConfiguration")

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