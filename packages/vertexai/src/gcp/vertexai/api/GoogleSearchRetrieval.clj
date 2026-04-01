;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.GoogleSearchRetrieval
  {:doc
     "<pre>\nTool to retrieve public web data for grounding, powered by Google.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GoogleSearchRetrieval}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.GoogleSearchRetrieval"
   :gcp.dev/certification
     {:base-seed 1774824668775
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1774824668775 :standard 1774824668776 :stress 1774824668777}
      :protocol-hash
        "b44d2581e3979d0a946f2f9d3973f275b05bb339485f3d5eb436c47b54f7dc70"
      :timestamp "2026-03-29T22:51:09.883063559Z"}}
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
  (cond-> {}
    (.hasDynamicRetrievalConfig arg) (assoc :dynamicRetrievalConfig
                                       (DynamicRetrievalConfig/to-edn
                                         (.getDynamicRetrievalConfig arg)))))

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