;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.CsvOptions
  {:doc
     "Google BigQuery options for CSV format. This class wraps some properties of CSV files used by\nBigQuery to parse external data."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.CsvOptions"
   :gcp.dev/certification
     {:base-seed 1771010250481
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771010250481 :standard 1771010250482 :stress 1771010250483}
      :protocol-hash
        "600a262ece6bd21dc98250ea6f25d9fa1a7ab0d8840c5d6ce9608615488fe05f"
      :timestamp "2026-02-13T19:17:30.501364632Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery CsvOptions CsvOptions$Builder]))

(defn ^CsvOptions from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/CsvOptions arg)
  (let [builder (CsvOptions/newBuilder)]
    (when (get arg :allowJaggedRows)
      (.setAllowJaggedRows builder (get arg :allowJaggedRows)))
    (when (get arg :allowQuotedNewLines)
      (.setAllowQuotedNewLines builder (get arg :allowQuotedNewLines)))
    (when (get arg :encoding) (.setEncoding builder (get arg :encoding)))
    (when (get arg :fieldDelimiter)
      (.setFieldDelimiter builder (get arg :fieldDelimiter)))
    (when (get arg :nullMarker) (.setNullMarker builder (get arg :nullMarker)))
    (when (get arg :preserveAsciiControlCharacters)
      (.setPreserveAsciiControlCharacters
        builder
        (get arg :preserveAsciiControlCharacters)))
    (when (get arg :quote) (.setQuote builder (get arg :quote)))
    (when (get arg :skipLeadingRows)
      (.setSkipLeadingRows builder (get arg :skipLeadingRows)))
    (.build builder)))

(defn to-edn
  [^CsvOptions arg]
  {:post [(global/strict! :gcp.bindings.bigquery/CsvOptions %)]}
  (cond-> {:type (.getType arg)}
    (.allowJaggedRows arg) (assoc :allowJaggedRows (.allowJaggedRows arg))
    (.allowQuotedNewLines arg) (assoc :allowQuotedNewLines
                                 (.allowQuotedNewLines arg))
    (.getEncoding arg) (assoc :encoding (.getEncoding arg))
    (.getFieldDelimiter arg) (assoc :fieldDelimiter (.getFieldDelimiter arg))
    (.getNullMarker arg) (assoc :nullMarker (.getNullMarker arg))
    (.getPreserveAsciiControlCharacters arg)
      (assoc :preserveAsciiControlCharacters
        (.getPreserveAsciiControlCharacters arg))
    (.getQuote arg) (assoc :quote (.getQuote arg))
    (.getSkipLeadingRows arg) (assoc :skipLeadingRows
                                (.getSkipLeadingRows arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery options for CSV format. This class wraps some properties of CSV files used by\nBigQuery to parse external data.",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bindings.bigquery/CsvOptions}
   [:allowJaggedRows
    {:optional true,
     :getter-doc
       "Returns whether BigQuery should accept rows that are missing trailing optional columns. If\n{@code true}, BigQuery treats missing trailing columns as null values. If {@code false},\nrecords with missing trailing columns are treated as bad records, and if the number of bad\nrecords exceeds {@link ExternalTableDefinition#getMaxBadRecords()}, an invalid error is\nreturned in the job result.",
     :setter-doc
       "Set whether BigQuery should accept rows that are missing trailing optional columns. If {@code\ntrue}, BigQuery treats missing trailing columns as null values. If {@code false}, records\nwith missing trailing columns are treated as bad records, and if there are too many bad\nrecords, an invalid error is returned in the job result. By default, rows with missing\ntrailing columns are considered bad records."}
    :boolean]
   [:allowQuotedNewLines
    {:optional true,
     :getter-doc
       "Returns whether BigQuery should allow quoted data sections that contain newline characters in a\nCSV file.",
     :setter-doc
       "Sets whether BigQuery should allow quoted data sections that contain newline characters in a\nCSV file. By default quoted newline are not allowed."}
    :boolean]
   [:encoding
    {:optional true,
     :getter-doc
       "Returns the character encoding of the data. The supported values are UTF-8 or ISO-8859-1. If\nnot set, UTF-8 is used. BigQuery decodes the data after the raw, binary data has been split\nusing the values set in {@link #getQuote()} and {@link #getFieldDelimiter()}.",
     :setter-doc
       "Sets the character encoding of the data. The supported values are UTF-8 or ISO-8859-1. The\ndefault value is UTF-8. BigQuery decodes the data after the raw, binary data has been split\nusing the values set in {@link #setQuote(String)} and {@link #setFieldDelimiter(String)}."}
    :string]
   [:fieldDelimiter
    {:optional true,
     :getter-doc "Returns the separator for fields in a CSV file.",
     :setter-doc
       "Sets the separator for fields in a CSV file. BigQuery converts the string to ISO-8859-1\nencoding, and then uses the first byte of the encoded string to split the data in its raw,\nbinary state. BigQuery also supports the escape sequence \"\\t\" to specify a tab separator. The\ndefault value is a comma (',')."}
    :string]
   [:nullMarker
    {:optional true,
     :getter-doc
       "Returns the string that represents a null value in a CSV file.",
     :setter-doc
       "[Optional] Specifies a string that represents a null value in a CSV file. For example, if you\nspecify \\\"\\\\N\\\", BigQuery interprets \\\"\\\\N\\\" as a null value when querying a CSV file. The\ndefault value is the empty string. If you set this property to a custom value, BigQuery\nthrows an error if an empty string is present for all data types except for STRING and BYTE.\nFor STRING and BYTE columns, BigQuery interprets the empty string as an empty value."}
    :string]
   [:preserveAsciiControlCharacters
    {:optional true,
     :getter-doc
       "Returns whether BigQuery should allow ascii control characters in a CSV file. By default ascii\ncontrol characters are not allowed.",
     :setter-doc
       "Sets whether BigQuery should allow ASCII control characters in a CSV file. By default ASCII\ncontrol characters are not allowed."}
    :boolean]
   [:quote
    {:optional true,
     :getter-doc
       "Returns the value that is used to quote data sections in a CSV file.",
     :setter-doc
       "Sets the value that is used to quote data sections in a CSV file. BigQuery converts the\nstring to ISO-8859-1 encoding, and then uses the first byte of the encoded string to split\nthe data in its raw, binary state. The default value is a double-quote ('\"'). If your data\ndoes not contain quoted sections, set the property value to an empty string. If your data\ncontains quoted newline characters, you must also set {@link\n#setAllowQuotedNewLines(boolean)} property to {@code true}."}
    :string]
   [:skipLeadingRows
    {:optional true,
     :getter-doc
       "Returns the number of rows at the top of a CSV file that BigQuery will skip when reading the\ndata.",
     :setter-doc
       "Sets the number of rows at the top of a CSV file that BigQuery will skip when reading the\ndata. The default value is 0. This property is useful if you have header rows in the file\nthat should be skipped."}
    :int]
   [:type
    {:read-only? true,
     :getter-doc "Returns the external data format, as a string."} [:= "CSV"]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/CsvOptions schema}
    {:gcp.global/name "gcp.bindings.bigquery.CsvOptions"}))