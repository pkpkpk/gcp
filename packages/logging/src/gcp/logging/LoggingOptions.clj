(ns gcp.logging.LoggingOptions
  (:require [gcp.global :as g])
  (:import (com.google.cloud.logging Logging LoggingOptions)))

(def schema
  (g/schema
    [:maybe
     [:map
      {:url "https://cloud.google.com/java/docs/reference/google-cloud-logging/latest/com.google.cloud.logging.LoggingOptions"}
      [:autoPopulateMetadataOnWrite {:optional true} :boolean]
      [:setBatchingSettings {:optional true} any?]          ;TODO
      [:transportOptions {:optional true} any?]]]))         ;TODO

(defn ^LoggingOptions from-edn
  [arg]
  (g/strict! schema arg)
  (let [builder (LoggingOptions/newBuilder)]
    (some->> (:autoPopulateMetadataOnWrite arg) (.setAutoPopulateMetadata builder))
    (when-let [batchSettings (:setBatchingSettings arg)]
      (throw (Exception. "unimplemented")))
    (when-let [transportOptions (:transportOptions arg)]
      (throw (Exception. "unimplemented")))
    (.build builder)))

(defn to-edn [^LoggingOptions arg]
  (throw (Exception. "unimplemented")))

(def clientable-schema
  (g/schema
    [:maybe
     [:or
      (g/instance-schema com.google.cloud.logging.Logging)
      (g/instance-schema com.google.cloud.logging.LoggingOptions)
      schema]]))

(defn ^Logging get-service
  ([]
   (get-service nil))
  ([arg]
   (if (instance? Logging arg)
     arg
     (if (instance? LoggingOptions arg)
       (.getService arg)
       (or (g/get-client ::client arg)
           (let [l (.getService (from-edn arg))]
             (g/put-client! ::client arg l)
             l))))))
