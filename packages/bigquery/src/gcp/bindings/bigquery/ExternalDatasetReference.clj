;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.ExternalDatasetReference
  {:doc "Configures the access a dataset defined in an external metadata storage."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ExternalDatasetReference"
   :gcp.dev/certification
     {:base-seed 1772045812378
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772045812378 :standard 1772045812379 :stress 1772045812380}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-25T18:56:52.385330722Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery ExternalDatasetReference
            ExternalDatasetReference$Builder]))

(defn ^ExternalDatasetReference from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/ExternalDatasetReference arg)
  (let [builder (ExternalDatasetReference/newBuilder)]
    (when (some? (get arg :connection))
      (.setConnection builder (get arg :connection)))
    (when (some? (get arg :externalSource))
      (.setExternalSource builder (get arg :externalSource)))
    (.build builder)))

(defn to-edn
  [^ExternalDatasetReference arg]
  {:post [(global/strict! :gcp.bindings.bigquery/ExternalDatasetReference %)]}
  (cond-> {}
    (.getConnection arg) (assoc :connection (.getConnection arg))
    (.getExternalSource arg) (assoc :externalSource (.getExternalSource arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Configures the access a dataset defined in an external metadata storage.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/ExternalDatasetReference}
   [:connection
    {:optional true,
     :setter-doc
       "The connection id that is used to access the external_source. Format:\nprojects/{project_id}/locations/{location_id}/connections/{connection_id} *"}
    [:string {:min 1}]]
   [:externalSource
    {:optional true, :setter-doc "External source that backs this dataset *"}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/ExternalDatasetReference schema}
    {:gcp.global/name "gcp.bindings.bigquery.ExternalDatasetReference"}))