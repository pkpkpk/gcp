;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.SearchStats
  {:doc "Represents Search statistics information of a search query."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.SearchStats"
   :gcp.dev/certification
     {:base-seed 1776499436086
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499436086 :standard 1776499436087 :stress 1776499436088}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:03:57.394438835Z"}}
  (:require [gcp.bigquery.IndexUnusedReason :as IndexUnusedReason]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery SearchStats SearchStats$Builder]))

(declare from-edn to-edn)

(defn ^SearchStats from-edn
  [arg]
  (global/strict! :gcp.bigquery/SearchStats arg)
  (let [builder (SearchStats/newBuilder)]
    (when (seq (get arg :indexUnusedReasons))
      (.setIndexUnusedReasons builder
                              (mapv IndexUnusedReason/from-edn
                                (get arg :indexUnusedReasons))))
    (when (some? (get arg :indexUsageMode))
      (.setIndexUsageMode builder (get arg :indexUsageMode)))
    (.build builder)))

(defn to-edn
  [^SearchStats arg]
  {:post [(global/strict! :gcp.bigquery/SearchStats %)]}
  (when arg
    (cond-> {}
      (seq (.getIndexUnusedReasons arg)) (assoc :indexUnusedReasons
                                           (mapv IndexUnusedReason/to-edn
                                             (.getIndexUnusedReasons arg)))
      (some->> (.getIndexUsageMode arg)
               (not= ""))
        (assoc :indexUsageMode (.getIndexUsageMode arg)))))

(def schema
  [:map
   {:closed true,
    :doc "Represents Search statistics information of a search query.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/SearchStats}
   [:indexUnusedReasons
    {:optional true,
     :setter-doc
       "When index_usage_mode is UNUSED or PARTIALLY_USED, this field explains why index was not used\nin all or part of the search query. If index_usage_mode is FULLY_USED, this field is not\npopulated.\n\n@param indexUnusedReasons"}
    [:sequential {:min 1} :gcp.bigquery/IndexUnusedReason]]
   [:indexUsageMode
    {:optional true,
     :setter-doc
       "Specifies index usage mode for the query.\n\n@param indexUsageMode, has three modes UNUSED, PARTIALLY_USED, and FULLY_USED"}
    [:string {:min 1}]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/SearchStats schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.SearchStats"}))