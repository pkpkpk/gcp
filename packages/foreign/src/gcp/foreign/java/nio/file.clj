(ns gcp.foreign.java.nio.file
 {:gcp.dev/certification
   {:Path {:base-seed 1767752538670
           :passed-stages {:smoke 1767752538670
                           :standard 1767752538671
                           :stress 1767752538672}
           :protocol-hash
             "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
           :source-hash
             "553f5fdc0fdd24f3dcfed72ee1e1a85d4d04fe85323d1dcb9b46d3b7055c1e6b"
           :timestamp "2026-01-07T02:22:18.738147929Z"}}}
  (:require
    [gcp.global :as g]
    [malli.core :as m])
  (:import
    (java.nio.file Path Paths)))

(def registry
  ^{:gcp.global/name :gcp.foreign.java.nio.file/registry}
  {:gcp.foreign.java.nio.file/Path [:or {:gen/schema :string}
                                    (g/instance-schema java.nio.file.Path)
                                    :string]})

(g/include-schema-registry! registry)

(defn ^Path Path-from-edn [arg]
  (if (instance? Path arg)
    arg
    (Paths/get arg (into-array String []))))

(defn Path-to-edn [^Path arg]
  (.toString arg))