;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.ArimaFittingMetrics
  {:doc
     "ARIMA model fitting metrics.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.ArimaFittingMetrics"
   :gcp.dev/certification
     {:base-seed 1775130875852
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1775130875852 :standard 1775130875853 :stress 1775130875854}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:54:37.081088150Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model ArimaFittingMetrics]))

(declare from-edn to-edn)

(defn ^ArimaFittingMetrics from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/ArimaFittingMetrics arg)
  (let [o (new ArimaFittingMetrics)]
    (when (some? (get arg :aic)) (.setAic o (double (get arg :aic))))
    (when (some? (get arg :logLikelihood))
      (.setLogLikelihood o (double (get arg :logLikelihood))))
    (when (some? (get arg :variance))
      (.setVariance o (double (get arg :variance))))
    o))

(defn to-edn
  [^ArimaFittingMetrics arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/ArimaFittingMetrics
                          %)]}
  (when arg
    (cond-> {}
      (.getAic arg) (assoc :aic (.getAic arg))
      (.getLogLikelihood arg) (assoc :logLikelihood (.getLogLikelihood arg))
      (.getVariance arg) (assoc :variance (.getVariance arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "ARIMA model fitting metrics.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/ArimaFittingMetrics}
   [:aic
    {:getter-doc "AIC.\n\n@return value or {@code null} for none",
     :setter-doc "AIC.\n\n@param aic aic or {@code null} for none",
     :optional true} :f64]
   [:logLikelihood
    {:getter-doc "Log-likelihood.\n\n@return value or {@code null} for none",
     :setter-doc
       "Log-likelihood.\n\n@param logLikelihood logLikelihood or {@code null} for none",
     :optional true} :f64]
   [:variance
    {:getter-doc "Variance.\n\n@return value or {@code null} for none",
     :setter-doc
       "Variance.\n\n@param variance variance or {@code null} for none",
     :optional true} :f64]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/ArimaFittingMetrics schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.ArimaFittingMetrics"}))