(ns gcp.foreign.com.google.auth
  {:gcp.dev/certification
   {:Credentials
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767571324993
     :timestamp "2026-01-05T00:02:05.139742832Z"
     :passed-stages {:smoke 1767571324993
                     :standard 1767571324994
                     :stress 1767571324995}
     :source-hash "2815478eb54ad7a438ca92eeaec48cb133b90bdaab6d471a1e01054b8ab5a5"}}}
  {:doc "Foreign bindings for com.google.auth"}
  (:require [gcp.global :as g])
  (:import (com.google.auth Credentials)
           (com.google.auth.oauth2 GoogleCredentials)))

(def registry
  (with-meta
    (let [base (g/instance-schema com.google.auth.Credentials)]
      {::Credentials [:or
                      (if (map? base)
                        (assoc base :gen/schema [:map [:type [:enum :getApplicationDefault]]])
                        [:and {:gen/schema [:map [:type [:enum :getApplicationDefault]]]} base])
                      [:map [:type [:enum :getApplicationDefault]]]]})
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
