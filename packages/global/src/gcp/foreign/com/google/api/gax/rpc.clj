(ns gcp.foreign.com.google.api.gax.rpc
  (:import (com.google.api.gax.rpc BidiStream ClientContext)))

(defn BidiStream-from-edn [_] (throw (Exception. "unimplemented")))
(defn BidiStream-to-edn [_] (throw (Exception. "unimplemented")))
(defn ClientContext-from-edn [_] (throw (Exception. "unimplemented")))
(defn ClientContext-to-edn [_] (throw (Exception. "unimplemented")))
