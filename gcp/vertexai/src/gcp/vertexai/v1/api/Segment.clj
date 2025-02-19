(ns gcp.vertexai.v1.api.Segment
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api Segment]))

(defn ^Segment from-edn [arg]
  (global/strict! :vertexai.api/Segment arg)
  (let [builder (Segment/newBuilder)]
    (.setStartIndex builder (:startIndex arg))
    (.setEndIndex builder (:endIndex arg))
    (.setPartIndex builder (:partIndex arg))
    (if (string? (:text arg))
      (.setText builder (:text arg))
      (.setTextBytes builder (:text arg)))
    (.build builder)))

(defn to-edn [^Segment arg]
  {:post [(global/strict! :vertexai.api/Segment %)]}
  {:startIndex (.getStartIndex arg)
   :endIndex (.getEndIndex arg)
   :partIndex (.getPartIndex arg)
   :text (.getText arg)})