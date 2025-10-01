(ns gcp.monitoring-dashboard.SectionHeader
  (:require [gcp.global :as g])
  (:import (com.google.monitoring.dashboard.v1 SectionHeader)))

(def schema (g/schema any?))

(defn ^SectionHeader from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^SectionHeader arg]
  (throw (Exception. "unimplemented")))
