(ns gcp.dev.packages.definitions
  (:require
   [clojure.java.io :as io]
   [gcp.dev.packages.layout :as layout]))

(def global
  {:name             'gcp.global
   :lib              'com.github.pkpkpk/gcp.global
   :description      "global malli registry & utilities for gcp bindings"
   :package-prefixes #{"gcp.global"}
   :package-root     (io/file layout/packages-root "global")
   :src-root         (io/file layout/packages-root "global" "src")
   :state-root       (io/file layout/state-root "global")})

(def foreign
  {:name             'gcp.foreign
   :description      "bindings for transitive foreign types using in gcp bindings"
   :package-prefixes #{"com.google.protobuf" "com.google.type" "com.google.rpc" "com.google.iam" "com.google.auth" "com.google.api" "com.google.cloud" "com.google.gson" "io.opentelemetry" "org.threeten"}
   :package-root     (io/file layout/packages-root "global")
   :src-root         (io/file layout/packages-root "global" "src")
   :mappings     '{com.google.cloud.MonitoredResource                                  gcp.foreign.com.google.cloud
                   com.google.cloud.MonitoredResourceDescriptor                        gcp.foreign.com.google.cloud
                   com.google.cloud.Policy                                             gcp.foreign.com.google.cloud
                   com.google.cloud.RetryOption                                        gcp.foreign.com.google.cloud
                   com.google.longrunning.Operation                                    gcp.foreign.com.google.longrunning
                   com.google.longrunning.stub.OperationsStub                          gcp.foreign.com.google.longrunning.stub
                   com.google.longrunning.stub.GrpcOperationsStub                      gcp.foreign.com.google.longrunning.stub
                   com.google.cloud.FieldSelector                                      gcp.foreign.com.google.cloud
                   com.google.cloud.ReadChannel                                        gcp.foreign.com.google.cloud
                   com.google.cloud.Restorable                                         gcp.foreign.com.google.cloud
                   com.google.cloud.RestorableState                                    gcp.foreign.com.google.cloud
                   com.google.cloud.Role                                               gcp.foreign.com.google.cloud
                   com.google.cloud.ServiceFactory                                     gcp.foreign.com.google.cloud
                   com.google.cloud.ServiceOptions                                     gcp.foreign.com.google.cloud
                   com.google.cloud.ServiceRpc                                         gcp.foreign.com.google.cloud
                   com.google.cloud.StringEnumType                                     gcp.foreign.com.google.cloud
                   com.google.cloud.StringEnumValue                                    gcp.foreign.com.google.cloud
                   com.google.cloud.TransportOptions                                   gcp.foreign.com.google.cloud
                   com.google.cloud.Tuple                                              gcp.foreign.com.google.cloud
                   com.google.cloud.WriteChannel                                       gcp.foreign.com.google.cloud
                   com.google.cloud.location.Location                                  gcp.foreign.com.google.cloud.location
                   com.google.cloud.location.GetLocationRequest                        gcp.foreign.com.google.cloud.location
                   com.google.cloud.location.ListLocationsRequest                      gcp.foreign.com.google.cloud.location
                   com.google.cloud.location.ListLocationsResponse                     gcp.foreign.com.google.cloud.location
                   com.google.cloud.spi.ServiceRpcFactory                              gcp.foreign.com.google.cloud.spi
                   com.google.gson.JsonObject                                          gcp.foreign.com.google.gson
                   com.google.type.Date                                                gcp.foreign.com.google.type
                   com.google.type.LatLng                                              gcp.foreign.com.google.type
                   com.google.common.util.concurrent.ListenableFuture                  gcp.foreign.com.google.common.util.concurrent
                   com.google.common.collect.ImmutableList                             gcp.foreign.com.google.common.collect
                   com.google.api.LabelDescriptor                                      gcp.foreign.com.google.api
                   com.google.api.MonitoredResource                                    gcp.foreign.com.google.api
                   com.google.api.MonitoredResourceDescriptor                          gcp.foreign.com.google.api
                   com.google.api.HttpBody                                             gcp.foreign.com.google.api
                   com.google.api.resourcenames.ResourceName                           gcp.foreign.com.google.api.resourcenames
                   com.google.api.core.ApiFuture                                       gcp.foreign.com.google.api.core
                   com.google.api.core.ApiService                                      gcp.foreign.com.google.api.core
                   com.google.api.core.AbstractApiService                              gcp.foreign.com.google.api.core
                   com.google.api.gax.batching.FlowControlSettings                     gcp.foreign.com.google.api.gax.batching
                   com.google.api.gax.batching.BatchingSettings                        gcp.foreign.com.google.api.gax.batching
                   com.google.api.gax.paging.Page                                      gcp.foreign.com.google.api.gax.paging
                   com.google.api.gax.paging.AsyncPage                                 gcp.foreign.com.google.api.gax.paging
                   com.google.api.gax.longrunning.OperationFuture                      gcp.foreign.com.google.api.gax.longrunning
                   com.google.api.gax.retrying.RetrySettings                           gcp.foreign.com.google.api.gax.retrying
                   com.google.api.gax.retrying.ResultRetryAlgorithm                    gcp.foreign.com.google.api.gax.retrying
                   com.google.api.gax.rpc.BidiStream                                   gcp.foreign.com.google.api.gax.rpc
                   com.google.api.gax.rpc.ClientContext                                gcp.foreign.com.google.api.gax.rpc
                   com.google.api.gax.grpc.GrpcStubCallableFactory                     gcp.foreign.com.google.api.gax.grpc
                   com.google.api.gax.httpjson.longrunning.OperationsClient            gcp.foreign.com.google.api.gax.httpjson.longrunning
                   com.google.api.gax.httpjson.longrunning.stub.OperationsStub         gcp.foreign.com.google.api.gax.httpjson.longrunning.stub
                   com.google.api.gax.httpjson.longrunning.stub.HttpJsonOperationsStub gcp.foreign.com.google.api.gax.httpjson.longrunning.stub
                   com.google.api.gax.core.BackgroundResource                          gcp.foreign.com.google.api.gax.core
                   com.google.api.gax.httpjson.HttpJsonStubCallableFactory             gcp.foreign.com.google.api.gax.httpjson
                   com.google.api.gax.rpc.ApiClientHeaderProvider                      gcp.foreign.com.google.api.gax.rpc
                   com.google.api.gax.rpc.ApiException                                 gcp.foreign.com.google.api.gax.rpc
                   com.google.api.gax.rpc.BatchingCallSettings                         gcp.foreign.com.google.api.gax.rpc
                   com.google.api.gax.rpc.ClientSettings                               gcp.foreign.com.google.api.gax.rpc
                   com.google.api.gax.rpc.HeaderProvider                               gcp.foreign.com.google.api.gax.rpc
                   com.google.api.gax.rpc.OperationCallSettings                        gcp.foreign.com.google.api.gax.rpc
                   com.google.api.gax.rpc.PageContext                                  gcp.foreign.com.google.api.gax.rpc
                   com.google.api.gax.rpc.PagedCallSettings                            gcp.foreign.com.google.api.gax.rpc
                   com.google.api.gax.rpc.ServerStreamingCallSettings                  gcp.foreign.com.google.api.gax.rpc
                   com.google.api.gax.rpc.StreamingCallSettings                        gcp.foreign.com.google.api.gax.rpc
                   com.google.api.gax.rpc.TransportChannelProvider                     gcp.foreign.com.google.api.gax.rpc
                   com.google.api.gax.rpc.UnaryCallSettings                            gcp.foreign.com.google.api.gax.rpc
                   com.google.rpc.Status                                               gcp.foreign.com.google.rpc
                   com.google.iam.v1.Policy                                            gcp.foreign.com.google.iam.v1
                   com.google.iam.v1.GetIamPolicyRequest                               gcp.foreign.com.google.iam.v1
                   com.google.iam.v1.SetIamPolicyRequest                               gcp.foreign.com.google.iam.v1
                   com.google.iam.v1.TestIamPermissionsRequest                         gcp.foreign.com.google.iam.v1
                   com.google.iam.v1.TestIamPermissionsResponse                        gcp.foreign.com.google.iam.v1

                   com.google.auth.Credentials                                         gcp.foreign.com.google.auth
                   com.google.auth.ServiceAccountSigner                                gcp.foreign.com.google.auth

                   com.google.protobuf.Timestamp                                       gcp.foreign.com.google.protobuf
                   com.google.protobuf.ByteString                                      gcp.foreign.com.google.protobuf
                   com.google.protobuf.Any                                             gcp.foreign.com.google.protobuf
                   com.google.protobuf.NullValue                                       gcp.foreign.com.google.protobuf
                   com.google.protobuf.Struct                                          gcp.foreign.com.google.protobuf
                   com.google.protobuf.Value                                           gcp.foreign.com.google.protobuf
                   com.google.protobuf.ProtocolStringList                              gcp.foreign.com.google.protobuf
                   com.google.protobuf.Duration                                        gcp.foreign.com.google.protobuf
                   com.google.protobuf.BoolValue                                       gcp.foreign.com.google.protobuf
                   com.google.protobuf.Empty                                           gcp.foreign.com.google.protobuf
                   com.google.protobuf.LazyStringArrayList                             gcp.foreign.com.google.protobuf
                   com.google.protobuf.ListValue                                       gcp.foreign.com.google.protobuf

                   io.opentelemetry.api.OpenTelemetry                                  gcp.foreign.io.opentelemetry.api
                   io.opentelemetry.api.common.Attributes                              gcp.foreign.io.opentelemetry.api.common
                   io.opentelemetry.api.trace.Tracer                                   gcp.foreign.io.opentelemetry.api.trace

                   org.threeten.extra.PeriodDuration                                   gcp.foreign.org.threeten.extra
                   org.threeten.bp.Duration                                            gcp.foreign.org.threeten.bp}})

#!----------------------------------------------------------------------------------------------------------------------
#! gcp.bigquery

(def bigquery
  {:name                      'gcp.bigquery
   :lib                       'com.github.pkpkpk/gcp.bigquery
   :description               "edn bindings for the google-cloud-bigquery sdk"
   :package-root              (io/file layout/packages-root "bigquery")
   :bindings-target-root      (io/file layout/packages-root "bigquery/src/bindings")
   :state-root                (io/file layout/state-root "bigquery")
   :type                      :static
   :discovery-url             "https://bigquery.googleapis.com/$discovery/rest?version=v2"
   :googleapis/mvn-org        "com.google.cloud"
   :googleapis/mvn-artifact   "google-cloud-bigquery"
   :googleapis/git-repo       "java-bigquery"
   :api-roots                 ["com.google.cloud.bigquery.BigQuery"
                               "com.google.cloud.bigquery.BigQueryOptions"
                               "com.google.cloud.bigquery.JobStatistics"]
   :custom-namespace-mappings '{com.google.cloud.bigquery.QueryParameterValue                      gcp.bigquery.custom
                                com.google.cloud.bigquery.TableResult                              gcp.bigquery.custom
                                com.google.cloud.bigquery.Field                                    gcp.bigquery.custom
                                com.google.cloud.bigquery.FieldElementType                         gcp.bigquery.custom
                                com.google.cloud.bigquery.FieldValue                               gcp.bigquery.custom
                                com.google.cloud.bigquery.Range                                    gcp.bigquery.custom
                                com.google.cloud.bigquery.InsertAllRequest                         gcp.bigquery.custom
                                com.google.cloud.bigquery.BigQueryException                        gcp.bigquery.custom.BigQueryException
                                com.google.cloud.bigquery.BigQuerySQLException                     gcp.bigquery.custom.BigQuerySQLException
                                com.google.cloud.bigquery.JobException                             gcp.bigquery.custom.JobException
                                com.google.cloud.bigquery.QueryJobConfiguration                    gcp.bigquery.custom.QueryJobConfiguration
                                com.google.cloud.bigquery.Dataset                                  gcp.bigquery.custom.Dataset
                                com.google.cloud.bigquery.Job                                      gcp.bigquery.custom.Job
                                com.google.cloud.bigquery.JobStatistics                            gcp.bigquery.custom.JobStatistics
                                com.google.cloud.bigquery.Model                                    gcp.bigquery.custom.Model
                                com.google.cloud.bigquery.Routine                                  gcp.bigquery.custom.Routine
                                com.google.cloud.bigquery.Table                                    gcp.bigquery.custom.Table
                                com.google.cloud.bigquery.StandardSQLDataType                      gcp.bigquery.custom.StandardSQL
                                com.google.cloud.bigquery.StandardSQLStructType                    gcp.bigquery.custom.StandardSQL
                                com.google.cloud.bigquery.StandardSQLField                         gcp.bigquery.custom.StandardSQL
                                com.google.cloud.bigquery.StandardSQLTypeName                      gcp.bigquery.custom.StandardSQL
                                com.google.cloud.bigquery.StandardSQLTableType                     gcp.bigquery.custom.StandardSQL
                                com.google.cloud.bigquery.BigQueryRetryConfig                      gcp.bigquery.custom.BigQueryRetryConfig
                                com.google.cloud.bigquery.BigQueryOptions                          gcp.bigquery.custom.BigQueryOptions}
   :support-packages          [:bigquery-services]
   :exempt-types              #{"com.google.cloud.bigquery.FieldValueList"
                                "com.google.cloud.bigquery.TableDataWriteChannel"
                                "com.google.cloud.bigquery.BigQueryBaseService"
                                "com.google.cloud.bigquery.BigQueryRetryAlgorithm"
                                "com.google.cloud.bigquery.BigQueryRetryHelper"
                                "com.google.cloud.bigquery.BigQueryFactory"
                                "com.google.cloud.bigquery.BigQueryResultImpl"
                                "com.google.cloud.bigquery.BigQueryResultStatsImpl"
                                "com.google.cloud.bigquery.BigQueryDryRunResultImpl"
                                "com.google.cloud.bigquery.LoadConfiguration"
                                "com.google.cloud.bigquery.LegacySQLTypeName"}
   :include                   ["/google-cloud-bigquery/src/main/java/com/google/cloud/bigquery"]
   :exclude                   ["/google-cloud-bigquery/src/main/java/com/google/cloud/bigquery/spi"
                               "/google-cloud-bigquery/src/main/java/com/google/cloud/bigquery/testing"
                               "google-cloud-bigquery-jdbc"]
   :package-prefixes          #{"com.google.cloud.bigquery"}})

(def bigquery-services
  {:name                      'gcp.bigquery-services
   :description               "edn bindings for the google-api-services-bigquery model"
   :package-root              (io/file layout/packages-root "bigquery-services")
   :bindings-target-root      (io/file layout/packages-root "bigquery/src/bindings")
   :state-root                (io/file layout/state-root "bigquery-services")
   :type                      :static
   :googleapis/mvn-org        "com.google.apis"
   :googleapis/mvn-artifact   "google-api-services-bigquery"
   :googleapis/git-repo       "google-api-java-client-services"
   ;; This repository does not use git tags for releases; pin to main to allow sync-to-release to function.
   :custom-namespace-mappings '{com.google.api.services.bigquery.model.QueryParameterValue gcp.api.services.bigquery.custom
                                com.google.api.services.bigquery.model.RangeValue gcp.api.services.bigquery.custom}
   :pinned-tag                "main"
   :include                   ["/clients/google-api-services-bigquery/v2/2.0.0"]
   :package-prefixes          #{"com.google.api.services.bigquery"}})

#!----------------------------------------------------------------------------------------------------------------------
#! gcp.storage

(def storage
  {:name                    'gcp.storage
   :lib                     'com.github.pkpkpk/gcp.storage
   :description             "edn bindings for the google-cloud-storage sdk"
   :package-root            (io/file layout/packages-root "storage")
   :bindings-target-root    (io/file layout/packages-root "storage/src/bindings")
   :state-root              (io/file layout/state-root "storage")
   :type                    :static
   :discovery-url           "https://storage.googleapis.com/$discovery/rest?version=v1"
   :googleapis/mvn-org      "com.google.cloud"
   :googleapis/mvn-artifact "google-cloud-storage"
   :googleapis/git-repo     "java-storage"
   :support-packages        [:storage-services]
   :api-roots               ["com.google.cloud.storage.Storage"]
   :include                 ["/google-cloud-storage/src/main/java/com/google/cloud/storage"
                             "/google-cloud-storage/src/main/java/com/google/cloud/storage/multipartupload/model"
                             "/google-cloud-storage/src/main/java/com/google/cloud/storage/transfermanager"]
   :custom-namespace-mappings '{com.google.cloud.storage.PostPolicyV4 gcp.storage.custom.PostPolicyV4
                                com.google.cloud.storage.Cors gcp.storage.custom.Cors
                                com.google.cloud.storage.Blob gcp.storage.custom.Blob
                                com.google.cloud.storage.Bucket gcp.storage.custom.Bucket
                                com.google.cloud.storage.StorageOptions gcp.storage.custom.StorageOptions}
   :exempt-types            #{"com.google.cloud.storage.ApiFutureUtils"
                              "com.google.cloud.storage.Backoff"
                              "com.google.cloud.storage.BlobInfo.ImmutableEmptyMap"
                              "com.google.cloud.storage.BidiWriteCtx"
                              "com.google.cloud.storage.BufferedReadableByteChannelSession"
                              "com.google.cloud.storage.BufferedWritableByteChannelSession"
                              "com.google.cloud.storage.ChannelSession"
                              "com.google.cloud.storage.OpenTelemetryBootstrappingUtils"
                              "com.google.cloud.storage.StorageV2ProtoUtils"
                              "com.google.cloud.storage.WriteCtx"
                              "com.google.cloud.storage.UnbufferedWritableByteChannelSession"
                              "com.google.cloud.storage.UnbufferedReadableByteChannelSession"
                              "com.google.cloud.storage.Conversions"
                              "com.google.cloud.storage.RecoveryFileManager"
                              "com.google.cloud.storage.HttpContentRange"
                              "com.google.cloud.storage.Retrying"
                              "com.google.cloud.storage.ThroughputSink"
                              "com.google.cloud.storage.UnifiedOpts"
                              "com.google.cloud.storage.multipartupload.model.ListMultipartUploadsResponse.CommonPrefixHelper"
                              "com.google.cloud.storage.ParallelCompositeUploadBlobWriteSessionConfig.PartMetadataFieldDecoratorInstance"}
   :exclude                 ["/google-cloud-storage/src/main/java/com/google/cloud/storage/ZeroCopySupport.java"
                             "/google-cloud-storage/src/main/java/com/google/cloud/storage/spi"
                             "/google-cloud-storage/src/main/java/com/google/cloud/storage/testing"]
   :package-prefixes         #{"com.google.cloud.storage"}})

(def storage-services
  {:name                      'gcp.storage-services
   :lib                       'com.github.pkpkpk/gcp.storage-services
   :description               "edn bindings for the google-api-services-storage model"
   :package-root              (io/file layout/packages-root "storage-services")
   :bindings-target-root      (io/file layout/packages-root "storage/src/bindings")
   :state-root                (io/file layout/state-root "storage-services")
   :type                      :static
   :googleapis/mvn-org        "com.google.apis"
   :googleapis/mvn-artifact   "google-api-services-storage"
   :googleapis/git-repo       "google-api-java-client-services"
   ;; This repository does not use git tags for releases; pin to main to allow sync-to-release to function.
   :pinned-tag                "main"
   :include                   ["/clients/google-api-services-storage/v2/2.0.0"]
   :package-prefixes          #{"com.google.api.services.storage"}})

#!----------------------------------------------------------------------------------------------------------------------
#! gcp.pubsub

(def pubsub
  {:name                    'gcp.pubsub
   :lib                     'com.github.pkpkpk/gcp.pubsub
   :description             "edn bindings for the google-cloud-pubsub sdk"
   :package-root            (io/file layout/packages-root "pubsub")
   :bindings-target-root    (io/file layout/packages-root "pubsub/src/bindings")
   :state-root              (io/file layout/state-root "pubsub")
   :type                    :static
   :discovery-url           "https://pubsub.googleapis.com/$discovery/rest?version=v1"
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
   :package-prefixes         #{"com.google.cloud.pubsub" "com.google.pubsub" "com.google.pubsub.v1"}
   :support-packages        [:pubsub-services]
   :include                 ["/google-cloud-pubsub/src/main/java/com/google/cloud/pubsub/v1"
                             "/proto-google-cloud-pubsub-v1/src/main/java/com/google/pubsub/v1"]
   :exclude                 ["/google-cloud-pubsub/src/main/java/com/google/cloud/pubsub/v1/stub"]})

(def pubsub-services
  {:name                      'gcp.pubsub-services
   :lib                       'com.github.pkpkpk/gcp.pubsub-services
   :description               "edn bindings for the google-api-services-pubsub model"
   :package-root              (io/file layout/packages-root "pubsub-services")
   :bindings-target-root      (io/file layout/packages-root "pubsub/src/bindings")
   :state-root                (io/file layout/state-root "pubsub-services")
   :type                      :static
   :googleapis/mvn-org        "com.google.apis"
   :googleapis/mvn-artifact   "google-api-services-pubsub"
   :googleapis/git-repo       "google-api-java-client-services"
   :pinned-tag                "main"
   :include                   ["/clients/google-api-services-pubsub/v1/2.0.0"]
   :package-prefixes          #{"com.google.api.services.pubsub"}})

#!----------------------------------------------------------------------------------------------------------------------
#! gcp.vertexai

(def vertexai
  {:name                    'gcp.vertexai
   :lib                     'com.github.pkpkpk/gcp.vertexai
   :description             "edn bindings for the google-cloud-vertexai sdk"
   :package-root            (io/file layout/packages-root "vertexai")
   :bindings-target-root    (io/file layout/packages-root "vertexai/src/bindings")
   :state-root              (io/file layout/state-root "vertexai")
   :type                    :static
   :discovery-url           "https://aiplatform.googleapis.com/$discovery/rest?version=v1"
   :googleapis/mvn-org      "com.google.cloud"
   :googleapis/mvn-artifact "google-cloud-vertexai"
   :googleapis/git-repo     "java-vertexai"
   :api-roots               ["com.google.cloud.vertexai.VertexAI"
                             "com.google.cloud.vertexai.api.LlmUtilityServiceSettings"
                             "com.google.cloud.vertexai.api.PredictionServiceSettings"
                             "com.google.cloud.vertexai.api.EndpointServiceSettings"
                             "com.google.cloud.vertexai.genai.Client"]
   :include                 ["/google-cloud-vertexai/src/main/java/com/google/cloud/vertexai/VertexAI.java"
                             "/google-cloud-vertexai/src/main/java/com/google/cloud/vertexai/api"
                             "/google-cloud-vertexai/src/main/java/com/google/cloud/vertexai/genai"
                             "/proto-google-cloud-vertexai-v1/src/main/java/com/google/cloud/vertexai/api"]
   :exclude                 ["/google-cloud-vertexai/src/main/java/com/google/cloud/vertexai/api/stub"
                             "/google-cloud-vertexai/src/main/java/com/google/cloud/vertexai/generativeai"]
   :exempt-types            #{"com.google.cloud.vertexai.api.PredictionServiceClient.ListLocationsPagedResponse"
                              "com.google.cloud.vertexai.api.EndpointServiceClient.ListEndpointsPagedResponse"
                              "com.google.cloud.vertexai.api.EndpointServiceClient.ListLocationsPagedResponse"
                              "com.google.cloud.vertexai.api.LlmUtilityServiceClient.ListLocationsPagedResponse"
                              "com.google.cloud.vertexai.api.PredictionServiceSettings"
                              "com.google.cloud.vertexai.api.EndpointServiceSettings"
                              "com.google.cloud.vertexai.api.LlmUtilityServiceSettings"
                              "com.google.cloud.vertexai.api.stub.PredictionServiceStub"
                              "com.google.cloud.vertexai.api.stub.EndpointServiceStub"
                              "com.google.cloud.vertexai.api.stub.LlmUtilityServiceStub"}
   :package-prefixes        #{"com.google.cloud.vertexai"}})

#!----------------------------------------------------------------------------------------------------------------------

(def artifact-registry
  {:name                     'gcp.artifact-registry
   :lib                      'com.github.pkpkpk/gcp.artifact-registry
   :description              "edn bindings for the google-cloud-artifact-registry sdk"
   :package-root             (io/file layout/packages-root "artifact-registry")
   :state-root               (io/file layout/state-root "artifact-registry")
   :type                     :static
   :monorepo?                true
   :discovery-url            "https://artifactregistry.googleapis.com/$discovery/rest?version=v1"
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
   :package-prefixes          #{"com.google.devtools.artifactregistry" "com.google.cloud.artifactregistry"}})

(def monitoring
  {:name                     'gcp.monitoring
   :lib                      'com.github.pkpkpk/gcp.monitoring
   :description              "edn bindings for the google-cloud-monitoring sdk"
   :package-root             (io/file layout/packages-root "monitoring")
   :state-root               (io/file layout/state-root "monitoring")
   :type                     :static
   :monorepo?                true
   :discovery-url            "https://monitoring.googleapis.com/$discovery/rest?version=v3"
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
   :package-prefixes         #{"com.google.cloud.monitoring" "com.google.monitoring" "com.google.monitoring.v3"}})

(def genai
  {:name                    'gcp.genai
   :lib                     'com.github.pkpkpk/gcp.genai
   :description             "edn bindings for the google-genai sdk"
   :package-root            (io/file layout/packages-root "genai")
   :state-root              (io/file layout/state-root "genai")
   :type                    :static
   :discovery-url           "https://generativelanguage.googleapis.com/$discovery/rest?version=v1beta"
   :googleapis/mvn-org      "com.google.genai"
   :googleapis/mvn-artifact "google-genai"
   :googleapis/git-repo     "java-genai"
   :api-roots               ["com.google.genai.Client"]
   :include                 ["/src/main/java/com/google/genai"]
   :package-prefixes         #{"com.google.genai"}})

(def logging
  {:name                    'gcp.logging
   :lib                     'com.github.pkpkpk/gcp.logging
   :description             "edn bindings for the google-cloud-logging sdk"
   :package-root            (io/file layout/packages-root "logging")
   :state-root              (io/file layout/state-root "logging")
   :type                    :static
   :discovery-url           "https://logging.googleapis.com/$discovery/rest?version=v2"
   :googleapis/mvn-org      "com.google.cloud"
   :googleapis/mvn-artifact "google-cloud-logging"
   :googleapis/git-repo     "java-logging"
   :api-roots               ["com.google.cloud.logging.Logging"
                             "com.google.cloud.logging.LoggingOptions"]
   :include                 ["/google-cloud-logging/src/main/java/com/google/cloud/logging"
                             "/google-cloud-logging/src/main/java/com/google/cloud/logging/v2"
                             "/proto-google-cloud-logging-v2/src/main/java/com/google/logging/v2"]
   :exclude                 ["/google-cloud-logging/src/main/java/com/google/cloud/logging/spi"
                             "/google-cloud-logging/src/main/java/com/google/cloud/logging/testing"
                             "/google-cloud-logging/src/main/java/com/google/cloud/logging/v2/stub"]
   :package-prefixes         #{"com.google.cloud.logging" "com.google.logging"}})

(def storage-control
  {:name                    'gcp.storage-control
   :lib                     'com.github.pkpkpk/gcp.storage-control
   :description             "edn bindings for the google-cloud-storage-control sdk"
   :package-root            (io/file layout/packages-root "storage-control")
   :state-root              (io/file layout/state-root "storage-control")
   :type                    :static
   :googleapis/mvn-org      "com.google.cloud"
   :googleapis/mvn-artifact "google-cloud-storage-control"
   :googleapis/git-repo     "java-storage"
   :api-roots               ["com.google.storage.control.v2.StorageControlClient"
                             "com.google.storage.control.v2.StorageControlSettings"]
   :include                 ["/google-cloud-storage-control/src/main/java/com/google/storage/control/v2"
                             "/proto-google-cloud-storage-control-v2/src/main/java/com/google/storage/control/v2"]
   :exclude                 ["/google-cloud-storage-control/src/main/java/com/google/storage/control/v2/stub"]
   :package-prefixes         #{"com.google.storage.control"}})

#!----------------------------------------------------------------------------------------------------------------------

(def pkg-key->package
  {:vertexai vertexai
   :bigquery bigquery
   :bigquery-services bigquery-services
   :pubsub pubsub
   :pubsub-services pubsub-services
   :storage storage
   :storage-services storage-services
   :storage-control storage-control
   :logging logging
   :genai genai
   :monitoring monitoring
   :artifact-registry artifact-registry})
