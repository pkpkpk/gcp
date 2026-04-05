(ns gcp.storage
  (:require [gcp.storage.core :as gsc]))

(def client gsc/client)

#!----------------------------------------------------------------------------------------------------------------------
#! https://docs.cloud.google.com/storage/docs/buckets

(defn list-buckets [& args]
  (gsc/execute! (gsc/->BucketList (vec args))))

(defn get-bucket [& args]
  (gsc/execute! (gsc/->BucketGet (vec args))))

(defn create-bucket [& args]
  (gsc/execute! (gsc/->BucketCreate (vec args))))

(defn update-bucket [& args]
  (gsc/execute! (gsc/->BucketUpdate (vec args))))

(defn delete-bucket [& args]
  (gsc/execute! (gsc/->BucketDelete (vec args))))

(defn lock-retention-policy [& args]
  (gsc/execute! (gsc/->LockRetentionPolicy (vec args))))

#!----------------------------------------------------------------------------------------------------------------------
#! https://docs.cloud.google.com/storage/docs/objects

(defn list-blobs [& args]
  (gsc/execute! (gsc/->BlobList (vec args))))

(defn get-blob [& args]
  (gsc/execute! (gsc/->BlobGet (vec args))))

(defn get-many-blobs [& args]
  (gsc/execute! (gsc/->BlobGetMany (vec args))))

(defn create-blob [& args]
  (gsc/execute! (gsc/->BlobCreate (vec args))))

(defn update-blob [& args]
  (gsc/execute! (gsc/->BlobUpdate (vec args))))

(defn update-many-blobs [& args]
  (gsc/execute! (gsc/->BlobUpdateMany (vec args))))

(defn delete-blob [& args]
  (gsc/execute! (gsc/->BlobDelete (vec args))))

(defn delete-many-blobs [& args]
  (gsc/execute! (gsc/->BlobDeleteMany (vec args))))

(defn read-blob [& args]
  (gsc/execute! (gsc/->BlobRead (vec args))))

(defn move-blob [& args]
  (gsc/execute! (gsc/->BlobMove (vec args))))

(defn download-blob [& args]
  (gsc/execute! (gsc/->BlobDownload (vec args))))

(defn restore-blob [& args]
  (gsc/execute! (gsc/->BlobRestore (vec args))))

(defn sign-blob-url [& args]
  (gsc/execute! (gsc/->BlobSignUrl (vec args))))

#!----------------------------------------------------------------------------------------------------------------------
#! Acl https://docs.cloud.google.com/storage/docs/access-control/lists

(defn list-acls [& args]
  (gsc/execute! (gsc/->AclList (vec args))))

(defn get-acl [& args]
  (gsc/execute! (gsc/->AclGet (vec args))))

(defn create-acl [& args]
  (gsc/execute! (gsc/->AclCreate (vec args))))

(defn update-acl [& args]
  (gsc/execute! (gsc/->AclUpdate (vec args))))

(defn delete-acl [& args]
  (gsc/execute! (gsc/->AclDelete (vec args))))

(defn list-default-acls [& args]
  (gsc/execute! (gsc/->DefaultAclList (vec args))))

(defn get-default-acl [& args]
  (gsc/execute! (gsc/->DefaultAclGet (vec args))))

(defn create-default-acl [& args]
  (gsc/execute! (gsc/->DefaultAclCreate (vec args))))

(defn update-default-acl [& args]
  (gsc/execute! (gsc/->DefaultAclUpdate (vec args))))

(defn delete-default-acl [& args]
  (gsc/execute! (gsc/->DefaultAclDelete (vec args))))

#!----------------------------------------------------------------------------------------------------------------------
#! HmacKeys https://docs.cloud.google.com/storage/docs/authentication/hmackeys

(defn list-hmac-keys [& args]
  (gsc/execute! (gsc/->HmacKeysList (vec args))))

(defn get-hmac-key [& args]
  (gsc/execute! (gsc/->HmacKeyGet (vec args))))

(defn create-hmac-key [& args]
  (gsc/execute! (gsc/->HmacKeyCreate (vec args))))

(defn update-hmac-key [& args]
  (gsc/execute! (gsc/->HmacKeyUpdate (vec args))))

(defn delete-hmac-key [& args]
  (gsc/execute! (gsc/->HmacKeyDelete (vec args))))

#!----------------------------------------------------------------------------------------------------------------------
#! Notifications https://docs.cloud.google.com/storage/docs/reporting-changes

(defn list-notifications [& args]
  (gsc/execute! (gsc/->NotificationsList (vec args))))

(defn get-notification [& args]
  (gsc/execute! (gsc/->NotificationGet (vec args))))

(defn create-notification [& args]
  (gsc/execute! (gsc/->NotificationCreate (vec args))))

(defn delete-notification [& args]
  (gsc/execute! (gsc/->NotificationDelete (vec args))))

#!----------------------------------------------------------------------------------------------------------------------
#! IAM https://docs.cloud.google.com/storage/docs/access-control/iam-roles

(defn get-iam-policy [& args]
  (gsc/execute! (gsc/->IamPolicyGet (vec args))))

(defn set-iam-policy [& args]
  (gsc/execute! (gsc/->IamPolicySet (vec args))))

(defn test-iam-permissions [& args]
  (gsc/execute! (gsc/->IamPermissionsTest (vec args))))

#!----------------------------------------------------------------------------------------------------------------------




