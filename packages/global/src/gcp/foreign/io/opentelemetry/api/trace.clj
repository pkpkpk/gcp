(ns gcp.foreign.io.opentelemetry.api.trace
  (:import (io.opentelemetry.api.trace Tracer)))

(defn Tracer-from-edn [_] (throw (Exception. "unimplemented")))
(defn Tracer-to-edn [_] (throw (Exception. "unimplemented")))
