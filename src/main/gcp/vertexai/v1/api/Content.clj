(ns gcp.vertexai.v1.api.Content
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Part :as part])
  (:import [com.google.cloud.vertexai.api Content]
           [com.google.cloud.vertexai.generativeai ContentMaker]))

(def ^{:class Content} schema
  [:or
   :string
   [:sequential part/schema]
   [:map
    [:role
     {:description "The producer of the content. Must be either 'user' or 'model'. Useful to set for multi-turn conversations, otherwise can be left blank or unset."
      :optional    true}
     [:enum "user" "model"]]
    [:parts
     {:optional false}
     [:or :string [:sequential part/schema]]]]])

(defn ^Content from-edn
  ":input accepts string|partlike|vector<partlike>|contentable-maps
   if you want to specify user, must provide expand content map form:
   ie {:user ...
       :parts [{} ...]"
  [arg]
  (global/strict! schema arg)
  (if (string? arg)
    (from-edn {:parts [arg]})
    (if (vector? arg)
      (from-edn {:parts arg})
      (let [{:keys [role parts] :or {role "user"}} arg]
        (if (string? parts)
          (.fromString (ContentMaker/forRole role) parts)
          (let [data (into-array Object (map part/from-edn parts))]
            (.fromMultiModalData (ContentMaker/forRole role) data)))))))

(defn to-edn [^Content c]
  {:post [(global/strict! schema %)]}
  {:role  (.getRole c)
   :parts (mapv part/->edn (.getPartsList c))})