(ns gcp.vertexai.v1.api.GroundingChunk
  (:require [gcp.global :as global])
  (:import [com.google.cloud.vertexai.api GroundingChunk
                                          GroundingChunk$ChunkTypeCase
                                          GroundingChunk$RetrievedContext
                                          GroundingChunk$Web]))

(defn ^GroundingChunk$Web Web-from-edn [arg]
  (let [builder (GroundingChunk$Web/newBuilder)]
    (.setTitle builder (:title arg))
    (.setUri builder (:uri arg))
    (.build builder)))

(defn Web-to-edn
  [^GroundingChunk$Web c]
  (cond-> {}
          (.hasUri c) (assoc :uri (.getUri c))
          (.hasTitle c) (assoc :title (.getTitle c))))

(defn ^GroundingChunk$RetrievedContext RetrievedContext-from-edn [arg]
  (let [builder (GroundingChunk$RetrievedContext/newBuilder)]
    (.setTitle builder (:title arg))
    (.setUri builder (:uri arg))
    (.build builder)))

(defn RetrievedContext-to-edn
  [^GroundingChunk$RetrievedContext arg]
  (cond-> {}
          (.hasUri arg)
          (assoc :uri (.getUri arg))
          (.hasTitle arg)
          (assoc :title (.getTitle arg))))

(defn ^GroundingChunk from-edn [arg]
  (global/strict! :vertexai.api/GroundingChunk arg)
  (if (contains? arg :web)
    (Web-from-edn (:web arg))
    (RetrievedContext-from-edn (:retrievedContext arg))))

(defn to-edn [^GroundingChunk arg]
  {:post [(global/strict! :vertexai.api/GroundingChunk %)]}
  (condp = (.getChunkTypeCase arg)
    GroundingChunk$ChunkTypeCase/WEB {:web (Web-to-edn (.getWeb arg))}
    GroundingChunk$ChunkTypeCase/RETRIEVED_CONTEXT {:retrievedContext (RetrievedContext-to-edn (.getRetrievedContext arg))}
    (throw (ex-info "cannot create edn from unknown grounding chuck type"
                    {:chunk arg
                     :chunk/type (.getChunkTypeCase arg)}))))
