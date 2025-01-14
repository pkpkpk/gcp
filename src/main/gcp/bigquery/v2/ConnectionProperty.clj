(ns gcp.bigquery.v2.ConnectionProperty
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery ConnectionProperty)))

(defn ^ConnectionProperty from-edn [arg]
  (global/strict! :bigquery/ConnectionProperty arg)
  (if (sequential? arg)
    (ConnectionProperty/of (first arg) (second arg))
    (if (map? arg)
      (if (not= 1 (count arg))
        (throw (Exception. "expected singleton map kv entry"))
        (recur (first arg))))))

(defn to-edn [^ConnectionProperty arg]
  {(.getKey arg) (.getValue arg)})