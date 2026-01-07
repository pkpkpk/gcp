(ns gcp.build
  (:require [gcp.build.util :as util]))

#_ (do (require :reload 'gcp.build) (in-ns 'gcp.build))

(defn global []
  (let [pom ((requiring-resolve 'gcp.build.global/pom))]
    (util/jar pom)
    (util/deploy pom)))

;; TODO check global hash

(defn bigquery []
  (let [pom ((requiring-resolve 'gcp.build.bigquery/pom))]
    (util/jar pom)
    (util/deploy pom)))

(defn vertexai []
  (let [pom ((requiring-resolve 'gcp.build.vertexai/pom))]
    (util/jar pom)
    (util/deploy pom)))

(comment
  (global)
  (bigquery)
  (vertexai)
  )