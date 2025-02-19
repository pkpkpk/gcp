(ns gcp.storage.v2.BlobInfo
  (:require [gcp.global :as global]
            [gcp.storage.v2.Acl :as Acl]
            [gcp.storage.v2.BlobId :as BlobId])
  (:import (com.google.cloud.storage BlobInfo BlobInfo$Retention BlobInfo$CustomerEncryption)))

(defn Retention-to-edn [^BlobInfo$Retention arg]
  {:mode (.name (.getMode arg))
   :retainUntilTime (.getRetainUntilTime arg)})

(defn CustomerEncryption-to-edn [arg]
  (throw (Exception. "unimplemented")))

(defn to-edn [^BlobInfo arg]
  {:post [(global/strict! :storage/BlobInfo %)]}
  (cond-> {:name (.getName arg)}

          (some? (.getBlobId arg))
          (assoc :blobId (BlobId/to-edn (.getBlobId arg)))

          (some? (.getBucket arg))
          (assoc :bucket (.getBucket arg))

          (seq (.getAcl arg))
          (assoc :acl (mapv Acl/to-edn (.getAcl arg)))

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
          (assoc :customerEncryption (CustomerEncryption-to-edn (.getCustomerEncryption arg)))

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
          (assoc :owner (Acl/Entity-to-edn (.getOwner arg)))

          (some? (.getRetention arg))
          (assoc :retention (Retention-to-edn (.getRetention arg)))

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
