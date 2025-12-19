(ns gcp.vertexai.v1.api.EnterpriseWebSearch
  (:require [gcp.global :as global])
  (:import (com.google.cloud.vertexai.api EnterpriseWebSearch)))

(defn ^EnterpriseWebSearch from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/EnterpriseWebSearch arg)
  (throw (Exception. "unimplemented")))

(defn to-edn [^EnterpriseWebSearch arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/EnterpriseWebSearch %)]}
  (throw (Exception. "unimplemented")))

(def schema :any)

(global/register-schema! :gcp.vertexai.v1.api/EnterpriseWebSearch schema)
