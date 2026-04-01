;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.PartialArg
  {:doc
     "<pre>\nPartial argument value of the function call.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.PartialArg}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.PartialArg"
   :gcp.dev/certification
     {:base-seed 1774824772543
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824772543 :standard 1774824772544 :stress 1774824772545}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:52:53.599668190Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api PartialArg PartialArg$Builder
            PartialArg$DeltaCase]
           [com.google.protobuf NullValue]))

(declare from-edn to-edn DeltaCase-from-edn DeltaCase-to-edn)

(def DeltaCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/PartialArg.DeltaCase} "NULL_VALUE" "NUMBER_VALUE"
   "STRING_VALUE" "BOOL_VALUE" "DELTA_NOT_SET"])

(defn ^PartialArg from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/PartialArg arg)
  (let [builder (PartialArg/newBuilder)]
    (when (some? (get arg :jsonPath))
      (.setJsonPath builder (get arg :jsonPath)))
    (when (some? (get arg :willContinue))
      (.setWillContinue builder (get arg :willContinue)))
    (clojure.core/cond
      (contains? arg :boolValue) (.setBoolValue builder (get arg :boolValue))
      (contains? arg :nullValue)
        (.setNullValue builder (NullValue/valueOf (get arg :nullValue)))
      (contains? arg :numberValue)
        (.setNumberValue builder (double (get arg :numberValue)))
      (contains? arg :stringValue) (.setStringValue builder
                                                    (get arg :stringValue)))
    (.build builder)))

(defn to-edn
  [^PartialArg arg]
  {:post [(global/strict! :gcp.vertexai.api/PartialArg %)]}
  (clojure.core/let [res (cond-> {:jsonPath (.getJsonPath arg)}
                           (.getWillContinue arg) (assoc :willContinue
                                                    (.getWillContinue arg)))
                     res (case (.name (.getDeltaCase arg))
                           "BOOL_VALUE" (clojure.core/assoc res
                                          :boolValue (.getBoolValue arg))
                           "NULL_VALUE" (clojure.core/assoc res
                                          :nullValue (.name (.getNullValue
                                                              arg)))
                           "NUMBER_VALUE" (clojure.core/assoc res
                                            :numberValue (.getNumberValue arg))
                           "STRING_VALUE" (clojure.core/assoc res
                                            :stringValue (.getStringValue arg))
                           res)]
    res))

(def schema
  [:and
   {:closed true,
    :doc
      "<pre>\nPartial argument value of the function call.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.PartialArg}",
    :gcp/category :union-protobuf-oneof,
    :gcp/key :gcp.vertexai.api/PartialArg}
   [:map {:closed true}
    [:boolValue
     {:optional true,
      :getter-doc
        "<pre>\nOptional. Represents a boolean value.\n</pre>\n\n<code>bool bool_value = 5 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The boolValue.",
      :setter-doc
        "<pre>\nOptional. Represents a boolean value.\n</pre>\n\n<code>bool bool_value = 5 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The boolValue to set.\n@return This builder for chaining."}
     :boolean]
    [:jsonPath
     {:getter-doc
        "<pre>\nRequired. A JSON Path (RFC 9535) to the argument being streamed.\nhttps://datatracker.ietf.org/doc/html/rfc9535. e.g. \"$.foo.bar[0].data\".\n</pre>\n\n<code>string json_path = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The jsonPath.",
      :setter-doc
        "<pre>\nRequired. A JSON Path (RFC 9535) to the argument being streamed.\nhttps://datatracker.ietf.org/doc/html/rfc9535. e.g. \"$.foo.bar[0].data\".\n</pre>\n\n<code>string json_path = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The jsonPath to set.\n@return This builder for chaining."}
     [:string {:min 1}]]
    [:nullValue
     {:optional true,
      :getter-doc
        "<pre>\nOptional. Represents a null value.\n</pre>\n\n<code>.google.protobuf.NullValue null_value = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The nullValue.",
      :setter-doc
        "<pre>\nOptional. Represents a null value.\n</pre>\n\n<code>.google.protobuf.NullValue null_value = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param value The nullValue to set.\n@return This builder for chaining."}
     [:enum {:closed true} "NULL_VALUE"]]
    [:numberValue
     {:optional true,
      :getter-doc
        "<pre>\nOptional. Represents a double value.\n</pre>\n\n<code>double number_value = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The numberValue.",
      :setter-doc
        "<pre>\nOptional. Represents a double value.\n</pre>\n\n<code>double number_value = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The numberValue to set.\n@return This builder for chaining."}
     :f64]
    [:stringValue
     {:optional true,
      :getter-doc
        "<pre>\nOptional. Represents a string value.\n</pre>\n\n<code>string string_value = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The stringValue.",
      :setter-doc
        "<pre>\nOptional. Represents a string value.\n</pre>\n\n<code>string string_value = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The stringValue to set.\n@return This builder for chaining."}
     [:string {:min 1}]]
    [:willContinue
     {:optional true,
      :getter-doc
        "<pre>\nOptional. Whether this is not the last part of the same json_path.\nIf true, another PartialArg message for the current json_path is expected\nto follow.\n</pre>\n\n<code>bool will_continue = 6 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The willContinue.",
      :setter-doc
        "<pre>\nOptional. Whether this is not the last part of the same json_path.\nIf true, another PartialArg message for the current json_path is expected\nto follow.\n</pre>\n\n<code>bool will_continue = 6 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The willContinue to set.\n@return This builder for chaining."}
     :boolean]]
   [:or
    [:map
     [:boolValue
      {:optional true,
       :getter-doc
         "<pre>\nOptional. Represents a boolean value.\n</pre>\n\n<code>bool bool_value = 5 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The boolValue.",
       :setter-doc
         "<pre>\nOptional. Represents a boolean value.\n</pre>\n\n<code>bool bool_value = 5 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The boolValue to set.\n@return This builder for chaining."}
      :boolean]]
    [:map
     [:nullValue
      {:optional true,
       :getter-doc
         "<pre>\nOptional. Represents a null value.\n</pre>\n\n<code>.google.protobuf.NullValue null_value = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The nullValue.",
       :setter-doc
         "<pre>\nOptional. Represents a null value.\n</pre>\n\n<code>.google.protobuf.NullValue null_value = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param value The nullValue to set.\n@return This builder for chaining."}
      [:enum {:closed true} "NULL_VALUE"]]]
    [:map
     [:numberValue
      {:optional true,
       :getter-doc
         "<pre>\nOptional. Represents a double value.\n</pre>\n\n<code>double number_value = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The numberValue.",
       :setter-doc
         "<pre>\nOptional. Represents a double value.\n</pre>\n\n<code>double number_value = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The numberValue to set.\n@return This builder for chaining."}
      :f64]]
    [:map
     [:stringValue
      {:optional true,
       :getter-doc
         "<pre>\nOptional. Represents a string value.\n</pre>\n\n<code>string string_value = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The stringValue.",
       :setter-doc
         "<pre>\nOptional. Represents a string value.\n</pre>\n\n<code>string string_value = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The stringValue to set.\n@return This builder for chaining."}
      [:string {:min 1}]]]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/PartialArg schema,
              :gcp.vertexai.api/PartialArg.DeltaCase DeltaCase-schema}
    {:gcp.global/name "gcp.vertexai.api.PartialArg"}))