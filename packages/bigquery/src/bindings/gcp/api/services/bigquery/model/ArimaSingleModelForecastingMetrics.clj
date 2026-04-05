;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.ArimaSingleModelForecastingMetrics
  {:doc
     "Model evaluation metrics for a single ARIMA forecasting model.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn
     "com.google.api.services.bigquery.model.ArimaSingleModelForecastingMetrics"
   :gcp.dev/certification
     {:base-seed 1775130971117
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1775130971117 :standard 1775130971118 :stress 1775130971119}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:56:12.417750052Z"}}
  (:require [gcp.api.services.bigquery.model.ArimaFittingMetrics :as
             ArimaFittingMetrics]
            [gcp.api.services.bigquery.model.ArimaOrder :as ArimaOrder]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model
            ArimaSingleModelForecastingMetrics]))

(declare from-edn to-edn)

(defn ^ArimaSingleModelForecastingMetrics from-edn
  [arg]
  (global/strict!
    :gcp.api.services.bigquery.model/ArimaSingleModelForecastingMetrics
    arg)
  (let [o (new ArimaSingleModelForecastingMetrics)]
    (when (some? (get arg :arimaFittingMetrics))
      (.setArimaFittingMetrics o
                               (ArimaFittingMetrics/from-edn
                                 (get arg :arimaFittingMetrics))))
    (when (some? (get arg :hasDrift)) (.setHasDrift o (get arg :hasDrift)))
    (when (some? (get arg :hasHolidayEffect))
      (.setHasHolidayEffect o (get arg :hasHolidayEffect)))
    (when (some? (get arg :hasSpikesAndDips))
      (.setHasSpikesAndDips o (get arg :hasSpikesAndDips)))
    (when (some? (get arg :hasStepChanges))
      (.setHasStepChanges o (get arg :hasStepChanges)))
    (when (some? (get arg :nonSeasonalOrder))
      (.setNonSeasonalOrder o
                            (ArimaOrder/from-edn (get arg :nonSeasonalOrder))))
    (when (some? (get arg :seasonalPeriods))
      (.setSeasonalPeriods o (seq (get arg :seasonalPeriods))))
    (when (some? (get arg :timeSeriesId))
      (.setTimeSeriesId o (get arg :timeSeriesId)))
    (when (some? (get arg :timeSeriesIds))
      (.setTimeSeriesIds o (seq (get arg :timeSeriesIds))))
    o))

(defn to-edn
  [^ArimaSingleModelForecastingMetrics arg]
  {:post [(global/strict!
            :gcp.api.services.bigquery.model/ArimaSingleModelForecastingMetrics
            %)]}
  (when arg
    (cond-> {}
      (.getArimaFittingMetrics arg) (assoc :arimaFittingMetrics
                                      (ArimaFittingMetrics/to-edn
                                        (.getArimaFittingMetrics arg)))
      (.getHasDrift arg) (assoc :hasDrift (.getHasDrift arg))
      (.getHasHolidayEffect arg) (assoc :hasHolidayEffect
                                   (.getHasHolidayEffect arg))
      (.getHasSpikesAndDips arg) (assoc :hasSpikesAndDips
                                   (.getHasSpikesAndDips arg))
      (.getHasStepChanges arg) (assoc :hasStepChanges (.getHasStepChanges arg))
      (.getNonSeasonalOrder arg)
        (assoc :nonSeasonalOrder (ArimaOrder/to-edn (.getNonSeasonalOrder arg)))
      (seq (.getSeasonalPeriods arg)) (assoc :seasonalPeriods
                                        (seq (.getSeasonalPeriods arg)))
      (some->> (.getTimeSeriesId arg)
               (not= ""))
        (assoc :timeSeriesId (.getTimeSeriesId arg))
      (seq (.getTimeSeriesIds arg)) (assoc :timeSeriesIds
                                      (seq (.getTimeSeriesIds arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "Model evaluation metrics for a single ARIMA forecasting model.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key
      :gcp.api.services.bigquery.model/ArimaSingleModelForecastingMetrics}
   [:arimaFittingMetrics
    {:getter-doc
       "Arima fitting metrics.\n\n@return value or {@code null} for none",
     :setter-doc
       "Arima fitting metrics.\n\n@param arimaFittingMetrics arimaFittingMetrics or {@code null} for none",
     :optional true} :gcp.api.services.bigquery.model/ArimaFittingMetrics]
   [:hasDrift
    {:getter-doc
       "Is arima model fitted with drift or not. It is always false when d is not 1.\n\n@return value or {@code null} for none",
     :setter-doc
       "Is arima model fitted with drift or not. It is always false when d is not 1.\n\n@param hasDrift hasDrift or {@code null} for none",
     :optional true} :boolean]
   [:hasHolidayEffect
    {:getter-doc
       "If true, holiday_effect is a part of time series decomposition result.\n\n@return value or {@code null} for none",
     :setter-doc
       "If true, holiday_effect is a part of time series decomposition result.\n\n@param hasHolidayEffect hasHolidayEffect or {@code null} for none",
     :optional true} :boolean]
   [:hasSpikesAndDips
    {:getter-doc
       "If true, spikes_and_dips is a part of time series decomposition result.\n\n@return value or {@code null} for none",
     :setter-doc
       "If true, spikes_and_dips is a part of time series decomposition result.\n\n@param hasSpikesAndDips hasSpikesAndDips or {@code null} for none",
     :optional true} :boolean]
   [:hasStepChanges
    {:getter-doc
       "If true, step_changes is a part of time series decomposition result.\n\n@return value or {@code null} for none",
     :setter-doc
       "If true, step_changes is a part of time series decomposition result.\n\n@param hasStepChanges hasStepChanges or {@code null} for none",
     :optional true} :boolean]
   [:nonSeasonalOrder
    {:getter-doc
       "Non-seasonal order.\n\n@return value or {@code null} for none",
     :setter-doc
       "Non-seasonal order.\n\n@param nonSeasonalOrder nonSeasonalOrder or {@code null} for none",
     :optional true} :gcp.api.services.bigquery.model/ArimaOrder]
   [:seasonalPeriods
    {:getter-doc
       "Seasonal periods. Repeated because multiple periods are supported for one time series.\n\n@return value or {@code null} for none",
     :setter-doc
       "Seasonal periods. Repeated because multiple periods are supported for one time series.\n\n@param seasonalPeriods seasonalPeriods or {@code null} for none",
     :optional true} [:sequential {:min 1} [:string {:min 1}]]]
   [:timeSeriesId
    {:getter-doc
       "The time_series_id value for this time series. It will be one of the unique values from the\ntime_series_id_column specified during ARIMA model training. Only present when\ntime_series_id_column training option was used.\n\n@return value or {@code null} for none",
     :setter-doc
       "The time_series_id value for this time series. It will be one of the unique values from the\ntime_series_id_column specified during ARIMA model training. Only present when\ntime_series_id_column training option was used.\n\n@param timeSeriesId timeSeriesId or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:timeSeriesIds
    {:getter-doc
       "The tuple of time_series_ids identifying this time series. It will be one of the unique tuples\nof values present in the time_series_id_columns specified during ARIMA model training. Only\npresent when time_series_id_columns training option was used and the order of values here are\nsame as the order of time_series_id_columns.\n\n@return value or {@code null} for none",
     :setter-doc
       "The tuple of time_series_ids identifying this time series. It will be one of the unique tuples\nof values present in the time_series_id_columns specified during ARIMA model training. Only\npresent when time_series_id_columns training option was used and the order of values here are\nsame as the order of time_series_id_columns.\n\n@param timeSeriesIds timeSeriesIds or {@code null} for none",
     :optional true} [:sequential {:min 1} [:string {:min 1}]]]])

(global/include-schema-registry!
  (with-meta
    {:gcp.api.services.bigquery.model/ArimaSingleModelForecastingMetrics schema}
    {:gcp.global/name
       "gcp.api.services.bigquery.model.ArimaSingleModelForecastingMetrics"}))