(ns gcp.vertexai.v1.api.TokensInfo
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api TokensInfo)))

(defn ^TokensInfo from-edn
  [{:keys [tokens tokenIds role] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/TokensInfo arg)
  (let [builder (TokensInfo/newBuilder)]
    (some->> tokens (map protobuf/bytestring-from-edn) (.addAllTokens builder))
    (some->> tokenIds (map long) (.addAllTokenIds builder))
    (some->> role (.setRole builder))
    (.build builder)))

(defn to-edn [^TokensInfo arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/TokensInfo %)]}
  (cond-> {}
          (pos? (.getTokensCount arg))
          (assoc :tokens (protobuf/protocolstringlist-to-edn (.getTokensList arg)))
          (pos? (.getTokenIdsCount arg))
          (assoc :tokenIds (vec (.getTokenIdsList arg)))
          (not (empty? (.getRole arg)))
          (assoc :role (.getRole arg))))

(def schema
  [:map
   {:doc              "Information about tokens. This class is part of the [Generative API](https://cloud.google.com/vertex-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.TokensInfo) of Vertex AI. The class is annotated with Protobuf message type for interoperability."
    :class            'com.google.cloud.vertexai.api.TokensInfo
    :protobuf/type    "google.cloud.vertexai.v1.TokensInfo"}
   [:tokens {:optional true} [:sequential :gcp.protobuf/ByteString]]
   [:tokenIds {:optional true} [:sequential :int]]
   [:role {:optional true} :string]])

(global/register-schema! :gcp.vertexai.v1.api/TokensInfo schema)
