(ns gcp.vertexai.v1.api.Content
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Part :as part])
  (:import [com.google.cloud.vertexai.api Content]
           [com.google.cloud.vertexai.generativeai ContentMaker]))

(defn ^Content from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/Content arg)
  (if (string? arg)
    (from-edn {:parts [arg]})
    (if (sequential? arg)
      (from-edn {:parts arg})
      (let [{:keys [role parts] :or {role "user"}} arg]
        (if (string? parts)
          (.fromString (ContentMaker/forRole role) parts)
          (let [data (into-array Object (map part/from-edn parts))]
            (.fromMultiModalData (ContentMaker/forRole role) data)))))))

(defn to-edn [^Content c]
  {:post [(global/strict! :gcp.vertexai.v1.api/Content %)]}
  {:role  (.getRole c)
   :parts (mapv part/->edn (.getPartsList c))})

(def schema
  [:or
   {:doc              "The base structured datatype containing multi-part content of a message. A Content includes a role field designating the producer of the Content and a parts field containing multi-part data that contains the content of the message turn"
    :gcp/sugared?     true
    :error/message    "Content must be (string|vector<Part>|{:user 'model' :parts [Part...]})"
    :class            'com.google.cloud.vertexai.api.Content
    :generativeai/url "https://ai.google.dev/api/caching#Content"
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Content"}
   :string
   [:sequential {:doc "part literals w/ role 'user'"} :gcp.vertexai.v1.api/Part]
   [:map
    {:doc "full specification"}
    [:role
     {:description "The producer of the content. Must be either 'user' or 'model'. Useful to set for multi-turn conversations, otherwise can be left blank or unset."
      :optional    true}
     [:enum "user" "model"]]
    [:parts
     {:optional false}
     [:sequential :gcp.vertexai.v1.api/Part]]]])

(global/register-schema! :gcp.vertexai.v1.api/Content schema)
