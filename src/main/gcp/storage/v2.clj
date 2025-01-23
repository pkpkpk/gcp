(ns gcp.storage.v2
  (:require [gcp.global :as g]
            [gcp.storage.v2.StorageOptions :as so])
  (:import [com.google.cloud.storage Storage]))

;; Interfaces
;BlobWriteSession
;Storage
;StorageFactory
;StorageRetryStrategy

;; enums
;Acl.Entity.Type
;BucketInfo.DeleteRule.Type
;BucketInfo.PublicAccessPrevention
;HmacKey.HmacKeyState
;NotificationInfo.EventType
;PostPolicyV4.ConditionV4Type
;Storage.BlobField
;Storage.BucketField
;Storage.PredefinedAcl
;Storage.UriScheme

(def registry
  {:storage/StorageOptions                                                           [:maybe
                                                                                      [:map
                                                                                       [:blobWriteSessionConfig {:optional true} :storage/BlobWriteSessionConfig]
                                                                                       [:storageRetryStrategy {:optional true} :storage.synth/StorageRetryStrategy]]]

   :storage.synth/client                                                             [:fn
                                                                                      {:error/message "expected Storage client instance"
                                                                                       :from-edn      so/get-service}
                                                                                      #(instance? Storage %)]

   :storage.synth/clientable                                                         [:or
                                                                                      :storage/StorageOptions
                                                                                      :storage.synth/client
                                                                                      [:map [:storage [:or :storage/StorageOptions :storage.synth/client]]]]

   :storage.synth/StorageRetryStrategy                                               [:map
                                                                                      {:url "https://cloud.google.com/java/docs/reference/google-cloud-storage/latest/com.google.cloud.storage.StorageRetryStrategy"}
                                                                                      ]

   #!--------------------------------------------------------------------------
   #! Top Level ops

   :storage.synth/BucketList                                                         [:maybe
                                                                                      [:map
                                                                                       [:storage {:optional true} :storage.synth/clientable]
                                                                                       [:options {:optional true} [:sequential :storage/Storage.BucketListOption]]]]

   :storage.synth/BucketGet                                                          [:map
                                                                                      [:storage {:optional true} :storage.synth/clientable]
                                                                                      [:bucket :string]
                                                                                      [:options {:optional true} [:sequential :storage/Storage.BucketGetOption]]]

   :storage.synth/BucketCreate                                                       [:map
                                                                                      [:storage {:optional true} :storage.synth/clientable]
                                                                                      [:bucketInfo :storage/BucketInfo]
                                                                                      [:options {:optional true} [:sequential :storage/Storage.BucketTargetOption]]]

   :storage.synth/BlobList                                                           [:map
                                                                                      [:storage {:optional true} :storage.synth/clientable]
                                                                                      [:bucket :string]
                                                                                      [:options {:optional true} [:sequential :storage/Storage.BlobListOption]]]

   :storage.synth/BlobDelete                                                         [:map
                                                                                      [:storage {:optional true} :storage.synth/clientable]
                                                                                      [:blobs [:sequential :storage/BlobId]]
                                                                                      [:options {:optional true} [:sequential :storage/Storage.BlobSourceOption]]]

   :storage.synth/ReadAllBytes                                                       [:map
                                                                                      [:storage {:optional true} :storage.synth/clientable]
                                                                                      [:blobId :storage/BlobId]
                                                                                      [:options {:optional true} [:sequential :storage/Storage.BlobSourceOption]]]

   #!--------------------------------------------------------------------------
   #! Storage Option classes given to IO ops

   :storage/Storage.BlobGetOption                                                    :any
   :storage/Storage.BlobListOption                                                   :any
   :storage/Storage.BlobRestoreOption                                                :any
   :storage/Storage.BlobSourceOption                                                 :any
   :storage/Storage.BlobTargetOption                                                 :any
   :storage/Storage.BlobWriteOption                                                  :any
   :storage/Storage.BucketGetOption                                                  :any
   :storage/Storage.BucketListOption                                                 :any
   :storage/Storage.BucketSourceOption                                               :any
   :storage/Storage.BucketTargetOption                                               :any
   :storage/Storage.ComposeRequest                                                   :any
   :storage/Storage.ComposeRequest.SourceBlob                                        :any
   :storage/Storage.CopyRequest                                                      :any
   :storage/Storage.CreateHmacKeyOption                                              :any
   :storage/Storage.DeleteHmacKeyOption                                              :any
   :storage/Storage.GetHmacKeyOption                                                 :any
   :storage/Storage.ListHmacKeysOption                                               :any
   :storage/Storage.PostPolicyV4Option                                               :any
   :storage/Storage.SignUrlOption                                                    :any
   :storage/Storage.UpdateHmacKeyOption                                              :any

   #!--------------------------------------------------------------------------
   #! Identity

   :storage/BlobId                                                                   [:map
                                                                                      {:closed   true
                                                                                       :class    'com.google.cloud.storage.BlobId
                                                                                       :from-edn 'gcp.storage.v2.BlobId/from-edn
                                                                                       :to-edn   'gcp.storage.v2.BlobId/to-edn}
                                                                                      [:bucket :string]
                                                                                      [:name :string]
                                                                                      [:generation {:optional true} :int]]

   #!--------------------------------------------------------------------------
   #! reified

   :storage/Blob                                                                     [:and
                                                                                      {:class 'com.google.cloud.storage.Blob}
                                                                                      [:map [:storage :storage.synth/client]]
                                                                                      :storage/BlobInfo]
   :storage/Bucket                                                                   [:and
                                                                                      {:class 'com.google.cloud.storage.Bucket}
                                                                                      [:map [:storage :storage.synth/client]]
                                                                                      :storage/BucketInfo]
   :storage/Notification                                                             [:and
                                                                                      {:class 'com.google.cloud.storage.Notification}
                                                                                      [:map [:storage :storage.synth/client]]
                                                                                      :storage/NotificationInfo]

   #!--------------------------------------------------------------------------
   #! Info

   :storage/BlobInfo                                                                 [:map
                                                                                      {:class 'com.google.cloud.storage.BlobInfo}
                                                                                      [:blobId :storage/BlobId]
                                                                                      [:bucket :string]

                                                                                      ;; Access control configuration (ACLs)
                                                                                      [:acl
                                                                                       {:doc      "Returns the blob's access control configuration. See Also: About Access Control Lists."
                                                                                        :optional true}
                                                                                       [:sequential :any]]

                                                                                      ;; Cache-Control header
                                                                                      [:cacheControl
                                                                                       {:doc      "Returns the blob's data Cache-Control. See Also: Cache-Control."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; The number of components composing this blob
                                                                                      [:componentCount
                                                                                       {:doc      "Returns the number of components that make up this blob (for composite blobs)."
                                                                                        :optional true}
                                                                                       :int]

                                                                                      ;; Content-Disposition header
                                                                                      [:contentDisposition
                                                                                       {:doc      "Returns the blob's data content disposition. See Also: Content-Disposition."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; Content-Encoding header
                                                                                      [:contentEncoding
                                                                                       {:doc      "Returns the blob's data content encoding. See Also: Content-Encoding."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; Content-Language header
                                                                                      [:contentLanguage
                                                                                       {:doc      "Returns the blob's data content language. See Also: Content-Language."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; Content-Type header
                                                                                      [:contentType
                                                                                       {:doc      "Returns the blob's data content type. See Also: Content-Type."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; Base64-encoded CRC32C
                                                                                      [:crc32c
                                                                                       {:doc      "Returns the base64-encoded CRC32C of the blob's data (big-endian)."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; Hex-decoded CRC32C
                                                                                      [:crc32cToHexString
                                                                                       {:doc      "Returns the CRC32C of blob's data, decoded to a hex string."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; Creation time in milliseconds (deprecated)
                                                                                      [:createTime
                                                                                       {:doc      "Deprecated. Use getCreateTimeOffsetDateTime(). Returns creation time (ms since epoch)."
                                                                                        :optional true}
                                                                                       :int]

                                                                                      ;; Creation time as OffsetDateTime
                                                                                      [:createTimeOffsetDateTime
                                                                                       {:doc      "Returns the creation time of the blob as OffsetDateTime."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; Custom time in milliseconds (deprecated)
                                                                                      [:customTime
                                                                                       {:doc      "Deprecated. Use getCustomTimeOffsetDateTime(). Returns the user-specified custom time in ms."
                                                                                        :optional true}
                                                                                       :int]

                                                                                      ;; Custom time as OffsetDateTime
                                                                                      [:customTimeOffsetDateTime
                                                                                       {:doc      "Returns the user-specified custom time for this object, as OffsetDateTime."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; Customer-supplied encryption key info
                                                                                      [:customerEncryption
                                                                                       {:doc      "Returns information on the customer-supplied encryption key, if applicable."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; Deletion time in milliseconds (deprecated)
                                                                                      [:deleteTime
                                                                                       {:doc      "Deprecated. Use getDeleteTimeOffsetDateTime(). Returns the deletion time of the blob (ms)."
                                                                                        :optional true}
                                                                                       :int]

                                                                                      ;; Deletion time as OffsetDateTime
                                                                                      [:deleteTimeOffsetDateTime
                                                                                       {:doc      "Returns the deletion time of the blob as OffsetDateTime."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; HTTP 1.1 ETag
                                                                                      [:etag
                                                                                       {:doc      "Returns HTTP 1.1 Entity tag for the blob."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; Event-based hold status (Beta)
                                                                                      [:eventBasedHold
                                                                                       {:doc      "Returns true, false, or null indicating whether the blob is under event-based hold."
                                                                                        :optional true}
                                                                                       :boolean]

                                                                                      ;; Service-generated ID for the blob
                                                                                      [:generatedId
                                                                                       {:doc      "Returns the service-generated ID for the blob."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; Data generation (for versioning)
                                                                                      [:generation
                                                                                       {:doc      "Returns the blob's data generation (used for versioning)."
                                                                                        :optional true}
                                                                                       :int]

                                                                                      ;; Hard delete time (if soft-deleted, this is when it will be permanently deleted)
                                                                                      [:hardDeleteTime
                                                                                       {:doc      "If soft-deleted, returns the time at which it will be permanently deleted (OffsetDateTime)."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; The Cloud KMS key used for encryption
                                                                                      [:kmsKeyName
                                                                                       {:doc      "Returns the Cloud KMS key used to encrypt the blob, if any."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; Base64-encoded MD5
                                                                                      [:md5
                                                                                       {:doc      "Returns the MD5 hash of the blob's data, base64-encoded."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; Hex-decoded MD5
                                                                                      [:md5ToHexString
                                                                                       {:doc      "Returns the MD5 hash decoded to a string."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; Media download link
                                                                                      [:mediaLink
                                                                                       {:doc      "Returns the blob's media download link."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; User-provided metadata
                                                                                      [:metadata
                                                                                       {:doc      "Returns the blob's user-provided metadata."
                                                                                        :optional true}
                                                                                       [:map-of :string [:maybe :string]]]

                                                                                      ;; Metageneration (for preconditions, etc.)
                                                                                      [:metageneration
                                                                                       {:doc      "Returns the blob's metageneration. Used for preconditions and metadata changes."
                                                                                        :optional true}
                                                                                       :int]

                                                                                      ;; Blob name
                                                                                      [:name
                                                                                       {:doc      "Returns the blob's name."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; Blob owner
                                                                                      [:owner
                                                                                       {:doc      "Returns the blob's owner (the uploader)."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; Retention policy info
                                                                                      [:retention
                                                                                       {:doc      "Returns the object's Retention policy."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; Retention expiration time in millis (deprecated)
                                                                                      [:retentionExpirationTime
                                                                                       {:doc      "Deprecated. Use getRetentionExpirationTimeOffsetDateTime(). Retention expiration time in ms."
                                                                                        :optional true}
                                                                                       :int]

                                                                                      ;; Retention expiration time as OffsetDateTime
                                                                                      [:retentionExpirationTimeOffsetDateTime
                                                                                       {:doc      "If a retention period is defined, returns the time it expires as OffsetDateTime."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; Self-link
                                                                                      [:selfLink
                                                                                       {:doc      "Returns the URI of this blob as a string."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; Content size in bytes
                                                                                      [:size
                                                                                       {:doc      "Returns the size of the blob (content length, in bytes)."
                                                                                        :optional true}
                                                                                       :int]

                                                                                      ;; Soft delete time (if soft-deleted, the time it was soft-deleted)
                                                                                      [:softDeleteTime
                                                                                       {:doc      "If soft-deleted, returns the time it was soft-deleted as OffsetDateTime."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; Storage class
                                                                                      [:storageClass
                                                                                       {:doc      "Returns the storage class of the blob."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; Temporary hold status (Beta)
                                                                                      [:temporaryHold
                                                                                       {:doc      "Returns true, false, or null indicating whether the blob is under temporary hold."
                                                                                        :optional true}
                                                                                       :boolean]

                                                                                      ;; Time storage class was last changed (ms) (deprecated)
                                                                                      [:timeStorageClassUpdated
                                                                                       {:doc      "Deprecated. Use getTimeStorageClassUpdatedOffsetDateTime(). Time the object's storage class was changed or created (ms)."
                                                                                        :optional true}
                                                                                       :int]

                                                                                      ;; Time storage class was last changed as OffsetDateTime
                                                                                      [:timeStorageClassUpdatedOffsetDateTime
                                                                                       {:doc      "Returns the time the object's storage class was last changed as OffsetDateTime."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; Last metadata update time in ms (deprecated)
                                                                                      [:updateTime
                                                                                       {:doc      "Deprecated. Use getUpdateTimeOffsetDateTime(). The last metadata update time in ms since epoch."
                                                                                        :optional true}
                                                                                       :int]

                                                                                      ;; Last metadata update time as OffsetDateTime
                                                                                      [:updateTimeOffsetDateTime
                                                                                       {:doc      "Returns the last modification time of the blob's metadata as OffsetDateTime."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; Indicates whether this blob represents a directory (when using currentDirectory() in list)
                                                                                      [:directory
                                                                                       {:doc      "Returns true if the blob represents a directory under currentDirectory() listing, else false."
                                                                                        :optional true}
                                                                                       :boolean]]

   :storage/BucketInfo                                                               [:map
                                                                                      {:class 'com.google.cloud.storage.BucketInfo}
                                                                                      [:name :string]

                                                                                      ;; the bucket's access control configuration
                                                                                      [:acl
                                                                                       {:doc      "Returns the bucket's access control configuration. See Also: About Access Control Lists."
                                                                                        :optional true}
                                                                                       [:sequential :any]] ; e.g. vector of ACL entries

                                                                                      ;; the Autoclass configuration
                                                                                      [:autoclass
                                                                                       {:doc      "Returns the Autoclass configuration."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; CORS configuration
                                                                                      [:cors
                                                                                       {:doc      "Returns the bucket's Cross-Origin Resource Sharing (CORS) configuration. See Also: CORS."
                                                                                        :optional true}
                                                                                       [:sequential :any]]

                                                                                      ;; creation time in millis (deprecated, use offset date-time)
                                                                                      [:createTime
                                                                                       {:doc      "Deprecated. #getCreateTimeOffsetDateTime(). Returns the time (millis) at which the bucket was created."
                                                                                        :optional true}
                                                                                       :int]

                                                                                      ;; creation time as OffsetDateTime
                                                                                      [:createTimeOffsetDateTime
                                                                                       {:doc      "Returns the time at which the bucket was created as OffsetDateTime."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; custom placement config for multi-region
                                                                                      [:customPlacementConfig
                                                                                       {:doc      "Returns the Custom Placement Configuration for multi-regional setup."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; default ACL for the bucket's blobs
                                                                                      [:defaultAcl
                                                                                       {:doc      "Returns the default access control configuration for this bucket's blobs. See Also: ACLs."
                                                                                        :optional true}
                                                                                       [:sequential :any]]

                                                                                      ;; default event-based hold
                                                                                      [:defaultEventBasedHold
                                                                                       {:doc      "Returns a Boolean that may be true, false, or null indicating default event-based hold."
                                                                                        :optional true}
                                                                                       :boolean]

                                                                                      ;; default Cloud KMS key
                                                                                      [:defaultKmsKeyName
                                                                                       {:doc      "Returns the default Cloud KMS key name for newly inserted objects."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; delete rules (deprecated, use lifecycleRules instead)
                                                                                      [:deleteRules
                                                                                       {:doc      "Deprecated. Lifecycle configuration as a list of delete rules. See Also: Lifecycle Management."
                                                                                        :optional true}
                                                                                       [:sequential :any]]

                                                                                      ;; HTTP 1.1 ETag
                                                                                      [:etag
                                                                                       {:doc      "Returns the HTTP 1.1 Entity tag for the bucket."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; service-generated bucket ID
                                                                                      [:generatedId
                                                                                       {:doc      "Returns the service-generated id for the bucket."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; hierarchical namespace configuration
                                                                                      [:hierarchicalNamespace
                                                                                       {:doc      "Returns the Hierarchical Namespace (Folders) configuration."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; IAM configuration
                                                                                      [:iamConfiguration
                                                                                       {:doc      "Beta feature. Returns the IAM configuration for the bucket."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; index page for website
                                                                                      [:indexPage
                                                                                       {:doc      "Returns the bucket's website index page (for directory browsing)."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; labels for the bucket
                                                                                      [:labels
                                                                                       {:doc      "Returns the labels for this bucket."
                                                                                        :optional true}
                                                                                       [:map-of :string [:maybe :string]]]

                                                                                      ;; lifecycle rules
                                                                                      [:lifecycleRules
                                                                                       {:doc      "Returns the bucket's lifecycle rules (replaces the older deleteRules)."
                                                                                        :optional true}
                                                                                       [:sequential :any]]

                                                                                      ;; bucket location (region)
                                                                                      [:location
                                                                                       {:doc      "Returns the bucket's location (region). If multiple regions, customPlacementConfig is also set."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; location type
                                                                                      [:locationType
                                                                                       {:doc      "Returns the bucket's locationType. See Also: Bucket LocationType."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; logging configuration
                                                                                      [:logging
                                                                                       {:doc      "Returns the Logging configuration for the bucket."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; metadata generation
                                                                                      [:metageneration
                                                                                       {:doc      "Returns the metadata generation of this bucket."
                                                                                        :optional true}
                                                                                       :int]

                                                                                      ;; not found page for website
                                                                                      [:notFoundPage
                                                                                       {:doc      "Returns the custom object to serve when a requested resource is not found."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; object retention configuration
                                                                                      [:objectRetention
                                                                                       {:doc      "Returns the Object Retention configuration."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; the bucket's owner (always the project team's owner group)
                                                                                      [:owner
                                                                                       {:doc      "Returns the bucket's owner entity."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; retention effective time in millis (deprecated)
                                                                                      [:retentionEffectiveTime
                                                                                       {:doc      "Deprecated. Use #getRetentionEffectiveTimeOffsetDateTime(). Retention effective time in millis if a policy is defined."
                                                                                        :optional true}
                                                                                       :int]

                                                                                      ;; retention effective time as OffsetDateTime
                                                                                      [:retentionEffectiveTimeOffsetDateTime
                                                                                       {:doc      "Beta feature. Returns the retention effective time a policy took effect, as OffsetDateTime."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; retention period in millis (deprecated)
                                                                                      [:retentionPeriod
                                                                                       {:doc      "Deprecated. Use #getRetentionPeriodDuration(). Retention policy period in millis."
                                                                                        :optional true}
                                                                                       :int]

                                                                                      ;; retention period as Duration
                                                                                      [:retentionPeriodDuration
                                                                                       {:doc      "Beta feature. Returns the retention policy period as a Duration."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; recovery point objective
                                                                                      [:rpo
                                                                                       {:doc      "Returns the bucket's recovery point objective (RPO) for dual-region replication."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; self-link
                                                                                      [:selfLink
                                                                                       {:doc      "Returns the URI of this bucket as a string."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; soft delete policy
                                                                                      [:softDeletePolicy
                                                                                       {:doc      "Returns the Soft Delete policy."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; storage class
                                                                                      [:storageClass
                                                                                       {:doc      "Returns the bucket's storage class (defines SLA and storage cost)."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; update time in millis (deprecated)
                                                                                      [:updateTime
                                                                                       {:doc      "Deprecated. Use #getUpdateTimeOffsetDateTime(). Returns the last metadata update time in millis."
                                                                                        :optional true}
                                                                                       :int]

                                                                                      ;; update time as OffsetDateTime
                                                                                      [:updateTimeOffsetDateTime
                                                                                       {:doc      "Returns the last modification time of the bucket's metadata as an OffsetDateTime."
                                                                                        :optional true}
                                                                                       :any]

                                                                                      ;; boolean that may be true, false, or null if requesting user pays
                                                                                      [:requesterPays
                                                                                       {:doc      "Returns whether the bucket is requester-pays. May be true, false, or null (unknown)."
                                                                                        :optional true}
                                                                                       :boolean]

                                                                                      ;; indicates if retention policy is locked
                                                                                      [:retentionPolicyIsLocked
                                                                                       {:doc      "Beta feature. Returns whether the bucket's retention policy is locked. May be true or null."
                                                                                        :optional true}
                                                                                       :boolean]

                                                                                      ;; whether versioning is enabled
                                                                                      [:versioningEnabled
                                                                                       {:doc      "Returns a Boolean that may be true, false, or null indicating if versioning is enabled."
                                                                                        :optional true}
                                                                                       :boolean]]

   :storage/NotificationInfo                                                         [:map
                                                                                      {:class 'com.google.cloud.storage.NotificationInfo}
                                                                                      [:topic
                                                                                       {:doc      "Returns the topic (string in the format 'projects/{project}/topics/{topic}') where notifications are published."
                                                                                        :optional false}
                                                                                       :string]

                                                                                      ;; A map of additional attributes for Cloud PubSub messages
                                                                                      [:customAttributes
                                                                                       {:doc      "Returns the list of additional attributes to attach to each published Pub/Sub message."
                                                                                        :optional true}
                                                                                       [:map-of :string :string]]

                                                                                      ;; HTTP 1.1 ETag
                                                                                      [:etag
                                                                                       {:doc      "Returns the HTTP 1.1 Entity tag for the notification. See Entity Tags."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; The event types that trigger notifications
                                                                                      [:eventTypes
                                                                                       {:doc      "Returns the events that trigger a notification. If empty, any event triggers a notification."
                                                                                        :optional true}
                                                                                       [:sequential :any]] ;; Replace :any with a more specific schema if you model EventType

                                                                                      ;; Service-generated notification ID
                                                                                      [:notificationId
                                                                                       {:doc      "Returns the service-generated ID for the notification."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; Object name prefix filter
                                                                                      [:objectNamePrefix
                                                                                       {:doc      "Returns the object name prefix for which this notification config applies."
                                                                                        :optional true}
                                                                                       :string]

                                                                                      ;; Payload format
                                                                                      [:payloadFormat
                                                                                       {:doc      "Returns the desired content of the published message payload."
                                                                                        :optional true}
                                                                                       :any] ;; Could be an enum if you have a dedicated schema: e.g. [:enum "JSON_API_V1" "NONE"]

                                                                                      ;; The canonical URI of this notification
                                                                                      [:selfLink
                                                                                       {:doc      "Returns the canonical URI of this topic as a string."
                                                                                        :optional true}
                                                                                       :string]]

   #!--------------------------------------------------------------------------

   :storage/Acl                                                                      :any
   :storage/Acl.Domain                                                               :any
   :storage/Acl.Entity                                                               :any
   :storage/Acl.Group                                                                :any
   :storage/Acl.Project                                                              :any
   :storage/Acl.Project.ProjectRole                                                  :any
   :storage/Acl.RawEntity                                                            :any
   :storage/Acl.Role                                                                 :any
   :storage/Acl.User                                                                 :any

   :storage/BidiBlobWriteSessionConfig                                               :any

   :storage/Blob.BlobSourceOption                                                    :any

   :storage/BlobInfo.CustomerEncryption                                              :any
   :storage/BlobInfo.ImmutableEmptyMap                                               :any
   :storage/BlobInfo.Retention                                                       :any
   :storage/BlobInfo.Retention.Mode                                                  :any

   :storage/BlobWriteSessionConfig                                                   :any
   :storage/BlobWriteSessionConfigs                                                  :any

   :storage/Bucket.BlobTargetOption                                                  :any
   :storage/Bucket.BlobWriteOption                                                   :any
   :storage/Bucket.BucketSourceOption                                                :any

   :storage/BucketInfo.AgeDeleteRule                                                 :any
   :storage/BucketInfo.Autoclass                                                     :any
   :storage/BucketInfo.CreatedBeforeDeleteRule                                       :any
   :storage/BucketInfo.CustomPlacementConfig                                         :any
   :storage/BucketInfo.DeleteRule                                                    :any
   :storage/BucketInfo.HierarchicalNamespace                                         :any
   :storage/BucketInfo.IamConfiguration                                              :any
   :storage/BucketInfo.IsLiveDeleteRule                                              :any
   :storage/BucketInfo.LifecycleRule                                                 :any
   :storage/BucketInfo.LifecycleRule.AbortIncompleteMPUAction                        :any
   :storage/BucketInfo.LifecycleRule.DeleteLifecycleAction                           :any
   :storage/BucketInfo.LifecycleRule.LifecycleAction                                 :any
   :storage/BucketInfo.LifecycleRule.LifecycleCondition                              :any
   :storage/BucketInfo.LifecycleRule.SetStorageClassLifecycleAction                  :any
   :storage/BucketInfo.Logging                                                       :any
   :storage/BucketInfo.NumNewerVersionsDeleteRule                                    :any
   :storage/BucketInfo.ObjectRetention                                               :any
   :storage/BucketInfo.ObjectRetention.Mode                                          :any
   :storage/BucketInfo.SoftDeletePolicy                                              :any

   :storage/BufferToDiskThenUpload                                                   :any
   :storage/CanonicalExtensionHeadersSerializer                                      :any
   :storage/CopyWriter                                                               :any
   :storage/Cors                                                                     :any
   :storage/Cors.Origin                                                              :any
   :storage/DefaultBlobWriteSessionConfig                                            :any
   :storage/GrpcStorageOptions                                                       :any
   :storage/GrpcStorageOptions.GrpcStorageDefaults                                   :any
   :storage/GrpcStorageOptions.GrpcStorageFactory                                    :any
   :storage/GrpcStorageOptions.GrpcStorageRpcFactory                                 :any
   :storage/HmacKey                                                                  :any
   :storage/HmacKey.HmacKeyMetadata                                                  :any
   :storage/HttpCopyWriter                                                           :any
   :storage/HttpMethod                                                               :any
   :storage/HttpStorageOptions                                                       :any
   :storage/HttpStorageOptions.HttpStorageDefaults                                   :any
   :storage/HttpStorageOptions.HttpStorageFactory                                    :any
   :storage/HttpStorageOptions.HttpStorageRpcFactory                                 :any
   :storage/JournalingBlobWriteSessionConfig                                         :any
   :storage/Option                                                                   :any
   :storage/ParallelCompositeUploadBlobWriteSessionConfig                            :any
   :storage/ParallelCompositeUploadBlobWriteSessionConfig.BufferAllocationStrategy   :any
   :storage/ParallelCompositeUploadBlobWriteSessionConfig.ExecutorSupplier           :any
   :storage/ParallelCompositeUploadBlobWriteSessionConfig.PartCleanupStrategy        :any
   :storage/ParallelCompositeUploadBlobWriteSessionConfig.PartMetadataFieldDecorator :any
   :storage/ParallelCompositeUploadBlobWriteSessionConfig.PartNamingStrategy         :any
   :storage/PostPolicyV4                                                             :any
   :storage/PostPolicyV4.ConditionV4                                                 :any
   :storage/PostPolicyV4.PostConditionsV4                                            :any
   :storage/PostPolicyV4.PostFieldsV4                                                :any
   :storage/PostPolicyV4.PostPolicyV4Document                                        :any
   :storage/Rpo                                                                      :any
   :storage/ServiceAccount                                                           :any
   :storage/SignatureInfo                                                            :any
   :storage/StorageBatch                                                             :any
   :storage/StorageBatchResult                                                       :any
   :storage/StorageClass                                                             :any
   :storage/StorageRoles                                                             :any})

(g/include! registry)