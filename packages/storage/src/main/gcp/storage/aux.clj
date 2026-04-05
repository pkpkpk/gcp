(ns gcp.storage.aux
  (:require [clojure.java.io :as io]
            [gcp.storage :as storage]
            [jsonista.core :as j])
  (:import (java.io ByteArrayInputStream ByteArrayOutputStream)
           (java.util.zip GZIPInputStream)))

(defn read-gzip-bytes [bucket file]
  (let [bytes (storage/read-blob bucket file)]
    (with-open [in  (GZIPInputStream. (ByteArrayInputStream. bytes))
                out (ByteArrayOutputStream.)]
      (io/copy in out)
      (.toByteArray out))))

(defn read-jsonl [bucket file]
  (j/read-values (storage/read-blob bucket file) j/keyword-keys-object-mapper))

(defn read-gzipped-jsonl [bucket file]
  (j/read-values (read-gzip-bytes bucket file) j/keyword-keys-object-mapper))