(ns gcp.vertexai.v1.api.FunctionCallingConfig
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api FunctionCallingConfig FunctionCallingConfig$Mode)))

(def schema
  [:map {:class FunctionCallingConfig
         :doc "Function calling config."}
   [:mode {:doc "Function calling mode."}
    [:or
     [:enum
      {:doc "Unspecified function calling mode. This value should not be used."}
      "MODE_UNSPECIFIED" 0]
     [:enum
      {:doc "Default model behavior, model decides to predict either a function call or a natural language response."}
      "AUTO" 1]
     [:enum
      {:doc "Model is constrained to always predicting a function call only. If \"allowed_function_names\" are set, the predicted function call will be limited to any one of \"allowed_function_names\", else the predicted function call will be any one of the provided \"function_declarations\"."}
      "ANY" 2]
     [:enum
      {:doc "Model will not predict any function call. Model behavior is same as when not passing any function declarations."}
      "NONE" 3]
     [:= {:doc "No recognized value."} "UNRECOGNIZED"]]]
   [:allowedFunctionNames [:sequential :string]]])

(defn ^FunctionCallingConfig from-edn
  [{:keys [allowedFunctionNames mode] :as arg}]
  (global/strict! schema arg)
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