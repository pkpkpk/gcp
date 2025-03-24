(ns gcp.pubsub.v1.SubscriptionName
  (:require [gcp.global :as g])
  (:import (com.google.pubsub.v1 SubscriptionName)))

(defn ^SubscriptionName from-edn
  [{:keys [project subscription] :as arg}]
  (g/strict! :gcp/pubsub.SubscriptionName arg)
  (SubscriptionName/of project subscription))