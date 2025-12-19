(ns gcp.bigquery.v2.synth
  (:require [gcp.global :as g]
            [gcp.bigquery.v2.BigQueryOptions]
            [gcp.bigquery.v2.BigQuery]))

(def schemas
  {:gcp.bigquery.v2.synth/labels       [:map-of :string :string]
   :gcp.bigquery.v2.synth/resourceTags [:map-of :string :string]
   :gcp.bigquery.v2.synth/location     [:string {:min 1}]
   :gcp.bigquery.v2.synth/project      [:string {:min 1}]
   :gcp.bigquery.v2.synth/dataset      [:string {:min 1}]
   :gcp.bigquery.v2.synth/table        [:string {:min 1}]
   :gcp.bigquery.v2.synth/uri          :string
   :gcp.bigquery.v2.synth/compression  [:enum "GZIP" "DEFLATE" "SNAPPY"]
   :gcp.bigquery.v2.synth/format       [:enum
                                     {:doc "The default value for tables is CSV. Tables with nested or repeated fields cannot be exported as CSV. The default value for models is ML_TF_SAVED_MODEL."}
                                     "AVRO" "CSV" "PARQUET" "NEWLINE_DELIMITED_JSON"
                                     "ML_TF_SAVED_MODEL" "ML_XGBOOST_BOOSTER"]
   :gcp.bigquery.v2.synth/client       (assoc-in (g/instance-schema com.google.cloud.bigquery.BigQuery)
                                               [1 :from-edn] 'gcp.bigquery.v2.BigQueryOptions/get-service)
   :gcp.bigquery.v2.synth/clientable   [:maybe
                                     [:or
                                      :gcp.bigquery.v2/BigQueryOptions
                                      :gcp.bigquery.v2.synth/client
                                      [:map [:bigquery [:or :gcp.bigquery.v2/BigQueryOptions :gcp.bigquery.v2.synth/client]]]]]})

(g/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
