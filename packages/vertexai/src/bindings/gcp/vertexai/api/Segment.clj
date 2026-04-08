;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.Segment
  {:doc
     "<pre>\nSegment of the content.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Segment}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.Segment"
   :gcp.dev/certification
     {:base-seed 1775465675467
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465675467 :standard 1775465675468 :stress 1775465675469}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:54:36.406910418Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api Segment Segment$Builder]))

(declare from-edn to-edn)

(defn ^Segment from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/Segment arg)
  (let [builder (Segment/newBuilder)]
    (when (some? (get arg :endIndex))
      (.setEndIndex builder (int (get arg :endIndex))))
    (when (some? (get arg :partIndex))
      (.setPartIndex builder (int (get arg :partIndex))))
    (when (some? (get arg :startIndex))
      (.setStartIndex builder (int (get arg :startIndex))))
    (when (some? (get arg :text)) (.setText builder (get arg :text)))
    (.build builder)))

(defn to-edn
  [^Segment arg]
  {:post [(global/strict! :gcp.vertexai.api/Segment %)]}
  (when arg
    (cond-> {}
      (.getEndIndex arg) (assoc :endIndex (.getEndIndex arg))
      (.getPartIndex arg) (assoc :partIndex (.getPartIndex arg))
      (.getStartIndex arg) (assoc :startIndex (.getStartIndex arg))
      (some->> (.getText arg)
               (not= ""))
        (assoc :text (.getText arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nSegment of the content.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Segment}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/Segment}
   [:endIndex
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. End index in the given Part, measured in bytes. Offset from\nthe start of the Part, exclusive, starting at zero.\n</pre>\n\n<code>int32 end_index = 3 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The endIndex."}
    :i32]
   [:partIndex
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. The index of a Part object within its parent Content object.\n</pre>\n\n<code>int32 part_index = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The partIndex."}
    :i32]
   [:startIndex
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Start index in the given Part, measured in bytes. Offset from\nthe start of the Part, inclusive, starting at zero.\n</pre>\n\n<code>int32 start_index = 2 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The startIndex."}
    :i32]
   [:text
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. The text corresponding to the segment from the response.\n</pre>\n\n<code>string text = 4 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The text."}
    [:string {:min 1}]]])

(global/include-schema-registry! (with-meta {:gcp.vertexai.api/Segment schema}
                                   {:gcp.global/name
                                      "gcp.vertexai.api.Segment"}))