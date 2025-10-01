(ns gcp.monitoring-dashboard.TimeSeriesQuery
  (:require
   [gcp.global :as g]
   [gcp.monitoring-dashboard.TimeSeriesFilter :as TimeSeriesFilter]
   [gcp.monitoring-dashboard.TimeSeriesFilterRatio :as TimeSeriesFilterRatio])
  (:import
   (com.google.monitoring.dashboard.v1 TimeSeriesQuery)))

(def schema
  (g/schema
   [:map
    [:timeSeriesFilter {:optional true} TimeSeriesFilter/schema]
    [:timeSeriesFilterRatio {:optional true} TimeSeriesFilterRatio/schema]
    [:timeSeriesQueryLanguage {:optional true} :string]
    [:prometheusQuery {:optional true} :string]
    [:unitOverride {:optional true} :string]
    [:outputFullDuration {:optional true} :boolean]]))

(defn from-edn
  [{:keys [prometheusQuery
           timeSeriesFilter
           timeSeriesFilterRatio
           timeSeriesQueryLanguage
           unitOverride
           outputFullDuration] :as arg}]
  (g/strict! schema arg)
  (let [builder (doto (TimeSeriesQuery/newBuilder)
                  (cond->
                   unitOverride (.setUnitOverride unitOverride)
                   outputFullDuration (.setOutputFullDuration outputFullDuration)))]
    (cond
      prometheusQuery (.setPrometheusQuery builder prometheusQuery)
      timeSeriesFilter (.setTimeSeriesFilter builder (TimeSeriesFilter/from-edn timeSeriesFilter))
      timeSeriesFilterRatio (.setTimeSeriesFilterRatio builder (TimeSeriesFilterRatio/from-edn timeSeriesFilterRatio))
      timeSeriesQueryLanguage (.setTimeSeriesQueryLanguage builder timeSeriesQueryLanguage))
    (.build builder)))

(defn to-edn
  [^TimeSeriesQuery arg]
  (let [source (cond
                 (.hasTimeSeriesFilter arg) {:timeSeriesFilter (TimeSeriesFilter/to-edn (.getTimeSeriesFilter arg))}
                 (.hasTimeSeriesFilterRatio arg) {:timeSeriesFilterRatio (TimeSeriesFilterRatio/to-edn (.getTimeSeriesFilterRatio arg))}
                 (.hasTimeSeriesQueryLanguage arg) {:timeSeriesQueryLanguage (.getTimeSeriesQueryLanguage arg)}
                 (.hasPrometheusQuery arg) {:prometheusQuery (.getPrometheusQuery arg)}
                 :else nil)]
    (cond-> source
            (not-empty (.getUnitOverride arg)) (assoc :unitOverride (.getUnitOverride arg))
            (.getOutputFullDuration arg) (assoc :outputFullDuration (.getOutputFullDuration arg)))))
