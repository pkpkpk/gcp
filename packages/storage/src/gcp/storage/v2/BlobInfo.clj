(ns gcp.storage.v2.BlobInfo
  (:require [gcp.global :as global]
            [gcp.storage.v2.Acl :as Acl]
            [gcp.storage.v2.BlobId :as BlobId])
  (:import (com.google.cloud.storage BlobInfo BlobInfo$Retention BlobInfo$CustomerEncryption)))

(defn Retention:to-edn [^BlobInfo$Retention arg]
  {:mode (.name (.getMode arg))
   :retainUntilTime (.getRetainUntilTime arg)})

(defn CustomerEncryption:to-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^BlobInfo arg]
  {:post [(global/strict! :gcp.storage.v2/BlobInfo %)]}
  (cond-> {:blobId (BlobId/to-edn (.getBlobId arg))}

          (seq (.getAcl arg))
          (assoc :acl (mapv Acl/Entity:to-edn (.getAcl arg)))

          (some? (.getCacheControl arg))
          (assoc :cacheControl (.getCacheControl arg))

          (some? (.getComponentCount arg))
          (assoc :componentCount (.getComponentCount arg))

          (some? (.getContentDisposition arg))
          (assoc :contentDisposition (.getContentDisposition arg))

          (some? (.getContentEncoding arg))
          (assoc :contentEncoding (.getContentEncoding arg))

          (some? (.getContentLanguage arg))
          (assoc :contentLanguage (.getContentLanguage arg))

          (some? (.getContentType arg))
          (assoc :contentType (.getContentType arg))

          (some? (.getCrc32c arg))
          (assoc :crc32c (.getCrc32c arg))

          (some? (.getCrc32cToHexString arg))
          (assoc :crc32cToHexString (.getCrc32cToHexString arg))

          (some? (.getCreateTimeOffsetDateTime arg))
          (assoc :createTimeOffsetDateTime (.getCreateTimeOffsetDateTime arg))

          (some? (.getCustomTimeOffsetDateTime arg))
          (assoc :customTimeOffsetDateTime (.getCustomTimeOffsetDateTime arg))

          (some? (.getCustomerEncryption arg))
          (assoc :customerEncryption (CustomerEncryption:to-edn (.getCustomerEncryption arg)))

          (some? (.getDeleteTimeOffsetDateTime arg))
          (assoc :deleteTimeOffsetDateTime (.getDeleteTimeOffsetDateTime arg))

          (some? (.getEtag arg))
          (assoc :etag (.getEtag arg))

          (some? (.getEventBasedHold arg))
          (assoc :eventBasedHold (.getEventBasedHold arg))

          (some? (.getGeneratedId arg))
          (assoc :generatedId (.getGeneratedId arg))

          (some? (.getGeneration arg))
          (assoc :generation (.getGeneration arg))

          (some? (.getHardDeleteTime arg))
          (assoc :hardDeleteTime (.getHardDeleteTime arg))

          (some? (.getKmsKeyName arg))
          (assoc :kmsKeyName (.getKmsKeyName arg))

          (some? (.getMd5 arg))
          (assoc :md5 (.getMd5 arg))

          (some? (.getMd5ToHexString arg))
          (assoc :md5ToHexString (.getMd5ToHexString arg))

          (some? (.getMediaLink arg))
          (assoc :mediaLink (.getMediaLink arg))

          (some? (.getMetadata arg))
          (assoc :metadata (into {} (.getMetadata arg)))

          (some? (.getMetageneration arg))
          (assoc :metageneration (.getMetageneration arg))

          (some? (.getOwner arg))
          (assoc :owner (Acl/Entity:to-edn (.getOwner arg)))

          (some? (.getRetention arg))
          (assoc :retention (Retention:to-edn (.getRetention arg)))

          (some? (.getRetentionExpirationTimeOffsetDateTime arg))
          (assoc :retentionExpirationTimeOffsetDateTime (.getRetentionExpirationTimeOffsetDateTime arg))

          (some? (.getSize arg))
          (assoc :size (.getSize arg))

          (some? (.getSoftDeleteTime arg))
          (assoc :softDeleteTime (.getSoftDeleteTime arg))

          (some? (.getStorageClass arg))
          (assoc :storageClass (.name (.getStorageClass arg)))

          (some? (.getTemporaryHold arg))
          (assoc :temporaryHold (.getTemporaryHold arg))

          (some? (.getTimeStorageClassUpdatedOffsetDateTime arg))
          (assoc :timeStorageClassUpdatedOffsetDateTime (.getTimeStorageClassUpdatedOffsetDateTime arg))

          (some? (.getUpdateTimeOffsetDateTime arg))
          (assoc :updateTimeOffsetDateTime (.getUpdateTimeOffsetDateTime arg))

          (some? (.isDirectory arg))
          (assoc :directory (.isDirectory arg))))

(defn ^BlobInfo from-edn [arg]
  (global/strict! :gcp.storage.v2/BlobInfo arg)
  (let [builder (BlobInfo/newBuilder (BlobId/from-edn (:blobId arg)))]
    (.build builder)))

(def schemas
  {:gcp.storage.v2/BlobInfo
   [:map
    {:class 'com.google.cloud.storage.BlobInfo}
    [:blobId :gcp.storage.v2/BlobId]

    [:acl {:doc "Returns the blob's access control configuration. See Also: About Access Control Lists." :optional true}
     [:sequential :any]]

    [:cacheControl {:doc "Returns the blob's data Cache-Control. See Also: Cache-Control." :optional true} :string]
    [:componentCount {:doc "Returns the number of components that make up this blob (for composite blobs)." :optional true} :int]
    [:contentDisposition {:doc "Returns the blob's data content disposition. See Also: Content-Disposition." :optional true} :string]
    [:contentEncoding {:doc "Returns the blob's data content encoding. See Also: Content-Encoding." :optional true} :string]
    [:contentLanguage {:doc "Returns the blob's data content language. See Also: Content-Language." :optional true} :string]
    [:contentType {:doc "Returns the blob's data content type. See Also: Content-Type." :optional true} :string]
    [:crc32c {:doc "Returns the base64-encoded CRC32C of the blob's data (big-endian)." :optional true} :string]
    [:crc32cToHexString {:doc "Returns the CRC32C of blob's data, decoded to a hex string." :optional true} :string]
    [:createTime {:doc "Deprecated. Use getCreateTimeOffsetDateTime(). Returns creation time (ms since epoch)." :optional true} :int]
    [:createTimeOffsetDateTime {:doc "Returns the creation time of the blob as OffsetDateTime." :optional true} :any]
    [:customTime {:doc "Deprecated. Use getCustomTimeOffsetDateTime(). Returns the user-specified custom time in ms." :optional true} :int]
    [:customTimeOffsetDateTime {:doc "Returns the user-specified custom time for this object, as OffsetDateTime." :optional true} :any]
    [:customerEncryption {:doc "Returns information on the customer-supplied encryption key, if applicable." :optional true} :any]
    [:deleteTime {:doc "Deprecated. Use getDeleteTimeOffsetDateTime(). Returns the deletion time of the blob (ms)." :optional true} :int]
    [:deleteTimeOffsetDateTime {:doc "Returns the deletion time of the blob as OffsetDateTime." :optional true} :any]
    [:etag {:doc "Returns HTTP 1.1 Entity tag for the blob." :optional true} :string]
    [:eventBasedHold {:doc "Returns true, false, or null indicating whether the blob is under event-based hold." :optional true} :boolean]
    [:generatedId {:doc "Returns the service-generated ID for the blob." :optional true} :string]
    [:generation {:doc "Returns the blob's data generation (used for versioning)." :optional true} :int]
    [:hardDeleteTime {:doc "If soft-deleted, returns the time at which it will be permanently deleted (OffsetDateTime)." :optional true} :any]
    [:kmsKeyName {:doc "Returns the Cloud KMS key used to encrypt the blob, if any." :optional true} :string]
    [:md5 {:doc "Returns the MD5 hash of the blob's data, base64-encoded." :optional true} :string]
    [:md5ToHexString {:doc "Returns the MD5 hash decoded to a string." :optional true} :string]
    [:mediaLink {:doc "Returns the blob's media download link." :optional true} :string]
    [:metadata {:doc "Returns the blob's user-provided metadata." :optional true} [:map-of :string [:maybe :string]]]
    [:metageneration {:doc "Returns the blob's metageneration. Used for preconditions and metadata changes." :optional true} :int]
    [:owner {:doc "Returns the blob's owner (the uploader)." :optional true} :any]
    [:retention {:doc "Returns the object's Retention policy." :optional true} :any]
    [:retentionExpirationTime {:doc "Deprecated. Use getRetentionExpirationTimeOffsetDateTime(). Retention expiration time in ms." :optional true} :int]
    [:retentionExpirationTimeOffsetDateTime {:doc "If a retention period is defined, returns the time it expires as OffsetDateTime." :optional true} :any]
    [:selfLink {:doc "Returns the URI of this blob as a string." :optional true} :string]
    [:size {:doc "Returns the size of the blob (content length, in bytes)." :optional true} :int]
    [:softDeleteTime {:doc "If soft-deleted, returns the time it was soft-deleted as OffsetDateTime." :optional true} :any]
    [:storageClass {:doc "Returns the storage class of the blob." :optional true} :any]
    [:temporaryHold {:doc "Returns true, false, or null indicating whether the blob is under temporary hold." :optional true} :boolean]
    [:timeStorageClassUpdated {:doc "Deprecated. Use getTimeStorageClassUpdatedOffsetDateTime(). Time the object's storage class was changed or created (ms)." :optional true} :int]
    [:timeStorageClassUpdatedOffsetDateTime {:doc "Returns the time the object's storage class was last changed as OffsetDateTime." :optional true} :any]
    [:updateTime {:doc "Deprecated. Use getUpdateTimeOffsetDateTime(). The last metadata update time in ms since epoch." :optional true} :int]
    [:updateTimeOffsetDateTime {:doc "Returns the last modification time of the blob's metadata as OffsetDateTime." :optional true} :any]
    [:directory {:doc "Returns true if the blob represents a directory under currentDirectory() listing, else false." :optional true} :boolean]]

   :gcp.storage.v2/BlobInfo.CustomerEncryption :any
   :gcp.storage.v2/BlobInfo.ImmutableEmptyMap :any
   :gcp.storage.v2/BlobInfo.Retention :any
   :gcp.storage.v2/BlobInfo.Retention.Mode :any})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))