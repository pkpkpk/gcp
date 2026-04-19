;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.RetrievalConfig
  {:doc
     "<pre>\nRetrieval config.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.RetrievalConfig}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.RetrievalConfig"
   :gcp.dev/certification
     {:base-seed 1776627451411
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627451411 :standard 1776627451412 :stress 1776627451413}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:37:32.281283241Z"}}
  (:require [gcp.foreign.com.google.type :as type]
            [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api RetrievalConfig
            RetrievalConfig$Builder]
           [com.google.type LatLng]))

(declare from-edn to-edn)

(defn ^RetrievalConfig from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/RetrievalConfig arg)
  (let [builder (RetrievalConfig/newBuilder)]
    (when (some? (get arg :languageCode))
      (.setLanguageCode builder (get arg :languageCode)))
    (when (some? (get arg :latLng))
      (.setLatLng builder (type/LatLng-from-edn (get arg :latLng))))
    (.build builder)))

(defn to-edn
  [^RetrievalConfig arg]
  {:post [(global/strict! :gcp.vertexai.api/RetrievalConfig %)]}
  (when arg
    (cond-> {}
      (.hasLanguageCode arg) (assoc :languageCode (.getLanguageCode arg))
      (.hasLatLng arg) (assoc :latLng (type/LatLng-to-edn (.getLatLng arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nRetrieval config.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.RetrievalConfig}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/RetrievalConfig}
   [:languageCode
    {:optional true,
     :getter-doc
       "<pre>\nThe language code of the user.\n</pre>\n\n<code>optional string language_code = 2;</code>\n\n@return The languageCode.",
     :setter-doc
       "<pre>\nThe language code of the user.\n</pre>\n\n<code>optional string language_code = 2;</code>\n\n@param value The languageCode to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:latLng
    {:optional true,
     :getter-doc
       "<pre>\nThe location of the user.\n</pre>\n\n<code>optional .google.type.LatLng lat_lng = 1;</code>\n\n@return The latLng.",
     :setter-doc
       "<pre>\nThe location of the user.\n</pre>\n\n<code>optional .google.type.LatLng lat_lng = 1;</code>"}
    :gcp.foreign.com.google.type/LatLng]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/RetrievalConfig schema}
    {:gcp.global/name "gcp.vertexai.api.RetrievalConfig"}))