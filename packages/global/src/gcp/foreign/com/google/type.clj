(ns gcp.foreign.com.google.type
  (:require
   [gcp.global :as g])
  (:import
   (com.google.type Date LatLng)))

(def registry
  ^{::g/name ::registry}
  {::Date [:map {:closed true}
           [:year [:int {:min 0 :max 9999}]]
           [:month [:enum 1 2 3 4 5 6 7 8 9 10 11 12]]
           [:day [:enum 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31]]]
   ::LatLng [:map {:closed true}
             [:latitude :double]
             [:longitude :double]]})

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

(defn LatLng-from-edn
  [{:keys [latitude longitude]}]
  (let [builder (LatLng/newBuilder)]
    (.setLatitude builder latitude)
    (.setLongitude builder longitude)
    (.build builder)))

(defn LatLng-to-edn [^LatLng l]
  {:latitude  (.getLatitude l)
   :longitude (.getLongitude l)})
