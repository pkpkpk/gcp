(ns gcp.bigquery.custom
  (:require [clojure.walk :as walk]
            [gcp.bigquery.PolicyTags :as PolicyTags]
            [gcp.bigquery.TableId :as TableId]
            [gcp.global :as g]
            [jsonista.core :as j])
  (:import (com.google.cloud.bigquery Field
                                      Field$Mode
                                      FieldElementType
                                      FieldValue
                                      InsertAllRequest LegacySQLTypeName
                                      QueryParameterValue
                                      Range
                                      StandardSQLTypeName
                                      TableResult)
           (com.google.gson JsonObject)
           (java.time Instant LocalDate LocalDateTime LocalTime OffsetDateTime ZoneOffset)
           (java.time.format DateTimeFormatter DateTimeFormatterBuilder)
           (java.time.temporal ChronoField ChronoUnit)
           (java.util Base64 Date HashMap)
           (org.threeten.extra PeriodDuration)))

#!-----------------------------
#! :Time

(def ^DateTimeFormatter ^:private
  time-formatter
  (-> (DateTimeFormatterBuilder.)
      (.appendPattern "HH:mm:ss")
      (.appendFraction ChronoField/MICRO_OF_SECOND 6 6 true)
      (.toFormatter)))

(defn String->Time [s]
  (.truncatedTo (LocalTime/parse s time-formatter) ChronoUnit/MICROS))

(defn- ^String Time->String
  [^LocalTime t]
  (.format (.truncatedTo t ChronoUnit/MICROS) time-formatter))

#!-----------------------------
#! :Date

(def date-formatter (DateTimeFormatter/ofPattern "yyyy-MM-dd"))

(defn ^LocalDate String->Date [s]
  (LocalDate/parse s date-formatter))

(defn ^String Date->String
  [^LocalDate d]
  (.format d date-formatter))

#!-----------------------------
#! :DateTime

(def ^DateTimeFormatter ^:private
  datetime-formatter
  (-> (DateTimeFormatterBuilder.)
      (.appendPattern "yyyy-MM-dd HH:mm:ss")
      (.optionalStart)
      (.appendFraction ChronoField/NANO_OF_SECOND 1 9 true)
      (.optionalEnd)
      (.toFormatter)))

(defn- String->DateTime [s]
  (LocalDateTime/parse s datetime-formatter))

(defn- ^String DateTime->String
  [^LocalDateTime arg]
  (.replace (.toString (.truncatedTo arg ChronoUnit/MICROS)) "T" " "))

#!-----------------------------

(def ^DateTimeFormatter ^:private
  timestamp-formatter
  (-> (DateTimeFormatterBuilder.)
      (.appendPattern "yyyy-MM-dd HH:mm:ss")
      (.optionalStart)
      (.appendFraction ChronoField/NANO_OF_SECOND 1 9 true)
      (.optionalEnd)
      (.appendOffsetId) ; parses +00:00, -05:00, Z, etc.
      (.toFormatter)))

(defn- ^Instant String->Instant [^String s]
  (.toInstant (OffsetDateTime/parse s timestamp-formatter)))

(defn- ^String Instant->String
  [inst]
  (let [^Instant inst (if (instance? Date inst) (.toInstant ^Date inst) inst)
        inst (.truncatedTo inst ChronoUnit/MICROS)]
    (-> (OffsetDateTime/ofInstant inst ZoneOffset/UTC)
        (.format timestamp-formatter))))

#!------------------------------------------------
#! FieldElementType

(def StandardSQLTypeNames-schema
  [:enum
   "BOOL"
   "INT64"
   "FLOAT64"
   "NUMERIC"
   "BIGNUMERIC"
   "STRING"
   "BYTES"
   "STRUCT"
   "ARRAY"
   "TIMESTAMP"
   "DATE"
   "TIME"
   "DATETIME"
   "GEOGRAPHY"
   "JSON"
   "INTERVAL"
   "RANGE"])

(def LegacySQLTypeNames-schema
  [:enum
   "BYTES"
   "STRING"
   "INTEGER"
   "FLOAT"
   "NUMERIC"
   "BIGNUMERIC"
   "BOOLEAN"
   "TIMESTAMP"
   "DATE"
   "GEOGRAPHY"
   "TIME"
   "DATETIME"
   "RECORD"
   "JSON"
   "INTERVAL"
   "RANGE"])

(def FieldElementType-schema
  [:or
   {:closed       true,
    :doc          nil,
    :gcp/category :accessor-with-builder,
    :gcp/key      :gcp.bigquery/FieldElementType}
   StandardSQLTypeNames-schema
   LegacySQLTypeNames-schema
   [:map
    [:type
     {:optional true,
      :getter-doc "The subtype of the RANGE, if the field type is RANGE.\n\n@return value or {@code null} for none"}
     [:or StandardSQLTypeNames-schema
          LegacySQLTypeNames-schema]]]])

(defn ^FieldElementType FieldElementType-from-edn [arg]
  (g/strict! :gcp.bigquery/FieldElementType arg)
  (if (string? arg)
    (-> (FieldElementType/newBuilder) (.setType arg) (.build))
    (let [builder (FieldElementType/newBuilder)]
      (when (get arg :type) (.setType builder (get arg :type)))
      (.build builder))))

(defn FieldElementType-to-edn [^FieldElementType arg]
  {:post [(g/strict! :gcp.bigquery/FieldElementType %)]}
  (cond-> {} (.getType arg) (assoc :type (.getType arg))))

#!------------------------------------------------
#! Field

(def legacy-sql-schema
  [:enum
   {:doc "A type used in legacy SQL contexts. NOTE: some contexts use a mix of types; for example, for
          queries that use standard SQL, the return types are the legacy SQL types.
          @see <a href=\"https://cloud.google.com/bigquery/data-types\">https://cloud.google.com/bigquery/data-types</a>"}
   "BYTES"
   "STRING"
   "INTEGER"
   "FLOAT"
   "NUMERIC"
   "BIGNUMERIC"
   "BOOLEAN"
   "TIMESTAMP"
   "DATE"
   "GEOGRAPHY"
   "TIME"
   "DATETIME"
   "RECORD"
   "JSON"
   "INTERVAL"
   "RANGE"])

(def _Field-fields
  [:map
   [:collation
    {:optional true,
     :setter-doc
     "Optional. Field collation can be set only when the type of field is STRING. The following\nvalues are supported:\n\n<p>* 'und:ci': undetermined locale, case insensitive. * '': empty string. Default to\ncase-sensitive behavior. (-- A wrapper is used here because it is possible to set the value\nto the empty string. --)"}
    :string]
   [:defaultValueExpression
    {:optional true,
     :getter-doc "Return the default value of the field.",
     :setter-doc
     "DefaultValueExpression is used to specify the default value of a field using a SQL\nexpression. It can only be set for top level fields (columns).\n\n<p>You can use struct or array expression to specify default value for the entire struct or\narray. The valid SQL expressions are:\n\n<pre>\n  Literals for all data types, including STRUCT and ARRAY.\n  The following functions:\n     - CURRENT_TIMESTAMP\n     - CURRENT_TIME\n     - CURRENT_DATE\n     - CURRENT_DATETIME\n     - GENERATE_UUID\n     - RAND\n     - SESSION_USER\n     - ST_GEOGPOINT\n\n  Struct or array composed with the above allowed functions, for example:\n     \"[CURRENT_DATE(), DATE '2020-01-01']\"\n</pre>"}
    :string]
   [:description
    {:optional true,
     :getter-doc "Returns the field description.",
     :setter-doc
     "Sets the field description. The maximum length is 16K characters."}
    :string]
   [:maxLength
    {:optional true,
     :getter-doc
     "Returns the maximum length of the field for STRING or BYTES type.",
     :setter-doc
     "Sets the maximum length of the field for STRING or BYTES type.\n\n<p>It is invalid to set value for types other than STRING or BYTES.\n\n<p>For STRING type, this represents the maximum UTF-8 length of strings allowed in the field.\nFor BYTES type, this represents the maximum number of bytes in the field."}
    :int]
   [:mode
    {:optional true,
     :doc "Mode for a BigQuery Table field. {@link Mode#NULLABLE} fields can be set to {@code null},\n{@link Mode#REQUIRED} fields must be provided. {@link Mode#REPEATED} fields can contain more\nthan one value.",
     :getter-doc "Returns the field mode. By default {@link Mode#NULLABLE} is used.",
     :setter-doc "Sets the mode of the field. When not specified {@link Mode#NULLABLE} is used."}
    [:enum "NULLABLE" "REQUIRED" "REPEATED"]]
   [:policyTags
    {:optional true,
     :getter-doc "Returns the policy tags for the field.",
     :setter-doc "Sets the policy tags for the field."}
    :gcp.bigquery/PolicyTags]
   [:precision
    {:optional true,
     :getter-doc
     "Returns the maximum number of total digits allowed for NUMERIC or BIGNUMERIC types.",
     :setter-doc
     "Precision can be used to constrain the maximum number of total digits allowed for NUMERIC or BIGNUMERIC types. It is invalid to set values for Precision for types other than // NUMERIC\nor BIGNUMERIC. For NUMERIC type, acceptable values for Precision must be: 1 ≤ (Precision -\nScale) ≤ 29. Values for Scale must be: 0 ≤ Scale ≤ 9. For BIGNUMERIC type, acceptable values\nfor Precision must be: 1 ≤ (Precision - Scale) ≤ 38. Values for Scale must be: 0 ≤ Scale ≤\n38."}
    :int]
   [:rangeElementType
    {:optional true,
     :getter-doc "Return the range element type the field.",
     :setter-doc
     "Optional. Field range element type can be set only when the type of field is RANGE."}
    [:ref :gcp.bigquery/FieldElementType]]
   [:scale
    {:optional true,
     :getter-doc
     "Returns the maximum number of digits set in the fractional part of a NUMERIC or BIGNUMERIC type.",
     :setter-doc
     "Scale can be used to constrain the maximum number of digits in the fractional part of a\nNUMERIC or BIGNUMERIC type. If the Scale value is set, the Precision value must be set as\nwell. It is invalid to set values for Scale for types other than NUMERIC or BIGNUMERIC. See\nthe Precision field for additional guidance about valid values."}
    :int]
   [:timestampPrecision
    {:optional true,
     :getter-doc "Returns the precision for TIMESTAMP type.",
     :setter-doc
     "Specifies the precision for TIMESTAMP types. The default value is 6. Possible values are 6 (microsecond) or 12 (picosecond)."}
    [:enum 6 12]]
   ;;----
   [:name {:getter-doc "Returns the field name."}
    [:string {:min 1}]]])

(def _Field-record-schema
  (conj _Field-fields
        [:type [:enum "RECORD" "STRUCT"]]
        [:subFields
         {:getter-doc "Returns the list of sub-fields if {@link #getType()} is a {@link LegacySQLTypeName#RECORD}.\nReturns {@code null} otherwise."}
         [:sequential {:min 1} [:ref :gcp.bigquery/Field]]]))

(def _Field-other-schema
  (conj _Field-fields
        [:type
         {:getter-doc "Returns the field type.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/reference/standard-sql/data-types\">Data\n    Types</a>"}
         [:enum "BYTES" "STRING" "INTEGER" "FLOAT"
          "NUMERIC" "BIGNUMERIC" "BOOLEAN" "TIMESTAMP"
          "DATE" "GEOGRAPHY" "TIME" "DATETIME" "JSON"
          "INTERVAL" "RANGE"
          "BOOL" "INT64" "FLOAT64" #_ "ARRAY"]]))

(def Field-schema
  [:or
   {:doc "Google BigQuery Table schema field. A table field has a name, a type, a mode and possibly a description."
    :fqcn         "com.google.cloud.bigquery.Field"
    :gcp/key :gcp.bigquery/Field}
   _Field-record-schema
   _Field-other-schema])

(defn ^Field Field-from-edn
  [{type :type
    subFields :subFields
    :as arg}]
  (when (= "ARRAY" type)
    (throw (ex-info (str "ARRAY types should be represented with :mode \"REPEATED\" instead") {:arg arg})))
  (assert (some? (:name arg)))
  (when (seq subFields) (assert (#{"RECORD" "STRUCT"} type)))
  (when (#{"RECORD" "STRUCT"} type) (assert (seq subFields)))
  (g/strict! :gcp.bigquery/Field arg)
  (let [builder (if (g/valid? legacy-sql-schema type)
                  (Field/newBuilder ^String (get arg :name)
                                    (LegacySQLTypeName/valueOf type)
                                    ^Field/1 (into-array Field (map Field-from-edn subFields)))
                  (Field/newBuilder ^String (get arg :name)
                                    (StandardSQLTypeName/valueOf type)
                                    ^Field/1 (into-array Field (map Field-from-edn subFields))))]
    (when (get arg :collation)
      (.setCollation builder (get arg :collation)))
    (when (get arg :defaultValueExpression)
      (.setDefaultValueExpression builder (get arg :defaultValueExpression)))
    (when (get arg :description)
      (.setDescription builder (get arg :description)))
    (when (get arg :maxLength)
      (.setMaxLength builder (get arg :maxLength)))
    (when (get arg :mode)
      (.setMode builder (Field$Mode/valueOf (get arg :mode))))
    (when (get arg :policyTags)
      (.setPolicyTags builder (PolicyTags/from-edn (get arg :policyTags))))
    (when (get arg :precision)
      (.setPrecision builder (get arg :precision)))
    (when (get arg :rangeElementType)
      (.setRangeElementType builder (FieldElementType-from-edn (get arg :rangeElementType))))
    (when (get arg :scale)
      (.setScale builder (get arg :scale)))
    (when (get arg :timestampPrecision)
      (.setTimestampPrecision builder (get arg :timestampPrecision)))
    (.build builder)))

(defn Field-to-edn
  [^Field arg]
  {:post [(g/strict! :gcp.bigquery/Field %)]}
  (cond-> {:name (.getName arg),
           :type (.name (.getType arg))}
          (seq (.getSubFields arg))        (assoc :subFields (map Field-to-edn (.getSubFields arg)))
          (.getCollation arg)              (assoc :collation (.getCollation arg))
          (.getDefaultValueExpression arg) (assoc :defaultValueExpression (.getDefaultValueExpression arg))
          (.getDescription arg)            (assoc :description (.getDescription arg))
          (.getMaxLength arg)              (assoc :maxLength (.getMaxLength arg))
          (.getMode arg)                   (assoc :mode (.name (.getMode arg)))
          (.getPolicyTags arg)             (assoc :policyTags (PolicyTags/to-edn (.getPolicyTags arg)))
          (.getPrecision arg)              (assoc :precision (.getPrecision arg))
          (.getRangeElementType arg)       (assoc :rangeElementType (FieldElementType-to-edn (.getRangeElementType arg)))
          (.getScale arg)                  (assoc :scale (.getScale arg))
          (.getTimestampPrecision arg)     (assoc :timestampPrecision (.getTimestampPrecision arg))))

#!------------------------------------------------
#! FieldValue

(declare Range-to-edn)

(defn FieldValue-to-edn
  [{field-type :type
    sub-fields :subFields
    :as field} ^FieldValue field-value]
  (when-not (.isNull field-value)
    (case (.name (.getAttribute field-value))
      "RECORD" (let [fvl (.getRecordValue field-value)]
                 (if (.hasSchema fvl)
                   (into {}
                         (map
                           (fn [[sf fv]]
                             [(keyword (:name sf)) (FieldValue-to-edn sf fv)]))
                         (map vector sub-fields fvl))
                   fvl))
      "RANGE" (Range-to-edn (.getRangeValue field-value))
      "REPEATED" (mapv (partial FieldValue-to-edn field) (.getRepeatedValue field-value))
      "PRIMITIVE"
      (case field-type
        "BOOLEAN"   (.getBooleanValue field-value)
        "STRING"    (.getStringValue field-value)
        "INTEGER"   (.getLongValue field-value)
        "FLOAT"     (.getDoubleValue field-value)
        "NUMERIC"   (.getNumericValue field-value)
        "TIMESTAMP" (String->Instant (.getValue field-value))
        "DATE"      (String->Date (.getValue field-value))
        "DATETIME"  (String->DateTime (.getValue field-value))
        "JSON"      (j/read-value (.getValue field-value) j/keyword-keys-object-mapper)
        "BYTES"     (.getBytesValue field-value)
        "GEOGRAPHY" (.getStringValue field-value)
        "INTERVAL"  (.getPeriodDuration field-value) ;org.threeten.extra.PeriodDuration
        (throw (ex-info (str "unimplemented PRIMITIVE '" field-type "'") {:field-value field-value
                                                                          :field-type  field-type})))
      (throw (ex-info (str "unimplemented FieldValue attribute: '" (.name (.getAttribute field-value)) "'")
                      {:field field :fieldValue field-value})))))

#!------------------------------------------------
#! Range

(def ^:private one-of-start-or-end
  [:fn
   {:error/message "one of :start or :end must be set"}
   '(fn [v]
      (or (contains? v :start)
          (contains? v :end)))])

(def Range-schema
  [:or
   {:gcp/key :gcp.bigquery/Range
    :fqcn "com.google.cloud.bigquery.Range"
    :closed true}
   [:and
    [:map {:closed true}
     [:start {:optional true} :Date]
     [:end {:optional true} :Date]
     [:type [:= "DATE"]]]
    one-of-start-or-end]
   [:and
    [:map {:closed true}
     [:start {:optional true} :DateTime]
     [:end {:optional true} :DateTime]
     [:type [:= "DATETIME"]]]
    one-of-start-or-end]
   [:and
    [:map {:closed true}
     [:start {:optional true} :Timestamp]
     [:end {:optional true} :Timestamp]
     [:type [:= "TIMESTAMP"]]]
    one-of-start-or-end]])

(defn ^Range Range-from-edn
  [{:keys [end start type] :as arg}]
  (g/strict! :gcp.bigquery/Range arg)
  (let [builder (Range/newBuilder)
        xf (case type
             "DATE" str
             "DATETIME" DateTime->String
             "TIMESTAMP" Instant->String)]
    (.setType builder (FieldElementType-from-edn type))
    (some->> end xf (.setEnd builder))
    (some->> start xf (.setStart builder))
    (.build builder)))

(defn Range-to-edn [^Range arg]
  {:post [(g/strict! :gcp.bigquery/Range %)]}
  (let [T (.getType (.getType arg))
        end (.getValue (.getEnd arg))
        start (.getValue (.getStart arg))
        xf (case T
             "TIMESTAMP" String->Instant
             "DATETIME" String->DateTime
             "DATE" String->Date)]
    (cond-> {:type T}
      end (assoc :end (xf end))
      start (assoc :start (xf start)))))

#!------------------------------------------------
#! TableResult

(defn TableResult-to-edn [^TableResult res]
  (if (zero? (.getTotalRows res))
    []
    (let [schema (.getSchema res)]
      (if (nil? schema)
        res
        (let [column-fields (mapv Field-to-edn (.getFields schema))
              ;; TODO some queries return singular fields with anonymous names
              ;; ... could drop the name and just return parsed value
              row->map (fn [row-value-list]
                         (into {}
                               (map
                                 (fn [[field field-value]]
                                   [(keyword (:name field)) (FieldValue-to-edn field field-value)]))
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

#!------------------------------------------------
#! QueryParameterValue

(defn- bignumeric?
  [^BigDecimal bd]
  (let [p (.precision bd)
        s (.scale bd)]
    (and (<= p 76)
         (<= s 38)
         (or (> p 38)
             (> s 9)))))

(def geography-schema
  [:map {:closed true}
   [:geography [:string {:min 1}]]])

(def json-schema
  [:map {:closed true}
   [:json [:string {:min 1}]]])

(def ^:private scalar-schema
  [:or
   :string
   :boolean
   :int
   :float
   :bigdec
   :Timestamp
   :Time
   :Date
   :DateTime
   :PeriodDuration
   [:ref :gcp.bigquery/Range]
   geography-schema
   json-schema])

;; array<JSON> not allowed
(def ^:private homogenous-sequence-schemas
  [:or
   [:sequential {:min 1} :string]
   [:sequential {:min 1} :boolean]
   [:sequential {:min 1} :int]
   [:sequential {:min 1} :float]
   [:sequential {:min 1} :bigdec]
   [:sequential {:min 1} :Timestamp]
   [:sequential {:min 1} :Time]
   [:sequential {:min 1} :Date]
   [:sequential {:min 1} :DateTime]
   [:sequential {:min 1} :PeriodDuration]
   [:sequential {:min 1} [:ref :gcp.bigquery/Range]]
   [:sequential {:min 1} geography-schema]])

(def QueryParameterValue-Struct-schema
  [:map-of [:or 'simple-keyword? :string]  [:ref :gcp.bigquery/QueryParameterValue.SCALAR]])

(def ^:private QueryParameterValue-schema
  [:or
   'bytes?
   [:ref :gcp.bigquery/QueryParameterValue.SCALAR]
   homogenous-sequence-schemas
   QueryParameterValue-Struct-schema
   [:sequential {:min 1} QueryParameterValue-Struct-schema]])

(defn ^QueryParameterValue QueryParameterValue-from-edn [arg]
  (g/strict! :gcp.bigquery/QueryParameterValue arg)
  (cond
    (nil? arg) (throw (ex-info "nil value not directly supported in QPV without type hint" {:arg arg}))
    (string? arg) (QueryParameterValue/string arg)
    (boolean? arg) (QueryParameterValue/bool arg)
    (int? arg) (QueryParameterValue/int64 (Long/valueOf (long arg)))
    (float? arg) (QueryParameterValue/float64 (Double/valueOf (double arg)))
    (decimal? arg) (if (bignumeric? arg)
                     (QueryParameterValue/bigNumeric arg)
                     (QueryParameterValue/numeric arg))
    (bytes? arg) (QueryParameterValue/bytes arg)
    #!--------------------------------------------------------------
    (inst? arg) (QueryParameterValue/timestamp ^String (Instant->String arg))
    (instance? LocalTime arg) (QueryParameterValue/time (Time->String arg))
    (instance? LocalDate arg) (QueryParameterValue/date (.toString ^LocalDate arg))
    (instance? LocalDateTime arg) (QueryParameterValue/dateTime (DateTime->String arg))
    #!--------------------------------------------------------------
    (instance? PeriodDuration arg) (QueryParameterValue/interval ^PeriodDuration arg)
    #!--------------------------------------------------------------
    (instance? JsonObject arg) (QueryParameterValue/json ^JsonObject arg)
    #!--------------------------------------------------------------
    (map? arg)
    (cond
      (g/valid? :gcp.bigquery/Range arg)    (QueryParameterValue/range (Range-from-edn arg))
      (contains? arg :interval) (QueryParameterValue/interval ^String (:interval arg))
      (contains? arg :geography) (QueryParameterValue/geography (:geography arg))
      (contains? arg :json) (QueryParameterValue/json ^String (:json arg))
      :else (QueryParameterValue/struct (into {} (map (fn [[k v]] [(name k) (QueryParameterValue-from-edn v)])) arg)) )

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

      (g/valid? [:sequential :bigdec] arg)
      (QueryParameterValue/array ^BigDecimal/1 (into-array BigDecimal arg) BigDecimal)

      (g/valid? [:sequential :Time] arg)
      (let [array ^String/1 (into-array String (map Time->String arg))
            T StandardSQLTypeName/TIME]
        (QueryParameterValue/array array T))

      (g/valid? [:sequential :Date] arg)
      (let [array ^String/1 (into-array String (map Date->String arg))
            T StandardSQLTypeName/DATE]
        (QueryParameterValue/array array T))

      (g/valid? [:sequential :DateTime] arg)
      (let [array ^String/1 (into-array String (map DateTime->String arg))
            T StandardSQLTypeName/DATETIME]
        (QueryParameterValue/array array T))

      (g/valid? [:sequential :PeriodDuration] arg)
      (let [array ^String/1 (into-array PeriodDuration arg)
            T StandardSQLTypeName/INTERVAL]
        (QueryParameterValue/array array T))

      (g/valid? [:sequential :inst] arg)
      (let [array ^String/1 (into-array String (map Instant->String arg))
            T StandardSQLTypeName/TIMESTAMP]
        (QueryParameterValue/array array T))

      ;(g/valid? [:sequential json-schema] arg)
      ;(let [array ^String/1 (into-array Object (map QueryParameterValue-from-edn arg))
      ;      T StandardSQLTypeName/JSON]
      ;  (QueryParameterValue/array array T))

      (g/valid? [:sequential geography-schema] arg)
      (let [array ^String/1 (into-array Object (map QueryParameterValue-from-edn arg))
            T StandardSQLTypeName/GEOGRAPHY]
        (QueryParameterValue/array array T))

      (g/valid? [:sequential map?] arg)
      (let [array ^Object/1 (into-array Object (map QueryParameterValue-from-edn arg))
            T StandardSQLTypeName/STRUCT]
        (QueryParameterValue/array array T))

      true
      (throw (ex-info "unimplemented sequential type" {:arg arg})))

    :else
    (throw (IllegalArgumentException. (str "Unknown type for value: " arg)))))

(defn- parse-interval [^String s]
  (try
    (PeriodDuration/parse s)
    (catch Exception _
      {:interval s})))

(defn QueryParameterValue-to-edn [^QueryParameterValue arg]
  {:post [(g/valid? :gcp.bigquery/QueryParameterValue %)]}
  (cond
    (some? (.getArrayValues arg)) ;; List<QueryParameterValue>
    (mapv QueryParameterValue-to-edn (.getArrayValues arg)) ;; TODO homogenous collections can return use arrays here
    (some? (.getStructValues arg))
    (into {}                                                ; Map<String, QueryParameterValue>
          (map (fn [[field-name field-val]]
                 [(keyword field-name) (QueryParameterValue-to-edn field-val)]))
          (.getStructValues arg))
    :else
    (case (.name (.getType arg))
      "DATE"       (String->Date (.getValue arg))
      "DATETIME"   (String->DateTime (.getValue arg))
      "TIME"       (String->Time (.getValue arg))
      "TIMESTAMP"  (String->Instant (.getValue arg))
      "NUMERIC"    (bigdec ^String (.getValue arg))
      "BIGNUMERIC" (bigdec ^String (.getValue arg))
      "GEOGRAPHY"  {:geography (.getValue arg)}
      "BOOL"       (Boolean/parseBoolean (.getValue arg))
      "STRING"     (.getValue arg)
      "INT64"      (Long/parseLong (.getValue arg))
      "FLOAT64"    (Double/parseDouble (.getValue arg))
      "INTERVAL"   (parse-interval (.getValue arg))
      "RANGE"      (Range-to-edn (.getRangeValues arg))
      "JSON"       {:json (.getValue arg)}
      "BYTES"      (.getValue arg)
      (throw (ex-info "UNKNOWN TYPE" {:arg arg})))))

#!----------------------------------------------------------------------------------------------------------------------

(def InsertAllRequest$RowValue-schema
  [:or
   scalar-schema
   'bytes?
   homogenous-sequence-schemas
   [:sequential {:min 1} [:ref :gcp.bigquery/InsertAllRequest$RowToInsert]]
   [:ref :gcp.bigquery/InsertAllRequest$RowToInsert]])

(def InsertAllRequest$RowToInsert-schema
  [:map-of
   [:or :string 'simple-keyword?]
   [:ref :gcp.bigquery/InsertAllRequest$RowValue]])

(defn serialize-scalar
  [val]
  (cond
    (string? val)  val
    (boolean? val) val
    (number? val)  (str val)
    (inst? val)    (Instant->String val)
    (instance? LocalTime val)     (Time->String val)
    (instance? LocalDate val)     (Date->String val)
    (instance? LocalDateTime val) (DateTime->String val)
    (instance? PeriodDuration val) (.toString ^PeriodDuration val)
    (map? val)
    (cond
      (contains? val :geography) (:geography val)
      (contains? val :json)      (:json val)
      (g/valid? :gcp.bigquery/Range val)     (let [{:keys [end start type]} val
                                                   xf (case type
                                                        "DATE" str
                                                        "DATETIME" DateTime->String
                                                        "TIMESTAMP" Instant->String)]
                                               (cond-> {}
                                                       start (assoc "start" (xf start))
                                                       end (assoc "end" (xf end))))
      :else val)
    :else val))

(defn serialize-value [val]
  (cond
    (nil? val) nil
    (bytes? val) (.encodeToString (Base64/getEncoder) val)

    (g/valid? scalar-schema val)
    (serialize-scalar val)

    (instance? JsonObject val)
    val

    (map? val)
    (into {} (map (fn [[k v]] [(name k) (serialize-value v)])) val)

    (or (sequential? val)
        (and (some? val) (.isArray (class val))))
    (mapv serialize-value val)

    :else val))

(defn ^HashMap serialize-row [row]
  (into {}
        (map
          (fn [[k v]]
            [k (serialize-value v)]))
        (walk/stringify-keys row)))

(def InsertAllRequest-schema
  [:map
   [:table :gcp.bigquery/TableId]
   [:rows [:sequential {:min 1} [:ref :gcp.bigquery/InsertAllRequest$RowToInsert]]]
   [:ignoreUnknownValues {:optional true} :boolean]
   [:skipInvalidRows {:optional true} :boolean]
   [:templateSuffix {:optional true} :string]])

(defn InsertAllRequest-from-edn
  [{:keys [table
           ignoreUnknownValues
           skipInvalidRows
           templateSuffix
           rows] :as arg}]
  (g/strict! :gcp.bigquery/InsertAllRequest arg)
  (let [builder (InsertAllRequest/newBuilder (TableId/from-edn table))]
    (doseq [row rows]
      (.addRow builder (serialize-row row)))
    (when skipInvalidRows
      (.setSkipInvalidRows builder skipInvalidRows))
    (when templateSuffix
      (.setTemplateSuffix builder templateSuffix))
    (when ignoreUnknownValues
      (.setIgnoreUnknownValues builder ignoreUnknownValues))
    (.build builder)))

#!----------------------------------------------------------------------------------------------------------------------

(g/include-schema-registry!
  (with-meta {:gcp.bigquery/Field                        Field-schema
              :gcp.bigquery/FieldElementType             FieldElementType-schema
              :gcp.bigquery/Range                        Range-schema
              :gcp.bigquery/QueryParameterValue.SCALAR   scalar-schema
              :gcp.bigquery/QueryParameterValue          QueryParameterValue-schema
              :gcp.bigquery/InsertAllRequest$RowValue    InsertAllRequest$RowValue-schema
              :gcp.bigquery/InsertAllRequest$RowToInsert InsertAllRequest$RowToInsert-schema
              :gcp.bigquery/InsertAllRequest             InsertAllRequest-schema}
             {::g/name "gcp.bigquery.custom"}))