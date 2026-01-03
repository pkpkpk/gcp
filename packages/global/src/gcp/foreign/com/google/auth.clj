(ns gcp.foreign.com.google.auth
  (:require
   [gcp.global :as g])
  (:import
   (com.google.auth Credentials)))

(def registry
  ^{::g/name ::registry}
  {::Credentials (g/instance-schema com.google.auth.Credentials)})

(g/include-schema-registry! registry)
