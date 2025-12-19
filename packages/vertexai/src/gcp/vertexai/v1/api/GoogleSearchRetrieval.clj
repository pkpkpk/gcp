(ns gcp.vertexai.v1.api.GoogleSearchRetrieval
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api GoogleSearchRetrieval]))

(defn ^GoogleSearchRetrieval from-edn [_]
  (global/strict! :gcp.vertexai.v1.api/GoogleSearchRetrieval _)
  (.build (GoogleSearchRetrieval/newBuilder)))

(defn to-edn [^GoogleSearchRetrieval _arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/GoogleSearchRetrieval %)]}
  {}) 

(def schema
  [:map
   {:ns       'gcp.vertexai.v1.api.GoogleSearchRetrieval
    :from-edn 'gcp.vertexai.v1.api.GoogleSearchRetrieval/from-edn
    :to-edn   'gcp.vertexai.v1.api.GoogleSearchRetrieval/to-edn
    :class    'com.google.cloud.vertexai.apiGoogleSearchRetrieval
    :closed   true}])

(global/register-schema! :gcp.vertexai.v1.api/GoogleSearchRetrieval schema)
