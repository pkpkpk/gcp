;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.MessageStoragePolicy
  {:doc "<pre>\nA policy constraining the storage of messages published to the topic.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.MessageStoragePolicy}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.MessageStoragePolicy"
   :gcp.dev/certification {:base-seed 1777050376783
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777050376783 :standard 1777050376784 :stress 1777050376785}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-24T17:06:17.881356255Z"}}
  (:require
   [gcp.foreign.com.google.protobuf :as protobuf]
   [gcp.global :as global])
  (:import
   (com.google.protobuf ProtocolStringList)
   (com.google.pubsub.v1 MessageStoragePolicy MessageStoragePolicy$Builder)))

(declare from-edn to-edn)

(defn ^MessageStoragePolicy from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/MessageStoragePolicy arg)
  (let [builder (MessageStoragePolicy/newBuilder)]
    (when (seq (get arg :allowedPersistenceRegions))
      (.addAllAllowedPersistenceRegions builder
                                        (seq (get arg
                                                  :allowedPersistenceRegions))))
    (when (some? (get arg :enforceInTransit))
      (.setEnforceInTransit builder (get arg :enforceInTransit)))
    (when (some? (get arg :owedPersistenceRegions))
      (.addAllowedPersistenceRegions builder (get arg :owedPersistenceRegions)))
    (.build builder)))

(defn to-edn
  [^MessageStoragePolicy arg]
  {:post [(global/strict! :gcp.pubsub.v1/MessageStoragePolicy %)]}
  (when arg
    (cond-> {}
      (seq (.getAllowedPersistenceRegionsList arg))
      (assoc :allowedPersistenceRegions
        (protobuf/ProtocolStringList-to-edn (.getAllowedPersistenceRegionsList
                                              arg)))
      (.getEnforceInTransit arg) (assoc :enforceInTransit
                                   (.getEnforceInTransit arg)))))

(def schema
  [:map
   {:closed true
    :doc
    "<pre>\nA policy constraining the storage of messages published to the topic.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.MessageStoragePolicy}"
    :gcp/category :protobuf-message
    :gcp/key :gcp.pubsub.v1/MessageStoragePolicy}
   [:allowedPersistenceRegions
    {:optional true
     :getter-doc
     "<pre>\nOptional. A list of IDs of Google Cloud regions where messages that are\npublished to the topic may be persisted in storage. Messages published by\npublishers running in non-allowed Google Cloud regions (or running outside\nof Google Cloud altogether) are routed for storage in one of the allowed\nregions. An empty list means that no regions are allowed, and is not a\nvalid configuration.\n</pre>\n\n<code>\nrepeated string allowed_persistence_regions = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return A list containing the allowedPersistenceRegions."
     :setter-doc
     "<pre>\nOptional. A list of IDs of Google Cloud regions where messages that are\npublished to the topic may be persisted in storage. Messages published by\npublishers running in non-allowed Google Cloud regions (or running outside\nof Google Cloud altogether) are routed for storage in one of the allowed\nregions. An empty list means that no regions are allowed, and is not a\nvalid configuration.\n</pre>\n\n<code>\nrepeated string allowed_persistence_regions = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param values The allowedPersistenceRegions to add.\n@return This builder for chaining."}
    :gcp.foreign.com.google.protobuf/ProtocolStringList]
   [:enforceInTransit
    {:optional true
     :getter-doc
     "<pre>\nOptional. If true, `allowed_persistence_regions` is also used to enforce\nin-transit guarantees for messages. That is, Pub/Sub will fail\nPublish operations on this topic and subscribe operations\non any subscription attached to this topic in any region that is\nnot in `allowed_persistence_regions`.\n</pre>\n\n<code>bool enforce_in_transit = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The enforceInTransit."
     :setter-doc
     "<pre>\nOptional. If true, `allowed_persistence_regions` is also used to enforce\nin-transit guarantees for messages. That is, Pub/Sub will fail\nPublish operations on this topic and subscribe operations\non any subscription attached to this topic in any region that is\nnot in `allowed_persistence_regions`.\n</pre>\n\n<code>bool enforce_in_transit = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The enforceInTransit to set.\n@return This builder for chaining."}
    :boolean]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/MessageStoragePolicy schema}
    {:gcp.global/name "gcp.pubsub.v1.MessageStoragePolicy"}))
