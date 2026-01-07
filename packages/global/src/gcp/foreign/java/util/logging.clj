(ns gcp.foreign.java.util.logging
 {:gcp.dev/certification
   {:Handler
      {:base-seed 1767752654402
       :passed-stages
         {:smoke 1767752654402 :standard 1767752654403 :stress 1767752654404}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "a9ee93aeca60dc501a8cac1f0c1d9f64f0a9f67653ba0d757461adb7f17fa100"
       :timestamp "2026-01-07T02:24:14.411622310Z"}
    :Level {:base-seed 1767752654411
            :passed-stages {:smoke 1767752654411
                            :standard 1767752654412
                            :stress 1767752654413}
            :protocol-hash
              "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
            :source-hash
              "a9ee93aeca60dc501a8cac1f0c1d9f64f0a9f67653ba0d757461adb7f17fa100"
            :timestamp "2026-01-07T02:24:14.415580145Z"}
    :LogRecord
      {:base-seed 1767752654418
       :passed-stages
         {:smoke 1767752654418 :standard 1767752654419 :stress 1767752654420}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "a9ee93aeca60dc501a8cac1f0c1d9f64f0a9f67653ba0d757461adb7f17fa100"
       :timestamp "2026-01-07T02:24:14.439272263Z"}
    :Logger
      {:base-seed 1767752654439
       :passed-stages
         {:smoke 1767752654439 :standard 1767752654440 :stress 1767752654441}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "a9ee93aeca60dc501a8cac1f0c1d9f64f0a9f67653ba0d757461adb7f17fa100"
       :timestamp "2026-01-07T02:24:14.448360429Z"}}}
  (:require [gcp.global :as g])
  (:import (java.util.logging Logger Level Handler LogRecord ConsoleHandler)))

(def registry
  ^{:gcp.global/name :gcp.foreign.java.util.logging/registry}
  {:gcp.foreign.java.util.logging/Logger [:string]
   :gcp.foreign.java.util.logging/Level [:enum "INFO" "WARNING" "SEVERE" "CONFIG" "FINE" "FINER" "FINEST" "ALL" "OFF"]
   :gcp.foreign.java.util.logging/Handler [:nil]
   :gcp.foreign.java.util.logging/LogRecord [:map
                                             [:level [:ref :gcp.foreign.java.util.logging/Level]]
                                             [:msg :string]]})

(g/include-schema-registry! registry)

(defn ^Logger Logger-from-edn [arg] (Logger/getLogger arg))
(defn Logger-to-edn [^Logger arg] (.getName arg))

(defn ^Level Level-from-edn [arg] (Level/parse arg))
(defn Level-to-edn [^Level arg] (.getName arg))

(defn ^Handler Handler-from-edn [_] (ConsoleHandler.))
(defn Handler-to-edn [_] nil)

(defn ^LogRecord LogRecord-from-edn [{:keys [level msg]}]
  (LogRecord. (Level-from-edn level) msg))
(defn LogRecord-to-edn [^LogRecord arg]
  {:level (Level-to-edn (.getLevel arg))
   :msg (.getMessage arg)})