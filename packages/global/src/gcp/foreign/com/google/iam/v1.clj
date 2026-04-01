(ns gcp.foreign.com.google.iam.v1
  {:gcp.dev/certification
   {:GetIamPolicyRequest
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767557975209
       :timestamp "2026-01-04T20:19:35.212390090Z"
       :passed-stages
         {:smoke 1767557975209 :standard 1767557975210 :stress 1767557975211}
       :source-hash
         "5df0912764ea4d2ff97ac579081ba530ff9e60020105271d5d9201cc69e61424"}
    :Policy
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767557975213
       :timestamp "2026-01-04T20:19:35.267716845Z"
       :passed-stages
         {:smoke 1767557975213 :standard 1767557975214 :stress 1767557975215}
       :source-hash
         "5df0912764ea4d2ff97ac579081ba530ff9e60020105271d5d9201cc69e61424"}
    :SetIamPolicyRequest
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767557975268
       :timestamp "2026-01-04T20:19:35.319519737Z"
       :passed-stages
         {:smoke 1767557975268 :standard 1767557975269 :stress 1767557975270}
       :source-hash
         "5df0912764ea4d2ff97ac579081ba530ff9e60020105271d5d9201cc69e61424"}
    :TestIamPermissionsRequest
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767557975319
       :timestamp "2026-01-04T20:19:35.338732747Z"
       :passed-stages
         {:smoke 1767557975319 :standard 1767557975320 :stress 1767557975321}
       :source-hash
         "5df0912764ea4d2ff97ac579081ba530ff9e60020105271d5d9201cc69e61424"}
    :TestIamPermissionsResponse
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767557975338
       :timestamp "2026-01-04T20:19:35.358716654Z"
       :passed-stages
         {:smoke 1767557975338 :standard 1767557975339 :stress 1767557975340}
       :source-hash
         "5df0912764ea4d2ff97ac579081ba530ff9e60020105271d5d9201cc69e61424"}}}
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

;; SetIamPolicyRequest
(defn SetIamPolicyRequest-from-edn [arg]
  (let [builder (SetIamPolicyRequest/newBuilder)]
    (when-some [v (:resource arg)] (.setResource builder v))
    (when-some [v (:policy arg)] (.setPolicy builder (Policy-from-edn v)))
    (.build builder)))

(defn SetIamPolicyRequest-to-edn [^SetIamPolicyRequest arg]
  {:resource (.getResource arg)
   :policy (Policy-to-edn (.getPolicy arg))})

;; TestIamPermissionsRequest
(defn TestIamPermissionsRequest-from-edn [arg]
  (let [builder (TestIamPermissionsRequest/newBuilder)]
    (when-some [v (:resource arg)] (.setResource builder v))
    (when-some [v (:permissions arg)] (.addAllPermissions builder v))
    (.build builder)))

(defn TestIamPermissionsRequest-to-edn [^TestIamPermissionsRequest arg]
  {:resource (.getResource arg)
   :permissions (into [] (.getPermissionsList arg))})

;; TestIamPermissionsResponse
(defn TestIamPermissionsResponse-from-edn [arg]
  (let [builder (TestIamPermissionsResponse/newBuilder)]
    (when-some [v (:permissions arg)] (.addAllPermissions builder v))
    (.build builder)))

(defn TestIamPermissionsResponse-to-edn [^TestIamPermissionsResponse arg]
  {:permissions (into [] (.getPermissionsList arg))})

(global/include-schema-registry!
 (with-meta
   {::Policy [:map
              [:version [:int {:min 0 :max 3}]]
              [:etag :gcp.foreign.com.google.protobuf/ByteString]]
    ::GetIamPolicyRequest [:map
                           [:resource :string]]
    ::SetIamPolicyRequest [:map
                           [:resource :string]
                           [:policy ::Policy]]
    ::TestIamPermissionsRequest [:map
                                 [:resource :string]
                                 [:permissions [:sequential :string]]]
    ::TestIamPermissionsResponse [:map
                                  [:permissions [:sequential :string]]]}
   {::global/name ::registry}))
