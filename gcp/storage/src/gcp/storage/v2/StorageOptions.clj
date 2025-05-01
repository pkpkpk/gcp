(ns gcp.storage.v2.StorageOptions
  (:require [gcp.global :as g]
            [gcp.gax.retrying.RetrySettings :as RetrySettings]
            [gcp.storage.v2.BlobWriteSessionConfig :as BlobWriteSessionConfig])
  (:import (com.google.cloud.storage Storage StorageOptions StorageRetryStrategy)))

#_ (do (require :reload 'gcp.storage.v2.StorageOptions) (in-ns 'gcp.storage.v2.StorageOptions))

(defn ^StorageOptions from-edn
  [{:keys [projectId
           blobWriteSessionConfig
           openTelemetry
           storageRetryStrategy] :as arg}]
  (g/strict! :gcp/storage.StorageOptions arg)
  (let [builder (StorageOptions/newBuilder)]
    (when blobWriteSessionConfig
      (.setBlobWriteSessionConfig builder (BlobWriteSessionConfig/from-edn blobWriteSessionConfig)))
    (when openTelemetry
      (.setOpenTelemetry builder openTelemetry))
    (when (contains? arg :storageRetryStrategy)
      ;; TODO these are static classes, default vs uniform.
      ;; scalar behaviors are set via :retrySettings
      (throw (Exception. "unimplemented")))
    ;com.google.cloud.ServiceOptions.Builder.setApiTracerFactory(com.google.api.gax.tracing.ApiTracerFactory)
    ;com.google.cloud.ServiceOptions.Builder.setClientLibToken(java.lang.String)
    ;com.google.cloud.ServiceOptions.Builder.setClock(com.google.api.core.ApiClock)
    ;com.google.cloud.ServiceOptions.Builder.setCredentials(com.google.auth.Credentials)
    ;com.google.cloud.ServiceOptions.Builder.setHeaderProvider(com.google.api.gax.rpc.HeaderProvider)
    ;com.google.cloud.ServiceOptions.Builder.setHost(java.lang.String)
    (when projectId
      (.setProjectId builder projectId))
    ;com.google.cloud.ServiceOptions.Builder.setQuotaProjectId(java.lang.String)
    ;com.google.cloud.ServiceOptions.Builder.setRetrySettings(com.google.api.gax.retrying.RetrySettings)
    (when (contains? arg :retrySettings)
      (let [default-builder (.toBuilder (StorageOptions/getDefaultRetrySettings))
            retry-settings  (RetrySettings/from-edn default-builder (:retrySettings arg))]
        (.setRetrySettings builder retry-settings)))
    ;com.google.cloud.ServiceOptions.Builder.setServiceFactory(com.google.cloud.ServiceFactory<ServiceT,OptionsT>)
    ;com.google.cloud.ServiceOptions.Builder.setServiceRpcFactory(com.google.cloud.spi.ServiceRpcFactory<OptionsT>)
    ;com.google.cloud.ServiceOptions.Builder.setTransportOptions(com.google.cloud.TransportOptions)
    ;com.google.cloud.ServiceOptions.Builder.setUniverseDomain(java.lang.String)
    (.build builder)))

(defn ^Storage get-service [arg]
  (g/strict! :gcp/storage.synth.clientable arg)
  (if (instance? Storage arg)
    arg
    (if (instance? StorageOptions arg)
      (.getService arg)
      (.getService (from-edn arg)))))

