(ns gcp.dev.digest
  "Calculates deterministic hashes for the build toolchain and inputs.
   Used to generate provenance signatures for generated bindings."
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [clojure.walk :as walk])
  (:import
   (java.security MessageDigest)))

(defn sha256
  "Computes the SHA-256 hash of a string or byte array."
  [input]
  (let [digest (MessageDigest/getInstance "SHA-256")
        bytes (if (string? input) (.getBytes ^String input "UTF-8") input)
        result (.digest digest bytes)]
    (apply str (map (partial format "%02x") result))))

(defn- file-hash
  "Calculates SHA-256 of a file's content."
  [f]
  (sha256 (slurp f)))

(defn- relative-path [base f]
  (let [base-path (.getAbsolutePath (io/file base))
        f-path (.getAbsolutePath f)]
    (if (string/starts-with? f-path base-path)
      (subs f-path (inc (count base-path)))
      f-path)))

(defn compute-toolchain-hash
  "Calculates a signature for the current toolchain state.
   Scans the gcp.dev source tree, hashes each file, and computes a composite hash.
   Returns a map with :hash and :components (map of file -> hash)."
  []
  (let [root (io/file "pkpkpk/gcp/dev/src")
        source-files (->> (file-seq root)
                          (filter #(and (.isFile %) (string/ends-with? (.getName %) ".clj")))
                          (sort-by #(.getAbsolutePath %)))
        components (reduce (fn [acc f]
                             (assoc acc (relative-path root f) (file-hash f)))
                           (sorted-map)
                           source-files)
        ;; Composite hash is hash of the sorted components map printed as string
        composite-hash (sha256 (pr-str components))]
    {:hash composite-hash
     :components components}))

(defn compute-ast-hash
  "Computes a deterministic hash of the AST node."
  [node]
  (sha256 (pr-str (dissoc node :doc)))) ;; Exclude doc strings from strict structural hash? Or keep them? Keeping is safer.
  ;; Actually, let's keep everything for now.
  ;; (sha256 (pr-str node))
  ;; But wait, if node contains :git-sha, and we change git-sha, ast-hash changes.
  ;; The node structure should be hashed.

(defn compute-generation-signature
  "Computes the final generation signature."
  [source-identity toolchain ast-hash]
  (sha256 (pr-str {:source source-identity
                   :toolchain (:hash toolchain)
                   :ast ast-hash})))
