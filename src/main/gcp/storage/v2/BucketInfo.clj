(ns gcp.storage.v2.BucketInfo
  (:require [gcp.global :as g])
  (:import (com.google.cloud.storage BucketInfo)))

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
