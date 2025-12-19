(ns gcp.bigquery.v2.QueryParameterValue
  (:require [gcp.global :as g]
            [gcp.global :as global]
            [jsonista.core :as j])
  (:import (com.google.cloud.bigquery QueryParameterValue StandardSQLTypeName)
           (com.google.gson JsonObject)
           (java.time Instant LocalDate LocalDateTime)
           (java.util Date Map)))

;; https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.QueryParameterValue

(defn ^QueryParameterValue from-edn [arg]
  (global/strict! :gcp.bigquery.v2/QueryParameterValue arg)
  (cond
    (string? arg) (QueryParameterValue/string arg)
    (boolean? arg) (QueryParameterValue/bool arg)
    (int? arg) (QueryParameterValue/int64 (Long/valueOf (long arg)))
    (float? arg) (QueryParameterValue/float64 (Double/valueOf (double arg)))
    (decimal? arg) (QueryParameterValue/numeric arg)
    (bytes? arg) (QueryParameterValue/bytes arg)

    ;; TODO rt tests to exact precision to and from clojure...
    (instance? java.util.Date arg) (QueryParameterValue/date (.toString arg))
    (instance? LocalDate arg)      (QueryParameterValue/date (.toString arg))
    (instance? LocalDateTime arg)  (QueryParameterValue/dateTime (.toString arg))
    (instance? Instant arg)        (QueryParameterValue/timestamp ^Long (.toEpochMilli ^Instant arg))

    ;; TODO reader tags?
    ;; TODO jsonista is already here, a json write path is feasible
    (instance? JsonObject arg) (QueryParameterValue/json ^JsonObject arg)
    (contains? arg :interval)  (QueryParameterValue/interval ^String (:interval arg))
    (contains? arg :geography) (QueryParameterValue/geography (:geography arg))

    (map? arg)
    (QueryParameterValue/struct (into {} (map (fn [[k v]] [(name k) (from-edn v)])) arg))

    ;; TODO explore & test structs + heterogeneous + recursive values?
    ;;   -- date-types, geo, intervals, json ?
    (sequential? arg)
    (cond
      (g/valid? [:sequential :string] arg)
      (QueryParameterValue/array ^String/1 (into-array String arg) String)

      (g/valid? [:sequential :boolean] arg)
      (QueryParameterValue/array ^Boolean/1 (into-array Boolean arg) Boolean)

      (g/valid? [:sequential :int] arg)
      (QueryParameterValue/array ^Long/1 (into-array Long arg) Long)

      (g/valid? [:sequential :float] arg)
      (QueryParameterValue/array ^Double/1 (into-array Double arg) Double)

      (g/valid? [:sequential (g/instance-schema java.math.BigDecimal)] arg)
      (QueryParameterValue/array ^BigDecimal/1 (into-array BigDecimal arg) BigDecimal)

      (g/valid? [:sequential map?] arg)
      (let [structs (into-array Object (map from-edn arg))]
        (QueryParameterValue/array ^Object/1 structs StandardSQLTypeName/STRUCT))

      true
      (throw (ex-info "unimplemented sequential type" {:arg arg})))

    :else
    (throw (IllegalArgumentException. (str "Unknown type for value: " arg)))))

(defn to-edn [^QueryParameterValue arg]
  {:post [(global/strict! :gcp.bigquery.v2/QueryParameterValue %)]}
  (cond
    (some? (.getArrayValues arg)) ;; List<QueryParameterValue>
    (mapv to-edn (.getArrayValues arg)) ;; TODO homogenous collections can return use arrays here

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

(def schemas
  {:gcp.bigquery.v2/QueryParameterValue
   [:or
    {:class 'com.google.cloud.bigquery.QueryParameterValue}
    :string
    :boolean
    :int
    :float
    'decimal?
    'bytes?
    'inst?
    (global/instance-schema com.google.gson.JsonObject)
    (global/instance-schema java.time.LocalDate)
    (global/instance-schema java.time.LocalDateTime)
    [:map {:closed true}
     [:interval :string]]
    [:map {:closed true}
     [:geography :string]]
    [:map-of [:or :keyword :string] [:ref :gcp.bigquery.v2/QueryParameterValue]]
    [:sequential [:ref :gcp.bigquery.v2/QueryParameterValue]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
