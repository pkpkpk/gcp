(ns gcp.bigquery.v2.BigLakeConfiguration
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery BigLakeConfiguration)))

(defn ^BigLakeConfiguration from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^BigLakeConfiguration arg] (throw (Exception. "unimplemented")))

(global/include-schema-registry! (with-meta {:gcp.bigquery.v2/BigLakeConfiguration :any} {:gcp.global/name (str *ns*)}))