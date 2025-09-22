(ns gcp.logging.Synchronicity
  (:require [gcp.global :as g])
  (:import (com.google.cloud.logging Synchronicity)))

(def schema (g/schema [:enum "ASYNC" "SYNC"]))

(defn ^Synchronicity from-edn [arg] (Synchronicity/valueOf arg))

(defn ^String to-edn [^Synchronicity arg] (.name arg))