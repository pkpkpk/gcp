;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.QueryParameter
  {:doc
     "A parameter given to a query.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.QueryParameter"
   :gcp.dev/certification
     {:base-seed 1776499427181
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1776499427181 :standard 1776499427182 :stress 1776499427183}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:03:48.776050039Z"}}
  (:require [gcp.api.services.bigquery.custom :as custom]
            [gcp.api.services.bigquery.model.QueryParameterType :as
             QueryParameterType]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model QueryParameter]))

(declare from-edn to-edn)

(defn ^QueryParameter from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/QueryParameter arg)
  (let [o (new QueryParameter)]
    (when (some? (get arg :name)) (.setName o (get arg :name)))
    (when (some? (get arg :parameterType))
      (.setParameterType o
                         (QueryParameterType/from-edn (get arg
                                                           :parameterType))))
    (when (some? (get arg :parameterValue))
      (.setParameterValue o
                          (custom/QueryParameterValue-from-edn
                            (get arg :parameterValue))))
    o))

(defn to-edn
  [^QueryParameter arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/QueryParameter %)]}
  (when arg
    (cond-> {}
      (some->> (.getName arg)
               (not= ""))
        (assoc :name (.getName arg))
      (.getParameterType arg) (assoc :parameterType
                                (QueryParameterType/to-edn (.getParameterType
                                                             arg)))
      (.getParameterValue arg) (assoc :parameterValue
                                 (custom/QueryParameterValue-to-edn
                                   (.getParameterValue arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "A parameter given to a query.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/QueryParameter}
   [:name
    {:getter-doc
       "Optional. If unset, this is a positional parameter. Otherwise, should be unique within a query.\n\n@return value or {@code null} for none",
     :setter-doc
       "Optional. If unset, this is a positional parameter. Otherwise, should be unique within a query.\n\n@param name name or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:parameterType
    {:getter-doc
       "Required. The type of this parameter.\n\n@return value or {@code null} for none",
     :setter-doc
       "Required. The type of this parameter.\n\n@param parameterType parameterType or {@code null} for none",
     :optional true} :gcp.api.services.bigquery.model/QueryParameterType]
   [:parameterValue
    {:getter-doc
       "Required. The value of this parameter.\n\n@return value or {@code null} for none",
     :setter-doc
       "Required. The value of this parameter.\n\n@param parameterValue parameterValue or {@code null} for none",
     :optional true} :gcp.api.services.bigquery.model/QueryParameterValue]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/QueryParameter schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.QueryParameter"}))