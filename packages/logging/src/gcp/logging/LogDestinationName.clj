(ns gcp.logging.LogDestinationName
  (:require [gcp.global :as g]
            [gcp.logging.v2.LogName :as LogName])
  (:import (com.google.cloud.logging LogDestinationName)))

(def schema
  [:or
   LogName/schema
   [:map {:closed true}
    [:id :string]
    [:type [:enum "BILLINGACCOUNT" "FOLDER" "ORGANIZATION" "PROJECT"]]]])

(defn ^LogDestinationName from-edn [arg]
  (g/strict! schema arg)
  (if (g/valid? LogName/schema arg)
    (LogDestinationName/fromLogName (LogName/from-edn arg))
    (let [{:keys [type id]} arg]
      (case type
        "BILLINGACCOUNT" (LogDestinationName/billingAccount id)
        "FOLDER" (LogDestinationName/folder id)
        "ORGANIZATION" (LogDestinationName/organization id)
        "PROJECT" (LogDestinationName/project id)))))

(defn to-edn [^LogDestinationName arg]
  {:type (.name (.getDestinationType arg))
   :id (.getDestinationId arg)})