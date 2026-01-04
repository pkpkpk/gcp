(ns gcp.foreign.io.opentelemetry.api
  (:import (io.opentelemetry.api OpenTelemetry)))

(defn OpenTelemetry-from-edn [_] (throw (Exception. "unimplemented")))
(defn OpenTelemetry-to-edn [_] (throw (Exception. "unimplemented")))
