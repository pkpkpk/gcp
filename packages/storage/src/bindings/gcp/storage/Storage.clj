;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.storage.Storage
  {:doc
     "An interface for Google Cloud Storage.\n\n@see <a href=\"https://cloud.google.com/storage/docs\">Google Cloud Storage</a>"
   :file-git-sha "f14fb68fa236c9584c9843d242bbdffff4944687"
   :fqcn "com.google.cloud.storage.Storage"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "215ec381-0f5f-5884-ab6d-eb0bb246cd16"
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :reason :client
      :skipped true
      :timestamp "2026-04-03T18:33:11.159789754Z"}}
  (:require [gcp.foreign.com.google.auth :as auth]
            [gcp.global :as global]
            [gcp.storage.BlobId :as BlobId]
            [gcp.storage.BlobInfo :as BlobInfo]
            [gcp.storage.ServiceAccount :as ServiceAccount])
  (:import [com.google.auth ServiceAccountSigner]
           [com.google.cloud.storage HttpMethod Storage Storage$BlobField
            Storage$BlobGetOption Storage$BlobListOption
            Storage$BlobRestoreOption Storage$BlobSourceOption
            Storage$BlobTargetOption Storage$BlobWriteOption Storage$BucketField
            Storage$BucketGetOption Storage$BucketListOption
            Storage$BucketSourceOption Storage$BucketTargetOption
            Storage$ComposeRequest Storage$ComposeRequest$Builder
            Storage$ComposeRequest$SourceBlob Storage$CopyRequest
            Storage$CopyRequest$Builder Storage$CreateHmacKeyOption
            Storage$DeleteHmacKeyOption Storage$GetHmacKeyOption
            Storage$ListHmacKeysOption Storage$MoveBlobRequest
            Storage$MoveBlobRequest$Builder Storage$PostPolicyV4Option
            Storage$PostPolicyV4Option$Option Storage$PredefinedAcl
            Storage$SignUrlOption Storage$SignUrlOption$Option
            Storage$SignUrlOption$SignatureVersion Storage$UpdateHmacKeyOption
            Storage$UriScheme]))

(def PredefinedAcl-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.storage/Storage.PredefinedAcl} "AUTHENTICATED_READ"
   "ALL_AUTHENTICATED_USERS" "PRIVATE" "PROJECT_PRIVATE" "PUBLIC_READ"
   "PUBLIC_READ_WRITE" "BUCKET_OWNER_READ" "BUCKET_OWNER_FULL_CONTROL"])

(def BucketField-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.storage/Storage.BucketField} "ID" "SELF_LINK" "NAME"
   "TIME_CREATED" "METAGENERATION" "ACL" "DEFAULT_OBJECT_ACL" "OWNER" "LABELS"
   "LOCATION" "LOCATION_TYPE" "WEBSITE" "VERSIONING" "CORS" "LIFECYCLE"
   "STORAGE_CLASS" "ETAG" "ENCRYPTION" "BILLING" "DEFAULT_EVENT_BASED_HOLD"
   "RETENTION_POLICY" "IAMCONFIGURATION" "LOGGING" "UPDATED" "RPO"
   "CUSTOM_PLACEMENT_CONFIG" "AUTOCLASS" "HIERARCHICAL_NAMESPACE"
   "OBJECT_RETENTION" "SOFT_DELETE_POLICY" "PROJECT" "IP_FILTER"])

(def BlobField-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.storage/Storage.BlobField} "ACL" "BUCKET" "CACHE_CONTROL"
   "COMPONENT_COUNT" "CONTENT_DISPOSITION" "CONTENT_ENCODING" "CONTENT_LANGUAGE"
   "CONTENT_TYPE" "CRC32C" "ETAG" "GENERATION" "ID" "KIND" "MD5HASH"
   "MEDIA_LINK" "METADATA" "METAGENERATION" "NAME" "OWNER" "SELF_LINK" "SIZE"
   "STORAGE_CLASS" "TIME_DELETED" "TIME_CREATED" "KMS_KEY_NAME"
   "EVENT_BASED_HOLD" "TEMPORARY_HOLD" "RETENTION_EXPIRATION_TIME" "UPDATED"
   "CUSTOM_TIME" "TIME_STORAGE_CLASS_UPDATED" "CUSTOMER_ENCRYPTION" "RETENTION"
   "SOFT_DELETE_TIME" "HARD_DELETE_TIME" "OBJECT_CONTEXTS"])

(def UriScheme-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.storage/Storage.UriScheme} "HTTP" "HTTPS"])

(do
  (defn ^Storage$BucketTargetOption/1 BucketTargetOption-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BucketTargetOption arg)
    (into-array
      Storage$BucketTargetOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :predefinedAcl (conj acc
                                 (Storage$BucketTargetOption/predefinedAcl
                                   (Storage$PredefinedAcl/valueOf v)))
            :predefinedDefaultObjectAcl
              (conj acc
                    (Storage$BucketTargetOption/predefinedDefaultObjectAcl
                      (Storage$PredefinedAcl/valueOf v)))
            :enableObjectRetention
              (conj acc (Storage$BucketTargetOption/enableObjectRetention v))
            :metagenerationMatch
              (if v
                (clojure.core/conj
                  acc
                  (Storage$BucketTargetOption/metagenerationMatch))
                acc)
            :metagenerationNotMatch
              (if v
                (clojure.core/conj
                  acc
                  (Storage$BucketTargetOption/metagenerationNotMatch))
                acc)
            :userProject (conj acc (Storage$BucketTargetOption/userProject v))
            :projection (conj acc (Storage$BucketTargetOption/projection v))
            :extraHeaders (conj acc
                                (Storage$BucketTargetOption/extraHeaders
                                  (into {} (map (fn [[k v]] [(name k) v])) v)))
            acc))
        []
        arg)))
  (defn ^Storage$BucketTargetOption BucketTargetOption-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BucketTargetOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :predefinedAcl (reduced (Storage$BucketTargetOption/predefinedAcl
                                    (Storage$PredefinedAcl/valueOf v)))
          :predefinedDefaultObjectAcl
            (reduced (Storage$BucketTargetOption/predefinedDefaultObjectAcl
                       (Storage$PredefinedAcl/valueOf v)))
          :enableObjectRetention
            (reduced (Storage$BucketTargetOption/enableObjectRetention v))
          :metagenerationMatch
            (if v
              (reduced (Storage$BucketTargetOption/metagenerationMatch))
              acc)
          :metagenerationNotMatch
            (if v
              (reduced (Storage$BucketTargetOption/metagenerationNotMatch))
              acc)
          :userProject (reduced (Storage$BucketTargetOption/userProject v))
          :projection (reduced (Storage$BucketTargetOption/projection v))
          :extraHeaders (reduced (Storage$BucketTargetOption/extraHeaders
                                   (into {} (map (fn [[k v]] [(name k) v])) v)))
          acc))
      nil
      arg)))

(def BucketTargetOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying bucket target options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.BucketTargetOption}
   [:map {:closed true}
    [:predefinedAcl
     {:optional true,
      :doc
        "Returns an option for specifying bucket's predefined ACL configuration."}
     [:enum {:closed true} "AUTHENTICATED_READ" "ALL_AUTHENTICATED_USERS"
      "PRIVATE" "PROJECT_PRIVATE" "PUBLIC_READ" "PUBLIC_READ_WRITE"
      "BUCKET_OWNER_READ" "BUCKET_OWNER_FULL_CONTROL"]]
    [:predefinedDefaultObjectAcl
     {:optional true,
      :doc
        "Returns an option for specifying bucket's default ACL configuration for blobs."}
     [:enum {:closed true} "AUTHENTICATED_READ" "ALL_AUTHENTICATED_USERS"
      "PRIVATE" "PROJECT_PRIVATE" "PUBLIC_READ" "PUBLIC_READ_WRITE"
      "BUCKET_OWNER_READ" "BUCKET_OWNER_FULL_CONTROL"]]
    [:enableObjectRetention
     {:optional true,
      :doc
        "Returns an option for enabling Object Retention on this bucket. Enabling this will create an\nObjectRetention object in the created bucket (You must use this option, creating your own\nObjectRetention object in the request won't work)."}
     :boolean]
    [:metagenerationMatch
     {:optional true,
      :doc
        "Returns an option for bucket's metageneration match. If this option is used the request will\nfail if metageneration does not match."}
     :boolean]
    [:metagenerationNotMatch
     {:optional true,
      :doc
        "Returns an option for bucket's metageneration mismatch. If this option is used the request\nwill fail if metageneration matches."}
     :boolean]
    [:userProject
     {:optional true,
      :doc
        "Returns an option to define the billing user project. This option is required by buckets with\n`requester_pays` flag enabled to assign operation costs."}
     [:string {:min 1}]]
    [:projection
     {:optional true,
      :doc
        "Returns an option to define the projection in the API request. In some cases this option may\nbe needed to be set to `noAcl` to omit ACL data from the response. The default value is\n`full`\n\n@see <a href=\"https://cloud.google.com/storage/docs/json_api/v1/buckets/patch\">Buckets:\n    patch</a>"}
     [:string {:min 1}]]
    [:extraHeaders
     {:optional true,
      :doc
        "A set of extra headers to be set for all requests performed within the scope of the operation\nthis option is passed to (a get, read, resumable upload etc).\n\n<p>If the same header name is specified across multiple options provided to a method, the\nfirst occurrence will be the value included in the request(s).\n\n<p>The following headers are not allowed to be specified, and will result in an {@link\nIllegalArgumentException}.\n\n<ol>\n  <li>{@code Accept-Encoding}\n  <li>{@code Cache-Control}\n  <li>{@code Connection}\n  <li>{@code Content-ID}\n  <li>{@code Content-Length}\n  <li>{@code Content-Range}\n  <li>{@code Content-Transfer-Encoding}\n  <li>{@code Content-Type}\n  <li>{@code Date}\n  <li>{@code ETag}\n  <li>{@code If-Match}\n  <li>{@code If-None-Match}\n  <li>{@code Keep-Alive}\n  <li>{@code Range}\n  <li>{@code TE}\n  <li>{@code Trailer}\n  <li>{@code Transfer-Encoding}\n  <li>{@code User-Agent}\n  <li>{@code X-Goog-Api-Client}\n  <li>{@code X-Goog-Content-Length-Range}\n  <li>{@code X-Goog-Copy-Source-Encryption-Algorithm}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Encryption-Algorithm}\n  <li>{@code X-Goog-Encryption-Key}\n  <li>{@code X-Goog-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Gcs-Idempotency-Token}\n  <li>{@code X-Goog-Meta-*}\n  <li>{@code X-Goog-User-Project}\n  <li>{@code X-HTTP-Method-Override}\n  <li>{@code X-Upload-Content-Length}\n  <li>{@code X-Upload-Content-Type}\n</ol>\n\n@since 2.49.0"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]]])

(do
  (defn ^Storage$BucketSourceOption/1 BucketSourceOption-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BucketSourceOption arg)
    (into-array
      Storage$BucketSourceOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :metagenerationMatch
              (conj acc
                    (Storage$BucketSourceOption/metagenerationMatch (long v)))
            :metagenerationNotMatch
              (conj acc
                    (Storage$BucketSourceOption/metagenerationNotMatch (long
                                                                         v)))
            :userProject (conj acc (Storage$BucketSourceOption/userProject v))
            :requestedPolicyVersion
              (conj acc
                    (Storage$BucketSourceOption/requestedPolicyVersion (long
                                                                         v)))
            :extraHeaders (conj acc
                                (Storage$BucketSourceOption/extraHeaders
                                  (into {} (map (fn [[k v]] [(name k) v])) v)))
            acc))
        []
        arg)))
  (defn ^Storage$BucketSourceOption BucketSourceOption-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BucketSourceOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :metagenerationMatch
            (reduced (Storage$BucketSourceOption/metagenerationMatch (long v)))
          :metagenerationNotMatch
            (reduced (Storage$BucketSourceOption/metagenerationNotMatch (long
                                                                          v)))
          :userProject (reduced (Storage$BucketSourceOption/userProject v))
          :requestedPolicyVersion
            (reduced (Storage$BucketSourceOption/requestedPolicyVersion (long
                                                                          v)))
          :extraHeaders (reduced (Storage$BucketSourceOption/extraHeaders
                                   (into {} (map (fn [[k v]] [(name k) v])) v)))
          acc))
      nil
      arg)))

(def BucketSourceOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying bucket source options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.BucketSourceOption}
   [:map {:closed true}
    [:metagenerationMatch
     {:optional true,
      :doc
        "Returns an option for bucket's metageneration match. If this option is used the request will\nfail if bucket's metageneration does not match the provided value."}
     :i64]
    [:metagenerationNotMatch
     {:optional true,
      :doc
        "Returns an option for bucket's metageneration mismatch. If this option is used the request\nwill fail if bucket's metageneration matches the provided value."}
     :i64]
    [:userProject
     {:optional true,
      :doc
        "Returns an option for bucket's billing user project. This option is only used by the buckets\nwith 'requester_pays' flag."}
     [:string {:min 1}]] [:requestedPolicyVersion {:optional true} :i64]
    [:extraHeaders
     {:optional true,
      :doc
        "A set of extra headers to be set for all requests performed within the scope of the operation\nthis option is passed to (a get, read, resumable upload etc).\n\n<p>If the same header name is specified across multiple options provided to a method, the\nfirst occurrence will be the value included in the request(s).\n\n<p>The following headers are not allowed to be specified, and will result in an {@link\nIllegalArgumentException}.\n\n<ol>\n  <li>{@code Accept-Encoding}\n  <li>{@code Cache-Control}\n  <li>{@code Connection}\n  <li>{@code Content-ID}\n  <li>{@code Content-Length}\n  <li>{@code Content-Range}\n  <li>{@code Content-Transfer-Encoding}\n  <li>{@code Content-Type}\n  <li>{@code Date}\n  <li>{@code ETag}\n  <li>{@code If-Match}\n  <li>{@code If-None-Match}\n  <li>{@code Keep-Alive}\n  <li>{@code Range}\n  <li>{@code TE}\n  <li>{@code Trailer}\n  <li>{@code Transfer-Encoding}\n  <li>{@code User-Agent}\n  <li>{@code X-Goog-Api-Client}\n  <li>{@code X-Goog-Content-Length-Range}\n  <li>{@code X-Goog-Copy-Source-Encryption-Algorithm}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Encryption-Algorithm}\n  <li>{@code X-Goog-Encryption-Key}\n  <li>{@code X-Goog-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Gcs-Idempotency-Token}\n  <li>{@code X-Goog-Meta-*}\n  <li>{@code X-Goog-User-Project}\n  <li>{@code X-HTTP-Method-Override}\n  <li>{@code X-Upload-Content-Length}\n  <li>{@code X-Upload-Content-Type}\n</ol>\n\n@since 2.49.0"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]]])

(do
  (defn ^Storage$ListHmacKeysOption/1 ListHmacKeysOption-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.ListHmacKeysOption arg)
    (into-array
      Storage$ListHmacKeysOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :serviceAccount (conj acc
                                  (Storage$ListHmacKeysOption/serviceAccount
                                    (ServiceAccount/from-edn v)))
            :maxResults (conj acc
                              (Storage$ListHmacKeysOption/maxResults (long v)))
            :pageToken (conj acc (Storage$ListHmacKeysOption/pageToken v))
            :showDeletedKeys
              (conj acc (Storage$ListHmacKeysOption/showDeletedKeys v))
            :userProject (conj acc (Storage$ListHmacKeysOption/userProject v))
            :projectId (conj acc (Storage$ListHmacKeysOption/projectId v))
            :extraHeaders (conj acc
                                (Storage$ListHmacKeysOption/extraHeaders
                                  (into {} (map (fn [[k v]] [(name k) v])) v)))
            acc))
        []
        arg)))
  (defn ^Storage$ListHmacKeysOption ListHmacKeysOption-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.ListHmacKeysOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :serviceAccount (reduced (Storage$ListHmacKeysOption/serviceAccount
                                     (ServiceAccount/from-edn v)))
          :maxResults (reduced (Storage$ListHmacKeysOption/maxResults (long v)))
          :pageToken (reduced (Storage$ListHmacKeysOption/pageToken v))
          :showDeletedKeys (reduced (Storage$ListHmacKeysOption/showDeletedKeys
                                      v))
          :userProject (reduced (Storage$ListHmacKeysOption/userProject v))
          :projectId (reduced (Storage$ListHmacKeysOption/projectId v))
          :extraHeaders (reduced (Storage$ListHmacKeysOption/extraHeaders
                                   (into {} (map (fn [[k v]] [(name k) v])) v)))
          acc))
      nil
      arg)))

(def ListHmacKeysOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying listHmacKeys options",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.ListHmacKeysOption}
   [:map {:closed true}
    [:serviceAccount
     {:optional true,
      :doc
        "Returns an option for the Service Account whose keys to list. If this option is not used,\nkeys for all accounts will be listed."}
     :gcp.storage/ServiceAccount]
    [:maxResults
     {:optional true,
      :doc
        "Returns an option for the maximum amount of HMAC keys returned per page."}
     :i64]
    [:pageToken
     {:optional true,
      :doc
        "Returns an option to specify the page token from which to start listing HMAC keys."}
     [:string {:min 1}]]
    [:showDeletedKeys
     {:optional true,
      :doc
        "Returns an option to specify whether to show deleted keys in the result. This option is false\nby default."}
     :boolean]
    [:userProject
     {:optional true,
      :doc
        "Returns an option to specify the project to be billed for this request. Required for\nRequester Pays buckets."}
     [:string {:min 1}]]
    [:projectId
     {:optional true,
      :doc
        "Returns an option to specify the Project ID for this request. If not specified, defaults to\nApplication Default Credentials."}
     [:string {:min 1}]]
    [:extraHeaders
     {:optional true,
      :doc
        "A set of extra headers to be set for all requests performed within the scope of the operation\nthis option is passed to (a get, read, resumable upload etc).\n\n<p>If the same header name is specified across multiple options provided to a method, the\nfirst occurrence will be the value included in the request(s).\n\n<p>The following headers are not allowed to be specified, and will result in an {@link\nIllegalArgumentException}.\n\n<ol>\n  <li>{@code Accept-Encoding}\n  <li>{@code Cache-Control}\n  <li>{@code Connection}\n  <li>{@code Content-ID}\n  <li>{@code Content-Length}\n  <li>{@code Content-Range}\n  <li>{@code Content-Transfer-Encoding}\n  <li>{@code Content-Type}\n  <li>{@code Date}\n  <li>{@code ETag}\n  <li>{@code If-Match}\n  <li>{@code If-None-Match}\n  <li>{@code Keep-Alive}\n  <li>{@code Range}\n  <li>{@code TE}\n  <li>{@code Trailer}\n  <li>{@code Transfer-Encoding}\n  <li>{@code User-Agent}\n  <li>{@code X-Goog-Api-Client}\n  <li>{@code X-Goog-Content-Length-Range}\n  <li>{@code X-Goog-Copy-Source-Encryption-Algorithm}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Encryption-Algorithm}\n  <li>{@code X-Goog-Encryption-Key}\n  <li>{@code X-Goog-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Gcs-Idempotency-Token}\n  <li>{@code X-Goog-Meta-*}\n  <li>{@code X-Goog-User-Project}\n  <li>{@code X-HTTP-Method-Override}\n  <li>{@code X-Upload-Content-Length}\n  <li>{@code X-Upload-Content-Type}\n</ol>\n\n@since 2.49.0"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]]])

(do
  (defn ^Storage$CreateHmacKeyOption/1 CreateHmacKeyOption-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.CreateHmacKeyOption arg)
    (into-array
      Storage$CreateHmacKeyOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :userProject (conj acc (Storage$CreateHmacKeyOption/userProject v))
            :projectId (conj acc (Storage$CreateHmacKeyOption/projectId v))
            :extraHeaders (conj acc
                                (Storage$CreateHmacKeyOption/extraHeaders
                                  (into {} (map (fn [[k v]] [(name k) v])) v)))
            acc))
        []
        arg)))
  (defn ^Storage$CreateHmacKeyOption CreateHmacKeyOption-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.CreateHmacKeyOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :userProject (reduced (Storage$CreateHmacKeyOption/userProject v))
          :projectId (reduced (Storage$CreateHmacKeyOption/projectId v))
          :extraHeaders (reduced (Storage$CreateHmacKeyOption/extraHeaders
                                   (into {} (map (fn [[k v]] [(name k) v])) v)))
          acc))
      nil
      arg)))

(def CreateHmacKeyOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying createHmacKey options",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.CreateHmacKeyOption}
   [:map {:closed true}
    [:userProject
     {:optional true,
      :doc
        "Returns an option to specify the project to be billed for this request. Required for\nRequester Pays buckets."}
     [:string {:min 1}]]
    [:projectId
     {:optional true,
      :doc
        "Returns an option to specify the Project ID for this request. If not specified, defaults to\nApplication Default Credentials."}
     [:string {:min 1}]]
    [:extraHeaders
     {:optional true,
      :doc
        "A set of extra headers to be set for all requests performed within the scope of the operation\nthis option is passed to (a get, read, resumable upload etc).\n\n<p>If the same header name is specified across multiple options provided to a method, the\nfirst occurrence will be the value included in the request(s).\n\n<p>The following headers are not allowed to be specified, and will result in an {@link\nIllegalArgumentException}.\n\n<ol>\n  <li>{@code Accept-Encoding}\n  <li>{@code Cache-Control}\n  <li>{@code Connection}\n  <li>{@code Content-ID}\n  <li>{@code Content-Length}\n  <li>{@code Content-Range}\n  <li>{@code Content-Transfer-Encoding}\n  <li>{@code Content-Type}\n  <li>{@code Date}\n  <li>{@code ETag}\n  <li>{@code If-Match}\n  <li>{@code If-None-Match}\n  <li>{@code Keep-Alive}\n  <li>{@code Range}\n  <li>{@code TE}\n  <li>{@code Trailer}\n  <li>{@code Transfer-Encoding}\n  <li>{@code User-Agent}\n  <li>{@code X-Goog-Api-Client}\n  <li>{@code X-Goog-Content-Length-Range}\n  <li>{@code X-Goog-Copy-Source-Encryption-Algorithm}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Encryption-Algorithm}\n  <li>{@code X-Goog-Encryption-Key}\n  <li>{@code X-Goog-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Gcs-Idempotency-Token}\n  <li>{@code X-Goog-Meta-*}\n  <li>{@code X-Goog-User-Project}\n  <li>{@code X-HTTP-Method-Override}\n  <li>{@code X-Upload-Content-Length}\n  <li>{@code X-Upload-Content-Type}\n</ol>\n\n@since 2.49.0"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]]])

(do
  (defn ^Storage$GetHmacKeyOption/1 GetHmacKeyOption-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.GetHmacKeyOption arg)
    (into-array
      Storage$GetHmacKeyOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :userProject (conj acc (Storage$GetHmacKeyOption/userProject v))
            :projectId (conj acc (Storage$GetHmacKeyOption/projectId v))
            :extraHeaders (conj acc
                                (Storage$GetHmacKeyOption/extraHeaders
                                  (into {} (map (fn [[k v]] [(name k) v])) v)))
            acc))
        []
        arg)))
  (defn ^Storage$GetHmacKeyOption GetHmacKeyOption-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.GetHmacKeyOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :userProject (reduced (Storage$GetHmacKeyOption/userProject v))
          :projectId (reduced (Storage$GetHmacKeyOption/projectId v))
          :extraHeaders (reduced (Storage$GetHmacKeyOption/extraHeaders
                                   (into {} (map (fn [[k v]] [(name k) v])) v)))
          acc))
      nil
      arg)))

(def GetHmacKeyOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying getHmacKey options",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.GetHmacKeyOption}
   [:map {:closed true}
    [:userProject
     {:optional true,
      :doc
        "Returns an option to specify the project to be billed for this request. Required for\nRequester Pays buckets."}
     [:string {:min 1}]]
    [:projectId
     {:optional true,
      :doc
        "Returns an option to specify the Project ID for this request. If not specified, defaults to\nApplication Default Credentials."}
     [:string {:min 1}]]
    [:extraHeaders
     {:optional true,
      :doc
        "A set of extra headers to be set for all requests performed within the scope of the operation\nthis option is passed to (a get, read, resumable upload etc).\n\n<p>If the same header name is specified across multiple options provided to a method, the\nfirst occurrence will be the value included in the request(s).\n\n<p>The following headers are not allowed to be specified, and will result in an {@link\nIllegalArgumentException}.\n\n<ol>\n  <li>{@code Accept-Encoding}\n  <li>{@code Cache-Control}\n  <li>{@code Connection}\n  <li>{@code Content-ID}\n  <li>{@code Content-Length}\n  <li>{@code Content-Range}\n  <li>{@code Content-Transfer-Encoding}\n  <li>{@code Content-Type}\n  <li>{@code Date}\n  <li>{@code ETag}\n  <li>{@code If-Match}\n  <li>{@code If-None-Match}\n  <li>{@code Keep-Alive}\n  <li>{@code Range}\n  <li>{@code TE}\n  <li>{@code Trailer}\n  <li>{@code Transfer-Encoding}\n  <li>{@code User-Agent}\n  <li>{@code X-Goog-Api-Client}\n  <li>{@code X-Goog-Content-Length-Range}\n  <li>{@code X-Goog-Copy-Source-Encryption-Algorithm}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Encryption-Algorithm}\n  <li>{@code X-Goog-Encryption-Key}\n  <li>{@code X-Goog-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Gcs-Idempotency-Token}\n  <li>{@code X-Goog-Meta-*}\n  <li>{@code X-Goog-User-Project}\n  <li>{@code X-HTTP-Method-Override}\n  <li>{@code X-Upload-Content-Length}\n  <li>{@code X-Upload-Content-Type}\n</ol>\n\n@since 2.49.0"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]]])

(do
  (defn ^Storage$DeleteHmacKeyOption/1 DeleteHmacKeyOption-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.DeleteHmacKeyOption arg)
    (into-array
      Storage$DeleteHmacKeyOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :userProject (conj acc (Storage$DeleteHmacKeyOption/userProject v))
            :extraHeaders (conj acc
                                (Storage$DeleteHmacKeyOption/extraHeaders
                                  (into {} (map (fn [[k v]] [(name k) v])) v)))
            acc))
        []
        arg)))
  (defn ^Storage$DeleteHmacKeyOption DeleteHmacKeyOption-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.DeleteHmacKeyOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :userProject (reduced (Storage$DeleteHmacKeyOption/userProject v))
          :extraHeaders (reduced (Storage$DeleteHmacKeyOption/extraHeaders
                                   (into {} (map (fn [[k v]] [(name k) v])) v)))
          acc))
      nil
      arg)))

(def DeleteHmacKeyOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying deleteHmacKey options",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.DeleteHmacKeyOption}
   [:map {:closed true}
    [:userProject
     {:optional true,
      :doc
        "Returns an option to specify the project to be billed for this request. Required for\nRequester Pays buckets."}
     [:string {:min 1}]]
    [:extraHeaders
     {:optional true,
      :doc
        "A set of extra headers to be set for all requests performed within the scope of the operation\nthis option is passed to (a get, read, resumable upload etc).\n\n<p>If the same header name is specified across multiple options provided to a method, the\nfirst occurrence will be the value included in the request(s).\n\n<p>The following headers are not allowed to be specified, and will result in an {@link\nIllegalArgumentException}.\n\n<ol>\n  <li>{@code Accept-Encoding}\n  <li>{@code Cache-Control}\n  <li>{@code Connection}\n  <li>{@code Content-ID}\n  <li>{@code Content-Length}\n  <li>{@code Content-Range}\n  <li>{@code Content-Transfer-Encoding}\n  <li>{@code Content-Type}\n  <li>{@code Date}\n  <li>{@code ETag}\n  <li>{@code If-Match}\n  <li>{@code If-None-Match}\n  <li>{@code Keep-Alive}\n  <li>{@code Range}\n  <li>{@code TE}\n  <li>{@code Trailer}\n  <li>{@code Transfer-Encoding}\n  <li>{@code User-Agent}\n  <li>{@code X-Goog-Api-Client}\n  <li>{@code X-Goog-Content-Length-Range}\n  <li>{@code X-Goog-Copy-Source-Encryption-Algorithm}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Encryption-Algorithm}\n  <li>{@code X-Goog-Encryption-Key}\n  <li>{@code X-Goog-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Gcs-Idempotency-Token}\n  <li>{@code X-Goog-Meta-*}\n  <li>{@code X-Goog-User-Project}\n  <li>{@code X-HTTP-Method-Override}\n  <li>{@code X-Upload-Content-Length}\n  <li>{@code X-Upload-Content-Type}\n</ol>\n\n@since 2.49.0"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]]])

(do
  (defn ^Storage$UpdateHmacKeyOption/1 UpdateHmacKeyOption-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.UpdateHmacKeyOption arg)
    (into-array
      Storage$UpdateHmacKeyOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :userProject (conj acc (Storage$UpdateHmacKeyOption/userProject v))
            :extraHeaders (conj acc
                                (Storage$UpdateHmacKeyOption/extraHeaders
                                  (into {} (map (fn [[k v]] [(name k) v])) v)))
            acc))
        []
        arg)))
  (defn ^Storage$UpdateHmacKeyOption UpdateHmacKeyOption-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.UpdateHmacKeyOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :userProject (reduced (Storage$UpdateHmacKeyOption/userProject v))
          :extraHeaders (reduced (Storage$UpdateHmacKeyOption/extraHeaders
                                   (into {} (map (fn [[k v]] [(name k) v])) v)))
          acc))
      nil
      arg)))

(def UpdateHmacKeyOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying updateHmacKey options",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.UpdateHmacKeyOption}
   [:map {:closed true}
    [:userProject
     {:optional true,
      :doc
        "Returns an option to specify the project to be billed for this request. Required for\nRequester Pays buckets."}
     [:string {:min 1}]]
    [:extraHeaders
     {:optional true,
      :doc
        "A set of extra headers to be set for all requests performed within the scope of the operation\nthis option is passed to (a get, read, resumable upload etc).\n\n<p>If the same header name is specified across multiple options provided to a method, the\nfirst occurrence will be the value included in the request(s).\n\n<p>The following headers are not allowed to be specified, and will result in an {@link\nIllegalArgumentException}.\n\n<ol>\n  <li>{@code Accept-Encoding}\n  <li>{@code Cache-Control}\n  <li>{@code Connection}\n  <li>{@code Content-ID}\n  <li>{@code Content-Length}\n  <li>{@code Content-Range}\n  <li>{@code Content-Transfer-Encoding}\n  <li>{@code Content-Type}\n  <li>{@code Date}\n  <li>{@code ETag}\n  <li>{@code If-Match}\n  <li>{@code If-None-Match}\n  <li>{@code Keep-Alive}\n  <li>{@code Range}\n  <li>{@code TE}\n  <li>{@code Trailer}\n  <li>{@code Transfer-Encoding}\n  <li>{@code User-Agent}\n  <li>{@code X-Goog-Api-Client}\n  <li>{@code X-Goog-Content-Length-Range}\n  <li>{@code X-Goog-Copy-Source-Encryption-Algorithm}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Encryption-Algorithm}\n  <li>{@code X-Goog-Encryption-Key}\n  <li>{@code X-Goog-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Gcs-Idempotency-Token}\n  <li>{@code X-Goog-Meta-*}\n  <li>{@code X-Goog-User-Project}\n  <li>{@code X-HTTP-Method-Override}\n  <li>{@code X-Upload-Content-Length}\n  <li>{@code X-Upload-Content-Type}\n</ol>\n\n@since 2.49.0"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]]])

(do
  (defn ^Storage$BucketGetOption/1 BucketGetOption-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BucketGetOption arg)
    (into-array
      Storage$BucketGetOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :metagenerationMatch
              (conj acc (Storage$BucketGetOption/metagenerationMatch (long v)))
            :metagenerationNotMatch
              (conj acc
                    (Storage$BucketGetOption/metagenerationNotMatch (long v)))
            :userProject (conj acc (Storage$BucketGetOption/userProject v))
            :fields (conj acc
                          (Storage$BucketGetOption/fields
                            (into-array Storage$BucketField
                                        (map Storage$BucketField/valueOf v))))
            :extraHeaders (conj acc
                                (Storage$BucketGetOption/extraHeaders
                                  (into {} (map (fn [[k v]] [(name k) v])) v)))
            acc))
        []
        arg)))
  (defn ^Storage$BucketGetOption BucketGetOption-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BucketGetOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :metagenerationMatch
            (reduced (Storage$BucketGetOption/metagenerationMatch (long v)))
          :metagenerationNotMatch
            (reduced (Storage$BucketGetOption/metagenerationNotMatch (long v)))
          :userProject (reduced (Storage$BucketGetOption/userProject v))
          :fields (reduced (Storage$BucketGetOption/fields
                             (into-array Storage$BucketField
                                         (map Storage$BucketField/valueOf v))))
          :extraHeaders (reduced (Storage$BucketGetOption/extraHeaders
                                   (into {} (map (fn [[k v]] [(name k) v])) v)))
          acc))
      nil
      arg)))

(def BucketGetOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying bucket get options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.BucketGetOption}
   [:map {:closed true}
    [:metagenerationMatch
     {:optional true,
      :doc
        "Returns an option for bucket's metageneration match. If this option is used the request will\nfail if bucket's metageneration does not match the provided value."}
     :i64]
    [:metagenerationNotMatch
     {:optional true,
      :doc
        "Returns an option for bucket's metageneration mismatch. If this option is used the request\nwill fail if bucket's metageneration matches the provided value."}
     :i64]
    [:userProject
     {:optional true,
      :doc
        "Returns an option for bucket's billing user project. This option is only used by the buckets\nwith 'requester_pays' flag."}
     [:string {:min 1}]]
    [:fields
     {:optional true,
      :doc
        "Returns an option to specify the bucket's fields to be returned by the RPC call. If this\noption is not provided all bucket's fields are returned. {@code BucketGetOption.fields}) can\nbe used to specify only the fields of interest. Bucket name is always returned, even if not\nspecified."}
     [:sequential {:min 1}
      [:enum {:closed true} "ID" "SELF_LINK" "NAME" "TIME_CREATED"
       "METAGENERATION" "ACL" "DEFAULT_OBJECT_ACL" "OWNER" "LABELS" "LOCATION"
       "LOCATION_TYPE" "WEBSITE" "VERSIONING" "CORS" "LIFECYCLE" "STORAGE_CLASS"
       "ETAG" "ENCRYPTION" "BILLING" "DEFAULT_EVENT_BASED_HOLD"
       "RETENTION_POLICY" "IAMCONFIGURATION" "LOGGING" "UPDATED" "RPO"
       "CUSTOM_PLACEMENT_CONFIG" "AUTOCLASS" "HIERARCHICAL_NAMESPACE"
       "OBJECT_RETENTION" "SOFT_DELETE_POLICY" "PROJECT" "IP_FILTER"]]]
    [:extraHeaders
     {:optional true,
      :doc
        "A set of extra headers to be set for all requests performed within the scope of the operation\nthis option is passed to (a get, read, resumable upload etc).\n\n<p>If the same header name is specified across multiple options provided to a method, the\nfirst occurrence will be the value included in the request(s).\n\n<p>The following headers are not allowed to be specified, and will result in an {@link\nIllegalArgumentException}.\n\n<ol>\n  <li>{@code Accept-Encoding}\n  <li>{@code Cache-Control}\n  <li>{@code Connection}\n  <li>{@code Content-ID}\n  <li>{@code Content-Length}\n  <li>{@code Content-Range}\n  <li>{@code Content-Transfer-Encoding}\n  <li>{@code Content-Type}\n  <li>{@code Date}\n  <li>{@code ETag}\n  <li>{@code If-Match}\n  <li>{@code If-None-Match}\n  <li>{@code Keep-Alive}\n  <li>{@code Range}\n  <li>{@code TE}\n  <li>{@code Trailer}\n  <li>{@code Transfer-Encoding}\n  <li>{@code User-Agent}\n  <li>{@code X-Goog-Api-Client}\n  <li>{@code X-Goog-Content-Length-Range}\n  <li>{@code X-Goog-Copy-Source-Encryption-Algorithm}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Encryption-Algorithm}\n  <li>{@code X-Goog-Encryption-Key}\n  <li>{@code X-Goog-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Gcs-Idempotency-Token}\n  <li>{@code X-Goog-Meta-*}\n  <li>{@code X-Goog-User-Project}\n  <li>{@code X-HTTP-Method-Override}\n  <li>{@code X-Upload-Content-Length}\n  <li>{@code X-Upload-Content-Type}\n</ol>\n\n@since 2.49.0"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]]])

(do
  (defn ^Storage$BlobTargetOption/1 BlobTargetOption-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BlobTargetOption arg)
    (into-array
      Storage$BlobTargetOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :extraHeaders (conj acc
                                (Storage$BlobTargetOption/extraHeaders
                                  (into {} (map (fn [[k v]] [(name k) v])) v)))
            :doesNotExist
              (if v
                (clojure.core/conj acc (Storage$BlobTargetOption/doesNotExist))
                acc)
            :predefinedAcl (conj acc
                                 (Storage$BlobTargetOption/predefinedAcl
                                   (Storage$PredefinedAcl/valueOf v)))
            :metagenerationMatch
              (if v
                (clojure.core/conj
                  acc
                  (Storage$BlobTargetOption/metagenerationMatch))
                acc)
            :encryptionKey (conj acc (Storage$BlobTargetOption/encryptionKey v))
            :generationNotMatch
              (if v
                (clojure.core/conj
                  acc
                  (Storage$BlobTargetOption/generationNotMatch))
                acc)
            :detectContentType (if v
                                 (clojure.core/conj
                                   acc
                                   (Storage$BlobTargetOption/detectContentType))
                                 acc)
            :disableGzipContent
              (if v
                (clojure.core/conj
                  acc
                  (Storage$BlobTargetOption/disableGzipContent))
                acc)
            :generationMatch (if v
                               (clojure.core/conj
                                 acc
                                 (Storage$BlobTargetOption/generationMatch))
                               acc)
            :overrideUnlockedRetention
              (conj acc (Storage$BlobTargetOption/overrideUnlockedRetention v))
            :kmsKeyName (conj acc (Storage$BlobTargetOption/kmsKeyName v))
            :userProject (conj acc (Storage$BlobTargetOption/userProject v))
            :metagenerationNotMatch
              (if v
                (clojure.core/conj
                  acc
                  (Storage$BlobTargetOption/metagenerationNotMatch))
                acc)
            acc))
        []
        arg)))
  (defn ^Storage$BlobTargetOption BlobTargetOption-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BlobTargetOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :extraHeaders (reduced (Storage$BlobTargetOption/extraHeaders
                                   (into {} (map (fn [[k v]] [(name k) v])) v)))
          :doesNotExist
            (if v (reduced (Storage$BlobTargetOption/doesNotExist)) acc)
          :predefinedAcl (reduced (Storage$BlobTargetOption/predefinedAcl
                                    (Storage$PredefinedAcl/valueOf v)))
          :metagenerationMatch
            (if v (reduced (Storage$BlobTargetOption/metagenerationMatch)) acc)
          :encryptionKey (reduced (Storage$BlobTargetOption/encryptionKey v))
          :generationNotMatch
            (if v (reduced (Storage$BlobTargetOption/generationNotMatch)) acc)
          :detectContentType
            (if v (reduced (Storage$BlobTargetOption/detectContentType)) acc)
          :disableGzipContent
            (if v (reduced (Storage$BlobTargetOption/disableGzipContent)) acc)
          :generationMatch
            (if v (reduced (Storage$BlobTargetOption/generationMatch)) acc)
          :overrideUnlockedRetention
            (reduced (Storage$BlobTargetOption/overrideUnlockedRetention v))
          :kmsKeyName (reduced (Storage$BlobTargetOption/kmsKeyName v))
          :userProject (reduced (Storage$BlobTargetOption/userProject v))
          :metagenerationNotMatch
            (if v
              (reduced (Storage$BlobTargetOption/metagenerationNotMatch))
              acc)
          acc))
      nil
      arg)))

(def BlobTargetOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying blob target options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.BlobTargetOption}
   [:map {:closed true}
    [:extraHeaders
     {:optional true,
      :doc
        "A set of extra headers to be set for all requests performed within the scope of the operation\nthis option is passed to (a get, read, resumable upload etc).\n\n<p>If the same header name is specified across multiple options provided to a method, the\nfirst occurrence will be the value included in the request(s).\n\n<p>The following headers are not allowed to be specified, and will result in an {@link\nIllegalArgumentException}.\n\n<ol>\n  <li>{@code Accept-Encoding}\n  <li>{@code Cache-Control}\n  <li>{@code Connection}\n  <li>{@code Content-ID}\n  <li>{@code Content-Length}\n  <li>{@code Content-Range}\n  <li>{@code Content-Transfer-Encoding}\n  <li>{@code Content-Type}\n  <li>{@code Date}\n  <li>{@code ETag}\n  <li>{@code If-Match}\n  <li>{@code If-None-Match}\n  <li>{@code Keep-Alive}\n  <li>{@code Range}\n  <li>{@code TE}\n  <li>{@code Trailer}\n  <li>{@code Transfer-Encoding}\n  <li>{@code User-Agent}\n  <li>{@code X-Goog-Api-Client}\n  <li>{@code X-Goog-Content-Length-Range}\n  <li>{@code X-Goog-Copy-Source-Encryption-Algorithm}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Encryption-Algorithm}\n  <li>{@code X-Goog-Encryption-Key}\n  <li>{@code X-Goog-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Gcs-Idempotency-Token}\n  <li>{@code X-Goog-Meta-*}\n  <li>{@code X-Goog-User-Project}\n  <li>{@code X-HTTP-Method-Override}\n  <li>{@code X-Upload-Content-Length}\n  <li>{@code X-Upload-Content-Type}\n</ol>\n\n@since 2.49.0"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
    [:doesNotExist
     {:optional true,
      :doc
        "Returns an option that causes an operation to succeed only if the target blob does not exist."}
     :boolean]
    [:predefinedAcl
     {:optional true,
      :doc
        "Returns an option for specifying blob's predefined ACL configuration."}
     [:enum {:closed true} "AUTHENTICATED_READ" "ALL_AUTHENTICATED_USERS"
      "PRIVATE" "PROJECT_PRIVATE" "PUBLIC_READ" "PUBLIC_READ_WRITE"
      "BUCKET_OWNER_READ" "BUCKET_OWNER_FULL_CONTROL"]]
    [:metagenerationMatch
     {:optional true,
      :doc
        "Returns an option for blob's metageneration match. If this option is used the request will\nfail if metageneration does not match."}
     :boolean]
    [:encryptionKey
     {:optional true,
      :doc
        "Returns an option to set a customer-supplied AES256 key for server-side encryption of the\nblob."}
     (gcp.global/instance-schema java.security.Key)]
    [:generationNotMatch
     {:optional true,
      :doc
        "Returns an option for blob's data generation mismatch. If this option is used the request\nwill fail if generation matches."}
     :boolean]
    [:detectContentType
     {:optional true,
      :doc
        "Returns an option for detecting content type. If this option is used, the content type is\ndetected from the blob name if not explicitly set. This option is on the client side only, it\ndoes not appear in a RPC call."}
     :boolean]
    [:disableGzipContent
     {:optional true,
      :doc
        "Returns an option for blob's data disabledGzipContent. If this option is used, the request\nwill create a blob with disableGzipContent; at present, this is only for upload."}
     :boolean]
    [:generationMatch
     {:optional true,
      :doc
        "Returns an option for blob's data generation match. If this option is used the request will\nfail if generation does not match."}
     :boolean]
    [:overrideUnlockedRetention
     {:optional true,
      :doc
        "Returns an option for overriding an Unlocked Retention policy. This must be set to true in\norder to change a policy from Unlocked to Locked, to set it to null, or to reduce its\nretainUntilTime attribute."}
     :boolean]
    [:kmsKeyName
     {:optional true,
      :doc
        "Returns an option to set a customer-managed key for server-side encryption of the blob."}
     [:string {:min 1}]]
    [:userProject
     {:optional true,
      :doc
        "Returns an option for blob's billing user project. This option is only used by the buckets\nwith 'requester_pays' flag."}
     [:string {:min 1}]]
    [:metagenerationNotMatch
     {:optional true,
      :doc
        "Returns an option for blob's metageneration mismatch. If this option is used the request will\nfail if metageneration matches."}
     :boolean]]])

(do
  (defn ^Storage$BlobWriteOption/1 BlobWriteOption-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BlobWriteOption arg)
    (into-array
      Storage$BlobWriteOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :extraHeaders (conj acc
                                (Storage$BlobWriteOption/extraHeaders
                                  (into {} (map (fn [[k v]] [(name k) v])) v)))
            :doesNotExist
              (if v
                (clojure.core/conj acc (Storage$BlobWriteOption/doesNotExist))
                acc)
            :predefinedAcl (conj acc
                                 (Storage$BlobWriteOption/predefinedAcl
                                   (Storage$PredefinedAcl/valueOf v)))
            :metagenerationMatch
              (if v
                (clojure.core/conj
                  acc
                  (Storage$BlobWriteOption/metagenerationMatch))
                acc)
            :crc32cMatch
              (if v
                (clojure.core/conj acc (Storage$BlobWriteOption/crc32cMatch))
                acc)
            :expectedObjectSize
              (conj acc (Storage$BlobWriteOption/expectedObjectSize (long v)))
            :encryptionKey (conj acc (Storage$BlobWriteOption/encryptionKey v))
            :generationNotMatch
              (if v
                (clojure.core/conj acc
                                   (Storage$BlobWriteOption/generationNotMatch))
                acc)
            :detectContentType (if v
                                 (clojure.core/conj
                                   acc
                                   (Storage$BlobWriteOption/detectContentType))
                                 acc)
            :disableGzipContent
              (if v
                (clojure.core/conj acc
                                   (Storage$BlobWriteOption/disableGzipContent))
                acc)
            :generationMatch (if v
                               (clojure.core/conj
                                 acc
                                 (Storage$BlobWriteOption/generationMatch))
                               acc)
            :kmsKeyName (conj acc (Storage$BlobWriteOption/kmsKeyName v))
            :userProject (conj acc (Storage$BlobWriteOption/userProject v))
            :metagenerationNotMatch
              (if v
                (clojure.core/conj
                  acc
                  (Storage$BlobWriteOption/metagenerationNotMatch))
                acc)
            acc))
        []
        arg)))
  (defn ^Storage$BlobWriteOption BlobWriteOption-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BlobWriteOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :extraHeaders (reduced (Storage$BlobWriteOption/extraHeaders
                                   (into {} (map (fn [[k v]] [(name k) v])) v)))
          :doesNotExist
            (if v (reduced (Storage$BlobWriteOption/doesNotExist)) acc)
          :predefinedAcl (reduced (Storage$BlobWriteOption/predefinedAcl
                                    (Storage$PredefinedAcl/valueOf v)))
          :metagenerationMatch
            (if v (reduced (Storage$BlobWriteOption/metagenerationMatch)) acc)
          :crc32cMatch
            (if v (reduced (Storage$BlobWriteOption/crc32cMatch)) acc)
          :expectedObjectSize
            (reduced (Storage$BlobWriteOption/expectedObjectSize (long v)))
          :encryptionKey (reduced (Storage$BlobWriteOption/encryptionKey v))
          :generationNotMatch
            (if v (reduced (Storage$BlobWriteOption/generationNotMatch)) acc)
          :detectContentType
            (if v (reduced (Storage$BlobWriteOption/detectContentType)) acc)
          :disableGzipContent
            (if v (reduced (Storage$BlobWriteOption/disableGzipContent)) acc)
          :generationMatch
            (if v (reduced (Storage$BlobWriteOption/generationMatch)) acc)
          :kmsKeyName (reduced (Storage$BlobWriteOption/kmsKeyName v))
          :userProject (reduced (Storage$BlobWriteOption/userProject v))
          :metagenerationNotMatch
            (if v
              (reduced (Storage$BlobWriteOption/metagenerationNotMatch))
              acc)
          acc))
      nil
      arg)))

(def BlobWriteOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying blob write options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.BlobWriteOption}
   [:map {:closed true}
    [:extraHeaders
     {:optional true,
      :doc
        "A set of extra headers to be set for all requests performed within the scope of the operation\nthis option is passed to (a get, read, resumable upload etc).\n\n<p>If the same header name is specified across multiple options provided to a method, the\nfirst occurrence will be the value included in the request(s).\n\n<p>The following headers are not allowed to be specified, and will result in an {@link\nIllegalArgumentException}.\n\n<ol>\n  <li>{@code Accept-Encoding}\n  <li>{@code Cache-Control}\n  <li>{@code Connection}\n  <li>{@code Content-ID}\n  <li>{@code Content-Length}\n  <li>{@code Content-Range}\n  <li>{@code Content-Transfer-Encoding}\n  <li>{@code Content-Type}\n  <li>{@code Date}\n  <li>{@code ETag}\n  <li>{@code If-Match}\n  <li>{@code If-None-Match}\n  <li>{@code Keep-Alive}\n  <li>{@code Range}\n  <li>{@code TE}\n  <li>{@code Trailer}\n  <li>{@code Transfer-Encoding}\n  <li>{@code User-Agent}\n  <li>{@code X-Goog-Api-Client}\n  <li>{@code X-Goog-Content-Length-Range}\n  <li>{@code X-Goog-Copy-Source-Encryption-Algorithm}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Encryption-Algorithm}\n  <li>{@code X-Goog-Encryption-Key}\n  <li>{@code X-Goog-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Gcs-Idempotency-Token}\n  <li>{@code X-Goog-Meta-*}\n  <li>{@code X-Goog-User-Project}\n  <li>{@code X-HTTP-Method-Override}\n  <li>{@code X-Upload-Content-Length}\n  <li>{@code X-Upload-Content-Type}\n</ol>\n\n@since 2.49.0"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
    [:doesNotExist
     {:optional true,
      :doc
        "Returns an option that causes an operation to succeed only if the target blob does not exist."}
     :boolean]
    [:predefinedAcl
     {:optional true,
      :doc
        "Returns an option for specifying blob's predefined ACL configuration."}
     [:enum {:closed true} "AUTHENTICATED_READ" "ALL_AUTHENTICATED_USERS"
      "PRIVATE" "PROJECT_PRIVATE" "PUBLIC_READ" "PUBLIC_READ_WRITE"
      "BUCKET_OWNER_READ" "BUCKET_OWNER_FULL_CONTROL"]]
    [:metagenerationMatch
     {:optional true,
      :doc
        "Returns an option for blob's metageneration match. If this option is used the request will\nfail if metageneration does not match."}
     :boolean]
    [:crc32cMatch
     {:optional true,
      :doc
        "Returns an option for blob's data CRC32C checksum match. If this option is used the request\nwill fail if blobs' data CRC32C checksum does not match."}
     :boolean]
    [:expectedObjectSize
     {:optional true,
      :doc
        "Set a precondition on the number of bytes that GCS should expect for a resumable upload. See\nthe docs for <a\nhref=\"https://cloud.google.com/storage/docs/json_api/v1/parameters#xuploadcontentlength\">X-Upload-Content-Length</a>\nfor more detail.\n\n<p>If the method invoked with this option does not perform a resumable upload, this option\nwill be ignored.\n\n@since 2.42.0"}
     :i64]
    [:encryptionKey
     {:optional true,
      :doc
        "Returns an option to set a customer-supplied AES256 key for server-side encryption of the\nblob."}
     (gcp.global/instance-schema java.security.Key)]
    [:generationNotMatch
     {:optional true,
      :doc
        "Returns an option for blob's data generation mismatch. If this option is used the request\nwill fail if generation matches."}
     :boolean]
    [:detectContentType
     {:optional true,
      :doc
        "Returns an option for detecting content type. If this option is used, the content type is\ndetected from the blob name if not explicitly set. This option is on the client side only, it\ndoes not appear in a RPC call.\n\n<p>Content type detection is based on the database presented by {@link\nURLConnection#getFileNameMap()}"}
     :boolean]
    [:disableGzipContent
     {:optional true,
      :doc
        "Returns an option that signals automatic gzip compression should not be performed en route to\nthe bucket."}
     :boolean]
    [:generationMatch
     {:optional true,
      :doc
        "Returns an option for blob's data generation match. If this option is used the request will\nfail if generation does not match."}
     :boolean]
    [:kmsKeyName
     {:optional true,
      :doc
        "Returns an option to set a customer-managed KMS key for server-side encryption of the blob.\n\n@param kmsKeyName the KMS key resource id"}
     [:string {:min 1}]]
    [:userProject
     {:optional true,
      :doc
        "Returns an option for blob's billing user project. This option is only used by the buckets\nwith 'requester_pays' flag."}
     [:string {:min 1}]]
    [:metagenerationNotMatch
     {:optional true,
      :doc
        "Returns an option for blob's metageneration mismatch. If this option is used the request will\nfail if metageneration matches."}
     :boolean]]])

(do
  (defn ^Storage$BlobSourceOption/1 BlobSourceOption-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BlobSourceOption arg)
    (into-array
      Storage$BlobSourceOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :generationMatch (if v
                               (clojure.core/conj
                                 acc
                                 (Storage$BlobSourceOption/generationMatch))
                               acc)
            :generationNotMatch
              (conj acc (Storage$BlobSourceOption/generationNotMatch (long v)))
            :metagenerationMatch
              (conj acc (Storage$BlobSourceOption/metagenerationMatch (long v)))
            :metagenerationNotMatch
              (conj acc
                    (Storage$BlobSourceOption/metagenerationNotMatch (long v)))
            :decryptionKey (conj acc (Storage$BlobSourceOption/decryptionKey v))
            :userProject (conj acc (Storage$BlobSourceOption/userProject v))
            :shouldReturnRawInputStream
              (conj acc (Storage$BlobSourceOption/shouldReturnRawInputStream v))
            :extraHeaders (conj acc
                                (Storage$BlobSourceOption/extraHeaders
                                  (into {} (map (fn [[k v]] [(name k) v])) v)))
            acc))
        []
        arg)))
  (defn ^Storage$BlobSourceOption BlobSourceOption-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BlobSourceOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :generationMatch
            (if v (reduced (Storage$BlobSourceOption/generationMatch)) acc)
          :generationNotMatch
            (reduced (Storage$BlobSourceOption/generationNotMatch (long v)))
          :metagenerationMatch
            (reduced (Storage$BlobSourceOption/metagenerationMatch (long v)))
          :metagenerationNotMatch
            (reduced (Storage$BlobSourceOption/metagenerationNotMatch (long v)))
          :decryptionKey (reduced (Storage$BlobSourceOption/decryptionKey v))
          :userProject (reduced (Storage$BlobSourceOption/userProject v))
          :shouldReturnRawInputStream
            (reduced (Storage$BlobSourceOption/shouldReturnRawInputStream v))
          :extraHeaders (reduced (Storage$BlobSourceOption/extraHeaders
                                   (into {} (map (fn [[k v]] [(name k) v])) v)))
          acc))
      nil
      arg)))

(def BlobSourceOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying blob source options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.BlobSourceOption}
   [:map {:closed true}
    [:generationMatch
     {:optional true,
      :doc
        "Returns an option for blob's data generation match. If this option is used the request will\nfail if blob's generation does not match. The generation value to compare with the actual\nblob's generation is taken from a source {@link BlobId} object. When this option is passed to\na {@link Storage} method and {@link BlobId#getGeneration()} is {@code null} or no {@link\nBlobId} is provided an exception is thrown."}
     :boolean]
    [:generationNotMatch
     {:optional true,
      :doc
        "Returns an option for blob's data generation mismatch. If this option is used the request\nwill fail if blob's generation matches the provided value."}
     :i64]
    [:metagenerationMatch
     {:optional true,
      :doc
        "Returns an option for blob's metageneration match. If this option is used the request will\nfail if blob's metageneration does not match the provided value."}
     :i64]
    [:metagenerationNotMatch
     {:optional true,
      :doc
        "Returns an option for blob's metageneration mismatch. If this option is used the request will\nfail if blob's metageneration matches the provided value."}
     :i64]
    [:decryptionKey
     {:optional true,
      :doc
        "Returns an option to set a customer-supplied AES256 key for server-side encryption of the\nblob."}
     (gcp.global/instance-schema java.security.Key)]
    [:userProject
     {:optional true,
      :doc
        "Returns an option for blob's billing user project. This option is only used by the buckets\nwith 'requester_pays' flag."}
     [:string {:min 1}]]
    [:shouldReturnRawInputStream
     {:optional true,
      :doc
        "Returns an option for whether the request should return the raw input stream, instead of\nautomatically decompressing the content. By default, this is false for Blob.downloadTo(), but\ntrue for ReadChannel.read()."}
     :boolean]
    [:extraHeaders
     {:optional true,
      :doc
        "A set of extra headers to be set for all requests performed within the scope of the operation\nthis option is passed to (a get, read, resumable upload etc).\n\n<p>If the same header name is specified across multiple options provided to a method, the\nfirst occurrence will be the value included in the request(s).\n\n<p>The following headers are not allowed to be specified, and will result in an {@link\nIllegalArgumentException}.\n\n<ol>\n  <li>{@code Accept-Encoding}\n  <li>{@code Cache-Control}\n  <li>{@code Connection}\n  <li>{@code Content-ID}\n  <li>{@code Content-Length}\n  <li>{@code Content-Range}\n  <li>{@code Content-Transfer-Encoding}\n  <li>{@code Content-Type}\n  <li>{@code Date}\n  <li>{@code ETag}\n  <li>{@code If-Match}\n  <li>{@code If-None-Match}\n  <li>{@code Keep-Alive}\n  <li>{@code Range}\n  <li>{@code TE}\n  <li>{@code Trailer}\n  <li>{@code Transfer-Encoding}\n  <li>{@code User-Agent}\n  <li>{@code X-Goog-Api-Client}\n  <li>{@code X-Goog-Content-Length-Range}\n  <li>{@code X-Goog-Copy-Source-Encryption-Algorithm}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Encryption-Algorithm}\n  <li>{@code X-Goog-Encryption-Key}\n  <li>{@code X-Goog-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Gcs-Idempotency-Token}\n  <li>{@code X-Goog-Meta-*}\n  <li>{@code X-Goog-User-Project}\n  <li>{@code X-HTTP-Method-Override}\n  <li>{@code X-Upload-Content-Length}\n  <li>{@code X-Upload-Content-Type}\n</ol>\n\n@since 2.49.0"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]]])

(do
  (defn ^Storage$BlobGetOption/1 BlobGetOption-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BlobGetOption arg)
    (into-array
      Storage$BlobGetOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :extraHeaders (conj acc
                                (Storage$BlobGetOption/extraHeaders
                                  (into {} (map (fn [[k v]] [(name k) v])) v)))
            :metagenerationMatch
              (conj acc (Storage$BlobGetOption/metagenerationMatch (long v)))
            :fields (conj acc
                          (Storage$BlobGetOption/fields
                            (into-array Storage$BlobField
                                        (map Storage$BlobField/valueOf v))))
            :generationNotMatch
              (conj acc (Storage$BlobGetOption/generationNotMatch (long v)))
            :decryptionKey (conj acc (Storage$BlobGetOption/decryptionKey v))
            :shouldReturnRawInputStream
              (conj acc (Storage$BlobGetOption/shouldReturnRawInputStream v))
            :generationMatch
              (if v
                (clojure.core/conj acc (Storage$BlobGetOption/generationMatch))
                acc)
            :userProject (conj acc (Storage$BlobGetOption/userProject v))
            :metagenerationNotMatch
              (conj acc (Storage$BlobGetOption/metagenerationNotMatch (long v)))
            :softDeleted (conj acc (Storage$BlobGetOption/softDeleted v))
            acc))
        []
        arg)))
  (defn ^Storage$BlobGetOption BlobGetOption-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BlobGetOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :extraHeaders (reduced (Storage$BlobGetOption/extraHeaders
                                   (into {} (map (fn [[k v]] [(name k) v])) v)))
          :metagenerationMatch
            (reduced (Storage$BlobGetOption/metagenerationMatch (long v)))
          :fields (reduced (Storage$BlobGetOption/fields
                             (into-array Storage$BlobField
                                         (map Storage$BlobField/valueOf v))))
          :generationNotMatch (reduced (Storage$BlobGetOption/generationNotMatch
                                         (long v)))
          :decryptionKey (reduced (Storage$BlobGetOption/decryptionKey v))
          :shouldReturnRawInputStream
            (reduced (Storage$BlobGetOption/shouldReturnRawInputStream v))
          :generationMatch
            (if v (reduced (Storage$BlobGetOption/generationMatch)) acc)
          :userProject (reduced (Storage$BlobGetOption/userProject v))
          :metagenerationNotMatch
            (reduced (Storage$BlobGetOption/metagenerationNotMatch (long v)))
          :softDeleted (reduced (Storage$BlobGetOption/softDeleted v))
          acc))
      nil
      arg)))

(def BlobGetOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying blob get options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.BlobGetOption}
   [:map {:closed true}
    [:extraHeaders
     {:optional true,
      :doc
        "A set of extra headers to be set for all requests performed within the scope of the operation\nthis option is passed to (a get, read, resumable upload etc).\n\n<p>If the same header name is specified across multiple options provided to a method, the\nfirst occurrence will be the value included in the request(s).\n\n<p>The following headers are not allowed to be specified, and will result in an {@link\nIllegalArgumentException}.\n\n<ol>\n  <li>{@code Accept-Encoding}\n  <li>{@code Cache-Control}\n  <li>{@code Connection}\n  <li>{@code Content-ID}\n  <li>{@code Content-Length}\n  <li>{@code Content-Range}\n  <li>{@code Content-Transfer-Encoding}\n  <li>{@code Content-Type}\n  <li>{@code Date}\n  <li>{@code ETag}\n  <li>{@code If-Match}\n  <li>{@code If-None-Match}\n  <li>{@code Keep-Alive}\n  <li>{@code Range}\n  <li>{@code TE}\n  <li>{@code Trailer}\n  <li>{@code Transfer-Encoding}\n  <li>{@code User-Agent}\n  <li>{@code X-Goog-Api-Client}\n  <li>{@code X-Goog-Content-Length-Range}\n  <li>{@code X-Goog-Copy-Source-Encryption-Algorithm}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Encryption-Algorithm}\n  <li>{@code X-Goog-Encryption-Key}\n  <li>{@code X-Goog-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Gcs-Idempotency-Token}\n  <li>{@code X-Goog-Meta-*}\n  <li>{@code X-Goog-User-Project}\n  <li>{@code X-HTTP-Method-Override}\n  <li>{@code X-Upload-Content-Length}\n  <li>{@code X-Upload-Content-Type}\n</ol>\n\n@since 2.49.0"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
    [:metagenerationMatch
     {:optional true,
      :doc
        "Returns an option for blob's metageneration match. If this option is used the request will\nfail if blob's metageneration does not match the provided value."}
     :i64]
    [:fields
     {:optional true,
      :doc
        "Returns an option to specify the blob's fields to be returned by the RPC call. If this option\nis not provided all blob's fields are returned. {@code BlobGetOption.fields}) can be used to\nspecify only the fields of interest. Blob name and bucket are always returned, even if not\nspecified."}
     [:sequential {:min 1}
      [:enum {:closed true} "ACL" "BUCKET" "CACHE_CONTROL" "COMPONENT_COUNT"
       "CONTENT_DISPOSITION" "CONTENT_ENCODING" "CONTENT_LANGUAGE"
       "CONTENT_TYPE" "CRC32C" "ETAG" "GENERATION" "ID" "KIND" "MD5HASH"
       "MEDIA_LINK" "METADATA" "METAGENERATION" "NAME" "OWNER" "SELF_LINK"
       "SIZE" "STORAGE_CLASS" "TIME_DELETED" "TIME_CREATED" "KMS_KEY_NAME"
       "EVENT_BASED_HOLD" "TEMPORARY_HOLD" "RETENTION_EXPIRATION_TIME" "UPDATED"
       "CUSTOM_TIME" "TIME_STORAGE_CLASS_UPDATED" "CUSTOMER_ENCRYPTION"
       "RETENTION" "SOFT_DELETE_TIME" "HARD_DELETE_TIME" "OBJECT_CONTEXTS"]]]
    [:generationNotMatch
     {:optional true,
      :doc
        "Returns an option for blob's data generation mismatch. If this option is used the request\nwill fail if blob's generation matches the provided value."}
     :i64]
    [:decryptionKey
     {:optional true,
      :doc
        "Returns an option to set a customer-supplied AES256 key for server-side decryption of the\nblob."}
     (gcp.global/instance-schema java.security.Key)]
    [:shouldReturnRawInputStream
     {:optional true,
      :doc
        "Returns an option for whether the request should return the raw input stream, instead of\nautomatically decompressing the content. By default, this is false for Blob.downloadTo(), but\ntrue for ReadChannel.read()."}
     :boolean]
    [:generationMatch
     {:optional true,
      :doc
        "Returns an option for blob's data generation match. If this option is used the request will\nfail if blob's generation does not match. The generation value to compare with the actual\nblob's generation is taken from a source {@link BlobId} object. When this option is passed to\na {@link Storage} method and {@link BlobId#getGeneration()} is {@code null} or no {@link\nBlobId} is provided an exception is thrown."}
     :boolean]
    [:userProject
     {:optional true,
      :doc
        "Returns an option for blob's billing user project. This option is only used by the buckets\nwith 'requester_pays' flag."}
     [:string {:min 1}]]
    [:metagenerationNotMatch
     {:optional true,
      :doc
        "Returns an option for blob's metageneration mismatch. If this option is used the request will\nfail if blob's metageneration matches the provided value."}
     :i64]
    [:softDeleted
     {:optional true,
      :doc
        "Returns an option for whether the request should return a soft-deleted object. If an object\nhas been soft-deleted (Deleted while a Soft Delete Policy) is in place, this must be true or\nthe request will return null."}
     :boolean]]])

(do
  (defn ^Storage$BlobRestoreOption/1 BlobRestoreOption-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BlobRestoreOption arg)
    (into-array
      Storage$BlobRestoreOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :generationMatch
              (conj acc (Storage$BlobRestoreOption/generationMatch (long v)))
            :generationNotMatch
              (conj acc (Storage$BlobRestoreOption/generationNotMatch (long v)))
            :metagenerationMatch
              (conj acc
                    (Storage$BlobRestoreOption/metagenerationMatch (long v)))
            :metagenerationNotMatch
              (conj acc
                    (Storage$BlobRestoreOption/metagenerationNotMatch (long v)))
            :copySourceAcl (conj acc
                                 (Storage$BlobRestoreOption/copySourceAcl v))
            :extraHeaders (conj acc
                                (Storage$BlobRestoreOption/extraHeaders
                                  (into {} (map (fn [[k v]] [(name k) v])) v)))
            acc))
        []
        arg)))
  (defn ^Storage$BlobRestoreOption BlobRestoreOption-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BlobRestoreOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :generationMatch (reduced (Storage$BlobRestoreOption/generationMatch
                                      (long v)))
          :generationNotMatch
            (reduced (Storage$BlobRestoreOption/generationNotMatch (long v)))
          :metagenerationMatch
            (reduced (Storage$BlobRestoreOption/metagenerationMatch (long v)))
          :metagenerationNotMatch
            (reduced (Storage$BlobRestoreOption/metagenerationNotMatch (long
                                                                         v)))
          :copySourceAcl (reduced (Storage$BlobRestoreOption/copySourceAcl v))
          :extraHeaders (reduced (Storage$BlobRestoreOption/extraHeaders
                                   (into {} (map (fn [[k v]] [(name k) v])) v)))
          acc))
      nil
      arg)))

(def BlobRestoreOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying blob restore options *",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.BlobRestoreOption}
   [:map {:closed true}
    [:generationMatch
     {:optional true,
      :doc
        "Returns an option for blob's data generation match. If this option is used the request will\nfail if generation does not match."}
     :i64]
    [:generationNotMatch
     {:optional true,
      :doc
        "Returns an option for blob's data generation mismatch. If this option is used the request\nwill fail if blob's generation matches the provided value."}
     :i64]
    [:metagenerationMatch
     {:optional true,
      :doc
        "Returns an option for blob's metageneration match. If this option is used the request will\nfail if blob's metageneration does not match the provided value."}
     :i64]
    [:metagenerationNotMatch
     {:optional true,
      :doc
        "Returns an option for blob's metageneration mismatch. If this option is used the request will\nfail if blob's metageneration matches the provided value."}
     :i64]
    [:copySourceAcl
     {:optional true,
      :doc
        "Returns an option for whether the restored object should copy the access controls of the\nsource object."}
     :boolean]
    [:extraHeaders
     {:optional true,
      :doc
        "A set of extra headers to be set for all requests performed within the scope of the operation\nthis option is passed to (a get, read, resumable upload etc).\n\n<p>If the same header name is specified across multiple options provided to a method, the\nfirst occurrence will be the value included in the request(s).\n\n<p>The following headers are not allowed to be specified, and will result in an {@link\nIllegalArgumentException}.\n\n<ol>\n  <li>{@code Accept-Encoding}\n  <li>{@code Cache-Control}\n  <li>{@code Connection}\n  <li>{@code Content-ID}\n  <li>{@code Content-Length}\n  <li>{@code Content-Range}\n  <li>{@code Content-Transfer-Encoding}\n  <li>{@code Content-Type}\n  <li>{@code Date}\n  <li>{@code ETag}\n  <li>{@code If-Match}\n  <li>{@code If-None-Match}\n  <li>{@code Keep-Alive}\n  <li>{@code Range}\n  <li>{@code TE}\n  <li>{@code Trailer}\n  <li>{@code Transfer-Encoding}\n  <li>{@code User-Agent}\n  <li>{@code X-Goog-Api-Client}\n  <li>{@code X-Goog-Content-Length-Range}\n  <li>{@code X-Goog-Copy-Source-Encryption-Algorithm}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Encryption-Algorithm}\n  <li>{@code X-Goog-Encryption-Key}\n  <li>{@code X-Goog-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Gcs-Idempotency-Token}\n  <li>{@code X-Goog-Meta-*}\n  <li>{@code X-Goog-User-Project}\n  <li>{@code X-HTTP-Method-Override}\n  <li>{@code X-Upload-Content-Length}\n  <li>{@code X-Upload-Content-Type}\n</ol>\n\n@since 2.49.0"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]]])

(do
  (defn ^Storage$BucketListOption/1 BucketListOption-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BucketListOption arg)
    (into-array
      Storage$BucketListOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :pageSize (conj acc (Storage$BucketListOption/pageSize (long v)))
            :returnPartialSuccess
              (conj acc (Storage$BucketListOption/returnPartialSuccess v))
            :pageToken (conj acc (Storage$BucketListOption/pageToken v))
            :prefix (conj acc (Storage$BucketListOption/prefix v))
            :userProject (conj acc (Storage$BucketListOption/userProject v))
            :fields (conj acc
                          (Storage$BucketListOption/fields
                            (into-array Storage$BucketField
                                        (map Storage$BucketField/valueOf v))))
            :extraHeaders (conj acc
                                (Storage$BucketListOption/extraHeaders
                                  (into {} (map (fn [[k v]] [(name k) v])) v)))
            acc))
        []
        arg)))
  (defn ^Storage$BucketListOption BucketListOption-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BucketListOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :pageSize (reduced (Storage$BucketListOption/pageSize (long v)))
          :returnPartialSuccess
            (reduced (Storage$BucketListOption/returnPartialSuccess v))
          :pageToken (reduced (Storage$BucketListOption/pageToken v))
          :prefix (reduced (Storage$BucketListOption/prefix v))
          :userProject (reduced (Storage$BucketListOption/userProject v))
          :fields (reduced (Storage$BucketListOption/fields
                             (into-array Storage$BucketField
                                         (map Storage$BucketField/valueOf v))))
          :extraHeaders (reduced (Storage$BucketListOption/extraHeaders
                                   (into {} (map (fn [[k v]] [(name k) v])) v)))
          acc))
      nil
      arg)))

(def BucketListOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying bucket list options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.BucketListOption}
   [:map {:closed true}
    [:pageSize
     {:optional true,
      :doc
        "Returns an option to specify the maximum number of buckets returned per page."}
     :i64] [:returnPartialSuccess {:optional true} :boolean]
    [:pageToken
     {:optional true,
      :doc
        "Returns an option to specify the page token from which to start listing buckets."}
     [:string {:min 1}]]
    [:prefix
     {:optional true,
      :doc
        "Returns an option to set a prefix to filter results to buckets whose names begin with this\nprefix."}
     [:string {:min 1}]]
    [:userProject
     {:optional true,
      :doc
        "Returns an option for bucket's billing user project. This option is only used by the buckets\nwith 'requester_pays' flag."}
     [:string {:min 1}]]
    [:fields
     {:optional true,
      :doc
        "Returns an option to specify the bucket's fields to be returned by the RPC call. If this\noption is not provided all bucket's fields are returned. {@code BucketListOption.fields}) can\nbe used to specify only the fields of interest. Bucket name is always returned, even if not\nspecified."}
     [:sequential {:min 1}
      [:enum {:closed true} "ID" "SELF_LINK" "NAME" "TIME_CREATED"
       "METAGENERATION" "ACL" "DEFAULT_OBJECT_ACL" "OWNER" "LABELS" "LOCATION"
       "LOCATION_TYPE" "WEBSITE" "VERSIONING" "CORS" "LIFECYCLE" "STORAGE_CLASS"
       "ETAG" "ENCRYPTION" "BILLING" "DEFAULT_EVENT_BASED_HOLD"
       "RETENTION_POLICY" "IAMCONFIGURATION" "LOGGING" "UPDATED" "RPO"
       "CUSTOM_PLACEMENT_CONFIG" "AUTOCLASS" "HIERARCHICAL_NAMESPACE"
       "OBJECT_RETENTION" "SOFT_DELETE_POLICY" "PROJECT" "IP_FILTER"]]]
    [:extraHeaders
     {:optional true,
      :doc
        "A set of extra headers to be set for all requests performed within the scope of the operation\nthis option is passed to (a get, read, resumable upload etc).\n\n<p>If the same header name is specified across multiple options provided to a method, the\nfirst occurrence will be the value included in the request(s).\n\n<p>The following headers are not allowed to be specified, and will result in an {@link\nIllegalArgumentException}.\n\n<ol>\n  <li>{@code Accept-Encoding}\n  <li>{@code Cache-Control}\n  <li>{@code Connection}\n  <li>{@code Content-ID}\n  <li>{@code Content-Length}\n  <li>{@code Content-Range}\n  <li>{@code Content-Transfer-Encoding}\n  <li>{@code Content-Type}\n  <li>{@code Date}\n  <li>{@code ETag}\n  <li>{@code If-Match}\n  <li>{@code If-None-Match}\n  <li>{@code Keep-Alive}\n  <li>{@code Range}\n  <li>{@code TE}\n  <li>{@code Trailer}\n  <li>{@code Transfer-Encoding}\n  <li>{@code User-Agent}\n  <li>{@code X-Goog-Api-Client}\n  <li>{@code X-Goog-Content-Length-Range}\n  <li>{@code X-Goog-Copy-Source-Encryption-Algorithm}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Encryption-Algorithm}\n  <li>{@code X-Goog-Encryption-Key}\n  <li>{@code X-Goog-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Gcs-Idempotency-Token}\n  <li>{@code X-Goog-Meta-*}\n  <li>{@code X-Goog-User-Project}\n  <li>{@code X-HTTP-Method-Override}\n  <li>{@code X-Upload-Content-Length}\n  <li>{@code X-Upload-Content-Type}\n</ol>\n\n@since 2.49.0"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]]])

(do
  (defn ^Storage$BlobListOption/1 BlobListOption-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BlobListOption arg)
    (into-array
      Storage$BlobListOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :extraHeaders (conj acc
                                (Storage$BlobListOption/extraHeaders
                                  (into {} (map (fn [[k v]] [(name k) v])) v)))
            :currentDirectory (if v
                                (clojure.core/conj
                                  acc
                                  (Storage$BlobListOption/currentDirectory))
                                acc)
            :startOffset (conj acc (Storage$BlobListOption/startOffset v))
            :includeFolders (conj acc (Storage$BlobListOption/includeFolders v))
            :fields (conj acc
                          (Storage$BlobListOption/fields
                            (into-array Storage$BlobField
                                        (map Storage$BlobField/valueOf v))))
            :endOffset (conj acc (Storage$BlobListOption/endOffset v))
            :prefix (conj acc (Storage$BlobListOption/prefix v))
            :pageToken (conj acc (Storage$BlobListOption/pageToken v))
            :delimiter (conj acc (Storage$BlobListOption/delimiter v))
            :filter (conj acc (Storage$BlobListOption/filter v))
            :pageSize (conj acc (Storage$BlobListOption/pageSize (long v)))
            :userProject (conj acc (Storage$BlobListOption/userProject v))
            :includeTrailingDelimiter
              (if v
                (clojure.core/conj
                  acc
                  (Storage$BlobListOption/includeTrailingDelimiter))
                acc)
            :matchGlob (conj acc (Storage$BlobListOption/matchGlob v))
            :softDeleted (conj acc (Storage$BlobListOption/softDeleted v))
            :versions (conj acc (Storage$BlobListOption/versions v))
            acc))
        []
        arg)))
  (defn ^Storage$BlobListOption BlobListOption-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.BlobListOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :extraHeaders (reduced (Storage$BlobListOption/extraHeaders
                                   (into {} (map (fn [[k v]] [(name k) v])) v)))
          :currentDirectory
            (if v (reduced (Storage$BlobListOption/currentDirectory)) acc)
          :startOffset (reduced (Storage$BlobListOption/startOffset v))
          :includeFolders (reduced (Storage$BlobListOption/includeFolders v))
          :fields (reduced (Storage$BlobListOption/fields
                             (into-array Storage$BlobField
                                         (map Storage$BlobField/valueOf v))))
          :endOffset (reduced (Storage$BlobListOption/endOffset v))
          :prefix (reduced (Storage$BlobListOption/prefix v))
          :pageToken (reduced (Storage$BlobListOption/pageToken v))
          :delimiter (reduced (Storage$BlobListOption/delimiter v))
          :filter (reduced (Storage$BlobListOption/filter v))
          :pageSize (reduced (Storage$BlobListOption/pageSize (long v)))
          :userProject (reduced (Storage$BlobListOption/userProject v))
          :includeTrailingDelimiter
            (if v
              (reduced (Storage$BlobListOption/includeTrailingDelimiter))
              acc)
          :matchGlob (reduced (Storage$BlobListOption/matchGlob v))
          :softDeleted (reduced (Storage$BlobListOption/softDeleted v))
          :versions (reduced (Storage$BlobListOption/versions v))
          acc))
      nil
      arg)))

(def BlobListOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying blob list options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.BlobListOption}
   [:map {:closed true}
    [:extraHeaders
     {:optional true,
      :doc
        "A set of extra headers to be set for all requests performed within the scope of the operation\nthis option is passed to (a get, read, resumable upload etc).\n\n<p>If the same header name is specified across multiple options provided to a method, the\nfirst occurrence will be the value included in the request(s).\n\n<p>The following headers are not allowed to be specified, and will result in an {@link\nIllegalArgumentException}.\n\n<ol>\n  <li>{@code Accept-Encoding}\n  <li>{@code Cache-Control}\n  <li>{@code Connection}\n  <li>{@code Content-ID}\n  <li>{@code Content-Length}\n  <li>{@code Content-Range}\n  <li>{@code Content-Transfer-Encoding}\n  <li>{@code Content-Type}\n  <li>{@code Date}\n  <li>{@code ETag}\n  <li>{@code If-Match}\n  <li>{@code If-None-Match}\n  <li>{@code Keep-Alive}\n  <li>{@code Range}\n  <li>{@code TE}\n  <li>{@code Trailer}\n  <li>{@code Transfer-Encoding}\n  <li>{@code User-Agent}\n  <li>{@code X-Goog-Api-Client}\n  <li>{@code X-Goog-Content-Length-Range}\n  <li>{@code X-Goog-Copy-Source-Encryption-Algorithm}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key}\n  <li>{@code X-Goog-Copy-Source-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Encryption-Algorithm}\n  <li>{@code X-Goog-Encryption-Key}\n  <li>{@code X-Goog-Encryption-Key-Sha256}\n  <li>{@code X-Goog-Gcs-Idempotency-Token}\n  <li>{@code X-Goog-Meta-*}\n  <li>{@code X-Goog-User-Project}\n  <li>{@code X-HTTP-Method-Override}\n  <li>{@code X-Upload-Content-Length}\n  <li>{@code X-Upload-Content-Type}\n</ol>\n\n@since 2.49.0"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
    [:currentDirectory
     {:optional true,
      :doc
        "If specified, results are returned in a directory-like mode. Blobs whose names, after a\npossible {@link #prefix(String)}, do not contain the '/' delimiter are returned as is. Blobs\nwhose names, after a possible {@link #prefix(String)}, contain the '/' delimiter, will have\ntheir name truncated after the delimiter and will be returned as {@link Blob} objects where\nonly {@link Blob#getBlobId()}, {@link Blob#getSize()} and {@link Blob#isDirectory()} are set.\nFor such directory blobs, ({@link BlobId#getGeneration()} returns {@code null}), {@link\nBlob#getSize()} returns {@code 0} while {@link Blob#isDirectory()} returns {@code true}.\nDuplicate directory blobs are omitted."}
     :boolean]
    [:startOffset
     {:optional true,
      :doc
        "Returns an option to set a startOffset to filter results to objects whose names are\nlexicographically equal to or after startOffset. If endOffset is also set, the objects listed\nhave names between startOffset (inclusive) and endOffset (exclusive).\n\n@param startOffset startOffset to filter the results"}
     [:string {:min 1}]]
    [:includeFolders
     {:optional true,
      :doc
        "Returns an option for whether to include all Folders (including empty Folders) in response."}
     :boolean]
    [:fields
     {:optional true,
      :doc
        "Returns an option to specify the blob's fields to be returned by the RPC call. If this option\nis not provided all blob's fields are returned. {@code BlobListOption.fields}) can be used to\nspecify only the fields of interest. Blob name and bucket are always returned, even if not\nspecified."}
     [:sequential {:min 1}
      [:enum {:closed true} "ACL" "BUCKET" "CACHE_CONTROL" "COMPONENT_COUNT"
       "CONTENT_DISPOSITION" "CONTENT_ENCODING" "CONTENT_LANGUAGE"
       "CONTENT_TYPE" "CRC32C" "ETAG" "GENERATION" "ID" "KIND" "MD5HASH"
       "MEDIA_LINK" "METADATA" "METAGENERATION" "NAME" "OWNER" "SELF_LINK"
       "SIZE" "STORAGE_CLASS" "TIME_DELETED" "TIME_CREATED" "KMS_KEY_NAME"
       "EVENT_BASED_HOLD" "TEMPORARY_HOLD" "RETENTION_EXPIRATION_TIME" "UPDATED"
       "CUSTOM_TIME" "TIME_STORAGE_CLASS_UPDATED" "CUSTOMER_ENCRYPTION"
       "RETENTION" "SOFT_DELETE_TIME" "HARD_DELETE_TIME" "OBJECT_CONTEXTS"]]]
    [:endOffset
     {:optional true,
      :doc
        "Returns an option to set a endOffset to filter results to objects whose names are\nlexicographically before endOffset. If startOffset is also set, the objects listed have names\nbetween startOffset (inclusive) and endOffset (exclusive).\n\n@param endOffset endOffset to filter the results"}
     [:string {:min 1}]]
    [:prefix
     {:optional true,
      :doc
        "Returns an option to set a prefix to filter results to blobs whose names begin with this\nprefix."}
     [:string {:min 1}]]
    [:pageToken
     {:optional true,
      :doc
        "Returns an option to specify the page token from which to start listing blobs."}
     [:string {:min 1}]]
    [:delimiter
     {:optional true,
      :doc
        "Returns an option to set a delimiter.\n\n@param delimiter generally '/' is the one used most often, but you can used other delimiters\n    as well."}
     [:string {:min 1}]]
    [:filter
     {:optional true,
      :doc
        "Returns an option to filter list results based on object attributes, such as object contexts.\n\n@param filter The filter string."}
     [:string {:min 1}]]
    [:pageSize
     {:optional true,
      :doc
        "Returns an option to specify the maximum number of blobs returned per page."}
     :i64]
    [:userProject
     {:optional true,
      :doc
        "Returns an option to define the billing user project. This option is required by buckets with\n`requester_pays` flag enabled to assign operation costs.\n\n@param userProject projectId of the billing user project."}
     [:string {:min 1}]]
    [:includeTrailingDelimiter
     {:optional true,
      :doc
        "Returns an option which will cause blobs that end in exactly one instance of `delimiter` will\nhave their metadata included rather than being synthetic objects.\n\n@since 2.52.0"}
     :boolean]
    [:matchGlob
     {:optional true,
      :doc
        "Returns an option to set a glob pattern to filter results to blobs that match the pattern.\n\n@see <a href=\"https://cloud.google.com/storage/docs/json_api/v1/objects/list\">List\n    Objects</a>"}
     [:string {:min 1}]]
    [:softDeleted
     {:optional true,
      :doc
        "Returns an option for whether the list result should include soft-deleted objects."}
     :boolean]
    [:versions
     {:optional true,
      :doc
        "If set to {@code true}, lists all versions of a blob. The default is {@code false}.\n\n@see <a href=\"https://cloud.google.com/storage/docs/object-versioning\">Object Versioning</a>"}
     :boolean]]])

(def PostPolicyV4Option$Option-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.storage/Storage.PostPolicyV4Option.Option} "PATH_STYLE"
   "VIRTUAL_HOSTED_STYLE" "BUCKET_BOUND_HOST_NAME" "SERVICE_ACCOUNT_CRED"])

(def PostPolicyV4Option$Option-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.storage/Storage.PostPolicyV4Option.Option} "PATH_STYLE"
   "VIRTUAL_HOSTED_STYLE" "BUCKET_BOUND_HOST_NAME" "SERVICE_ACCOUNT_CRED"])

(do
  (defn ^Storage$PostPolicyV4Option/1 PostPolicyV4Option-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.PostPolicyV4Option arg)
    (into-array
      Storage$PostPolicyV4Option
      (reduce-kv
        (fn [acc k v]
          (case k
            :signWith (conj acc
                            (Storage$PostPolicyV4Option/signWith
                              (auth/ServiceAccountSigner-from-edn v)))
            :withVirtualHostedStyle
              (if v
                (clojure.core/conj
                  acc
                  (Storage$PostPolicyV4Option/withVirtualHostedStyle))
                acc)
            :withPathStyle (if v
                             (clojure.core/conj
                               acc
                               (Storage$PostPolicyV4Option/withPathStyle))
                             acc)
            :withBucketBoundHostname
              (conj acc (Storage$PostPolicyV4Option/withBucketBoundHostname v))
            acc))
        []
        arg)))
  (defn ^Storage$PostPolicyV4Option PostPolicyV4Option-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.PostPolicyV4Option arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :signWith (reduced (Storage$PostPolicyV4Option/signWith
                               (auth/ServiceAccountSigner-from-edn v)))
          :withVirtualHostedStyle
            (if v
              (reduced (Storage$PostPolicyV4Option/withVirtualHostedStyle))
              acc)
          :withPathStyle
            (if v (reduced (Storage$PostPolicyV4Option/withPathStyle)) acc)
          :withBucketBoundHostname
            (reduced (Storage$PostPolicyV4Option/withBucketBoundHostname v))
          acc))
      nil
      arg)))

(def PostPolicyV4Option-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying Post Policy V4 options. *",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.PostPolicyV4Option}
   [:map {:closed true}
    [:signWith
     {:optional true,
      :doc
        "Provides a service account signer to sign the policy. If not provided an attempt is made to\nget it from the environment.\n\n@see <a href=\"https://cloud.google.com/storage/docs/authentication#service_accounts\">Service\n    Accounts</a>"}
     :gcp.foreign.com.google.auth/ServiceAccountSigner]
    [:withVirtualHostedStyle
     {:optional true,
      :doc
        "Use a virtual hosted-style hostname, which adds the bucket into the host portion of the URI\nrather than the path, e.g. 'https://mybucket.storage.googleapis.com/...'. The bucket name is\nobtained from the resource passed in.\n\n@see <a href=\"https://cloud.google.com/storage/docs/request-endpoints\">Request Endpoints</a>"}
     :boolean]
    [:withPathStyle
     {:optional true,
      :doc
        "Generates a path-style URL, which places the bucket name in the path portion of the URL\ninstead of in the hostname, e.g 'https://storage.googleapis.com/mybucket/...'. Note that this\ncannot be used alongside {@code withVirtualHostedStyle()}. Virtual hosted-style URLs, which\ncan be used via the {@code withVirtualHostedStyle()} method, should generally be preferred\ninstead of path-style URLs.\n\n@see <a href=\"https://cloud.google.com/storage/docs/request-endpoints\">Request Endpoints</a>"}
     :boolean]
    [:withBucketBoundHostname
     {:optional true,
      :doc
        "Use a bucket-bound hostname, which replaces the storage.googleapis.com host with the name of\na CNAME bucket, e.g. a bucket named 'gcs-subdomain.my.domain.tld', or a Google Cloud Load\nBalancer which routes to a bucket you own, e.g. 'my-load-balancer-domain.tld'. Note that this\ncannot be used alongside {@code withVirtualHostedStyle()} or {@code withPathStyle()}. This\nmethod signature uses HTTP for the URI scheme, and is equivalent to calling {@code\nwithBucketBoundHostname(\"...\", UriScheme.HTTP).}\n\n@see <a href=\"https://cloud.google.com/storage/docs/request-endpoints#cname\">CNAME\n    Redirects</a>\n@see <a\n    href=\"https://cloud.google.com/load-balancing/docs/https/adding-backend-buckets-to-load-balancers\">\n    GCLB Redirects</a>"}
     [:string {:min 1}]]]])

(def SignUrlOption$Option-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.storage/Storage.SignUrlOption.Option} "HTTP_METHOD"
   "CONTENT_TYPE" "MD5" "EXT_HEADERS" "SERVICE_ACCOUNT_CRED" "SIGNATURE_VERSION"
   "HOST_NAME" "PATH_STYLE" "VIRTUAL_HOSTED_STYLE" "BUCKET_BOUND_HOST_NAME"
   "QUERY_PARAMS"])

(def SignUrlOption$SignatureVersion-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.storage/Storage.SignUrlOption.SignatureVersion} "V2" "V4"])

(def SignUrlOption$Option-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.storage/Storage.SignUrlOption.Option} "HTTP_METHOD"
   "CONTENT_TYPE" "MD5" "EXT_HEADERS" "SERVICE_ACCOUNT_CRED" "SIGNATURE_VERSION"
   "HOST_NAME" "PATH_STYLE" "VIRTUAL_HOSTED_STYLE" "BUCKET_BOUND_HOST_NAME"
   "QUERY_PARAMS"])

(def SignUrlOption$SignatureVersion-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.storage/Storage.SignUrlOption.SignatureVersion} "V2" "V4"])

(do
  (defn ^Storage$SignUrlOption/1 SignUrlOption-Array-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.SignUrlOption arg)
    (into-array
      Storage$SignUrlOption
      (reduce-kv
        (fn [acc k v]
          (case k
            :withMd5
              (if v (clojure.core/conj acc (Storage$SignUrlOption/withMd5)) acc)
            :withQueryParams (conj
                               acc
                               (Storage$SignUrlOption/withQueryParams
                                 (into {} (map (fn [[k v]] [(name k) v])) v)))
            :withContentType
              (if v
                (clojure.core/conj acc (Storage$SignUrlOption/withContentType))
                acc)
            :withV4Signature
              (if v
                (clojure.core/conj acc (Storage$SignUrlOption/withV4Signature))
                acc)
            :withExtHeaders (conj
                              acc
                              (Storage$SignUrlOption/withExtHeaders
                                (into {} (map (fn [[k v]] [(name k) v])) v)))
            :withPathStyle
              (if v
                (clojure.core/conj acc (Storage$SignUrlOption/withPathStyle))
                acc)
            :withV2Signature
              (if v
                (clojure.core/conj acc (Storage$SignUrlOption/withV2Signature))
                acc)
            :withHostName (conj acc (Storage$SignUrlOption/withHostName v))
            :withBucketBoundHostname
              (conj acc (Storage$SignUrlOption/withBucketBoundHostname v))
            :withVirtualHostedStyle
              (if v
                (clojure.core/conj
                  acc
                  (Storage$SignUrlOption/withVirtualHostedStyle))
                acc)
            :httpMethod (conj acc
                              (Storage$SignUrlOption/httpMethod
                                (HttpMethod/valueOf v)))
            :signWith (conj acc
                            (Storage$SignUrlOption/signWith
                              (auth/ServiceAccountSigner-from-edn v)))
            acc))
        []
        arg)))
  (defn ^Storage$SignUrlOption SignUrlOption-from-edn
    [arg]
    (global/strict! :gcp.storage/Storage.SignUrlOption arg)
    (reduce-kv
      (fn [acc k v]
        (case k
          :withMd5 (if v (reduced (Storage$SignUrlOption/withMd5)) acc)
          :withQueryParams (reduced
                             (Storage$SignUrlOption/withQueryParams
                               (into {} (map (fn [[k v]] [(name k) v])) v)))
          :withContentType
            (if v (reduced (Storage$SignUrlOption/withContentType)) acc)
          :withV4Signature
            (if v (reduced (Storage$SignUrlOption/withV4Signature)) acc)
          :withExtHeaders (reduced
                            (Storage$SignUrlOption/withExtHeaders
                              (into {} (map (fn [[k v]] [(name k) v])) v)))
          :withPathStyle
            (if v (reduced (Storage$SignUrlOption/withPathStyle)) acc)
          :withV2Signature
            (if v (reduced (Storage$SignUrlOption/withV2Signature)) acc)
          :withHostName (reduced (Storage$SignUrlOption/withHostName v))
          :withBucketBoundHostname
            (reduced (Storage$SignUrlOption/withBucketBoundHostname v))
          :withVirtualHostedStyle
            (if v (reduced (Storage$SignUrlOption/withVirtualHostedStyle)) acc)
          :httpMethod (reduced (Storage$SignUrlOption/httpMethod
                                 (HttpMethod/valueOf v)))
          :signWith (reduced (Storage$SignUrlOption/signWith
                               (auth/ServiceAccountSigner-from-edn v)))
          acc))
      nil
      arg)))

(def SignUrlOption-schema
  [:maybe
   {:closed true,
    :doc "Class for specifying signed URL options.",
    :gcp/category :nested/client-options,
    :gcp/key :gcp.storage/Storage.SignUrlOption}
   [:map {:closed true}
    [:withMd5
     {:optional true,
      :doc
        "Use it if signature should include the blob's md5. When used, users of the signed URL should\ninclude the blob's md5 with their request."}
     :boolean]
    [:withQueryParams
     {:optional true,
      :doc
        "Use if the URL should contain additional query parameters.\n\n<p>Warning: For V2 Signed URLs, it is possible for query parameters to be altered after the\nURL has been signed, as the parameters are not used to compute the signature. The V4 signing\nmethod should be preferred when supplying additional query parameters, as the parameters\ncannot be added, removed, or otherwise altered after a V4 signature is generated.\n\n@see <a href=\"https://cloud.google.com/storage/docs/authentication/canonical-requests\">\n    Canonical Requests</a>\n@see <a href=\"https://cloud.google.com/storage/docs/access-control/signed-urls-v2\">V2 Signing\n    Process</a>"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
    [:withContentType
     {:optional true,
      :doc
        "Use it if signature should include the blob's content-type. When used, users of the signed\nURL should include the blob's content-type with their request. If using this URL from a\nbrowser, you must include a content type that matches what the browser will send."}
     :boolean]
    [:withV4Signature
     {:optional true,
      :doc
        "Use if signature version should be V4. Note that V4 Signed URLs can't have an expiration\nlonger than 7 days. V2 will be the default if neither this or {@code withV2Signature()} is\ncalled."}
     :boolean]
    [:withExtHeaders
     {:optional true,
      :doc
        "Use it if signature should include the blob's canonicalized extended headers. When used,\nusers of the signed URL should include the canonicalized extended headers with their request.\n\n@see <a href=\"https://cloud.google.com/storage/docs/xml-api/reference-headers\">Request\n    Headers</a>"}
     [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
    [:withPathStyle
     {:optional true,
      :doc
        "Generates a path-style URL, which places the bucket name in the path portion of the URL\ninstead of in the hostname, e.g 'https://storage.googleapis.com/mybucket/...'. This cannot be\nused alongside {@code withVirtualHostedStyle()}. Virtual hosted-style URLs, which can be used\nvia the {@code withVirtualHostedStyle()} method, should generally be preferred instead of\npath-style URLs.\n\n@see <a href=\"https://cloud.google.com/storage/docs/request-endpoints\">Request Endpoints</a>"}
     :boolean]
    [:withV2Signature
     {:optional true,
      :doc
        "Use if signature version should be V2. This is the default if neither this or {@code\nwithV4Signature()} is called."}
     :boolean]
    [:withHostName
     {:optional true,
      :doc
        "Use a different host name than the default host name 'storage.googleapis.com'. This option is\nparticularly useful for developers to point requests to an alternate endpoint (e.g. a staging\nenvironment or sending requests through VPC). If using this with the {@code\nwithVirtualHostedStyle()} method, you should omit the bucket name from the hostname, as it\nautomatically gets prepended to the hostname for virtual hosted-style URLs."}
     [:string {:min 1}]]
    [:withBucketBoundHostname
     {:optional true,
      :doc
        "Use a bucket-bound hostname, which replaces the storage.googleapis.com host with the name of\na CNAME bucket, e.g. a bucket named 'gcs-subdomain.my.domain.tld', or a Google Cloud Load\nBalancer which routes to a bucket you own, e.g. 'my-load-balancer-domain.tld'. This cannot be\nused alongside {@code withVirtualHostedStyle()} or {@code withPathStyle()}. This method\nsignature uses HTTP for the URI scheme, and is equivalent to calling {@code\nwithBucketBoundHostname(\"...\", UriScheme.HTTP).}\n\n@see <a href=\"https://cloud.google.com/storage/docs/request-endpoints#cname\">CNAME\n    Redirects</a>\n@see <a\n    href=\"https://cloud.google.com/load-balancing/docs/https/adding-backend-buckets-to-load-balancers\">\n    GCLB Redirects</a>"}
     [:string {:min 1}]]
    [:withVirtualHostedStyle
     {:optional true,
      :doc
        "Use a virtual hosted-style hostname, which adds the bucket into the host portion of the URI\nrather than the path, e.g. 'https://mybucket.storage.googleapis.com/...'. The bucket name is\nobtained from the resource passed in. For V4 signing, this also sets the \"host\" header in the\ncanonicalized extension headers to the virtual hosted-style host, unless that header is\nsupplied via the {@code withExtHeaders()} method.\n\n@see <a href=\"https://cloud.google.com/storage/docs/request-endpoints\">Request Endpoints</a>"}
     :boolean]
    [:httpMethod
     {:optional true,
      :doc
        "The HTTP method to be used with the signed URL. If this method is not called, defaults to\nGET."}
     [:enum {:closed true} "GET" "HEAD" "PUT" "POST" "DELETE" "OPTIONS"]]
    [:signWith
     {:optional true,
      :doc
        "Provides a service account signer to sign the URL. If not provided an attempt is made to get\nit from the environment.\n\n@see <a href=\"https://cloud.google.com/storage/docs/authentication#service_accounts\">Service\n    Accounts</a>"}
     :gcp.foreign.com.google.auth/ServiceAccountSigner]]])

(defn ^Storage$ComposeRequest$SourceBlob ComposeRequest$SourceBlob-from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.storage.Storage.ComposeRequest.SourceBlob is read-only")))

(def ComposeRequest$SourceBlob-schema
  [:map
   {:closed true,
    :doc "Class for Compose source blobs.",
    :gcp/category :nested/read-only,
    :gcp/key :gcp.storage/Storage.ComposeRequest.SourceBlob}
   [:generation {:read-only? true} :i64]
   [:name {:read-only? true} [:string {:min 1}]]])

(defn ^Storage$ComposeRequest$SourceBlob ComposeRequest$SourceBlob-from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.storage.Storage.ComposeRequest.SourceBlob is read-only")))

(def ComposeRequest$SourceBlob-schema
  [:map
   {:closed true,
    :doc "Class for Compose source blobs.",
    :gcp/category :nested/read-only,
    :gcp/key :gcp.storage/Storage.ComposeRequest.SourceBlob}
   [:generation {:read-only? true} :i64]
   [:name {:read-only? true} [:string {:min 1}]]])

(defn ^Storage$ComposeRequest ComposeRequest-from-edn
  [arg]
  (let [builder (Storage$ComposeRequest/newBuilder)]
    (when (some? (get arg :target))
      (.setTarget builder (BlobInfo/from-edn (get arg :target))))
    (when (seq (get arg :targetOptions))
      (.setTargetOptions builder
                         (map BlobTargetOption-from-edn
                           (get arg :targetOptions))))
    (.build builder)))

(def ComposeRequest-schema
  [:map
   {:closed true,
    :doc
      "A class to contain all information needed for a Google Cloud Storage Compose operation.\n\n@see <a href=\"https://cloud.google.com/storage/docs/composite-objects#_Compose\">Compose\n    Operation</a>",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/Storage.ComposeRequest}
   [:sourceBlobs
    {:optional true,
     :read-only? true,
     :getter-doc "Returns compose operation's source blobs."}
    [:sequential {:min 1}
     [:ref :gcp.storage/Storage.ComposeRequest.SourceBlob]]]
   [:target
    {:optional true,
     :getter-doc "Returns compose operation's target blob.",
     :setter-doc "Sets compose operation's target blob."} :gcp.storage/BlobInfo]
   [:targetOptions
    {:optional true,
     :getter-doc "Returns compose operation's target blob's options.",
     :setter-doc "Sets compose operation's target blob options."}
    [:sequential {:min 1} [:ref :gcp.storage/Storage.BlobTargetOption]]]])

(defn ^Storage$CopyRequest CopyRequest-from-edn
  [arg]
  (let [builder (Storage$CopyRequest/newBuilder)]
    (when (some? (get arg :megabytesCopiedPerChunk))
      (.setMegabytesCopiedPerChunk builder
                                   (long (get arg :megabytesCopiedPerChunk))))
    (when (some? (get arg :source))
      (.setSource builder (BlobId/from-edn (get arg :source))))
    (when (seq (get arg :sourceOptions))
      (.setSourceOptions builder
                         (map BlobSourceOption-from-edn
                           (get arg :sourceOptions))))
    (when (some? (get arg :target))
      (.setTarget builder (BlobId/from-edn (get arg :target))))
    (.build builder)))

(def CopyRequest-schema
  [:map
   {:closed true,
    :doc
      "A class to contain all information needed for a Google Cloud Storage Copy operation.",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/Storage.CopyRequest}
   [:megabytesCopiedPerChunk
    {:optional true,
     :getter-doc
       "Returns the maximum number of megabytes to copy for each RPC call. This parameter is ignored\nif source and target blob share the same location and storage class as copy is made with one\nsingle RPC.",
     :setter-doc
       "Sets the maximum number of megabytes to copy for each RPC call. This parameter is ignored\nif source and target blob share the same location and storage class as copy is made with\none single RPC.\n\n@return the builder"}
    :i64]
   [:overrideInfo
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns whether to override the target blob information with {@link #getTarget()}. If {@code\ntrue}, the value of {@link #getTarget()} is used to replace source blob information (e.g.\n{@code contentType}, {@code contentLanguage}). Target blob information is set exactly to this\nvalue, no information is inherited from the source blob. If {@code false}, target blob\ninformation is inherited from the source blob."}
    :boolean]
   [:source
    {:optional true,
     :getter-doc "Returns the blob to copy, as a {@link BlobId}.",
     :setter-doc
       "Sets the blob to copy given a {@link BlobId}.\n\n@return the builder"}
    :gcp.storage/BlobId]
   [:sourceOptions
    {:optional true,
     :getter-doc "Returns blob's source options.",
     :setter-doc "Sets blob's source options.\n\n@return the builder"}
    [:sequential {:min 1} [:ref :gcp.storage/Storage.BlobSourceOption]]]
   [:target
    {:optional true,
     :getter-doc "Returns the {@link BlobInfo} for the target blob.",
     :setter-doc
       "Sets the copy target. Target blob information is copied from source.\n\n@return the builder"}
    :gcp.storage/BlobId]
   [:targetOptions
    {:optional true,
     :read-only? true,
     :getter-doc "Returns blob's target options."}
    [:sequential {:min 1} [:ref :gcp.storage/Storage.BlobTargetOption]]]])

(defn ^Storage$MoveBlobRequest MoveBlobRequest-from-edn
  [arg]
  (let [builder (Storage$MoveBlobRequest/newBuilder)]
    (when (some? (get arg :source))
      (.setSource builder (BlobId/from-edn (get arg :source))))
    (when (seq (get arg :sourceOptions))
      (.setSourceOptions builder
                         (map BlobSourceOption-from-edn
                           (get arg :sourceOptions))))
    (when (some? (get arg :target))
      (.setTarget builder (BlobId/from-edn (get arg :target))))
    (when (seq (get arg :targetOptions))
      (.setTargetOptions builder
                         (map BlobTargetOption-from-edn
                           (get arg :targetOptions))))
    (.build builder)))

(def MoveBlobRequest-schema
  [:map
   {:closed true,
    :doc
      "A class to contain all information needed for a Google Cloud Storage Object Move.\n\n@since 2.48.0\n@see Storage#moveBlob(MoveBlobRequest)",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/Storage.MoveBlobRequest}
   [:source {:optional true, :setter-doc nil} :gcp.storage/BlobId]
   [:sourceOptions {:optional true, :setter-doc nil}
    [:sequential {:min 1} [:ref :gcp.storage/Storage.BlobSourceOption]]]
   [:target {:optional true, :setter-doc nil} :gcp.storage/BlobId]
   [:targetOptions {:optional true, :setter-doc nil}
    [:sequential {:min 1} [:ref :gcp.storage/Storage.BlobTargetOption]]]])

(global/include-schema-registry!
  (with-meta
    {:gcp.storage/Storage.BlobField BlobField-schema,
     :gcp.storage/Storage.BlobGetOption BlobGetOption-schema,
     :gcp.storage/Storage.BlobListOption BlobListOption-schema,
     :gcp.storage/Storage.BlobRestoreOption BlobRestoreOption-schema,
     :gcp.storage/Storage.BlobSourceOption BlobSourceOption-schema,
     :gcp.storage/Storage.BlobTargetOption BlobTargetOption-schema,
     :gcp.storage/Storage.BlobWriteOption BlobWriteOption-schema,
     :gcp.storage/Storage.BucketField BucketField-schema,
     :gcp.storage/Storage.BucketGetOption BucketGetOption-schema,
     :gcp.storage/Storage.BucketListOption BucketListOption-schema,
     :gcp.storage/Storage.BucketSourceOption BucketSourceOption-schema,
     :gcp.storage/Storage.BucketTargetOption BucketTargetOption-schema,
     :gcp.storage/Storage.ComposeRequest ComposeRequest-schema,
     :gcp.storage/Storage.ComposeRequest.SourceBlob
       ComposeRequest$SourceBlob-schema,
     :gcp.storage/Storage.CopyRequest CopyRequest-schema,
     :gcp.storage/Storage.CreateHmacKeyOption CreateHmacKeyOption-schema,
     :gcp.storage/Storage.DeleteHmacKeyOption DeleteHmacKeyOption-schema,
     :gcp.storage/Storage.GetHmacKeyOption GetHmacKeyOption-schema,
     :gcp.storage/Storage.ListHmacKeysOption ListHmacKeysOption-schema,
     :gcp.storage/Storage.MoveBlobRequest MoveBlobRequest-schema,
     :gcp.storage/Storage.PostPolicyV4Option PostPolicyV4Option-schema,
     :gcp.storage/Storage.PostPolicyV4Option.Option
       PostPolicyV4Option$Option-schema,
     :gcp.storage/Storage.PredefinedAcl PredefinedAcl-schema,
     :gcp.storage/Storage.SignUrlOption SignUrlOption-schema,
     :gcp.storage/Storage.SignUrlOption.Option SignUrlOption$Option-schema,
     :gcp.storage/Storage.SignUrlOption.SignatureVersion
       SignUrlOption$SignatureVersion-schema,
     :gcp.storage/Storage.UpdateHmacKeyOption UpdateHmacKeyOption-schema,
     :gcp.storage/Storage.UriScheme UriScheme-schema}
    {:gcp.global/name "gcp.storage.Storage"}))