;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.DatasetInfo
  {:doc
     "Google BigQuery Dataset information. A dataset is a grouping mechanism that holds zero or more\ntables. Datasets are the lowest level unit of access control; you cannot control access at the\ntable level.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/managing_jobs_datasets_projects#datasets\">\n    Managing Jobs, Datasets, and Projects</a>"
   :file-git-sha "6e3e07a22b8397e1e9d5b567589e44abc55961f2"
   :fqcn "com.google.cloud.bigquery.DatasetInfo"
   :gcp.dev/certification
     {:base-seed 1775131017231
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1775131017231 :standard 1775131017232 :stress 1775131017233}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:56:58.596928898Z"}}
  (:require [gcp.bigquery.Acl :as Acl]
            [gcp.bigquery.DatasetId :as DatasetId]
            [gcp.bigquery.EncryptionConfiguration :as EncryptionConfiguration]
            [gcp.bigquery.ExternalDatasetReference :as ExternalDatasetReference]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery DatasetInfo DatasetInfo$Builder]))

(declare from-edn to-edn)

(defn ^DatasetInfo from-edn
  [arg]
  (global/strict! :gcp.bigquery/DatasetInfo arg)
  (let [builder (DatasetInfo/newBuilder (DatasetId/from-edn (get arg
                                                                 :datasetId)))]
    (when (seq (get arg :acl))
      (.setAcl builder (map Acl/from-edn (get arg :acl))))
    (when (some? (get arg :defaultCollation))
      (.setDefaultCollation builder (get arg :defaultCollation)))
    (when (some? (get arg :defaultEncryptionConfiguration))
      (.setDefaultEncryptionConfiguration
        builder
        (EncryptionConfiguration/from-edn
          (get arg :defaultEncryptionConfiguration))))
    (when (some? (get arg :defaultPartitionExpirationMs))
      (.setDefaultPartitionExpirationMs
        builder
        (long (get arg :defaultPartitionExpirationMs))))
    (when (some? (get arg :defaultTableLifetime))
      (.setDefaultTableLifetime builder (long (get arg :defaultTableLifetime))))
    (when (some? (get arg :description))
      (.setDescription builder (get arg :description)))
    (when (some? (get arg :externalDatasetReference))
      (.setExternalDatasetReference builder
                                    (ExternalDatasetReference/from-edn
                                      (get arg :externalDatasetReference))))
    (when (some? (get arg :friendlyName))
      (.setFriendlyName builder (get arg :friendlyName)))
    (when (seq (get arg :labels))
      (.setLabels builder
                  (into {} (map (fn [[k v]] [(name k) v])) (get arg :labels))))
    (when (some? (get arg :location))
      (.setLocation builder (get arg :location)))
    (when (some? (get arg :maxTimeTravelHours))
      (.setMaxTimeTravelHours builder (long (get arg :maxTimeTravelHours))))
    (when (seq (get arg :resourceTags))
      (.setResourceTags
        builder
        (into {} (map (fn [[k v]] [(name k) v])) (get arg :resourceTags))))
    (when (some? (get arg :storageBillingModel))
      (.setStorageBillingModel builder (get arg :storageBillingModel)))
    (.build builder)))

(defn to-edn
  [^DatasetInfo arg]
  {:post [(global/strict! :gcp.bigquery/DatasetInfo %)]}
  (when arg
    (cond-> {:datasetId (DatasetId/to-edn (.getDatasetId arg))}
      (seq (.getAcl arg)) (assoc :acl (map Acl/to-edn (.getAcl arg)))
      (.getCreationTime arg) (assoc :creationTime (.getCreationTime arg))
      (some->> (.getDefaultCollation arg)
               (not= ""))
        (assoc :defaultCollation (.getDefaultCollation arg))
      (.getDefaultEncryptionConfiguration arg)
        (assoc :defaultEncryptionConfiguration
          (EncryptionConfiguration/to-edn (.getDefaultEncryptionConfiguration
                                            arg)))
      (.getDefaultPartitionExpirationMs arg)
        (assoc :defaultPartitionExpirationMs
          (.getDefaultPartitionExpirationMs arg))
      (.getDefaultTableLifetime arg) (assoc :defaultTableLifetime
                                       (.getDefaultTableLifetime arg))
      (some->> (.getDescription arg)
               (not= ""))
        (assoc :description (.getDescription arg))
      (some->> (.getEtag arg)
               (not= ""))
        (assoc :etag (.getEtag arg))
      (.getExternalDatasetReference arg)
        (assoc :externalDatasetReference
          (ExternalDatasetReference/to-edn (.getExternalDatasetReference arg)))
      (some->> (.getFriendlyName arg)
               (not= ""))
        (assoc :friendlyName (.getFriendlyName arg))
      (some->> (.getGeneratedId arg)
               (not= ""))
        (assoc :generatedId (.getGeneratedId arg))
      (seq (.getLabels arg))
        (assoc :labels
          (into {} (map (fn [[k v]] [(keyword k) v])) (.getLabels arg)))
      (.getLastModified arg) (assoc :lastModified (.getLastModified arg))
      (some->> (.getLocation arg)
               (not= ""))
        (assoc :location (.getLocation arg))
      (.getMaxTimeTravelHours arg) (assoc :maxTimeTravelHours
                                     (.getMaxTimeTravelHours arg))
      (seq (.getResourceTags arg))
        (assoc :resourceTags
          (into {} (map (fn [[k v]] [(keyword k) v])) (.getResourceTags arg)))
      (some->> (.getSelfLink arg)
               (not= ""))
        (assoc :selfLink (.getSelfLink arg))
      (some->> (.getStorageBillingModel arg)
               (not= ""))
        (assoc :storageBillingModel (.getStorageBillingModel arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Google BigQuery Dataset information. A dataset is a grouping mechanism that holds zero or more\ntables. Datasets are the lowest level unit of access control; you cannot control access at the\ntable level.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/managing_jobs_datasets_projects#datasets\">\n    Managing Jobs, Datasets, and Projects</a>",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.bigquery/DatasetInfo}
   [:acl
    {:optional true,
     :getter-doc
       "Returns the dataset's access control configuration.\n\n<p>Update the ACLs for a dataset.\n\n<pre>{@code\nDataset dataset = bigquery.getDataset(DatasetId.of(\"my_dataset\"));\nList<Acl> beforeAcls = dataset.getAcl();\n\n// Make a copy of the ACLs so that they can be modified.\nArrayList<Acl> acls = new ArrayList<>(beforeAcls);\nacls.add(Acl.of(new Acl.User(\"sample.bigquery.dev@gmail.com\"), Acl.Role.READER));\nDataset.Builder builder = dataset.toBuilder();\nbuilder.setAcl(acls);\n\nbigquery.update(builder.build());  // API request.\n}</pre>\n\n@see <a href=\"https://cloud.google.com/bigquery/access-control\">Access Control</a>",
     :setter-doc
       "Sets the dataset's access control configuration.\n\n@see <a href=\"https://cloud.google.com/bigquery/access-control\">Access Control</a>"}
    [:sequential {:min 1} :gcp.bigquery/Acl]]
   [:creationTime
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the time when this dataset was created, in milliseconds since the epoch."}
    :i64]
   [:datasetId {:getter-doc "Returns the dataset identity."}
    :gcp.bigquery/DatasetId]
   [:defaultCollation
    {:optional true,
     :setter-doc
       "Optional. Defines the default collation specification of future tables created in the\ndataset. If a table is created in this dataset without table-level default collation, then\nthe table inherits the dataset default collation, which is applied to the string fields that\ndo not have explicit collation specified. A change to this field affects only tables created\nafterwards, and does not alter the existing tables. The following values are supported:\n\n<p>* 'und:ci': undetermined locale, case insensitive. * '': empty string. Default to\ncase-sensitive behavior. (-- A wrapper is used here because it is possible to set the value\nto the empty string. --) (-- api-linter: standard-fields=disabled --)"}
    [:string {:min 1}]]
   [:defaultEncryptionConfiguration
    {:optional true,
     :setter-doc
       "The default encryption key for all tables in the dataset. Once this property is set, all\nnewly-created partitioned tables in the dataset will have encryption key set to this value,\nunless table creation request (or query) overrides the key."}
    :gcp.bigquery/EncryptionConfiguration]
   [:defaultPartitionExpirationMs
    {:optional true,
     :setter-doc
       "[Optional] The default partition expiration time for all partitioned tables in the dataset,\nin milliseconds. Once this property is set, all newly-created partitioned tables in the\ndataset will has an expirationMs property in the timePartitioning settings set to this value.\nChanging the value only affect new tables, not existing ones. The storage in a partition will\nhave an expiration time of its partition time plus this value. Setting this property\noverrides the use of defaultTableExpirationMs for partitioned tables: only one of\ndefaultTableExpirationMs and defaultPartitionExpirationMs will be used for any new\npartitioned table. If you provide an explicit timePartitioning.expirationMs when creating or\nupdating a partitioned table, that value takes precedence over the default partition\nexpiration time indicated by this property. The value may be {@code null}."}
    :i64]
   [:defaultTableLifetime
    {:optional true,
     :getter-doc
       "Returns the default lifetime of all tables in the dataset, in milliseconds. Once this property\nis set, all newly-created tables in the dataset will have an expirationTime property set to the\ncreation time plus the value in this property, and changing the value will only affect new\ntables, not existing ones. When the expirationTime for a given table is reached, that table\nwill be deleted automatically. If a table's expirationTime is modified or removed before the\ntable expires, or if you provide an explicit expirationTime when creating a table, that value\ntakes precedence over the default expiration time indicated by this property.\n\n<p>Update the default table expiration time for a dataset.\n\n<pre>{@code\nDataset dataset = bigquery.getDataset(DatasetId.of(\"my_dataset\"));\nLong beforeExpiration = dataset.getDefaultTableLifetime();\n\nLong oneDayMilliseconds = 24 * 60 * 60 * 1000L;\nDataset.Builder builder = dataset.toBuilder();\nbuilder.setDefaultTableLifetime(oneDayMilliseconds);\nbigquery.update(builder.build());  // API request.\n}</pre>",
     :setter-doc
       "Sets the default lifetime of all tables in the dataset, in milliseconds. The minimum value is\n3600000 milliseconds (one hour). Once this property is set, all newly-created tables in the\ndataset will have an expirationTime property set to the creation time plus the value in this\nproperty, and changing the value will only affect new tables, not existing ones. When the\nexpirationTime for a given table is reached, that table will be deleted automatically. If a\ntable's expirationTime is modified or removed before the table expires, or if you provide an\nexplicit expirationTime when creating a table, that value takes precedence over the default\nexpiration time indicated by this property. This property is experimental and might be\nsubject to change or removed."}
    :i64]
   [:description
    {:optional true,
     :getter-doc "Returns a user-friendly description for the dataset.",
     :setter-doc "Sets a user-friendly description for the dataset."}
    [:string {:min 1}]]
   [:etag
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the hash of the dataset resource."}
    [:string {:min 1}]]
   [:externalDatasetReference
    {:optional true,
     :getter-doc
       "Returns information about the external metadata storage where the dataset is defined. Filled\nout when the dataset type is EXTERNAL.",
     :setter-doc
       "Optional. Information about the external metadata storage where the dataset is defined.\nFilled out when the dataset type is EXTERNAL"}
    :gcp.bigquery/ExternalDatasetReference]
   [:friendlyName
    {:optional true,
     :getter-doc "Returns a user-friendly name for the dataset.",
     :setter-doc "Sets a user-friendly name for the dataset."}
    [:string {:min 1}]]
   [:generatedId
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the service-generated id for the dataset."}
    [:string {:min 1}]]
   [:labels
    {:optional true,
     :getter-doc
       "Return a map for labels applied to the dataset.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/labeling-datasets\">Labeling Datasets</a>"}
    [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
   [:lastModified
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns the time when this dataset or any of its tables was last modified, in milliseconds\nsince the epoch."}
    :i64]
   [:location
    {:optional true,
     :getter-doc
       "Returns the geographic location where the dataset should reside.\n\n@see <a\n    href=\"https://cloud.google.com/bigquery/docs/managing_jobs_datasets_projects#dataset-location\">\n    Dataset Location</a>",
     :setter-doc
       "Sets the geographic location where the dataset should reside. This property is experimental\nand might be subject to change or removed.\n\n@see <a href=\"https://cloud.google.com/bigquery/docs/reference/v2/datasets#location\">Dataset\n    Location</a>"}
    [:string {:min 1}]]
   [:maxTimeTravelHours
    {:optional true,
     :getter-doc
       "Returns the number of hours that deleted or updated data will be available to be queried for\nall tables in the dataset.",
     :setter-doc
       "Optional. Defines the time travel window in hours. The value can be from 48 to 168 hours (2\nto 7 days). The default value is 168 hours if this is not set. The value may be {@code null}."}
    :i64]
   [:resourceTags
    {:optional true,
     :getter-doc
       "Optional. The <a href=\"https://cloud.google.com/bigquery/docs/tags\">tags</a> attached to this\ndataset. Tag keys are globally unique. Tag key is expected to be in the namespaced format, for\nexample \"123456789012/environment\" where 123456789012 is the ID of the parent organization or\nproject resource for this tag key. Tag value is expected to be the short name, for example\n\"Production\".\n\n@see <a href=\"https://cloud.google.com/iam/docs/tags-access-control#definitions\">Tag\n    definitions</a> for more details.\n@return value or {@code null} for none",
     :setter-doc
       "Optional. The <a href=\"https://cloud.google.com/bigquery/docs/tags\">tags</a> attached to this\ndataset. Tag keys are globally unique. Tag key is expected to be in the namespaced format,\nfor example \"123456789012/environment\" where 123456789012 is the ID of the parent\norganization or project resource for this tag key. Tag value is expected to be the short\nname, for example \"Production\".\n\n@see <a href=\"https://cloud.google.com/iam/docs/tags-access-control#definitions\">Tag\n    definitions</a> for more details.\n@param resourceTags resourceTags or {@code null} for none"}
    [:map-of [:or simple-keyword? [:string {:min 1}]] [:string {:min 1}]]]
   [:selfLink
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns an URL that can be used to access the resource again. The returned URL can be used for\nget or update requests."}
    [:string {:min 1}]]
   [:storageBillingModel
    {:optional true,
     :setter-doc
       "Optional. Storage billing model to be used for all tables in the dataset. Can be set to\nPHYSICAL. Default is LOGICAL."}
    [:string {:min 1}]]])

(global/include-schema-registry! (with-meta {:gcp.bigquery/DatasetInfo schema}
                                   {:gcp.global/name
                                      "gcp.bigquery.DatasetInfo"}))