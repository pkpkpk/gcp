(ns gcp.foreign.com.google.gson
  (:require [gcp.global :as global]
            [malli.core :as m])
  (:import (com.google.gson JsonObject JsonParser)))

(defn JsonObject-from-edn [arg]
  (cond
    (instance? JsonObject arg) arg
    (string? arg) (JsonParser/parseString arg)
    (map? arg) (let [obj (JsonObject.)]
                 (doseq [[k v] arg]
                   (.addProperty obj (name k) (str v)))
                 obj)
    :else (throw (ex-info "Unsupported type for JsonObject" {:arg arg}))))

(defn JsonObject-to-edn [^JsonObject arg]
  (str arg))

(def registry
  {:gcp.foreign.com.google.gson/JsonObject
   [:or
    (global/instance-schema com.google.gson.JsonObject)
    [:map-of :string :any]
    [:map-of :keyword :any]
    :string]})

(global/include-schema-registry! (with-meta registry {:gcp.global/name (str *ns*)}))
