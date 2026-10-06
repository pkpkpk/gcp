(ns gcp.foreign.com.google.auth.oauth2
  {:doc "Foreign bindings for com.google.auth.oauth2"}
  (:require
    [gcp.global :as g])
  (:import
    (com.google.auth.oauth2 GoogleCredentials ImpersonatedCredentials)))

(def registry
  (with-meta
    {::ImpersonatedCredentials
     [:or
      (g/instance-schema com.google.auth.oauth2.ImpersonatedCredentials)
      [:map
       [:sourceCredentials
        (g/instance-schema com.google.auth.oauth2.GoogleCredentials)]
       [:targetPrincipal :string]
       [:scopes [:vector :string]]
       [:delegates {:optional true} [:vector :string]]
       [:lifetime {:optional true} :int]
       [:quotaProjectId {:optional true} :string]
       [:httpTransportFactory
        {:optional true}
        (g/instance-schema com.google.auth.http.HttpTransportFactory)]
       [:iamEndpointOverride {:optional true} :string]]]}
    {::g/name ::registry}))

(g/include-schema-registry! registry)

(defn ImpersonatedCredentials-from-edn
  [arg]
  (if (instance? ImpersonatedCredentials arg)
    arg
    (let [builder (ImpersonatedCredentials/newBuilder)]
      (.setSourceCredentials
        builder
        (:sourceCredentials arg))
      (.setTargetPrincipal
        builder
        (:targetPrincipal arg))
      (.setScopes
        builder
        (:scopes arg))
      (some->> (:delegates arg)
               (.setDelegates builder))
      (some->> (:lifetime arg)
               (.setLifetime builder))
      (some->> (:quotaProjectId arg)
               (.setQuotaProjectId builder))
      (some->> (:httpTransportFactory arg)
               (.setHttpTransportFactory builder))
      (some->> (:iamEndpointOverride arg)
               (.setIamEndpointOverride builder))
      (.build builder))))

(defn ImpersonatedCredentials-to-edn
  [^ImpersonatedCredentials arg]
  (cond-> {:sourceCredentials (.getSourceCredentials arg)
           :targetPrincipal (.getTargetPrincipal arg)
           :scopes (.getScopes arg)}
          (.getDelegates arg)
          (assoc :delegates (.getDelegates arg))

          (some? (.getLifetime arg))
          (assoc :lifetime (.getLifetime arg))

          (.getQuotaProjectId arg)
          (assoc :quotaProjectId (.getQuotaProjectId arg))

          (.getHttpTransportFactory arg)
          (assoc :httpTransportFactory
                 (.getHttpTransportFactory arg))

          (.getIamEndpointOverride arg)
          (assoc :iamEndpointOverride
                 (.getIamEndpointOverride arg))))