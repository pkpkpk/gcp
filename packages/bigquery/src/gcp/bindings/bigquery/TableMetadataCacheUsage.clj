;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.TableMetadataCacheUsage
  {:doc "Represents Table level detail on the usage of metadata caching."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.TableMetadataCacheUsage"
   :gcp.dev/certification
     {:base-seed 1772047164204
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772047164204 :standard 1772047164205 :stress 1772047164206}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-25T19:19:24.220690715Z"}}
  (:require [gcp.bindings.bigquery.TableId :as TableId]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery TableMetadataCacheUsage
            TableMetadataCacheUsage$Builder
            TableMetadataCacheUsage$UnusedReason]))

(declare TableMetadataCacheUsage$UnusedReason-from-edn
         TableMetadataCacheUsage$UnusedReason-to-edn)

(def TableMetadataCacheUsage$UnusedReason-schema
  [:enum
   {:closed true,
    :doc "Reason for not using metadata caching for the table.",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bindings.bigquery/TableMetadataCacheUsage.UnusedReason}
   "UNUSED_REASON_UNSPECIFIED" "EXCEEDED_MAX_STALENESS"
   "METADATA_CACHING_NOT_ENABLED" "OTHER_REASON"])

(defn ^TableMetadataCacheUsage from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/TableMetadataCacheUsage arg)
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
  {:post [(global/strict! :gcp.bindings.bigquery/TableMetadataCacheUsage %)]}
  (cond-> {}
    (.getExplanation arg) (assoc :explanation (.getExplanation arg))
    (.getTableReference arg) (assoc :tableReference
                               (TableId/to-edn (.getTableReference arg)))
    (.getTableType arg) (assoc :tableType (.getTableType arg))
    (.getUnusedReason arg) (assoc :unusedReason
                             (.name (.getUnusedReason arg)))))

(def schema
  [:map
   {:closed true,
    :doc "Represents Table level detail on the usage of metadata caching.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/TableMetadataCacheUsage}
   [:explanation
    {:optional true,
     :setter-doc
       "Sets the free form human-readable reason metadata caching was unused for the job."}
    [:string {:min 1}]]
   [:tableReference
    {:optional true,
     :setter-doc
       "Sets the metadata caching eligible table referenced in the query."}
    :gcp.bindings.bigquery/TableId]
   [:tableType {:optional true, :setter-doc "Sets the table type."}
    [:string {:min 1}]]
   [:unusedReason
    {:optional true,
     :setter-doc "Sets reason for not using metadata caching for the table."}
    [:enum {:closed true} "UNUSED_REASON_UNSPECIFIED" "EXCEEDED_MAX_STALENESS"
     "METADATA_CACHING_NOT_ENABLED" "OTHER_REASON"]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/TableMetadataCacheUsage schema,
              :gcp.bindings.bigquery/TableMetadataCacheUsage.UnusedReason
                TableMetadataCacheUsage$UnusedReason-schema}
    {:gcp.global/name "gcp.bindings.bigquery.TableMetadataCacheUsage"}))