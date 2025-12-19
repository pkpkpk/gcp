(ns gcp.vertexai.v1.api.GroundingChunk
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import [com.google.cloud.vertexai.api GroundingChunk
                                          GroundingChunk$ChunkTypeCase
                                          GroundingChunk$RetrievedContext
                                          GroundingChunk$Web]))

(defn ^GroundingChunk$Web Web-from-edn [arg]
  (let [builder (GroundingChunk$Web/newBuilder)]
    (some->> (:title arg) (.setTitle builder))
    (some->> (:uri arg) protobuf/bytestring-from-edn (.setUri builder))
    (.build builder)))

(defn Web-to-edn
  [^GroundingChunk$Web c]
  (cond-> {}
          (.hasUri c) (assoc :uri (protobuf/bytestring-to-edn (.getUri c)))
          (.hasTitle c) (assoc :title (.getTitle c))))

(defn ^GroundingChunk$RetrievedContext RetrievedContext-from-edn [arg]
  (let [builder (GroundingChunk$RetrievedContext/newBuilder)]
    (some->> (:title arg) (.setTitle builder))
    (some->> (:uri arg) protobuf/bytestring-from-edn (.setUri builder))
    (.build builder)))

(defn RetrievedContext-to-edn
  [^GroundingChunk$RetrievedContext arg]
  (cond-> {}
          (.hasUri arg)
          (assoc :uri (protobuf/bytestring-to-edn (.getUri arg)))
          (.hasTitle arg)
          (assoc :title (.getTitle arg))))

(defn ^GroundingChunk from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/GroundingChunk arg)
  (if (contains? arg :web)
    (let [{:keys [title uri]} (:web arg)]
      (.setWeb (GroundingChunk/newBuilder) (Web-from-edn {:title title :uri uri})))
    (let [{:keys [title uri]} (:retrievedContext arg)]
      (.setRetrievedContext (GroundingChunk/newBuilder) (RetrievedContext-from-edn {:title title :uri uri}))))) ;; TODO check this - returns builder not chunk

(defn to-edn [^GroundingChunk arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/GroundingChunk %)]}
  (condp = (.getChunkTypeCase arg)
    GroundingChunk$ChunkTypeCase/WEB {:web (Web-to-edn (.getWeb arg))}
    GroundingChunk$ChunkTypeCase/RETRIEVED_CONTEXT {:retrievedContext (RetrievedContext-to-edn (.getRetrievedContext arg))}
    (throw (ex-info "cannot create edn from unknown grounding chuck type"
                    {:chunk arg
                     :chunk/type (.getChunkTypeCase arg)}))))

(def schema
  [:or
   {:ns               'gcp.vertexai.v1.api.GroundingChunk
    :from-edn         'gcp.vertexai.v1.api.GroundingChunk/from-edn
    :to-edn           'gcp.vertexai.v1.api.GroundingChunk/to-edn
    :doc              "union type of :web or :retrievedContext"
    :generativeai/url "https://ai.google.dev/api/generate-content#GroundingChunk"
    :protobuf/type    "google.cloud.vertexai.v1.GroundingChunk"
    :class            'com.google.cloud.vertexai.api.GroundingChunk
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.GroundingChunk"}
   [:map {:closed true}
    [:web [:map
           [:title {:optional true} :string]
           [:uri {:optional true} :gcp.protobuf/ByteString]]]]
   [:map {:closed true}
    [:retrievedContext [:map
                        [:title {:optional true} :string]
                        [:uri {:optional true} :gcp.protobuf/ByteString]]]]])

(global/register-schema! :gcp.vertexai.v1.api/GroundingChunk schema)
