(ns gcp.dev.analyzer.javaparser.core
  "Core analysis logic for Java projects.
   Orchestrates the parsing process, including file discovery, caching, and Git integration."
  (:require [clojure.java.io :as io]
            [clojure.java.shell :as shell]
            [clojure.pprint :as pp]
            [clojure.string :as string]
            [clojure.walk :as walk]
            [clojure.edn :as edn]
            [gcp.dev.analyzer.javaparser.ast :as ast])
  (:import (com.github.javaparser StaticJavaParser)
           (java.io FileInputStream File)
           (java.security MessageDigest)))

(def output-file-path "output.edn")
(def cache-dir (or (System/getenv "GCP_CACHE_PATH") ".cache"))

(defn clear-cache 
  "Deletes the entire cache directory."
  []
  (let [dir (io/file cache-dir)]
    (when (.exists dir)
      (run! io/delete-file (reverse (file-seq dir)))
      (println "Cache cleared."))))

(defn gc-cache 
  "Deletes cache files older than 'days' (default 7)."
  ([] (gc-cache 7))
  ([days]
   (let [cutoff (- (System/currentTimeMillis) (* days 24 60 60 1000))
         dir (io/file cache-dir)]
     (when (.exists dir)
       (let [deleted (count (for [f (file-seq dir)
                                  :when (and (.isFile f)
                                             (< (.lastModified f) cutoff))]
                              (do (io/delete-file f) 1)))]
         (println "GC complete. Deleted" deleted "files."))))))

(defn sha256 
  "Computes the SHA-256 hash of a string."
  [s]
  (let [digest (MessageDigest/getInstance "SHA-256")
        bytes (.digest digest (.getBytes s "UTF-8"))]
    (apply str (map (partial format "%02x") bytes))))

(def parser-source-hash
  (try
    (let [res (io/resource "gcp/dev/analyzer/javaparser/ast.clj")]
      (if res
        (sha256 (slurp res))
        "unknown"))
    (catch Exception _ "unknown")))

(defmacro time-stage [name & body]
  `(let [start# (System/nanoTime)
         res# (do ~@body)
         end# (System/nanoTime)
         ms# (/ (double (- end# start#)) 1000000.0)]
     (println (format "Stage '%s' took %.2f ms" ~name ms#))
     res#))

(defn get-file-git-sha 
  "Gets the latest git commit SHA for a specific file."
  [file-path]
  (try
    (let [abs-path (.getAbsolutePath (io/file file-path))
          {:keys [exit out]} (clojure.java.shell/sh "git" "log" "-n" "1" "--format=%H" abs-path)]
      (when (zero? exit) (string/trim out)))
    (catch Exception _ nil)))

(defn get-cache-path 
  "Determines the cache file path for a given file based on its content hash and parser version."
  [file-git-sha file-path]
  (let [hash-key (sha256 (str parser-source-hash file-git-sha (.getAbsolutePath (io/file file-path))))]
    (io/file cache-dir (str hash-key ".edn"))))

(defn parse-file-cached 
  "Parses a single Java file, using a persistent cache if the file hasn't changed.
   Returns a vector of AST nodes."
  [file-path options]
  (let [file-git-sha (get-file-git-sha file-path)
        cache-file (get-cache-path file-git-sha file-path)]
    (if (.exists cache-file)
      (try
        (read-string (slurp cache-file))
        (catch Exception e
          (println "Error reading cache for" file-path "at" (.getAbsolutePath cache-file) ":" (.getMessage e))
          (let [ast (ast/parse file-path options file-git-sha)]
            (io/make-parents cache-file)
            (binding [*print-length* nil
                      *print-level* nil]
              (spit cache-file (pr-str ast)))
            ast)))
      (let [ast (ast/parse file-path options file-git-sha)]
        (io/make-parents cache-file)
        (binding [*print-length* nil
                  *print-level* nil]
          (spit cache-file (pr-str ast)))
        ast))))

(defn extract-javadoc 
  "Extracts Javadoc from a node, if available."
  [node]
  (try
    (if (.isPresent (.getJavadoc node))
      (.toText (.get (.getJavadoc node)))
      nil)
    (catch IllegalArgumentException _
      ;; Fallback for nodes that don't have getJavadoc (like PackageDeclaration in some versions)
      (let [comment (.getComment node)]
        (if (and (.isPresent comment)
                 (instance? com.github.javaparser.ast.comments.JavadocComment (.get comment)))
          (.toText (.parse (.asJavadocComment (.get comment))))
          nil)))))

(defn get-git-sha 
  "Gets the current HEAD SHA for a git directory."
  [dir]
  (try
    (let [{:keys [exit out]} (clojure.java.shell/sh "git" "rev-parse" "HEAD" :dir dir)]
      (when (zero? exit) (string/trim out)))
    (catch Exception _ nil)))

(defn get-git-tag 
  "Gets the latest tag for a git directory."
  [dir]
  (try
    (let [{:keys [exit out]} (clojure.java.shell/sh "git" "describe" "--tags" "--abbrev=0" :dir dir)]
      (when (zero? exit) (string/trim out)))
    (catch Exception _ nil)))

(defn get-pom-version
  "Extracts the version from a pom.xml file in the given directory."
  [dir]
  (let [pom-file (io/file dir "pom.xml")]
    (when (.exists pom-file)
      (try
        (let [content (slurp pom-file)
              ;; Simple regex to find <version>...</version> inside <project> or <parent>
              ;; We prioritize the first one found which is usually the artifact version
              matcher (re-matcher #"<version>(.+?)</version>" content)]
          (when (re-find matcher)
            (second (re-groups matcher))))
        (catch Exception _ nil)))))

(defn get-sdk-name 
  "Infers the SDK name from the directory path."
  [path]
  (let [parts (string/split path #"/")
        src-index (.indexOf parts "src")]
    (if (and (pos? src-index) (< src-index (count parts)))
      (nth parts (dec src-index))
      (last parts))))

(defn find-pom-root
  "Walks up the directory tree to find the first directory containing a pom.xml file."
  [dir]
  (let [d (io/file dir)]
    (if (or (nil? d) (not (.exists d)))
      nil
      (if (.exists (io/file d "pom.xml"))
        (.getAbsolutePath d)
        (recur (.getParent d))))))

(defn analyze-package 
  "Analyzes a package (directory of Java files).
   Orchestrates parsing of individual files (cached), and aggregates the results into a package AST.
   Also manages a package-level cache to skip re-aggregation if nothing has changed."
  [path files options]
  (let [sdk-root (or (find-pom-root path) 
                     (if (.isDirectory (io/file path)) path (.getParent (io/file path))))
        repo-sha (get-git-sha sdk-root)
        package-cache-key (sha256 (str parser-source-hash repo-sha (.getAbsolutePath (io/file path))))
        package-cache-file (io/file cache-dir (str "pkg-" package-cache-key ".edn"))]
    (println (str "Analyzing package path: " path))
    (if (.exists package-cache-file)
      (do
        (println (str "Package cache hit: " (.getName package-cache-file)))
        (try
          (read-string (slurp package-cache-file))
          (catch Exception e
            (println "Error reading package cache:" (.getMessage e))
            ;; If read fails, delete and recurse to re-generate (safe fallback)
            (io/delete-file package-cache-file)
            (analyze-package path files options))))
      
      (do
        (println "Package cache miss. Analyzing package...")
        (let [class-asts (time-stage "Parsing Classes (Cached)"
                           (reduce (fn [acc f]
                                     (let [res (parse-file-cached (.getPath f) options)]
                                       (if (seq res)
                                         (reduce (fn [inner-acc type-node]
                                                   (assoc inner-acc (symbol (:name type-node)) type-node))
                                                 acc
                                                 res)
                                         acc)))
                                   {}
                                   files))
              git-sha (time-stage "Getting Repo SHA" repo-sha) ;; Use pre-calculated
              git-tag (time-stage "Getting Repo Tag" (or (get-git-tag sdk-root)
                                                         (get-pom-version sdk-root)))
              package-doc (time-stage "Extracting Package Doc"
                            (when-let [info (->> files
                                               (filter #(string/ends-with? (.getName %) "package-info.java"))
                                               (sort-by #(.getPath %))
                                               first)]
                              (let [cu (StaticJavaParser/parse (FileInputStream. info))]
                                (when-let [pkg (.getPackageDeclaration cu)]
                                  (if (.isPresent pkg)
                                    (extract-javadoc (.get pkg))
                                    nil)))))
              service-clients (->> (vals class-asts)
                                   (filter #(= (:category %) :client))
                                   (mapv (fn [node]
                                           (symbol (str (:package node) "." (:name node))))))
              pkg-ast {:sdk-name (get-sdk-name path)
                       :path path
                       :git-sha git-sha
                       :git-tag git-tag
                       :doc package-doc
                       :service-clients service-clients
                       :children class-asts}]
          
          (io/make-parents package-cache-file)
          (time-stage "Writing Package Cache"
            (binding [*print-length* nil
                      *print-level* nil]
              (spit package-cache-file (pr-str pkg-ast))))
          pkg-ast)))))

(defn -main 
  "Entry point for command-line usage.
   Usage: clj -M -m gcp.dev.analyzer.javaparser.core <source-path> <output-path> [--pretty]"
  [& args]
  (let [args-set (set args)
        pretty? (contains? args-set "--pretty")
        input-args (remove #(= % "--pretty") args)]
    
    (when (< (count input-args) 2)
      (println "Usage: clj -M -m gcp.dev.analyzer.javaparser.core <source-path> <output-path> [--pretty]")
      (println "Error: Missing required arguments.")
      (System/exit 1))

    (let [path (first input-args)
          output-path (second input-args)
          options {:include-private? false
                   :include-package-private? false}]
      (println "Processing:" path "with options:" options)
      (println "Output will be written to:" output-path)
      (println "Pretty printing:" pretty?)
      (io/make-parents (io/file cache-dir "dummy"))
      
      (time-stage "Total Execution"
        (let [file (io/file path)
              files (time-stage "File Discovery"
                      (if (.isDirectory file)
                        (filter #(string/ends-with? (.getName %) ".java") (file-seq file))
                        [file]))
              file-count (count files)]
          
          (println "Found" file-count "Java files to parse.")
          (println "Parser source hash:" parser-source-hash)
          
          (let [package-ast (analyze-package path files options)]
            (time-stage "Writing Output"
              (with-open [writer (io/writer output-path)]
                (if pretty?
                  (pp/pprint package-ast writer)
                  (binding [*print-length* nil
                            *print-level* nil]
                    (.write writer (pr-str package-ast))))))
            (println (str "Processed package. Output written to " output-path))))))))