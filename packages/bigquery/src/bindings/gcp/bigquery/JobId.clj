;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.JobId
  {:doc "Google BigQuery Job identity."
   :file-git-sha "6e3e07a22b8397e1e9d5b567589e44abc55961f2"
   :fqcn "com.google.cloud.bigquery.JobId"
   :gcp.dev/certification
     {:base-seed 1775130932250
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775130932250 :standard 1775130932251 :stress 1775130932252}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:55:33.402569056Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery JobId JobId$Builder]))

(declare from-edn to-edn)

(defn ^JobId from-edn
  [arg]
  (global/strict! :gcp.bigquery/JobId arg)
  (let [builder (JobId/newBuilder)]
    (when (some? (get arg :job)) (.setJob builder (get arg :job)))
    (when (some? (get arg :location))
      (.setLocation builder (get arg :location)))
    (when (some? (get arg :project)) (.setProject builder (get arg :project)))
    (.build builder)))

(defn to-edn
  [^JobId arg]
  {:post [(global/strict! :gcp.bigquery/JobId %)]}
  (when arg
    (cond-> {}
      (some->> (.getJob arg)
               (not= ""))
        (assoc :job (.getJob arg))
      (some->> (.getLocation arg)
               (not= ""))
        (assoc :location (.getLocation arg))
      (some->> (.getProject arg)
               (not= ""))
        (assoc :project (.getProject arg)))))

(def schema
  [:map
   {:closed true,
    :doc "Google BigQuery Job identity.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/JobId}
   [:job
    {:optional true,
     :getter-doc
       "Returns the job's id.\n\n<p>The server returns null job id for dry-run queries."}
    [:string {:min 1}]]
   [:location
    {:optional true,
     :getter-doc
       "Returns the job's location.\n\n<p>When sending requests, the location must be specified for jobs whose location not \"US\" or\n\"EU\"."}
    [:string {:min 1}]]
   [:project
    {:optional true,
     :getter-doc
       "Returns job's project id.\n\n<p>When sending requests with null project, the client will attempt to infer the project name\nfrom the environment."}
    [:string {:min 1}]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/JobId schema}
                                   {:gcp.global/name "gcp.bigquery.JobId"}))