(ns gcp.vertexai.v1.api.Citation
  (:require [clojure.string :as string]
            [gcp.global :as global]
            [gcp.type :as t])
  (:import (com.google.type Date)
           [com.google.cloud.vertexai.api Citation]))

(def ^{:class Citation} schema
  [:map {:closed true}
   [:startIndex
    {:doc "Start index into the content."
     :optional true}
    :int]
   [:endIndex
    {:doc "End index into the content."
     :optional true}
    :int]
   [:uri
    {:doc "URI that is attributed as a source for a portion of the text."
     :optional true}
    :string]
   [:license
    {:doc "License of the attribution."
     :optional true}
    :string]
   [:title
    {:doc "Title of the attribution."
     :generativeai/field false
     :optional true}
    :string]
   [:publicationDate
    {:doc "Publication date of the attribution"
     :generativeai/field false
     :optional true}
    ;[:ref :com.google.type/Date]
    t/Date-schema]])

(defn ^Citation from-edn
  [{:keys [startIndex endIndex uri license publicationDate title] :as arg}]
  (global/strict! schema arg)
  (let [builder (Citation/newBuilder)]
    (some->> startIndex (.setStartIndex builder))
    (some->> endIndex (.setEndIndex builder))
    (some->> uri (.setUri builder))
    (some->> license (.setLicense builder))
    (some->> publicationDate ^Date t/Date-from-edn (.setPublicationDate builder))
    (some->> title (.setTitle builder))
    (.build builder)))

(defn to-edn [^Citation arg]
  {:post [(global/strict! schema %)]}
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
