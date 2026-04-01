(ns gcp.foreign.com.google.cloud.spi
  {:gcp.dev/certification
   {:ServiceRpcFactory
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767573303573
     :timestamp "2026-01-05T00:35:03.630764088Z"
     :passed-stages {:smoke 1767573303573
                     :standard 1767573303574
                     :stress 1767573303575}
     :source-hash "1a8b4e3aeab11b11ba0442ca38b6094bc589e8cf93f9669b04ba818b82d5447e"}}}
  {:doc "Foreign bindings for com.google.cloud.spi"}
  (:require [gcp.global :as global])
  (:import (com.google.cloud.spi ServiceRpcFactory)))

(def registry
  (with-meta
    {:gcp.foreign.com.google.cloud.spi/ServiceRpcFactory
     [:or
      [:and {:gen/schema [:map [:id :string]]} (global/instance-schema com.google.cloud.spi.ServiceRpcFactory)]
      [:map [:id :string]]]}
    {:gcp.global/name :gcp.foreign.com.google.cloud.spi/registry}))

(global/include-schema-registry! registry)

(defn ServiceRpcFactory-to-edn [^ServiceRpcFactory arg]
  {:id (str arg)})

(defn ServiceRpcFactory-from-edn [arg]
  (let [id (:id arg)]
    (reify ServiceRpcFactory
      (create [_ _] nil)
      (toString [_] id))))
