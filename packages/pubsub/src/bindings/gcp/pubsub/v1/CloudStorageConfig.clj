;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.CloudStorageConfig
  {:doc
     "<pre>\nConfiguration for a Cloud Storage subscription.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.CloudStorageConfig}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.CloudStorageConfig"
   :gcp.dev/certification
     {:base-seed 1777403440522
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777403440522 :standard 1777403440523 :stress 1777403440524}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-28T19:10:42.199735922Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global])
  (:import [com.google.protobuf Duration]
           [com.google.pubsub.v1 CloudStorageConfig
            CloudStorageConfig$AvroConfig CloudStorageConfig$AvroConfig$Builder
            CloudStorageConfig$Builder CloudStorageConfig$OutputFormatCase
            CloudStorageConfig$State CloudStorageConfig$TextConfig
            CloudStorageConfig$TextConfig$Builder]))

(declare from-edn
         to-edn
         State-from-edn
         State-to-edn
         TextConfig-from-edn
         TextConfig-to-edn
         AvroConfig-from-edn
         AvroConfig-to-edn
         OutputFormatCase-from-edn
         OutputFormatCase-to-edn)

(def State-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nPossible states for a Cloud Storage subscription.\n</pre>\n\nProtobuf enum {@code google.pubsub.v1.CloudStorageConfig.State}",
    :gcp/category :nested/enum,
    :gcp/key :gcp.pubsub.v1/CloudStorageConfig.State} "STATE_UNSPECIFIED"
   "ACTIVE" "PERMISSION_DENIED" "NOT_FOUND" "IN_TRANSIT_LOCATION_RESTRICTION"
   "SCHEMA_MISMATCH" "VERTEX_AI_LOCATION_RESTRICTION"])

(defn ^CloudStorageConfig$TextConfig TextConfig-from-edn
  [arg]
  (let [builder (CloudStorageConfig$TextConfig/newBuilder)] (.build builder)))

(defn TextConfig-to-edn
  [^CloudStorageConfig$TextConfig arg]
  (when arg (cond-> {})))

(def TextConfig-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nConfiguration for writing message data in text format.\nMessage payloads will be written to files as raw text, separated by a\nnewline.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.CloudStorageConfig.TextConfig}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.pubsub.v1/CloudStorageConfig.TextConfig}])

(defn ^CloudStorageConfig$AvroConfig AvroConfig-from-edn
  [arg]
  (let [builder (CloudStorageConfig$AvroConfig/newBuilder)]
    (when (some? (get arg :useTopicSchema))
      (.setUseTopicSchema builder (get arg :useTopicSchema)))
    (when (some? (get arg :writeMetadata))
      (.setWriteMetadata builder (get arg :writeMetadata)))
    (.build builder)))

(defn AvroConfig-to-edn
  [^CloudStorageConfig$AvroConfig arg]
  (when arg
    (cond-> {}
      (.getUseTopicSchema arg) (assoc :useTopicSchema (.getUseTopicSchema arg))
      (.getWriteMetadata arg) (assoc :writeMetadata (.getWriteMetadata arg)))))

(def AvroConfig-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nConfiguration for writing message data in Avro format.\nMessage payloads and metadata will be written to files as an Avro binary.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.CloudStorageConfig.AvroConfig}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.pubsub.v1/CloudStorageConfig.AvroConfig}
   [:useTopicSchema
    {:optional true,
     :getter-doc
       "<pre>\nOptional. When true, the output Cloud Storage file will be serialized\nusing the topic schema, if it exists.\n</pre>\n\n<code>bool use_topic_schema = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The useTopicSchema.",
     :setter-doc
       "<pre>\nOptional. When true, the output Cloud Storage file will be serialized\nusing the topic schema, if it exists.\n</pre>\n\n<code>bool use_topic_schema = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The useTopicSchema to set.\n@return This builder for chaining."}
    :boolean]
   [:writeMetadata
    {:optional true,
     :getter-doc
       "<pre>\nOptional. When true, write the subscription name, message_id,\npublish_time, attributes, and ordering_key as additional fields in the\noutput. The subscription name, message_id, and publish_time fields are\nput in their own fields while all other message properties other than\ndata (for example, an ordering_key, if present) are added as entries in\nthe attributes map.\n</pre>\n\n<code>bool write_metadata = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The writeMetadata.",
     :setter-doc
       "<pre>\nOptional. When true, write the subscription name, message_id,\npublish_time, attributes, and ordering_key as additional fields in the\noutput. The subscription name, message_id, and publish_time fields are\nput in their own fields while all other message properties other than\ndata (for example, an ordering_key, if present) are added as entries in\nthe attributes map.\n</pre>\n\n<code>bool write_metadata = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The writeMetadata to set.\n@return This builder for chaining."}
    :boolean]])

(def OutputFormatCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.pubsub.v1/CloudStorageConfig.OutputFormatCase} "TEXT_CONFIG"
   "AVRO_CONFIG" "OUTPUTFORMAT_NOT_SET"])

(defn ^CloudStorageConfig from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/CloudStorageConfig arg)
  (let [builder (CloudStorageConfig/newBuilder)]
    (when (some? (get arg :bucket)) (.setBucket builder (get arg :bucket)))
    (when (some? (get arg :filenameDatetimeFormat))
      (.setFilenameDatetimeFormat builder (get arg :filenameDatetimeFormat)))
    (when (some? (get arg :filenamePrefix))
      (.setFilenamePrefix builder (get arg :filenamePrefix)))
    (when (some? (get arg :filenameSuffix))
      (.setFilenameSuffix builder (get arg :filenameSuffix)))
    (when (some? (get arg :maxBytes))
      (.setMaxBytes builder (long (get arg :maxBytes))))
    (when (some? (get arg :maxDuration))
      (.setMaxDuration builder
                       (protobuf/Duration-from-edn (get arg :maxDuration))))
    (when (some? (get arg :maxMessages))
      (.setMaxMessages builder (long (get arg :maxMessages))))
    (when (some? (get arg :serviceAccountEmail))
      (.setServiceAccountEmail builder (get arg :serviceAccountEmail)))
    (when (some? (get arg :state))
      (.setState builder (CloudStorageConfig$State/valueOf (get arg :state))))
    (cond (contains? arg :avroConfig)
            (.setAvroConfig builder (AvroConfig-from-edn (get arg :avroConfig)))
          (contains? arg :textConfig) (.setTextConfig builder
                                                      (TextConfig-from-edn
                                                        (get arg :textConfig))))
    (.build builder)))

(defn to-edn
  [^CloudStorageConfig arg]
  {:post [(global/strict! :gcp.pubsub.v1/CloudStorageConfig %)]}
  (when arg
    (let [res (cond-> {:bucket (.getBucket arg)}
                (some->> (.getFilenameDatetimeFormat arg)
                         (not= ""))
                  (assoc :filenameDatetimeFormat
                    (.getFilenameDatetimeFormat arg))
                (some->> (.getFilenamePrefix arg)
                         (not= ""))
                  (assoc :filenamePrefix (.getFilenamePrefix arg))
                (some->> (.getFilenameSuffix arg)
                         (not= ""))
                  (assoc :filenameSuffix (.getFilenameSuffix arg))
                (.getMaxBytes arg) (assoc :maxBytes (.getMaxBytes arg))
                (.hasMaxDuration arg) (assoc :maxDuration
                                        (protobuf/Duration-to-edn
                                          (.getMaxDuration arg)))
                (.getMaxMessages arg) (assoc :maxMessages (.getMaxMessages arg))
                (some->> (.getServiceAccountEmail arg)
                         (not= ""))
                  (assoc :serviceAccountEmail (.getServiceAccountEmail arg))
                (.getState arg) (assoc :state (.name (.getState arg))))
          res
            (case (.name (.getOutputFormatCase arg))
              "AVRO_CONFIG"
                (assoc res :avroConfig (AvroConfig-to-edn (.getAvroConfig arg)))
              "TEXT_CONFIG"
                (assoc res :textConfig (TextConfig-to-edn (.getTextConfig arg)))
              res)]
      res)))

(def schema
  [:and
   {:closed true,
    :doc
      "<pre>\nConfiguration for a Cloud Storage subscription.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.CloudStorageConfig}",
    :gcp/category :union-protobuf-oneof,
    :gcp/key :gcp.pubsub.v1/CloudStorageConfig}
   [:map {:closed true}
    [:avroConfig
     {:optional true,
      :getter-doc
        "<pre>\nOptional. If set, message data will be written to Cloud Storage in Avro\nformat.\n</pre>\n\n<code>\n.google.pubsub.v1.CloudStorageConfig.AvroConfig avro_config = 5 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The avroConfig.",
      :setter-doc
        "<pre>\nOptional. If set, message data will be written to Cloud Storage in Avro\nformat.\n</pre>\n\n<code>\n.google.pubsub.v1.CloudStorageConfig.AvroConfig avro_config = 5 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:ref :gcp.pubsub.v1/CloudStorageConfig.AvroConfig]]
    [:bucket
     {:getter-doc
        "<pre>\nRequired. User-provided name for the Cloud Storage bucket.\nThe bucket must be created by the user. The bucket name must be without\nany prefix like \"gs://\". See the [bucket naming\nrequirements] (https://cloud.google.com/storage/docs/buckets#naming).\n</pre>\n\n<code>string bucket = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The bucket.",
      :setter-doc
        "<pre>\nRequired. User-provided name for the Cloud Storage bucket.\nThe bucket must be created by the user. The bucket name must be without\nany prefix like \"gs://\". See the [bucket naming\nrequirements] (https://cloud.google.com/storage/docs/buckets#naming).\n</pre>\n\n<code>string bucket = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The bucket to set.\n@return This builder for chaining."}
     [:string {:min 1, :gen/max 1}]]
    [:filenameDatetimeFormat
     {:optional true,
      :getter-doc
        "<pre>\nOptional. User-provided format string specifying how to represent datetimes\nin Cloud Storage filenames. See the [datetime format\nguidance](https://cloud.google.com/pubsub/docs/create-cloudstorage-subscription#file_names).\n</pre>\n\n<code>string filename_datetime_format = 10 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The filenameDatetimeFormat.",
      :setter-doc
        "<pre>\nOptional. User-provided format string specifying how to represent datetimes\nin Cloud Storage filenames. See the [datetime format\nguidance](https://cloud.google.com/pubsub/docs/create-cloudstorage-subscription#file_names).\n</pre>\n\n<code>string filename_datetime_format = 10 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The filenameDatetimeFormat to set.\n@return This builder for chaining."}
     [:string {:min 1, :gen/max 1}]]
    [:filenamePrefix
     {:optional true,
      :getter-doc
        "<pre>\nOptional. User-provided prefix for Cloud Storage filename. See the [object\nnaming requirements](https://cloud.google.com/storage/docs/objects#naming).\n</pre>\n\n<code>string filename_prefix = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The filenamePrefix.",
      :setter-doc
        "<pre>\nOptional. User-provided prefix for Cloud Storage filename. See the [object\nnaming requirements](https://cloud.google.com/storage/docs/objects#naming).\n</pre>\n\n<code>string filename_prefix = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The filenamePrefix to set.\n@return This builder for chaining."}
     [:string {:min 1, :gen/max 1}]]
    [:filenameSuffix
     {:optional true,
      :getter-doc
        "<pre>\nOptional. User-provided suffix for Cloud Storage filename. See the [object\nnaming requirements](https://cloud.google.com/storage/docs/objects#naming).\nMust not end in \"/\".\n</pre>\n\n<code>string filename_suffix = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The filenameSuffix.",
      :setter-doc
        "<pre>\nOptional. User-provided suffix for Cloud Storage filename. See the [object\nnaming requirements](https://cloud.google.com/storage/docs/objects#naming).\nMust not end in \"/\".\n</pre>\n\n<code>string filename_suffix = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The filenameSuffix to set.\n@return This builder for chaining."}
     [:string {:min 1, :gen/max 1}]]
    [:maxBytes
     {:optional true,
      :getter-doc
        "<pre>\nOptional. The maximum bytes that can be written to a Cloud Storage file\nbefore a new file is created. Min 1 KB, max 10 GiB. The max_bytes limit may\nbe exceeded in cases where messages are larger than the limit.\n</pre>\n\n<code>int64 max_bytes = 7 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The maxBytes.",
      :setter-doc
        "<pre>\nOptional. The maximum bytes that can be written to a Cloud Storage file\nbefore a new file is created. Min 1 KB, max 10 GiB. The max_bytes limit may\nbe exceeded in cases where messages are larger than the limit.\n</pre>\n\n<code>int64 max_bytes = 7 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The maxBytes to set.\n@return This builder for chaining."}
     :i64]
    [:maxDuration
     {:optional true,
      :getter-doc
        "<pre>\nOptional. The maximum duration that can elapse before a new Cloud Storage\nfile is created. Min 1 minute, max 10 minutes, default 5 minutes. May not\nexceed the subscription's acknowledgment deadline.\n</pre>\n\n<code>.google.protobuf.Duration max_duration = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The maxDuration.",
      :setter-doc
        "<pre>\nOptional. The maximum duration that can elapse before a new Cloud Storage\nfile is created. Min 1 minute, max 10 minutes, default 5 minutes. May not\nexceed the subscription's acknowledgment deadline.\n</pre>\n\n<code>.google.protobuf.Duration max_duration = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     :gcp.foreign.com.google.protobuf/Duration]
    [:maxMessages
     {:optional true,
      :getter-doc
        "<pre>\nOptional. The maximum number of messages that can be written to a Cloud\nStorage file before a new file is created. Min 1000 messages.\n</pre>\n\n<code>int64 max_messages = 8 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The maxMessages.",
      :setter-doc
        "<pre>\nOptional. The maximum number of messages that can be written to a Cloud\nStorage file before a new file is created. Min 1000 messages.\n</pre>\n\n<code>int64 max_messages = 8 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The maxMessages to set.\n@return This builder for chaining."}
     :i64]
    [:serviceAccountEmail
     {:optional true,
      :getter-doc
        "<pre>\nOptional. The service account to use to write to Cloud Storage. The\nsubscription creator or updater that specifies this field must have\n`iam.serviceAccounts.actAs` permission on the service account. If not\nspecified, the Pub/Sub\n[service agent](https://cloud.google.com/iam/docs/service-agents),\nservice-{project_number}&#64;gcp-sa-pubsub.iam.gserviceaccount.com, is used.\n</pre>\n\n<code>string service_account_email = 11 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The serviceAccountEmail.",
      :setter-doc
        "<pre>\nOptional. The service account to use to write to Cloud Storage. The\nsubscription creator or updater that specifies this field must have\n`iam.serviceAccounts.actAs` permission on the service account. If not\nspecified, the Pub/Sub\n[service agent](https://cloud.google.com/iam/docs/service-agents),\nservice-{project_number}&#64;gcp-sa-pubsub.iam.gserviceaccount.com, is used.\n</pre>\n\n<code>string service_account_email = 11 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The serviceAccountEmail to set.\n@return This builder for chaining."}
     [:string {:min 1, :gen/max 1}]]
    [:state
     {:optional true,
      :read-only true,
      :getter-doc
        "<pre>\nOutput only. An output-only field that indicates whether or not the\nsubscription can receive messages.\n</pre>\n\n<code>\n.google.pubsub.v1.CloudStorageConfig.State state = 9 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The state."}
     [:enum {:closed true} "STATE_UNSPECIFIED" "ACTIVE" "PERMISSION_DENIED"
      "NOT_FOUND" "IN_TRANSIT_LOCATION_RESTRICTION" "SCHEMA_MISMATCH"
      "VERTEX_AI_LOCATION_RESTRICTION"]]
    [:textConfig
     {:optional true,
      :getter-doc
        "<pre>\nOptional. If set, message data will be written to Cloud Storage in text\nformat.\n</pre>\n\n<code>\n.google.pubsub.v1.CloudStorageConfig.TextConfig text_config = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The textConfig.",
      :setter-doc
        "<pre>\nOptional. If set, message data will be written to Cloud Storage in text\nformat.\n</pre>\n\n<code>\n.google.pubsub.v1.CloudStorageConfig.TextConfig text_config = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:ref :gcp.pubsub.v1/CloudStorageConfig.TextConfig]]]
   [:fn
    {:error/message
       "Only one of these keys may be present: #{:avroConfig :textConfig}"}
    (quote (fn [m]
             (<= (count (filter (set (keys m)) #{:avroConfig :textConfig}))
                 1)))]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/CloudStorageConfig schema,
              :gcp.pubsub.v1/CloudStorageConfig.AvroConfig AvroConfig-schema,
              :gcp.pubsub.v1/CloudStorageConfig.OutputFormatCase
                OutputFormatCase-schema,
              :gcp.pubsub.v1/CloudStorageConfig.State State-schema,
              :gcp.pubsub.v1/CloudStorageConfig.TextConfig TextConfig-schema}
    {:gcp.global/name "gcp.pubsub.v1.CloudStorageConfig"}))