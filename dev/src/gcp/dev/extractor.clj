(ns gcp.dev.extractor
  (:require [gcp.dev.util :refer :all]
            [gcp.global :as g]))

(defn- extract-enum [package className] (throw (Exception. "unimplemented")))
(defn- extract-builder [package className] (throw (Exception. "unimplemented")))
(defn- extract-simple-accessor [package className] (throw (Exception. "unimplemented")))

(def package-schema
  [:map
   [:types/enums set?]
   [:types/simple-accessors set?]])

(defn extract
  [package className]
  (g/coerce package-schema package)
  (cond

    (contains? (:types/enums package) className)
    (extract-enum package className)

    (contains? (:types/simple-accessors package) className)
    (extract-simple-accessor package className)

    ;:types/concrete-unions
    ;:types/abstract-unions
    ;:types/static-factories
    ;:types/nested-static-factories
    ;:types/complex-accessors
    ;:types/nested-accessors

    :else
    (throw (Exception. (str "unknown type category for class '" className "'")))))


