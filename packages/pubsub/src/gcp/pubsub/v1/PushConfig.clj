(ns gcp.pubsub.v1.PushConfig
  (:require [clojure.string :as string]
            [gcp.global :as global])
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

(def schemas
  {:gcp.pubsub.v1/PushConfig
   [:map
    [:pushEndpoint {:optional true} :string]
    [:attributes {:optional true} [:map-of :string :string]]
    ;; oneof authentication_method
    [:oidcToken {:optional true} :any]
    ;; oneof wrapper
    [:pubsubWrapper {:optional true} :any]
    [:noWrapper {:optional true} :any]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))