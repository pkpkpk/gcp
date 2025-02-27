(ns gcp.vertexai.v1.api.FunctionCall
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import [com.google.cloud.vertexai.api FunctionCall]))

(defn ^FunctionCall from-edn [arg]
  (global/strict! :gcp/vertexai.api.FunctionCall arg)
  (let [builder (FunctionCall/newBuilder)]
    (.setName builder (:name arg))
    (.setArgs builder (protobuf/struct-from-edn (:args arg)))
    (.build builder)))

(defn to-edn [^FunctionCall fc]
  {:post [(global/strict! :gcp/vertexai.api.FunctionCall %)]}
  {:name (.getName fc)
   :args (protobuf/struct-to-edn (.getArgs fc))})