(ns gcp.foreign.com.google.common.collect
  {:gcp.dev/certification
   {:ImmutableList
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767558046161
       :timestamp "2026-01-04T20:20:46.172955367Z"
       :passed-stages
         {:smoke 1767558046161 :standard 1767558046162 :stress 1767558046163}
       :source-hash
         "e8b9abfd22a231e4954a56f2a80f3bfe680ad3bea4563ec8ba27a77bd8441ecf"}}}
  (:require [gcp.global :as global])
  (:import (com.google.common.collect ImmutableList)))

(defn ImmutableList-from-edn [arg]
  (ImmutableList/copyOf ^java.lang.Iterable arg))

(defn ImmutableList-to-edn [^ImmutableList arg]
  (into [] arg))

(global/include-schema-registry!
 (with-meta
   {::ImmutableList [:sequential [:or :string :int :boolean :double]]}
   {::global/name ::registry}))
