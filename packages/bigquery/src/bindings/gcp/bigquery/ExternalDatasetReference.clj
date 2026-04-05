;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ExternalDatasetReference
  {:doc "Configures the access a dataset defined in an external metadata storage."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ExternalDatasetReference"
   :gcp.dev/certification
     {:base-seed 1775131015642
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775131015642 :standard 1775131015643 :stress 1775131015644}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:56:56.857204960Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery ExternalDatasetReference
            ExternalDatasetReference$Builder]))

(declare from-edn to-edn)

(defn ^ExternalDatasetReference from-edn
  [arg]
  (global/strict! :gcp.bigquery/ExternalDatasetReference arg)
  (let [builder (ExternalDatasetReference/newBuilder)]
    (when (some? (get arg :connection))
      (.setConnection builder (get arg :connection)))
    (when (some? (get arg :externalSource))
      (.setExternalSource builder (get arg :externalSource)))
    (.build builder)))

(defn to-edn
  [^ExternalDatasetReference arg]
  {:post [(global/strict! :gcp.bigquery/ExternalDatasetReference %)]}
  (when arg
    (cond-> {}
      (some->> (.getConnection arg)
               (not= ""))
        (assoc :connection (.getConnection arg))
      (some->> (.getExternalSource arg)
               (not= ""))
        (assoc :externalSource (.getExternalSource arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Configures the access a dataset defined in an external metadata storage.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/ExternalDatasetReference}
   [:connection
    {:optional true,
     :setter-doc
       "The connection id that is used to access the external_source. Format:\nprojects/{project_id}/locations/{location_id}/connections/{connection_id} *"}
    [:string {:min 1}]]
   [:externalSource
    {:optional true, :setter-doc "External source that backs this dataset *"}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/ExternalDatasetReference schema}
    {:gcp.global/name "gcp.bigquery.ExternalDatasetReference"}))