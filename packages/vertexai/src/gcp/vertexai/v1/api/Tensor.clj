(ns gcp.vertexai.v1.api.Tensor
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import (com.google.cloud.vertexai.api Tensor Tensor$DataType)))

;; Tensor implementation is complex due to various data types.
;; This is a simplified version focusing on common types or recursive structure if needed.
;; For full completeness, one would map all list types (float_val, int_val, etc.)

(defn ^Tensor from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/Tensor arg)
  (let [builder (Tensor/newBuilder)]
    (some->> (:dtype arg) (let [dt (:dtype arg)] (if (number? dt) (.setDtypeValue builder dt) (.setDtype builder (Tensor$DataType/valueOf dt)))))
    (some->> (:shape arg) (map long) (.addAllShape builder))
    (some->> (:boolVal arg) (.addAllBoolVal builder))
    (some->> (:stringVal arg) (.addAllStringVal builder))
    (some->> (:bytesVal arg) (map protobuf/bytestring-from-edn) (.addAllBytesVal builder))
    (some->> (:floatVal arg) (.addAllFloatVal builder))
    (some->> (:doubleVal arg) (.addAllDoubleVal builder))
    (some->> (:intVal arg) (.addAllIntVal builder))
    (some->> (:int64Val arg) (.addAllInt64Val builder))
    (some->> (:uint64Val arg) (.addAllUint64Val builder))
    (some->> (:listVal arg) (map from-edn) (.addAllListVal builder))
    (some->> (:structVal arg) (map (fn [[k v]] [k (from-edn v)])) (into {}) (.putAllStructVal builder))
    (some->> (:tensorVal arg) (map from-edn) (.setTensorVal builder)) 
    (.build builder)))

(defn to-edn [^Tensor arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/Tensor %)]}
  (cond-> {:dtype (.name (.getDtype arg))
           :shape (vec (.getShapeList arg))}
          (pos? (.getBoolValCount arg)) (assoc :boolVal (vec (.getBoolValList arg)))
          (pos? (.getStringValCount arg)) (assoc :stringVal (vec (.getStringValList arg)))
          (pos? (.getBytesValCount arg)) (assoc :bytesVal (mapv protobuf/bytestring-to-edn (.getBytesValList arg)))
          (pos? (.getFloatValCount arg)) (assoc :floatVal (vec (.getFloatValList arg)))
          (pos? (.getDoubleValCount arg)) (assoc :doubleVal (vec (.getDoubleValList arg)))
          (pos? (.getIntValCount arg)) (assoc :intVal (vec (.getIntValList arg)))
          (pos? (.getInt64ValCount arg)) (assoc :int64Val (vec (.getInt64ValList arg)))
          (pos? (.getUint64ValCount arg)) (assoc :uint64Val (vec (.getUint64ValList arg)))
          (pos? (.getListValCount arg)) (assoc :listVal (mapv to-edn (.getListValList arg)))
          (pos? (.getStructValCount arg)) (assoc :structVal (into {} (map (fn [[k v]] [k (to-edn v)])) (.getStructValMap arg)))))

(def schema
  [:map
   {:doc              "A multidimensional array of values. This class is part of the [Tensor API](https://cloud.google.com/vertex-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Tensor) of Vertex AI. The class is annotated with Protobuf message type for interoperability."
    :class            'com.google.cloud.vertexai.api.Tensor
    :protobuf/type    "google.cloud.vertexai.v1.Tensor"}
   [:dtype {:optional true} [:or :string :int]]
   [:shape {:optional true} [:sequential :int]]
   [:boolVal {:optional true} [:sequential :boolean]]
   [:stringVal {:optional true} [:sequential :string]]
   [:bytesVal {:optional true} [:sequential :gcp.protobuf/ByteString]]
   [:floatVal {:optional true} [:sequential :float]]
   [:doubleVal {:optional true} [:sequential :double]]
   [:intVal {:optional true} [:sequential :int]]
   [:int64Val {:optional true} [:sequential :int]]
   [:uint64Val {:optional true} [:sequential :int]]
   [:listVal {:optional true} [:sequential [:ref :gcp.vertexai.v1.api/Tensor]]]
   [:structVal {:optional true} [:map-of :string [:ref :gcp.vertexai.v1.api/Tensor]]]])

(global/register-schema! :gcp.vertexai.v1.api/Tensor schema)
