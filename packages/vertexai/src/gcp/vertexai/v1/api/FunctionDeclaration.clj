(ns gcp.vertexai.v1.api.FunctionDeclaration
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Schema :as Schema])
  (:import [com.google.cloud.vertexai.api FunctionDeclaration]
           (com.google.cloud.vertexai.generativeai FunctionDeclarationMaker)
           (com.google.gson JsonObject)
           (java.lang.reflect Modifier)))

(defn ^FunctionDeclaration from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/FunctionDeclaration arg)
  (cond
    (string? arg)
    (FunctionDeclarationMaker/fromJsonString arg)

    (instance? JsonObject arg)
    (FunctionDeclarationMaker/fromJsonObject arg)

    true
    (if (some-> arg :function #(instance? java.lang.reflect.Method %))
      (let [{:keys [functionDescription function orderedParameterNames]} arg]
        (FunctionDeclarationMaker/fromFunc functionDescription function orderedParameterNames))
      ;;---------------------------------------------------------------------------------------
      (let [builder (FunctionDeclaration/newBuilder)]
        (.setName builder (:name arg))
        (.setDescription builder (:description arg))
        (when-let [params (:parameters arg)]
          (.setParameters builder (Schema/from-edn params)))
        (.build builder)))))

(defn to-edn [^FunctionDeclaration fnd]
  {:post [(global/strict! :gcp.vertexai.v1.api/FunctionDeclaration %)]}
  (cond-> {:name (.getName fnd)
           :description (.getDescription fnd)}
          (.hasParameters fnd)
          (assoc :parameters (Schema/to-edn (.getParameters fnd)))))

(def schema
  [:or
   {:ns               'gcp.vertexai.v1.api.FunctionDeclaration
    :from-edn         'gcp.vertexai.v1.api.FunctionDeclaration/from-edn
    :to-edn           'gcp.vertexai.v1.api.FunctionDeclaration/to-edn
    :doc              "Structured representation of a function declaration as defined by the OpenAPI 3.03 specification. Included in this declaration are the function name and parameters. This FunctionDeclaration is a representation of a block of code that can be used as a Tool by the model and executed by the client."
    :generativeai/url "https://ai.google.dev/api/caching#FunctionDeclaration"
    :protobuf/type    "google.cloud.vertexai.v1.FunctionDeclaration"
    :class            'com.google.cloud.vertexai.api.FunctionDeclaration
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.FunctionDeclaration"}
   :string
   (global/instance-schema com.google.gson.JsonObject)
   [:map {:doc           "provided schema"
          :error/message "provided parameters bad"}
    [:description {:optional true} :string]
    [:name {:optional false} :string]
    [:parameters {:optional true} :gcp.vertexai.v1.api/Schema]]
   [:map {:doc           "create schema from static-method via reflection"
          :error/message "static-method map bad"}
    [:function [:and
                (global/instance-schema java.lang.reflect.Method)
                [:fn '(fn [v] (java.lang.reflect.Modifier/isStatic (.getModifiers v)))]]]
    [:functionDescription {:optional true} :string]
    [:orderedParameters {:optional true} [:sequential :string]]]])

(global/register-schema! :gcp.vertexai.v1.api/FunctionDeclaration schema)
