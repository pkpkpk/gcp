(ns gcp.foreign.com.google.longrunning
  {:gcp.dev/certification
   {:Operation
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767571341782
     :timestamp "2026-01-05T00:02:21.871738575Z"
     :passed-stages {:smoke 1767571341782
                     :standard 1767571341783
                     :stress 1767571341784}
     :source-hash "5d8c5ecc7dc812b336dc2483537402297e88dc3efde7809641bb79a190ad2816"}}}
  (:require [gcp.global :as global]
            [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.foreign.com.google.rpc :as rpc])
  (:import (com.google.longrunning Operation OperationsClient)))

(defn Operation-from-edn [arg]
  (let [builder (Operation/newBuilder)]
    (when-some [v (:name arg)] (.setName builder v))
    (when-some [v (:done arg)] (.setDone builder v))
    (when-some [v (:metadata arg)] (.setMetadata builder (protobuf/Any-from-edn v)))
    (when-some [v (:error arg)] (.setError builder (rpc/Status-from-edn v)))
    (when-some [v (:response arg)] (.setResponse builder (protobuf/Any-from-edn v)))
    (.build builder)))

(defn Operation-to-edn [^Operation arg]
  (cond-> {:name (.getName arg)
           :done (.getDone arg)}
    (.hasMetadata arg) (assoc :metadata (protobuf/Any-to-edn (.getMetadata arg)))
    (.hasError arg) (assoc :error (rpc/Status-to-edn (.getError arg)))
    (.hasResponse arg) (assoc :response (protobuf/Any-to-edn (.getResponse arg)))))

(def registry
  (with-meta
    {:gcp.foreign.com.google.longrunning/Operation
     [:map
      [:name :string]
      [:done :boolean]
      [:metadata {:optional true} [:ref :gcp.foreign.com.google.protobuf/Any]]
      [:error {:optional true} [:ref :gcp.foreign.com.google.rpc/Status]]
      [:response {:optional true} [:ref :gcp.foreign.com.google.protobuf/Any]]]}
    {:gcp.global/name :gcp.foreign.com.google.longrunning/registry}))

(global/include-schema-registry! registry)
