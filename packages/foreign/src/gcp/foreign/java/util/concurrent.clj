(ns gcp.foreign.java.util.concurrent
 {:gcp.dev/certification
   {:CompletableFuture
      {:base-seed 1767752607039
       :passed-stages
         {:smoke 1767752607039 :standard 1767752607040 :stress 1767752607041}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "0706ae7756b0d0d563250337c63cd326f6c0a7274884c1ddffd9583bdd448a7a"
       :timestamp "2026-01-07T02:23:27.129036731Z"}
    :TimeUnit
      {:base-seed 1767752607130
       :passed-stages
         {:smoke 1767752607130 :standard 1767752607131 :stress 1767752607132}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "0706ae7756b0d0d563250337c63cd326f6c0a7274884c1ddffd9583bdd448a7a"
       :timestamp "2026-01-07T02:23:27.139702891Z"}}}
  (:require [gcp.global :as g])
  (:import (java.util.concurrent CompletableFuture TimeUnit)))

(def registry
  ^{:gcp.global/name :gcp.foreign.java.util.concurrent/registry}
  {:gcp.foreign.java.util.concurrent/CompletableFuture [:any]
   :gcp.foreign.java.util.concurrent/TimeUnit [:enum :NANOSECONDS :MICROSECONDS :MILLISECONDS :SECONDS :MINUTES :HOURS :DAYS]})

(g/include-schema-registry! registry)

(defn ^CompletableFuture CompletableFuture-from-edn [arg]
  (CompletableFuture/completedFuture arg))

(defn CompletableFuture-to-edn [^CompletableFuture arg]
  (try (.getNow arg nil) (catch Exception _ nil)))

(defn ^TimeUnit TimeUnit-from-edn [arg]
  (TimeUnit/valueOf (name arg)))

(defn TimeUnit-to-edn [^TimeUnit arg]
  (keyword (.name arg)))