;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.RemoteFunctionOptions
  {:doc
     "Represents Remote Function Options. Options for a remote user-defined function."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.RemoteFunctionOptions"
   :gcp.dev/certification
     {:base-seed 1772045397606
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772045397606 :standard 1772045397607 :stress 1772045397608}
      :protocol-hash
        "b8a5eb17212acb18f49dcb7f15a243eac9b32f54c0f054a99be660a3a25e0315"
      :timestamp "2026-02-25T18:49:57.639502709Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery RemoteFunctionOptions
            RemoteFunctionOptions$Builder]))

(defn ^RemoteFunctionOptions from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/RemoteFunctionOptions arg)
  (let [builder (RemoteFunctionOptions/newBuilder)]
    (when (some? (get arg :connection))
      (.setConnection builder (get arg :connection)))
    (when (some? (get arg :endpoint))
      (.setEndpoint builder (get arg :endpoint)))
    (when (some? (get arg :maxBatchingRows))
      (.setMaxBatchingRows builder (get arg :maxBatchingRows)))
    (when (some? (get arg :userDefinedContext))
      (.setUserDefinedContext builder
                              (into {}
                                    (map (fn [[k v]] [(name k) v]))
                                    (get arg :userDefinedContext))))
    (.build builder)))

(defn to-edn
  [^RemoteFunctionOptions arg]
  {:post [(global/strict! :gcp.bindings.bigquery/RemoteFunctionOptions %)]}
  (cond-> {}
    (.getConnection arg) (assoc :connection (.getConnection arg))
    (.getEndpoint arg) (assoc :endpoint (.getEndpoint arg))
    (.getMaxBatchingRows arg) (assoc :maxBatchingRows (.getMaxBatchingRows arg))
    (.getUserDefinedContext arg) (assoc :userDefinedContext
                                   (into {}
                                         (map (fn [[k v]] [(keyword k) v]))
                                         (.getUserDefinedContext arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Represents Remote Function Options. Options for a remote user-defined function.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/RemoteFunctionOptions}
   [:connection
    {:optional true,
     :getter-doc
       "Returns the fully qualified name of the user-provided connection object.\n\n@return String",
     :setter-doc
       "Fully qualified name of the user-provided connection object which holds the authentication\ninformation to send requests to the remote service. Format:\n```\\\"projects/{projectId}/locations/{locationId}/connections/{connectionId}\\\"```"}
    [:string {:min 1}]]
   [:endpoint
    {:optional true,
     :getter-doc
       "Returns the endpoint of the user-provided service.\n\n@return String",
     :setter-doc
       "Sets Endpoint argument Endpoint of the user-provided remote service, e.g.\n```https://us-east1-my_gcf_project.cloudfunctions.net/remote_add```"}
    [:string {:min 1}]]
   [:maxBatchingRows
    {:optional true,
     :getter-doc
       "Returns max number of rows in each batch sent to the remote service.\n\n@return Long",
     :setter-doc
       "Max number of rows in each batch sent to the remote service. If absent or if 0, BigQuery\ndynamically decides the number of rows in a batch."}
    :int]
   [:userDefinedContext
    {:optional true,
     :getter-doc
       "Returns the user-defined context as a set of key/value pairs.\n\n@return Map&lt;String, String&gt;",
     :setter-doc
       "User-defined context as a set of key/value pairs, which will be sent as function invocation\ncontext together with batched arguments in the requests to the remote service. The total\nnumber of bytes of keys and values must be less than 8KB."}
    [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/RemoteFunctionOptions schema}
    {:gcp.global/name "gcp.bindings.bigquery.RemoteFunctionOptions"}))