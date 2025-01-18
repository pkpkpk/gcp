(ns gcp.bigquery.v2.TableResult
  (:require [gcp.bigquery.v2.Field :as Field]
            [jsonista.core :as j])
  (:import (com.google.cloud.bigquery FieldValue TableResult)
           (java.time LocalDate LocalDateTime)
           (java.time.format DateTimeFormatter)))

;FieldValue$Attribute/RANGE
;FieldValue$Attribute/RECORD
;FieldValue$Attribute/REPEATED
;FieldValue$Attribute/PRIMITIVE

;; TODO periodDuration, range, record

(defn- parse-field-value
  [{:as field} ^FieldValue fieldValue]
  (when-not (.isNull fieldValue)
    (let [attribute (.name (.getAttribute fieldValue))]
      (if (= attribute "PRIMITIVE")
        (case (:type field)
          "STRING"    (.getStringValue fieldValue)
          "FLOAT"     (.getDoubleValue fieldValue)
          "TIMESTAMP" (.getTimestampInstant fieldValue)
          "INTEGER"   (.getLongValue fieldValue)
          "BOOLEAN"   (.getBooleanValue fieldValue)
          "NUMERIC"   (.getNumericValue fieldValue)
          "DATE"      (LocalDate/parse (.getValue fieldValue) (DateTimeFormatter/ofPattern "yyyy-MM-dd"))
          "DATETIME"  (LocalDateTime/parse (.getValue fieldValue) (DateTimeFormatter/ofPattern "yyyy-MM-dd'T'HH:mm:ss"))
          "JSON"      (j/read-value (.getValue fieldValue) j/keyword-keys-object-mapper)
          (throw (ex-info (str "unimplemented field type " (:type field)) {:field field :fieldValue field})))
        (if (= attribute "REPEATED")
          (case (:type field)
            "STRING" (mapv #(.getStringValue %) (.getRepeatedValue fieldValue))
            (throw (Exception. (str "unimplemented REPEATED type '" (:type field) "'"))))
          (throw (Exception. (str "unimplemented attribute '" attribute "'"))))))))

(defn- parse-row
  [{:as field} fieldValue]
  [(keyword (:name field)) (parse-field-value field fieldValue)])

(defn to-edn [^TableResult res]
  (when (.hasNextPage res)
    (throw (Exception. "unimplemented")))
  (if-let [schema (.getSchema res)]
    (let [columns    (mapv Field/to-edn (.getFields schema))
          row-parser (fn [row]
                       (into {} (map parse-row columns row)))]
      (mapv row-parser (.iterateAll res)))
    (if (zero? (.getTotalRows res))
      []
      res)))