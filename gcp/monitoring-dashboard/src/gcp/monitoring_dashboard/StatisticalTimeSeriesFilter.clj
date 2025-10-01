(ns gcp.monitoring-dashboard.StatisticalTimeSeriesFilter
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 StatisticalTimeSeriesFilter)))

(def schema (g/schema any?))

(defn ^StatisticalTimeSeriesFilter from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^StatisticalTimeSeriesFilter arg]
  (throw (Exception. "unimplemented")))
