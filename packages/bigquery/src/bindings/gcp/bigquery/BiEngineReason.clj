;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.BiEngineReason
  {:doc nil
   :file-git-sha "f4502aa716b19dd5836c19ac14e3167601e7f8a3"
   :fqcn "com.google.cloud.bigquery.BiEngineReason"
   :gcp.dev/certification
     {:base-seed 1790034133153
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034133153 :standard 1790034133154 :stress 1790034133155}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:42:14.181680523Z"}}
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
    [:string {:min 1, :gen/max 1}]]
   [:message
    {:optional true,
     :getter-doc
       "Free form human-readable reason for partial or disabled acceleration.\n\n@return value or {@code null} for none",
     :setter-doc
       "Free form human-readable reason for partial or disabled acceleration.\n\n@param message message or {@code null} for none"}
    [:string {:min 1, :gen/max 1}]]])

(global/include-registry! "gcp.bigquery.BiEngineReason"
                          {:gcp.bigquery/BiEngineReason schema})