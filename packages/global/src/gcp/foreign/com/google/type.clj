(ns gcp.foreign.com.google.type
  {:gcp.dev/certification
   {:Date {:base-seed 1767492450171
           :passed-stages {:smoke 1767492450171
                           :standard 1767492450172
                           :stress 1767492450173}
           :protocol-hash
             "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
           :source-hash
             "778672123dd24b7ecc4a22c14a9200e76ced0a381899a6b089ac705c53f9f60c"
           :timestamp "2026-01-04T02:07:30.181374689Z"}
    :LatLng
      {:base-seed 1767492450181
       :passed-stages
         {:smoke 1767492450181 :standard 1767492450182 :stress 1767492450183}
       :protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :source-hash
         "778672123dd24b7ecc4a22c14a9200e76ced0a381899a6b089ac705c53f9f60c"
       :timestamp "2026-01-04T02:07:30.188305566Z"}}}
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
