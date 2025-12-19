(ns gcp.vertexai.v1.api.Segment
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import [com.google.cloud.vertexai.api Segment]))

(defn ^Segment from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/Segment arg)
  (let [builder (Segment/newBuilder)]
    (.setStartIndex builder (:startIndex arg))
    (.setEndIndex builder (:endIndex arg))
    (.setPartIndex builder (:partIndex arg))
    (if (string? (:text arg))
      (.setText builder (:text arg))
      (.setTextBytes builder (protobuf/bytestring-from-edn (:text arg))))
    (.build builder)))

(defn to-edn [^Segment arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/Segment %)]}
  (cond-> {:startIndex (.getStartIndex arg)
           :endIndex (.getEndIndex arg)
           :partIndex (.getPartIndex arg)}
          (not (empty? (.getText arg)))
          (assoc :text (.getText arg))))

(def schema
  [:map
   {:ns               'gcp.vertexai.v1.api.Segment
    :from-edn         'gcp.vertexai.v1.api.Segment/from-edn
    :to-edn           'gcp.vertexai.v1.api.Segment/to-edn
    :doc              "Segment of the content."
    :generativeai/url "https://ai.google.dev/api/generate-content#Segment"
    :protobuf/type    "google.cloud.vertexai.v1.Segment"
    :class            'com.google.cloud.vertexai.api.Segment
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Segment"}
   [:text
    {:doc "Output only. The text corresponding to the segment from the response."
     :optional true}
    [:or :string :gcp.protobuf/ByteString]]
   [:endIndex
    {:doc "Output only. End index in the given Part, measured in bytes. Offset from the start of the Part, exclusive, starting at zero."
     :optional true}
    :int]
   [:partIndex
    {:doc "Output only. The index of a Part object within its parent Content object."
     :optional true}
    :int]
   [:startIndex
    {:doc "Output only. Start index in the given Part, measured in bytes. Offset from the start of the Part, inclusive, starting at zero."
     :optional true}
    :int]])

(global/register-schema! :gcp.vertexai.v1.api/Segment schema)
