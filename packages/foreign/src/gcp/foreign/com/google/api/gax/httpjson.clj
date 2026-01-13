(ns gcp.foreign.com.google.api.gax.httpjson
  {:gcp.dev/certification
   {:HttpJsonStubCallableFactory
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767573136042
     :timestamp "2026-01-05T00:32:16.092711444Z"
     :passed-stages {:smoke 1767573136042
                     :standard 1767573136043
                     :stress 1767573136044}
     :source-hash "6baa422f40ec128a3e43d44193478b14b4b0b3845fb48fb261e110378a303d60"}}}
  {:doc "Foreign bindings for com.google.api.gax.httpjson"}
  (:require [gcp.global :as global])
  (:import (com.google.api.gax.httpjson HttpJsonStubCallableFactory)))

(def registry
  (with-meta
    {:gcp.foreign.com.google.api.gax.httpjson/HttpJsonStubCallableFactory
     [:or
      [:and {:gen/schema [:map [:id :string]]} (global/instance-schema com.google.api.gax.httpjson.HttpJsonStubCallableFactory)]
      [:map [:id :string]]]}
    {:gcp.global/name :gcp.foreign.com.google.api.gax.httpjson/registry}))

(global/include-schema-registry! registry)

(defn HttpJsonStubCallableFactory-to-edn [^HttpJsonStubCallableFactory arg]
  {:id (str arg)})

(defn HttpJsonStubCallableFactory-from-edn [arg]
  (let [id (:id arg)]
    (reify HttpJsonStubCallableFactory
      (createUnaryCallable [_ _ _ _] nil)
      (createPagedCallable [_ _ _ _] nil)
      (createBatchingCallable [_ _ _ _] nil)
      (createOperationCallable [_ _ _ _ _] nil)
      (toString [_] id))))
