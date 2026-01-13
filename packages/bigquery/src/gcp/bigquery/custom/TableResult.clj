(ns gcp.bigquery.custom.TableResult
  (:require [gcp.bigquery.bindings.Field :as Field]
            [gcp.bigquery.bindings.FieldElementType :as FieldElementType]
            [gcp.global :as global]
            [jsonista.core :as j])
  (:import (com.google.cloud.bigquery FieldValue TableResult Range Range$Builder)
           (java.time LocalDate LocalDateTime)
           (java.time.format DateTimeFormatter)))

(defn ^Range Range-from-edn
  [{:keys [end start type]}]
  (let [builder (Range$Builder.)]
    (some-> end (.setEnd builder))
    (some-> start (.setStart builder))
    (when type
      (.setType builder (FieldElementType/from-edn type)))
    (.build builder)))

(defn Range-to-edn [^Range arg]
  (cond-> {}
          (some? (.getEnd arg))
          (assoc :end (.getEnd arg))
          (some? (.getStart arg))
          (assoc :start (.getStart arg))
          (some? (.getType arg))
          (assoc :type (FieldElementType/to-edn (.getType arg)))
          (some? (.getValues arg))
          (assoc :values (.getValues arg))))

(def date-formatter     (DateTimeFormatter/ofPattern "yyyy-MM-dd"))
(def datetime-formatter (DateTimeFormatter/ofPattern "yyyy-MM-dd'T'HH:mm:ss"))

(defn field-value-to-edn
  [{field-type :type
    sub-fields :subFields
    :as field} ^FieldValue field-value]
  (global/strict! :gcp.bigquery.v2/LegacySQLTypeName field-type)
  (when-not (.isNull field-value)
    (case (.name (.getAttribute field-value))
      "RECORD" (let [fvl (.getRecordValue field-value)]
                 (if (.hasSchema fvl)
                   (into {}
                         (map
                           (fn [[sf fv]]
                             [(keyword (:name sf)) (field-value-to-edn sf fv)]))
                         (map vector sub-fields fvl))
                   fvl))
      "RANGE" (Range-to-edn (.getRangeValue field-value))
      "REPEATED" (mapv (partial field-value-to-edn field) (.getRepeatedValue field-value))
      "PRIMITIVE"
      (case field-type
        "BOOLEAN"   (.getBooleanValue field-value)
        "STRING"    (.getStringValue field-value)
        "INTEGER"   (.getLongValue field-value)
        "FLOAT"     (.getDoubleValue field-value)
        "TIMESTAMP" (.getTimestampInstant field-value)
        "NUMERIC"   (.getNumericValue field-value)
        "DATE"      (LocalDate/parse (.getValue field-value) date-formatter)
        "DATETIME"  (LocalDateTime/parse (.getValue field-value) datetime-formatter)
        "JSON"      (j/read-value (.getValue field-value) j/keyword-keys-object-mapper)
        "BYTES"     (.getBytesValue field-value)
        "GEOGRAPHY" (.getStringValue field-value)
        "INTERVAL"  (.getPeriodDuration field-value) ;org.threeten.extra.PeriodDuration
        (throw (ex-info (str "unimplemented PRIMITIVE '" field-type "'") {:field-value field-value
                                                                          :field-type  field-type})))
      (throw (ex-info (str "unimplemented FieldValue attribute: '" (.name (.getAttribute field-value)) "'")
                      {:field field :fieldValue field-value})))))

(defn to-edn [^TableResult res]
  (if (zero? (.getTotalRows res))
    []
    (let [schema (.getSchema res)]
      (if (nil? schema)
        res
        (let [column-fields (mapv Field/to-edn (.getFields schema))
              ;; TODO some queries return singular fields with anonymous names
              ;; ... could drop the name and just return parsed value
              row->map (fn [row-value-list]
                         (into {}
                               (map
                                 (fn [[field field-value]]
                                   [(keyword (:name field)) (field-value-to-edn field field-value)]))
                               (map vector column-fields row-value-list)))]
          (if-not (.hasNextPage res)
            (map row->map (.iterateAll res))
            (sequence cat
              (iteration
                (fn [page]
                  (cond
                    (nil? page) res
                    (.hasNextPage page) (.getNextPage page)
                    :else nil))
                :somef some?
                :vf (fn [page] (map row->map (.getValues page)))
                :kf identity
                :initk nil))))))))

(def schemas
  {:gcp.bigquery/TableResult :any
   :gcp.bigquery/Range
   [:map {:closed true}
    [:start {:optional true} :any]
    [:end {:optional true} :any]
    [:type {:optional true} :gcp.bigquery/FieldElementType]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))