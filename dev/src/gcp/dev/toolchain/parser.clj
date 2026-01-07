(ns gcp.dev.toolchain.parser
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [gcp.dev.toolchain.parser.ast :as ast]
   [gcp.dev.toolchain.parser.core :as core]))

(defn parse-file
  "Parses a single Java file and returns a sequence of AST nodes.
   options defaults to {:include-private? false :include-package-private? false}"
  ([file-path] (parse-file file-path nil))
  ([file-path options]
   (let [opts (merge {:include-private? false :include-package-private? false} options)]
     (ast/parse (str file-path) opts "in-memory"))))

(defn parse-files
  "Parses a collection of file paths and returns a map of class-name (symbol) to AST node."
  [file-paths]
  (reduce (fn [acc path]
            (let [res (parse-file path)]
              (if (seq res)
                (reduce (fn [inner-acc type-node]
                          (assoc inner-acc (symbol (:name type-node)) type-node))
                        acc
                        res)
                acc)))
          {}
          file-paths))

(defn parse-package
  "Analyzes a source directory and returns the package AST map.
   Leverages parser.core's caching mechanism."
  [source-path options]
  (let [source-file (io/file source-path)
        files (if (.isDirectory source-file)
                (filter #(and (string/ends-with? (.getName %) ".java")
                              (not (string/includes? (.getPath %) "/src/test/")))
                        (file-seq source-file))
                [source-file])
        opts (merge {:include-private? false :include-package-private? false} options)]
    (core/analyze-package source-path files opts)))

(defn analyze-package
  "Analyzes a package given a root path and a list of specific files.
   This bypasses internal file discovery to allow for custom inclusion/exclusion logic."
  [source-path files options]
  (core/analyze-package source-path files options))

(defn parse-to-file
  "Analyzes a source directory and spits the result to an output file.
   Leverages parser.core's caching mechanism."
  [source-path output-path options]
  (let [result (parse-package source-path options)]
    (io/make-parents (io/file output-path))
    (binding [*print-length* nil
              *print-level* nil]
      (spit output-path (pr-str result)))
    output-path))

(defn clear-cache []
  (core/clear-cache))

(defn gc-cache
  ([] (core/gc-cache))
  ([days] (core/gc-cache days)))
