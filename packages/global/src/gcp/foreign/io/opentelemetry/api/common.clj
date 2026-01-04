(ns gcp.foreign.io.opentelemetry.api.common
  (:import (io.opentelemetry.api.common Attributes)))

(defn Attributes-from-edn [_] (throw (Exception. "unimplemented")))
(defn Attributes-to-edn [_] (throw (Exception. "unimplemented")))
