(ns gcp.pubsub.v1.ProjectName
  (:require [gcp.global :as g])
  (:import (com.google.pubsub.v1 ProjectName)))

(defn ^ProjectName from-edn [arg]
  (ProjectName/of (if (string? arg) arg (g/coerce :string (:project arg)))))