;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.GoogleSheetsOptions
  {:doc "Google BigQuery options for the Google Sheets format."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.GoogleSheetsOptions"
   :gcp.dev/certification
     {:base-seed 1775130849071
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130849071 :standard 1775130849072 :stress 1775130849073}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:54:10.243899762Z"}}
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
    [:string {:min 1}]]
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