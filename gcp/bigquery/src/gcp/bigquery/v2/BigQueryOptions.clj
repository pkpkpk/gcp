(ns gcp.bigquery.v2.BigQueryOptions
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery BigQuery BigQueryOptions]))

(defn ^BigQueryOptions from-edn
  [{:keys [location transportOptions useInt64Timestamps
           projectId] :as arg}]
  (global/strict! :gcp/bigquery.BigQueryOptions arg)
  (let [builder (BigQueryOptions/newBuilder)]
    (when location
      (.setLocation builder location))
    (when transportOptions
      (.setTransportOptions builder transportOptions))
    (when useInt64Timestamps
      (.setUseInt64Timestamps builder useInt64Timestamps))
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
    ;com.google.cloud.ServiceOptions.Builder.setServiceFactory(com.google.cloud.ServiceFactory<ServiceT,OptionsT>)
    ;com.google.cloud.ServiceOptions.Builder.setServiceRpcFactory(com.google.cloud.spi.ServiceRpcFactory<OptionsT>)
    ;com.google.cloud.ServiceOptions.Builder.setTransportOptions(com.google.cloud.TransportOptions)
    ;com.google.cloud.ServiceOptions.Builder.setUniverseDomain(java.lang.String)
    (.build builder)))

(defn ^BigQuery get-service [arg]
  (if (instance? BigQueryOptions arg)
    (.getService arg)
    (.getService (from-edn arg))))