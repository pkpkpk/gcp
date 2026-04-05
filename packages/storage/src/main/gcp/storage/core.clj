(ns gcp.storage.core
  (:require [gcp.foreign.com.google.cloud :as cloud]
            [gcp.global :as g]
            [gcp.storage.Acl :as Acl]
            [gcp.storage.BlobId :as BlobId]
            [gcp.storage.BlobInfo :as BlobInfo]
            [gcp.storage.BucketInfo :as BucketInfo]
            [gcp.storage.HmacKey :as HmacKey]
            [gcp.storage.NotificationInfo :as NotificationInfo]
            [gcp.storage.ServiceAccount :as ServiceAccount]
            [gcp.storage.Storage :as S]
            [gcp.storage.custom.Blob :as Blob]
            [gcp.storage.custom.Bucket :as Bucket]
            [gcp.storage.custom.Notification :as Notification]
            [gcp.storage.custom.StorageOptions :as SO]
            [malli.core :as m])
  (:import (com.google.cloud.storage Storage)))

(defonce ^:dynamic *client* nil)

(defonce *clients (atom {}))

(defn client
  ([]
   (client nil))
  ([arg]
   (or *client*
       (if (instance? Storage arg)
         arg
         (or (get @*clients arg)
             (let [client (SO/get-service arg)]
               (swap! *clients assoc arg client)
               client))))))

(def registry
  {::clientable
   [:or
    (g/instance-schema com.google.cloud.storage.Storage)
    :gcp.storage/StorageOptions
    [:map [:storage [:or :gcp.storage/StorageOptions (g/instance-schema com.google.cloud.storage.Storage)]]]]

   ::BucketList
   [:map {:closed true :doc "call record for storage.list()"}
    [:op [:= ::BucketList]]
    [:storage {:optional true} [:ref ::clientable]]
    [:opts {:optional true} :gcp.storage/Storage.BucketListOption]]

   ::BucketGet
   [:map {:closed true :doc "call record for storage.get(bucket)"}
    [:op [:= ::BucketGet]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket string?]
    [:opts {:optional true} :gcp.storage/Storage.BucketGetOption]]

   ::BucketCreate
   [:map {:closed true :doc "call record for storage.create(bucketInfo)"}
    [:op [:= ::BucketCreate]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucketInfo :gcp.storage/BucketInfo]
    [:opts {:optional true} :gcp.storage/Storage.BucketTargetOption]]

   ::BucketUpdate
   [:map {:closed true :doc "call record for storage.update(bucketInfo)"}
    [:op [:= ::BucketUpdate]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucketInfo :gcp.storage/BucketInfo]
    [:opts {:optional true} :gcp.storage/Storage.BucketTargetOption]]

   ::BucketDelete
   [:map {:closed true :doc "call record for storage.delete(bucket)"}
    [:op [:= ::BucketDelete]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket string?]
    [:opts {:optional true} :gcp.storage/Storage.BucketSourceOption]]

   ::BlobList
   [:map {:closed true :doc "call record for storage.list(bucket)"}
    [:op [:= ::BlobList]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket string?]
    [:opts {:optional true} :gcp.storage/Storage.BlobListOption]]

   ::BlobGet
   [:map {:closed true :doc "call record for storage.get(blobId)"}
    [:op [:= ::BlobGet]]
    [:storage {:optional true} [:ref ::clientable]]
    [:blobId :gcp.storage/BlobId]
    [:opts {:optional true} :gcp.storage/Storage.BlobGetOption]]

   ::BlobGetMany
   [:map {:closed true :doc "call record for storage.get(blobIds)"}
    [:op [:= ::BlobGetMany]]
    [:storage {:optional true} [:ref ::clientable]]
    [:blobIds [:sequential :gcp.storage/BlobId]]]

   ::BlobCreate
   [:map {:closed true :doc "call record for storage.create(blobInfo, content)"}
    [:op [:= ::BlobCreate]]
    [:storage {:optional true} [:ref ::clientable]]
    [:blobInfo :gcp.storage/BlobInfo]
    [:content {:optional true} [:or 'bytes? (g/instance-schema java.io.InputStream) (g/instance-schema java.nio.file.Path)]]
    [:offset {:optional true} :int]
    [:length {:optional true} :int]
    [:bufferSize {:optional true} :int]
    [:opts {:optional true} [:or :gcp.storage/Storage.BlobTargetOption :gcp.storage/Storage.BlobWriteOption]]]

   ::BlobUpdate
   [:map {:closed true :doc "call record for storage.update(blobInfo)"}
    [:op [:= ::BlobUpdate]]
    [:storage {:optional true} [:ref ::clientable]]
    [:blobInfo :gcp.storage/BlobInfo]
    [:opts {:optional true} :gcp.storage/Storage.BlobTargetOption]]

   ::BlobUpdateMany
   [:map {:closed true :doc "call record for storage.update(blobInfos)"}
    [:op [:= ::BlobUpdateMany]]
    [:storage {:optional true} [:ref ::clientable]]
    [:blobInfos [:sequential :gcp.storage/BlobInfo]]]

   ::BlobDelete
   [:map {:closed true :doc "call record for storage.delete(blobId)"}
    [:op [:= ::BlobDelete]]
    [:storage {:optional true} [:ref ::clientable]]
    [:blobId :gcp.storage/BlobId]
    [:opts {:optional true} :gcp.storage/Storage.BlobSourceOption]]

   ::BlobDeleteMany
   [:map {:closed true :doc "call record for storage.delete(blobIds)"}
    [:op [:= ::BlobDeleteMany]]
    [:storage {:optional true} [:ref ::clientable]]
    [:blobIds [:sequential :gcp.storage/BlobId]]]

   ::BlobRead
   [:map {:closed true :doc "call record for storage.readAllBytes(blobId)"}
    [:op [:= ::BlobRead]]
    [:storage {:optional true} [:ref ::clientable]]
    [:blobId :gcp.storage/BlobId]
    [:opts {:optional true} :gcp.storage/Storage.BlobSourceOption]]

   ::BlobMove
   [:map {:closed true :doc "call record for storage.moveBlob(request)"}
    [:op [:= ::BlobMove]]
    [:storage {:optional true} [:ref ::clientable]]
    [:request :gcp.storage/Storage.MoveBlobRequest]]

   ::BlobDownload
   [:map {:closed true :doc "call record for storage.downloadTo(blobId, destination)"}
    [:op [:= ::BlobDownload]]
    [:storage {:optional true} [:ref ::clientable]]
    [:blobId :gcp.storage/BlobId]
    [:destination [:or (g/instance-schema java.nio.file.Path) (g/instance-schema java.io.OutputStream)]]
    [:opts {:optional true} :gcp.storage/Storage.BlobSourceOption]]

   ::BlobRestore
   [:map {:closed true :doc "call record for storage.restore(blobId)"}
    [:op [:= ::BlobRestore]]
    [:storage {:optional true} [:ref ::clientable]]
    [:blobId :gcp.storage/BlobId]
    [:opts {:optional true} :gcp.storage/Storage.BlobRestoreOption]]

   ::BlobSignUrl
   [:map {:closed true :doc "call record for storage.signUrl(blobInfo, duration, unit)"}
    [:op [:= ::BlobSignUrl]]
    [:storage {:optional true} [:ref ::clientable]]
    [:blobInfo :gcp.storage/BlobInfo]
    [:duration :int]
    [:unit (g/instance-schema java.util.concurrent.TimeUnit)]
    [:opts {:optional true} :gcp.storage/Storage.SignUrlOption]]

   ::AclList
   [:map {:closed true :doc "call record for storage.listAcls(bucket|blobId)"}
    [:op [:= ::AclList]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket {:optional true} string?]
    [:blobId {:optional true} :gcp.storage/BlobId]
    [:opts {:optional true} :gcp.storage/Storage.BucketSourceOption]]

   ::AclCreate
   [:map {:closed true :doc "call record for storage.createAcl(bucket|blobId, acl)"}
    [:op [:= ::AclCreate]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket {:optional true} string?]
    [:blobId {:optional true} :gcp.storage/BlobId]
    [:acl :gcp.storage/Acl]
    [:opts {:optional true} :gcp.storage/Storage.BucketSourceOption]]

   ::AclGet
   [:map {:closed true :doc "call record for storage.getAcl(bucket|blobId, entity)"}
    [:op [:= ::AclGet]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket {:optional true} string?]
    [:blobId {:optional true} :gcp.storage/BlobId]
    [:entity :gcp.storage/Acl.Entity]
    [:opts {:optional true} :gcp.storage/Storage.BucketSourceOption]]

   ::AclDelete
   [:map {:closed true :doc "call record for storage.deleteAcl(bucket|blobId, entity)"}
    [:op [:= ::AclDelete]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket {:optional true} string?]
    [:blobId {:optional true} :gcp.storage/BlobId]
    [:entity :gcp.storage/Acl.Entity]
    [:opts {:optional true} :gcp.storage/Storage.BucketSourceOption]]

   ::AclUpdate
   [:map {:closed true :doc "call record for storage.updateAcl(bucket|blobId, acl)"}
    [:op [:= ::AclUpdate]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket {:optional true} string?]
    [:blobId {:optional true} :gcp.storage/BlobId]
    [:acl :gcp.storage/Acl]
    [:opts {:optional true} :gcp.storage/Storage.BucketSourceOption]]

   ::DefaultAclList
   [:map {:closed true :doc "call record for storage.listDefaultAcls(bucket)"}
    [:op [:= ::DefaultAclList]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket string?]]

   ::DefaultAclGet
   [:map {:closed true :doc "call record for storage.getDefaultAcl(bucket, entity)"}
    [:op [:= ::DefaultAclGet]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket string?]
    [:entity :gcp.storage/Acl.Entity]]

   ::DefaultAclCreate
   [:map {:closed true :doc "call record for storage.createDefaultAcl(bucket, acl)"}
    [:op [:= ::DefaultAclCreate]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket string?]
    [:acl :gcp.storage/Acl]]

   ::DefaultAclUpdate
   [:map {:closed true :doc "call record for storage.updateDefaultAcl(bucket, acl)"}
    [:op [:= ::DefaultAclUpdate]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket string?]
    [:acl :gcp.storage/Acl]]

   ::DefaultAclDelete
   [:map {:closed true :doc "call record for storage.deleteDefaultAcl(bucket, entity)"}
    [:op [:= ::DefaultAclDelete]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket string?]
    [:entity :gcp.storage/Acl.Entity]]

   ::BucketLockRetentionPolicy
   [:map {:closed true :doc "call record for storage.lockRetentionPolicy(bucketInfo)"}
    [:op [:= ::BucketLockRetentionPolicy]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucketInfo :gcp.storage/BucketInfo]
    [:opts {:optional true} :gcp.storage/Storage.BucketTargetOption]]

   ::HmacKeysList
   [:map {:closed true :doc "call record for storage.listHmacKeys()"}
    [:op [:= ::HmacKeysList]]
    [:storage {:optional true} [:ref ::clientable]]
    [:opts {:optional true} :gcp.storage/Storage.ListHmacKeysOption]]

   ::HmacKeyGet
   [:map {:closed true :doc "call record for storage.getHmacKey(accessId)"}
    [:op [:= ::HmacKeyGet]]
    [:storage {:optional true} [:ref ::clientable]]
    [:accessId string?]
    [:opts {:optional true} :gcp.storage/Storage.GetHmacKeyOption]]

   ::HmacKeyCreate
   [:map {:closed true :doc "call record for storage.createHmacKey(serviceAccount)"}
    [:op [:= ::HmacKeyCreate]]
    [:storage {:optional true} [:ref ::clientable]]
    [:serviceAccount :gcp.storage/ServiceAccount]
    [:opts {:optional true} :gcp.storage/Storage.CreateHmacKeyOption]]

   ::HmacKeyUpdate
   [:map {:closed true :doc "call record for storage.updateHmacKeyState(hmacKeyMetadata, state)"}
    [:op [:= ::HmacKeyUpdate]]
    [:storage {:optional true} [:ref ::clientable]]
    [:hmacKeyMetadata :gcp.storage/HmacKey.HmacKeyMetadata]
    [:state :gcp.storage/HmacKey.HmacKeyState]
    [:opts {:optional true} :gcp.storage/Storage.UpdateHmacKeyOption]]

   ::HmacKeyDelete
   [:map {:closed true :doc "call record for storage.deleteHmacKey(hmacKeyMetadata)"}
    [:op [:= ::HmacKeyDelete]]
    [:storage {:optional true} [:ref ::clientable]]
    [:hmacKeyMetadata :gcp.storage/HmacKey.HmacKeyMetadata]
    [:opts {:optional true} :gcp.storage/Storage.DeleteHmacKeyOption]]

   ::NotificationsList
   [:map {:closed true :doc "call record for storage.listNotifications(bucket)"}
    [:op [:= ::NotificationsList]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket string?]]

   ::NotificationGet
   [:map {:closed true :doc "call record for storage.getNotification(bucket, notificationId)"}
    [:op [:= ::NotificationGet]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket string?]
    [:notificationId string?]]

   ::NotificationCreate
   [:map {:closed true :doc "call record for storage.createNotification(bucket, notificationInfo)"}
    [:op [:= ::NotificationCreate]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket string?]
    [:notificationInfo :gcp.storage/NotificationInfo]]

   ::NotificationDelete
   [:map {:closed true :doc "call record for storage.deleteNotification(bucket, notificationId)"}
    [:op [:= ::NotificationDelete]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket string?]
    [:notificationId string?]]

   ::IamPolicyGet
   [:map {:closed true :doc "call record for storage.getIamPolicy(bucket)"}
    [:op [:= ::IamPolicyGet]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket string?]
    [:opts {:optional true} :gcp.storage/Storage.BucketSourceOption]]

   ::IamPolicySet
   [:map {:closed true :doc "call record for storage.setIamPolicy(bucket, policy)"}
    [:op [:= ::IamPolicySet]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket string?]
    [:policy :gcp.foreign.com.google.cloud/Policy]
    [:opts {:optional true} :gcp.storage/Storage.BucketSourceOption]]

   ::IamPermissionsTest
   [:map {:closed true :doc "call record for storage.testIamPermissions(bucket, permissions)"}
    [:op [:= ::IamPermissionsTest]]
    [:storage {:optional true} [:ref ::clientable]]
    [:bucket string?]
    [:permissions [:sequential string?]]
    [:opts {:optional true} :gcp.storage/Storage.BucketSourceOption]]})

(g/include-schema-registry! (with-meta registry {::g/name "gcp.storage.core"}))

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
#! ::BucketList

(def ^:private list-buckets-args-schema
  [:altn
   [:arity-0 [:catn]]
   [:arity-1 [:altn
              [:opts    [:catn [:opts :gcp.storage/Storage.BucketListOption]]]
              [:client  [:catn [:clientable ::clientable]]]]]
   [:arity-2 [:catn [:clientable ::clientable] [:opts :gcp.storage/Storage.BucketListOption]]]])

(defn ->BucketList [args]
  (let [schema (g/schema list-buckets-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to list-buckets" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable opts]} (extract-parse-values parsed)]
        {:op        ::BucketList
         :storage   clientable
         :opts      opts}))))

(defmethod execute! ::BucketList [{:keys [storage opts]}]
  (let [client (client storage)
        opts (S/BucketListOption-Array-from-edn opts)
        res (.list client opts)]
    (map Bucket/to-edn (seq (.iterateAll res)))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BucketGet

(def ^:private get-bucket-args-schema
  [:altn
   [:arity-1 [:catn [:bucket string?]]]
   [:arity-2 [:altn
              [:bucket-opts [:catn [:bucket string?] [:opts :gcp.storage/Storage.BucketGetOption]]]
              [:client-bucket [:catn [:clientable ::clientable] [:bucket string?]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:bucket string?] [:opts :gcp.storage/Storage.BucketGetOption]]]])

(defn ->BucketGet [args]
  (let [schema (g/schema get-bucket-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to get-bucket" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket opts]} (extract-parse-values parsed)]
        {:op      ::BucketGet
         :storage clientable
         :bucket  bucket
         :opts    opts}))))

(defmethod execute! ::BucketGet [{:keys [storage bucket opts]}]
  (let [client (client storage)
        opts (S/BucketGetOption-Array-from-edn opts)]
    (Bucket/to-edn (.get client bucket opts))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BucketCreate

(def ^:private create-bucket-args-schema
  [:altn
   [:arity-1 [:catn [:bucketInfo :gcp.storage/BucketInfo]]]
   [:arity-2 [:altn
              [:info-opts   [:catn [:bucketInfo :gcp.storage/BucketInfo] [:opts :gcp.storage/Storage.BucketTargetOption]]]
              [:client-info [:catn [:clientable ::clientable] [:bucketInfo :gcp.storage/BucketInfo]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:bucketInfo :gcp.storage/BucketInfo] [:opts :gcp.storage/Storage.BucketTargetOption]]]])

(defn ->BucketCreate [args]
  (let [schema (g/schema create-bucket-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to create-bucket" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucketInfo opts]} (extract-parse-values parsed)]
        {:op         ::BucketCreate
         :storage    clientable
         :bucketInfo bucketInfo
         :opts       opts}))))

(defmethod execute! ::BucketCreate [{:keys [storage bucketInfo opts]}]
  (let [client (client storage)
        info (BucketInfo/from-edn bucketInfo)
        opts (S/BucketTargetOption-Array-from-edn opts)]
    (Bucket/to-edn (.create client info opts))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BucketUpdate

(def ^:private update-bucket-args-schema
  [:altn
   [:arity-1 [:catn [:bucketInfo :gcp.storage/BucketInfo]]]
   [:arity-2 [:altn
              [:info-opts   [:catn [:bucketInfo :gcp.storage/BucketInfo] [:opts :gcp.storage/Storage.BucketTargetOption]]]
              [:client-info [:catn [:clientable ::clientable] [:bucketInfo :gcp.storage/BucketInfo]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:bucketInfo :gcp.storage/BucketInfo] [:opts :gcp.storage/Storage.BucketTargetOption]]]])

(defn ->BucketUpdate [args]
  (let [schema (g/schema update-bucket-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to update-bucket" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucketInfo opts]} (extract-parse-values parsed)]
        {:op         ::BucketUpdate
         :storage    clientable
         :bucketInfo bucketInfo
         :opts       opts}))))

(defmethod execute! ::BucketUpdate [{:keys [storage bucketInfo opts]}]
  (let [client (client storage)
        info (BucketInfo/from-edn bucketInfo)
        opts (S/BucketTargetOption-Array-from-edn opts)]
    (Bucket/to-edn (.update client info opts))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BucketDelete

(def ^:private delete-bucket-args-schema
  [:altn
   [:arity-1 [:catn [:bucket string?]]]
   [:arity-2 [:altn
              [:bucket-opts [:catn [:bucket string?] [:opts :gcp.storage/Storage.BucketSourceOption]]]
              [:client-bucket [:catn [:clientable ::clientable] [:bucket string?]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:bucket string?] [:opts :gcp.storage/Storage.BucketSourceOption]]]])

(defn ->BucketDelete [args]
  (let [schema (g/schema delete-bucket-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to delete-bucket" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket opts]} (extract-parse-values parsed)]
        {:op      ::BucketDelete
         :storage clientable
         :bucket  bucket
         :opts    opts}))))

(defmethod execute! ::BucketDelete [{:keys [storage bucket opts]}]
  (let [client (client storage)
        opts (S/BucketSourceOption-Array-from-edn opts)]
    (.delete client bucket opts)))

#!----------------------------------------------------------------------------------------------------------------------
#! ::LockRetentionPolicy

(def ^:private lock-retention-policy-args-schema
  [:altn
   [:arity-1 [:catn [:bucketInfo :gcp.storage/BucketInfo]]]
   [:arity-2 [:altn
              [:info-opts   [:catn [:bucketInfo :gcp.storage/BucketInfo] [:opts :gcp.storage/Storage.BucketTargetOption]]]
              [:client-info [:catn [:clientable ::clientable] [:bucketInfo :gcp.storage/BucketInfo]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:bucketInfo :gcp.storage/BucketInfo] [:opts :gcp.storage/Storage.BucketTargetOption]]]])

(defn ->LockRetentionPolicy [args]
  (let [schema (g/schema lock-retention-policy-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to lock-retention-policy" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucketInfo opts]} (extract-parse-values parsed)]
        (cond-> {:op         ::BucketLockRetentionPolicy
                 :bucketInfo bucketInfo}
          clientable (assoc :storage clientable)
          opts       (assoc :opts opts))))))

(defmethod execute! ::BucketLockRetentionPolicy [{:keys [storage bucketInfo opts]}]
  (let [client (client storage)
        info (BucketInfo/from-edn bucketInfo)
        opts (S/BucketTargetOption-Array-from-edn opts)]
    (Bucket/to-edn (.lockRetentionPolicy client info opts))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BlobList

(def ^:private list-blobs-args-schema
  [:altn
   [:arity-1 [:catn [:bucket string?]]]
   [:arity-2 [:altn
              [:bucket-opts [:catn [:bucket string?] [:opts :gcp.storage/Storage.BlobListOption]]]
              [:client-bucket [:catn [:clientable ::clientable] [:bucket string?]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:bucket string?] [:opts :gcp.storage/Storage.BlobListOption]]]])

(defn ->BlobList [args]
  (let [schema (g/schema list-blobs-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to list-blobs" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket opts]} (extract-parse-values parsed)]
        {:op      ::BlobList
         :storage clientable
         :bucket  bucket
         :opts    opts}))))

(defmethod execute! ::BlobList [{:keys [storage bucket opts]}]
  (let [client (client storage)
        opts (S/BlobListOption-Array-from-edn opts)
        res (.list client bucket opts)]
    (map Blob/to-edn (seq (.iterateAll res)))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BlobGet

(def ^:private get-blob-args-schema
  [:altn
   [:arity-1 [:catn [:blobId :gcp.storage/BlobId]]]
   [:arity-2 [:altn
              [:bucket-name [:catn [:bucket string?] [:name string?]]]
              [:id-opts     [:catn [:blobId :gcp.storage/BlobId] [:opts :gcp.storage/Storage.BlobGetOption]]]
              [:client-id   [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId]]]]]
   [:arity-3 [:altn
              [:client-bucket-name [:catn [:clientable ::clientable] [:bucket string?] [:name string?]]]
              [:bucket-name-opts   [:catn [:bucket string?] [:name string?] [:opts :gcp.storage/Storage.BlobGetOption]]]
              [:client-id-opts     [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId] [:opts :gcp.storage/Storage.BlobGetOption]]]]]
   [:arity-4 [:catn [:clientable ::clientable] [:bucket string?] [:name string?] [:opts :gcp.storage/Storage.BlobGetOption]]]])

(defn ->BlobGet [args]
  (let [schema (g/schema get-blob-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to get-blob" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket name blobId opts]} (extract-parse-values parsed)
            resolved-id (or blobId {:bucket bucket :name name})]
        {:op      ::BlobGet
         :storage clientable
         :blobId  resolved-id
         :opts    opts}))))

(defmethod execute! ::BlobGet [{:keys [storage blobId opts]}]
  (let [client (client storage)
        id (BlobId/from-edn blobId)
        opts (S/BlobGetOption-Array-from-edn opts)]
    (Blob/to-edn (.get client id opts))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BlobGetMany

(def ^:private get-blobs-args-schema
  [:altn
   [:arity-1 [:catn [:blobIds [:sequential :gcp.storage/BlobId]]]]
   [:arity-2 [:catn [:clientable ::clientable] [:blobIds [:sequential :gcp.storage/BlobId]]]]])

(defn ->BlobGetMany [args]
  (let [schema (g/schema get-blobs-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to get-blobs" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable blobIds]} (extract-parse-values parsed)]
        {:op      ::BlobGetMany
         :storage clientable
         :blobIds blobIds}))))

(defmethod execute! ::BlobGetMany [{:keys [storage blobIds]}]
  (let [client (client storage)
        ids (into-array com.google.cloud.storage.BlobId (map BlobId/from-edn blobIds))]
    (map Blob/to-edn (.get client ids))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BlobCreate

(def ^:private create-blob-args-schema
  [:altn
   [:arity-1 [:catn [:blobInfo :gcp.storage/BlobInfo]]]
   [:arity-2 [:altn
              [:info-content [:catn [:blobInfo :gcp.storage/BlobInfo] [:content [:or 'bytes? (g/instance-schema java.io.InputStream) (g/instance-schema java.nio.file.Path)]]]]
              [:info-opts    [:catn [:blobInfo :gcp.storage/BlobInfo] [:opts [:or :gcp.storage/Storage.BlobTargetOption :gcp.storage/Storage.BlobWriteOption]]]]
              [:client-info  [:catn [:clientable ::clientable] [:blobInfo :gcp.storage/BlobInfo]]]]]
   [:arity-3 [:altn
              [:client-info-content [:catn [:clientable ::clientable] [:blobInfo :gcp.storage/BlobInfo] [:content [:or 'bytes? (g/instance-schema java.io.InputStream) (g/instance-schema java.nio.file.Path)]]]]
              [:client-info-opts    [:catn [:clientable ::clientable] [:blobInfo :gcp.storage/BlobInfo] [:opts [:or :gcp.storage/Storage.BlobTargetOption :gcp.storage/Storage.BlobWriteOption]]]]
              [:info-content-opts   [:catn [:blobInfo :gcp.storage/BlobInfo] [:content [:or 'bytes? (g/instance-schema java.io.InputStream) (g/instance-schema java.nio.file.Path)]] [:opts [:or :gcp.storage/Storage.BlobTargetOption :gcp.storage/Storage.BlobWriteOption]]]]
              [:info-content-off-len [:catn [:blobInfo :gcp.storage/BlobInfo] [:content 'bytes?] [:offset :int] [:length :int]]]]]
   [:arity-4 [:altn
              [:client-info-content-opts    [:catn [:clientable ::clientable] [:blobInfo :gcp.storage/BlobInfo] [:content [:or 'bytes? (g/instance-schema java.io.InputStream) (g/instance-schema java.nio.file.Path)]] [:opts [:or :gcp.storage/Storage.BlobTargetOption :gcp.storage/Storage.BlobWriteOption]]]]
              [:client-info-content-off-len [:catn [:clientable ::clientable] [:blobInfo :gcp.storage/BlobInfo] [:content 'bytes?] [:offset :int] [:length :int]]]
              [:info-content-off-len-opts   [:catn [:blobInfo :gcp.storage/BlobInfo] [:content 'bytes?] [:offset :int] [:length :int] [:opts :gcp.storage/Storage.BlobTargetOption]]]
              [:info-from-path-buf-opts     [:catn [:blobInfo :gcp.storage/BlobInfo] [:content (g/instance-schema java.nio.file.Path)] [:bufferSize :int] [:opts :gcp.storage/Storage.BlobWriteOption]]]
              [:info-from-stream-buf-opts   [:catn [:blobInfo :gcp.storage/BlobInfo] [:content (g/instance-schema java.io.InputStream)] [:bufferSize :int] [:opts :gcp.storage/Storage.BlobWriteOption]]]]]
   [:arity-5 [:altn
              [:client-info-content-off-len-opts [:catn [:clientable ::clientable] [:blobInfo :gcp.storage/BlobInfo] [:content 'bytes?] [:offset :int] [:length :int] [:opts :gcp.storage/Storage.BlobTargetOption]]]
              [:client-info-from-path-buf-opts   [:catn [:clientable ::clientable] [:blobInfo :gcp.storage/BlobInfo] [:content (g/instance-schema java.nio.file.Path)] [:bufferSize :int] [:opts :gcp.storage/Storage.BlobWriteOption]]]
              [:client-info-from-stream-buf-opts [:catn [:clientable ::clientable] [:blobInfo :gcp.storage/BlobInfo] [:content (g/instance-schema java.io.InputStream)] [:bufferSize :int] [:opts :gcp.storage/Storage.BlobWriteOption]]]]]])

(defn ->BlobCreate [args]
  (let [schema (g/schema create-blob-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to create-blob" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable blobInfo content opts offset length bufferSize]} (extract-parse-values parsed)]
        (cond-> {:op       ::BlobCreate
                 :blobInfo blobInfo}
          clientable (assoc :storage clientable)
          content    (assoc :content content)
          opts       (assoc :opts opts)
          offset     (assoc :offset offset)
          length     (assoc :length length)
          bufferSize (assoc :bufferSize bufferSize))))))

(defmethod execute! ::BlobCreate [{:keys [storage blobInfo content opts offset length bufferSize]}]
  (let [client (client storage)
        info (BlobInfo/from-edn blobInfo)
        is-path (instance? java.nio.file.Path content)
        is-stream (instance? java.io.InputStream content)]
    (cond
      (and is-stream bufferSize)
      (Blob/to-edn (.createFrom client info ^java.io.InputStream content ^int bufferSize (S/BlobWriteOption-Array-from-edn opts)))
      is-stream
      (Blob/to-edn (.createFrom client info ^java.io.InputStream content (S/BlobWriteOption-Array-from-edn opts)))
      (and is-path bufferSize)
      (Blob/to-edn (.createFrom client info ^java.nio.file.Path content ^int bufferSize (S/BlobWriteOption-Array-from-edn opts)))
      is-path
      (Blob/to-edn (.createFrom client info ^java.nio.file.Path content (S/BlobWriteOption-Array-from-edn opts)))
      (and content offset length)
      (Blob/to-edn (.create client info ^bytes content ^int offset ^int length (S/BlobTargetOption-Array-from-edn opts)))
      content
      (Blob/to-edn (.create client info ^bytes content (S/BlobTargetOption-Array-from-edn opts)))
      :else
      (Blob/to-edn (.create client info (S/BlobTargetOption-Array-from-edn opts))))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BlobUpdate

(def ^:private update-blob-args-schema
  [:altn
   [:arity-1 [:catn [:blobInfo :gcp.storage/BlobInfo]]]
   [:arity-2 [:altn
              [:info-opts   [:catn [:blobInfo :gcp.storage/BlobInfo] [:opts :gcp.storage/Storage.BlobTargetOption]]]
              [:client-info [:catn [:clientable ::clientable] [:blobInfo :gcp.storage/BlobInfo]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:blobInfo :gcp.storage/BlobInfo] [:opts :gcp.storage/Storage.BlobTargetOption]]]])

(defn ->BlobUpdate [args]
  (let [schema (g/schema update-blob-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to update-blob" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable blobInfo opts]} (extract-parse-values parsed)]
        {:op       ::BlobUpdate
         :storage  clientable
         :blobInfo blobInfo
         :opts     opts}))))

(defmethod execute! ::BlobUpdate [{:keys [storage blobInfo opts]}]
  (let [client (client storage)
        info (BlobInfo/from-edn blobInfo)
        opts (S/BlobTargetOption-Array-from-edn opts)]
    (Blob/to-edn (.update client info opts))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BlobUpdateMany

(def ^:private update-blobs-args-schema
  [:altn
   [:arity-1 [:catn [:blobInfos [:sequential :gcp.storage/BlobInfo]]]]
   [:arity-2 [:catn [:clientable ::clientable] [:blobInfos [:sequential :gcp.storage/BlobInfo]]]]])

(defn ->BlobUpdateMany [args]
  (let [schema (g/schema update-blobs-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to update-blobs" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable blobInfos]} (extract-parse-values parsed)]
        {:op        ::BlobUpdateMany
         :storage   clientable
         :blobInfos blobInfos}))))

(defmethod execute! ::BlobUpdateMany [{:keys [storage blobInfos]}]
  (let [client (client storage)
        infos (into-array com.google.cloud.storage.BlobInfo (map BlobInfo/from-edn blobInfos))]
    (map Blob/to-edn (.update client infos))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BlobDelete

(def ^:private delete-blob-args-schema
  [:altn
   [:arity-1 [:catn [:blobId :gcp.storage/BlobId]]]
   [:arity-2 [:altn
              [:bucket-name [:catn [:bucket string?] [:name string?]]]
              [:id-opts     [:catn [:blobId :gcp.storage/BlobId] [:opts :gcp.storage/Storage.BlobSourceOption]]]
              [:client-id   [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId]]]]]
   [:arity-3 [:altn
              [:client-bucket-name [:catn [:clientable ::clientable] [:bucket string?] [:name string?]]]
              [:bucket-name-opts   [:catn [:bucket string?] [:name string?] [:opts :gcp.storage/Storage.BlobSourceOption]]]
              [:client-id-opts     [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId] [:opts :gcp.storage/Storage.BlobSourceOption]]]]]
   [:arity-4 [:catn [:clientable ::clientable] [:bucket string?] [:name string?] [:opts :gcp.storage/Storage.BlobSourceOption]]]])

(defn ->BlobDelete [args]
  (let [schema (g/schema delete-blob-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to delete-blob" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket name blobId opts]} (extract-parse-values parsed)
            resolved-id (or blobId {:bucket bucket :name name})]
        {:op      ::BlobDelete
         :storage clientable
         :blobId  resolved-id
         :opts    opts}))))

(defmethod execute! ::BlobDelete [{:keys [storage blobId opts]}]
  (let [client (client storage)
        id (BlobId/from-edn blobId)
        opts (S/BlobSourceOption-Array-from-edn opts)]
    (.delete client id opts)))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BlobDeleteMany

(def ^:private delete-blobs-args-schema
  [:altn
   [:arity-1 [:catn [:blobIds [:sequential :gcp.storage/BlobId]]]]
   [:arity-2 [:catn [:clientable ::clientable] [:blobIds [:sequential :gcp.storage/BlobId]]]]])

(defn ->BlobDeleteMany [args]
  (let [schema (g/schema delete-blobs-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to delete-blobs" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable blobIds]} (extract-parse-values parsed)]
        {:op      ::BlobDeleteMany
         :storage clientable
         :blobIds blobIds}))))

(defmethod execute! ::BlobDeleteMany [{:keys [storage blobIds]}]
  (let [client (client storage)
        ids (into-array com.google.cloud.storage.BlobId (map BlobId/from-edn blobIds))]
    (.delete client ids)))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BlobRead

(def ^:private read-blob-args-schema
  [:altn
   [:arity-1 [:catn [:blobId :gcp.storage/BlobId]]]
   [:arity-2 [:altn
              [:bucket-name [:catn [:bucket string?] [:name string?]]]
              [:id-opts     [:catn [:blobId :gcp.storage/BlobId] [:opts :gcp.storage/Storage.BlobSourceOption]]]
              [:client-id   [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId]]]]]
   [:arity-3 [:altn
              [:client-bucket-name [:catn [:clientable ::clientable] [:bucket string?] [:name string?]]]
              [:bucket-name-opts   [:catn [:bucket string?] [:name string?] [:opts :gcp.storage/Storage.BlobSourceOption]]]
              [:client-id-opts     [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId] [:opts :gcp.storage/Storage.BlobSourceOption]]]]]
   [:arity-4 [:catn [:clientable ::clientable] [:bucket string?] [:name string?] [:opts :gcp.storage/Storage.BlobSourceOption]]]])

(defn ->BlobRead [args]
  (let [schema (g/schema read-blob-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to read-blob" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket name blobId opts]} (extract-parse-values parsed)
            resolved-id (or blobId {:bucket bucket :name name})]
        {:op      ::BlobRead
         :storage clientable
         :blobId  resolved-id
         :opts    opts}))))

(defmethod execute! ::BlobRead [{:keys [storage blobId opts]}]
  (let [client (client storage)
        id (BlobId/from-edn blobId)
        opts (S/BlobSourceOption-Array-from-edn opts)]
    (.readAllBytes client id opts)))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BlobMove

(def ^:private move-blob-args-schema
  [:altn
   [:arity-1 [:catn [:request :gcp.storage/Storage.MoveBlobRequest]]]
   [:arity-2 [:catn [:clientable ::clientable] [:request :gcp.storage/Storage.MoveBlobRequest]]]])

(defn ->BlobMove [args]
  (let [schema (g/schema move-blob-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to move-blob" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable request]} (extract-parse-values parsed)]
        {:op      ::BlobMove
         :storage clientable
         :request request}))))

(defmethod execute! ::BlobMove [{:keys [storage request]}]
  (let [client (client storage)
        req (S/MoveBlobRequest-from-edn request)]
    (Blob/to-edn (.move client req))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BlobDownload

(def ^:private download-blob-args-schema
  [:altn
   [:arity-2 [:catn [:blobId :gcp.storage/BlobId] [:destination [:or (g/instance-schema java.nio.file.Path) (g/instance-schema java.io.OutputStream)]]]]
   [:arity-3 [:altn
              [:client-id-dest [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId] [:destination [:or (g/instance-schema java.nio.file.Path) (g/instance-schema java.io.OutputStream)]]]]
              [:id-dest-opts   [:catn [:blobId :gcp.storage/BlobId] [:destination [:or (g/instance-schema java.nio.file.Path) (g/instance-schema java.io.OutputStream)]] [:opts :gcp.storage/Storage.BlobSourceOption]]]]]
   [:arity-4 [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId] [:destination [:or (g/instance-schema java.nio.file.Path) (g/instance-schema java.io.OutputStream)]] [:opts :gcp.storage/Storage.BlobSourceOption]]]])

(defn ->BlobDownload [args]
  (let [schema (g/schema download-blob-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to download-blob" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable blobId destination opts]} (extract-parse-values parsed)]
        {:op          ::BlobDownload
         :storage     clientable
         :blobId      blobId
         :destination destination
         :opts        opts}))))

(defmethod execute! ::BlobDownload [{:keys [storage blobId destination opts]}]
  (let [client (client storage)
        id (BlobId/from-edn blobId)
        opts (S/BlobSourceOption-Array-from-edn opts)]
    (.downloadTo client id destination opts)))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BlobRestore

(def ^:private restore-blob-args-schema
  [:altn
   [:arity-1 [:catn [:blobId :gcp.storage/BlobId]]]
   [:arity-2 [:altn
              [:id-opts   [:catn [:blobId :gcp.storage/BlobId] [:opts :gcp.storage/Storage.BlobRestoreOption]]]
              [:client-id [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId] [:opts :gcp.storage/Storage.BlobRestoreOption]]]])

(defn ->BlobRestore [args]
  (let [schema (g/schema restore-blob-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to restore-blob" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable blobId opts]} (extract-parse-values parsed)]
        {:op      ::BlobRestore
         :storage clientable
         :blobId  blobId
         :opts    opts}))))

(defmethod execute! ::BlobRestore [{:keys [storage blobId opts]}]
  (let [client (client storage)
        id (BlobId/from-edn blobId)
        opts (S/BlobRestoreOption-Array-from-edn opts)]
    (Blob/to-edn (.restore client id opts))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::BlobSignUrl

(def ^:private sign-url-args-schema
  [:altn
   [:arity-3 [:catn [:blobInfo :gcp.storage/BlobInfo] [:duration :int] [:unit (g/instance-schema java.util.concurrent.TimeUnit)]]]
   [:arity-4 [:altn
              [:client-info-dur-unit [:catn [:clientable ::clientable] [:blobInfo :gcp.storage/BlobInfo] [:duration :int] [:unit (g/instance-schema java.util.concurrent.TimeUnit)]]]
              [:info-dur-unit-opts   [:catn [:blobInfo :gcp.storage/BlobInfo] [:duration :int] [:unit (g/instance-schema java.util.concurrent.TimeUnit)] [:opts :gcp.storage/Storage.SignUrlOption]]]]]
   [:arity-5 [:catn [:clientable ::clientable] [:blobInfo :gcp.storage/BlobInfo] [:duration :int] [:unit (g/instance-schema java.util.concurrent.TimeUnit)] [:opts :gcp.storage/Storage.SignUrlOption]]]])

(defn ->BlobSignUrl [args]
  (let [schema (g/schema sign-url-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to sign-url" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable blobInfo duration unit opts]} (extract-parse-values parsed)]
        {:op       ::BlobSignUrl
         :storage  clientable
         :blobInfo blobInfo
         :duration duration
         :unit     unit
         :opts     opts}))))

(defmethod execute! ::BlobSignUrl [{:keys [storage blobInfo duration unit opts]}]
  (let [client (client storage)
        info (BlobInfo/from-edn blobInfo)
        opts (S/SignUrlOption-Array-from-edn opts)]
    (.signUrl client info duration unit opts)))

#!----------------------------------------------------------------------------------------------------------------------
#! ::AclList

(def ^:private list-acls-args-schema
  [:altn
   [:arity-1 [:altn
              [:bucket [:catn [:bucket string?]]]
              [:blobId [:catn [:blobId :gcp.storage/BlobId]]]]]
   [:arity-2 [:altn
              [:client-bucket [:catn [:clientable ::clientable] [:bucket string?]]]
              [:client-blobId [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId]]]
              [:bucket-opts   [:catn [:bucket string?] [:opts :gcp.storage/Storage.BucketSourceOption]]]
              [:blobId-opts   [:catn [:blobId :gcp.storage/BlobId] [:opts :gcp.storage/Storage.BucketSourceOption]]]]]
   [:arity-3 [:altn
              [:client-bucket-opts [:catn [:clientable ::clientable] [:bucket string?] [:opts :gcp.storage/Storage.BucketSourceOption]]]
              [:client-blobId-opts [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId] [:opts :gcp.storage/Storage.BucketSourceOption]]]]]])

(defn ->AclList [args]
  (let [schema (g/schema list-acls-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to list-acls" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket blobId opts]} (extract-parse-values parsed)]
        (cond-> {:op ::AclList}
          clientable (assoc :storage clientable)
          bucket     (assoc :bucket bucket)
          blobId     (assoc :blobId blobId)
          opts       (assoc :opts opts))))))

(defmethod execute! ::AclList [{:keys [storage bucket blobId opts]}]
  (let [client (client storage)
        opts (S/BucketSourceOption-Array-from-edn opts)]
    (if bucket
      (map Acl/to-edn (.listAcls client bucket opts))
      (let [id (BlobId/from-edn blobId)]
        (map Acl/to-edn (.listAcls client id opts))))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::AclCreate

(def ^:private create-acl-args-schema
  [:altn
   [:arity-2 [:altn
              [:bucket-acl [:catn [:bucket string?] [:acl :gcp.storage/Acl]]]
              [:blobId-acl [:catn [:blobId :gcp.storage/BlobId] [:acl :gcp.storage/Acl]]]]]
   [:arity-3 [:altn
              [:client-bucket-acl [:catn [:clientable ::clientable] [:bucket string?] [:acl :gcp.storage/Acl]]]
              [:client-blobId-acl [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId] [:acl :gcp.storage/Acl]]]
              [:bucket-acl-opts   [:catn [:bucket string?] [:acl :gcp.storage/Acl] [:opts :gcp.storage/Storage.BucketSourceOption]]]
              [:blobId-acl-opts   [:catn [:blobId :gcp.storage/BlobId] [:acl :gcp.storage/Acl] [:opts :gcp.storage/Storage.BucketSourceOption]]]]]
   [:arity-4 [:altn
              [:client-bucket-acl-opts [:catn [:clientable ::clientable] [:bucket string?] [:acl :gcp.storage/Acl] [:opts :gcp.storage/Storage.BucketSourceOption]]]
              [:client-blobId-acl-opts [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId] [:acl :gcp.storage/Acl] [:opts :gcp.storage/Storage.BucketSourceOption]]]]]])

(defn ->AclCreate [args]
  (let [schema (g/schema create-acl-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to create-acl" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket blobId acl opts]} (extract-parse-values parsed)]
        (cond-> {:op  ::AclCreate
                 :acl acl}
          clientable (assoc :storage clientable)
          bucket     (assoc :bucket bucket)
          blobId     (assoc :blobId blobId)
          opts       (assoc :opts opts))))))

(defmethod execute! ::AclCreate [{:keys [storage bucket blobId acl opts]}]
  (let [client (client storage)
        opts (S/BucketSourceOption-Array-from-edn opts)
        acl-obj (Acl/from-edn acl)]
    (if bucket
      (Acl/to-edn (.createAcl client bucket acl-obj opts))
      (let [id (BlobId/from-edn blobId)]
        (Acl/to-edn (.createAcl client id acl-obj opts))))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::AclGet

(def ^:private get-acl-args-schema
  [:altn
   [:arity-2 [:altn
              [:bucket-entity [:catn [:bucket string?] [:entity :gcp.storage/Acl.Entity]]]
              [:blobId-entity [:catn [:blobId :gcp.storage/BlobId] [:entity :gcp.storage/Acl.Entity]]]]]
   [:arity-3 [:altn
              [:client-bucket-entity [:catn [:clientable ::clientable] [:bucket string?] [:entity :gcp.storage/Acl.Entity]]]
              [:client-blobId-entity [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId] [:entity :gcp.storage/Acl.Entity]]]
              [:bucket-entity-opts   [:catn [:bucket string?] [:entity :gcp.storage/Acl.Entity] [:opts :gcp.storage/Storage.BucketSourceOption]]]
              [:blobId-entity-opts   [:catn [:blobId :gcp.storage/BlobId] [:entity :gcp.storage/Acl.Entity] [:opts :gcp.storage/Storage.BucketSourceOption]]]]]
   [:arity-4 [:altn
              [:client-bucket-entity-opts [:catn [:clientable ::clientable] [:bucket string?] [:entity :gcp.storage/Acl.Entity] [:opts :gcp.storage/Storage.BucketSourceOption]]]
              [:client-blobId-entity-opts [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId] [:entity :gcp.storage/Acl.Entity] [:opts :gcp.storage/Storage.BucketSourceOption]]]]]])

(defn ->AclGet [args]
  (let [schema (g/schema get-acl-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to get-acl" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket blobId entity opts]} (extract-parse-values parsed)]
        (cond-> {:op     ::AclGet
                 :entity entity}
          clientable (assoc :storage clientable)
          bucket     (assoc :bucket bucket)
          blobId     (assoc :blobId blobId)
          opts       (assoc :opts opts))))))

(defmethod execute! ::AclGet [{:keys [storage bucket blobId entity opts]}]
  (let [client (client storage)
        opts (S/BucketSourceOption-Array-from-edn opts)
        ent (Acl/Entity-from-edn entity)]
    (if bucket
      (Acl/to-edn (.getAcl client bucket ent opts))
      (let [id (BlobId/from-edn blobId)]
        (Acl/to-edn (.getAcl client id ent opts))))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::AclDelete

(def ^:private delete-acl-args-schema
  [:altn
   [:arity-2 [:altn
              [:bucket-entity [:catn [:bucket string?] [:entity :gcp.storage/Acl.Entity]]]
              [:blobId-entity [:catn [:blobId :gcp.storage/BlobId] [:entity :gcp.storage/Acl.Entity]]]]]
   [:arity-3 [:altn
              [:client-bucket-entity [:catn [:clientable ::clientable] [:bucket string?] [:entity :gcp.storage/Acl.Entity]]]
              [:client-blobId-entity [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId] [:entity :gcp.storage/Acl.Entity]]]
              [:bucket-entity-opts   [:catn [:bucket string?] [:entity :gcp.storage/Acl.Entity] [:opts :gcp.storage/Storage.BucketSourceOption]]]
              [:blobId-entity-opts   [:catn [:blobId :gcp.storage/BlobId] [:entity :gcp.storage/Acl.Entity] [:opts :gcp.storage/Storage.BucketSourceOption]]]]]
   [:arity-4 [:altn
              [:client-bucket-entity-opts [:catn [:clientable ::clientable] [:bucket string?] [:entity :gcp.storage/Acl.Entity] [:opts :gcp.storage/Storage.BucketSourceOption]]]
              [:client-blobId-entity-opts [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId] [:entity :gcp.storage/Acl.Entity] [:opts :gcp.storage/Storage.BucketSourceOption]]]]]])

(defn ->AclDelete [args]
  (let [schema (g/schema delete-acl-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to delete-acl" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket blobId entity opts]} (extract-parse-values parsed)]
        (cond-> {:op     ::AclDelete
                 :entity entity}
          clientable (assoc :storage clientable)
          bucket     (assoc :bucket bucket)
          blobId     (assoc :blobId blobId)
          opts       (assoc :opts opts))))))

(defmethod execute! ::AclDelete [{:keys [storage bucket blobId entity opts]}]
  (let [client (client storage)
        opts (S/BucketSourceOption-Array-from-edn opts)
        ent (Acl/Entity-from-edn entity)]
    (if bucket
      (.deleteAcl client bucket ent opts)
      (let [id (BlobId/from-edn blobId)]
        (.deleteAcl client id ent opts)))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::AclUpdate

(def ^:private update-acl-args-schema
  [:altn
   [:arity-2 [:altn
              [:bucket-acl [:catn [:bucket string?] [:acl :gcp.storage/Acl]]]
              [:blobId-acl [:catn [:blobId :gcp.storage/BlobId] [:acl :gcp.storage/Acl]]]]]
   [:arity-3 [:altn
              [:client-bucket-acl [:catn [:clientable ::clientable] [:bucket string?] [:acl :gcp.storage/Acl]]]
              [:client-blobId-acl [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId] [:acl :gcp.storage/Acl]]]
              [:bucket-acl-opts   [:catn [:bucket string?] [:acl :gcp.storage/Acl] [:opts :gcp.storage/Storage.BucketSourceOption]]]
              [:blobId-acl-opts   [:catn [:blobId :gcp.storage/BlobId] [:acl :gcp.storage/Acl] [:opts :gcp.storage/Storage.BucketSourceOption]]]]]
   [:arity-4 [:altn
              [:client-bucket-acl-opts [:catn [:clientable ::clientable] [:bucket string?] [:acl :gcp.storage/Acl] [:opts :gcp.storage/Storage.BucketSourceOption]]]
              [:client-blobId-acl-opts [:catn [:clientable ::clientable] [:blobId :gcp.storage/BlobId] [:acl :gcp.storage/Acl] [:opts :gcp.storage/Storage.BucketSourceOption]]]]]])

(defn ->AclUpdate [args]
  (let [schema (g/schema update-acl-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to update-acl" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket blobId acl opts]} (extract-parse-values parsed)]
        (cond-> {:op  ::AclUpdate
                 :acl acl}
          clientable (assoc :storage clientable)
          bucket     (assoc :bucket bucket)
          blobId     (assoc :blobId blobId)
          opts       (assoc :opts opts))))))

(defmethod execute! ::AclUpdate [{:keys [storage bucket blobId acl opts]}]
  (let [client (client storage)
        opts (S/BucketSourceOption-Array-from-edn opts)
        acl-obj (Acl/from-edn acl)]
    (if bucket
      (Acl/to-edn (.updateAcl client bucket acl-obj opts))
      (let [id (BlobId/from-edn blobId)]
        (Acl/to-edn (.updateAcl client id acl-obj opts))))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::DefaultAclList

(def ^:private list-default-acls-args-schema
  [:altn
   [:arity-1 [:catn [:bucket string?]]]
   [:arity-2 [:catn [:clientable ::clientable] [:bucket string?]]]])

(defn ->DefaultAclList [args]
  (let [schema (g/schema list-default-acls-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to list-default-acls" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket]} (extract-parse-values parsed)]
        (cond-> {:op      ::DefaultAclList
                 :bucket  bucket}
          clientable (assoc :storage clientable))))))

(defmethod execute! ::DefaultAclList [{:keys [storage bucket]}]
  (let [client (client storage)]
    (map Acl/to-edn (.listDefaultAcls client bucket))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::DefaultAclGet

(def ^:private get-default-acl-args-schema
  [:altn
   [:arity-2 [:catn [:bucket string?] [:entity :gcp.storage/Acl.Entity]]]
   [:arity-3 [:catn [:clientable ::clientable] [:bucket string?] [:entity :gcp.storage/Acl.Entity]]]])

(defn ->DefaultAclGet [args]
  (let [schema (g/schema get-default-acl-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to get-default-acl" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket entity]} (extract-parse-values parsed)]
        (cond-> {:op      ::DefaultAclGet
                 :bucket  bucket
                 :entity  entity}
          clientable (assoc :storage clientable))))))

(defmethod execute! ::DefaultAclGet [{:keys [storage bucket entity]}]
  (let [client (client storage)
        ent (Acl/Entity-from-edn entity)]
    (Acl/to-edn (.getDefaultAcl client bucket ent))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::DefaultAclCreate

(def ^:private create-default-acl-args-schema
  [:altn
   [:arity-2 [:catn [:bucket string?] [:acl :gcp.storage/Acl]]]
   [:arity-3 [:catn [:clientable ::clientable] [:bucket string?] [:acl :gcp.storage/Acl]]]])

(defn ->DefaultAclCreate [args]
  (let [schema (g/schema create-default-acl-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to create-default-acl" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket acl]} (extract-parse-values parsed)]
        (cond-> {:op      ::DefaultAclCreate
                 :bucket  bucket
                 :acl     acl}
          clientable (assoc :storage clientable))))))

(defmethod execute! ::DefaultAclCreate [{:keys [storage bucket acl]}]
  (let [client (client storage)
        acl-obj (Acl/from-edn acl)]
    (Acl/to-edn (.createDefaultAcl client bucket acl-obj))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::DefaultAclUpdate

(def ^:private update-default-acl-args-schema
  [:altn
   [:arity-2 [:catn [:bucket string?] [:acl :gcp.storage/Acl]]]
   [:arity-3 [:catn [:clientable ::clientable] [:bucket string?] [:acl :gcp.storage/Acl]]]])

(defn ->DefaultAclUpdate [args]
  (let [schema (g/schema update-default-acl-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to update-default-acl" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket acl]} (extract-parse-values parsed)]
        (cond-> {:op      ::DefaultAclUpdate
                 :bucket  bucket
                 :acl     acl}
          clientable (assoc :storage clientable))))))

(defmethod execute! ::DefaultAclUpdate [{:keys [storage bucket acl]}]
  (let [client (client storage)
        acl-obj (Acl/from-edn acl)]
    (Acl/to-edn (.updateDefaultAcl client bucket acl-obj))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::DefaultAclDelete

(def ^:private delete-default-acl-args-schema
  [:altn
   [:arity-2 [:catn [:bucket string?] [:entity :gcp.storage/Acl.Entity]]]
   [:arity-3 [:catn [:clientable ::clientable] [:bucket string?] [:entity :gcp.storage/Acl.Entity]]]])

(defn ->DefaultAclDelete [args]
  (let [schema (g/schema delete-default-acl-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to delete-default-acl" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket entity]} (extract-parse-values parsed)]
        (cond-> {:op      ::DefaultAclDelete
                 :bucket  bucket
                 :entity  entity}
          clientable (assoc :storage clientable))))))

(defmethod execute! ::DefaultAclDelete [{:keys [storage bucket entity]}]
  (let [client (client storage)
        ent (Acl/Entity-from-edn entity)]
    (.deleteDefaultAcl client bucket ent)))

#!----------------------------------------------------------------------------------------------------------------------
#! ::HmacKeysList

(def ^:private list-hmac-keys-args-schema
  [:altn
   [:arity-0 [:catn]]
   [:arity-1 [:altn
              [:opts   [:catn [:opts :gcp.storage/Storage.ListHmacKeysOption]]]
              [:client [:catn [:clientable ::clientable]]]]]
   [:arity-2 [:catn [:clientable ::clientable] [:opts :gcp.storage/Storage.ListHmacKeysOption]]]])

(defn ->HmacKeysList [args]
  (let [schema (g/schema list-hmac-keys-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to list-hmac-keys" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable opts]} (extract-parse-values parsed)]
        (cond-> {:op ::HmacKeysList}
          clientable (assoc :storage clientable)
          opts       (assoc :opts opts))))))

(defmethod execute! ::HmacKeysList [{:keys [storage opts]}]
  (let [client (client storage)
        opts (S/ListHmacKeysOption-Array-from-edn opts)
        res (.listHmacKeys client opts)]
    (map HmacKey/HmacKeyMetadata-to-edn (seq (.iterateAll res)))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::HmacKeyGet

(def ^:private get-hmac-key-args-schema
  [:altn
   [:arity-1 [:catn [:accessId string?]]]
   [:arity-2 [:altn
              [:id-opts   [:catn [:accessId string?] [:opts :gcp.storage/Storage.GetHmacKeyOption]]]
              [:client-id [:catn [:clientable ::clientable] [:accessId string?]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:accessId string?] [:opts :gcp.storage/Storage.GetHmacKeyOption]]]])

(defn ->HmacKeyGet [args]
  (let [schema (g/schema get-hmac-key-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to get-hmac-key" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable accessId opts]} (extract-parse-values parsed)]
        (cond-> {:op       ::HmacKeyGet
                 :accessId accessId}
          clientable (assoc :storage clientable)
          opts       (assoc :opts opts))))))

(defmethod execute! ::HmacKeyGet [{:keys [storage accessId opts]}]
  (let [client (client storage)
        opts (S/GetHmacKeyOption-Array-from-edn opts)]
    (HmacKey/HmacKeyMetadata-to-edn (.getHmacKey client accessId opts))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::HmacKeyCreate

(def ^:private create-hmac-key-args-schema
  [:altn
   [:arity-1 [:catn [:serviceAccount :gcp.storage/ServiceAccount]]]
   [:arity-2 [:altn
              [:sa-opts   [:catn [:serviceAccount :gcp.storage/ServiceAccount] [:opts :gcp.storage/Storage.CreateHmacKeyOption]]]
              [:client-sa [:catn [:clientable ::clientable] [:serviceAccount :gcp.storage/ServiceAccount]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:serviceAccount :gcp.storage/ServiceAccount] [:opts :gcp.storage/Storage.CreateHmacKeyOption]]]])

(defn ->HmacKeyCreate [args]
  (let [schema (g/schema create-hmac-key-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to create-hmac-key" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable serviceAccount opts]} (extract-parse-values parsed)]
        (cond-> {:op             ::HmacKeyCreate
                 :serviceAccount serviceAccount}
          clientable (assoc :storage clientable)
          opts       (assoc :opts opts))))))

(defmethod execute! ::HmacKeyCreate [{:keys [storage serviceAccount opts]}]
  (let [client (client storage)
        sa (ServiceAccount/from-edn serviceAccount)
        opts (S/CreateHmacKeyOption-Array-from-edn opts)]
    (HmacKey/to-edn (.createHmacKey client sa opts))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::HmacKeyUpdate

(def ^:private update-hmac-key-args-schema
  [:altn
   [:arity-2 [:catn [:hmacKeyMetadata :gcp.storage/HmacKey.HmacKeyMetadata] [:state :gcp.storage/HmacKey.HmacKeyState]]]
   [:arity-3 [:altn
              [:meta-state-opts   [:catn [:hmacKeyMetadata :gcp.storage/HmacKey.HmacKeyMetadata] [:state :gcp.storage/HmacKey.HmacKeyState] [:opts :gcp.storage/Storage.UpdateHmacKeyOption]]]
              [:client-meta-state [:catn [:clientable ::clientable] [:hmacKeyMetadata :gcp.storage/HmacKey.HmacKeyMetadata] [:state :gcp.storage/HmacKey.HmacKeyState]]]]]
   [:arity-4 [:catn [:clientable ::clientable] [:hmacKeyMetadata :gcp.storage/HmacKey.HmacKeyMetadata] [:state :gcp.storage/HmacKey.HmacKeyState] [:opts :gcp.storage/Storage.UpdateHmacKeyOption]]]])

(defn ->HmacKeyUpdate [args]
  (let [schema (g/schema update-hmac-key-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to update-hmac-key" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable hmacKeyMetadata state opts]} (extract-parse-values parsed)]
        (cond-> {:op              ::HmacKeyUpdate
                 :hmacKeyMetadata hmacKeyMetadata
                 :state           state}
          clientable (assoc :storage clientable)
          opts       (assoc :opts opts))))))

(defmethod execute! ::HmacKeyUpdate [{:keys [storage hmacKeyMetadata state opts]}]
  (let [client (client storage)
        meta (HmacKey/HmacKeyMetadata-from-edn hmacKeyMetadata)
        st (HmacKey/HmacKeyState-from-edn state)
        opts (S/UpdateHmacKeyOption-Array-from-edn opts)]
    (HmacKey/HmacKeyMetadata-to-edn (.updateHmacKeyState client meta st opts))))

#!----------------------------------------------------------------------------------------------------------------------
#! ::HmacKeyDelete

(def ^:private delete-hmac-key-args-schema
  [:altn
   [:arity-1 [:catn [:hmacKeyMetadata :gcp.storage/HmacKey.HmacKeyMetadata]]]
   [:arity-2 [:altn
              [:meta-opts   [:catn [:hmacKeyMetadata :gcp.storage/HmacKey.HmacKeyMetadata] [:opts :gcp.storage/Storage.DeleteHmacKeyOption]]]
              [:client-meta [:catn [:clientable ::clientable] [:hmacKeyMetadata :gcp.storage/HmacKey.HmacKeyMetadata]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:hmacKeyMetadata :gcp.storage/HmacKey.HmacKeyMetadata] [:opts :gcp.storage/Storage.DeleteHmacKeyOption]]]])

(defn ->HmacKeyDelete [args]
  (let [schema (g/schema delete-hmac-key-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to delete-hmac-key" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable hmacKeyMetadata opts]} (extract-parse-values parsed)]
        (cond-> {:op              ::HmacKeyDelete
                 :hmacKeyMetadata hmacKeyMetadata}
          clientable (assoc :storage clientable)
          opts       (assoc :opts opts))))))

(defmethod execute! ::HmacKeyDelete [{:keys [storage hmacKeyMetadata opts]}]
  (let [client (client storage)
        meta (HmacKey/HmacKeyMetadata-from-edn hmacKeyMetadata)
        opts (S/DeleteHmacKeyOption-Array-from-edn opts)]
    (.deleteHmacKey client meta opts)))

#!----------------------------------------------------------------------------------------------------------------------
#! ::NotificationsList

(def ^:private list-notifications-args-schema
  [:altn
   [:arity-1 [:catn [:bucket string?]]]
   [:arity-2 [:catn [:clientable ::clientable] [:bucket string?]]]])

(defn ->NotificationsList [args]
  (let [schema (g/schema list-notifications-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to list-notifications" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket]} (extract-parse-values parsed)]
        (cond-> {:op     ::NotificationsList
                 :bucket bucket}
          clientable (assoc :storage clientable))))))

(defmethod execute! ::NotificationsList [{:keys [storage bucket]}]
  (let [client (client storage)]
    (.listNotifications client bucket)))

#!----------------------------------------------------------------------------------------------------------------------
#! ::NotificationGet

(def ^:private get-notification-args-schema
  [:altn
   [:arity-2 [:catn [:bucket string?] [:notificationId string?]]]
   [:arity-3 [:catn [:clientable ::clientable] [:bucket string?] [:notificationId string?]]]])

(defn ->NotificationGet [args]
  (let [schema (g/schema get-notification-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to get-notification" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket notificationId]} (extract-parse-values parsed)]
        (cond-> {:op             ::NotificationGet
                 :bucket         bucket
                 :notificationId notificationId}
          clientable (assoc :storage clientable))))))

(defmethod execute! ::NotificationGet [{:keys [storage bucket notificationId]}]
  (let [client (client storage)]
    (.getNotification client bucket notificationId)))

#!----------------------------------------------------------------------------------------------------------------------
#! ::NotificationCreate

(def ^:private create-notification-args-schema
  [:altn
   [:arity-2 [:catn [:bucket string?] [:notificationInfo :gcp.storage/NotificationInfo]]]
   [:arity-3 [:catn [:clientable ::clientable] [:bucket string?] [:notificationInfo :gcp.storage/NotificationInfo]]]])

(defn ->NotificationCreate [args]
  (let [schema (g/schema create-notification-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to create-notification" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket notificationInfo]} (extract-parse-values parsed)]
        (cond-> {:op               ::NotificationCreate
                 :bucket           bucket
                 :notificationInfo notificationInfo}
          clientable (assoc :storage clientable))))))

(defmethod execute! ::NotificationCreate [{:keys [storage bucket notificationInfo]}]
  (let [client (client storage)
        info (NotificationInfo/from-edn notificationInfo)]
    (.createNotification client bucket info)))

#!----------------------------------------------------------------------------------------------------------------------
#! ::NotificationDelete

(def ^:private delete-notification-args-schema
  [:altn
   [:arity-2 [:catn [:bucket string?] [:notificationId string?]]]
   [:arity-3 [:catn [:clientable ::clientable] [:bucket string?] [:notificationId string?]]]])

(defn ->NotificationDelete [args]
  (let [schema (g/schema delete-notification-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to delete-notification" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket notificationId]} (extract-parse-values parsed)]
        (cond-> {:op             ::NotificationDelete
                 :bucket         bucket
                 :notificationId notificationId}
          clientable (assoc :storage clientable))))))

(defmethod execute! ::NotificationDelete [{:keys [storage bucket notificationId]}]
  (let [client (client storage)]
    (.deleteNotification client bucket notificationId)))

#!----------------------------------------------------------------------------------------------------------------------
#! ::IamPolicyGet

(def ^:private get-iam-policy-args-schema
  [:altn
   [:arity-1 [:catn [:bucket string?]]]
   [:arity-2 [:altn
              [:bucket-opts [:catn [:bucket string?] [:opts :gcp.storage/Storage.BucketSourceOption]]]
              [:client-bucket [:catn [:clientable ::clientable] [:bucket string?]]]]]
   [:arity-3 [:catn [:clientable ::clientable] [:bucket string?] [:opts :gcp.storage/Storage.BucketSourceOption]]]])

(defn ->IamPolicyGet [args]
  (let [schema (g/schema get-iam-policy-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to get-iam-policy" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket opts]} (extract-parse-values parsed)]
        (cond-> {:op     ::IamPolicyGet
                 :bucket bucket}
          clientable (assoc :storage clientable)
          opts       (assoc :opts opts))))))

(defmethod execute! ::IamPolicyGet [{:keys [storage bucket opts]}]
  (let [client (client storage)
        opts (S/BucketSourceOption-Array-from-edn opts)]
    (.getIamPolicy client bucket opts)))

#!----------------------------------------------------------------------------------------------------------------------
#! ::IamPolicySet

(def ^:private set-iam-policy-args-schema
  [:altn
   [:arity-2 [:catn [:bucket string?] [:policy :gcp.foreign.com.google.cloud/Policy]]]
   [:arity-3 [:altn
              [:bucket-policy-opts   [:catn [:bucket string?] [:policy :gcp.foreign.com.google.cloud/Policy] [:opts :gcp.storage/Storage.BucketSourceOption]]]
              [:client-bucket-policy [:catn [:clientable ::clientable] [:bucket string?] [:policy :gcp.foreign.com.google.cloud/Policy]]]]]
   [:arity-4 [:catn [:clientable ::clientable] [:bucket string?] [:policy :gcp.foreign.com.google.cloud/Policy] [:opts :gcp.storage/Storage.BucketSourceOption]]]])

(defn ->IamPolicySet [args]
  (let [schema (g/schema set-iam-policy-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to set-iam-policy" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket policy opts]} (extract-parse-values parsed)]
        (cond-> {:op     ::IamPolicySet
                 :bucket bucket
                 :policy policy}
          clientable (assoc :storage clientable)
          opts       (assoc :opts opts))))))

(defmethod execute! ::IamPolicySet [{:keys [storage bucket policy opts]}]
  (let [client (client storage)
        opts (S/BucketSourceOption-Array-from-edn opts)]
    (.setIamPolicy client bucket ^com.google.cloud.Policy policy opts)))

#!----------------------------------------------------------------------------------------------------------------------
#! ::IamPermissionsTest

(def ^:private test-iam-permissions-args-schema
  [:altn
   [:arity-2 [:catn [:bucket string?] [:permissions [:sequential string?]]]]
   [:arity-3 [:altn
              [:bucket-perms-opts   [:catn [:bucket string?] [:permissions [:sequential string?]] [:opts :gcp.storage/Storage.BucketSourceOption]]]
              [:client-bucket-perms [:catn [:clientable ::clientable] [:bucket string?] [:permissions [:sequential string?]]]]]]
   [:arity-4 [:catn [:clientable ::clientable] [:bucket string?] [:permissions [:sequential string?]] [:opts :gcp.storage/Storage.BucketSourceOption]]]])

(defn ->IamPermissionsTest [args]
  (let [schema (g/schema test-iam-permissions-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to test-iam-permissions" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable bucket permissions opts]} (extract-parse-values parsed)]
        (cond-> {:op          ::IamPermissionsTest
                 :bucket      bucket
                 :permissions permissions}
          clientable (assoc :storage clientable)
          opts       (assoc :opts opts))))))

(defmethod execute! ::IamPermissionsTest [{:keys [storage bucket permissions opts]}]
  (let [client (client storage)
        opts (S/BucketSourceOption-Array-from-edn opts)]
    (into [] (.testIamPermissions client bucket permissions opts))))

#!----------------------------------------------------------------------------------------------------------------------

#_{:name "getServiceAccount",
 :returnType com.google.cloud.storage.ServiceAccount,
 :parameters [{:name "projectId", :type java.lang.String}]}

#!----------------------------------------------------------------------------------------------------------------------

#_{:name "compose",
 :returnType com.google.cloud.storage.Blob,
 :parameters [{:name "composeRequest", :type com.google.cloud.storage.Storage.ComposeRequest}]}

#!----------------------------------------------------------------------------------------------------------------------

#_{:name "copy",
 :returnType com.google.cloud.storage.CopyWriter,
 :parameters [{:name "copyRequest", :type com.google.cloud.storage.Storage.CopyRequest}]}

#!----------------------------------------------------------------------------------------------------------------------

#_ #_
{:name "reader",
 :returnType com.google.cloud.ReadChannel,
 :parameters [{:name "bucket", :type java.lang.String}
              {:name "blob", :type java.lang.String}
              {:name "options", :type [:array com.google.cloud.storage.Storage.BlobSourceOption], :varArgs? true}]}
{:name "reader",
 :returnType com.google.cloud.ReadChannel,
 :parameters [{:name "blob", :type com.google.cloud.storage.BlobId}
              {:name "options", :type [:array com.google.cloud.storage.Storage.BlobSourceOption], :varArgs? true}]}
#!----------------------------------------------------------------------------------------------------------------------
#_ #_

{:name "writer",
 :returnType com.google.cloud.WriteChannel,
 :parameters [{:name "blobInfo", :type com.google.cloud.storage.BlobInfo}
              {:name "options", :type [:array com.google.cloud.storage.Storage.BlobWriteOption], :varArgs? true}]}
{:name "writer", :returnType com.google.cloud.WriteChannel, :parameters [{:name "signedURL", :type java.net.URL}]}

#!----------------------------------------------------------------------------------------------------------------------

#_
{:name "blobAppendableUpload",
 :returnType com.google.cloud.storage.BlobAppendableUpload,
 :parameters [{:name "blobInfo", :type com.google.cloud.storage.BlobInfo}
              {:name "uploadConfig", :type com.google.cloud.storage.BlobAppendableUploadConfig}
              {:name "options", :type [:array com.google.cloud.storage.Storage.BlobWriteOption], :varArgs? true}]}

#!----------------------------------------------------------------------------------------------------------------------

#_{:name "batch", :returnType com.google.cloud.storage.StorageBatch, :parameters []}
#!----------------------------------------------------------------------------------------------------------------------

#_{:name "blobReadSession",
   :returnType [com.google.api.core.ApiFuture com.google.cloud.storage.BlobReadSession],
   :parameters [{:name "id", :type com.google.cloud.storage.BlobId}
                {:name "options", :type [:array com.google.cloud.storage.Storage.BlobSourceOption]}]}

#!----------------------------------------------------------------------------------------------------------------------

#_
{:name "blobWriteSession",
 :returnType com.google.cloud.storage.BlobWriteSession,
 :parameters [{:name "blobInfo", :type com.google.cloud.storage.BlobInfo}
              {:name "options", :type [:array com.google.cloud.storage.Storage.BlobWriteOption], :varArgs? true}]}

#!----------------------------------------------------------------------------------------------------------------------

(comment
{:name "generateSignedPostPolicyV4",
 :returnType com.google.cloud.storage.PostPolicyV4,
 :parameters [{:name "blobInfo", :type com.google.cloud.storage.BlobInfo}
              {:name "duration", :type long}
              {:name "unit", :type java.util.concurrent.TimeUnit}
              {:name "fields", :type com.google.cloud.storage.PostPolicyV4.PostFieldsV4}
              {:name "conditions", :type com.google.cloud.storage.PostPolicyV4.PostConditionsV4}
              {:name "options", :type [:array com.google.cloud.storage.Storage.PostPolicyV4Option], :varArgs? true}]}
{:name "generateSignedPostPolicyV4",
 :returnType com.google.cloud.storage.PostPolicyV4,
 :parameters [{:name "blobInfo", :type com.google.cloud.storage.BlobInfo}
              {:name "duration", :type long}
              {:name "unit", :type java.util.concurrent.TimeUnit}
              {:name "fields", :type com.google.cloud.storage.PostPolicyV4.PostFieldsV4}
              {:name "options", :type [:array com.google.cloud.storage.Storage.PostPolicyV4Option], :varArgs? true}]}
{:name "generateSignedPostPolicyV4",
 :returnType com.google.cloud.storage.PostPolicyV4,
 :parameters [{:name "blobInfo", :type com.google.cloud.storage.BlobInfo}
              {:name "duration", :type long}
              {:name "unit", :type java.util.concurrent.TimeUnit}
              {:name "conditions", :type com.google.cloud.storage.PostPolicyV4.PostConditionsV4}
              {:name "options", :type [:array com.google.cloud.storage.Storage.PostPolicyV4Option], :varArgs? true}]}
{:name "generateSignedPostPolicyV4",
 :returnType com.google.cloud.storage.PostPolicyV4,
 :parameters [{:name "blobInfo", :type com.google.cloud.storage.BlobInfo}
              {:name "duration", :type long}
              {:name "unit", :type java.util.concurrent.TimeUnit}
              {:name "options", :type [:array com.google.cloud.storage.Storage.PostPolicyV4Option], :varArgs? true}]}
  )