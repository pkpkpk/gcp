(ns gcp.dev.digest
  "Calculates deterministic hashes for the build toolchain and inputs.
   Used to generate provenance signatures for generated bindings."
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [clojure.walk :as walk]
   [edamame.core :as edamame]
   [gcp.dev.util :as u])
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

(defn compute-foreign-source-hash
  "Computes a hash of a foreign namespace source file, excluding the certification metadata.
   This allows the certification to be embedded in the file without invalidating the hash."
  [ns-sym]
  (let [root (u/get-gcp-repo-root)
        path (str root "/packages/global/src/" (string/replace (name ns-sym) #"\." "/") ".clj")
        source (slurp path)
        forms (edamame/parse-string-all source {:all true :auto-resolve {:current ns-sym}})]
    (if (and (seq forms) (= 'ns (first (first forms))))
      (let [[ns-form & rest-forms] forms
            ;; ns-form is (ns name ?doc ?attr-map & args)
            ;; We want to find the attr-map and dissoc :gcp.dev/certification
            sanitized-ns-form
            (let [name-sym (second ns-form)
                  after-name (drop 2 ns-form)
                  maybe-doc (first after-name)
                  [doc rest-after-name] (if (string? maybe-doc)
                                          [maybe-doc (rest after-name)]
                                          [nil after-name])
                  maybe-attr (first rest-after-name)
                  [attr rest-final] (if (map? maybe-attr)
                                      [(dissoc maybe-attr :gcp.dev/certification) (rest rest-after-name)]
                                      [nil rest-after-name])]
              (apply list
                     (cond-> [(symbol "ns") name-sym]
                       doc (conj doc)
                       (seq attr) (conj attr)
                       true (concat rest-final))))]
        (sha256 (pr-str (cons sanitized-ns-form rest-forms))))
      (sha256 source))))

(defn compute-toolchain-hash
  "Calculates a signature for the current toolchain state.
   Scans the gcp.dev source tree, hashes each file, and computes a composite hash.
   Returns a map with :hash and :components (map of file -> hash)."
  []
  (let [root (io/file (u/get-gcp-repo-root) "dev/src")
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
