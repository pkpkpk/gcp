(ns gcp.bigquery.custom-tests
  (:require [clojure.test :refer :all]
            [gcp.bigquery.custom :refer [QueryParameterValue-from-edn
                                         QueryParameterValue-to-edn
                                         Range-from-edn
                                         Range-to-edn]]
            [gcp.global :as g])
  (:import (com.google.gson JsonObject)
           (java.time Instant LocalDate LocalDateTime LocalTime)
           (java.time.temporal ChronoUnit)
           (org.threeten.extra PeriodDuration)))

(deftest scalar-roundtrip-test
  (and
    (testing "DATETIME Represents a year, month, day, hour, minute, second, and subsecond (microsecond precision)"
      (let [arg (LocalDateTime/now)]
        (and
          (is (g/valid? :gcp.bigquery.custom/QueryParameterValue.SCALAR arg))
          (is (= "DATETIME" (.name (.getType (QueryParameterValue-from-edn arg)))))
          (is (= (.truncatedTo arg ChronoUnit/MICROS)
                 (QueryParameterValue-to-edn (QueryParameterValue-from-edn arg)))))))
    (testing "DATE Represents a logical calendar date. Values range between the years 1 and 9999, inclusive"
      (let [arg (LocalDate/now)]
        (and
          (is (g/valid? :gcp.bigquery.custom/QueryParameterValue.SCALAR arg))
          (is (= "DATE" (.name (.getType (QueryParameterValue-from-edn arg)))))
          (is (= arg (QueryParameterValue-to-edn (QueryParameterValue-from-edn arg)))))))
    (testing "TIMESTAMP Represents an absolute point in time, with microsecond precision. Values range between the years 1 and 9999, inclusive."
      (let [now (Instant/now)
            arg  (.truncatedTo now ChronoUnit/MICROS)]
        (and
          (is (g/valid? :gcp.bigquery.custom/QueryParameterValue.SCALAR arg))
          (is (= "TIMESTAMP" (.name (.getType (QueryParameterValue-from-edn arg)))))
          (is (= arg (QueryParameterValue-to-edn (QueryParameterValue-from-edn arg)))))))
    (testing "TIME Represents a time, independent of a specific date, to microsecond precision."
      (let [t0  (LocalTime/now)
            arg (.truncatedTo t0 ChronoUnit/MICROS)]
        (and
          (is (g/valid? :gcp.bigquery.custom/QueryParameterValue.SCALAR arg))
          (is (= "TIME" (.name (.getType (QueryParameterValue-from-edn arg)))))
          (is (= arg (QueryParameterValue-to-edn (QueryParameterValue-from-edn arg)))))))
    (testing "BIGNUMERIC A decimal value with 76+ digits of precision (the 77th digit is partial) and 38 digits of scale"
      (let [arg (bigdec Math/PI)]
        (and
          (is (g/valid? :gcp.bigquery.custom/QueryParameterValue.SCALAR arg))
          (is (= "BIGNUMERIC" (.name (.getType (QueryParameterValue-from-edn arg)))))
          (is (= arg (QueryParameterValue-to-edn (QueryParameterValue-from-edn arg)))))))
    (testing "NUMERIC A decimal value with 38 digits of precision and 9 digits of scale."
      (let [arg (bigdec 42)]
        (and
          (is (g/valid? :gcp.bigquery.custom/QueryParameterValue.SCALAR arg))
          (is (= "NUMERIC" (.name (.getType (QueryParameterValue-from-edn arg)))))
          (is (= arg (QueryParameterValue-to-edn (QueryParameterValue-from-edn arg)))))))
    (testing "GEOGRAPHY Represents a set of geographic points, represented as a Well Known Text (WKT) string."
      (let [arg {:geography "POINT(-65.79139 18.31056)"}]
        (and
          (is (g/valid? :gcp.bigquery.custom/QueryParameterValue.SCALAR arg))
          (is (= "GEOGRAPHY" (.name (.getType (QueryParameterValue-from-edn arg)))))
          (is (= arg (QueryParameterValue-to-edn (QueryParameterValue-from-edn arg)))))))
    (testing "BOOL A Boolean value (true or false)."
      (let [arg true]
        (and
          (is (g/valid? :gcp.bigquery.custom/QueryParameterValue.SCALAR arg))
          (is (= "BOOL" (.name (.getType (QueryParameterValue-from-edn arg)))))
          (is (= arg (QueryParameterValue-to-edn (QueryParameterValue-from-edn arg)))))))
    (testing "STRING Variable-length character (Unicode) data."
      (let [arg "string"]
        (and
          (is (g/valid? :gcp.bigquery.custom/QueryParameterValue.SCALAR arg))
          (is (= "STRING" (.name (.getType (QueryParameterValue-from-edn arg)))))
          (is (= arg (QueryParameterValue-to-edn (QueryParameterValue-from-edn arg)))))))
    (testing "INT64 A 64-bit signed integer value."
      (let [arg Long/MAX_VALUE]
        (and
          (is (g/valid? :gcp.bigquery.custom/QueryParameterValue.SCALAR arg))
          (is (= "INT64" (.name (.getType (QueryParameterValue-from-edn arg)))))
          (is (= arg (QueryParameterValue-to-edn (QueryParameterValue-from-edn arg)))))))
    (testing "FLOAT64 A 64-bit IEEE binary floating-point value."
      (let [arg Double/MAX_VALUE]
        (and
          (is (g/valid? :gcp.bigquery.custom/QueryParameterValue.SCALAR arg))
          (is (= "FLOAT64" (.name (.getType (QueryParameterValue-from-edn arg)))))
          (is (= arg (QueryParameterValue-to-edn (QueryParameterValue-from-edn arg)))))))
    (testing "INTERVAL Represents duration or amount of time."
      (let [arg PeriodDuration/ZERO]
        (and
          (is (g/valid? :gcp.bigquery.custom/QueryParameterValue.SCALAR arg))
          (is (= "INTERVAL" (.name (.getType (QueryParameterValue-from-edn arg)))))
          (is (= arg (QueryParameterValue-to-edn (QueryParameterValue-from-edn arg)))))))
    (testing "RANGE Represents a contiguous range of values."
      (let [start (.truncatedTo (Instant/now) ChronoUnit/MICROS)
            end (.truncatedTo (Instant/now) ChronoUnit/MICROS)
            arg {:end end :start start :type "TIMESTAMP"}]
        (and
          (is (g/valid? :gcp.bigquery.custom/Range arg))
          (is (g/valid? :gcp.bigquery.custom/QueryParameterValue.SCALAR arg))
          (is (= "RANGE" (.name (.getType (QueryParameterValue-from-edn arg)))))
          (is (= arg (QueryParameterValue-to-edn (QueryParameterValue-from-edn arg)))))))
    (testing "JSON Represents JSON data."
      (let [arg (JsonObject/new)]
        (and
          (is (g/valid? :gcp.bigquery.custom/QueryParameterValue.SCALAR arg))
          (is (= "JSON" (.name (.getType (QueryParameterValue-from-edn arg)))))
          (is (= {} (QueryParameterValue-to-edn (QueryParameterValue-from-edn arg)))))))))

#!----------------------------------------------------------------------------------------------------------------------
;ARRAY Ordered list of zero or more elements of any non-array type.
;BYTES Variable-length binary data.
;STRUCT Container of ordered fields each with a type (required) and field name (optional).
