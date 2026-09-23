;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.JobId
  {:doc "Google BigQuery Job identity."
   :file-git-sha "9b6aa80452ca4b123ad92df10545db8bfca2e622"
   :fqcn "com.google.cloud.bigquery.JobId"
   :gcp.dev/certification
     {:base-seed 1790034128386
      :manifest "068dae53-75f2-5aa6-8d27-30391b1c6297"
      :passed-stages
        {:smoke 1790034128386 :standard 1790034128387 :stress 1790034128388}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-09-21T23:42:09.424072291Z"}}
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
       "Returns the job's id.\n\n<p>The server returns null job id for dry-run queries.",
     :setter-doc nil} [:string {:min 1, :gen/max 1}]]
   [:location
    {:optional true,
     :getter-doc
       "Returns the job's location.\n\n<p>When sending requests, the location must be specified for jobs whose location not \"US\" or\n\"EU\".",
     :setter-doc nil} [:string {:min 1, :gen/max 1}]]
   [:project
    {:optional true,
     :getter-doc
       "Returns job's project id.\n\n<p>When sending requests with null project, the client will attempt to infer the project name\nfrom the environment.",
     :setter-doc nil} [:string {:min 1, :gen/max 1}]]])

(global/include-registry! "gcp.bigquery.JobId" {:gcp.bigquery/JobId schema})