(ns gcp.dev.packages
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [gcp.dev.packages.package :as pkg]
   [gcp.dev.packages.parser :as parser]))

(defn- get-googleapis-repos-path []
  (let [root (System/getenv "GOOGLEAPIS_REPOS_PATH")]
    (cond
      (string/blank? root)
      (throw (ex-info "GOOGLEAPIS_REPOS_PATH environment variable is not set." {}))

      (not (.isAbsolute (io/file root)))
      (throw (ex-info "GOOGLEAPIS_REPOS_PATH must be an absolute path." {:path root}))

      :else root)))

(def package-repos
  (delay
    (let [root (get-googleapis-repos-path)]
      {:bigquery (str root "/java-bigquery/google-cloud-bigquery/src/main/java")
       :storage  (str root "/java-storage/google-cloud-storage/src/main/java")
       :pubsub   (str root "/java-pubsub/google-cloud-pubsub/src/main/java")
       :vertexai (str root "/google-cloud-java/java-vertexai")})))

(defn parse
  "Analyzes a package specified by keyword (e.g. :bigquery) or path string.
   Returns the package AST."
  [key-or-path]
  (if (keyword? key-or-path)
    (if-let [path (get @package-repos key-or-path)]
      (parser/parse-package path {})
      (throw (ex-info "Unknown package keyword" {:package key-or-path :available (keys @package-repos)})))
    (parser/parse-package key-or-path {})))

(defn- collect-nested-types
  "Recursively collects all nested types (excluding top-level)."
  [nodes]
  (mapcat (fn [node]
            (tree-seq (comp seq :nested) :nested node))
          (mapcat :nested nodes)))

(defn- collect-nested-outliers
  "Recursively collects all nested classes categorized as :other, returning absolute path strings (Parent$Child)."
  [nodes]
  (letfn [(traverse [node parent-path]
            (let [current-name (:name node)
                  current-path (if parent-path
                                 (str parent-path "$" current-name)
                                 current-name)
                  outliers (if (and parent-path (= (:category node) :other))
                             [current-path]
                             [])]
              (reduce (fn [acc child] (into acc (traverse child current-path)))
                      outliers
                      (:nested node))))]
    (mapcat #(traverse % nil) nodes)))

(defn summarize
  "Returns a concise summary of the package analysis result.
   Accepts either a package AST map or a package keyword (e.g. :bigquery)."
  [key-or-path]
  (let [pkg-ast           (parse key-or-path)
        all-classes       (vals (:class/by-fqcn pkg-ast))
        class-count       (count all-classes)
        git-tag           (:git-tag pkg-ast)
        git-sha           (:git-sha pkg-ast)
        clients           (:service-clients pkg-ast)
        categories        (frequencies (map :category all-classes))
        nested-types      (collect-nested-types all-classes)
        nested-categories (frequencies (map :category nested-types))
        nested-outliers   (collect-nested-outliers all-classes)]
    (cond-> {:git-tag           git-tag
             :git-sha           git-sha
             :class-count       class-count
             :clients           clients
             :categories        categories
             :nested-categories nested-categories}
            (seq nested-outliers) (assoc :nested-outliers (sort nested-outliers)))))

(defn lookup-class
  [pkg-like class-like]
  (let [pkg (if (map? pkg-like)
              pkg-like
              (parse pkg-like))]
    (pkg/lookup-class pkg class-like)))

(defn user-types [pkg-like]
  (let [pkg (if (map? pkg-like)
              pkg-like
              (parse pkg-like))]
    (pkg/user-types pkg)))

(defn foreign-user-types [pkg-like]
  (let [pkg (if (map? pkg-like)
              pkg-like
              (parse pkg-like))]
    (pkg/foreign-user-types pkg)))

(defn class-user-types
  ([class-node]
   (pkg/class-user-types class-node))
  ([pkg-like class-like]
   (let [pkg (if (map? pkg-like) pkg-like (parse pkg-like))]
     (pkg/class-user-types pkg class-like))))

(defn class-foreign-user-types [pkg-like class-like]
  (let [pkg (if (map? pkg-like) pkg-like (parse pkg-like))]
    (pkg/class-foreign-user-types pkg class-like)))

(defn class-package-user-types [pkg-like class-like]
  (let [pkg (if (map? pkg-like) pkg-like (parse pkg-like))]
    (pkg/class-package-user-types pkg class-like)))

(defn dependency-seq [pkg-like class-like]
  (let [pkg (if (map? pkg-like) pkg-like (parse pkg-like))]
    (pkg/dependency-seq pkg class-like)))

(defn dependency-tree [pkg-like class-like]
  (let [pkg (if (map? pkg-like) pkg-like (parse pkg-like))]
    (pkg/dependency-tree pkg class-like)))

(defn dependency-post-order [pkg-like class-like]
  (let [pkg (if (map? pkg-like) pkg-like (parse pkg-like))]
    (pkg/dependency-post-order pkg class-like)))
