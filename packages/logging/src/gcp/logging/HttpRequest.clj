(ns gcp.logging.HttpRequest
  (:require [gcp.global :as g])
  (:import (com.google.cloud.logging HttpRequest HttpRequest$RequestMethod)))

(def schema
  (g/schema
    [:map
     [:cacheFillBytes :int]
     [:cacheHit :boolean]
     [:cacheLookup :boolean]
     [:cacheValidatedWithOriginServer :boolean]
     [:latency (g/instance-schema java.time.Duration)]
     [:referer :string]
     [:remoteIp :string]
     [:requestMethod [:enum "GET" "HEAD" "POST" "PUT"]]
     [:requestSize :int]
     [:requestUrl :string]
     [:responseSize :int]
     [:serverIp :string]
     [:status :int]
     [:userAgent :string]]))

(defn ^HttpRequest from-edn [arg]
  (g/strict! schema arg)
  (let [builder (HttpRequest/newBuilder)]
    (some->> (:cacheFillBytes arg) (.setCacheFillBytes builder))
    (some->> (:cacheHit arg) (.setCacheHit builder))
    (some->> (:cacheLookup arg) (.setCacheLookup builder))
    (some->> (:cacheValidatedWithOriginServer arg) (.setCacheValidatedWithOriginServer builder))
    (some->> (:latency arg) (.setLatencyDuration builder))
    (some->> (:referer arg) (.setReferer builder))
    (some->> (:remoteIp arg) (.setRemoteIp builder))
    (some->> (:requestMethod arg) HttpRequest$RequestMethod/valueOf (.setRequestMethod builder))
    (some->> (:requestSize arg) (.setRequestSize builder))
    (some->> (:requestUrl arg) (.setRequestUrl builder))
    (some->> (:responseSize arg) (.setResponseSize builder))
    (some->> (:serverIp arg) (.setServerIp builder))
    (some->> (:status arg) (.setStatus builder))
    (some->> (:userAgent arg) (.setUserAgent builder))
    (.build builder)))

(defn to-edn [^HttpRequest arg]
  (cond-> {}
          (some? (.getCacheFillBytes arg))
          (assoc :cacheFillBytes (.getCacheFillBytes arg))

          (some? (.cacheHit arg))
          (assoc :cacheHit (.cacheHit arg))

          (some? (.cacheLookup arg))
          (assoc :cacheLookup (.cacheLookup arg))

          (some? (.cacheValidatedWithOriginServer arg))
          (assoc :cacheValidatedWithOriginServer (.cacheValidatedWithOriginServer arg))

          (some? (.getLatencyDuration arg))
          (assoc :latency (.getLatencyDuration arg))

          (some? (.getReferer arg))
          (assoc :referer (.getReferer arg))

          (some? (.getRemoteIp arg))
          (assoc :remoteIp (.getRemoteIp arg))

          (some? (.getRequestMethod arg))
          (assoc :requestMethod (.name (.getRequestMethod arg)))

          (some? (.getRequestSize arg))
          (assoc :requestSize (.getRequestSize arg))

          (some? (.getRequestUrl arg))
          (assoc :requestUrl (.getRequestUrl arg))

          (some? (.getResponseSize arg))
          (assoc :responseSize (.getResponseSize arg))

          (some? (.getServerIp arg))
          (assoc :serverIp (.getServerIp arg))

          (some? (.getStatus arg))
          (assoc :status (.getStatus arg))

          (some? (.getUserAgent arg))
          (assoc :userAgent (.getUserAgent arg))))
