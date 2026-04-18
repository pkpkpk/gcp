;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.BiEngineStats
  {:doc
     "BIEngineStatistics contains query statistics specific to the use of BI Engine."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.BiEngineStats"
   :gcp.dev/certification
     {:base-seed 1776499430760
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499430760 :standard 1776499430761 :stress 1776499430762}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:03:52.109503076Z"}}
  (:require [gcp.bigquery.BiEngineReason :as BiEngineReason]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery BiEngineStats BiEngineStats$Builder]))

(declare from-edn to-edn)

(defn ^BiEngineStats from-edn
  [arg]
  (global/strict! :gcp.bigquery/BiEngineStats arg)
  (let [builder (BiEngineStats/newBuilder)]
    (when (some? (get arg :biEngineMode))
      (.setBiEngineMode builder (get arg :biEngineMode)))
    (when (seq (get arg :biEngineReasons))
      (.setBiEngineReasons builder
                           (mapv BiEngineReason/from-edn
                             (get arg :biEngineReasons))))
    (.build builder)))

(defn to-edn
  [^BiEngineStats arg]
  {:post [(global/strict! :gcp.bigquery/BiEngineStats %)]}
  (when arg
    (cond-> {}
      (some->> (.getBiEngineMode arg)
               (not= ""))
        (assoc :biEngineMode (.getBiEngineMode arg))
      (seq (.getBiEngineReasons arg)) (assoc :biEngineReasons
                                        (mapv BiEngineReason/to-edn
                                          (.getBiEngineReasons arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "BIEngineStatistics contains query statistics specific to the use of BI Engine.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/BiEngineStats}
   [:biEngineMode
    {:optional true,
     :getter-doc
       "Specifies which mode of BI Engine acceleration was performed (if any).\n\n@return value or {@code null} for none",
     :setter-doc
       "Specifies which mode of BI Engine acceleration was performed (if any).\n\n@param biEngineMode biEngineMode or {@code null} for none"}
    [:string {:min 1}]]
   [:biEngineReasons
    {:optional true,
     :getter-doc
       "In case of DISABLED or PARTIAL bi_engine_mode, these contain the explanatory reasons as to why\nBI Engine could not accelerate. In case the full query was accelerated, this field is not\npopulated.\n\n@return value or {@code null} for none",
     :setter-doc
       "In case of DISABLED or PARTIAL bi_engine_mode, these contain the explanatory reasons as to\nwhy BI Engine could not accelerate. In case the full query was accelerated, this field is not\npopulated.\n\n@param biEngineReasons biEngineReasons or {@code null} for none"}
    [:sequential {:min 1} :gcp.bigquery/BiEngineReason]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/BiEngineStats schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.BiEngineStats"}))