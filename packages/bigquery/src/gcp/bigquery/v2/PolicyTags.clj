(ns gcp.bigquery.v2.PolicyTags
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery PolicyTags)))

(defn ^PolicyTags from-edn [{names :names}]
  (let [builder (PolicyTags/newBuilder)]
    (.setNames builder (seq names))
    (.build builder)))

(defn to-edn [^PolicyTags arg] {:names (vec (.getNames arg))})

(def schemas
  {:gcp.bigquery.v2/PolicyTags
   [:map {:closed true} [:names [:sequential :string]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
