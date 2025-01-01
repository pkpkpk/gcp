(ns gcp.vertexai.v1.generativeai
  (:require [clojure.string :as string]
            [gcp.global :as global :refer [instance-schema satisfies-schema]]
            gcp.vertexai.v1
            [gcp.vertexai.v1.api.Candidate :as Candidate]
            [gcp.vertexai.v1.api.CitationMetadata :as CitationMetadata]
            [gcp.vertexai.v1.api.Content :as Content]
            [gcp.vertexai.v1.api.CountTokensRequest :as CountTokensRequest]
            [gcp.vertexai.v1.api.CountTokensResponse :as CountTokensResponse]
            [gcp.vertexai.v1.api.GenerateContentRequest :as GenerateContentRequest]
            [gcp.vertexai.v1.api.GenerateContentResponse :as GenerateContentResponse]
            [gcp.vertexai.v1.api.GenerationConfig :as GenerationConfig]
            [gcp.vertexai.v1.api.Part :as Part]
            [gcp.vertexai.v1.api.SafetySetting :as SafetySetting]
            [gcp.vertexai.v1.api.Tool :as Tool]
            [gcp.vertexai.v1.api.ToolConfig :as ToolConfig]
            [gcp.vertexai.v1.VertexAI]
            [malli.core :as m]
            [malli.error :as me])
  (:import (com.google.api.core ApiFutureCallback ApiFutures)
           [com.google.cloud.vertexai VertexAI]
           (com.google.cloud.vertexai.generativeai ResponseStream ResponseStreamIteratorWithHistory)
           (com.google.common.util.concurrent MoreExecutors)))

(defn client
  ([]
   (client {}))
  ([arg]
   (gcp.vertexai.v1.VertexAI/from-edn arg)))

(def ^{:private true
       :doc "model config + :contents + client"}
  requestable-schema
  [:and GenerateContentRequest/schema
   [:map
    [:vertexai {:optional false} (global/instance-schema VertexAI)]]])

(defn requestable? [o]
  (m/validate requestable-schema o))

; (defn model-config? [])

(def ^:private contentable-schema
  [:or :string Content/schema [:sequential Content/schema]])

(defn contentable? [o]
  (m/validate contentable-schema o))

(defn- reconcile-model-name [^String model-name]
  (let [_model-name (atom model-name)]
    (doseq [prefix #{"projects/" "publishers/" "models/"}]
      (when (string/starts-with? @_model-name prefix)
        (swap! _model-name subs (inc (.lastIndexOf ^String @_model-name \/)))))
    @_model-name))

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
              (reconcile-model-name model-name)))))

(defn- as-requestable
  ([requestable]
   (if (requestable? requestable)
     (assoc requestable :model (resource-name (:model requestable) (:vertexai requestable)))
     (if (nil? (:vertexai requestable))
       (throw (Exception. ":vertexai must have VertexAI client instance"))
       (if (nil? (get requestable :contents))
         (throw (Exception. ":contents must be seq of Content maps"))
         (if (not (m/validate [:sequential Content/schema] (:contents requestable)))
           (let [explanation (m/explain [:sequential Content/schema] (:contents requestable))
                 msg         (str ":contents schema failed : " (me/humanize explanation))]
             (throw (ex-info msg {:explanation explanation :requestable requestable})))
           (let [explanation (m/explain requestable-schema requestable)
                 msg         (str "cannot form request: " (me/humanize explanation))]
             (throw (ex-info msg {:explanation explanation :requestable requestable}))))))))
  ([gm-like contentable]
   (if (contentable? contentable)
     (as-requestable (assoc gm-like :contents (cond-> contentable (not (sequential? contentable)) list)))
     (let [explanation (m/explain contentable-schema contentable)
           msg         (str "cannot form content seq from contentable: " (me/humanize explanation))]
       (throw (ex-info msg {:explanation explanation :contentable contentable})))))
  ([gm-like contentable & more]
   (if (contentable? contentable)
     (if (m/validate [:sequential contentable-schema] more)
       (as-requestable gm-like (reduce (fn [acc c] (if (sequential? c) (into acc c) (conj acc c)))
                                         (cond-> contentable (not (sequential? contentable)) vector)
                                         more))
       (let [explanation (m/explain [:sequential contentable-schema] more)
             msg         (str "cannot reduce overloaded contentable arguments: " (me/humanize explanation))]
         (throw (ex-info msg {:explanation explanation :more more}))))
     (let [explanation (m/explain contentable-schema contentable)
           msg         (str "cannot form content seq from contentable: " (me/humanize explanation))]
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
   (global/strict! contentable-schema contentable)
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

(defprotocol IHistory
  :extend-via-metadata true
  (history-to-contentable [this])
  (history-clone [this] "produce new state container w/ identical content")
  (history-revert [this n] "drop last n contentables from conversation")
  (history-add [this contentable] "add a contentable to conversation")
  (history-count [this] "how many contentables are in context")
  (history-token-count [this] "how many tokens are in context"))

(defn- default-history []
  (let [state* (atom [])]
    (reify IHistory
      (history-to-contentable [_] @state*)
      (history-revert [_ drop-count] (swap! state* subvec 0 (max (- (count @state*) drop-count) 0)))
      (history-add [_ contentable] (swap! state* conj contentable)))))

(defn- check-finish-reason-and-edit-history [chat response]
  (let [finishReason (get-in response [:candidates 0 :finishReason])]
    (when (and (not= "STOP" finishReason) (not= "MAX_TOKEN" finishReason))
      (history-revert (:history chat) (deref (:*previousHistorySize chat)))
      (throw (Exception. (format "The last round of conversation will not be added to history because response stream did not finish normally. Finish reason is %s." finishReason))))))

(defn- merge-adjacent-strings [coll part]
  (if (empty? coll)
    [part]
    (if (and (string? part) (string? (peek coll)))
      (conj (vec (butlast coll)) (str (peek coll) part))
      (conj coll part))))

(defn- aggregate-response-stream [response-stream]
  (let [res*                         (atom nil)
        candidates*                  (atom {})
        aggregated-content-parts*    (atom {})               ; candidate-index => seq<parts>
        aggregated-citations*        (atom {})]
    (doseq [response (map GenerateContentResponse/from-edn (seq response-stream))]
      (reset! res* response)
      (doseq [{:keys [index content citationMetadata] :as candidate} (get response :candidates)]
        (assert (some? index))
        (swap! candidates* assoc index candidate)
        (when content
          (let [parts' (reduce merge-adjacent-strings [] (:parts content))]
            ;; TODO This is the logic in ResponseHandler.java... it seems to assume
            ;; that candidate indexes grow across responses within the stream?
            ;; ie response_0_candidate_0 has index 0, response_1_candidate_0 has index 1 ?
            ;; or that only one candidate is present and we are concatenating parts?
            ;; either way, this feels brittle without knowing stream mechanics.
            ;; for example, multiple 'response_i_candidate_2' would be grouped in such a way that disguises
            ;; useful ordering
            (swap! aggregated-content-parts* update-in [index] (fn [?ps] (if ?ps (into ?ps parts') parts')))))
        (when-some [citations (some-> citationMetadata :citations seq)]
          (swap! aggregated-citations* update-in [index] (fn [?cs] (if ?cs (into ?cs citations) citations))))))
    (let [aggregated-candidates* (atom [])]
      (doseq [[i candidate] (map-indexed vector @candidates*)]
        (let [builder (.toBuilder (Candidate/from-edn candidate))]
          (when-let [parts (get @aggregated-content-parts* i)]
            (.setContent builder (Content/from-edn {:role "model" :parts parts})))
          (when-let [citations (get @aggregated-citations* i)]
            (.setCitationMetadata builder (CitationMetadata/from-edn {:citations citations})))
          (swap! aggregated-candidates* (Candidate/to-edn (.build builder)))))
      (assoc @res* :candidates @aggregated-candidates*))))

(defn- check-last-response-and-edit-history [chat]
  (when-let [current-response (deref (:*currentResponse chat))]
    (reset! (:*currentResponse chat) nil)
    (check-finish-reason-and-edit-history chat current-response)
    (history-add (:history chat) (get-in current-response [:candidates 0 :content]))
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
        (history-add (:history chat) (get-in response [:candidates 0 :content]))
        (swap! (:*previousHistorySize chat) + 2)))))

(defn- call [{:keys [library *responderState]} fnc]
  {:post [(gcp.global/strict! Part/schema %)]}
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

(def ^:private chat-session-schema
  [:map
   [:model :string]
   ;;; TODO library can be f(string) -> f(Struct) -> Struct
   ;;; what is best way to schema this, can we sync w/ tools
   [:library                {:optional true} [:map-of :string fn?]]
   [:history                {:optional false} (satisfies-schema IHistory)]
   [:rootChat               {:optional true} [:ref #'chat-session-schema]]
   [:*previousHistorySize   {:optional true} (instance-schema clojure.lang.Atom)]
   [:*responderState        {:optional false} [:and (instance-schema clojure.lang.Atom)
                                                    [:fn #(number? (:maxCalls (deref %)))]]]
   [:*currentResponseStream {:optional true} (instance-schema clojure.lang.Atom)]
   [:*currentResponse       {:optional true} (instance-schema clojure.lang.Atom)]
   [:generationConfig       {:optional true} GenerationConfig/schema]
   [:safetySettings         {:optional true} [:sequential SafetySetting/schema]]
   [:systemInstruction      {:optional true} Content/schema]
   [:toolConfig             {:optional true} ToolConfig/schema]
   [:tools                  {:optional true} [:sequential Tool/schema]]])

;; TODO 'model-cfg' as synthetic type?
;; TODO send-msg-async
;; TODO pass in history? :history == content, :chat/IHistory -> IHistory ??
;; readline-chat
;; IChat protocol... undo, redo, fork, map/reduce/walk

(defn chat-session
  [{:keys [startingHistory
           *currentResponse
           *currentResponseStream
           *previousHistorySize
           *responderState] :as arg}]
  {:post [(gcp.global/strict! chat-session-schema %)]}
  (cond-> (assoc arg :history (if (nil? startingHistory)
                                (default-history)
                                (if (satisfies? IHistory arg)
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
   (gcp.global/strict! chat-session-schema chat-session)
   (check-last-response-and-edit-history chat-session)
   (history-add (:history chat-session) contentable)
   (try
     (let [{:as response} (generate-content chat-session (history-to-contentable (:history chat-session)))]
       (reset! (:*currentResponse chat-session) response)
       (auto-respond chat-session response))
     (catch Exception e
       (check-last-response-and-edit-history chat-session)
       (history-revert (:history chat-session) (deref (:*previousHistorySize chat-session)))
       (throw e))))
  ([chat contentable & more]
   (send-msg chat (reduce (fn [acc c] (if (sequential? c) (into acc c) (conj acc c)))
                          (cond-> contentable (not (sequential? contentable)) vector)
                          more))))

(defn send-msg-stream
  "Sends a message to the model and returns a response."
  [chat contentable]
  (gcp.global/strict! chat-session-schema chat)
  (check-last-response-and-edit-history chat)
  (history-add (:history chat) contentable)
  (try
    (let [stream (generate-content-seq (history-to-contentable (:history chat)))]
      (reset! (:*currentResponseStream chat) stream)
      (auto-respond chat (aggregate-response-stream stream)))
    (catch Exception e
      (check-last-response-and-edit-history chat)
      (history-revert (:history chat) (deref (:*previousHistorySize chat)))
      (throw e))))
