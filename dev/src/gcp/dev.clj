(ns gcp.dev
  (:require
   [clojure.java.io :as io]
   [clojure.repl :refer :all]
   [clojure.string :as string]
   [gcp.dev.compiler :as c]
   [gcp.dev.packages :as packages]
   [gcp.dev.util :refer :all]
   [gcp.global :as g]))

#_(do (require :reload 'gcp.dev) (in-ns 'gcp.dev))

(set! *print-namespace-maps* false)
