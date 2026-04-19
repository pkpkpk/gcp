;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.VertexAISearch
  {:doc
     "<pre>\nRetrieve from Vertex AI Search datastore or engine for grounding.\ndatastore and engine are mutually exclusive.\nSee https://cloud.google.com/products/agent-builder\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.VertexAISearch}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.VertexAISearch"
   :gcp.dev/certification
     {:base-seed 1776627455430
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1776627455430 :standard 1776627455431 :stress 1776627455432}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-19T19:37:36.315114748Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api VertexAISearch VertexAISearch$Builder
            VertexAISearch$DataStoreSpec VertexAISearch$DataStoreSpec$Builder]))

(declare from-edn to-edn DataStoreSpec-from-edn DataStoreSpec-to-edn)

(defn ^VertexAISearch$DataStoreSpec DataStoreSpec-from-edn
  [arg]
  (let [builder (VertexAISearch$DataStoreSpec/newBuilder)]
    (when (some? (get arg :dataStore))
      (.setDataStore builder (get arg :dataStore)))
    (when (some? (get arg :filter)) (.setFilter builder (get arg :filter)))
    (.build builder)))

(defn DataStoreSpec-to-edn
  [^VertexAISearch$DataStoreSpec arg]
  (when arg
    (cond-> {}
      (some->> (.getDataStore arg)
               (not= ""))
        (assoc :dataStore (.getDataStore arg))
      (some->> (.getFilter arg)
               (not= ""))
        (assoc :filter (.getFilter arg)))))

(def DataStoreSpec-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nDefine data stores within engine to filter on in a search call and\nconfigurations for those data stores. For more information, see\nhttps://cloud.google.com/generative-ai-app-builder/docs/reference/rpc/google.cloud.discoveryengine.v1#datastorespec\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.VertexAISearch.DataStoreSpec}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/VertexAISearch.DataStoreSpec}
   [:dataStore
    {:optional true,
     :getter-doc
       "<pre>\nFull resource name of DataStore, such as\nFormat:\n`projects/{project}/locations/{location}/collections/{collection}/dataStores/{dataStore}`\n</pre>\n\n<code>string data_store = 1;</code>\n\n@return The dataStore.",
     :setter-doc
       "<pre>\nFull resource name of DataStore, such as\nFormat:\n`projects/{project}/locations/{location}/collections/{collection}/dataStores/{dataStore}`\n</pre>\n\n<code>string data_store = 1;</code>\n\n@param value The dataStore to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:filter
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Filter specification to filter documents in the data store\nspecified by data_store field. For more information on filtering, see\n[Filtering](https://cloud.google.com/generative-ai-app-builder/docs/filter-search-metadata)\n</pre>\n\n<code>string filter = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The filter.",
     :setter-doc
       "<pre>\nOptional. Filter specification to filter documents in the data store\nspecified by data_store field. For more information on filtering, see\n[Filtering](https://cloud.google.com/generative-ai-app-builder/docs/filter-search-metadata)\n</pre>\n\n<code>string filter = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The filter to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(defn ^VertexAISearch from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/VertexAISearch arg)
  (let [builder (VertexAISearch/newBuilder)]
    (when (seq (get arg :dataStoreSpecs))
      (.addAllDataStoreSpecs builder
                             (mapv DataStoreSpec-from-edn
                               (get arg :dataStoreSpecs))))
    (when (some? (get arg :datastore))
      (.setDatastore builder (get arg :datastore)))
    (when (some? (get arg :engine)) (.setEngine builder (get arg :engine)))
    (when (some? (get arg :filter)) (.setFilter builder (get arg :filter)))
    (when (some? (get arg :maxResults))
      (.setMaxResults builder (int (get arg :maxResults))))
    (.build builder)))

(defn to-edn
  [^VertexAISearch arg]
  {:post [(global/strict! :gcp.vertexai.api/VertexAISearch %)]}
  (when arg
    (cond-> {}
      (seq (.getDataStoreSpecsList arg)) (assoc :dataStoreSpecs
                                           (mapv DataStoreSpec-to-edn
                                             (.getDataStoreSpecsList arg)))
      (some->> (.getDatastore arg)
               (not= ""))
        (assoc :datastore (.getDatastore arg))
      (some->> (.getEngine arg)
               (not= ""))
        (assoc :engine (.getEngine arg))
      (some->> (.getFilter arg)
               (not= ""))
        (assoc :filter (.getFilter arg))
      (.getMaxResults arg) (assoc :maxResults (.getMaxResults arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nRetrieve from Vertex AI Search datastore or engine for grounding.\ndatastore and engine are mutually exclusive.\nSee https://cloud.google.com/products/agent-builder\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.VertexAISearch}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.vertexai.api/VertexAISearch}
   [:dataStoreSpecs
    {:optional true,
     :getter-doc
       "<pre>\nSpecifications that define the specific DataStores to be searched, along\nwith configurations for those data stores. This is only considered for\nEngines with multiple data stores.\nIt should only be set if engine is used.\n</pre>\n\n<code>repeated .google.cloud.vertexai.v1.VertexAISearch.DataStoreSpec data_store_specs = 5;\n</code>",
     :setter-doc
       "<pre>\nSpecifications that define the specific DataStores to be searched, along\nwith configurations for those data stores. This is only considered for\nEngines with multiple data stores.\nIt should only be set if engine is used.\n</pre>\n\n<code>repeated .google.cloud.vertexai.v1.VertexAISearch.DataStoreSpec data_store_specs = 5;\n</code>"}
    [:sequential {:min 1, :gen/max 2}
     [:ref :gcp.vertexai.api/VertexAISearch.DataStoreSpec]]]
   [:datastore
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Fully-qualified Vertex AI Search data store resource ID.\nFormat:\n`projects/{project}/locations/{location}/collections/{collection}/dataStores/{dataStore}`\n</pre>\n\n<code>string datastore = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The datastore.",
     :setter-doc
       "<pre>\nOptional. Fully-qualified Vertex AI Search data store resource ID.\nFormat:\n`projects/{project}/locations/{location}/collections/{collection}/dataStores/{dataStore}`\n</pre>\n\n<code>string datastore = 1 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The datastore to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:engine
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Fully-qualified Vertex AI Search engine resource ID.\nFormat:\n`projects/{project}/locations/{location}/collections/{collection}/engines/{engine}`\n</pre>\n\n<code>string engine = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The engine.",
     :setter-doc
       "<pre>\nOptional. Fully-qualified Vertex AI Search engine resource ID.\nFormat:\n`projects/{project}/locations/{location}/collections/{collection}/engines/{engine}`\n</pre>\n\n<code>string engine = 2 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The engine to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:filter
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Filter strings to be passed to the search API.\n</pre>\n\n<code>string filter = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The filter.",
     :setter-doc
       "<pre>\nOptional. Filter strings to be passed to the search API.\n</pre>\n\n<code>string filter = 4 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The filter to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:maxResults
    {:optional true,
     :getter-doc
       "<pre>\nOptional. Number of search results to return per query.\nThe default value is 10.\nThe maximumm allowed value is 10.\n</pre>\n\n<code>int32 max_results = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The maxResults.",
     :setter-doc
       "<pre>\nOptional. Number of search results to return per query.\nThe default value is 10.\nThe maximumm allowed value is 10.\n</pre>\n\n<code>int32 max_results = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The maxResults to set.\n@return This builder for chaining."}
    :i32]])

(global/include-schema-registry!
  (with-meta {:gcp.vertexai.api/VertexAISearch schema,
              :gcp.vertexai.api/VertexAISearch.DataStoreSpec
                DataStoreSpec-schema}
    {:gcp.global/name "gcp.vertexai.api.VertexAISearch"}))