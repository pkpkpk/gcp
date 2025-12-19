(ns gcp.bigquery.v2.Acl
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery Acl)))

;https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.Acl

(defn ^Acl from-edn
  [{:as arg}]
  (global/strict! :gcp.bigquery.v2/Acl arg)
  (throw (Exception. "unimplemented")))

(defn to-edn [^Acl arg]
  {:entity {:type (.name (.getType (.getEntity arg)))}
   :role   (.name (.getRole arg))})

(def schemas
  {:gcp.bigquery.v2/Acl
   [:map
    [:role [:enum "OWNER" "READER" "WRITER"]]
    [:entity [:map [:type [:enum "DATASET" "DOMAIN" "GROUP" "IAM_MEMBER" "ROUTINE" "USER" "VIEW"]]]]]

   :gcp.bigquery.v2/Acl.Entity.Type [:enum "USER" "VIEW" "IAM_MEMBER" "ROUTINE" "DOMAIN" "DATASET" "GROUP"]
   :gcp.bigquery.v2/Acl.Role [:enum "READER" "OWNER" "WRITER"]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
