(ns gcp.monitoring-dashboard.ChartOptions
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 ChartOptions ChartOptions$Mode)))

(def schema
  (g/schema
   [:map
    [:mode {:optional true} [:enum "MODE_UNSPECIFIED" "COLOR" "X_RAY" "STATS"]]
    [:showLegend {:optional true} :boolean]
    [:displayHorizontal {:optional true} :boolean]]))

(defn from-edn
  [{:keys [mode showLegend displayHorizontal] :as arg}]
  (g/strict! schema arg)
  (let [builder (doto (ChartOptions/newBuilder)
                  (cond->
                   showLegend (.setShowLegend showLegend)
                   displayHorizontal (.setDisplayHorizontal displayHorizontal)))]
    (some->> mode (ChartOptions$Mode/valueOf) (.setMode builder))
    (.build builder)))

(defn to-edn
  [^ChartOptions arg]
  (cond-> {}
          (not= "MODE_UNSPECIFIED" (.name (.getMode arg))) (assoc :mode (.name (.getMode arg)))
          (.getShowLegend arg) (assoc :showLegend (.getShowLegend arg))
          (.getDisplayHorizontal arg) (assoc :displayHorizontal (.getDisplayHorizontal arg))))
