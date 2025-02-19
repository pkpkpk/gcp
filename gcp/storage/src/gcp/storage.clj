(ns gcp.storage
  (:require [gcp.global :as g]
            gcp.storage.v2
            [gcp.storage.v2.Blob :as Blob]
            [gcp.storage.v2.BlobId :as BlobId]
            [gcp.storage.v2.Bucket :as Bucket]
            [gcp.storage.v2.BucketInfo :as BucketInfo]
            [gcp.storage.v2.Storage.BlobListOption  :as BlobLO]
            [gcp.storage.v2.Storage.BlobSourceOption :as BlobSourceOption]
            [gcp.storage.v2.Storage.BucketGetOption :as BucketGetOption]
            [gcp.storage.v2.Storage.BucketListOption :as BucketListOption]
            [gcp.storage.v2.Storage.BucketTargetOption :as BucketTargetOption])
  (:import [com.google.cloud.storage Storage Storage$BlobListOption Storage$BlobSourceOption Storage$BucketGetOption Storage$BucketListOption Storage$BucketTargetOption]))

(defonce ^:dynamic *client* nil)

(defn ^Storage client
  ([] (client nil))
  ([arg]
   (or *client*
       (do
         (g/strict! :storage.synth/clientable arg)
         (if (instance? Storage arg)
           arg
           (g/client :storage.synth/client arg))))))

#!-----------------------------------------------------------------------------
#! Bucket Operations

(defn list-buckets
  ([] (list-buckets nil))
  ([{:keys [storage options] :as arg}]
   (g/coerce :storage.synth/BucketList arg)
   (let [opts (into-array Storage$BucketListOption (map BucketListOption/from-edn options))]
     (map Bucket/to-edn (seq (.iterateAll (.list (client storage) ^Storage$BucketListOption/1 opts)))))))

(defn get-bucket [arg]
  (if (string? arg)
    (get-bucket {:bucket arg})
    (if (g/valid? :storage/BucketInfo arg)
      (get-bucket {:bucket (:name arg)})
      (let [{:keys [storage bucket options]} (g/coerce :storage.synth/BucketGet arg)
            opts (into-array Storage$BucketGetOption (map BucketGetOption/from-edn options))]
        (Bucket/to-edn (.get (client storage) ^String bucket ^Storage$BucketGetOption/1 opts))))))

(defn create-bucket
  ([arg]
   (if (string? arg)
     (create-bucket {:bucketInfo {:name arg}})
     (if (g/valid? :storage/BucketInfo arg)
       (create-bucket {:bucketInfo arg})
       (let [{:keys [storage bucketInfo options]} (g/coerce :storage.synth/BucketCreate arg)
             opts ^Storage$BucketTargetOption/1 (into-array Storage$BucketTargetOption (map BucketTargetOption/from-edn options))]
         (.create (client storage) (BucketInfo/from-edn bucketInfo) opts))))))

;delete(String bucket, Storage.BucketSourceOption[] options)
;listNotifications(String bucket)
;lockRetentionPolicy(BucketInfo bucket, Storage.BucketTargetOption[] options)
;setIamPolicy(String bucket, Policy policy, Storage.BucketSourceOption[] options)
;update(BucketInfo bucketInfo, Storage.BucketTargetOption[] options)

#!-----------------------------------------------------------------------------
#! Blob Operations

(defn list-blobs [arg]
  (if (string? arg)
    (list-blobs {:bucket arg})
    (if (g/valid? :storage/BucketInfo arg)
      (list-blobs (assoc arg :bucket (:name arg)))
      (let [{:keys [storage bucket options]} (g/coerce :storage.synth/BlobList arg)
            opts (into-array Storage$BlobListOption (map BlobLO/from-edn options))]
        (map Blob/to-edn (seq (.iterateAll (.list (client storage) bucket ^Storage$BlobListOption/1 opts))))))))

(defn delete-blob
  "blobId|seq<BlobId>|storage.synth.BlobDelete -> seq<bool>"
  ([arg]
   (if (g/valid? :storage/BlobId arg)
     (delete-blob {:blobs [arg]})
     (if (g/valid? [:sequential :storage/BlobId] arg)
       (delete-blob {:blobs arg})
       (let [{:keys [storage blobs]} (g/coerce :storage.synth/BlobDelete arg)]
         (.delete (client storage) ^Iterable (map BlobId/from-edn blobs))))))
  ([blobId & options]
   (throw (Exception. "unimplemented"))))

;batch()
;blobWriteSession(BlobInfo blobInfo, Storage.BlobWriteOption[] options)
;close()
;compose(Storage.ComposeRequest composeRequest)
;copy(Storage.CopyRequest copyRequest)
;create(BlobInfo blobInfo, byte[] content, Storage.BlobTargetOption[] options)
;create(BlobInfo blobInfo, byte[] content, int offset, int length, Storage.BlobTargetOption[] options)
;create(BlobInfo blobInfo, Storage.BlobTargetOption[] options)
;create(BlobInfo blobInfo, InputStream content, Storage.BlobWriteOption[] options) (deprecated)
;downloadTo(BlobId blob, OutputStream outputStream, Storage.BlobSourceOption[] options)
;downloadTo(BlobId blob, Path path, Storage.BlobSourceOption[] options)
;get(BlobId blob)
;get(BlobId blob, Storage.BlobGetOption[] options)
;get(BlobId[] blobIds)
;get(Iterable<BlobId> blobIds)

;readAllBytes(BlobId blob, Storage.BlobSourceOption[] options)
;readAllBytes(String bucket, String blob, Storage.BlobSourceOption[] options)
(defn read-all-bytes
  ([arg]
   (if (g/valid? :storage/BlobId arg)
     (read-all-bytes {:blobId arg})
     (let [{:keys [storage blobId options]} (g/coerce :storage.synth/ReadAllBytes arg)
           opts ^Storage$BlobSourceOption/1 (into-array Storage$BlobSourceOption (map BlobSourceOption/from-edn options))]
       (.readAllBytes (client storage) (BlobId/from-edn blobId) opts))))
  ([arg & more]
   (if (string? arg)
     (if (string? (first more))
       (read-all-bytes {:blobId {:name (first more)
                                 :bucket arg}
                        :options (rest more)})
       (throw (Exception. "unimplemented")))
     (throw (Exception. "unimplemented")))))

;reader(BlobId blob, Storage.BlobSourceOption[] options)
;reader(String bucket, String blob, Storage.BlobSourceOption[] options)


;restore(BlobId blob, Storage.BlobRestoreOption[] options)
;update(BlobInfo blobInfo)
;update(BlobInfo blobInfo, Storage.BlobTargetOption[] options)
;update(BlobInfo[] blobInfos)
;update(Iterable<BlobInfo> blobInfos)
;writer(BlobInfo blobInfo, Storage.BlobWriteOption[] options)
;writer(URL signedURL)

#! ACL Operations
;createAcl(BlobId blob, Acl acl)
;createAcl(String bucket, Acl acl)
;createAcl(String bucket, Acl acl, Storage.BucketSourceOption[] options)
;createDefaultAcl(String bucket, Acl.Entity entity)
;deleteAcl(BlobId blob, Acl.Entity entity)
;deleteAcl(String bucket, Acl.Entity entity)
;deleteAcl(String bucket, Acl.Entity entity, Storage.BucketSourceOption[] options)
;deleteDefaultAcl(String bucket, Acl.Entity entity)
;getAcl(BlobId blob, Acl.Entity entity)
;getAcl(String bucket, Acl.Entity entity)
;getAcl(String bucket, Acl.Entity entity, Storage.BucketSourceOption[] options)
;getDefaultAcl(String bucket, Acl.Entity entity)
;listAcls(BlobId blob)
;listAcls(String bucket)
;listAcls(String bucket, Storage.BucketSourceOption[] options)
;listDefaultAcls(String bucket)
;updateAcl(BlobId blob, Acl acl)
;updateAcl(String bucket, Acl acl)
;updateAcl(String bucket, Acl acl, Storage.BucketSourceOption[] options)
;updateDefaultAcl(String bucket, Acl acl)

#! HMAC Key Operations
;createHmacKey(ServiceAccount serviceAccount, Storage.CreateHmacKeyOption[] options)
;deleteHmacKey(HmacKey.HmacKeyMetadata hmacKeyMetadata, Storage.DeleteHmacKeyOption[] options)
;getHmacKey(String accessId, Storage.GetHmacKeyOption[] options)
;listHmacKeys(Storage.ListHmacKeysOption[] options)
;updateHmacKeyState(HmacKey.HmacKeyMetadata hmacKeyMetadata, HmacKey.HmacKeyState state, Storage.UpdateHmacKeyOption[] options)

#! IAM Operations
;getIamPolicy(String bucket, Storage.BucketSourceOption[] options)
;setIamPolicy(String bucket, Policy policy, Storage.BucketSourceOption[] options)
;testIamPermissions(String bucket, List<String> permissions, Storage.BucketSourceOption[] options)

#! Notifications
;createNotification(String bucket, NotificationInfo notificationInfo)
;deleteNotification(String bucket, String notificationId)
;getNotification(String bucket, String notificationId)
;listNotifications(String bucket)

#! Retention Policies
;lockRetentionPolicy(BucketInfo bucket, Storage.BucketTargetOption[] options)

#! Signed URLs
;signUrl(BlobInfo blobInfo, long duration, TimeUnit unit, Storage.SignUrlOption[] options)

#! Signed Policies
;generateSignedPostPolicyV4(BlobInfo blobInfo, long duration, TimeUnit unit, PostPolicyV4.PostConditionsV4 conditions, Storage.PostPolicyV4Option[] options)
;generateSignedPostPolicyV4(BlobInfo blobInfo, long duration, TimeUnit unit, PostPolicyV4.PostFieldsV4 fields, PostPolicyV4.PostConditionsV4 conditions, Storage.PostPolicyV4Option[] options)
;generateSignedPostPolicyV4(BlobInfo blobInfo, long duration, TimeUnit unit, Storage.PostPolicyV4Option[] options)

#! Other Operations
;createFrom(BlobInfo blobInfo, InputStream content, Storage.BlobWriteOption[] options)
;createFrom(BlobInfo blobInfo, InputStream content, int bufferSize, Storage.BlobWriteOption[] options)
;createFrom(BlobInfo blobInfo, Path path, Storage.BlobWriteOption[] options)
;createFrom(BlobInfo blobInfo, Path path, int bufferSize, Storage.BlobWriteOption[] options)
