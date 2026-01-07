(ns gcp.foreign.java.sql
 
 {:gcp.dev/certification
   {:SQLException
      {:base-seed 1767752774971
       :passed-stages
         {:smoke 1767752774971 :standard 1767752774972 :stress 1767752774973}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "07fc53f9913f58d09ba33c8c159071d7f56b931cc1369ad18bcafccf2b35ce04"
       :timestamp "2026-01-07T02:26:15.064561077Z"}}}
  (:require [gcp.global :as g] [malli.core :as m])
  (:import (java.sql SQLException)))

(def registry
  ^{:gcp.global/name :gcp.foreign.java.sql/registry}
  {:gcp.foreign.java.sql/SQLException [:or {:gen/schema :string} (g/instance-schema java.sql.SQLException) :string]})

(g/include-schema-registry! registry)

(defn ^SQLException SQLException-from-edn [arg]
  (if (instance? SQLException arg) arg (SQLException. (str arg))))
(defn SQLException-to-edn [^SQLException arg] (.getMessage arg))