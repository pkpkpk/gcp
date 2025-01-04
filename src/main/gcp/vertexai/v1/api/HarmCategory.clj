(ns gcp.vertexai.v1.api.HarmCategory
  (:require [gcp.global :as global])
  (:import (com.google.cloud.vertexai.api HarmCategory)))

(defn ^HarmCategory from-edn [arg]
  (global/strict! :vertex.api/HarmCategory arg)
  (if (number? arg)
    (HarmCategory/forNumber (int arg))
    (HarmCategory/valueOf ^String arg)))

(defn ^String to-edn [arg]
  {:post [(global/strict! :vertex.api/HarmCategory %)]}
  (if (int? arg)
    (.name (HarmCategory/forNumber arg))
    (if (string? arg)
      arg
      (if (instance? HarmCategory arg)
        (.name arg)
        (throw (ex-info "unsupported arg" {:arg arg}))))))