(ns gcp.pubsub.v1.ProjectName
  (:require [gcp.global :as global])
  (:import (com.google.pubsub.v1 ProjectName)))

(defn ^ProjectName from-edn [arg]
  (ProjectName/of (if (string? arg) arg (global/coerce :string (:project arg)))))

(def schemas
  {:gcp.pubsub.v1/ProjectName
   [:or
    :string
    [:map {:closed true} [:project :string]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))