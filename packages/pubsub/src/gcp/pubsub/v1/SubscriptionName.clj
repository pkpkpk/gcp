(ns gcp.pubsub.v1.SubscriptionName
  (:require [gcp.global :as global])
  (:import (com.google.pubsub.v1 SubscriptionName)))

(defn ^SubscriptionName from-edn
  [{:keys [project subscription] :as arg}]
  (global/strict! :gcp.pubsub.v1/SubscriptionName arg)
  (SubscriptionName/of project subscription))

(def schemas
  {:gcp.pubsub.v1/SubscriptionName
   [:map {:closed true}
    [:project :string]
    [:subscription :string]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))