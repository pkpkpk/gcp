(ns gcp.storage.v2.BucketInfo
  (:require [gcp.global :as g]
            [gcp.storage.v2.Acl :as Acl]
            [gcp.storage.v2.Cors :as Cors])
  (:import (com.google.cloud.storage BucketInfo BucketInfo$IamConfiguration StorageClass)))

(defn ^BucketInfo$IamConfiguration IamConfiguration-from-edn [arg]
  (let [builder (BucketInfo$IamConfiguration/newBuilder)]
    (throw (Exception. "unimplemented"))
    (.build builder)))

(defn IamConfiguration-to-edn [^BucketInfo$IamConfiguration arg]
  (throw (Exception. "unimplemented")))

(defn ^BucketInfo from-edn [arg]
  (g/strict! :storage/BucketInfo arg)
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
    (when (:etag arg)
      (.setEtag builder (:etag arg)))
    (when (:generatedId arg)
      (.setGeneratedId builder (:generatedId arg)))
    (when (:hierarchicalNamespace arg)
      (.setHierarchicalNamespace builder (:hierarchicalNamespace arg)))
    (when (:iamConfiguration arg)
      (.setIamConfiguration builder (IamConfiguration-from-edn (:iamConfiguration arg))))
    (when (:indexPage arg)
      (.setIndexPage builder (:indexPage arg)))
    (when (:labels arg)
      (.setLabels builder (:labels arg)))
    (when (:lifecycleRules arg)
      (.setLifecycleRules builder (vec (:lifecycleRules arg))))
    (when (:location arg)
      (.setLocation builder (:location arg)))
    (when (:locationType arg)
      (.setLocationType builder (:locationType arg)))
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
  {:post [(g/strict! :storage/BucketInfo %)]}
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
