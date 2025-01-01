(ns gcp.vertexai.v1.api.Segment
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import [com.google.cloud.vertexai.api Segment]))

(def ^{:class Segment} schema
  [:map
   [:text
    {:doc "Output only. The text corresponding to the segment from the response."}
    [:or :string protobuf/bytestring-schema]]
   [:endIndex
    {:doc "Output only. End index in the given Part, measured in bytes. Offset from the start of the Part, exclusive, starting at zero."}
    :int]
   [:partIndex
    {:doc "Output only. The index of a Part object within its parent Content object."}
    :int]
   [:startIndex
    {:doc "Output only. Start index in the given Part, measured in bytes. Offset from the start of the Part, inclusive, starting at zero."}
    :int]])

(defn ^Segment from-edn [arg]
  (global/strict! schema arg)
  (let [builder (Segment/newBuilder)]
    (.setStartIndex builder (:startIndex arg))
    (.setEndIndex builder (:endIndex arg))
    (.setPartIndex builder (:partIndex arg))
    (if (string? (:text arg))
      (.setText builder (:text arg))
      (.setTextBytes builder (:text arg)))
    (.build builder)))

(defn to-edn [^Segment arg]
  {:startIndex (.getStartIndex arg)
   :endIndex (.getEndIndex arg)
   :partIndex (.getPartIndex arg)
   :text (.getText arg)})