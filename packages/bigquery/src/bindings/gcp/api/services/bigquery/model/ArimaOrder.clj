;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.ArimaOrder
  {:doc
     "Arima order, can be used for both non-seasonal and seasonal parts.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.ArimaOrder"
   :gcp.dev/certification
     {:base-seed 1775130877724
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1775130877724 :standard 1775130877725 :stress 1775130877726}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:54:38.954373306Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model ArimaOrder]))

(declare from-edn to-edn)

(defn ^ArimaOrder from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/ArimaOrder arg)
  (let [o (new ArimaOrder)]
    (when (some? (get arg :d)) (.setD o (long (get arg :d))))
    (when (some? (get arg :p)) (.setP o (long (get arg :p))))
    (when (some? (get arg :q)) (.setQ o (long (get arg :q))))
    o))

(defn to-edn
  [^ArimaOrder arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/ArimaOrder %)]}
  (when arg
    (cond-> {}
      (.getD arg) (assoc :d (.getD arg))
      (.getP arg) (assoc :p (.getP arg))
      (.getQ arg) (assoc :q (.getQ arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Arima order, can be used for both non-seasonal and seasonal parts.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/ArimaOrder}
   [:d
    {:getter-doc
       "Order of the differencing part.\n\n@return value or {@code null} for none",
     :setter-doc
       "Order of the differencing part.\n\n@param d d or {@code null} for none",
     :optional true} :i64]
   [:p
    {:getter-doc
       "Order of the autoregressive part.\n\n@return value or {@code null} for none",
     :setter-doc
       "Order of the autoregressive part.\n\n@param p p or {@code null} for none",
     :optional true} :i64]
   [:q
    {:getter-doc
       "Order of the moving-average part.\n\n@return value or {@code null} for none",
     :setter-doc
       "Order of the moving-average part.\n\n@param q q or {@code null} for none",
     :optional true} :i64]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/ArimaOrder schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.ArimaOrder"}))