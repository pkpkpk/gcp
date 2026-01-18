(ns gcp.dev.packages
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as string]
   [gcp.dev.packages.maven :as mvn]
   [gcp.dev.packages.package :as pkg]
   [gcp.dev.toolchain.analyzer :as analyzer]
   [gcp.dev.toolchain.parser :as parser]
   [gcp.dev.toolchain.shared :as shared]
   [gcp.dev.util :as u]))

(defonce googleapis (u/get-googleapis-repos-path))
(defonce packages-root (io/file (u/get-gcp-repo-root) "packages"))

(def global
  {:name         'gcp.global
   :lib          'com.github.pkpkpk/gcp.global
   :description  "global malli registry & utilities for gcp bindings"
   :package-root (io/file packages-root "global")
   :src-root     (io/file packages-root "global" "src")})

(def foreign
  {:name         'gcp.foreign
   :lib          'com.github.pkpkpk/gcp.foreign
   :description  "bindings for transitive foreign types using in gcp bindings"
   :package-root (io/file packages-root "foreign")
   :src-root     (io/file packages-root "foreign" "src")
   :mappings     {"com.google.cloud.MonitoredResource" 'gcp.foreign.com.google.cloud
                  "com.google.cloud.MonitoredResourceDescriptor" 'gcp.foreign.com.google.cloud
                  "com.google.cloud.RetryOption" 'gcp.foreign.com.google.cloud
                  "com.google.longrunning.Operation" 'gcp.foreign.com.google.longrunning
                  "com.google.longrunning.stub.OperationsStub" 'gcp.foreign.com.google.longrunning.stub
                  "com.google.longrunning.stub.GrpcOperationsStub" 'gcp.foreign.com.google.longrunning.stub
                  "com.google.cloud.location.Location" 'gcp.foreign.com.google.cloud.location
                  "com.google.cloud.location.GetLocationRequest" 'gcp.foreign.com.google.cloud.location
                  "com.google.cloud.location.ListLocationsRequest" 'gcp.foreign.com.google.cloud.location
                  "com.google.cloud.location.ListLocationsResponse" 'gcp.foreign.com.google.cloud.location
                  "com.google.cloud.spi.ServiceRpcFactory" 'gcp.foreign.com.google.cloud.spi
                  "com.google.type.Date" 'gcp.foreign.com.google.type
                  "com.google.type.LatLng" 'gcp.foreign.com.google.type
                  "com.google.common.util.concurrent.ListenableFuture" 'gcp.foreign.com.google.common.util.concurrent
                  "com.google.common.collect.ImmutableList" 'gcp.foreign.com.google.common.collect
                  "com.google.api.LabelDescriptor" 'gcp.foreign.com.google.api
                  "com.google.api.MonitoredResource" 'gcp.foreign.com.google.api
                  "com.google.api.MonitoredResourceDescriptor" 'gcp.foreign.com.google.api
                  "com.google.api.HttpBody" 'gcp.foreign.com.google.api
                  "com.google.api.resourcenames.ResourceName" 'gcp.foreign.com.google.api.resourcenames
                  "com.google.api.services.bigquery.model.UserDefinedFunctionResource" 'gcp.foreign.com.google.api.services.bigquery.model
                  "com.google.api.services.bigquery.model.QueryParameter" 'gcp.foreign.com.google.api.services.bigquery.model
                  "com.google.api.core.ApiFuture" 'gcp.foreign.com.google.api.core
                  "com.google.api.core.ApiService" 'gcp.foreign.com.google.api.core
                  "com.google.api.core.AbstractApiService" 'gcp.foreign.com.google.api.core
                  "com.google.api.gax.batching.FlowControlSettings" 'gcp.foreign.com.google.api.gax.batching
                  "com.google.api.gax.batching.BatchingSettings" 'gcp.foreign.com.google.api.gax.batching
                  "com.google.api.gax.paging.Page" 'gcp.foreign.com.google.api.gax.paging
                  "com.google.api.gax.paging.AsyncPage" 'gcp.foreign.com.google.api.gax.paging
                  "com.google.api.gax.longrunning.OperationFuture" 'gcp.foreign.com.google.api.gax.longrunning
                  "com.google.api.gax.retrying.RetrySettings" 'gcp.foreign.com.google.api.gax.retrying
                  "com.google.api.gax.retrying.ResultRetryAlgorithm" 'gcp.foreign.com.google.api.gax.retrying
                  "com.google.api.gax.rpc.BidiStream" 'gcp.foreign.com.google.api.gax.rpc
                  "com.google.api.gax.rpc.ClientContext" 'gcp.foreign.com.google.api.gax.rpc
                  "com.google.api.gax.grpc.GrpcStubCallableFactory" 'gcp.foreign.com.google.api.gax.grpc
                  "com.google.api.gax.httpjson.longrunning.OperationsClient" 'gcp.foreign.com.google.api.gax.httpjson.longrunning
                  "com.google.api.gax.httpjson.longrunning.stub.OperationsStub" 'gcp.foreign.com.google.api.gax.httpjson.longrunning.stub
                  "com.google.api.gax.httpjson.longrunning.stub.HttpJsonOperationsStub" 'gcp.foreign.com.google.api.gax.httpjson.longrunning.stub
                  "com.google.api.gax.core.BackgroundResource" 'gcp.foreign.com.google.api.gax.core
                  "com.google.api.gax.httpjson.HttpJsonStubCallableFactory" 'gcp.foreign.com.google.api.gax.httpjson
                  "com.google.rpc.Status" 'gcp.foreign.com.google.rpc
                  "com.google.iam.v1.Policy" 'gcp.foreign.com.google.iam.v1
                  "com.google.iam.v1.GetIamPolicyRequest" 'gcp.foreign.com.google.iam.v1
                  "com.google.iam.v1.SetIamPolicyRequest" 'gcp.foreign.com.google.iam.v1
                  "com.google.iam.v1.TestIamPermissionsRequest" 'gcp.foreign.com.google.iam.v1
                  "com.google.iam.v1.TestIamPermissionsResponse" 'gcp.foreign.com.google.iam.v1
                  "com.google.auth.Credentials" 'gcp.foreign.com.google.auth
                  "com.google.protobuf.Timestamp" 'gcp.foreign.com.google.protobuf
                  "com.google.protobuf.ByteString" 'gcp.foreign.com.google.protobuf
                  "com.google.protobuf.Any" 'gcp.foreign.com.google.protobuf
                  "com.google.protobuf.Struct" 'gcp.foreign.com.google.protobuf
                  "com.google.protobuf.Value" 'gcp.foreign.com.google.protobuf
                  "com.google.protobuf.ProtocolStringList" 'gcp.foreign.com.google.protobuf
                  "com.google.protobuf.Duration" 'gcp.foreign.com.google.protobuf
                  "org.threeten.extra.PeriodDuration" 'gcp.foreign.org.threeten.extra
                  "org.threeten.bp.Duration" 'gcp.foreign.org.threeten.bp
                  "java.util.concurrent.CompletableFuture" 'gcp.foreign.java.util.concurrent
                  "java.util.concurrent.TimeUnit" 'gcp.foreign.java.util.concurrent
                  "java.util.logging.Logger" 'gcp.foreign.java.util.logging
                  "java.util.logging.Level" 'gcp.foreign.java.util.logging
                  "java.util.logging.Handler" 'gcp.foreign.java.util.logging
                  "java.util.logging.LogRecord" 'gcp.foreign.java.util.logging
                  "java.util.function.Consumer" 'gcp.foreign.java.util.function
                  "java.util.OptionalLong" 'gcp.foreign.java.util
                  "java.net.URL" 'gcp.foreign.java.net
                  "java.io.InputStream" 'gcp.foreign.java.io
                  "java.io.OutputStream" 'gcp.foreign.java.io
                  "java.io.Closeable" 'gcp.foreign.java.io
                  "java.io.Serializable" 'gcp.foreign.java.io
                  "java.io.ObjectInputStream" 'gcp.foreign.java.io
                  "java.sql.SQLException" 'gcp.foreign.java.sql
                  "java.time.Duration" 'gcp.foreign.java.time
                  "java.time.OffsetDateTime" 'gcp.foreign.java.time
                  "java.nio.ByteBuffer" 'gcp.foreign.java.nio
                  "java.nio.channels.ScatteringByteChannel" 'gcp.foreign.java.nio.channels
                  "java.nio.channels.SeekableByteChannel" 'gcp.foreign.java.nio.channels
                  "java.nio.channels.WritableByteChannel" 'gcp.foreign.java.nio.channels
                  "java.nio.file.Path" 'gcp.foreign.java.nio.file
                  "io.opentelemetry.api.OpenTelemetry" 'gcp.foreign.io.opentelemetry.api
                  "io.opentelemetry.api.common.Attributes" 'gcp.foreign.io.opentelemetry.api.common
                  "io.opentelemetry.api.trace.Tracer" 'gcp.foreign.io.opentelemetry.api.trace}})

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
  {:name                      'gcp.bigquery
   :lib                       'com.github.pkpkpk/gcp.bigquery
   :description               "edn bindings for the google-cloud-bigquery sdk"
   :package-root              (io/file packages-root "bigquery")
   :type                      :static
   :googleapis/mvn-org        "com.google.cloud"
   :googleapis/mvn-artifact   "google-cloud-bigquery"
   :googleapis/git-repo       "java-bigquery"
   :api-roots                 ["com.google.cloud.bigquery.BigQuery"
                               "com.google.cloud.bigquery.BigQueryOptions"
                               "com.google.cloud.bigquery.JobStatistics"]
   :custom-namespace-mappings {"com.google.cloud.bigquery.QueryParameterValue"   'gcp.bigquery.custom.QueryParameterValue
                               "com.google.cloud.bigquery.TableResult"           'gcp.bigquery.custom.TableResult
                               "com.google.cloud.bigquery.FieldValue"            'gcp.bigquery.custom.TableResult
                               "com.google.cloud.bigquery.FieldValueList"        'gcp.bigquery.custom.TableResult
                               "com.google.cloud.bigquery.Range"                 'gcp.bigquery.custom.TableResult
                               "com.google.cloud.bigquery.Dataset"               'gcp.bigquery.custom.Dataset
                               "com.google.cloud.bigquery.Job"                   'gcp.bigquery.custom.Job
                               "com.google.cloud.bigquery.JobStatistics"         'gcp.bigquery.custom.JobStatistics
                               "com.google.cloud.bigquery.Model"                 'gcp.bigquery.custom.Model
                               "com.google.cloud.bigquery.Routine"               'gcp.bigquery.custom.Routine
                               "com.google.cloud.bigquery.Table"                 'gcp.bigquery.custom.Table
                               "com.google.cloud.bigquery.StandardSQLDataType"   'gcp.bigquery.custom.StandardSQL
                               "com.google.cloud.bigquery.StandardSQLStructType" 'gcp.bigquery.custom.StandardSQL
                               "com.google.cloud.bigquery.StandardSQLField"      'gcp.bigquery.custom.StandardSQL
                               "com.google.cloud.bigquery.StandardSQLTypeName"   'gcp.bigquery.custom.StandardSQL}
   :opaque-types              #{"com.google.cloud.bigquery.Option"}
   :exempt-types              #{"com.google.cloud.bigquery.TableDataWriteChannel"}
   :prune-dependencies        {"com.google.cloud.bigquery.Field"                 #{"com.google.cloud.bigquery.FieldList"}
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
   :include                   ["/google-cloud-bigquery/src/main/java/com/google/cloud/bigquery"]
   :exclude                   ["/google-cloud-bigquery/src/main/java/com/google/cloud/bigquery/spi"
                               "/google-cloud-bigquery/src/main/java/com/google/cloud/bigquery/testing"]
   :native-prefixes           #{"com.google.cloud.bigquery"}})

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
   :exclude                 ["/google-cloud-storage/src/main/java/com/google/cloud/storage/ZeroCopySupport.java"
                             "/google-cloud-storage/src/main/java/com/google/cloud/storage/spi"
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

(defn clear-cache [] (parser/clear-cache))

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

(defn- resolve-package-files
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
            opaque-types (:opaque-types pkg-like)
            ;; Inject configuration into class nodes
            updated-classes (reduce-kv (fn [acc fqcn node]
                                         (assoc acc fqcn
                                                (cond-> node
                                                  (contains? prune-deps fqcn)
                                                  (assoc :prune-dependencies (get prune-deps fqcn))
                                                  opaque-types
                                                  (assoc :opaque-types opaque-types))))
                                       (:class/by-fqcn pkg-ast)
                                       (:class/by-fqcn pkg-ast))
            forwarded-keys [:name
                            :native-prefixes
                            :prune-dependencies
                            :custom-namespace-mappings
                            :exempt-types
                            :opaque-types]]
        (merge pkg-ast
               (select-keys pkg-like forwarded-keys)
               {:type :parsed
                :class/by-fqcn updated-classes})))

    (string? pkg-like)
    (assoc (parser/parse-package pkg-like {}) :type :parsed)

    :else
    (throw (ex-info "Invalid argument to parse" {:arg pkg-like}))))

(defn lookup-class
  [pkg-like class-like]
  (let [pkg (parse pkg-like)]
    (when-let [node (pkg/lookup-class pkg class-like)]
      (into (sorted-map) node))))

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
  (let [{:keys [custom-namespace-mappings exempt-types] :as pkg} (parse pkg-like)
        pred (into (or exempt-types #{}) (keys custom-namespace-mappings))]
    (sort (remove pred (keys (:class/by-fqcn pkg))))))

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
  (let [{:keys [custom-namespace-mappings exempt-types] :as pkg} (parse pkg-like)]
    (if-let [{:keys [fqcn] :as class-node} (lookup-class pkg class-like)]
      (if (contains? custom-namespace-mappings fqcn)
        (throw (Exception. (str "Class " fqcn " is listed as custom override and is exempt from analysis")))
        (if (contains? exempt-types fqcn)
          (throw (Exception. (str "Class " fqcn " is listed as exempt-type and is exempt from analysis")))
          (analyzer/analyze-class-node (assoc class-node :foreign-mappings (get foreign :mappings)))))
      (throw (ex-info (str "failed to find node for class-like '" class-like "'")
                      {:pkg-like pkg-like :class-like class-like})))))

(defn transitive-closure
  [pkg-like roots]
  (pkg/transitive-closure (parse pkg-like) roots))

(defn topological-sort
  [pkg-like nodes]
  (pkg/topological-sort (parse pkg-like) nodes))

(defn class-deps
  "Analyzes the dependencies of a node and returns a map separating them into
   :internal (same package/service) and :foreign (external, e.g. java.*, protobuf)."
  ([pkg-like class-like]
   (class-deps pkg-like class-like false))
  ([pkg-like class-like recursive?]
   (pkg/class-deps (parse pkg-like) class-like recursive?)))

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

(defn api-types-for-category
  [pkg-like target-category]
  (assert (contains? shared/categories target-category))
  (let [pkg (parse pkg-like)]
    (reduce
      (fn [acc fqcn]
        (let [{:keys [category]} (analyze-class pkg fqcn)]
          (if (= target-category category)
            (conj acc fqcn)
            acc)))
      (sorted-set)
      (package-api-types pkg))))

(defn api-types-by-category [pkg-like]
  (let [pkg (parse pkg-like)]
    (reduce
      (fn [acc fqcn]
        (let [{:keys [category]} (analyze-class pkg fqcn)]
          (assert (= category (:category (lookup-class pkg fqcn))))
          (if (contains? acc category)
            (update-in acc [category] conj fqcn)
            (assoc acc category (sorted-set fqcn)))))
      (sorted-map)
      (package-api-types pkg))))

(defn global-api-types-by-category []
  (reduce
    (partial merge-with into)
    (map api-types-by-category (keys packages))))
