(ns gcp.storage.aux
  (:require [clojure.java.io :as io]
            [gcp.storage :as storage]
            [jsonista.core :as j])
  (:import (java.io ByteArrayInputStream ByteArrayOutputStream)
           (java.util.zip GZIPInputStream ZipException)))

(defn read-gzip-bytes [bucket file]
  (let [bytes (storage/read-blob bucket file)]
    (with-open [in  (GZIPInputStream. (ByteArrayInputStream. bytes))
                out (ByteArrayOutputStream.)]
      (io/copy in out)
      (.toByteArray out))))

(defn read-jsonl [bucket file]
  (let [blob (storage/read-blob bucket file)]
    (try
      (with-open [in  (GZIPInputStream. (ByteArrayInputStream. blob))
                  out (ByteArrayOutputStream.)]
        (io/copy in out)
        (j/read-values (.toByteArray out) j/keyword-keys-object-mapper))
      (catch ZipException _
        (j/read-values blob j/keyword-keys-object-mapper)))))

(defn read-gzipped-jsonl [bucket file]
  (j/read-values (read-gzip-bytes bucket file) j/keyword-keys-object-mapper))