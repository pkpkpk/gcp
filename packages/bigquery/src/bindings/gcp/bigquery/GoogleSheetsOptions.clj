;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.GoogleSheetsOptions
  {:doc "Google BigQuery options for the Google Sheets format."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.GoogleSheetsOptions"
   :gcp.dev/certification
     {:base-seed 1779204634399
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204634399 :standard 1779204634400 :stress 1779204634401}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:35.168770803Z"}}
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

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/GoogleSheetsOptions schema}
    {:gcp.global/name "gcp.bigquery.GoogleSheetsOptions"}))