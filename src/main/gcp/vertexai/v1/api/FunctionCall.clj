(ns gcp.vertexai.v1.api.FunctionCall
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import [com.google.cloud.vertexai.api FunctionCall]))

(def ^{:class FunctionCall} schema
  [:map
   [:name {:optional false} :string]
   [:args protobuf/struct-schema]])

(defn ^FunctionCall from-edn [arg]
  (global/strict! schema arg)
  (let [builder (FunctionCall/newBuilder)]
    (.setName builder (:name arg))
    (.setArgs builder (protobuf/struct-from-edn (:args arg)))
    (.build builder)))

(defn to-edn [^FunctionCall fc]
  {:post [(global/strict! schema %)]}
  {:name (.getName fc)
   :args (protobuf/struct-to-edn (.getArgs fc))})