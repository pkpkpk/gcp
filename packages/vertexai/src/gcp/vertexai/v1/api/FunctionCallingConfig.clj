(ns gcp.vertexai.v1.api.FunctionCallingConfig
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api FunctionCallingConfig FunctionCallingConfig$Mode)))

(defn ^FunctionCallingConfig from-edn
  [{:keys [allowedFunctionNames mode] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/FunctionCallingConfig arg)
  (let [builder (FunctionCallingConfig/newBuilder)]
    (some->> (not-empty allowedFunctionNames)
             (.addAllAllowedFunctionNames builder))
    (.setMode builder (if (number? mode)
                        (FunctionCallingConfig$Mode/forNumber (int mode))
                        (FunctionCallingConfig$Mode/valueOf ^String mode)))
    (.build builder)))

(defn to-edn
  [^FunctionCallingConfig arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/FunctionCallingConfig %)]}
  (cond-> {:mode (.name (.getMode arg))}
          (pos? (.getAllowedFunctionNamesCount arg))
          (assoc :allowedFunctionNames (protobuf/protocolstringlist-to-edn (.getAllowedFunctionNamesList arg)))))

(def schema
  [:map
   {:class    'com.google.cloud.vertexai.api.FunctionCallingConfig
    :from-edn 'gcp.vertexai.v1.api.FunctionCallingConfig/from-edn
    :to-edn   'gcp.vertexai.v1.api.FunctionCallingConfig/to-edn
    :doc      "Function calling config."
    :protobuf/type    "google.cloud.vertexai.v1.FunctionCallingConfig"
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.FunctionCallingConfig"}
   [:mode {:doc "Function calling mode."
           :optional true}
    [:or
     [:enum
      {:doc "Unspecified function calling mode. This value should not be used."
       :value 0}
      "MODE_UNSPECIFIED"]
     [:enum
      {:doc "Default model behavior, model decides to predict either a function call or a natural language response."
       :value 1}
      "AUTO"]
     [:enum
      {:doc "Model is constrained to always predicting a function call only. If \"allowed_function_names\" are set, the predicted function call will be limited to any one of \"allowed_function_names\", else the predicted function call will be any one of the provided \"function_declarations\"."
       :value 2}
      "ANY"]
     [:enum
      {:doc "Model will not predict any function call. Model behavior is same as when not passing any function declarations."
       :value 3}
      "NONE"]
     [:enum {:doc "No recognized value."} "UNRECOGNIZED"]]]
   [:allowedFunctionNames {:optional true} [:sequential :string]]])

(global/register-schema! :gcp.vertexai.v1.api/FunctionCallingConfig schema)
