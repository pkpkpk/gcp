(ns gcp.bigquery.v2.GoogleSheetsOptions
  (:import [com.google.cloud.bigquery GoogleSheetsOptions])
  (:require [gcp.global :as global]))

(defn ^GoogleSheetsOptions from-edn
  [arg]
  (global/strict! :gcp.bigquery.v2/GoogleSheetsOptions arg)
  (let [builder (GoogleSheetsOptions/newBuilder)]
    (when (get arg :range) (.setRange builder (get arg :range)))
    (when (get arg :skipLeadingRows)
      (.setSkipLeadingRows builder (get arg :skipLeadingRows)))
    (.build builder)))

(defn to-edn
  [^GoogleSheetsOptions arg]
  {:post [(global/strict! :gcp.bigquery.v2/GoogleSheetsOptions %)]}
  (cond-> {}
    (get arg :range) (assoc :range (.getRange arg))
    (get arg :skipLeadingRows) (assoc :skipLeadingRows
                                 (.getSkipLeadingRows arg))))

(def schemas
  {:gcp.bigquery.v2/GoogleSheetsOptions
   [:map {:closed true}
    [:type {:optional true} [:= "GOOGLE_SHEETS"]]
    [:range {:optional true} :string]
    [:skipLeadingRows {:optional true} :int]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))