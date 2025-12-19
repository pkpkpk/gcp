(ns gcp.storage
  (:require [gcp.global :as g]
            [gcp.storage.v2.Blob :as Blob]
            [gcp.storage.v2.BlobId :as BlobId]
            [gcp.storage.v2.BlobInfo :as BlobInfo]
            [gcp.storage.v2.Bucket :as Bucket]
            [gcp.storage.v2.BucketInfo :as BucketInfo]
            [gcp.storage.v2.Storage :as S]
            [gcp.storage.v2.StorageOptions :as StorageOptions]
            [gcp.storage.v2.synth])
  (:import [com.google.cloud.storage Storage Storage$BlobListOption Storage$BlobSourceOption Storage$BlobTargetOption Storage$BucketGetOption Storage$BucketListOption Storage$BucketTargetOption]))

(def synthetic-schemas
  {:gcp.storage.v2.synth/client               :any
   :gcp.storage.v2.synth/clientable           [:maybe
                                               [:or
                                                :gcp.storage.v2/StorageOptions
                                                :gcp.storage.v2.synth/client
                                                [:map [:storage [:or :gcp.storage.v2/StorageOptions :gcp.storage.v2.synth/client]]]]]
   :gcp.storage.v2.synth/StorageRetryStrategy :any
   :gcp.storage.v2.synth/BucketList           [:maybe
                                               [:map {:closed true}
                                                [:storage {:optional true} :gcp.storage.v2.synth/clientable]
                                                [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BucketListOption]]]]
   :gcp.storage.v2.synth/BucketGet            [:map {:closed true}
                                               [:storage {:optional true} :gcp.storage.v2.synth/clientable]
                                               [:bucket :string]
                                               [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BucketGetOption]]]
   :gcp.storage.v2.synth/BucketCreate         [:map {:closed true}
                                               [:storage {:optional true} :gcp.storage.v2.synth/clientable]
                                               [:bucketInfo :gcp.storage.v2/BucketInfo]
                                               [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BucketTargetOption]]]
   :gcp.storage.v2.synth/BlobList             [:map {:closed true}
                                               [:storage {:optional true} :gcp.storage.v2.synth/clientable]
                                               [:bucket :string]
                                               [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BlobListOption]]]
   :gcp.storage.v2.synth/BlobDelete           [:map {:closed true}
                                               [:storage {:optional true} :gcp.storage.v2.synth/clientable]
                                               [:blobs [:sequential :gcp.storage.v2/BlobId]]
                                               [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BlobSourceOption]]]
   :gcp.storage.v2.synth/BlobCreate           [:map {:closed true}
                                               [:storage {:optional true} :gcp.storage.v2.synth/clientable]
                                               [:blobInfo {:optional false} :gcp.storage.v2/BlobInfo]
                                               [:content {:optional true} 'bytes?]
                                               [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BlobSourceOption]]]
   :gcp.storage.v2.synth/ReadAllBytes         [:map {:closed true}
                                               [:storage {:optional true} :gcp.storage.v2.synth/clientable]
                                               [:blobId :gcp.storage.v2/BlobId]
                                               [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BlobSourceOption]]]})

(g/include-schema-registry! (with-meta synthetic-schemas {:gcp.global/name (str *ns*)}))

(defonce ^:dynamic *client* nil)
(defonce *clients (atom {}))

(defn ^Storage client
  ([] (client nil))
  ([arg]
   (or *client*
       (do
         (g/strict! :gcp.storage.v2.synth/clientable arg)
         (if (instance? Storage arg)
           arg
           (or (get @*clients arg)
               (let [client (StorageOptions/get-service arg)]
                 (swap! *clients assoc arg client)
                 client)))))))

#!-----------------------------------------------------------------------------
#! Bucket Operations

(defn list-buckets
  ([] (list-buckets nil))
  ([{:keys [storage options] :as arg}]
   (g/coerce :gcp.storage.v2.synth/BucketList arg)
   (let [opts (into-array Storage$BucketListOption (map S/BucketListOption:from-edn options))]
     (map Bucket/to-edn (seq (.iterateAll (.list (client storage) ^Storage$BucketListOption/1 opts)))))))

(defn get-bucket [arg]
  (if (string? arg)
    (get-bucket {:bucket arg})
    (if (g/valid? :gcp.storage.v2/BucketInfo arg)
      (get-bucket {:bucket (:name arg)})
      (let [{:keys [storage bucket options]} (g/coerce :gcp.storage.v2.synth/BucketGet arg)
            opts (into-array Storage$BucketGetOption (map S/BucketGetOption:from-edn options))]
        (Bucket/to-edn (.get (client storage) ^String bucket ^Storage$BucketGetOption/1 opts))))))

(defn create-bucket
  ([arg]
   (if (string? arg)
     (create-bucket {:bucketInfo {:name arg}})
     (if (g/valid? :gcp.storage.v2/BucketInfo arg)
       (create-bucket {:bucketInfo arg})
       (let [{:keys [storage bucketInfo options]} (g/coerce :gcp.storage.v2.synth/BucketCreate arg)
             opts ^Storage$BucketTargetOption/1 (into-array Storage$BucketTargetOption (map S/BucketTargetOption:from-edn options))]
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
    (if (g/valid? :gcp.storage.v2/BucketInfo arg)
      (list-blobs (assoc arg :bucket (:name arg)))
      (let [{:keys [storage bucket options]} (g/coerce :gcp.storage.v2.synth/BlobList arg)
            opts (into-array Storage$BlobListOption (map S/BlobListOption:from-edn options))]
        (map Blob/to-edn (seq (.iterateAll (.list (client storage) bucket ^Storage$BlobListOption/1 opts))))))))

(defn delete-blob
  "blobId|seq<BlobId>|storage.synth.BlobDelete -> seq<bool>"
  ([arg]
   (if (g/valid? :gcp.storage.v2/BlobId arg)
     (delete-blob {:blobs [arg]})
     (if (g/valid? [:sequential :gcp.storage.v2/BlobId] arg)
       (delete-blob {:blobs arg})
       (let [{:keys [storage blobs]} (g/coerce :gcp.storage.v2.synth/BlobDelete arg)]
         (.delete (client storage) ^Iterable (map BlobId/from-edn blobs))))))
  ([arg0 arg1]
   (if (string? arg0)
     (if (string? arg1)
       (delete-blob {:blobs [{:bucket arg0 :name arg1}]})
       (throw (Exception. "unimplemented")))
     (throw (Exception. "unimplemented")))))

;batch()
;blobWriteSession(BlobInfo blobInfo, Storage.BlobWriteOption[] options)
;close()
;compose(Storage.ComposeRequest composeRequest)
;copy(Storage.CopyRequest copyRequest)

(defn create-blob
  ([arg]
   (if (g/valid? :gcp.storage.v2/BlobId arg)
     (create-blob {:blobInfo {:blobId arg}})
     (if (g/valid? :gcp.storage.v2/BlobInfo arg)
       (create-blob {:blobInfo arg})
       (let [{:keys [storage blobInfo content options]} (g/coerce :gcp.storage.v2.synth/BlobCreate arg)
             client    (client storage)
             blob-info (BlobInfo/from-edn blobInfo)
             opts      ^Storage$BlobTargetOption/1 (into-array Storage$BlobTargetOption (map S/BlobTargetOption:from-edn options))]
         (if (nil? content)
           (.create client blob-info opts)
           (.create client blob-info ^Byte/1 content opts))))))
  ([arg0 arg1]
   (if (g/valid? :gcp.storage.v2/BlobId arg0)
     (create-blob {:blobInfo {:blobId arg0}
                   :content arg1})
     (if (g/valid? :gcp.storage.v2/BlobInfo arg0)
       (create-blob {:blobInfo arg0
                     :content arg1})
       (if (string? arg0)
         (if (string? arg1)
           (create-blob {:blobInfo {:blobId {:bucket arg0 :name arg1}}})
           (throw (Exception. "unimplemented")))
         (throw (Exception. "unimplemented"))))))
  ([arg0 arg1 arg2]
   (if (string? arg0)
     (if (string? arg1)
       (create-blob {:blobInfo {:blobId {:bucket arg0 :name arg1}} :content arg2})
       (throw (Exception. "unimplemented")))
     (throw (Exception. "unimplemented"))))
  ([arg0 arg1 arg2 & more]
   ;TODO create(BlobInfo blobInfo, byte[] content, int offset, int length, Storage.BlobTargetOption[] options)
   (throw (Exception. "unimplemented"))))


;; createFrom

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
   (if (g/valid? :gcp.storage.v2/BlobId arg)
     (read-all-bytes {:blobId arg})
     (let [{:keys [storage blobId options]} (g/coerce :gcp.storage.v2.synth/ReadAllBytes arg)
           opts ^Storage$BlobSourceOption/1 (into-array Storage$BlobSourceOption (map S/BlobSourceOption:from-edn options))]
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
