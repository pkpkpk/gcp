(ns gcp.type
  (:require [gcp.global :as g])
  (:import (com.google.type Date)))

(def registry
  ^{::g/name ::registry}
  {::Date [:map {:closed true}
           [:year [:and :int]]
           [:month [:and :int [:enum 1 2 3 4 5 6 7 8 9 10 11 12]]]
           [:day [:and :int [:enum 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31]]]]})

(g/include-schema-registry! registry)

(defn Date-from-edn
  [{:keys [year month day]}]
  (let [builder (Date/newBuilder)]
    (.setDay builder day)
    (.setMonth builder month)
    (.setYear builder year)
    (.build builder)))

(defn Date-to-edn [^Date d]
  {:year  (.getYear d)
   :month (.getMonth d)
   :day   (.getDay d)})