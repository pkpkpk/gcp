;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.BiEngineStats
  {:doc
     "BIEngineStatistics contains query statistics specific to the use of BI Engine."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.BiEngineStats"
   :gcp.dev/certification
     {:base-seed 1772046104464
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772046104464 :standard 1772046104465 :stress 1772046104466}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-25T19:01:44.501488714Z"}}
  (:require [gcp.bindings.bigquery.BiEngineReason :as BiEngineReason]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery BiEngineStats BiEngineStats$Builder]))

(defn ^BiEngineStats from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/BiEngineStats arg)
  (let [builder (BiEngineStats/newBuilder)]
    (when (some? (get arg :biEngineMode))
      (.setBiEngineMode builder (get arg :biEngineMode)))
    (when (some? (get arg :biEngineReasons))
      (.setBiEngineReasons builder
                           (map BiEngineReason/from-edn
                             (get arg :biEngineReasons))))
    (.build builder)))

(defn to-edn
  [^BiEngineStats arg]
  {:post [(global/strict! :gcp.bindings.bigquery/BiEngineStats %)]}
  (cond-> {}
    (.getBiEngineMode arg) (assoc :biEngineMode (.getBiEngineMode arg))
    (.getBiEngineReasons arg) (assoc :biEngineReasons
                                (map BiEngineReason/to-edn
                                  (.getBiEngineReasons arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "BIEngineStatistics contains query statistics specific to the use of BI Engine.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/BiEngineStats}
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
    [:sequential {:min 1} :gcp.bindings.bigquery/BiEngineReason]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/BiEngineStats schema}
    {:gcp.global/name "gcp.bindings.bigquery.BiEngineStats"}))