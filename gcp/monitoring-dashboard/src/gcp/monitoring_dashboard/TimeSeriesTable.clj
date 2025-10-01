(ns gcp.monitoring-dashboard.TimeSeriesTable
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 TimeSeriesTable)))

(def schema (g/schema any?))

(defn ^TimeSeriesTable from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^TimeSeriesTable arg]
  (throw (Exception. "unimplemented")))
