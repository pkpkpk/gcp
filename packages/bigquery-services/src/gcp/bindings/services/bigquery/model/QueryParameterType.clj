;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.QueryParameterType
  {:doc
     "The type of a query parameter.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.QueryParameterType"
   :gcp.dev/certification
     {:base-seed 1772471373350
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772471373350 :standard 1772471373351 :stress 1772471373352}
      :protocol-hash
        "f4effb663e7e6af6dfc9051b90dfeb17820045df78c3901c15d91ac97dbdc861"
      :timestamp "2026-03-02T17:09:36.912531354Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model QueryParameterType
            QueryParameterType$StructTypes]))

(declare from-edn
         to-edn
         QueryParameterType$StructTypes-from-edn
         QueryParameterType$StructTypes-to-edn)

(defn
  ^QueryParameterType$StructTypes QueryParameterType$StructTypes-from-edn
  [arg]
  (let [o (new QueryParameterType$StructTypes)]
    (when (some? (get arg :description))
      (.setDescription o (get arg :description)))
    (when (some? (get arg :name)) (.setName o (get arg :name)))
    (when (some? (get arg :type)) (.setType o (from-edn (get arg :type))))
    o))

(defn
  QueryParameterType$StructTypes-to-edn
  [^QueryParameterType$StructTypes arg]
  (cond-> {}
    (.getDescription arg) (assoc :description (.getDescription arg))
    (.getName arg) (assoc :name (.getName arg))
    (.getType arg) (assoc :type (to-edn (.getType arg)))))

(def QueryParameterType$StructTypes-schema
  [:map
   {:closed true,
    :doc "The type of a struct parameter.",
    :gcp/category :nested/mutable-pojo,
    :gcp/key
      :gcp.bindings.services.bigquery.model/QueryParameterType.StructTypes}
   [:description
    {:getter-doc
       "Optional. Human-oriented description of the field.\n\n@return value or {@code null} for none",
     :setter-doc
       "Optional. Human-oriented description of the field.\n\n@param description description or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:name
    {:getter-doc
       "Optional. The name of this field.\n\n@return value or {@code null} for none",
     :setter-doc
       "Optional. The name of this field.\n\n@param name name or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:type
    {:getter-doc
       "Required. The type of this field.\n\n@return value or {@code null} for none",
     :setter-doc
       "Required. The type of this field.\n\n@param type type or {@code null} for none",
     :optional true}
    [:ref :gcp.bindings.services.bigquery.model/QueryParameterType]]])

(defn ^QueryParameterType from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/QueryParameterType arg)
  (let [o (new QueryParameterType)]
    (when (some? (get arg :arrayType))
      (.setArrayType o (from-edn (get arg :arrayType))))
    (when (some? (get arg :rangeElementType))
      (.setRangeElementType o (from-edn (get arg :rangeElementType))))
    (when (some? (get arg :structTypes))
      (.setStructTypes o
                       (map QueryParameterType$StructTypes-from-edn
                         (get arg :structTypes))))
    (when (some? (get arg :timestampPrecision))
      (.setTimestampPrecision o (get arg :timestampPrecision)))
    (when (some? (get arg :type)) (.setType o (get arg :type)))
    o))

(defn to-edn
  [^QueryParameterType arg]
  {:post [(global/strict!
            :gcp.bindings.services.bigquery.model/QueryParameterType
            %)]}
  (cond-> {}
    (.getArrayType arg) (assoc :arrayType (to-edn (.getArrayType arg)))
    (.getRangeElementType arg) (assoc :rangeElementType
                                 (to-edn (.getRangeElementType arg)))
    (.getStructTypes arg) (assoc :structTypes
                            (map QueryParameterType$StructTypes-to-edn
                              (.getStructTypes arg)))
    (.getTimestampPrecision arg) (assoc :timestampPrecision
                                   (.getTimestampPrecision arg))
    (.getType arg) (assoc :type (.getType arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "The type of a query parameter.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/QueryParameterType}
   [:arrayType
    {:getter-doc
       "Optional. The type of the array's elements, if this is an array.\n\n@return value or {@code null} for none",
     :setter-doc
       "Optional. The type of the array's elements, if this is an array.\n\n@param arrayType arrayType or {@code null} for none",
     :optional true}
    [:ref :gcp.bindings.services.bigquery.model/QueryParameterType]]
   [:rangeElementType
    {:getter-doc
       "Optional. The element type of the range, if this is a range.\n\n@return value or {@code null} for none",
     :setter-doc
       "Optional. The element type of the range, if this is a range.\n\n@param rangeElementType rangeElementType or {@code null} for none",
     :optional true}
    [:ref :gcp.bindings.services.bigquery.model/QueryParameterType]]
   [:structTypes
    {:getter-doc
       "Optional. The types of the fields of this struct, in order, if this is a struct.\n\n@return value or {@code null} for none",
     :setter-doc
       "Optional. The types of the fields of this struct, in order, if this is a struct.\n\n@param structTypes structTypes or {@code null} for none",
     :optional true}
    [:sequential {:min 1}
     :gcp.bindings.services.bigquery.model/QueryParameterType.StructTypes]]
   [:timestampPrecision
    {:getter-doc
       "Optional. Precision (maximum number of total digits in base 10) for seconds of TIMESTAMP type.\nPossible values include: * 6 (Default, for TIMESTAMP type with microsecond precision) * 12 (For\nTIMESTAMP type with picosecond precision)\n\n@return value or {@code null} for none",
     :setter-doc
       "Optional. Precision (maximum number of total digits in base 10) for seconds of TIMESTAMP type.\nPossible values include: * 6 (Default, for TIMESTAMP type with microsecond precision) * 12 (For\nTIMESTAMP type with picosecond precision)\n\n@param timestampPrecision timestampPrecision or {@code null} for none",
     :optional true} :int]
   [:type
    {:getter-doc
       "Required. The top level type of this field.\n\n@return value or {@code null} for none",
     :setter-doc
       "Required. The top level type of this field.\n\n@param type type or {@code null} for none",
     :optional true} [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta
    {:gcp.bindings.services.bigquery.model/QueryParameterType schema,
     :gcp.bindings.services.bigquery.model/QueryParameterType.StructTypes
       QueryParameterType$StructTypes-schema}
    {:gcp.global/name
       "gcp.bindings.services.bigquery.model.QueryParameterType"}))