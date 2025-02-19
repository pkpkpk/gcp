(ns gcp.bigquery.v2.GoogleSheetsOptions
  (:import [com.google.cloud.bigquery GoogleSheetsOptions])
  (:require [gcp.global :as g]))

(defn ^GoogleSheetsOptions from-edn
  [arg]
  (gcp.global/strict! :gcp/bigquery.GoogleSheetsOptions arg)
  (let [builder (GoogleSheetsOptions/newBuilder)]
    (when (get arg :range) (.setRange builder (get arg :range)))
    (when (get arg :skipLeadingRows)
      (.setSkipLeadingRows builder (get arg :skipLeadingRows)))
    (.build builder)))

(defn to-edn
  [^GoogleSheetsOptions arg]
  {:post [(gcp.global/strict! :gcp/bigquery.GoogleSheetsOptions %)]}
  (cond-> {}
    (get arg :range) (assoc :range (.getRange arg))
    (get arg :skipLeadingRows) (assoc :skipLeadingRows
                                 (.getSkipLeadingRows arg))))