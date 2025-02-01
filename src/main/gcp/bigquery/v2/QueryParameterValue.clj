(ns gcp.bigquery.v2.QueryParameterValue
  (:require [gcp.global :as global]
            [jsonista.core :as j])
  (:import (com.google.cloud.bigquery QueryParameterValue StandardSQLTypeName)
           (com.google.gson JsonObject)
           (java.time LocalDate LocalDateTime)))

(defn ^QueryParameterValue from-edn [arg]
  (global/strict! :gcp/bigquery.QueryParameterValue arg)
  (cond
    (string? arg) (QueryParameterValue/string arg)
    (boolean? arg) (QueryParameterValue/bool arg)
    (int? arg) (QueryParameterValue/int64 (Long/valueOf (long arg)))
    (float? arg) (QueryParameterValue/float64 (Double/valueOf (double arg)))
    (decimal? arg) (QueryParameterValue/numeric arg)
    (bytes? arg) (QueryParameterValue/bytes arg)
    (inst? arg) (QueryParameterValue/timestamp (Long/valueOf (.getTime ^java.util.Date arg)))
    (instance? LocalDate arg) (QueryParameterValue/date (.toString arg))
    (instance? LocalDateTime arg) (QueryParameterValue/dateTime (.toString arg))
    (instance? JsonObject arg) (QueryParameterValue/json ^JsonObject arg)
    (contains? arg :interval) (QueryParameterValue/interval ^String (:interval arg))
    (contains? arg :geography) (QueryParameterValue/geography (:geography arg))
    (map? arg) (QueryParameterValue/struct
                 (into {} (map (fn [[k v]]
                                 [(name k) (from-edn v)])) arg))
    (sequential? arg) (QueryParameterValue/array
                        ^Object/1 (into-array Object (map from-edn arg))
                        StandardSQLTypeName/ARRAY)
    :else (throw (IllegalArgumentException. (str "Unknown type for value: " arg)))))

(defn to-edn [^QueryParameterValue arg]
  {:post [(global/strict! :gcp/bigquery.QueryParameterValue %)]}
  (cond
    (some? (.getArrayValues arg)) ;; List<QueryParameterValue>
    (mapv to-edn (.getArrayValues arg))

    (some? (.getStructTypes arg))
    (into {}                                                ; Map<String, QueryParameterValue>
          (map (fn [field]
                 (let [field-name (.getName field)
                       field-val  (.get (.getStructValues arg) field-name)]
                   [(keyword field-name) (to-edn field-val)]))
               (.getStructTypes arg)))
    :else
    (let [raw-value (.getValue arg)]
      (case (.getType arg)
        StandardSQLTypeName/BOOL (Boolean/valueOf raw-value)
        StandardSQLTypeName/INT64 (Long/valueOf raw-value)
        StandardSQLTypeName/FLOAT64 (Double/valueOf raw-value)
        StandardSQLTypeName/NUMERIC (bigdec raw-value)
        StandardSQLTypeName/BIGNUMERIC (bigdec raw-value)
        StandardSQLTypeName/DATE (java.time.LocalDate/parse raw-value)
        StandardSQLTypeName/DATETIME (java.time.LocalDateTime/parse raw-value)
        StandardSQLTypeName/TIME (java.time.LocalTime/parse raw-value)
        StandardSQLTypeName/TIMESTAMP (java.time.Instant/parse raw-value)
        StandardSQLTypeName/GEOGRAPHY {:geography raw-value} ;; these two serialize like this (see from-edn above)
        StandardSQLTypeName/INTERVAL {:interval raw-value}   ;; otherwise is just string
        StandardSQLTypeName/JSON (j/read-value raw-value j/keyword-keys-object-mapper)
        raw-value))))
