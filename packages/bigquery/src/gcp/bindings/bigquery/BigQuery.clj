;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.BigQuery
  {:doc
     "An interface for Google Cloud BigQuery.\n\n@see <a href=\"https://cloud.google.com/bigquery/what-is-bigquery\">Google Cloud BigQuery</a>",
   :file-git-sha "9c0df5422c05696f7ce4bedf914a58306150dc21",
   :fqcn "com.google.cloud.bigquery.BigQuery"}
  (:require [gcp.bigquery.custom.BigQueryRetryConfig :as BigQueryRetryConfig]
            [gcp.foreign.com.google.cloud :as cloud]
            [gcp.global :as global])
  (:import
    [com.google.cloud RetryOption]
    [com.google.cloud.bigquery BigQuery BigQuery$DatasetDeleteOption
     BigQuery$DatasetField BigQuery$DatasetListOption BigQuery$DatasetOption
     BigQuery$DatasetUpdateMode BigQuery$DatasetView BigQuery$IAMOption
     BigQuery$JobField BigQuery$JobListOption BigQuery$JobOption
     BigQuery$ModelField BigQuery$ModelListOption BigQuery$ModelOption
     BigQuery$QueryOption BigQuery$QueryResultsOption BigQuery$RoutineField
     BigQuery$RoutineListOption BigQuery$RoutineOption
     BigQuery$TableDataListOption BigQuery$TableField BigQuery$TableListOption
     BigQuery$TableMetadataView BigQuery$TableOption JobStatus$State]))

(def DatasetField-schema
  [:enum
   {:closed true,
    :doc
      "Fields of a BigQuery Dataset resource.\n\n@see <a href= \"https://cloud.google.com/bigquery/docs/reference/v2/datasets#resource\">Dataset\n    Resource</a>",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bindings.bigquery/BigQuery.DatasetField} "ACCESS"
   "CREATION_TIME" "DATASET_REFERENCE" "DEFAULT_TABLE_EXPIRATION_MS"
   "DESCRIPTION" "ETAG" "FRIENDLY_NAME" "ID" "LABELS" "LAST_MODIFIED_TIME"
   "LOCATION" "SELF_LINK"])

(def DatasetView-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.bindings.bigquery/BigQuery.DatasetView}
   "DATASET_VIEW_UNSPECIFIED" "FULL" "METADATA" "ACL"])

(def DatasetUpdateMode-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.bindings.bigquery/BigQuery.DatasetUpdateMode}
   "UPDATE_MODE_UNSPECIFIED" "UPDATE_FULL" "UPDATE_METADATA" "UPDATE_ACL"])

(def TableField-schema
  [:enum
   {:closed true,
    :doc
      "Fields of a BigQuery Table resource.\n\n@see <a href= \"https://cloud.google.com/bigquery/docs/reference/v2/tables#resource\">Table\n    Resource</a>",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bindings.bigquery/BigQuery.TableField} "CREATION_TIME"
   "DESCRIPTION" "ETAG" "EXPIRATION_TIME" "EXTERNAL_DATA_CONFIGURATION"
   "FRIENDLY_NAME" "ID" "LABELS" "LAST_MODIFIED_TIME" "LOCATION" "NUM_BYTES"
   "NUM_LONG_TERM_BYTES" "NUM_ROWS" "SCHEMA" "SELF_LINK" "STREAMING_BUFFER"
   "TABLE_REFERENCE" "TIME_PARTITIONING" "RANGE_PARTITIONING" "TYPE" "VIEW"])

(def TableMetadataView-schema
  [:enum
   {:closed true,
    :doc
      "Metadata of a BigQuery Table.\n\n@see <a href=\n    \"https://cloud.google.com/bigquery/docs/reference/rest/v2/tables/get#tablemetadataview\">Table\n    Resource</a>",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bindings.bigquery/BigQuery.TableMetadataView} "BASIC" "FULL"
   "STORAGE_STATS" "TABLE_METADATA_VIEW_UNSPECIFIED"])

(def ModelField-schema
  [:enum
   {:closed true,
    :doc
      "Fields of a BigQuery Model resource.\n\n@see <a href= \"https://cloud.google.com/bigquery/docs/reference/v2/models#resource\">Model\n    Resource</a>",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bindings.bigquery/BigQuery.ModelField} "CREATION_TIME"
   "DESCRIPTION" "ETAG" "EXPIRATION_TIME" "FRIENDLY_NAME" "LABELS"
   "LAST_MODIFIED_TIME" "LOCATION" "MODEL_REFERENCE" "TRAINING_RUNS"
   "LABEL_COLUMNS" "FEATURE_COLUMNS" "TYPE"])

(def RoutineField-schema
  [:enum
   {:closed true,
    :doc
      "Fields of a BigQuery Routine resource.\n\n@see <a href= \"https://cloud.google.com/bigquery/docs/reference/v2/routines#resource\">Routine\n    Resource</a>",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bindings.bigquery/BigQuery.RoutineField} "ARGUMENTS"
   "CREATION_TIME" "DEFINITION_BODY" "ETAG" "IMPORTED_LIBRARIES" "LANGUAGE"
   "LAST_MODIFIED_TIME" "RETURN_TYPE" "ROUTINE_REFERENCE" "ROUTINE_TYPE"])

(def JobField-schema
  [:enum
   {:closed true,
    :doc
      "Fields of a BigQuery Job resource.\n\n@see <a href= \"https://cloud.google.com/bigquery/docs/reference/v2/jobs#resource\">Job Resource\n    </a>",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bindings.bigquery/BigQuery.JobField} "CONFIGURATION" "ETAG"
   "ID" "JOB_REFERENCE" "SELF_LINK" "STATISTICS" "STATUS" "USER_EMAIL"])

(do (defn ^BigQuery$DatasetListOption/1 DatasetListOption-Array-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.DatasetListOption arg)
      (into-array
        BigQuery$DatasetListOption
        (reduce-kv
          (fn [acc k v]
            (case k
              :labelFilter (conj acc (BigQuery$DatasetListOption/labelFilter v))
              :pageSize (conj acc (BigQuery$DatasetListOption/pageSize v))
              :pageToken (conj acc (BigQuery$DatasetListOption/pageToken v))
              :all (if v
                     (clojure.core/conj acc (BigQuery$DatasetListOption/all))
                     acc)
              acc))
          []
          arg)))
    (defn ^BigQuery$DatasetListOption DatasetListOption-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.DatasetListOption arg)
      (reduce-kv
        (fn [acc k v]
          (case k
            :labelFilter (reduced (BigQuery$DatasetListOption/labelFilter v))
            :pageSize (reduced (BigQuery$DatasetListOption/pageSize v))
            :pageToken (reduced (BigQuery$DatasetListOption/pageToken v))
            :all (if v (reduced (BigQuery$DatasetListOption/all)) acc)
            acc))
        nil
        arg)))

(def DatasetListOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying dataset list options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.bindings.bigquery/BigQuery.DatasetListOption}
   [:map {:closed true}
    [:labelFilter
     {:optional true,
      :doc
        "Returns an option to specify a label filter. @see <a href=\n\"https://cloud.google.com/bigquery/docs/adding-using-labels#filtering_datasets_using_labels\">Filtering\nusing labels</a>\n\n@param labelFilter In the form \"labels.key:value\""}
     [:string {:min 1}]]
    [:pageSize
     {:optional true,
      :doc
        "Returns an option to specify the maximum number of datasets returned per page."}
     :int]
    [:pageToken
     {:optional true,
      :doc
        "Returns an option to specify the page token from which to start listing datasets."}
     [:string {:min 1}]]
    [:all
     {:optional true,
      :doc "Returns an options to list all datasets, even hidden ones."}
     :boolean]]])

(do
  (defn ^BigQuery$DatasetOption/1 DatasetOption-Array-from-edn
    [arg]
    (global/strict! :gcp.bindings.bigquery/BigQuery.DatasetOption arg)
    (into-array
      BigQuery$DatasetOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :fields (conj acc
                          (BigQuery$DatasetOption/fields
                            (into-array BigQuery$DatasetField
                                        (map BigQuery$DatasetField/valueOf v))))
            :accessPolicyVersion
              (conj acc (BigQuery$DatasetOption/accessPolicyVersion (int v)))
            :datasetView (conj acc
                               (BigQuery$DatasetOption/datasetView
                                 (BigQuery$DatasetView/valueOf v)))
            :updateMode (conj acc
                              (BigQuery$DatasetOption/updateMode
                                (BigQuery$DatasetUpdateMode/valueOf v)))
            acc))
        []
        arg)))
  (defn ^BigQuery$DatasetOption DatasetOption-from-edn
    [arg]
    (global/strict! :gcp.bindings.bigquery/BigQuery.DatasetOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :fields (reduced (BigQuery$DatasetOption/fields
                             (into-array BigQuery$DatasetField
                                         (map BigQuery$DatasetField/valueOf
                                           v))))
          :accessPolicyVersion
            (reduced (BigQuery$DatasetOption/accessPolicyVersion (int v)))
          :datasetView (reduced (BigQuery$DatasetOption/datasetView
                                  (BigQuery$DatasetView/valueOf v)))
          :updateMode (reduced (BigQuery$DatasetOption/updateMode
                                 (BigQuery$DatasetUpdateMode/valueOf v)))
          acc))
      nil
      arg)))

(def DatasetOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying dataset get, create and update options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.bindings.bigquery/BigQuery.DatasetOption}
   [:map {:closed true}
    [:fields
     {:optional true,
      :doc
        "Returns an option to specify the dataset's fields to be returned by the RPC call. If this\noption is not provided all dataset's fields are returned. {@code DatasetOption.fields} can be\nused to specify only the fields of interest. {@link Dataset#getDatasetId()} is always\nreturned, even if not specified."}
     [:sequential {:min 1}
      [:enum {:closed true} "ACCESS" "CREATION_TIME" "DATASET_REFERENCE"
       "DEFAULT_TABLE_EXPIRATION_MS" "DESCRIPTION" "ETAG" "FRIENDLY_NAME" "ID"
       "LABELS" "LAST_MODIFIED_TIME" "LOCATION" "SELF_LINK"]]]
    [:accessPolicyVersion
     {:optional true,
      :doc
        "Returns an option to specify the dataset's access policy version for conditional access. If\nthis option is not provided the field remains unset and conditional access cannot be used.\nValid values are 0, 1, and 3. Requests specifying an invalid value will be rejected. Requests\nfor conditional access policy binding in datasets must specify version 3. Datasets with no\nconditional role bindings in access policy may specify any valid value or leave the field\nunset. This field will be mapped to <a\nhref=\"https://cloud.google.com/iam/docs/policies#versions\">IAM Policy version</a> and will be\nused to fetch the policy from IAM. If unset or if 0 or 1 the value is used for a dataset with\nconditional bindings, access entry with condition will have role string appended by\n'withcond' string followed by a hash value. Please refer to <a\nhref=\"https://cloud.google.com/iam/docs/troubleshooting-withcond\">Troubleshooting\nwithcond</a> for more details."}
     [:int {:min -2147483648, :max 2147483647}]]
    [:datasetView
     {:optional true,
      :doc
        "Returns an option to specify the view that determines which dataset information is returned.\nBy default, metadata and ACL information are returned."}
     [:enum {:closed true} "DATASET_VIEW_UNSPECIFIED" "FULL" "METADATA" "ACL"]]
    [:updateMode
     {:optional true,
      :doc
        "Returns an option to specify the fields of dataset that update/patch operation is targeting.\nBy default, both metadata and ACL fields are updated."}
     [:enum {:closed true} "UPDATE_MODE_UNSPECIFIED" "UPDATE_FULL"
      "UPDATE_METADATA" "UPDATE_ACL"]]]])

(do (defn
      ^BigQuery$DatasetDeleteOption/1 DatasetDeleteOption-Array-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.DatasetDeleteOption arg)
      (into-array BigQuery$DatasetDeleteOption
                  (reduce-kv
                    (fn [acc k v]
                      (case k
                        :deleteContents
                          (if v
                            (clojure.core/conj
                              acc
                              (BigQuery$DatasetDeleteOption/deleteContents))
                            acc)
                        acc))
                    []
                    arg)))
    (defn ^BigQuery$DatasetDeleteOption DatasetDeleteOption-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.DatasetDeleteOption arg)
      (reduce-kv
        (fn [acc k v]
          (case k
            :deleteContents
              (if v (reduced (BigQuery$DatasetDeleteOption/deleteContents)) acc)
            acc))
        nil
        arg)))

(def DatasetDeleteOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying dataset delete options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.bindings.bigquery/BigQuery.DatasetDeleteOption}
   [:map {:closed true}
    [:deleteContents
     {:optional true,
      :doc
        "Returns an option to delete a dataset even if non-empty. If not provided, attempting to\ndelete a non-empty dataset will result in a {@link BigQueryException} being thrown."}
     :boolean]]])

(do (defn ^BigQuery$ModelListOption/1 ModelListOption-Array-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.ModelListOption arg)
      (into-array
        BigQuery$ModelListOption
        (reduce-kv
          (fn [acc k v]
            (case k
              :pageSize (conj acc (BigQuery$ModelListOption/pageSize v))
              :pageToken (conj acc (BigQuery$ModelListOption/pageToken v))
              acc))
          []
          arg)))
    (defn ^BigQuery$ModelListOption ModelListOption-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.ModelListOption arg)
      (reduce-kv (fn [acc k v]
                   (case k
                     :pageSize (reduced (BigQuery$ModelListOption/pageSize v))
                     :pageToken (reduced (BigQuery$ModelListOption/pageToken v))
                     acc))
                 nil
                 arg)))

(def ModelListOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying table list options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.bindings.bigquery/BigQuery.ModelListOption}
   [:map {:closed true}
    [:pageSize
     {:optional true,
      :doc
        "Returns an option to specify the maximum number of models returned per page."}
     :int]
    [:pageToken
     {:optional true,
      :doc
        "Returns an option to specify the page token from which to start listing models."}
     [:string {:min 1}]]]])

(do (defn ^BigQuery$RoutineListOption/1 RoutineListOption-Array-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.RoutineListOption arg)
      (into-array
        BigQuery$RoutineListOption
        (reduce-kv
          (fn [acc k v]
            (case k
              :pageSize (conj acc (BigQuery$RoutineListOption/pageSize v))
              :pageToken (conj acc (BigQuery$RoutineListOption/pageToken v))
              acc))
          []
          arg)))
    (defn ^BigQuery$RoutineListOption RoutineListOption-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.RoutineListOption arg)
      (reduce-kv (fn [acc k v]
                   (case k
                     :pageSize (reduced (BigQuery$RoutineListOption/pageSize v))
                     :pageToken (reduced (BigQuery$RoutineListOption/pageToken
                                           v))
                     acc))
                 nil
                 arg)))

(def RoutineListOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying routine list options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.bindings.bigquery/BigQuery.RoutineListOption}
   [:map {:closed true}
    [:pageSize
     {:optional true,
      :doc
        "Returns an option to specify the maximum number of routines returned per page."}
     :int]
    [:pageToken
     {:optional true,
      :doc
        "Returns an option to specify the page token from which to start listing routines."}
     [:string {:min 1}]]]])

(do (defn ^BigQuery$TableListOption/1 TableListOption-Array-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.TableListOption arg)
      (into-array
        BigQuery$TableListOption
        (reduce-kv
          (fn [acc k v]
            (case k
              :pageSize (conj acc (BigQuery$TableListOption/pageSize v))
              :pageToken (conj acc (BigQuery$TableListOption/pageToken v))
              acc))
          []
          arg)))
    (defn ^BigQuery$TableListOption TableListOption-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.TableListOption arg)
      (reduce-kv (fn [acc k v]
                   (case k
                     :pageSize (reduced (BigQuery$TableListOption/pageSize v))
                     :pageToken (reduced (BigQuery$TableListOption/pageToken v))
                     acc))
                 nil
                 arg)))

(def TableListOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying table list options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.bindings.bigquery/BigQuery.TableListOption}
   [:map {:closed true}
    [:pageSize
     {:optional true,
      :doc
        "Returns an option to specify the maximum number of tables returned per page."}
     :int]
    [:pageToken
     {:optional true,
      :doc
        "Returns an option to specify the page token from which to start listing tables."}
     [:string {:min 1}]]]])

(do
  (defn ^BigQuery$TableOption/1 TableOption-Array-from-edn
    [arg]
    (global/strict! :gcp.bindings.bigquery/BigQuery.TableOption arg)
    (into-array
      BigQuery$TableOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :fields (conj acc
                          (BigQuery$TableOption/fields
                            (into-array BigQuery$TableField
                                        (map BigQuery$TableField/valueOf v))))
            :autodetectSchema (conj acc
                                    (BigQuery$TableOption/autodetectSchema v))
            :tableMetadataView (conj acc
                                     (BigQuery$TableOption/tableMetadataView
                                       (BigQuery$TableMetadataView/valueOf v)))
            acc))
        []
        arg)))
  (defn ^BigQuery$TableOption TableOption-from-edn
    [arg]
    (global/strict! :gcp.bindings.bigquery/BigQuery.TableOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :fields (reduced (BigQuery$TableOption/fields
                             (into-array BigQuery$TableField
                                         (map BigQuery$TableField/valueOf v))))
          :autodetectSchema (reduced (BigQuery$TableOption/autodetectSchema v))
          :tableMetadataView (reduced (BigQuery$TableOption/tableMetadataView
                                        (BigQuery$TableMetadataView/valueOf v)))
          acc))
      nil
      arg)))

(def TableOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying table get, create and update options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.bindings.bigquery/BigQuery.TableOption}
   [:map {:closed true}
    [:fields
     {:optional true,
      :doc
        "Returns an option to specify the table's fields to be returned by the RPC call. If this\noption is not provided all table's fields are returned. {@code TableOption.fields} can be\nused to specify only the fields of interest. {@link Table#getTableId()} and type (which is\npart of {@link Table#getDefinition()}) are always returned, even if not specified."}
     [:sequential {:min 1}
      [:enum {:closed true} "CREATION_TIME" "DESCRIPTION" "ETAG"
       "EXPIRATION_TIME" "EXTERNAL_DATA_CONFIGURATION" "FRIENDLY_NAME" "ID"
       "LABELS" "LAST_MODIFIED_TIME" "LOCATION" "NUM_BYTES"
       "NUM_LONG_TERM_BYTES" "NUM_ROWS" "SCHEMA" "SELF_LINK" "STREAMING_BUFFER"
       "TABLE_REFERENCE" "TIME_PARTITIONING" "RANGE_PARTITIONING" "TYPE"
       "VIEW"]]]
    [:autodetectSchema
     {:optional true,
      :doc
        "Returns an option to specify the schema of the table (only applicable for external tables)\nshould be autodetected when updating the table from the underlying source."}
     :boolean]
    [:tableMetadataView
     {:optional true,
      :doc "Returns an option to specify the metadata of the table."}
     [:enum {:closed true} "BASIC" "FULL" "STORAGE_STATS"
      "TABLE_METADATA_VIEW_UNSPECIFIED"]]]])

(do (defn ^BigQuery$IAMOption/1 IAMOption-Array-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.IAMOption arg)
      (into-array BigQuery$IAMOption
                  (reduce-kv
                    (fn [acc k v]
                      (case k
                        :requestedPolicyVersion
                          (conj acc
                                (BigQuery$IAMOption/requestedPolicyVersion v))
                        acc))
                    []
                    arg)))
    (defn ^BigQuery$IAMOption IAMOption-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.IAMOption arg)
      (reduce-kv (fn [acc k v]
                   (case k
                     :requestedPolicyVersion
                       (reduced (BigQuery$IAMOption/requestedPolicyVersion v))
                     acc))
                 nil
                 arg)))

(def IAMOption-schema
  [:maybe
   {:closed true,
    :doc nil,
    :gcp/category :nested/client-options,
    :gcp/key :gcp.bindings.bigquery/BigQuery.IAMOption}
   [:map {:closed true} [:requestedPolicyVersion {:optional true} :int]]])

(do (defn ^BigQuery$ModelOption/1 ModelOption-Array-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.ModelOption arg)
      (into-array BigQuery$ModelOption
                  (reduce-kv
                    (fn [acc k v]
                      (case k
                        :fields (conj acc
                                      (BigQuery$ModelOption/fields
                                        (into-array
                                          BigQuery$ModelField
                                          (map BigQuery$ModelField/valueOf v))))
                        acc))
                    []
                    arg)))
    (defn ^BigQuery$ModelOption ModelOption-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.ModelOption arg)
      (reduce-kv (fn [acc k v]
                   (case k
                     :fields (reduced (BigQuery$ModelOption/fields
                                        (into-array
                                          BigQuery$ModelField
                                          (map BigQuery$ModelField/valueOf v))))
                     acc))
                 nil
                 arg)))

(def ModelOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying model get, create and update options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.bindings.bigquery/BigQuery.ModelOption}
   [:map {:closed true}
    [:fields
     {:optional true,
      :doc
        "Returns an option to specify the model's fields to be returned by the RPC call. If this\noption is not provided all model's fields are returned. {@code ModelOption.fields} can be\nused to specify only the fields of interest."}
     [:sequential {:min 1}
      [:enum {:closed true} "CREATION_TIME" "DESCRIPTION" "ETAG"
       "EXPIRATION_TIME" "FRIENDLY_NAME" "LABELS" "LAST_MODIFIED_TIME"
       "LOCATION" "MODEL_REFERENCE" "TRAINING_RUNS" "LABEL_COLUMNS"
       "FEATURE_COLUMNS" "TYPE"]]]]])

(do
  (defn ^BigQuery$RoutineOption/1 RoutineOption-Array-from-edn
    [arg]
    (global/strict! :gcp.bindings.bigquery/BigQuery.RoutineOption arg)
    (into-array BigQuery$RoutineOption
                (reduce-kv
                  (fn [acc k v]
                    (case k
                      :fields (conj acc
                                    (BigQuery$RoutineOption/fields
                                      (into-array
                                        BigQuery$RoutineField
                                        (map BigQuery$RoutineField/valueOf v))))
                      acc))
                  []
                  arg)))
  (defn ^BigQuery$RoutineOption RoutineOption-from-edn
    [arg]
    (global/strict! :gcp.bindings.bigquery/BigQuery.RoutineOption arg)
    (reduce-kv (fn [acc k v]
                 (case k
                   :fields (reduced (BigQuery$RoutineOption/fields
                                      (into-array
                                        BigQuery$RoutineField
                                        (map BigQuery$RoutineField/valueOf v))))
                   acc))
               nil
               arg)))

(def RoutineOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying table get, create and update options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.bindings.bigquery/BigQuery.RoutineOption}
   [:map {:closed true}
    [:fields
     {:optional true,
      :doc
        "Returns an option to specify the routines's fields to be returned by the RPC call. If this\noption is not provided all model's fields are returned. {@code RoutineOption.fields} can be\nused to specify only the fields of interest."}
     [:sequential {:min 1}
      [:enum {:closed true} "ARGUMENTS" "CREATION_TIME" "DEFINITION_BODY" "ETAG"
       "IMPORTED_LIBRARIES" "LANGUAGE" "LAST_MODIFIED_TIME" "RETURN_TYPE"
       "ROUTINE_REFERENCE" "ROUTINE_TYPE"]]]]])

(do (defn
      ^BigQuery$TableDataListOption/1 TableDataListOption-Array-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.TableDataListOption arg)
      (into-array
        BigQuery$TableDataListOption
        (reduce-kv
          (fn [acc k v]
            (case k
              :pageSize (conj acc (BigQuery$TableDataListOption/pageSize v))
              :pageToken (conj acc (BigQuery$TableDataListOption/pageToken v))
              :startIndex (conj acc (BigQuery$TableDataListOption/startIndex v))
              acc))
          []
          arg)))
    (defn ^BigQuery$TableDataListOption TableDataListOption-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.TableDataListOption arg)
      (reduce-kv
        (fn [acc k v]
          (case k
            :pageSize (reduced (BigQuery$TableDataListOption/pageSize v))
            :pageToken (reduced (BigQuery$TableDataListOption/pageToken v))
            :startIndex (reduced (BigQuery$TableDataListOption/startIndex v))
            acc))
        nil
        arg)))

(def TableDataListOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying table data list options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.bindings.bigquery/BigQuery.TableDataListOption}
   [:map {:closed true}
    [:pageSize
     {:optional true,
      :doc
        "Returns an option to specify the maximum number of rows returned per page."}
     :int]
    [:pageToken
     {:optional true,
      :doc
        "Returns an option to specify the page token from which to start listing table data."}
     [:string {:min 1}]]
    [:startIndex
     {:optional true,
      :doc
        "Returns an option that sets the zero-based index of the row from which to start listing table\ndata."}
     :int]]])

(do
  (defn ^BigQuery$JobListOption/1 JobListOption-Array-from-edn
    [arg]
    (global/strict! :gcp.bindings.bigquery/BigQuery.JobListOption arg)
    (into-array
      BigQuery$JobListOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :allUsers (if v
                        (clojure.core/conj acc
                                           (BigQuery$JobListOption/allUsers))
                        acc)
            :stateFilter (conj acc
                               (BigQuery$JobListOption/stateFilter
                                 (into-array JobStatus$State
                                             (map JobStatus$State/valueOf v))))
            :minCreationTime (conj acc
                                   (BigQuery$JobListOption/minCreationTime v))
            :maxCreationTime (conj acc
                                   (BigQuery$JobListOption/maxCreationTime v))
            :pageSize (conj acc (BigQuery$JobListOption/pageSize v))
            :pageToken (conj acc (BigQuery$JobListOption/pageToken v))
            :parentJobId (conj acc (BigQuery$JobListOption/parentJobId v))
            :fields (conj acc
                          (BigQuery$JobListOption/fields
                            (into-array BigQuery$JobField
                                        (map BigQuery$JobField/valueOf v))))
            acc))
        []
        arg)))
  (defn ^BigQuery$JobListOption JobListOption-from-edn
    [arg]
    (global/strict! :gcp.bindings.bigquery/BigQuery.JobListOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :allUsers (if v (reduced (BigQuery$JobListOption/allUsers)) acc)
          :stateFilter (reduced (BigQuery$JobListOption/stateFilter
                                  (into-array JobStatus$State
                                              (map JobStatus$State/valueOf v))))
          :minCreationTime (reduced (BigQuery$JobListOption/minCreationTime v))
          :maxCreationTime (reduced (BigQuery$JobListOption/maxCreationTime v))
          :pageSize (reduced (BigQuery$JobListOption/pageSize v))
          :pageToken (reduced (BigQuery$JobListOption/pageToken v))
          :parentJobId (reduced (BigQuery$JobListOption/parentJobId v))
          :fields (reduced (BigQuery$JobListOption/fields
                             (into-array BigQuery$JobField
                                         (map BigQuery$JobField/valueOf v))))
          acc))
      nil
      arg)))

(def JobListOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying job list options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.bindings.bigquery/BigQuery.JobListOption}
   [:map {:closed true}
    [:allUsers
     {:optional true,
      :doc
        "Returns an option to list all jobs, even the ones issued by other users."}
     :boolean]
    [:stateFilter
     {:optional true,
      :doc
        "Returns an option to list only jobs that match the provided state filters."}
     [:sequential {:min 1} [:enum {:closed true} "PENDING" "RUNNING" "DONE"]]]
    [:minCreationTime
     {:optional true,
      :doc
        "Returns an option to filter out jobs before the given minimum creation time."}
     :int]
    [:maxCreationTime
     {:optional true,
      :doc
        "Returns an option to filter out jobs after the given maximum creation time."}
     :int]
    [:pageSize
     {:optional true,
      :doc
        "Returns an option to specify the maximum number of jobs returned per page."}
     :int]
    [:pageToken
     {:optional true,
      :doc
        "Returns an option to specify the page token from which to start listing jobs."}
     [:string {:min 1}]]
    [:parentJobId
     {:optional true,
      :doc
        "Returns an option to list only child job from specify parent job id."}
     [:string {:min 1}]]
    [:fields
     {:optional true,
      :doc
        "Returns an option to specify the job's fields to be returned by the RPC call. If this option\nis not provided all job's fields are returned. {@code JobOption.fields()} can be used to\nspecify only the fields of interest. {@link Job#getJobId()}, {@link JobStatus#getState()},\n{@link JobStatus#getError()} as well as type-specific configuration (e.g. {@link\nQueryJobConfiguration#getQuery()} for Query Jobs) are always returned, even if not specified.\n{@link JobField#SELF_LINK} and {@link JobField#ETAG} can not be selected when listing jobs."}
     [:sequential {:min 1}
      [:enum {:closed true} "CONFIGURATION" "ETAG" "ID" "JOB_REFERENCE"
       "SELF_LINK" "STATISTICS" "STATUS" "USER_EMAIL"]]]]])

(do
  (defn ^BigQuery$JobOption/1 JobOption-Array-from-edn
    [arg]
    (global/strict! :gcp.bindings.bigquery/BigQuery.JobOption arg)
    (into-array
      BigQuery$JobOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :fields (conj acc
                          (BigQuery$JobOption/fields
                            (into-array BigQuery$JobField
                                        (map BigQuery$JobField/valueOf v))))
            :bigQueryRetryConfig (conj acc
                                       (BigQuery$JobOption/bigQueryRetryConfig
                                         (BigQueryRetryConfig/from-edn v)))
            :retryOptions (conj acc
                                (BigQuery$JobOption/retryOptions
                                  (into-array RetryOption
                                              (map cloud/RetryOption-from-edn
                                                v))))
            acc))
        []
        arg)))
  (defn ^BigQuery$JobOption JobOption-from-edn
    [arg]
    (global/strict! :gcp.bindings.bigquery/BigQuery.JobOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :fields (reduced (BigQuery$JobOption/fields
                             (into-array BigQuery$JobField
                                         (map BigQuery$JobField/valueOf v))))
          :bigQueryRetryConfig (reduced (BigQuery$JobOption/bigQueryRetryConfig
                                          (BigQueryRetryConfig/from-edn v)))
          :retryOptions (reduced (BigQuery$JobOption/retryOptions
                                   (into-array RetryOption
                                               (map cloud/RetryOption-from-edn
                                                 v))))
          acc))
      nil
      arg)))

(def JobOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying table get and create options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.bindings.bigquery/BigQuery.JobOption}
   [:map {:closed true}
    [:fields
     {:optional true,
      :doc
        "Returns an option to specify the job's fields to be returned by the RPC call. If this option\nis not provided all job's fields are returned. {@code JobOption.fields()} can be used to\nspecify only the fields of interest. {@link Job#getJobId()} as well as type-specific\nconfiguration (e.g. {@link QueryJobConfiguration#getQuery()} for Query Jobs) are always\nreturned, even if not specified."}
     [:sequential {:min 1}
      [:enum {:closed true} "CONFIGURATION" "ETAG" "ID" "JOB_REFERENCE"
       "SELF_LINK" "STATISTICS" "STATUS" "USER_EMAIL"]]]
    [:bigQueryRetryConfig
     {:optional true,
      :doc
        "Returns an option to specify the job's BigQuery retry configuration."}
     :gcp.bigquery.custom/BigQueryRetryConfig]
    [:retryOptions
     {:optional true,
      :doc "Returns an option to specify the job's retry options."}
     [:sequential {:min 1} :gcp.foreign.com.google.cloud/RetryOption]]]])

(do (defn ^BigQuery$QueryResultsOption/1 QueryResultsOption-Array-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.QueryResultsOption arg)
      (into-array
        BigQuery$QueryResultsOption
        (reduce-kv
          (fn [acc k v]
            (case k
              :pageSize (conj acc (BigQuery$QueryResultsOption/pageSize v))
              :pageToken (conj acc (BigQuery$QueryResultsOption/pageToken v))
              :startIndex (conj acc (BigQuery$QueryResultsOption/startIndex v))
              :maxWaitTime (conj acc
                                 (BigQuery$QueryResultsOption/maxWaitTime v))
              acc))
          []
          arg)))
    (defn ^BigQuery$QueryResultsOption QueryResultsOption-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.QueryResultsOption arg)
      (reduce-kv
        (fn [acc k v]
          (case k
            :pageSize (reduced (BigQuery$QueryResultsOption/pageSize v))
            :pageToken (reduced (BigQuery$QueryResultsOption/pageToken v))
            :startIndex (reduced (BigQuery$QueryResultsOption/startIndex v))
            :maxWaitTime (reduced (BigQuery$QueryResultsOption/maxWaitTime v))
            acc))
        nil
        arg)))

(def QueryResultsOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying query results options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.bindings.bigquery/BigQuery.QueryResultsOption}
   [:map {:closed true}
    [:pageSize
     {:optional true,
      :doc
        "Returns an option to specify the maximum number of rows returned per page."}
     :int]
    [:pageToken
     {:optional true,
      :doc
        "Returns an option to specify the page token from which to start getting query results."}
     [:string {:min 1}]]
    [:startIndex
     {:optional true,
      :doc
        "Returns an option that sets the zero-based index of the row from which to start getting query\nresults."}
     :int]
    [:maxWaitTime
     {:optional true,
      :doc
        "Returns an option that sets how long to wait for the query to complete, in milliseconds,\nbefore returning. Default is 10 seconds."}
     :int]]])

(do (defn ^BigQuery$QueryOption/1 QueryOption-Array-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.QueryOption arg)
      (into-array BigQuery$QueryOption
                  (reduce-kv
                    (fn [acc k v]
                      (case k
                        :resultsOption (conj acc
                                             (BigQuery$QueryOption/of
                                               (QueryResultsOption-from-edn v)))
                        :waitOption (conj acc
                                          (BigQuery$QueryOption/of
                                            (cloud/RetryOption-from-edn v)))
                        acc))
                    []
                    arg)))
    (defn ^BigQuery$QueryOption QueryOption-from-edn
      [arg]
      (global/strict! :gcp.bindings.bigquery/BigQuery.QueryOption arg)
      (reduce-kv (fn [acc k v]
                   (case k
                     :resultsOption (reduced (BigQuery$QueryOption/of
                                               (QueryResultsOption-from-edn v)))
                     :waitOption (reduced (BigQuery$QueryOption/of
                                            (cloud/RetryOption-from-edn v)))
                     acc))
                 nil
                 arg)))

(def QueryOption-schema
  [:maybe
   {:closed true,
    :doc nil,
    :gcp/category :nested/client-options,
    :gcp/key :gcp.bindings.bigquery/BigQuery.QueryOption}
   [:map {:closed true}
    [:resultsOption {:optional true}
     :gcp.bindings.bigquery/BigQuery.QueryResultsOption]
    [:waitOption {:optional true} :gcp.foreign.com.google.cloud/RetryOption]]])

(global/include-schema-registry!
  (with-meta
    {:gcp.bindings.bigquery/BigQuery.DatasetDeleteOption DatasetDeleteOption-schema,
     :gcp.bindings.bigquery/BigQuery.DatasetField DatasetField-schema,
     :gcp.bindings.bigquery/BigQuery.DatasetListOption DatasetListOption-schema,
     :gcp.bindings.bigquery/BigQuery.DatasetOption DatasetOption-schema,
     :gcp.bindings.bigquery/BigQuery.DatasetUpdateMode DatasetUpdateMode-schema,
     :gcp.bindings.bigquery/BigQuery.DatasetView DatasetView-schema,
     :gcp.bindings.bigquery/BigQuery.IAMOption IAMOption-schema,
     :gcp.bindings.bigquery/BigQuery.JobField JobField-schema,
     :gcp.bindings.bigquery/BigQuery.JobListOption JobListOption-schema,
     :gcp.bindings.bigquery/BigQuery.JobOption JobOption-schema,
     :gcp.bindings.bigquery/BigQuery.ModelField ModelField-schema,
     :gcp.bindings.bigquery/BigQuery.ModelListOption ModelListOption-schema,
     :gcp.bindings.bigquery/BigQuery.ModelOption ModelOption-schema,
     :gcp.bindings.bigquery/BigQuery.QueryOption QueryOption-schema,
     :gcp.bindings.bigquery/BigQuery.QueryResultsOption QueryResultsOption-schema,
     :gcp.bindings.bigquery/BigQuery.RoutineField RoutineField-schema,
     :gcp.bindings.bigquery/BigQuery.RoutineListOption RoutineListOption-schema,
     :gcp.bindings.bigquery/BigQuery.RoutineOption RoutineOption-schema,
     :gcp.bindings.bigquery/BigQuery.TableDataListOption TableDataListOption-schema,
     :gcp.bindings.bigquery/BigQuery.TableField TableField-schema,
     :gcp.bindings.bigquery/BigQuery.TableListOption TableListOption-schema,
     :gcp.bindings.bigquery/BigQuery.TableMetadataView TableMetadataView-schema,
     :gcp.bindings.bigquery/BigQuery.TableOption TableOption-schema}
    {:gcp.global/name "gcp.bindings.bigquery.BigQuery"}))