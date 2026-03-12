;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.JobId
  {:doc "Google BigQuery Job identity."
   :file-git-sha "6e3e07a22b8397e1e9d5b567589e44abc55961f2"
   :fqcn "com.google.cloud.bigquery.JobId"
   :gcp.dev/certification
     {:base-seed 1772038094535
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1772038094535 :standard 1772038094536 :stress 1772038094537}
      :protocol-hash
        "62616b045d3dd853f6e527d31a44a851f587c87ad57ad3f2927b4519e248d6c9"
      :timestamp "2026-02-25T16:48:14.609061959Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery JobId JobId$Builder]))

(defn ^JobId from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/JobId arg)
  (let [builder (JobId/newBuilder)]
    (when (some? (get arg :job)) (.setJob builder (get arg :job)))
    (when (some? (get arg :location))
      (.setLocation builder (get arg :location)))
    (when (some? (get arg :project)) (.setProject builder (get arg :project)))
    (.build builder)))

(defn to-edn
  [^JobId arg]
  {:post [(global/strict! :gcp.bindings.bigquery/JobId %)]}
  (cond-> {}
    (.getJob arg) (assoc :job (.getJob arg))
    (.getLocation arg) (assoc :location (.getLocation arg))
    (.getProject arg) (assoc :project (.getProject arg))))

(def schema
  [:map
   {:closed true,
    :doc "Google BigQuery Job identity.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bindings.bigquery/JobId}
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

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/JobId schema}
    {:gcp.global/name "gcp.bindings.bigquery.JobId"}))