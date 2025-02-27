(ns gcp.vertexai.v1.api.FunctionCallingConfig
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api FunctionCallingConfig FunctionCallingConfig$Mode)))

(defn ^FunctionCallingConfig from-edn
  [{:keys [allowedFunctionNames mode] :as arg}]
  (global/strict! :gcp/vertexai.api.FunctionCallingConfig arg)
  (let [builder (FunctionCallingConfig/newBuilder)]
    (some->> (not-empty allowedFunctionNames)
             (.addAllAllowedFunctionNames builder))
    (.setMode builder (if (number? mode)
                        (FunctionCallingConfig$Mode/forNumber (int mode))
                        (FunctionCallingConfig$Mode/valueOf ^String mode)))
    (.build builder)))

(defn to-edn
  [^FunctionCallingConfig arg]
  (cond-> {:mode (.name (.getMode arg))}
          (pos? (.getAllowedFunctionNamesCount arg))
          (assoc :allowedFunctionNames (protobuf/protocolstringlist-to-edn (.getAllowedFunctionNamesCount arg)))))