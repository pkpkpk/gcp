(ns gcp.storage.v2.synth
  (:require [gcp.global :as g]
            [gcp.storage.v2.StorageOptions]))

(def schemas
  {:gcp.storage.v2.synth/project              :string
   :gcp.storage.v2.synth/client               (assoc-in (g/instance-schema com.google.cloud.storage.Storage)
                                                        [1 :from-edn] 'gcp.storage.v2.StorageOptions/get-service)
   :gcp.storage.v2.synth/clientable           [:maybe
                                             [:or
                                              :gcp.storage.v2/StorageOptions
                                              :gcp.storage.v2.synth/client
                                              [:map [:storage [:or :gcp.storage.v2/StorageOptions :gcp.storage.v2.synth/client]]]]]
   :gcp.storage.v2.synth/StorageRetryStrategy :any
   :gcp.storage.v2.synth/BucketList           [:maybe
                                             [:map {:closed true}
                                              [:storage {:optional true} :gcp.storage.v2.synth/clientable]
                                              [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BucketListOption]]]]
   :gcp.storage.v2.synth/BucketGet            [:map {:closed true}
                                             [:storage {:optional true} :gcp.storage.v2.synth/clientable]
                                             [:bucket :string]
                                             [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BucketGetOption]]]
   :gcp.storage.v2.synth/BucketCreate         [:map {:closed true}
                                             [:storage {:optional true} :gcp.storage.v2.synth/clientable]
                                             [:bucketInfo :gcp.storage.v2/BucketInfo]
                                             [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BucketTargetOption]]]
   :gcp.storage.v2.synth/BlobList             [:map {:closed true}
                                             [:storage {:optional true} :gcp.storage.v2.synth/clientable]
                                             [:bucket :string]
                                             [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BlobListOption]]]
   :gcp.storage.v2.synth/BlobDelete           [:map {:closed true}
                                             [:storage {:optional true} :gcp.storage.v2.synth/clientable]
                                             [:blobs [:sequential :gcp.storage.v2/BlobId]]
                                             [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BlobSourceOption]]]
   :gcp.storage.v2.synth/BlobCreate           [:map {:closed true}
                                             [:storage {:optional true} :gcp.storage.v2.synth/clientable]
                                             [:blobInfo {:optional false} :gcp.storage.v2/BlobInfo]
                                             [:content {:optional true} 'bytes?]
                                             [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BlobSourceOption]]]
   :gcp.storage.v2.synth/ReadAllBytes         [:map {:closed true}
                                             [:storage {:optional true} :gcp.storage.v2.synth/clientable]
                                             [:blobId :gcp.storage.v2/BlobId]
                                             [:options {:optional true} [:sequential :gcp.storage.v2/Storage.BlobSourceOption]]]})

(g/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
