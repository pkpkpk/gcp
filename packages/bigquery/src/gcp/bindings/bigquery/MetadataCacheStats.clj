;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.MetadataCacheStats
  {:doc
     "Represents statistics for metadata caching in BigLake tables.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/biglake-intro\">BigLake Tables</a>"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.MetadataCacheStats"
   :gcp.dev/certification
     {:base-seed 1772047181290
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772047181290 :standard 1772047181291 :stress 1772047181292}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-25T19:19:41.324775856Z"}}
  (:require [gcp.bindings.bigquery.TableMetadataCacheUsage :as
             TableMetadataCacheUsage]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery MetadataCacheStats
            MetadataCacheStats$Builder]))

(defn ^MetadataCacheStats from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/MetadataCacheStats arg)
  (let [builder (MetadataCacheStats/newBuilder)]
    (when (some? (get arg :tableMetadataCacheUsage))
      (.setTableMetadataCacheUsage builder
                                   (map TableMetadataCacheUsage/from-edn
                                     (get arg :tableMetadataCacheUsage))))
    (.build builder)))

(defn to-edn
  [^MetadataCacheStats arg]
  {:post [(global/strict! :gcp.bindings.bigquery/MetadataCacheStats %)]}
  (cond-> {}
    (.getTableMetadataCacheUsage arg) (assoc :tableMetadataCacheUsage
                                        (map TableMetadataCacheUsage/to-edn
                                          (.getTableMetadataCacheUsage arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Represents statistics for metadata caching in BigLake tables.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/biglake-intro\">BigLake Tables</a>",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/MetadataCacheStats}
   [:tableMetadataCacheUsage
    {:optional true,
     :setter-doc
       "Sets the free form human-readable reason metadata caching was unused for the job."}
    [:sequential {:min 1} :gcp.bindings.bigquery/TableMetadataCacheUsage]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/MetadataCacheStats schema}
    {:gcp.global/name "gcp.bindings.bigquery.MetadataCacheStats"}))