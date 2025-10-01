(ns gcp.monitoring-dashboard.PieChart
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 PieChart)))

(def schema (g/schema any?))

(defn ^PieChart from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^PieChart arg]
  (throw (Exception. "unimplemented")))
