(ns gcp.monitoring-dashboard.Threshold
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 Threshold)))

(def schema (g/schema any?))

(defn ^Threshold from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^Threshold arg]
  (throw (Exception. "unimplemented")))
