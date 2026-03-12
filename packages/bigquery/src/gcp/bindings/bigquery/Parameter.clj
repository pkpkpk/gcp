;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.Parameter
  {:doc nil
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.Parameter"
   :gcp.dev/certification
     {:base-seed 1772045148440
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772045148440 :standard 1772045148441 :stress 1772045148442}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-25T18:46:41.392674530Z"}}
  (:require [gcp.bigquery.custom :as custom]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery Parameter Parameter$Builder]))

(defn ^Parameter from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/Parameter arg)
  (let [builder (Parameter/newBuilder)]
    (when (some? (get arg :name)) (.setName builder (get arg :name)))
    (when (some? (get arg :value))
      (.setValue builder
                 (custom/QueryParameterValue-from-edn (get arg :value))))
    (.build builder)))

(defn to-edn
  [^Parameter arg]
  {:post [(global/strict! :gcp.bindings.bigquery/Parameter %)]}
  (cond-> {:value (custom/QueryParameterValue-to-edn (.getValue arg))}
    (.getName arg) (assoc :name (.getName arg))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/Parameter}
   [:name
    {:optional true,
     :getter-doc
       "Returns the name of the query parameter. If unset, this is a positional parameter. Otherwise,\nshould be unique within a query.\n\n@return value or {@code null} for none",
     :setter-doc
       "[Optional] Sets the name of the query parameter. If unset, this is a positional parameter.\nOtherwise, should be unique within a query.\n\n@param name name or {@code null} for none"}
    [:string {:min 1}]]
   [:value
    {:getter-doc "Returns the value for a query parameter along with its type.",
     :setter-doc
       "Sets the the value for a query parameter along with its type.\n\n@param parameter parameter or {@code null} for none"}
    :gcp.bigquery.custom/QueryParameterValue]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/Parameter schema}
    {:gcp.global/name "gcp.bindings.bigquery.Parameter"}))