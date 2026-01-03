(ns gcp.foreign.com.google.protobuf
  (:require
    [gcp.global :as g]
    [malli.core :as m])
  (:import
    (com.google.protobuf ByteString Duration ListValue NullValue ProtocolStringList Struct Timestamp Value)
    (java.nio ByteBuffer)))

(def registry
  ^{::g/name ::registry}
  {::Timestamp  [:map
                 [:seconds :int]
                 [:nanos [:int {:min 0 :max 999999999}]]]
   ::Duration   [:or
                 :int
                 [:map
                  [:seconds :int]
                  [:nanos [:int {:min 0 :max 999999999}]]]]
   ::Value      [:or
                 {:class 'com.google.protobuf.Value
                  :doc "schema for com.google.protobuf.Value"}
                 :boolean
                 :nil
                 :int
                 :float
                 :string
                 [:ref ::Struct]
                 [:sequential [:ref ::Value]]]

   ::Struct     [:map-of {:class 'com.google.protobuf.Struct
                          :doc "schema for com.google.protobuf.Struct"}
                 [:or :string :keyword]
                 [:ref ::Value]]

   ::ByteString [:or {:class 'com.google.protobuf.ByteString
                      :doc "schema for com.google.protobuf.ByteString"
                      :gen/schema :string}
                 :string
                 'bytes?
                 (g/instance-schema java.nio.ByteBuffer)]})

(g/include-schema-registry! registry)

#!-----------------------------------------------------------------------------

(defn ^Timestamp Timestamp-from-edn [arg]
  (let [builder (Timestamp/newBuilder)]
    (if (int? arg)
      (.setSeconds builder arg)
      (let [{:keys [seconds nanos]} arg]
        (when seconds
          (.setSeconds builder seconds))
        (when nanos
          (.setNanos builder nanos))))
    (.build builder)))

(defn Timestamp-to-edn [^Timestamp arg]
  {:seconds (.getSeconds arg)
   :nanos   (.getNanos arg)})

#!-----------------------------------------------------------------------------

(defn ByteString-from-edn [arg]
  (if (string? arg)
    (ByteString/copyFromUtf8 ^String arg)
    (ByteString/copyFrom arg)))

(defn ByteString-to-edn
  [^ByteString arg]
  (if (.isValidUtf8 arg)
    (.toStringUtf8 arg)
    (.toByteArray arg)))

#!-----------------------------------------------------------------------------

(declare Value-from-edn Value-to-edn)

#!-----------------------------------------------------------------------------

(defn ^Struct Struct-from-edn [arg]
  (let [builder (Struct/newBuilder)]
    (.putAllFields builder (into {} (map (fn [[k v]] [(name k) (Value-from-edn v)])) arg))
    (.build builder)))

(defn Struct-to-edn [^Struct s]
  (into {}
        (map
          (fn [[k v]]
            [k (Value-to-edn v)]))
        (.getFieldsMap s)))

#!-----------------------------------------------------------------------------

(defn ^ListValue ListValue-from-edn [arg]
  (let [builder (ListValue/newBuilder)]
    (.addAllValues builder (map Value-from-edn arg))
    (.build builder)))

#!-----------------------------------------------------------------------------

(defn ^Value Value-from-edn [arg]
  (g/strict! ::Value arg)
  (let [builder (Value/newBuilder)]
    (cond
      (nil? arg)
      (.setNullValue builder NullValue/NULL_VALUE)

      (number? arg)
      (.setNumberValue builder arg)

      (string? arg)
      (.setStringValue builder arg)

      (boolean? arg)
      (.setBoolValue builder arg)

      (g/valid? ::Struct arg)
      (.setStructValue builder (Struct-from-edn arg))

      (g/valid? [:sequential ::Value] arg)
      (.setListValue builder (ListValue-from-edn arg))

      true
      (throw (ex-info "cannot make Value from arg" {:arg arg})))
    (.build builder)))

(defn Value-to-edn [val]
  (if (instance? Value val)
    (cond
      (.hasBoolValue val) (.getBoolValue val)
      (.hasListValue val) (map Value-to-edn (.getValuesList (.getListValue val)))
      (.hasNullValue val) nil
      (.hasNumberValue val) (.getNumberValue val)
      (.hasStringValue val) (.getStringValue val)
      (.hasStructValue val) (Struct-to-edn (.getStructValue val))
      true (throw (ex-info "unexpected protobuf value type" {:val val})))
    val))

#!-----------------------------------------------------------------------------

(defn ProtocolStringList-to-edn
  "https://cloud.google.com/java/docs/reference/protobuf/latest/com.google.protobuf.ProtocolStringList.html"
  [^ProtocolStringList lst]
  (mapv ByteString-to-edn (.asByteStringList lst)))

(defn ProtocolStringList-from-edn [arg]
  (throw (Exception. "TODO")))

#!-----------------------------------------------------------------------------

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

(defn Duration-to-edn [^Duration arg]
  {:seconds (.getSeconds arg)
   :nanos   (.getNanos arg)})