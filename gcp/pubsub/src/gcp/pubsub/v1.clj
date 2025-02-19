(ns gcp.pubsub.v1
  (:require [gcp.global :as g]))

(def registry
  {:pubsub/SubscriptionAdminClient []
   :pubsub/TopicAdminClient []})

(g/include-schema-registry! registry)