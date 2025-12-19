(ns gcp.logging.Operation
  (:require [gcp.global :as g])
  (:import (com.google.cloud.logging Operation)))

(def schema
  (g/schema
    [:map
     [:id :string]
     [:producer :string]
     [:first {:optional true} :boolean]
     [:last {:optional true} :boolean]]))

(defn ^Operation from-edn
  [{:keys [id producer first last] :as arg}]
  (g/strict! schema arg)
  (let [builder (Operation/newBuilder id producer)]
    (some->> first (.setFirst builder))
    (some->> last (.setLast builder))
    (.build builder)))

(defn to-edn [^Operation arg]
  {:id (.getId arg)
   :producer (.getProducer arg)
   :first (.first arg)
   :last (.last arg)})
