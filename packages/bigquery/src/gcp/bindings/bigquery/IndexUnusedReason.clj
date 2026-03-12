;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.IndexUnusedReason
  {:doc "Represents Reason of why the index was not used in a SQL search."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.IndexUnusedReason"
   :gcp.dev/certification
     {:base-seed 1772045906675
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772045906675 :standard 1772045906676 :stress 1772045906677}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-25T18:58:26.686636462Z"}}
  (:require [gcp.bindings.bigquery.TableId :as TableId]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery IndexUnusedReason
            IndexUnusedReason$Builder]))

(defn ^IndexUnusedReason from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/IndexUnusedReason arg)
  (let [builder (IndexUnusedReason/newBuilder)]
    (when (some? (get arg :baseTableId))
      (.setBaseTableId builder (TableId/from-edn (get arg :baseTableId))))
    (when (some? (get arg :code)) (.setCode builder (get arg :code)))
    (when (some? (get arg :indexName))
      (.setIndexName builder (get arg :indexName)))
    (when (some? (get arg :message)) (.setMessage builder (get arg :message)))
    (.build builder)))

(defn to-edn
  [^IndexUnusedReason arg]
  {:post [(global/strict! :gcp.bindings.bigquery/IndexUnusedReason %)]}
  (cond-> {}
    (.getBaseTableId arg) (assoc :baseTableId
                            (TableId/to-edn (.getBaseTableId arg)))
    (.getCode arg) (assoc :code (.getCode arg))
    (.getIndexName arg) (assoc :indexName (.getIndexName arg))
    (.getMessage arg) (assoc :message (.getMessage arg))))

(def schema
  [:map
   {:closed true,
    :doc "Represents Reason of why the index was not used in a SQL search.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/IndexUnusedReason}
   [:baseTableId
    {:optional true,
     :getter-doc
       "Returns the base table involved in the reason that no search index was used.\n\n@return value or {@code null} for none",
     :setter-doc
       "Specifies the base table involved in the reason that no search index was used.\n\n@param baseTable baseTable or {@code null} for none"}
    :gcp.bindings.bigquery/TableId]
   [:code
    {:optional true,
     :getter-doc
       "Returns the high-level reason for the scenario when no search index was used.\n\n@return value or {@code null} for none",
     :setter-doc
       "Specifies the high-level reason for the scenario when no search index was used.\n\n@param code code or {@code null} for none"}
    [:string {:min 1}]]
   [:indexName
    {:optional true,
     :getter-doc
       "Returns the name of the unused search index, if available.\n\n@return value or {@code null} for none",
     :setter-doc
       "Specifies the name of the unused search index, if available.\n\n@param indexName indexName or {@code null} for none"}
    [:string {:min 1}]]
   [:message
    {:optional true,
     :getter-doc
       "Returns free form human-readable reason for the scenario when no search index was used.\n\n@return value or {@code null} for none",
     :setter-doc
       "Free form human-readable reason for the scenario when no search index was used.\n\n@param message message or {@code null} for none"}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/IndexUnusedReason schema}
    {:gcp.global/name "gcp.bindings.bigquery.IndexUnusedReason"}))