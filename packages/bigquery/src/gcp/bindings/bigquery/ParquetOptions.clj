;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.ParquetOptions
  {:doc nil
   :file-git-sha "a335927e16d0907d62e584f08fa8393daae40354"
   :fqcn "com.google.cloud.bigquery.ParquetOptions"
   :gcp.dev/certification
     {:base-seed 1770997727433
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1770997727433 :standard 1770997727434 :stress 1770997727435}
      :protocol-hash
        "600a262ece6bd21dc98250ea6f25d9fa1a7ab0d8840c5d6ce9608615488fe05f"
      :timestamp "2026-02-13T15:48:47.443758983Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery ParquetOptions ParquetOptions$Builder]))

(defn ^ParquetOptions from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/ParquetOptions arg)
  (let [builder (ParquetOptions/newBuilder)]
    (when (get arg :enableListInference)
      (.setEnableListInference builder (get arg :enableListInference)))
    (when (get arg :enumAsString)
      (.setEnumAsString builder (get arg :enumAsString)))
    (when (get arg :mapTargetType)
      (.setMapTargetType builder (get arg :mapTargetType)))
    (.build builder)))

(defn to-edn
  [^ParquetOptions arg]
  {:post [(global/strict! :gcp.bindings.bigquery/ParquetOptions %)]}
  (cond-> {:type (.getType arg)}
    (.getEnableListInference arg) (assoc :enableListInference
                                    (.getEnableListInference arg))
    (.getEnumAsString arg) (assoc :enumAsString (.getEnumAsString arg))
    (.getMapTargetType arg) (assoc :mapTargetType (.getMapTargetType arg))))

(def schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bindings.bigquery/ParquetOptions}
   [:enableListInference {:optional true} :boolean]
   [:enumAsString {:optional true} :boolean]
   [:mapTargetType
    {:optional true,
     :getter-doc "Returns how the Parquet map is represented.",
     :setter-doc
       "[Optional] Indicates how to represent a Parquet map if present.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/reference/rest/v2/tables#maptargettype\">\n    MapTargetType</a>"}
    :string]
   [:type
    {:read-only? true,
     :getter-doc "Returns the external data format, as a string."}
    [:= "PARQUET"]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/ParquetOptions schema}
    {:gcp.global/name "gcp.bindings.bigquery.ParquetOptions"}))