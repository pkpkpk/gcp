(ns gcp.storage.v2.BucketInfo
  (:require [gcp.global :as global]
            [gcp.storage.v2.Acl :as Acl]
            [gcp.storage.v2.Cors :as Cors])
  (:import (com.google.cloud.storage BucketInfo BucketInfo$IamConfiguration StorageClass)))

(defn ^BucketInfo$IamConfiguration IamConfiguration:from-edn [arg]
  (let [builder (BucketInfo$IamConfiguration/newBuilder)]
    (throw (Exception. "unimplemented"))
    (.build builder)))

(defn IamConfiguration:to-edn [^BucketInfo$IamConfiguration arg]
  (throw (Exception. "unimplemented")))

(defn ^BucketInfo from-edn [arg]
  (global/strict! :gcp.storage.v2/BucketInfo arg)
  (let [builder (BucketInfo/newBuilder (:name arg))]
    (when (:acl arg)
      (.setAcl builder (map Acl/from-edn (:acl arg))))
    (when (:autoclass arg)
      (.setAutoclass builder (:autoclass arg)))
    (when (:cors arg)
      (.setCors builder (map Cors/from-edn (:cors arg))))
    (when (:createTimeOffsetDateTime arg)
      (.setCreateTimeOffsetDateTime builder (:createTimeOffsetDateTime arg)))
    (when (:customPlacementConfig arg)
      (.setCustomPlacementConfig builder (:customPlacementConfig arg)))
    (when (:defaultAcl arg)
      (.setDefaultAcl builder (vec (map Acl/from-edn (:defaultAcl arg)))))
    (when (:defaultEventBasedHold arg)
      (.setDefaultEventBasedHold builder (:defaultEventBasedHold arg)))
    (when (:defaultKmsKeyName arg)
      (.setDefaultKmsKeyName builder (:defaultKmsKeyName arg)))
    ;(when (:etag arg)
    ;  (.setEtag builder (:etag arg)))
    ;(when (:generatedId arg)
    ;  (.setGeneratedId builder (:generatedId arg)))
    (when (:hierarchicalNamespace arg)
      (.setHierarchicalNamespace builder (:hierarchicalNamespace arg)))
    (when (:iamConfiguration arg)
      (.setIamConfiguration builder (IamConfiguration:from-edn (:iamConfiguration arg))))
    (when (:indexPage arg)
      (.setIndexPage builder (:indexPage arg)))
    (when (:labels arg)
      (.setLabels builder (:labels arg)))
    (when (:lifecycleRules arg)
      (.setLifecycleRules builder (vec (:lifecycleRules arg))))
    (when (:location arg)
      (.setLocation builder (:location arg)))
    ;(when (:locationType arg)
    ;  (.setLocationType builder (:locationType arg)))
    (when (:logging arg)
      (.setLogging builder (:logging arg)))
    (when (:metageneration arg)
      (.setMetageneration builder (:metageneration arg)))
    (when (:notFoundPage arg)
      (.setNotFoundPage builder (:notFoundPage arg)))
    (when (:objectRetention arg)
      (.setObjectRetention builder (:objectRetention arg)))
    (when (:owner arg)
      (.setOwner builder (:owner arg)))
    (when (:retentionEffectiveTimeOffsetDateTime arg)
      (.setRetentionEffectiveTimeOffsetDateTime builder (:retentionEffectiveTimeOffsetDateTime arg)))
    (when (:retentionPeriodDuration arg)
      (.setRetentionPeriodDuration builder (:retentionPeriodDuration arg)))
    (when (:rpo arg)
      (.setRpo builder (:rpo arg)))
    (when (:softDeletePolicy arg)
      (.setSoftDeletePolicy builder (:softDeletePolicy arg)))
    (when (:storageClass arg)
      (.setStorageClass builder (StorageClass/valueOf (:storageClass arg))))
    (when (:updateTimeOffsetDateTime arg)
      (.setUpdateTimeOffsetDateTime builder (:updateTimeOffsetDateTime arg)))
    (when (:requesterPays arg)
      (.setRequesterPays builder (:requesterPays arg)))
    (when (:retentionPolicyIsLocked arg)
      (.setRetentionPolicyIsLocked builder (:retentionPolicyIsLocked arg)))
    (when (:versioningEnabled arg)
      (.setVersioningEnabled builder (:versioningEnabled arg)))
    (.build builder)))

(defn to-edn [^BucketInfo arg]
  {:post [(global/strict! :gcp.storage.v2/BucketInfo %)]}
  (cond-> {:name (.getName arg)}
          (seq (.getAcl arg))
          (assoc :acl (vec (.getAcl arg))) ;; or (mapv Acl/to-edn (.getAcl arg)) if you have Acl/to-edn

          (some? (.getAutoclass arg))
          (assoc :autoclass (.getAutoclass arg))

          (seq (.getCors arg))
          (assoc :cors (vec (.getCors arg)))

          (some? (.getCreateTimeOffsetDateTime arg))
          (assoc :createTimeOffsetDateTime (.getCreateTimeOffsetDateTime arg))

          (some? (.getCustomPlacementConfig arg))
          (assoc :customPlacementConfig (.getCustomPlacementConfig arg))

          (seq (.getDefaultAcl arg))
          (assoc :defaultAcl (vec (.getDefaultAcl arg)))

          (some? (.getDefaultEventBasedHold arg))
          (assoc :defaultEventBasedHold (.getDefaultEventBasedHold arg))

          (some? (.getDefaultKmsKeyName arg))
          (assoc :defaultKmsKeyName (.getDefaultKmsKeyName arg))

          (some? (.getEtag arg))
          (assoc :etag (.getEtag arg))

          (some? (.getGeneratedId arg))
          (assoc :generatedId (.getGeneratedId arg))

          (some? (.getHierarchicalNamespace arg))
          (assoc :hierarchicalNamespace (.getHierarchicalNamespace arg))

          (some? (.getIamConfiguration arg))
          (assoc :iamConfiguration (.getIamConfiguration arg))

          (some? (.getIndexPage arg))
          (assoc :indexPage (.getIndexPage arg))

          (seq (.getLabels arg))
          (assoc :labels (into {} (.getLabels arg)))

          (seq (.getLifecycleRules arg))
          (assoc :lifecycleRules (vec (.getLifecycleRules arg)))

          (some? (.getLocation arg))
          (assoc :location (.getLocation arg))

          (some? (.getLocationType arg))
          (assoc :locationType (.getLocationType arg))

          (some? (.getLogging arg))
          (assoc :logging (.getLogging arg))

          (some? (.getMetageneration arg))
          (assoc :metageneration (.getMetageneration arg))

          (some? (.getNotFoundPage arg))
          (assoc :notFoundPage (.getNotFoundPage arg))

          (some? (.getObjectRetention arg))
          (assoc :objectRetention (.getObjectRetention arg))

          (some? (.getOwner arg))
          (assoc :owner (.getOwner arg))

          (some? (.getRetentionEffectiveTimeOffsetDateTime arg))
          (assoc :retentionEffectiveTimeOffsetDateTime (.getRetentionEffectiveTimeOffsetDateTime arg))

          (some? (.getRetentionPeriodDuration arg))
          (assoc :retentionPeriodDuration (.getRetentionPeriodDuration arg))

          (some? (.getRpo arg))
          (assoc :rpo (.getRpo arg))

          (some? (.getSoftDeletePolicy arg))
          (assoc :softDeletePolicy (.getSoftDeletePolicy arg))

          (some? (.getStorageClass arg))
          (assoc :storageClass (.getStorageClass arg))

          (some? (.getUpdateTimeOffsetDateTime arg))
          (assoc :updateTimeOffsetDateTime (.getUpdateTimeOffsetDateTime arg))

          (some? (.requesterPays arg))
          (assoc :requesterPays (.requesterPays arg))

          (some? (.retentionPolicyIsLocked arg))
          (assoc :retentionPolicyIsLocked (.retentionPolicyIsLocked arg))

          (some? (.versioningEnabled arg))
          (assoc :versioningEnabled (.versioningEnabled arg))))

(def schemas
  {:gcp.storage.v2/BucketInfo
   [:map
    {:class 'com.google.cloud.storage.BucketInfo}
    [:name :string]
    [:acl {:doc "Returns the bucket's access control configuration. See Also: About Access Control Lists." :optional true} [:sequential :any]]
    [:autoclass {:doc "Returns the Autoclass configuration." :optional true} :any]
    [:cors {:doc "Returns the bucket's Cross-Origin Resource Sharing (CORS) configuration. See Also: CORS." :optional true} [:sequential :any]]
    [:createTime {:doc "Deprecated. #getCreateTimeOffsetDateTime(). Returns the time (millis) at which the bucket was created." :optional true} :int]
    [:createTimeOffsetDateTime {:doc "Returns the time at which the bucket was created as OffsetDateTime." :optional true} :any]
    [:customPlacementConfig {:doc "Returns the Custom Placement Configuration for multi-regional setup." :optional true} :any]
    [:defaultAcl {:doc "Returns the default access control configuration for this bucket's blobs. See Also: ACLs." :optional true} [:sequential :any]]
    [:defaultEventBasedHold {:doc "Returns a Boolean that may be true, false, or null indicating default event-based hold." :optional true} :boolean]
    [:defaultKmsKeyName {:doc "Returns the default Cloud KMS key name for newly inserted objects." :optional true} :string]
    [:deleteRules {:doc "Deprecated. Lifecycle configuration as a list of delete rules. See Also: Lifecycle Management." :optional true} [:sequential :any]]
    [:etag {:doc "Returns the HTTP 1.1 Entity tag for the bucket." :optional true} :string]
    [:generatedId {:doc "Returns the service-generated id for the bucket." :optional true} :string]
    [:hierarchicalNamespace {:doc "Returns the Hierarchical Namespace (Folders) configuration." :optional true} :any]
    [:iamConfiguration {:doc "Beta feature. Returns the IAM configuration for the bucket." :optional true} :any]
    [:indexPage {:doc "Returns the bucket's website index page (for directory browsing)." :optional true} :string]
    [:labels {:doc "Returns the labels for this bucket." :optional true} [:map-of :string [:maybe :string]]]
    [:lifecycleRules {:doc "Returns the bucket's lifecycle rules (replaces the older deleteRules)." :optional true} [:sequential :any]]
    [:location {:doc "Returns the bucket's location (region). If multiple regions, customPlacementConfig is also set." :optional true} :string]
    [:locationType {:doc "Returns the bucket's locationType. See Also: Bucket LocationType." :optional true} :string]
    [:logging {:doc "Returns the Logging configuration for the bucket." :optional true} :any]
    [:metageneration {:doc "Returns the metadata generation of this bucket." :optional true} :int]
    [:notFoundPage {:doc "Returns the custom object to serve when a requested resource is not found." :optional true} :string]
    [:objectRetention {:doc "Returns the Object Retention configuration." :optional true} :any]
    [:owner {:doc "Returns the bucket's owner entity." :optional true} :any]
    [:retentionEffectiveTime {:doc "Deprecated. Use #getRetentionEffectiveTimeOffsetDateTime(). Retention effective time in millis if a policy is defined." :optional true} :int]
    [:retentionEffectiveTimeOffsetDateTime {:doc "Beta feature. Returns the retention effective time a policy took effect, as OffsetDateTime." :optional true} :any]
    [:retentionPeriod {:doc "Deprecated. Use #getRetentionPeriodDuration(). Retention policy period in millis." :optional true} :int]
    [:retentionPeriodDuration {:doc "Beta feature. Returns the retention policy period as a Duration." :optional true} :any]
    [:rpo {:doc "Returns the bucket's recovery point objective (RPO) for dual-region replication." :optional true} :any]
    [:selfLink {:doc "Returns the URI of this bucket as a string." :optional true} :string]
    [:softDeletePolicy {:doc "Returns the Soft Delete policy." :optional true} :any]
    [:storageClass {:doc "Returns the bucket's storage class (defines SLA and storage cost)." :optional true} :any]
    [:updateTime {:doc "Deprecated. Use #getUpdateTimeOffsetDateTime(). Returns the last metadata update time in millis." :optional true} :int]
    [:updateTimeOffsetDateTime {:doc "Returns the last modification time of the bucket's metadata as an OffsetDateTime." :optional true} :any]
    [:requesterPays {:doc "Returns whether the bucket is requester-pays. May be true, false, or null (unknown)." :optional true} :boolean]
    [:retentionPolicyIsLocked {:doc "Beta feature. Returns whether the bucket's retention policy is locked. May be true or null." :optional true} :boolean]
    [:versioningEnabled {:doc "Returns a Boolean that may be true, false, or null indicating if versioning is enabled." :optional true} :boolean]]

   :gcp.storage.v2/BucketInfo.AgeDeleteRule :any
   :gcp.storage.v2/BucketInfo.Autoclass :any
   :gcp.storage.v2/BucketInfo.CreatedBeforeDeleteRule :any
   :gcp.storage.v2/BucketInfo.CustomPlacementConfig :any
   :gcp.storage.v2/BucketInfo.DeleteRule :any
   :gcp.storage.v2/BucketInfo.HierarchicalNamespace :any
   :gcp.storage.v2/BucketInfo.IamConfiguration :any
   :gcp.storage.v2/BucketInfo.IsLiveDeleteRule :any
   :gcp.storage.v2/BucketInfo.LifecycleRule :any
   :gcp.storage.v2/BucketInfo.LifecycleRule.AbortIncompleteMPUAction :any
   :gcp.storage.v2/BucketInfo.LifecycleRule.DeleteLifecycleAction :any
   :gcp.storage.v2/BucketInfo.LifecycleRule.LifecycleAction :any
   :gcp.storage.v2/BucketInfo.LifecycleRule.LifecycleCondition :any
   :gcp.storage.v2/BucketInfo.LifecycleRule.SetStorageClassLifecycleAction :any
   :gcp.storage.v2/BucketInfo.Logging :any
   :gcp.storage.v2/BucketInfo.NumNewerVersionsDeleteRule :any
   :gcp.storage.v2/BucketInfo.ObjectRetention :any
   :gcp.storage.v2/BucketInfo.ObjectRetention.Mode :any
   :gcp.storage.v2/BucketInfo.SoftDeletePolicy :any})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
