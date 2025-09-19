(ns gcp.logging.Sink
  (:require [gcp.logging.SinkInfo :as SinkInfo])
  (:import (com.google.cloud.logging Sink)))

(defn to-edn [^Sink arg]
  (when arg
    (SinkInfo/to-edn arg)))