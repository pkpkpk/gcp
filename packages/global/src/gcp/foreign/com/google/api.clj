(ns gcp.foreign.com.google.api
  (:require [gcp.global :as global]
            [gcp.foreign.com.google.protobuf :as protobuf])
  (:import (com.google.api HttpBody MetricDescriptor MonitoredResource MonitoredResourceDescriptor LabelDescriptor LabelDescriptor$ValueType)))

(defn LabelDescriptor-from-edn [arg]
  (let [builder (LabelDescriptor/newBuilder)]
    (when-some [v (:key arg)] (.setKey builder v))
    (when-some [v (:description arg)] (.setDescription builder v))
    (when-some [v (:valueType arg)]
      (.setValueType builder (if (string? v)
                               (LabelDescriptor$ValueType/valueOf v)
                               v)))
    (.build builder)))

(defn LabelDescriptor-to-edn [^LabelDescriptor arg]
  {:key (.getKey arg)
   :description (.getDescription arg)
   :valueType (str (.getValueType arg))})

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
    (when-some [v (:labels arg)] (.addAllLabels builder (map LabelDescriptor-from-edn v)))
    (.build builder)))

(defn MonitoredResourceDescriptor-to-edn [^MonitoredResourceDescriptor arg]
  {:type (.getType arg)
   :displayName (.getDisplayName arg)
   :description (.getDescription arg)
   :labels (mapv LabelDescriptor-to-edn (.getLabelsList arg))})

;; HttpBody
(defn HttpBody-from-edn [arg]
  (let [builder (HttpBody/newBuilder)]
    (when-some [v (:contentType arg)] (.setContentType builder v))
    (when-some [v (:data arg)] (.setData builder (protobuf/ByteString-from-edn v)))
    (.build builder)))

(defn HttpBody-to-edn [^HttpBody arg]
  {:contentType (.getContentType arg)
   :data (protobuf/ByteString-to-edn (.getData arg))})

(global/include-schema-registry!
 (with-meta
   {::LabelDescriptor [:map
                       [:key :string]
                       [:description :string]
                       [:valueType [:enum "STRING" "BOOL" "INT64"]]]
    ::MonitoredResource [:map
                         [:type :string]
                         [:labels [:map-of :string :string]]]
    ::MonitoredResourceDescriptor [:map
                                   [:type :string]
                                   [:displayName :string]
                                   [:description :string]
                                   [:labels [:sequential ::LabelDescriptor]]]
    ::HttpBody [:map
                [:contentType :string]
                [:data :gcp.foreign.com.google.protobuf/ByteString]]}
   {::global/name ::registry}))
