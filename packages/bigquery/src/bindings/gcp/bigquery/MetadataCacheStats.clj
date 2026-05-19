;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.MetadataCacheStats
  {:doc
     "Represents statistics for metadata caching in BigLake tables.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/biglake-intro\">BigLake Tables</a>"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.MetadataCacheStats"
   :gcp.dev/certification
     {:base-seed 1779204699614
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204699614 :standard 1779204699615 :stress 1779204699616}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:31:40.470610728Z"}}
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
                                   (mapv TableMetadataCacheUsage/from-edn
                                     (get arg :tableMetadataCacheUsage))))
    (.build builder)))

(defn to-edn
  [^MetadataCacheStats arg]
  {:post [(global/strict! :gcp.bigquery/MetadataCacheStats %)]}
  (when arg
    (cond-> {}
      (seq (.getTableMetadataCacheUsage arg))
        (assoc :tableMetadataCacheUsage
          (mapv TableMetadataCacheUsage/to-edn
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
    [:sequential {:min 1, :gen/max 2} :gcp.bigquery/TableMetadataCacheUsage]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/MetadataCacheStats schema}
    {:gcp.global/name "gcp.bigquery.MetadataCacheStats"}))