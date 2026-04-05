;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.RoutineArgument
  {:doc "An argument for a BigQuery Routine."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.RoutineArgument"
   :gcp.dev/certification
     {:base-seed 1775130960018
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130960018 :standard 1775130960019 :stress 1775130960020}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:56:01.277976650Z"}}
  (:require [gcp.bigquery.custom.StandardSQL :as StandardSQL]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery RoutineArgument RoutineArgument$Builder]))

(declare from-edn to-edn)

(defn ^RoutineArgument from-edn
  [arg]
  (global/strict! :gcp.bigquery/RoutineArgument arg)
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
  {:post [(global/strict! :gcp.bigquery/RoutineArgument %)]}
  (when arg
    (cond-> {}
      (.getDataType arg) (assoc :dataType
                           (StandardSQL/StandardSQLDataType-to-edn (.getDataType
                                                                     arg)))
      (some->> (.getKind arg)
               (not= ""))
        (assoc :kind (.getKind arg))
      (some->> (.getMode arg)
               (not= ""))
        (assoc :mode (.getMode arg))
      (some->> (.getName arg)
               (not= ""))
        (assoc :name (.getName arg)))))

(def schema
  [:map
   {:closed true,
    :doc "An argument for a BigQuery Routine.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/RoutineArgument}
   [:dataType
    {:optional true,
     :setter-doc
       "Sets the data type specification for the argument. It is required except for ANY_TYPE\nargument kinds."}
    :gcp.bigquery/StandardSQLDataType]
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
  (with-meta {:gcp.bigquery/RoutineArgument schema}
    {:gcp.global/name "gcp.bigquery.RoutineArgument"}))