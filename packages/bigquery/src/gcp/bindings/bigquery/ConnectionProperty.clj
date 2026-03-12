;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.ConnectionProperty
  {:doc nil
   :file-git-sha "c3548a2f521b19761c844c0b24fc8caab541aba7"
   :fqcn "com.google.cloud.bigquery.ConnectionProperty"
   :gcp.dev/certification
     {:base-seed 1771344410368
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771344410368 :standard 1771344410369 :stress 1771344410370}
      :protocol-hash
        "32175171656ac95c72455bff25fe019346fb52c916efee805be149692e31cec4"
      :timestamp "2026-02-17T16:06:50.380822314Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery ConnectionProperty
            ConnectionProperty$Builder]))

(defn ^ConnectionProperty from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/ConnectionProperty arg)
  (let [builder (ConnectionProperty/newBuilder)]
    (when (some? (get arg :key)) (.setKey builder (get arg :key)))
    (when (some? (get arg :value)) (.setValue builder (get arg :value)))
    (.build builder)))

(defn to-edn
  [^ConnectionProperty arg]
  {:post [(global/strict! :gcp.bindings.bigquery/ConnectionProperty %)]}
  (cond-> {}
    (.getKey arg) (assoc :key (.getKey arg))
    (.getValue arg) (assoc :value (.getValue arg))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/ConnectionProperty}
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
  (with-meta {:gcp.bindings.bigquery/ConnectionProperty schema}
    {:gcp.global/name "gcp.bindings.bigquery.ConnectionProperty"}))