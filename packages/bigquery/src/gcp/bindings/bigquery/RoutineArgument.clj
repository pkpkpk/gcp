;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.RoutineArgument
  {:doc "An argument for a BigQuery Routine."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.RoutineArgument"
   :gcp.dev/certification
     {:base-seed 1772138344307
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772138344307 :standard 1772138344308 :stress 1772138344309}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-26T20:39:05.283183709Z"}}
  (:require [gcp.bigquery.custom.StandardSQL :as StandardSQL]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery RoutineArgument RoutineArgument$Builder]))

(defn ^RoutineArgument from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/RoutineArgument arg)
  (let [builder (RoutineArgument/newBuilder)]
    (when (some? (get arg :dataType))
      (.setDataType builder
                    (StandardSQL/StandardSQLDataType-from-edn (get arg
                                                                   :dataType))))
    (when (some? (get arg :kind)) (.setKind builder (get arg :kind)))
    (when (some? (get arg :mode)) (.setMode builder (get arg :mode)))
    (when (some? (get arg :name)) (.setName builder (get arg :name)))
    (.build builder)))

(defn to-edn
  [^RoutineArgument arg]
  {:post [(global/strict! :gcp.bindings.bigquery/RoutineArgument %)]}
  (cond-> {}
    (.getDataType arg) (assoc :dataType
                         (StandardSQL/StandardSQLDataType-to-edn (.getDataType
                                                                   arg)))
    (.getKind arg) (assoc :kind (.getKind arg))
    (.getMode arg) (assoc :mode (.getMode arg))
    (.getName arg) (assoc :name (.getName arg))))

(def schema
  [:map
   {:closed true,
    :doc "An argument for a BigQuery Routine.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/RoutineArgument}
   [:dataType
    {:optional true,
     :setter-doc
       "Sets the data type specification for the argument. It is required except for ANY_TYPE\nargument kinds."}
    :gcp.bigquery.custom.StandardSQL/StandardSQLDataType]
   [:kind
    {:optional true,
     :getter-doc "Returns the kind of the argument.",
     :setter-doc
       "Sets the kind of argument.\n\n<p>A FIXED_TYPE argument is a fully specified type. It can be a struct or an array, but not a\ntable.\n\n<p>An ANY_TYPE argument is any type. It can be a struct or an array, but not a table."}
    [:string {:min 1}]]
   [:mode
    {:optional true,
     :getter-doc "Returns the mode of the argument.",
     :setter-doc
       "Optionally specifies the input/output mode of the argument.\n\n<p>An IN mode argument is input-only. An OUT mode argument is output-only. An INOUT mode\nargument is both an input and output."}
    [:string {:min 1}]]
   [:name
    {:optional true,
     :getter-doc "Returns the name of the argument.",
     :setter-doc "Sets the argument name."} [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/RoutineArgument schema}
    {:gcp.global/name "gcp.bindings.bigquery.RoutineArgument"}))