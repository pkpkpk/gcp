(ns gcp.foreign.com.google.cloud
  (:require [gcp.global :as g]
            [gcp.foreign.com.google.api :as api]
            [gcp.foreign.com.google.protobuf :as protobuf])
  (:import
   (com.google.cloud MonitoredResource MonitoredResourceDescriptor
                     RetryOption RetryOption$OptionType
                     Binding Condition Policy)
   (java.time Duration)))

(defn- edn->java-duration [arg]
  (if (number? arg)
    (Duration/ofSeconds arg)
    (let [{:keys [seconds nanos] :or {seconds 0 nanos 0}} arg]
      (Duration/ofSeconds seconds nanos))))

(defn- java-duration->edn [^Duration d]
  {:seconds (.getSeconds d)
   :nanos (.getNano d)})

;; MonitoredResource
(defn ^MonitoredResource MonitoredResource-from-edn [arg]
  (MonitoredResource/fromPb (api/MonitoredResource-from-edn arg)))

(defn MonitoredResource-to-edn [^MonitoredResource arg]
  (api/MonitoredResource-to-edn (.toPb arg)))

;; MonitoredResourceDescriptor
(defn ^MonitoredResourceDescriptor MonitoredResourceDescriptor-from-edn [arg]
  (MonitoredResourceDescriptor/fromPb (api/MonitoredResourceDescriptor-from-edn arg)))

(defn MonitoredResourceDescriptor-to-edn [^MonitoredResourceDescriptor arg]
  (api/MonitoredResourceDescriptor-to-edn (.toPb arg)))

;; RetryOption
(defn RetryOption-from-edn [arg]
  (cond
    (:totalTimeout arg) (RetryOption/totalTimeoutDuration (edn->java-duration (:totalTimeout arg)))
    (:initialRetryDelay arg) (RetryOption/initialRetryDelayDuration (edn->java-duration (:initialRetryDelay arg)))
    (:retryDelayMultiplier arg) (RetryOption/retryDelayMultiplier (double (:retryDelayMultiplier arg)))
    (:maxRetryDelay arg) (RetryOption/maxRetryDelayDuration (edn->java-duration (:maxRetryDelay arg)))
    (:maxAttempts arg) (RetryOption/maxAttempts (int (:maxAttempts arg)))
    (contains? arg :jittered) (RetryOption/jittered (boolean (:jittered arg)))
    :else (throw (ex-info "Unknown RetryOption" {:arg arg}))))

(defn RetryOption-to-edn [^RetryOption arg]
  ;; Use reflection to get type and value
  (let [f-type (.getDeclaredField RetryOption "type")
        f-value (.getDeclaredField RetryOption "value")]
    (.setAccessible f-type true)
    (.setAccessible f-value true)
    (let [type (.get f-type arg)
          value (.get f-value arg)]
      (case (str type)
        "TOTAL_TIMEOUT" {:totalTimeout (java-duration->edn value)}
        "INITIAL_RETRY_DELAY" {:initialRetryDelay (java-duration->edn value)}
        "RETRY_DELAY_MULTIPLIER" {:retryDelayMultiplier value}
        "MAX_RETRY_DELAY" {:maxRetryDelay (java-duration->edn value)}
        "MAX_ATTEMPTS" {:maxAttempts value}
        "JITTERED" {:jittered value}
        (throw (ex-info "Unknown RetryOption type" {:type type}))))))

(def Condition-schema
  [:map
   {:closed true
    :doc "Class for Identity and Access Management (IAM) policies. IAM policies are used to specify access settings for Cloud Platform resources. A policy is a list of bindings. A binding assigns a set of identities to a role, where the identities can be user accounts, Google groups, Google domains, and service accounts. A role is a named list of permissions defined by IAM."
    :urls ["https://docs.cloud.google.com/java/docs/reference/google-cloud-core/latest/com.google.cloud.Condition"]}
   [:description [:string {:min 1}]]
   [:expression [:string {:min 1}]]
   [:title [:string {:min 1}]]])

(defn ^Condition Condition-from-edn [arg]
  (let [builder (Condition/newBuilder)]
    (some->>  (:description arg) (.setDescription builder))
    (some->> (:expression arg) (.setExpression builder))
    (some->> (:title arg) (.setTitle builder))
    (.build builder)))

(defn Condition-to-edn [^Condition arg]
  (cond-> {}
          (.getDescription arg) (assoc :description (.getDescription arg))
          (.getExpression arg) (assoc :expression (.getExpression arg))
          (.getTitle arg) (assoc :title (.getTitle arg))))

(def Binding-schema
  [:map
   {:closed true
    :doc "Class for Identity and Access Management (IAM) policies. IAM policies are used to specify access settings for Cloud Platform resources. A policy is a list of bindings. A binding assigns a set of identities to a role, where the identities can be user accounts, Google groups, Google domains, and service accounts. A role is a named list of permissions defined by IAM"
    :urls ["https://docs.cloud.google.com/java/docs/reference/google-cloud-core/latest/com.google.cloud.Binding"]}
   [:role [:string {:min 1}]]
   [:members [:sequential [:string {:min 1}]]]
   [:condition [:ref ::Condition]]])

(defn ^Binding Binding-from-edn [arg]
  (let [builder (Binding/newBuilder)]
    (some->> (:role arg) (.setRole builder))
    (some->> (:members arg) (.setMembers builder))
    (some->> (:condition arg) Condition-from-edn (.setCondition builder))
    (.build builder)))

(defn Binding-to-edn [^Binding arg]
  (cond-> {}
          (.getRole arg) (assoc :role (.getRole arg))
          (.getMembers arg) (assoc :members (.getMembers arg))
          (.getCondition arg) (assoc :condition (Condition-to-edn (.getCondition arg)))))

(def Policy-schema
  [:map {:closed true
         :doc "Class for Identity and Access Management (IAM) policies. IAM policies are used to specify access settings for Cloud Platform resources. A policy is a list of bindings. A binding assigns a set of identities to a role, where the identities can be user accounts, Google groups, Google domains, and service accounts. A role is a named list of permissions defined by IAM"
         :urls ["https://docs.cloud.google.com/java/docs/reference/google-cloud-core/latest/com.google.cloud.Policy"
                "https://docs.cloud.google.com/iam/docs/reference/rest/v1/Policy"]}
   [:version {:doc "Returns the version of the policy. The default version is 0, meaning only the \"owner\", \"editor\", and \"viewer\" roles are permitted. If the version is 1, you may also use other roles."}
    [:enum 0 1 3]]
   [:etag {:read-only? true
           :optional true
           :doc "Etags are used for optimistic concurrency control as a way to help prevent simultaneous updates of a policy from overwriting each other. It is strongly suggested that systems make use of the etag in the read-modify-write cycle to perform policy updates in order to avoid race conditions. An etag is returned in the response to getIamPolicy, and systems are expected to put that etag in the request to setIamPolicy to ensure that their change will be applied to the same version of the policy. If no etag is provided in the call to setIamPolicy, then the existing policy is overwritten blindly."}
    [:string {:min 1}]]
   [:bindings {:optional true} [:sequential [:ref ::Binding]]]])

(defn ^Policy Policy-from-edn [arg]
  (let [builder (Policy/newBuilder)]
    (some->> (:etag arg) (.setEtag builder))
    (some->> (:version arg) (.setVersion builder))
    (some->> (:bindings arg) (map Binding-from-edn) (.setBindings builder))
    (.build builder)))

(defn Policy-to-edn [^Policy arg]
  {:post [(g/strict! ::Policy %)]}
  (cond-> {}
          (.getEtag arg) (assoc :etag (.getEtag arg))
          (.getVersion arg) (assoc :version (.getVersion arg))
          (seq (.getBindings arg)) (assoc :bindings (map Binding-to-edn (.getBindings arg)))))

(g/include-schema-registry!
  (with-meta
    {::Condition    Condition-schema
    ::Binding      Binding-schema
    ::Policy       Policy-schema
    ::RetryOption [:or
                   [:map {:closed true} [:totalTimeout ::protobuf/Duration]]
                   [:map {:closed true} [:initialRetryDelay ::protobuf/Duration]]
                   [:map {:closed true} [:retryDelayMultiplier :double]]
                   [:map {:closed true} [:maxRetryDelay ::protobuf/Duration]]
                   [:map {:closed true} [:maxAttempts :int]]
                   [:map {:closed true} [:jittered :boolean]]]
    ::MonitoredResource [:map
                         [:type :string]
                         [:labels [:map-of :string :string]]]
    ::MonitoredResourceDescriptor [:map
                                   [:type :string]
                                   [:displayName :string]
                                   [:description :string]
                                   [:labels [:sequential :gcp.foreign.com.google.api/LabelDescriptor]]]}
    {::g/name ::registry}))
