(ns gcp.foreign.com.google.iam.v1
  (:require [gcp.global :as global]
            [gcp.foreign.com.google.protobuf :as protobuf])
  (:import (com.google.iam.v1 Policy GetIamPolicyRequest SetIamPolicyRequest TestIamPermissionsRequest TestIamPermissionsResponse)))

;; Policy
(defn Policy-from-edn [arg]
  (let [builder (Policy/newBuilder)]
    (when-some [v (:version arg)] (.setVersion builder v))
    (when-some [v (:etag arg)] (.setEtag builder (protobuf/ByteString-from-edn v)))
    (.build builder)))

(defn Policy-to-edn [^Policy arg]
  {:version (.getVersion arg)
   :etag (protobuf/ByteString-to-edn (.getEtag arg))})

;; GetIamPolicyRequest
(defn GetIamPolicyRequest-from-edn [arg]
  (let [builder (GetIamPolicyRequest/newBuilder)]
    (when-some [v (:resource arg)] (.setResource builder v))
    (.build builder)))

(defn GetIamPolicyRequest-to-edn [^GetIamPolicyRequest arg]
  {:resource (.getResource arg)})

(global/include-schema-registry!
 (with-meta
   {::Policy [:map
              [:version [:int {:min 0 :max 3}]]
              [:etag :gcp.foreign.com.google.protobuf/ByteString]]
    ::GetIamPolicyRequest [:map
                           [:resource :string]]}
   {::global/name ::registry}))
