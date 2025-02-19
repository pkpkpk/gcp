(ns gcp.vertexai.v1.api.CountTokensRequest
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Content :as Content]
            [gcp.vertexai.v1.api.Tool :as Tool])
  (:import (com.google.cloud.vertexai.api CountTokensRequest)))

(defn ^CountTokensRequest from-edn
  [{:keys [model
           contents
           endpoint
           instances
           systemInstruction
           tools]
    :as arg}]
  (global/strict! :vertexai.api/CountTokensRequest arg)
  (let [builder (CountTokensRequest/newBuilder)]
    (some->> contents (map Content/from-edn) (.addAllContents builder))
    (some->> systemInstruction Content/from-edn (.setSystemInstruction builder))
    (some->> tools (map Tool/from-edn) (.addAllTools builder))
    (some->> endpoint (.setEndpoint builder))
    (some->> model (.setModel builder))
    (some->> instances (.addAllInstances builder))
    (.build builder)))

(defn to-edn [^CountTokensRequest arg]
  {:post [(global/strict! :vertexai.api/CountTokensRequest %)]}
  (cond-> {:endpoint (.getEndpoint arg)
           :model (.getModel arg)}
          (.hasSystemInstruction arg)
          (assoc :systemInstruction (Content/to-edn (.getSystemInstruction arg)))
          (pos? (.getContentsCount arg))
          (assoc :contents (mapv Content/to-edn (.getContentsList arg)))
          (pos? (.getToolsCount arg))
          (assoc :tools (mapv Tool/to-edn (.getToolsList arg)))
          (pos? (.getInstancesCount arg))
          (assoc :instances (vec (.getInstancesList arg)))))