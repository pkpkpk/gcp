(ns gcp.logging.Payload
  (:require [gcp.global :as g]
            [gcp.protobuf :as p])
  (:import (com.google.cloud.logging Payload Payload$JsonPayload Payload$ProtoPayload Payload$StringPayload)))

(def schema
  (g/schema
    [:or
     [:map {:closed true}
      [:type [:= "STRING"]]
      [:data :string]]
     [:map {:closed true}
      [:type [:= "JSON"]]
      [:data :gcp.protobuf/Struct]]
     [:map {:closed true}
      [:type [:= "PROTO"]]
      [:data any?]]]))

(defn ^Payload from-edn
  [{:keys [type data] :as arg}]
  (g/strict! schema arg)
  (case type
    "STRING" (Payload$StringPayload/of data)
    "JSON" (Payload$JsonPayload/of (p/struct-from-edn data))
    (Payload$ProtoPayload/of data)))

(defn to-edn [^Payload arg]
  (let [T (.name (.getType arg))]
    (case T
      "STRING" {:type "STRING"
                :data (.getData arg)}
      "JSON" {:type "JSON"
              :data (p/struct-to-edn (.getData arg))}
      "PROTO" {:type "PROTO"
               :data (.getData arg)})))