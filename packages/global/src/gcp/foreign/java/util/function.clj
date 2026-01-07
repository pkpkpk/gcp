(ns gcp.foreign.java.util.function
 {:gcp.dev/certification
   {:Consumer
      {:base-seed 1767752648472
       :passed-stages
         {:smoke 1767752648472 :standard 1767752648473 :stress 1767752648474}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "fd9b99c1baa17b36067335475485859696f8e577fa262fd43cfbdd65f58d4377"
       :timestamp "2026-01-07T02:24:08.473625272Z"}}}
  (:require [gcp.global :as g])
  (:import (java.util.function Consumer)))

(def registry
  ^{:gcp.global/name :gcp.foreign.java.util.function/registry}
  {:gcp.foreign.java.util.function/Consumer [:nil]})

(g/include-schema-registry! registry)

(defn ^Consumer Consumer-from-edn [_]
  (reify Consumer (accept [_ _])))

(defn Consumer-to-edn [_] nil)