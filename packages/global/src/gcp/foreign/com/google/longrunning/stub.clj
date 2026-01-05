(ns gcp.foreign.com.google.longrunning.stub
  {:gcp.dev/certification
   {:GrpcOperationsStub
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767573250061
     :timestamp "2026-01-05T00:34:10.067781170Z"
     :passed-stages {:smoke 1767573250061
                     :standard 1767573250062
                     :stress 1767573250063}
     :source-hash "ec634f1f49c36f73d8ced054290b92220c2645e0cc7df6bd4054184038adb474"}
    :OperationsStub
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767573250068
     :timestamp "2026-01-05T00:34:10.112311837Z"
     :passed-stages {:smoke 1767573250068
                     :standard 1767573250069
                     :stress 1767573250070}
     :source-hash "ec634f1f49c36f73d8ced054290b92220c2645e0cc7df6bd4054184038adb474"}}}
  {:doc "Foreign bindings for com.google.longrunning.stub"}
  (:require [gcp.global :as global])
  (:import (com.google.longrunning.stub GrpcOperationsStub OperationsStub)))

(def registry
  (with-meta
    (let [os-sk :gcp.foreign.com.google.longrunning.stub/OperationsStub
          gos-sk :gcp.foreign.com.google.longrunning.stub/GrpcOperationsStub]
      {os-sk [:or
              [:and {:gen/schema [:map [:id :string]]} (global/instance-schema com.google.longrunning.stub.OperationsStub)]
              [:map [:id :string]]]
       gos-sk [:map [:id :string]]})
    {:gcp.global/name :gcp.foreign.com.google.longrunning.stub/registry}))

(global/include-schema-registry! registry)

#!-----------------------------------------------------------------------------

(defn OperationsStub-to-edn [^OperationsStub arg]
  {:id (str arg)})

(defn OperationsStub-from-edn [arg]
  (let [id (:id arg)]
    (proxy [OperationsStub] []
      (getOperationCallable [] nil)
      (listOperationsCallable [] nil)
      (listOperationsPagedCallable [] nil)
      (cancelOperationCallable [] nil)
      (deleteOperationCallable [] nil)
      (waitOperationCallable [] nil)
      (close [] nil)
      (shutdown [] nil)
      (shutdownNow [] nil)
      (isShutdown [] true)
      (isTerminated [] true)
      (awaitTermination [_ _] true)
      (toString [] id))))

#!-----------------------------------------------------------------------------

(defn GrpcOperationsStub-to-edn [arg]
  (if (instance? GrpcOperationsStub arg)
    {:id (str arg)}
    arg))

(defn GrpcOperationsStub-from-edn [arg]
  arg)
