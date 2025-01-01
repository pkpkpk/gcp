(ns gcp.vertexai.v1.api.GroundingChunk
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import [com.google.cloud.vertexai.api GroundingChunk
                                          GroundingChunk$ChunkTypeCase
                                          GroundingChunk$RetrievedContext
                                          GroundingChunk$Web]))

(def ^{:class GroundingChunk$Web} Web-schema
  [:map
   [:title :string]
   [:uri [:or :string protobuf/bytestring-schema]]])

(defn ^GroundingChunk$Web Web-from-edn [arg]
  (global/strict! Web-schema arg)
  (if (instance? GroundingChunk$Web arg)
    arg
    (let [builder (GroundingChunk$Web/newBuilder)]
      (.setTitle builder (:title arg))
      (.setUri builder (:uri arg))
      (.build builder))))

(defn Web-to-edn
  [^GroundingChunk$Web c]
  (cond-> {}
          (.hasUri c) (assoc :uri (.getUri  c))
          (.hasTitle c) (assoc :title (.getTitle c))))

#!-----------------------------------------------------------------------------

(def ^{:class GroundingChunk$RetrievedContext}
  RetrievedContext-schema
  [:map
   [:title :string]
   [:uri [:or :string protobuf/bytestring-schema]]])

(defn ^GroundingChunk$RetrievedContext RetrievedContext-from-edn [arg]
  (global/strict! RetrievedContext-schema arg)
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

#!-----------------------------------------------------------------------------

(def ^{:class GroundingChunk
       :doc "union type of :web or :retrievedContext"}
  schema
  [:or
   [:map
    [:web Web-schema]]
   [:map
    [:retrievedContext RetrievedContext-schema]]])

(defn ^GroundingChunk from-edn [arg]
  (global/strict! schema arg)
  (if (contains? arg :web)
    (Web-from-edn (:web arg))
    (RetrievedContext-from-edn (:retrievedContext arg))))

(defn to-edn [^GroundingChunk arg]
  {:post [(global/strict! schema %)]}
  (condp = (.getChunkTypeCase arg)
    GroundingChunk$ChunkTypeCase/WEB {:web (Web-to-edn (.getWeb arg))}
    GroundingChunk$ChunkTypeCase/RETRIEVED_CONTEXT {:retrievedContext (RetrievedContext-to-edn (.getRetrievedContext arg))}
    (throw (ex-info "cannot create edn from unknown grounding chuck type"
                    {:chunk arg
                     :chunk/type (.getChunkTypeCase arg)}))))
