;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ConnectionProperty
  {:doc nil
   :file-git-sha "5410f6bbd78031621045db8ac9f56dc8ab6bb837"
   :fqcn "com.google.cloud.bigquery.ConnectionProperty"
   :gcp.dev/certification
     {:base-seed 1790034033848
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034033848 :standard 1790034033849 :stress 1790034033850}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:40:34.815790373Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery ConnectionProperty
            ConnectionProperty$Builder]))

(declare from-edn to-edn)

(defn ^ConnectionProperty from-edn
  [arg]
  (global/strict! :gcp.bigquery/ConnectionProperty arg)
  (let [builder (ConnectionProperty/newBuilder)]
    (when (some? (get arg :key)) (.setKey builder (get arg :key)))
    (when (some? (get arg :value)) (.setValue builder (get arg :value)))
    (.build builder)))

(defn to-edn
  [^ConnectionProperty arg]
  {:post [(global/strict! :gcp.bigquery/ConnectionProperty %)]}
  (when arg
    (cond-> {}
      (some->> (.getKey arg)
               (not= ""))
        (assoc :key (.getKey arg))
      (some->> (.getValue arg)
               (not= ""))
        (assoc :value (.getValue arg)))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/ConnectionProperty}
   [:key
    {:optional true,
     :getter-doc "Return the key of property.",
     :setter-doc "[Required] Name of the connection property to set."}
    [:string {:min 1, :gen/max 1}]]
   [:value
    {:optional true,
     :getter-doc "Return the value of property.",
     :setter-doc "[Required] Value of the connection property."}
    [:string {:min 1, :gen/max 1}]]])

(global/include-registry! "gcp.bigquery.ConnectionProperty"
                          {:gcp.bigquery/ConnectionProperty schema})