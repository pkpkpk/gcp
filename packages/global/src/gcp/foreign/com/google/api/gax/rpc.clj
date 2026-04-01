(ns gcp.foreign.com.google.api.gax.rpc
  {:gcp.dev/certification
   {:BidiStream
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767572959426
     :timestamp "2026-01-05T00:29:19.437365269Z"
     :passed-stages {:smoke 1767572959426
                     :standard 1767572959427
                     :stress 1767572959428}
     :source-hash "c834290612a668d02fb0d5aa13493b1c35850654a9bbaac50105b60cd901eae0"}
    :ClientContext
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767572959437
     :timestamp "2026-01-05T00:29:19.442126669Z"
     :passed-stages {:smoke 1767572959437
                     :standard 1767572959438
                     :stress 1767572959439}
     :source-hash "c834290612a668d02fb0d5aa13493b1c35850654a9bbaac50105b60cd901eae0"}}}
  {:doc "Foreign bindings for com.google.api.gax.rpc"}
  (:require [gcp.global :as global])
  (:import (com.google.api.gax.rpc BidiStream ClientContext)))

(def registry
  (with-meta
    (let [bs-sk :gcp.foreign.com.google.api.gax.rpc/BidiStream
          cc-sk :gcp.foreign.com.google.api.gax.rpc/ClientContext]
      {bs-sk [:map [:id :string]]
       cc-sk [:map [:endpoint :string]]})
    {:gcp.global/name :gcp.foreign.com.google.api.gax.rpc/registry}))

(global/include-schema-registry! registry)

#!-----------------------------------------------------------------------------

(defn BidiStream-to-edn [arg]
  {:id (str arg)})

(defn BidiStream-from-edn [arg]
  arg)

#!-----------------------------------------------------------------------------

(defn ClientContext-to-edn [arg]
  (if (instance? ClientContext arg)
    {:endpoint (.getEndpoint ^ClientContext arg)}
    arg))

(defn ClientContext-from-edn [arg]
  arg)
