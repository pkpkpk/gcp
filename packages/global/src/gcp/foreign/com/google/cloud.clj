(ns gcp.foreign.com.google.cloud
  (:require [gcp.global :as global])
  (:import
   (com.google.cloud MonitoredResource MonitoredResourceDescriptor RetryOption)))

;; MonitoredResource
(defn ^MonitoredResource MonitoredResource-from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn MonitoredResource-to-edn [^MonitoredResource arg]
  (throw (Exception. "unimplemented")))

;; MonitoredResourceDescriptor
(defn ^MonitoredResourceDescriptor MonitoredResourceDescriptor-from-edn [arg]
  (throw (Exception. "unimplemented")))

(defn MonitoredResourceDescriptor-to-edn [^MonitoredResourceDescriptor arg]
  (throw (Exception. "unimplemented")))

;; RetryOption
(defn RetryOption-from-edn [arg]
  (throw (Exception. "unimplemented")))

(global/include-schema-registry!
 (with-meta
   {::RetryOption :any
    ::MonitoredResource :any
    ::MonitoredResourceDescriptor :any}
   {::global/name ::registry}))
