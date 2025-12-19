(ns gcp.bigquery.v2.ConnectionProperty
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery ConnectionProperty)))

(defn ^ConnectionProperty from-edn [arg]
  (global/strict! :gcp.bigquery.v2/ConnectionProperty arg)
  (if (sequential? arg)
    (ConnectionProperty/of (first arg) (second arg))
    (if (map? arg)
      (if (not= 1 (count arg))
        (throw (Exception. "expected singleton map kv entry"))
        (recur (first arg))))))

(defn to-edn [^ConnectionProperty arg]
  {(.getKey arg) (.getValue arg)})

(def schemas
  {:gcp.bigquery.v2/ConnectionProperty
   [:or
    {:error/message "must be single-entry map or [string string] tuple"}
    [:and
     [:map-of :string :string]
     [:fn '(fn [v] (= 1 (count v)))]]
    [:tuple :string :string]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))