(ns gcp.foreign.com.google.rpc
  {:gcp.dev/certification
   {:Status
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767571334309
     :timestamp "2026-01-05T00:02:14.378490489Z"
     :passed-stages {:smoke 1767571334309
                     :standard 1767571334310
                     :stress 1767571334311}
     :source-hash "f45177128e98e248720fbb59f5b2c599f409d20f20f13f2971b52ac95860e759"}}}
  (:require [gcp.global :as global]
            [gcp.foreign.com.google.protobuf :as protobuf])
  (:import (com.google.rpc Status)))

(defn Status-from-edn [arg]
  (let [builder (Status/newBuilder)]
    (when-some [v (:code arg)] (.setCode builder v))
    (when-some [v (:message arg)] (.setMessage builder v))
    (when-some [v (:details arg)]
      (doseq [d v]
        (.addDetails builder (protobuf/Any-from-edn d))))
    (.build builder)))

(defn Status-to-edn [^Status arg]
  (cond-> {:code (.getCode arg)
           :message (.getMessage arg)}
    (> (.getDetailsCount arg) 0)
    (assoc :details (mapv protobuf/Any-to-edn (.getDetailsList arg)))))

(def registry
  (with-meta
    {:gcp.foreign.com.google.rpc/Status
     [:map
      [:code [:int {:min 0 :max 20}]]
      [:message :string]
      [:details {:optional true} [:sequential [:ref :gcp.foreign.com.google.protobuf/Any]]]]}
    {:gcp.global/name :gcp.foreign.com.google.rpc/registry}))

(global/include-schema-registry! registry)
