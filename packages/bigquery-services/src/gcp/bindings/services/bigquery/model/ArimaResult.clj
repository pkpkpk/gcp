;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.ArimaResult
  {:doc
     "(Auto-)arima fitting result. Wrap everything in ArimaResult for easier refactoring if we want to\nuse model-specific iteration results.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.ArimaResult"
   :gcp.dev/certification
     {:base-seed 1772390233817
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390233817 :standard 1772390233818 :stress 1772390233819}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:14.142879458Z"}}
  (:require [gcp.bindings.services.bigquery.model.ArimaModelInfo :as
             ArimaModelInfo]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model ArimaResult]))

(defn ^ArimaResult from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/ArimaResult arg)
  (let [o (new ArimaResult)]
    (when (some? (get arg :arimaModelInfo))
      (.setArimaModelInfo o
                          (map ArimaModelInfo/from-edn
                            (get arg :arimaModelInfo))))
    (when (some? (get arg :seasonalPeriods))
      (.setSeasonalPeriods o (seq (get arg :seasonalPeriods))))
    o))

(defn to-edn
  [^ArimaResult arg]
  {:post [(global/strict! :gcp.bindings.services.bigquery.model/ArimaResult %)]}
  (cond-> {}
    (.getArimaModelInfo arg) (assoc :arimaModelInfo
                               (map ArimaModelInfo/to-edn
                                 (.getArimaModelInfo arg)))
    (.getSeasonalPeriods arg) (assoc :seasonalPeriods
                                (seq (.getSeasonalPeriods arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "(Auto-)arima fitting result. Wrap everything in ArimaResult for easier refactoring if we want to\nuse model-specific iteration results.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/ArimaResult}
   [:arimaModelInfo
    {:getter-doc
       "This message is repeated because there are multiple arima models fitted in auto-arima. For non-\nauto-arima model, its size is one.\n\n@return value or {@code null} for none",
     :setter-doc
       "This message is repeated because there are multiple arima models fitted in auto-arima. For non-\nauto-arima model, its size is one.\n\n@param arimaModelInfo arimaModelInfo or {@code null} for none",
     :optional true}
    [:sequential {:min 1} :gcp.bindings.services.bigquery.model/ArimaModelInfo]]
   [:seasonalPeriods
    {:getter-doc
       "Seasonal periods. Repeated because multiple periods are supported for one time series.\n\n@return value or {@code null} for none",
     :setter-doc
       "Seasonal periods. Repeated because multiple periods are supported for one time series.\n\n@param seasonalPeriods seasonalPeriods or {@code null} for none",
     :optional true} [:sequential {:min 1} [:string {:min 1}]]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/ArimaResult schema}
    {:gcp.global/name "gcp.bindings.services.bigquery.model.ArimaResult"}))