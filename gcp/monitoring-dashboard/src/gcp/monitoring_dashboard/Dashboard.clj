(ns gcp.monitoring-dashboard.Dashboard
  (:require [gcp.global :as g]
            [gcp.monitoring-dashboard.ColumnLayout :as ColumnLayout]
            [gcp.monitoring-dashboard.DashboardFilter :as DashboardFilter]
            [gcp.monitoring-dashboard.GridLayout :as GridLayout]
            [gcp.monitoring-dashboard.MosaicLayout :as MosaicLayout]
            [gcp.monitoring-dashboard.RowLayout :as RowLayout])
  (:import (com.google.monitoring.dashboard.v1 Dashboard)))

(def schema
  (g/schema
   [:map
    [:dashboardFilters [:seqable DashboardFilter/schema]]
    [:displayName :string]
    [:labels {:optional true} [:map-of :string :string]]
    [:columnLayout {:optional true} ColumnLayout/schema]
    [:gridLayout {:optional true} GridLayout/schema]
    [:mosaicLayout {:optional true} MosaicLayout/schema]
    [:rowLayout {:optional true} RowLayout/schema]]))

(defn ^Dashboard from-edn
  [{:keys [columnLayout
           dashboardFilters
           displayName
           gridLayout
           labels
           mosaicLayout
           rowLayout] :as arg}]
  (g/strict! schema arg)
  (let [builder (doto (Dashboard/newBuilder)
                  (.setDisplayName displayName))]
    (some->> columnLayout ColumnLayout/from-edn (.setColumnLayout builder))
    (some->> dashboardFilters (map DashboardFilter/from-edn) (.addAllDashboardFilters builder))
    (some->> gridLayout GridLayout/from-edn (.setGridLayout builder))
    (some->> labels (.putAllLabels builder))
    (some->> mosaicLayout MosaicLayout/from-edn (.setMosaicLayout builder))
    (some->> rowLayout RowLayout/from-edn (.setRowLayout builder))
    (.build builder)))

(defn to-edn
  [^Dashboard arg]
  (cond-> {:displayName (.getDisplayName arg)}
          (not-empty (.getDashboardFiltersList arg)) (assoc :dashboardFilters (mapv DashboardFilter/to-edn (.getDashboardFiltersList arg)))
          (not-empty (.getLabelsMap arg)) (assoc :labels (into {} (.getLabelsMap arg)))
          (.hasColumnLayout arg) (assoc :columnLayout (ColumnLayout/to-edn (.getColumnLayout arg)))
          (.hasGridLayout arg) (assoc :gridLayout (GridLayout/to-edn (.getGridLayout arg)))
          (.hasMosaicLayout arg) (assoc :mosaicLayout (MosaicLayout/to-edn (.getMosaicLayout arg)))
          (.hasRowLayout arg) (assoc :rowLayout (RowLayout/to-edn (.getRowLayout arg)))))
