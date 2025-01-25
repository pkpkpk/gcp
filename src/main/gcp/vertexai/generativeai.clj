(ns gcp.vertexai.generativeai
  (:require [clojure.string :as string]
            [gcp.global :as g]
            [gcp.vertexai.v1 :as v1]
            [gcp.vertexai.v1.api.Candidate :as Candidate]
            [gcp.vertexai.v1.api.CitationMetadata :as CitationMetadata]
            [gcp.vertexai.v1.api.Content :as Content]
            [gcp.vertexai.v1.api.CountTokensRequest :as CountTokensRequest]
            [gcp.vertexai.v1.api.CountTokensResponse :as CountTokensResponse]
            [gcp.vertexai.v1.api.GenerateContentRequest :as GenerateContentRequest]
            [gcp.vertexai.v1.api.GenerateContentResponse :as GenerateContentResponse]
            [gcp.vertexai.v1.generativeai.protocols :as impl]
            [gcp.vertexai.v1.VertexAI])
  (:import (com.google.api.core ApiFutureCallback ApiFutures)
           [com.google.cloud.vertexai VertexAI]
           (com.google.cloud.vertexai.generativeai ResponseStream ResponseStreamIteratorWithHistory)
           (com.google.common.util.concurrent MoreExecutors)))

(gcp.global/include! v1/registry)

(defn client
  ([]
   (client {}))
  ([arg]
   (gcp.vertexai.v1.VertexAI/from-edn arg)))

(defn requestable? [o]
  (g/valid? :vertexai.synth/Requestable o))

(defn model-config [m]
  (g/coerce :vertexai.synth/ModelConfig m))

(defn contentable? [o]
  (g/valid? :vertexai.synth/Contentable o))

(defn- resource-name [^String model-name ^VertexAI client]
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
                @*model-name)))))

(defn- as-requestable
  ([requestable]
   (if (requestable? requestable)
     (assoc requestable :model (resource-name (:model requestable) (:vertexai requestable)))
     (if (nil? (:vertexai requestable))
       (throw (Exception. ":vertexai must have VertexAI client instance"))
       (if (nil? (get requestable :contents))
         (throw (Exception. ":contents must be seq of Content maps"))
         (if (not (g/valid? [:sequential :vertexai.api/Content] (:contents requestable)))
           (let [explanation (g/explain [:sequential :vertexai.api/Content] (:contents requestable))
                 msg         (str ":contents schema failed : " (g/humanize explanation))]
             (throw (ex-info msg {:explanation explanation :requestable requestable})))
           (let [explanation (g/explain :vertexai.synth/Requestable requestable)
                 msg         (str "cannot form request: " (g/humanize explanation))]
             (throw (ex-info msg {:explanation explanation :requestable requestable}))))))))
  ([gm-like contentable]
   (if (contentable? contentable)
     (as-requestable (assoc gm-like :contents (cond-> contentable (not (sequential? contentable)) list)))
     (let [explanation (g/explain :vertexai.synth/Contentable contentable)
           msg         (str "cannot form content seq from contentable: " (g/humanize explanation))]
       (throw (ex-info msg {:explanation explanation :contentable contentable})))))
  ([gm-like contentable & more]
   (if (contentable? contentable)
     (if (g/valid? [:sequential :vertexai.synth/Contentable] more)
       (as-requestable gm-like (reduce (fn [acc c] (if (sequential? c) (into acc c) (conj acc c)))
                                         (cond-> contentable (not (sequential? contentable)) vector)
                                         more))
       (let [explanation (g/explain [:sequential :vertexai.synth/Contentable] more)
             msg         (str "cannot reduce overloaded contentable arguments: " (g/humanize explanation))]
         (throw (ex-info msg {:explanation explanation :more more}))))
     (let [explanation (g/explain :vertexai.synth/Contentable contentable)
           msg         (str "cannot form content seq from contentable: " (g/humanize explanation))]
       (throw (ex-info msg {:explanation explanation :contentable contentable}))))))

(defn generate-content
  "Send a request with multimodal 'content' and synchronously wait for a response.

   ```clojure
     (generate-content requestable)
     (generate-content gm-like contentable)
     (generate-content gm-like contentable & more-contentables)
   ```

   A 'requestable' is a GenerateRequestResponse map with a VertexAI client
   added in the :vertexai key. It contains all information necessary to make
   a request (assoc gm-like :contents content-seq).

   ```clojure
     (generate-content {:vertexai client
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
   (-> (:vertexai requestable)
       (.getPredictionServiceClient)
       (.generateContentCallable)
       (.call (GenerateContentRequest/from-edn requestable))
       GenerateContentResponse/to-edn))
  ([gm-like contentable]
   (generate-content (as-requestable gm-like contentable)))
  ([gm-like contentable & more]
   (generate-content (apply (partial as-requestable gm-like contentable) more))))

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
  ([gm-like contentable]
   (generate-content-async (as-requestable gm-like contentable)))
  ([gm-like contentable & more]
   (generate-content-async (apply (partial as-requestable gm-like contentable) more))))

(defn generate-content-seq
  "returns seq of response maps using ResponseStream iterable"
  ([requestable]
   (let [stream (-> (:vertexai requestable)
                    (.getPredictionServiceClient)
                    (.streamGenerateContentCallable)
                    (.call (GenerateContentRequest/from-edn requestable))
                    (.iterator)
                    (ResponseStreamIteratorWithHistory.)
                    (ResponseStream.))]
     (map GenerateContentResponse/to-edn stream)))
  ([gm-like contentable]
   (generate-content-seq (as-requestable gm-like contentable)))
  ([gm-like contentable & more]
   (generate-content-seq (apply (partial as-requestable gm-like contentable) more))))

(defn count-tokens
  ([{:keys [vertexai model] :as gm} contentable]
   (g/strict! :vertexai.synth/Contentable contentable)
   (let [contents (cond-> contentable (not (sequential? contentable)) list)
         request (CountTokensRequest/from-edn (assoc gm :contents contents
                                                        :model (resource-name model vertexai)
                                                        :endpoint (resource-name model vertexai)))]
     (CountTokensResponse/to-edn (.countTokens (.getLlmUtilityClient vertexai) request))))
  ([gm contentable & more]
   (count-tokens gm (reduce (fn [acc c] (if (sequential? c) (into acc c) (conj acc c)))
                            (cond-> contentable (not (sequential? contentable)) vector)
                            more))))

#!-----------------------------------------------------------------------------
#!
#! ChatSession
#!

(defn- default-history []
  (let [*state (atom [])]
    (reify impl/IHistory
      (history-to-contentable [_] @*state)
      (history-revert [_ drop-count] (swap! *state subvec 0 (max (- (count @*state) drop-count) 0)))
      (history-add [_ contentable] (swap! *state conj contentable)))))

(defn- check-finish-reason-and-edit-history [chat response]
  (let [finishReason (get-in response [:candidates 0 :finishReason])]
    (when (and (not= "STOP" finishReason) (not= "MAX_TOKEN" finishReason))
      (impl/history-revert (:history chat) (deref (:*previousHistorySize chat)))
      (throw (Exception. (format "The last round of conversation will not be added to history because response stream did not finish normally. Finish reason is %s." finishReason))))))

(defn- merge-adjacent-strings [coll part]
  (if (empty? coll)
    [part]
    (if (and (string? part) (string? (peek coll)))
      (conj (vec (butlast coll)) (str (peek coll) part))
      (conj coll part))))

(defn aggregate-response-stream [response-stream]
  (let [*res                         (atom nil)
        *candidates                  (atom {})
        *aggregated-content-parts    (atom {})               ; candidate-index => seq<parts>
        *aggregated-citations        (atom {})]
    (doseq [response (map GenerateContentResponse/from-edn (seq response-stream))]
      (reset! *res response)
      (doseq [{:keys [index content citationMetadata] :as candidate} (get response :candidates)]
        (assert (some? index))
        (swap! *candidates assoc index candidate)
        (when content
          (let [parts' (reduce merge-adjacent-strings [] (:parts content))]
            ;; TODO This is the logic in ResponseHandler.java... it seems to assume
            ;; that candidate indexes grow across responses within the stream?
            ;; ie response_0_candidate_0 has index 0, response_1_candidate_0 has index 1 ?
            ;; or that only one candidate is present and we are concatenating parts?
            ;; either way, this feels brittle without knowing stream mechanics.
            ;; for example, multiple 'response_i_candidate_2' would be grouped in such a way that disguises
            ;; useful ordering
            (swap! *aggregated-content-parts update-in [index] (fn [?ps] (if ?ps (into ?ps parts') parts')))))
        (when-some [citations (some-> citationMetadata :citations seq)]
          (swap! *aggregated-citations update-in [index] (fn [?cs] (if ?cs (into ?cs citations) citations))))))
    (let [aggregated-candidates* (atom [])]
      (doseq [[i candidate] (map-indexed vector @*candidates)]
        (let [builder (.toBuilder (Candidate/from-edn candidate))]
          (when-let [parts (get @*aggregated-content-parts i)]
            (.setContent builder (Content/from-edn {:role "model" :parts parts})))
          (when-let [citations (get @*aggregated-citations i)]
            (.setCitationMetadata builder (CitationMetadata/from-edn {:citations citations})))
          (swap! aggregated-candidates* (Candidate/to-edn (.build builder)))))
      (assoc @*res :candidates @aggregated-candidates*))))

(defn- check-last-response-and-edit-history [chat]
  (when-let [current-response (deref (:*currentResponse chat))]
    (reset! (:*currentResponse chat) nil)
    (check-finish-reason-and-edit-history chat current-response)
    (impl/history-add (:history chat) (get-in current-response [:candidates 0 :content]))
    ;; from comment in ChatSession.java:
    ;;  If `checkFinishReasonAndEditHistory` passes, we add 2 contents:
    ;;     (user's message) + (model's response)
    ;;  to the history and then one round of conversation is finished.
    ;;  ... So we add 2 to the previousHistorySize.
    (swap! (:*previousHistorySize chat) + 2))
  (when-let [current-response-stream (deref (:*currentResponseStream chat))]
    (if-not (.isConsumed current-response-stream)
      (throw (ex-info "response stream is not consumed" {:chat chat
                                                         :currentResponseStream current-response-stream}))
      (let [response (aggregate-response-stream current-response-stream)]
        (reset! (:*currentResponseStream chat) nil)
        (check-finish-reason-and-edit-history chat response)
        (impl/history-add (:history chat) (get-in response [:candidates 0 :content]))
        (swap! (:*previousHistorySize chat) + 2)))))

(defn- call [{:keys [library *responderState]} fnc]
  {:post [(gcp.global/strict! :vertexai.api/Part %)]}
  (if (neg? (get @*responderState :remainingCalls))
    (throw (Exception. "exceeded max calls"))
    (if-let [f (get library (:name fnc))]
      (do
        (swap! *responderState update-in [:remainingCalls] dec)
        (try
          {:functionResponse {:name     (:name fnc)
                              :response {"result" (f (:args fnc))}}}
          (catch Exception e
            (throw (ex-info (str "Exception in chat library call '" (:name fnc) "': " (ex-message e))
                            {:error        e
                             :f            f
                             :functionCall fnc})))))
      (throw (ex-info (str "model request function '" (:name fnc) "' was not found")
                      {:functionCall fnc
                       :library      library})))))

(declare send-msg)

(defn- auto-respond
  [{:keys [library *previousHistorySize *responderState] :as chat} response]
  (if (empty? library)
    response
    (let [parts (get-in response [:candidates 0 :content :parts])]
      (if-some [function-calls (seq (filter some? (map :functionCall parts)))]
        ; Via comment from ChatSession.java :
        ; 'Each time we call the `autoRespond'
        ;  add 2 contents (user's functionResponse and model's // functionCall) to the history
        ;  and update the previousHistorySize during `checkLastResponseAndEditHistory`.
        ;  But we shouldn't update the previousHistorySize because we
        ;  will revert the whole history if any intermediate step fails. So we offset the
        ;  previousHistorySize by 2 here.
        (try
          (swap! *previousHistorySize - 2)
          (send-msg chat {:parts (mapv (partial call chat) function-calls)})
          (finally
            (swap! *responderState (fn [{:keys [maxCalls]}] {:remainingCalls maxCalls :maxCalls maxCalls}))))
        response))))


;; TODO send-msg-async
;; TODO pass in history? :history == content, :chat/IHistory -> IHistory ??
;; readline-chat
;; IChat protocol... undo, redo, fork, map/reduce/walk

(defn chat-session ; :vertexai.synth/ModelConfig + :startingHistory ?
  [{:keys [startingHistory
           *currentResponse
           *currentResponseStream
           *previousHistorySize
           *responderState] :as arg}]
  {:post [(gcp.global/strict! :vertexai.synth/ChatSession %)]}
  (cond-> (assoc arg :history (if (nil? startingHistory)
                                (default-history)
                                (if (satisfies? impl/IHistory arg)
                                  (throw (Exception. "kaboom"))
                                  (throw (Exception. "kaboom")))))
          (nil? *responderState) (assoc :*responderState (atom {:maxCalls       10
                                                                :remainingCalls 10}))
          (nil? *currentResponse) (assoc :*currentResponse (atom nil))
          (nil? *currentResponseStream) (assoc :*currentResponseStream (atom nil))
          (nil? *previousHistorySize) (assoc :*previousHistorySize (atom 0))))

(defn clone-chat-session
  [{:keys [*currentResponse
           *currentResponseStream
           *previousHistorySize
           *responderState] :as chat}]
  (assoc chat :*currentResponse (atom @*currentResponse)
              :*currentResponseStream (atom @*currentResponseStream)
              :*previousHistorySize (atom @*previousHistorySize)
              :*responderState (atom @*responderState)))

(defn send-msg
  "Sends a message to the model and returns a response."
  ([chat-session contentable]
   (gcp.global/strict! :vertexai.synth/ChatSession chat-session)
   (check-last-response-and-edit-history chat-session)
   (impl/history-add (:history chat-session) contentable)
   (try
     (let [{:as response} (generate-content chat-session (impl/history-to-contentable (:history chat-session)))]
       (reset! (:*currentResponse chat-session) response)
       (auto-respond chat-session response))
     (catch Exception e
       (check-last-response-and-edit-history chat-session)
       (impl/history-revert (:history chat-session) (deref (:*previousHistorySize chat-session)))
       (throw e))))
  ([chat contentable & more]
   (send-msg chat (reduce (fn [acc c] (if (sequential? c) (into acc c) (conj acc c)))
                          (cond-> contentable (not (sequential? contentable)) vector)
                          more))))

(defn send-msg-stream
  "Sends a message to the model and returns a response."
  [chat contentable]
  (gcp.global/strict! :vertexai.synth/ChatSession chat)
  (check-last-response-and-edit-history chat)
  (impl/history-add (:history chat) contentable)
  (try
    (let [stream (generate-content-seq (impl/history-to-contentable (:history chat)))]
      (reset! (:*currentResponseStream chat) stream)
      (auto-respond chat (aggregate-response-stream stream)))
    (catch Exception e
      (check-last-response-and-edit-history chat)
      (impl/history-revert (:history chat) (deref (:*previousHistorySize chat)))
      (throw e))))
