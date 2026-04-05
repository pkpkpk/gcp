;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.IndexUnusedReason
  {:doc "Represents Reason of why the index was not used in a SQL search."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.IndexUnusedReason"
   :gcp.dev/certification
     {:base-seed 1775130889401
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130889401 :standard 1775130889402 :stress 1775130889403}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:54:50.377751552Z"}}
  (:require [gcp.bigquery.TableId :as TableId]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery IndexUnusedReason
            IndexUnusedReason$Builder]))

(declare from-edn to-edn)

(defn ^IndexUnusedReason from-edn
  [arg]
  (global/strict! :gcp.bigquery/IndexUnusedReason arg)
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
  {:post [(global/strict! :gcp.bigquery/IndexUnusedReason %)]}
  (when arg
    (cond-> {}
      (.getBaseTableId arg) (assoc :baseTableId
                              (TableId/to-edn (.getBaseTableId arg)))
      (some->> (.getCode arg)
               (not= ""))
        (assoc :code (.getCode arg))
      (some->> (.getIndexName arg)
               (not= ""))
        (assoc :indexName (.getIndexName arg))
      (some->> (.getMessage arg)
               (not= ""))
        (assoc :message (.getMessage arg)))))

(def schema
  [:map
   {:closed true,
    :doc "Represents Reason of why the index was not used in a SQL search.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/IndexUnusedReason}
   [:baseTableId
    {:optional true,
     :getter-doc
       "Returns the base table involved in the reason that no search index was used.\n\n@return value or {@code null} for none",
     :setter-doc
       "Specifies the base table involved in the reason that no search index was used.\n\n@param baseTable baseTable or {@code null} for none"}
    :gcp.bigquery/TableId]
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
  (with-meta {:gcp.bigquery/IndexUnusedReason schema}
    {:gcp.global/name "gcp.bigquery.IndexUnusedReason"}))