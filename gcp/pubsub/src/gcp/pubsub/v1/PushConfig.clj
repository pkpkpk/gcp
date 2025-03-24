(ns gcp.pubsub.v1.PushConfig
  (:require [clojure.string :as string])
  (:import (com.google.pubsub.v1 PushConfig)))

(defn to-edn [^PushConfig arg]
  (cond-> {}

          (not (string/blank? (.getPushEndpoint arg)))
          (assoc :pushEndpoint (.getPushEndpoint arg))

          (.hasPubsubWrapper arg)
          (assoc :pubsubWrapper (.getPubsubWrapper arg))

          (.hasOidcToken arg)
          (assoc :oidcToken (.getOidcToken arg))

          (.hasNoWrapper arg)
          (assoc :noWrapper (.getNoWrapper arg))

          (pos? (.getAttributesCount arg))
          (assoc :attributes (.getAttributesMap arg))

          (some? (.getAuthenticationMethodCase arg))
          (assoc :authenticationMethodCase (.getAuthenticationMethodCase arg))))

(defn ^PushConfig from-edn [arg] (throw (Exception. "unimplemented")))