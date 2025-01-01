(ns gcp.vertexai.v1.api.Type
  (:import [com.google.cloud.vertexai.api Type]))

(def ^{:class Type}
  schema
  [:or
   [:and :string
    [:enum "TYPE_UNSPECIFIED" "STRING" "NUMBER" "INTEGER" "BOOLEAN" "ARRAY" "OBJECT" "UNRECOGNIZED"]]
   [:and :int [:enum 0 1 2 3 4 5 6]]])

(defn ^Type from-edn [arg]
  (if (number? arg)
    (if (neg? arg)
      Type/UNRECOGNIZED
      (Type/forNumber (int arg)))
    (Type/valueOf ^String arg)))

(defn ^String to-edn [^Type t] (.toString t))
