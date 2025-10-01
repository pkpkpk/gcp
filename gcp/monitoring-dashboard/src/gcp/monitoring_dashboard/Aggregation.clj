(ns gcp.monitoring-dashboard.Aggregation
  (:require [gcp.global :as g]
            [gcp.protobuf :as p])
  (:import (com.google.monitoring.dashboard.v1 Aggregation Aggregation$Aligner Aggregation$Reducer)))

(def schema
  (g/schema
   [:map
    [:alignmentPeriod {:optional true} :gcp.protobuf/Duration]
    [:perSeriesAligner {:optional true} [:enum "ALIGN_NONE" "ALIGN_DELTA" "ALIGN_RATE" "ALIGN_INTERPOLATE" "ALIGN_NEXT_OLDER" "ALIGN_MIN" "ALIGN_MAX" "ALIGN_MEAN" "ALIGN_COUNT" "ALIGN_SUM" "ALIGN_STDDEV" "ALIGN_COUNT_TRUE" "ALIGN_COUNT_FALSE" "ALIGN_FRACTION_TRUE" "ALIGN_PERCENTILE_99" "ALIGN_PERCENTILE_95" "ALIGN_PERCENTILE_50" "ALIGN_PERCENTILE_05" "ALIGN_PERCENT_CHANGE"]]
    [:crossSeriesReducer {:optional true} [:enum "REDUCE_NONE" "REDUCE_MEAN" "REDUCE_MIN" "REDUCE_MAX" "REDUCE_SUM" "REDUCE_STDDEV" "REDUCE_COUNT" "REDUCE_COUNT_TRUE" "REDUCE_COUNT_FALSE" "REDUCE_FRACTION_TRUE" "REDUCE_PERCENTILE_99" "REDUCE_PERCENTILE_95" "REDUCE_PERCENTILE_50" "REDUCE_PERCENTILE_05"]]
    [:groupByFields {:optional true} [:sequential :string]]]))

(defn from-edn
  [{:keys [alignmentPeriod perSeriesAligner crossSeriesReducer groupByFields] :as arg}]
  (g/strict! schema arg)
  (let [builder (doto (Aggregation/newBuilder)
                  (cond->
                   groupByFields (.addAllGroupByFields groupByFields)))]
    (some->> alignmentPeriod p/Duration-from-edn (.setAlignmentPeriod builder))
    (some->> perSeriesAligner (Aggregation$Aligner/valueOf) (.setPerSeriesAligner builder))
    (some->> crossSeriesReducer (Aggregation$Reducer/valueOf) (.setCrossSeriesReducer builder))
    (.build builder)))

(defn to-edn
  [^Aggregation arg]
  (cond-> {}
          (.hasAlignmentPeriod arg) (assoc :alignmentPeriod (p/Duration-to-edn (.getAlignmentPeriod arg)))
          (not= "ALIGN_NONE" (.name (.getPerSeriesAligner arg))) (assoc :perSeriesAligner (.name (.getPerSeriesAligner arg)))
          (not= "REDUCE_NONE" (.name (.getCrossSeriesReducer arg))) (assoc :crossSeriesReducer (.name (.getCrossSeriesReducer arg)))
          (not-empty (.getGroupByFieldsList arg)) (assoc :groupByFields (vec (.getGroupByFieldsList arg)))))
