(ns gcp.storage.aux
  (:require [clojure.java.io :as io]
            [gcp.storage :as storage]
            [jsonista.core :as j]))

(defn read-gzip-bytes [bucket file]
  (let [bytes (storage/read-all-bytes bucket file)]
    (with-open [in  (java.util.zip.GZIPInputStream. (java.io.ByteArrayInputStream. bytes))
                out (java.io.ByteArrayOutputStream.)]
      (io/copy in out)
      (.toByteArray out))))

(defn read-jsonl [bucket file]
  (j/read-values (storage/read-all-bytes bucket file) j/keyword-keys-object-mapper))

(defn read-gzipped-jsonl [bucket file]
  (j/read-values (read-gzip-bytes bucket file) j/keyword-keys-object-mapper))
