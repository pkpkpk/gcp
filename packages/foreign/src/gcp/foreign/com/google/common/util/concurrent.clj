(ns gcp.foreign.com.google.common.util.concurrent
  {:gcp.dev/certification
   {:ListenableFuture
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767571683082
     :timestamp "2026-01-05T00:08:03.144552701Z"
     :passed-stages {:smoke 1767571683082
                     :standard 1767571683083
                     :stress 1767571683084}
     :source-hash "bcb6a0a841cb8fc4af7ef062fdf74fd3518a28015746aa0ce120208caaf20fbe"}}}
  {:doc "Foreign bindings for com.google.common.util.concurrent"}
  (:require [gcp.global :as global])
  (:import (com.google.common.util.concurrent ListenableFuture)))

(def registry
  (with-meta
    {:gcp.foreign.com.google.common.util.concurrent/ListenableFuture
     [:or
      [:and {:gen/schema [:map [:val :string]]} (global/instance-schema com.google.common.util.concurrent.ListenableFuture)]
      [:map [:val :string]]]}
    {:gcp.global/name :gcp.foreign.com.google.common.util.concurrent/registry}))

(global/include-schema-registry! registry)

(defn ListenableFuture-to-edn [^ListenableFuture arg]
  {:val (.get arg)})

(defn ListenableFuture-from-edn [arg]
  (let [v (:val arg)]
    (reify ListenableFuture
      (get [_] v)
      (get [_ _ _] v)
      (addListener [_ _ _] nil)
      (cancel [_ _] false)
      (isCancelled [_] false)
      (isDone [_] true))))
