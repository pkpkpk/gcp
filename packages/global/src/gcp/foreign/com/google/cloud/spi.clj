(ns gcp.foreign.com.google.cloud.spi
  (:import (com.google.cloud.spi ServiceRpcFactory)))

(defn ServiceRpcFactory-from-edn [_] (throw (Exception. "unimplemented")))
(defn ServiceRpcFactory-to-edn [_] (throw (Exception. "unimplemented")))
