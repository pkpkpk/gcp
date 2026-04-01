;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.Citation
  {:doc
     "<pre>\nSource attributions for content.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Citation}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.Citation"
   :gcp.dev/certification
     {:base-seed 1774824738668
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824738668 :standard 1774824738669 :stress 1774824738670}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:52:19.770093601Z"}}
  (:require [gcp.foreign.com.google.type :as type]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api Citation Citation$Builder]
           [com.google.type Date]))

(declare from-edn to-edn)

(defn ^Citation from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/Citation arg)
  (let [builder (Citation/newBuilder)]
    (when (some? (get arg :endIndex))
      (.setEndIndex builder (int (get arg :endIndex))))
    (when (some? (get arg :license)) (.setLicense builder (get arg :license)))
    (when (some? (get arg :publicationDate))
      (.setPublicationDate builder
                           (type/Date-from-edn (get arg :publicationDate))))
    (when (some? (get arg :startIndex))
      (.setStartIndex builder (int (get arg :startIndex))))
    (when (some? (get arg :title)) (.setTitle builder (get arg :title)))
    (when (some? (get arg :uri)) (.setUri builder (get arg :uri)))
    (.build builder)))

(defn to-edn
  [^Citation arg]
  {:post [(global/strict! :gcp.vertexai.api/Citation %)]}
  (cond-> {}
    (.getEndIndex arg) (assoc :endIndex (.getEndIndex arg))
    (some->> (.getLicense arg)
             (not= ""))
      (assoc :license (.getLicense arg))
    (.hasPublicationDate arg) (assoc :publicationDate
                                (type/Date-to-edn (.getPublicationDate arg)))
    (.getStartIndex arg) (assoc :startIndex (.getStartIndex arg))
    (some->> (.getTitle arg)
             (not= ""))
      (assoc :title (.getTitle arg))
    (some->> (.getUri arg)
             (not= ""))
      (assoc :uri (.getUri arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nSource attributions for content.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Citation}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/Citation}
   [:endIndex
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. End index into the content.\n</pre>\n\n<code>int32 end_index = 2 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The endIndex."}
    :i32]
   [:license
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. License of the attribution.\n</pre>\n\n<code>string license = 5 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The license."}
    [:string {:min 1}]]
   [:publicationDate
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Publication date of the attribution.\n</pre>\n\n<code>.google.type.Date publication_date = 6 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The publicationDate."}
    :gcp.foreign.com.google.type/Date]
   [:startIndex
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Start index into the content.\n</pre>\n\n<code>int32 start_index = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The startIndex."}
    :i32]
   [:title
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Title of the attribution.\n</pre>\n\n<code>string title = 4 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The title."}
    [:string {:min 1}]]
   [:uri
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. Url reference of the attribution.\n</pre>\n\n<code>string uri = 3 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The uri."}
    [:string {:min 1}]]])

(global/include-schema-registry! (with-meta {:gcp.vertexai.api/Citation schema}
                                   {:gcp.global/name
                                      "gcp.vertexai.api.Citation"}))