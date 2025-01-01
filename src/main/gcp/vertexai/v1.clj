(ns gcp.vertexai.v1)

;; TODO
;; emit integrant init,halt,doc
;; emit function schemas
;; switch to versioned ie :vertexai.v1.generativeai/foo
;; malli schema registry
;; registries for protobuf etc
;; can we derive :Vertex.api/Schema from malli schemas

(def ^{:package/url "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api"}
  registry
  {
   :vertexai.synth/Contentable {}
   :vertexai.synth/Valueable {}
   :vertexai.synth/DeclarableFunction {}
   :vertexai.synth/Requestable {}
   :vertexai.synth/ChatSession {}
   :vertexai.synth/IHistory {:doc "satisfies vertexai.generativeai/IHistory"}
   :vertexai.synth/ModelConfig {}
   #!--------------
   :vertexai.generativeai/generate-content {}
   :vertexai.generativeai/generate-content-async {}
   :vertexai.generativeai/generate-content-seq {}
   :vertexai.generativeai/chat {}
   :vertexai.generativeai/send-msg {}
   :vertexai.generativeai/send-msg-stream {}
   :vertexai.generativeai/clone-chat {}
   :vertexai.generativeai/history-clone {}
   #!--------------
   :vertexai/VertexAI                   {:doc       "This class holds default settings and credentials to make Vertex AI API calls. Note: The VertexAI instance will start a service client when the first API call is made. Please close the VertexAI instance after making any API calls so that clients get closed as well."
                                         :class     'com.google.cloud.vertexai.VertexAI
                                         :class/url "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.VertexAI"
                                         :ns        'gcp.vertexai.v1.VertexAI
                                         :schema    'gcp.vertexai.v1.VertexAI/schema
                                         :ig/halt!  'gcp.vertexai.v1.VertexAI/halt!}

   :vertexai.api/Blob                   {:ns               'gcp.vertexai.v1.api.Blob
                                         :schema           'gcp.vertexai.v1.api.Blob/schema
                                         :doc              "Raw media bytes. Text should not be sent as raw bytes, use Part/text"
                                         :generativeai/url "https://ai.google.dev/api/caching#Blob"
                                         :protobuf/type    "google.cloud.vertexai.v1.Blob"
                                         :class            'com.google.cloud.vertexai.api.Blob
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Blob"}

   :vertexai.api/Citation               {:ns               'gcp.vertexai.v1.api.Citation
                                         :schema           'gcp.vertexai.v1.api.Citation/schema
                                         :from-edn         'gcp.vertexai.v1.api.Citation/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.Citation/to-edn
                                         :doc              "A citation to a source for a portion of a specific response."
                                         :dependencies     [:gcp.type/Date]
                                         :generativeai/url "https://ai.google.dev/api/generate-content#CitationSource"
                                         :protobuf/type    "google.cloud.vertexai.v1.Citation"
                                         :class            'com.google.cloud.vertexai.api.Citation
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Citation"}

   :vertexai.api/FileData               {:ns               'gcp.vertexai.v1.api.FileData
                                         :schema           'gcp.vertexai.v1.api.FileData/schema
                                         :from-edn         'gcp.vertexai.v1.api.FileData/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.FileData/to-edn
                                         :doc              "URI based data."
                                         :generativeai/url "https://ai.google.dev/api/caching#FileData"
                                         :protobuf/type    "google.cloud.vertexai.v1.FileData"
                                         :class            'com.google.cloud.vertexai.api.FileData
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.FileData"}

   :vertexai.api/FunctionCall           {:ns               'gcp.vertexai.v1.api.FunctionCall
                                         :schema           'gcp.vertexai.v1.api.FunctionCall/schema
                                         :from-edn         'gcp.vertexai.v1.api.FunctionCall/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.FunctionCall/to-edn
                                         :doc              "A predicted FunctionCall returned from the model that contains a string representing the FunctionDeclaration.name with the arguments and their values."
                                         :generativeai/url "https://ai.google.dev/api/caching#FunctionCall"
                                         :protobuf/type    "google.cloud.vertexai.v1.FunctionCall"
                                         :class            'com.google.cloud.vertexai.api.FunctionCall
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.FunctionCall"
                                         :dependents       [:vertexai.api/Part]}

   :vertexai.api/FunctionResponse       {:ns               'gcp.vertexai.v1.api.FunctionResponse
                                         :schema           'gcp.vertexai.v1.api.FunctionResponse/schema
                                         :from-edn         'gcp.vertexai.v1.api.FunctionResponse/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.FunctionResponse/to-edn
                                         :doc              "A predicted FunctionCall returned from the model that contains a string representing the FunctionDeclaration.name with the arguments and their values."
                                         :generativeai/url "https://ai.google.dev/api/caching#FunctionResponse"
                                         :protobuf/type    "google.cloud.vertexai.v1.FunctionResponse"
                                         :class            'com.google.cloud.vertexai.api.FunctionResponse
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.FunctionResponse"
                                         :dependents       [:vertexai.api/Part]}

   :vertexai.api/GroundingChunk         {:ns               'gcp.vertexai.v1.api.GroundingChunk
                                         :schema           'gcp.vertexai.v1.api.GroundingChunk/schema
                                         :from-edn         'gcp.vertexai.v1.api.GroundingChunk/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.GroundingChunk/to-edn
                                         :doc              "Grounding chunk."
                                         :generativeai/url "https://ai.google.dev/api/generate-content#GroundingChunk"
                                         :protobuf/type    "google.cloud.vertexai.v1.GroundingChunk"
                                         :class            'com.google.cloud.vertexai.api.GroundingChunk
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.GroundingChunk"
                                         :dependencies     [:gcp.protobuf/ByteString]
                                         :dependents       [:vertexai.api/GroundingMetaData]}

   :vertex.api/HarmCategory             {:ns               'gcp.vertexai.v1.api.HarmCategory
                                         :schema           'gcp.vertexai.v1.api.HarmCategory/schema
                                         :from-edn         'gcp.vertexai.v1.api.HarmCategory/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.HarmCategory/to-edn
                                         :doc              "Harm categories that will block the content."
                                         :generativeai/url "https://ai.google.dev/api/generate-content#harmcategory"
                                         :protobuf/type    "google.cloud.vertexai.v1.HarmCategory"
                                         :class            'com.google.cloud.vertexai.api.HarmCategory
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.HarmCategory"}

   :vertexai.api.SafetyRating           {:ns               'gcp.vertexai.v1.api.SafetyRating
                                         :schema           'gcp.vertexai.v1.api.SafetyRating/schema
                                         :from-edn         'gcp.vertexai.v1.api.SafetyRating/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.SafetyRating/to-edn
                                         :doc              "The safety rating contains the category of harm and the harm probability level in that category for a piece of content. Content is classified for safety across a number of harm categories and the probability of the harm classification is included here."
                                         :generativeai/url "https://ai.google.dev/api/generate-content#safetyrating"
                                         :protobuf/type    "google.cloud.vertexai.v1.SafetyRating"
                                         :class            'com.google.cloud.vertexai.api.SafetyRating
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.SafetyRating"}

   :vertexai.api/Segment                {:ns               'gcp.vertexai.v1.api.Segment
                                         :schema           'gcp.vertexai.v1.api.Segment/schema
                                         :from-edn         'gcp.vertexai.v1.api.Segment/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.Segment/to-edn
                                         :doc              "Segment of the content."
                                         :generativeai/url "https://ai.google.dev/api/generate-content#Segment"
                                         :protobuf/type    "google.cloud.vertexai.v1.Segment"
                                         :class            'com.google.cloud.vertexai.api.Segment
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Segment"}

   :vertexai.api/Type                   {:ns               'gcp.vertexai.v1.api.Type
                                         :schema           'gcp.vertexai.v1.api.Type/schema
                                         :from-edn         'gcp.vertexai.v1.api.Type/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.Type/to-edn
                                         :doc              "OpenAPI data types as defined by https://swagger.io/docs/specification/data-models/data-types/"
                                         :generativeai/url "https://ai.google.dev/api/caching#Type"
                                         :protobuf/type    "google.cloud.vertexai.v1.Type"
                                         :class            'com.google.cloud.vertexai.api.Type
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Type"}

   ;;------------------------------------------------------------------

   :vertexai.api/Candidate              {:ns               'gcp.vertexai.v1.api.Candidate
                                         :schema           'gcp.vertexai.v1.api.Candidate/schema
                                         :from-edn         'gcp.vertexai.v1.api.Candidate/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.Candidate/to-edn
                                         :doc              "A response candidate generated from the model."
                                         :dependencies     [:vertexai.api/CitationMetadata
                                                            :vertexai.api/Content
                                                            :vertexai.api/GroundingMetaData
                                                            :vertexai.api/SafetyRating]
                                         :generativeai/url "https://ai.google.dev/api/generate-content#Candidate"
                                         :protobuf/type    "google.cloud.vertexai.v1.Candidate"
                                         :class            'com.google.cloud.vertexai.api.Candidate
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Candidate"}

   :vertexai.api/CitationMetadata       {:ns               'gcp.vertexai.v1.api.CitationMetadata
                                         :schema           'gcp.vertexai.v1.api.CitationMetadata/schema
                                         :from-edn         'gcp.vertexai.v1.api.CitationMetadata/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.CitationMetadata/to-edn
                                         :dependencies     [:vertexai.api/Citation]
                                         :dependents       [:vertexai.api/Candidate]
                                         :doc              "A collection of source attributions for a piece of content"
                                         :generativeai/url "https://ai.google.dev/api/generate-content#citationmetadata"
                                         :protobuf/type    "google.cloud.vertexai.v1.CitationMetadata"
                                         :class            'com.google.cloud.vertexai.api.CitationMetadata
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.CitationMetadata"}

   :vertexai.api/Content                {:ns               'gcp.vertexai.v1.api.Content
                                         :schema           'gcp.vertexai.v1.api.Content/schema
                                         :from-edn         'gcp.vertexai.v1.api.Content/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.Content/to-edn
                                         :dependencies     [:vertexai.api/Part]
                                         :dependents       [:vertexai.api/Candidate]
                                         :doc              "The base structured datatype containing multi-part content of a message. A Content includes a role field designating the producer of the Content and a parts field containing multi-part data that contains the content of the message turn"
                                         :generativeai/url "https://ai.google.dev/api/caching#Content"
                                         :protobuf/type    "google.cloud.vertexai.v1.Content"
                                         :class            'com.google.cloud.vertexai.api.Content
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Content"}

   :vertexai.api/FunctionDeclaration    {:ns               'gcp.vertexai.v1.api.FunctionDeclaration
                                         :schema           'gcp.vertexai.v1.api.FunctionDeclaration/schema
                                         :from-edn         'gcp.vertexai.v1.api.FunctionDeclaration/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.FunctionDeclaration/to-edn
                                         :dependencies     [:vertexai.api/Schema]
                                         :dependents       []
                                         :doc              "Structured representation of a function declaration as defined by the OpenAPI 3.03 specification. Included in this declaration are the function name and parameters. This FunctionDeclaration is a representation of a block of code that can be used as a Tool by the model and executed by the client."
                                         :generativeai/url "https://ai.google.dev/api/caching#FunctionDeclaration"
                                         :protobuf/type    "google.cloud.vertexai.v1.FunctionDeclaration"
                                         :class            'com.google.cloud.vertexai.api.FunctionDeclaration
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.FunctionDeclaration"}

   :vertexai.api/GenerationConfig       {:ns               'gcp.vertexai.v1.api.GenerationConfig
                                         :schema           'gcp.vertexai.v1.api.GenerationConfig/schema
                                         :from-edn         'gcp.vertexai.v1.api.GenerationConfig/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.GenerationConfig/to-edn
                                         :doc              "Configuration options for model generation and outputs. Not all parameters are configurable for every model"
                                         :dependents       []
                                         :dependencies     [:vertexai.api/Schema :gcp.protobuf/ByteString]
                                         :generativeai/url "https://ai.google.dev/api/generate-content#generationconfig"
                                         :protobuf/type    "google.cloud.vertexai.v1.GenerationConfig"
                                         :class            'com.google.cloud.vertexai.api.GenerationConfig
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.GenerationConfig"}

   :vertexai.api/GenerateContentRequest {:ns               'gcp.vertexai.v1.api.GenerateContentRequest
                                         :schema           'gcp.vertexai.v1.api.GenerateContentRequest/schema
                                         :from-edn         'gcp.vertexai.v1.api.GenerateContentRequest/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.GenerateContentRequest/to-edn
                                         :doc              "Request message for [PredictionService.GenerateContent]"
                                         :dependents       []
                                         :dependencies     []
                                         :generativeai/url "https://ai.google.dev/api/generate-content#request-body"
                                         :protobuf/type    "google.cloud.vertexai.v1.GenerateContentRequest"
                                         :class            'com.google.cloud.vertexai.api.GenerateContentRequest
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.GenerateContentRequest"}

   :vertexai.api/GroundingMetaData      {:ns               'gcp.vertexai.v1.api.GroundingMetadata
                                         :schema           'gcp.vertexai.v1.api.GroundingMetadata/schema
                                         :from-edn         'gcp.vertexai.v1.api.GroundingMetadata/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.GroundingMetadata/to-edn
                                         :doc              "Metadata returned to client when grounding is enabled."
                                         :dependents       [:vertexai.api/Candidate]
                                         :dependencies     [:vertexai.api/GroundingChunk
                                                            :vertexai.api/GroundingSupport
                                                            :vertexai.api/SearchEntryPoint]
                                         :generativeai/url "https://ai.google.dev/api/generate-content#GroundingMetadata"
                                         :protobuf/type    "google.cloud.vertexai.v1.GroundingMetadata"
                                         :class            'com.google.cloud.vertexai.api.GroundingMetadata
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.GroundingMetadata"}

   :vertexai.api/GroundingSupport       {:ns               'gcp.vertexai.v1.api.GroundingSupport
                                         :schema           'gcp.vertexai.v1.api.GroundingSupport/schema
                                         :from-edn         'gcp.vertexai.v1.api.GroundingSupport/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.GroundingSupport/to-edn
                                         :dependencies     [:vertexai.api/Segment]
                                         :dependents       [:vertexai.api/GroundingMetaData]
                                         :generativeai/url "https://ai.google.dev/api/generate-content#GroundingSupport"
                                         :doc              "Grounding support."
                                         :protobuf/type    "google.cloud.vertexai.v1.GroundingSupport"
                                         :class            'com.google.cloud.vertexai.api.GroundingSupport
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.GroundingSupport"}

   :vertexai.api/Part                   {:ns               'gcp.vertexai.v1.api.Part
                                         :schema           'gcp.vertexai.v1.api.Part/schema
                                         :from-edn         'gcp.vertexai.v1.api.Part/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.Part/to-edn
                                         :dependencies     [:vertexai.api/Blob
                                                            :vertexai.api/FileData
                                                            :vertexai.api/FunctionCall
                                                            :vertexai.api/FunctionResponse]
                                         :dependents       [:vertexai.api/Content]
                                         :doc              "A (union) datatype containing media that is part of a multi-part Content message. A Part consists of data which has an associated datatype. A Part can only contain one of the accepted types in Part.data. A Part must have a fixed IANA MIME type identifying the type and subtype of the media if the inlineData field is filled with raw bytes."
                                         :generativeai/url "https://ai.google.dev/api/caching#Part"
                                         :protobuf/type    "google.cloud.vertexai.v1.Part"
                                         :class            'com.google.cloud.vertexai.api.Part
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Part"}

   :vertexai.api/Schema                 {:ns               'gcp.vertexai.v1.api.Schema
                                         :schema           'gcp.vertexai.v1.api.Schema/schema
                                         :from-edn         'gcp.vertexai.v1.api.Schema/from-edn
                                         :to-edn           'gcp.vertexai.v1.api.Schema/to-edn
                                         :dependencies     [:vertexai.api/Type]
                                         :dependents       []
                                         :doc              "Represents a select subset of an OpenAPI 3.0 schema object"
                                         :generativeai/url "https://ai.google.dev/api/caching#Schema"
                                         :protobuf/type    "google.cloud.vertexai.v1.Schema"
                                         :class            'com.google.cloud.vertexai.api.Schema
                                         :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Schema"}}

  )
