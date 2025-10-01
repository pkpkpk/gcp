(ns gcp.monitoring-dashboard.AlertChart
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 AlertChart)))

(def schema (g/schema any?))

(defn ^AlertChart from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^AlertChart arg]
  (throw (Exception. "unimplemented")))
