(ns gcp.type
  (:import (com.google.type Date)))

(def Date-schema
  [:map {:closed true}
   [:year [:and :int]]
   [:month [:and :int [:enum 1 2 3 4 5 6 7 8 9 10 11 12]]]
   [:day [:and :int (into [:enum] (range 0 32))]]])

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