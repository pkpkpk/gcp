(ns gcp.foreign.io.opentelemetry.api.trace
 {:gcp.dev/certification
   {:Tracer
      {:base-seed 1767754205858
       :passed-stages
         {:smoke 1767754205858 :standard 1767754205859 :stress 1767754205860}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "18a09acffab9dbaed9b0aab3ed6df6334efe6cf29c2fb2f42811f62a9fae7190"
       :timestamp "2026-01-07T02:50:05.915563969Z"}}}
  (:require
    [gcp.global :as g]
    [malli.core :as m])
  (:import
    (io.opentelemetry.api OpenTelemetry)
    (io.opentelemetry.api.trace Tracer)))

(def registry
  ^{:gcp.global/name :gcp.foreign.io.opentelemetry.api.trace/registry}
  {:gcp.foreign.io.opentelemetry.api.trace/Tracer [:or {:gen/schema :string}
                                                   (g/instance-schema io.opentelemetry.api.trace.Tracer)
                                                   :any]})

(g/include-schema-registry! registry)

(defn ^Tracer Tracer-from-edn [arg]
  (if (instance? Tracer arg)
    arg
    (.getTracer (OpenTelemetry/noop) "noop")))

(defn Tracer-to-edn [^Tracer arg]
  nil)