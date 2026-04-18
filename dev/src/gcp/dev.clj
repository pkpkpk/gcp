(ns gcp.dev
  (:refer-clojure :exclude [compile])
  (:require
   [clojure.core.match :refer [match]]
   [clojure.java.io :as io]
   [clojure.repl :refer :all]
   [clojure.string :as string]
   [gcp.dev.compiler :as c]
   [gcp.dev.packages :as p]
   [gcp.dev.toolchain.analyzer :as ana]
   [gcp.dev.toolchain.emitter :as e]
   [gcp.dev.toolchain.fuzz :as fuzz]
   [gcp.dev.toolchain.malli :as m]
   [gcp.dev.toolchain.shared :refer [categorize-type]]
   [gcp.dev.util :as u :refer :all]
   [gcp.global :as g])
  (:import
   (java.io File)))

#_(use :reload 'gcp.dev)
#_(in-ns 'gcp.dev)

(defn fetch-all-upstream
  "Fetches all upstream commits and tags for a package's repository without updating local coordinates.
   Useful for ensuring the local git cache is warm before running summary or patch operations."
  [pkg-like]
  (p/fetch-all-upstream pkg-like))

(defn status
  "Returns a map representing the current synchronization status of the package, including its 
   current release, the latest upstream release, its local manifest, and git worktree status."
  [pkg-like]
  (p/status pkg-like))

(defn needs-sync?
  "Returns true if the package's current manifest version differs from the latest stable release
   available upstream."
  [pkg-like]
  (p/needs-sync? pkg-like))

(defn sync-to-release
  "Synchronizes the local package to the latest upstream release. Updates the internal manifest,
   adjusts coordinates in deps.edn, and fetches the necessary sources into a git worktree."
  [pkg-like]
  (p/sync-to-release pkg-like))

(defn needs-interpretation?
  "Returns true if the summary contains changes to non-superficial files (e.g. source files),
   indicating that semantic analysis of the update is required."
  [pkg-like delta-map]
  (p/needs-interpretation? pkg-like delta-map))

(defn patch
  "Returns a unified diff patch string for the non-superficial files (e.g. .java, .proto) 
   found in the provided summary-map. Useful for semantic analysis by an LLM."
  [pkg-like summary-map]
  (p/patch pkg-like summary-map))

(defn delta
  "Returns a map of commits and all raw file changes between two revisions.
   If only one coordinate is provided, compares current manifest release to it.
   If no coordinates are provided, compares current manifest release to the latest stable release."
  ([pkg-like]
   (p/delta pkg-like))
  ([pkg-like new-rev]
   (p/delta pkg-like new-rev))
  ([pkg-like old-rev new-rev]
   (p/delta pkg-like old-rev new-rev)))

(defn summary
  "Returns a high-level summary of changes by computing the delta and filtering out superficial file changes.
   Useful for identifying meaningful semantic updates."
  ([pkg-like]
   (p/summary pkg-like))
  ([pkg-like new-rev]
   (p/summary pkg-like new-rev))
  ([pkg-like old-rev new-rev]
   (p/summary pkg-like old-rev new-rev)))

(defn lookup
  "retrieve the parsed class node for a given fqcn
   works for nested classes too"
  [fqcn]
  (p/lookup-class fqcn))

(defn deps
  "return all potential types used in a class"
  [fqcn]
  (p/class-deps fqcn))

(defn analyze [fqcn]
  (p/analyze-class fqcn))

(defn schema [fqcn]
  (m/->schema (analyze fqcn)))

(defn compile-to-string [fqcn]
  (c/compile-to-string (analyze fqcn)))

(defn compile-to-file [fqcn]
  (let [node   (analyze fqcn)
        target (p/target-file fqcn)]
    (c/compile-to-file node target)))

(defn target-file [fqcn] (p/target-file fqcn))

(defn certify [arg]
  (let [[fqcn file] (if (instance? File arg)
                      [(p/target-file->fqcn arg) arg]
                      [arg (target-file arg)])
        pkg-key (p/lookup-pkg-key fqcn)]
    (if (contains? (p/packages) pkg-key)
      (let [pkg     (p/parse pkg-key)
            custom? (contains? (:custom-namespace-mappings pkg) (symbol fqcn))]
        (when-not custom?
          (compile-to-file fqcn))
        (fuzz/certify-file file))
      (println "Skipping certification for foreign type:" fqcn))))

(defn certified? [fqcn]
  (let [target (target-file fqcn)]
    (if (not (.exists target))
      false
      (fuzz/certified? target))))

(defn delete [fqcn]
  (let [pkg-key (p/lookup-pkg-key fqcn)]
    (if (contains? (p/packages) pkg-key)
      (let [pkg     (p/parse pkg-key)
            custom? (contains? (:custom-namespace-mappings pkg) (symbol fqcn))]
        (if custom?
          (println "Skipping deletion for custom type:" fqcn)
          (io/delete-file (target-file fqcn) true)))
      (println "Skipping deletion for foreign type:" fqcn))))

(def manifest p/manifest)

(defn clear-cache [] (p/clear-cache))

(defn delete-bindings [pkg-key]
  (p/delete-bindings pkg-key))

(def api-types-by-category p/api-types-by-category)

(defn remaining-by-category [pkg-key]
  (into (sorted-map)
        (map
          (fn [[key fqcns]]
            (let [remaining (remove certified? fqcns)]
              (when (seq remaining)
                [key remaining]))))
        (p/api-types-by-category pkg-key)))

(defn graph [fqcn] (p/require-graph fqcn))

(defn delete-graph [& fqcns]
  (let [order (p/topological-order-many fqcns)]
    (doseq [node order]
      (delete node))))

(defn certify-graph [& fqcns]
  (let [order (p/topological-order-many fqcns)]
    (doseq [node order]
      (try
        (certify node)
        (catch Throwable t
          (println "FAILED certifying " node)
          (throw t))))))

#!------------------------------------------------------------------------

(defn smallest
  ([] (smallest *e))
  ([e]
   (some-> e ex-data :result :shrunk :smallest first)))

#!------------------------------------------------------------------------

(defn clean-method [m]
  (-> (dissoc m :doc :static? :private? :abstract? :beta? :parameter-mappings)
      (update :parameters (fn [ps] (mapv #(dissoc % :varArgs?) ps)))))

(defn can-consolidate?
  [methods]
  (and (apply = (map :name methods))
       (apply = (map :returnType methods))))

(defn consolidate-signatures
  [methods]
  (assert (can-consolidate? methods))
  (let [base       (dissoc (first methods) :parameters)
        signatures (vec (sort-by count < (map :parameters methods)))]
    (assoc base :signatures signatures)))

(defn reduce-types
  [acc t]
  (if (symbol? t)
    (conj acc t)
    (match t
      [(:or
         :array
         'java.util.List
         'java.lang.Iterable
         'com.google.api.gax.paging.Page
         'com.google.api.core.ApiFuture
         'com.google.common.util.concurrent.ListenableFuture) E] (reduce-types acc E)
      ['java.util.Map (K :guard symbol?) (V :guard symbol?)] (conj acc K V)
      [(:or 'com.google.api.gax.longrunning.OperationFuture) A B] (conj acc A B)

      :else (throw (Exception. (str "unmatched shape: " t))))))

(defn client-method-types
  [client-fqcn]
  (let [{:keys [category methods]} (lookup client-fqcn)
        _       (assert (= :client category))
        all     (reduce
                  (fn [acc {:keys [returnType parameters] :as method}]
                    (let [acc (reduce-types acc returnType)]
                      (reduce reduce-types acc (map :type parameters))))
                  #{}
                  (remove #(string/ends-with? (:name %) "Callable") methods))
        {:keys [package]} (u/split-fqcn client-fqcn)
        native  (into (sorted-set) (filter gcp.dev.packages.package/native-types all))
        peer    (into (sorted-set) (filter #(string/starts-with? % package) all))
        foreign (into (sorted-set) (remove (clojure.set/union native peer
                                                              gcp.dev.packages.package/scalars)) all)]
    {:peer    peer
     :foreign foreign
     :native  native}))

(defn user-types [& client-fqcns]
  (let [ms    (map client-method-types client-fqcns)
        {:keys [peer foreign native]} (apply (partial merge-with into) ms)
        peers (into (sorted-set)
                    (map #(if (u/nested-fqcn? %)
                            (symbol (:parent-fqcn (u/split-fqcn %)))
                            %))
                    peer)]
    (p/topological-order-many peers)))

(defn unsafe-errors []
  (into (sorted-map)
        (for [[k {:keys [err]}] (:unsafe (ex-data (ex-cause *e)))]
          [k (ex-data err)])))

#!----------------------------------------------------------------------------------------------------------------------
#! bigquery

(comment
  (delete-bindings :bigquery)

  (client-method-types "com.google.cloud.bigquery.BigQuery")

  (def bq-user-types (user-types "com.google.cloud.bigquery.BigQuery"))

  (certify-graph "com.google.cloud.bigquery.JobInfo"
                 "com.google.cloud.bigquery.TableInfo"
                 "com.google.cloud.bigquery.ModelInfo"
                 "com.google.cloud.bigquery.DatasetInfo"
                 "com.google.cloud.bigquery.RoutineInfo"
                 "com.google.cloud.bigquery.Connection"
                 "com.google.cloud.bigquery.ConnectionSettings"
                 "com.google.cloud.bigquery.InsertAllResponse"
                 "com.google.cloud.bigquery.WriteChannelConfiguration"
                 "com.google.cloud.bigquery.DataFormatOptions" ; TODO this needs to be discoverable;  req'd by BQO (custom) only
                 "com.google.cloud.bigquery.BigQuery")

  (require :reload 'gcp.bigquery.core 'gcp.bigquery.aux '[gcp.bigquery :as bq]))

#!----------------------------------------------------------------------------------------------------------------------
#! storage

(comment
  (delete-bindings :storage)

  (client-method-types "com.google.cloud.storage.Storage")

  (def storage-user-types (user-types "com.google.cloud.storage.Storage"))

  (def storage-by-category (gcp.dev.packages/api-types-by-category :storage))

  (certify-graph "com.google.cloud.storage.BlobInfo"
                 "com.google.cloud.storage.BucketInfo"
                 "com.google.cloud.storage.NotificationInfo") ; <---TODO fix certification

  (require :reload 'gcp.storage.core 'gcp.storage.aux '[gcp.storage :as storage]))

#!----------------------------------------------------------------------------------------------------------------------
#! vertexai

(comment
  (delete-bindings :vertexai)

  "com.google.cloud.vertexai.api.PredictionServiceClient"
  "com.google.cloud.vertexai.api.EndpointServiceClient"
  "com.google.cloud.vertexai.api.LlmUtilityServiceClient"
  "com.google.cloud.vertexai.VertexAI"

  (map clean-method (:methods (lookup "com.google.vertexai.api.PredictionServiceClient")))

  (certify-graph "com.google.cloud.vertexai.api.GenerateContentRequest"
                 "com.google.cloud.vertexai.api.GenerateContentResponse"

                 "com.google.cloud.vertexai.api.PredictRequest"
                 "com.google.cloud.vertexai.api.PredictResponse"
                 "com.google.cloud.vertexai.api.EndpointName"
                 "com.google.cloud.vertexai.api.RawPredictRequest"
                 "com.google.cloud.vertexai.api.DirectPredictResponse"
                 "com.google.cloud.vertexai.api.DirectPredictRequest"

                 "com.google.cloud.vertexai.api.EmbedContentRequest"
                 "com.google.cloud.vertexai.api.EmbedContentResponse"

                 "com.google.cloud.vertexai.api.ExplainRequest"
                 "com.google.cloud.vertexai.api.ExplainResponse"
                 "com.google.cloud.vertexai.api.PredictionServiceClient.ListLocationsPagedResponse"

                 ;;; --- LlmUtilityService --------------------------------
                 "com.google.cloud.vertexai.api.CountTokensRequest"
                 "com.google.cloud.vertexai.api.CountTokensResponse")

  #_ com.google.api.HttpBody
  #_ com.google.cloud.location.GetLocationRequest
  #_ com.google.cloud.location.Location
  #_ com.google.cloud.location.ListLocationsRequest
  #_ com.google.iam.v1.Policy
  #_ com.google.iam.v1.TestIamPermissionsResponse
  #_ com.google.iam.v1.GetIamPolicyRequest
  #_ com.google.iam.v1.TestIamPermissionsRequest)

(comment

  ({:name "predict"
    :returnType com.google.cloud.vertexai.api.PredictResponse
    :parameters [{:name "endpoint", :type com.google.cloud.vertexai.api.EndpointName}
                 {:name "instances", :type [java.util.List com.google.protobuf.Value]}
                 {:name "parameters", :type com.google.protobuf.Value}]}
    {:name "predict"
     :returnType com.google.cloud.vertexai.api.PredictResponse
     :parameters [{:name "endpoint", :type java.lang.String}
                  {:name "instances", :type [java.util.List com.google.protobuf.Value]}
                  {:name "parameters", :type com.google.protobuf.Value}]}
    {:name "predict"
     :returnType com.google.cloud.vertexai.api.PredictResponse
     :parameters [{:name "request", :type com.google.cloud.vertexai.api.PredictRequest}]}

    {:name "rawPredict"
     :returnType com.google.api.HttpBody
     :parameters [{:name "endpoint", :type com.google.cloud.vertexai.api.EndpointName}
                  {:name "httpBody", :type com.google.api.HttpBody}]}
    {:name "rawPredict"
     :returnType com.google.api.HttpBody
     :parameters [{:name "endpoint", :type java.lang.String} {:name "httpBody", :type com.google.api.HttpBody}]}
    {:name "rawPredict"
     :returnType com.google.api.HttpBody
     :parameters [{:name "request", :type com.google.cloud.vertexai.api.RawPredictRequest}]}

    {:name "directPredict"
     :returnType com.google.cloud.vertexai.api.DirectPredictResponse
     :parameters [{:name "request", :type com.google.cloud.vertexai.api.DirectPredictRequest}]}
    {:name "directRawPredict"
     :returnType com.google.cloud.vertexai.api.DirectRawPredictResponse
     :parameters [{:name "request", :type com.google.cloud.vertexai.api.DirectRawPredictRequest}]}

    {:name "explain"
     :returnType com.google.cloud.vertexai.api.ExplainResponse
     :parameters [{:name "endpoint", :type com.google.cloud.vertexai.api.EndpointName}
                  {:name "instances", :type [java.util.List com.google.protobuf.Value]}
                  {:name "parameters", :type com.google.protobuf.Value}
                  {:name "deployedModelId", :type java.lang.String}]}
    {:name "explain"
     :returnType com.google.cloud.vertexai.api.ExplainResponse
     :parameters [{:name "endpoint", :type java.lang.String}
                  {:name "instances", :type [java.util.List com.google.protobuf.Value]}
                  {:name "parameters", :type com.google.protobuf.Value}
                  {:name "deployedModelId", :type java.lang.String}]}
    {:name "explain"
     :returnType com.google.cloud.vertexai.api.ExplainResponse
     :parameters [{:name "request", :type com.google.cloud.vertexai.api.ExplainRequest}]}

    {:name "embedContent"
     :returnType com.google.cloud.vertexai.api.EmbedContentResponse
     :parameters [{:name "model", :type com.google.cloud.vertexai.api.EndpointName}
                  {:name "content", :type com.google.cloud.vertexai.api.Content}]}
    {:name "embedContent"
     :returnType com.google.cloud.vertexai.api.EmbedContentResponse
     :parameters [{:name "model", :type java.lang.String} {:name "content", :type com.google.cloud.vertexai.api.Content}]}
    {:name "embedContent"
     :returnType com.google.cloud.vertexai.api.EmbedContentResponse
     :parameters [{:name "request", :type com.google.cloud.vertexai.api.EmbedContentRequest}]}

    {:name "listLocations"
     :returnType com.google.cloud.vertexai.api.PredictionServiceClient.ListLocationsPagedResponse
     :parameters [{:name "request", :type com.google.cloud.location.ListLocationsRequest}]}
    {:name "getLocation"
     :returnType com.google.cloud.location.Location
     :parameters [{:name "request", :type com.google.cloud.location.GetLocationRequest}]}

    {:name "setIamPolicy"
     :returnType com.google.iam.v1.Policy
     :parameters [{:name "request", :type com.google.iam.v1.SetIamPolicyRequest}]}
    {:name "getIamPolicy"
     :returnType com.google.iam.v1.Policy
     :parameters [{:name "request", :type com.google.iam.v1.GetIamPolicyRequest}]}
    {:name "testIamPermissions"
     :returnType com.google.iam.v1.TestIamPermissionsResponse
     :parameters [{:name "request", :type com.google.iam.v1.TestIamPermissionsRequest}]}

    {:name "create"
     :throws [java.io.IOException]
     :returnType com.google.cloud.vertexai.api.PredictionServiceClient
     :parameters []}
    {:name "create"
     :throws [java.io.IOException]
     :field-name "settings"
     :returnType com.google.cloud.vertexai.api.PredictionServiceClient
     :parameters [{:name "settings", :type com.google.cloud.vertexai.api.PredictionServiceSettings}]}
    {:name "create"
     :field-name "settings"
     :returnType com.google.cloud.vertexai.api.PredictionServiceClient
     :parameters [{:name "stub", :type com.google.cloud.vertexai.api.stub.PredictionServiceStub}]}
    {:name "getSettings"
     :returnType com.google.cloud.vertexai.api.PredictionServiceSettings
     :parameters []
     :field-name "settings"}
    {:name "getStub"
     :returnType com.google.cloud.vertexai.api.stub.PredictionServiceStub
     :parameters []
     :field-name "stub"}
    {:name "close", :returnType void, :parameters [], :field-name "stub"}
    {:name "shutdown", :returnType void, :parameters [], :field-name "stub"}
    {:name "isShutdown", :returnType boolean, :parameters [], :field-name "stub"}
    {:name "isTerminated", :returnType boolean, :parameters [], :field-name "stub"}
    {:name "shutdownNow", :returnType void, :parameters [], :field-name "stub"}
    {:name "awaitTermination"
     :returnType boolean
     :parameters [{:name "duration", :type long} {:name "unit", :type java.util.concurrent.TimeUnit}]
     :throws [com.google.cloud.vertexai.api.InterruptedException]}))

#!----------------------------------------------------------------------------------------------------------------------

;; TODO
;; doc discovery! gen example, repair
;; sugar single args for accessor with builder (JobId has no arg newBuilder but also .of(job) etc)
;; *strict* mode disable, investigate malli instrumentation
;; errors thrown! check ast! bq.query()
;; IAM, sandbox, client
;; doc strings
;; examples
;; fixture dataset
;; empty labels+resourceTags map checks before assoc :labels {} etc

;; inline nested/peer enums... see ConnectionSettings requiring custom.QueryJobConfiguration Priority/to-edn+from-edn etc

;; make java.nio.ByteBuffer global type ::ByteBuffer w/ :gen/return etc

(comment

  clojure.core/isa?
  clojure.core/parents
  clojure.core/bases
  clojure.core/supers
  clojure.core/ancestors
  clojure.core/descendants
  clojure.core/derive

  (def bq-user-types (user-types "com.google.cloud.bigquery.BigQuery"))
  (def storage-user-types (user-types "com.google.cloud.storage.Storage"))

  (def vertexai (p/parse :vertexai))
  (def vertexai-user-types (user-types "com.google.cloud.vertexai.api.PredictionServiceClient"
                                       #_ "com.google.cloud.vertexai.api.EndpointServiceClient"
                                       #_ "com.google.cloud.vertexai.api.LlmUtilityServiceClient"))

  (def pubsub (p/parse :pubsub))
  (def pubsub-user-types (user-types  "com.google.cloud.pubsub.v1.TopicAdminClient"
                                      "com.google.cloud.pubsub.v1.SubscriptionAdminClient"))

  (p/clear-cache)
  (def bq (gcp.dev.packages/api-types-by-category :bigquery))
  (def bq (remaining-by-category :bigquery))
  (def global-by-category (p/global-api-types-by-category))
  (get global-by-category :static-factory))
