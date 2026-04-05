;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.storage.StorageRoles
  {:doc
     "IAM roles specific to Storage. An overview of the permissions available to Storage and the\ncapabilities they grant can be found in the <a\nhref=\"https://cloud.google.com/storage/docs/access-control/iam\">Google Cloud Storage IAM\ndocumentation.</a>"
   :file-git-sha "ca6878974eaf83f0237fa7faacd1e664db93e7a1"
   :fqcn "com.google.cloud.storage.StorageRoles"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "215ec381-0f5f-5884-ab6d-eb0bb246cd16"
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :reason :write-only
      :skipped true
      :timestamp "2026-04-04T03:02:38.882249969Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.storage StorageRoles]))

(declare from-edn to-edn)

(defn ^StorageRoles from-edn
  [arg]
  (global/strict! :gcp.storage/StorageRoles arg)
  (case (first (keys arg))
    :objectViewer (StorageRoles/objectViewer)
    :legacyBucketReader (StorageRoles/legacyBucketReader)
    :objectAdmin (StorageRoles/objectAdmin)
    :objectCreator (StorageRoles/objectCreator)
    :legacyBucketWriter (StorageRoles/legacyBucketWriter)
    :admin (StorageRoles/admin)
    :legacyObjectOwner (StorageRoles/legacyObjectOwner)
    :legacyBucketOwner (StorageRoles/legacyBucketOwner)
    :legacyObjectReader (StorageRoles/legacyObjectReader)))

(def schema
  [:or
   {:closed true,
    :doc
      "IAM roles specific to Storage. An overview of the permissions available to Storage and the\ncapabilities they grant can be found in the <a\nhref=\"https://cloud.google.com/storage/docs/access-control/iam\">Google Cloud Storage IAM\ndocumentation.</a>",
    :gcp/category :factory,
    :gcp/key :gcp.storage/StorageRoles}
   [:map
    {:closed true,
     :doc
       "Grants the following permissions:\n\n<ul>\n  <li>storage.buckets.*\n  <li>storage.objects.*\n</ul>"}
    [:admin :nil]]
   [:map
    {:closed true,
     :doc
       "Grants the following permissions:\n\n<ul>\n  <li>storage.objects.list\n  <li>storage.objects.get\n</ul>"}
    [:objectViewer :nil]]
   [:map
    {:closed true,
     :doc
       "Grants the following permissions:\n\n<ul>\n  <li>storage.objects.create\n</ul>"}
    [:objectCreator :nil]]
   [:map
    {:closed true,
     :doc
       "Grants the following permissions:\n\n<ul>\n  <li>storage.objects.*\n</ul>"}
    [:objectAdmin :nil]]
   [:map
    {:closed true,
     :doc
       "Grants the following permissions:\n\n<ul>\n  <li>storage.buckets.get\n  <li>storage.buckets.update\n  <li>storage.buckets.setIamPolicy\n  <li>storage.buckets.getIamPolicy\n  <li>storage.objects.list\n  <li>storage.objects.create\n  <li>storage.objects.delete\n</ul>"}
    [:legacyBucketOwner :nil]]
   [:map
    {:closed true,
     :doc
       "Grants the following permissions:\n\n<ul>\n  <li>storage.buckets.get\n  <li>storage.objects.list\n  <li>storage.objects.create\n  <li>storage.objects.delete\n</ul>"}
    [:legacyBucketWriter :nil]]
   [:map
    {:closed true,
     :doc
       "Grants the following permissions:\n\n<ul>\n  <li>storage.buckets.get\n  <li>storage.objects.list\n</ul>"}
    [:legacyBucketReader :nil]]
   [:map
    {:closed true,
     :doc
       "Grants the following permissions:\n\n<ul>\n  <li>storage.objects.get\n  <li>storage.objects.update\n  <li>storage.objects.getIamPolicy\n  <li>storage.objects.setIamPolicy\n</ul>"}
    [:legacyObjectOwner :nil]]
   [:map
    {:closed true,
     :doc
       "Grants the following permissions:\n\n<ul>\n  <li>storage.objects.get\n</ul>"}
    [:legacyObjectReader :nil]]])

(global/include-schema-registry! (with-meta {:gcp.storage/StorageRoles schema}
                                   {:gcp.global/name
                                      "gcp.storage.StorageRoles"}))