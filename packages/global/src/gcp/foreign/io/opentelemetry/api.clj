(ns gcp.foreign.io.opentelemetry.api
 {:gcp.dev/certification
   {:OpenTelemetry
      {:base-seed 1767754205782
       :passed-stages
         {:smoke 1767754205782 :standard 1767754205783 :stress 1767754205784}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "b187a017401ec0a34abb286e07cb0ecaa0f1bdbc4aab1008a239e028cd37028f"
       :timestamp "2026-01-07T02:50:05.837593893Z"}}}
  (:require
    [gcp.global :as g]
    [malli.core :as m])
  (:import
    (io.opentelemetry.api OpenTelemetry)))

(def registry
  ^{:gcp.global/name :gcp.foreign.io.opentelemetry.api/registry}
  {:gcp.foreign.io.opentelemetry.api/OpenTelemetry [:or {:gen/schema :string}
                                                    (g/instance-schema io.opentelemetry.api.OpenTelemetry)
                                                    :any]})

(g/include-schema-registry! registry)

(defn ^OpenTelemetry OpenTelemetry-from-edn [arg]
  (if (instance? OpenTelemetry arg)
    arg
    (OpenTelemetry/noop)))

(defn OpenTelemetry-to-edn [^OpenTelemetry arg]
  nil)