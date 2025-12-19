(ns gcp.storage.v2.BlobWriteSessionConfig
  (:require [gcp.global :as global])
  (:import (com.google.cloud.storage BlobWriteSessionConfig)))

(defn to-edn [^BlobWriteSessionConfig arg] (throw (Exception. "unimplemented")))

(defn ^BlobWriteSessionConfig from-edn [arg] (throw (Exception. "unimplemented")))

(global/register-schema! :gcp.storage.v2/BlobWriteSessionConfig :any)
