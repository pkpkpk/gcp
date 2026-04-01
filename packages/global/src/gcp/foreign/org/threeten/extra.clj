(ns gcp.foreign.org.threeten.extra
  {:gcp.dev/certification
   {:PeriodDuration
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767558422885
       :timestamp "2026-01-04T20:27:02.891507385Z"
       :passed-stages
         {:smoke 1767558422885 :standard 1767558422886 :stress 1767558422887}
       :source-hash
         "449743d6eddf1ab16dce93d73ddc1fd7f8b67c9096bb320ce946c3962c0d9ea1"}}}
  (:require [gcp.global :as global])
  (:import (org.threeten.extra PeriodDuration)
           (java.time Period Duration)))

(defn PeriodDuration-from-edn [{:keys [period duration]}]
  (let [p (if period
            (Period/of (:years period 0) (:months period 0) (:days period 0))
            (Period/ZERO))
        d (if duration
            (Duration/ofSeconds (:seconds duration 0) (:nanos duration 0))
            (Duration/ZERO))]
    (PeriodDuration/of p d)))

(defn PeriodDuration-to-edn [^PeriodDuration arg]
  (let [p (.getPeriod arg)
        d (.getDuration arg)]
    {:period {:years (.getYears p)
              :months (.getMonths p)
              :days (.getDays p)}
     :duration {:seconds (.getSeconds d)
                :nanos (.getNano d)}}))

(global/include-schema-registry!
 (with-meta
   {::PeriodDuration [:map
                      [:period [:map
                                [:years [:int {:min -2147483648 :max 2147483647}]]
                                [:months [:int {:min -2147483648 :max 2147483647}]]
                                [:days [:int {:min -2147483648 :max 2147483647}]]]]
                      [:duration [:map
                                  [:seconds :int]
                                  [:nanos [:int {:min 0 :max 999999999}]]]]]}
   {::global/name ::registry}))
