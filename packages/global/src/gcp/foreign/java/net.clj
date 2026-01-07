(ns gcp.foreign.java.net
 {:gcp.dev/certification
   {:URL {:base-seed 1767752115685
          :passed-stages
            {:smoke 1767752115685 :standard 1767752115686 :stress 1767752115687}
          :protocol-hash
            "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
          :source-hash
            "95c7c2343539b33cf362db9036998cc8f170602248aa7c0c450f49d69d4ce13f"
          :timestamp "2026-01-07T02:15:15.729403170Z"}}}
  (:require
    [gcp.global :as g]
    [malli.core :as m])
  (:import
    (java.net URL)))

(def registry
  ^{:gcp.global/name :gcp.foreign.java.net/registry}
  {:gcp.foreign.java.net/URL [:string {:gen/fmap '(fn [_] "https://google.com")} ]})

(g/include-schema-registry! registry)

(defn ^URL URL-from-edn [arg]
  (URL. arg))

(defn URL-to-edn [^URL arg]
  (.toString arg))