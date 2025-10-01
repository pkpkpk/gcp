(ns gcp.monitoring-dashboard.LogsPanel
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 LogsPanel)))

(def schema (g/schema any?))

(defn ^LogsPanel from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^LogsPanel arg]
  (throw (Exception. "unimplemented")))
