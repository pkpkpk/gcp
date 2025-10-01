(ns gcp.monitoring-dashboard.IncidentList
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 IncidentList)))

(def schema (g/schema any?))

(defn ^IncidentList from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^IncidentList arg]
  (throw (Exception. "unimplemented")))
