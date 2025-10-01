(ns gcp.monitoring-dashboard.CollapsibleGroup
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 CollapsibleGroup)))

(def schema (g/schema any?))

(defn ^CollapsibleGroup from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^CollapsibleGroup arg]
  (throw (Exception. "unimplemented")))
