;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.GoogleSheetsOptions
  {:doc "Google BigQuery options for the Google Sheets format."
   :file-git-sha "e0fe2fa71024889dfc47b91f8c9bbacf5540d7d8"
   :fqcn "com.google.cloud.bigquery.GoogleSheetsOptions"
   :gcp.dev/certification
     {:base-seed 1790034048366
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034048366 :standard 1790034048367 :stress 1790034048368}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:40:49.678922286Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery GoogleSheetsOptions
            GoogleSheetsOptions$Builder]))

(declare from-edn to-edn)

(defn ^GoogleSheetsOptions from-edn
  [arg]
  (global/strict! :gcp.bigquery/GoogleSheetsOptions arg)
  (let [builder (GoogleSheetsOptions/newBuilder)]
    (when (some? (get arg :range)) (.setRange builder (get arg :range)))
    (when (some? (get arg :skipLeadingRows))
      (.setSkipLeadingRows builder (long (get arg :skipLeadingRows))))
    (.build builder)))

(defn to-edn
  [^GoogleSheetsOptions arg]
  {:post [(global/strict! :gcp.bigquery/GoogleSheetsOptions %)]}
  (when arg
    (cond-> {:type "GOOGLE_SHEETS"}
      (some->> (.getRange arg)
               (not= ""))
        (assoc :range (.getRange arg))
      (.getSkipLeadingRows arg) (assoc :skipLeadingRows
                                  (.getSkipLeadingRows arg)))))

(def schema
  [:map
   {:closed true,
    :doc "Google BigQuery options for the Google Sheets format.",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bigquery/GoogleSheetsOptions} [:type [:= "GOOGLE_SHEETS"]]
   [:range
    {:optional true,
     :getter-doc
       "Returns the number of range of a sheet when reading the data.",
     :setter-doc
       "[Optional] Range of a sheet to query from. Only used when non-empty. Typical format:\nsheet_name!top_left_cell_id:bottom_right_cell_id For example: sheet1!A1:B20\n\n@param range or {@code null} for none"}
    [:string {:min 1, :gen/max 1}]]
   [:skipLeadingRows
    {:optional true,
     :getter-doc
       "Returns the number of rows at the top of a sheet that BigQuery will skip when reading the data.",
     :setter-doc
       "Sets the number of rows at the top of a sheet that BigQuery will skip when reading the data.\nThe default value is 0. This property is useful if you have header rows that should be\nskipped."}
    :i64]])

(global/include-registry! "gcp.bigquery.GoogleSheetsOptions"
                          {:gcp.bigquery/GoogleSheetsOptions schema})