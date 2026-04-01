(ns gcp.bigquery.custom.Model
  (:require [gcp.bigquery.ModelInfo :as ModelInfo])
  (:import (com.google.cloud.bigquery Model)))

(defn to-edn [^Model arg]
  (when arg
    (assoc (ModelInfo/to-edn arg) :bigquery (.getBigQuery arg))))