;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.SchemaSettings
  {:doc "<pre>\nSettings for validating messages published against a schema.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.SchemaSettings}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.SchemaSettings"
   :gcp.dev/certification {:base-seed 1777050370369
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777050370369 :standard 1777050370370 :stress 1777050370371}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-24T17:06:11.279605810Z"}}
  (:require
   [gcp.global :as global])
  (:import
   (com.google.pubsub.v1 Encoding SchemaSettings SchemaSettings$Builder)))

(declare from-edn to-edn)

(defn ^SchemaSettings from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/SchemaSettings arg)
  (let [builder (SchemaSettings/newBuilder)]
    (when (some? (get arg :encoding))
      (.setEncoding builder (Encoding/valueOf (get arg :encoding))))
    (when (some? (get arg :firstRevisionId))
      (.setFirstRevisionId builder (get arg :firstRevisionId)))
    (when (some? (get arg :lastRevisionId))
      (.setLastRevisionId builder (get arg :lastRevisionId)))
    (when (some? (get arg :schema)) (.setSchema builder (get arg :schema)))
    (.build builder)))

(defn to-edn
  [^SchemaSettings arg]
  {:post [(global/strict! :gcp.pubsub.v1/SchemaSettings %)]}
  (when arg
    (cond-> {:schema (.getSchema arg)}
      (.getEncoding arg) (assoc :encoding (.name (.getEncoding arg)))
      (some->> (.getFirstRevisionId arg)
               (not= ""))
      (assoc :firstRevisionId (.getFirstRevisionId arg))
      (some->> (.getLastRevisionId arg)
               (not= ""))
      (assoc :lastRevisionId (.getLastRevisionId arg)))))

(def schema
  [:map
   {:closed true
    :doc
    "<pre>\nSettings for validating messages published against a schema.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.SchemaSettings}"
    :gcp/category :protobuf-message
    :gcp/key :gcp.pubsub.v1/SchemaSettings}
   [:encoding
    {:optional true
     :getter-doc
     "<pre>\nOptional. The encoding of messages validated against `schema`.\n</pre>\n\n<code>.google.pubsub.v1.Encoding encoding = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The encoding."
     :setter-doc
     "<pre>\nOptional. The encoding of messages validated against `schema`.\n</pre>\n\n<code>.google.pubsub.v1.Encoding encoding = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param value The encoding to set.\n@return This builder for chaining."}
    [:enum {:closed true} "ENCODING_UNSPECIFIED" "JSON" "BINARY"]]
   [:firstRevisionId
    {:optional true
     :getter-doc
     "<pre>\nOptional. The minimum (inclusive) revision allowed for validating messages.\nIf empty or not present, allow any revision to be validated against\nlast_revision or any revision created before.\n</pre>\n\n<code>string first_revision_id = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The firstRevisionId."
     :setter-doc
     "<pre>\nOptional. The minimum (inclusive) revision allowed for validating messages.\nIf empty or not present, allow any revision to be validated against\nlast_revision or any revision created before.\n</pre>\n\n<code>string first_revision_id = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The firstRevisionId to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:lastRevisionId
    {:optional true
     :getter-doc
     "<pre>\nOptional. The maximum (inclusive) revision allowed for validating messages.\nIf empty or not present, allow any revision to be validated against\nfirst_revision or any revision created after.\n</pre>\n\n<code>string last_revision_id = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The lastRevisionId."
     :setter-doc
     "<pre>\nOptional. The maximum (inclusive) revision allowed for validating messages.\nIf empty or not present, allow any revision to be validated against\nfirst_revision or any revision created after.\n</pre>\n\n<code>string last_revision_id = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The lastRevisionId to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:schema
    {:getter-doc
     "<pre>\nRequired. The name of the schema that messages published should be\nvalidated against. Format is `projects/{project}/schemas/{schema}`. The\nvalue of this field will be `_deleted-schema_` if the schema has been\ndeleted.\n</pre>\n\n<code>\nstring schema = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }\n</code>\n\n@return The schema."
     :setter-doc
     "<pre>\nRequired. The name of the schema that messages published should be\nvalidated against. Format is `projects/{project}/schemas/{schema}`. The\nvalue of this field will be `_deleted-schema_` if the schema has been\ndeleted.\n</pre>\n\n<code>\nstring schema = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }\n</code>\n\n@param value The schema to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/SchemaSettings schema}
    {:gcp.global/name "gcp.pubsub.v1.SchemaSettings"}))
