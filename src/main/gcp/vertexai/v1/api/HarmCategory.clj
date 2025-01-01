(ns gcp.vertexai.v1.api.HarmCategory
  (:require [gcp.global :as global])
  (:import (com.google.cloud.vertexai.api HarmCategory)))

(def ^{:class HarmCategory} schema
  [:enum
   "HARM_CATEGORY_DANGEROUS_CONTENT"
   "HARM_CATEGORY_HARASSMENT"
   "HARM_CATEGORY_HATE_SPEECH"
   "HARM_CATEGORY_SEXUALLY_EXPLICIT"
   "HARM_CATEGORY_UNSPECIFIED"
   "UNRECOGNIZED"
   0 1 2 3 4])

(defn ^HarmCategory from-edn [arg]
  (global/strict! schema arg)
  (if (number? arg)
    (HarmCategory/forNumber (int arg))
    (HarmCategory/valueOf ^String arg)))

(defn ^String to-edn [arg]
  {:post [(global/strict! schema %)]}
  (if (int? arg)
    (.name (HarmCategory/forNumber arg))
    (if (string? arg)
      arg
      (if (instance? HarmCategory arg)
        (.name arg)
        (throw (ex-info "unsupported arg" {:arg arg}))))))