(ns gcp.monitoring-dashboard.SingleViewGroup
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 SingleViewGroup)))

(def schema (g/schema any?))

(defn ^SingleViewGroup from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^SingleViewGroup arg]
  (throw (Exception. "unimplemented")))
