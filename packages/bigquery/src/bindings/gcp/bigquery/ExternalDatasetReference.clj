;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.ExternalDatasetReference
  {:doc "Configures the access a dataset defined in an external metadata storage."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ExternalDatasetReference"
   :gcp.dev/certification
     {:base-seed 1779204752063
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1779204752063 :standard 1779204752064 :stress 1779204752065}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:32:32.931042252Z"}}
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
    [:string {:min 1, :gen/max 1}]]
   [:externalSource
    {:optional true, :setter-doc "External source that backs this dataset *"}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/ExternalDatasetReference schema}
    {:gcp.global/name "gcp.bigquery.ExternalDatasetReference"}))