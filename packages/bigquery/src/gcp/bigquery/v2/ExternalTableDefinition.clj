(ns gcp.bigquery.v2.ExternalTableDefinition
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery ExternalTableDefinition)))

;; https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.ExternalTableDefinition
(defn ^ExternalTableDefinition from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^ExternalTableDefinition arg] (throw (Exception. "unimplemented")))

(def schemas
  {:gcp.bigquery.v2/ExternalTableDefinition
   [:map {:closed true}
    [:type [:= "EXTERNAL"]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))