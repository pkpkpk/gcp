;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.GoogleSheetsOptions
  {:doc "Google BigQuery options for the Google Sheets format."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.GoogleSheetsOptions"
   :gcp.dev/certification
     {:base-seed 1771346970904
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771346970904 :standard 1771346970905 :stress 1771346970906}
      :protocol-hash
        "32175171656ac95c72455bff25fe019346fb52c916efee805be149692e31cec4"
      :timestamp "2026-02-17T16:49:30.924470675Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery GoogleSheetsOptions
            GoogleSheetsOptions$Builder]))

(defn ^GoogleSheetsOptions from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/GoogleSheetsOptions arg)
  (let [builder (GoogleSheetsOptions/newBuilder)]
    (when (some? (get arg :range)) (.setRange builder (get arg :range)))
    (when (some? (get arg :skipLeadingRows))
      (.setSkipLeadingRows builder (get arg :skipLeadingRows)))
    (.build builder)))

(defn to-edn
  [^GoogleSheetsOptions arg]
  {:post [(global/strict! :gcp.bindings.bigquery/GoogleSheetsOptions %)]}
  (cond-> {:type "GOOGLE_SHEETS"}
    (.getRange arg) (assoc :range (.getRange arg))
    (.getSkipLeadingRows arg) (assoc :skipLeadingRows
                                (.getSkipLeadingRows arg))))

(def schema
  [:map
   {:closed true,
    :doc "Google BigQuery options for the Google Sheets format.",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bindings.bigquery/GoogleSheetsOptions}
   [:type [:= "GOOGLE_SHEETS"]]
   [:skipLeadingRows
    {:optional true,
     :getter-doc
       "Returns the number of rows at the top of a sheet that BigQuery will skip when reading the data.",
     :setter-doc
       "Sets the number of rows at the top of a sheet that BigQuery will skip when reading the data.\nThe default value is 0. This property is useful if you have header rows that should be\nskipped."}
    :int]
   [:range
    {:optional true,
     :getter-doc
       "Returns the number of range of a sheet when reading the data.",
     :setter-doc
       "[Optional] Range of a sheet to query from. Only used when non-empty. Typical format:\nsheet_name!top_left_cell_id:bottom_right_cell_id For example: sheet1!A1:B20\n\n@param range or {@code null} for none"}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/GoogleSheetsOptions schema}
    {:gcp.global/name "gcp.bindings.bigquery.GoogleSheetsOptions"}))