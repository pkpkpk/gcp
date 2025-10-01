(ns gcp.monitoring-dashboard.Widget
  (:require
   [gcp.global :as g]
   [gcp.monitoring-dashboard.AlertChart :as AlertChart]
   [gcp.monitoring-dashboard.CollapsibleGroup :as CollapsibleGroup]
   [gcp.monitoring-dashboard.ErrorReportingPanel :as ErrorReportingPanel]
   [gcp.monitoring-dashboard.IncidentList :as IncidentList]
   [gcp.monitoring-dashboard.LogsPanel :as LogsPanel]
   [gcp.monitoring-dashboard.PieChart :as PieChart]
   [gcp.monitoring-dashboard.Scorecard :as Scorecard]
   [gcp.monitoring-dashboard.SectionHeader :as SectionHeader]
   [gcp.monitoring-dashboard.SingleViewGroup :as SingleViewGroup]
   [gcp.monitoring-dashboard.Text :as Text]
   [gcp.monitoring-dashboard.TimeSeriesTable :as TimeSeriesTable]
   [gcp.monitoring-dashboard.XyChart :as XyChart])
  (:import
   (com.google.monitoring.dashboard.v1 Widget)
   (com.google.protobuf Empty)))

(def schema
  (g/schema
   [:map
    [:title {:optional true} :string]
    [:id {:optional true} :string]
    [:alertChart {:optional true} AlertChart/schema]
    [:blank {:optional true} any?]
    [:collapsibleGroup {:optional true} CollapsibleGroup/schema]
    [:errorReportingPanel {:optional true} ErrorReportingPanel/schema]
    [:incidentList {:optional true} IncidentList/schema]
    [:logsPanel {:optional true} LogsPanel/schema]
    [:pieChart {:optional true} PieChart/schema]
    [:scorecard {:optional true} Scorecard/schema]
    [:sectionHeader {:optional true} SectionHeader/schema]
    [:singleViewGroup {:optional true} SingleViewGroup/schema]
    [:text {:optional true} Text/schema]
    [:timeSeriesTable {:optional true} TimeSeriesTable/schema]
    [:xyChart {:optional true} XyChart/schema]]))

(defn from-edn
  [{:keys [alertChart
           blank
           collapsibleGroup
           errorReportingPanel
           id
           incidentList
           logsPanel
           pieChart
           scorecard
           sectionHeader
           singleViewGroup
           text
           timeSeriesTable
           title
           xyChart]
    :as   arg}]
  (g/strict! schema arg)
  (let [builder (doto (Widget/newBuilder)
                  (cond->
                   id (.setId id)
                   title (.setTitle title)))]
    (cond
      alertChart (.setAlertChart builder (AlertChart/from-edn alertChart))
      blank (.setBlank builder (Empty/getDefaultInstance))
      collapsibleGroup (.setCollapsibleGroup builder (CollapsibleGroup/from-edn collapsibleGroup))
      errorReportingPanel (.setErrorReportingPanel builder (ErrorReportingPanel/from-edn errorReportingPanel))
      incidentList (.setIncidentList builder (IncidentList/from-edn incidentList))
      logsPanel (.setLogsPanel builder (LogsPanel/from-edn logsPanel))
      pieChart (.setPieChart builder (PieChart/from-edn pieChart))
      scorecard (.setScorecard builder (Scorecard/from-edn scorecard))
      sectionHeader (.setSectionHeader builder (SectionHeader/from-edn sectionHeader))
      singleViewGroup (.setSingleViewGroup builder (SingleViewGroup/from-edn singleViewGroup))
      text (.setText builder (Text/from-edn text))
      timeSeriesTable (.setTimeSeriesTable builder (TimeSeriesTable/from-edn timeSeriesTable))
      xyChart (.setXyChart builder (XyChart/from-edn xyChart)))
    (.build builder)))

(defn to-edn
  [^Widget arg]
  (let [content (cond
                  (.hasAlertChart arg) {:alertChart (AlertChart/to-edn (.getAlertChart arg))}
                  (.hasBlank arg) {:blank {}}
                  (.hasCollapsibleGroup arg) {:collapsibleGroup (CollapsibleGroup/to-edn (.getCollapsibleGroup arg))}
                  (.hasErrorReportingPanel arg) {:errorReportingPanel (ErrorReportingPanel/to-edn (.getErrorReportingPanel arg))}
                  (.hasIncidentList arg) {:incidentList (IncidentList/to-edn (.getIncidentList arg))}
                  (.hasLogsPanel arg) {:logsPanel (LogsPanel/to-edn (.getLogsPanel arg))}
                  (.hasPieChart arg) {:pieChart (PieChart/to-edn (.getPieChart arg))}
                  (.hasScorecard arg) {:scorecard (Scorecard/to-edn (.getScorecard arg))}
                  (.hasSectionHeader arg) {:sectionHeader (SectionHeader/to-edn (.getSectionHeader arg))}
                  (.hasSingleViewGroup arg) {:singleViewGroup (SingleViewGroup/to-edn (.getSingleViewGroup arg))}
                  (.hasText arg) {:text (Text/to-edn (.getText arg))}
                  (.hasTimeSeriesTable arg) {:timeSeriesTable (TimeSeriesTable/to-edn (.getTimeSeriesTable arg))}
                  (.hasXyChart arg) {:xyChart (XyChart/to-edn (.getXyChart arg))}
                  :else nil)]
    (cond-> content
            (not-empty (.getId arg)) (assoc :id (.getId arg))
            (not-empty (.getTitle arg)) (assoc :title (.getTitle arg)))))
