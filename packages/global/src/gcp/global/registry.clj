(ns gcp.global.registry
  (:require [malli.core :as m]
            [malli.registry :as mr]))

(defn fallback-registry
  "Creates a Malli registry that resolves missing keys to a tagged [:any] instead of throwing :malli.core/invalid-ref.
   Used to break the 'invalid-ref' deadlock during schema composition."
  ([] (fallback-registry {}))
  ([reg-map]
   (let [base (if (mr/registry? reg-map) reg-map (mr/simple-registry reg-map))]
     (mr/composite-registry
      base
      (reify
        mr/Registry
        (-schemas [_] {})
        (-schema [_ k] (m/schema [:any {::forward-ref k}])))))))
