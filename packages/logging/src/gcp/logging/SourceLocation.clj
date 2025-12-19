(ns gcp.logging.SourceLocation
  (:require [gcp.global :as g])
  (:import (com.google.cloud.logging SourceLocation)))

(def schema
  (g/schema
    [:map
     [:file :string]
     [:line :int]
     [:function :string]]))

(defn ^SourceLocation from-edn
  [{:keys [file line function]}]
  (let [builder (SourceLocation/newBuilder)]
    (.setFile builder file)
    (.setFunction builder function)
    (.setLine builder line)
    (.build builder)))

(defn to-edn [^SourceLocation arg]
  {:function (.getFunction arg)
   :line (.getLine arg)
   :file (.getFile arg)})
