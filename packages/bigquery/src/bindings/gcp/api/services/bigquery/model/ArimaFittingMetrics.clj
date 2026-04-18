;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.ArimaFittingMetrics
  {:doc
     "ARIMA model fitting metrics.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.ArimaFittingMetrics"
   :gcp.dev/certification
     {:base-seed 1776499366179
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1776499366179 :standard 1776499366180 :stress 1776499366181}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:02:47.495587949Z"}}
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