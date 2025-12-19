(ns gcp.bigquery.v2.EncryptionConfiguration
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery EncryptionConfiguration)))

(defn ^EncryptionConfiguration from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^EncryptionConfiguration arg] (throw (Exception. "unimplemented")))

(def schemas
  {:gcp.bigquery.v2/EncryptionConfiguration
   [:map {:closed true} [:kmsKeyName :string]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))