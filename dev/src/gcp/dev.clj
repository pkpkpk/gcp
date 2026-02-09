(ns gcp.dev
 (:refer-clojure :exclude [compile])
  (:require
   [clojure.java.io :as io]
   [clojure.repl :refer :all]
   [clojure.string :as string]
   [gcp.dev.toolchain.shared]
   [gcp.dev.toolchain.parser.ast]
   ;[gcp.dev.toolchain.analyzer :as ana]
   [gcp.dev.toolchain.malli :as m]
   [gcp.dev.toolchain.emitter :as e]
   [gcp.dev.compiler :as c]
   [gcp.dev.packages :as p]
   [gcp.dev.util :refer :all]
   [gcp.global :as g]))

#_(do (require :reload 'gcp.dev) (in-ns 'gcp.dev))

;; TODO
;; - superfluous requires -> (compile "com.google.cloud.bigquery.BigQueryOptions")

(comment
 ;; This needs to be handled in custom QueryParameterValue?
 (when (seq queryParameters)
  (if (map? queryParameters)
   (do
    (.setParameterMode builder "NAMED")
    (doseq [[k v] queryParameters]
     (when (some? v)
      (.addNamedParameter builder (name k) (QueryParameterValue/from-edn v)))))
   (do
    (.setParameterMode builder "POSITIONAL")
    (.setPositionalParameters builder (map QueryParameterValue/from-edn queryParameters)))))
 )

(defn lookup [fqcn]
 (p/lookup-class fqcn))

(defn deps [fqcn]
 (p/class-deps fqcn))

(defn analyze [fqcn]
 (p/analyze-class fqcn))

(defn schema [fqcn]
 (m/->schema (analyze fqcn)))

(defn compile [fqcn]
 (c/compile-to-string (analyze fqcn)))

(defn clear-cache []
 (p/clear-cache))

(set! *print-namespace-maps* false)

(comment
 (p/clear-cache)
 (def bq (p/api-types-by-category :bigquery))
 (def global-by-category (p/global-api-types-by-category))
 (get global-by-category :static-factory)

 ;com.google.cloud.bigquery.InsertAllRequest.RowToInsert

 ;(def ^:dynamic *strict-foreign-presence?*
 ;  "If true, throws an exception when a foreign (external library) dependency is missing."
 ;  true)
 ;
 ;(def ^:dynamic *strict-peer-presence?*
 ;  "If true, throws an exception when a peer (same library, different file) dependency is missing."
 ;  false)
 ;
 ;(defn- check-certification [ns-sym fqcn]
 ;  (let [ns-meta (u/ns-meta ns-sym)]
 ;    (when-not (:gcp.dev/certification ns-meta)
 ;      (throw (ex-info (str "Foreign namespace NOT CERTIFIED: " ns-sym)
 ;                      {:namespace ns-sym :used-by fqcn})))))
 ;
 )