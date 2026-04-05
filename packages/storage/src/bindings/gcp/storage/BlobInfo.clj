;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.storage.BlobInfo
  {:doc
     "Information about an object in Google Cloud Storage. A {@code BlobInfo} object includes the\n{@code BlobId} instance and the set of properties, such as the blob's access control\nconfiguration, user provided metadata, the CRC32C checksum, etc. Instances of this class are used\nto create a new object in Google Cloud Storage or update the properties of an existing object. To\ndeal with existing Storage objects the API includes the {@link Blob} class which extends {@code\nBlobInfo} and declares methods to perform operations on the object. Neither {@code BlobInfo} nor\n{@code Blob} instances keep the object content, just the object properties.\n\n<p>Example of usage {@code BlobInfo} to create an object in Google Cloud Storage:\n\n<pre>{@code\nBlobId blobId = BlobId.of(bucketName, blobName);\nBlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(\"text/plain\").build();\nBlob blob = storage.create(blobInfo, \"Hello, world\".getBytes(StandardCharsets.UTF_8));\n}</pre>\n\n@see <a href=\"https://cloud.google.com/storage/docs/concepts-techniques#concepts\">Concepts and\n    Terminology</a>"
   :file-git-sha "2fd15f69e93a3df2b8dbbd4f08edd07c087e957c"
   :fqcn "com.google.cloud.storage.BlobInfo"
   :gcp.dev/certification
     {:base-seed 1775175413689
      :manifest "215ec381-0f5f-5884-ab6d-eb0bb246cd16"
      :passed-stages
        {:smoke 1775175413689 :standard 1775175413690 :stress 1775175413691}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-03T00:16:56.842141049Z"}}
  (:require [gcp.global :as global]
            [gcp.storage.Acl :as Acl]
            [gcp.storage.BlobId :as BlobId])
  (:import [com.google.cloud.storage BlobInfo BlobInfo$Builder
            BlobInfo$CustomerEncryption BlobInfo$ObjectContexts
            BlobInfo$ObjectContexts$Builder BlobInfo$ObjectCustomContextPayload
            BlobInfo$ObjectCustomContextPayload$Builder BlobInfo$Retention
            BlobInfo$Retention$Builder BlobInfo$Retention$Mode StorageClass]))

(declare from-edn
         to-edn
         CustomerEncryption-from-edn
         CustomerEncryption-to-edn
         Retention$Mode-from-edn
         Retention$Mode-to-edn
         Retention-from-edn
         Retention-to-edn
         Retention$Mode-from-edn
         Retention$Mode-to-edn
         ObjectContexts-from-edn
         ObjectContexts-to-edn
         ObjectCustomContextPayload-from-edn
         ObjectCustomContextPayload-to-edn)

(defn ^BlobInfo$CustomerEncryption CustomerEncryption-from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.storage.BlobInfo.CustomerEncryption is read-only")))

(defn CustomerEncryption-to-edn
  [^BlobInfo$CustomerEncryption arg]
  (when arg
    (cond-> {}
      (some->> (.getEncryptionAlgorithm arg)
               (not= ""))
        (assoc :encryptionAlgorithm (.getEncryptionAlgorithm arg))
      (some->> (.getKeySha256 arg)
               (not= ""))
        (assoc :keySha256 (.getKeySha256 arg)))))

(def CustomerEncryption-schema
  [:map
   {:closed true,
    :doc
      "Objects of this class hold information on the customer-supplied encryption key, if the blob is\nencrypted using such a key.",
    :gcp/category :nested/read-only,
    :gcp/key :gcp.storage/BlobInfo.CustomerEncryption}
   [:encryptionAlgorithm
    {:read-only? true, :doc "Returns the algorithm used to encrypt the blob."}
    [:string {:min 1}]]
   [:keySha256
    {:read-only? true, :doc "Returns the SHA256 hash of the encryption key."}
    [:string {:min 1}]]])

(def Retention$Mode-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/string-enum,
    :gcp/key :gcp.storage/BlobInfo.Retention.Mode} "UNLOCKED" "LOCKED"])

(def Retention$Mode-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/string-enum,
    :gcp/key :gcp.storage/BlobInfo.Retention.Mode} "UNLOCKED" "LOCKED"])

(defn ^BlobInfo$Retention Retention-from-edn
  [arg]
  (let [builder (BlobInfo$Retention/newBuilder)]
    (when (some? (get arg :mode))
      (.setMode builder (BlobInfo$Retention$Mode/valueOf (get arg :mode))))
    (when (some? (get arg :retainUntilTime))
      (.setRetainUntilTime builder (get arg :retainUntilTime)))
    (.build builder)))

(defn Retention-to-edn
  [^BlobInfo$Retention arg]
  (when arg
    (cond-> {}
      (.getMode arg) (assoc :mode (.name (.getMode arg)))
      (.getRetainUntilTime arg) (assoc :retainUntilTime
                                  (.getRetainUntilTime arg)))))

(def Retention-schema
  [:map
   {:closed true,
    :doc
      "Defines a blob's Retention policy. Can only be used on objects in a retention-enabled bucket.",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/BlobInfo.Retention}
   [:mode
    {:optional true,
     :getter-doc
       "Returns the retention policy's Mode. Can be Locked or Unlocked.",
     :setter-doc "Sets the retention policy's Mode. Can be Locked or Unlocked."}
    [:enum {:closed true} "Unlocked" "Locked"]]
   [:retainUntilTime
    {:optional true,
     :getter-doc
       "Returns what time this object will be retained until, if the mode is Locked.",
     :setter-doc
       "Sets what time this object will be retained until, if the mode is Locked."}
    :OffsetDateTime]])

(defn ^BlobInfo$ObjectContexts ObjectContexts-from-edn
  [arg]
  (let [builder (BlobInfo$ObjectContexts/newBuilder)]
    (when (seq (get arg :custom))
      (.setCustom builder
                  (into {}
                        (map (fn [[k v]] [(name k)
                                          (ObjectCustomContextPayload-from-edn
                                            v)]))
                        (get arg :custom))))
    (.build builder)))

(defn ObjectContexts-to-edn
  [^BlobInfo$ObjectContexts arg]
  (when arg
    (cond-> {}
      (seq (.getCustom arg))
        (assoc :custom
          (into {}
                (map (fn [[k v]] [(keyword k)
                                  (ObjectCustomContextPayload-to-edn v)]))
                (.getCustom arg))))))

(def ObjectContexts-schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/BlobInfo.ObjectContexts}
   [:custom
    {:optional true,
     :getter-doc "Returns the map of user-defined object contexts."}
    [:map-of [:or simple-keyword? [:string {:min 1}]]
     :gcp.storage/BlobInfo.ObjectCustomContextPayload]]])

(defn
  ^BlobInfo$ObjectCustomContextPayload ObjectCustomContextPayload-from-edn
  [arg]
  (let [builder (BlobInfo$ObjectCustomContextPayload/newBuilder)]
    (when (some? (get arg :createTime))
      (.setCreateTime builder (get arg :createTime)))
    (when (some? (get arg :updateTime))
      (.setUpdateTime builder (get arg :updateTime)))
    (when (some? (get arg :value)) (.setValue builder (get arg :value)))
    (.build builder)))

(defn
  ObjectCustomContextPayload-to-edn
  [^BlobInfo$ObjectCustomContextPayload arg]
  (when arg
    (cond-> {}
      (.getCreateTime arg) (assoc :createTime (.getCreateTime arg))
      (.getUpdateTime arg) (assoc :updateTime (.getUpdateTime arg))
      (some->> (.getValue arg)
               (not= ""))
        (assoc :value (.getValue arg)))))

(def ObjectCustomContextPayload-schema
  [:map
   {:closed true,
    :doc "Represents the payload of a user-defined object context.",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/BlobInfo.ObjectCustomContextPayload}
   [:createTime {:optional true} :OffsetDateTime]
   [:updateTime {:optional true} :OffsetDateTime]
   [:value {:optional true} [:string {:min 1}]]])

(defn ^BlobInfo from-edn
  [arg]
  (global/strict! :gcp.storage/BlobInfo arg)
  (let [builder (BlobInfo/newBuilder (BlobId/from-edn (get arg :blobId)))]
    (when (seq (get arg :acl))
      (.setAcl builder (map Acl/from-edn (get arg :acl))))
    (when (some? (get arg :cacheControl))
      (.setCacheControl builder (get arg :cacheControl)))
    (when (some? (get arg :contentDisposition))
      (.setContentDisposition builder (get arg :contentDisposition)))
    (when (some? (get arg :contentEncoding))
      (.setContentEncoding builder (get arg :contentEncoding)))
    (when (some? (get arg :contentLanguage))
      (.setContentLanguage builder (get arg :contentLanguage)))
    (when (some? (get arg :contentType))
      (.setContentType builder (get arg :contentType)))
    (when (some? (get arg :contexts))
      (.setContexts builder (ObjectContexts-from-edn (get arg :contexts))))
    (when (some? (get arg :crc32c)) (.setCrc32c builder (get arg :crc32c)))
    (when (some? (get arg :crc32cFromHexString))
      (.setCrc32cFromHexString builder (get arg :crc32cFromHexString)))
    (when (some? (get arg :customTimeOffsetDateTime))
      (.setCustomTimeOffsetDateTime builder
                                    (get arg :customTimeOffsetDateTime)))
    (when (some? (get arg :eventBasedHold))
      (.setEventBasedHold builder (get arg :eventBasedHold)))
    (when (some? (get arg :md5)) (.setMd5 builder (get arg :md5)))
    (when (some? (get arg :md5FromHexString))
      (.setMd5FromHexString builder (get arg :md5FromHexString)))
    (when (seq (get arg :metadata))
      (.setMetadata
        builder
        (into {} (map (fn [[k v]] [(name k) v])) (get arg :metadata))))
    (when (some? (get arg :retention))
      (.setRetention builder (Retention-from-edn (get arg :retention))))
    (when (some? (get arg :storageClass))
      (.setStorageClass builder (StorageClass/valueOf (get arg :storageClass))))
    (when (some? (get arg :temporaryHold))
      (.setTemporaryHold builder (get arg :temporaryHold)))
    (when (some? (get arg :timeStorageClassUpdatedOffsetDateTime))
      (.setTimeStorageClassUpdatedOffsetDateTime
        builder
        (get arg :timeStorageClassUpdatedOffsetDateTime)))
    (.build builder)))

(defn to-edn
  [^BlobInfo arg]
  {:post [(global/strict! :gcp.storage/BlobInfo %)]}
  (when arg
    (cond-> {:blobId (BlobId/to-edn (.getBlobId arg))}
      (seq (.getAcl arg)) (assoc :acl (map Acl/to-edn (.getAcl arg)))
      (some->> (.getBucket arg)
               (not= ""))
        (assoc :bucket (.getBucket arg))
      (some->> (.getCacheControl arg)
               (not= ""))
        (assoc :cacheControl (.getCacheControl arg))
      (.getComponentCount arg) (assoc :componentCount (.getComponentCount arg))
      (some->> (.getContentDisposition arg)
               (not= ""))
        (assoc :contentDisposition (.getContentDisposition arg))
      (some->> (.getContentEncoding arg)
               (not= ""))
        (assoc :contentEncoding (.getContentEncoding arg))
      (some->> (.getContentLanguage arg)
               (not= ""))
        (assoc :contentLanguage (.getContentLanguage arg))
      (some->> (.getContentType arg)
               (not= ""))
        (assoc :contentType (.getContentType arg))
      (.getContexts arg) (assoc :contexts
                           (ObjectContexts-to-edn (.getContexts arg)))
      (some->> (.getCrc32c arg)
               (not= ""))
        (assoc :crc32c (.getCrc32c arg))
      (.getCreateTimeOffsetDateTime arg) (assoc :createTime
                                           (.getCreateTimeOffsetDateTime arg))
      (.getCustomTimeOffsetDateTime arg) (assoc :customTime
                                           (.getCustomTimeOffsetDateTime arg))
      (.getCustomerEncryption arg) (assoc :customerEncryption
                                     (CustomerEncryption-to-edn
                                       (.getCustomerEncryption arg)))
      (.getDeleteTimeOffsetDateTime arg) (assoc :deleteTime
                                           (.getDeleteTimeOffsetDateTime arg))
      (.isDirectory arg) (assoc :directory (.isDirectory arg))
      (some->> (.getEtag arg)
               (not= ""))
        (assoc :etag (.getEtag arg))
      (.getEventBasedHold arg) (assoc :eventBasedHold (.getEventBasedHold arg))
      (some->> (.getGeneratedId arg)
               (not= ""))
        (assoc :generatedId (.getGeneratedId arg))
      (.getGeneration arg) (assoc :generation (.getGeneration arg))
      (.getHardDeleteTime arg) (assoc :hardDeleteTime (.getHardDeleteTime arg))
      (some->> (.getKmsKeyName arg)
               (not= ""))
        (assoc :kmsKeyName (.getKmsKeyName arg))
      (some->> (.getMd5 arg)
               (not= ""))
        (assoc :md5 (.getMd5 arg))
      (some->> (.getMediaLink arg)
               (not= ""))
        (assoc :mediaLink (.getMediaLink arg))
      (seq (.getMetadata arg))
        (assoc :metadata
          (into {} (map (fn [[k v]] [(keyword k) v])) (.getMetadata arg)))
      (.getMetageneration arg) (assoc :metageneration (.getMetageneration arg))
      (some->> (.getName arg)
               (not= ""))
        (assoc :name (.getName arg))
      (.getOwner arg) (assoc :owner (Acl/Entity-to-edn (.getOwner arg)))
      (.getRetention arg) (assoc :retention
                            (Retention-to-edn (.getRetention arg)))
      (.getRetentionExpirationTimeOffsetDateTime arg)
        (assoc :retentionExpirationTime
          (.getRetentionExpirationTimeOffsetDateTime arg))
      (some->> (.getSelfLink arg)
               (not= ""))
        (assoc :selfLink (.getSelfLink arg))
      (.getSize arg) (assoc :size (.getSize arg))
      (.getSoftDeleteTime arg) (assoc :softDeleteTime (.getSoftDeleteTime arg))
      (.getStorageClass arg) (assoc :storageClass
                               (.name (.getStorageClass arg)))
      (.getTemporaryHold arg) (assoc :temporaryHold (.getTemporaryHold arg))
      (.getTimeStorageClassUpdatedOffsetDateTime arg)
        (assoc :timeStorageClassUpdated
          (.getTimeStorageClassUpdatedOffsetDateTime arg))
      (.getUpdateTimeOffsetDateTime arg)
        (assoc :updateTime (.getUpdateTimeOffsetDateTime arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Information about an object in Google Cloud Storage. A {@code BlobInfo} object includes the\n{@code BlobId} instance and the set of properties, such as the blob's access control\nconfiguration, user provided metadata, the CRC32C checksum, etc. Instances of this class are used\nto create a new object in Google Cloud Storage or update the properties of an existing object. To\ndeal with existing Storage objects the API includes the {@link Blob} class which extends {@code\nBlobInfo} and declares methods to perform operations on the object. Neither {@code BlobInfo} nor\n{@code Blob} instances keep the object content, just the object properties.\n\n<p>Example of usage {@code BlobInfo} to create an object in Google Cloud Storage:\n\n<pre>{@code\nBlobId blobId = BlobId.of(bucketName, blobName);\nBlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(\"text/plain\").build();\nBlob blob = storage.create(blobInfo, \"Hello, world\".getBytes(StandardCharsets.UTF_8));\n}</pre>\n\n@see <a href=\"https://cloud.google.com/storage/docs/concepts-techniques#concepts\">Concepts and\n    Terminology</a>",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.storage/BlobInfo}
   [:acl
    {:optional true,
     :getter-doc
       "Returns the blob's access control configuration.\n\n@see <a href=\"https://cloud.google.com/storage/docs/access-control#About-Access-Control-Lists\">\n    About Access Control Lists</a>",
     :setter-doc
       "Sets the blob's access control configuration.\n\n@see <a\n    href=\"https://cloud.google.com/storage/docs/access-control#About-Access-Control-Lists\">\n    About Access Control Lists</a>"}
    [:sequential {:min 1} :gcp.storage/Acl]]
   [:blobId {:getter-doc "Returns the blob's identity."} :gcp.storage/BlobId]
   [:bucket
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the name of the containing bucket."}
    [:string {:min 1}]]
   [:cacheControl
    {:optional true,
     :getter-doc
       "Returns the blob's data cache control.\n\n@see <a href=\"https://tools.ietf.org/html/rfc7234#section-5.2\">Cache-Control</a>",
     :setter-doc
       "Sets the blob's data cache control.\n\n@see <a href=\"https://tools.ietf.org/html/rfc7234#section-5.2\">Cache-Control</a>"}
    [:string {:min 1}]]
   [:componentCount
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the number of components that make up this blob. Components are accumulated through the\n{@link Storage#compose(Storage.ComposeRequest)} operation and are limited to a count of 1024,\ncounting 1 for each non-composite component blob and componentCount for each composite\ncomponent blob. This value is set only for composite blobs.\n\n@see <a href=\"https://cloud.google.com/storage/docs/composite-objects#_Count\">Component Count\n    Property</a>"}
    :i32]
   [:contentDisposition
    {:optional true,
     :getter-doc
       "Returns the blob's data content disposition.\n\n@see <a href=\"https://tools.ietf.org/html/rfc6266\">Content-Disposition</a>",
     :setter-doc
       "Sets the blob's data content disposition.\n\n@see <a href=\"https://tools.ietf.org/html/rfc6266\">Content-Disposition</a>"}
    [:string {:min 1}]]
   [:contentEncoding
    {:optional true,
     :getter-doc
       "Returns the blob's data content encoding.\n\n@see <a href=\"https://tools.ietf.org/html/rfc7231#section-3.1.2.2\">Content-Encoding</a>",
     :setter-doc
       "Sets the blob's data content encoding.\n\n@see <a href=\"https://tools.ietf.org/html/rfc7231#section-3.1.2.2\">Content-Encoding</a>"}
    [:string {:min 1}]]
   [:contentLanguage
    {:optional true,
     :getter-doc
       "Returns the blob's data content language.\n\n@see <a href=\"http://tools.ietf.org/html/bcp47\">Content-Language</a>",
     :setter-doc
       "Sets the blob's data content language.\n\n@see <a href=\"http://tools.ietf.org/html/bcp47\">Content-Language</a>"}
    [:string {:min 1}]]
   [:contentType
    {:optional true,
     :getter-doc
       "Returns the blob's data content type.\n\n@see <a href=\"https://tools.ietf.org/html/rfc2616#section-14.17\">Content-Type</a>",
     :setter-doc
       "Sets the blob's data content type.\n\n@see <a href=\"https://tools.ietf.org/html/rfc2616#section-14.17\">Content-Type</a>"}
    [:string {:min 1}]]
   [:contexts {:optional true} [:ref :gcp.storage/BlobInfo.ObjectContexts]]
   [:crc32c
    {:optional true,
     :getter-doc
       "Returns the CRC32C checksum of blob's data as described in <a\nhref=\"http://tools.ietf.org/html/rfc4960#appendix-B\">RFC 4960, Appendix B;</a> encoded in\nbase64 in big-endian order.\n\n@see <a href=\"https://cloud.google.com/storage/docs/hashes-etags#_JSONAPI\">Hashes and ETags:\n    Best Practices</a>",
     :setter-doc
       "Sets the CRC32C checksum of blob's data as described in <a\nhref=\"http://tools.ietf.org/html/rfc4960#appendix-B\">RFC 4960, Appendix B;</a> encoded in\nbase64 in big-endian order.\n\n@see <a href=\"https://cloud.google.com/storage/docs/hashes-etags#_JSONAPI\">Hashes and ETags:\n    Best Practices</a>"}
    [:string {:min 1}]]
   [:createTime
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the creation time of the blob."} :OffsetDateTime]
   [:customTime
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the custom time specified by the user for an object."}
    :OffsetDateTime]
   [:customerEncryption
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns information on the customer-supplied encryption key, if the blob is encrypted using\nsuch a key."}
    [:ref :gcp.storage/BlobInfo.CustomerEncryption]]
   [:deleteTime
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the deletion time of the blob."} :OffsetDateTime]
   [:directory
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns {@code true} if the current blob represents a directory. This can only happen if the\nblob is returned by {@link Storage#list(String, Storage.BlobListOption...)} when the {@link\nStorage.BlobListOption#currentDirectory()} option is used. When this is the case only {@link\n#getBlobId()} and {@link #getSize()} are set for the current blob: {@link BlobId#getName()}\nends with the '/' character, {@link BlobId#getGeneration()} returns {@code null} and {@link\n#getSize()} is {@code 0}."}
    :boolean]
   [:etag
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns HTTP 1.1 Entity tag for the blob.\n\n@see <a href=\"http://tools.ietf.org/html/rfc2616#section-3.11\">Entity Tags</a>"}
    [:string {:min 1}]]
   [:eventBasedHold
    {:optional true,
     :getter-doc
       "Returns a {@code Boolean} with either {@code true}, {@code null} and in certain cases {@code\nfalse}.\n\n<p>Case 1: {@code true} the field {@link\ncom.google.cloud.storage.Storage.BlobField#EVENT_BASED_HOLD} is selected in a {@link\nStorage#get(BlobId, Storage.BlobGetOption...)} and event-based hold for the blob is enabled.\n\n<p>Case 2.1: {@code null} the field {@link\ncom.google.cloud.storage.Storage.BlobField#EVENT_BASED_HOLD} is selected in a {@link\nStorage#get(BlobId, Storage.BlobGetOption...)}, but event-based hold for the blob is not\nenabled. This case can be considered implicitly {@code false}.\n\n<p>Case 2.2: {@code null} the field {@link\ncom.google.cloud.storage.Storage.BlobField#EVENT_BASED_HOLD} is not selected in a {@link\nStorage#get(BlobId, Storage.BlobGetOption...)}, and the state for this field is unknown.\n\n<p>Case 3: {@code false} event-based hold is explicitly set to false using in a {@link\nBuilder#setEventBasedHold(Boolean)} client side for a follow-up request e.g. {@link\nStorage#update(BlobInfo, Storage.BlobTargetOption...)} in which case the value of event-based\nhold will remain {@code false} for the given instance.",
     :setter-doc "Sets the blob's event-based hold."} :boolean]
   [:generatedId
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the service-generated for the blob."}
    [:string {:min 1}]]
   [:generation
    {:optional true,
     :read-only? true,
     :getter-doc "Returns blob's data generation. Used for blob versioning."}
    :i64]
   [:hardDeleteTime
    {:optional true,
     :read-only? true,
     :getter-doc
       "If this object has been soft-deleted, returns the time at which it will be permanently deleted."}
    :OffsetDateTime]
   [:kmsKeyName
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the Cloud KMS key used to encrypt the blob, if any."}
    [:string {:min 1}]]
   [:md5
    {:optional true,
     :getter-doc
       "Returns the MD5 hash of blob's data encoded in base64.\n\n@see <a href=\"https://cloud.google.com/storage/docs/hashes-etags#_JSONAPI\">Hashes and ETags:\n    Best Practices</a>",
     :setter-doc
       "Sets the MD5 hash of blob's data. MD5 value must be encoded in base64.\n\n@see <a href=\"https://cloud.google.com/storage/docs/hashes-etags#_JSONAPI\">Hashes and ETags:\n    Best Practices</a>"}
    [:string {:min 1}]]
   [:mediaLink
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the blob's media download link."} [:string {:min 1}]]
   [:metadata
    {:optional true,
     :getter-doc "Returns blob's user provided metadata.",
     :setter-doc "Sets the blob's user provided metadata."}
    [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
   [:metageneration
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns blob's metageneration. Used for preconditions and for detecting changes in metadata. A\nmetageneration number is only meaningful in the context of a particular generation of a\nparticular blob."}
    :i64]
   [:name
    {:optional true, :read-only? true, :getter-doc "Returns the blob's name."}
    [:string {:min 1}]]
   [:owner
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the blob's owner. This will always be the uploader of the blob."}
    :gcp.storage/Acl.Entity]
   [:retention
    {:optional true, :getter-doc "Returns the object's Retention policy."}
    [:ref :gcp.storage/BlobInfo.Retention]]
   [:retentionExpirationTime
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the retention expiration time of the blob, if a retention period is defined. If\nretention period is not defined this value returns {@code null}"}
    :OffsetDateTime]
   [:selfLink
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the URI of this blob as a string."}
    [:string {:min 1}]]
   [:size
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the content length of the data in bytes.\n\n@see <a href=\"https://tools.ietf.org/html/rfc2616#section-14.13\">Content-Length</a>"}
    :i64]
   [:softDeleteTime
    {:optional true,
     :read-only? true,
     :getter-doc
       "If this object has been soft-deleted, returns the time it was soft-deleted."}
    :OffsetDateTime]
   [:storageClass
    {:optional true,
     :getter-doc "Returns the storage class of the blob.",
     :setter-doc "Sets the blob's storage class."}
    [:enum {:closed true} "STANDARD" "NEARLINE" "COLDLINE" "ARCHIVE" "REGIONAL"
     "MULTI_REGIONAL" "DURABLE_REDUCED_AVAILABILITY"]]
   [:temporaryHold
    {:optional true,
     :getter-doc
       "Returns a {@code Boolean} with either {@code true}, {@code null} and in certain cases {@code\nfalse}.\n\n<p>Case 1: {@code true} the field {@link\ncom.google.cloud.storage.Storage.BlobField#TEMPORARY_HOLD} is selected in a {@link\nStorage#get(BlobId, Storage.BlobGetOption...)} and temporary hold for the blob is enabled.\n\n<p>Case 2.1: {@code null} the field {@link\ncom.google.cloud.storage.Storage.BlobField#TEMPORARY_HOLD} is selected in a {@link\nStorage#get(BlobId, Storage.BlobGetOption...)}, but temporary hold for the blob is not enabled.\nThis case can be considered implicitly {@code false}.\n\n<p>Case 2.2: {@code null} the field {@link\ncom.google.cloud.storage.Storage.BlobField#TEMPORARY_HOLD} is not selected in a {@link\nStorage#get(BlobId, Storage.BlobGetOption...)}, and the state for this field is unknown.\n\n<p>Case 3: {@code false} event-based hold is explicitly set to false using in a {@link\nBuilder#setEventBasedHold(Boolean)} client side for a follow-up request e.g. {@link\nStorage#update(BlobInfo, Storage.BlobTargetOption...)} in which case the value of temporary\nhold will remain {@code false} for the given instance.",
     :setter-doc "Sets the blob's temporary hold."} :boolean]
   [:timeStorageClassUpdated
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the time that the object's storage class was last changed or the time of the object\ncreation."}
    :OffsetDateTime]
   [:updateTime
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the last modification time of the blob's metadata."}
    :OffsetDateTime]])

(global/include-schema-registry!
  (with-meta {:gcp.storage/BlobInfo schema,
              :gcp.storage/BlobInfo.CustomerEncryption
                CustomerEncryption-schema,
              :gcp.storage/BlobInfo.ObjectContexts ObjectContexts-schema,
              :gcp.storage/BlobInfo.ObjectCustomContextPayload
                ObjectCustomContextPayload-schema,
              :gcp.storage/BlobInfo.Retention Retention-schema,
              :gcp.storage/BlobInfo.Retention.Mode Retention$Mode-schema}
    {:gcp.global/name "gcp.storage.BlobInfo"}))