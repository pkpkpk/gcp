(ns gcp.foreign.com.google.api.gax.longrunning
  {:gcp.dev/certification
   {:OperationFuture
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767571630432
     :timestamp "2026-01-05T00:07:10.482429541Z"
     :passed-stages {:smoke 1767571630432
                     :standard 1767571630433
                     :stress 1767571630434}
     :source-hash "23b51bc04f1e96e62841106d7c45f3c486cd8e51c643b1bc712015a5bf6e9dde"}}}
  (:require [gcp.global :as global])
  (:import (com.google.api.gax.longrunning OperationFuture)))

(def registry
  (with-meta
    (let [sk :gcp.foreign.com.google.api.gax.longrunning/OperationFuture]
      {sk [:or
           [:and {:gen/schema [:map [:name :string]]} (global/instance-schema com.google.api.gax.longrunning.OperationFuture)]
           [:map [:name :string]]]})
    {:gcp.global/name :gcp.foreign.com.google.api.gax.longrunning/registry}))

(global/include-schema-registry! registry)

(defn OperationFuture-to-edn [^OperationFuture arg]
  {:name (.getName arg)})

(defn OperationFuture-from-edn [arg]
  (let [n (:name arg)]
    (reify OperationFuture
      (getName [_] n)
      (getMetadata [_] (throw (UnsupportedOperationException.)))
      (peekMetadata [_] (throw (UnsupportedOperationException.)))
      (getInitialFuture [_] (throw (UnsupportedOperationException.)))
      (getPollingFuture [_] (throw (UnsupportedOperationException.)))
      (cancel [_ _] false)
      (isCancelled [_] false)
      (isDone [_] true)
      (get [_] nil)
      (get [_ _ _] nil)
      (addListener [_ _ _] nil))))
