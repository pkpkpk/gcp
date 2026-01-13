(ns gcp.foreign.com.google.api.gax.grpc
  {:gcp.dev/certification
   {:GrpcStubCallableFactory
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767573074906
     :timestamp "2026-01-05T00:31:14.955467024Z"
     :passed-stages {:smoke 1767573074906
                     :standard 1767573074907
                     :stress 1767573074908}
     :source-hash "5439f9181597ed41029f6e309e059fe39849d7375795d496a02fb8ee6fc28ed1"}}}
  {:doc "Foreign bindings for com.google.api.gax.grpc"}
  (:require [gcp.global :as global])
  (:import (com.google.api.gax.grpc GrpcStubCallableFactory)))

(def registry
  (with-meta
    {:gcp.foreign.com.google.api.gax.grpc/GrpcStubCallableFactory
     [:or
      [:and {:gen/schema [:map [:id :string]]} (global/instance-schema com.google.api.gax.grpc.GrpcStubCallableFactory)]
      [:map [:id :string]]]}
    {:gcp.global/name :gcp.foreign.com.google.api.gax.grpc/registry}))

(global/include-schema-registry! registry)

(defn GrpcStubCallableFactory-to-edn [^GrpcStubCallableFactory arg]
  {:id (str arg)})

(defn GrpcStubCallableFactory-from-edn [arg]
  (let [id (:id arg)]
    (reify GrpcStubCallableFactory
      (createUnaryCallable [_ _ _ _] nil)
      (createPagedCallable [_ _ _ _] nil)
      (createBatchingCallable [_ _ _ _] nil)
      (createOperationCallable [_ _ _ _ _] nil)
      (createBidiStreamingCallable [_ _ _ _] nil)
      (createServerStreamingCallable [_ _ _ _] nil)
      (createClientStreamingCallable [_ _ _ _] nil)
      (toString [_] id))))
