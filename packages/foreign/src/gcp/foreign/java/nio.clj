(ns gcp.foreign.java.nio
 {:gcp.dev/certification
   {:ByteBuffer
      {:base-seed 1767752476751
       :passed-stages
         {:smoke 1767752476751 :standard 1767752476752 :stress 1767752476753}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "282eada41b0338fcae66777301d02cad4d173003db5274f8696fd0604d42fc56"
       :timestamp "2026-01-07T02:21:16.811773336Z"}}}
  (:require
    [gcp.global :as g]
    [malli.core :as m])
  (:import
    (java.nio ByteBuffer)))

(def registry
  ^{:gcp.global/name :gcp.foreign.java.nio/registry}
  {:gcp.foreign.java.nio/ByteBuffer [:or {:gen/schema :string}
                                     (g/instance-schema java.nio.ByteBuffer)
                                     :string]})

(g/include-schema-registry! registry)

(defn ^ByteBuffer ByteBuffer-from-edn [arg]
  (cond
    (instance? ByteBuffer arg) arg
    (string? arg) (ByteBuffer/wrap (.getBytes ^String arg "UTF-8"))
    :else (ByteBuffer/wrap arg)))

(defn ByteBuffer-to-edn [^ByteBuffer arg]
  (if (.hasArray arg)
    (String. (.array arg) "UTF-8")
    (let [bytes (byte-array (.remaining arg))]
      (.get arg bytes)
      (String. bytes "UTF-8"))))