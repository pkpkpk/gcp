(ns gcp.vertexai.generativeai
  (:require [clojure.string :as string]
            [gcp.global :as g]
            [gcp.vertexai.v1.api.CountTokensRequest :as CountTokensRequest]
            [gcp.vertexai.v1.api.CountTokensResponse :as CountTokensResponse]
            [gcp.vertexai.v1.api.GenerateContentRequest :as GenerateContentRequest]
            [gcp.vertexai.v1.api.GenerateContentResponse :as GenerateContentResponse]
            [gcp.vertexai.v1.VertexAI]
            [jsonista.core :as j])
  (:import (com.google.api.core ApiFutureCallback ApiFutures)
           [com.google.cloud.vertexai VertexAI]
           (com.google.cloud.vertexai.generativeai ResponseStream ResponseStreamIteratorWithHistory)
           (com.google.common.util.concurrent MoreExecutors)))

(def synth-registry ^{::g/name ::synth}
  {:gcp.vertexai.synth/contentable           [:or
                                              {:error/message "Contentable should be singular or many Contents like (string|vector<Part>|{:user 'model' :parts [Part...]})"}
                                              :gcp.vertexai.v1.api/Content
                                              [:sequential :gcp.vertexai.v1.api/Content]]
   :gcp.vertexai.synth/clientable            [:maybe
                                              [:or
                                               (g/instance-schema com.google.cloud.vertexai.VertexAI)
                                               :gcp.vertexai.v1/VertexAI]]
   :gcp.vertexai.synth/requestable           [:and
                                              {:doc "requestable === (model config) + :contents + :client"}
                                              :gcp.vertexai.v1.api/GenerateContentRequest
                                              [:map
                                               [:vertexai {:optional false} :gcp.vertexai.synth/clientable]]]})

(g/include-schema-registry! synth-registry)

(def models
  {:gemini-3-pro-preview               "publishers/google/models/gemini-3-pro-preview"
   :gemini-3-flash-preview             "publishers/google/models/gemini-3-flash-preview"
   :gemini-3-pro-image-preview         "publishers/google/models/gemini-3-pro-image-preview"

   :gemini-2.5-pro                     "publishers/google/models/gemini-2.5-pro"
   :gemini-2.5-flash                   "publishers/google/models/gemini-2.5-flash"
   :gemini-2.5-flash-image             "publishers/google/models/gemini-2.5-flash-image"
   :gemini-2.5-flash-lite              "publishers/google/models/gemini-2.5-flash-lite"
   :gemini-live-2.5-flash-native-audio "publishers/google/models/gemini-live-2.5-flash-native-audio"

   :gemini-2.0-flash                   "publishers/google/models/gemini-2.0-flash"
   :gemini-2.0-flash-lite              "publishers/google/models/gemini-2.0-flash-lite"

   :gemini-1.5-pro                     "publishers/google/models/gemini-1.5-pro"
   :gemini-1.5-pro-001                 "publishers/google/models/gemini-1.5-pro-001"
   :gemini-1.5-pro-002                 "publishers/google/models/gemini-1.5-pro-002"

   :gemini-1.5-flash                   "publishers/google/models/gemini-1.5-flash"
   :gemini-1.5-flash-001               "publishers/google/models/gemini-1.5-flash-001"
   :gemini-1.5-flash-002               "publishers/google/models/gemini-1.5-flash-002"
   :gemini-1.5-flash-8b                "publishers/google/models/gemini-1.5-flash-8b"

   :gemini-1.0-pro                     "publishers/google/models/gemini-1.0-pro"
   :gemini-1.0-pro-001                 "publishers/google/models/gemini-1.0-pro-001"
   :gemini-1.0-pro-002                 "publishers/google/models/gemini-1.0-pro-002"
   :gemini-1.0-pro-vision              "publishers/google/models/gemini-1.0-pro-vision"})

(defonce ^:dynamic *client* nil)

(defn ^VertexAI client
  ([] (client nil))
  ([arg]
   (or *client*
       (do
         (g/strict! :gcp.vertexai.synth/clientable arg)
         (if (instance? VertexAI arg)
           arg
           (g/client :gcp.vertexai.v1/VertexAI arg))))))

(defn requestable? [o]
  (g/valid? :gcp.vertexai.synth/requestable o))

(defn model-config [m]
  (g/coerce :gcp.vertexai.synth/ModelConfig m))

(defn contentable? [o]
  (g/valid? :gcp.vertexai.synth/contentable o))

(defn- resource-name [model-name ^VertexAI client]
  (let [model-name (if (keyword? model-name)
                     (or (get models model-name)
                         (throw (ex-info (str "Unknown model keyword: " model-name)
                                         {:model-name model-name :available (keys models)})))
                     model-name)]
    (if (string/starts-with? model-name "projects/")
      model-name
      (if (string/starts-with? model-name "publishers/")
        (format "projects/%s/locations/%s/%s"
                (.getProjectId client)
                (.getLocation client)
                model-name)
        (format "projects/%s/locations/%s/publishers/google/models/%s"
                (.getProjectId client)
                (.getLocation client)
                (let [*model-name (atom model-name)]
                  (doseq [prefix #{"projects/" "publishers/" "models/"}]
                    (when (string/starts-with? @*model-name prefix)
                      (swap! *model-name subs (inc (.lastIndexOf ^String @*model-name \/)))))
                  @*model-name))))))

(defn- resolve-model-name
  [model-name vertexai]
  (cond
    (nil? model-name)
    nil

    (keyword? model-name)
    (let [resolved (get models model-name)]
      (when-not resolved
        (throw (ex-info (str "Unknown model keyword: " model-name)
                        {:model-name model-name :available (keys models)})))
      (if (some? vertexai) (resource-name resolved vertexai) resolved))

    (some? vertexai)
    (resource-name model-name vertexai)

    :else
    model-name))

(defn- normalize-model
  [m]
  (if-not (contains? m :model)
    m
    (let [vertexai (:vertexai m)]
      (update m :model resolve-model-name vertexai))))

(defn as-request
  ([request-like]
   (let [request-like (normalize-model request-like)
         contents     (get request-like :contents)]
     (when (nil? contents)
       (throw (Exception. ":contents must be seq of Content maps")))
     (when-not (g/valid? [:sequential :gcp.vertexai.v1.api/Content] contents)
       (let [explanation (g/explain [:sequential :gcp.vertexai.v1.api/Content] contents)
             msg         (str ":contents schema failed : " (g/humanize explanation))]
         (throw (ex-info msg {:explanation explanation :request-like request-like}))))
     (when-not (g/valid? :gcp.vertexai.v1.api/GenerateContentRequest request-like)
       (let [explanation (g/explain :gcp.vertexai.v1.api/GenerateContentRequest request-like)
             msg         (str "cannot form request: " (g/humanize explanation))]
         (throw (ex-info msg {:explanation explanation :request-like request-like}))))
     (GenerateContentRequest/from-edn request-like)))
  ([gm-like & contentables]
   (if (g/valid? [:seqable :gcp.vertexai.synth/contentable] contentables)
     (as-request (assoc gm-like :contents (vec contentables)))
     (let [explanation (g/explain [:seqable :gcp.vertexai.synth/contentable] contentables)
           msg         (str "cannot form content seq from var-args: " (g/humanize explanation))]
       (throw (ex-info msg {:explanation explanation :contentables contentables}))))))

(defn- as-requestable
  ([requestable]
   (let [requestable (-> requestable
                         (update :vertexai client)
                         normalize-model)]
     (if (requestable? requestable)
       requestable
       (if (nil? (:vertexai requestable))
         (throw (Exception. ":vertexai must have VertexAI client instance"))
         (let [contents (:contents requestable)]
           (if (nil? contents)
             (throw (Exception. ":contents must be seq of Content maps"))
             (if-not (g/valid? [:sequential :gcp.vertexai.v1.api/Content] contents)
               (let [explanation (g/explain [:sequential :gcp.vertexai.v1.api/Content] contents)
                     msg         (str ":contents schema failed : " (g/humanize explanation))]
                 (throw (ex-info msg {:explanation explanation :requestable requestable})))
               (let [explanation (g/explain :gcp.vertexai.synth/Requestable requestable)
                     msg         (str "cannot form request: " (g/humanize explanation))]
                 (throw (ex-info msg {:explanation explanation :requestable requestable}))))))))))
  ([gm-like & contentables]
   (if (g/valid? [:seqable :gcp.vertexai.synth/contentable] contentables)
     (as-requestable (assoc gm-like :contents (vec contentables)))
     (let [explanation (g/explain [:seqable :gcp.vertexai.synth/contentable] contentables)
           msg         (str "cannot form content seq from var-args: " (g/humanize explanation))]
       (throw (ex-info msg {:explanation explanation :contentables contentables}))))))

(defn desugar
  "Convert edn form with tolerant Content sugar into strict representation suitable for JSON requests."
  ([request-like]
   (GenerateContentRequest/to-edn (as-request request-like)))
  ([gm-like & contentables]
   (if (g/valid? [:seqable :gcp.vertexai.synth/contentable] contentables)
     (desugar (assoc gm-like :contents (vec contentables)))
     (let [explanation (g/explain [:seqable :gcp.vertexai.synth/contentable] contentables)
           msg         (str "cannot form content seq from var-args: " (g/humanize explanation))]
       (throw (ex-info msg {:explanation explanation :contentables contentables}))))))

(defn generate-content
  "Send a request with multimodal 'content' and synchronously wait for a response.

   ```clojure
     (generate-content requestable)
     (generate-content gm-like contentable)
     (generate-content gm-like contentable & more-contentables)
   ```

   A 'requestable' is a :gcp.vertexai.api.GenerateContentRequest map with
   (optional) VertexAI client in the :vertexai key. It contains all
   information necessary to make a request.

     (assoc gm-like :contents [\"context\" \"and\" \"prompts\" \"go\" \"here\"]])

   You can omit the :vertexai client and it will you the default from your DAC,
   but for vertexai in particular some services and models are only available
   in limited locations

   ```clojure
     (generate-content {:vertexai {:location \"us-central1\"}
                        :model \"gemini-1.5-flash-001\"
                        :systemInstruction \"You are a math tutor...\"
                        :contents [\"quiz me on trigonometry\"]})
   ```

   Some details you may want hold constant across many requests while
   varying content. You can separate model configuration into a map
   in first arg with n-many 'contentable' arguments after

   ```clojure
     (def math-tutor {:vertexai  client
                      :model \"gemini-1.5-flash-001\"
                      :systemInstruction \"You are a math tutor...\"})

     (generate-content math-tutor \"quiz me on trigonometry\")
     (generate-content math-tutor \"what is 2 + 2\")
   ```

   'contentable' is (string, Content, seq<Content>, seq<seq<Part>>) where:
     Content: (string | ContentMap | Seq<Part>)
     ContentMap:  {:role ('user'|'model')
                   :parts (String|Seq<(String|Part)>)}
        - role defaults to 'user' and is almost always elided
     Part: one-of
      {:partData (String, gcs URI,bytes, ByteString)
       :mimeType String}
      {:text String}
      {:inlineData Blob}
      {:functionCall FunctionCall}
      {:functionResponse FunctionResponse}
      {:fileData FileData}
    Blob: {:mimeType String, :data (bytes|bb|ByteString|seq<ByteString>)
    FileData: {:mimeType String, :fileUri String}
    FunctionCall: {:name String, :args Struct}
    FunctionResponse: {:name String, :response Struct}
    Struct: {String Value}
    Value: (nil, bool, number, string, seq<Value>, Struct)

   `(generate-content gm 'foo')`
      is sugar for
   `(generate-content gm {:role 'user', :parts [{:text 'foo'}]})`

   `(generate-content gm [{:mimeType 'audio/mp3'
                          :partData 'gs://cloud-samples-data/generative-ai/audio/pixel.mp3'}])`
     is sugar for
   `(generate-content gm {:role 'user', :parts [{:fileData {:mimeType 'audio/mp3', ...}}]})`"
  ([requestable]
   (let [requestable (as-requestable requestable)]
     (-> (:vertexai requestable)
         (.getPredictionServiceClient)
         (.generateContentCallable)
         (.call (GenerateContentRequest/from-edn requestable))
         GenerateContentResponse/to-edn)))
  ([gm-like & contentables]
   (generate-content (apply (partial as-requestable gm-like) contentables))))

(defn generate-content-async
  "same as generate-content but uses direct executor to deliver a promise."
  ([requestable]
   (let [p (promise)
         fut (-> (:vertexai requestable)
                 (.getPredictionServiceClient)
                 (.generateContentCallable)
                 (.futureCall (GenerateContentRequest/from-edn requestable)))]
     (ApiFutures/addCallback fut
                             (reify ApiFutureCallback
                               (onSuccess [_ result] (deliver p (GenerateContentResponse/to-edn result)))
                               (onFailure [_ throwable] (deliver p throwable)))
                             (MoreExecutors/directExecutor))
     p))
  ([gm-like & contentables]
   (generate-content-async (apply (partial as-requestable gm-like) contentables))))

(defn count-tokens
  ([request-like]
   (g/coerce [:seqable :gcp.vertexai.synth/contentable] (:contents request-like))
   (let [{:keys [model vertexai] :as request-like} (as-requestable request-like)
         request (CountTokensRequest/from-edn (assoc request-like
                                                :model (resource-name model vertexai)
                                                :endpoint (resource-name model vertexai)))]
     (CountTokensResponse/to-edn (.countTokens (.getLlmUtilityClient vertexai) request))))
  ([gm & contentables]
   (count-tokens (apply (partial as-requestable gm) contentables))))

(defn response-text [response]
  (get-in response [:candidates 0 :content :parts 0 :text]))

(defn response-json [response]
  (try
    (some-> response response-text (j/read-value j/keyword-keys-object-mapper))
    (catch Exception e
      (throw (ex-info (str "error parsing json from response: " (ex-message e))
                      {:response response
                       :cause e})))))
