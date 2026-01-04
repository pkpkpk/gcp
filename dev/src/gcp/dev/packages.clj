(ns gcp.dev.packages
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [gcp.dev.packages.package :as pkg]
   [gcp.dev.toolchain.analyzer :as analyzer]
   [gcp.dev.toolchain.parser :as parser]
   [gcp.dev.util :as u]))

(def package-repos
  (delay
    (let [root (u/get-googleapis-repos-path)]
      {:bigquery         (str root "/java-bigquery/google-cloud-bigquery/src/main/java")
       :storage          (str root "/java-storage/google-cloud-storage/src/main/java")
       :pubsub           (str root "/java-pubsub/google-cloud-pubsub/src/main/java")
       :vertexai         (str root "/google-cloud-java/java-vertexai")
       :monitoring       (str root "/google-cloud-java/java-monitoring/google-cloud-monitoring/src/main/java")
       :logging          (str root "/java-logging/google-cloud-logging/src/main/java")
       :genai            (str root "/java-genai/src/main/java")
       :iam              (str root "/google-cloud-java/java-iam/google-iam-policy/src/main/java")
       :artifactregistry (str root "/google-cloud-java/java-artifact-registry/google-cloud-artifact-registry/src/main/java")})))

(def package-native-prefixes
  {:pubsub           #{"com.google.cloud.pubsub" "com.google.pubsub"}
   :logging          #{"com.google.cloud.logging" "com.google.logging"}
   :monitoring       #{"com.google.cloud.monitoring" "com.google.monitoring"}
   :genai            #{"com.google.genai"}
   :iam              #{"com.google.cloud.iam" "com.google.iam"}
   :artifactregistry #{"com.google.devtools.artifactregistry" "com.google.cloud.artifactregistry"}})

(defn parse
  "Analyzes a package specified by keyword (e.g. :bigquery) or path string.
   Returns the package AST."
  [key-or-path]
  (if (keyword? key-or-path)
    (if-let [path (get @package-repos key-or-path)]
      (let [pkg-ast (parser/parse-package path {})]
        (if-let [prefixes (get package-native-prefixes key-or-path)]
          (assoc pkg-ast :native-prefixes prefixes)
          pkg-ast))
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

(defn package-user-types [pkg-like]
  (let [pkg (if (map? pkg-like)
              pkg-like
              (parse pkg-like))]
    (pkg/package-user-types pkg)))

(defn foreign-user-types [pkg-like]
  (let [pkg (if (map? pkg-like)
              pkg-like
              (parse pkg-like))]
    (pkg/foreign-user-types pkg)))

(defn foreign-user-types-by-package [pkg-like]
  (let [pkg (if (map? pkg-like)
              pkg-like
              (parse pkg-like))]
    (pkg/foreign-user-types-by-package pkg)))

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

(defn analyze-class
  [pkg-like class-like]
  (let [class-node (lookup-class pkg-like class-like)]
    (analyzer/analyze-class-node class-node)))

#!----------------------------------------------------------------------------------------------------------------------

(defn class-deps
  "Analyzes the dependencies of a node and returns a map separating them into
   :internal (same package/service) and :foreign (external, e.g. java.*, protobuf)."
  ([pkg-like class-like]
   (class-deps (lookup-class pkg-like class-like)))
  ([node]
   (let [analyzed    (analyzer/analyze-class-node node)
         deps        (:typeDependencies analyzed)
         package     (:package node)
         ;; Heuristic: Internal if starts with same package prefix (up to service level)
         ;; e.g. com.google.cloud.vertexai
         service-pkg (if (string/starts-with? package "com.google.cloud.")
                       (let [parts (string/split package #"\.")]
                         (string/join "." (take 5 parts)))  ;; com.google.cloud.service.vX
                       package)]
     (reduce (fn [acc dep]
               (let [dep-str (str dep)]
                 (if (string/starts-with? dep-str service-pkg)
                   (update acc :internal conj dep)
                   (update acc :foreign conj dep))))
             {:internal (sorted-set) :foreign (sorted-set)}
             deps))))

(defn class-foreign-deps
  ([pkg-like class-like]
   (class-foreign-deps (lookup-class pkg-like class-like)))
  ([node]
   (:foreign (class-deps node))))

(defn package-foreign-deps
  [pkg-like]
  ;; for all package-user-types, analyze the type dependents relevant to bindings, and collect and merge the foreign types
  (let [pkg (if (map? pkg-like) pkg-like (parse pkg-like))
        ;; We iterate over all defined classes in the package that are considered user types
        ;; (i.e. part of the API surface we care about).
        types (package-user-types pkg)
        ;; Use native prefixes to filter out internal types
        native-prefixes (or (:native-prefixes pkg) #{(:package-name pkg)})
        is-native? (fn [t] (some #(string/starts-with? (str t) %) native-prefixes))]
    (->> types
         (map #(lookup-class pkg %))
         (filter some?)
         (map class-deps)
         (mapcat :foreign)
         (remove is-native?)
         (into (sorted-set)))))

(defn global-foreign-deps []
  (reduce into (map package-foreign-deps (keys @package-repos))))