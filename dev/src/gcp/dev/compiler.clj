(ns gcp.dev.compiler
  "Top-level orchestration for compiling Java ASTs into Clojure code.
   Handles generation provenance and signature injection."
  (:require
   [clojure.java.io :as io]
   [gcp.dev.digest :as digest]
   [gcp.dev.packages :as pkg]
   [gcp.dev.toolchain.analyzer :as ana]
   [gcp.dev.toolchain.emitter :as emitter]
   [gcp.dev.toolchain.fuzz :as fuzz]
   [gcp.dev.util :as u]
   [zprint.core :as zp]))

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
    {:signature signature
     :source source-identity
     :toolchain-hash (:hash toolchain)
     :generation-time (str (java.time.Instant/now))}))

(defn compile-class
  "Compiles a class node into a formatted string string with provenance."
  ([node] (compile-class node nil))
  ([node extra-metadata]
   (let [version (u/extract-version (:doc node))
         provenance (compute-provenance node version)
         metadata (merge {:gcp.dev/provenance provenance} extra-metadata)]
     (emitter/compile-class node metadata))))

(defn compile-to-file
  "Compiles a class node and writes the result to the specified output path."
  [node output-path]
  (let [code (compile-class node)]
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
  (let [cert-result (fuzz/certify-class pkg-key fqcn (merge options {:timeout-ms 60000}))
        cert-metadata {:gcp.dev/certification cert-result}
        ;; Analyze and Emit
        node (pkg/lookup-class pkg-key fqcn)
        ana-node (ana/analyze-class-node node)
        code (compile-class ana-node cert-metadata)]
    (io/make-parents (io/file output-path))
    (spit output-path code)
    output-path))
