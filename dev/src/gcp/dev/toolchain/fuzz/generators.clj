(ns gcp.dev.toolchain.fuzz.generators
  (:require
   [clojure.test.check.generators :as tgen]
   [gcp.gen :as gen]
   [gcp.global :as g]
   [malli.core :as m]
   [malli.registry :as mr])
  (:import
   (java.time LocalDate LocalDateTime LocalTime)
   (java.time.temporal ChronoField ChronoUnit)
   (org.threeten.extra PeriodDuration)))

(defn with-gen [schema gen]
  (let [[type props & rest] schema]
    (into [type (assoc props :gen/gen gen)] rest)))

(defn test-registry []
  (let [global-registry (mr/schemas g/*registry*)]
    (mr/registry
      (merge global-registry
             {:Time           (with-gen (g/instance-schema java.time.LocalTime)
                                        (tgen/return (.truncatedTo (LocalTime/now) ChronoUnit/MICROS)))
              :Date           (with-gen (g/instance-schema java.time.LocalDate)
                                        (tgen/return (LocalDate/now)))
              :DateTime       (with-gen (g/instance-schema java.time.LocalDateTime)
                                        (tgen/return (.truncatedTo (LocalDateTime/now) ChronoUnit/MICROS)))
              :PeriodDuration (with-gen (g/instance-schema org.threeten.extra.PeriodDuration)
                                        (tgen/return (PeriodDuration/of (java.time.Period/ofDays 1))))
              :bigint         (with-gen (g/instance-schema java.math.BigInteger)
                                        (tgen/return (java.math.BigInteger. "1")))
              :bigdec         (with-gen (g/instance-schema java.math.BigDecimal)
                                        (tgen/return (bigdec 2.718)))}))))

(comment
  (defn certify-custom-binding [sk verify-fn options]
    (let [reg (test-registry)
          ;; Update global options to include the new registry for this context
          mopts (merge (g/mopts) {:registry reg})
          schema (m/schema (get reg sk) mopts)
          _ (when-not schema (throw (ex-info "Schema not found" {:sk sk})))
          pruned-schema (fuzz/prune-read-only-schema schema)
          ;; Create generator with explicit options
          generator (gen/generator pruned-schema mopts)]
      (fuzz/run-certification-protocol sk generator verify-fn options))))
