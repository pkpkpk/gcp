(ns gcp.dev.compiler
  "Top-level orchestration for compiling Java ASTs into Clojure code.
   Handles generation provenance and signature injection."
  (:require
   [clojure.java.io :as io]
   [gcp.dev.digest :as digest]
   [gcp.dev.packages :as pkg]
   [gcp.dev.toolchain.emitter :as emitter]
   [gcp.dev.toolchain.fuzz :as fuzz]
   [gcp.dev.toolchain.shared :as shared]
   [gcp.dev.util :as u])
  (:import
   (java.time Instant)))

(set! *print-namespace-maps* false)

(defn compute-provenance
  "Computes the provenance map for a class node."
  [node version]
  (let [source-identity {:package (:package node)
                         :version version
                         :class (:className node)
                         :git-sha (:git-sha node)}
        toolchain (digest/compute-toolchain-hash)
        ast-hash (digest/compute-ast-hash node)
        signature (digest/compute-generation-signature source-identity toolchain ast-hash)]
    {:signature       signature
     :source          source-identity
     :toolchain-hash  (:hash toolchain)
     :generation-time (str (Instant/now))}))

(defn compile-to-string
  "Compiles a class node into a formatted string with optional metadata & provenance."
  ([node]
   (compile-to-string node nil))
  ([node {:keys [metadata provenance?] :or {provenance? false metadata {}}}]
   (when-not (contains? shared/categories (:category node))
     (throw (Exception. (str "Unsupported category in class-node: '" (:category node) "'"))))
   (let [metadata (cond-> metadata
                          provenance? (merge {:gcp.dev/provenance (compute-provenance node (u/extract-version (:doc node)))}))]
     (emitter/emit-to-string node (not-empty metadata)))))

(defn compile-to-file
  "Compiles a class node and writes the result to the specified output path."
  [node output-path]
  (let [code (compile-to-string node)]
    (io/make-parents (io/file output-path))
    (spit output-path code)
    output-path))

(defn compile-and-certify
  "Compiles, certifies (fuzzes), and writes a class to disk.
   
   Args:
     pkg-key: Package keyword (e.g., :vertexai).
     fqcn: Fully qualified class name.
     output-path: Target file path.
     options: Map containing :seed (optional)."
  [pkg-key fqcn output-path options]
  (let [cert-result     (fuzz/certify-class pkg-key fqcn (merge options {:timeout-ms 60000}))
        cert-metadata   {:gcp.dev/certification cert-result}
        ana-node        (pkg/analyze-class pkg-key fqcn)
        metadata        cert-metadata
        code            (compile-to-string ana-node {:metadata metadata})]
    (io/make-parents (io/file output-path))
    (spit output-path code)
    output-path))
