(ns gcp.storage.parse-args-tests
  (:require [clojure.test :refer :all]
            [gcp.storage.core :as gsc]))

(def client (gsc/client))

(deftest bucket-test
  (testing "list-buckets (->BucketList)"
    (is (= {:op ::gsc/BucketList :storage nil :opts nil} (gsc/->BucketList [])))
    (is (= {:op ::gsc/BucketList :storage client :opts nil} (gsc/->BucketList [client])))
    (is (= {:op ::gsc/BucketList :storage nil :opts {:pageSize 10}} (gsc/->BucketList [{:pageSize 10}])))
    (is (= {:op ::gsc/BucketList :storage client :opts {:pageSize 10}} (gsc/->BucketList [client {:pageSize 10}]))))

  (testing "get-bucket (->BucketGet)"
    (is (= {:op ::gsc/BucketGet :storage nil :bucket "b" :opts nil} (gsc/->BucketGet ["b"])))
    (is (= {:op ::gsc/BucketGet :storage client :bucket "b" :opts nil} (gsc/->BucketGet [client "b"])))
    (is (= {:op ::gsc/BucketGet :storage nil :bucket "b" :opts {:userProject "p"}} (gsc/->BucketGet ["b" {:userProject "p"}]))))

  (testing "create-bucket (->BucketCreate)"
    (is (= {:op ::gsc/BucketCreate :storage nil :bucketInfo {:name "b"} :opts nil} (gsc/->BucketCreate [{:name "b"}])))
    (is (= {:op ::gsc/BucketCreate :storage client :bucketInfo {:name "b"} :opts nil} (gsc/->BucketCreate [client {:name "b"}])))
    (is (= {:op ::gsc/BucketCreate :storage nil :bucketInfo {:name "b"} :opts {:predefinedAcl "PRIVATE"}} (gsc/->BucketCreate [{:name "b"} {:predefinedAcl "PRIVATE"}]))))

  (testing "update-bucket (->BucketUpdate)"
    (is (= {:op ::gsc/BucketUpdate :storage nil :bucketInfo {:name "b"} :opts nil} (gsc/->BucketUpdate [{:name "b"}])))
    (is (= {:op ::gsc/BucketUpdate :storage client :bucketInfo {:name "b"} :opts nil} (gsc/->BucketUpdate [client {:name "b"}])))
    (is (= {:op ::gsc/BucketUpdate :storage nil :bucketInfo {:name "b"} :opts {:predefinedAcl "PRIVATE"}} (gsc/->BucketUpdate [{:name "b"} {:predefinedAcl "PRIVATE"}]))))

  (testing "delete-bucket (->BucketDelete)"
    (is (= {:op ::gsc/BucketDelete :storage nil :bucket "b" :opts nil} (gsc/->BucketDelete ["b"])))
    (is (= {:op ::gsc/BucketDelete :storage client :bucket "b" :opts nil} (gsc/->BucketDelete [client "b"])))
    (is (= {:op ::gsc/BucketDelete :storage nil :bucket "b" :opts {:userProject "p"}} (gsc/->BucketDelete ["b" {:userProject "p"}]))))

  (testing "lock-retention-policy (->LockRetentionPolicy)"
    (is (= {:op ::gsc/BucketLockRetentionPolicy :bucketInfo {:name "b"}} (gsc/->LockRetentionPolicy [{:name "b"}])))
    (is (= {:op ::gsc/BucketLockRetentionPolicy :storage client :bucketInfo {:name "b"}} (gsc/->LockRetentionPolicy [client {:name "b"}])))
    (is (= {:op ::gsc/BucketLockRetentionPolicy :bucketInfo {:name "b"} :opts {:userProject "p"}} (gsc/->LockRetentionPolicy [{:name "b"} {:userProject "p"}])))))

(def blob-id {:bucket "b" :name "n"})
(def blob-info {:blobId blob-id})

(deftest blob-test
  (testing "list-blobs (->BlobList)"
    (is (= {:op ::gsc/BlobList :storage nil :bucket "b" :opts nil} (gsc/->BlobList ["b"])))
    (is (= {:op ::gsc/BlobList :storage client :bucket "b" :opts nil} (gsc/->BlobList [client "b"])))
    (is (= {:op ::gsc/BlobList :storage nil :bucket "b" :opts {:pageSize 10}} (gsc/->BlobList ["b" {:pageSize 10}])))
    (is (= {:op ::gsc/BlobList :storage client :bucket "b" :opts {:pageSize 10}} (gsc/->BlobList [client "b" {:pageSize 10}]))))

  (testing "get-blob (->BlobGet)"
    (is (= {:op ::gsc/BlobGet :storage nil :blobId blob-id :opts nil} (gsc/->BlobGet [blob-id])))
    (is (= {:op ::gsc/BlobGet :storage nil :blobId blob-id :opts nil} (gsc/->BlobGet ["b" "n"])))
    (is (= {:op ::gsc/BlobGet :storage client :blobId blob-id :opts nil} (gsc/->BlobGet [client blob-id])))
    (is (= {:op ::gsc/BlobGet :storage nil :blobId blob-id :opts {:userProject "p"}} (gsc/->BlobGet [blob-id {:userProject "p"}])))
    (is (= {:op ::gsc/BlobGet :storage client :blobId blob-id :opts nil} (gsc/->BlobGet [client "b" "n"]))))

  (testing "get-blobs (->BlobGetMany)"
    (is (= {:op ::gsc/BlobGetMany :storage nil :blobIds [blob-id]} (gsc/->BlobGetMany [[blob-id]])))
    (is (= {:op ::gsc/BlobGetMany :storage client :blobIds [blob-id]} (gsc/->BlobGetMany [client [blob-id]]))))

  (testing "create-blob (->BlobCreate)"
    (let [content (.getBytes "hello")]
      (is (= {:op ::gsc/BlobCreate :blobInfo blob-info} (gsc/->BlobCreate [blob-info])))
      (is (= {:op ::gsc/BlobCreate :blobInfo blob-info :content content} (gsc/->BlobCreate [blob-info content])))
      (is (= {:op ::gsc/BlobCreate :storage client :blobInfo blob-info :content content} (gsc/->BlobCreate [client blob-info content])))
      (is (= {:op ::gsc/BlobCreate :blobInfo blob-info :content content :offset 0 :length 5} (gsc/->BlobCreate [blob-info content 0 5])))
      (is (= {:op ::gsc/BlobCreate :blobInfo blob-info :opts {:predefinedAcl "PRIVATE"}} (gsc/->BlobCreate [blob-info {:predefinedAcl "PRIVATE"}])))))

  (testing "update-blob (->BlobUpdate)"
    (is (= {:op ::gsc/BlobUpdate :storage nil :blobInfo blob-info :opts nil} (gsc/->BlobUpdate [blob-info])))
    (is (= {:op ::gsc/BlobUpdate :storage client :blobInfo blob-info :opts nil} (gsc/->BlobUpdate [client blob-info])))
    (is (= {:op ::gsc/BlobUpdate :storage nil :blobInfo blob-info :opts {:predefinedAcl "PRIVATE"}} (gsc/->BlobUpdate [blob-info {:predefinedAcl "PRIVATE"}]))))

  (testing "delete-blob (->BlobDelete)"
    (is (= {:op ::gsc/BlobDelete :storage nil :blobId blob-id :opts nil} (gsc/->BlobDelete [blob-id])))
    (is (= {:op ::gsc/BlobDelete :storage client :blobId blob-id :opts nil} (gsc/->BlobDelete [client blob-id])))
    (is (= {:op ::gsc/BlobDelete :storage nil :blobId blob-id :opts {:userProject "p"}} (gsc/->BlobDelete [blob-id {:userProject "p"}]))))

  (testing "read-blob (->BlobRead)"
    (is (= {:op ::gsc/BlobRead :storage nil :blobId blob-id :opts nil} (gsc/->BlobRead [blob-id])))
    (is (= {:op ::gsc/BlobRead :storage client :blobId blob-id :opts nil} (gsc/->BlobRead [client blob-id])))
    (is (= {:op ::gsc/BlobRead :storage nil :blobId blob-id :opts {:userProject "p"}} (gsc/->BlobRead [blob-id {:userProject "p"}]))))

  (testing "download-blob (->BlobDownload)"
    (let [out (java.io.ByteArrayOutputStream.)]
      (is (= {:op ::gsc/BlobDownload :storage nil :blobId blob-id :destination out :opts nil} (gsc/->BlobDownload [blob-id out])))
      (is (= {:op ::gsc/BlobDownload :storage client :blobId blob-id :destination out :opts nil} (gsc/->BlobDownload [client blob-id out])))
      (is (= {:op ::gsc/BlobDownload :storage nil :blobId blob-id :destination out :opts {:userProject "p"}} (gsc/->BlobDownload [blob-id out {:userProject "p"}]))))))

(def acl {:entity {:type "USER" :email "p@example.com"} :role "OWNER"})
(def entity {:type "USER" :email "p@example.com"})

(deftest acl-test
  (testing "list-acls (->AclList)"
    (is (= {:op ::gsc/AclList :bucket "b"} (gsc/->AclList ["b"])))
    (is (= {:op ::gsc/AclList :blobId blob-id} (gsc/->AclList [blob-id])))
    (is (= {:op ::gsc/AclList :storage client :bucket "b"} (gsc/->AclList [client "b"])))
    (is (= {:op ::gsc/AclList :bucket "b" :opts {:userProject "p"}} (gsc/->AclList ["b" {:userProject "p"}]))))
  
  (testing "create-acl (->AclCreate)"
    (is (= {:op ::gsc/AclCreate :bucket "b" :acl acl} (gsc/->AclCreate ["b" acl])))
    (is (= {:op ::gsc/AclCreate :blobId blob-id :acl acl} (gsc/->AclCreate [blob-id acl])))
    (is (= {:op ::gsc/AclCreate :storage client :bucket "b" :acl acl} (gsc/->AclCreate [client "b" acl])))
    (is (= {:op ::gsc/AclCreate :bucket "b" :acl acl :opts {:userProject "p"}} (gsc/->AclCreate ["b" acl {:userProject "p"}]))))

  (testing "get-acl (->AclGet)"
    (is (= {:op ::gsc/AclGet :bucket "b" :entity entity} (gsc/->AclGet ["b" entity])))
    (is (= {:op ::gsc/AclGet :blobId blob-id :entity entity} (gsc/->AclGet [blob-id entity])))
    (is (= {:op ::gsc/AclGet :storage client :bucket "b" :entity entity} (gsc/->AclGet [client "b" entity])))
    (is (= {:op ::gsc/AclGet :bucket "b" :entity entity :opts {:userProject "p"}} (gsc/->AclGet ["b" entity {:userProject "p"}]))))

  (testing "delete-acl (->AclDelete)"
    (is (= {:op ::gsc/AclDelete :bucket "b" :entity entity} (gsc/->AclDelete ["b" entity])))
    (is (= {:op ::gsc/AclDelete :blobId blob-id :entity entity} (gsc/->AclDelete [blob-id entity])))
    (is (= {:op ::gsc/AclDelete :storage client :bucket "b" :entity entity} (gsc/->AclDelete [client "b" entity])))
    (is (= {:op ::gsc/AclDelete :bucket "b" :entity entity :opts {:userProject "p"}} (gsc/->AclDelete ["b" entity {:userProject "p"}]))))

  (testing "update-acl (->AclUpdate)"
    (is (= {:op ::gsc/AclUpdate :bucket "b" :acl acl} (gsc/->AclUpdate ["b" acl])))
    (is (= {:op ::gsc/AclUpdate :blobId blob-id :acl acl} (gsc/->AclUpdate [blob-id acl])))
    (is (= {:op ::gsc/AclUpdate :storage client :bucket "b" :acl acl} (gsc/->AclUpdate [client "b" acl])))
    (is (= {:op ::gsc/AclUpdate :bucket "b" :acl acl :opts {:userProject "p"}} (gsc/->AclUpdate ["b" acl {:userProject "p"}]))))
  
  (testing "default-acl-list (->DefaultAclList)"
    (is (= {:op ::gsc/DefaultAclList :bucket "b"} (gsc/->DefaultAclList ["b"])))
    (is (= {:op ::gsc/DefaultAclList :storage client :bucket "b"} (gsc/->DefaultAclList [client "b"]))))

  (testing "default-acl-get (->DefaultAclGet)"
    (is (= {:op ::gsc/DefaultAclGet :bucket "b" :entity entity} (gsc/->DefaultAclGet ["b" entity])))
    (is (= {:op ::gsc/DefaultAclGet :storage client :bucket "b" :entity entity} (gsc/->DefaultAclGet [client "b" entity]))))

  (testing "default-acl-create (->DefaultAclCreate)"
    (is (= {:op ::gsc/DefaultAclCreate :bucket "b" :acl acl} (gsc/->DefaultAclCreate ["b" acl])))
    (is (= {:op ::gsc/DefaultAclCreate :storage client :bucket "b" :acl acl} (gsc/->DefaultAclCreate [client "b" acl]))))

  (testing "default-acl-update (->DefaultAclUpdate)"
    (is (= {:op ::gsc/DefaultAclUpdate :bucket "b" :acl acl} (gsc/->DefaultAclUpdate ["b" acl])))
    (is (= {:op ::gsc/DefaultAclUpdate :storage client :bucket "b" :acl acl} (gsc/->DefaultAclUpdate [client "b" acl]))))

  (testing "default-acl-delete (->DefaultAclDelete)"
    (is (= {:op ::gsc/DefaultAclDelete :bucket "b" :entity entity} (gsc/->DefaultAclDelete ["b" entity])))
    (is (= {:op ::gsc/DefaultAclDelete :storage client :bucket "b" :entity entity} (gsc/->DefaultAclDelete [client "b" entity])))))

(def hmac-meta {:serviceAccount {:email "sa@example.com"} :accessId "aid" :state "ACTIVE"})
(def hmac-state "INACTIVE")
(def service-account {:email "sa@example.com"})

(deftest hmac-keys-test
  (testing "list-hmac-keys (->HmacKeysList)"
    (is (= {:op ::gsc/HmacKeysList} (gsc/->HmacKeysList [])))
    (is (= {:op ::gsc/HmacKeysList :storage client} (gsc/->HmacKeysList [client])))
    (is (= {:op ::gsc/HmacKeysList :opts {:projectId "p"}} (gsc/->HmacKeysList [{:projectId "p"}])))
    (is (= {:op ::gsc/HmacKeysList :storage client :opts {:projectId "p"}} (gsc/->HmacKeysList [client {:projectId "p"}]))))

  (testing "get-hmac-key (->HmacKeyGet)"
    (is (= {:op ::gsc/HmacKeyGet :accessId "aid"} (gsc/->HmacKeyGet ["aid"])))
    (is (= {:op ::gsc/HmacKeyGet :storage client :accessId "aid"} (gsc/->HmacKeyGet [client "aid"])))
    (is (= {:op ::gsc/HmacKeyGet :accessId "aid" :opts {:userProject "p"}} (gsc/->HmacKeyGet ["aid" {:userProject "p"}])))
    (is (= {:op ::gsc/HmacKeyGet :storage client :accessId "aid" :opts {:userProject "p"}} (gsc/->HmacKeyGet [client "aid" {:userProject "p"}]))))

  (testing "create-hmac-key (->HmacKeyCreate)"
    (is (= {:op ::gsc/HmacKeyCreate :serviceAccount service-account} (gsc/->HmacKeyCreate [service-account])))
    (is (= {:op ::gsc/HmacKeyCreate :storage client :serviceAccount service-account} (gsc/->HmacKeyCreate [client service-account])))
    (is (= {:op ::gsc/HmacKeyCreate :serviceAccount service-account :opts {:userProject "p"}} (gsc/->HmacKeyCreate [service-account {:userProject "p"}])))
    (is (= {:op ::gsc/HmacKeyCreate :storage client :serviceAccount service-account :opts {:userProject "p"}} (gsc/->HmacKeyCreate [client service-account {:userProject "p"}]))))

  (testing "update-hmac-key (->HmacKeyUpdate)"
    (is (= {:op ::gsc/HmacKeyUpdate :hmacKeyMetadata hmac-meta :state hmac-state} (gsc/->HmacKeyUpdate [hmac-meta hmac-state])))
    (is (= {:op ::gsc/HmacKeyUpdate :storage client :hmacKeyMetadata hmac-meta :state hmac-state} (gsc/->HmacKeyUpdate [client hmac-meta hmac-state])))
    (is (= {:op ::gsc/HmacKeyUpdate :hmacKeyMetadata hmac-meta :state hmac-state :opts {:userProject "p"}} (gsc/->HmacKeyUpdate [hmac-meta hmac-state {:userProject "p"}])))
    (is (= {:op ::gsc/HmacKeyUpdate :storage client :hmacKeyMetadata hmac-meta :state hmac-state :opts {:userProject "p"}} (gsc/->HmacKeyUpdate [client hmac-meta hmac-state {:userProject "p"}]))))

  (testing "delete-hmac-key (->HmacKeyDelete)"
    (is (= {:op ::gsc/HmacKeyDelete :hmacKeyMetadata hmac-meta} (gsc/->HmacKeyDelete [hmac-meta])))
    (is (= {:op ::gsc/HmacKeyDelete :storage client :hmacKeyMetadata hmac-meta} (gsc/->HmacKeyDelete [client hmac-meta])))
    (is (= {:op ::gsc/HmacKeyDelete :hmacKeyMetadata hmac-meta :opts {:userProject "p"}} (gsc/->HmacKeyDelete [hmac-meta {:userProject "p"}])))
    (is (= {:op ::gsc/HmacKeyDelete :storage client :hmacKeyMetadata hmac-meta :opts {:userProject "p"}} (gsc/->HmacKeyDelete [client hmac-meta {:userProject "p"}])))))

(def notif-info {:topic "t" :payloadFormat "NONE"})

(deftest notification-test
  (testing "list-notifications (->NotificationsList)"
    (is (= {:op ::gsc/NotificationsList :bucket "b"} (gsc/->NotificationsList ["b"])))
    (is (= {:op ::gsc/NotificationsList :storage client :bucket "b"} (gsc/->NotificationsList [client "b"]))))

  (testing "get-notification (->NotificationGet)"
    (is (= {:op ::gsc/NotificationGet :bucket "b" :notificationId "n1"} (gsc/->NotificationGet ["b" "n1"])))
    (is (= {:op ::gsc/NotificationGet :storage client :bucket "b" :notificationId "n1"} (gsc/->NotificationGet [client "b" "n1"]))))

  (testing "create-notification (->NotificationCreate)"
    (is (= {:op ::gsc/NotificationCreate :bucket "b" :notificationInfo notif-info} (gsc/->NotificationCreate ["b" notif-info])))
    (is (= {:op ::gsc/NotificationCreate :storage client :bucket "b" :notificationInfo notif-info} (gsc/->NotificationCreate [client "b" notif-info]))))

  (testing "delete-notification (->NotificationDelete)"
    (is (= {:op ::gsc/NotificationDelete :bucket "b" :notificationId "n1"} (gsc/->NotificationDelete ["b" "n1"])))
    (is (= {:op ::gsc/NotificationDelete :storage client :bucket "b" :notificationId "n1"} (gsc/->NotificationDelete [client "b" "n1"])))))

(def policy {:version 3})

(deftest iam-test
  (testing "get-iam-policy (->IamPolicyGet)"
    (is (= {:op ::gsc/IamPolicyGet :bucket "b"} (gsc/->IamPolicyGet ["b"])))
    (is (= {:op ::gsc/IamPolicyGet :storage client :bucket "b"} (gsc/->IamPolicyGet [client "b"])))
    (is (= {:op ::gsc/IamPolicyGet :bucket "b" :opts {:userProject "p"}} (gsc/->IamPolicyGet ["b" {:userProject "p"}])))
    (is (= {:op ::gsc/IamPolicyGet :storage client :bucket "b" :opts {:userProject "p"}} (gsc/->IamPolicyGet [client "b" {:userProject "p"}]))))

  (testing "set-iam-policy (->IamPolicySet)"
    (is (= {:op ::gsc/IamPolicySet :bucket "b" :policy policy} (gsc/->IamPolicySet ["b" policy])))
    (is (= {:op ::gsc/IamPolicySet :storage client :bucket "b" :policy policy} (gsc/->IamPolicySet [client "b" policy])))
    (is (= {:op ::gsc/IamPolicySet :bucket "b" :policy policy :opts {:userProject "p"}} (gsc/->IamPolicySet ["b" policy {:userProject "p"}])))
    (is (= {:op ::gsc/IamPolicySet :storage client :bucket "b" :policy policy :opts {:userProject "p"}} (gsc/->IamPolicySet [client "b" policy {:userProject "p"}]))))

  (testing "test-iam-permissions (->IamPermissionsTest)"
    (is (= {:op ::gsc/IamPermissionsTest :bucket "b" :permissions ["p1"]} (gsc/->IamPermissionsTest ["b" ["p1"]])))
    (is (= {:op ::gsc/IamPermissionsTest :storage client :bucket "b" :permissions ["p1"]} (gsc/->IamPermissionsTest [client "b" ["p1"]])))
    (is (= {:op ::gsc/IamPermissionsTest :bucket "b" :permissions ["p1"] :opts {:userProject "p"}} (gsc/->IamPermissionsTest ["b" ["p1"] {:userProject "p"}])))
    (is (= {:op ::gsc/IamPermissionsTest :storage client :bucket "b" :permissions ["p1"] :opts {:userProject "p"}} (gsc/->IamPermissionsTest [client "b" ["p1"] {:userProject "p"}])))))
