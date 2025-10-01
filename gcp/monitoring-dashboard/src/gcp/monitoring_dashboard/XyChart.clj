(ns gcp.monitoring-dashboard.XyChart
  (:require
    [gcp.global :as g]
    [gcp.monitoring-dashboard.ChartOptions :as ChartOptions]
    [gcp.monitoring-dashboard.Threshold :as Threshold]
    [gcp.monitoring-dashboard.TimeSeriesQuery :as TimeSeriesQuery]
    [gcp.protobuf :as p])
  (:import
   (com.google.monitoring.dashboard.v1 XyChart XyChart$Axis XyChart$DataSet XyChart$Axis$Scale XyChart$DataSet$PlotType XyChart$DataSet$TargetAxis)
   (com.google.protobuf Duration)))

(def XyChart$Axis:schema
  (g/schema
   [:map
    [:label {:optional true} :string]
    [:scale {:optional true} [:enum "SCALE_UNSPECIFIED" "LINEAR" "LOG10"]]]))

(defn XyChart$Axis:from-edn
  [{:keys [label scale] :as arg}]
  (g/strict! XyChart$Axis:schema arg)
  (let [builder (doto (XyChart$Axis/newBuilder)
                  (cond-> label (.setLabel label)))]
    (some->> scale (XyChart$Axis$Scale/valueOf) (.setScale builder))
    (.build builder)))

(defn XyChart$Axis:to-edn
  [^XyChart$Axis arg]
  (cond-> {}
          (not-empty (.getLabel arg)) (assoc :label (.getLabel arg))
          (not= "SCALE_UNSPECIFIED" (.name (.getScale arg))) (assoc :scale (.name (.getScale arg)))))

(def XyChart$DataSet:schema
  (g/schema
   [:map
    [:timeSeriesQuery TimeSeriesQuery/schema]
    [:plotType {:optional true} [:enum "PLOT_TYPE_UNSPECIFIED" "LINE" "STACKED_AREA" "STACKED_BAR" "HEATMAP"]]
    [:legendTemplate {:optional true} :string]
    [:minAlignmentPeriod {:optional true} :gcp.protobuf/Duration]
    [:targetAxis {:optional true} [:enum "TARGET_AXIS_UNSPECIFIED" "Y1" "Y2"]]]))

(defn XyChart$DataSet:from-edn
  [{:keys [legendTemplate minAlignmentPeriod plotType targetAxis timeSeriesQuery] :as arg}]
  (g/strict! XyChart$DataSet:schema arg)
  (let [builder (doto (XyChart$DataSet/newBuilder)
                  (.setTimeSeriesQuery (TimeSeriesQuery/from-edn timeSeriesQuery))
                  (cond-> legendTemplate (.setLegendTemplate legendTemplate)))]
    (some->> minAlignmentPeriod p/Duration-from-edn (.setMinAlignmentPeriod builder))
    (some->> plotType (XyChart$DataSet$PlotType/valueOf) (.setPlotType builder))
    (some->> targetAxis (XyChart$DataSet$TargetAxis/valueOf) (.setTargetAxis builder))
    (.build builder)))

(defn XyChart$DataSet:to-edn
  [^XyChart$DataSet arg]
  (cond-> {:timeSeriesQuery (TimeSeriesQuery/to-edn (.getTimeSeriesQuery arg))}
          (not-empty (.getLegendTemplate arg)) (assoc :legendTemplate (.getLegendTemplate arg))
          (.hasMinAlignmentPeriod arg) (assoc :minAlignmentPeriod (p/Duration-to-edn (.getMinAlignmentPeriod arg)))
          (not= "PLOT_TYPE_UNSPECIFIED" (.name (.getPlotType arg))) (assoc :plotType (.name (.getPlotType arg)))
          (not= "TARGET_AXIS_UNSPECIFIED" (.name (.getTargetAxis arg))) (assoc :targetAxis (.name (.getTargetAxis arg)))))

(def schema
  (g/schema
   [:map
    [:dataSets [:sequential XyChart$DataSet:schema]]
    [:timeshiftDuration {:optional true} :gcp.protobuf/Duration]
    [:thresholds {:optional true} [:sequential Threshold/schema]]
    [:xAxis {:optional true} XyChart$Axis:schema]
    [:yAxis {:optional true} XyChart$Axis:schema]
    [:y2Axis {:optional true} XyChart$Axis:schema]
    [:chartOptions {:optional true} ChartOptions/schema]]))

(defn from-edn
  [{:keys [chartOptions dataSets thresholds timeshiftDuration xAxis y2Axis yAxis] :as arg}]
  (g/strict! schema arg)
  (let [builder (XyChart/newBuilder)]
    (some->> chartOptions ChartOptions/from-edn (.setChartOptions builder))
    (some->> dataSets (map XyChart$DataSet:from-edn) (.addAllDataSets builder))
    (some->> thresholds (map Threshold/from-edn) (.addAllThresholds builder))
    (some->> timeshiftDuration p/Duration-to-edn (.setTimeshiftDuration builder))
    (some->> xAxis XyChart$Axis:from-edn (.setXAxis builder))
    (some->> y2Axis XyChart$Axis:from-edn (.setY2Axis builder))
    (some->> yAxis XyChart$Axis:from-edn (.setYAxis builder))
    (.build builder)))

(defn to-edn
  [^XyChart arg]
  (cond-> {:dataSets (mapv XyChart$DataSet:to-edn (.getDataSetsList arg))}
          (.hasChartOptions arg) (assoc :chartOptions (ChartOptions/to-edn (.getChartOptions arg)))
          (not-empty (.getThresholdsList arg)) (assoc :thresholds (mapv Threshold/to-edn (.getThresholdsList arg)))
          (.hasTimeshiftDuration arg) (assoc :timeshiftDuration (p/Duration-to-edn (.getTimeshiftDuration arg)))
          (.hasXAxis arg) (assoc :xAxis (XyChart$Axis:to-edn (.getXAxis arg)))
          (.hasY2Axis arg) (assoc :y2Axis (XyChart$Axis:to-edn (.getY2Axis arg)))
          (.hasYAxis arg) (assoc :yAxis (XyChart$Axis:to-edn (.getYAxis arg)))))
