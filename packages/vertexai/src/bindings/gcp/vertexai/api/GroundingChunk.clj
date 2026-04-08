;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.vertexai.api.GroundingChunk
  {:doc
     "<pre>\nGrounding chunk.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GroundingChunk}"
   :file-git-sha "d937fcec0c42304b32ec37bc46cfb9739b978382"
   :fqcn "com.google.cloud.vertexai.api.GroundingChunk"
   :gcp.dev/certification
     {:base-seed 1775465673305
      :manifest "2e809e6a-933c-51dd-8bb9-567961e7a29e"
      :passed-stages
        {:smoke 1775465673305 :standard 1775465673306 :stress 1775465673307}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-06T08:54:34.284429585Z"}}
  (:require [gcp.global :as global]
            [gcp.vertexai.api.RagChunk :as RagChunk])
  (:import [com.google.cloud.vertexai.api GroundingChunk GroundingChunk$Builder
            GroundingChunk$ChunkTypeCase GroundingChunk$Maps
            GroundingChunk$Maps$Builder GroundingChunk$Maps$PlaceAnswerSources
            GroundingChunk$Maps$PlaceAnswerSources$Builder
            GroundingChunk$Maps$PlaceAnswerSources$ReviewSnippet
            GroundingChunk$Maps$PlaceAnswerSources$ReviewSnippet$Builder
            GroundingChunk$RetrievedContext
            GroundingChunk$RetrievedContext$Builder
            GroundingChunk$RetrievedContext$ContextDetailsCase
            GroundingChunk$Web GroundingChunk$Web$Builder]))

(declare from-edn
         to-edn
         Web-from-edn
         Web-to-edn
         RetrievedContext$ContextDetailsCase-from-edn
         RetrievedContext$ContextDetailsCase-to-edn
         RetrievedContext-from-edn
         RetrievedContext-to-edn
         RetrievedContext$ContextDetailsCase-from-edn
         RetrievedContext$ContextDetailsCase-to-edn
         Maps$PlaceAnswerSources$ReviewSnippet-from-edn
         Maps$PlaceAnswerSources$ReviewSnippet-to-edn
         Maps$PlaceAnswerSources-from-edn
         Maps$PlaceAnswerSources-to-edn
         Maps$PlaceAnswerSources$ReviewSnippet-from-edn
         Maps$PlaceAnswerSources$ReviewSnippet-to-edn
         Maps-from-edn
         Maps-to-edn
         Maps$PlaceAnswerSources$ReviewSnippet-from-edn
         Maps$PlaceAnswerSources$ReviewSnippet-to-edn
         Maps$PlaceAnswerSources-from-edn
         Maps$PlaceAnswerSources-to-edn
         Maps$PlaceAnswerSources$ReviewSnippet-from-edn
         Maps$PlaceAnswerSources$ReviewSnippet-to-edn
         ChunkTypeCase-from-edn
         ChunkTypeCase-to-edn)

(defn ^GroundingChunk$Web Web-from-edn
  [arg]
  (let [builder (GroundingChunk$Web/newBuilder)]
    (when (some? (get arg :title)) (.setTitle builder (get arg :title)))
    (when (some? (get arg :uri)) (.setUri builder (get arg :uri)))
    (.build builder)))

(defn Web-to-edn
  [^GroundingChunk$Web arg]
  (when arg
    (cond-> {}
      (.hasTitle arg) (assoc :title (.getTitle arg))
      (.hasUri arg) (assoc :uri (.getUri arg)))))

(def Web-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nChunk from the web.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GroundingChunk.Web}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/GroundingChunk.Web}
   [:title
    {:optional true,
     :getter-doc
       "<pre>\nTitle of the chunk.\n</pre>\n\n<code>optional string title = 2;</code>\n\n@return The title.",
     :setter-doc
       "<pre>\nTitle of the chunk.\n</pre>\n\n<code>optional string title = 2;</code>\n\n@param value The title to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:uri
    {:optional true,
     :getter-doc
       "<pre>\nURI reference of the chunk.\n</pre>\n\n<code>optional string uri = 1;</code>\n\n@return The uri.",
     :setter-doc
       "<pre>\nURI reference of the chunk.\n</pre>\n\n<code>optional string uri = 1;</code>\n\n@param value The uri to set.\n@return This builder for chaining."}
    [:string {:min 1}]]])

(def RetrievedContext$ContextDetailsCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key
      :gcp.vertexai.api/GroundingChunk.RetrievedContext.ContextDetailsCase}
   "RAG_CHUNK" "CONTEXTDETAILS_NOT_SET"])

(def RetrievedContext$ContextDetailsCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key
      :gcp.vertexai.api/GroundingChunk.RetrievedContext.ContextDetailsCase}
   "RAG_CHUNK" "CONTEXTDETAILS_NOT_SET"])

(defn ^GroundingChunk$RetrievedContext RetrievedContext-from-edn
  [arg]
  (let [builder (GroundingChunk$RetrievedContext/newBuilder)]
    (when (some? (get arg :documentName))
      (.setDocumentName builder (get arg :documentName)))
    (when (some? (get arg :text)) (.setText builder (get arg :text)))
    (when (some? (get arg :title)) (.setTitle builder (get arg :title)))
    (when (some? (get arg :uri)) (.setUri builder (get arg :uri)))
    (cond (contains? arg :ragChunk)
            (.setRagChunk builder (RagChunk/from-edn (get arg :ragChunk))))
    (.build builder)))

(defn RetrievedContext-to-edn
  [^GroundingChunk$RetrievedContext arg]
  (when arg
    (let [res (cond-> {}
                (.hasDocumentName arg) (assoc :documentName
                                         (.getDocumentName arg))
                (.hasText arg) (assoc :text (.getText arg))
                (.hasTitle arg) (assoc :title (.getTitle arg))
                (.hasUri arg) (assoc :uri (.getUri arg)))
          res (case (.name (.getContextDetailsCase arg))
                "RAG_CHUNK" (assoc res
                              :ragChunk (RagChunk/to-edn (.getRagChunk arg)))
                res)]
      res)))

(def RetrievedContext-schema
  [:and
   {:closed true,
    :doc
      "<pre>\nChunk from context retrieved by the retrieval tools.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GroundingChunk.RetrievedContext}",
    :gcp/category :nested/union-protobuf-oneof,
    :gcp/key :gcp.vertexai.api/GroundingChunk.RetrievedContext}
   [:map {:closed true}
    [:documentName
     {:optional true,
      :read-only true,
      :getter-doc
        "<pre>\nOutput only. The full document name for the referenced Vertex AI Search\ndocument.\n</pre>\n\n<code>optional string document_name = 6 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>\n\n@return The documentName."}
     [:string {:min 1}]]
    [:ragChunk
     {:optional true,
      :getter-doc
        "<pre>\nAdditional context for the RAG retrieval result. This is only populated\nwhen using the RAG retrieval tool.\n</pre>\n\n<code>.google.cloud.vertexai.v1.RagChunk rag_chunk = 4;</code>\n\n@return The ragChunk.",
      :setter-doc
        "<pre>\nAdditional context for the RAG retrieval result. This is only populated\nwhen using the RAG retrieval tool.\n</pre>\n\n<code>.google.cloud.vertexai.v1.RagChunk rag_chunk = 4;</code>"}
     :gcp.vertexai.api/RagChunk]
    [:text
     {:optional true,
      :getter-doc
        "<pre>\nText of the attribution.\n</pre>\n\n<code>optional string text = 3;</code>\n\n@return The text.",
      :setter-doc
        "<pre>\nText of the attribution.\n</pre>\n\n<code>optional string text = 3;</code>\n\n@param value The text to set.\n@return This builder for chaining."}
     [:string {:min 1}]]
    [:title
     {:optional true,
      :getter-doc
        "<pre>\nTitle of the attribution.\n</pre>\n\n<code>optional string title = 2;</code>\n\n@return The title.",
      :setter-doc
        "<pre>\nTitle of the attribution.\n</pre>\n\n<code>optional string title = 2;</code>\n\n@param value The title to set.\n@return This builder for chaining."}
     [:string {:min 1}]]
    [:uri
     {:optional true,
      :getter-doc
        "<pre>\nURI reference of the attribution.\n</pre>\n\n<code>optional string uri = 1;</code>\n\n@return The uri.",
      :setter-doc
        "<pre>\nURI reference of the attribution.\n</pre>\n\n<code>optional string uri = 1;</code>\n\n@param value The uri to set.\n@return This builder for chaining."}
     [:string {:min 1}]]]
   [:or
    [:map
     [:ragChunk
      {:optional true,
       :getter-doc
         "<pre>\nAdditional context for the RAG retrieval result. This is only populated\nwhen using the RAG retrieval tool.\n</pre>\n\n<code>.google.cloud.vertexai.v1.RagChunk rag_chunk = 4;</code>\n\n@return The ragChunk.",
       :setter-doc
         "<pre>\nAdditional context for the RAG retrieval result. This is only populated\nwhen using the RAG retrieval tool.\n</pre>\n\n<code>.google.cloud.vertexai.v1.RagChunk rag_chunk = 4;</code>"}
      :gcp.vertexai.api/RagChunk]]]])

(defn
  ^GroundingChunk$Maps$PlaceAnswerSources$ReviewSnippet Maps$PlaceAnswerSources$ReviewSnippet-from-edn
  [arg]
  (let [builder
          (GroundingChunk$Maps$PlaceAnswerSources$ReviewSnippet/newBuilder)]
    (when (some? (get arg :googleMapsUri))
      (.setGoogleMapsUri builder (get arg :googleMapsUri)))
    (when (some? (get arg :reviewId))
      (.setReviewId builder (get arg :reviewId)))
    (when (some? (get arg :title)) (.setTitle builder (get arg :title)))
    (.build builder)))

(defn
  Maps$PlaceAnswerSources$ReviewSnippet-to-edn
  [^GroundingChunk$Maps$PlaceAnswerSources$ReviewSnippet arg]
  (when arg
    (cond-> {}
      (some->> (.getGoogleMapsUri arg)
               (not= ""))
        (assoc :googleMapsUri (.getGoogleMapsUri arg))
      (some->> (.getReviewId arg)
               (not= ""))
        (assoc :reviewId (.getReviewId arg))
      (some->> (.getTitle arg)
               (not= ""))
        (assoc :title (.getTitle arg)))))

(def Maps$PlaceAnswerSources$ReviewSnippet-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nEncapsulates a review snippet.\n</pre>\n\nProtobuf type {@code\ngoogle.cloud.vertexai.v1.GroundingChunk.Maps.PlaceAnswerSources.ReviewSnippet}",
    :gcp/category :nested/protobuf-message,
    :gcp/key
      :gcp.vertexai.api/GroundingChunk.Maps.PlaceAnswerSources.ReviewSnippet}
   [:googleMapsUri
    {:optional true,
     :getter-doc
       "<pre>\nA link to show the review on Google Maps.\n</pre>\n\n<code>string google_maps_uri = 2;</code>\n\n@return The googleMapsUri.",
     :setter-doc
       "<pre>\nA link to show the review on Google Maps.\n</pre>\n\n<code>string google_maps_uri = 2;</code>\n\n@param value The googleMapsUri to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:reviewId
    {:optional true,
     :getter-doc
       "<pre>\nId of the review referencing the place.\n</pre>\n\n<code>string review_id = 1;</code>\n\n@return The reviewId.",
     :setter-doc
       "<pre>\nId of the review referencing the place.\n</pre>\n\n<code>string review_id = 1;</code>\n\n@param value The reviewId to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:title
    {:optional true,
     :getter-doc
       "<pre>\nTitle of the review.\n</pre>\n\n<code>string title = 3;</code>\n\n@return The title.",
     :setter-doc
       "<pre>\nTitle of the review.\n</pre>\n\n<code>string title = 3;</code>\n\n@param value The title to set.\n@return This builder for chaining."}
    [:string {:min 1}]]])

(defn
  ^GroundingChunk$Maps$PlaceAnswerSources$ReviewSnippet Maps$PlaceAnswerSources$ReviewSnippet-from-edn
  [arg]
  (let [builder
          (GroundingChunk$Maps$PlaceAnswerSources$ReviewSnippet/newBuilder)]
    (when (some? (get arg :googleMapsUri))
      (.setGoogleMapsUri builder (get arg :googleMapsUri)))
    (when (some? (get arg :reviewId))
      (.setReviewId builder (get arg :reviewId)))
    (when (some? (get arg :title)) (.setTitle builder (get arg :title)))
    (.build builder)))

(defn
  Maps$PlaceAnswerSources$ReviewSnippet-to-edn
  [^GroundingChunk$Maps$PlaceAnswerSources$ReviewSnippet arg]
  (when arg
    (cond-> {}
      (some->> (.getGoogleMapsUri arg)
               (not= ""))
        (assoc :googleMapsUri (.getGoogleMapsUri arg))
      (some->> (.getReviewId arg)
               (not= ""))
        (assoc :reviewId (.getReviewId arg))
      (some->> (.getTitle arg)
               (not= ""))
        (assoc :title (.getTitle arg)))))

(def Maps$PlaceAnswerSources$ReviewSnippet-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nEncapsulates a review snippet.\n</pre>\n\nProtobuf type {@code\ngoogle.cloud.vertexai.v1.GroundingChunk.Maps.PlaceAnswerSources.ReviewSnippet}",
    :gcp/category :nested/protobuf-message,
    :gcp/key
      :gcp.vertexai.api/GroundingChunk.Maps.PlaceAnswerSources.ReviewSnippet}
   [:googleMapsUri
    {:optional true,
     :getter-doc
       "<pre>\nA link to show the review on Google Maps.\n</pre>\n\n<code>string google_maps_uri = 2;</code>\n\n@return The googleMapsUri.",
     :setter-doc
       "<pre>\nA link to show the review on Google Maps.\n</pre>\n\n<code>string google_maps_uri = 2;</code>\n\n@param value The googleMapsUri to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:reviewId
    {:optional true,
     :getter-doc
       "<pre>\nId of the review referencing the place.\n</pre>\n\n<code>string review_id = 1;</code>\n\n@return The reviewId.",
     :setter-doc
       "<pre>\nId of the review referencing the place.\n</pre>\n\n<code>string review_id = 1;</code>\n\n@param value The reviewId to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:title
    {:optional true,
     :getter-doc
       "<pre>\nTitle of the review.\n</pre>\n\n<code>string title = 3;</code>\n\n@return The title.",
     :setter-doc
       "<pre>\nTitle of the review.\n</pre>\n\n<code>string title = 3;</code>\n\n@param value The title to set.\n@return This builder for chaining."}
    [:string {:min 1}]]])

(defn
  ^GroundingChunk$Maps$PlaceAnswerSources Maps$PlaceAnswerSources-from-edn
  [arg]
  (let [builder (GroundingChunk$Maps$PlaceAnswerSources/newBuilder)]
    (when (seq (get arg :reviewSnippets))
      (.addAllReviewSnippets builder
                             (map Maps$PlaceAnswerSources$ReviewSnippet-from-edn
                               (get arg :reviewSnippets))))
    (.build builder)))

(defn
  Maps$PlaceAnswerSources-to-edn
  [^GroundingChunk$Maps$PlaceAnswerSources arg]
  (when arg
    (cond-> {}
      (seq (.getReviewSnippetsList arg))
        (assoc :reviewSnippets
          (map Maps$PlaceAnswerSources$ReviewSnippet-to-edn
            (.getReviewSnippetsList arg))))))

(def Maps$PlaceAnswerSources-schema
  [:map
   {:closed true,
    :doc
      "Protobuf type {@code google.cloud.vertexai.v1.GroundingChunk.Maps.PlaceAnswerSources}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/GroundingChunk.Maps.PlaceAnswerSources}
   [:reviewSnippets
    {:optional true,
     :getter-doc
       "<pre>\nSnippets of reviews that are used to generate the answer.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.GroundingChunk.Maps.PlaceAnswerSources.ReviewSnippet review_snippets = 1;\n</code>",
     :setter-doc
       "<pre>\nSnippets of reviews that are used to generate the answer.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.GroundingChunk.Maps.PlaceAnswerSources.ReviewSnippet review_snippets = 1;\n</code>"}
    [:sequential {:min 1}
     [:ref
      :gcp.vertexai.api/GroundingChunk.Maps.PlaceAnswerSources.ReviewSnippet]]]])

(defn
  ^GroundingChunk$Maps$PlaceAnswerSources$ReviewSnippet Maps$PlaceAnswerSources$ReviewSnippet-from-edn
  [arg]
  (let [builder
          (GroundingChunk$Maps$PlaceAnswerSources$ReviewSnippet/newBuilder)]
    (when (some? (get arg :googleMapsUri))
      (.setGoogleMapsUri builder (get arg :googleMapsUri)))
    (when (some? (get arg :reviewId))
      (.setReviewId builder (get arg :reviewId)))
    (when (some? (get arg :title)) (.setTitle builder (get arg :title)))
    (.build builder)))

(defn
  Maps$PlaceAnswerSources$ReviewSnippet-to-edn
  [^GroundingChunk$Maps$PlaceAnswerSources$ReviewSnippet arg]
  (when arg
    (cond-> {}
      (some->> (.getGoogleMapsUri arg)
               (not= ""))
        (assoc :googleMapsUri (.getGoogleMapsUri arg))
      (some->> (.getReviewId arg)
               (not= ""))
        (assoc :reviewId (.getReviewId arg))
      (some->> (.getTitle arg)
               (not= ""))
        (assoc :title (.getTitle arg)))))

(def Maps$PlaceAnswerSources$ReviewSnippet-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nEncapsulates a review snippet.\n</pre>\n\nProtobuf type {@code\ngoogle.cloud.vertexai.v1.GroundingChunk.Maps.PlaceAnswerSources.ReviewSnippet}",
    :gcp/category :nested/protobuf-message,
    :gcp/key
      :gcp.vertexai.api/GroundingChunk.Maps.PlaceAnswerSources.ReviewSnippet}
   [:googleMapsUri
    {:optional true,
     :getter-doc
       "<pre>\nA link to show the review on Google Maps.\n</pre>\n\n<code>string google_maps_uri = 2;</code>\n\n@return The googleMapsUri.",
     :setter-doc
       "<pre>\nA link to show the review on Google Maps.\n</pre>\n\n<code>string google_maps_uri = 2;</code>\n\n@param value The googleMapsUri to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:reviewId
    {:optional true,
     :getter-doc
       "<pre>\nId of the review referencing the place.\n</pre>\n\n<code>string review_id = 1;</code>\n\n@return The reviewId.",
     :setter-doc
       "<pre>\nId of the review referencing the place.\n</pre>\n\n<code>string review_id = 1;</code>\n\n@param value The reviewId to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:title
    {:optional true,
     :getter-doc
       "<pre>\nTitle of the review.\n</pre>\n\n<code>string title = 3;</code>\n\n@return The title.",
     :setter-doc
       "<pre>\nTitle of the review.\n</pre>\n\n<code>string title = 3;</code>\n\n@param value The title to set.\n@return This builder for chaining."}
    [:string {:min 1}]]])

(defn
  ^GroundingChunk$Maps$PlaceAnswerSources$ReviewSnippet Maps$PlaceAnswerSources$ReviewSnippet-from-edn
  [arg]
  (let [builder
          (GroundingChunk$Maps$PlaceAnswerSources$ReviewSnippet/newBuilder)]
    (when (some? (get arg :googleMapsUri))
      (.setGoogleMapsUri builder (get arg :googleMapsUri)))
    (when (some? (get arg :reviewId))
      (.setReviewId builder (get arg :reviewId)))
    (when (some? (get arg :title)) (.setTitle builder (get arg :title)))
    (.build builder)))

(defn
  Maps$PlaceAnswerSources$ReviewSnippet-to-edn
  [^GroundingChunk$Maps$PlaceAnswerSources$ReviewSnippet arg]
  (when arg
    (cond-> {}
      (some->> (.getGoogleMapsUri arg)
               (not= ""))
        (assoc :googleMapsUri (.getGoogleMapsUri arg))
      (some->> (.getReviewId arg)
               (not= ""))
        (assoc :reviewId (.getReviewId arg))
      (some->> (.getTitle arg)
               (not= ""))
        (assoc :title (.getTitle arg)))))

(def Maps$PlaceAnswerSources$ReviewSnippet-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nEncapsulates a review snippet.\n</pre>\n\nProtobuf type {@code\ngoogle.cloud.vertexai.v1.GroundingChunk.Maps.PlaceAnswerSources.ReviewSnippet}",
    :gcp/category :nested/protobuf-message,
    :gcp/key
      :gcp.vertexai.api/GroundingChunk.Maps.PlaceAnswerSources.ReviewSnippet}
   [:googleMapsUri
    {:optional true,
     :getter-doc
       "<pre>\nA link to show the review on Google Maps.\n</pre>\n\n<code>string google_maps_uri = 2;</code>\n\n@return The googleMapsUri.",
     :setter-doc
       "<pre>\nA link to show the review on Google Maps.\n</pre>\n\n<code>string google_maps_uri = 2;</code>\n\n@param value The googleMapsUri to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:reviewId
    {:optional true,
     :getter-doc
       "<pre>\nId of the review referencing the place.\n</pre>\n\n<code>string review_id = 1;</code>\n\n@return The reviewId.",
     :setter-doc
       "<pre>\nId of the review referencing the place.\n</pre>\n\n<code>string review_id = 1;</code>\n\n@param value The reviewId to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:title
    {:optional true,
     :getter-doc
       "<pre>\nTitle of the review.\n</pre>\n\n<code>string title = 3;</code>\n\n@return The title.",
     :setter-doc
       "<pre>\nTitle of the review.\n</pre>\n\n<code>string title = 3;</code>\n\n@param value The title to set.\n@return This builder for chaining."}
    [:string {:min 1}]]])

(defn
  ^GroundingChunk$Maps$PlaceAnswerSources Maps$PlaceAnswerSources-from-edn
  [arg]
  (let [builder (GroundingChunk$Maps$PlaceAnswerSources/newBuilder)]
    (when (seq (get arg :reviewSnippets))
      (.addAllReviewSnippets builder
                             (map Maps$PlaceAnswerSources$ReviewSnippet-from-edn
                               (get arg :reviewSnippets))))
    (.build builder)))

(defn
  Maps$PlaceAnswerSources-to-edn
  [^GroundingChunk$Maps$PlaceAnswerSources arg]
  (when arg
    (cond-> {}
      (seq (.getReviewSnippetsList arg))
        (assoc :reviewSnippets
          (map Maps$PlaceAnswerSources$ReviewSnippet-to-edn
            (.getReviewSnippetsList arg))))))

(def Maps$PlaceAnswerSources-schema
  [:map
   {:closed true,
    :doc
      "Protobuf type {@code google.cloud.vertexai.v1.GroundingChunk.Maps.PlaceAnswerSources}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/GroundingChunk.Maps.PlaceAnswerSources}
   [:reviewSnippets
    {:optional true,
     :getter-doc
       "<pre>\nSnippets of reviews that are used to generate the answer.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.GroundingChunk.Maps.PlaceAnswerSources.ReviewSnippet review_snippets = 1;\n</code>",
     :setter-doc
       "<pre>\nSnippets of reviews that are used to generate the answer.\n</pre>\n\n<code>\nrepeated .google.cloud.vertexai.v1.GroundingChunk.Maps.PlaceAnswerSources.ReviewSnippet review_snippets = 1;\n</code>"}
    [:sequential {:min 1}
     [:ref
      :gcp.vertexai.api/GroundingChunk.Maps.PlaceAnswerSources.ReviewSnippet]]]])

(defn ^GroundingChunk$Maps Maps-from-edn
  [arg]
  (let [builder (GroundingChunk$Maps/newBuilder)]
    (when (some? (get arg :placeAnswerSources))
      (.setPlaceAnswerSources builder
                              (Maps$PlaceAnswerSources-from-edn
                                (get arg :placeAnswerSources))))
    (when (some? (get arg :placeId)) (.setPlaceId builder (get arg :placeId)))
    (when (some? (get arg :text)) (.setText builder (get arg :text)))
    (when (some? (get arg :title)) (.setTitle builder (get arg :title)))
    (when (some? (get arg :uri)) (.setUri builder (get arg :uri)))
    (.build builder)))

(defn Maps-to-edn
  [^GroundingChunk$Maps arg]
  (when arg
    (cond-> {}
      (.hasPlaceAnswerSources arg) (assoc :placeAnswerSources
                                     (Maps$PlaceAnswerSources-to-edn
                                       (.getPlaceAnswerSources arg)))
      (.hasPlaceId arg) (assoc :placeId (.getPlaceId arg))
      (.hasText arg) (assoc :text (.getText arg))
      (.hasTitle arg) (assoc :title (.getTitle arg))
      (.hasUri arg) (assoc :uri (.getUri arg)))))

(def Maps-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nChunk from Google Maps.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GroundingChunk.Maps}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.vertexai.api/GroundingChunk.Maps}
   [:placeAnswerSources
    {:optional true,
     :getter-doc
       "<pre>\nSources used to generate the place answer.\nThis includes review snippets and photos that were used to generate the\nanswer, as well as uris to flag content.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.GroundingChunk.Maps.PlaceAnswerSources place_answer_sources = 5;\n</code>\n\n@return The placeAnswerSources.",
     :setter-doc
       "<pre>\nSources used to generate the place answer.\nThis includes review snippets and photos that were used to generate the\nanswer, as well as uris to flag content.\n</pre>\n\n<code>\n.google.cloud.vertexai.v1.GroundingChunk.Maps.PlaceAnswerSources place_answer_sources = 5;\n</code>"}
    [:ref :gcp.vertexai.api/GroundingChunk.Maps.PlaceAnswerSources]]
   [:placeId
    {:optional true,
     :getter-doc
       "<pre>\nThis Place's resource name, in `places/{place_id}` format.  Can be used\nto look up the Place.\n</pre>\n\n<code>optional string place_id = 4;</code>\n\n@return The placeId.",
     :setter-doc
       "<pre>\nThis Place's resource name, in `places/{place_id}` format.  Can be used\nto look up the Place.\n</pre>\n\n<code>optional string place_id = 4;</code>\n\n@param value The placeId to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:text
    {:optional true,
     :getter-doc
       "<pre>\nText of the chunk.\n</pre>\n\n<code>optional string text = 3;</code>\n\n@return The text.",
     :setter-doc
       "<pre>\nText of the chunk.\n</pre>\n\n<code>optional string text = 3;</code>\n\n@param value The text to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:title
    {:optional true,
     :getter-doc
       "<pre>\nTitle of the chunk.\n</pre>\n\n<code>optional string title = 2;</code>\n\n@return The title.",
     :setter-doc
       "<pre>\nTitle of the chunk.\n</pre>\n\n<code>optional string title = 2;</code>\n\n@param value The title to set.\n@return This builder for chaining."}
    [:string {:min 1}]]
   [:uri
    {:optional true,
     :getter-doc
       "<pre>\nURI reference of the chunk.\n</pre>\n\n<code>optional string uri = 1;</code>\n\n@return The uri.",
     :setter-doc
       "<pre>\nURI reference of the chunk.\n</pre>\n\n<code>optional string uri = 1;</code>\n\n@param value The uri to set.\n@return This builder for chaining."}
    [:string {:min 1}]]])

(def ChunkTypeCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.vertexai.api/GroundingChunk.ChunkTypeCase} "WEB"
   "RETRIEVED_CONTEXT" "MAPS" "CHUNKTYPE_NOT_SET"])

(defn ^GroundingChunk from-edn
  [arg]
  (global/strict! :gcp.vertexai.api/GroundingChunk arg)
  (let [builder (GroundingChunk/newBuilder)]
    (cond (contains? arg :maps) (.setMaps builder
                                          (Maps-from-edn (get arg :maps)))
          (contains? arg :retrievedContext) (.setRetrievedContext
                                              builder
                                              (RetrievedContext-from-edn
                                                (get arg :retrievedContext)))
          (contains? arg :web) (.setWeb builder (Web-from-edn (get arg :web))))
    (.build builder)))

(defn to-edn
  [^GroundingChunk arg]
  {:post [(global/strict! :gcp.vertexai.api/GroundingChunk %)]}
  (when arg
    (let [res (cond-> {})
          res (case (.name (.getChunkTypeCase arg))
                "MAPS" (assoc res :maps (Maps-to-edn (.getMaps arg)))
                "RETRIEVED_CONTEXT" (assoc res
                                      :retrievedContext (RetrievedContext-to-edn
                                                          (.getRetrievedContext
                                                            arg)))
                "WEB" (assoc res :web (Web-to-edn (.getWeb arg)))
                res)]
      res)))

(def schema
  [:and
   {:closed true,
    :doc
      "<pre>\nGrounding chunk.\n</pre>\n\nProtobuf type {@code google.cloud.vertexai.v1.GroundingChunk}",
    :gcp/category :union-protobuf-oneof,
    :gcp/key :gcp.vertexai.api/GroundingChunk}
   [:map {:closed true}
    [:maps
     {:optional true,
      :getter-doc
        "<pre>\nGrounding chunk from Google Maps.\n</pre>\n\n<code>.google.cloud.vertexai.v1.GroundingChunk.Maps maps = 3;</code>\n\n@return The maps.",
      :setter-doc
        "<pre>\nGrounding chunk from Google Maps.\n</pre>\n\n<code>.google.cloud.vertexai.v1.GroundingChunk.Maps maps = 3;</code>"}
     [:ref :gcp.vertexai.api/GroundingChunk.Maps]]
    [:retrievedContext
     {:optional true,
      :getter-doc
        "<pre>\nGrounding chunk from context retrieved by the retrieval tools.\n</pre>\n\n<code>.google.cloud.vertexai.v1.GroundingChunk.RetrievedContext retrieved_context = 2;</code>\n\n@return The retrievedContext.",
      :setter-doc
        "<pre>\nGrounding chunk from context retrieved by the retrieval tools.\n</pre>\n\n<code>.google.cloud.vertexai.v1.GroundingChunk.RetrievedContext retrieved_context = 2;</code>"}
     [:ref :gcp.vertexai.api/GroundingChunk.RetrievedContext]]
    [:web
     {:optional true,
      :getter-doc
        "<pre>\nGrounding chunk from the web.\n</pre>\n\n<code>.google.cloud.vertexai.v1.GroundingChunk.Web web = 1;</code>\n\n@return The web.",
      :setter-doc
        "<pre>\nGrounding chunk from the web.\n</pre>\n\n<code>.google.cloud.vertexai.v1.GroundingChunk.Web web = 1;</code>"}
     [:ref :gcp.vertexai.api/GroundingChunk.Web]]]
   [:or
    [:map
     [:maps
      {:optional true,
       :getter-doc
         "<pre>\nGrounding chunk from Google Maps.\n</pre>\n\n<code>.google.cloud.vertexai.v1.GroundingChunk.Maps maps = 3;</code>\n\n@return The maps.",
       :setter-doc
         "<pre>\nGrounding chunk from Google Maps.\n</pre>\n\n<code>.google.cloud.vertexai.v1.GroundingChunk.Maps maps = 3;</code>"}
      [:ref :gcp.vertexai.api/GroundingChunk.Maps]]]
    [:map
     [:retrievedContext
      {:optional true,
       :getter-doc
         "<pre>\nGrounding chunk from context retrieved by the retrieval tools.\n</pre>\n\n<code>.google.cloud.vertexai.v1.GroundingChunk.RetrievedContext retrieved_context = 2;</code>\n\n@return The retrievedContext.",
       :setter-doc
         "<pre>\nGrounding chunk from context retrieved by the retrieval tools.\n</pre>\n\n<code>.google.cloud.vertexai.v1.GroundingChunk.RetrievedContext retrieved_context = 2;</code>"}
      [:ref :gcp.vertexai.api/GroundingChunk.RetrievedContext]]]
    [:map
     [:web
      {:optional true,
       :getter-doc
         "<pre>\nGrounding chunk from the web.\n</pre>\n\n<code>.google.cloud.vertexai.v1.GroundingChunk.Web web = 1;</code>\n\n@return The web.",
       :setter-doc
         "<pre>\nGrounding chunk from the web.\n</pre>\n\n<code>.google.cloud.vertexai.v1.GroundingChunk.Web web = 1;</code>"}
      [:ref :gcp.vertexai.api/GroundingChunk.Web]]]]])

(global/include-schema-registry!
  (with-meta
    {:gcp.vertexai.api/GroundingChunk schema,
     :gcp.vertexai.api/GroundingChunk.ChunkTypeCase ChunkTypeCase-schema,
     :gcp.vertexai.api/GroundingChunk.Maps Maps-schema,
     :gcp.vertexai.api/GroundingChunk.Maps.PlaceAnswerSources
       Maps$PlaceAnswerSources-schema,
     :gcp.vertexai.api/GroundingChunk.Maps.PlaceAnswerSources.ReviewSnippet
       Maps$PlaceAnswerSources$ReviewSnippet-schema,
     :gcp.vertexai.api/GroundingChunk.RetrievedContext RetrievedContext-schema,
     :gcp.vertexai.api/GroundingChunk.RetrievedContext.ContextDetailsCase
       RetrievedContext$ContextDetailsCase-schema,
     :gcp.vertexai.api/GroundingChunk.Web Web-schema}
    {:gcp.global/name "gcp.vertexai.api.GroundingChunk"}))