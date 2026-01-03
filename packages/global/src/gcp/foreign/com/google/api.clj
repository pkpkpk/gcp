(ns gcp.foreign.com.google.api
  (:require [gcp.global :as global])
  (:import (com.google.api HttpBody MetricDescriptor MonitoredResource MonitoredResourceDescriptor)))

;; MonitoredResource
(defn MonitoredResource-from-edn [arg]
  (let [builder (MonitoredResource/newBuilder)]
    (when-some [v (:type arg)] (.setType builder v))
    (when-some [v (:labels arg)] (.putAllLabels builder v))
    (.build builder)))

(defn MonitoredResource-to-edn [^MonitoredResource arg]
  {:type (.getType arg)
   :labels (into {} (.getLabelsMap arg))})

;; MonitoredResourceDescriptor
(defn MonitoredResourceDescriptor-from-edn [arg]
  (let [builder (MonitoredResourceDescriptor/newBuilder)]
    (when-some [v (:type arg)] (.setType builder v))
    (when-some [v (:displayName arg)] (.setDisplayName builder v))
    (when-some [v (:description arg)] (.setDescription builder v))
    (.build builder)))

(defn MonitoredResourceDescriptor-to-edn [^MonitoredResourceDescriptor arg]
  {:type (.getType arg)
   :displayName (.getDisplayName arg)
   :description (.getDescription arg)})

;; HttpBody
(defn HttpBody-from-edn [arg]
  (let [builder (HttpBody/newBuilder)]
    (when-some [v (:contentType arg)] (.setContentType builder v))
    (when-some [v (:data arg)] (.setData builder v)) ;; Expecting ByteString or bytes
    (.build builder)))

(defn HttpBody-to-edn [^HttpBody arg]
  {:contentType (.getContentType arg)
   :data (.getData arg)})

(global/include-schema-registry!
 (with-meta
   {::MonitoredResource [:map
                         [:type :string]
                         [:labels [:map-of :string :string]]]
    ::MonitoredResourceDescriptor [:map
                                   [:type :string]
                                   [:displayName :string]
                                   [:description :string]]
    ::HttpBody [:map
                [:contentType :string]
                [:data :any]]}
   {::global/name ::registry}))
