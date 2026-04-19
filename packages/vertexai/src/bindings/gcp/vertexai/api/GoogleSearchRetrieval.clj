;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.GoogleSearchRetrieval
  {:doc
     "<pre>\nTool to retrieve public web data for grounding, powered by Google.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GoogleSearchRetrieval}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.GoogleSearchRetrieval"
   :gcp.dev/certification
     {:base-seed 1776627493435
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627493435 :standard 1776627493436 :stress 1776627493437}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:38:14.462876322Z"}}
  (:require [gcp.global :as global]
            [gcp.vertexai.api.DynamicRetrievalConfig :as
             DynamicRetrievalConfig])
  (:import [com.google.cloud.vertexai.api GoogleSearchRetrieval
            GoogleSearchRetrieval$Builder]))

(declare from-edn to-edn)

(defn ^GoogleSearchRetrieval from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/GoogleSearchRetrieval arg)
  (let [builder (GoogleSearchRetrieval/newBuilder)]
    (when (some? (get arg :dynamicRetrievalConfig))
      (.setDynamicRetrievalConfig builder
                                  (DynamicRetrievalConfig/from-edn
                                    (get arg :dynamicRetrievalConfig))))
    (.build builder)))

(defn to-edn
  [^GoogleSearchRetrieval arg]
  {:post [(global/strict! :gcp.vertexai.api/GoogleSearchRetrieval %)]}
  (when arg
    (cond-> {}
      (.hasDynamicRetrievalConfig arg) (assoc :dynamicRetrievalConfig
                                         (DynamicRetrievalConfig/to-edn
                                           (.getDynamicRetrievalConfig arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nTool to retrieve public web data for grounding, powered by Google.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GoogleSearchRetrieval}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/GoogleSearchRetrieval}
   [:dynamicRetrievalConfig
    {:optional true,
     :getter-doc
       "<pre>\nSpecifies the dynamic retrieval configuration for the given source.\n</pre>\n\n<code>.google.cloud.vertexai.v1.DynamicRetrievalConfig dynamic_retrieval_config = 2;</code>\n\n@return The dynamicRetrievalConfig.",
     :setter-doc
       "<pre>\nSpecifies the dynamic retrieval configuration for the given source.\n</pre>\n\n<code>.google.cloud.vertexai.v1.DynamicRetrievalConfig dynamic_retrieval_config = 2;</code>"}
    :gcp.vertexai.api/DynamicRetrievalConfig]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/GoogleSearchRetrieval schema}
    {:gcp.global/name "gcp.vertexai.api.GoogleSearchRetrieval"}))