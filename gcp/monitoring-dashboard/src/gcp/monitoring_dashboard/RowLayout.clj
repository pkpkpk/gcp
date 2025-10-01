(ns gcp.monitoring-dashboard.RowLayout
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 RowLayout)))

(def schema (g/schema any?))

(defn ^RowLayout from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^RowLayout arg]
  (throw (Exception. "unimplemented")))