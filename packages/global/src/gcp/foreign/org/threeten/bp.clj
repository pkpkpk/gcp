(ns gcp.foreign.org.threeten.bp
  {:gcp.dev/certification
   {:Duration
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767558288329
       :timestamp "2026-01-04T20:24:48.332991590Z"
       :passed-stages
         {:smoke 1767558288329 :standard 1767558288330 :stress 1767558288331}
       :source-hash
         "63f3fcb0ef7dbf91e66e49956d84832e7ff20bd6912df83653aff25a508ad923"}}}
  (:require [gcp.global :as global])
  (:import (org.threeten.bp Duration)))

(defn Duration-from-edn [arg]
  (if (number? arg)
    (Duration/ofSeconds arg)
    (let [{:keys [seconds nanos] :or {seconds 0 nanos 0}} arg]
      (Duration/ofSeconds seconds nanos))))

(defn Duration-to-edn [^Duration arg]
  {:seconds (.getSeconds arg)
   :nanos (.getNano arg)})

(global/include-schema-registry!
 (with-meta
   {::Duration [:or
                :int
                [:map
                 [:seconds :int]
                 [:nanos [:int {:min 0 :max 999999999}]]]]}
   {::global/name ::registry}))
