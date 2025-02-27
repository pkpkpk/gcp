(ns gcp.vertexai.v1.api.ToolConfig
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.FunctionCallingConfig :as FunctionCallingConfig])
  (:import (com.google.cloud.vertexai.api ToolConfig)))

(defn ^ToolConfig from-edn
  [{:keys [functionCallingConfig] :as arg}]
  (global/strict! :gcp/vertexai.api.ToolConfig arg)
  (let [builder (ToolConfig/newBuilder)]
    (some->> functionCallingConfig FunctionCallingConfig/from-edn (.setFunctionCallingConfig builder))
    (.build builder)))

(defn to-edn [^ToolConfig arg]
  {:post [(global/strict! :gcp/vertexai.api.ToolConfig %)]}
  (cond-> {}
          (.hasFunctionCallingConfig arg)
          (assoc :functionCallingConfig (FunctionCallingConfig/to-edn (.getFunctionCallingConfig arg)))))
