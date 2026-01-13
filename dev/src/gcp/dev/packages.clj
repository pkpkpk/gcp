(ns gcp.dev.packages
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as string]
   [gcp.dev.packages.maven :as mvn]
   [gcp.dev.packages.package :as pkg]
   [gcp.dev.toolchain.analyzer :as analyzer]
   [gcp.dev.toolchain.parser :as parser]
   [gcp.dev.util :as u]))

(defonce googleapis (u/get-googleapis-repos-path))
(defonce packages-root (io/file (u/get-gcp-repo-root) "packages"))

#!----------------------------------------------------------------------------------------------------------------------

;; TODO entry API class(es)
;;  -- recursive API class deps? post-order traversal

(def global
  {:name         'gcp.global
   :lib          'com.github.pkpkpk/gcp.global
   :description  "global malli registry & utilities for gcp bindings"
   :package-root (io/file packages-root "global")
   :src-root     (io/file packages-root "global" "src")})

(def artifact-registry
  {:name                     'gcp.artifact-registry
   :lib                      'com.github.pkpkpk/gcp.artifact-registry
   :description              "edn bindings for the google-cloud-artifact-registry sdk"
   :package-root             (io/file packages-root "artifact-registry")
   :type                     :static
   :googleapis/mvn-org       "com.google.cloud"
   :googleapis/mvn-artifact  "google-cloud-artifact-registry"
   :googleapis/git-repo      "google-cloud-java"
   :googleapis/git-repo-root "java-artifact-registry"
   ; :pinned-version           "1.4.0"
   :api-roots                ["com.google.devtools.artifactregistry.v1.ArtifactRegistryClient"
                              "com.google.devtools.artifactregistry.v1.ArtifactRegistrySettings"]
   :include                  ["/google-cloud-artifact-registry/src/main/java/com/google/devtools/artifactregistry/v1"
                              "/proto-google-cloud-artifact-registry-v1/src/main/java/com/google/devtools/artifactregistry/v1"]
   :exclude                  ["/google-cloud-artifact-registry/src/main/java/com/google/devtools/artifactregistry/v1/stub"]
   :native-prefixes          #{"com.google.devtools.artifactregistry" "com.google.cloud.artifactregistry"}})

(def monitoring
  {:name                     'gcp.monitoring
   :lib                      'com.github.pkpkpk/gcp.monitoring
   :description              "edn bindings for the google-cloud-monitoring sdk"
   :package-root             (io/file packages-root "monitoring")
   :type                     :static
   :googleapis/mvn-org       "com.google.cloud"
   :googleapis/mvn-artifact  "google-cloud-monitoring"
   :googleapis/git-repo      "google-cloud-java"
   :googleapis/git-repo-root "java-monitoring"
   :api-roots                ["com.google.cloud.monitoring.v3.AlertPolicyServiceClient"
                              "com.google.cloud.monitoring.v3.AlertPolicyServiceSettings"
                              "com.google.cloud.monitoring.v3.GroupServiceClient"
                              "com.google.cloud.monitoring.v3.GroupServiceSettings"
                              "com.google.cloud.monitoring.v3.MetricServiceClient"
                              "com.google.cloud.monitoring.v3.MetricServiceSettings"
                              "com.google.cloud.monitoring.v3.NotificationChannelServiceClient"
                              "com.google.cloud.monitoring.v3.NotificationChannelServiceSettings"
                              "com.google.cloud.monitoring.v3.QueryServiceClient"
                              "com.google.cloud.monitoring.v3.QueryServiceSettings"
                              "com.google.cloud.monitoring.v3.ServiceMonitoringServiceClient"
                              "com.google.cloud.monitoring.v3.ServiceMonitoringServiceSettings"
                              "com.google.cloud.monitoring.v3.SnoozeServiceClient"
                              "com.google.cloud.monitoring.v3.SnoozeServiceSettings"
                              "com.google.cloud.monitoring.v3.UptimeCheckServiceClient"
                              "com.google.cloud.monitoring.v3.UptimeCheckServiceSettings"]
   :include                  ["/google-cloud-monitoring/src/main/java/com/google/cloud/monitoring/v3"
                              "/proto-google-cloud-monitoring-v3/src/main/java/com/google/monitoring/v3"]
   :exclude                  ["/google-cloud-monitoring/src/main/java/com/google/cloud/monitoring/v3/stub"]
   :native-prefixes          #{"com.google.cloud.monitoring" "com.google.monitoring"}})

(def vertexai
  {:name                     'gcp.vertexai
   :lib                      'com.github.pkpkpk/gcp.vertexai
   :description              "edn bindings for the google-cloud-vertexai sdk"
   :package-root             (io/file packages-root "vertexai")
   :type                     :static
   :googleapis/mvn-org       "com.google.cloud"
   :googleapis/mvn-artifact  "google-cloud-vertexai"
   :googleapis/git-repo      "google-cloud-java"
   :googleapis/git-repo-root "java-vertexai"
   :api-roots                ["com.google.cloud.vertexai.VertexAI"
                              "com.google.cloud.vertexai.api.LlmUtilityServiceSettings"
                              "com.google.cloud.vertexai.api.PredictionServiceSettings"
                              "com.google.cloud.vertexai.api.EndpointServiceSettings"]
   :include                  ["/google-cloud-vertexai/src/main/java/com/google/cloud/vertexai/VertexAI.java"
                              "/google-cloud-vertexai/src/main/java/com/google/cloud/vertexai/api"
                              "/proto-google-cloud-vertexai-v1/src/main/java/com/google/cloud/vertexai/api"]
   :exclude                  ["/google-cloud-vertexai/src/main/java/com/google/cloud/vertexai/api/stub"]
   :native-prefixes          #{"com.google.cloud.vertexai" "com.google.vertexai"}})

(def bigquery
  {:name                    'gcp.bigquery
   :lib                     'com.github.pkpkpk/gcp.bigquery
   :description             "edn bindings for the google-cloud-bigquery sdk"
   :package-root            (io/file packages-root "bigquery")
   :type                    :static
   :googleapis/mvn-org      "com.google.cloud"
   :googleapis/mvn-artifact "google-cloud-bigquery"
   :googleapis/git-repo     "java-bigquery"
   :api-roots               ["com.google.cloud.bigquery.BigQuery"
                             "com.google.cloud.bigquery.BigQueryOptions"
                             "com.google.cloud.bigquery.JobStatistics"]
   :custom                  {"com.google.cloud.bigquery.QueryParameterValue"   'gcp.bigquery.custom.QueryParameterValue
                             "com.google.cloud.bigquery.TableResult"           'gcp.bigquery.custom.TableResult
                             "com.google.cloud.bigquery.FieldValue"            'gcp.bigquery.custom.TableResult
                             "com.google.cloud.bigquery.FieldValueList"        'gcp.bigquery.custom.TableResult
                             "com.google.cloud.bigquery.Range"                 'gcp.bigquery.custom.TableResult
                             "com.google.cloud.bigquery.Dataset"               'gcp.bigquery.custom.Dataset
                             "com.google.cloud.bigquery.Job"                   'gcp.bigquery.custom.Job
                             "com.google.cloud.bigquery.Model"                 'gcp.bigquery.custom.Model
                             "com.google.cloud.bigquery.Routine"               'gcp.bigquery.custom.Routine
                             "com.google.cloud.bigquery.Table"                 'gcp.bigquery.custom.Table
                             "com.google.cloud.bigquery.StandardSQLDataType"   'gcp.bigquery.custom.StandardSQL
                             "com.google.cloud.bigquery.StandardSQLStructType" 'gcp.bigquery.custom.StandardSQL
                             "com.google.cloud.bigquery.StandardSQLField"      'gcp.bigquery.custom.StandardSQL
                             "com.google.cloud.bigquery.StandardSQLTypeName"   'gcp.bigquery.custom.StandardSQL}
   :opaque                  #{"com.google.cloud.bigquery.Option"}
   :prune-dependencies      {"com.google.cloud.bigquery.Field"                 #{"com.google.cloud.bigquery.FieldList"}
                             "com.google.cloud.bigquery.FieldValue"            #{"com.google.cloud.bigquery.FieldValueList"}
                             "com.google.cloud.bigquery.Range"                 #{"com.google.cloud.bigquery.FieldValueList"
                                                                                 "com.google.cloud.bigquery.FieldValue"}
                             "com.google.cloud.bigquery.Dataset"               #{"com.google.cloud.bigquery.BigQuery"}
                             "com.google.cloud.bigquery.Job"                   #{"com.google.cloud.bigquery.BigQuery"}
                             "com.google.cloud.bigquery.Model"                 #{"com.google.cloud.bigquery.BigQuery"}
                             "com.google.cloud.bigquery.Routine"               #{"com.google.cloud.bigquery.BigQuery"}
                             "com.google.cloud.bigquery.Table"                 #{"com.google.cloud.bigquery.BigQuery"}
                             "com.google.cloud.bigquery.StandardSQLDataType"   #{"com.google.cloud.bigquery.StandardSQLStructType"
                                                                                 "com.google.cloud.bigquery.StandardSQLField"}
                             "com.google.cloud.bigquery.StandardSQLStructType" #{"com.google.cloud.bigquery.StandardSQLDataType"
                                                                                 "com.google.cloud.bigquery.StandardSQLField"}
                             "com.google.cloud.bigquery.StandardSQLField"      #{"com.google.cloud.bigquery.StandardSQLDataType"
                                                                                 "com.google.cloud.bigquery.StandardSQLStructType"}
                             "com.google.cloud.bigquery.UserDefinedFunction"   #{"com.google.api.services.bigquery.model.UserDefinedFunctionResource"}}
   :include                 ["/google-cloud-bigquery/src/main/java/com/google/cloud/bigquery"]
   :exclude                 ["/google-cloud-bigquery/src/main/java/com/google/cloud/bigquery/spi"
                             "/google-cloud-bigquery/src/main/java/com/google/cloud/bigquery/testing"]
   :native-prefixes         #{"com.google.cloud.bigquery"}})

(def genai
  {:name                    'gcp.genai
   :lib                     'com.github.pkpkpk/gcp.genai
   :description             "edn bindings for the google-genai sdk"
   :package-root            (io/file packages-root "genai")
   :type                    :static
   :googleapis/mvn-org      "com.google.genai"
   :googleapis/mvn-artifact "google-genai"
   :googleapis/git-repo     "java-genai"
   :api-roots               ["com.google.genai.Client"]
   :native-prefixes         #{"com.google.genai"}
   :include                 ["/src/main/java/com/google/genai"]})

(def logging
  {:name                    'gcp.logging
   :lib                     'com.github.pkpkpk/gcp.logging
   :description             "edn bindings for the google-cloud-logging sdk"
   :package-root            (io/file packages-root "logging")
   :type                    :static
   :googleapis/mvn-org      "com.google.cloud"
   :googleapis/mvn-artifact "google-cloud-logging"
   :googleapis/git-repo     "java-logging"
   :api-roots               ["com.google.cloud.logging.Logging"
                             "com.google.cloud.logging.LoggingOptions"]
   :native-prefixes         #{"com.google.cloud.logging" "com.google.logging"}
   :include                 ["/google-cloud-logging/src/main/java/com/google/cloud/logging"
                             "/google-cloud-logging/src/main/java/com/google/cloud/logging/v2"
                             "/proto-google-cloud-logging-v2/src/main/java/com/google/logging/v2"]
   :exclude                 ["/google-cloud-logging/src/main/java/com/google/cloud/logging/spi"
                             "/google-cloud-logging/src/main/java/com/google/cloud/logging/testing"
                             "/google-cloud-logging/src/main/java/com/google/cloud/logging/v2/stub"]})

(def pubsub
  {:name                    'gcp.pubsub
   :lib                     'com.github.pkpkpk/gcp.pubsub
   :description             "edn bindings for the google-cloud-pubsub sdk"
   :package-root            (io/file packages-root "pubsub")
   :type                    :static
   :googleapis/mvn-org      "com.google.cloud"
   :googleapis/mvn-artifact "google-cloud-pubsub"
   :googleapis/git-repo     "java-pubsub"
   :api-roots               ["com.google.cloud.pubsub.v1.TopicAdminClient"
                             "com.google.cloud.pubsub.v1.TopicAdminSettings"
                             "com.google.cloud.pubsub.v1.SubscriptionAdminClient"
                             "com.google.cloud.pubsub.v1.SubscriptionAdminSettings"
                             "com.google.cloud.pubsub.v1.SchemaServiceClient"
                             "com.google.cloud.pubsub.v1.SchemaServiceSettings"
                             "com.google.cloud.pubsub.v1.Subscriber"
                             "com.google.cloud.pubsub.v1.Publisher"]
   :native-prefixes         #{"com.google.cloud.pubsub" "com.google.pubsub"}
   :include                 ["/google-cloud-pubsub/src/main/java/com/google/cloud/pubsub/v1"]
   :exclude                 ["/google-cloud-pubsub/src/main/java/com/google/cloud/pubsub/v1/stub"]})

(def storage
  {:name                    'gcp.storage
   :lib                     'com.github.pkpkpk/gcp.storage
   :description             "edn bindings for the google-cloud-storage sdk"
   :package-root            (io/file packages-root "storage")
   :type                    :static
   :googleapis/mvn-org      "com.google.cloud"
   :googleapis/mvn-artifact "google-cloud-storage"
   :googleapis/git-repo     "java-storage"
   :api-roots               ["com.google.cloud.storage.Storage"
                             "com.google.cloud.storage.StorageOptions"]
   :include                 ["/google-cloud-storage/src/main/java/com/google/cloud/storage"
                             "/google-cloud-storage/src/main/java/com/google/cloud/storage/multipartupload/model"
                             "/google-cloud-storage/src/main/java/com/google/cloud/storage/transfermanager"]
   :exclude                 ["/google-cloud-storage/src/main/java/com/google/cloud/storage/spi"
                             "/google-cloud-storage/src/main/java/com/google/cloud/storage/testing"]
   :native-prefixes         #{"com.google.cloud.storage"}})

(def storage-control
  {:name                    'gcp.storage-control
   :lib                     'com.github.pkpkpk/gcp.storage-control
   :description             "edn bindings for the google-cloud-storage-control sdk"
   :package-root            (io/file packages-root "storage-control")
   :type                    :static
   :googleapis/mvn-org      "com.google.cloud"
   :googleapis/mvn-artifact "google-cloud-storage-control"
   :googleapis/git-repo     "java-storage"
   :api-roots               ["com.google.storage.control.v2.StorageControlClient"
                             "com.google.storage.control.v2.StorageControlSettings"]
   :include                 ["/google-cloud-storage-control/src/main/java/com/google/storage/control/v2"
                             "/proto-google-cloud-storage-control-v2/src/main/java/com/google/storage/control/v2"]
   :exclude                 ["/google-cloud-storage-control/src/main/java/com/google/storage/control/v2/stub"]
   :native-prefixes         #{"com.google.storage.control"}})

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

(defn latest-release [arg]
  (if (keyword? arg)
    (latest-release (get packages arg))
    (let [{:keys [googleapis/mvn-org googleapis/mvn-artifact]} arg]
      (mvn/latest-release mvn-org mvn-artifact))))

#!----------------------------------------------------------------------------------------------------------------------

(defn manifest-path
  "Returns the path to the manifest.edn file for a given package."
  [pkg-like]
  (let [pkg-def (if (keyword? pkg-like) (get packages pkg-like) pkg-like)]
    (io/file (:package-root pkg-def) "manifest.edn")))

(defn load-manifest
  "Loads the manifest.edn for a package, returning nil if it doesn't exist."
  [pkg-like]
  (let [f (manifest-path pkg-like)]
    (when (.exists f)
      (edn/read-string (slurp f)))))

(defn save-manifest
  "Saves the given manifest map to the package's manifest.edn file."
  [pkg-like manifest]
  (let [f (manifest-path pkg-like)]
    (io/make-parents f)
    (spit f (with-out-str (clojure.pprint/pprint manifest)))))

#!----------------------------------------------------------------------------------------------------------------------

(defn package-root [pkg-like]
  (let [{:keys [googleapis/git-repo googleapis/git-repo-root]} (if (keyword? pkg-like) (get packages pkg-like) pkg-like)]
    (if-let [override (:override-root pkg-like)]
      (if git-repo-root
        (io/file override git-repo-root)
        override)
      (if (keyword? pkg-like)
        (package-root (get packages pkg-like))
        (if git-repo-root
          (io/file googleapis git-repo git-repo-root)
          (io/file googleapis git-repo))))))

(defn resolve-package-files
  [{:keys [include exclude] :as pkg}]
  (let [root      (package-root pkg)
        excludes  (map #(io/file root (subs % 1)) exclude)
        includes  (map #(io/file root (subs % 1)) include)
        all-files (mapcat (fn [f]
                            (let [exists? (.exists f)]
                              (if (.isDirectory f)
                                (filter #(string/ends-with? (.getName %) ".java") (file-seq f))
                                (if (and (.isFile f) (string/ends-with? (.getName f) ".java"))
                                  [f]
                                  []))))
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
      (let [files   (resolve-package-files pkg-like)
            pkg-ast (parser/analyze-package (package-root pkg-like) files {})
            prune-deps (:prune-dependencies pkg-like)
            opaque-types (:opaque pkg-like)
            ;; Inject configuration into class nodes
            updated-classes (reduce-kv (fn [acc fqcn node]
                                         (assoc acc fqcn
                                                (cond-> node
                                                  (contains? prune-deps fqcn)
                                                  (assoc :prune-dependencies (get prune-deps fqcn))
                                                  opaque-types
                                                  (assoc :opaque-types opaque-types))))
                                       (:class/by-fqcn pkg-ast)
                                       (:class/by-fqcn pkg-ast))]
        (merge pkg-ast
               (select-keys pkg-like [:name :native-prefixes :prune-dependencies])
               {:type :parsed
                :class/by-fqcn updated-classes})))

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

(defn dependency-post-order [pkg-like class-like]
  (let [pkg (parse pkg-like)]
    (pkg/dependency-post-order pkg class-like)))

(defn analyze-class
  [pkg-like class-like]
  (let [class-node (lookup-class pkg-like class-like)]
    (analyzer/analyze-class-node class-node)))

(defn transitive-closure [pkg-like roots]
  (let [pkg (parse pkg-like)]
    (pkg/transitive-closure pkg roots)))

(defn topological-sort [pkg-like nodes]
  (let [pkg (parse pkg-like)]
    (pkg/topological-sort pkg nodes)))

#!----------------------------------------------------------------------------------------------------------------------

(defn- normalize-type [type-name]
  (let [s (str type-name)
        parts (string/split s #"[.$]")]
    (if-let [idx (first (keep-indexed (fn [i part]
                                        (when (and (seq part)
                                                   (^[char] Character/isUpperCase (first part)))
                                          i))
                                      parts))]
      (string/join "." (subvec parts 0 (inc idx)))
      s)))

(defn class-deps
  "Analyzes the dependencies of a node and returns a map separating them into
   :internal (same package/service) and :foreign (external, e.g. java.*, protobuf)."
  ([pkg-like class-like]
   (class-deps pkg-like class-like false))
  ([pkg-like class-like recursive?]
   (let [pkg             (parse pkg-like)
         native-prefixes (or (:native-prefixes pkg)
                             (let [package (:package-name pkg)]
                               (if (and package (string/starts-with? package "com.google.cloud."))
                                 #{(let [parts (string/split package #"\.")]
                                     (string/join "." (take 5 parts)))}
                                 (if package #{package} #{}))))
         is-internal?    (fn [dep]
                           (let [dep-str (str dep)]
                             (some #(string/starts-with? dep-str %) native-prefixes)))
         calc-deps       (fn [node-or-name]
                           (let [node (if (map? node-or-name)
                                        node-or-name
                                        (lookup-class pkg node-or-name))]
                             (if node
                               (let [analyzed (analyzer/analyze-class-node node)
                                     deps     (:typeDependencies analyzed)]
                                 (reduce (fn [acc dep]
                                           (let [norm-dep (normalize-type dep)
                                                 sym (symbol norm-dep)]
                                             (if (is-internal? norm-dep)
                                               (update acc :internal conj sym)
                                               (update acc :foreign conj sym))))
                                         {:internal (sorted-set) :foreign (sorted-set)}
                                         deps))
                               {:internal (sorted-set) :foreign (sorted-set)})))]
     (if-not recursive?
       (calc-deps class-like)
       (let [all-internal (dependency-seq pkg class-like)]
         (reduce (fn [acc fqcn]
                   (let [deps (calc-deps fqcn)]
                     (-> acc
                         (update :internal into (:internal deps))
                         (update :foreign into (:foreign deps)))))
                 {:internal (sorted-set) :foreign (sorted-set)}
                 all-internal))))))

(defn class-foreign-deps
  ([pkg-like class-like]
   (class-foreign-deps pkg-like class-like false))
  ([pkg-like class-like recursive?]
   (get (class-deps pkg-like class-like recursive?) :foreign)))

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
         (map #(class-deps pkg %))
         (mapcat :foreign)
         (remove is-native?)
         (into (sorted-set)))))

(defn global-foreign-deps []
  (reduce into (map package-foreign-deps (keys packages))))
