(ns gcp.monitoring-dashboard.Scorecard
  (:require
    [gcp.global :as g]
    [gcp.monitoring-dashboard.Threshold :as Threshold]
    [gcp.monitoring-dashboard.TimeSeriesQuery :as TimeSeriesQuery]
    gcp.protobuf
    [gcp.protobuf :as p])
  (:import
   (com.google.monitoring.dashboard.v1 Scorecard Scorecard$GaugeView Scorecard$SparkChartView SparkChartType)
   (com.google.protobuf Empty)))

(def Scorecard$GaugeView:schema
  (g/schema
   [:map
    [:lowerBound {:optional true} :double]
    [:upperBound {:optional true} :double]]))

(defn Scorecard$GaugeView:from-edn
  [{:keys [lowerBound upperBound] :as arg}]
  (g/strict! Scorecard$GaugeView:schema arg)
  (let [builder (doto (Scorecard$GaugeView/newBuilder)
                  (cond->
                   lowerBound (.setLowerBound lowerBound)
                   upperBound (.setUpperBound upperBound)))]
    (.build builder)))

(defn Scorecard$GaugeView:to-edn
  [^Scorecard$GaugeView arg]
  (cond-> {}
          (not (zero? (.getLowerBound arg))) (assoc :lowerBound (.getLowerBound arg))
          (not (zero? (.getUpperBound arg))) (assoc :upperBound (.getUpperBound arg))))

(def Scorecard$SparkChartView:schema
  (g/schema
   [:map
    [:sparkChartType [:enum "SPARK_CHART_TYPE_UNSPECIFIED" "SPARK_LINE" "SPARK_BAR"]]
    [:minAlignmentPeriod {:optional true} :gcp.protobuf/Duration]]))

(defn Scorecard$SparkChartView:from-edn
  [{:keys [sparkChartType minAlignmentPeriod] :as arg}]
  (g/strict! Scorecard$SparkChartView:schema arg)
  (let [builder (doto (Scorecard$SparkChartView/newBuilder)
                  (.setSparkChartType (SparkChartType/valueOf sparkChartType)))]
    (some->> minAlignmentPeriod p/Duration-from-edn (.setMinAlignmentPeriod builder))
    (.build builder)))

(defn Scorecard$SparkChartView:to-edn
  [^Scorecard$SparkChartView arg]
  (cond-> {}
          (not= "SPARK_CHART_TYPE_UNSPECIFIED" (.name (.getSparkChartType arg))) (assoc :sparkChartType (.name (.getSparkChartType arg)))
          (.hasMinAlignmentPeriod arg) (assoc :minAlignmentPeriod (p/Duration-to-edn (.getMinAlignmentPeriod arg)))))

(def schema
  (g/schema
   [:map
    [:timeSeriesQuery TimeSeriesQuery/schema]
    [:thresholds {:optional true} [:sequential Threshold/schema]]
    [:gaugeView {:optional true} Scorecard$GaugeView:schema]
    [:sparkChartView {:optional true} Scorecard$SparkChartView:schema]
    [:blankView {:optional true} any?]]))

(defn from-edn
  [{:keys [timeSeriesQuery thresholds gaugeView sparkChartView blankView] :as arg}]
  (g/strict! schema arg)
  (let [builder (doto (Scorecard/newBuilder)
                  (.setTimeSeriesQuery (TimeSeriesQuery/from-edn timeSeriesQuery)))]
    (some->> thresholds (map Threshold/from-edn) (.addAllThresholds builder))
    (cond
      gaugeView (.setGaugeView builder (Scorecard$GaugeView:from-edn gaugeView))
      sparkChartView (.setSparkChartView builder (Scorecard$SparkChartView:from-edn sparkChartView))
      blankView (.setBlankView builder (Empty/getDefaultInstance)))
    (.build builder)))

(defn to-edn
  [^Scorecard arg]
  (let [data-view (cond
                      (.hasGaugeView arg) {:gaugeView (Scorecard$GaugeView:to-edn (.getGaugeView arg))}
                      (.hasSparkChartView arg) {:sparkChartView (Scorecard$SparkChartView:to-edn (.getSparkChartView arg))}
                      (.hasBlankView arg) {:blankView {}}
                      :else nil)]
    (cond-> (merge {:timeSeriesQuery (TimeSeriesQuery/to-edn (.getTimeSeriesQuery arg))} data-view)
            (not-empty (.getThresholdsList arg)) (assoc :thresholds (mapv Threshold/to-edn (.getThresholdsList arg))))))
