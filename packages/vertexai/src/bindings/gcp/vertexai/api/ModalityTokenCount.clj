;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.ModalityTokenCount
  {:doc
     "<pre>\nRepresents token counting info for a single modality.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.ModalityTokenCount}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.ModalityTokenCount"
   :gcp.dev/certification
     {:base-seed 1776627511338
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627511338 :standard 1776627511339 :stress 1776627511340}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:38:32.200561863Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api Modality ModalityTokenCount
            ModalityTokenCount$Builder]))

(declare from-edn to-edn)

(defn ^ModalityTokenCount from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/ModalityTokenCount arg)
  (let [builder (ModalityTokenCount/newBuilder)]
    (when (some? (get arg :modality))
      (.setModality builder (Modality/valueOf (get arg :modality))))
    (when (some? (get arg :tokenCount))
      (.setTokenCount builder (int (get arg :tokenCount))))
    (.build builder)))

(defn to-edn
  [^ModalityTokenCount arg]
  {:post [(global/strict! :gcp.vertexai.api/ModalityTokenCount %)]}
  (when arg
    (cond-> {}
      (.getModality arg) (assoc :modality (.name (.getModality arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nRepresents token counting info for a single modality.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.ModalityTokenCount}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/ModalityTokenCount}
   [:modality
    {:optional true,
     :getter-doc
       "<pre>\nThe modality associated with this token count.\n</pre>\n\n<code>.google.cloud.vertexai.v1.Modality modality = 1;</code>\n\n@return The modality.",
     :setter-doc
       "<pre>\nThe modality associated with this token count.\n</pre>\n\n<code>.google.cloud.vertexai.v1.Modality modality = 1;</code>\n\n@param value The modality to set.\n@return This builder for chaining."}
    [:enum {:closed true} "MODALITY_UNSPECIFIED" "TEXT" "IMAGE" "VIDEO" "AUDIO"
     "DOCUMENT"]]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/ModalityTokenCount schema}
    {:gcp.global/name "gcp.vertexai.api.ModalityTokenCount"}))