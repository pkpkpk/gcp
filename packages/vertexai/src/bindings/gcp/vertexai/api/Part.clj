;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.Part
  {:doc
     "<pre>\nA datatype containing media that is part of a multi-part `Content` message.\n\nA `Part` consists of data which has an associated datatype. A `Part` can only\ncontain one of the accepted types in `Part.data`.\n\nA `Part` must have a fixed IANA MIME type identifying the type and subtype\nof the media if `inline_data` or `file_data` field is filled with raw bytes.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Part}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.Part"
   :gcp.dev/certification
     {:base-seed 1776627474729
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627474729 :standard 1776627474730 :stress 1776627474731}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:37:56.514878050Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global]
            [gcp.vertexai.api.Blob :as Blob]
            [gcp.vertexai.api.CodeExecutionResult :as CodeExecutionResult]
            [gcp.vertexai.api.ExecutableCode :as ExecutableCode]
            [gcp.vertexai.api.FileData :as FileData]
            [gcp.vertexai.api.FunctionCall :as FunctionCall]
            [gcp.vertexai.api.FunctionResponse :as FunctionResponse]
            [gcp.vertexai.api.VideoMetadata :as VideoMetadata])
  (:import [com.google.cloud.vertexai.api Part Part$Builder Part$DataCase
            Part$MetadataCase]
           [com.google.protobuf ByteString]))

(declare from-edn
         to-edn
         DataCase-from-edn
         DataCase-to-edn
         MetadataCase-from-edn
         MetadataCase-to-edn)

(def DataCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/Part.DataCase} "TEXT" "INLINE_DATA" "FILE_DATA"
   "FUNCTION_CALL" "FUNCTION_RESPONSE" "EXECUTABLE_CODE" "CODE_EXECUTION_RESULT"
   "DATA_NOT_SET"])

(def MetadataCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/Part.MetadataCase} "VIDEO_METADATA"
   "METADATA_NOT_SET"])

(defn ^Part from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/Part arg)
  (let [builder (Part/newBuilder)]
    (when (some? (get arg :thought)) (.setThought builder (get arg :thought)))
    (when (some? (get arg :thoughtSignature))
      (.setThoughtSignature builder
                            (protobuf/ByteString-from-edn
                              (get arg :thoughtSignature))))
    (cond (contains? arg :codeExecutionResult)
            (.setCodeExecutionResult builder
                                     (CodeExecutionResult/from-edn
                                       (get arg :codeExecutionResult)))
          (contains? arg :executableCode) (.setExecutableCode
                                            builder
                                            (ExecutableCode/from-edn
                                              (get arg :executableCode)))
          (contains? arg :fileData)
            (.setFileData builder (FileData/from-edn (get arg :fileData)))
          (contains? arg :functionCall)
            (.setFunctionCall builder
                              (FunctionCall/from-edn (get arg :functionCall)))
          (contains? arg :functionResponse) (.setFunctionResponse
                                              builder
                                              (FunctionResponse/from-edn
                                                (get arg :functionResponse)))
          (contains? arg :inlineData)
            (.setInlineData builder (Blob/from-edn (get arg :inlineData)))
          (contains? arg :text) (.setText builder (get arg :text)))
    (cond (contains? arg :videoMetadata) (.setVideoMetadata
                                           builder
                                           (VideoMetadata/from-edn
                                             (get arg :videoMetadata))))
    (.build builder)))

(defn to-edn
  [^Part arg]
  {:post [(global/strict! :gcp.vertexai.api/Part %)]}
  (when arg
    (let [res (cond-> {}
                (.getThought arg) (assoc :thought (.getThought arg))
                (.getThoughtSignature arg) (assoc :thoughtSignature
                                             (protobuf/ByteString-to-edn
                                               (.getThoughtSignature arg))))
          res (case (.name (.getDataCase arg))
                "CODE_EXECUTION_RESULT" (assoc res
                                          :codeExecutionResult
                                            (CodeExecutionResult/to-edn
                                              (.getCodeExecutionResult arg)))
                "EXECUTABLE_CODE" (assoc res
                                    :executableCode (ExecutableCode/to-edn
                                                      (.getExecutableCode arg)))
                "FILE_DATA" (assoc res
                              :fileData (FileData/to-edn (.getFileData arg)))
                "FUNCTION_CALL" (assoc res
                                  :functionCall (FunctionCall/to-edn
                                                  (.getFunctionCall arg)))
                "FUNCTION_RESPONSE" (assoc res
                                      :functionResponse (FunctionResponse/to-edn
                                                          (.getFunctionResponse
                                                            arg)))
                "INLINE_DATA" (assoc res
                                :inlineData (Blob/to-edn (.getInlineData arg)))
                "TEXT" (assoc res :text (.getText arg))
                res)
          res (case (.name (.getMetadataCase arg))
                "VIDEO_METADATA" (assoc res
                                   :videoMetadata (VideoMetadata/to-edn
                                                    (.getVideoMetadata arg)))
                res)]
      res)))

(def schema
  [:and
   {:closed true,
    :doc
      "<pre>\nA datatype containing media that is part of a multi-part `Content` message.\n\nA `Part` consists of data which has an associated datatype. A `Part` can only\ncontain one of the accepted types in `Part.data`.\n\nA `Part` must have a fixed IANA MIME type identifying the type and subtype\nof the media if `inline_data` or `file_data` field is filled with raw bytes.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Part}",
    :gcp/category :union-protobuf-oneof,
    :gcp/key :gcp.vertexai.api/Part}
   [:map {:closed true}
    [:codeExecutionResult
     {:optional true,
      :getter-doc
        "<pre>\nOptional. Result of executing the [ExecutableCode].\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.CodeExecutionResult code_execution_result = 9 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The codeExecutionResult.",
      :setter-doc
        "<pre>\nOptional. Result of executing the [ExecutableCode].\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.CodeExecutionResult code_execution_result = 9 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     :gcp.vertexai.api/CodeExecutionResult]
    [:executableCode
     {:optional true,
      :getter-doc
        "<pre>\nOptional. Code generated by the model that is meant to be executed.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.ExecutableCode executable_code = 8 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The executableCode.",
      :setter-doc
        "<pre>\nOptional. Code generated by the model that is meant to be executed.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.ExecutableCode executable_code = 8 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     :gcp.vertexai.api/ExecutableCode]
    [:fileData
     {:optional true,
      :getter-doc
        "<pre>\nOptional. URI based data.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.FileData file_data = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The fileData.",
      :setter-doc
        "<pre>\nOptional. URI based data.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.FileData file_data = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     :gcp.vertexai.api/FileData]
    [:functionCall
     {:optional true,
      :getter-doc
        "<pre>\nOptional. A predicted [FunctionCall] returned from the model that\ncontains a string representing the [FunctionDeclaration.name] with the\nparameters and their values.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.FunctionCall function_call = 5 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The functionCall.",
      :setter-doc
        "<pre>\nOptional. A predicted [FunctionCall] returned from the model that\ncontains a string representing the [FunctionDeclaration.name] with the\nparameters and their values.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.FunctionCall function_call = 5 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     :gcp.vertexai.api/FunctionCall]
    [:functionResponse
     {:optional true,
      :getter-doc
        "<pre>\nOptional. The result output of a [FunctionCall] that contains a string\nrepresenting the [FunctionDeclaration.name] and a structured JSON object\ncontaining any output from the function call. It is used as context to\nthe model.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.FunctionResponse function_response = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The functionResponse.",
      :setter-doc
        "<pre>\nOptional. The result output of a [FunctionCall] that contains a string\nrepresenting the [FunctionDeclaration.name] and a structured JSON object\ncontaining any output from the function call. It is used as context to\nthe model.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.FunctionResponse function_response = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     :gcp.vertexai.api/FunctionResponse]
    [:inlineData
     {:optional true,
      :getter-doc
        "<pre>\nOptional. Inlined bytes data.\n</pre>\n\n<code>.google.cloud.vertexai.v1.Blob inline_data = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The inlineData.",
      :setter-doc
        "<pre>\nOptional. Inlined bytes data.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.Blob inline_data = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     :gcp.vertexai.api/Blob]
    [:text
     {:optional true,
      :getter-doc
        "<pre>\nOptional. Text part (can be code).\n</pre>\n\n<code>string text = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The text.",
      :setter-doc
        "<pre>\nOptional. Text part (can be code).\n</pre>\n\n<code>string text = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The text to set.\n@return This builder for chaining."}
     [:string {:min 1, :gen/max 1}]]
    [:thought
     {:optional true,
      :getter-doc
        "<pre>\nIndicates if the part is thought from the model.\n</pre>\n\n<code>bool thought = 10 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The thought.",
      :setter-doc
        "<pre>\nIndicates if the part is thought from the model.\n</pre>\n\n<code>bool thought = 10 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The thought to set.\n@return This builder for chaining."}
     :boolean]
    [:thoughtSignature
     {:optional true,
      :getter-doc
        "<pre>\nAn opaque signature for the thought so it can be reused in subsequent\nrequests.\n</pre>\n\n<code>bytes thought_signature = 11 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The thoughtSignature.",
      :setter-doc
        "<pre>\nAn opaque signature for the thought so it can be reused in subsequent\nrequests.\n</pre>\n\n<code>bytes thought_signature = 11 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The thoughtSignature to set.\n@return This builder for chaining."}
     :gcp.foreign.com.google.protobuf/ByteString]
    [:videoMetadata
     {:optional true,
      :getter-doc
        "<pre>\nOptional. Video metadata. The metadata should only be specified while the\nvideo data is presented in inline_data or file_data.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.VideoMetadata video_metadata = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The videoMetadata.",
      :setter-doc
        "<pre>\nOptional. Video metadata. The metadata should only be specified while the\nvideo data is presented in inline_data or file_data.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.VideoMetadata video_metadata = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     :gcp.vertexai.api/VideoMetadata]]
   [:fn
    {:error/message
       "Only one of these keys may be present: #{:inlineData :codeExecutionResult :executableCode :functionCall :fileData :functionResponse :text}"}
    (quote (fn [m]
             (<= (count (filter (set (keys m))
                          #{:inlineData :codeExecutionResult :executableCode
                            :functionCall :fileData :functionResponse :text}))
                 1)))]
   [:fn
    {:error/message "Only one of these keys may be present: #{:videoMetadata}"}
    (quote (fn [m] (<= (count (filter (set (keys m)) #{:videoMetadata})) 1)))]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/Part schema,
              :gcp.vertexai.api/Part.DataCase DataCase-schema,
              :gcp.vertexai.api/Part.MetadataCase MetadataCase-schema}
    {:gcp.global/name "gcp.vertexai.api.Part"}))