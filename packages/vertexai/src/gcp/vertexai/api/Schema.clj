;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.Schema
  {:doc
     "<pre>\nSchema is used to define the format of input/output data. Represents a select\nsubset of an [OpenAPI 3.0 schema\nobject](https://spec.openapis.org/oas/v3.0.3#schema-object). More fields may\nbe added in the future as needed.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Schema}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.Schema"
   :gcp.dev/certification
     {:base-seed 1774824595461
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824595461 :standard 1774824595462 :stress 1774824595463}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:50:10.104335738Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api Schema Schema$Builder Type]
           [com.google.protobuf ProtocolStringList Value]))

(declare from-edn to-edn)

(defn ^Schema from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/Schema arg)
  (let [builder (Schema/newBuilder)]
    (when (some? (get arg :additionalProperties))
      (.setAdditionalProperties builder
                                (protobuf/Value-from-edn
                                  (get arg :additionalProperties))))
    (when (seq (get arg :anyOf))
      (.addAllAnyOf builder (map from-edn (get arg :anyOf))))
    (when (some? (get arg :default))
      (.setDefault builder (protobuf/Value-from-edn (get arg :default))))
    (when (seq (get arg :defs))
      (.putAllDefs builder
                   (into {}
                         (map (fn [[k v]] [(name k) (from-edn v)]))
                         (get arg :putAllDefs))))
    (when (some? (get arg :description))
      (.setDescription builder (get arg :description)))
    (when (seq (get arg :enum)) (.addAllEnum builder (seq (get arg :enum))))
    (when (some? (get arg :example))
      (.setExample builder (protobuf/Value-from-edn (get arg :example))))
    (when (some? (get arg :format)) (.setFormat builder (get arg :format)))
    (when (some? (get arg :items))
      (.setItems builder (from-edn (get arg :items))))
    (when (some? (get arg :maxItems))
      (.setMaxItems builder (long (get arg :maxItems))))
    (when (some? (get arg :maxLength))
      (.setMaxLength builder (long (get arg :maxLength))))
    (when (some? (get arg :maxProperties))
      (.setMaxProperties builder (long (get arg :maxProperties))))
    (when (some? (get arg :maximum))
      (.setMaximum builder (double (get arg :maximum))))
    (when (some? (get arg :minItems))
      (.setMinItems builder (long (get arg :minItems))))
    (when (some? (get arg :minLength))
      (.setMinLength builder (long (get arg :minLength))))
    (when (some? (get arg :minProperties))
      (.setMinProperties builder (long (get arg :minProperties))))
    (when (some? (get arg :minimum))
      (.setMinimum builder (double (get arg :minimum))))
    (when (some? (get arg :nullable))
      (.setNullable builder (get arg :nullable)))
    (when (some? (get arg :pattern)) (.setPattern builder (get arg :pattern)))
    (when (seq (get arg :properties))
      (.putAllProperties builder
                         (into {}
                               (map (fn [[k v]] [(name k) (from-edn v)]))
                               (get arg :putAllProperties))))
    (when (seq (get arg :propertyOrdering))
      (.addAllPropertyOrdering builder (seq (get arg :propertyOrdering))))
    (when (some? (get arg :ref)) (.setRef builder (get arg :ref)))
    (when (seq (get arg :required))
      (.addAllRequired builder (seq (get arg :required))))
    (when (some? (get arg :title)) (.setTitle builder (get arg :title)))
    (when (some? (get arg :type))
      (.setType builder (Type/valueOf (get arg :type))))
    (.build builder)))

(defn to-edn
  [^Schema arg]
  {:post [(global/strict! :gcp.vertexai.api/Schema %)]}
  (cond-> {}
    (.hasAdditionalProperties arg) (assoc :additionalProperties
                                     (protobuf/Value-to-edn
                                       (.getAdditionalProperties arg)))
    (seq (.getAnyOfList arg)) (assoc :anyOf (map to-edn (.getAnyOfList arg)))
    (.hasDefault arg) (assoc :default (protobuf/Value-to-edn (.getDefault arg)))
    (seq (.getDefsMap arg))
      (assoc :defs
        (into {} (map (fn [[k v]] [(keyword k) (to-edn v)])) (.getDefsMap arg)))
    (some->> (.getDescription arg)
             (not= ""))
      (assoc :description (.getDescription arg))
    (seq (.getEnumList arg))
      (assoc :enum (protobuf/ProtocolStringList-to-edn (.getEnumList arg)))
    (.hasExample arg) (assoc :example (protobuf/Value-to-edn (.getExample arg)))
    (some->> (.getFormat arg)
             (not= ""))
      (assoc :format (.getFormat arg))
    (.hasItems arg) (assoc :items (to-edn (.getItems arg)))
    (.getMaxItems arg) (assoc :maxItems (.getMaxItems arg))
    (.getMaxLength arg) (assoc :maxLength (.getMaxLength arg))
    (.getMaxProperties arg) (assoc :maxProperties (.getMaxProperties arg))
    (.getMaximum arg) (assoc :maximum (.getMaximum arg))
    (.getMinItems arg) (assoc :minItems (.getMinItems arg))
    (.getMinLength arg) (assoc :minLength (.getMinLength arg))
    (.getMinProperties arg) (assoc :minProperties (.getMinProperties arg))
    (.getMinimum arg) (assoc :minimum (.getMinimum arg))
    (.getNullable arg) (assoc :nullable (.getNullable arg))
    (some->> (.getPattern arg)
             (not= ""))
      (assoc :pattern (.getPattern arg))
    (seq (.getPropertiesMap arg)) (assoc :properties
                                    (into {}
                                          (map (fn [[k v]] [(keyword k)
                                                            (to-edn v)]))
                                          (.getPropertiesMap arg)))
    (seq (.getPropertyOrderingList arg)) (assoc :propertyOrdering
                                           (protobuf/ProtocolStringList-to-edn
                                             (.getPropertyOrderingList arg)))
    (some->> (.getRef arg)
             (not= ""))
      (assoc :ref (.getRef arg))
    (seq (.getRequiredList arg)) (assoc :required
                                   (protobuf/ProtocolStringList-to-edn
                                     (.getRequiredList arg)))
    (some->> (.getTitle arg)
             (not= ""))
      (assoc :title (.getTitle arg))
    (.getType arg) (assoc :type (.name (.getType arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nSchema is used to define the format of input/output data. Represents a select\nsubset of an [OpenAPI 3.0 schema\nobject](https://spec.openapis.org/oas/v3.0.3#schema-object). More fields may\nbe added in the future as needed.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Schema}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/Schema}
   [:additionalProperties
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Can either be a boolean or an object; controls the presence of\nadditional properties.\n</pre>\n\n<code>\n.google.protobuf.Value additional_properties = 26 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The additionalProperties.",
     :setter-doc
       "<pre>\nOptional. Can either be a boolean or an object; controls the presence of\nadditional properties.\n</pre>\n\n<code>\n.google.protobuf.Value additional_properties = 26 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.foreign.com.google.protobuf/Value]
   [:anyOf
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The value should be validated against any (one or more) of the\nsubschemas in the list.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.Schema any_of = 11 [(.google.api.field_behavior) = OPTIONAL];\n</code>",
     :setter-doc
       "<pre>\nOptional. The value should be validated against any (one or more) of the\nsubschemas in the list.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.Schema any_of = 11 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:sequential {:min 1} [:ref :gcp.vertexai.api/Schema]]]
   [:default
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Default value of the data.\n</pre>\n\n<code>.google.protobuf.Value default = 23 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The default.",
     :setter-doc
       "<pre>\nOptional. Default value of the data.\n</pre>\n\n<code>.google.protobuf.Value default = 23 [(.google.api.field_behavior) = OPTIONAL];</code>"}
    :gcp.foreign.com.google.protobuf/Value]
   [:defs
    {:optional true,
     :getter-doc
       "<pre>\nOptional. A map of definitions for use by `ref`\nOnly allowed at the root of the schema.\n</pre>\n\n<code>\nmap&lt;string, .google.cloud.vertexai.v1.Schema&gt; defs = 28 [(.google.api.field_behavior) = OPTIONAL];\n</code>",
     :setter-doc
       "<pre>\nOptional. A map of definitions for use by `ref`\nOnly allowed at the root of the schema.\n</pre>\n\n<code>\nmap&lt;string, .google.cloud.vertexai.v1.Schema&gt; defs = 28 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:map-of [:or simple-keyword? [:string {:min 1}]]
     [:ref :gcp.vertexai.api/Schema]]]
   [:description
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The description of the data.\n</pre>\n\n<code>string description = 8 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The description.",
     :setter-doc
       "<pre>\nOptional. The description of the data.\n</pre>\n\n<code>string description = 8 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The description to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:enum
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Possible values of the element of primitive type with enum\nformat. Examples:\n1. We can define direction as :\n{type:STRING, format:enum, enum:[\"EAST\", NORTH\", \"SOUTH\", \"WEST\"]}\n2. We can define apartment number as :\n{type:INTEGER, format:enum, enum:[\"101\", \"201\", \"301\"]}\n</pre>\n\n<code>repeated string enum = 9 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return A list containing the enum.",
     :setter-doc
       "<pre>\nOptional. Possible values of the element of primitive type with enum\nformat. Examples:\n1. We can define direction as :\n{type:STRING, format:enum, enum:[\"EAST\", NORTH\", \"SOUTH\", \"WEST\"]}\n2. We can define apartment number as :\n{type:INTEGER, format:enum, enum:[\"101\", \"201\", \"301\"]}\n</pre>\n\n<code>repeated string enum = 9 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param values The enum to add.\n@return This builder for chaining."}
    :gcp.foreign.com.google.protobuf/ProtocolStringList]
   [:example
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Example of the object. Will only populated when the object is the\nroot.\n</pre>\n\n<code>.google.protobuf.Value example = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The example.",
     :setter-doc
       "<pre>\nOptional. Example of the object. Will only populated when the object is the\nroot.\n</pre>\n\n<code>.google.protobuf.Value example = 4 [(.google.api.field_behavior) = OPTIONAL];</code>"}
    :gcp.foreign.com.google.protobuf/Value]
   [:format
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The format of the data.\nSupported formats:\nfor NUMBER type: \"float\", \"double\"\nfor INTEGER type: \"int32\", \"int64\"\nfor STRING type: \"email\", \"byte\", etc\n</pre>\n\n<code>string format = 7 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The format.",
     :setter-doc
       "<pre>\nOptional. The format of the data.\nSupported formats:\nfor NUMBER type: \"float\", \"double\"\nfor INTEGER type: \"int32\", \"int64\"\nfor STRING type: \"email\", \"byte\", etc\n</pre>\n\n<code>string format = 7 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The format to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:items
    {:optional true,
     :getter-doc
       "<pre>\nOptional. SCHEMA FIELDS FOR TYPE ARRAY\nSchema of the elements of Type.ARRAY.\n</pre>\n\n<code>.google.cloud.vertexai.v1.Schema items = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The items.",
     :setter-doc
       "<pre>\nOptional. SCHEMA FIELDS FOR TYPE ARRAY\nSchema of the elements of Type.ARRAY.\n</pre>\n\n<code>.google.cloud.vertexai.v1.Schema items = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:ref :gcp.vertexai.api/Schema]]
   [:maxItems
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Maximum number of the elements for Type.ARRAY.\n</pre>\n\n<code>int64 max_items = 22 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The maxItems.",
     :setter-doc
       "<pre>\nOptional. Maximum number of the elements for Type.ARRAY.\n</pre>\n\n<code>int64 max_items = 22 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The maxItems to set.\n@return This builder for chaining."}
    :i64]
   [:maxLength
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Maximum length of the Type.STRING\n</pre>\n\n<code>int64 max_length = 19 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The maxLength.",
     :setter-doc
       "<pre>\nOptional. Maximum length of the Type.STRING\n</pre>\n\n<code>int64 max_length = 19 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The maxLength to set.\n@return This builder for chaining."}
    :i64]
   [:maxProperties
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Maximum number of the properties for Type.OBJECT.\n</pre>\n\n<code>int64 max_properties = 15 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The maxProperties.",
     :setter-doc
       "<pre>\nOptional. Maximum number of the properties for Type.OBJECT.\n</pre>\n\n<code>int64 max_properties = 15 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The maxProperties to set.\n@return This builder for chaining."}
    :i64]
   [:maximum
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Maximum value of the Type.INTEGER and Type.NUMBER\n</pre>\n\n<code>double maximum = 17 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The maximum.",
     :setter-doc
       "<pre>\nOptional. Maximum value of the Type.INTEGER and Type.NUMBER\n</pre>\n\n<code>double maximum = 17 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The maximum to set.\n@return This builder for chaining."}
    :f64]
   [:minItems
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Minimum number of the elements for Type.ARRAY.\n</pre>\n\n<code>int64 min_items = 21 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The minItems.",
     :setter-doc
       "<pre>\nOptional. Minimum number of the elements for Type.ARRAY.\n</pre>\n\n<code>int64 min_items = 21 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The minItems to set.\n@return This builder for chaining."}
    :i64]
   [:minLength
    {:optional true,
     :getter-doc
       "<pre>\nOptional. SCHEMA FIELDS FOR TYPE STRING\nMinimum length of the Type.STRING\n</pre>\n\n<code>int64 min_length = 18 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The minLength.",
     :setter-doc
       "<pre>\nOptional. SCHEMA FIELDS FOR TYPE STRING\nMinimum length of the Type.STRING\n</pre>\n\n<code>int64 min_length = 18 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The minLength to set.\n@return This builder for chaining."}
    :i64]
   [:minProperties
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Minimum number of the properties for Type.OBJECT.\n</pre>\n\n<code>int64 min_properties = 14 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The minProperties.",
     :setter-doc
       "<pre>\nOptional. Minimum number of the properties for Type.OBJECT.\n</pre>\n\n<code>int64 min_properties = 14 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The minProperties to set.\n@return This builder for chaining."}
    :i64]
   [:minimum
    {:optional true,
     :getter-doc
       "<pre>\nOptional. SCHEMA FIELDS FOR TYPE INTEGER and NUMBER\nMinimum value of the Type.INTEGER and Type.NUMBER\n</pre>\n\n<code>double minimum = 16 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The minimum.",
     :setter-doc
       "<pre>\nOptional. SCHEMA FIELDS FOR TYPE INTEGER and NUMBER\nMinimum value of the Type.INTEGER and Type.NUMBER\n</pre>\n\n<code>double minimum = 16 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The minimum to set.\n@return This builder for chaining."}
    :f64]
   [:nullable
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Indicates if the value may be null.\n</pre>\n\n<code>bool nullable = 6 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The nullable.",
     :setter-doc
       "<pre>\nOptional. Indicates if the value may be null.\n</pre>\n\n<code>bool nullable = 6 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The nullable to set.\n@return This builder for chaining."}
    :boolean]
   [:pattern
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Pattern of the Type.STRING to restrict a string to a regular\nexpression.\n</pre>\n\n<code>string pattern = 20 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The pattern.",
     :setter-doc
       "<pre>\nOptional. Pattern of the Type.STRING to restrict a string to a regular\nexpression.\n</pre>\n\n<code>string pattern = 20 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The pattern to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:properties
    {:optional true,
     :getter-doc
       "<pre>\nOptional. SCHEMA FIELDS FOR TYPE OBJECT\nProperties of Type.OBJECT.\n</pre>\n\n<code>\nmap&lt;string, .google.cloud.vertexai.v1.Schema&gt; properties = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>",
     :setter-doc
       "<pre>\nOptional. SCHEMA FIELDS FOR TYPE OBJECT\nProperties of Type.OBJECT.\n</pre>\n\n<code>\nmap&lt;string, .google.cloud.vertexai.v1.Schema&gt; properties = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:map-of [:or simple-keyword? [:string {:min 1}]]
     [:ref :gcp.vertexai.api/Schema]]]
   [:propertyOrdering
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The order of the properties.\nNot a standard field in open api spec. Only used to support the order of\nthe properties.\n</pre>\n\n<code>repeated string property_ordering = 25 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return A list containing the propertyOrdering.",
     :setter-doc
       "<pre>\nOptional. The order of the properties.\nNot a standard field in open api spec. Only used to support the order of\nthe properties.\n</pre>\n\n<code>repeated string property_ordering = 25 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param values The propertyOrdering to add.\n@return This builder for chaining."}
    :gcp.foreign.com.google.protobuf/ProtocolStringList]
   [:ref
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Allows indirect references between schema nodes. The value should\nbe a valid reference to a child of the root `defs`.\n\nFor example, the following schema defines a reference to a schema node\nnamed \"Pet\":\n\ntype: object\nproperties:\npet:\nref: #/defs/Pet\ndefs:\nPet:\ntype: object\nproperties:\nname:\ntype: string\n\nThe value of the \"pet\" property is a reference to the schema node\nnamed \"Pet\".\nSee details in\nhttps://json-schema.org/understanding-json-schema/structuring\n</pre>\n\n<code>string ref = 27 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The ref.",
     :setter-doc
       "<pre>\nOptional. Allows indirect references between schema nodes. The value should\nbe a valid reference to a child of the root `defs`.\n\nFor example, the following schema defines a reference to a schema node\nnamed \"Pet\":\n\ntype: object\nproperties:\npet:\nref: #/defs/Pet\ndefs:\nPet:\ntype: object\nproperties:\nname:\ntype: string\n\nThe value of the \"pet\" property is a reference to the schema node\nnamed \"Pet\".\nSee details in\nhttps://json-schema.org/understanding-json-schema/structuring\n</pre>\n\n<code>string ref = 27 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The ref to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:required
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Required properties of Type.OBJECT.\n</pre>\n\n<code>repeated string required = 5 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return A list containing the required.",
     :setter-doc
       "<pre>\nOptional. Required properties of Type.OBJECT.\n</pre>\n\n<code>repeated string required = 5 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param values The required to add.\n@return This builder for chaining."}
    :gcp.foreign.com.google.protobuf/ProtocolStringList]
   [:title
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The title of the Schema.\n</pre>\n\n<code>string title = 24 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The title.",
     :setter-doc
       "<pre>\nOptional. The title of the Schema.\n</pre>\n\n<code>string title = 24 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The title to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:type
    {:optional true,
     :getter-doc
       "<pre>\nOptional. The type of the data.\n</pre>\n\n<code>.google.cloud.vertexai.v1.Type type = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The type.",
     :setter-doc
       "<pre>\nOptional. The type of the data.\n</pre>\n\n<code>.google.cloud.vertexai.v1.Type type = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param value The type to set.\n@return This builder for chaining."}
    [:enum {:closed true} "TYPE_UNSPECIFIED" "STRING" "NUMBER" "INTEGER"
     "BOOLEAN" "ARRAY" "OBJECT"]]])

(global/include-schema-registry! (with-meta {:gcp.vertexai.api/Schema schema}
                                   {:gcp.global/name
                                      "gcp.vertexai.api.Schema"}))