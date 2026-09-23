;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.MetadataCacheStats
  {:doc
     "Represents statistics for metadata caching in BigLake tables.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/biglake-intro\">BigLake Tables</a>"
   :file-git-sha "cddead933ec9e24f8173156d6bdc3b0c4cb4cf8d"
   :fqcn "com.google.cloud.bigquery.MetadataCacheStats"
   :gcp.dev/certification
     {:base-seed 1790034137241
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034137241 :standard 1790034137242 :stress 1790034137243}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:42:18.320404591Z"}}
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

(global/include-registry! "gcp.bigquery.MetadataCacheStats"
                          {:gcp.bigquery/MetadataCacheStats schema})