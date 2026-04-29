;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.BigtableConfig
  {:doc
     "<pre>\nConfiguration for a Bigtable subscription. The Pub/Sub message will be\nwritten to a Bigtable row as follows:\n- row key: subscription name and message ID delimited by #.\n- columns: message bytes written to a single column family \"data\" with an\nempty-string column qualifier.\n- cell timestamp: the message publish timestamp.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.BigtableConfig}"
   :file-git-sha "f69de7ee2cb5b96adbf3fc5b11e1f8a3bbbd0bee"
   :fqcn "com.google.pubsub.v1.BigtableConfig"
   :gcp.dev/certification
     {:base-seed 1777403438094
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777403438094 :standard 1777403438095 :stress 1777403438096}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-28T19:10:39.195414770Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.pubsub.v1 BigtableConfig BigtableConfig$Builder
            BigtableConfig$State]))

(declare from-edn to-edn State-from-edn State-to-edn)

(def State-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nPossible states for a Bigtable subscription.\nNote: more states could be added in the future. Please code accordingly.\n</pre>\n\nProtobuf enum {@code google.pubsub.v1.BigtableConfig.State}",
    :gcp/category :nested/enum,
    :gcp/key :gcp.pubsub.v1/BigtableConfig.State} "STATE_UNSPECIFIED" "ACTIVE"
   "NOT_FOUND" "APP_PROFILE_MISCONFIGURED" "PERMISSION_DENIED" "SCHEMA_MISMATCH"
   "IN_TRANSIT_LOCATION_RESTRICTION" "VERTEX_AI_LOCATION_RESTRICTION"])

(defn ^BigtableConfig from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/BigtableConfig arg)
  (let [builder (BigtableConfig/newBuilder)]
    (when (some? (get arg :appProfileId))
      (.setAppProfileId builder (get arg :appProfileId)))
    (when (some? (get arg :serviceAccountEmail))
      (.setServiceAccountEmail builder (get arg :serviceAccountEmail)))
    (when (some? (get arg :state))
      (.setState builder (BigtableConfig$State/valueOf (get arg :state))))
    (when (some? (get arg :table)) (.setTable builder (get arg :table)))
    (when (some? (get arg :writeMetadata))
      (.setWriteMetadata builder (get arg :writeMetadata)))
    (.build builder)))

(defn to-edn
  [^BigtableConfig arg]
  {:post [(global/strict! :gcp.pubsub.v1/BigtableConfig %)]}
  (when arg
    (cond-> {}
      (some->> (.getAppProfileId arg)
               (not= ""))
        (assoc :appProfileId (.getAppProfileId arg))
      (some->> (.getServiceAccountEmail arg)
               (not= ""))
        (assoc :serviceAccountEmail (.getServiceAccountEmail arg))
      (.getState arg) (assoc :state (.name (.getState arg)))
      (some->> (.getTable arg)
               (not= ""))
        (assoc :table (.getTable arg))
      (.getWriteMetadata arg) (assoc :writeMetadata (.getWriteMetadata arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nConfiguration for a Bigtable subscription. The Pub/Sub message will be\nwritten to a Bigtable row as follows:\n- row key: subscription name and message ID delimited by #.\n- columns: message bytes written to a single column family \"data\" with an\nempty-string column qualifier.\n- cell timestamp: the message publish timestamp.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.BigtableConfig}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.pubsub.v1/BigtableConfig}
   [:appProfileId
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The app profile to use for the Bigtable writes. If not specified,\nthe \"default\" application profile will be used. The app profile must use\nsingle-cluster routing.\n</pre>\n\n<code>string app_profile_id = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The appProfileId.",
     :setter-doc
       "<pre>\nOptional. The app profile to use for the Bigtable writes. If not specified,\nthe \"default\" application profile will be used. The app profile must use\nsingle-cluster routing.\n</pre>\n\n<code>string app_profile_id = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The appProfileId to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:serviceAccountEmail
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The service account to use to write to Bigtable. The subscription\ncreator or updater that specifies this field must have\n`iam.serviceAccounts.actAs` permission on the service account. If not\nspecified, the Pub/Sub [service\nagent](https://cloud.google.com/iam/docs/service-agents),\nservice-{project_number}&#64;gcp-sa-pubsub.iam.gserviceaccount.com, is used.\n</pre>\n\n<code>string service_account_email = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The serviceAccountEmail.",
     :setter-doc
       "<pre>\nOptional. The service account to use to write to Bigtable. The subscription\ncreator or updater that specifies this field must have\n`iam.serviceAccounts.actAs` permission on the service account. If not\nspecified, the Pub/Sub [service\nagent](https://cloud.google.com/iam/docs/service-agents),\nservice-{project_number}&#64;gcp-sa-pubsub.iam.gserviceaccount.com, is used.\n</pre>\n\n<code>string service_account_email = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The serviceAccountEmail to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:state
    {:optional true,
     :read-only true,
     :getter-doc
       "<pre>\nOutput only. An output-only field that indicates whether or not the\nsubscription can receive messages.\n</pre>\n\n<code>\n.google.pubsub.v1.BigtableConfig.State state = 4 [(.google.api.field_behavior) = OUTPUT_ONLY];\n</code>\n\n@return The state."}
    [:enum {:closed true} "STATE_UNSPECIFIED" "ACTIVE" "NOT_FOUND"
     "APP_PROFILE_MISCONFIGURED" "PERMISSION_DENIED" "SCHEMA_MISMATCH"
     "IN_TRANSIT_LOCATION_RESTRICTION" "VERTEX_AI_LOCATION_RESTRICTION"]]
   [:table
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The unique name of the table to write messages to.\n\nValues are of the form\n`projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.\n</pre>\n\n<code>string table = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The table.",
     :setter-doc
       "<pre>\nOptional. The unique name of the table to write messages to.\n\nValues are of the form\n`projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.\n</pre>\n\n<code>string table = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The table to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:writeMetadata
    {:optional true,
     :getter-doc
       "<pre>\nOptional. When true, write the subscription name, message_id, publish_time,\nattributes, and ordering_key to additional columns in the table under the\npubsub_metadata column family. The subscription name, message_id, and\npublish_time fields are put in their own columns while all other message\nproperties (other than data) are written to a JSON object in the attributes\ncolumn.\n</pre>\n\n<code>bool write_metadata = 5 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The writeMetadata.",
     :setter-doc
       "<pre>\nOptional. When true, write the subscription name, message_id, publish_time,\nattributes, and ordering_key to additional columns in the table under the\npubsub_metadata column family. The subscription name, message_id, and\npublish_time fields are put in their own columns while all other message\nproperties (other than data) are written to a JSON object in the attributes\ncolumn.\n</pre>\n\n<code>bool write_metadata = 5 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The writeMetadata to set.\n@return This builder for chaining."}
    :boolean]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/BigtableConfig schema,
              :gcp.pubsub.v1/BigtableConfig.State State-schema}
    {:gcp.global/name "gcp.pubsub.v1.BigtableConfig"}))