(ns gcp.bigquery.tests.field-tests
  (:require [clojure.string :as string]
            [clojure.test :refer :all]
            [gcp.bigquery :as bq]
            [gcp.bigquery.core :as bqc]
            [gcp.bigquery.custom :refer [Field-from-edn Field-to-edn]]
            [gcp.bigquery.tests.util :as util]
            [gcp.global :as g]
            [clojure.edn :as edn]))

(def dataset-id {:dataset "field_test"})
(def dataset-info {:datasetId dataset-id
                   :location "us-east1"})

(def field-test-tables
  "Each schema column's :description is a valid Clojure expression that evaluates to the value used for roundtrip testing.
   Keep expressions self-contained and preserve precision/type-specific edge cases; do not put prose in :description.
   1) creates the table from :schema table-info
   2) for each field create values (eval (edn/read-string (get field :description)))
   3) assemble and insert row
   4) read row back and compare"
  {"INTEGER"
   {:tableId {:dataset "field_test" :table "INTEGER"}
    :description "INTEGER boundary and basic integer values."
    :schema [{:name "Long_MIN_VALUE"
              :type "INTEGER"
              :description "Long/MIN_VALUE"}
             {:name "minus1"
              :type "INTEGER"
              :description "-1"}
             {:name "zero"
              :type "INTEGER"
              :description "0"}
             {:name "plus1"
              :type "INTEGER"
              :description "1"}
             {:name "Long_MAX_VALUE"
              :type "INTEGER"
              :description "Long/MAX_VALUE"}]}
   "FLOAT"
   {:tableId {:dataset "field_test" :table "FLOAT"}
    :description "IEEE-754 FLOAT roundtrip values including finite boundaries, subnormals, non-finite values, fractional values, and large approximate values."
    :schema [{:name "negative_max"
              :type "FLOAT"
              :description "(- Double/MAX_VALUE)"}
             {:name "negative_min"
              :type "FLOAT"
              :description "(- Double/MIN_VALUE)"}
             {:name "negative1"
              :type "FLOAT"
              :description "-1.0"}
             {:name "zero"
              :type "FLOAT"
              :description "0.0"}
             {:name "positive_min"
              :type "FLOAT"
              :description "Double/MIN_VALUE"}
             {:name "positive1"
              :type "FLOAT"
              :description "1.0"}
             {:name "positive_max"
              :type "FLOAT"
              :description "Double/MAX_VALUE"}
             {:name "NaN"
              :type "FLOAT"
              :description "Double/NaN"}
             {:name "positive_infinity"
              :type "FLOAT"
              :description "Double/POSITIVE_INFINITY"}
             {:name "negative_infinity"
              :type "FLOAT"
              :description "Double/NEGATIVE_INFINITY"}
             {:name "fraction"
              :type "FLOAT"
              :description "0.1"}
             {:name "precision_loss"
              :type "FLOAT"
              :description "(Double/parseDouble \"1e30\")"}]}
   "NUMERIC"
   {:tableId {:dataset "field_test" :table "NUMERIC"}
    :description "NUMERIC values and precision/scale constraints."
    :schema [{:name "zero"
              :type "NUMERIC"
              :description "0M"}
             {:name "negative1"
              :type "NUMERIC"
              :description "-1M"}
             {:name "positive1"
              :type "NUMERIC"
              :description "1M"}
             {:name "min_increment"
              :type "NUMERIC"
              :description "(bigdec \"0.000000001\")"}
             {:name "max_scale"
              :type "NUMERIC"
              :precision 29
              :scale 9
              :description "(bigdec \"99999999999999999999999999999.999999999\")"}
             {:name "integer_only"
              :type "NUMERIC"
              :precision 29
              :scale 0
              :description "(bigdec \"99999999999999999999999999999\")"}
             {:name "one_digit_integer"
              :type "NUMERIC"
              :precision 10
              :scale 9
              :description "9.999999999M"}
             {:name "max_unparameterized"
              :type "NUMERIC"
              :description "99999999999999999999999999999.999999999M"}
             {:name "min_unparameterized"
              :type "NUMERIC"
              :description "-99999999999999999999999999999.999999999M"}]}
   "BIGNUMERIC"
   {:tableId {:dataset "field_test" :table "BIGNUMERIC"}
    :description "BIGNUMERIC values and precision/scale constraints."
    :schema [{:name "zero"
              :type "BIGNUMERIC"
              :description "0M"}
             {:name "negative1"
              :type "BIGNUMERIC"
              :description "-1M"}
             {:name "positive1"
              :type "BIGNUMERIC"
              :description "1M"}
             {:name "min_increment"
              :type "BIGNUMERIC"
              :description "(bigdec \"0.00000000000000000000000000000000000001\")"}
             {:name "max_scale"
              :type "BIGNUMERIC"
              :precision 76
              :scale 38
              :description "(bigdec \"99999999999999999999999999999999999999.99999999999999999999999999999999999999\")"}
             {:name "integer_only"
              :type "BIGNUMERIC"
              :precision 38
              :scale 0
              :description "99999999999999999999999999999999999999M"}
             {:name "one_digit_integer"
              :type "BIGNUMERIC"
              :precision 39
              :scale 38
              :description "9.99999999999999999999999999999999999999M"}
             {:name "max_unparameterized"
              :type "BIGNUMERIC"
              :description "578960446186580977117854925043439539267.99999999999999999999999999999999999999M"}
             {:name "min_unparameterized"
              :type "BIGNUMERIC"
              :description "-578960446186580977117854925043439539267.99999999999999999999999999999999999999M"}]}
   "BYTES"
   {:tableId {:dataset "field_test" :table "BYTES"}
    :description "Raw binary values including empty, ASCII, zero, high-bit, non-UTF-8, and mixed binary data."
    :schema [{:name "empty"
              :type "BYTES"
              :description "(byte-array [])"}
             {:name "ascii"
              :type "BYTES"
              :description "(byte-array [72 101 108 108 111])"}
             {:name "zero_byte"
              :type "BYTES"
              :description "(byte-array [0])"}
             {:name "high_bit"
              :type "BYTES"
              :description "(byte-array [-128 -1])"}
             {:name "non_utf8"
              :type "BYTES"
              :description "(byte-array [-1 -2 -3 0])"}
             {:name "binary"
              :type "BYTES"
              :description "(byte-array [0 1 2 127 -128 -2 -1])"}]}
   "STRING"
   {:tableId {:dataset "field_test" :table "STRING"}
    :description "Unicode STRING roundtrip values including empty, ASCII, Unicode, combining characters, supplementary code points, and mixed-width UTF-8."
    :schema [{:name "empty"
              :type "STRING"
              :description "\"\""}
             {:name "ascii"
              :type "STRING"
              :description "\"Hello, world!\""}
             {:name "unicode"
              :type "STRING"
              :description "\"こんにちは世界\""}
             {:name "emoji"
              :type "STRING"
              :description "\"😀🌍🚀\""}
             {:name "combining"
              :type "STRING"
              :description "\"e\\u0301\""}
             {:name "precomposed"
              :type "STRING"
              :description "\"é\""}
             {:name "mixed_width"
              :type "STRING"
              :description "\"Aé中😀\""}
             {:name "supplementary"
              :type "STRING"
              :description "\"𝄞\""}
             {:name "long"
              :type "STRING"
              :description "(apply str (repeat 1000 \"x\"))"}]}
   "TIME"
   {:tableId {:dataset "field_test" :table "TIME"}
    :description "TIME roundtrip values covering boundaries, ordinary times, and microsecond precision."
    :schema [{:name "midnight"
              :type "TIME"
              :description "java.time.LocalTime/MIDNIGHT"}
             {:name "one_second"
              :type "TIME"
              :description "(java.time.LocalTime/of 0 0 1)"}
             {:name "fraction"
              :type "TIME"
              :description "(java.time.LocalTime/of 12 34 56 123456000)"}
             {:name "one_microsecond"
              :type "TIME"
              :description "(java.time.LocalTime/of 0 0 0 1000)"}
             {:name "max_microsecond"
              :type "TIME"
              :description "(java.time.LocalTime/of 23 59 59 999999000)"}
             {:name "last_second"
              :type "TIME"
              :description "(java.time.LocalTime/of 23 59 59)"}]}
   "DATE"
   {:tableId {:dataset "field_test" :table "DATE"}
    :description "DATE roundtrip values covering Gregorian calendar boundaries, ordinary dates, and leap years."
    :schema [{:name "minimum"
              :type "DATE"
              :description "(java.time.LocalDate/of 1 1 1)"}
             {:name "epoch"
              :type "DATE"
              :description "(java.time.LocalDate/of 1970 1 1)"}
             {:name "ordinary"
              :type "DATE"
              :description "(java.time.LocalDate/of 2026 10 7)"}
             {:name "leap_day"
              :type "DATE"
              :description "(java.time.LocalDate/of 2024 2 29)"}
             {:name "century_non_leap"
              :type "DATE"
              :description "(java.time.LocalDate/of 1900 2 28)"}
             {:name "century_leap"
              :type "DATE"
              :description "(java.time.LocalDate/of 2000 2 29)"}
             {:name "maximum"
              :type "DATE"
              :description "(java.time.LocalDate/of 9999 12 31)"}]}
   "DATETIME"
   {:tableId {:dataset "field_test" :table "DATETIME"}
    :description "DATETIME roundtrip values covering Gregorian calendar boundaries, ordinary dates, leap years, and microsecond precision."
    :schema [{:name "minimum"
              :type "DATETIME"
              :description "(java.time.LocalDateTime/of 1 1 1 0 0)"}
             {:name "epoch"
              :type "DATETIME"
              :description "(java.time.LocalDateTime/of 1970 1 1 0 0)"}
             {:name "ordinary"
              :type "DATETIME"
              :description "(java.time.LocalDateTime/of 2026 10 7 12 34 56)"}
             {:name "leap_day"
              :type "DATETIME"
              :description "(java.time.LocalDateTime/of 2024 2 29 23 59 59)"}
             {:name "fraction"
              :type "DATETIME"
              :description "(java.time.LocalDateTime/of 2026 10 7 12 34 56 123456000)"}
             {:name "one_microsecond"
              :type "DATETIME"
              :description "(java.time.LocalDateTime/of 2026 10 7 0 0 0 1000)"}
             {:name "maximum"
              :type "DATETIME"
              :description "(java.time.LocalDateTime/of 9999 12 31 23 59 59 999999000)"}]}
   "INTERVAL"
   {:tableId {:dataset "field_test" :table "INTERVAL"}
    :description "INTERVAL values covering zero, calendar components, day components, time components, mixed components, negative values, and microsecond precision."
    :schema [{:name "zero"
              :type "INTERVAL"
              :description "org.threeten.extra.PeriodDuration/ZERO"}
             {:name "years_months"
              :type "INTERVAL"
              :description "(org.threeten.extra.PeriodDuration/of (java.time.Period/of 2 11 0))"}
             {:name "days"
              :type "INTERVAL"
              :description "(org.threeten.extra.PeriodDuration/of (java.time.Period/ofDays 42))"}
             {:name "hours"
              :type "INTERVAL"
              :description "(org.threeten.extra.PeriodDuration/of (java.time.Duration/ofHours 25))"}
             {:name "minutes_seconds"
              :type "INTERVAL"
              :description "(org.threeten.extra.PeriodDuration/of (java.time.Duration/ofSeconds 91))"}
             {:name "mixed"
              :type "INTERVAL"
              :description "(org.threeten.extra.PeriodDuration/of (java.time.Period/of 2 11 28) (java.time.Duration/ofSeconds 58514))"}
             {:name "microseconds"
              :type "INTERVAL"
              :description "(org.threeten.extra.PeriodDuration/of (java.time.Duration/ofNanos 123456000))"}
             {:name "negative"
              :type "INTERVAL"
              :description "(org.threeten.extra.PeriodDuration/of (java.time.Period/of -2 -11 -28) (java.time.Duration/ofSeconds -58514))"}
             {:name "max"
              :type "INTERVAL"
              :description "(org.threeten.extra.PeriodDuration/of (java.time.Period/of 10000 0 3660000) (java.time.Duration/ofSeconds 87840000))"}
             {:name "min"
              :type "INTERVAL"
              :description "(org.threeten.extra.PeriodDuration/of (java.time.Period/of -10000 0 -3660000) (java.time.Duration/ofSeconds -87840000))"}]}
   "GEOGRAPHY"
   {:tableId {:dataset "field_test" :table "GEOGRAPHY"}
    :description "GEOGRAPHY values represented as WKT strings."
    :schema [{:name "point"
              :type "GEOGRAPHY"
              :description "\"POINT(-66.1057 18.4655)\""}
             {:name "line"
              :type "GEOGRAPHY"
              :description "\"LINESTRING(-66.1057 18.4655, -66.1060 18.4660)\""}
             {:name "polygon"
              :type "GEOGRAPHY"
              :description "\"POLYGON((-66 18, -67 18, -67 19, -66 19, -66 18))\""}
             {:name "multipoint"
              :type "GEOGRAPHY"
              :description "\"MULTIPOINT((-66 18), (-67 19))\""}
             {:name "empty"
              :type "GEOGRAPHY"
              :description "\"POINT EMPTY\""}]}
   "JSON"
   {:tableId {:dataset "field_test" :table "JSON"}
    :description "JSON values covering scalar, object, array, nested, null, and explicit JSON string representations."
    :schema [{:name "object"
              :type "JSON"
              :description "{:name \"Alice\", :age 42, :active true}"}
             {:name "nested"
              :type "JSON"
              :description "{:user {:name \"Alice\", :address {:city \"San Juan\", :country \"PR\"}}}"}
             {:name "array"
              :type "JSON"
              :description "[1 2 3 4 5]"}
             {:name "mixed_array"
              :type "JSON"
              :description "[\"hello\" 42 true nil {:nested [1 2 3]}]"}
             {:name "string"
              :type "JSON"
              :description "\"hello\""}
             {:name "number"
              :type "JSON"
              :description "123.456M"}
             {:name "boolean"
              :type "JSON"
              :description "true"}
             {:name "null"
              :type "JSON"
              :description "nil"}
             {:name "explicit_json"
              :type "JSON"
              :description "\"{\\\"name\\\":\\\"Alice\\\",\\\"values\\\":[1,2,3]}\""}]}
   "TIMESTAMP"
   {:tableId {:dataset "field_test" :table "TIMESTAMP"}
    :description "TIMESTAMP roundtrip values covering UTC boundaries, epoch boundaries, negative epoch values, microsecond precision, and nanosecond precision."
    :schema [{:name "minimum"
              :type "TIMESTAMP"
              :description "(java.time.Instant/parse \"0001-01-01T00:00:00Z\")"}
             {:name "epoch"
              :type "TIMESTAMP"
              :description "java.time.Instant/EPOCH"}
             {:name "before_epoch"
              :type "TIMESTAMP"
              :description "(java.time.Instant/parse \"1969-12-31T23:59:59.999999Z\")"}
             {:name "microsecond"
              :type "TIMESTAMP"
              :description "(java.time.Instant/parse \"2026-10-07T12:34:56.123456Z\")"}
             {:name "maximum"
              :type "TIMESTAMP"
              :description "(java.time.Instant/parse \"9999-12-31T23:59:59.999999Z\")"}]}
   "RANGE"
   {:tableId {:dataset "field_test" :table "RANGE"}
    :description "RANGE values covering DATE, DATETIME, and TIMESTAMP elements, bounded and unbounded ranges, and single-sided bounds."
    :schema [
             {:name "date_bounded"
              :type "RANGE"
              :rangeElementType "DATE"
              :description "{:type \"DATE\", :start (java.time.LocalDate/of 2026 1 1), :end (java.time.LocalDate/of 2026 12 31)}"}
             {:name "date_unbounded_start"
              :type "RANGE"
              :rangeElementType "DATE"
              :description "{:type \"DATE\", :end (java.time.LocalDate/of 2026 12 31)}"}
             {:name "date_unbounded_end"
              :type "RANGE"
              :rangeElementType "DATE"
              :description "{:type \"DATE\", :start (java.time.LocalDate/of 2026 1 1)}"}
             {:name "datetime_bounded"
              :type "RANGE"
              :rangeElementType "DATETIME"
              :description "{:type \"DATETIME\", :start (java.time.LocalDateTime/of 2026 1 1 0 0), :end (java.time.LocalDateTime/of 2026 12 31 23 59 59 999999000)}"}
             {:name "datetime_unbounded_start"
              :type "RANGE"
              :rangeElementType "DATETIME"
              :description "{:type \"DATETIME\", :end (java.time.LocalDateTime/of 2026 12 31 23 59 59)}"}
             {:name "datetime_unbounded_end"
              :type "RANGE"
              :rangeElementType "DATETIME"
              :description "{:type \"DATETIME\", :start (java.time.LocalDateTime/of 2026 1 1 0 0)}"}
             {:name "timestamp_bounded"
              :type "RANGE"
              :rangeElementType "TIMESTAMP"
              :description "{:type \"TIMESTAMP\", :start (java.time.Instant/parse \"2026-01-01T00:00:00Z\"), :end (java.time.Instant/parse \"2026-12-31T23:59:59.999999Z\")}"}
             {:name "timestamp_unbounded_start"
              :type "RANGE"
              :rangeElementType "TIMESTAMP"
              :description "{:type \"TIMESTAMP\", :end (java.time.Instant/parse \"2026-12-31T23:59:59Z\")}"}
             {:name "timestamp_unbounded_end"
              :type "RANGE"
              :rangeElementType "TIMESTAMP"
              :description "{:type \"TIMESTAMP\", :start (java.time.Instant/parse \"2026-01-01T00:00:00Z\")}"}]}
   "STRUCT"
   {:tableId {:dataset "field_test" :table "STRUCT"}
    :description "STRUCT values covering ordered fields, named and unnamed fields, nested structs, mixed types, and null field values."
    :schema [
             {:name "simple"
              :type "RECORD"
              :subFields [{:name "id"
                           :type "INTEGER"}
                          {:name "name"
                           :type "STRING"}
                          {:name "active"
                           :type "BOOLEAN"}]
              :description "{:id 42, :name \"Alice\", :active true}"}
             {:name "ordered"
              :type "RECORD"
              :subFields [{:name "first"
                           :type "STRING"}
                          {:name "second"
                           :type "INTEGER"}
                          {:name "third"
                           :type "FLOAT"}]
              :description "{:first \"one\", :second 2, :third 3.0}"}
             {:name "nested"
              :type "RECORD"
              :subFields [{:name "id"
                           :type "INTEGER"}
                          {:name "address"
                           :type "RECORD"
                           :subFields [{:name "city"
                                        :type "STRING"}
                                       {:name "zip"
                                        :type "INTEGER"}]}]
              :description "{:id 42, :address {:city \"San Juan\", :zip 901}}"}
             {:name "mixed"
              :type "RECORD"
              :subFields [{:name "date"
                           :type "DATE"}
                          {:name "timestamp"
                           :type "TIMESTAMP"}
                          {:name "amount"
                           :type "NUMERIC"}
                          {:name "location"
                           :type "GEOGRAPHY"}]
              :description "{:date (java.time.LocalDate/of 2026 10 7), :timestamp (java.time.Instant/parse \"2026-10-07T12:34:56Z\"), :amount 123.45M, :location {:geography \"POINT(-66.1057 18.4655)\"}}"}
             {:name "null_field"
              :type "RECORD"
              :subFields [{:name "present"
                           :type "STRING"}
                          {:name "missing"
                           :type "STRING"}]
              :description "{:present \"value\", :missing nil}"}]}})

(defn read-description [s]
  (let [val (edn/read-string s)]
    (if (or (symbol? val) (list? val))
      (eval val)
      val)))


(doseq [[k {:keys [schema tableId]}] (into (sorted-map) field-test-tables)]
  (g/coerce ::bq/Schema schema)
  (let [table-info {:tableId tableId
                    :definition {:type "TABLE"
                                 :schema schema}}]
    (testing "fixture field Integrity"
      (doseq [{:keys [name type description] :as field} schema]
        (g/coerce ::bq/Field field)
        (assert (= field (Field-to-edn (Field-from-edn field)))
                (do
                  (clojure.pprint/pprint field)
                  (clojure.pprint/pprint (Field-to-edn (Field-from-edn field)))
                  (str "Field " name " does not roundtrip through binding")))
        (let [val (edn/read-string description)]
          (assert (= val (edn/read-string (pr-str val)))
                  (str type ":" name ": '" description "' is not edn-safe")))
        (read-description description)))
    (g/coerce ::bq/TableInfo table-info)
    (try
      (bqc/->TableCreate [table-info])
      (catch Throwable e
        (throw (ex-info "error parsing table-info" {:table-info table-info
                                                    :cause e}))))))

(deftest field-test
  (try
    (bq/create-dataset dataset-info)
    (doseq [[type {:keys [schema tableId]}] (into (sorted-map) field-test-tables)]
      (let [table-info {:tableId tableId
                        :definition {:type "TABLE"
                                     :schema schema}}]
        (testing type
          (bq/create-table table-info)
          )))
    (finally
      (util/wipe-dataset! dataset-id))))

(deftest struct-test
  (is (= [{:anonymous       {:_field_1 1, :_field_2 "hello", :_field_3 {:_field_1 true, :_field_2 3.14}},
           :named           {:x 1, :y 2},
           :duplicate_names {:x 1, :_field_2 2},
           :mixed_names     {:_field_1 1, :_field_2 2, :x 3, :_field_4 4}}]
         (bq/query
          (string/join
            ["SELECT"
             "  STRUCT(1, 'hello', STRUCT(TRUE, 3.14)) AS anonymous,"
             "  STRUCT(1 AS x, 2 AS y) AS named,"
             "  STRUCT(1 AS x, 2 AS x) AS duplicate_names,"
             "  STRUCT(1 AS _field_1, 2, 3 AS x, 4 AS x) AS mixed_names;"])))))


(comment
  (do (require :reload 'gcp.bigquery.tests.field-tests) (in-ns 'gcp.bigquery.tests.field-tests))
  (util/wipe-dataset! dataset-info)
  (def res (util/run-test-capture #'field-test))
  (def err (->> res :events (filter #(= :error (:type %))) first :actual))

  (g/get-schema :gcp.bigquery/Field)
  (g/get-schema :gcp.bigquery/PolicyTags)

  )