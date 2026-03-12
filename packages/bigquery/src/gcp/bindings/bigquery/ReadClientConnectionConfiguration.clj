;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.ReadClientConnectionConfiguration
  {:doc "Represents BigQueryStorage Read client connection information."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.ReadClientConnectionConfiguration"
   :gcp.dev/certification
     {:base-seed 1772046004838
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772046004838 :standard 1772046004839 :stress 1772046004840}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-25T19:00:04.844324182Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery ReadClientConnectionConfiguration
            ReadClientConnectionConfiguration$Builder]))

(defn ^ReadClientConnectionConfiguration from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/ReadClientConnectionConfiguration arg)
  (let [builder (ReadClientConnectionConfiguration/newBuilder)]
    (when (some? (get arg :bufferSize))
      (.setBufferSize builder (get arg :bufferSize)))
    (when (some? (get arg :minResultSize))
      (.setMinResultSize builder (get arg :minResultSize)))
    (when (some? (get arg :totalToPageRowCountRatio))
      (.setTotalToPageRowCountRatio builder
                                    (get arg :totalToPageRowCountRatio)))
    (.build builder)))

(defn to-edn
  [^ReadClientConnectionConfiguration arg]
  {:post [(global/strict!
            :gcp.bindings.bigquery/ReadClientConnectionConfiguration
            %)]}
  (cond-> {}
    (.getBufferSize arg) (assoc :bufferSize (.getBufferSize arg))
    (.getMinResultSize arg) (assoc :minResultSize (.getMinResultSize arg))
    (.getTotalToPageRowCountRatio arg) (assoc :totalToPageRowCountRatio
                                         (.getTotalToPageRowCountRatio arg))))

(def schema
  [:map
   {:closed true,
    :doc "Represents BigQueryStorage Read client connection information.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/ReadClientConnectionConfiguration}
   [:bufferSize
    {:optional true,
     :getter-doc "Returns the bufferSize in this configuration.",
     :setter-doc
       "Sets the maximum number of table rows allowed in buffer before streaming them to the\nBigQueryResult."}
    :int]
   [:minResultSize
    {:optional true,
     :getter-doc "Returns the minResultSize in this configuration.",
     :setter-doc
       "Sets the minimum number of table rows in the query results used to determine whether to us\nthe BigQueryStorage Read client to fetch result sets after the first page."}
    :int]
   [:totalToPageRowCountRatio
    {:optional true,
     :getter-doc "Returns the totalToPageRowCountRatio in this configuration.",
     :setter-doc
       "Sets the total row count to page row count ratio used to determine whether to us the\nBigQueryStorage Read client to fetch result sets after the first page."}
    :int]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/ReadClientConnectionConfiguration schema}
    {:gcp.global/name
       "gcp.bindings.bigquery.ReadClientConnectionConfiguration"}))