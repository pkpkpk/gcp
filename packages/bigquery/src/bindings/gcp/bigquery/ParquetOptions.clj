;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ParquetOptions
  {:doc nil
   :file-git-sha "a335927e16d0907d62e584f08fa8393daae40354"
   :fqcn "com.google.cloud.bigquery.ParquetOptions"
   :gcp.dev/certification
     {:base-seed 1779204635424
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204635424 :standard 1779204635425 :stress 1779204635426}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:36.172796764Z"}}
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

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/ParquetOptions schema}
    {:gcp.global/name "gcp.bigquery.ParquetOptions"}))