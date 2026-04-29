;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.Topic
  {:doc "<pre>\nA topic resource.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.Topic}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.Topic"
   :gcp.dev/certification {:base-seed 1777050384215
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777050384215 :standard 1777050384216 :stress 1777050384217}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-24T17:06:26.258250125Z"}}
  (:require
   [gcp.foreign.com.google.protobuf :as protobuf]
   [gcp.global :as global]
   [gcp.pubsub.v1.IngestionDataSourceSettings :as IngestionDataSourceSettings]
   [gcp.pubsub.v1.MessageStoragePolicy :as MessageStoragePolicy]
   [gcp.pubsub.v1.MessageTransform :as MessageTransform]
   [gcp.pubsub.v1.SchemaSettings :as SchemaSettings])
  (:import
   (com.google.protobuf Duration)
   (com.google.pubsub.v1 Topic Topic$Builder Topic$State)))

(declare from-edn to-edn State-from-edn State-to-edn)

(def State-schema
  [:enum
   {:closed true
    :doc
    "<pre>\nThe state of the topic.\n</pre>\n\nProtobuf enum {@code google.pubsub.v1.Topic.State}"
    :gcp/category :nested/enum
    :gcp/key :gcp.pubsub.v1/Topic.State} "STATE_UNSPECIFIED" "ACTIVE"
   "INGESTION_RESOURCE_ERROR"])

(defn ^Topic from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/Topic arg)
  (let [builder (Topic/newBuilder)]
    (when (some? (get arg :ingestionDataSourceSettings))
      (.setIngestionDataSourceSettings builder
                                       (IngestionDataSourceSettings/from-edn
                                         (get arg
                                              :ingestionDataSourceSettings))))
    (when (some? (get arg :kmsKeyName))
      (.setKmsKeyName builder (get arg :kmsKeyName)))
    (when (seq (get arg :labels))
      (.putAllLabels
        builder
        (into {} (map (fn [[k v]] [(name k) v])) (get arg :putAllLabels))))
    (when (some? (get arg :messageRetentionDuration))
      (.setMessageRetentionDuration builder
                                    (protobuf/Duration-from-edn
                                      (get arg :messageRetentionDuration))))
    (when (some? (get arg :messageStoragePolicy))
      (.setMessageStoragePolicy builder
                                (MessageStoragePolicy/from-edn
                                  (get arg :messageStoragePolicy))))
    (when (seq (get arg :messageTransforms))
      (.addAllMessageTransforms builder
                                (mapv MessageTransform/from-edn
                                  (get arg :messageTransforms))))
    (when (some? (get arg :name)) (.setName builder (get arg :name)))
    (when (some? (get arg :satisfiesPzs))
      (.setSatisfiesPzs builder (get arg :satisfiesPzs)))
    (when (some? (get arg :schemaSettings))
      (.setSchemaSettings builder
                          (SchemaSettings/from-edn (get arg :schemaSettings))))
    (when (some? (get arg :state))
      (.setState builder (Topic$State/valueOf (get arg :state))))
    (when (seq (get arg :tags))
      (.putAllTags
        builder
        (into {} (map (fn [[k v]] [(name k) v])) (get arg :putAllTags))))
    (.build builder)))

(defn to-edn
  [^Topic arg]
  {:post [(global/strict! :gcp.pubsub.v1/Topic %)]}
  (when arg
    (cond-> {:name (.getName arg)}
      (.hasIngestionDataSourceSettings arg)
      (assoc :ingestionDataSourceSettings
        (IngestionDataSourceSettings/to-edn (.getIngestionDataSourceSettings
                                              arg)))
      (some->> (.getKmsKeyName arg)
               (not= ""))
      (assoc :kmsKeyName (.getKmsKeyName arg))
      (seq (.getLabelsMap arg))
      (assoc :labels
        (into {} (map (fn [[k v]] [(keyword k) v])) (.getLabelsMap arg)))
      (.hasMessageRetentionDuration arg)
      (assoc :messageRetentionDuration
        (protobuf/Duration-to-edn (.getMessageRetentionDuration arg)))
      (.hasMessageStoragePolicy arg) (assoc :messageStoragePolicy
                                       (MessageStoragePolicy/to-edn
                                         (.getMessageStoragePolicy arg)))
      (seq (.getMessageTransformsList arg))
      (assoc :messageTransforms
        (mapv MessageTransform/to-edn (.getMessageTransformsList arg)))
      (.getSatisfiesPzs arg) (assoc :satisfiesPzs (.getSatisfiesPzs arg))
      (.hasSchemaSettings arg)
      (assoc :schemaSettings (SchemaSettings/to-edn (.getSchemaSettings arg)))
      (.getState arg) (assoc :state (.name (.getState arg)))
      (seq (.getTagsMap arg))
      (assoc :tags
        (into {} (map (fn [[k v]] [(keyword k) v])) (.getTagsMap arg))))))

(def schema
  [:map
   {:closed true
    :doc
    "<pre>\nA topic resource.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.Topic}"
    :gcp/category :protobuf-message
    :gcp/key :gcp.pubsub.v1/Topic}
   [:ingestionDataSourceSettings
    {:optional true
     :getter-doc
     "<pre>\nOptional. Settings for ingestion from a data source into this topic.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings ingestion_data_source_settings = 10 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The ingestionDataSourceSettings."
     :setter-doc
     "<pre>\nOptional. Settings for ingestion from a data source into this topic.\n</pre>\n\n<code>\n.google.pubsub.v1.IngestionDataSourceSettings ingestion_data_source_settings = 10 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.pubsub.v1/IngestionDataSourceSettings]
   [:kmsKeyName
    {:optional true
     :getter-doc
     "<pre>\nOptional. The resource name of the Cloud KMS CryptoKey to be used to\nprotect access to messages published on this topic.\n\nThe expected format is `projects/&#42;&#47;locations/&#42;&#47;keyRings/&#42;&#47;cryptoKeys/&#42;`.\n</pre>\n\n<code>\nstring kms_key_name = 5 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }\n</code>\n\n@return The kmsKeyName."
     :setter-doc
     "<pre>\nOptional. The resource name of the Cloud KMS CryptoKey to be used to\nprotect access to messages published on this topic.\n\nThe expected format is `projects/&#42;&#47;locations/&#42;&#47;keyRings/&#42;&#47;cryptoKeys/&#42;`.\n</pre>\n\n<code>\nstring kms_key_name = 5 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }\n</code>\n\n@param value The kmsKeyName to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:labels
    {:optional true
     :getter-doc
     "<pre>\nOptional. See [Creating and managing labels]\n(https://cloud.google.com/pubsub/docs/labels).\n</pre>\n\n<code>map&lt;string, string&gt; labels = 2 [(.google.api.field_behavior) = OPTIONAL];</code>"
     :setter-doc
     "<pre>\nOptional. See [Creating and managing labels]\n(https://cloud.google.com/pubsub/docs/labels).\n</pre>\n\n<code>map&lt;string, string&gt; labels = 2 [(.google.api.field_behavior) = OPTIONAL];</code>"}
    [:map-of [:or simple-keyword? [:string {:min 1}]]
     [:string {:min 1, :gen/max 1}]]]
   [:messageRetentionDuration
    {:optional true
     :getter-doc
     "<pre>\nOptional. Indicates the minimum duration to retain a message after it is\npublished to the topic. If this field is set, messages published to the\ntopic in the last `message_retention_duration` are always available to\nsubscribers. For instance, it allows any attached subscription to [seek to\na\ntimestamp](https://cloud.google.com/pubsub/docs/replay-overview#seek_to_a_time)\nthat is up to `message_retention_duration` in the past. If this field is\nnot set, message retention is controlled by settings on individual\nsubscriptions. Cannot be more than 31 days or less than 10 minutes.\n</pre>\n\n<code>\n.google.protobuf.Duration message_retention_duration = 8 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The messageRetentionDuration."
     :setter-doc
     "<pre>\nOptional. Indicates the minimum duration to retain a message after it is\npublished to the topic. If this field is set, messages published to the\ntopic in the last `message_retention_duration` are always available to\nsubscribers. For instance, it allows any attached subscription to [seek to\na\ntimestamp](https://cloud.google.com/pubsub/docs/replay-overview#seek_to_a_time)\nthat is up to `message_retention_duration` in the past. If this field is\nnot set, message retention is controlled by settings on individual\nsubscriptions. Cannot be more than 31 days or less than 10 minutes.\n</pre>\n\n<code>\n.google.protobuf.Duration message_retention_duration = 8 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.foreign.com.google.protobuf/Duration]
   [:messageStoragePolicy
    {:optional true
     :getter-doc
     "<pre>\nOptional. Policy constraining the set of Google Cloud Platform regions\nwhere messages published to the topic may be stored. If not present, then\nno constraints are in effect.\n</pre>\n\n<code>\n.google.pubsub.v1.MessageStoragePolicy message_storage_policy = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The messageStoragePolicy."
     :setter-doc
     "<pre>\nOptional. Policy constraining the set of Google Cloud Platform regions\nwhere messages published to the topic may be stored. If not present, then\nno constraints are in effect.\n</pre>\n\n<code>\n.google.pubsub.v1.MessageStoragePolicy message_storage_policy = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.pubsub.v1/MessageStoragePolicy]
   [:messageTransforms
    {:optional true
     :getter-doc
     "<pre>\nOptional. Transforms to be applied to messages published to the topic.\nTransforms are applied in the order specified.\n</pre>\n\n<code>\nrepeated .google.pubsub.v1.MessageTransform message_transforms = 13 [(.google.api.field_behavior) = OPTIONAL];\n</code>"
     :setter-doc
     "<pre>\nOptional. Transforms to be applied to messages published to the topic.\nTransforms are applied in the order specified.\n</pre>\n\n<code>\nrepeated .google.pubsub.v1.MessageTransform message_transforms = 13 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:sequential {:min 1, :gen/max 2} :gcp.pubsub.v1/MessageTransform]]
   [:name
    {:getter-doc
     "<pre>\nRequired. Identifier. The name of the topic. It must have the format\n`\"projects/{project}/topics/{topic}\"`. `{topic}` must start with a letter,\nand contain only letters (`[A-Za-z]`), numbers (`[0-9]`), dashes (`-`),\nunderscores (`_`), periods (`.`), tildes (`~`), plus (`+`) or percent\nsigns (`%`). It must be between 3 and 255 characters in length, and it\nmust not start with `\"goog\"`.\n</pre>\n\n<code>\nstring name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.field_behavior) = IDENTIFIER];\n</code>\n\n@return The name."
     :setter-doc
     "<pre>\nRequired. Identifier. The name of the topic. It must have the format\n`\"projects/{project}/topics/{topic}\"`. `{topic}` must start with a letter,\nand contain only letters (`[A-Za-z]`), numbers (`[0-9]`), dashes (`-`),\nunderscores (`_`), periods (`.`), tildes (`~`), plus (`+`) or percent\nsigns (`%`). It must be between 3 and 255 characters in length, and it\nmust not start with `\"goog\"`.\n</pre>\n\n<code>\nstring name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.field_behavior) = IDENTIFIER];\n</code>\n\n@param value The name to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:satisfiesPzs
    {:optional true
     :getter-doc
     "<pre>\nOptional. Reserved for future use. This field is set only in responses from\nthe server; it is ignored if it is set in any requests.\n</pre>\n\n<code>bool satisfies_pzs = 7 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The satisfiesPzs."
     :setter-doc
     "<pre>\nOptional. Reserved for future use. This field is set only in responses from\nthe server; it is ignored if it is set in any requests.\n</pre>\n\n<code>bool satisfies_pzs = 7 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The satisfiesPzs to set.\n@return This builder for chaining."}
    :boolean]
   [:schemaSettings
    {:optional true
     :getter-doc
     "<pre>\nOptional. Settings for validating messages published against a schema.\n</pre>\n\n<code>\n.google.pubsub.v1.SchemaSettings schema_settings = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The schemaSettings."
     :setter-doc
     "<pre>\nOptional. Settings for validating messages published against a schema.\n</pre>\n\n<code>\n.google.pubsub.v1.SchemaSettings schema_settings = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.pubsub.v1/SchemaSettings]
   [:state
    {:optional true
     :read-only true
     :getter-doc
     "<pre>\nOutput only. An output-only field indicating the state of the topic.\n</pre>\n\n<code>.google.pubsub.v1.Topic.State state = 9 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The state."}
    [:enum {:closed true} "STATE_UNSPECIFIED" "ACTIVE"
     "INGESTION_RESOURCE_ERROR"]]
   [:tags
    {:optional true
     :getter-doc
     "<pre>\nOptional. Input only. Immutable. Tag keys/values directly bound to this\nresource. For example:\n\"123/environment\": \"production\",\n\"123/costCenter\": \"marketing\"\nSee https://docs.cloud.google.com/pubsub/docs/tags for more information on\nusing tags with Pub/Sub resources.\n</pre>\n\n<code>\nmap&lt;string, string&gt; tags = 14 [(.google.api.field_behavior) = INPUT_ONLY, (.google.api.field_behavior) = IMMUTABLE, (.google.api.field_behavior) = OPTIONAL];\n</code>"
     :setter-doc
     "<pre>\nOptional. Input only. Immutable. Tag keys/values directly bound to this\nresource. For example:\n\"123/environment\": \"production\",\n\"123/costCenter\": \"marketing\"\nSee https://docs.cloud.google.com/pubsub/docs/tags for more information on\nusing tags with Pub/Sub resources.\n</pre>\n\n<code>\nmap&lt;string, string&gt; tags = 14 [(.google.api.field_behavior) = INPUT_ONLY, (.google.api.field_behavior) = IMMUTABLE, (.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:map-of [:or simple-keyword? [:string {:min 1}]]
     [:string {:min 1, :gen/max 1}]]]])

(global/include-schema-registry! (with-meta {:gcp.pubsub.v1/Topic schema
                                             :gcp.pubsub.v1/Topic.State
                                             State-schema}
                                   {:gcp.global/name "gcp.pubsub.v1.Topic"}))
