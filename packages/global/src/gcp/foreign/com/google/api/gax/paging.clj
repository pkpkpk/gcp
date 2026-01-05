(ns gcp.foreign.com.google.api.gax.paging
  {:gcp.dev/certification
   {:AsyncPage
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767573021035
     :timestamp "2026-01-05T00:30:21.127698090Z"
     :passed-stages {:smoke 1767573021035
                     :standard 1767573021036
                     :stress 1767573021037}
     :source-hash "1cda6d46880dc0b134168e349999ec6c08e9cca60234ce4eacd9f9c6f7ae6bd9"}
    :Page
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767573021130
     :timestamp "2026-01-05T00:30:21.209584956Z"
     :passed-stages {:smoke 1767573021130
                     :standard 1767573021131
                     :stress 1767573021132}
     :source-hash "1cda6d46880dc0b134168e349999ec6c08e9cca60234ce4eacd9f9c6f7ae6bd9"}}}
  {:doc "Foreign bindings for com.google.api.gax.paging"}
  (:require [gcp.global :as global])
  (:import (com.google.api.gax.paging AsyncPage Page)))

(def registry
  (with-meta
    (let [p-sk :gcp.foreign.com.google.api.gax.paging/Page
          ap-sk :gcp.foreign.com.google.api.gax.paging/AsyncPage]
      {p-sk [:or
             [:and {:gen/schema [:map [:values [:sequential :string]]]} (global/instance-schema com.google.api.gax.paging.Page)]
             [:map [:values [:sequential :string]]]]
       ap-sk [:or
              [:and {:gen/schema [:map [:values [:sequential :string]]]} (global/instance-schema com.google.api.gax.paging.AsyncPage)]
              [:map [:values [:sequential :string]]]]})
    {:gcp.global/name :gcp.foreign.com.google.api.gax.paging/registry}))

(global/include-schema-registry! registry)

#!-----------------------------------------------------------------------------

(defn Page-to-edn [^Page arg]
  {:values (vec (.getValues arg))})

(defn Page-from-edn [arg]
  (let [vs (:values arg)]
    (reify Page
      (getValues [_] vs)
      (hasNextPage [_] false)
      (getNextPageToken [_] nil)
      (getNextPage [_] nil)
      (iterateAll [_] vs))))

#!-----------------------------------------------------------------------------

(defn AsyncPage-to-edn [^AsyncPage arg]
  {:values (vec (.getValues arg))})

(defn AsyncPage-from-edn [arg]
  (let [vs (:values arg)]
    (reify AsyncPage
      (getValues [_] vs)
      (hasNextPage [_] false)
      (getNextPageToken [_] nil)
      (getNextPage [_] nil)
      (iterateAll [_] vs)
      (getNextPageAsync [_] nil))))
