(ns gcp.dev.packages
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [gcp.dev.packages.package :as pkg]
   [gcp.dev.toolchain.analyzer :as analyzer]
   [gcp.dev.toolchain.parser :as parser]
   [gcp.dev.util :as u]))

(defonce googleapis (u/get-googleapis-repos-path))

#!----------------------------------------------------------------------------------------------------------------------

(def artifact-registry
  {:name 'gcp.artifact-registry
   :type :static
   :root (str googleapis "/google-cloud-java/java-artifact-registry")
   :include ["/google-cloud-artifact-registry/src/main/java/com/google/devtools/artifactregistry/v1"
             "/proto-google-cloud-artifact-registry-v1/src/main/java/com/google/devtools/artifactregistry/v1"]
   :exclude ["/google-cloud-artifact-registry/src/main/java/com/google/devtools/artifactregistry/v1/stub"]
   :native-prefixes #{"com.google.devtools.artifactregistry" "com.google.cloud.artifactregistry"}})

(def monitoring
  {:name 'gcp.monitoring
   :type :static
   :root (str googleapis "/google-cloud-java/java-monitoring")
   :include ["/google-cloud-monitoring/src/main/java/com/google/cloud/monitoring/v3"
             "/proto-google-cloud-monitoring-v3/src/main/java/com/google/monitoring/v3"]
   :exclude ["/google-cloud-monitoring/src/main/java/com/google/cloud/monitoring/v3/stub"]
   :native-prefixes #{"com.google.cloud.monitoring" "com.google.monitoring"}})

(def vertexai
  {:name 'gcp.vertexai
   :type :static
   :root (str googleapis "/google-cloud-java/java-vertexai")
   :include ["/google-cloud-vertexai/src/main/java/com/google/cloud/vertexai/VertexAI.java"
             "/google-cloud-vertexai/src/main/java/com/google/cloud/vertexai/api"
             "/proto-google-cloud-vertexai-v1/src/main/java/com/google/cloud/vertexai/api"]
   :exclude ["/google-cloud-vertexai/src/main/java/com/google/cloud/vertexai/api/stub"]
   :native-prefixes #{"com.google.cloud.vertexai" "com.google.vertexai"}})

(def bigquery
  {:name 'gcp.bigquery
   :type :static
   :root (str googleapis "/java-bigquery")
   :include ["/google-cloud-bigquery/src/main/java/com/google/cloud/bigquery"]
   :exclude ["/google-cloud-bigquery/src/main/java/com/google/cloud/bigquery/spi"
             "/google-cloud-bigquery/src/main/java/com/google/cloud/bigquery/testing"]
   :native-prefixes #{"com.google.cloud.bigquery"}})

(def genai
  {:name 'gcp.genai
   :type :static
   :root (str googleapis "/java-genai")
   :native-prefixes #{"com.google.genai"}
   :include ["/src/main/java/com/google/genai"]})

(def logging
  {:name 'gcp.logging
   :type :static
   :root (str googleapis "/java-logging")
   :native-prefixes #{"com.google.cloud.logging" "com.google.logging"}
   :include ["/google-cloud-logging/src/main/java/com/google/cloud/logging"
             "/google-cloud-logging/src/main/java/com/google/cloud/logging/v2"
             "/proto-google-cloud-logging-v2/src/main/java/com/google/logging/v2"]
   :exclude ["/google-cloud-logging/src/main/java/com/google/cloud/logging/spi"
             "/google-cloud-logging/src/main/java/com/google/cloud/logging/testing"
             "/google-cloud-logging/src/main/java/com/google/cloud/logging/v2/stub"]})

(def pubsub
  {:name 'gcp.pubsub
   :type :static
   :root (str googleapis "/java-pubsub")
   :native-prefixes #{"com.google.cloud.pubsub" "com.google.pubsub"}
   :include ["/google-cloud-pubsub/src/main/java/com/google/cloud/pubsub/v1"]
   :exclude ["/google-cloud-pubsub/src/main/java/com/google/cloud/pubsub/v1/stub"]})

(def storage
  {:name 'gcp.storage
   :type :static
   :root (str googleapis "/java-storage")
   :include ["/google-cloud-storage/src/main/java/com/google/cloud/storage"
             "/google-cloud-storage/src/main/java/com/google/cloud/storage/multipartupload/model"
             "/google-cloud-storage/src/main/java/com/google/cloud/storage/transfermanager"]
   :exclude ["/google-cloud-storage/src/main/java/com/google/cloud/storage/spi"
             "/google-cloud-storage/src/main/java/com/google/cloud/storage/testing"]
   :native-prefixes #{"com.google.cloud.storage"}})

(def storage-control
  {:name 'gcp.storage-control
   :type :static
   :root (str googleapis "/java-storage")
   :include ["/google-cloud-storage-control/src/main/java/com/google/storage/control/v2"
             "/proto-google-cloud-storage-control-v2/src/main/java/com/google/storage/control/v2"]
   :exclude ["/google-cloud-storage-control/src/main/java/com/google/storage/control/v2/stub"]
   :native-prefixes #{"com.google.storage.control"}})

#!----------------------------------------------------------------------------------------------------------------------

(def packages
  {:vertexai vertexai
   :bigquery bigquery
   :pubsub pubsub
   :storage storage
   :storage-control storage-control
   :logging logging
   :genai genai
   :monitoring monitoring
   :artifact-registry artifact-registry})

#!----------------------------------------------------------------------------------------------------------------------

(defn- resolve-files [{:keys [root include exclude]}]
  (let [excludes (map #(io/file root (subs % 1)) exclude)
        includes (map #(io/file root (subs % 1)) include)
        all-files (mapcat (fn [f]
                            (if (.isDirectory f)
                              (filter #(string/ends-with? (.getName %) ".java") (file-seq f))
                              (if (and (.isFile f) (string/ends-with? (.getName f) ".java"))
                                [f]
                                [])))
                          includes)]
    (filter (fn [f]
              (let [abs-path (.getAbsolutePath f)]
                (not-any? (fn [ex]
                            (string/starts-with? abs-path (.getAbsolutePath ex)))
                          excludes)))
            all-files)))

(defn parse
  "Analyzes a package specified by keyword (e.g. :bigquery), static definition map, or path string.
   Returns the parsed package AST with :type :parsed."
  [pkg-like]
  (cond
    (keyword? pkg-like)
    (if-let [pkg-def (get packages pkg-like)]
      (parse pkg-def)
      (throw (ex-info "Unknown package keyword" {:package pkg-like :available (keys packages)})))

    (map? pkg-like)
    (if (= (:type pkg-like) :parsed)
      pkg-like
      (let [files   (resolve-files pkg-like)
            pkg-ast (parser/analyze-package (:root pkg-like) files {})]
        (merge pkg-ast
               (select-keys pkg-like [:name :native-prefixes])
               {:type :parsed})))

    (string? pkg-like)
    (assoc (parser/parse-package pkg-like {}) :type :parsed)

    :else
    (throw (ex-info "Invalid argument to parse" {:arg pkg-like}))))

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
  [pkg-like]
  (let [pkg-ast           (parse pkg-like)
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
  (let [pkg (parse pkg-like)]
    (pkg/lookup-class pkg class-like)))

(defn user-types
  [pkg-like]
  (let [pkg (parse pkg-like)]
    (pkg/user-types pkg)))

(defn package-user-types
  [pkg-like]
  (let [pkg (parse pkg-like)]
    (pkg/package-user-types pkg)))

(defn package-api-types
  "returns sorted list of all binding targets for the given package"
  [pkg-like]
  (let [pkg (parse pkg-like)]
    (sort (keys (:class/by-fqcn pkg)))))

(defn foreign-user-types
  [pkg-like]
  (let [pkg (parse pkg-like)]
    (pkg/foreign-user-types pkg)))

(defn foreign-user-types-by-package
  [pkg-like]
  (let [pkg (parse pkg-like)]
    (pkg/foreign-user-types-by-package pkg)))

(defn class-user-types
  ([class-node]
   (pkg/class-user-types class-node))
  ([pkg-like class-like]
   (let [pkg (parse pkg-like)]
     (pkg/class-user-types pkg class-like))))

(defn class-foreign-user-types [pkg-like class-like]
  (let [pkg (parse pkg-like)]
    (pkg/class-foreign-user-types pkg class-like)))

(defn class-package-user-types [pkg-like class-like]
  (let [pkg (parse pkg-like)]
    (pkg/class-package-user-types pkg class-like)))

(defn dependency-seq [pkg-like class-like]
  (let [pkg (parse pkg-like)]
    (pkg/dependency-seq pkg class-like)))

(defn dependency-tree [pkg-like class-like]
  (let [pkg (parse pkg-like)]
    (pkg/dependency-tree pkg class-like)))

(defn dependency-post-order [pkg-like class-like]
  (let [pkg (parse pkg-like)]
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
  (let [pkg (parse pkg-like)
        ;; We iterate over all defined classes in the package that are considered user types
        ;; (i.e. part of the API surface we care about).
        types (pkg/package-user-types pkg)
        ;; Use native prefixes to filter out internal types
        native-prefixes (or (:native-prefixes pkg) #{(:package-name pkg)})
        is-native? (fn [t] (some #(string/starts-with? (str t) %) native-prefixes))]
    (->> types
         (map #(pkg/lookup-class pkg %))
         (filter some?)
         (map class-deps)
         (mapcat :foreign)
         (remove is-native?)
         (into (sorted-set)))))

(defn global-foreign-deps []
  (reduce into (map package-foreign-deps (keys packages))))