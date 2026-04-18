;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.CsvOptions
  {:doc
     "Google BigQuery options for CSV format. This class wraps some properties of CSV files used by\nBigQuery to parse external data."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.CsvOptions"
   :gcp.dev/certification
     {:base-seed 1776499333201
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499333201 :standard 1776499333202 :stress 1776499333203}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:02:14.819284993Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery CsvOptions CsvOptions$Builder]))

(declare from-edn to-edn)

(defn ^CsvOptions from-edn
  [arg]
  (global/strict! :gcp.bigquery/CsvOptions arg)
  (let [builder (CsvOptions/newBuilder)]
    (when (some? (get arg :allowJaggedRows))
      (.setAllowJaggedRows builder (get arg :allowJaggedRows)))
    (when (some? (get arg :allowQuotedNewLines))
      (.setAllowQuotedNewLines builder (get arg :allowQuotedNewLines)))
    (when (some? (get arg :encoding))
      (.setEncoding builder (get arg :encoding)))
    (when (some? (get arg :fieldDelimiter))
      (.setFieldDelimiter builder (get arg :fieldDelimiter)))
    (when (some? (get arg :nullMarker))
      (.setNullMarker builder (get arg :nullMarker)))
    (when (some? (get arg :preserveAsciiControlCharacters))
      (.setPreserveAsciiControlCharacters
        builder
        (get arg :preserveAsciiControlCharacters)))
    (when (some? (get arg :quote)) (.setQuote builder (get arg :quote)))
    (when (some? (get arg :skipLeadingRows))
      (.setSkipLeadingRows builder (long (get arg :skipLeadingRows))))
    (.build builder)))

(defn to-edn
  [^CsvOptions arg]
  {:post [(global/strict! :gcp.bigquery/CsvOptions %)]}
  (when arg
    (cond-> {:type "CSV"}
      (.allowJaggedRows arg) (assoc :allowJaggedRows (.allowJaggedRows arg))
      (.allowQuotedNewLines arg) (assoc :allowQuotedNewLines
                                   (.allowQuotedNewLines arg))
      (some->> (.getEncoding arg)
               (not= ""))
        (assoc :encoding (.getEncoding arg))
      (some->> (.getFieldDelimiter arg)
               (not= ""))
        (assoc :fieldDelimiter (.getFieldDelimiter arg))
      (some->> (.getNullMarker arg)
               (not= ""))
        (assoc :nullMarker (.getNullMarker arg))
      (.getPreserveAsciiControlCharacters arg)
        (assoc :preserveAsciiControlCharacters
          (.getPreserveAsciiControlCharacters arg))
      (some->> (.getQuote arg)
               (not= ""))
        (assoc :quote (.getQuote arg))
      (.getSkipLeadingRows arg) (assoc :skipLeadingRows
                                  (.getSkipLeadingRows arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery options for CSV format. This class wraps some properties of CSV files used by\nBigQuery to parse external data.",
    :gcp/category :variant-accessor,
    :gcp/key :gcp.bigquery/CsvOptions} [:type [:= "CSV"]]
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
    [:string {:min 1}]]
   [:fieldDelimiter
    {:optional true,
     :getter-doc "Returns the separator for fields in a CSV file.",
     :setter-doc
       "Sets the separator for fields in a CSV file. BigQuery converts the string to ISO-8859-1\nencoding, and then uses the first byte of the encoded string to split the data in its raw,\nbinary state. BigQuery also supports the escape sequence \"\\t\" to specify a tab separator. The\ndefault value is a comma (',')."}
    [:string {:min 1}]]
   [:nullMarker
    {:optional true,
     :getter-doc
       "Returns the string that represents a null value in a CSV file.",
     :setter-doc
       "[Optional] Specifies a string that represents a null value in a CSV file. For example, if you\nspecify \\\"\\\\N\\\", BigQuery interprets \\\"\\\\N\\\" as a null value when querying a CSV file. The\ndefault value is the empty string. If you set this property to a custom value, BigQuery\nthrows an error if an empty string is present for all data types except for STRING and BYTE.\nFor STRING and BYTE columns, BigQuery interprets the empty string as an empty value."}
    [:string {:min 1}]]
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
    [:string {:min 1}]]
   [:skipLeadingRows
    {:optional true,
     :getter-doc
       "Returns the number of rows at the top of a CSV file that BigQuery will skip when reading the\ndata.",
     :setter-doc
       "Sets the number of rows at the top of a CSV file that BigQuery will skip when reading the\ndata. The default value is 0. This property is useful if you have header rows in the file\nthat should be skipped."}
    :i64]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/CsvOptions schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.CsvOptions"}))