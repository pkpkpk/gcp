(ns gcp.bigquery.v2.ModelId
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery ModelId)))

(defn ^ModelId from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^ModelId arg] (throw (Exception. "unimplemented")))

(def schemas
  {:gcp.bigquery.v2/ModelId :any})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))