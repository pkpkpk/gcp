(ns gcp.logging.Metric
  (:require [gcp.logging.MetricInfo :as MetricInfo])
  (:import (com.google.cloud.logging Metric)))

(defn to-edn [^Metric arg]
  (when arg
    (MetricInfo/to-edn arg)))
