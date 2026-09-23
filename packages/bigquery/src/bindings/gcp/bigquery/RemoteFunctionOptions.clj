;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.RemoteFunctionOptions
  {:doc
     "Represents Remote Function Options. Options for a remote user-defined function."
   :file-git-sha "ddf5a80a285f1b09da596e2544888ad4e161268f"
   :fqcn "com.google.cloud.bigquery.RemoteFunctionOptions"
   :gcp.dev/certification
     {:base-seed 1790034200126
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034200126 :standard 1790034200127 :stress 1790034200128}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:43:21.163612964Z"}}
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
    [:string {:min 1, :gen/max 1}]]
   [:endpoint
    {:optional true,
     :getter-doc
       "Returns the endpoint of the user-provided service.\n\n@return String",
     :setter-doc
       "Sets Endpoint argument Endpoint of the user-provided remote service, e.g.\n```https://us-east1-my_gcf_project.cloudfunctions.net/remote_add```"}
    [:string {:min 1, :gen/max 1}]]
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
    [:map-of [:or simple-keyword? [:string {:min 1}]]
     [:string {:min 1, :gen/max 1}]]]])

(global/include-registry! "gcp.bigquery.RemoteFunctionOptions"
                          {:gcp.bigquery/RemoteFunctionOptions schema})