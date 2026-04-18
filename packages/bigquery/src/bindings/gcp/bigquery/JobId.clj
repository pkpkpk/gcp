;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.JobId
  {:doc "Google BigQuery Job identity."
   :file-git-sha "6e3e07a22b8397e1e9d5b567589e44abc55961f2"
   :fqcn "com.google.cloud.bigquery.JobId"
   :gcp.dev/certification
     {:base-seed 1776499422130
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499422130 :standard 1776499422131 :stress 1776499422132}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:03:43.740982554Z"}}
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
     :setter-doc nil} [:string {:min 1}]]
   [:location
    {:optional true,
     :getter-doc
       "Returns the job's location.\n\n<p>When sending requests, the location must be specified for jobs whose location not \"US\" or\n\"EU\".",
     :setter-doc nil} [:string {:min 1}]]
   [:project
    {:optional true,
     :getter-doc
       "Returns job's project id.\n\n<p>When sending requests with null project, the client will attempt to infer the project name\nfrom the environment.",
     :setter-doc nil} [:string {:min 1}]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/JobId schema}
                                   {:gcp.global/name "gcp.bigquery.JobId"}))