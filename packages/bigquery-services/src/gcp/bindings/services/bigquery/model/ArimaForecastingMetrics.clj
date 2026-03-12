;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.ArimaForecastingMetrics
  {:doc
     "Model evaluation metrics for ARIMA forecasting models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.ArimaForecastingMetrics"
   :gcp.dev/certification
     {:base-seed 1772390245510
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390245510 :standard 1772390245511 :stress 1772390245512}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:26.062889411Z"}}
  (:require
    [gcp.bindings.services.bigquery.model.ArimaFittingMetrics :as
     ArimaFittingMetrics]
    [gcp.bindings.services.bigquery.model.ArimaOrder :as ArimaOrder]
    [gcp.bindings.services.bigquery.model.ArimaSingleModelForecastingMetrics :as
     ArimaSingleModelForecastingMetrics]
    [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model ArimaForecastingMetrics]))

(defn ^ArimaForecastingMetrics from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/ArimaForecastingMetrics
                  arg)
  (let [o (new ArimaForecastingMetrics)]
    (when (some? (get arg :arimaFittingMetrics))
      (.setArimaFittingMetrics o
                               (map ArimaFittingMetrics/from-edn
                                 (get arg :arimaFittingMetrics))))
    (when (some? (get arg :arimaSingleModelForecastingMetrics))
      (.setArimaSingleModelForecastingMetrics
        o
        (map ArimaSingleModelForecastingMetrics/from-edn
          (get arg :arimaSingleModelForecastingMetrics))))
    (when (some? (get arg :hasDrift))
      (.setHasDrift o (seq (get arg :hasDrift))))
    (when (some? (get arg :nonSeasonalOrder))
      (.setNonSeasonalOrder o
                            (map ArimaOrder/from-edn
                              (get arg :nonSeasonalOrder))))
    (when (some? (get arg :seasonalPeriods))
      (.setSeasonalPeriods o (seq (get arg :seasonalPeriods))))
    (when (some? (get arg :timeSeriesId))
      (.setTimeSeriesId o (seq (get arg :timeSeriesId))))
    o))

(defn to-edn
  [^ArimaForecastingMetrics arg]
  {:post [(global/strict!
            :gcp.bindings.services.bigquery.model/ArimaForecastingMetrics
            %)]}
  (cond-> {}
    (.getArimaFittingMetrics arg) (assoc :arimaFittingMetrics
                                    (map ArimaFittingMetrics/to-edn
                                      (.getArimaFittingMetrics arg)))
    (.getArimaSingleModelForecastingMetrics arg)
      (assoc :arimaSingleModelForecastingMetrics
        (map ArimaSingleModelForecastingMetrics/to-edn
          (.getArimaSingleModelForecastingMetrics arg)))
    (.getHasDrift arg) (assoc :hasDrift (seq (.getHasDrift arg)))
    (.getNonSeasonalOrder arg) (assoc :nonSeasonalOrder
                                 (map ArimaOrder/to-edn
                                   (.getNonSeasonalOrder arg)))
    (.getSeasonalPeriods arg) (assoc :seasonalPeriods
                                (seq (.getSeasonalPeriods arg)))
    (.getTimeSeriesId arg) (assoc :timeSeriesId (seq (.getTimeSeriesId arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Model evaluation metrics for ARIMA forecasting models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/ArimaForecastingMetrics}
   [:arimaFittingMetrics
    {:getter-doc
       "Arima model fitting metrics.\n\n@return value or {@code null} for none",
     :setter-doc
       "Arima model fitting metrics.\n\n@param arimaFittingMetrics arimaFittingMetrics or {@code null} for none",
     :optional true}
    [:sequential {:min 1}
     :gcp.bindings.services.bigquery.model/ArimaFittingMetrics]]
   [:arimaSingleModelForecastingMetrics
    {:getter-doc
       "Repeated as there can be many metric sets (one for each model) in auto-arima and the large-\nscale case.\n\n@return value or {@code null} for none",
     :setter-doc
       "Repeated as there can be many metric sets (one for each model) in auto-arima and the large-\nscale case.\n\n@param arimaSingleModelForecastingMetrics arimaSingleModelForecastingMetrics or {@code null} for none",
     :optional true}
    [:sequential {:min 1}
     :gcp.bindings.services.bigquery.model/ArimaSingleModelForecastingMetrics]]
   [:hasDrift
    {:getter-doc
       "Whether Arima model fitted with drift or not. It is always false when d is not 1.\n\n@return value or {@code null} for none",
     :setter-doc
       "Whether Arima model fitted with drift or not. It is always false when d is not 1.\n\n@param hasDrift hasDrift or {@code null} for none",
     :optional true} [:sequential {:min 1} :boolean]]
   [:nonSeasonalOrder
    {:getter-doc
       "Non-seasonal order.\n\n@return value or {@code null} for none",
     :setter-doc
       "Non-seasonal order.\n\n@param nonSeasonalOrder nonSeasonalOrder or {@code null} for none",
     :optional true}
    [:sequential {:min 1} :gcp.bindings.services.bigquery.model/ArimaOrder]]
   [:seasonalPeriods
    {:getter-doc
       "Seasonal periods. Repeated because multiple periods are supported for one time series.\n\n@return value or {@code null} for none",
     :setter-doc
       "Seasonal periods. Repeated because multiple periods are supported for one time series.\n\n@param seasonalPeriods seasonalPeriods or {@code null} for none",
     :optional true} [:sequential {:min 1} [:string {:min 1}]]]
   [:timeSeriesId
    {:getter-doc
       "Id to differentiate different time series for the large-scale case.\n\n@return value or {@code null} for none",
     :setter-doc
       "Id to differentiate different time series for the large-scale case.\n\n@param timeSeriesId timeSeriesId or {@code null} for none",
     :optional true} [:sequential {:min 1} [:string {:min 1}]]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/ArimaForecastingMetrics
                schema}
    {:gcp.global/name
       "gcp.bindings.services.bigquery.model.ArimaForecastingMetrics"}))