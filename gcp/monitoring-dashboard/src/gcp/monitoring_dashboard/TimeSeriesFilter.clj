(ns gcp.monitoring-dashboard.TimeSeriesFilter
  (:require
   [gcp.global :as g]
   [gcp.monitoring-dashboard.Aggregation :as Aggregation]
   [gcp.monitoring-dashboard.PickTimeSeriesFilter :as PickTimeSeriesFilter]
   [gcp.monitoring-dashboard.StatisticalTimeSeriesFilter :as StatisticalTimeSeriesFilter])
  (:import
   (com.google.monitoring.dashboard.v1 TimeSeriesFilter)))

(def schema
  (g/schema
   [:map
    [:filter :string]
    [:aggregation {:optional true} Aggregation/schema]
    [:secondaryAggregation {:optional true} Aggregation/schema]
    [:pickTimeSeriesFilter {:optional true} PickTimeSeriesFilter/schema]
    [:statisticalTimeSeriesFilter {:optional true} StatisticalTimeSeriesFilter/schema]]))

(defn from-edn
  [{:keys [filter aggregation secondaryAggregation pickTimeSeriesFilter statisticalTimeSeriesFilter] :as arg}]
  (g/strict! schema arg)
  (let [builder (doto (TimeSeriesFilter/newBuilder)
                  (.setFilter filter))]
    (some->> aggregation Aggregation/from-edn (.setAggregation builder))
    (some->> secondaryAggregation Aggregation/from-edn (.setSecondaryAggregation builder))
    (cond
      pickTimeSeriesFilter (.setPickTimeSeriesFilter builder (PickTimeSeriesFilter/from-edn pickTimeSeriesFilter))
      statisticalTimeSeriesFilter (.setStatisticalTimeSeriesFilter builder (StatisticalTimeSeriesFilter/from-edn statisticalTimeSeriesFilter)))
    (.build builder)))

(defn to-edn
  [^TimeSeriesFilter arg]
  (let [output-filter (cond
                        (.hasPickTimeSeriesFilter arg) {:pickTimeSeriesFilter (PickTimeSeriesFilter/to-edn (.getPickTimeSeriesFilter arg))}
                        (.hasStatisticalTimeSeriesFilter arg) {:statisticalTimeSeriesFilter (StatisticalTimeSeriesFilter/to-edn (.getStatisticalTimeSeriesFilter arg))}
                        :else nil)]
    (cond-> (merge {:filter (.getFilter arg)} output-filter)
            (.hasAggregation arg) (assoc :aggregation (Aggregation/to-edn (.getAggregation arg)))
            (.hasSecondaryAggregation arg) (assoc :secondaryAggregation (Aggregation/to-edn (.getSecondaryAggregation arg))))))
