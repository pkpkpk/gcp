(ns gcp.foreign.java.nio.channels
 {:gcp.dev/certification
   {:ScatteringByteChannel
      {:base-seed 1767752538767
       :passed-stages
         {:smoke 1767752538767 :standard 1767752538768 :stress 1767752538769}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "4f2ba667e74b20739f31a2c76ba2541a0b5b410fc5951bf25d24756b6bd5dc2a"
       :timestamp "2026-01-07T02:22:18.770883446Z"}
    :SeekableByteChannel
      {:base-seed 1767752538771
       :passed-stages
         {:smoke 1767752538771 :standard 1767752538772 :stress 1767752538773}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "4f2ba667e74b20739f31a2c76ba2541a0b5b410fc5951bf25d24756b6bd5dc2a"
       :timestamp "2026-01-07T02:22:18.774537821Z"}
    :WritableByteChannel
      {:base-seed 1767752538774
       :passed-stages
         {:smoke 1767752538774 :standard 1767752538775 :stress 1767752538776}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "4f2ba667e74b20739f31a2c76ba2541a0b5b410fc5951bf25d24756b6bd5dc2a"
       :timestamp "2026-01-07T02:22:18.777984908Z"}}}
  (:require
    [gcp.global :as g]
    [malli.core :as m])
  (:import
    (java.nio.channels ScatteringByteChannel SeekableByteChannel WritableByteChannel Channels)
    (java.io ByteArrayInputStream ByteArrayOutputStream)))

(def registry
  ^{:gcp.global/name :gcp.foreign.java.nio.channels/registry}
  {:gcp.foreign.java.nio.channels/ScatteringByteChannel [:nil]
   :gcp.foreign.java.nio.channels/SeekableByteChannel [:nil]
   :gcp.foreign.java.nio.channels/WritableByteChannel [:nil]})

(g/include-schema-registry! registry)

(defn ^ScatteringByteChannel ScatteringByteChannel-from-edn [_]
  nil)

(defn ScatteringByteChannel-to-edn [_] nil)

(defn ^SeekableByteChannel SeekableByteChannel-from-edn [_]
  nil)

(defn SeekableByteChannel-to-edn [_] nil)

(defn ^WritableByteChannel WritableByteChannel-from-edn [_]
  (Channels/newChannel (ByteArrayOutputStream.)))

(defn WritableByteChannel-to-edn [_] nil)