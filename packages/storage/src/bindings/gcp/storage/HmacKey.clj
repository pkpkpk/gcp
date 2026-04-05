;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.storage.HmacKey
  {:doc "HMAC key for a service account."
   :file-git-sha "8f9f5ec4506bde58fbf2351c99f0d67cdcfcd88e"
   :fqcn "com.google.cloud.storage.HmacKey"
   :gcp.dev/certification
     {:base-seed 1775342709947
      :manifest "215ec381-0f5f-5884-ab6d-eb0bb246cd16"
      :passed-stages
        {:smoke 1775342709947 :standard 1775342709948 :stress 1775342709949}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-04T22:45:11.654614440Z"}}
  (:require [gcp.global :as global]
            [gcp.storage.ServiceAccount :as ServiceAccount])
  (:import [com.google.cloud.storage HmacKey HmacKey$Builder
            HmacKey$HmacKeyMetadata HmacKey$HmacKeyMetadata$Builder
            HmacKey$HmacKeyState]))

(declare from-edn
         to-edn
         HmacKeyState-from-edn
         HmacKeyState-to-edn
         HmacKeyMetadata-from-edn
         HmacKeyMetadata-to-edn)

(def HmacKeyState-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.storage/HmacKey.HmacKeyState} "ACTIVE" "INACTIVE" "DELETED"])

(defn ^HmacKey$HmacKeyMetadata HmacKeyMetadata-from-edn
  [arg]
  (let [builder (HmacKey$HmacKeyMetadata/newBuilder
                  (ServiceAccount/from-edn (get arg :serviceAccount)))]
    (when (some? (get arg :accessId))
      (.setAccessId builder (get arg :accessId)))
    (when (some? (get arg :createTime))
      (.setCreateTimeOffsetDateTime builder
                                    (get arg :createTimeOffsetDateTime)))
    (when (some? (get arg :etag)) (.setEtag builder (get arg :etag)))
    (when (some? (get arg :id)) (.setId builder (get arg :id)))
    (when (some? (get arg :projectId))
      (.setProjectId builder (get arg :projectId)))
    (when (some? (get arg :state))
      (.setState builder (HmacKey$HmacKeyState/valueOf (get arg :state))))
    (when (some? (get arg :updateTime))
      (.setUpdateTimeOffsetDateTime builder
                                    (get arg :updateTimeOffsetDateTime)))
    (.build builder)))

(defn HmacKeyMetadata-to-edn
  [^HmacKey$HmacKeyMetadata arg]
  (when arg
    (cond-> {:serviceAccount (ServiceAccount/to-edn (.getServiceAccount arg))}
      (some->> (.getAccessId arg)
               (not= ""))
        (assoc :accessId (.getAccessId arg))
      (.getCreateTimeOffsetDateTime arg) (assoc :createTime
                                           (.getCreateTimeOffsetDateTime arg))
      (some->> (.getEtag arg)
               (not= ""))
        (assoc :etag (.getEtag arg))
      (some->> (.getId arg)
               (not= ""))
        (assoc :id (.getId arg))
      (some->> (.getProjectId arg)
               (not= ""))
        (assoc :projectId (.getProjectId arg))
      (.getState arg) (assoc :state (.name (.getState arg)))
      (.getUpdateTimeOffsetDateTime arg)
        (assoc :updateTime (.getUpdateTimeOffsetDateTime arg)))))

(def HmacKeyMetadata-schema
  [:map
   {:closed true,
    :doc
      "The metadata for a service account HMAC key. This class holds all data associated with an HMAC\nkey other than the secret key.",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/HmacKey.HmacKeyMetadata}
   [:accessId
    {:optional true,
     :getter-doc
       "Returns the access id for this HMAC key. This is the id needed to get or delete the key. *",
     :setter-doc nil} [:string {:min 1}]]
   [:createTime
    {:optional true,
     :getter-doc "Returns the creation time of this HMAC key. *",
     :setter-doc nil} :OffsetDateTime]
   [:etag
    {:optional true,
     :getter-doc
       "Returns HTTP 1.1 Entity tag for this HMAC key.\n\n@see <a href=\"http://tools.ietf.org/html/rfc2616#section-3.11\">Entity Tags</a>",
     :setter-doc nil} [:string {:min 1}]]
   [:id
    {:optional true,
     :getter-doc "Returns the resource name of this HMAC key. *",
     :setter-doc nil} [:string {:min 1}]]
   [:projectId
    {:optional true,
     :getter-doc "Returns the project id associated with this HMAC key. *",
     :setter-doc nil} [:string {:min 1}]]
   [:serviceAccount
    {:getter-doc "Returns the service account associated with this HMAC key. *"}
    :gcp.storage/ServiceAccount]
   [:state
    {:optional true,
     :getter-doc "Returns the current state of this HMAC key. *",
     :setter-doc nil} [:enum {:closed true} "ACTIVE" "INACTIVE" "DELETED"]]
   [:updateTime
    {:optional true,
     :getter-doc "Returns the last updated time of this HMAC key. *",
     :setter-doc nil} :OffsetDateTime]])

(defn ^HmacKey from-edn
  [arg]
  (global/strict! :gcp.storage/HmacKey arg)
  (let [builder (HmacKey/newBuilder (get arg :secretKey))]
    (when (some? (get arg :metadata))
      (.setMetadata builder (HmacKeyMetadata-from-edn (get arg :metadata))))
    (.build builder)))

(defn to-edn
  [^HmacKey arg]
  {:post [(global/strict! :gcp.storage/HmacKey %)]}
  (when arg
    (cond-> {:secretKey (.getSecretKey arg)}
      (.getMetadata arg) (assoc :metadata
                           (HmacKeyMetadata-to-edn (.getMetadata arg))))))

(def schema
  [:map
   {:closed true,
    :doc "HMAC key for a service account.",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.storage/HmacKey}
   [:metadata
    {:optional true,
     :getter-doc "Returns the metadata associated with this HMAC key. *",
     :setter-doc nil} [:ref :gcp.storage/HmacKey.HmacKeyMetadata]]
   [:secretKey
    {:getter-doc "Returns the secret key associated with this HMAC key. *"}
    [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.storage/HmacKey schema,
              :gcp.storage/HmacKey.HmacKeyMetadata HmacKeyMetadata-schema,
              :gcp.storage/HmacKey.HmacKeyState HmacKeyState-schema}
    {:gcp.global/name "gcp.storage.HmacKey"}))