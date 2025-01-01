(ns gcp.vertexai.v1.api.SafetySetting
  (:refer-clojure :exclude [class])
  (:require [gcp.global :as global]
            [gcp.vertexai.v1.api.HarmCategory :as hc])
  (:import (com.google.cloud.vertexai.api SafetySetting
                                          SafetySetting$HarmBlockThreshold)))

(def ^{:class SafetySetting
       :doc ""}
  schema
  [:map
   [:category {:optional false} hc/schema]
   [:threshold {:optional false}
    [:or {:doc "Probability based thresholds levels for blocking."
          :protobuf/type "google.cloud.vertexai.v1.SafetySetting.HarmBlockThreshold"}
     [:enum
      {:doc "Block low threshold and above (i.e. block more)."}
      "BLOCK_LOW_AND_ABOVE" 1]
     [:enum
      {:doc "Block medium threshold and above."}
      "BLOCK_MEDIUM_AND_ABOVE" 2]
     [:enum
      {:doc "Block none."}
      "BLOCK_NONE" 4]
     [:enum
      {:doc "Block only high threshold (i.e. block less)."}
      "BLOCK_ONLY_HIGH" 3]
     [:enum
      {:doc "Unspecified harm block threshold."}
      "HARM_BLOCK_THRESHOLD_UNSPECIFIED" 0]
     [:enum
      {:doc "Turn off the safety filter."}
      "OFF" 5]
     [:enum
      {:doc "No recognized value."}
      "UNRECOGNIZED"]]]])

(defn ^SafetySetting from-edn
  [{:keys [threshold category] :as arg}]
  (global/strict! schema arg)
  (let [builder (SafetySetting/newBuilder)
        threshold (if (number? threshold)
                    (SafetySetting$HarmBlockThreshold/forNumber (int threshold))
                    (SafetySetting$HarmBlockThreshold/valueOf ^String threshold))]
    (.setThreshold builder threshold)
    (.setCategory builder (hc/from-edn category))
    (.build builder)))

(defn to-edn
  [^SafetySetting ss]
  {:post [(global/strict! schema %)]}
  {:category  (hc/to-edn (.getCategory ss))
   :threshold (.name (.getThreshold ss))})