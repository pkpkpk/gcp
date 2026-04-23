(ns gcp.foreign.com.google.protobuf
  (:require [gcp.global :as g])
  (:import
    (com.google.protobuf Any ByteString Duration ListValue NullValue ProtocolStringList Struct Timestamp Value LazyStringArrayList)
    (java.nio ByteBuffer)))

(def registry
  ^{:gcp.global/name :gcp.foreign.com.google.protobuf/registry}
  {:gcp.foreign.com.google.protobuf/Timestamp  [:map {:gen/max 2}
                                                [:seconds :pi64]
                                                [:nanos :pi64]]
   :gcp.foreign.com.google.protobuf/Duration   [:or {:gen/max 2}
                                                :pi64
                                                [:map
                                                 [:seconds :pi64]
                                                 [:nanos {:optional true} :pi32]]]
   :gcp.foreign.com.google.protobuf/Value      [:or
                                                {:class 'com.google.protobuf.Value
                                                 :doc "schema for com.google.protobuf.Value"
                                                 :gen/max 2}
                                                :boolean
                                                :nil
                                                :i64
                                                :f64
                                                :string
                                                [:ref :gcp.foreign.com.google.protobuf/Struct]
                                                [:sequential [:ref :gcp.foreign.com.google.protobuf/Value]]]

   :gcp.foreign.com.google.protobuf/Any        [:map
                                                {:class 'com.google.protobuf.Any
                                                 :doc "schema for com.google.protobuf.Any"
                                                 :gen/max 2}
                                                [:type-url :string]
                                                [:value [:ref :gcp.foreign.com.google.protobuf/ByteString]]]

   :gcp.foreign.com.google.protobuf/Struct     [:map-of {:class 'com.google.protobuf.Struct
                                                         :doc "schema for com.google.protobuf.Struct"
                                                         :gen/max 2}
                                                [:or :string :keyword]
                                                [:ref :gcp.foreign.com.google.protobuf/Value]]

   :gcp.foreign.com.google.protobuf/ByteString [:or {:class 'com.google.protobuf.ByteString
                                                     :doc "schema for com.google.protobuf.ByteString"
                                                     :gen/schema :string
                                                     :gen/max 2}
                                                :string
                                                'bytes?
                                                (g/instance-schema java.nio.ByteBuffer)]

   :gcp.foreign.com.google.protobuf/ProtocolStringList [:sequential {:class 'com.google.protobuf.ProtocolStringList
                                                                     :doc "schema for com.google.protobuf.ProtocolStringList"
                                                                     :gen/max 2}
                                                        [:ref :gcp.foreign.com.google.protobuf/ByteString]]})

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

(defn ^Any Any-from-edn [arg]
  (let [builder (Any/newBuilder)]
    (when (:type-url arg) (.setTypeUrl builder (:type-url arg)))
    (when (:value arg) (.setValue builder (ByteString-from-edn (:value arg))))
    (.build builder)))

(defn Any-to-edn [^Any arg]
  {:type-url (.getTypeUrl arg)
   :value    (ByteString-to-edn (.getValue arg))})

#!-----------------------------------------------------------------------------

(defn ^NullValue NullValue-from-edn [_arg] (NullValue/NULL_VALUE))

(defn NullValue-to-edn [^NullValue arg])

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
  (let [l (LazyStringArrayList.)]
    (doseq [v arg]
      (.add l (ByteString-from-edn v)))
    l))

#!-----------------------------------------------------------------------------

(defn ^Duration Duration-from-edn [arg]
  (g/strict! ::Duration arg)
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
  ;{:post [(g/strict! ::Duration %)]}
  {:seconds (.getSeconds arg)
   :nanos   (.getNanos arg)})

(g/include-schema-registry! registry)