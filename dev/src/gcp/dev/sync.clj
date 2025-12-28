(ns gcp.dev.sync
  (:require
   [clojure.java.io :as io]
   [clojure.set :as s]
   [clojure.string :as string]
   [gcp.dev.analyzer :as ana]
   [gcp.dev.compiler :as compiler]
   [gcp.dev.packages :as pkg]
   [gcp.dev.util :as u]))

;; We need to expose package-to-ns from compiler or re-implement it.
;; Since compiler is internal, I will reimplement helper here or just rely on the fact that
;; I can analyze the class node to get the expected namespace.

(defn- extract-version [doc]
  (when doc
    (when-let [m (re-find #"google\.cloud\.[^.]+\.([^.]+)\.[^.]+" doc)]
      (let [v (second m)]
        (when (re-matches #"v\d+.*" v)
          v)))))

(defn- package-to-ns [package-name version]
  (let [base (string/replace package-name #"^com\.google\.cloud\." "")]
    (if version
      (let [parts (string/split base #"\.")
            new-parts (if (> (count parts) 1)
                        (into (vec (butlast parts)) [version (last parts)])
                        (conj parts version))]
        (str "gcp." (string/join "." new-parts)))
      (str "gcp." base))))

(defn- expected-file-path [node root-dir]
  (let [version (extract-version (:doc node))
        ns-str (package-to-ns (:package node) version)
        class-name (:className node)
        ;; Convert namespace to path: gcp.vertexai.v1.api -> gcp/vertexai/v1/api
        ns-path (string/replace ns-str "." "/")]
    (str root-dir "/" ns-path "/" class-name ".clj")))

(defn- list-clj-files [dir]
  (->> (file-seq (io/file dir))
       (filter #(and (.isFile %) (string/ends-with? (.getName %) ".clj")))
       (map #(.getAbsolutePath %))
       (into #{})))

(defn diff-package
  "Compares the expected generated files for a package against the actual files in a directory.
   Returns a map with :missing, :extra, :matching."
  [pkg-key output-dir]
  (let [pkg-ast (pkg/parse pkg-key)
        ;; We only care about classes that we would generate bindings for.
        relevant-nodes (->> (vals (:class/by-fqcn pkg-ast))
                            (map ana/analyze-class-node)
                            (filter #(#{:accessor :enum :client :static-factory :string-enum :concrete-union :abstract-union} (:type %))))
        expected-files (reduce (fn [acc node]
                                 (assoc acc (expected-file-path node output-dir) node))
                               {}
                               relevant-nodes)
        actual-files (list-clj-files output-dir)
        expected-paths (set (keys expected-files))
        missing (s/difference expected-paths actual-files)
        extra (s/difference actual-files expected-paths)
        matching (s/intersection expected-paths actual-files)]
    {:missing (sort missing)
     :extra (sort extra)
     :matching (count matching)
     :total-expected (count expected-paths)
     :total-actual (count actual-files)}))

(defn report
  [pkg-key output-dir]
  (let [result (diff-package pkg-key output-dir)]
    (println "Sync Report for" pkg-key "in" output-dir)
    (println "--------------------------------------------------")
    (println "Total Expected:" (:total-expected result))
    (println "Total Actual:  " (:total-actual result))
    (println "Matching:      " (:matching result))
    (println "--------------------------------------------------")
    (when (seq (:missing result))
      (println "Missing Files:")
      (doseq [f (:missing result)]
        (println " - " f)))
    (println "--------------------------------------------------")
    (when (seq (:extra result))
      (println "Extra Files (or manually created/renamed):")
      (doseq [f (:extra result)]
        (println " - " f)))))
