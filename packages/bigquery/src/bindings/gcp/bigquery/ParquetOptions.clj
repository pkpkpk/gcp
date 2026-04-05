;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ParquetOptions
  {:doc nil
   :file-git-sha "a335927e16d0907d62e584f08fa8393daae40354"
   :fqcn "com.google.cloud.bigquery.ParquetOptions"
   :gcp.dev/certification
     {:base-seed 1775130850655
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130850655 :standard 1775130850656 :stress 1775130850657}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:54:11.882605112Z"}}
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
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/ParquetOptions schema}
    {:gcp.global/name "gcp.bigquery.ParquetOptions"}))