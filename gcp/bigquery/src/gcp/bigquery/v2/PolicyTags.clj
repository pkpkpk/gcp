(ns gcp.bigquery.v2.PolicyTags
  (:import (com.google.cloud.bigquery PolicyTags)))

(defn ^PolicyTags from-edn [{names :names}]
  (let [builder (PolicyTags/newBuilder)]
    (.setNames builder (seq names))
    (.build builder)))

(defn to-edn [^PolicyTags arg] {:names (vec (.getNames arg))})
