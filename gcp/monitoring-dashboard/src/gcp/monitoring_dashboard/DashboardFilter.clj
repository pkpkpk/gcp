(ns gcp.monitoring-dashboard.DashboardFilter
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 DashboardFilter DashboardFilter$FilterType)))

(def schema
  (g/schema
    [:map
     [:labelKey :string]
     [:filterType {:optional true} [:enum "FILTER_TYPE_UNSPECIFIED" "GROUP" "METRIC_LABEL" "RESOURCE_LABEL" "SYSTEM_METADATA_LABEL" "UNRECOGNIZED" "USER_METADATA_LABEL"]]
     [:stringValue {:optional true} :string]
     [:templateVariable {:optional true} :string]]))

(defn ^DashboardFilter from-edn
  [{:keys [labelKey filterType stringValue templateVariable] :as arg}]
  (g/strict! schema arg)
  (let [builder (doto (DashboardFilter/newBuilder)
                  (.setLabelKey labelKey)
                  (.setTemplateVariable templateVariable))]
    (some->> filterType (DashboardFilter$FilterType/valueOf) (.setFilterType builder))
    (some->> stringValue (.setStringValue builder))
    (.build builder)))

(defn to-edn
  [^DashboardFilter arg]
  (cond-> {:labelKey (.getLabelKey arg)
           :templateVariable (.getTemplateVariable arg)}
          (not= "FILTER_TYPE_UNSPECIFIED" (.name (.getFilterType arg))) (assoc :filterType (.name (.getFilterType arg)))
          (.hasStringValue arg) (assoc :stringValue (.getStringValue arg))))