(ns gcp.vertexai.v1.api.Content
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Part :as part])
  (:import [com.google.cloud.vertexai.api Content]
           [com.google.cloud.vertexai.generativeai ContentMaker]))

(defn ^Content from-edn [arg]
  (global/strict! :vertexai.api/Content arg)
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
  {:post [(global/strict! :vertexai.api/Content %)]}
  {:role  (.getRole c)
   :parts (mapv part/->edn (.getPartsList c))})