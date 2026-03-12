;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.BiEngineReason
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.BiEngineReason"
   :gcp.dev/certification
     {:base-seed 1772046098817
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772046098817 :standard 1772046098818 :stress 1772046098819}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-25T19:01:38.825404024Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery BiEngineReason BiEngineReason$Builder]))

(defn ^BiEngineReason from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/BiEngineReason arg)
  (let [builder (BiEngineReason/newBuilder)]
    (when (some? (get arg :code)) (.setCode builder (get arg :code)))
    (when (some? (get arg :message)) (.setMessage builder (get arg :message)))
    (.build builder)))

(defn to-edn
  [^BiEngineReason arg]
  {:post [(global/strict! :gcp.bindings.bigquery/BiEngineReason %)]}
  (cond-> {}
    (.getCode arg) (assoc :code (.getCode arg))
    (.getMessage arg) (assoc :message (.getMessage arg))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/BiEngineReason}
   [:code
    {:optional true,
     :getter-doc
       "High-level BI Engine reason for partial or disabled acceleration.\n\n@return value or {@code null} for none",
     :setter-doc
       "High-level BI Engine reason for partial or disabled acceleration.\n\n@param code code or {@code null} for none"}
    [:string {:min 1}]]
   [:message
    {:optional true,
     :getter-doc
       "Free form human-readable reason for partial or disabled acceleration.\n\n@return value or {@code null} for none",
     :setter-doc
       "Free form human-readable reason for partial or disabled acceleration.\n\n@param message message or {@code null} for none"}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/BiEngineReason schema}
    {:gcp.global/name "gcp.bindings.bigquery.BiEngineReason"}))