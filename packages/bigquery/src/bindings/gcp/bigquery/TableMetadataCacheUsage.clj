;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.TableMetadataCacheUsage
  {:doc "Represents Table level detail on the usage of metadata caching."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.TableMetadataCacheUsage"
   :gcp.dev/certification
     {:base-seed 1775130892393
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130892393 :standard 1775130892394 :stress 1775130892395}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:54:53.369009050Z"}}
  (:require [gcp.bigquery.TableId :as TableId]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery TableMetadataCacheUsage
            TableMetadataCacheUsage$Builder
            TableMetadataCacheUsage$UnusedReason]))

(declare from-edn to-edn UnusedReason-from-edn UnusedReason-to-edn)

(def UnusedReason-schema
  [:enum
   {:closed true,
    :doc "Reason for not using metadata caching for the table.",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bigquery/TableMetadataCacheUsage.UnusedReason}
   "UNUSED_REASON_UNSPECIFIED" "EXCEEDED_MAX_STALENESS"
   "METADATA_CACHING_NOT_ENABLED" "OTHER_REASON"])

(defn ^TableMetadataCacheUsage from-edn
  [arg]
  (global/strict! :gcp.bigquery/TableMetadataCacheUsage arg)
  (let [builder (TableMetadataCacheUsage/newBuilder)]
    (when (some? (get arg :explanation))
      (.setExplanation builder (get arg :explanation)))
    (when (some? (get arg :tableReference))
      (.setTableReference builder (TableId/from-edn (get arg :tableReference))))
    (when (some? (get arg :tableType))
      (.setTableType builder (get arg :tableType)))
    (when (some? (get arg :unusedReason))
      (.setUnusedReason builder
                        (TableMetadataCacheUsage$UnusedReason/valueOf
                          (get arg :unusedReason))))
    (.build builder)))

(defn to-edn
  [^TableMetadataCacheUsage arg]
  {:post [(global/strict! :gcp.bigquery/TableMetadataCacheUsage %)]}
  (when arg
    (cond-> {}
      (some->> (.getExplanation arg)
               (not= ""))
        (assoc :explanation (.getExplanation arg))
      (.getTableReference arg) (assoc :tableReference
                                 (TableId/to-edn (.getTableReference arg)))
      (some->> (.getTableType arg)
               (not= ""))
        (assoc :tableType (.getTableType arg))
      (.getUnusedReason arg) (assoc :unusedReason
                               (.name (.getUnusedReason arg))))))

(def schema
  [:map
   {:closed true,
    :doc "Represents Table level detail on the usage of metadata caching.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/TableMetadataCacheUsage}
   [:explanation
    {:optional true,
     :setter-doc
       "Sets the free form human-readable reason metadata caching was unused for the job."}
    [:string {:min 1}]]
   [:tableReference
    {:optional true,
     :setter-doc
       "Sets the metadata caching eligible table referenced in the query."}
    :gcp.bigquery/TableId]
   [:tableType {:optional true, :setter-doc "Sets the table type."}
    [:string {:min 1}]]
   [:unusedReason
    {:optional true,
     :setter-doc "Sets reason for not using metadata caching for the table."}
    [:enum {:closed true} "UNUSED_REASON_UNSPECIFIED" "EXCEEDED_MAX_STALENESS"
     "METADATA_CACHING_NOT_ENABLED" "OTHER_REASON"]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/TableMetadataCacheUsage schema,
              :gcp.bigquery/TableMetadataCacheUsage.UnusedReason
                UnusedReason-schema}
    {:gcp.global/name "gcp.bigquery.TableMetadataCacheUsage"}))