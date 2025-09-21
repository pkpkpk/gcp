(ns gcp.logging.MetricInfo
  (:require [gcp.global :as g])
  (:import (com.google.cloud.logging MetricInfo)))

(def schema
  [:map
   {:url "https://cloud.google.com/java/docs/reference/google-cloud-logging/latest/com.google.cloud.logging.MetricInfo"}
   [:name :string]
   [:filter :string]
   [:description {:optional true} :string]])

(defn ^MetricInfo from-edn
  [{:keys [name filter] :as arg}]
  (g/strict! schema arg)
  (let [builder (MetricInfo/newBuilder name filter)]
    (some->> (:description arg) (.setDescription builder))
    (.build builder)))

(defn to-edn [^MetricInfo arg]
  (cond-> {:name   (.getName arg)
           :filter (.getFilter arg)}
          (.getDescription arg) (assoc :description (.getDescription arg))))