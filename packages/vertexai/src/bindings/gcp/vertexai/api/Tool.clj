;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.Tool
  {:doc
     "<pre>\nTool details that the model may use to generate response.\n\nA `Tool` is a piece of code that enables the system to interact with\nexternal systems to perform an action, or set of actions, outside of\nknowledge and scope of the model. A Tool object should contain exactly\none type of Tool (e.g FunctionDeclaration, Retrieval or\nGoogleSearchRetrieval).\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Tool}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.Tool"
   :gcp.dev/certification
     {:base-seed 1775465569338
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465569338 :standard 1775465569339 :stress 1775465569340}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:52:59.381760515Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global]
            [gcp.vertexai.api.EnterpriseWebSearch :as EnterpriseWebSearch]
            [gcp.vertexai.api.FunctionDeclaration :as FunctionDeclaration]
            [gcp.vertexai.api.GoogleMaps :as GoogleMaps]
            [gcp.vertexai.api.GoogleSearchRetrieval :as GoogleSearchRetrieval]
            [gcp.vertexai.api.Retrieval :as Retrieval]
            [gcp.vertexai.api.UrlContext :as UrlContext])
  (:import [com.google.cloud.vertexai.api Tool Tool$Builder Tool$CodeExecution
            Tool$CodeExecution$Builder Tool$ComputerUse Tool$ComputerUse$Builder
            Tool$ComputerUse$Environment Tool$GoogleSearch
            Tool$GoogleSearch$Builder Tool$PhishBlockThreshold]
           [com.google.protobuf ProtocolStringList]))

(declare from-edn
         to-edn
         PhishBlockThreshold-from-edn
         PhishBlockThreshold-to-edn
         GoogleSearch-from-edn
         GoogleSearch-to-edn
         CodeExecution-from-edn
         CodeExecution-to-edn
         ComputerUse$Environment-from-edn
         ComputerUse$Environment-to-edn
         ComputerUse-from-edn
         ComputerUse-to-edn
         ComputerUse$Environment-from-edn
         ComputerUse$Environment-to-edn)

(def PhishBlockThreshold-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nThese are available confidence level user can set to block malicious urls\nwith chosen confidence and above. For understanding different confidence of\nwebrisk, please refer to\nhttps://cloud.google.com/web-risk/docs/reference/rpc/google.cloud.webrisk.v1eap1#confidencelevel\n</pre>\n\nProtobuf enum {@code google.cloud.vertexai.v1.Tool.PhishBlockThreshold}",
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/Tool.PhishBlockThreshold}
   "PHISH_BLOCK_THRESHOLD_UNSPECIFIED" "BLOCK_LOW_AND_ABOVE"
   "BLOCK_MEDIUM_AND_ABOVE" "BLOCK_HIGH_AND_ABOVE" "BLOCK_HIGHER_AND_ABOVE"
   "BLOCK_VERY_HIGH_AND_ABOVE" "BLOCK_ONLY_EXTREMELY_HIGH"])

(defn ^Tool$GoogleSearch GoogleSearch-from-edn
  [arg]
  (let [builder (Tool$GoogleSearch/newBuilder)]
    (when (some? (get arg :blockingConfidence))
      (.setBlockingConfidence builder
                              (Tool$PhishBlockThreshold/valueOf
                                (get arg :blockingConfidence))))
    (when (seq (get arg :excludeDomains))
      (.addAllExcludeDomains builder (seq (get arg :excludeDomains))))
    (.build builder)))

(defn GoogleSearch-to-edn
  [^Tool$GoogleSearch arg]
  (when arg
    (cond-> {}
      (.hasBlockingConfidence arg) (assoc :blockingConfidence
                                     (.name (.getBlockingConfidence arg)))
      (seq (.getExcludeDomainsList arg)) (assoc :excludeDomains
                                           (protobuf/ProtocolStringList-to-edn
                                             (.getExcludeDomainsList arg))))))

(def GoogleSearch-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nGoogleSearch tool type.\nTool to support Google Search in Model. Powered by Google.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Tool.GoogleSearch}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/Tool.GoogleSearch}
   [:blockingConfidence
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Sites with confidence level chosen &amp; above this value will be\nblocked from the search results.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.Tool.PhishBlockThreshold blocking_confidence = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The blockingConfidence.",
     :setter-doc
       "<pre>\nOptional. Sites with confidence level chosen &amp; above this value will be\nblocked from the search results.\n</pre>\n\n<code>\noptional .google.cloud.vertexai.v1.Tool.PhishBlockThreshold blocking_confidence = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param value The blockingConfidence to set.\n@return This builder for chaining."}
    [:enum {:closed true} "PHISH_BLOCK_THRESHOLD_UNSPECIFIED"
     "BLOCK_LOW_AND_ABOVE" "BLOCK_MEDIUM_AND_ABOVE" "BLOCK_HIGH_AND_ABOVE"
     "BLOCK_HIGHER_AND_ABOVE" "BLOCK_VERY_HIGH_AND_ABOVE"
     "BLOCK_ONLY_EXTREMELY_HIGH"]]
   [:excludeDomains
    {:optional true,
     :getter-doc
       "<pre>\nOptional. List of domains to be excluded from the search results.\nThe default limit is 2000 domains.\nExample: [\"amazon.com\", \"facebook.com\"].\n</pre>\n\n<code>repeated string exclude_domains = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return A list containing the excludeDomains.",
     :setter-doc
       "<pre>\nOptional. List of domains to be excluded from the search results.\nThe default limit is 2000 domains.\nExample: [\"amazon.com\", \"facebook.com\"].\n</pre>\n\n<code>repeated string exclude_domains = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param values The excludeDomains to add.\n@return This builder for chaining."}
    :gcp.foreign.com.google.protobuf/ProtocolStringList]])

(defn ^Tool$CodeExecution CodeExecution-from-edn
  [arg]
  (let [builder (Tool$CodeExecution/newBuilder)] (.build builder)))

(defn CodeExecution-to-edn
  [^Tool$CodeExecution arg]
  (when arg (cond-> {})))

(def CodeExecution-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nTool that executes code generated by the model, and automatically returns\nthe result to the model.\n\nSee also [ExecutableCode]and [CodeExecutionResult] which are input and\noutput to this tool.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Tool.CodeExecution}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/Tool.CodeExecution}])

(def ComputerUse$Environment-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nRepresents the environment being operated, such as a web browser.\n</pre>\n\nProtobuf enum {@code google.cloud.vertexai.v1.Tool.ComputerUse.Environment}",
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/Tool.ComputerUse.Environment}
   "ENVIRONMENT_UNSPECIFIED" "ENVIRONMENT_BROWSER"])

(def ComputerUse$Environment-schema
  [:enum
   {:closed true,
    :doc
      "<pre>\nRepresents the environment being operated, such as a web browser.\n</pre>\n\nProtobuf enum {@code google.cloud.vertexai.v1.Tool.ComputerUse.Environment}",
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/Tool.ComputerUse.Environment}
   "ENVIRONMENT_UNSPECIFIED" "ENVIRONMENT_BROWSER"])

(defn ^Tool$ComputerUse ComputerUse-from-edn
  [arg]
  (let [builder (Tool$ComputerUse/newBuilder)]
    (when (some? (get arg :environment))
      (.setEnvironment builder
                       (Tool$ComputerUse$Environment/valueOf
                         (get arg :environment))))
    (when (seq (get arg :excludedPredefinedFunctions))
      (.addAllExcludedPredefinedFunctions
        builder
        (seq (get arg :excludedPredefinedFunctions))))
    (.build builder)))

(defn ComputerUse-to-edn
  [^Tool$ComputerUse arg]
  (when arg
    (cond-> {:environment (.name (.getEnvironment arg))}
      (seq (.getExcludedPredefinedFunctionsList arg))
        (assoc :excludedPredefinedFunctions
          (protobuf/ProtocolStringList-to-edn
            (.getExcludedPredefinedFunctionsList arg))))))

(def ComputerUse-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nTool to support computer use.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Tool.ComputerUse}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/Tool.ComputerUse}
   [:environment
    {:getter-doc
       "<pre>\nRequired. The environment being operated.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.Tool.ComputerUse.Environment environment = 1 [(.google.api.field_behavior) = REQUIRED];\n</code>\n\n@return The environment.",
     :setter-doc
       "<pre>\nRequired. The environment being operated.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.Tool.ComputerUse.Environment environment = 1 [(.google.api.field_behavior) = REQUIRED];\n</code>\n\n@param value The environment to set.\n@return This builder for chaining."}
    [:enum {:closed true} "ENVIRONMENT_UNSPECIFIED" "ENVIRONMENT_BROWSER"]]
   [:excludedPredefinedFunctions
    {:optional true,
     :getter-doc
       "<pre>\nOptional. By default, [predefined\nfunctions](https://cloud.google.com/vertex-ai/generative-ai/docs/computer-use#supported-actions)\nare included in the final model call. Some of them can be explicitly\nexcluded from being automatically included. This can serve two purposes:\n1. Using a more restricted / different action space.\n2. Improving the definitions / instructions of predefined functions.\n</pre>\n\n<code>\nrepeated string excluded_predefined_functions = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return A list containing the excludedPredefinedFunctions.",
     :setter-doc
       "<pre>\nOptional. By default, [predefined\nfunctions](https://cloud.google.com/vertex-ai/generative-ai/docs/computer-use#supported-actions)\nare included in the final model call. Some of them can be explicitly\nexcluded from being automatically included. This can serve two purposes:\n1. Using a more restricted / different action space.\n2. Improving the definitions / instructions of predefined functions.\n</pre>\n\n<code>\nrepeated string excluded_predefined_functions = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param values The excludedPredefinedFunctions to add.\n@return This builder for chaining."}
    :gcp.foreign.com.google.protobuf/ProtocolStringList]])

(defn ^Tool from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/Tool arg)
  (let [builder (Tool/newBuilder)]
    (when (some? (get arg :codeExecution))
      (.setCodeExecution builder
                         (CodeExecution-from-edn (get arg :codeExecution))))
    (when (some? (get arg :computerUse))
      (.setComputerUse builder (ComputerUse-from-edn (get arg :computerUse))))
    (when (some? (get arg :enterpriseWebSearch))
      (.setEnterpriseWebSearch builder
                               (EnterpriseWebSearch/from-edn
                                 (get arg :enterpriseWebSearch))))
    (when (seq (get arg :functionDeclarations))
      (.addAllFunctionDeclarations builder
                                   (map FunctionDeclaration/from-edn
                                     (get arg :functionDeclarations))))
    (when (some? (get arg :googleMaps))
      (.setGoogleMaps builder (GoogleMaps/from-edn (get arg :googleMaps))))
    (when (some? (get arg :googleSearch))
      (.setGoogleSearch builder
                        (GoogleSearch-from-edn (get arg :googleSearch))))
    (when (some? (get arg :googleSearchRetrieval))
      (.setGoogleSearchRetrieval builder
                                 (GoogleSearchRetrieval/from-edn
                                   (get arg :googleSearchRetrieval))))
    (when (some? (get arg :retrieval))
      (.setRetrieval builder (Retrieval/from-edn (get arg :retrieval))))
    (when (some? (get arg :urlContext))
      (.setUrlContext builder (UrlContext/from-edn (get arg :urlContext))))
    (.build builder)))

(defn to-edn
  [^Tool arg]
  {:post [(global/strict! :gcp.vertexai.api/Tool %)]}
  (when arg
    (cond-> {}
      (.hasCodeExecution arg) (assoc :codeExecution
                                (CodeExecution-to-edn (.getCodeExecution arg)))
      (.hasComputerUse arg) (assoc :computerUse
                              (ComputerUse-to-edn (.getComputerUse arg)))
      (.hasEnterpriseWebSearch arg) (assoc :enterpriseWebSearch
                                      (EnterpriseWebSearch/to-edn
                                        (.getEnterpriseWebSearch arg)))
      (seq (.getFunctionDeclarationsList arg))
        (assoc :functionDeclarations
          (map FunctionDeclaration/to-edn (.getFunctionDeclarationsList arg)))
      (.hasGoogleMaps arg) (assoc :googleMaps
                             (GoogleMaps/to-edn (.getGoogleMaps arg)))
      (.hasGoogleSearch arg) (assoc :googleSearch
                               (GoogleSearch-to-edn (.getGoogleSearch arg)))
      (.hasGoogleSearchRetrieval arg) (assoc :googleSearchRetrieval
                                        (GoogleSearchRetrieval/to-edn
                                          (.getGoogleSearchRetrieval arg)))
      (.hasRetrieval arg) (assoc :retrieval
                            (Retrieval/to-edn (.getRetrieval arg)))
      (.hasUrlContext arg) (assoc :urlContext
                             (UrlContext/to-edn (.getUrlContext arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nTool details that the model may use to generate response.\n\nA `Tool` is a piece of code that enables the system to interact with\nexternal systems to perform an action, or set of actions, outside of\nknowledge and scope of the model. A Tool object should contain exactly\none type of Tool (e.g FunctionDeclaration, Retrieval or\nGoogleSearchRetrieval).\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.Tool}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/Tool}
   [:codeExecution
    {:optional true,
     :getter-doc
       "<pre>\nOptional. CodeExecution tool type.\nEnables the model to execute code as part of generation.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.Tool.CodeExecution code_execution = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The codeExecution.",
     :setter-doc
       "<pre>\nOptional. CodeExecution tool type.\nEnables the model to execute code as part of generation.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.Tool.CodeExecution code_execution = 4 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:ref :gcp.vertexai.api/Tool.CodeExecution]]
   [:computerUse
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Tool to support the model interacting directly with the computer.\nIf enabled, it automatically populates computer-use specific Function\nDeclarations.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.Tool.ComputerUse computer_use = 11 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The computerUse.",
     :setter-doc
       "<pre>\nOptional. Tool to support the model interacting directly with the computer.\nIf enabled, it automatically populates computer-use specific Function\nDeclarations.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.Tool.ComputerUse computer_use = 11 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:ref :gcp.vertexai.api/Tool.ComputerUse]]
   [:enterpriseWebSearch
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Tool to support searching public web data, powered by Vertex AI\nSearch and Sec4 compliance.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.EnterpriseWebSearch enterprise_web_search = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The enterpriseWebSearch.",
     :setter-doc
       "<pre>\nOptional. Tool to support searching public web data, powered by Vertex AI\nSearch and Sec4 compliance.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.EnterpriseWebSearch enterprise_web_search = 6 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/EnterpriseWebSearch]
   [:functionDeclarations
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Function tool type.\nOne or more function declarations to be passed to the model along with the\ncurrent user query. Model may decide to call a subset of these functions\nby populating [FunctionCall][google.cloud.aiplatform.v1.Part.function_call]\nin the response. User should provide a\n[FunctionResponse][google.cloud.aiplatform.v1.Part.function_response] for\neach function call in the next turn. Based on the function responses, Model\nwill generate the final response back to the user. Maximum 128 function\ndeclarations can be provided.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.FunctionDeclaration function_declarations = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>",
     :setter-doc
       "<pre>\nOptional. Function tool type.\nOne or more function declarations to be passed to the model along with the\ncurrent user query. Model may decide to call a subset of these functions\nby populating [FunctionCall][google.cloud.aiplatform.v1.Part.function_call]\nin the response. User should provide a\n[FunctionResponse][google.cloud.aiplatform.v1.Part.function_response] for\neach function call in the next turn. Based on the function responses, Model\nwill generate the final response back to the user. Maximum 128 function\ndeclarations can be provided.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.FunctionDeclaration function_declarations = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:sequential {:min 1} :gcp.vertexai.api/FunctionDeclaration]]
   [:googleMaps
    {:optional true,
     :getter-doc
       "<pre>\nOptional. GoogleMaps tool type.\nTool to support Google Maps in Model.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.GoogleMaps google_maps = 5 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The googleMaps.",
     :setter-doc
       "<pre>\nOptional. GoogleMaps tool type.\nTool to support Google Maps in Model.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.GoogleMaps google_maps = 5 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/GoogleMaps]
   [:googleSearch
    {:optional true,
     :getter-doc
       "<pre>\nOptional. GoogleSearch tool type.\nTool to support Google Search in Model. Powered by Google.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.Tool.GoogleSearch google_search = 7 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The googleSearch.",
     :setter-doc
       "<pre>\nOptional. GoogleSearch tool type.\nTool to support Google Search in Model. Powered by Google.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.Tool.GoogleSearch google_search = 7 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    [:ref :gcp.vertexai.api/Tool.GoogleSearch]]
   [:googleSearchRetrieval
    {:optional true,
     :getter-doc
       "<pre>\nOptional. GoogleSearchRetrieval tool type.\nSpecialized retrieval tool that is powered by Google search.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.GoogleSearchRetrieval google_search_retrieval = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The googleSearchRetrieval.",
     :setter-doc
       "<pre>\nOptional. GoogleSearchRetrieval tool type.\nSpecialized retrieval tool that is powered by Google search.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.GoogleSearchRetrieval google_search_retrieval = 3 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/GoogleSearchRetrieval]
   [:retrieval
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Retrieval tool type.\nSystem will always execute the provided retrieval tool(s) to get external\nknowledge to answer the prompt. Retrieval results are presented to the\nmodel for generation.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.Retrieval retrieval = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The retrieval.",
     :setter-doc
       "<pre>\nOptional. Retrieval tool type.\nSystem will always execute the provided retrieval tool(s) to get external\nknowledge to answer the prompt. Retrieval results are presented to the\nmodel for generation.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.Retrieval retrieval = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/Retrieval]
   [:urlContext
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Tool to support URL context retrieval.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.UrlContext url_context = 10 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The urlContext.",
     :setter-doc
       "<pre>\nOptional. Tool to support URL context retrieval.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.UrlContext url_context = 10 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.vertexai.api/UrlContext]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/Tool schema,
              :gcp.vertexai.api/Tool.CodeExecution CodeExecution-schema,
              :gcp.vertexai.api/Tool.ComputerUse ComputerUse-schema,
              :gcp.vertexai.api/Tool.ComputerUse.Environment
                ComputerUse$Environment-schema,
              :gcp.vertexai.api/Tool.GoogleSearch GoogleSearch-schema,
              :gcp.vertexai.api/Tool.PhishBlockThreshold
                PhishBlockThreshold-schema}
    {:gcp.global/name "gcp.vertexai.api.Tool"}))