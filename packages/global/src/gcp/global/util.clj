(ns gcp.global.util
  (:refer-clojure :exclude [assoc])
  (:require [gcp.global :as g]
            [malli.core :as m]
            [malli.util :as mu]
            [gcp.global.registry :as registry]))

(defn assoc [schema key value]
  (let [reg (registry/fallback-registry (g/get-all-schemas))
        opts (g/mopts reg)
        ;; If schema is a keyword, resolve it to its form to avoid [:ref kw] wrapping if possible
        base (if (keyword? schema)
               (or (g/get-schema schema reg) [:ref schema])
               schema)]
    (mu/assoc base key value opts)))
