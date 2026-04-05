;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.RemoteFunctionOptions
  {:doc
     "Represents Remote Function Options. Options for a remote user-defined function."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.RemoteFunctionOptions"
   :gcp.dev/certification
     {:base-seed 1775131012105
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775131012105 :standard 1775131012106 :stress 1775131012107}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:56:53.351937583Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery RemoteFunctionOptions
            RemoteFunctionOptions$Builder]))

(declare from-edn to-edn)

(defn ^RemoteFunctionOptions from-edn
  [arg]
  (global/strict! :gcp.bigquery/RemoteFunctionOptions arg)
  (let [builder (RemoteFunctionOptions/newBuilder)]
    (when (some? (get arg :connection))
      (.setConnection builder (get arg :connection)))
    (when (some? (get arg :endpoint))
      (.setEndpoint builder (get arg :endpoint)))
    (when (some? (get arg :maxBatchingRows))
      (.setMaxBatchingRows builder (long (get arg :maxBatchingRows))))
    (when (seq (get arg :userDefinedContext))
      (.setUserDefinedContext builder
                              (into {}
                                    (map (fn [[k v]] [(name k) v]))
                                    (get arg :userDefinedContext))))
    (.build builder)))

(defn to-edn
  [^RemoteFunctionOptions arg]
  {:post [(global/strict! :gcp.bigquery/RemoteFunctionOptions %)]}
  (when arg
    (cond-> {}
      (some->> (.getConnection arg)
               (not= ""))
        (assoc :connection (.getConnection arg))
      (some->> (.getEndpoint arg)
               (not= ""))
        (assoc :endpoint (.getEndpoint arg))
      (.getMaxBatchingRows arg) (assoc :maxBatchingRows
                                  (.getMaxBatchingRows arg))
      (seq (.getUserDefinedContext arg)) (assoc :userDefinedContext
                                           (into
                                             {}
                                             (map (fn [[k v]] [(keyword k) v]))
                                             (.getUserDefinedContext arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "Represents Remote Function Options. Options for a remote user-defined function.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/RemoteFunctionOptions}
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
    :i64]
   [:userDefinedContext
    {:optional true,
     :getter-doc
       "Returns the user-defined context as a set of key/value pairs.\n\n@return Map&lt;String, String&gt;",
     :setter-doc
       "User-defined context as a set of key/value pairs, which will be sent as function invocation\ncontext together with batched arguments in the requests to the remote service. The total\nnumber of bytes of keys and values must be less than 8KB."}
    [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/RemoteFunctionOptions schema}
    {:gcp.global/name "gcp.bigquery.RemoteFunctionOptions"}))