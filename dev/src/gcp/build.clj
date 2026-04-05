(ns gcp.build
  (:require [gcp.build.core :as core]
            [gcp.build.global :as global]
            [gcp.dev.packages.definitions :as defs]))

(defn global []
  (global/build))

(defn bigquery []
  (core/build-package defs/bigquery))

(defn storage []
  (core/build-package defs/storage))

(defn vertexai []
  (core/build-package defs/vertexai))

(defn pubsub []
  (core/build-package defs/pubsub))

(defn all []
  (let [gv (global)]
    {:global gv
     :bigquery (bigquery)
     :storage (storage)
     :vertexai (vertexai)
     :pubsub (pubsub)}))
