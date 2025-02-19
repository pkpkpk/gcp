(ns gcp.vertexai.v1.api.SafetySetting
  (:refer-clojure :exclude [class])
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.HarmCategory :as hc])
  (:import (com.google.cloud.vertexai.api SafetySetting
                                          SafetySetting$HarmBlockThreshold)))

(defn ^SafetySetting from-edn
  [{:keys [threshold category] :as arg}]
  (global/strict! :vertexai.api/SafetySetting arg)
  (let [builder (SafetySetting/newBuilder)
        threshold (if (number? threshold)
                    (SafetySetting$HarmBlockThreshold/forNumber (int threshold))
                    (SafetySetting$HarmBlockThreshold/valueOf ^String threshold))]
    (.setThreshold builder threshold)
    (.setCategory builder (hc/from-edn category))
    (.build builder)))

(defn to-edn
  [^SafetySetting ss]
  {:post [(global/strict! :vertexai.api/SafetySetting %)]}
  {:category  (hc/to-edn (.getCategory ss))
   :threshold (.name (.getThreshold ss))})