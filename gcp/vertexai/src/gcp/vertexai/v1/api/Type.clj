(ns gcp.vertexai.v1.api.Type
  (:import [com.google.cloud.vertexai.api Type]))

(defn ^Type from-edn [arg]
  (if (number? arg)
    (if (neg? arg)
      Type/UNRECOGNIZED
      (Type/forNumber (int arg)))
    (Type/valueOf ^String arg)))

(defn ^String to-edn [^Type t] (.toString t))
