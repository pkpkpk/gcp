(ns gcp.vertexai.v1.api.HarmCategory
  (:import (com.google.cloud.vertexai.api HarmCategory)))

(defn ^HarmCategory from-edn [arg]
  (if (number? arg)
    (HarmCategory/forNumber (int arg))
    (HarmCategory/valueOf ^String arg)))

(defn ^String to-edn [arg]
  (if (int? arg)
    (.name (HarmCategory/forNumber arg))
    (if (string? arg)
      arg
      (if (instance? HarmCategory arg)
        (.name arg)
        (throw (ex-info "unsupported arg" {:arg arg}))))))