(ns gcp.monitoring-dashboard.TimeSeriesFilterRatio
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 TimeSeriesFilterRatio)))

(def schema (g/schema any?))

(defn ^TimeSeriesFilterRatio from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^TimeSeriesFilterRatio arg]
  (throw (Exception. "unimplemented")))
