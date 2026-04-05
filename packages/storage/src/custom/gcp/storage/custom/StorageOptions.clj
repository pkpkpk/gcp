(ns gcp.storage.custom.StorageOptions
  (:require gcp.foreign.io.opentelemetry.api
            [gcp.global :as g]
            [gcp.storage.BlobWriteSessionConfigs :as BlobWriteSessionConfigs]
            [gcp.storage.custom.StorageRetryStrategy :as StorageRetryStrategy])
  (:import (com.google.cloud.storage Storage StorageOptions)))

(def schema
  [:maybe
   {:gcp/key :gcp.storage/StorageOptions}
   [:map
    {:closed true}
    [:version {:optional true :read-only? true} :string]
    [:openTelemetry
     {:optional   true
      :getter-doc "@since 2.47.0 This new api is in preview and is subject to breaking changes.",
      :setter-doc "Enable OpenTelemetry Tracing and provide an instance for the client to use.

                 @param openTelemetry User defined instance of OpenTelemetry to be used by the library
                 @since 2.47.0 This new api is in preview and is subject to breaking changes."}
     :gcp.foreign.io.opentelemetry.api/OpenTelemetry]
    [:blobWriteSessionConfig {:optional true :write-only? true} :gcp.storage/BlobWriteSessionConfigs]
    [:storageRetryStrategy {:optional true :write-only? true} :gcp.storage/StorageRetryStrategy]]])

(defn ^StorageOptions from-edn
  ([] (from-edn nil))
  ([arg]
   (if (nil? arg)
     (StorageOptions/getDefaultInstance)
     (let [builder (StorageOptions/newBuilder)]
       (some->> (:openTelemetry arg) (.setOpenTelemetry builder))
       (some->> (:blobWriteSessionConfig arg) BlobWriteSessionConfigs/from-edn (.setBlobWriteSessionConfig builder))
       (some->> (:storageRetryStrategy arg) StorageRetryStrategy/from-edn (.setStorageRetryStrategy builder))
       (.build builder)))))

(defn to-edn [^StorageOptions arg]
  (cond-> {:version (.getLibraryVersion arg)}
          (.getOpenTelemetry arg) (assoc :openTelemetry (.getOpenTelemetry arg))))

(defn ^String get-library-version
  ([]
   (get-library-version nil))
  ([arg]
   (.getLibraryVersion (from-edn arg))))

(defn ^Storage get-service [arg]
  (if (instance? Storage arg)
    arg
    (.getService (from-edn arg))))

(g/include-schema-registry!
  (with-meta {:gcp.storage/StorageOptions schema} {::name "gcp.storage.StorageOptions"}))