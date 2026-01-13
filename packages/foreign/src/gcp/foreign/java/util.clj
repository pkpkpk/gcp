(ns gcp.foreign.java.util
 {:gcp.dev/certification
   {:OptionalLong
      {:base-seed 1767751884688
       :passed-stages
         {:smoke 1767751884688 :standard 1767751884689 :stress 1767751884690}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "ec9d06b2bbe7e17e4a7ac51ffc2db26ceec34aa2b2309178e26a10fb2c0dd30c"
       :timestamp "2026-01-07T02:11:24.734030705Z"}}}
  (:require
    [gcp.global :as g]
    [malli.core :as m])
  (:import
    (java.util OptionalLong)))

(def registry
  ^{:gcp.global/name :gcp.foreign.java.util/registry}
  {:gcp.foreign.java.util/OptionalLong [:maybe :int]})

(g/include-schema-registry! registry)

(defn ^OptionalLong OptionalLong-from-edn [arg]
  (if (some? arg)
    (OptionalLong/of arg)
    (OptionalLong/empty)))

(defn OptionalLong-to-edn [^OptionalLong arg]
  (if (.isPresent arg)
    (.getAsLong arg)
    nil))