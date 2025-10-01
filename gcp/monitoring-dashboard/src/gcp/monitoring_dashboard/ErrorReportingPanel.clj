(ns gcp.monitoring-dashboard.ErrorReportingPanel
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 ErrorReportingPanel)))

(def schema (g/schema any?))

(defn ^ErrorReportingPanel from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^ErrorReportingPanel arg]
  (throw (Exception. "unimplemented")))
