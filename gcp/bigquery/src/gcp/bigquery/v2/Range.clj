(ns gcp.bigquery.v2.Range
  (:require [gcp.bigquery.v2.FieldElementType :as FieldElementType])
  (:import (com.google.cloud.bigquery Range Range$Builder)))

(defn ^Range from-edn
  [{:keys [end start type]}]
  (let [builder (Range$Builder.)]
    (some-> end (.setEnd builder))
    (some-> start (.setStart builder))
    (when type
      (.setType builder (FieldElementType/from-edn type)))
    (.build builder)))

(defn to-edn [^Range arg]
  (cond-> {}
          (some? (.getEnd arg))
          (assoc :end (.getEnd arg))
          (some? (.getStart arg))
          (assoc :start (.getStart arg))
          (some? (.getType arg))
          (assoc :type (FieldElementType/from-edn (.getType arg)))
          (some? (.getValues arg))
          (assoc :values (.getValues arg))))