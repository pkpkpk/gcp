(ns gcp.protobuf
  (:require [gcp.global :as g]
            [malli.core :as m])
  (:import (com.google.protobuf ByteString ListValue NullValue ProtocolStringList Struct Value Duration)
           (java.nio ByteBuffer)))

(def
  registry
  ^{::g/name ::registry}
  {
   ::Value      [:or
                 {:class 'com.google.protobuf.Value}
                 :boolean
                 :nil
                 :int
                 :float
                 :string
                 [:ref ::Struct]
                 [:sequential [:ref ::Value]]]

   ::Struct     [:map-of {:class 'com.google.protobuf.Struct}
                 [:or :string 'simple-keyword?]
                 [:ref ::Value]]

   ::ByteString [:or {:class 'com.google.protobuf.ByteString}
                 :string
                 'bytes?
                 (g/instance-schema java.nio.ByteBuffer)]})

(g/include-schema-registry! registry)

(defn bytestring-from-edn [arg]
  (if (string? arg)
    (ByteString/copyFromUtf8 arg)
    (ByteString/copyFrom arg)))

(defn bytestring-to-edn
  [^ByteString arg]
  (if (.isValidUtf8 arg)
    (.toStringUtf8 arg)
    (.toByteArray arg)))

(declare value-from-edn value-to-edn)

(defn ^Struct struct-from-edn [arg]
  (let [builder (Struct/newBuilder)]
    (.putAllFields builder (into {} (map (fn [[k v]] [(name k) (value-from-edn v)])) arg))
    (.build builder)))

(defn struct-to-edn [^Struct s]
  (into {}
        (map
          (fn [[k v]]
            [k (value-to-edn v)]))
        (.getFieldsMap s)))

(defn ^ListValue list-value-from-edn [arg]
  (let [builder (ListValue/newBuilder)]
    (.addAllValues builder (map value-from-edn arg))
    (.build builder)))

(defn ^Value value-from-edn [arg]
  (gcp.global/strict! ::Value arg)
  (let [builder (Value/newBuilder)]
    (cond
      (nil? arg)
      (.setNullValue builder NullValue)

      (number? arg)
      (.setNumberValue builder arg)

      (string? arg)
      (.setStringValue builder arg)

      (m/validate [:schema {:registry registry} ::Struct] arg)
      (.setStructValue builder (struct-from-edn arg))

      (m/validate [:sequential [:schema {:registry registry} ::Value]] arg)
      (.setListValue builder (list-value-from-edn arg))

      true
      (throw (ex-info "cannot make Value from arg" {:arg arg})))
    (.build builder)))

(defn value-to-edn [val]
  (if (instance? Value val)
    (cond
      (.hasBoolValue val) (.getBoolValue val)
      (.hasListValue val) (map value-to-edn (.getListValue val))
      (.hasNullValue val) nil
      (.hasNumberValue val) (.getNumberValue val)
      (.hasStringValue val) (.getStringValue val)
      (.hasStructValue val) (into {}
                                  (map (fn [[k v]] [(name k) (value-to-edn v)]))
                                  (.getStructValue val))
      true (throw (ex-info "unexpected protobuf value type" {:val val})))
    val))

(defn protocolstringlist-to-edn
  "https://cloud.google.com/java/docs/reference/protobuf/latest/com.google.protobuf.ProtocolStringList.html"
  [^ProtocolStringList lst]
  (mapv bytestring-to-edn (.asByteStringList lst)))

(defn ^Duration Duration-from-edn [arg]
  (let [builder (Duration/newBuilder)]
    (if (int? arg)
      (.setSeconds builder arg)
      (let [{:keys [seconds nanos]} arg]
        (when seconds
          (.setSeconds builder seconds))
        (when nanos
          (.setNanos builder nanos))))
    (.build builder)))
;
;(defn Duration-to-edn [^Duration arg]
;  (throw (Exception. "unimplemented")))
