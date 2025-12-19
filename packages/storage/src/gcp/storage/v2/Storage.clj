(ns gcp.storage.v2.Storage
  (:require [gcp.global :as global]
            [gcp.storage.v2.BlobId]
            [gcp.storage.v2.BlobInfo]
            [gcp.storage.v2.BucketInfo]
            [gcp.storage.v2.StorageOptions])
  (:import (com.google.cloud.storage Storage Storage$BlobGetOption Storage$BlobListOption Storage$BlobSourceOption Storage$BlobTargetOption Storage$BucketGetOption Storage$BucketListOption Storage$BucketTargetOption)))

(defn ^Storage$BlobListOption     BlobListOption:from-edn [arg] (throw (Exception. "unimplemented")))
(defn ^Storage$BlobSourceOption   BlobSourceOption:from-edn [arg] (throw (Exception. "unimplemented")))
(defn ^Storage$BlobGetOption      BlobGetOption:from-edn [arg] (throw (Exception. "unimplemented")))
(defn ^Storage$BlobTargetOption   BlobTargetOption:from-edn [arg] (throw (Exception. "unimplemented")))

(defn ^Storage$BucketGetOption    BucketGetOption:from-edn [arg] (throw (Exception. "unimplemented")))
(defn ^Storage$BucketListOption   BucketListOption:from-edn [arg] (throw (Exception. "unimplemented")))
(defn ^Storage$BucketTargetOption BucketTargetOption:from-edn [arg] (throw (Exception. "unimplemented")))

(def schemas
  {:gcp.storage.v2/Storage
   (assoc-in (global/instance-schema com.google.cloud.storage.Storage)
             [1 :from-edn] 'gcp.storage.v2.StorageOptions/get-service)

   :gcp.storage.v2/Clientable
   [:maybe
    [:or
     :gcp.storage.v2/StorageOptions
     :gcp.storage.v2/Storage
     [:map [:storage [:or :gcp.storage.v2/StorageOptions :gcp.storage.v2/Storage]]]]]

   :gcp.storage.v2/BucketList
   [:maybe
    [:map {:closed true}
     [:storage {:optional true} :gcp.storage.v2/Clientable]
     [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BucketListOption]]]]

   :gcp.storage.v2/BucketGet
   [:map {:closed true}
    [:storage {:optional true} :gcp.storage.v2/Clientable]
    [:bucket :string]
    [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BucketGetOption]]]

   :gcp.storage.v2/BucketCreate
   [:map {:closed true}
    [:storage {:optional true} :gcp.storage.v2/Clientable]
    [:bucketInfo :gcp.storage.v2/BucketInfo]
    [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BucketTargetOption]]]

   :gcp.storage.v2/BlobList
   [:map {:closed true}
    [:storage {:optional true} :gcp.storage.v2/Clientable]
    [:bucket :string]
    [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BlobListOption]]]

   :gcp.storage.v2/BlobDelete
   [:map {:closed true}
    [:storage {:optional true} :gcp.storage.v2/Clientable]
    [:blobs [:sequential :gcp.storage.v2/BlobId]]
    [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BlobSourceOption]]]

   :gcp.storage.v2/BlobCreate
   [:map {:closed true}
    [:storage {:optional true} :gcp.storage.v2/Clientable]
    [:blobInfo {:optional false} :gcp.storage.v2/BlobInfo]
    [:content {:optional true} 'bytes?]
    [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BlobSourceOption]]]

   :gcp.storage.v2/ReadAllBytes
   [:map {:closed true}
    [:storage {:optional true} :gcp.storage.v2/Clientable]
    [:blobId :gcp.storage.v2/BlobId]
    [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BlobSourceOption]]]

   :gcp.storage.v2/Storage.BlobGetOption :any
   :gcp.storage.v2/Storage.BlobListOption :any
   :gcp.storage.v2/Storage.BlobRestoreOption :any
   :gcp.storage.v2/Storage.BlobSourceOption :any
   :gcp.storage.v2/Storage.BlobTargetOption :any
   :gcp.storage.v2/Storage.BlobWriteOption :any
   :gcp.storage.v2/Storage.BucketGetOption :any
   :gcp.storage.v2/Storage.BucketListOption :any
   :gcp.storage.v2/Storage.BucketSourceOption :any
   :gcp.storage.v2/Storage.BucketTargetOption :any
   :gcp.storage.v2/Storage.ComposeRequest :any
   :gcp.storage.v2/Storage.ComposeRequest.SourceBlob :any
   :gcp.storage.v2/Storage.CopyRequest :any
   :gcp.storage.v2/Storage.CreateHmacKeyOption :any
   :gcp.storage.v2/Storage.DeleteHmacKeyOption :any
   :gcp.storage.v2/Storage.GetHmacKeyOption :any
   :gcp.storage.v2/Storage.ListHmacKeysOption :any
   :gcp.storage.v2/Storage.PostPolicyV4Option :any
   :gcp.storage.v2/Storage.SignUrlOption :any
   :gcp.storage.v2/Storage.UpdateHmacKeyOption :any})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
