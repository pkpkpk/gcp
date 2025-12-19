(ns gcp.vertexai.v1.api.CachedContent
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.Content :as Content]
            [gcp.vertexai.v1.api.Tool :as Tool]
            [gcp.vertexai.v1.api.ToolConfig :as ToolConfig]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api CachedContent CachedContent$UsageMetadata)))

(defn ^CachedContent$UsageMetadata UsageMetadata-from-edn [arg]
  (let [builder (CachedContent$UsageMetadata/newBuilder)]
    (some->> (:totalTokenCount arg) (.setTotalTokenCount builder))
    (some->> (:textCount arg) (.setTextCount builder))
    (some->> (:imageCount arg) (.setImageCount builder))
    (some->> (:videoDurationSeconds arg) (.setVideoDurationSeconds builder))
    (some->> (:audioDurationSeconds arg) (.setAudioDurationSeconds builder))
    (.build builder)))

(defn UsageMetadata-to-edn [^CachedContent$UsageMetadata arg]
  {:totalTokenCount (.getTotalTokenCount arg)
   :textCount       (.getTextCount arg)
   :imageCount      (.getImageCount arg)
   :videoDurationSeconds (.getVideoDurationSeconds arg)
   :audioDurationSeconds (.getAudioDurationSeconds arg)})

(defn ^CachedContent from-edn
  [{:keys [name
           displayName
           model
           systemInstruction
           contents
           tools
           toolConfig
           createTime
           updateTime
           expireTime
           ttl
           usageMetadata] :as arg}]
  (global/strict! :gcp.vertexai.v1.api/CachedContent arg)
  (let [builder (CachedContent/newBuilder)]
    (some->> name (.setName builder))
    (some->> displayName (.setDisplayName builder))
    (some->> model (.setModel builder))
    (some->> systemInstruction Content/from-edn (.setSystemInstruction builder))
    (some->> contents (map Content/from-edn) (.addAllContents builder))
    (some->> tools (map Tool/from-edn) (.addAllTools builder))
    (some->> toolConfig ToolConfig/from-edn (.setToolConfig builder))
    (some->> createTime (.setCreateTime builder)) ; Timestamp (protobuf)
    (some->> updateTime (.setUpdateTime builder)) ; Timestamp (protobuf)
    (when expireTime (.setExpireTime builder expireTime)) ; Timestamp (protobuf)
    (when ttl (.setTtl builder (protobuf/Duration-from-edn ttl))) ; Duration (protobuf)
    (some->> usageMetadata UsageMetadata-from-edn (.setUsageMetadata builder))
    (.build builder)))

(defn to-edn [^CachedContent arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/CachedContent %)]}
  (cond-> {}
          (not (empty? (.getName arg)))
          (assoc :name (.getName arg))
          (not (empty? (.getDisplayName arg)))
          (assoc :displayName (.getDisplayName arg))
          (not (empty? (.getModel arg)))
          (assoc :model (.getModel arg))
          (.hasSystemInstruction arg)
          (assoc :systemInstruction (Content/to-edn (.getSystemInstruction arg)))
          (pos? (.getContentsCount arg))
          (assoc :contents (mapv Content/to-edn (.getContentsList arg)))
          (pos? (.getToolsCount arg))
          (assoc :tools (mapv Tool/to-edn (.getToolsList arg)))
          (.hasToolConfig arg)
          (assoc :toolConfig (ToolConfig/to-edn (.getToolConfig arg)))
          (.hasCreateTime arg)
          (assoc :createTime (.getCreateTime arg)) ; Timestamp
          (.hasUpdateTime arg)
          (assoc :updateTime (.getUpdateTime arg)) ; Timestamp
          (.hasExpireTime arg)
          (assoc :expireTime (.getExpireTime arg)) ; Timestamp
          (.hasTtl arg)
          (assoc :ttl (protobuf/Duration-to-edn (.getTtl arg)))
          (.hasUsageMetadata arg)
          (assoc :usageMetadata (UsageMetadata-to-edn (.getUsageMetadata arg)))))

(def schema
  [:map
   {:doc              "A cached content object that can be used to store and reuse previous generated model responses to reduce costs. The resource name of the CachedContent. Format: `projects/{project}/locations/{location}/cachedContents/{cached_content}`. This class is part of the [Generative API](https://cloud.google.com/vertex-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.CachedContent) of Vertex AI. The class is annotated with Protobuf message type for interoperability."
    :class            'com.google.cloud.vertexai.api.CachedContent
    :protobuf/type    "google.cloud.vertexai.v1.CachedContent"}
   [:name {:optional true} :string]
   [:displayName {:optional true} :string]
   [:model {:optional true} :string]
   [:systemInstruction {:optional true} :gcp.vertexai.v1.api/Content]
   [:contents {:optional true} [:sequential :gcp.vertexai.v1.api/Content]]
   [:tools {:optional true} [:sequential :gcp.vertexai.v1.api/Tool]]
   [:toolConfig {:optional true} :gcp.vertexai.v1.api/ToolConfig]
   [:createTime {:optional true} :gcp.protobuf/Timestamp]
   [:updateTime {:optional true} :gcp.protobuf/Timestamp]
   [:expireTime {:optional true} :gcp.protobuf/Timestamp]
   [:ttl {:optional true} :gcp.protobuf/Duration]
   [:usageMetadata {:optional true} [:map
                                     [:totalTokenCount {:optional true} :int]
                                     [:textCount {:optional true} :int]
                                     [:imageCount {:optional true} :int]
                                     [:videoDurationSeconds {:optional true} :double]
                                     [:audioDurationSeconds {:optional true} :double]]]])

(global/register-schema! :gcp.vertexai.v1.api/CachedContent schema)
