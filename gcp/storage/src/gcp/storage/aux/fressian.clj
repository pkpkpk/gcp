(ns gcp.storage.aux.fressian
  (:require [clojure.data.fressian :as fressian]
            [gcp.storage :as storage])
  (:import [java.io Closeable OutputStream]
           [java.nio ByteBuffer]
           [com.google.cloud WriteChannel]
           [com.google.cloud.storage BlobId BlobInfo Storage$BlobWriteOption]
           [com.google.common.hash Hashing Hasher]
           (org.fressian FressianWriter)))

;https://cloud.google.com/java/docs/reference/google-cloud-storage/latest/com.google.cloud.storage.BlobWriteSession

;https://github.com/clojure/data.fressian/blob/master/src/main/clojure/clojure/data/fressian.clj

(defn ^WriteChannel create-write-channel
  "Creates a blob and returns a channel for writing its content.
   By default any MD5 and CRC32C values in the given blobInfo are ignored unless requested via the
   BlobWriteOption.md5Match and BlobWriteOption.crc32cMatch options."
  ;;https://cloud.google.com/java/docs/reference/google-cloud-core/latest/com.google.cloud.WriteChannel
  ;;https://cloud.google.com/java/docs/reference/google-cloud-storage/latest/com.google.cloud.storage.Storage.BlobWriteOption
  [bucket-name object-name]
  (let [blob-id (BlobId/of bucket-name object-name)
        blob-info (.build (BlobInfo/newBuilder blob-id))
        opts [(Storage$BlobWriteOption/doesNotExist)
              (Storage$BlobWriteOption/disableGzipContent)]]
    (.writer (storage/client) blob-info (into-array Storage$BlobWriteOption opts))))

(defn write-channel-hashing-proxy
  [^WriteChannel write-channel
   ^Hasher crc32c-hasher]
  (proxy [OutputStream] []
    (write
      ([b]
       (if (number? b)
         (let [byte (byte (if (< b 128) b (- b 256)))]
           (.putByte crc32c-hasher byte)
           (.write write-channel (ByteBuffer/wrap (byte-array [byte]))))
         (do
           #_(assert (instance? (Class/forName "[B") b))
           (.putBytes crc32c-hasher ^bytes b)
           (.write write-channel (ByteBuffer/wrap b)))))
      ([b off len]
       (let [bb (ByteBuffer/wrap b off len)]
         (.putBytes crc32c-hasher bb)
         (.write write-channel bb))))
    (close []
      (.close write-channel))))

(defn- get-remote-crc32c
  [client bucket-name object-name]
  (let [blob (.get client (BlobId/of bucket-name object-name))]
    (when blob
      (.getCrc32cToHexString blob))))

(defprotocol IFressianBlobStream
  (write-object [this object])
  (begin-open-list [this])
  (begin-closed-list [this])
  (end-list [this])
  (get-hash [this])
  (write-footer [this])
  (finish-stream [this]))

(defrecord FressianBlobStream
  [client
   bucket-name
   blob-name
   ^WriteChannel blob-write-channel
   ^FressianWriter fressian-writer
   ^Hasher crc32c-hasher]
  IFressianBlobStream
  (get-hash [_]
    (Integer/toHexString (.asInt (.hash crc32c-hasher))))
  (write-object [_ object]
    (fressian/write-object fressian-writer object))
  (begin-open-list [_]
    (fressian/begin-open-list fressian-writer))
  (begin-closed-list [_]
    (fressian/begin-closed-list fressian-writer))
  (end-list [_]
    (fressian/end-list fressian-writer))
  (write-footer [_]
    (fressian/write-footer fressian-writer))
  (finish-stream [this]
    (.close this)
    (let [local-hash (get-hash this)]
      (if-let [remote-hash (get-remote-crc32c client bucket-name blob-name)]
        (when (not= local-hash remote-hash)
          (let [msg (str "Remote hash does not match for " bucket-name "/" blob-name " try again!")]
            (throw (Exception. ^String msg))))
        (let [msg (str "Could not find blob to retrieve remote hash from: " bucket-name "/" blob-name)]
          (throw (Exception. ^String msg))))))
  Closeable
  (close [_]
    (.close fressian-writer)
    (.close blob-write-channel)))

(defn create-fressian-blob-stream
  [bucket-name blob-name]
  (let [write-channel (create-write-channel bucket-name blob-name)
        hasher (.newHasher (Hashing/crc32c))
        hashing-proxy (write-channel-hashing-proxy write-channel hasher)
        fressian-writer (fressian/create-writer hashing-proxy)]
    (->FressianBlobStream (storage/client)
                          bucket-name
                          blob-name write-channel fressian-writer hasher)))

(defn write-fressian-blob
  [bucket-name blob-name object]
  (let [bytes (.array (fressian/write object))]
    (storage/create-blob bucket-name blob-name bytes)))

(defn read-fressian-blob
  [bucket-name blob-name]
  (fressian/read (storage/read-all-bytes bucket-name blob-name)))
