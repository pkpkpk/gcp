;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ParquetOptions
  {:doc nil
   :file-git-sha "7a51614ff8754158b545420f0864ec4b1c27c82b"
   :fqcn "com.google.cloud.bigquery.ParquetOptions"
   :gcp.dev/certification
     {:base-seed 1790034050102
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034050102 :standard 1790034050103 :stress 1790034050104}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:40:51.586589940Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery ParquetOptions ParquetOptions$Builder]))

(declare from-edn to-edn)

(defn ^ParquetOptions from-edn
  [arg]
  (global/strict! :gcp.bigquery/ParquetOptions arg)
  (let [builder (ParquetOptions/newBuilder)]
    (when (some? (get arg :enableListInference))
      (.setEnableListInference builder (get arg :enableListInference)))
    (when (some? (get arg :enumAsString))
      (.setEnumAsString builder (get arg :enumAsString)))
    (when (some? (get arg :mapTargetType))
      (.setMapTargetType builder (get arg :mapTargetType)))
    (.build builder)))

(defn to-edn
  [^ParquetOptions arg]
  {:post [(global/strict! :gcp.bigquery/ParquetOptions %)]}
  (when arg
    (cond-> {:type "PARQUET"}
      (.getEnableListInference arg) (assoc :enableListInference
                                      (.getEnableListInference arg))
      (.getEnumAsString arg) (assoc :enumAsString (.getEnumAsString arg))
      (some->> (.getMapTargetType arg)
               (not= ""))
        (assoc :mapTargetType (.getMapTargetType arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bigquery/ParquetOptions} [:type [:= "PARQUET"]]
   [:enableListInference {:optional true} :boolean]
   [:enumAsString {:optional true} :boolean]
   [:mapTargetType
    {:optional true,
     :getter-doc "Returns how the Parquet map is represented.",
     :setter-doc
       "[Optional] Indicates how to represent a Parquet map if present.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/reference/rest/v2/tables#maptargettype\">\n    MapTargetType</a>"}
    [:string {:min 1, :gen/max 1}]]])

(global/include-registry! "gcp.bigquery.ParquetOptions"
                          {:gcp.bigquery/ParquetOptions schema})