(ns gcp.build
  (:require
   [gcp.build.core :as core]
   [gcp.build.global :as global]
   [gcp.build.release :as release]
   [gcp.dev.packages.definitions :as defs]))

(defn global []
  (global/build))

(defn release-bigquery []
  (release/release-many [defs/bigquery]))

(defn release-storage []
  (release/release-many [defs/storage]))

(defn release-vertexai []
  (release/release-many [defs/vertexai]))

(defn release-pubsub []
  (release/release-many [defs/pubsub]))



#_(do (require :reload 'gcp.build) (in-ns 'gcp.build))