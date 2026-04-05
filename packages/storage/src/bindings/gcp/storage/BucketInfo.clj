;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.storage.BucketInfo
  {:doc
     "Google Storage bucket metadata;\n\n@see <a href=\"https://cloud.google.com/storage/docs/concepts-techniques#concepts\">Concepts and\n    Terminology</a>"
   :file-git-sha "41f9fbb36fae4f4bbdf9341064ad89fbb4be72d8"
   :fqcn "com.google.cloud.storage.BucketInfo"
   :gcp.dev/certification
     {:base-seed 1775175951864
      :manifest "215ec381-0f5f-5884-ab6d-eb0bb246cd16"
      :passed-stages
        {:smoke 1775175951864 :standard 1775175951865 :stress 1775175951866}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-03T00:25:54.939223192Z"}}
  (:require [gcp.global :as global]
            [gcp.storage.Acl :as Acl]
            [gcp.storage.custom.Cors :as Cors])
  (:import
    [com.google.cloud.storage BucketInfo BucketInfo$Autoclass
     BucketInfo$Autoclass$Builder BucketInfo$Builder
     BucketInfo$CustomPlacementConfig BucketInfo$CustomPlacementConfig$Builder
     BucketInfo$CustomerManagedEncryptionEnforcementConfig
     BucketInfo$CustomerSuppliedEncryptionEnforcementConfig
     BucketInfo$EncryptionEnforcementRestrictionMode
     BucketInfo$GoogleManagedEncryptionEnforcementConfig
     BucketInfo$HierarchicalNamespace BucketInfo$HierarchicalNamespace$Builder
     BucketInfo$IamConfiguration BucketInfo$IamConfiguration$Builder
     BucketInfo$IpFilter BucketInfo$IpFilter$Builder
     BucketInfo$IpFilter$PublicNetworkSource
     BucketInfo$IpFilter$VpcNetworkSource
     BucketInfo$IpFilter$VpcNetworkSource$Builder BucketInfo$LifecycleRule
     BucketInfo$LifecycleRule$AbortIncompleteMPUAction
     BucketInfo$LifecycleRule$DeleteLifecycleAction
     BucketInfo$LifecycleRule$LifecycleAction
     BucketInfo$LifecycleRule$LifecycleCondition
     BucketInfo$LifecycleRule$LifecycleCondition$Builder
     BucketInfo$LifecycleRule$SetStorageClassLifecycleAction BucketInfo$Logging
     BucketInfo$Logging$Builder BucketInfo$ObjectRetention
     BucketInfo$ObjectRetention$Builder BucketInfo$ObjectRetention$Mode
     BucketInfo$PublicAccessPrevention BucketInfo$RawDeleteRule
     BucketInfo$SoftDeletePolicy BucketInfo$SoftDeletePolicy$Builder Rpo
     StorageClass]))

(declare
  from-edn
  to-edn
  PublicAccessPrevention-from-edn
  PublicAccessPrevention-to-edn
  IamConfiguration-from-edn
  IamConfiguration-to-edn
  SoftDeletePolicy-from-edn
  SoftDeletePolicy-to-edn
  Autoclass-from-edn
  Autoclass-to-edn
  ObjectRetention$Mode-from-edn
  ObjectRetention$Mode-to-edn
  ObjectRetention-from-edn
  ObjectRetention-to-edn
  ObjectRetention$Mode-from-edn
  ObjectRetention$Mode-to-edn
  CustomPlacementConfig-from-edn
  CustomPlacementConfig-to-edn
  Logging-from-edn
  Logging-to-edn
  HierarchicalNamespace-from-edn
  HierarchicalNamespace-to-edn
  LifecycleRule$LifecycleCondition-from-edn
  LifecycleRule$LifecycleCondition-to-edn
  LifecycleRule$LifecycleAction-from-edn
  LifecycleRule$LifecycleAction-to-edn
  LifecycleRule$DeleteLifecycleAction-from-edn
  LifecycleRule$DeleteLifecycleAction-to-edn
  LifecycleRule$SetStorageClassLifecycleAction-from-edn
  LifecycleRule$SetStorageClassLifecycleAction-to-edn
  LifecycleRule$AbortIncompleteMPUAction-from-edn
  LifecycleRule$AbortIncompleteMPUAction-to-edn
  LifecycleRule-from-edn
  LifecycleRule-to-edn
  LifecycleRule$LifecycleCondition-from-edn
  LifecycleRule$LifecycleCondition-to-edn
  LifecycleRule$LifecycleAction-from-edn
  LifecycleRule$LifecycleAction-to-edn
  LifecycleRule$DeleteLifecycleAction-from-edn
  LifecycleRule$DeleteLifecycleAction-to-edn
  LifecycleRule$SetStorageClassLifecycleAction-from-edn
  LifecycleRule$SetStorageClassLifecycleAction-to-edn
  LifecycleRule$AbortIncompleteMPUAction-from-edn
  LifecycleRule$AbortIncompleteMPUAction-to-edn
  RawDeleteRule-from-edn
  RawDeleteRule-to-edn
  IpFilter$PublicNetworkSource-from-edn
  IpFilter$PublicNetworkSource-to-edn
  IpFilter$VpcNetworkSource-from-edn
  IpFilter$VpcNetworkSource-to-edn
  IpFilter-from-edn
  IpFilter-to-edn
  IpFilter$PublicNetworkSource-from-edn
  IpFilter$PublicNetworkSource-to-edn
  IpFilter$VpcNetworkSource-from-edn
  IpFilter$VpcNetworkSource-to-edn
  GoogleManagedEncryptionEnforcementConfig-from-edn
  GoogleManagedEncryptionEnforcementConfig-to-edn
  CustomerManagedEncryptionEnforcementConfig-from-edn
  CustomerManagedEncryptionEnforcementConfig-to-edn
  CustomerSuppliedEncryptionEnforcementConfig-from-edn
  CustomerSuppliedEncryptionEnforcementConfig-to-edn
  EncryptionEnforcementRestrictionMode-from-edn
  EncryptionEnforcementRestrictionMode-to-edn)

(def PublicAccessPrevention-schema
  [:enum
   {:closed true,
    :doc
      "Public Access Prevention enum with expected values.\n\n@see <a\n    href=\"https://cloud.google.com/storage/docs/public-access-prevention\">public-access-prevention</a>",
    :gcp/category :nested/enum,
    :gcp/key :gcp.storage/BucketInfo.PublicAccessPrevention} "ENFORCED"
   "UNSPECIFIED" "UNKNOWN" "INHERITED"])

(defn ^BucketInfo$IamConfiguration IamConfiguration-from-edn
  [arg]
  (let [builder (BucketInfo$IamConfiguration/newBuilder)]
    (when (some? (get arg :publicAccessPrevention))
      (.setPublicAccessPrevention builder
                                  (BucketInfo$PublicAccessPrevention/valueOf
                                    (get arg :publicAccessPrevention))))
    (when (some? (get arg :uniformBucketLevelAccessEnabled))
      (.setIsUniformBucketLevelAccessEnabled
        builder
        (get arg :isUniformBucketLevelAccessEnabled)))
    (.build builder)))

(defn IamConfiguration-to-edn
  [^BucketInfo$IamConfiguration arg]
  (when arg
    (cond-> {}
      (.getPublicAccessPrevention arg)
        (assoc :publicAccessPrevention (.name (.getPublicAccessPrevention arg)))
      (.isUniformBucketLevelAccessEnabled arg)
        (assoc :uniformBucketLevelAccessEnabled
          (.isUniformBucketLevelAccessEnabled arg))
      (.getUniformBucketLevelAccessLockedTimeOffsetDateTime arg)
        (assoc :uniformBucketLevelAccessLockedTime
          (.getUniformBucketLevelAccessLockedTimeOffsetDateTime arg)))))

(def IamConfiguration-schema
  [:map
   {:closed true,
    :doc
      "The Bucket's IAM Configuration.\n\n@see <a href=\"https://cloud.google.com/storage/docs/uniform-bucket-level-access\">uniform\n    bucket-level access</a>\n@see <a\n    href=\"https://cloud.google.com/storage/docs/public-access-prevention\">public-access-prevention</a>",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/BucketInfo.IamConfiguration}
   [:publicAccessPrevention
    {:optional true,
     :getter-doc "Returns the Public Access Prevention. *",
     :setter-doc
       "Sets the bucket's Public Access Prevention configuration. Currently supported options are\n{@link PublicAccessPrevention#INHERITED} or {@link PublicAccessPrevention#ENFORCED}\n\n@see <a\n    href=\"https://cloud.google.com/storage/docs/public-access-prevention\">public-access-prevention</a>"}
    [:enum {:closed true} "ENFORCED" "UNSPECIFIED" "UNKNOWN" "INHERITED"]]
   [:uniformBucketLevelAccessEnabled
    {:optional true,
     :setter-doc
       "Sets whether uniform bucket-level access is enabled for this bucket. When this is enabled,\naccess to the bucket will be configured through IAM, and legacy ACL policies will not work.\nWhen this is first enabled, {@code uniformBucketLevelAccess.lockedTime} will be set by the\nAPI automatically. This field can then be disabled until the time specified, after which it\nwill become immutable and calls to change it will fail. If this is enabled, calls to access\nlegacy ACL information will fail."}
    :boolean]
   [:uniformBucketLevelAccessLockedTime {:optional true, :read-only? true}
    :OffsetDateTime]])

(defn ^BucketInfo$SoftDeletePolicy SoftDeletePolicy-from-edn
  [arg]
  (let [builder (BucketInfo$SoftDeletePolicy/newBuilder)]
    (when (some? (get arg :retentionDuration))
      (.setRetentionDuration builder (get arg :retentionDuration)))
    (.build builder)))

(defn SoftDeletePolicy-to-edn
  [^BucketInfo$SoftDeletePolicy arg]
  (when arg
    (cond-> {}
      (.getEffectiveTime arg) (assoc :effectiveTime (.getEffectiveTime arg))
      (.getRetentionDuration arg) (assoc :retentionDuration
                                    (.getRetentionDuration arg)))))

(def SoftDeletePolicy-schema
  [:map
   {:closed true,
    :doc
      "The bucket's soft delete policy. If this policy is set, any deleted objects will be\nsoft-deleted according to the time specified in the policy",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/BucketInfo.SoftDeletePolicy}
   [:effectiveTime {:optional true, :read-only? true} :OffsetDateTime]
   [:retentionDuration
    {:optional true,
     :setter-doc
       "Sets the length of time to retain soft-deleted objects for, expressed as a Duration"}
    :Duration]])

(defn ^BucketInfo$Autoclass Autoclass-from-edn
  [arg]
  (let [builder (BucketInfo$Autoclass/newBuilder)]
    (when (some? (get arg :enabled)) (.setEnabled builder (get arg :enabled)))
    (when (some? (get arg :terminalStorageClass))
      (.setTerminalStorageClass builder
                                (StorageClass/valueOf
                                  (get arg :terminalStorageClass))))
    (.build builder)))

(defn Autoclass-to-edn
  [^BucketInfo$Autoclass arg]
  (when arg
    (cond-> {}
      (.getEnabled arg) (assoc :enabled (.getEnabled arg))
      (.getTerminalStorageClass arg) (assoc :terminalStorageClass
                                       (.name (.getTerminalStorageClass arg)))
      (.getTerminalStorageClassUpdateTime arg)
        (assoc :terminalStorageClassUpdateTime
          (.getTerminalStorageClassUpdateTime arg))
      (.getToggleTime arg) (assoc :toggleTime (.getToggleTime arg)))))

(def Autoclass-schema
  [:map
   {:closed true,
    :doc
      "Configuration for the Autoclass settings of a bucket.\n\n@see <a\n    href=\"https://cloud.google.com/storage/docs/autoclass\">https://cloud.google.com/storage/docs/autoclass</a>",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/BucketInfo.Autoclass}
   [:enabled
    {:optional true,
     :setter-doc
       "Sets whether Autoclass is enabled for this bucket. Currently, autoclass can only be enabled\nat bucket create time. Any calls to update an existing Autoclass configuration must be to\ndisable it, calls to enable Autoclass on an existing bucket will fail."}
    :boolean]
   [:terminalStorageClass
    {:optional true,
     :setter-doc
       "When set to {@link StorageClass#NEARLINE}, Autoclass restricts transitions between Standard\nand Nearline storage classes only.\n\n<p>When set to {@link StorageClass#ARCHIVE}, Autoclass allows transitions to Coldline and\nArchive as well.\n\n<p>Only valid values are {@code NEARLINE} and {@code ARCHIVE}."}
    [:enum {:closed true} "STANDARD" "NEARLINE" "COLDLINE" "ARCHIVE" "REGIONAL"
     "MULTI_REGIONAL" "DURABLE_REDUCED_AVAILABILITY"]]
   [:terminalStorageClassUpdateTime {:optional true, :read-only? true}
    :OffsetDateTime]
   [:toggleTime {:optional true, :read-only? true} :OffsetDateTime]])

(def ObjectRetention$Mode-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/string-enum,
    :gcp/key :gcp.storage/BucketInfo.ObjectRetention.Mode} "ENABLED"
   "DISABLED"])

(def ObjectRetention$Mode-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/string-enum,
    :gcp/key :gcp.storage/BucketInfo.ObjectRetention.Mode} "ENABLED"
   "DISABLED"])

(defn ^BucketInfo$ObjectRetention ObjectRetention-from-edn
  [arg]
  (let [builder (BucketInfo$ObjectRetention/newBuilder)]
    (when (some? (get arg :mode))
      (.setMode builder
                (BucketInfo$ObjectRetention$Mode/valueOf (get arg :mode))))
    (.build builder)))

(defn ObjectRetention-to-edn
  [^BucketInfo$ObjectRetention arg]
  (when arg (cond-> {} (.getMode arg) (assoc :mode (.name (.getMode arg))))))

(def ObjectRetention-schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/BucketInfo.ObjectRetention}
   [:mode
    {:optional true,
     :setter-doc "Sets the object retention mode. Can be Enabled or Disabled."}
    [:enum {:closed true} "Enabled" "Disabled"]]])

(defn ^BucketInfo$CustomPlacementConfig CustomPlacementConfig-from-edn
  [arg]
  (let [builder (BucketInfo$CustomPlacementConfig/newBuilder)]
    (when (seq (get arg :dataLocations))
      (.setDataLocations builder (seq (get arg :dataLocations))))
    (.build builder)))

(defn CustomPlacementConfig-to-edn
  [^BucketInfo$CustomPlacementConfig arg]
  (when arg
    (cond-> {}
      (seq (.getDataLocations arg)) (assoc :dataLocations
                                      (seq (.getDataLocations arg))))))

(def CustomPlacementConfig-schema
  [:map
   {:closed true,
    :doc
      "The bucket's custom placement configuration for Custom Dual Regions. If using `location` is\nalso required.",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/BucketInfo.CustomPlacementConfig}
   [:dataLocations
    {:optional true,
     :setter-doc "A list of regions for custom placement configurations."}
    [:sequential {:min 1} [:string {:min 1}]]]])

(defn ^BucketInfo$Logging Logging-from-edn
  [arg]
  (let [builder (BucketInfo$Logging/newBuilder)]
    (when (some? (get arg :logBucket))
      (.setLogBucket builder (get arg :logBucket)))
    (when (some? (get arg :logObjectPrefix))
      (.setLogObjectPrefix builder (get arg :logObjectPrefix)))
    (.build builder)))

(defn Logging-to-edn
  [^BucketInfo$Logging arg]
  (when arg
    (cond-> {}
      (some->> (.getLogBucket arg)
               (not= ""))
        (assoc :logBucket (.getLogBucket arg))
      (some->> (.getLogObjectPrefix arg)
               (not= ""))
        (assoc :logObjectPrefix (.getLogObjectPrefix arg)))))

(def Logging-schema
  [:map
   {:closed true,
    :doc
      "The bucket's logging configuration, which defines the destination bucket and optional name\nprefix for the current bucket's logs.",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/BucketInfo.Logging}
   [:logBucket
    {:optional true,
     :setter-doc
       "The destination bucket where the current bucket's logs should be placed."}
    [:string {:min 1}]]
   [:logObjectPrefix
    {:optional true, :setter-doc "A prefix for log object names."}
    [:string {:min 1}]]])

(defn ^BucketInfo$HierarchicalNamespace HierarchicalNamespace-from-edn
  [arg]
  (let [builder (BucketInfo$HierarchicalNamespace/newBuilder)]
    (when (some? (get arg :enabled)) (.setEnabled builder (get arg :enabled)))
    (.build builder)))

(defn HierarchicalNamespace-to-edn
  [^BucketInfo$HierarchicalNamespace arg]
  (when arg (cond-> {} (.getEnabled arg) (assoc :enabled (.getEnabled arg)))))

(def HierarchicalNamespace-schema
  [:map
   {:closed true,
    :doc
      "The bucket's hierarchical namespace (Folders) configuration. Enable this to use HNS.",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/BucketInfo.HierarchicalNamespace}
   [:enabled
    {:optional true,
     :setter-doc
       "Sets whether Hierarchical Namespace (Folders) is enabled for this bucket. This can only be\nenabled at bucket create time. If this is enabled, Uniform Bucket-Level Access must also be\nenabled."}
    :boolean]])

(defn
  ^BucketInfo$LifecycleRule$LifecycleCondition LifecycleRule$LifecycleCondition-from-edn
  [arg]
  (let [builder (BucketInfo$LifecycleRule$LifecycleCondition/newBuilder)]
    (when (some? (get arg :age)) (.setAge builder (int (get arg :age))))
    (when (some? (get arg :createdBefore))
      (.setCreatedBeforeOffsetDateTime builder
                                       (get arg :createdBeforeOffsetDateTime)))
    (when (some? (get arg :customTimeBefore))
      (.setCustomTimeBeforeOffsetDateTime
        builder
        (get arg :customTimeBeforeOffsetDateTime)))
    (when (some? (get arg :daysSinceCustomTime))
      (.setDaysSinceCustomTime builder (int (get arg :daysSinceCustomTime))))
    (when (some? (get arg :daysSinceNoncurrentTime))
      (.setDaysSinceNoncurrentTime builder
                                   (int (get arg :daysSinceNoncurrentTime))))
    (when (some? (get arg :live)) (.setIsLive builder (get arg :isLive)))
    (when (seq (get arg :matchesPrefix))
      (.setMatchesPrefix builder (seq (get arg :matchesPrefix))))
    (when (seq (get arg :matchesStorageClass))
      (.setMatchesStorageClass builder
                               (map StorageClass/valueOf
                                 (get arg :matchesStorageClass))))
    (when (seq (get arg :matchesSuffix))
      (.setMatchesSuffix builder (seq (get arg :matchesSuffix))))
    (when (some? (get arg :noncurrentTimeBefore))
      (.setNoncurrentTimeBeforeOffsetDateTime
        builder
        (get arg :noncurrentTimeBeforeOffsetDateTime)))
    (when (some? (get arg :numberOfNewerVersions))
      (.setNumberOfNewerVersions builder
                                 (int (get arg :numberOfNewerVersions))))
    (.build builder)))

(defn
  LifecycleRule$LifecycleCondition-to-edn
  [^BucketInfo$LifecycleRule$LifecycleCondition arg]
  (when arg
    (cond-> {}
      (.getAge arg) (assoc :age (.getAge arg))
      (.getCreatedBeforeOffsetDateTime arg)
        (assoc :createdBefore (.getCreatedBeforeOffsetDateTime arg))
      (.getCustomTimeBeforeOffsetDateTime arg)
        (assoc :customTimeBefore (.getCustomTimeBeforeOffsetDateTime arg))
      (.getDaysSinceCustomTime arg) (assoc :daysSinceCustomTime
                                      (.getDaysSinceCustomTime arg))
      (.getDaysSinceNoncurrentTime arg) (assoc :daysSinceNoncurrentTime
                                          (.getDaysSinceNoncurrentTime arg))
      (.getIsLive arg) (assoc :live (.getIsLive arg))
      (seq (.getMatchesPrefix arg)) (assoc :matchesPrefix
                                      (seq (.getMatchesPrefix arg)))
      (seq (.getMatchesStorageClass arg)) (assoc :matchesStorageClass
                                            (map (fn [e] (.name e))
                                              (.getMatchesStorageClass arg)))
      (seq (.getMatchesSuffix arg)) (assoc :matchesSuffix
                                      (seq (.getMatchesSuffix arg)))
      (.getNoncurrentTimeBeforeOffsetDateTime arg)
        (assoc :noncurrentTimeBefore
          (.getNoncurrentTimeBeforeOffsetDateTime arg))
      (.getNumberOfNewerVersions arg) (assoc :numberOfNewerVersions
                                        (.getNumberOfNewerVersions arg)))))

(def LifecycleRule$LifecycleCondition-schema
  [:map
   {:closed true,
    :doc
      "Condition for a Lifecycle rule, specifies under what criteria an Action should be executed.\n\n@see <a href=\"https://cloud.google.com/storage/docs/lifecycle#conditions\">Object Lifecycle\n    Management</a>",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/BucketInfo.LifecycleRule.LifecycleCondition}
   [:age
    {:optional true,
     :setter-doc
       "Sets the age in days. This condition is satisfied when a Blob reaches the specified age\n(in days). When you specify the Age condition, you are specifying a Time to Live (TTL)\nfor objects in a bucket with lifecycle management configured. The time when the Age\ncondition is considered to be satisfied is calculated by adding the specified value to\nthe object creation time."}
    :i32]
   [:createdBefore
    {:optional true,
     :getter-doc
       "Returns the date and offset from UTC for this condition. If a time other than 00:00:00.000\nis present in the value, GCS will truncate to 00:00:00.000.",
     :setter-doc
       "Sets the date a Blob should be created before for an Action to be executed. Note that\nonly the date will be considered, if the time is specified it will be truncated. This\ncondition is satisfied when an object is created before midnight of the specified date in\nUTC."}
    :OffsetDateTime]
   [:customTimeBefore
    {:optional true,
     :getter-doc
       "Returns the date and offset from UTC for this condition. If a time other than 00:00:00.000\nis present in the value, GCS will truncate to 00:00:00.000.",
     :setter-doc
       "Sets the date with only the date part (for instance, \"2013-01-15\"). Note that only date\npart will be considered, if the time is specified it will be truncated. This condition is\nsatisfied when the custom time on an object is before this date in UTC."}
    :OffsetDateTime]
   [:daysSinceCustomTime
    {:optional true,
     :getter-doc
       "Returns the number of days elapsed since the user-specified timestamp set on an object.",
     :setter-doc
       "Sets the number of days elapsed since the user-specified timestamp set on an object. The\ncondition is satisfied if the days elapsed is at least this number. If no custom\ntimestamp is specified on an object, the condition does not apply."}
    :i32]
   [:daysSinceNoncurrentTime
    {:optional true,
     :getter-doc
       "Returns the number of days elapsed since the noncurrent timestamp of an object.",
     :setter-doc
       "Sets the number of days elapsed since the noncurrent timestamp of an object. The\ncondition is satisfied if the days elapsed is at least this number. This condition is\nrelevant only for versioned objects. The value of the field must be a nonnegative\ninteger. If it's zero, the object version will become eligible for Lifecycle action as\nsoon as it becomes noncurrent."}
    :i32]
   [:live
    {:optional true,
     :setter-doc
       "Sets an isLive Boolean condition. If the value is true, this lifecycle condition matches\nonly live Blobs; if the value is false, it matches only archived objects. For the\npurposes of this condition, Blobs in non-versioned buckets are considered live."}
    :boolean]
   [:matchesPrefix
    {:optional true,
     :setter-doc
       "Sets the list of prefixes. If any prefix matches the beginning of the object’s name, this\nportion of the condition is satisfied for that object."}
    [:sequential {:min 1} [:string {:min 1}]]]
   [:matchesStorageClass
    {:optional true,
     :setter-doc
       "Sets a list of Storage Classes for a objects that satisfy the condition to execute the\nAction. *"}
    [:sequential {:min 1}
     [:enum {:closed true} "STANDARD" "NEARLINE" "COLDLINE" "ARCHIVE" "REGIONAL"
      "MULTI_REGIONAL" "DURABLE_REDUCED_AVAILABILITY"]]]
   [:matchesSuffix
    {:optional true,
     :setter-doc
       "Sets the list of suffixes. If any suffix matches the end of the object’s name, this\nportion of the condition is satisfied for that object."}
    [:sequential {:min 1} [:string {:min 1}]]]
   [:noncurrentTimeBefore
    {:optional true,
     :getter-doc
       "Returns the date and offset from UTC for this condition. If a time other than 00:00:00.000\nis present in the value, GCS will truncate to 00:00:00.000.",
     :setter-doc
       "Sets the date with only the date part (for instance, \"2013-01-15\"). Note that only date\npart will be considered, if the time is specified it will be truncated. This condition is\nsatisfied when the noncurrent time on an object is before this date. This condition is\nrelevant only for versioned objects."}
    :OffsetDateTime]
   [:numberOfNewerVersions
    {:optional true,
     :setter-doc
       "Sets the number of newer versions a Blob should have for an Action to be executed.\nRelevant only when versioning is enabled on a bucket. *"}
    :i32]])

(defn
  ^BucketInfo$LifecycleRule$LifecycleAction LifecycleRule$LifecycleAction-from-edn
  [arg]
  (cond
    (contains? arg :actionType) (new BucketInfo$LifecycleRule$LifecycleAction
                                     (get arg :actionType))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.storage.BucketInfo.LifecycleRule.LifecycleAction"
          {:arg arg}))))

(defn
  LifecycleRule$LifecycleAction-to-edn
  [^BucketInfo$LifecycleRule$LifecycleAction arg]
  (when arg {:actionType (.getActionType arg)}))

(def LifecycleRule$LifecycleAction-schema
  [:map
   {:closed true,
    :doc
      "Base class for the Action to take when a Lifecycle Condition is met. Supported Actions are\nexpressed as subclasses of this class, accessed by static factory methods.",
    :gcp/category :nested/pojo,
    :gcp/key :gcp.storage/BucketInfo.LifecycleRule.LifecycleAction}
   [:actionType {} [:string {:min 1}]]])

(defn
  ^BucketInfo$LifecycleRule$DeleteLifecycleAction LifecycleRule$DeleteLifecycleAction-from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.storage.BucketInfo.LifecycleRule.DeleteLifecycleAction is read-only")))

(defn
  LifecycleRule$DeleteLifecycleAction-to-edn
  [^BucketInfo$LifecycleRule$DeleteLifecycleAction arg]
  (when arg {:type "TYPE"}))

(def LifecycleRule$DeleteLifecycleAction-schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :nested/variant-read-only,
    :gcp/key :gcp.storage/BucketInfo.LifecycleRule.DeleteLifecycleAction}
   [:type [:= "TYPE"]]])

(defn
  ^BucketInfo$LifecycleRule$SetStorageClassLifecycleAction LifecycleRule$SetStorageClassLifecycleAction-from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.storage.BucketInfo.LifecycleRule.SetStorageClassLifecycleAction is read-only")))

(defn
  LifecycleRule$SetStorageClassLifecycleAction-to-edn
  [^BucketInfo$LifecycleRule$SetStorageClassLifecycleAction arg]
  (when arg {:type "TYPE"}))

(def LifecycleRule$SetStorageClassLifecycleAction-schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :nested/variant-read-only,
    :gcp/key
      :gcp.storage/BucketInfo.LifecycleRule.SetStorageClassLifecycleAction}
   [:type [:= "TYPE"]]])

(defn
  ^BucketInfo$LifecycleRule$AbortIncompleteMPUAction LifecycleRule$AbortIncompleteMPUAction-from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.storage.BucketInfo.LifecycleRule.AbortIncompleteMPUAction is read-only")))

(defn
  LifecycleRule$AbortIncompleteMPUAction-to-edn
  [^BucketInfo$LifecycleRule$AbortIncompleteMPUAction arg]
  (when arg {:type "TYPE"}))

(def LifecycleRule$AbortIncompleteMPUAction-schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :nested/variant-read-only,
    :gcp/key :gcp.storage/BucketInfo.LifecycleRule.AbortIncompleteMPUAction}
   [:type [:= "TYPE"]]])

(defn
  ^BucketInfo$LifecycleRule$LifecycleCondition LifecycleRule$LifecycleCondition-from-edn
  [arg]
  (let [builder (BucketInfo$LifecycleRule$LifecycleCondition/newBuilder)]
    (when (some? (get arg :age)) (.setAge builder (int (get arg :age))))
    (when (some? (get arg :createdBefore))
      (.setCreatedBeforeOffsetDateTime builder
                                       (get arg :createdBeforeOffsetDateTime)))
    (when (some? (get arg :customTimeBefore))
      (.setCustomTimeBeforeOffsetDateTime
        builder
        (get arg :customTimeBeforeOffsetDateTime)))
    (when (some? (get arg :daysSinceCustomTime))
      (.setDaysSinceCustomTime builder (int (get arg :daysSinceCustomTime))))
    (when (some? (get arg :daysSinceNoncurrentTime))
      (.setDaysSinceNoncurrentTime builder
                                   (int (get arg :daysSinceNoncurrentTime))))
    (when (some? (get arg :live)) (.setIsLive builder (get arg :isLive)))
    (when (seq (get arg :matchesPrefix))
      (.setMatchesPrefix builder (seq (get arg :matchesPrefix))))
    (when (seq (get arg :matchesStorageClass))
      (.setMatchesStorageClass builder
                               (map StorageClass/valueOf
                                 (get arg :matchesStorageClass))))
    (when (seq (get arg :matchesSuffix))
      (.setMatchesSuffix builder (seq (get arg :matchesSuffix))))
    (when (some? (get arg :noncurrentTimeBefore))
      (.setNoncurrentTimeBeforeOffsetDateTime
        builder
        (get arg :noncurrentTimeBeforeOffsetDateTime)))
    (when (some? (get arg :numberOfNewerVersions))
      (.setNumberOfNewerVersions builder
                                 (int (get arg :numberOfNewerVersions))))
    (.build builder)))

(defn
  LifecycleRule$LifecycleCondition-to-edn
  [^BucketInfo$LifecycleRule$LifecycleCondition arg]
  (when arg
    (cond-> {}
      (.getAge arg) (assoc :age (.getAge arg))
      (.getCreatedBeforeOffsetDateTime arg)
        (assoc :createdBefore (.getCreatedBeforeOffsetDateTime arg))
      (.getCustomTimeBeforeOffsetDateTime arg)
        (assoc :customTimeBefore (.getCustomTimeBeforeOffsetDateTime arg))
      (.getDaysSinceCustomTime arg) (assoc :daysSinceCustomTime
                                      (.getDaysSinceCustomTime arg))
      (.getDaysSinceNoncurrentTime arg) (assoc :daysSinceNoncurrentTime
                                          (.getDaysSinceNoncurrentTime arg))
      (.getIsLive arg) (assoc :live (.getIsLive arg))
      (seq (.getMatchesPrefix arg)) (assoc :matchesPrefix
                                      (seq (.getMatchesPrefix arg)))
      (seq (.getMatchesStorageClass arg)) (assoc :matchesStorageClass
                                            (map (fn [e] (.name e))
                                              (.getMatchesStorageClass arg)))
      (seq (.getMatchesSuffix arg)) (assoc :matchesSuffix
                                      (seq (.getMatchesSuffix arg)))
      (.getNoncurrentTimeBeforeOffsetDateTime arg)
        (assoc :noncurrentTimeBefore
          (.getNoncurrentTimeBeforeOffsetDateTime arg))
      (.getNumberOfNewerVersions arg) (assoc :numberOfNewerVersions
                                        (.getNumberOfNewerVersions arg)))))

(def LifecycleRule$LifecycleCondition-schema
  [:map
   {:closed true,
    :doc
      "Condition for a Lifecycle rule, specifies under what criteria an Action should be executed.\n\n@see <a href=\"https://cloud.google.com/storage/docs/lifecycle#conditions\">Object Lifecycle\n    Management</a>",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/BucketInfo.LifecycleRule.LifecycleCondition}
   [:age
    {:optional true,
     :setter-doc
       "Sets the age in days. This condition is satisfied when a Blob reaches the specified age\n(in days). When you specify the Age condition, you are specifying a Time to Live (TTL)\nfor objects in a bucket with lifecycle management configured. The time when the Age\ncondition is considered to be satisfied is calculated by adding the specified value to\nthe object creation time."}
    :i32]
   [:createdBefore
    {:optional true,
     :getter-doc
       "Returns the date and offset from UTC for this condition. If a time other than 00:00:00.000\nis present in the value, GCS will truncate to 00:00:00.000.",
     :setter-doc
       "Sets the date a Blob should be created before for an Action to be executed. Note that\nonly the date will be considered, if the time is specified it will be truncated. This\ncondition is satisfied when an object is created before midnight of the specified date in\nUTC."}
    :OffsetDateTime]
   [:customTimeBefore
    {:optional true,
     :getter-doc
       "Returns the date and offset from UTC for this condition. If a time other than 00:00:00.000\nis present in the value, GCS will truncate to 00:00:00.000.",
     :setter-doc
       "Sets the date with only the date part (for instance, \"2013-01-15\"). Note that only date\npart will be considered, if the time is specified it will be truncated. This condition is\nsatisfied when the custom time on an object is before this date in UTC."}
    :OffsetDateTime]
   [:daysSinceCustomTime
    {:optional true,
     :getter-doc
       "Returns the number of days elapsed since the user-specified timestamp set on an object.",
     :setter-doc
       "Sets the number of days elapsed since the user-specified timestamp set on an object. The\ncondition is satisfied if the days elapsed is at least this number. If no custom\ntimestamp is specified on an object, the condition does not apply."}
    :i32]
   [:daysSinceNoncurrentTime
    {:optional true,
     :getter-doc
       "Returns the number of days elapsed since the noncurrent timestamp of an object.",
     :setter-doc
       "Sets the number of days elapsed since the noncurrent timestamp of an object. The\ncondition is satisfied if the days elapsed is at least this number. This condition is\nrelevant only for versioned objects. The value of the field must be a nonnegative\ninteger. If it's zero, the object version will become eligible for Lifecycle action as\nsoon as it becomes noncurrent."}
    :i32]
   [:live
    {:optional true,
     :setter-doc
       "Sets an isLive Boolean condition. If the value is true, this lifecycle condition matches\nonly live Blobs; if the value is false, it matches only archived objects. For the\npurposes of this condition, Blobs in non-versioned buckets are considered live."}
    :boolean]
   [:matchesPrefix
    {:optional true,
     :setter-doc
       "Sets the list of prefixes. If any prefix matches the beginning of the object’s name, this\nportion of the condition is satisfied for that object."}
    [:sequential {:min 1} [:string {:min 1}]]]
   [:matchesStorageClass
    {:optional true,
     :setter-doc
       "Sets a list of Storage Classes for a objects that satisfy the condition to execute the\nAction. *"}
    [:sequential {:min 1}
     [:enum {:closed true} "STANDARD" "NEARLINE" "COLDLINE" "ARCHIVE" "REGIONAL"
      "MULTI_REGIONAL" "DURABLE_REDUCED_AVAILABILITY"]]]
   [:matchesSuffix
    {:optional true,
     :setter-doc
       "Sets the list of suffixes. If any suffix matches the end of the object’s name, this\nportion of the condition is satisfied for that object."}
    [:sequential {:min 1} [:string {:min 1}]]]
   [:noncurrentTimeBefore
    {:optional true,
     :getter-doc
       "Returns the date and offset from UTC for this condition. If a time other than 00:00:00.000\nis present in the value, GCS will truncate to 00:00:00.000.",
     :setter-doc
       "Sets the date with only the date part (for instance, \"2013-01-15\"). Note that only date\npart will be considered, if the time is specified it will be truncated. This condition is\nsatisfied when the noncurrent time on an object is before this date. This condition is\nrelevant only for versioned objects."}
    :OffsetDateTime]
   [:numberOfNewerVersions
    {:optional true,
     :setter-doc
       "Sets the number of newer versions a Blob should have for an Action to be executed.\nRelevant only when versioning is enabled on a bucket. *"}
    :i32]])

(defn
  ^BucketInfo$LifecycleRule$LifecycleAction LifecycleRule$LifecycleAction-from-edn
  [arg]
  (cond
    (contains? arg :actionType) (new BucketInfo$LifecycleRule$LifecycleAction
                                     (get arg :actionType))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.storage.BucketInfo.LifecycleRule.LifecycleAction"
          {:arg arg}))))

(defn
  LifecycleRule$LifecycleAction-to-edn
  [^BucketInfo$LifecycleRule$LifecycleAction arg]
  (when arg {:actionType (.getActionType arg)}))

(def LifecycleRule$LifecycleAction-schema
  [:map
   {:closed true,
    :doc
      "Base class for the Action to take when a Lifecycle Condition is met. Supported Actions are\nexpressed as subclasses of this class, accessed by static factory methods.",
    :gcp/category :nested/pojo,
    :gcp/key :gcp.storage/BucketInfo.LifecycleRule.LifecycleAction}
   [:actionType {} [:string {:min 1}]]])

(defn
  ^BucketInfo$LifecycleRule$DeleteLifecycleAction LifecycleRule$DeleteLifecycleAction-from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.storage.BucketInfo.LifecycleRule.DeleteLifecycleAction is read-only")))

(defn
  LifecycleRule$DeleteLifecycleAction-to-edn
  [^BucketInfo$LifecycleRule$DeleteLifecycleAction arg]
  (when arg {:type "TYPE"}))

(def LifecycleRule$DeleteLifecycleAction-schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :nested/variant-read-only,
    :gcp/key :gcp.storage/BucketInfo.LifecycleRule.DeleteLifecycleAction}
   [:type [:= "TYPE"]]])

(defn
  ^BucketInfo$LifecycleRule$SetStorageClassLifecycleAction LifecycleRule$SetStorageClassLifecycleAction-from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.storage.BucketInfo.LifecycleRule.SetStorageClassLifecycleAction is read-only")))

(defn
  LifecycleRule$SetStorageClassLifecycleAction-to-edn
  [^BucketInfo$LifecycleRule$SetStorageClassLifecycleAction arg]
  (when arg {:type "TYPE"}))

(def LifecycleRule$SetStorageClassLifecycleAction-schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :nested/variant-read-only,
    :gcp/key
      :gcp.storage/BucketInfo.LifecycleRule.SetStorageClassLifecycleAction}
   [:type [:= "TYPE"]]])

(defn
  ^BucketInfo$LifecycleRule$AbortIncompleteMPUAction LifecycleRule$AbortIncompleteMPUAction-from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.storage.BucketInfo.LifecycleRule.AbortIncompleteMPUAction is read-only")))

(defn
  LifecycleRule$AbortIncompleteMPUAction-to-edn
  [^BucketInfo$LifecycleRule$AbortIncompleteMPUAction arg]
  (when arg {:type "TYPE"}))

(def LifecycleRule$AbortIncompleteMPUAction-schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :nested/variant-read-only,
    :gcp/key :gcp.storage/BucketInfo.LifecycleRule.AbortIncompleteMPUAction}
   [:type [:= "TYPE"]]])

(defn ^BucketInfo$LifecycleRule LifecycleRule-from-edn
  [arg]
  (cond
    (and (contains? arg :condition) (contains? arg :action))
      (new BucketInfo$LifecycleRule
           (LifecycleRule$LifecycleAction-from-edn (get arg :action))
           (LifecycleRule$LifecycleCondition-from-edn (get arg :condition)))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.storage.BucketInfo.LifecycleRule"
          {:arg arg}))))

(defn LifecycleRule-to-edn
  [^BucketInfo$LifecycleRule arg]
  (when arg
    (cond-> {:condition (LifecycleRule$LifecycleCondition-to-edn (.getCondition
                                                                   arg)),
             :action (LifecycleRule$LifecycleAction-to-edn (.getAction arg))}
      (global/get-private-field arg "lifecycleAction")
        (assoc :lifecycleaction
          (LifecycleRule$LifecycleAction-to-edn
            (global/get-private-field arg "lifecycleAction")))
      (global/get-private-field arg "lifecycleCondition")
        (assoc :lifecyclecondition
          (LifecycleRule$LifecycleCondition-to-edn
            (global/get-private-field arg "lifecycleCondition"))))))

(def LifecycleRule-schema
  [:map
   {:closed true,
    :doc
      "Lifecycle rule for a bucket. Allows supported Actions, such as deleting and changing storage\nclass, to be executed when certain Conditions are met.\n\n<p>Versions 1.50.0-1.111.2 of this library don’t support the CustomTimeBefore,\nDaysSinceCustomTime, DaysSinceNoncurrentTime and NoncurrentTimeBefore lifecycle conditions. To\nread GCS objects with those lifecycle conditions, update your Java client library to the latest\nversion.\n\n@see <a href=\"https://cloud.google.com/storage/docs/lifecycle#actions\">Object Lifecycle\n    Management</a>",
    :gcp/category :nested/pojo,
    :gcp/key :gcp.storage/BucketInfo.LifecycleRule}
   [:action {} [:ref :gcp.storage/BucketInfo.LifecycleRule.LifecycleAction]]
   [:condition {}
    [:ref :gcp.storage/BucketInfo.LifecycleRule.LifecycleCondition]]
   [:lifecycleaction
    {:doc "Synthetic getter for lifecycleAction", :optional true}
    [:ref :gcp.storage/BucketInfo.LifecycleRule.LifecycleAction]]
   [:lifecyclecondition
    {:doc "Synthetic getter for lifecycleCondition", :optional true}
    [:ref :gcp.storage/BucketInfo.LifecycleRule.LifecycleCondition]]])

(defn ^BucketInfo$RawDeleteRule RawDeleteRule-from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.storage.BucketInfo.RawDeleteRule is read-only")))

(defn RawDeleteRule-to-edn
  [^BucketInfo$RawDeleteRule arg]
  (when arg {:type "UNKNOWN"}))

(def RawDeleteRule-schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :nested/variant-read-only,
    :gcp/key :gcp.storage/BucketInfo.RawDeleteRule} [:type [:= "UNKNOWN"]]])

(defn
  ^BucketInfo$IpFilter$PublicNetworkSource IpFilter$PublicNetworkSource-from-edn
  [arg]
  (if (gcp.global/valid? [:sequential string?] arg)
    (BucketInfo$IpFilter$PublicNetworkSource/of (seq arg))
    (BucketInfo$IpFilter$PublicNetworkSource/of
      (seq (get arg :allowedIpCidrRanges)))))

(defn
  IpFilter$PublicNetworkSource-to-edn
  [^BucketInfo$IpFilter$PublicNetworkSource arg]
  (when arg {:allowedIpCidrRanges (seq (.getAllowedIpCidrRanges arg))}))

(def IpFilter$PublicNetworkSource-schema
  [:or
   {:closed true,
    :doc
      "The public network IP address ranges that can access the bucket and its data.\n\n@since 2.54.0",
    :gcp/category :nested/static-factory,
    :gcp/key :gcp.storage/BucketInfo.IpFilter.PublicNetworkSource}
   [:sequential {:min 1} [:string {:min 1}]]
   [:map {:closed true}
    [:allowedIpCidrRanges [:sequential {:min 1} [:string {:min 1}]]]]])

(defn
  ^BucketInfo$IpFilter$VpcNetworkSource IpFilter$VpcNetworkSource-from-edn
  [arg]
  (let [builder (BucketInfo$IpFilter$VpcNetworkSource/newBuilder)]
    (when (seq (get arg :allowedIpCidrRanges))
      (.setAllowedIpCidrRanges builder (seq (get arg :allowedIpCidrRanges))))
    (when (some? (get arg :network)) (.setNetwork builder (get arg :network)))
    (.build builder)))

(defn
  IpFilter$VpcNetworkSource-to-edn
  [^BucketInfo$IpFilter$VpcNetworkSource arg]
  (when arg
    (cond-> {}
      (seq (.getAllowedIpCidrRanges arg)) (assoc :allowedIpCidrRanges
                                            (seq (.getAllowedIpCidrRanges arg)))
      (some->> (.getNetwork arg)
               (not= ""))
        (assoc :network (.getNetwork arg)))))

(def IpFilter$VpcNetworkSource-schema
  [:map
   {:closed true,
    :doc
      "The list of VPC networks that can access the bucket.\n\n@since 2.54.0",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/BucketInfo.IpFilter.VpcNetworkSource}
   [:allowedIpCidrRanges
    {:optional true,
     :getter-doc
       "Optional. The list of public or private IPv4 and IPv6 CIDR ranges that can access the\nbucket. In the CIDR IP address block, the specified IP address must be properly truncated,\nmeaning all the host bits must be zero or else the input is considered malformed. For\nexample, `192.0.2.0/24` is accepted but `192.0.2.1/24` is not. Similarly, for IPv6,\n`2001:db8::/32` is accepted whereas `2001:db8::1/32` is not.\n\n@since 2.54.0\n@see Builder#setAllowedIpCidrRanges(List)",
     :setter-doc
       "Optional. The list of public or private IPv4 and IPv6 CIDR ranges that can access the\nbucket. In the CIDR IP address block, the specified IP address must be properly\ntruncated, meaning all the host bits must be zero or else the input is considered\nmalformed. For example, `192.0.2.0/24` is accepted but `192.0.2.1/24` is not. Similarly,\nfor IPv6, `2001:db8::/32` is accepted whereas `2001:db8::1/32` is not.\n\n@since 2.54.0\n@see VpcNetworkSource#getAllowedIpCidrRanges()"}
    [:sequential {:min 1} [:string {:min 1}]]]
   [:network
    {:optional true,
     :getter-doc
       "Name of the network.\n\n<p>Format: `projects/PROJECT_ID/global/networks/NETWORK_NAME`\n\n@since 2.54.0\n@see Builder#setNetwork(String)",
     :setter-doc
       "Name of the network.\n\n<p>Format: `projects/PROJECT_ID/global/networks/NETWORK_NAME`\n\n@since 2.54.0\n@see VpcNetworkSource#getNetwork()"}
    [:string {:min 1}]]])

(defn
  ^BucketInfo$IpFilter$PublicNetworkSource IpFilter$PublicNetworkSource-from-edn
  [arg]
  (if (gcp.global/valid? [:sequential string?] arg)
    (BucketInfo$IpFilter$PublicNetworkSource/of (seq arg))
    (BucketInfo$IpFilter$PublicNetworkSource/of
      (seq (get arg :allowedIpCidrRanges)))))

(defn
  IpFilter$PublicNetworkSource-to-edn
  [^BucketInfo$IpFilter$PublicNetworkSource arg]
  (when arg {:allowedIpCidrRanges (seq (.getAllowedIpCidrRanges arg))}))

(def IpFilter$PublicNetworkSource-schema
  [:or
   {:closed true,
    :doc
      "The public network IP address ranges that can access the bucket and its data.\n\n@since 2.54.0",
    :gcp/category :nested/static-factory,
    :gcp/key :gcp.storage/BucketInfo.IpFilter.PublicNetworkSource}
   [:sequential {:min 1} [:string {:min 1}]]
   [:map {:closed true}
    [:allowedIpCidrRanges [:sequential {:min 1} [:string {:min 1}]]]]])

(defn
  ^BucketInfo$IpFilter$VpcNetworkSource IpFilter$VpcNetworkSource-from-edn
  [arg]
  (let [builder (BucketInfo$IpFilter$VpcNetworkSource/newBuilder)]
    (when (seq (get arg :allowedIpCidrRanges))
      (.setAllowedIpCidrRanges builder (seq (get arg :allowedIpCidrRanges))))
    (when (some? (get arg :network)) (.setNetwork builder (get arg :network)))
    (.build builder)))

(defn
  IpFilter$VpcNetworkSource-to-edn
  [^BucketInfo$IpFilter$VpcNetworkSource arg]
  (when arg
    (cond-> {}
      (seq (.getAllowedIpCidrRanges arg)) (assoc :allowedIpCidrRanges
                                            (seq (.getAllowedIpCidrRanges arg)))
      (some->> (.getNetwork arg)
               (not= ""))
        (assoc :network (.getNetwork arg)))))

(def IpFilter$VpcNetworkSource-schema
  [:map
   {:closed true,
    :doc
      "The list of VPC networks that can access the bucket.\n\n@since 2.54.0",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/BucketInfo.IpFilter.VpcNetworkSource}
   [:allowedIpCidrRanges
    {:optional true,
     :getter-doc
       "Optional. The list of public or private IPv4 and IPv6 CIDR ranges that can access the\nbucket. In the CIDR IP address block, the specified IP address must be properly truncated,\nmeaning all the host bits must be zero or else the input is considered malformed. For\nexample, `192.0.2.0/24` is accepted but `192.0.2.1/24` is not. Similarly, for IPv6,\n`2001:db8::/32` is accepted whereas `2001:db8::1/32` is not.\n\n@since 2.54.0\n@see Builder#setAllowedIpCidrRanges(List)",
     :setter-doc
       "Optional. The list of public or private IPv4 and IPv6 CIDR ranges that can access the\nbucket. In the CIDR IP address block, the specified IP address must be properly\ntruncated, meaning all the host bits must be zero or else the input is considered\nmalformed. For example, `192.0.2.0/24` is accepted but `192.0.2.1/24` is not. Similarly,\nfor IPv6, `2001:db8::/32` is accepted whereas `2001:db8::1/32` is not.\n\n@since 2.54.0\n@see VpcNetworkSource#getAllowedIpCidrRanges()"}
    [:sequential {:min 1} [:string {:min 1}]]]
   [:network
    {:optional true,
     :getter-doc
       "Name of the network.\n\n<p>Format: `projects/PROJECT_ID/global/networks/NETWORK_NAME`\n\n@since 2.54.0\n@see Builder#setNetwork(String)",
     :setter-doc
       "Name of the network.\n\n<p>Format: `projects/PROJECT_ID/global/networks/NETWORK_NAME`\n\n@since 2.54.0\n@see VpcNetworkSource#getNetwork()"}
    [:string {:min 1}]]])

(defn ^BucketInfo$IpFilter IpFilter-from-edn
  [arg]
  (let [builder (BucketInfo$IpFilter/newBuilder)]
    (when (some? (get arg :allowAllServiceAgentAccess))
      (.setAllowAllServiceAgentAccess builder
                                      (get arg :allowAllServiceAgentAccess)))
    (when (some? (get arg :allowCrossOrgVpcs))
      (.setAllowCrossOrgVpcs builder (get arg :allowCrossOrgVpcs)))
    (when (some? (get arg :mode)) (.setMode builder (get arg :mode)))
    (when (some? (get arg :publicNetworkSource))
      (.setPublicNetworkSource builder
                               (IpFilter$PublicNetworkSource-from-edn
                                 (get arg :publicNetworkSource))))
    (when (seq (get arg :vpcNetworkSources))
      (.setVpcNetworkSources builder
                             (map IpFilter$VpcNetworkSource-from-edn
                               (get arg :vpcNetworkSources))))
    (.build builder)))

(defn IpFilter-to-edn
  [^BucketInfo$IpFilter arg]
  (when arg
    (cond-> {}
      (.getAllowAllServiceAgentAccess arg)
        (assoc :allowAllServiceAgentAccess (.getAllowAllServiceAgentAccess arg))
      (.getAllowCrossOrgVpcs arg) (assoc :allowCrossOrgVpcs
                                    (.getAllowCrossOrgVpcs arg))
      (some->> (.getMode arg)
               (not= ""))
        (assoc :mode (.getMode arg))
      (.getPublicNetworkSource arg) (assoc :publicNetworkSource
                                      (IpFilter$PublicNetworkSource-to-edn
                                        (.getPublicNetworkSource arg)))
      (seq (.getVpcNetworkSources arg)) (assoc :vpcNetworkSources
                                          (map IpFilter$VpcNetworkSource-to-edn
                                            (.getVpcNetworkSources arg))))))

(def IpFilter-schema
  [:map
   {:closed true,
    :doc
      "A buckets <a href=\"https://cloud.google.com/storage/docs/ip-filtering-overview\">IP\nfiltering</a> configuration. Specifies the network sources that can access the bucket, as well\nas its underlying objects.\n\n@since 2.54.0",
    :gcp/category :nested/accessor-with-builder,
    :gcp/key :gcp.storage/BucketInfo.IpFilter}
   [:allowAllServiceAgentAccess
    {:optional true,
     :getter-doc
       "Whether or not to allow all P4SA access to the bucket. When set to true, IP filter config\nvalidation will not apply.\n\n@since 2.54.0\n@see Builder#setAllowAllServiceAgentAccess(Boolean)",
     :setter-doc
       "Whether or not to allow all P4SA access to the bucket. When set to true, IP filter config\nvalidation will not apply.\n\n@since 2.54.0\n@see IpFilter#getAllowAllServiceAgentAccess()"}
    :boolean]
   [:allowCrossOrgVpcs
    {:optional true,
     :getter-doc
       "Optional. Whether or not to allow VPCs from orgs different than the bucket's parent org to\naccess the bucket. When set to true, validations on the existence of the VPCs won't be\nperformed. If set to false, each VPC network source will be checked to belong to the same org\nas the bucket as well as validated for existence.\n\n@since 2.54.0\n@see Builder#setAllowCrossOrgVpcs(Boolean)",
     :setter-doc
       "Optional. Whether or not to allow VPCs from orgs different than the bucket's parent org to\naccess the bucket. When set to true, validations on the existence of the VPCs won't be\nperformed. If set to false, each VPC network source will be checked to belong to the same\norg as the bucket as well as validated for existence.\n\n@since 2.54.0\n@see IpFilter#getAllowCrossOrgVpcs()"}
    :boolean]
   [:mode
    {:optional true,
     :getter-doc
       "The state of the IP filter configuration. Valid values are `Enabled` and `Disabled`. When set\nto `Enabled`, IP filtering rules are applied to a bucket and all incoming requests to the\nbucket are evaluated against these rules. When set to `Disabled`, IP filtering rules are not\napplied to a bucket.\n\n@since 2.54.0\n@see Builder#setMode",
     :setter-doc
       "The state of the IP filter configuration. Valid values are `Enabled` and `Disabled`. When\nset to `Enabled`, IP filtering rules are applied to a bucket and all incoming requests to\nthe bucket are evaluated against these rules. When set to `Disabled`, IP filtering rules\nare not applied to a bucket.\n\n@since 2.54.0\n@see IpFilter#getMode"}
    [:string {:min 1}]]
   [:publicNetworkSource
    {:optional true,
     :getter-doc
       "Optional. Public IPs allowed to operate or access the bucket.\n\n@since 2.54.0\n@see Builder#setPublicNetworkSource(PublicNetworkSource)",
     :setter-doc
       "Optional. Public IPs allowed to operate or access the bucket.\n\n@since 2.54.0\n@see IpFilter#getPublicNetworkSource()"}
    [:ref :gcp.storage/BucketInfo.IpFilter.PublicNetworkSource]]
   [:vpcNetworkSources
    {:optional true,
     :getter-doc
       "Optional. The list of network sources that are allowed to access operations on the bucket or\nthe underlying objects.\n\n@since 2.54.0\n@see Builder#setVpcNetworkSources(List)",
     :setter-doc
       "Optional. The list of network sources that are allowed to access operations on the bucket\nor the underlying objects.\n\n@since 2.54.0\n@see IpFilter#getVpcNetworkSources()"}
    [:sequential {:min 1}
     [:ref :gcp.storage/BucketInfo.IpFilter.VpcNetworkSource]]]])

(defn
  ^BucketInfo$GoogleManagedEncryptionEnforcementConfig GoogleManagedEncryptionEnforcementConfig-from-edn
  [arg]
  (if (gcp.global/valid? [:enum "NotRestricted" "FullyRestricted"] arg)
    (BucketInfo$GoogleManagedEncryptionEnforcementConfig/of
      (BucketInfo$EncryptionEnforcementRestrictionMode/valueOf arg))
    (if (get arg :restrictionMode)
      (BucketInfo$GoogleManagedEncryptionEnforcementConfig/of
        (BucketInfo$EncryptionEnforcementRestrictionMode/valueOf
          (get arg :restrictionMode))
        (get arg :effectiveTime))
      (BucketInfo$GoogleManagedEncryptionEnforcementConfig/of
        (BucketInfo$EncryptionEnforcementRestrictionMode/valueOf
          (get arg :restrictionMode))))))

(defn
  GoogleManagedEncryptionEnforcementConfig-to-edn
  [^BucketInfo$GoogleManagedEncryptionEnforcementConfig arg]
  (when arg
    (cond-> {:restrictionMode (.name (.getRestrictionMode arg))}
      (.getEffectiveTime arg) (assoc :effectiveTime (.getEffectiveTime arg)))))

(def GoogleManagedEncryptionEnforcementConfig-schema
  [:or
   {:closed true,
    :doc
      "Google Managed Encryption (GMEK) enforcement config of a bucket.\n\n@since 2.55.0 This new api is in preview and is subject to breaking changes.",
    :gcp/category :nested/static-factory,
    :gcp/key :gcp.storage/BucketInfo.GoogleManagedEncryptionEnforcementConfig}
   [:enum {:closed true} "NotRestricted" "FullyRestricted"]
   [:map {:closed true}
    [:effectiveTime
     {:doc
        "Output only. Time from which the config was effective.\n\n@since 2.55.0 This new api is in preview and is subject to breaking changes.",
      :optional true} :OffsetDateTime]
    [:restrictionMode
     {:doc
        "Restriction mode for new objects within the bucket. If {@link\nEncryptionEnforcementRestrictionMode#NOT_RESTRICTED NotRestricted} or {@code null}, creation\nof new objects with google-managed encryption is allowed. If `FullyRestricted`, new objects\ncan not be created using google-managed encryption.\n\n@since 2.55.0 This new api is in preview and is subject to breaking changes."}
     [:enum {:closed true} "NotRestricted" "FullyRestricted"]]]])

(defn
  ^BucketInfo$CustomerManagedEncryptionEnforcementConfig CustomerManagedEncryptionEnforcementConfig-from-edn
  [arg]
  (if (gcp.global/valid? [:enum "NotRestricted" "FullyRestricted"] arg)
    (BucketInfo$CustomerManagedEncryptionEnforcementConfig/of
      (BucketInfo$EncryptionEnforcementRestrictionMode/valueOf arg))
    (if (get arg :restrictionMode)
      (BucketInfo$CustomerManagedEncryptionEnforcementConfig/of
        (BucketInfo$EncryptionEnforcementRestrictionMode/valueOf
          (get arg :restrictionMode))
        (get arg :effectiveTime))
      (BucketInfo$CustomerManagedEncryptionEnforcementConfig/of
        (BucketInfo$EncryptionEnforcementRestrictionMode/valueOf
          (get arg :restrictionMode))))))

(defn
  CustomerManagedEncryptionEnforcementConfig-to-edn
  [^BucketInfo$CustomerManagedEncryptionEnforcementConfig arg]
  (when arg
    (cond-> {:restrictionMode (.name (.getRestrictionMode arg))}
      (.getEffectiveTime arg) (assoc :effectiveTime (.getEffectiveTime arg)))))

(def CustomerManagedEncryptionEnforcementConfig-schema
  [:or
   {:closed true,
    :doc
      "Customer Managed Encryption (CMEK) enforcement config of a bucket.\n\n@since 2.55.0 This new api is in preview and is subject to breaking changes.",
    :gcp/category :nested/static-factory,
    :gcp/key :gcp.storage/BucketInfo.CustomerManagedEncryptionEnforcementConfig}
   [:enum {:closed true} "NotRestricted" "FullyRestricted"]
   [:map {:closed true}
    [:effectiveTime
     {:doc
        "Output only. Time from which the config was effective.\n\n@since 2.55.0 This new api is in preview and is subject to breaking changes.",
      :optional true} :OffsetDateTime]
    [:restrictionMode
     {:doc
        "Restriction mode for new objects within the bucket. If {@link\nEncryptionEnforcementRestrictionMode#NOT_RESTRICTED NotRestricted} or {@code null}, creation\nof new objects with customer-managed encryption is allowed. If `FullyRestricted`, new objects\ncan not be created using customer-managed encryption.\n\n@since 2.55.0 This new api is in preview and is subject to breaking changes."}
     [:enum {:closed true} "NotRestricted" "FullyRestricted"]]]])

(defn
  ^BucketInfo$CustomerSuppliedEncryptionEnforcementConfig CustomerSuppliedEncryptionEnforcementConfig-from-edn
  [arg]
  (if (gcp.global/valid? [:enum "NotRestricted" "FullyRestricted"] arg)
    (BucketInfo$CustomerSuppliedEncryptionEnforcementConfig/of
      (BucketInfo$EncryptionEnforcementRestrictionMode/valueOf arg))
    (if (get arg :restrictionMode)
      (BucketInfo$CustomerSuppliedEncryptionEnforcementConfig/of
        (BucketInfo$EncryptionEnforcementRestrictionMode/valueOf
          (get arg :restrictionMode))
        (get arg :effectiveTime))
      (BucketInfo$CustomerSuppliedEncryptionEnforcementConfig/of
        (BucketInfo$EncryptionEnforcementRestrictionMode/valueOf
          (get arg :restrictionMode))))))

(defn
  CustomerSuppliedEncryptionEnforcementConfig-to-edn
  [^BucketInfo$CustomerSuppliedEncryptionEnforcementConfig arg]
  (when arg
    (cond-> {:restrictionMode (.name (.getRestrictionMode arg))}
      (.getEffectiveTime arg) (assoc :effectiveTime (.getEffectiveTime arg)))))

(def CustomerSuppliedEncryptionEnforcementConfig-schema
  [:or
   {:closed true,
    :doc
      "Customer Supplied Encryption (CSEK) enforcement config of a bucket.\n\n@since 2.55.0 This new api is in preview and is subject to breaking changes.",
    :gcp/category :nested/static-factory,
    :gcp/key
      :gcp.storage/BucketInfo.CustomerSuppliedEncryptionEnforcementConfig}
   [:enum {:closed true} "NotRestricted" "FullyRestricted"]
   [:map {:closed true}
    [:effectiveTime
     {:doc
        "Output only. Time from which the config was effective.\n\n@since 2.55.0 This new api is in preview and is subject to breaking changes.",
      :optional true} :OffsetDateTime]
    [:restrictionMode
     {:doc
        "Restriction mode for new objects within the bucket. If {@link\nEncryptionEnforcementRestrictionMode#NOT_RESTRICTED NotRestricted} or {@code null}, creation\nof new objects with customer-supplied encryption is allowed. If `FullyRestricted`, new\nobjects can not be created using customer-supplied encryption.\n\n@since 2.55.0 This new api is in preview and is subject to breaking changes."}
     [:enum {:closed true} "NotRestricted" "FullyRestricted"]]]])

(def EncryptionEnforcementRestrictionMode-schema
  [:enum
   {:closed true,
    :doc
      "@since 2.55.0 This new api is in preview and is subject to breaking changes.",
    :gcp/category :nested/string-enum,
    :gcp/key :gcp.storage/BucketInfo.EncryptionEnforcementRestrictionMode}
   "NOT_RESTRICTED" "FULLY_RESTRICTED"])

(defn ^BucketInfo from-edn
  [arg]
  (global/strict! :gcp.storage/BucketInfo arg)
  (let [builder (BucketInfo/newBuilder (get arg :name))]
    (when (seq (get arg :acl))
      (.setAcl builder (map Acl/from-edn (get arg :acl))))
    (when (some? (get arg :autoclass))
      (.setAutoclass builder (Autoclass-from-edn (get arg :autoclass))))
    (when (seq (get arg :cors))
      (.setCors builder (map Cors/from-edn (get arg :cors))))
    (when (some? (get arg :customPlacementConfig))
      (.setCustomPlacementConfig builder
                                 (CustomPlacementConfig-from-edn
                                   (get arg :customPlacementConfig))))
    (when (some? (get arg :customerManagedEncryptionEnforcementConfig))
      (.setCustomerManagedEncryptionEnforcementConfig
        builder
        (CustomerManagedEncryptionEnforcementConfig-from-edn
          (get arg :customerManagedEncryptionEnforcementConfig))))
    (when (some? (get arg :customerSuppliedEncryptionEnforcementConfig))
      (.setCustomerSuppliedEncryptionEnforcementConfig
        builder
        (CustomerSuppliedEncryptionEnforcementConfig-from-edn
          (get arg :customerSuppliedEncryptionEnforcementConfig))))
    (when (seq (get arg :defaultAcl))
      (.setDefaultAcl builder (map Acl/from-edn (get arg :defaultAcl))))
    (when (some? (get arg :defaultEventBasedHold))
      (.setDefaultEventBasedHold builder (get arg :defaultEventBasedHold)))
    (when (some? (get arg :defaultKmsKeyName))
      (.setDefaultKmsKeyName builder (get arg :defaultKmsKeyName)))
    (when (some? (get arg :googleManagedEncryptionEnforcementConfig))
      (.setGoogleManagedEncryptionEnforcementConfig
        builder
        (GoogleManagedEncryptionEnforcementConfig-from-edn
          (get arg :googleManagedEncryptionEnforcementConfig))))
    (when (some? (get arg :hierarchicalNamespace))
      (.setHierarchicalNamespace builder
                                 (HierarchicalNamespace-from-edn
                                   (get arg :hierarchicalNamespace))))
    (when (some? (get arg :iamConfiguration))
      (.setIamConfiguration builder
                            (IamConfiguration-from-edn
                              (get arg :iamConfiguration))))
    (when (some? (get arg :indexPage))
      (.setIndexPage builder (get arg :indexPage)))
    (when (some? (get arg :ipFilter))
      (.setIpFilter builder (IpFilter-from-edn (get arg :ipFilter))))
    (when (some? (get arg :isUnreachable))
      (.setIsUnreachable builder (get arg :isUnreachable)))
    (when (seq (get arg :labels))
      (.setLabels builder
                  (into {} (map (fn [[k v]] [(name k) v])) (get arg :labels))))
    (when (seq (get arg :lifecycleRules))
      (.setLifecycleRules builder
                          (map LifecycleRule-from-edn
                            (get arg :lifecycleRules))))
    (when (some? (get arg :location))
      (.setLocation builder (get arg :location)))
    (when (some? (get arg :logging))
      (.setLogging builder (Logging-from-edn (get arg :logging))))
    (when (some? (get arg :notFoundPage))
      (.setNotFoundPage builder (get arg :notFoundPage)))
    (when (some? (get arg :requesterPays))
      (.setRequesterPays builder (get arg :requesterPays)))
    (when (some? (get arg :retentionPeriodDuration))
      (.setRetentionPeriodDuration builder (get arg :retentionPeriodDuration)))
    (when (some? (get arg :rpo)) (.setRpo builder (Rpo/valueOf (get arg :rpo))))
    (when (some? (get arg :softDeletePolicy))
      (.setSoftDeletePolicy builder
                            (SoftDeletePolicy-from-edn
                              (get arg :softDeletePolicy))))
    (when (some? (get arg :storageClass))
      (.setStorageClass builder (StorageClass/valueOf (get arg :storageClass))))
    (when (some? (get arg :versioningEnabled))
      (.setVersioningEnabled builder (get arg :versioningEnabled)))
    (.build builder)))

(defn to-edn
  [^BucketInfo arg]
  {:post [(global/strict! :gcp.storage/BucketInfo %)]}
  (when arg
    (cond-> {:name (.getName arg)}
      (seq (.getAcl arg)) (assoc :acl (map Acl/to-edn (.getAcl arg)))
      (.getAutoclass arg) (assoc :autoclass
                            (Autoclass-to-edn (.getAutoclass arg)))
      (seq (.getCors arg)) (assoc :cors (map Cors/to-edn (.getCors arg)))
      (.getCreateTimeOffsetDateTime arg) (assoc :createTime
                                           (.getCreateTimeOffsetDateTime arg))
      (.getCustomPlacementConfig arg) (assoc :customPlacementConfig
                                        (CustomPlacementConfig-to-edn
                                          (.getCustomPlacementConfig arg)))
      (.getCustomerManagedEncryptionEnforcementConfig arg)
        (assoc :customerManagedEncryptionEnforcementConfig
          (CustomerManagedEncryptionEnforcementConfig-to-edn
            (.getCustomerManagedEncryptionEnforcementConfig arg)))
      (.getCustomerSuppliedEncryptionEnforcementConfig arg)
        (assoc :customerSuppliedEncryptionEnforcementConfig
          (CustomerSuppliedEncryptionEnforcementConfig-to-edn
            (.getCustomerSuppliedEncryptionEnforcementConfig arg)))
      (seq (.getDefaultAcl arg)) (assoc :defaultAcl
                                   (map Acl/to-edn (.getDefaultAcl arg)))
      (.getDefaultEventBasedHold arg) (assoc :defaultEventBasedHold
                                        (.getDefaultEventBasedHold arg))
      (some->> (.getDefaultKmsKeyName arg)
               (not= ""))
        (assoc :defaultKmsKeyName (.getDefaultKmsKeyName arg))
      (some->> (.getEtag arg)
               (not= ""))
        (assoc :etag (.getEtag arg))
      (some->> (.getGeneratedId arg)
               (not= ""))
        (assoc :generatedId (.getGeneratedId arg))
      (.getGoogleManagedEncryptionEnforcementConfig arg)
        (assoc :googleManagedEncryptionEnforcementConfig
          (GoogleManagedEncryptionEnforcementConfig-to-edn
            (.getGoogleManagedEncryptionEnforcementConfig arg)))
      (.getHierarchicalNamespace arg) (assoc :hierarchicalNamespace
                                        (HierarchicalNamespace-to-edn
                                          (.getHierarchicalNamespace arg)))
      (.getIamConfiguration arg) (assoc :iamConfiguration
                                   (IamConfiguration-to-edn
                                     (.getIamConfiguration arg)))
      (some->> (.getIndexPage arg)
               (not= ""))
        (assoc :indexPage (.getIndexPage arg))
      (.getIpFilter arg) (assoc :ipFilter (IpFilter-to-edn (.getIpFilter arg)))
      (seq (.getLabels arg))
        (assoc :labels
          (into {} (map (fn [[k v]] [(keyword k) v])) (.getLabels arg)))
      (seq (.getLifecycleRules arg)) (assoc :lifecycleRules
                                       (map LifecycleRule-to-edn
                                         (.getLifecycleRules arg)))
      (some->> (.getLocation arg)
               (not= ""))
        (assoc :location (.getLocation arg))
      (some->> (.getLocationType arg)
               (not= ""))
        (assoc :locationType (.getLocationType arg))
      (.getLogging arg) (assoc :logging (Logging-to-edn (.getLogging arg)))
      (.getMetageneration arg) (assoc :metageneration (.getMetageneration arg))
      (some->> (.getNotFoundPage arg)
               (not= ""))
        (assoc :notFoundPage (.getNotFoundPage arg))
      (.getObjectRetention arg) (assoc :objectRetention
                                  (ObjectRetention-to-edn (.getObjectRetention
                                                            arg)))
      (.getOwner arg) (assoc :owner (Acl/Entity-to-edn (.getOwner arg)))
      (.getProject arg) (assoc :project (.getProject arg))
      (.requesterPays arg) (assoc :requesterPays (.requesterPays arg))
      (.getRetentionEffectiveTimeOffsetDateTime arg)
        (assoc :retentionEffectiveTime
          (.getRetentionEffectiveTimeOffsetDateTime arg))
      (.getRetentionPeriodDuration arg) (assoc :retentionPeriod
                                          (.getRetentionPeriodDuration arg))
      (.retentionPolicyIsLocked arg) (assoc :retentionPolicyIsLocked
                                       (.retentionPolicyIsLocked arg))
      (.getRpo arg) (assoc :rpo (.name (.getRpo arg)))
      (some->> (.getSelfLink arg)
               (not= ""))
        (assoc :selfLink (.getSelfLink arg))
      (.getSoftDeletePolicy arg) (assoc :softDeletePolicy
                                   (SoftDeletePolicy-to-edn
                                     (.getSoftDeletePolicy arg)))
      (.getStorageClass arg) (assoc :storageClass
                               (.name (.getStorageClass arg)))
      (.isUnreachable arg) (assoc :unreachable (.isUnreachable arg))
      (.getUpdateTimeOffsetDateTime arg) (assoc :updateTime
                                           (.getUpdateTimeOffsetDateTime arg))
      (.versioningEnabled arg) (assoc :versioningEnabled
                                 (.versioningEnabled arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google Storage bucket metadata;\n\n@see <a href=\"https://cloud.google.com/storage/docs/concepts-techniques#concepts\">Concepts and\n    Terminology</a>",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.storage/BucketInfo}
   [:acl
    {:optional true,
     :getter-doc
       "Returns the bucket's access control configuration.\n\n@see <a href=\"https://cloud.google.com/storage/docs/access-control#About-Access-Control-Lists\">\n    About Access Control Lists</a>",
     :setter-doc
       "Sets the bucket's access control configuration.\n\n@see <a\n    href=\"https://cloud.google.com/storage/docs/access-control#About-Access-Control-Lists\">\n    About Access Control Lists</a>"}
    [:sequential {:min 1} :gcp.storage/Acl]]
   [:autoclass
    {:optional true, :getter-doc "Returns the Autoclass configuration"}
    [:ref :gcp.storage/BucketInfo.Autoclass]]
   [:cors
    {:optional true,
     :getter-doc
       "Returns the bucket's Cross-Origin Resource Sharing (CORS) configuration.\n\n@see <a href=\"https://cloud.google.com/storage/docs/cross-origin\">Cross-Origin Resource Sharing\n    (CORS)</a>",
     :setter-doc
       "Sets the bucket's Cross-Origin Resource Sharing (CORS) configuration.\n\n@see <a href=\"https://cloud.google.com/storage/docs/cross-origin\">Cross-Origin Resource\n    Sharing (CORS)</a>"}
    [:sequential {:min 1} :gcp.storage/Cors]]
   [:createTime {:optional true, :read-only? true} :OffsetDateTime]
   [:customPlacementConfig
    {:optional true, :getter-doc "Returns the Custom Placement Configuration"}
    [:ref :gcp.storage/BucketInfo.CustomPlacementConfig]]
   [:customerManagedEncryptionEnforcementConfig
    {:optional true,
     :setter-doc
       "@since 2.55.0 This new api is in preview and is subject to breaking changes."}
    [:ref :gcp.storage/BucketInfo.CustomerManagedEncryptionEnforcementConfig]]
   [:customerSuppliedEncryptionEnforcementConfig
    {:optional true,
     :setter-doc
       "@since 2.55.0 This new api is in preview and is subject to breaking changes."}
    [:ref :gcp.storage/BucketInfo.CustomerSuppliedEncryptionEnforcementConfig]]
   [:defaultAcl
    {:optional true,
     :getter-doc
       "Returns the default access control configuration for this bucket's blobs.\n\n@see <a href=\"https://cloud.google.com/storage/docs/access-control#About-Access-Control-Lists\">\n    About Access Control Lists</a>",
     :setter-doc
       "Sets the default access control configuration to apply to bucket's blobs when no other\nconfiguration is specified.\n\n@see <a\n    href=\"https://cloud.google.com/storage/docs/access-control#About-Access-Control-Lists\">\n    About Access Control Lists</a>"}
    [:sequential {:min 1} :gcp.storage/Acl]]
   [:defaultEventBasedHold
    {:optional true,
     :getter-doc
       "Returns a {@code Boolean} with either {@code true}, {@code null} and in certain cases {@code\nfalse}.\n\n<p>Case 1: {@code true} the field {@link\ncom.google.cloud.storage.Storage.BucketField#DEFAULT_EVENT_BASED_HOLD} is selected in a {@link\nStorage#get(String, Storage.BucketGetOption...)} and default event-based hold for the bucket is\nenabled.\n\n<p>Case 2.1: {@code null} the field {@link\ncom.google.cloud.storage.Storage.BucketField#DEFAULT_EVENT_BASED_HOLD} is selected in a {@link\nStorage#get(String, Storage.BucketGetOption...)}, but default event-based hold for the bucket\nis not enabled. This case can be considered implicitly {@code false}.\n\n<p>Case 2.2: {@code null} the field {@link\ncom.google.cloud.storage.Storage.BucketField#DEFAULT_EVENT_BASED_HOLD} is not selected in a\n{@link Storage#get(String, Storage.BucketGetOption...)}, and the state for this field is\nunknown.\n\n<p>Case 3: {@code false} default event-based hold is explicitly set to false using in a {@link\nBuilder#setDefaultEventBasedHold(Boolean)} client side for a follow-up request e.g. {@link\nStorage#update(BucketInfo, Storage.BucketTargetOption...)} in which case the value of default\nevent-based hold will remain {@code false} for the given instance.",
     :setter-doc "Sets the default event-based hold for this bucket."} :boolean]
   [:defaultKmsKeyName
    {:optional true,
     :getter-doc
       "Returns the default Cloud KMS key to be applied to newly inserted objects in this bucket.",
     :setter-doc "Sets the default Cloud KMS key name for this bucket."}
    [:string {:min 1}]]
   [:etag
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns HTTP 1.1 Entity tag for the bucket.\n\n@see <a href=\"http://tools.ietf.org/html/rfc2616#section-3.11\">Entity Tags</a>"}
    [:string {:min 1}]]
   [:generatedId
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the service-generated id for the bucket."}
    [:string {:min 1}]]
   [:googleManagedEncryptionEnforcementConfig
    {:optional true,
     :setter-doc
       "@since 2.55.0 This new api is in preview and is subject to breaking changes."}
    [:ref :gcp.storage/BucketInfo.GoogleManagedEncryptionEnforcementConfig]]
   [:hierarchicalNamespace
    {:optional true,
     :getter-doc "Returns the Hierarchical Namespace (Folders) Configuration"}
    [:ref :gcp.storage/BucketInfo.HierarchicalNamespace]]
   [:iamConfiguration
    {:optional true,
     :getter-doc "Returns the IAM configuration",
     :setter-doc
       "Sets the IamConfiguration to specify whether IAM access should be enabled.\n\n@see <a href=\"https://cloud.google.com/storage/docs/bucket-policy-only\">Bucket Policy\n    Only</a>"}
    [:ref :gcp.storage/BucketInfo.IamConfiguration]]
   [:indexPage
    {:optional true,
     :getter-doc
       "Returns bucket's website index page. Behaves as the bucket's directory index where missing\nblobs are treated as potential directories.",
     :setter-doc
       "Sets the bucket's website index page. Behaves as the bucket's directory index where missing\nblobs are treated as potential directories."}
    [:string {:min 1}]]
   [:ipFilter
    {:optional true, :getter-doc "@since 2.54.0", :setter-doc "@since 2.54.0"}
    [:ref :gcp.storage/BucketInfo.IpFilter]]
   [:labels
    {:optional true,
     :getter-doc "Returns the labels for this bucket.",
     :setter-doc "Sets the label of this bucket."}
    [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
   [:lifecycleRules
    {:optional true,
     :setter-doc
       "Sets the bucket's lifecycle configuration as a number of lifecycle rules, consisting of an\naction and a condition.\n\n@see <a href=\"https://cloud.google.com/storage/docs/lifecycle\">Object Lifecycle\n    Management</a>"}
    [:sequential {:min 1} [:ref :gcp.storage/BucketInfo.LifecycleRule]]]
   [:location
    {:optional true,
     :getter-doc
       "Returns the bucket's location. Data for blobs in the bucket resides in physical storage within\nthis region or regions. If specifying more than one region `customPlacementConfig` should be\nset in conjunction.\n\n@see <a href=\"https://cloud.google.com/storage/docs/bucket-locations\">Bucket Locations</a>",
     :setter-doc
       "Sets the bucket's location. Data for blobs in the bucket resides in physical storage within\nthis region or regions. A list of supported values is available <a\nhref=\"https://cloud.google.com/storage/docs/bucket-locations\">here</a>."}
    [:string {:min 1}]]
   [:locationType
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the bucket's locationType.\n\n@see <a href=\"https://cloud.google.com/storage/docs/bucket-locations\">Bucket LocationType</a>"}
    [:string {:min 1}]]
   [:logging {:optional true, :getter-doc "Returns the Logging"}
    [:ref :gcp.storage/BucketInfo.Logging]]
   [:metageneration
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the metadata generation of this bucket."} :i64]
   [:name {:getter-doc "Returns the bucket's name."} [:string {:min 1}]]
   [:notFoundPage
    {:optional true,
     :getter-doc
       "Returns the custom object to return when a requested resource is not found.",
     :setter-doc
       "Sets the custom object to return when a requested resource is not found."}
    [:string {:min 1}]]
   [:objectRetention
    {:optional true,
     :read-only? true,
     :getter-doc "returns the Object Retention configuration"}
    [:ref :gcp.storage/BucketInfo.ObjectRetention]]
   [:owner
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the bucket's owner. This is always the project team's owner group."}
    :gcp.storage/Acl.Entity]
   [:project
    {:optional true,
     :read-only? true,
     :getter-doc "The project number of the project the bucket belongs to"}
    :bigint]
   [:requesterPays
    {:optional true,
     :getter-doc
       "Returns a {@code Boolean} with either {@code true}, {@code false}, and in a specific case\n{@code null}.\n\n<p>Case 1: {@code true} the field {@link com.google.cloud.storage.Storage.BucketField#BILLING}\nis selected in a {@link Storage#get(String, Storage.BucketGetOption...)} and requester pays for\nthe bucket is enabled.\n\n<p>Case 2: {@code false} the field {@link com.google.cloud.storage.Storage.BucketField#BILLING}\nin a {@link Storage#get(String, Storage.BucketGetOption...)} is selected and requester pays for\nthe bucket is disable.\n\n<p>Case 3: {@code null} the field {@link com.google.cloud.storage.Storage.BucketField#BILLING}\nin a {@link Storage#get(String, Storage.BucketGetOption...)} is not selected, the value is\nunknown.",
     :setter-doc
       "Sets whether a user accessing the bucket or an object it contains should assume the transit\ncosts related to the access."}
    :boolean]
   [:retentionEffectiveTime
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the retention effective time a policy took effect if a retention policy is defined."}
    :OffsetDateTime]
   [:retentionPeriod
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the retention policy retention period."} :Duration]
   [:retentionPolicyIsLocked
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns a {@code Boolean} with either {@code true} or {@code null}.\n\n<p>Case 1: {@code true} the field {@link\ncom.google.cloud.storage.Storage.BucketField#RETENTION_POLICY} is selected in a {@link\nStorage#get(String, Storage.BucketGetOption...)} and retention policy for the bucket is locked.\n\n<p>Case 2.1: {@code null} the field {@link\ncom.google.cloud.storage.Storage.BucketField#RETENTION_POLICY} is selected in a {@link\nStorage#get(String, Storage.BucketGetOption...)}, but retention policy for the bucket is not\nlocked. This case can be considered implicitly {@code false}.\n\n<p>Case 2.2: {@code null} the field {@link\ncom.google.cloud.storage.Storage.BucketField#RETENTION_POLICY} is not selected in a {@link\nStorage#get(String, Storage.BucketGetOption...)}, and the state for this field is unknown."}
    :boolean]
   [:rpo
    {:optional true,
     :getter-doc
       "Returns the bucket's recovery point objective (RPO). This defines how quickly data is\nreplicated between regions in a dual-region bucket. Not defined for single-region buckets.\n\n@see <a href=\"https://cloud.google.com/storage/docs/turbo-replication\">Turbo Replication\"</a>",
     :setter-doc
       "Sets the bucket's Recovery Point Objective (RPO). This can only be set for a dual-region\nbucket, and determines the speed at which data will be replicated between regions. See the\n{@code Rpo} class for supported values, and <a\nhref=\"https://cloud.google.com/storage/docs/turbo-replication\">here</a> for additional\ndetails."}
    [:enum {:closed true} "DEFAULT" "ASYNC_TURBO"]]
   [:selfLink
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the URI of this bucket as a string."}
    [:string {:min 1}]]
   [:softDeletePolicy
    {:optional true, :getter-doc "returns the Soft Delete policy"}
    [:ref :gcp.storage/BucketInfo.SoftDeletePolicy]]
   [:storageClass
    {:optional true,
     :getter-doc
       "Returns the bucket's storage class. This defines how blobs in the bucket are stored and\ndetermines the SLA and the cost of storage.\n\n@see <a href=\"https://cloud.google.com/storage/docs/storage-classes\">Storage Classes</a>",
     :setter-doc
       "Sets the bucket's storage class. This defines how blobs in the bucket are stored and\ndetermines the SLA and the cost of storage. A list of supported values is available <a\nhref=\"https://cloud.google.com/storage/docs/storage-classes\">here</a>."}
    [:enum {:closed true} "STANDARD" "NEARLINE" "COLDLINE" "ARCHIVE" "REGIONAL"
     "MULTI_REGIONAL" "DURABLE_REDUCED_AVAILABILITY"]]
   [:unreachable
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns a {@code Boolean} with {@code true} if the bucket is unreachable, else {@code null}\n\n<p>A bucket may be unreachable if the region in which it resides is experiencing an outage or\nif there are other temporary access issues."}
    :boolean] [:updateTime {:optional true, :read-only? true} :OffsetDateTime]
   [:versioningEnabled
    {:optional true,
     :getter-doc
       "Returns a {@code Boolean} with either {@code true}, {@code null} and in certain cases {@code\nfalse}.\n\n<p>Case 1: {@code true} the field {@link\ncom.google.cloud.storage.Storage.BucketField#VERSIONING} is selected in a {@link\nStorage#get(String, Storage.BucketGetOption...)} and versions for the bucket is enabled.\n\n<p>Case 2.1: {@code null} the field {@link\ncom.google.cloud.storage.Storage.BucketField#VERSIONING} is selected in a {@link\nStorage#get(String, Storage.BucketGetOption...)}, but versions for the bucket is not enabled.\nThis case can be considered implicitly {@code false}.\n\n<p>Case 2.2: {@code null} the field {@link\ncom.google.cloud.storage.Storage.BucketField#VERSIONING} is not selected in a {@link\nStorage#get(String, Storage.BucketGetOption...)}, and the state for this field is unknown.\n\n<p>Case 3: {@code false} versions is explicitly set to false client side for a follow-up\nrequest for example {@link Storage#update(BucketInfo, Storage.BucketTargetOption...)} in which\ncase the value of versions will remain {@code false} for for the given instance.",
     :setter-doc
       "Sets whether versioning should be enabled for this bucket. When set to true, versioning is\nfully enabled."}
    :boolean]])

(global/include-schema-registry!
  (with-meta
    {:gcp.storage/BucketInfo schema,
     :gcp.storage/BucketInfo.Autoclass Autoclass-schema,
     :gcp.storage/BucketInfo.CustomPlacementConfig CustomPlacementConfig-schema,
     :gcp.storage/BucketInfo.CustomerManagedEncryptionEnforcementConfig
       CustomerManagedEncryptionEnforcementConfig-schema,
     :gcp.storage/BucketInfo.CustomerSuppliedEncryptionEnforcementConfig
       CustomerSuppliedEncryptionEnforcementConfig-schema,
     :gcp.storage/BucketInfo.EncryptionEnforcementRestrictionMode
       EncryptionEnforcementRestrictionMode-schema,
     :gcp.storage/BucketInfo.GoogleManagedEncryptionEnforcementConfig
       GoogleManagedEncryptionEnforcementConfig-schema,
     :gcp.storage/BucketInfo.HierarchicalNamespace HierarchicalNamespace-schema,
     :gcp.storage/BucketInfo.IamConfiguration IamConfiguration-schema,
     :gcp.storage/BucketInfo.IpFilter IpFilter-schema,
     :gcp.storage/BucketInfo.IpFilter.PublicNetworkSource
       IpFilter$PublicNetworkSource-schema,
     :gcp.storage/BucketInfo.IpFilter.VpcNetworkSource
       IpFilter$VpcNetworkSource-schema,
     :gcp.storage/BucketInfo.LifecycleRule LifecycleRule-schema,
     :gcp.storage/BucketInfo.LifecycleRule.AbortIncompleteMPUAction
       LifecycleRule$AbortIncompleteMPUAction-schema,
     :gcp.storage/BucketInfo.LifecycleRule.DeleteLifecycleAction
       LifecycleRule$DeleteLifecycleAction-schema,
     :gcp.storage/BucketInfo.LifecycleRule.LifecycleAction
       LifecycleRule$LifecycleAction-schema,
     :gcp.storage/BucketInfo.LifecycleRule.LifecycleCondition
       LifecycleRule$LifecycleCondition-schema,
     :gcp.storage/BucketInfo.LifecycleRule.SetStorageClassLifecycleAction
       LifecycleRule$SetStorageClassLifecycleAction-schema,
     :gcp.storage/BucketInfo.Logging Logging-schema,
     :gcp.storage/BucketInfo.ObjectRetention ObjectRetention-schema,
     :gcp.storage/BucketInfo.ObjectRetention.Mode ObjectRetention$Mode-schema,
     :gcp.storage/BucketInfo.PublicAccessPrevention
       PublicAccessPrevention-schema,
     :gcp.storage/BucketInfo.RawDeleteRule RawDeleteRule-schema,
     :gcp.storage/BucketInfo.SoftDeletePolicy SoftDeletePolicy-schema}
    {:gcp.global/name "gcp.storage.BucketInfo"}))