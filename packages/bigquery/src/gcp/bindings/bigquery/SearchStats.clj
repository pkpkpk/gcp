;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.SearchStats
  {:doc "Represents Search statistics information of a search query."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.SearchStats"
   :gcp.dev/certification
     {:base-seed 1772045910584
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772045910584 :standard 1772045910585 :stress 1772045910586}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-25T18:58:30.624833165Z"}}
  (:require [gcp.bindings.bigquery.IndexUnusedReason :as IndexUnusedReason]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery SearchStats SearchStats$Builder]))

(defn ^SearchStats from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/SearchStats arg)
  (let [builder (SearchStats/newBuilder)]
    (when (some? (get arg :indexUnusedReasons))
      (.setIndexUnusedReasons builder
                              (map IndexUnusedReason/from-edn
                                (get arg :indexUnusedReasons))))
    (when (some? (get arg :indexUsageMode))
      (.setIndexUsageMode builder (get arg :indexUsageMode)))
    (.build builder)))

(defn to-edn
  [^SearchStats arg]
  {:post [(global/strict! :gcp.bindings.bigquery/SearchStats %)]}
  (cond-> {}
    (.getIndexUnusedReasons arg) (assoc :indexUnusedReasons
                                   (map IndexUnusedReason/to-edn
                                     (.getIndexUnusedReasons arg)))
    (.getIndexUsageMode arg) (assoc :indexUsageMode (.getIndexUsageMode arg))))

(def schema
  [:map
   {:closed true,
    :doc "Represents Search statistics information of a search query.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/SearchStats}
   [:indexUnusedReasons
    {:optional true,
     :setter-doc
       "When index_usage_mode is UNUSED or PARTIALLY_USED, this field explains why index was not used\nin all or part of the search query. If index_usage_mode is FULLY_USED, this field is not\npopulated.\n\n@param indexUnusedReasons"}
    [:sequential {:min 1} :gcp.bindings.bigquery/IndexUnusedReason]]
   [:indexUsageMode
    {:optional true,
     :setter-doc
       "Specifies index usage mode for the query.\n\n@param indexUsageMode, has three modes UNUSED, PARTIALLY_USED, and FULLY_USED"}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/SearchStats schema}
    {:gcp.global/name "gcp.bindings.bigquery.SearchStats"}))