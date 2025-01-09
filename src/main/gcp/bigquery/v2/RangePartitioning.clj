(ns gcp.bigquery.v2.RangePartitioning
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery RangePartitioning RangePartitioning$Range)))

(defn ^RangePartitioning from-edn
  [{field :field
    {:keys [start end interval]} :range :as arg}]
  (global/strict! :bigquery/RangePartitioning arg)
  (let [r (let [builder (RangePartitioning$Range/newBuilder)]
            (.setInterval builder (long interval))
            (.setStart builder (long start))
            (.setEnd builder (long end))
            (.build builder))
        builder (RangePartitioning/newBuilder)]
    (.setField builder field)
    (.setRange builder r)))