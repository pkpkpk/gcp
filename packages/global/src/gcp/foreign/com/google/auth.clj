(ns gcp.foreign.com.google.auth
  (:require [gcp.global :as g])
  (:import (com.google.auth Credentials ServiceAccountSigner)
           (com.google.auth.oauth2 GoogleCredentials)))

(def registry
  (with-meta
    (let [base (g/instance-schema com.google.auth.Credentials)]
      {::Credentials [:or
                      (if (map? base)
                        (assoc base :gen/schema [:map [:type [:enum :getApplicationDefault]]])
                        [:and {:gen/schema [:map [:type [:enum :getApplicationDefault]]]} base])
                      [:map [:type [:enum :getApplicationDefault]]]]
       ::ServiceAccountSigner [:any]})
    {::g/name ::registry}))

(g/include-schema-registry! registry)

(defn Credentials-to-edn [arg]
  (cond
    (instance? GoogleCredentials arg) {:type :getApplicationDefault}
    :else (throw (ex-info "Unknown credentials type" {:class (class arg)}))))

(defn Credentials-from-edn [arg]
  (case (:type arg)
    :getApplicationDefault (GoogleCredentials/getApplicationDefault)
    (throw (ex-info "Unknown credentials type in EDN" {:arg arg}))))

(defn ServiceAccountSigner-to-edn [arg]
  (throw (UnsupportedOperationException. "ServiceAccountSigner-to-edn not implemented")))

(defn ServiceAccountSigner-from-edn [arg]
  (throw (UnsupportedOperationException. "ServiceAccountSigner-from-edn not implemented")))
