;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.BigLakeConfiguration
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.BigLakeConfiguration"
   :gcp.dev/certification
     {:base-seed 1776499389711
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499389711 :standard 1776499389712 :stress 1776499389713}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:03:11.047035546Z"}}
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
    (cond-> {:connectionId (.getConnectionId arg),
             :fileFormat (.getFileFormat arg),
             :storageUri (.getStorageUri arg),
             :tableFormat (.getTableFormat arg)})))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/BigLakeConfiguration}
   [:connectionId
    {:getter-doc
       "Credential reference for accessing external storage system. Normalized as\nproject_id.location_id.connection_id.\n\n@return value or {@code null} for none",
     :setter-doc
       "[Required] Required and immutable. Credential reference for accessing external storage\nsystem. Normalized as project_id.location_id.connection_id.\n\n@param connectionId connectionId or {@code null} for none"}
    [:string {:min 1}]]
   [:fileFormat
    {:getter-doc
       "Open source file format that the table data is stored in. Currently only PARQUET is supported.\n\n@return value or {@code null} for none",
     :setter-doc
       "[Required] Required and immutable. Open source file format that the table data is stored in.\nCurrently only PARQUET is supported.\n\n@param fileFormat fileFormat or {@code null} for none"}
    [:string {:min 1}]]
   [:storageUri
    {:getter-doc
       "Fully qualified location prefix of the external folder where data is stored. Starts with\n\"gs://\" ends with \"/\". Does not contain \"*\".\n\n@return value or {@code null} for none",
     :setter-doc
       "[Required] Required and immutable. Fully qualified location prefix of the external folder\nwhere data is stored. Starts with \"gs://\" and ends with \"/\". Does not contain \"*\".\n\n@param storageUri storageUri or {@code null} for none"}
    [:string {:min 1}]]
   [:tableFormat
    {:getter-doc
       "Open source file format that the table data is stored in. Currently only PARQUET is supported.\n\n@return value or {@code null} for none",
     :setter-doc
       "[Required] Required and immutable. Open source file format that the table data is stored in.\nCurrently only PARQUET is supported.\n\n@param tableFormat tableFormat or {@code null} for none"}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/BigLakeConfiguration schema}
    {:gcp.global/name "gcp.bigquery.BigLakeConfiguration"}))