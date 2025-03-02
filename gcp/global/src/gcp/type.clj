(ns gcp.type
  (:require [gcp.global :as g])
  (:import (com.google.type Date)))

(def registry
  ^{::g/name ::registry}
  {::Date [:map {:closed true}
           [:year [:and :int]]
           [:month [:and :int (-> [:enum]
                                  (into (range 1 13))
                                  (into (map double (range 1 13))))]]
           [:day [:and :int (into [:enum] (range 0 32))]]]})

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