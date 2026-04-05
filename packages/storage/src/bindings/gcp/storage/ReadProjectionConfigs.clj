;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.storage.ReadProjectionConfigs
  {:doc
     "Factory class to select {@link ReadProjectionConfig}s.\n\n<p>There are multiple projections which can be used to access the content of a {@link BlobInfo}\nin Google Cloud Storage.\n\n@see Storage#blobReadSession(BlobId, BlobSourceOption...)\n@see BlobReadSession\n@see ReadProjectionConfig\n@since 2.51.0 This new api is in preview and is subject to breaking changes."
   :file-git-sha "c8576194dfc555975414e177a66c6f90ae74ac0d"
   :fqcn "com.google.cloud.storage.ReadProjectionConfigs"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "215ec381-0f5f-5884-ab6d-eb0bb246cd16"
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :reason :write-only
      :skipped true
      :timestamp "2026-04-04T03:02:38.381681536Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.storage ReadProjectionConfigs
            ReadProjectionConfigs$BaseConfig]))

(declare from-edn to-edn BaseConfig-from-edn BaseConfig-to-edn)

(defn ^ReadProjectionConfigs$BaseConfig BaseConfig-from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.storage.ReadProjectionConfigs.BaseConfig is read-only")))

(defn BaseConfig-to-edn
  [^ReadProjectionConfigs$BaseConfig arg]
  (when arg (cond-> {})))

(def BaseConfig-schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :nested/read-only,
    :gcp/key :gcp.storage/ReadProjectionConfigs.BaseConfig}])

(defn ^ReadProjectionConfigs from-edn
  [arg]
  (global/strict! :gcp.storage/ReadProjectionConfigs arg)
  (case (first (keys arg))
    :asChannel (ReadProjectionConfigs/asChannel)
    :asFutureBytes (ReadProjectionConfigs/asFutureBytes)
    :asFutureByteString (ReadProjectionConfigs/asFutureByteString)
    :asSeekableChannel (ReadProjectionConfigs/asSeekableChannel)))

(def schema
  [:or
   {:closed true,
    :doc
      "Factory class to select {@link ReadProjectionConfig}s.\n\n<p>There are multiple projections which can be used to access the content of a {@link BlobInfo}\nin Google Cloud Storage.\n\n@see Storage#blobReadSession(BlobId, BlobSourceOption...)\n@see BlobReadSession\n@see ReadProjectionConfig\n@since 2.51.0 This new api is in preview and is subject to breaking changes.",
    :gcp/category :factory,
    :gcp/key :gcp.storage/ReadProjectionConfigs}
   [:map
    {:closed true,
     :doc
       "Read a range as a non-blocking {@link ScatteringByteChannel}.\n\n<p>The returned channel will be non-blocking for all read calls. If bytes have not yet\nasynchronously been delivered from Google Cloud Storage the method will return rather than\nwaiting for the bytes to arrive.\n\n<p>The resulting {@link ScatteringByteChannel} MUST be {@link ScatteringByteChannel#close()\nclose()}ed to avoid leaking memory\n\n@see ReadAsChannel\n@see ScatteringByteChannel\n@since 2.51.0 This new api is in preview and is subject to breaking changes."}
    [:asChannel :nil]]
   [:map
    {:closed true,
     :doc
       "Read a range of {@code byte}s as an {@link ApiFuture}{@code <byte[]>}\n\n<p>The entire range will be accumulated in memory before the future will resolve.\n\n<p>If you do not want the entire range accumulated in memory, please use one of the other\n{@link ReadProjectionConfig}s available.\n\n@see ApiFuture\n@since 2.51.0 This new api is in preview and is subject to breaking changes."}
    [:asFutureBytes :nil]]
   [:map
    {:closed true,
     :doc
       "Read a range of {@code byte}s as an {@link ApiFuture}{@code <}{@link\nDisposableByteString}{@code >}\n\n<p>The resulting {@link DisposableByteString} MUST be {@link DisposableByteString#close()\nclose()}ed to avoid leaking memory\n\n<p>The entire range will be accumulated in memory before the future will resolve.\n\n<p>If you do not want the entire range accumulated in memory, please use one of the other\n{@link ReadProjectionConfig}s available.\n\n@see ApiFuture\n@see com.google.protobuf.ByteString\n@since 2.51.0 This new api is in preview and is subject to breaking changes."}
    [:asFutureByteString :nil]]
   [:map
    {:closed true,
     :doc
       "Read from the object as a {@link SeekableByteChannel}\n\n<p>The returned channel will be non-blocking for all read calls. If bytes have not yet\nasynchronously been delivered from Google Cloud Storage the method will return rather than\nwaiting for the bytes to arrive.\n\n<p>The resulting {@link SeekableByteChannel} MUST be {@link SeekableByteChannel#close()\nclose()}ed to avoid leaking memory\n\n@see SeekableByteChannel\n@since 2.51.0 This new api is in preview and is subject to breaking changes."}
    [:asSeekableChannel :nil]]])

(global/include-schema-registry!
  (with-meta {:gcp.storage/ReadProjectionConfigs schema,
              :gcp.storage/ReadProjectionConfigs.BaseConfig BaseConfig-schema}
    {:gcp.global/name "gcp.storage.ReadProjectionConfigs"}))