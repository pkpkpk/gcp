;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.ArimaFittingMetrics
  {:doc
     "ARIMA model fitting metrics.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.ArimaFittingMetrics"
   :gcp.dev/certification
     {:base-seed 1772390231693
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390231693 :standard 1772390231694 :stress 1772390231695}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:11.725594970Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model ArimaFittingMetrics]))

(defn ^ArimaFittingMetrics from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/ArimaFittingMetrics arg)
  (let [o (new ArimaFittingMetrics)]
    (when (some? (get arg :aic)) (.setAic o (get arg :aic)))
    (when (some? (get arg :logLikelihood))
      (.setLogLikelihood o (get arg :logLikelihood)))
    (when (some? (get arg :variance)) (.setVariance o (get arg :variance)))
    o))

(defn to-edn
  [^ArimaFittingMetrics arg]
  {:post [(global/strict!
            :gcp.bindings.services.bigquery.model/ArimaFittingMetrics
            %)]}
  (cond-> {}
    (.getAic arg) (assoc :aic (.getAic arg))
    (.getLogLikelihood arg) (assoc :logLikelihood (.getLogLikelihood arg))
    (.getVariance arg) (assoc :variance (.getVariance arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "ARIMA model fitting metrics.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/ArimaFittingMetrics}
   [:aic
    {:getter-doc "AIC.\n\n@return value or {@code null} for none",
     :setter-doc "AIC.\n\n@param aic aic or {@code null} for none",
     :optional true} :double]
   [:logLikelihood
    {:getter-doc "Log-likelihood.\n\n@return value or {@code null} for none",
     :setter-doc
       "Log-likelihood.\n\n@param logLikelihood logLikelihood or {@code null} for none",
     :optional true} :double]
   [:variance
    {:getter-doc "Variance.\n\n@return value or {@code null} for none",
     :setter-doc
       "Variance.\n\n@param variance variance or {@code null} for none",
     :optional true} :double]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/ArimaFittingMetrics schema}
    {:gcp.global/name
       "gcp.bindings.services.bigquery.model.ArimaFittingMetrics"}))