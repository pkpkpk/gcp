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
