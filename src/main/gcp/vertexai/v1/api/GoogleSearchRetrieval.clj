(ns gcp.vertexai.v1.api.GoogleSearchRetrieval
  (:import [com.google.cloud.vertexai.api GoogleSearchRetrieval]))

(def schema [:map {:closed true}])

(defn ^GoogleSearchRetrieval from-edn [_]
  (.build (GoogleSearchRetrieval/newBuilder)))

(defn to-edn [^GoogleSearchRetrieval _arg] {})