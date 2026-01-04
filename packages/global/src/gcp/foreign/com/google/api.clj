(ns gcp.foreign.com.google.api
  {:gcp.dev/certification
   {:HttpBody
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767557701831
       :timestamp "2026-01-04T20:15:02.079814287Z"
       :passed-stages
         {:smoke 1767557701831 :standard 1767557701832 :stress 1767557701833}
       :source-hash
         "6805d7def1b979498a558df5300f39158ef6dd99e537a5ca0cfe4c50f0140ca4"}
    :LabelDescriptor
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767557702080
       :timestamp "2026-01-04T20:15:02.104201007Z"
       :passed-stages
         {:smoke 1767557702080 :standard 1767557702081 :stress 1767557702082}
       :source-hash
         "6805d7def1b979498a558df5300f39158ef6dd99e537a5ca0cfe4c50f0140ca4"}
    :MonitoredResource
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767557702104
       :timestamp "2026-01-04T20:15:02.226345170Z"
       :passed-stages
         {:smoke 1767557702104 :standard 1767557702105 :stress 1767557702106}
       :source-hash
         "6805d7def1b979498a558df5300f39158ef6dd99e537a5ca0cfe4c50f0140ca4"}
    :MonitoredResourceDescriptor
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767557702227
       :timestamp "2026-01-04T20:15:02.320909679Z"
       :passed-stages
         {:smoke 1767557702227 :standard 1767557702228 :stress 1767557702229}
       :source-hash
         "6805d7def1b979498a558df5300f39158ef6dd99e537a5ca0cfe4c50f0140ca4"}}}
  (:require [gcp.global :as global]
            [gcp.foreign.com.google.protobuf :as protobuf])
  (:import (com.google.api HttpBody MetricDescriptor MonitoredResource MonitoredResourceDescriptor LabelDescriptor LabelDescriptor$ValueType)))

;; LabelDescriptor
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
