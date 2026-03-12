(ns gcp.dev
  (:refer-clojure :exclude [compile])
  (:require
   [clojure.java.io :as io]
   [clojure.repl :refer :all]
   [clojure.string :as string]
   [gcp.dev.compiler :as c]
   [gcp.dev.packages :as p]
   [gcp.dev.toolchain.analyzer :as ana]
   [gcp.dev.toolchain.emitter :as e]
   [gcp.dev.toolchain.fuzz :as fuzz]
   [gcp.dev.toolchain.malli :as m]
   [gcp.dev.util :refer :all]
   [gcp.global :as g])
  (:import
   (java.io File)))

#_(use :reload 'gcp.dev)
#_(in-ns 'gcp.dev)

(defn needs-sync? [pkg-like]
  (p/needs-sync? pkg-like))

(defn lookup [fqcn]
  (p/lookup-class fqcn))

(defn deps [fqcn]
  (p/class-deps fqcn))

(defn analyze [fqcn]
  (p/analyze-class fqcn))

(defn schema [fqcn]
  (m/->schema (analyze fqcn)))

(defn compile-to-string [fqcn]
  (c/compile-to-string (analyze fqcn)))

(defn compile-to-file [fqcn]
  (let [node (analyze fqcn)
        target (p/target-file fqcn)]
    (c/compile-to-file node target)))

(defn target-file [fqcn] (p/target-file fqcn))

(defn certify [arg]
  (let [[fqcn file] (if (instance? File arg)
                      [(p/target-file->fqcn arg) arg]
                      [arg (target-file arg)])
        pkg-key (p/lookup-pkg-key fqcn)
        pkg     (p/parse pkg-key)
        custom? (contains? (:custom-namespace-mappings pkg) (symbol fqcn))]
    (when-not custom?
      (compile-to-file fqcn))
    (fuzz/certify-file file)))

(defn certified? [fqcn]
  (let [target (target-file fqcn)]
    (if (not (.exists target))
      false
      (fuzz/certified? target))))

(defn delete [fqcn]
  (io/delete-file (target-file fqcn) true))

(def manifest p/manifest)

(defn clear-cache [] (p/clear-cache))

(defn remaining-by-category [pkg-key]
  (into (sorted-map)
        (map
          (fn [[key fqcns]]
            (let [remaining (remove certified? fqcns)]
              (when (seq remaining)
                [key remaining]))))
        (p/api-types-by-category pkg-key)))

(defn delete-graph [fqcn]
  (let [nodes (keys (p/require-graph fqcn))]
    (doseq [node nodes]
      (delete node))))

(defn certify-graph [fqcn]
  (let [order (p/topological-order fqcn)]
    (doseq [node order]
      (certify node))))

#!------------------------------------------------------------------------

(defn smallest
  ([] (smallest *e))
  ([e]
   (some-> e ex-data :result :shrunk :smallest first)))

#!------------------------------------------------------------------------

;; TODO
;; doc discovery! gen example, repair
;; sugar single args for accessor with builder (JobId has no arg newBuilder but also .of(job) etc)
;; *strict* mode disable, investigate malli instrumentation
;; errors thrown! check ast! bq.query()
;; IAM, sandbox, client
;; doc strings
;; examples
;; fixture dataset

(comment
  (p/clear-cache)
  (def bq (p/api-types-by-category :bigquery))
  (def bq (remaining-by-category :bigquery))
  (def global-by-category (p/global-api-types-by-category))
  (get global-by-category :static-factory)

  (reduce
    (fn [acc {:keys [name parameters]}]
      (if (contains? acc name)
        (update-in acc [name] conj parameters)
        (assoc acc name [parameters])))
    (sorted-map)
    (:methods (lookup "com.google.cloud.bigquery.BigQuery"))))

(defn clean-method [m]
  (-> (dissoc m :doc :static? :private? :abstract?)
      (update :parameters (fn [ps] (mapv #(dissoc % :varArgs?) ps)))))

(defn can-consolidate?
  [methods]
  (and (apply = (map :name methods))
       (apply = (map :returnType methods))))

(defn consolidate-signatures
  [methods]
  (assert (can-consolidate? methods))
  (let [base (dissoc (first methods) :parameters)
        signatures (vec (sort-by count < (map :parameters methods)))]
    (assoc base :signatures signatures)))

(defn aggregate-methods [arg]
  (if (string? arg)
    (aggregate-methods (lookup arg))
    (let [methods       (map clean-method (:methods arg))
          method-groups (partition-by :name (sort-by :name methods))]
      (reduce
        (fn [acc method-group]
          (if (can-consolidate? method-group)
            (conj acc (consolidate-signatures method-group))
            (into acc method-group)))
        []
        method-groups))))
