(ns gcp.bigquery.v2.RemoteFunctionOptions
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery RemoteFunctionOptions)))

(defn from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [arg] (throw (Exception. "unimplemented")))

(def schemas
  {:gcp.bigquery.v2/RemoteFunctionOptions :any})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))