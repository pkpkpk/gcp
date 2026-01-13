(ns gcp.foreign.java.time
 {:gcp.dev/certification
   {:Duration
      {:base-seed 1767752830599
       :passed-stages
         {:smoke 1767752830599 :standard 1767752830600 :stress 1767752830601}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "afe7eac6250cc78dfdf04d55b0b593c00b3a1c8d4770c3625db8b0ce5afb5c39"
       :timestamp "2026-01-07T02:27:10.672895904Z"}
    :OffsetDateTime
      {:base-seed 1767752830674
       :passed-stages
         {:smoke 1767752830674 :standard 1767752830675 :stress 1767752830676}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "afe7eac6250cc78dfdf04d55b0b593c00b3a1c8d4770c3625db8b0ce5afb5c39"
       :timestamp "2026-01-07T02:27:10.751673109Z"}}}
  (:require [gcp.global :as g] [malli.core :as m])
  (:import (java.time Duration OffsetDateTime)))

(def registry
  ^{:gcp.global/name :gcp.foreign.java.time/registry}
  {:gcp.foreign.java.time/Duration [:or {:gen/schema [:int {:min 1 :max 9999 :gen/fmap '(fn [i] (str "PT" i "S"))}]}
                                    (g/instance-schema java.time.Duration)
                                    :string]
   :gcp.foreign.java.time/OffsetDateTime [:or {:gen/schema [:string {:gen/fmap '(fn [_] "2023-01-01T00:00:00Z")}]}
                                          (g/instance-schema java.time.OffsetDateTime)
                                          :string]})

(g/include-schema-registry! registry)

(defn ^Duration Duration-from-edn [arg]
  (if (instance? Duration arg) arg (Duration/parse arg)))
(defn Duration-to-edn [^Duration arg] (.toString arg))

(defn ^OffsetDateTime OffsetDateTime-from-edn [arg]
  (if (instance? OffsetDateTime arg) arg (OffsetDateTime/parse arg)))
(defn OffsetDateTime-to-edn [^OffsetDateTime arg] (.toString arg))