;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.SearchStats
  {:doc "Represents Search statistics information of a search query."
   :file-git-sha "486338fc73e62e318d13d1f2f476a3cc6a1b407a"
   :fqcn "com.google.cloud.bigquery.SearchStats"
   :gcp.dev/certification
     {:base-seed 1790034138928
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034138928 :standard 1790034138929 :stress 1790034138930}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:42:19.991172866Z"}}
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
    [:sequential {:min 1, :gen/max 2} :gcp.bigquery/IndexUnusedReason]]
   [:indexUsageMode
    {:optional true,
     :setter-doc
       "Specifies index usage mode for the query.\n\n@param indexUsageMode, has three modes UNUSED, PARTIALLY_USED, and FULLY_USED"}
    [:string {:min 1, :gen/max 1}]]])

(global/include-registry! "gcp.bigquery.SearchStats"
                          {:gcp.bigquery/SearchStats schema})