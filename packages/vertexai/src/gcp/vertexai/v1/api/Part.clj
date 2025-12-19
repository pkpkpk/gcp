(ns gcp.vertexai.v1.api.Part
  (:require [gcp.vertexai.v1.api.Blob :as blob]
            [gcp.vertexai.v1.api.CodeExecutionResult :as CodeExecutionResult]
            [gcp.vertexai.v1.api.ExecutableCode :as ExecutableCode]
            [gcp.vertexai.v1.api.FileData :as fd]
            [gcp.vertexai.v1.api.FunctionCall :as fnc]
            [gcp.vertexai.v1.api.FunctionResponse :as fnr]
            [gcp.vertexai.v1.api.VideoMetadata :as VideoMetadata]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.generativeai PartMaker]
           [com.google.cloud.vertexai.api Part]))

(defn ^Part from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/Part arg)
  (if (string? arg)
    (from-edn {:text arg})
    (if (contains? arg :mimeType)
      ;;TODO to make this json safe, recur this as inline blob
      (PartMaker/fromMimeTypeAndData (:mimeType arg) (:partData arg))
      (let [builder (Part/newBuilder)]
        (when-let [text (:text arg)]
          (.setText builder text))
        (when-let [inline (:inlineData arg)]
          (.setInlineData builder (blob/from-edn inline)))
        (when-let [functionCall (:functionCall arg)]
          (.setFunctionCall builder (fnc/from-edn functionCall)))
        (when-let [functionResponse (:functionResponse arg)]
          (.setFunctionResponse builder (fnr/from-edn functionResponse)))
        (when-let [fileData (:fileData arg)]
          (.setFileData builder (fd/from-edn fileData)))
        (when-let [executableCode (:executableCode arg)]
          (.setExecutableCode builder (ExecutableCode/from-edn executableCode)))
        (when-let [codeExecutionResult (:codeExecutionResult arg)]
          (.setCodeExecutionResult builder (CodeExecutionResult/from-edn codeExecutionResult)))
        (when-let [videoMetadata (:videoMetadata arg)]
          (.setVideoMetadata builder (VideoMetadata/from-edn videoMetadata)))
        (.build builder)))))

(defn ->edn [^Part part]
  {:post [(global/strict! :gcp.vertexai.v1.api/Part %)]}
  (cond-> {}
          (.hasText part)
          (assoc :text (.getText part))
          (.hasFileData part)
          (assoc :fileData (fd/to-edn (.getFileData part)))
          (.hasFunctionCall part)
          (assoc :functionCall (fnc/to-edn (.getFunctionCall part)))
          (.hasFunctionResponse part)
          (assoc :functionResponse (fnr/to-edn (.getFunctionResponse part)))
          (.hasInlineData part)
          (assoc :inlineData (blob/to-edn (.getInlineData part)))
          (.hasExecutableCode part)
          (assoc :executableCode (ExecutableCode/to-edn (.getExecutableCode part)))
          (.hasCodeExecutionResult part)
          (assoc :codeExecutionResult (CodeExecutionResult/to-edn (.getCodeExecutionResult part)))
          (.hasVideoMetadata part)
          (assoc :videoMetadata (VideoMetadata/to-edn (.getVideoMetadata part)))))

(def schema
  [:or
   {:doc              "A (union) datatype containing media that is part of a multi-part Content message. A Part consists of data which has an associated datatype. A Part can only contain one of the accepted types in Part.data. A Part must have a fixed IANA MIME type identifying the type and subtype of the media if the inlineData field is filled with raw bytes."
    :generativeai/url "https://ai.google.dev/api/caching#Part"
    :class            'com.google.cloud.vertexai.api.Part
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Part"}
   :string
   [:map {:closed true} ;; TODO this might not be JSON safe
    [:mimeType {:optional false} :string]
    [:partData
     {:doc      "(string|URI<gcs>|bytes|ByteString"
      :optional false}
     [:or 'bytes? :string 'uri? :gcp.protobuf/ByteString]]]
   [:map {:closed true}
    [:text {:optional false} :string]]
   [:map {:closed true}
    [:inlineData {:optional false} :gcp.vertexai.v1.api/Blob]]
   [:map {:closed true}
    [:functionCall {:optional false} :gcp.vertexai.v1.api/FunctionCall]]
   [:map {:closed true}
    [:functionResponse {:optional false} :gcp.vertexai.v1.api/FunctionResponse]]
   [:map {:closed true}
    [:fileData {:optional false} :gcp.vertexai.v1.api/FileData]]
   [:map {:closed true}
    [:executableCode {:optional false} :gcp.vertexai.v1.api/ExecutableCode]]
   [:map {:closed true}
    [:codeExecutionResult {:optional false} :gcp.vertexai.v1.api/CodeExecutionResult]]
   [:map {:closed true}
    [:videoMetadata {:optional false} :gcp.vertexai.v1.api/VideoMetadata]]])

(global/register-schema! :gcp.vertexai.v1.api/Part schema)