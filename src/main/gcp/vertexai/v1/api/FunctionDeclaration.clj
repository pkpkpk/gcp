(ns gcp.vertexai.v1.api.FunctionDeclaration
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Schema :as Schema])
  (:import [com.google.cloud.vertexai.api FunctionDeclaration]
           (com.google.cloud.vertexai.generativeai FunctionDeclarationMaker)
           (com.google.gson JsonObject)
           (java.lang.reflect Modifier)))

(def ^{:class FunctionDeclaration} schema
  [:or
   :string
   (global/instance-schema JsonObject)
   [:map {:doc "provided schema"
          :error/message "provided parameters bad"}
    [:description :string]
    [:name :string]
    [:parameters Schema/schema]]
   [:map {:doc "create schema from static-method via reflection"
          :error/message "static-method map bad"}
    [:function  [:and
                 [:fn #(instance? java.lang.reflect.Method %)]
                 [:fn #(Modifier/isStatic (.getModifiers %))]]]
    [:functionDescription :string]
    [:orderedParameters [:sequential :string]]]])

(defn ^FunctionDeclaration from-edn [arg]
  (global/strict! schema arg)
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
  {:post [(global/strict! schema %)]}
  (cond-> {:name (.getName fnd)
           :description (.getDescription fnd)}
          (.hasParameters fnd)
          (assoc :parameters (Schema/to-edn (.getParameters fnd)))))
