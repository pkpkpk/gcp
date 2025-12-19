(ns gcp.bigquery.v2.CsvOptions
  (:import [com.google.cloud.bigquery CsvOptions])
  (:require [gcp.global :as global]))

(defn ^CsvOptions from-edn
  [arg]
  (global/strict! :gcp.bigquery.v2/CsvOptions arg)
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
  {:post [(global/strict! :gcp.bigquery.v2/CsvOptions %)]}
  (cond-> {}
    (get arg :allowJaggedRows) (assoc :allowJaggedRows (.allowJaggedRows arg))
    (get arg :allowQuotedNewLines) (assoc :allowQuotedNewLines
                                     (.allowQuotedNewLines arg))
    (get arg :encoding) (assoc :encoding (.getEncoding arg))
    (get arg :fieldDelimiter) (assoc :fieldDelimiter (.getFieldDelimiter arg))
    (get arg :nullMarker) (assoc :nullMarker (.getNullMarker arg))
    (get arg :preserveAsciiControlCharacters)
      (assoc :preserveAsciiControlCharacters
        (.getPreserveAsciiControlCharacters arg))
    (get arg :quote) (assoc :quote (.getQuote arg))
    (get arg :skipLeadingRows) (assoc :skipLeadingRows
                                 (.getSkipLeadingRows arg))))

(def schemas
  {:gcp.bigquery.v2/CsvOptions
   [:map {:closed true}
    [:type {:optional true} [:= "CSV"]]
    [:allowJaggedRows {:optional true} :boolean]
    [:allowQuotedNewLines {:optional true} :boolean]
    [:encoding {:optional true} :string]
    [:fieldDelimiter {:optional true} :string]
    [:nullMarker {:optional true} :string]
    [:preserveAsciiControlCharacters {:optional true} :boolean]
    [:quote {:optional true} :string]
    [:skipLeadingRows {:optional true} :int]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))