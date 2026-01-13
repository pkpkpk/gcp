(ns gcp.foreign.com.google.api.gax.httpjson.longrunning.stub
  {:gcp.dev/certification
   {:HttpJsonOperationsStub
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767573501887
     :timestamp "2026-01-05T00:38:21.893422525Z"
     :passed-stages {:smoke 1767573501887
                     :standard 1767573501888
                     :stress 1767573501889}
     :source-hash "c0b22b791826106c4d1c66c627cfe7d6185aa93c5ad2291b5e80785be3eff817"}
    :OperationsStub
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767573501894
     :timestamp "2026-01-05T00:38:21.941770784Z"
     :passed-stages {:smoke 1767573501894
                     :standard 1767573501895
                     :stress 1767573501896}
     :source-hash "c0b22b791826106c4d1c66c627cfe7d6185aa93c5ad2291b5e80785be3eff817"}}}
  {:doc "Foreign bindings for com.google.api.gax.httpjson.longrunning.stub"}
  (:require [gcp.global :as global])
  (:import (com.google.api.gax.httpjson.longrunning.stub HttpJsonOperationsStub OperationsStub)))

(def registry
  (with-meta
    (let [os-sk :gcp.foreign.com.google.api.gax.httpjson.longrunning.stub/OperationsStub
          hjos-sk :gcp.foreign.com.google.api.gax.httpjson.longrunning.stub/HttpJsonOperationsStub]
      {os-sk [:or
              [:and {:gen/schema [:map [:id :string]]} (global/instance-schema com.google.api.gax.httpjson.longrunning.stub.OperationsStub)]
              [:map [:id :string]]]
       hjos-sk [:map [:id :string]]})
    {:gcp.global/name :gcp.foreign.com.google.api.gax.httpjson.longrunning.stub/registry}))

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
      (close [] nil)
      (shutdown [] nil)
      (shutdownNow [] nil)
      (isShutdown [] true)
      (isTerminated [] true)
      (awaitTermination [_ _] true)
      (toString [] id))))

#!-----------------------------------------------------------------------------

(defn HttpJsonOperationsStub-to-edn [arg]
  (if (instance? HttpJsonOperationsStub arg)
    {:id (str arg)}
    arg))

(defn HttpJsonOperationsStub-from-edn [arg]
  arg)
