;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.BigQueryConfig
  {:doc
     "<pre>\nConfiguration for a BigQuery subscription.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.BigQueryConfig}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.BigQueryConfig"
   :gcp.dev/certification
     {:base-seed 1777403434239
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777403434239 :standard 1777403434240 :stress 1777403434241}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-28T19:10:35.805397671Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.pubsub.v1 BigQueryConfig BigQueryConfig$Builder
            BigQueryConfig$State]))

(declare from-edn to-edn State-from-edn State-to-edn)

(def State-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nPossible states for a BigQuery subscription.\n</pre>\n\nProtobuf enum {@code google.pubsub.v1.BigQueryConfig.State}",
    :gcp/category :nested/enum,
    :gcp/key :gcp.pubsub.v1/BigQueryConfig.State} "STATE_UNSPECIFIED" "ACTIVE"
   "PERMISSION_DENIED" "NOT_FOUND" "SCHEMA_MISMATCH"
   "IN_TRANSIT_LOCATION_RESTRICTION" "VERTEX_AI_LOCATION_RESTRICTION"])

(defn ^BigQueryConfig from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/BigQueryConfig arg)
  (let [builder (BigQueryConfig/newBuilder)]
    (when (some? (get arg :dropUnknownFields))
      (.setDropUnknownFields builder (get arg :dropUnknownFields)))
    (when (some? (get arg :serviceAccountEmail))
      (.setServiceAccountEmail builder (get arg :serviceAccountEmail)))
    (when (some? (get arg :state))
      (.setState builder (BigQueryConfig$State/valueOf (get arg :state))))
    (when (some? (get arg :table)) (.setTable builder (get arg :table)))
    (when (some? (get arg :useTableSchema))
      (.setUseTableSchema builder (get arg :useTableSchema)))
    (when (some? (get arg :useTopicSchema))
      (.setUseTopicSchema builder (get arg :useTopicSchema)))
    (when (some? (get arg :writeMetadata))
      (.setWriteMetadata builder (get arg :writeMetadata)))
    (.build builder)))

(defn to-edn
  [^BigQueryConfig arg]
  {:post [(global/strict! :gcp.pubsub.v1/BigQueryConfig %)]}
  (when arg
    (cond-> {}
      (.getDropUnknownFields arg) (assoc :dropUnknownFields
                                    (.getDropUnknownFields arg))
      (some->> (.getServiceAccountEmail arg)
               (not= ""))
        (assoc :serviceAccountEmail (.getServiceAccountEmail arg))
      (.getState arg) (assoc :state (.name (.getState arg)))
      (some->> (.getTable arg)
               (not= ""))
        (assoc :table (.getTable arg))
      (.getUseTableSchema arg) (assoc :useTableSchema (.getUseTableSchema arg))
      (.getUseTopicSchema arg) (assoc :useTopicSchema (.getUseTopicSchema arg))
      (.getWriteMetadata arg) (assoc :writeMetadata (.getWriteMetadata arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nConfiguration for a BigQuery subscription.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.BigQueryConfig}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.pubsub.v1/BigQueryConfig}
   [:dropUnknownFields
    {:optional true,
     :getter-doc
       "<pre>\nOptional. When true and use_topic_schema is true, any fields that are a\npart of the topic schema that are not part of the BigQuery table schema are\ndropped when writing to BigQuery. Otherwise, the schemas must be kept in\nsync and any messages with extra fields are not written and remain in the\nsubscription's backlog.\n</pre>\n\n<code>bool drop_unknown_fields = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The dropUnknownFields.",
     :setter-doc
       "<pre>\nOptional. When true and use_topic_schema is true, any fields that are a\npart of the topic schema that are not part of the BigQuery table schema are\ndropped when writing to BigQuery. Otherwise, the schemas must be kept in\nsync and any messages with extra fields are not written and remain in the\nsubscription's backlog.\n</pre>\n\n<code>bool drop_unknown_fields = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The dropUnknownFields to set.\n@return This builder for chaining."}
    :boolean]
   [:serviceAccountEmail
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The service account to use to write to BigQuery. The subscription\ncreator or updater that specifies this field must have\n`iam.serviceAccounts.actAs` permission on the service account. If not\nspecified, the Pub/Sub [service\nagent](https://cloud.google.com/iam/docs/service-agents),\nservice-{project_number}&#64;gcp-sa-pubsub.iam.gserviceaccount.com, is used.\n</pre>\n\n<code>string service_account_email = 7 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The serviceAccountEmail.",
     :setter-doc
       "<pre>\nOptional. The service account to use to write to BigQuery. The subscription\ncreator or updater that specifies this field must have\n`iam.serviceAccounts.actAs` permission on the service account. If not\nspecified, the Pub/Sub [service\nagent](https://cloud.google.com/iam/docs/service-agents),\nservice-{project_number}&#64;gcp-sa-pubsub.iam.gserviceaccount.com, is used.\n</pre>\n\n<code>string service_account_email = 7 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The serviceAccountEmail to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:state
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. An output-only field that indicates whether or not the\nsubscription can receive messages.\n</pre>\n\n<code>\n.google.pubsub.v1.BigQueryConfig.State state = 5 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The state."}
    [:enum {:closed true} "STATE_UNSPECIFIED" "ACTIVE" "PERMISSION_DENIED"
     "NOT_FOUND" "SCHEMA_MISMATCH" "IN_TRANSIT_LOCATION_RESTRICTION"
     "VERTEX_AI_LOCATION_RESTRICTION"]]
   [:table
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The name of the table to which to write data, of the form\n{projectId}.{datasetId}.{tableId}\n</pre>\n\n<code>string table = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The table.",
     :setter-doc
       "<pre>\nOptional. The name of the table to which to write data, of the form\n{projectId}.{datasetId}.{tableId}\n</pre>\n\n<code>string table = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The table to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:useTableSchema
    {:optional true,
     :getter-doc
       "<pre>\nOptional. When true, use the BigQuery table's schema as the columns to\nwrite to in BigQuery. `use_table_schema` and `use_topic_schema` cannot be\nenabled at the same time.\n</pre>\n\n<code>bool use_table_schema = 6 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The useTableSchema.",
     :setter-doc
       "<pre>\nOptional. When true, use the BigQuery table's schema as the columns to\nwrite to in BigQuery. `use_table_schema` and `use_topic_schema` cannot be\nenabled at the same time.\n</pre>\n\n<code>bool use_table_schema = 6 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The useTableSchema to set.\n@return This builder for chaining."}
    :boolean]
   [:useTopicSchema
    {:optional true,
     :getter-doc
       "<pre>\nOptional. When true, use the topic's schema as the columns to write to in\nBigQuery, if it exists. `use_topic_schema` and `use_table_schema` cannot be\nenabled at the same time.\n</pre>\n\n<code>bool use_topic_schema = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The useTopicSchema.",
     :setter-doc
       "<pre>\nOptional. When true, use the topic's schema as the columns to write to in\nBigQuery, if it exists. `use_topic_schema` and `use_table_schema` cannot be\nenabled at the same time.\n</pre>\n\n<code>bool use_topic_schema = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The useTopicSchema to set.\n@return This builder for chaining."}
    :boolean]
   [:writeMetadata
    {:optional true,
     :getter-doc
       "<pre>\nOptional. When true, write the subscription name, message_id, publish_time,\nattributes, and ordering_key to additional columns in the table. The\nsubscription name, message_id, and publish_time fields are put in their own\ncolumns while all other message properties (other than data) are written to\na JSON object in the attributes column.\n</pre>\n\n<code>bool write_metadata = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The writeMetadata.",
     :setter-doc
       "<pre>\nOptional. When true, write the subscription name, message_id, publish_time,\nattributes, and ordering_key to additional columns in the table. The\nsubscription name, message_id, and publish_time fields are put in their own\ncolumns while all other message properties (other than data) are written to\na JSON object in the attributes column.\n</pre>\n\n<code>bool write_metadata = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The writeMetadata to set.\n@return This builder for chaining."}
    :boolean]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/BigQueryConfig schema,
              :gcp.pubsub.v1/BigQueryConfig.State State-schema}
    {:gcp.global/name "gcp.pubsub.v1.BigQueryConfig"}))