(ns gcp.foreign.com.google.api.core
  {:gcp.dev/certification
   {:AbstractApiService
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767572767973
     :timestamp "2026-01-05T00:26:08.047526470Z"
     :passed-stages {:smoke 1767572767973
                     :standard 1767572767974
                     :stress 1767572767975}
     :source-hash "52c4e422b2ff6a75c5d770d23cdf1a8254ae4213e5d42d0d318e7f3a48d5902b"}
    :ApiFuture
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767572768050
     :timestamp "2026-01-05T00:26:08.130961721Z"
     :passed-stages {:smoke 1767572768050
                     :standard 1767572768051
                     :stress 1767572768052}
     :source-hash "52c4e422b2ff6a75c5d770d23cdf1a8254ae4213e5d42d0d318e7f3a48d5902b"}
    :ApiService
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767572768132
     :timestamp "2026-01-05T00:26:08.194933846Z"
     :passed-stages {:smoke 1767572768132
                     :standard 1767572768133
                     :stress 1767572768134}
     :source-hash "52c4e422b2ff6a75c5d770d23cdf1a8254ae4213e5d42d0d318e7f3a48d5902b"}}}
  {:doc "Foreign bindings for com.google.api.core"}
  (:require [gcp.global :as global])
  (:import (com.google.api.core AbstractApiService ApiFuture ApiService ApiService$State)))

(def registry
  (with-meta
    (let [f-sk :gcp.foreign.com.google.api.core/ApiFuture
          s-sk :gcp.foreign.com.google.api.core/ApiService
          as-sk :gcp.foreign.com.google.api.core/AbstractApiService]
      {f-sk [:or
             [:and {:gen/schema [:map [:val :string]]} (global/instance-schema com.google.api.core.ApiFuture)]
             [:map [:val :string]]]
       s-sk [:or
             [:and {:gen/schema [:map [:state [:enum :NEW :STARTING :RUNNING :STOPPING :TERMINATED :FAILED]]]} (global/instance-schema com.google.api.core.ApiService)]
             [:map [:state [:enum :NEW :STARTING :RUNNING :STOPPING :TERMINATED :FAILED]]]]
       as-sk [:or
              [:and {:gen/schema [:map [:state [:enum :NEW :STARTING :RUNNING :STOPPING :TERMINATED :FAILED]]]} (global/instance-schema com.google.api.core.AbstractApiService)]
              [:map [:state [:enum :NEW :STARTING :RUNNING :STOPPING :TERMINATED :FAILED]]]]})
    {:gcp.global/name :gcp.foreign.com.google.api.core/registry}))

(global/include-schema-registry! registry)

#!-----------------------------------------------------------------------------

(defn ApiFuture-to-edn [^ApiFuture arg]
  {:val (.get arg)})

(defn ApiFuture-from-edn [arg]
  (let [v (:val arg)]
    (reify ApiFuture
      (get [_] v)
      (get [_ _ _] v)
      (addListener [_ _ _] nil)
      (cancel [_ _] false)
      (isCancelled [_] false)
      (isDone [_] true))))

#!-----------------------------------------------------------------------------

(defn ApiService-to-edn [^ApiService arg]
  {:state (keyword (.name (.state arg)))})

(defn ApiService-from-edn [arg]
  (let [s (ApiService$State/valueOf (name (:state arg)))]
    (reify ApiService
      (state [_] s)
      (startAsync [_] nil)
      (stopAsync [_] nil)
      (awaitRunning [_] nil)
      (awaitRunning [_ _ _] nil)
      (awaitTerminated [_] nil)
      (awaitTerminated [_ _ _] nil)
      (failureCause [_] nil)
      (addListener [_ _ _] nil)
      (isRunning [_] (= s ApiService$State/RUNNING)))))

#!-----------------------------------------------------------------------------

(defn AbstractApiService-to-edn [^AbstractApiService arg]
  {:state (keyword (.name (.state arg)))})

(defn AbstractApiService-from-edn [arg]
  (let [s (ApiService$State/valueOf (name (:state arg)))]
    (proxy [AbstractApiService] []
      (state [] s)
      (doStart [] nil)
      (doStop [] nil))))
