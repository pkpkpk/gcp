(ns gcp.api.services.bigquery.custom
  (:require [gcp.global :as g])
  (:import [com.google.api.services.bigquery.model QueryParameterValue RangeValue]))

(declare RangeValue-from-edn RangeValue-to-edn
         QueryParameterValue-from-edn QueryParameterValue-to-edn)

(defn ^QueryParameterValue QueryParameterValue-from-edn
  [arg]
  (g/strict! :gcp.api.services.bigquery/QueryParameterValue arg)
  (let [o (new QueryParameterValue)]
    (when (some? (get arg :arrayValues))
      (.setArrayValues o (map QueryParameterValue-from-edn (get arg :arrayValues))))
    (when (some? (get arg :rangeValue))
      (.setRangeValue o (RangeValue-from-edn (get arg :rangeValue))))
    (when (some? (get arg :structValues))
      (.setStructValues o
                        (into {}
                              (map (fn [[k v]] [(name k) (QueryParameterValue-from-edn v)]))
                              (get arg :structValues))))
    (when (some? (get arg :value)) (.setValue o (get arg :value)))
    o))

(defn QueryParameterValue-to-edn
  [^QueryParameterValue arg]
  {:post [(g/strict! :gcp.api.services.bigquery/QueryParameterValue %)]}
  (cond-> {}
    (.getArrayValues arg)  (assoc :arrayValues (map QueryParameterValue-to-edn (.getArrayValues arg)))
    (.getRangeValue arg)   (assoc :rangeValue (RangeValue-to-edn (.getRangeValue arg)))
    (.getStructValues arg) (assoc :structValues
                                  (into {}
                                        (map (fn [[k v]] [(keyword k) (QueryParameterValue-to-edn v)]))
                                        (.getStructValues arg)))
    (.getValue arg) (assoc :value (.getValue arg))))

(def QueryParameterValue-schema
  [:map
   {:closed true
    :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c",
    :fqcn "com.google.api.services.bigquery.model.QueryParameterValue"
    :doc "The value of a query parameter. This is the Java data model class that specifies how to parse/serialize into the JSON that is transmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery/QueryParameterValue}
   [:arrayValues
    {:getter-doc
     "Optional. The array values, if this is an array type.\n\n@return value or {@code null} for none",
     :setter-doc
     "Optional. The array values, if this is an array type.\n\n@param arrayValues arrayValues or {@code null} for none",
     :optional true}
    [:sequential {:min 1} [:ref :gcp.api.services.bigquery/QueryParameterValue]]]
   [:rangeValue
    {:getter-doc
     "Optional. The range value, if this is a range type.\n\n@return value or {@code null} for none",
     :setter-doc
     "Optional. The range value, if this is a range type.\n\n@param rangeValue rangeValue or {@code null} for none",
     :optional true} [:ref :gcp.api.services.bigquery/RangeValue]]
   [:structValues
    {:getter-doc
     "The struct field values.\n\n@return value or {@code null} for none",
     :setter-doc
     "The struct field values.\n\n@param structValues structValues or {@code null} for none",
     :optional true}
    [:map-of [:or simple-keyword? [:string {:min 1}]] [:ref :gcp.api.services.bigquery/QueryParameterValue]]]
   [:value
    {:getter-doc
     "Optional. The value of this value, if a simple scalar type.\n\n@return value or {@code null} for none",
     :setter-doc
     "Optional. The value of this value, if a simple scalar type.\n\n@param value value or {@code null} for none",
     :optional true} [:string {:min 1}]]])

#!------------------------------------------------------------------------------------------

(defn ^RangeValue RangeValue-from-edn [arg]
  (g/strict! :gcp.api.services.bigquery/RangeValue arg)
  (let [o (new RangeValue)]
    (when (some? (get arg :end))
      (.setEnd o (QueryParameterValue-from-edn (get arg :end))))
    (when (some? (get arg :start))
      (.setStart o (QueryParameterValue-from-edn (get arg :start))))
    o))

(defn RangeValue-to-edn
  [^RangeValue arg]
  {:post [(g/strict! :gcp.api.services.bigquery/RangeValue %)]}
  (cond-> {}
          (.getEnd arg) (assoc :end (QueryParameterValue-to-edn (.getEnd arg)))
          (.getStart arg) (assoc :start (QueryParameterValue-to-edn (.getStart arg)))))

(def RangeValue-schema
  [:map
   {:closed true
    :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c",
    :fqcn "com.google.api.services.bigquery.model.RangeValue"
    :doc "Represents the value of a range. This is the Java data model class that specifies how to parse/serialize into the JSON that is transmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery/RangeValue}
   [:end
    {:getter-doc "Optional. The end value of the range. A missing value represents an unbounded end.\n\n@return value or {@code null} for none",
     :setter-doc "Optional. The end value of the range. A missing value represents an unbounded end.\n\n@param end end or {@code null} for none",
     :optional true}
    [:ref :gcp.api.services.bigquery/QueryParameterValue]]
   [:start
    {:getter-doc "Optional. The start value of the range. A missing value represents an unbounded start.\n\n@return value or {@code null} for none",
     :setter-doc "Optional. The start value of the range. A missing value represents an unbounded start.\n\n@param start start or {@code null} for none",
     :optional true}
    [:ref :gcp.api.services.bigquery/QueryParameterValue]]])

#!------------------------------------------------------------------------------------------

(g/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery/QueryParameterValue QueryParameterValue-schema
              :gcp.api.services.bigquery/RangeValue RangeValue-schema}
             {::g/name "gcp.services.bigquery.custom"}))