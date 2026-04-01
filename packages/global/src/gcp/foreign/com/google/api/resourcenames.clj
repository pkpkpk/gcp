(ns gcp.foreign.com.google.api.resourcenames
  {:gcp.dev/certification
   {:ResourceName
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767558085006
       :timestamp "2026-01-04T20:21:25.009850676Z"
       :passed-stages
         {:smoke 1767558085006 :standard 1767558085007 :stress 1767558085008}
       :source-hash
         "10a4a8162953454c786fb854d37d1a9e4500a8a8eb2409ec1ff1f6cee2df18d3"}}}
  (:require [gcp.global :as global])
  (:import (com.google.api.resourcenames ResourceName UntypedResourceName)))

(defn ResourceName-from-edn [arg]
  (UntypedResourceName/parse arg))

(defn ResourceName-to-edn [^ResourceName arg]
  (.toString arg))

(global/include-schema-registry!
 (with-meta
   {::ResourceName :string}
   {::global/name ::registry}))
