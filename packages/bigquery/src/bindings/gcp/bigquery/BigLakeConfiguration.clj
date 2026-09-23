;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.BigLakeConfiguration
  {:doc nil
   :file-git-sha "e2cca4df5f4702e16942211dfa4c5274d41bb12e"
   :fqcn "com.google.cloud.bigquery.BigLakeConfiguration"
   :gcp.dev/certification
     {:base-seed 1790034092473
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034092473 :standard 1790034092474 :stress 1790034092475}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:41:33.533731840Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery BigLakeConfiguration
            BigLakeConfiguration$Builder]))

(declare from-edn to-edn)

(defn ^BigLakeConfiguration from-edn
  [arg]
  (global/strict! :gcp.bigquery/BigLakeConfiguration arg)
  (let [builder (BigLakeConfiguration/newBuilder)]
    (when (some? (get arg :connectionId))
      (.setConnectionId builder (get arg :connectionId)))
    (when (some? (get arg :fileFormat))
      (.setFileFormat builder (get arg :fileFormat)))
    (when (some? (get arg :storageUri))
      (.setStorageUri builder (get arg :storageUri)))
    (when (some? (get arg :tableFormat))
      (.setTableFormat builder (get arg :tableFormat)))
    (.build builder)))

(defn to-edn
  [^BigLakeConfiguration arg]
  {:post [(global/strict! :gcp.bigquery/BigLakeConfiguration %)]}
  (when arg
    (cond-> {}
      (some->> (.getConnectionId arg)
               (not= ""))
        (assoc :connectionId (.getConnectionId arg))
      (some->> (.getFileFormat arg)
               (not= ""))
        (assoc :fileFormat (.getFileFormat arg))
      (some->> (.getStorageUri arg)
               (not= ""))
        (assoc :storageUri (.getStorageUri arg))
      (some->> (.getTableFormat arg)
               (not= ""))
        (assoc :tableFormat (.getTableFormat arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/BigLakeConfiguration}
   [:connectionId
    {:optional true,
     :getter-doc
       "Credential reference for accessing external storage system. Normalized as\nproject_id.location_id.connection_id.\n\n@return value or {@code null} for none",
     :setter-doc
       "Credential reference for accessing external storage system. Normalized as\nproject_id.location_id.connection_id.\n\n@param connectionId connectionId or {@code null} for none"}
    [:string {:min 1, :gen/max 1}]]
   [:fileFormat
    {:optional true,
     :getter-doc
       "Open source file format that the table data is stored in. Currently only PARQUET is supported.\n\n@return value or {@code null} for none",
     :setter-doc
       "Open source file format that the table data is stored in. Currently only PARQUET is\nsupported.\n\n@param fileFormat fileFormat or {@code null} for none"}
    [:string {:min 1, :gen/max 1}]]
   [:storageUri
    {:optional true,
     :getter-doc
       "Fully qualified location prefix of the external folder where data is stored. Starts with\n\"gs://\" ends with \"/\". Does not contain \"*\".\n\n@return value or {@code null} for none",
     :setter-doc
       "Fully qualified location prefix of the external folder where data is stored. Starts with\n\"gs://\" and ends with \"/\". Does not contain \"*\".\n\n@param storageUri storageUri or {@code null} for none"}
    [:string {:min 1, :gen/max 1}]]
   [:tableFormat
    {:optional true,
     :getter-doc
       "Open source table format that the table data is stored in. Currently only ICEBERG is supported.\n\n@return value or {@code null} for none",
     :setter-doc
       "Open source table format that the table data is stored in. Currently only ICEBERG is\nsupported.\n\n@param tableFormat tableFormat or {@code null} for none"}
    [:string {:min 1, :gen/max 1}]]])

(global/include-registry! "gcp.bigquery.BigLakeConfiguration"
                          {:gcp.bigquery/BigLakeConfiguration schema})