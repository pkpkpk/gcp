(ns gcp.bigquery.v2.ExternalDatasetReference
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery ExternalDatasetReference)))

(defn ^ExternalDatasetReference from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^ExternalDatasetReference arg]
  (throw (Exception. "unimplemented")))

(def schemas
  {:gcp.bigquery.v2/ExternalDatasetReference :any})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))