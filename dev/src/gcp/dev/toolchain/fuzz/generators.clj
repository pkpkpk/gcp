(ns gcp.dev.toolchain.fuzz.generators
  (:refer-clojure :exclude [int long boolean])
  (:require
   [clojure.test.check.generators :as gen])
  (:import
   (com.google.protobuf ByteString)))

(def byte-string
  (gen/fmap #(ByteString/copyFrom (byte-array %))
            (gen/vector gen/byte 0 5)))

(def string
  gen/string-alphanumeric)

(def int
  gen/small-integer)

(def long
  gen/large-integer)

(def boolean
  gen/boolean)

(def generators
  {"com.google.protobuf.ByteString" byte-string
   "java.lang.String" string
   "java.lang.Integer" int
   "int" int
   "java.lang.Long" long
   "long" long
   "java.lang.Boolean" boolean
   "boolean" boolean})

(defn get-generator [type-name]
  (get generators type-name))
