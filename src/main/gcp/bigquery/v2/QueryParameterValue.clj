(ns gcp.bigquery.v2.QueryParameterValue
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery QueryParameterValue StandardSQLTypeName)
           (com.google.gson JsonObject)
           (java.time LocalDate LocalDateTime)))

(defn ^QueryParameterValue from-edn [arg]
  (global/strict! :bigquery/QueryParameterValue arg)
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

(defn to-edn
  "Converts a QueryParameterValue instance back into a Clojure data structure.
   It attempts to parse arrays, structs, and common scalar types (e.g. BOOL, INT64, FLOAT64, etc.).
   For less common types (like INTERVAL, JSON, GEOGRAPHY), it returns a raw string or uses a placeholder."
  [^QueryParameterValue arg]
  (global/strict! :bigquery/QueryParameterValue arg)
  (let [sql-type   (.getType arg)       ;; StandardSQLTypeName
        raw-value  (.getValue arg)      ;; String representation
        arr-values (.getArrayValues arg) ;; List<QueryParameterValue> if it's an array
        st-types   (.getStructTypes arg) ;; List<Field> if it's a struct
        st-values  (.getStructValues arg)] ;; Map<String, QueryParameterValue> if it's a struct
    (cond
      ;; ARRAY
      (some? arr-values)
      (mapv to-edn arr-values)

      ;; STRUCT
      (and (some? st-types) (some? st-values))
      (into {}
            (map (fn [field]
                   (let [field-name (.getName field)
                         field-val  (.get st-values field-name)]
                     [(keyword field-name) (to-edn field-val)]))
                 st-types))

      ;; Otherwise, parse or return the raw string depending on the SQL type
      :else
      (case sql-type
        ;; Booleans ("TRUE"/"FALSE")
        com.google.cloud.bigquery.StandardSQLTypeName/BOOL
        (Boolean/valueOf raw-value)

        ;; 64-bit integers
        com.google.cloud.bigquery.StandardSQLTypeName/INT64
        (Long/valueOf raw-value)

        ;; 64-bit float/double
        com.google.cloud.bigquery.StandardSQLTypeName/FLOAT64
        (Double/valueOf raw-value)

        ;; DECIMAL/NUMERIC/BIGNUMERIC → BigDecimal
        com.google.cloud.bigquery.StandardSQLTypeName/NUMERIC
        (bigdec raw-value)

        com.google.cloud.bigquery.StandardSQLTypeName/BIGNUMERIC
        (bigdec raw-value)

        ;; DATE, DATETIME, TIME, TIMESTAMP, etc.
        ;; You could parse them into java.time.* if you like.
        ;; Here we just return the raw string.
        com.google.cloud.bigquery.StandardSQLTypeName/DATE raw-value
        com.google.cloud.bigquery.StandardSQLTypeName/DATETIME raw-value
        com.google.cloud.bigquery.StandardSQLTypeName/TIME raw-value
        com.google.cloud.bigquery.StandardSQLTypeName/TIMESTAMP raw-value

        ;; GEOGRAPHY, INTERVAL, JSON => we just return the raw string
        com.google.cloud.bigquery.StandardSQLTypeName/GEOGRAPHY raw-value
        com.google.cloud.bigquery.StandardSQLTypeName/INTERVAL raw-value
        com.google.cloud.bigquery.StandardSQLTypeName/JSON raw-value

        ;; Default case: return the raw string if we don't have a more specific parse
        raw-value))))
