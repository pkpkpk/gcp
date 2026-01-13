(ns gcp.foreign.io.opentelemetry.api.common
 {:gcp.dev/certification
   {:Attributes
      {:base-seed 1767754205947
       :passed-stages
         {:smoke 1767754205947 :standard 1767754205948 :stress 1767754205949}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "aafb05a3f565b0188aad6359ee7b4c663a4ee2212f5d9f414ebe7e5b13dd96d5"
       :timestamp "2026-01-07T02:50:06.105204597Z"}}}
  (:require
    [gcp.global :as g]
    [malli.core :as m])
  (:import
    (io.opentelemetry.api.common AttributeKey Attributes)))

(def registry
  ^{:gcp.global/name :gcp.foreign.io.opentelemetry.api.common/registry}
  {:gcp.foreign.io.opentelemetry.api.common/Attributes [:or {:gen/schema [:map-of :string :string]}
                                                        (g/instance-schema io.opentelemetry.api.common.Attributes)
                                                        :map]})

(g/include-schema-registry! registry)

(defn ^Attributes Attributes-from-edn [arg]
  (if (instance? Attributes arg)
    arg
    (if (map? arg)
      (let [b (Attributes/builder)]
        (doseq [[k v] arg]
          (.put b (AttributeKey/stringKey (str k)) (str v)))
        (.build b))
      (Attributes/empty))))

(defn Attributes-to-edn [^Attributes arg]
  (into {}
        (map (fn [[k v]] [(str k) (str v)]))
        (.asMap arg)))