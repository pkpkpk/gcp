(ns gcp.monitoring-dashboard.GridLayout
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 GridLayout)))

(def schema (g/schema any?))

(defn ^GridLayout from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^GridLayout arg]
  (throw (Exception. "unimplemented")))
