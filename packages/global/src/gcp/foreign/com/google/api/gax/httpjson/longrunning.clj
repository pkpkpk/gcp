(ns gcp.foreign.com.google.api.gax.httpjson.longrunning
  {:gcp.dev/certification
   {:OperationsClient
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767573428416
     :timestamp "2026-01-05T00:37:08.426347571Z"
     :passed-stages {:smoke 1767573428416
                     :standard 1767573428417
                     :stress 1767573428418}
     :source-hash "83da420abd8b6909b64f54e1672edc426e4815d268cd6669e5b4ac384a5bbd5e"}}}
  {:doc "Foreign bindings for com.google.api.gax.httpjson.longrunning"}
  (:require [gcp.global :as global])
  (:import (com.google.api.gax.httpjson.longrunning OperationsClient)))

(def registry
  (with-meta
    {:gcp.foreign.com.google.api.gax.httpjson.longrunning/OperationsClient [:map [:id :string]]}
    {:gcp.global/name :gcp.foreign.com.google.api.gax.httpjson.longrunning/registry}))

(global/include-schema-registry! registry)

(defn OperationsClient-to-edn [arg]
  (if (instance? OperationsClient arg)
    {:id (str arg)}
    arg))

(defn OperationsClient-from-edn [arg]
  arg)
