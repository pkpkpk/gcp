;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.BiEngineReason
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.BiEngineReason"
   :gcp.dev/certification
     {:base-seed 1776499429142
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499429142 :standard 1776499429143 :stress 1776499429144}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:03:50.471995796Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery BiEngineReason BiEngineReason$Builder]))

(declare from-edn to-edn)

(defn ^BiEngineReason from-edn
  [arg]
  (global/strict! :gcp.bigquery/BiEngineReason arg)
  (let [builder (BiEngineReason/newBuilder)]
    (when (some? (get arg :code)) (.setCode builder (get arg :code)))
    (when (some? (get arg :message)) (.setMessage builder (get arg :message)))
    (.build builder)))

(defn to-edn
  [^BiEngineReason arg]
  {:post [(global/strict! :gcp.bigquery/BiEngineReason %)]}
  (when arg
    (cond-> {}
      (some->> (.getCode arg)
               (not= ""))
        (assoc :code (.getCode arg))
      (some->> (.getMessage arg)
               (not= ""))
        (assoc :message (.getMessage arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/BiEngineReason}
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
  (with-meta {:gcp.bigquery/BiEngineReason schema}
    {:gcp.global/name "gcp.bigquery.BiEngineReason"}))