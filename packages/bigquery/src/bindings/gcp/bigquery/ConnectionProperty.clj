;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ConnectionProperty
  {:doc nil
   :file-git-sha "c3548a2f521b19761c844c0b24fc8caab541aba7"
   :fqcn "com.google.cloud.bigquery.ConnectionProperty"
   :gcp.dev/certification
     {:base-seed 1779204624618
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204624618 :standard 1779204624619 :stress 1779204624620}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:25.371732651Z"}}
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

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/ConnectionProperty schema}
    {:gcp.global/name "gcp.bigquery.ConnectionProperty"}))