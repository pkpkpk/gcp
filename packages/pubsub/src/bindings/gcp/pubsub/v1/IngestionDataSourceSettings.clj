;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.IngestionDataSourceSettings
  {:doc "<pre>\nSettings for an ingestion data source on a topic.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.IngestionDataSourceSettings}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.IngestionDataSourceSettings"
   :gcp.dev/certification {:base-seed 1777050374609
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777050374609 :standard 1777050374610 :stress 1777050374611}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-24T17:06:16.069901167Z"}}
  (:require
   [gcp.foreign.com.google.protobuf :as protobuf]
   [gcp.global :as global]
   [gcp.pubsub.v1.PlatformLogsSettings :as PlatformLogsSettings])
  (:import
   (com.google.protobuf Timestamp)
   (com.google.pubsub.v1 IngestionDataSourceSettings IngestionDataSourceSettings$AwsKinesis IngestionDataSourceSettings$AwsKinesis$Builder IngestionDataSourceSettings$AwsKinesis$State IngestionDataSourceSettings$AwsMsk IngestionDataSourceSettings$AwsMsk$Builder IngestionDataSourceSettings$AwsMsk$State IngestionDataSourceSettings$AzureEventHubs IngestionDataSourceSettings$AzureEventHubs$Builder IngestionDataSourceSettings$AzureEventHubs$State IngestionDataSourceSettings$Builder IngestionDataSourceSettings$CloudStorage IngestionDataSourceSettings$CloudStorage$AvroFormat IngestionDataSourceSettings$CloudStorage$AvroFormat$Builder IngestionDataSourceSettings$CloudStorage$Builder IngestionDataSourceSettings$CloudStorage$InputFormatCase IngestionDataSourceSettings$CloudStorage$PubSubAvroFormat IngestionDataSourceSettings$CloudStorage$PubSubAvroFormat$Builder IngestionDataSourceSettings$CloudStorage$State IngestionDataSourceSettings$CloudStorage$TextFormat IngestionDataSourceSettings$CloudStorage$TextFormat$Builder IngestionDataSourceSettings$ConfluentCloud IngestionDataSourceSettings$ConfluentCloud$Builder IngestionDataSourceSettings$ConfluentCloud$State IngestionDataSourceSettings$SourceCase)))

(declare
  from-edn
  to-edn
  AwsKinesis$State-from-edn
  AwsKinesis$State-to-edn
  AwsKinesis-from-edn
  AwsKinesis-to-edn
  AwsKinesis$State-from-edn
  AwsKinesis$State-to-edn
  CloudStorage$State-from-edn
  CloudStorage$State-to-edn
  CloudStorage$TextFormat-from-edn
  CloudStorage$TextFormat-to-edn
  CloudStorage$AvroFormat-from-edn
  CloudStorage$AvroFormat-to-edn
  CloudStorage$PubSubAvroFormat-from-edn
  CloudStorage$PubSubAvroFormat-to-edn
  CloudStorage$InputFormatCase-from-edn
  CloudStorage$InputFormatCase-to-edn
  CloudStorage-from-edn
  CloudStorage-to-edn
  CloudStorage$State-from-edn
  CloudStorage$State-to-edn
  CloudStorage$TextFormat-from-edn
  CloudStorage$TextFormat-to-edn
  CloudStorage$AvroFormat-from-edn
  CloudStorage$AvroFormat-to-edn
  CloudStorage$PubSubAvroFormat-from-edn
  CloudStorage$PubSubAvroFormat-to-edn
  CloudStorage$InputFormatCase-from-edn
  CloudStorage$InputFormatCase-to-edn
  AzureEventHubs$State-from-edn
  AzureEventHubs$State-to-edn
  AzureEventHubs-from-edn
  AzureEventHubs-to-edn
  AzureEventHubs$State-from-edn
  AzureEventHubs$State-to-edn
  AwsMsk$State-from-edn
  AwsMsk$State-to-edn
  AwsMsk-from-edn
  AwsMsk-to-edn
  AwsMsk$State-from-edn
  AwsMsk$State-to-edn
  ConfluentCloud$State-from-edn
  ConfluentCloud$State-to-edn
  ConfluentCloud-from-edn
  ConfluentCloud-to-edn
  ConfluentCloud$State-from-edn
  ConfluentCloud$State-to-edn
  SourceCase-from-edn
  SourceCase-to-edn)

(def AwsKinesis$State-schema
  [:enum
   {:closed true
    :doc
    "<pre>\nPossible states for ingestion from Amazon Kinesis Data Streams.\n</pre>\n\nProtobuf enum {@code google.pubsub.v1.IngestionDataSourceSettings.AwsKinesis.State}"
    :gcp/category :nested/enum
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.AwsKinesis.State}
   "STATE_UNSPECIFIED" "ACTIVE" "KINESIS_PERMISSION_DENIED"
   "PUBLISH_PERMISSION_DENIED" "STREAM_NOT_FOUND" "CONSUMER_NOT_FOUND"])

(def AwsKinesis$State-schema
  [:enum
   {:closed true
    :doc
    "<pre>\nPossible states for ingestion from Amazon Kinesis Data Streams.\n</pre>\n\nProtobuf enum {@code google.pubsub.v1.IngestionDataSourceSettings.AwsKinesis.State}"
    :gcp/category :nested/enum
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.AwsKinesis.State}
   "STATE_UNSPECIFIED" "ACTIVE" "KINESIS_PERMISSION_DENIED"
   "PUBLISH_PERMISSION_DENIED" "STREAM_NOT_FOUND" "CONSUMER_NOT_FOUND"])

(defn ^IngestionDataSourceSettings$AwsKinesis AwsKinesis-from-edn
  [arg]
  (let [builder (IngestionDataSourceSettings$AwsKinesis/newBuilder)]
    (when (some? (get arg :awsRoleArn))
      (.setAwsRoleArn builder (get arg :awsRoleArn)))
    (when (some? (get arg :consumerArn))
      (.setConsumerArn builder (get arg :consumerArn)))
    (when (some? (get arg :gcpServiceAccount))
      (.setGcpServiceAccount builder (get arg :gcpServiceAccount)))
    (when (some? (get arg :state))
      (.setState builder
                 (IngestionDataSourceSettings$AwsKinesis$State/valueOf
                   (get arg :state))))
    (when (some? (get arg :streamArn))
      (.setStreamArn builder (get arg :streamArn)))
    (.build builder)))

(defn AwsKinesis-to-edn
  [^IngestionDataSourceSettings$AwsKinesis arg]
  (when arg
    (cond-> {:awsRoleArn (.getAwsRoleArn arg)
             :consumerArn (.getConsumerArn arg)
             :gcpServiceAccount (.getGcpServiceAccount arg)
             :streamArn (.getStreamArn arg)}
      (.getState arg) (assoc :state (.name (.getState arg))))))

(def AwsKinesis-schema
  [:map
   {:closed true
    :doc
    "<pre>\nIngestion settings for Amazon Kinesis Data Streams.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.IngestionDataSourceSettings.AwsKinesis}"
    :gcp/category :nested/protobuf-message
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.AwsKinesis}
   [:awsRoleArn
    {:getter-doc
     "<pre>\nRequired. AWS role ARN to be used for Federated Identity authentication\nwith Kinesis. Check the Pub/Sub docs for how to set up this role and the\nrequired permissions that need to be attached to it.\n</pre>\n\n<code>string aws_role_arn = 4 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The awsRoleArn."
     :setter-doc
     "<pre>\nRequired. AWS role ARN to be used for Federated Identity authentication\nwith Kinesis. Check the Pub/Sub docs for how to set up this role and the\nrequired permissions that need to be attached to it.\n</pre>\n\n<code>string aws_role_arn = 4 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The awsRoleArn to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:consumerArn
    {:getter-doc
     "<pre>\nRequired. The Kinesis consumer ARN to used for ingestion in Enhanced\nFan-Out mode. The consumer must be already created and ready to be used.\n</pre>\n\n<code>string consumer_arn = 3 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The consumerArn."
     :setter-doc
     "<pre>\nRequired. The Kinesis consumer ARN to used for ingestion in Enhanced\nFan-Out mode. The consumer must be already created and ready to be used.\n</pre>\n\n<code>string consumer_arn = 3 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The consumerArn to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:gcpServiceAccount
    {:getter-doc
     "<pre>\nRequired. The GCP service account to be used for Federated Identity\nauthentication with Kinesis (via a `AssumeRoleWithWebIdentity` call for\nthe provided role). The `aws_role_arn` must be set up with\n`accounts.google.com:sub` equals to this service account number.\n</pre>\n\n<code>string gcp_service_account = 5 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The gcpServiceAccount."
     :setter-doc
     "<pre>\nRequired. The GCP service account to be used for Federated Identity\nauthentication with Kinesis (via a `AssumeRoleWithWebIdentity` call for\nthe provided role). The `aws_role_arn` must be set up with\n`accounts.google.com:sub` equals to this service account number.\n</pre>\n\n<code>string gcp_service_account = 5 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The gcpServiceAccount to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:state
    {:optional true
     :read-only true
     :getter-doc
     "<pre>\nOutput only. An output-only field that indicates the state of the Kinesis\ningestion source.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.AwsKinesis.State state = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The state."}
    [:enum {:closed true} "STATE_UNSPECIFIED" "ACTIVE"
     "KINESIS_PERMISSION_DENIED" "PUBLISH_PERMISSION_DENIED" "STREAM_NOT_FOUND"
     "CONSUMER_NOT_FOUND"]]
   [:streamArn
    {:getter-doc
     "<pre>\nRequired. The Kinesis stream ARN to ingest data from.\n</pre>\n\n<code>string stream_arn = 2 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The streamArn."
     :setter-doc
     "<pre>\nRequired. The Kinesis stream ARN to ingest data from.\n</pre>\n\n<code>string stream_arn = 2 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The streamArn to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(def CloudStorage$State-schema
  [:enum
   {:closed true
    :doc
    "<pre>\nPossible states for ingestion from Cloud Storage.\n</pre>\n\nProtobuf enum {@code google.pubsub.v1.IngestionDataSourceSettings.CloudStorage.State}"
    :gcp/category :nested/enum
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.State}
   "STATE_UNSPECIFIED" "ACTIVE" "CLOUD_STORAGE_PERMISSION_DENIED"
   "PUBLISH_PERMISSION_DENIED" "BUCKET_NOT_FOUND" "TOO_MANY_OBJECTS"])

(defn
  ^IngestionDataSourceSettings$CloudStorage$TextFormat CloudStorage$TextFormat-from-edn
  [arg]
  (let [builder
        (IngestionDataSourceSettings$CloudStorage$TextFormat/newBuilder)]
    (when (some? (get arg :delimiter))
      (.setDelimiter builder (get arg :delimiter)))
    (.build builder)))

(defn
  CloudStorage$TextFormat-to-edn
  [^IngestionDataSourceSettings$CloudStorage$TextFormat arg]
  (when arg
    (cond-> {} (.hasDelimiter arg) (assoc :delimiter (.getDelimiter arg)))))

(def CloudStorage$TextFormat-schema
  [:map
   {:closed true
    :doc
    "<pre>\nConfiguration for reading Cloud Storage data in text format. Each line of\ntext as specified by the delimiter will be set to the `data` field of a\nPub/Sub message.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.IngestionDataSourceSettings.CloudStorage.TextFormat}"
    :gcp/category :nested/protobuf-message
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.TextFormat}
   [:delimiter
    {:optional true
     :getter-doc
     "<pre>\nOptional. When unset, '&#92;n' is used.\n</pre>\n\n<code>optional string delimiter = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The delimiter."
     :setter-doc
     "<pre>\nOptional. When unset, '&#92;n' is used.\n</pre>\n\n<code>optional string delimiter = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The delimiter to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(defn
  ^IngestionDataSourceSettings$CloudStorage$AvroFormat CloudStorage$AvroFormat-from-edn
  [arg]
  (let [builder
        (IngestionDataSourceSettings$CloudStorage$AvroFormat/newBuilder)]
    (.build builder)))

(defn
  CloudStorage$AvroFormat-to-edn
  [^IngestionDataSourceSettings$CloudStorage$AvroFormat arg]
  (when arg (cond-> {})))

(def CloudStorage$AvroFormat-schema
  [:map
   {:closed true
    :doc
    "<pre>\nConfiguration for reading Cloud Storage data in Avro binary format. The\nbytes of each object will be set to the `data` field of a Pub/Sub\nmessage.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.IngestionDataSourceSettings.CloudStorage.AvroFormat}"
    :gcp/category :nested/protobuf-message
    :gcp/key
    :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.AvroFormat}])

(defn
  ^IngestionDataSourceSettings$CloudStorage$PubSubAvroFormat CloudStorage$PubSubAvroFormat-from-edn
  [arg]
  (let
    [builder
     (IngestionDataSourceSettings$CloudStorage$PubSubAvroFormat/newBuilder)]
    (.build builder)))

(defn
  CloudStorage$PubSubAvroFormat-to-edn
  [^IngestionDataSourceSettings$CloudStorage$PubSubAvroFormat arg]
  (when arg (cond-> {})))

(def CloudStorage$PubSubAvroFormat-schema
  [:map
   {:closed true
    :doc
    "<pre>\nConfiguration for reading Cloud Storage data written via [Cloud Storage\nsubscriptions](https://cloud.google.com/pubsub/docs/cloudstorage). The\ndata and attributes fields of the originally exported Pub/Sub message\nwill be restored when publishing.\n</pre>\n\nProtobuf type {@code\ngoogle.pubsub.v1.IngestionDataSourceSettings.CloudStorage.PubSubAvroFormat}"
    :gcp/category :nested/protobuf-message
    :gcp/key
    :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.PubSubAvroFormat}])

(def CloudStorage$InputFormatCase-schema
  [:enum
   {:closed true
    :doc nil
    :gcp/category :nested/enum
    :gcp/key
    :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.InputFormatCase}
   "TEXT_FORMAT" "AVRO_FORMAT" "PUBSUB_AVRO_FORMAT" "INPUTFORMAT_NOT_SET"])

(def CloudStorage$State-schema
  [:enum
   {:closed true
    :doc
    "<pre>\nPossible states for ingestion from Cloud Storage.\n</pre>\n\nProtobuf enum {@code google.pubsub.v1.IngestionDataSourceSettings.CloudStorage.State}"
    :gcp/category :nested/enum
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.State}
   "STATE_UNSPECIFIED" "ACTIVE" "CLOUD_STORAGE_PERMISSION_DENIED"
   "PUBLISH_PERMISSION_DENIED" "BUCKET_NOT_FOUND" "TOO_MANY_OBJECTS"])

(defn
  ^IngestionDataSourceSettings$CloudStorage$TextFormat CloudStorage$TextFormat-from-edn
  [arg]
  (let [builder
        (IngestionDataSourceSettings$CloudStorage$TextFormat/newBuilder)]
    (when (some? (get arg :delimiter))
      (.setDelimiter builder (get arg :delimiter)))
    (.build builder)))

(defn
  CloudStorage$TextFormat-to-edn
  [^IngestionDataSourceSettings$CloudStorage$TextFormat arg]
  (when arg
    (cond-> {} (.hasDelimiter arg) (assoc :delimiter (.getDelimiter arg)))))

(def CloudStorage$TextFormat-schema
  [:map
   {:closed true
    :doc
    "<pre>\nConfiguration for reading Cloud Storage data in text format. Each line of\ntext as specified by the delimiter will be set to the `data` field of a\nPub/Sub message.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.IngestionDataSourceSettings.CloudStorage.TextFormat}"
    :gcp/category :nested/protobuf-message
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.TextFormat}
   [:delimiter
    {:optional true
     :getter-doc
     "<pre>\nOptional. When unset, '&#92;n' is used.\n</pre>\n\n<code>optional string delimiter = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The delimiter."
     :setter-doc
     "<pre>\nOptional. When unset, '&#92;n' is used.\n</pre>\n\n<code>optional string delimiter = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The delimiter to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(defn
  ^IngestionDataSourceSettings$CloudStorage$AvroFormat CloudStorage$AvroFormat-from-edn
  [arg]
  (let [builder
        (IngestionDataSourceSettings$CloudStorage$AvroFormat/newBuilder)]
    (.build builder)))

(defn
  CloudStorage$AvroFormat-to-edn
  [^IngestionDataSourceSettings$CloudStorage$AvroFormat arg]
  (when arg (cond-> {})))

(def CloudStorage$AvroFormat-schema
  [:map
   {:closed true
    :doc
    "<pre>\nConfiguration for reading Cloud Storage data in Avro binary format. The\nbytes of each object will be set to the `data` field of a Pub/Sub\nmessage.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.IngestionDataSourceSettings.CloudStorage.AvroFormat}"
    :gcp/category :nested/protobuf-message
    :gcp/key
    :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.AvroFormat}])

(defn
  ^IngestionDataSourceSettings$CloudStorage$PubSubAvroFormat CloudStorage$PubSubAvroFormat-from-edn
  [arg]
  (let
    [builder
     (IngestionDataSourceSettings$CloudStorage$PubSubAvroFormat/newBuilder)]
    (.build builder)))

(defn
  CloudStorage$PubSubAvroFormat-to-edn
  [^IngestionDataSourceSettings$CloudStorage$PubSubAvroFormat arg]
  (when arg (cond-> {})))

(def CloudStorage$PubSubAvroFormat-schema
  [:map
   {:closed true
    :doc
    "<pre>\nConfiguration for reading Cloud Storage data written via [Cloud Storage\nsubscriptions](https://cloud.google.com/pubsub/docs/cloudstorage). The\ndata and attributes fields of the originally exported Pub/Sub message\nwill be restored when publishing.\n</pre>\n\nProtobuf type {@code\ngoogle.pubsub.v1.IngestionDataSourceSettings.CloudStorage.PubSubAvroFormat}"
    :gcp/category :nested/protobuf-message
    :gcp/key
    :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.PubSubAvroFormat}])

(def CloudStorage$InputFormatCase-schema
  [:enum
   {:closed true
    :doc nil
    :gcp/category :nested/enum
    :gcp/key
    :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.InputFormatCase}
   "TEXT_FORMAT" "AVRO_FORMAT" "PUBSUB_AVRO_FORMAT" "INPUTFORMAT_NOT_SET"])

(defn ^IngestionDataSourceSettings$CloudStorage CloudStorage-from-edn
  [arg]
  (let [builder (IngestionDataSourceSettings$CloudStorage/newBuilder)]
    (when (some? (get arg :bucket)) (.setBucket builder (get arg :bucket)))
    (when (some? (get arg :matchGlob))
      (.setMatchGlob builder (get arg :matchGlob)))
    (when (some? (get arg :minimumObjectCreateTime))
      (.setMinimumObjectCreateTime builder
                                   (protobuf/Timestamp-from-edn
                                     (get arg :minimumObjectCreateTime))))
    (when (some? (get arg :state))
      (.setState builder
                 (IngestionDataSourceSettings$CloudStorage$State/valueOf
                   (get arg :state))))
    (cond (contains? arg :avroFormat) (.setAvroFormat
                                        builder
                                        (CloudStorage$AvroFormat-from-edn
                                          (get arg :avroFormat)))
          (contains? arg :pubsubAvroFormat)
          (.setPubsubAvroFormat builder
                                (CloudStorage$PubSubAvroFormat-from-edn
                                  (get arg :pubsubAvroFormat)))
          (contains? arg :textFormat) (.setTextFormat
                                        builder
                                        (CloudStorage$TextFormat-from-edn
                                          (get arg :textFormat))))
    (.build builder)))

(defn CloudStorage-to-edn
  [^IngestionDataSourceSettings$CloudStorage arg]
  (when arg
    (let [res (cond-> {}
                (some->> (.getBucket arg)
                         (not= ""))
                (assoc :bucket (.getBucket arg))
                (some->> (.getMatchGlob arg)
                         (not= ""))
                (assoc :matchGlob (.getMatchGlob arg))
                (.hasMinimumObjectCreateTime arg)
                (assoc :minimumObjectCreateTime
                  (protobuf/Timestamp-to-edn (.getMinimumObjectCreateTime
                                               arg)))
                (.getState arg) (assoc :state (.name (.getState arg))))
          res (case (.name (.getInputFormatCase arg))
                "AVRO_FORMAT" (assoc res
                                :avroFormat (CloudStorage$AvroFormat-to-edn
                                              (.getAvroFormat arg)))
                "PUBSUB_AVRO_FORMAT" (assoc res
                                       :pubsubAvroFormat
                                       (CloudStorage$PubSubAvroFormat-to-edn
                                         (.getPubsubAvroFormat arg)))
                "TEXT_FORMAT" (assoc res
                                :textFormat (CloudStorage$TextFormat-to-edn
                                              (.getTextFormat arg)))
                res)]
      res)))

(def CloudStorage-schema
  [:and
   {:closed true
    :doc
    "<pre>\nIngestion settings for Cloud Storage.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.IngestionDataSourceSettings.CloudStorage}"
    :gcp/category :nested/union-protobuf-oneof
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage}
   [:map {:closed true}
    [:avroFormat
     {:optional true
      :getter-doc
      "<pre>\nOptional. Data from Cloud Storage will be interpreted in Avro format.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.CloudStorage.AvroFormat avro_format = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The avroFormat."
      :setter-doc
      "<pre>\nOptional. Data from Cloud Storage will be interpreted in Avro format.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.CloudStorage.AvroFormat avro_format = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:ref :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.AvroFormat]]
    [:bucket
     {:optional true
      :getter-doc
      "<pre>\nOptional. Cloud Storage bucket. The bucket name must be without any\nprefix like \"gs://\". See the [bucket naming requirements]\n(https://cloud.google.com/storage/docs/buckets#naming).\n</pre>\n\n<code>string bucket = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The bucket."
      :setter-doc
      "<pre>\nOptional. Cloud Storage bucket. The bucket name must be without any\nprefix like \"gs://\". See the [bucket naming requirements]\n(https://cloud.google.com/storage/docs/buckets#naming).\n</pre>\n\n<code>string bucket = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The bucket to set.\n@return This builder for chaining."}
     [:string {:min 1, :gen/max 1}]]
    [:matchGlob
     {:optional true
      :getter-doc
      "<pre>\nOptional. Glob pattern used to match objects that will be ingested. If\nunset, all objects will be ingested. See the [supported\npatterns](https://cloud.google.com/storage/docs/json_api/v1/objects/list#list-objects-and-prefixes-using-glob).\n</pre>\n\n<code>string match_glob = 9 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The matchGlob."
      :setter-doc
      "<pre>\nOptional. Glob pattern used to match objects that will be ingested. If\nunset, all objects will be ingested. See the [supported\npatterns](https://cloud.google.com/storage/docs/json_api/v1/objects/list#list-objects-and-prefixes-using-glob).\n</pre>\n\n<code>string match_glob = 9 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The matchGlob to set.\n@return This builder for chaining."}
     [:string {:min 1, :gen/max 1}]]
    [:minimumObjectCreateTime
     {:optional true
      :getter-doc
      "<pre>\nOptional. Only objects with a larger or equal creation timestamp will be\ningested.\n</pre>\n\n<code>\n.google.protobuf.Timestamp minimum_object_create_time = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The minimumObjectCreateTime."
      :setter-doc
      "<pre>\nOptional. Only objects with a larger or equal creation timestamp will be\ningested.\n</pre>\n\n<code>\n.google.protobuf.Timestamp minimum_object_create_time = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     :gcp.foreign.com.google.protobuf/Timestamp]
    [:pubsubAvroFormat
     {:optional true
      :getter-doc
      "<pre>\nOptional. It will be assumed data from Cloud Storage was written via\n[Cloud Storage\nsubscriptions](https://cloud.google.com/pubsub/docs/cloudstorage).\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.CloudStorage.PubSubAvroFormat pubsub_avro_format = 5 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The pubsubAvroFormat."
      :setter-doc
      "<pre>\nOptional. It will be assumed data from Cloud Storage was written via\n[Cloud Storage\nsubscriptions](https://cloud.google.com/pubsub/docs/cloudstorage).\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.CloudStorage.PubSubAvroFormat pubsub_avro_format = 5 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:ref
      :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.PubSubAvroFormat]]
    [:state
     {:optional true
      :read-only true
      :getter-doc
      "<pre>\nOutput only. An output-only field that indicates the state of the Cloud\nStorage ingestion source.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.CloudStorage.State state = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The state."}
     [:enum {:closed true} "STATE_UNSPECIFIED" "ACTIVE"
      "CLOUD_STORAGE_PERMISSION_DENIED" "PUBLISH_PERMISSION_DENIED"
      "BUCKET_NOT_FOUND" "TOO_MANY_OBJECTS"]]
    [:textFormat
     {:optional true
      :getter-doc
      "<pre>\nOptional. Data from Cloud Storage will be interpreted as text.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.CloudStorage.TextFormat text_format = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The textFormat."
      :setter-doc
      "<pre>\nOptional. Data from Cloud Storage will be interpreted as text.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.CloudStorage.TextFormat text_format = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:ref :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.TextFormat]]]
   [:fn
    {:error/message
     "Only one of these keys may be present: #{:pubsubAvroFormat :textFormat :avroFormat}"}
    (quote (fn [m]
             (<= (count (filter (set (keys m))
                          #{:pubsubAvroFormat :textFormat :avroFormat}))
                 1)))]])

(def AzureEventHubs$State-schema
  [:enum
   {:closed true
    :doc
    "<pre>\nPossible states for managed ingestion from Event Hubs.\n</pre>\n\nProtobuf enum {@code google.pubsub.v1.IngestionDataSourceSettings.AzureEventHubs.State}"
    :gcp/category :nested/enum
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.AzureEventHubs.State}
   "STATE_UNSPECIFIED" "ACTIVE" "EVENT_HUBS_PERMISSION_DENIED"
   "PUBLISH_PERMISSION_DENIED" "NAMESPACE_NOT_FOUND" "EVENT_HUB_NOT_FOUND"
   "SUBSCRIPTION_NOT_FOUND" "RESOURCE_GROUP_NOT_FOUND"])

(def AzureEventHubs$State-schema
  [:enum
   {:closed true
    :doc
    "<pre>\nPossible states for managed ingestion from Event Hubs.\n</pre>\n\nProtobuf enum {@code google.pubsub.v1.IngestionDataSourceSettings.AzureEventHubs.State}"
    :gcp/category :nested/enum
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.AzureEventHubs.State}
   "STATE_UNSPECIFIED" "ACTIVE" "EVENT_HUBS_PERMISSION_DENIED"
   "PUBLISH_PERMISSION_DENIED" "NAMESPACE_NOT_FOUND" "EVENT_HUB_NOT_FOUND"
   "SUBSCRIPTION_NOT_FOUND" "RESOURCE_GROUP_NOT_FOUND"])

(defn ^IngestionDataSourceSettings$AzureEventHubs AzureEventHubs-from-edn
  [arg]
  (let [builder (IngestionDataSourceSettings$AzureEventHubs/newBuilder)]
    (when (some? (get arg :clientId))
      (.setClientId builder (get arg :clientId)))
    (when (some? (get arg :eventHub))
      (.setEventHub builder (get arg :eventHub)))
    (when (some? (get arg :gcpServiceAccount))
      (.setGcpServiceAccount builder (get arg :gcpServiceAccount)))
    (when (some? (get arg :namespace))
      (.setNamespace builder (get arg :namespace)))
    (when (some? (get arg :resourceGroup))
      (.setResourceGroup builder (get arg :resourceGroup)))
    (when (some? (get arg :state))
      (.setState builder
                 (IngestionDataSourceSettings$AzureEventHubs$State/valueOf
                   (get arg :state))))
    (when (some? (get arg :subscriptionId))
      (.setSubscriptionId builder (get arg :subscriptionId)))
    (when (some? (get arg :tenantId))
      (.setTenantId builder (get arg :tenantId)))
    (.build builder)))

(defn AzureEventHubs-to-edn
  [^IngestionDataSourceSettings$AzureEventHubs arg]
  (when arg
    (cond-> {}
      (some->> (.getClientId arg)
               (not= ""))
      (assoc :clientId (.getClientId arg))
      (some->> (.getEventHub arg)
               (not= ""))
      (assoc :eventHub (.getEventHub arg))
      (some->> (.getGcpServiceAccount arg)
               (not= ""))
      (assoc :gcpServiceAccount (.getGcpServiceAccount arg))
      (some->> (.getNamespace arg)
               (not= ""))
      (assoc :namespace (.getNamespace arg))
      (some->> (.getResourceGroup arg)
               (not= ""))
      (assoc :resourceGroup (.getResourceGroup arg))
      (.getState arg) (assoc :state (.name (.getState arg)))
      (some->> (.getSubscriptionId arg)
               (not= ""))
      (assoc :subscriptionId (.getSubscriptionId arg))
      (some->> (.getTenantId arg)
               (not= ""))
      (assoc :tenantId (.getTenantId arg)))))

(def AzureEventHubs-schema
  [:map
   {:closed true
    :doc
    "<pre>\nIngestion settings for Azure Event Hubs.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.IngestionDataSourceSettings.AzureEventHubs}"
    :gcp/category :nested/protobuf-message
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.AzureEventHubs}
   [:clientId
    {:optional true
     :getter-doc
     "<pre>\nOptional. The client id of the Azure application that is being used to\nauthenticate Pub/Sub.\n</pre>\n\n<code>string client_id = 5 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The clientId."
     :setter-doc
     "<pre>\nOptional. The client id of the Azure application that is being used to\nauthenticate Pub/Sub.\n</pre>\n\n<code>string client_id = 5 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The clientId to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:eventHub
    {:optional true
     :getter-doc
     "<pre>\nOptional. The name of the Event Hub.\n</pre>\n\n<code>string event_hub = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The eventHub."
     :setter-doc
     "<pre>\nOptional. The name of the Event Hub.\n</pre>\n\n<code>string event_hub = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The eventHub to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:gcpServiceAccount
    {:optional true
     :getter-doc
     "<pre>\nOptional. The GCP service account to be used for Federated Identity\nauthentication.\n</pre>\n\n<code>string gcp_service_account = 8 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The gcpServiceAccount."
     :setter-doc
     "<pre>\nOptional. The GCP service account to be used for Federated Identity\nauthentication.\n</pre>\n\n<code>string gcp_service_account = 8 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The gcpServiceAccount to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:namespace
    {:optional true
     :getter-doc
     "<pre>\nOptional. The name of the Event Hubs namespace.\n</pre>\n\n<code>string namespace = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The namespace."
     :setter-doc
     "<pre>\nOptional. The name of the Event Hubs namespace.\n</pre>\n\n<code>string namespace = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The namespace to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:resourceGroup
    {:optional true
     :getter-doc
     "<pre>\nOptional. Name of the resource group within the azure subscription.\n</pre>\n\n<code>string resource_group = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The resourceGroup."
     :setter-doc
     "<pre>\nOptional. Name of the resource group within the azure subscription.\n</pre>\n\n<code>string resource_group = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The resourceGroup to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:state
    {:optional true
     :read-only true
     :getter-doc
     "<pre>\nOutput only. An output-only field that indicates the state of the Event\nHubs ingestion source.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.AzureEventHubs.State state = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The state."}
    [:enum {:closed true} "STATE_UNSPECIFIED" "ACTIVE"
     "EVENT_HUBS_PERMISSION_DENIED" "PUBLISH_PERMISSION_DENIED"
     "NAMESPACE_NOT_FOUND" "EVENT_HUB_NOT_FOUND" "SUBSCRIPTION_NOT_FOUND"
     "RESOURCE_GROUP_NOT_FOUND"]]
   [:subscriptionId
    {:optional true
     :getter-doc
     "<pre>\nOptional. The Azure subscription id.\n</pre>\n\n<code>string subscription_id = 7 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The subscriptionId."
     :setter-doc
     "<pre>\nOptional. The Azure subscription id.\n</pre>\n\n<code>string subscription_id = 7 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The subscriptionId to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:tenantId
    {:optional true
     :getter-doc
     "<pre>\nOptional. The tenant id of the Azure application that is being used to\nauthenticate Pub/Sub.\n</pre>\n\n<code>string tenant_id = 6 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The tenantId."
     :setter-doc
     "<pre>\nOptional. The tenant id of the Azure application that is being used to\nauthenticate Pub/Sub.\n</pre>\n\n<code>string tenant_id = 6 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The tenantId to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(def AwsMsk$State-schema
  [:enum
   {:closed true
    :doc
    "<pre>\nPossible states for managed ingestion from Amazon MSK.\n</pre>\n\nProtobuf enum {@code google.pubsub.v1.IngestionDataSourceSettings.AwsMsk.State}"
    :gcp/category :nested/enum
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.AwsMsk.State}
   "STATE_UNSPECIFIED" "ACTIVE" "MSK_PERMISSION_DENIED"
   "PUBLISH_PERMISSION_DENIED" "CLUSTER_NOT_FOUND" "TOPIC_NOT_FOUND"])

(def AwsMsk$State-schema
  [:enum
   {:closed true
    :doc
    "<pre>\nPossible states for managed ingestion from Amazon MSK.\n</pre>\n\nProtobuf enum {@code google.pubsub.v1.IngestionDataSourceSettings.AwsMsk.State}"
    :gcp/category :nested/enum
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.AwsMsk.State}
   "STATE_UNSPECIFIED" "ACTIVE" "MSK_PERMISSION_DENIED"
   "PUBLISH_PERMISSION_DENIED" "CLUSTER_NOT_FOUND" "TOPIC_NOT_FOUND"])

(defn ^IngestionDataSourceSettings$AwsMsk AwsMsk-from-edn
  [arg]
  (let [builder (IngestionDataSourceSettings$AwsMsk/newBuilder)]
    (when (some? (get arg :awsRoleArn))
      (.setAwsRoleArn builder (get arg :awsRoleArn)))
    (when (some? (get arg :clusterArn))
      (.setClusterArn builder (get arg :clusterArn)))
    (when (some? (get arg :gcpServiceAccount))
      (.setGcpServiceAccount builder (get arg :gcpServiceAccount)))
    (when (some? (get arg :state))
      (.setState builder
                 (IngestionDataSourceSettings$AwsMsk$State/valueOf
                   (get arg :state))))
    (when (some? (get arg :topic)) (.setTopic builder (get arg :topic)))
    (.build builder)))

(defn AwsMsk-to-edn
  [^IngestionDataSourceSettings$AwsMsk arg]
  (when arg
    (cond-> {:awsRoleArn (.getAwsRoleArn arg)
             :clusterArn (.getClusterArn arg)
             :gcpServiceAccount (.getGcpServiceAccount arg)
             :topic (.getTopic arg)}
      (.getState arg) (assoc :state (.name (.getState arg))))))

(def AwsMsk-schema
  [:map
   {:closed true
    :doc
    "<pre>\nIngestion settings for Amazon MSK.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.IngestionDataSourceSettings.AwsMsk}"
    :gcp/category :nested/protobuf-message
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.AwsMsk}
   [:awsRoleArn
    {:getter-doc
     "<pre>\nRequired. AWS role ARN to be used for Federated Identity authentication\nwith Amazon MSK. Check the Pub/Sub docs for how to set up this role and\nthe required permissions that need to be attached to it.\n</pre>\n\n<code>string aws_role_arn = 4 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The awsRoleArn."
     :setter-doc
     "<pre>\nRequired. AWS role ARN to be used for Federated Identity authentication\nwith Amazon MSK. Check the Pub/Sub docs for how to set up this role and\nthe required permissions that need to be attached to it.\n</pre>\n\n<code>string aws_role_arn = 4 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The awsRoleArn to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:clusterArn
    {:getter-doc
     "<pre>\nRequired. The Amazon Resource Name (ARN) that uniquely identifies the\ncluster.\n</pre>\n\n<code>string cluster_arn = 2 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The clusterArn."
     :setter-doc
     "<pre>\nRequired. The Amazon Resource Name (ARN) that uniquely identifies the\ncluster.\n</pre>\n\n<code>string cluster_arn = 2 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The clusterArn to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:gcpServiceAccount
    {:getter-doc
     "<pre>\nRequired. The GCP service account to be used for Federated Identity\nauthentication with Amazon MSK (via a `AssumeRoleWithWebIdentity` call\nfor the provided role). The `aws_role_arn` must be set up with\n`accounts.google.com:sub` equals to this service account number.\n</pre>\n\n<code>string gcp_service_account = 5 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The gcpServiceAccount."
     :setter-doc
     "<pre>\nRequired. The GCP service account to be used for Federated Identity\nauthentication with Amazon MSK (via a `AssumeRoleWithWebIdentity` call\nfor the provided role). The `aws_role_arn` must be set up with\n`accounts.google.com:sub` equals to this service account number.\n</pre>\n\n<code>string gcp_service_account = 5 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The gcpServiceAccount to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:state
    {:optional true
     :read-only true
     :getter-doc
     "<pre>\nOutput only. An output-only field that indicates the state of the Amazon\nMSK ingestion source.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.AwsMsk.State state = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The state."}
    [:enum {:closed true} "STATE_UNSPECIFIED" "ACTIVE" "MSK_PERMISSION_DENIED"
     "PUBLISH_PERMISSION_DENIED" "CLUSTER_NOT_FOUND" "TOPIC_NOT_FOUND"]]
   [:topic
    {:getter-doc
     "<pre>\nRequired. The name of the topic in the Amazon MSK cluster that Pub/Sub\nwill import from.\n</pre>\n\n<code>\nstring topic = 3 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }\n</code>\n\n@return The topic."
     :setter-doc
     "<pre>\nRequired. The name of the topic in the Amazon MSK cluster that Pub/Sub\nwill import from.\n</pre>\n\n<code>\nstring topic = 3 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }\n</code>\n\n@param value The topic to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(def ConfluentCloud$State-schema
  [:enum
   {:closed true
    :doc
    "<pre>\nPossible states for managed ingestion from Confluent Cloud.\n</pre>\n\nProtobuf enum {@code google.pubsub.v1.IngestionDataSourceSettings.ConfluentCloud.State}"
    :gcp/category :nested/enum
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.ConfluentCloud.State}
   "STATE_UNSPECIFIED" "ACTIVE" "CONFLUENT_CLOUD_PERMISSION_DENIED"
   "PUBLISH_PERMISSION_DENIED" "UNREACHABLE_BOOTSTRAP_SERVER"
   "CLUSTER_NOT_FOUND" "TOPIC_NOT_FOUND"])

(def ConfluentCloud$State-schema
  [:enum
   {:closed true
    :doc
    "<pre>\nPossible states for managed ingestion from Confluent Cloud.\n</pre>\n\nProtobuf enum {@code google.pubsub.v1.IngestionDataSourceSettings.ConfluentCloud.State}"
    :gcp/category :nested/enum
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.ConfluentCloud.State}
   "STATE_UNSPECIFIED" "ACTIVE" "CONFLUENT_CLOUD_PERMISSION_DENIED"
   "PUBLISH_PERMISSION_DENIED" "UNREACHABLE_BOOTSTRAP_SERVER"
   "CLUSTER_NOT_FOUND" "TOPIC_NOT_FOUND"])

(defn ^IngestionDataSourceSettings$ConfluentCloud ConfluentCloud-from-edn
  [arg]
  (let [builder (IngestionDataSourceSettings$ConfluentCloud/newBuilder)]
    (when (some? (get arg :bootstrapServer))
      (.setBootstrapServer builder (get arg :bootstrapServer)))
    (when (some? (get arg :clusterId))
      (.setClusterId builder (get arg :clusterId)))
    (when (some? (get arg :gcpServiceAccount))
      (.setGcpServiceAccount builder (get arg :gcpServiceAccount)))
    (when (some? (get arg :identityPoolId))
      (.setIdentityPoolId builder (get arg :identityPoolId)))
    (when (some? (get arg :state))
      (.setState builder
                 (IngestionDataSourceSettings$ConfluentCloud$State/valueOf
                   (get arg :state))))
    (when (some? (get arg :topic)) (.setTopic builder (get arg :topic)))
    (.build builder)))

(defn ConfluentCloud-to-edn
  [^IngestionDataSourceSettings$ConfluentCloud arg]
  (when arg
    (cond-> {:bootstrapServer (.getBootstrapServer arg)
             :clusterId (.getClusterId arg)
             :gcpServiceAccount (.getGcpServiceAccount arg)
             :identityPoolId (.getIdentityPoolId arg)
             :topic (.getTopic arg)}
      (.getState arg) (assoc :state (.name (.getState arg))))))

(def ConfluentCloud-schema
  [:map
   {:closed true
    :doc
    "<pre>\nIngestion settings for Confluent Cloud.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.IngestionDataSourceSettings.ConfluentCloud}"
    :gcp/category :nested/protobuf-message
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.ConfluentCloud}
   [:bootstrapServer
    {:getter-doc
     "<pre>\nRequired. The address of the bootstrap server. The format is url:port.\n</pre>\n\n<code>string bootstrap_server = 2 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The bootstrapServer."
     :setter-doc
     "<pre>\nRequired. The address of the bootstrap server. The format is url:port.\n</pre>\n\n<code>string bootstrap_server = 2 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The bootstrapServer to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:clusterId
    {:getter-doc
     "<pre>\nRequired. The id of the cluster.\n</pre>\n\n<code>string cluster_id = 3 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The clusterId."
     :setter-doc
     "<pre>\nRequired. The id of the cluster.\n</pre>\n\n<code>string cluster_id = 3 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The clusterId to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:gcpServiceAccount
    {:getter-doc
     "<pre>\nRequired. The GCP service account to be used for Federated Identity\nauthentication with `identity_pool_id`.\n</pre>\n\n<code>string gcp_service_account = 6 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The gcpServiceAccount."
     :setter-doc
     "<pre>\nRequired. The GCP service account to be used for Federated Identity\nauthentication with `identity_pool_id`.\n</pre>\n\n<code>string gcp_service_account = 6 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The gcpServiceAccount to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:identityPoolId
    {:getter-doc
     "<pre>\nRequired. The id of the identity pool to be used for Federated Identity\nauthentication with Confluent Cloud. See\nhttps://docs.confluent.io/cloud/current/security/authenticate/workload-identities/identity-providers/oauth/identity-pools.html#add-oauth-identity-pools.\n</pre>\n\n<code>string identity_pool_id = 5 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The identityPoolId."
     :setter-doc
     "<pre>\nRequired. The id of the identity pool to be used for Federated Identity\nauthentication with Confluent Cloud. See\nhttps://docs.confluent.io/cloud/current/security/authenticate/workload-identities/identity-providers/oauth/identity-pools.html#add-oauth-identity-pools.\n</pre>\n\n<code>string identity_pool_id = 5 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The identityPoolId to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:state
    {:optional true
     :read-only true
     :getter-doc
     "<pre>\nOutput only. An output-only field that indicates the state of the\nConfluent Cloud ingestion source.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.ConfluentCloud.State state = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The state."}
    [:enum {:closed true} "STATE_UNSPECIFIED" "ACTIVE"
     "CONFLUENT_CLOUD_PERMISSION_DENIED" "PUBLISH_PERMISSION_DENIED"
     "UNREACHABLE_BOOTSTRAP_SERVER" "CLUSTER_NOT_FOUND" "TOPIC_NOT_FOUND"]]
   [:topic
    {:getter-doc
     "<pre>\nRequired. The name of the topic in the Confluent Cloud cluster that\nPub/Sub will import from.\n</pre>\n\n<code>string topic = 4 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The topic."
     :setter-doc
     "<pre>\nRequired. The name of the topic in the Confluent Cloud cluster that\nPub/Sub will import from.\n</pre>\n\n<code>string topic = 4 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The topic to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(def SourceCase-schema
  [:enum
   {:closed true
    :doc nil
    :gcp/category :nested/enum
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings.SourceCase}
   "AWS_KINESIS" "CLOUD_STORAGE" "AZURE_EVENT_HUBS" "AWS_MSK" "CONFLUENT_CLOUD"
   "SOURCE_NOT_SET"])

(defn ^IngestionDataSourceSettings from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/IngestionDataSourceSettings arg)
  (let [builder (IngestionDataSourceSettings/newBuilder)]
    (when (some? (get arg :platformLogsSettings))
      (.setPlatformLogsSettings builder
                                (PlatformLogsSettings/from-edn
                                  (get arg :platformLogsSettings))))
    (cond (contains? arg :awsKinesis)
      (.setAwsKinesis builder (AwsKinesis-from-edn (get arg :awsKinesis)))
      (contains? arg :awsMsk)
      (.setAwsMsk builder (AwsMsk-from-edn (get arg :awsMsk)))
      (contains? arg :azureEventHubs) (.setAzureEventHubs
                                        builder
                                        (AzureEventHubs-from-edn
                                          (get arg :azureEventHubs)))
      (contains? arg :cloudStorage)
      (.setCloudStorage builder
                        (CloudStorage-from-edn (get arg :cloudStorage)))
      (contains? arg :confluentCloud) (.setConfluentCloud
                                        builder
                                        (ConfluentCloud-from-edn
                                          (get arg :confluentCloud))))
    (.build builder)))

(defn to-edn
  [^IngestionDataSourceSettings arg]
  {:post [(global/strict! :gcp.pubsub.v1/IngestionDataSourceSettings %)]}
  (when arg
    (let [res (cond-> {}
                (.hasPlatformLogsSettings arg) (assoc :platformLogsSettings
                                                 (PlatformLogsSettings/to-edn
                                                   (.getPlatformLogsSettings
                                                     arg))))
          res
          (case (.name (.getSourceCase arg))
            "AWS_KINESIS"
            (assoc res :awsKinesis (AwsKinesis-to-edn (.getAwsKinesis arg)))
            "AWS_MSK" (assoc res :awsMsk (AwsMsk-to-edn (.getAwsMsk arg)))
            "AZURE_EVENT_HUBS" (assoc res
                                 :azureEventHubs (AzureEventHubs-to-edn
                                                   (.getAzureEventHubs arg)))
            "CLOUD_STORAGE" (assoc res
                              :cloudStorage (CloudStorage-to-edn
                                              (.getCloudStorage arg)))
            "CONFLUENT_CLOUD" (assoc res
                                :confluentCloud (ConfluentCloud-to-edn
                                                  (.getConfluentCloud arg)))
            res)]
      res)))

(def schema
  [:and
   {:closed true
    :doc
    "<pre>\nSettings for an ingestion data source on a topic.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.IngestionDataSourceSettings}"
    :gcp/category :union-protobuf-oneof
    :gcp/key :gcp.pubsub.v1/IngestionDataSourceSettings}
   [:map {:closed true}
    [:awsKinesis
     {:optional true
      :getter-doc
      "<pre>\nOptional. Amazon Kinesis Data Streams.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.AwsKinesis aws_kinesis = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The awsKinesis."
      :setter-doc
      "<pre>\nOptional. Amazon Kinesis Data Streams.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.AwsKinesis aws_kinesis = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:ref :gcp.pubsub.v1/IngestionDataSourceSettings.AwsKinesis]]
    [:awsMsk
     {:optional true
      :getter-doc
      "<pre>\nOptional. Amazon MSK.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.AwsMsk aws_msk = 5 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The awsMsk."
      :setter-doc
      "<pre>\nOptional. Amazon MSK.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.AwsMsk aws_msk = 5 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:ref :gcp.pubsub.v1/IngestionDataSourceSettings.AwsMsk]]
    [:azureEventHubs
     {:optional true
      :getter-doc
      "<pre>\nOptional. Azure Event Hubs.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.AzureEventHubs azure_event_hubs = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The azureEventHubs."
      :setter-doc
      "<pre>\nOptional. Azure Event Hubs.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.AzureEventHubs azure_event_hubs = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:ref :gcp.pubsub.v1/IngestionDataSourceSettings.AzureEventHubs]]
    [:cloudStorage
     {:optional true
      :getter-doc
      "<pre>\nOptional. Cloud Storage.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.CloudStorage cloud_storage = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The cloudStorage."
      :setter-doc
      "<pre>\nOptional. Cloud Storage.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.CloudStorage cloud_storage = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:ref :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage]]
    [:confluentCloud
     {:optional true
      :getter-doc
      "<pre>\nOptional. Confluent Cloud.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.ConfluentCloud confluent_cloud = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The confluentCloud."
      :setter-doc
      "<pre>\nOptional. Confluent Cloud.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings.ConfluentCloud confluent_cloud = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:ref :gcp.pubsub.v1/IngestionDataSourceSettings.ConfluentCloud]]
    [:platformLogsSettings
     {:optional true
      :getter-doc
      "<pre>\nOptional. Platform Logs settings. If unset, no Platform Logs will be\ngenerated.\n</pre>\n\n<code>\n.google.pubsub.v1.PlatformLogsSettings platform_logs_settings = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The platformLogsSettings."
      :setter-doc
      "<pre>\nOptional. Platform Logs settings. If unset, no Platform Logs will be\ngenerated.\n</pre>\n\n<code>\n.google.pubsub.v1.PlatformLogsSettings platform_logs_settings = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     :gcp.pubsub.v1/PlatformLogsSettings]]
   [:fn
    {:error/message
     "Only one of these keys may be present: #{:cloudStorage :awsMsk :azureEventHubs :awsKinesis :confluentCloud}"}
    (quote (fn [m]
             (<= (count (filter (set (keys m))
                          #{:cloudStorage :awsMsk :azureEventHubs :awsKinesis
                            :confluentCloud}))
                 1)))]])

(global/include-schema-registry!
  (with-meta
    {:gcp.pubsub.v1/IngestionDataSourceSettings schema
     :gcp.pubsub.v1/IngestionDataSourceSettings.AwsKinesis AwsKinesis-schema
     :gcp.pubsub.v1/IngestionDataSourceSettings.AwsKinesis.State
     AwsKinesis$State-schema
     :gcp.pubsub.v1/IngestionDataSourceSettings.AwsMsk AwsMsk-schema
     :gcp.pubsub.v1/IngestionDataSourceSettings.AwsMsk.State
     AwsMsk$State-schema
     :gcp.pubsub.v1/IngestionDataSourceSettings.AzureEventHubs
     AzureEventHubs-schema
     :gcp.pubsub.v1/IngestionDataSourceSettings.AzureEventHubs.State
     AzureEventHubs$State-schema
     :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage
     CloudStorage-schema
     :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.AvroFormat
     CloudStorage$AvroFormat-schema
     :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.InputFormatCase
     CloudStorage$InputFormatCase-schema
     :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.PubSubAvroFormat
     CloudStorage$PubSubAvroFormat-schema
     :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.State
     CloudStorage$State-schema
     :gcp.pubsub.v1/IngestionDataSourceSettings.CloudStorage.TextFormat
     CloudStorage$TextFormat-schema
     :gcp.pubsub.v1/IngestionDataSourceSettings.ConfluentCloud
     ConfluentCloud-schema
     :gcp.pubsub.v1/IngestionDataSourceSettings.ConfluentCloud.State
     ConfluentCloud$State-schema
     :gcp.pubsub.v1/IngestionDataSourceSettings.SourceCase SourceCase-schema}
    {:gcp.global/name "gcp.pubsub.v1.IngestionDataSourceSettings"}))
