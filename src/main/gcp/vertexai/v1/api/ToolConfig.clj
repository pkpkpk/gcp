(ns gcp.vertexai.v1.api.ToolConfig
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.FunctionCallingConfig :as FunctionCallingConfig])
  (:import (com.google.cloud.vertexai.api ToolConfig)))

(def ^{:class ToolConfig} schema
  [:map {:closed true
         :doc "Tool config. This config is shared for all tools provided in the request."}
   [:functionCallingConfig {:optional true} FunctionCallingConfig/schema]])

(defn ^ToolConfig from-edn
  [{:keys [functionCallingConfig] :as arg}]
  (global/strict! schema arg)
  (let [builder (ToolConfig/newBuilder)]
    (some->> functionCallingConfig FunctionCallingConfig/from-edn (.setFunctionCallingConfig builder))
    (.build builder)))

(defn to-edn [^ToolConfig arg]
  (cond-> {}
          (.hasFunctionCallingConfig arg)
          (assoc :functionCallingConfig (FunctionCallingConfig/to-edn (.getFunctionCallingConfig arg)))))
