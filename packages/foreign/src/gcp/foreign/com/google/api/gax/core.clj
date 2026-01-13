(ns gcp.foreign.com.google.api.gax.core
  {:gcp.dev/certification
   {:BackgroundResource
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767572823198
     :timestamp "2026-01-05T00:27:03.254735815Z"
     :passed-stages {:smoke 1767572823198
                     :standard 1767572823199
                     :stress 1767572823200}
     :source-hash "78cb47235a018c2a0165dd16ee4b2265e0f481d5713cf7538a1304ae22da9de1"}}}
  {:doc "Foreign bindings for com.google.api.gax.core"}
  (:require [gcp.global :as global])
  (:import (com.google.api.gax.core BackgroundResource)))

(def registry
  (with-meta
    {:gcp.foreign.com.google.api.gax.core/BackgroundResource
     [:or
      [:and {:gen/schema [:map [:id :string]]} (global/instance-schema com.google.api.gax.core.BackgroundResource)]
      [:map [:id :string]]]}
    {:gcp.global/name :gcp.foreign.com.google.api.gax.core/registry}))

(global/include-schema-registry! registry)

(defn BackgroundResource-to-edn [^BackgroundResource arg]
  {:id (str arg)})

(defn BackgroundResource-from-edn [arg]
  (let [id (:id arg)]
    (reify BackgroundResource
      (shutdown [_] nil)
      (shutdownNow [_] nil)
      (isShutdown [_] true)
      (isTerminated [_] true)
      (awaitTermination [_ _ _] true)
      (close [_] nil)
      (toString [_] id))))
