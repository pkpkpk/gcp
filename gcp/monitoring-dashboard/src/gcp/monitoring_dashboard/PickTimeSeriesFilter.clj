(ns gcp.monitoring-dashboard.PickTimeSeriesFilter
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 PickTimeSeriesFilter)))

(def schema (g/schema any?))

(defn ^PickTimeSeriesFilter from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^PickTimeSeriesFilter arg]
  (throw (Exception. "unimplemented")))
