(ns gcp.storage.v2.Cors
  (:require [gcp.global :as global])
  (:import (com.google.cloud.storage Cors)))

(defn ^Cors from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^Cors arg] (throw (Exception. "unimplemented")))

(global/include-schema-registry! (with-meta {:gcp.storage.v2/Cors :any
                                     :gcp.storage.v2/Cors.Origin :any}
                                    {:gcp.global/name (str *ns*)}))