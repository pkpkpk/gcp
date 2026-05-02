(ns gcp.pubsub.SubscriptionAdmin
  (:require
   [clojure.string :as string]
   [gcp.global :as g]
   [gcp.global.util :as gu]
   [gcp.pubsub.SubscriptionAdminSettings :as SubscriptionAdminSettings]
   [gcp.pubsub.v1.DeleteSubscriptionRequest :as DeleteSubscriptionRequest]
   [gcp.pubsub.v1.GetSubscriptionRequest :as GetSubscriptionRequest]
   [gcp.pubsub.v1.ListSubscriptionsRequest :as ListSubscriptionsRequest]
   [gcp.pubsub.v1.UpdateSubscriptionRequest :as UpdateSubscriptionRequest]
   [gcp.pubsub.v1.ProjectName :as ProjectName]
   [gcp.pubsub.v1.PushConfig :as PushConfig]
   [gcp.pubsub.v1.Subscription :as Subscription]
   [gcp.pubsub.v1.SubscriptionName :as SubscriptionName]
   [gcp.pubsub.v1.TopicName :as TopicName]
   [malli.core :as m])
  (:import
    (com.google.api.gax.rpc NotFoundException)
    (com.google.cloud.pubsub.v1 SubscriptionAdminClient)))

(defonce ^:dynamic *client* nil)

(def *clients (atom {}))

(defn client
  ([]
   (client nil))
  ([arg]
   (or *client*
       (if (instance? SubscriptionAdminClient arg)
         arg
         (or (get @*clients arg)
             (let [client (SubscriptionAdminClient/create (SubscriptionAdminSettings/from-edn arg))]
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
     (g/instance-schema com.google.cloud.pubsub.v1.SubscriptionAdminClient)
     :gcp.pubsub/SubscriptionAdminSettings]]
     
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
            (= "subscriptions" (nth parts 2))
            (some? (nth parts 3)))))]]

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

   ::SubscriptionList
   [:map {:closed true :doc "call record for SubscriptionAdminClient.listSubscriptions"}
    [:subscriptionAdmin {:optional true} [:ref ::clientable]]
    [:request :gcp.pubsub.v1/ListSubscriptionsRequest]]

   ::GetSubscriptionRequest (gu/assoc :gcp.pubsub.v1/GetSubscriptionRequest :subscription [:ref ::SubscriptionResourcePathString])

   ::SubscriptionGet
   [:map {:closed true :doc "call record for SubscriptionAdminClient.getSubscription"}
    [:subscriptionAdmin {:optional true} [:ref ::clientable]]
    [:request [:ref ::GetSubscriptionRequest]]]

   ::CreateSubscriptionRequest
   (-> :gcp.pubsub.v1/Subscription
       (gu/assoc :name [:or [:string {:min 1, :gen/max 1}] [:ref ::SubscriptionResourcePathString] :gcp.pubsub.v1/SubscriptionName])
       (gu/assoc :topic [:or [:string {:min 1, :gen/max 1}] [:ref ::TopicResourcePathString] :gcp.pubsub.v1/TopicName]))

   ::SubscriptionCreate
   [:map {:closed true :doc "call record for SubscriptionAdminClient.createSubscription"}
    [:subscriptionAdmin {:optional true} [:ref ::clientable]]
    [:request [:ref ::CreateSubscriptionRequest]]]

   ::DeleteSubscriptionRequest (gu/assoc :gcp.pubsub.v1/DeleteSubscriptionRequest :subscription [:ref ::SubscriptionResourcePathString])

   ::SubscriptionDelete
   [:map {:closed true :doc "call record for SubscriptionAdminClient.deleteSubscription"}
    [:subscriptionAdmin {:optional true} [:ref ::clientable]]
    [:request [:ref ::DeleteSubscriptionRequest]]]
   })

(g/include-schema-registry! (with-meta registry {::g/name "gcp.pubsub.SubscriptionAdmin"}))

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

(def ^:private list-subscriptions-args-schema
  [:altn
   [:arity-1-CR [:altn [:callRecord [:catn [:callRecord ::SubscriptionList]]]]]
   [:arity-1 [:altn
              [:request     [:catn [:request :gcp.pubsub.v1/ListSubscriptionsRequest]]]
              [:projectName [:catn [:projectName :gcp.pubsub.v1/ProjectName]]]
              [:project     [:catn [:project string?]]]]]
   [:arity-2 [:altn
              [:client-request     [:catn [:clientable ::clientable] [:request :gcp.pubsub.v1/ListSubscriptionsRequest]]]
              [:client-projectName [:catn [:clientable ::clientable] [:projectName :gcp.pubsub.v1/ProjectName]]]
              [:client-project     [:catn [:clientable ::clientable] [:project string?]]]]]])

(defn conform-project-resource-string [project]
  (if (g/valid? ::ProjectResourcePathString project)
    project
    (str "projects/" project)))

(defn conform-subscription-resource-string [subscription]
  (if (g/valid? ::SubscriptionResourcePathString subscription)
    subscription
    (let [[project subscription-id] (clojure.string/split subscription #"\.")]
      (if subscription-id
        (str "projects/" project "/subscriptions/" subscription-id)
        (throw (ex-info "Invalid subscription string. Must be 'projects/{project}/subscriptions/{subscription}' or '{project}.{subscription}'" {:subscription subscription}))))))

(defn conform-topic-resource-string [topic]
  (if (g/valid? ::TopicResourcePathString topic)
    topic
    (let [[project topic-id] (clojure.string/split topic #"\.")]
      (if topic-id
        (str "projects/" project "/topics/" topic-id)
        (throw (ex-info "Invalid topic string. Must be 'projects/{project}/topics/{topic}' or '{project}.{topic}'" {:topic topic}))))))

(defn parse-list-subscriptions-args [args]
  (let [schema (g/schema list-subscriptions-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to list-subscriptions" {:args args :explain (g/explain schema args)}))
      (extract-parse-values parsed))))

(defn ->SubscriptionList [args]
  (let [{:keys [clientable project projectName request callRecord]} (parse-list-subscriptions-args args)]
    (if callRecord
      (assoc callRecord :op ::SubscriptionList)
      (let [request (if request
                      (update-in request [:project] conform-project-resource-string)
                      (if projectName
                        {:project (str (ProjectName/from-edn projectName))}
                        {:project (conform-project-resource-string project)}))]
        {:op ::SubscriptionList
         :subscriptionAdmin clientable
         :request request}))))

(defmethod execute! ::SubscriptionList [{:keys [subscriptionAdmin request]}]
  (let [client (client subscriptionAdmin)
        response (.listSubscriptions client (ListSubscriptionsRequest/from-edn request))]
    (map Subscription/to-edn (seq (.iterateAll response)))))

#!----------------------------------------------------------------------------------------------------------------------
#! SubscriptionGet

(def ^:private get-subscription-args-schema
  [:altn
   [:arity-1-CR [:altn [:callRecord [:catn [:callRecord ::SubscriptionGet]]]]]
   [:arity-1 [:altn
              [:request              [:catn [:request ::GetSubscriptionRequest]]]
              [:subscriptionName     [:catn [:subscriptionName :gcp.pubsub.v1/SubscriptionName]]]
              [:subscriptionResource [:catn [:subscriptionResource ::SubscriptionResourcePathString]]]]]
   [:arity-2 [:altn
              [:client-request              [:catn [:clientable ::clientable] [:request ::GetSubscriptionRequest]]]
              [:client-subscriptionName     [:catn [:clientable ::clientable] [:subscriptionName :gcp.pubsub.v1/SubscriptionName]]]
              [:client-subscriptionResource [:catn [:clientable ::clientable] [:subscriptionResource ::SubscriptionResourcePathString]]]
              [:project-subscription        [:catn [:project string?] [:subscription string?]]]]]
   [:arity-3 [:altn
              [:client-project-subscription [:catn [:clientable ::clientable] [:project string?] [:subscription string?]]]]]])

(defn parse-get-subscription-args [args]
  (let [schema (g/schema get-subscription-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to get-subscription" {:args args :explain (g/explain schema args)}))
      (extract-parse-values parsed))))

(defn ->SubscriptionGet [args]
  (let [{:keys [clientable subscriptionResource subscriptionName project subscription request callRecord]} (parse-get-subscription-args args)]
    (if callRecord
      (assoc callRecord :op ::SubscriptionGet)
      (let [request (or request
                        (if subscriptionName
                          {:subscription (str (SubscriptionName/from-edn subscriptionName))}
                          (if subscriptionResource
                            {:subscription (conform-subscription-resource-string subscriptionResource)}
                            {:subscription (str "projects/" project "/subscriptions/" subscription)})))]
        {:op ::SubscriptionGet
         :subscriptionAdmin clientable
         :request request}))))

(defmethod execute! ::SubscriptionGet [{:keys [subscriptionAdmin request]}]
  (let [client (client subscriptionAdmin)]
    (try
      (Subscription/to-edn (.getSubscription client (GetSubscriptionRequest/from-edn request)))
      (catch NotFoundException _
        nil))))

#!----------------------------------------------------------------------------------------------------------------------
#! SubscriptionCreate

(def ^:private create-subscription-args-schema
  [:altn
   [:arity-1-CR [:altn [:callRecord [:catn [:callRecord ::SubscriptionCreate]]]]]
   [:arity-1 [:altn
              [:request              [:catn [:request ::CreateSubscriptionRequest]]]
              [:subscriptionName     [:catn [:subscriptionName :gcp.pubsub.v1/SubscriptionName]]]
              [:subscriptionResource [:catn [:subscriptionResource ::SubscriptionResourcePathString]]]]]
   [:arity-2 [:altn
              [:client-request              [:catn [:clientable ::clientable] [:request ::CreateSubscriptionRequest]]]
              [:client-subscriptionName     [:catn [:clientable ::clientable] [:subscriptionName :gcp.pubsub.v1/SubscriptionName]]]
              [:client-subscriptionResource [:catn [:clientable ::clientable] [:subscriptionResource ::SubscriptionResourcePathString]]]
              [:project-subscription        [:catn [:project string?] [:subscription string?]]]
              [:subscription-topic          [:catn [:subscription string?] [:topic string?]]]]]
   [:arity-3 [:altn
              [:client-project-subscription [:catn [:clientable ::clientable] [:project string?] [:subscription string?]]]
              [:client-subscription-topic   [:catn [:clientable ::clientable] [:subscription string?] [:topic string?]]]
              [:project-subscription-topic  [:catn [:project string?] [:subscription string?] [:topic string?]]]]]
   [:arity-4 [:altn
              [:client-project-subscription-topic [:catn [:clientable ::clientable] [:project string?] [:subscription string?] [:topic string?]]]
              [:project-subscription-topic-push   [:catn [:project string?] [:subscription string?] [:topic string?] [:pushConfig :gcp.pubsub.v1/PushConfig]]]]]
   [:arity-5 [:altn
              [:client-project-subscription-topic-push [:catn [:clientable ::clientable] [:project string?] [:subscription string?] [:topic string?] [:pushConfig :gcp.pubsub.v1/PushConfig]]]
              [:project-subscription-topic-push-ack   [:catn [:project string?] [:subscription string?] [:topic string?] [:pushConfig :gcp.pubsub.v1/PushConfig] [:ackDeadlineSeconds int?]]]]]
   [:arity-6 [:altn
              [:client-project-subscription-topic-push-ack [:catn [:clientable ::clientable] [:project string?] [:subscription string?] [:topic string?] [:pushConfig :gcp.pubsub.v1/PushConfig] [:ackDeadlineSeconds int?]]]]]])

(defn parse-create-subscription-args [args]
  (let [schema (g/schema create-subscription-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to create-subscription" {:args args :explain (g/explain schema args)}))
      (extract-parse-values parsed))))

(defn ->SubscriptionCreate [args]
  (let [{:keys [clientable subscriptionResource subscriptionName project subscription topic pushConfig ackDeadlineSeconds request callRecord]} (parse-create-subscription-args args)]
    (if callRecord
      (assoc callRecord :op ::SubscriptionCreate)
      (let [request (or request
                        (cond-> {}
                          subscriptionName (assoc :name (str (SubscriptionName/from-edn subscriptionName)))
                          subscriptionResource (assoc :name (conform-subscription-resource-string subscriptionResource))
                          (and project subscription) (assoc :name (str "projects/" project "/subscriptions/" subscription))
                          (and (not (or project subscriptionName subscriptionResource)) subscription) (assoc :name (conform-subscription-resource-string subscription))

                          topic (assoc :topic (if project
                                                (if (string/includes? topic "/topics/")
                                                  topic
                                                  (if (string/includes? topic ".")
                                                    (conform-topic-resource-string topic)
                                                    (str "projects/" project "/topics/" topic)))
                                                (conform-topic-resource-string topic)))
                          (some? pushConfig) (assoc :pushConfig pushConfig)
                          (some? ackDeadlineSeconds) (assoc :ackDeadlineSeconds ackDeadlineSeconds)))
            request (cond-> request
                      (map? (:name request)) (assoc :name (str (SubscriptionName/from-edn (:name request))))
                      (map? (:topic request)) (assoc :topic (str (TopicName/from-edn (:topic request)))))]
        {:op ::SubscriptionCreate
         :subscriptionAdmin clientable
         :request request}))))

(defmethod execute! ::SubscriptionCreate [{:keys [subscriptionAdmin request]}]
  (let [client (client subscriptionAdmin)
        response (.createSubscription client (Subscription/from-edn request))]
    (Subscription/to-edn response)))

#!----------------------------------------------------------------------------------------------------------------------
#! SubscriptionDelete

(def ^:private delete-subscription-args-schema
  [:altn
   [:arity-1-CR [:altn [:callRecord [:catn [:callRecord ::SubscriptionDelete]]]]]
   [:arity-1 [:altn
              [:request              [:catn [:request ::DeleteSubscriptionRequest]]]
              [:subscriptionName     [:catn [:subscriptionName :gcp.pubsub.v1/SubscriptionName]]]
              [:subscriptionResource [:catn [:subscriptionResource ::SubscriptionResourcePathString]]]]]
   [:arity-2 [:altn
              [:client-request              [:catn [:clientable ::clientable] [:request ::DeleteSubscriptionRequest]]]
              [:client-subscriptionName     [:catn [:clientable ::clientable] [:subscriptionName :gcp.pubsub.v1/SubscriptionName]]]
              [:client-subscriptionResource [:catn [:clientable ::clientable] [:subscriptionResource ::SubscriptionResourcePathString]]]
              [:project-subscription        [:catn [:project string?] [:subscription string?]]]]]
   [:arity-3 [:altn
              [:client-project-subscription [:catn [:clientable ::clientable] [:project string?] [:subscription string?]]]]]])

(defn parse-delete-subscription-args [args]
  (let [schema (g/schema delete-subscription-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to delete-subscription" {:args args :explain (g/explain schema args)}))
      (extract-parse-values parsed))))

(defn ->SubscriptionDelete [args]
  (let [{:keys [clientable subscriptionResource subscriptionName project subscription request callRecord]} (parse-delete-subscription-args args)]
    (if callRecord
      (assoc callRecord :op ::SubscriptionDelete)
      (let [request (or request
                        (if subscriptionName
                          {:subscription (str (SubscriptionName/from-edn subscriptionName))}
                          (if subscriptionResource
                            {:subscription (conform-subscription-resource-string subscriptionResource)}
                            {:subscription (str "projects/" project "/subscriptions/" subscription)})))]
        {:op ::SubscriptionDelete
         :subscriptionAdmin clientable
         :request request}))))

(defmethod execute! ::SubscriptionDelete [{:keys [subscriptionAdmin request]}]
  (let [client (client subscriptionAdmin)]
    (.deleteSubscription client (DeleteSubscriptionRequest/from-edn request))))

(comment
  [{:name "deleteSubscription",
    :returnType void,
    :parameters
    [{:name "subscription",
      :type com.google.pubsub.v1.SubscriptionName}],
    :doc
    "Deletes an existing subscription. All messages retained in the subscription are immediately\ndropped. Calls to `Pull` after deletion will return `NOT_FOUND`. After a subscription is\ndeleted, a new one may be created with the same name, but the new one has no association with\nthe old subscription or its topic unless the same topic is specified.\n\n<p>Sample code:\n\n<pre>{@code\n// This snippet has been automatically generated and should be regarded as a code template only.\n// It will require modifications to work:\n// - It may require correct/in-range values for request initialization.\n// - It may require specifying regional endpoints when creating the service client as shown in\n// https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library\ntry (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {\n  SubscriptionName subscription = SubscriptionName.of(\"[PROJECT]\", \"[SUBSCRIPTION]\");\n  subscriptionAdminClient.deleteSubscription(subscription);\n}\n}</pre>\n\n@param subscription Required. The subscription to delete. Format is\n    `projects/{project}/subscriptions/{sub}`.\n@throws com.google.api.gax.rpc.ApiException if the remote call fails"}
   {:name "deleteSubscription",
    :returnType void,
    :parameters [{:name "subscription", :type java.lang.String}],
    :doc
    "Deletes an existing subscription. All messages retained in the subscription are immediately\ndropped. Calls to `Pull` after deletion will return `NOT_FOUND`. After a subscription is\ndeleted, a new one may be created with the same name, but the new one has no association with\nthe old subscription or its topic unless the same topic is specified.\n\n<p>Sample code:\n\n<pre>{@code\n// This snippet has been automatically generated and should be regarded as a code template only.\n// It will require modifications to work:\n// - It may require correct/in-range values for request initialization.\n// - It may require specifying regional endpoints when creating the service client as shown in\n// https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library\ntry (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {\n  String subscription = SubscriptionName.of(\"[PROJECT]\", \"[SUBSCRIPTION]\").toString();\n  subscriptionAdminClient.deleteSubscription(subscription);\n}\n}</pre>\n\n@param subscription Required. The subscription to delete. Format is\n    `projects/{project}/subscriptions/{sub}`.\n@throws com.google.api.gax.rpc.ApiException if the remote call fails"}
   {:name "deleteSubscription",
    :returnType void,
    :parameters
    [{:name "request",
      :type com.google.pubsub.v1.DeleteSubscriptionRequest}],
    :doc
    "Deletes an existing subscription. All messages retained in the subscription are immediately\ndropped. Calls to `Pull` after deletion will return `NOT_FOUND`. After a subscription is\ndeleted, a new one may be created with the same name, but the new one has no association with\nthe old subscription or its topic unless the same topic is specified.\n\n<p>Sample code:\n\n<pre>{@code\n// This snippet has been automatically generated and should be regarded as a code template only.\n// It will require modifications to work:\n// - It may require correct/in-range values for request initialization.\n// - It may require specifying regional endpoints when creating the service client as shown in\n// https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library\ntry (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {\n  DeleteSubscriptionRequest request =\n      DeleteSubscriptionRequest.newBuilder()\n          .setSubscription(SubscriptionName.of(\"[PROJECT]\", \"[SUBSCRIPTION]\").toString())\n          .build();\n  subscriptionAdminClient.deleteSubscription(request);\n}\n}</pre>\n\n@param request The request object containing all of the parameters for the API call.\n@throws com.google.api.gax.rpc.ApiException if the remote call fails"}],

  )