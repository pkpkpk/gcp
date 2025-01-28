(ns gcp.vertexai.v1
  (:require [clojure.string :as string]
            gcp.auth
            [gcp.global :as global]
            [gcp.vertexai.v1.generativeai.protocols :refer [IHistory]]
            gcp.protobuf)
  (:import (com.google.cloud.vertexai VertexAI)
           (com.google.gson JsonObject)
           (com.google.protobuf Value)
           (java.lang.reflect Modifier)))

;; TODO
;; emit function schemas
;; switch to versioned ie :vertexai.v1.generativeai/foo
;; malli schema registry
;; registries for protobuf etc
;; can we derive :Vertex.api/Schema from malli schemas

(def ^{:package/url "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api"}
  registry
  {:vertexai.synth/Valueable                     {}
   :vertexai.synth/DeclarableFunction            {}
   :vertexai.generativeai/generate-content       {}
   :vertexai.generativeai/generate-content-async {}
   :vertexai.generativeai/generate-content-seq   {}
   :vertexai.generativeai/chat                   {}
   :vertexai.generativeai/send-msg               {}
   :vertexai.generativeai/send-msg-stream        {}
   :vertexai.generativeai/clone-chat             {}
   :vertexai.generativeai/history-clone          {}
   #!--------------
   :vertexai.synth/Contentable                   [:or
                                                  {:error/message "Contentable should be singular or many Contents like (string|vector<Part>|{:user 'model' :parts [Part...]})"}
                                                  :vertexai.api/Content [:sequential :vertexai.api/Content]]
   :vertexai.synth/ModelConfig                   [:map
                                                  [:generationConfig {:optional true} :vertexai.api/GenerationConfig]
                                                  [:model :string]
                                                  [:safetySettings {:optional true} [:sequential :vertexai.api/SafetySetting]]
                                                  [:systemInstruction {:optional true} :vertexai.api/Content]
                                                  [:toolConfig {:optional true} :vertexai.api/ToolConfig]
                                                  [:tools {:optional true} [:sequential :vertexai.api/Tool]]]

   :vertexai.synth/Clientable                    [:or (global/instance-schema VertexAI) :vertexai/VertexAI]

   :vertexai.synth/Requestable                   [:and
                                                  {:doc "model config + :contents + client"}
                                                  :vertexai.api/GenerateContentRequest
                                                  [:map
                                                   [:vertexai {:optional false} :vertexai.synth/Clientable]]]

   :vertexai.synth/ChatSession                   [:and
                                                  :vertexai.synth/ModelConfig
                                                  [:map
                                                   ;;; TODO library can be f(string) -> f(Struct) -> Struct
                                                   ;;; what is best way to schema this, can we sync w/ tools
                                                   [:library {:optional true} [:map-of :string fn?]]
                                                   [:history {:optional false} (global/satisfies-schema IHistory)]
                                                   [:rootChat {:optional true} [:ref :vertexai.synth/ChatSession]]
                                                   [:*previousHistorySize {:optional true} (global/instance-schema clojure.lang.Atom)]
                                                   [:*responderState {:optional false} [:and (global/instance-schema clojure.lang.Atom)
                                                                                        [:fn #(number? (:maxCalls (deref %)))]]]
                                                   [:*currentResponseStream {:optional true} (global/instance-schema clojure.lang.Atom)]
                                                   [:*currentResponse {:optional true} (global/instance-schema clojure.lang.Atom)]]]

   :vertexai/datastore-resource-id               [:and
                                                  :string
                                                  [:fn
                                                   {:error/message "datastore-resource-id must conform to format 'projects/{project}/locations/{location}/collections/{collection}/dataStores/{dataStore}'"}
                                                   (fn [s]
                                                     (let [parts (string/split s #"/")]
                                                       (and
                                                         (= "projects" (nth parts 0))
                                                         (some? (nth parts 1))
                                                         (= "locations" (nth parts 2))
                                                         (some? (nth parts 3))
                                                         (= "collections" (nth parts 4))
                                                         (some? (nth parts 5))
                                                         (= "dataStores" (nth parts 6))
                                                         (some? (nth parts 7)))))]]

   :vertexai/VertexAI                            [:map {:closed    true
                                                        :doc       "This class holds default settings and credentials to make Vertex AI API calls. Note: The VertexAI instance will start a service client when the first API call is made. Please close the VertexAI instance after making any API calls so that clients get closed as well."
                                                        :class     'com.google.cloud.vertexai.VertexAI
                                                        :class/url "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.VertexAI"
                                                        :ns        'gcp.vertexai.v1.VertexAI
                                                        :from-edn  'gcp.vertexai.v1.VertexAI/from-edn
                                                        :ig/halt!  'gcp.vertexai.v1.VertexAI/halt!}
                                                  [:apiEndpoint {:optional true} :string]
                                                  [:credentials {:optional true} :gcp.auth/Credentials]
                                                  [:customHeaders {:optional true} [:map-of :string :string]]
                                                  [:llmClientSupplier {:optional true} :any]
                                                  [:location {:optional true} :string]
                                                  [:predictionClientSupplier {:optional true} :any]
                                                  [:projectId {:optional true} :string]
                                                  [:scopes {:optional true} [:sequential :string]]
                                                  [:transport {:optional true}
                                                   [:or
                                                    [:=
                                                     {:doc "When used, the clients will send gRPC to the backing service. This is usually more efficient and is the default transport."}
                                                     "GRPC"]
                                                    [:=
                                                     {:doc "When used, the clients will send REST requests to the backing service."}
                                                     "REST"]]]]

   :vertexai.api/Content                         [:or
                                                  {:doc              "The base structured datatype containing multi-part content of a message. A Content includes a role field designating the producer of the Content and a parts field containing multi-part data that contains the content of the message turn"
                                                   :error/message    "Content must be (string|vector<Part>|{:user 'model' :parts [Part...]})"
                                                   :class            'com.google.cloud.vertexai.api.Content
                                                   :ns               'gcp.vertexai.v1.api.Content
                                                   :from-edn         'gcp.vertexai.v1.api.Content/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.Content/to-edn
                                                   :generativeai/url "https://ai.google.dev/api/caching#Content"
                                                   :protobuf/type    "google.cloud.vertexai.v1.Content"
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Content"}
                                                  :string
                                                  [:sequential {:doc "part literals w/ role 'user'"} :vertexai.api/Part]
                                                  [:map
                                                   {:doc "full specification"}
                                                   [:role
                                                    {:description "The producer of the content. Must be either 'user' or 'model'. Useful to set for multi-turn conversations, otherwise can be left blank or unset."
                                                     :optional    true}
                                                    [:enum "user" "model"]]
                                                   [:parts
                                                    {:optional false}
                                                    [:sequential :vertexai.api/Part]]]]

   :vertexai.api/Part                            [:or
                                                  {:ns               'gcp.vertexai.v1.api.Part
                                                   :from-edn         'gcp.vertexai.v1.api.Part/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.Part/to-edn
                                                   :doc              "A (union) datatype containing media that is part of a multi-part Content message. A Part consists of data which has an associated datatype. A Part can only contain one of the accepted types in Part.data. A Part must have a fixed IANA MIME type identifying the type and subtype of the media if the inlineData field is filled with raw bytes."
                                                   :generativeai/url "https://ai.google.dev/api/caching#Part"
                                                   :protobuf/type    "google.cloud.vertexai.v1.Part"
                                                   :class            'com.google.cloud.vertexai.api.Part
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Part"}
                                                  :string
                                                  [:map {:closed true} ;; TODO this might not be JSON safe
                                                   [:mimeType {:optional false} :string]
                                                   [:partData
                                                    {:doc      "(string|URI<gcs>|bytes|ByteString"
                                                     :optional false}
                                                    [:or bytes? string? uri? :gcp.protobuf/ByteString]]]
                                                  [:map {:closed true}
                                                   [:text {:optional false} :string]]
                                                  [:map {:closed true}
                                                   [:inlineData {:optional false} :vertexai.api/Blob]]
                                                  [:map {:closed true}
                                                   [:functionCall {:optional false} :vertexai.api/FunctionCall]]
                                                  [:map {:closed true}
                                                   [:functionResponse {:optional false} :vertexai.api/FunctionResponse]]
                                                  [:map {:closed true}
                                                   [:fileData {:optional false} :vertexai.api/FileData]]]

   :vertexai.api/Blob                            [:map
                                                  {:ns               'gcp.vertexai.v1.api.Blob
                                                   :doc              "Raw media bytes. Text should not be sent as raw bytes, use Part/text"
                                                   :generativeai/url "https://ai.google.dev/api/caching#Blob"
                                                   :protobuf/type    "google.cloud.vertexai.v1.Blob"
                                                   :class            'com.google.cloud.vertexai.api.Blob
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Blob"
                                                   :from-edn         'gcp.vertexai.v1.api.Blob/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.Blob/to-edn}
                                                  [:mimeType {:optional false :doc "TODO consider validating"} :string]
                                                  [:data {:optional false} :gcp.protobuf/ByteString]]

   :vertexai.api/FileData                        [:map
                                                  {:ns               'gcp.vertexai.v1.api.FileData
                                                   :from-edn         'gcp.vertexai.v1.api.FileData/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.FileData/to-edn
                                                   :doc              "URI based data."
                                                   :generativeai/url "https://ai.google.dev/api/caching#FileData"
                                                   :protobuf/type    "google.cloud.vertexai.v1.FileData"
                                                   :class            'com.google.cloud.vertexai.api.FileData
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.FileData"}
                                                  [:fileUri :string]
                                                  [:mimeType :string]]

   :vertexai.api/FunctionCall                    [:map
                                                  {:ns               'gcp.vertexai.v1.api.FunctionCall
                                                   :from-edn         'gcp.vertexai.v1.api.FunctionCall/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.FunctionCall/to-edn
                                                   :doc              "A predicted FunctionCall returned from the model that contains a string representing the FunctionDeclaration.name with the arguments and their values."
                                                   :generativeai/url "https://ai.google.dev/api/caching#FunctionCall"
                                                   :protobuf/type    "google.cloud.vertexai.v1.FunctionCall"
                                                   :class            'com.google.cloud.vertexai.api.FunctionCall
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.FunctionCall"}
                                                  [:name {:optional false} :string]
                                                  [:args :gcp.protobuf/Struct]]

   :vertexai.api/FunctionResponse                [:map
                                                  {:ns               'gcp.vertexai.v1.api.FunctionResponse
                                                   :from-edn         'gcp.vertexai.v1.api.FunctionResponse/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.FunctionResponse/to-edn
                                                   :doc              "A predicted FunctionCall returned from the model that contains a string representing the FunctionDeclaration.name with the arguments and their values."
                                                   :generativeai/url "https://ai.google.dev/api/caching#FunctionResponse"
                                                   :protobuf/type    "google.cloud.vertexai.v1.FunctionResponse"
                                                   :class            'com.google.cloud.vertexai.api.FunctionResponse
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.FunctionResponse"}
                                                  [:name {:optional false} :string]
                                                  [:response {:optional false} :gcp.protobuf/Struct]]

   :vertexai.api/GenerateContentRequest          [:and
                                                  {:ns               'gcp.vertexai.v1.api.GenerateContentRequest
                                                   :from-edn         'gcp.vertexai.v1.api.GenerateContentRequest/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.GenerateContentRequest/to-edn
                                                   :doc              "Request message for [PredictionService.GenerateContent]"
                                                   :generativeai/url "https://ai.google.dev/api/generate-content#request-body"
                                                   :protobuf/type    "google.cloud.vertexai.v1.GenerateContentRequest"
                                                   :class            'com.google.cloud.vertexai.api.GenerateContentRequest
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.GenerateContentRequest"}
                                                  :vertexai.synth/ModelConfig
                                                  [:map
                                                   [:contents {:optional false} [:sequential :vertexai.api/Content]]]]

   :vertexai.api/Tool                            [:or
                                                  {:class    'com.google.cloud.vertexai.api.Tool
                                                   :from-edn 'gcp.vertexai.v1.api.Tool/from-edn
                                                   :to-edn   'gcp.vertexai.v1.api.Tool/to-edn}
                                                  [:map {:closed true}
                                                   [:functionDeclarations {:optional true} [:sequential :vertexai.api/FunctionDeclaration]]
                                                   [:retrieval {:optional true} :vertexai.api/Retrieval]]
                                                  [:map {:closed true}
                                                   [:functionDeclarations {:optional true} [:sequential :vertexai.api/FunctionDeclaration]]
                                                   [:googleSearchRetrieval {:optional true} :vertexai.api/GoogleSearchRetrieval]]]

   :vertexai.api/SafetySetting                   [:map
                                                  {:class    'com.google.cloud.vertexai.api.SafetySetting
                                                   :from-edn 'gcp.vertexai.v1.api.SafetySetting/from-edn
                                                   :to-edn   'gcp.vertexai.v1.api.SafetySetting/to-edn}
                                                  [:category {:optional false} :vertexai.api/HarmCategory]
                                                  [:threshold {:optional false}
                                                   [:or {:doc           "Probability based thresholds levels for blocking."
                                                         :protobuf/type "google.cloud.vertexai.v1.SafetySetting.HarmBlockThreshold"}
                                                    [:enum
                                                     {:doc "Block low threshold and above (i.e. block more)."}
                                                     "BLOCK_LOW_AND_ABOVE" 1]
                                                    [:enum
                                                     {:doc "Block medium threshold and above."}
                                                     "BLOCK_MEDIUM_AND_ABOVE" 2]
                                                    [:enum
                                                     {:doc "Block none."}
                                                     "BLOCK_NONE" 4]
                                                    [:enum
                                                     {:doc "Block only high threshold (i.e. block less)."}
                                                     "BLOCK_ONLY_HIGH" 3]
                                                    [:enum
                                                     {:doc "Unspecified harm block threshold."}
                                                     "HARM_BLOCK_THRESHOLD_UNSPECIFIED" 0]
                                                    [:enum
                                                     {:doc "Turn off the safety filter."}
                                                     "OFF" 5]
                                                    [:enum
                                                     {:doc "No recognized value."}
                                                     "UNRECOGNIZED"]]]]

   :vertexai.api/ToolConfig                      [:map {:class    'com.google.cloud.vertexai.api.ToolConfig
                                                        :closed   true
                                                        :doc      "Tool config. This config is shared for all tools provided in the request."
                                                        :from-edn 'gcp.vertexai.v1.api.ToolConfig/from-edn
                                                        :to-edn   'gcp.vertexai.v1.api.ToolConfig/to-edn}
                                                  [:functionCallingConfig {:optional true} :vertexai.api/FunctionCallingConfig]]

   :vertexai.api/VertexAISearch                  [:map {:closed   true
                                                        :class    'com.google.cloud.vertexai.api.VertexAISearch
                                                        :from-edn 'gcp.vertexai.v1.api.VertexAISearch/from-edn
                                                        :to-edn   'gcp.vertexai.v1.api.VertexAISearch/to-edn}
                                                  [:datastore {:optional false} :vertexai/datastore-resource-id]]

   :vertexai.api/Type                            [:or
                                                  {:class            'com.google.cloud.vertexai.api.Type
                                                   :ns               'gcp.vertexai.v1.api.Type
                                                   :from-edn         'gcp.vertexai.v1.api.Type/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.Type/to-edn
                                                   :doc              "OpenAPI data types as defined by https://swagger.io/docs/specification/data-models/data-types/"
                                                   :generativeai/url "https://ai.google.dev/api/caching#Type"
                                                   :protobuf/type    "google.cloud.vertexai.v1.Type"
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Type"}
                                                  [:and :string
                                                   [:enum "TYPE_UNSPECIFIED" "STRING" "NUMBER" "INTEGER" "BOOLEAN" "ARRAY" "OBJECT" "UNRECOGNIZED"]]
                                                  [:and :int [:enum 0 1 2 3 4 5 6]]]

   :vertexai.api/Segment                         [:map
                                                  {:ns               'gcp.vertexai.v1.api.Segment
                                                   :from-edn         'gcp.vertexai.v1.api.Segment/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.Segment/to-edn
                                                   :doc              "Segment of the content."
                                                   :generativeai/url "https://ai.google.dev/api/generate-content#Segment"
                                                   :protobuf/type    "google.cloud.vertexai.v1.Segment"
                                                   :class            'com.google.cloud.vertexai.api.Segment
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Segment"}
                                                  [:text
                                                   {:doc "Output only. The text corresponding to the segment from the response."}
                                                   [:or :string :gcp.protobuf/ByteString]]
                                                  [:endIndex
                                                   {:doc "Output only. End index in the given Part, measured in bytes. Offset from the start of the Part, exclusive, starting at zero."}
                                                   :int]
                                                  [:partIndex
                                                   {:doc "Output only. The index of a Part object within its parent Content object."}
                                                   :int]
                                                  [:startIndex
                                                   {:doc "Output only. Start index in the given Part, measured in bytes. Offset from the start of the Part, inclusive, starting at zero."}
                                                   :int]]

   :vertexai.api/GroundingChunk                  [:or
                                                  {:ns               'gcp.vertexai.v1.api.GroundingChunk
                                                   :from-edn         'gcp.vertexai.v1.api.GroundingChunk/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.GroundingChunk/to-edn
                                                   :doc              "union type of :web or :retrievedContext"
                                                   :generativeai/url "https://ai.google.dev/api/generate-content#GroundingChunk"
                                                   :protobuf/type    "google.cloud.vertexai.v1.GroundingChunk"
                                                   :class            'com.google.cloud.vertexai.api.GroundingChunk
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.GroundingChunk"}
                                                  [:map {:closed true}
                                                   [:web [:map
                                                          [:title :string]
                                                          [:uri :gcp.protobuf/ByteString]]]]
                                                  [:map {:closed true}
                                                   [:retrievedContext [:map
                                                                       [:title :string]
                                                                       [:uri :gcp.protobuf/ByteString]]]]]

   :vertexai.api/SearchEntryPoint                [:map
                                                  {:class    'com.google.cloud.vertexai.api.SearchEntryPoint
                                                   :from-edn 'gcp.vertexai.v1.api.SearchEntryPoint/from-edn
                                                   :to-edn   'gcp.vertexai.v1.api.SearchEntryPoint/to-edn}
                                                  [:renderedContent
                                                   {:doc      "Optional. Web content snippet that can be embedded in a web page or an app webview."
                                                    :optional true}
                                                   :string]
                                                  [:sdkBlob
                                                   {:doc      "Optional. Base64 encoded JSON representing array of <search term, search url> tuple. A base64-encoded string"
                                                    :optional true}
                                                   :gcp.protobuf/ByteString]]

   :vertexai.api/GroundingMetadata               [:map
                                                  {:ns               'gcp.vertexai.v1.api.GroundingMetadata
                                                   :from-edn         'gcp.vertexai.v1.api.GroundingMetadata/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.GroundingMetadata/to-edn
                                                   :doc              "Metadata returned to client when grounding is enabled."
                                                   :generativeai/url "https://ai.google.dev/api/generate-content#GroundingMetadata"
                                                   :protobuf/type    "google.cloud.vertexai.v1.GroundingMetadata"
                                                   :class            'com.google.cloud.vertexai.api.GroundingMetadata
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.GroundingMetadata"}
                                                  [:groundingChunks
                                                   {:optional true
                                                    :doc      "List of supporting references retrieved from specified grounding source"}
                                                   [:sequential :vertexai.api/GroundingChunk]]
                                                  [:groundingSupports {:optional true} [:sequential :vertexai.api/GroundingSupport]]
                                                  [:webSearchQueries {:optional true} [:sequential :string]]
                                                  [:searchEntryPoint
                                                   {:optional true
                                                    :doc      "Optional. Google search entry for the following-up web searches."}
                                                   :vertexai.api/SearchEntryPoint]
                                                  #_[:retrievalMetadata
                                                     {:gemini-only? true
                                                      :optional     true
                                                      :doc          "Metadata related to retrieval in the grounding flow."}
                                                     :any]]

   :vertexai.api/GroundingSupport                [:map
                                                  {:ns               'gcp.vertexai.v1.api.GroundingSupport
                                                   :from-edn         'gcp.vertexai.v1.api.GroundingSupport/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.GroundingSupport/to-edn
                                                   :generativeai/url "https://ai.google.dev/api/generate-content#GroundingSupport"
                                                   :doc              "Grounding support."
                                                   :protobuf/type    "google.cloud.vertexai.v1.GroundingSupport"
                                                   :class            'com.google.cloud.vertexai.api.GroundingSupport
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.GroundingSupport"}
                                                  [:groundingChunkIndices [:sequential :int]]
                                                  [:confidenceScores [:sequential [:or :int :double]]]
                                                  [:segment :vertexai.api/Segment]]

   :vertexai.api/GoogleSearchRetrieval           [:map
                                                  {:ns       'gcp.vertexai.v1.api.GoogleSearchRetrieval
                                                   :from-edn 'gcp.vertexai.v1.api.GoogleSearchRetrieval/from-edn
                                                   :to-edn   'gcp.vertexai.v1.api.GoogleSearchRetrieval/to-edn
                                                   :class    'com.google.cloud.vertexai.apiGoogleSearchRetrieval
                                                   :closed   true}]

   :vertexai.api/GenerationConfig                [:map
                                                  {:ns               'gcp.vertexai.v1.api.GenerationConfig
                                                   :from-edn         'gcp.vertexai.v1.api.GenerationConfig/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.GenerationConfig/to-edn
                                                   :doc              "Configuration options for model generation and outputs. Not all parameters are configurable for every model"
                                                   :generativeai/url "https://ai.google.dev/api/generate-content#generationconfig"
                                                   :protobuf/type    "google.cloud.vertexai.v1.GenerationConfig"
                                                   :class            'com.google.cloud.vertexai.api.GenerationConfig
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.GenerationConfig"}
                                                  [:stopSequences
                                                   {:optional true
                                                    :doc      "The set of character sequences (up to 5) that will stop output generation. If specified, the API will stop at the first appearance of a stop_sequence. The stop sequence will not be included as part of the response."}
                                                   [:sequential :string]]
                                                  [:responseMimeType
                                                   {:optional true
                                                    :doc      "MIME type of the generated candidate text. Supported MIME types are: text/plain: (default) Text output. application/json: JSON response in the response candidates. text/x.enum: ENUM as a string response in the response candidates. Refer to the docs for a list of all supported text MIME types."}
                                                   :gcp.protobuf/ByteString]
                                                  [:responseSchema
                                                   {:optional true
                                                    :doc      "Output schema of the generated candidate text. Schemas must be a subset of the OpenAPI schema and can be objects, primitives or arrays.\n\nIf set, a compatible responseMimeType must also be set. Compatible MIME types: application/json: Schema for JSON response. Refer to the JSON text generation guide for more details."}
                                                   :vertexai.api/Schema]
                                                  [:candidateCount
                                                   {:optional true
                                                    :doc      "Number of generated responses to return.\n\nCurrently, this value can only be set to 1. If unset, this will default to 1."}
                                                   :int]
                                                  [:maxOutputTokens
                                                   {:optional true
                                                    :doc      "The maximum number of tokens to include in a response candidate.\n\nNote: The default value varies by model, see the Model.output_token_limit attribute of the Model returned from the getModel function."}
                                                   :int]
                                                  [:temperature
                                                   {:optional true
                                                    :doc      "Controls the randomness of the output.\n\nNote: The default value varies by model, see the Model.temperature attribute of the Model returned from the getModel function.\n\nValues can range from [0.0, 2.0]."}
                                                   :double]
                                                  [:topP
                                                   {:optional true
                                                    :doc      "The maximum cumulative probability of tokens to consider when sampling.\n\nThe model uses combined Top-k and Top-p (nucleus) sampling.\n\nTokens are sorted based on their assigned probabilities so that only the most likely tokens are considered. Top-k sampling directly limits the maximum number of tokens to consider, while Nucleus sampling limits the number of tokens based on the cumulative probability.\n\nNote: The default value varies by Model and is specified by the Model.top_p attribute returned from the getModel function. An empty topK attribute indicates that the model doesn't apply top-k sampling and doesn't allow setting topK on requests."}
                                                   :double]
                                                  [:topK
                                                   {:optional true
                                                    :doc      "The maximum number of tokens to consider when sampling.\n\nGemini models use Top-p (nucleus) sampling or a combination of Top-k and nucleus sampling. Top-k sampling considers the set of topK most probable tokens. Models running with nucleus sampling don't allow topK setting.\n\nNote: The default value varies by Model and is specified by the Model.top_p attribute returned from the getModel function. An empty topK attribute indicates that the model doesn't apply top-k sampling and doesn't allow setting topK on requests."}
                                                   :int]
                                                  [:presencePenalty
                                                   {:optional true
                                                    :doc      "Presence penalty applied to the next token's logprobs if the token has already been seen in the response. This penalty is binary on/off and not dependant on the number of times the token is used (after the first). Use frequencyPenalty for a penalty that increases with each use.\n\nA positive penalty will discourage the use of tokens that have already been used in the response, increasing the vocabulary.\n\nA negative penalty will encourage the use of tokens that have already been used in the response, decreasing the vocabulary."}
                                                   :double]
                                                  [:frequencyPenalty
                                                   {:optional true
                                                    :doc      "Frequency penalty applied to the next token's logprobs, multiplied by the number of times each token has been seen in the response so far.\n\nA positive penalty will discourage the use of tokens that have already been used, proportional to the number of times the token has been used: The more a token is used, the more difficult it is for the model to use that token again increasing the vocabulary of responses.\n\nCaution: A negative penalty will encourage the model to reuse tokens proportional to the number of times the token has been used. Small negative values will reduce the vocabulary of a response. Larger negative values will cause the model to start repeating a common token until it hits the maxOutputTokens limit: \"...the the the the the...\"."}
                                                   :double]
                                                  [:seed {:optional true} :int]
                                                  #_[:responseLogprobs
                                                     {:optional true
                                                      :doc      "If true, export the logprobs results in response."}
                                                     :boolean]
                                                  #_[:logprobs
                                                     {:optional true
                                                      :doc      "Only valid if responseLogprobs=True. This sets the number of top logprobs to return at each decoding step in the Candidate.logprobs_result."}
                                                     :int]

                                                  [:routingConfig
                                                   {:optional true}
                                                   [:or
                                                    [:map {:closed true}
                                                     [:autoMode
                                                      [:map {:closed true}
                                                       [:modelRoutingPreference
                                                        {:optional true}
                                                        [:enum "BALANCED" "PRIORITIZE_COST" "PRIORITIZE_QUALITY" "UNKNOWN" "UNRECOGNIZED" 0 1 2 3]]]]]
                                                    [:map {:closed true}
                                                     [:manualMode
                                                      {:doc "When manual routing is set, the specified model will be used directly."}
                                                      [:map {:closed true}
                                                       [:modelName {:optional true} :string]]]]]]]

   :vertexai.api/Candidate                       [:map
                                                  {:ns               'gcp.vertexai.v1.api.Candidate
                                                   :from-edn         'gcp.vertexai.v1.api.Candidate/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.Candidate/to-edn
                                                   :doc              "A response candidate generated from the model."
                                                   :generativeai/url "https://ai.google.dev/api/generate-content#Candidate"
                                                   :protobuf/type    "google.cloud.vertexai.v1.Candidate"
                                                   :class            'com.google.cloud.vertexai.api.Candidate
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Candidate"}
                                                  [:content
                                                   {:doc "Output only. Generated content returned from the model."}
                                                   :vertexai.api/Content]
                                                  [:finishReason
                                                   {:doc      "Optional. Output only. The reason why the model stopped generating tokens. If empty, the model has not stopped generating tokens."
                                                    :optional true}
                                                   [:enum
                                                    "BLOCKED_REASON_UNSPECIFIED"
                                                    "UNRECOGNIZED"
                                                    "FINISH_REASON_UNSPECIFIED"
                                                    "MALFORMED_FUNCTION_CALL"
                                                    "MAX_TOKENS"
                                                    "BLOCKLIST"
                                                    "PROHIBITED_CONTENT"
                                                    "OTHER"
                                                    "SPII"
                                                    "SAFETY"
                                                    "RECITATION"
                                                    "STOP"]]
                                                  [:safetyRatings
                                                   {:doc "List of ratings for the safety of a response candidate. There is at most one rating per category."}
                                                   [:seqable :vertexai.api/SafetyRating]]
                                                  [:citationMetadata
                                                   {:optional true
                                                    :doc      "Output only. Citation information for model-generated candidate. This field may be populated with recitation information for any text included in the content. These are passages that are \"recited\" from copyrighted material in the foundational LLM's training data."}
                                                   :vertexai.api/CitationMetadata]
                                                  [:groundingMetadata
                                                   {:optional true
                                                    :doc      "Output only. Metadata specifies sources used to ground generated content."}
                                                   :vertexai.api/GroundingMetadata]
                                                  #_[:tokenCount
                                                     {:gemini-only? true
                                                      :optional     true
                                                      :doc          "Output only. Token count for this candidate."}
                                                     :int]
                                                  #_[:groundingAttributions
                                                     {:gemini-only? true
                                                      :optional     true
                                                      :doc          "Output only. Attribution information for sources that contributed to a grounded answer. This field is populated for GenerateAnswer calls."}
                                                     :any]
                                                  #_[:logprobsResult
                                                     {:optional     true
                                                      :gemini-only? true
                                                      :doc          "Output only. Log-likelihood scores for the response tokens and top tokens index integer JSON representation"}
                                                     :any]
                                                  [:avgLogprobs
                                                   {:doc "Output only."}
                                                   :double]
                                                  [:index :int]
                                                  [:score :double]]

   :vertexai.api/Citation                        [:map
                                                  {:ns               'gcp.vertexai.v1.api.Citation
                                                   :from-edn         'gcp.vertexai.v1.api.Citation/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.Citation/to-edn
                                                   :doc              "A citation to a source for a portion of a specific response."
                                                   :generativeai/url "https://ai.google.dev/api/generate-content#CitationSource"
                                                   :protobuf/type    "google.cloud.vertexai.v1.Citation"
                                                   :class            'com.google.cloud.vertexai.api.Citation
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Citation"}
                                                  [:startIndex
                                                   {:doc      "Start index into the content."
                                                    :optional true}
                                                   :int]
                                                  [:endIndex
                                                   {:doc      "End index into the content."
                                                    :optional true}
                                                   :int]
                                                  [:uri
                                                   {:doc      "URI that is attributed as a source for a portion of the text."
                                                    :optional true}
                                                   :string]
                                                  [:license
                                                   {:doc      "License of the attribution."
                                                    :optional true}
                                                   :string]
                                                  [:title
                                                   {:doc                "Title of the attribution."
                                                    :generativeai/field false
                                                    :optional           true}
                                                   :string]
                                                  [:publicationDate
                                                   {:doc                "Publication date of the attribution"
                                                    :generativeai/field false
                                                    :optional           true}
                                                   :gcp.type/Date]]

   :vertexai.api/CitationMetadata                [:map
                                                  {:closed           true
                                                   :ns               'gcp.vertexai.v1.api.CitationMetadata
                                                   :from-edn         'gcp.vertexai.v1.api.CitationMetadata/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.CitationMetadata/to-edn
                                                   :doc              "A collection of source attributions for a piece of content"
                                                   :generativeai/url "https://ai.google.dev/api/generate-content#citationmetadata"
                                                   :protobuf/type    "google.cloud.vertexai.v1.CitationMetadata"
                                                   :class            'com.google.cloud.vertexai.api.CitationMetadata
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.CitationMetadata"}
                                                  [:citations [:sequential :vertexai.api/Citation]]]

   :vertexai.api/HarmCategory                    [:enum
                                                  {:ns               'gcp.vertexai.v1.api.HarmCategory
                                                   :from-edn         'gcp.vertexai.v1.api.HarmCategory/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.HarmCategory/to-edn
                                                   :doc              "Harm categories that will block the content."
                                                   :generativeai/url "https://ai.google.dev/api/generate-content#harmcategory"
                                                   :protobuf/type    "google.cloud.vertexai.v1.HarmCategory"
                                                   :class            'com.google.cloud.vertexai.api.HarmCategory
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.HarmCategory"}
                                                  "HARM_CATEGORY_DANGEROUS_CONTENT"
                                                  "HARM_CATEGORY_HARASSMENT"
                                                  "HARM_CATEGORY_HATE_SPEECH"
                                                  "HARM_CATEGORY_SEXUALLY_EXPLICIT"
                                                  "HARM_CATEGORY_UNSPECIFIED"
                                                  "UNRECOGNIZED"
                                                  0 1 2 3 4]

   :vertexai.api/Retrieval                       [:map
                                                  {:closed   true
                                                   :class    'com.google.cloud.vertexai.api.Retrieval
                                                   :from-edn 'gcp.vertexai.v1.api.Retrieval/from-edn
                                                   :to-edn   'gcp.vertexai.v1.api.Retrieval/to-edn}
                                                  [:vertexAiSearch {:optional? false} :vertexai.api/VertexAISearch]]

   :vertexai.api/SafetyRating                    [:map
                                                  {:ns               'gcp.vertexai.v1.api.SafetyRating
                                                   :from-edn         'gcp.vertexai.v1.api.SafetyRating/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.SafetyRating/to-edn
                                                   :doc              "The safety rating contains the category of harm and the harm probability level in that category for a piece of content. Content is classified for safety across a number of harm categories and the probability of the harm classification is included here."
                                                   :generativeai/url "https://ai.google.dev/api/generate-content#safetyrating"
                                                   :protobuf/type    "google.cloud.vertexai.v1.SafetyRating"
                                                   :class            'com.google.cloud.vertexai.api.SafetyRating
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.SafetyRating"}
                                                  [:category :vertexai.api/HarmCategory]
                                                  [:blocked :boolean]
                                                  [:probability [:enum
                                                                 "HARM_PROBABILITY_UNSPECIFIED"
                                                                 "HIGH"
                                                                 "LOW"
                                                                 "MEDIUM"
                                                                 "NEGLIGIBLE"
                                                                 "UNRECOGNIZED"]]
                                                  [:probabilityScore :float]
                                                  [:severity [:enum
                                                              "HARM_SEVERITY_HIGH"
                                                              "HARM_SEVERITY_LOW"
                                                              "HARM_SEVERITY_MEDIUM"
                                                              "HARM_SEVERITY_NEGLIGIBLE"
                                                              "HARM_SEVERITY_UNSPECIFIED"]]
                                                  [:severityScore :float]]

   :vertexai.api/FunctionDeclaration             [:or
                                                  {:ns               'gcp.vertexai.v1.api.FunctionDeclaration
                                                   :from-edn         'gcp.vertexai.v1.api.FunctionDeclaration/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.FunctionDeclaration/to-edn
                                                   :doc              "Structured representation of a function declaration as defined by the OpenAPI 3.03 specification. Included in this declaration are the function name and parameters. This FunctionDeclaration is a representation of a block of code that can be used as a Tool by the model and executed by the client."
                                                   :generativeai/url "https://ai.google.dev/api/caching#FunctionDeclaration"
                                                   :protobuf/type    "google.cloud.vertexai.v1.FunctionDeclaration"
                                                   :class            'com.google.cloud.vertexai.api.FunctionDeclaration
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.FunctionDeclaration"}
                                                  :string
                                                  (global/instance-schema JsonObject)
                                                  [:map {:doc           "provided schema"
                                                         :error/message "provided parameters bad"}
                                                   [:description :string]
                                                   [:name :string]
                                                   [:parameters :vertexai.api/Schema]]
                                                  [:map {:doc           "create schema from static-method via reflection"
                                                         :error/message "static-method map bad"}
                                                   [:function [:and
                                                               [:fn #(instance? java.lang.reflect.Method %)]
                                                               [:fn #(Modifier/isStatic (.getModifiers %))]]]
                                                   [:functionDescription :string]
                                                   [:orderedParameters [:sequential :string]]]]

   :vertexai.api/GenerateContentResponse         [:map
                                                  {:doc      "Response from the model supporting multiple candidate responses Safety ratings and content filtering are reported for both prompt in GenerateContentResponse.prompt_feedback and for each candidate in finishReason and in safetyRatings. The API: - Returns either all requested candidates or none of them - Returns no candidates at all only if there was something wrong with the prompt (check promptFeedback) - Reports feedback on each candidate in finishReason and safetyRatings."
                                                   :from-edn 'gcp.vertexai.v1.api.GenerateContentResponse/from-edn
                                                   :to-edn   'gcp.vertexai.v1.api.GenerateContentResponse/to-edn}
                                                  [:candidates
                                                   {:doc "Candidate responses from the model."}
                                                   ;; TODO count should be exactly= 1
                                                   [:seqable :vertexai.api/Candidate]]
                                                  [:promptFeedback
                                                   {:optional true
                                                    :doc      "Returns the prompt's feedback related to the content filters."}
                                                   [:map {:doc "A set of the feedback metadata the prompt specified in GenerateContentRequest.content"}
                                                    [:blockReason
                                                     {:optional true
                                                      :doc      "If set, the prompt was blocked and no candidates are returned. Rephrase the prompt."}
                                                     [:enum "BLOCK_REASON_UNSPECIFIED" "SAFETY" "OTHER" "BLOCKLIST" "PROHIBITED_CONTENT"]]
                                                    [:safetyRatings
                                                     {:doc "Ratings for safety of the prompt. There is at most one rating per category."}
                                                     [:seqable :vertexai.api/SafetyRating]]]]
                                                  [:usageMetadata
                                                   {:optional true
                                                    :doc      "Output only. Metadata on the generation requests' token usage."}
                                                   [:map
                                                    [:promptTokenCount
                                                     {:doc "Number of tokens in the prompt. When cachedContent is set, this is still the total effective prompt size meaning this includes the number of tokens in the cached content."}
                                                     :int]
                                                    #_[:cachedContentTokenCount
                                                       {:optional true
                                                        :doc      "Number of tokens in the cached part of the prompt (the cached content)"}
                                                       :int]
                                                    [:candidatesTokenCount
                                                     {:doc "Total number of tokens across all the generated response candidates"}
                                                     :int]
                                                    [:totalTokenCount
                                                     {:doc "Total token count for the generation request (prompt + response candidates)"}
                                                     :int]]]]

   :vertexai.api/FunctionCallingConfig           [:map {:class    'com.google.cloud.vertexai.api.FunctionCallingConfig
                                                        :from-edn 'gcp.vertexai.v1.api.FunctionCallingConfig/from-edn
                                                        :to-edn   'gcp.vertexai.v1.api.FunctionCallingConfig/to-edn
                                                        :doc      "Function calling config."}
                                                  [:mode {:doc "Function calling mode."}
                                                   [:or
                                                    [:enum
                                                     {:doc "Unspecified function calling mode. This value should not be used."}
                                                     "MODE_UNSPECIFIED" 0]
                                                    [:enum
                                                     {:doc "Default model behavior, model decides to predict either a function call or a natural language response."}
                                                     "AUTO" 1]
                                                    [:enum
                                                     {:doc "Model is constrained to always predicting a function call only. If \"allowed_function_names\" are set, the predicted function call will be limited to any one of \"allowed_function_names\", else the predicted function call will be any one of the provided \"function_declarations\"."}
                                                     "ANY" 2]
                                                    [:enum
                                                     {:doc "Model will not predict any function call. Model behavior is same as when not passing any function declarations."}
                                                     "NONE" 3]
                                                    [:= {:doc "No recognized value."} "UNRECOGNIZED"]]]
                                                  [:allowedFunctionNames [:sequential :string]]]

   :vertexai.api/CountTokensRequest              [:map
                                                  [:endpoint :string]
                                                  [:model :string]
                                                  [:contents {:optional true} [:sequential [:ref :vertexai.api/Content]]]
                                                  [:systemInstruction {:optional true} [:ref :vertexai.api/Content]]
                                                  [:instances {:optional true} [:sequential (global/instance-schema Value)]]
                                                  [:tools {:optional true} [:sequential :vertexai.api/Tool]]]

   :vertexai.api/CountTokensResponse             [:map
                                                  {:class    'com.google.cloud.vertexai.api.CountTokensResponse
                                                   :from-edn 'gcp.vertexai.v1.api.CountTokensResponse/from-edn
                                                   :to-edn   'gcp.vertexai.v1.api.CountTokensResponse/to-edn}
                                                  [:totalTokens :int]
                                                  [:totalBillableCharacters :int]]

   :vertexai.api/Schema                          [:map
                                                  {:doc              "Represents a select subset of an OpenAPI 3.0 schema object"
                                                   :ns               'gcp.vertexai.v1.api.Schema
                                                   :from-edn         'gcp.vertexai.v1.api.Schema/from-edn
                                                   :to-edn           'gcp.vertexai.v1.api.Schema/to-edn
                                                   :generativeai/url "https://ai.google.dev/api/caching#Schema"
                                                   :protobuf/type    "google.cloud.vertexai.v1.Schema"
                                                   :class            'com.google.cloud.vertexai.api.Schema
                                                   :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Schema"}
                                                  [:type {:optional false} :vertexai.api/Type]
                                                  [:description
                                                   {:optional true
                                                    :doc      "A brief description of the parameter. This could contain examples of use. Parameter description may be formatted as Markdown."}
                                                   :string]
                                                  [:example {:optional true
                                                             :doc      "example of an object"}
                                                   :gcp.protobuf/Value]
                                                  [:nullable {:optional true} :boolean]
                                                  [:title {:optional true} :string]
                                                  ;; STRING -----------------------
                                                  [:format
                                                   {:optional true
                                                    :doc      "The format of the data. This is used only for primitive datatypes. Supported formats: for NUMBER type: float, double for INTEGER type: int32, int64 for STRING type: enum"}
                                                   :string]
                                                  [:pattern {:optional true} :string]
                                                  [:minLength {:optional true} :int]
                                                  [:maxLength {:optional true} :int]
                                                  ;; NUMBER -----------------------
                                                  [:minimum {:optional true} :double]
                                                  [:maximum {:optional true} :double]
                                                  ;; ARRAY -----------------------
                                                  [:items {:optional true} [:ref :vertexai.api/Schema]]
                                                  [:minItems {:optional true} :int]
                                                  [:maxItems {:optional true} :int]
                                                  ;; OBJECT -----------------------
                                                  [:properties
                                                   {:optional true
                                                    :doc      "string->schema. An object containing a list of \"key\": value pairs. Example: { \"name\": \"wrench\", \"mass\": \"1.3kg\", \"count\": \"3\" }."}
                                                   [:map-of [:or :string simple-keyword?] [:ref :vertexai.api/Schema]]]
                                                  [:minProperties {:optional true} :int]
                                                  [:maxProperties {:optional true} :int]
                                                  [:required {:optional true} [:sequential [:or :string simple-keyword?]]]]})

(global/include! registry)