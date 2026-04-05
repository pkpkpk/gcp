;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ConnectionProperty
  {:doc nil
   :file-git-sha "c3548a2f521b19761c844c0b24fc8caab541aba7"
   :fqcn "com.google.cloud.bigquery.ConnectionProperty"
   :gcp.dev/certification
     {:base-seed 1775130834571
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130834571 :standard 1775130834572 :stress 1775130834573}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:53:55.763617467Z"}}
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
    [:string {:min 1}]]
   [:value
    {:optional true,
     :getter-doc "Return the value of property.",
     :setter-doc "[Required] Value of the connection property."}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/ConnectionProperty schema}
    {:gcp.global/name "gcp.bigquery.ConnectionProperty"}))