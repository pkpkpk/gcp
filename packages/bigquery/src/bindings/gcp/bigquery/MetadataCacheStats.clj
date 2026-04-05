;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.MetadataCacheStats
  {:doc
     "Represents statistics for metadata caching in BigLake tables.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/biglake-intro\">BigLake Tables</a>"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.MetadataCacheStats"
   :gcp.dev/certification
     {:base-seed 1775130942639
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130942639 :standard 1775130942640 :stress 1775130942641}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:55:43.873814195Z"}}
  (:require [gcp.bigquery.TableMetadataCacheUsage :as TableMetadataCacheUsage]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery MetadataCacheStats
            MetadataCacheStats$Builder]))

(declare from-edn to-edn)

(defn ^MetadataCacheStats from-edn
  [arg]
  (global/strict! :gcp.bigquery/MetadataCacheStats arg)
  (let [builder (MetadataCacheStats/newBuilder)]
    (when (seq (get arg :tableMetadataCacheUsage))
      (.setTableMetadataCacheUsage builder
                                   (map TableMetadataCacheUsage/from-edn
                                     (get arg :tableMetadataCacheUsage))))
    (.build builder)))

(defn to-edn
  [^MetadataCacheStats arg]
  {:post [(global/strict! :gcp.bigquery/MetadataCacheStats %)]}
  (when arg
    (cond-> {}
      (seq (.getTableMetadataCacheUsage arg))
        (assoc :tableMetadataCacheUsage
          (map TableMetadataCacheUsage/to-edn
            (.getTableMetadataCacheUsage arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "Represents statistics for metadata caching in BigLake tables.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/biglake-intro\">BigLake Tables</a>",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/MetadataCacheStats}
   [:tableMetadataCacheUsage
    {:optional true,
     :setter-doc
       "Sets the free form human-readable reason metadata caching was unused for the job."}
    [:sequential {:min 1} :gcp.bigquery/TableMetadataCacheUsage]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/MetadataCacheStats schema}
    {:gcp.global/name "gcp.bigquery.MetadataCacheStats"}))