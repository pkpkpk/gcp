;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.TableMetadataCacheUsage
  {:doc "Represents Table level detail on the usage of metadata caching."
   :file-git-sha "cddead933ec9e24f8173156d6bdc3b0c4cb4cf8d"
   :fqcn "com.google.cloud.bigquery.TableMetadataCacheUsage"
   :gcp.dev/certification
     {:base-seed 1790034087954
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034087954 :standard 1790034087955 :stress 1790034087956}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:41:29.002064135Z"}}
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
    [:string {:min 1, :gen/max 1}]]
   [:tableReference
    {:optional true,
     :setter-doc
       "Sets the metadata caching eligible table referenced in the query."}
    :gcp.bigquery/TableId]
   [:tableType {:optional true, :setter-doc "Sets the table type."}
    [:string {:min 1, :gen/max 1}]]
   [:unusedReason
    {:optional true,
     :setter-doc "Sets reason for not using metadata caching for the table."}
    [:enum {:closed true} "UNUSED_REASON_UNSPECIFIED" "EXCEEDED_MAX_STALENESS"
     "METADATA_CACHING_NOT_ENABLED" "OTHER_REASON"]]])

(global/include-registry! "gcp.bigquery.TableMetadataCacheUsage"
                          {:gcp.bigquery/TableMetadataCacheUsage schema,
                           :gcp.bigquery/TableMetadataCacheUsage.UnusedReason
                             UnusedReason-schema})