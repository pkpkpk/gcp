(ns gcp.pubsub.TopicAdmin
  (:require
    [clojure.string :as string]
    [gcp.global :as g]
    [gcp.global.util :as gu]
    [gcp.pubsub.TopicAdminSettings :as TopicAdminSettings]
    [gcp.pubsub.v1.DeleteTopicRequest :as DeleteTopicRequest]
    [gcp.pubsub.v1.GetTopicRequest :as GetTopicRequest]
    [gcp.pubsub.v1.ListTopicSubscriptionsRequest :as ListTopicSubscriptionsRequest]
    [gcp.pubsub.v1.ListTopicsRequest :as ListTopicsRequest]
    [gcp.pubsub.v1.ProjectName :as ProjectName]
    [gcp.pubsub.v1.Subscription :as Subscription]
    [gcp.pubsub.v1.Topic :as Topic]
    [gcp.pubsub.v1.TopicName :as TopicName]
    [malli.core :as m])
  (:import
    (com.google.cloud.pubsub.v1 TopicAdminClient)))

(defonce ^:dynamic *client* nil)

(defonce *clients (atom {}))

(defn client
  ([]
   (client nil))
  ([arg]
   (or *client*
       (if (instance? TopicAdminClient arg)
         arg
         (or (get @*clients arg)
             (let [client (TopicAdminClient/create (TopicAdminSettings/from-edn arg))]
               (swap! *clients assoc arg client)
               client))))))

(defn emulator-client
  "nil if the PUBSUB_EMULATOR_HOST environment variable is not set."
  []
  (when (System/getenv "PUBSUB_EMULATOR_HOST")
    (client)))

(defn emulator-client!
  "Throws if PUBSUB_EMULATOR_HOST is not set"
  []
  (or (emulator-client)
      (throw (Exception. "PUBSUB_EMULATOR_HOST is not set."))))

(def registry
  {::clientable
   [:maybe
    [:or
     (g/instance-schema com.google.cloud.pubsub.v1.TopicAdminClient)
     :gcp.pubsub/TopicAdminSettings]]

   ::ProjectResourcePathString
   [:and
    :string
    [:fn
     {:error/message "string representations of project must be in form 'projects/{project}'"}
     '(fn [s]
        (let [parts (clojure.string/split s (re-pattern "/"))]
          (and
            (= "projects" (nth parts 0))
            (some? (nth parts 1)))))]]

   ::TopicResourcePathString
   [:and
    :string
    [:fn
     {:error/message "string representations of topics must be in form 'projects/{project}/topics/{topic}'"}
     '(fn [s]
        (let [parts (clojure.string/split s (re-pattern "/"))]
          (and
            (= "projects" (nth parts 0))
            (some? (nth parts 1))
            (= "topics" (nth parts 2))
            (some? (nth parts 3)))))]]

   ::SubscriptionResourcePathString
   [:and
    :string
    [:fn
     {:error/message "string representations of subscriptions must be in form 'projects/{project}/subscriptions/{subscription}'"}
     '(fn [s]
        (let [parts (clojure.string/split s (re-pattern "/"))]
          (and
            (= "projects" (nth parts 0))
            (some? (nth parts 1))
            (= "subscription" (nth parts 2))
            (some? (nth parts 3)))))]]

   ::TableResourcePathString
   [:and
    :string
    [:fn
     {:error/message "string representations of tables must be in form '{project}.{dataset}.{table}'"}
     '(fn [s] (= 3 (count (clojure.string/split s (re-pattern "\\.")))))]]

   ::TopicList
   [:map {:closed true :doc "call record for TopicAdminClient.listTopics"}
    [:topicAdmin {:optional true} [:ref ::clientable]]
    [:request :gcp.pubsub.v1/ListTopicsRequest]]

   ::GetTopicRequest (gu/assoc :gcp.pubsub.v1/GetTopicRequest :topic [:ref ::TopicResourcePathString])

   ::TopicGet
   [:map {:closed true :doc "call record for TopicAdminClient.getTopic"}
    [:topicAdmin {:optional true} ::clientable]
    [:request [:ref ::GetTopicRequest]]]

   ::ListTopicSubscriptionsRequest (gu/assoc :gcp.pubsub.v1/ListTopicSubscriptionsRequest :topic [:ref ::TopicResourcePathString])

   ::TopicListSubscriptions
   [:map {:closed true :doc "call record for TopicAdminClient.listTopicSubscriptions"}
    [:topicAdmin {:optional true} [:ref ::clientable]]
    [:request [:ref ::ListTopicSubscriptionsRequest]]]

   ::TopicCreate
   [:map {:closed true :doc "call record for TopicAdminClient.createTopic"}
    [:topicAdmin {:optional true} [:ref ::clientable]]
    [:request :gcp.pubsub.v1/Topic]]

   ::DeleteTopicRequest (gu/assoc :gcp.pubsub.v1/DeleteTopicRequest :topic [:ref ::TopicResourcePathString])

   ::TopicDelete
   [:map {:closed true :doc "call record for TopicAdminClient.deleteTopic"}
    [:topicAdmin {:optional true} [:ref ::clientable]]
    [:request [:ref ::DeleteTopicRequest]]]

   })

(g/include-schema-registry! (with-meta registry {::g/name "gcp.pubsub.TopicAdmin"}))

#!----------------------------------------------------------------------------------------------------------------------

(defn- extract-parse-values [parsed]
  (cond
    (instance? malli.core.Tag parsed)
    (extract-parse-values (:value parsed))

    (instance? malli.core.Tags parsed)
    (:values parsed)

    (map? parsed)
    parsed

    :else parsed))

(defmulti execute! :op)

#!----------------------------------------------------------------------------------------------------------------------
#! ::TopicList

(def ^:private list-topics-args-schema
  [:altn
   [:arity-1-CR [:altn [:callRecord [:catn [:callRecord ::TopicList]]]]]
   [:arity-1 [:altn
              [:request     [:catn [:request :gcp.pubsub.v1/ListTopicsRequest]]]
              [:projectName [:catn [:projectName :gcp.pubsub.v1/ProjectName]]]
              [:project     [:catn [:project string?]]]]]
   [:arity-2 [:altn
              [:client-request     [:catn [:clientable ::clientable] [:request :gcp.pubsub.v1/ListTopicsRequest]]]
              [:client-projectName [:catn [:clientable ::clientable] [:projectName :gcp.pubsub.v1/ProjectName]]]
              [:client-project     [:catn [:clientable ::clientable] [:project string?]]]]]])

(defn conform-project-resource-string [project]
  (if (g/valid? ::ProjectResourcePathString project)
    project
    (str "projects/" project)))

(defn conform-topic-resource-string [topic]
  (if (g/valid? ::TopicResourcePathString topic)
    topic
    (let [[project topic-id] (clojure.string/split topic #"\.")]
      (if topic-id
        (str "projects/" project "/topics/" topic-id)
        (throw (ex-info "Invalid topic string. Must be 'projects/{project}/topics/{topic}' or '{project}.{topic}'" {:topic topic}))))))

(defn parse-list-topics-args [args]
  (let [schema (g/schema list-topics-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to list-topics" {:args args :explain (g/explain schema args)}))
      (extract-parse-values parsed))))

(defn ->TopicList [args]
  (let [{:keys [clientable project projectName request callRecord]} (parse-list-topics-args args)]
    (if callRecord
      (assoc callRecord :op ::TopicList)
      (let [request (if request
                      (update-in request [:project] conform-project-resource-string)
                      (if projectName
                        {:project (str (ProjectName/from-edn projectName))}
                        {:project (conform-project-resource-string project)}))]
        {:op ::TopicList
         :topicAdmin clientable
         :request request}))))

(defmethod execute! ::TopicList [{:keys [topicAdmin request]}]
  (let [client (client topicAdmin)
        response (.listTopics client (ListTopicsRequest/from-edn request))]
    (map Topic/to-edn (seq (.iterateAll response)))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::TopicGet

(def ^:private get-topic-args-schema
  [:altn
   [:arity-1-CR [:altn [:callRecord [:catn [:callRecord ::TopicGet]]]]]
   [:arity-1 [:altn
              [:request       [:catn [:request ::GetTopicRequest]]]
              [:topicName     [:catn [:topicName :gcp.pubsub.v1/TopicName]]]
              [:topicResource [:catn [:topicResource ::TopicResourcePathString]]]]]
   [:arity-2 [:altn
              [:client-request       [:catn [:clientable ::clientable] [:request ::GetTopicRequest]]]
              [:client-topicName     [:catn [:clientable ::clientable] [:topicName :gcp.pubsub.v1/TopicName]]]
              [:client-topicResource [:catn [:clientable ::clientable] [:topicResource ::TopicResourcePathString]]]
              [:project-topic        [:catn [:project string?] [:topic string?]]]]]
   [:arity-3 [:altn
              [:client-project-topic [:catn [:clientable ::clientable] [:project string?] [:topic string?]]]]]])

(defn parse-get-topic-args [args]
  (let [schema (g/schema get-topic-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to get-topic" {:args args :explain (g/explain schema args)}))
      (extract-parse-values parsed))))

(defn ->TopicGet [args]
  (let [{:keys [clientable topicResource topicName project topic request callRecord]} (parse-get-topic-args args)]
    (if callRecord
      (assoc callRecord :op ::TopicGet)
      (let [request (or request
                      (if topicName
                        {:topic (str (TopicName/from-edn topicName))}
                        (if topicResource
                          {:topic (conform-topic-resource-string topicResource)}
                          {:topic (str "projects/" project "/topics/" topic)})))]
        {:op ::TopicGet
         :topicAdmin clientable
         :request request}))))

(defmethod execute! ::TopicGet [{:keys [topicAdmin request]}]
  (let [client (client topicAdmin)
        response (.getTopic client (GetTopicRequest/from-edn request))]
    (Topic/to-edn response)))

#!----------------------------------------------------------------------------------------------------------------------
#! ::TopicListSubscription

(def ^:private list-topic-subscriptions-args-schema
  [:altn
   [:arity-1-CR [:altn [:callRecord [:catn [:callRecord ::TopicListSubscriptions]]]]]
   [:arity-1 [:altn
              [:request       [:catn [:request ::ListTopicSubscriptionsRequest]]]
              [:topicName     [:catn [:topicName :gcp.pubsub.v1/TopicName]]]
              [:topicResource [:catn [:topicResource ::TopicResourcePathString]]]]]
   [:arity-2 [:altn
              [:client-request       [:catn [:clientable ::clientable] [:request ::ListTopicSubscriptionsRequest]]]
              [:client-topicName     [:catn [:clientable ::clientable] [:topicName :gcp.pubsub.v1/TopicName]]]
              [:client-topicResource [:catn [:clientable ::clientable] [:topicResource ::TopicResourcePathString]]]
              [:project-topic        [:catn [:project string?] [:topic string?]]]]]
   [:arity-3 [:altn
              [:client-project-topic [:catn [:clientable ::clientable] [:project string?] [:topic string?]]]]]])

(defn parse-list-topic-subscriptions-args [args]
  (let [schema (g/schema list-topic-subscriptions-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to list-topic-subscriptions" {:args args :explain (g/explain schema args)}))
      (extract-parse-values parsed))))

(defn ->TopicListSubscriptions [args]
  (let [{:keys [clientable topicResource topicName project topic request callRecord]} (parse-list-topic-subscriptions-args args)]
    (if callRecord
      (assoc callRecord :op ::TopicListSubscriptions)
      (let [request (or request
                        (if topicName
                          {:topic (str (TopicName/from-edn topicName))}
                          (if topicResource
                            {:topic (conform-topic-resource-string topicResource)}
                            {:topic (str "projects/" project "/topics/" topic)})))]
        {:op ::TopicListSubscriptions
         :topicAdmin clientable
         :request request}))))

(defmethod execute! ::TopicListSubscriptions [{:keys [topicAdmin request]}]
  (let [client (client topicAdmin)
        response (.listTopicSubscriptions client (ListTopicSubscriptionsRequest/from-edn request))]
    (map Subscription/to-edn (seq (.iterateAll response)))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::TopicCreate

(def ^:private create-topic-args-schema
  [:altn
   [:arity-1-CR [:altn [:callRecord [:catn [:callRecord ::TopicCreate]]]]]
   [:arity-1 [:altn
              [:request       [:catn [:request :gcp.pubsub.v1/Topic]]]
              [:topicName     [:catn [:topicName :gcp.pubsub.v1/TopicName]]]
              [:topicResource [:catn [:topicResource ::TopicResourcePathString]]]]]
   [:arity-2 [:altn
              [:client-request       [:catn [:clientable ::clientable] [:request :gcp.pubsub.v1/Topic]]]
              [:client-topicName     [:catn [:clientable ::clientable] [:topicName :gcp.pubsub.v1/TopicName]]]
              [:client-topicResource [:catn [:clientable ::clientable] [:topicResource ::TopicResourcePathString]]]
              [:project-topic        [:catn [:project string?] [:topic string?]]]]]
   [:arity-3 [:altn
              [:client-project-topic [:catn [:clientable ::clientable] [:project string?] [:topic string?]]]]]])

(defn parse-create-topic-args [args]
  (let [schema (g/schema create-topic-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to create-topic" {:args args :explain (g/explain schema args)}))
      (extract-parse-values parsed))))

(defn ->TopicCreate [args]
  (let [{:keys [clientable topicResource topicName project topic request callRecord]} (parse-create-topic-args args)]
    (if callRecord
      (assoc callRecord :op ::TopicCreate)
      (let [request (or request
                        (if topicName
                          {:name (str (TopicName/from-edn topicName))}
                          (if topicResource
                            {:name (conform-topic-resource-string topicResource)}
                            {:name (str "projects/" project "/topics/" topic)})))]
        {:op ::TopicCreate
         :topicAdmin clientable
         :request request}))))

(defmethod execute! ::TopicCreate [{:keys [topicAdmin request]}]
  (let [client (client topicAdmin)
        response (.createTopic client (Topic/from-edn request))]
    (Topic/to-edn response)))

#!----------------------------------------------------------------------------------------------------------------------
#! ::TopicDelete

(def ^:private delete-topic-args-schema
  [:altn
   [:arity-1-CR [:altn [:callRecord [:catn [:callRecord ::TopicDelete]]]]]
   [:arity-1 [:altn
              [:request       [:catn [:request ::DeleteTopicRequest]]]
              [:topicName     [:catn [:topicName :gcp.pubsub.v1/TopicName]]]
              [:topicResource [:catn [:topicResource ::TopicResourcePathString]]]]]
   [:arity-2 [:altn
              [:client-request       [:catn [:clientable ::clientable] [:request ::DeleteTopicRequest]]]
              [:client-topicName     [:catn [:clientable ::clientable] [:topicName :gcp.pubsub.v1/TopicName]]]
              [:client-topicResource [:catn [:clientable ::clientable] [:topicResource ::TopicResourcePathString]]]
              [:project-topic        [:catn [:project string?] [:topic string?]]]]]
   [:arity-3 [:altn
              [:client-project-topic [:catn [:clientable ::clientable] [:project string?] [:topic string?]]]]]])

(defn parse-delete-topic-args [args]
  (let [schema (g/schema delete-topic-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to delete-topic" {:args args :explain (g/explain schema args)}))
      (extract-parse-values parsed))))

(defn ->TopicDelete [args]
  (let [{:keys [clientable topicResource topicName project topic request callRecord]} (parse-delete-topic-args args)]
    (if callRecord
      (assoc callRecord :op ::TopicDelete)
      (let [request (or request
                        (if topicName
                          {:topic (str (TopicName/from-edn topicName))}
                          (if topicResource
                            {:topic (conform-topic-resource-string topicResource)}
                            {:topic (str "projects/" project "/topics/" topic)})))]
        {:op ::TopicDelete
         :topicAdmin clientable
         :request request}))))

(defmethod execute! ::TopicDelete [{:keys [topicAdmin request]}]
  (let [client (client topicAdmin)]
    (.deleteTopic client (DeleteTopicRequest/from-edn request))))

