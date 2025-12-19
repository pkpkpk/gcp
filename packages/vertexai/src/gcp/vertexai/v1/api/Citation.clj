(ns gcp.vertexai.v1.api.Citation
  (:require [clojure.string :as string]
            [gcp.global :as global]
            [gcp.type :as t])
  (:import (com.google.type Date)
           [com.google.cloud.vertexai.api Citation]))

(defn ^Citation from-edn
  [{:keys [startIndex endIndex uri license publicationDate title] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/Citation arg)
  (let [builder (Citation/newBuilder)]
    (some->> startIndex (.setStartIndex builder))
    (some->> endIndex (.setEndIndex builder))
    (some->> uri (.setUri builder))
    (some->> license (.setLicense builder))
    (some->> publicationDate ^Date t/Date-from-edn (.setPublicationDate builder))
    (some->> title (.setTitle builder))
    (.build builder)))

(defn to-edn [^Citation arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/Citation %)]}
  (cond-> {:endIndex (.getEndIndex arg)
           :startIndex (.getStartIndex arg)}

          (and (.getUri arg) (not (string/blank? (.getUri arg))))
          (assoc :uri (.getUri arg))

          (and (.getTitle arg) (not (string/blank? (.getTitle arg))))
          (assoc :title (.getTitle arg))

          (and (.getLicense arg) (not (string/blank? (.getLicense arg))))
          (assoc :license (.getLicense arg))

          (.hasPublicationDate arg)
          (assoc :publicationDate (t/Date-to-edn (.getPublicationDate arg)))))

(def schema
  [:map
   {:ns               'gcp.vertexai.v1.api.Citation
    :from-edn         'gcp.vertexai.v1.api.Citation/from-edn
    :to-edn           'gcp.vertexai.v1.api.Citation/to-edn
    :doc              "A citation to a source for a portion of a specific response."
    :generativeai/url "https://ai.google.dev/api/generate-content#CitationSource"
    :protobuf/type    "google.cloud.vertexai.v1.Citation"
    :class            'com.google.cloud.vertexai.api.Citation
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Citation"}
   [:startIndex
    {:doc      "Start index into the content."
     :optional true}
    :int]
   [:endIndex
    {:doc      "End index into the content."
     :optional true}
    :int]
   [:uri
    {:doc      "URI that is attributed as a source for a portion of the text."
     :optional true}
    :string]
   [:license
    {:doc      "License of the attribution."
     :optional true}
    :string]
   [:title
    {:doc                "Title of the attribution."
     :generativeai/field false
     :optional           true}
    :string]
   [:publicationDate
    {:doc                "Publication date of the attribution"
     :generativeai/field false
     :optional           true}
    :gcp.type/Date]])

(global/register-schema! :gcp.vertexai.v1.api/Citation schema)
