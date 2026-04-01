(ns gcp.dev.toolchain.fuzz.generators
  (:require
   [clojure.test.check.generators :as tgen]
   [gcp.gen :as gen]
   [gcp.global :as g]
   [malli.core :as m]
   [malli.registry :as mr])
  (:import
    (java.time Duration LocalDate LocalDateTime LocalTime OffsetDateTime ZoneOffset)
   (java.time.temporal ChronoField ChronoUnit)
   (org.threeten.extra PeriodDuration)))

(defn with-gen [schema gen]
  (cond
    (vector? schema)
    (let [[type props & rest] schema]
      (if (map? props)
        (into [type (assoc props :gen/gen gen)] rest)
        (into [type {:gen/gen gen} props] rest)))

    (or (keyword? schema) (symbol? schema))
    [schema {:gen/gen gen}]

    :else
    (throw (ex-info "Unsupported schema type for with-gen" {:schema schema}))))

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
              :OffsetDateTime (with-gen (g/instance-schema java.time.OffsetDateTime)
                                        (tgen/return (.truncatedTo (OffsetDateTime/now ZoneOffset/UTC) ChronoUnit/MICROS)))
              :Duration       (with-gen (g/instance-schema java.time.Duration)
                                        (tgen/return (Duration/ofDays 1)))
              :PeriodDuration (with-gen (g/instance-schema org.threeten.extra.PeriodDuration)
                                        (tgen/return (PeriodDuration/of (java.time.Period/ofDays 1))))
              :bigint         (with-gen (g/instance-schema java.math.BigInteger)
                                        (tgen/return (java.math.BigInteger. "1")))
              :bigdec         (with-gen (g/instance-schema java.math.BigDecimal)
                                        (tgen/return (bigdec 2.718)))
              :f32            (with-gen (g/get-schema :f32)
                                        (tgen/return (float 2.718)))
              :f64            (with-gen (g/get-schema :f64)
                                        (tgen/return (double 2.718)))
              :i32            (with-gen (g/get-schema :i32)
                                        (tgen/return (int 123)))
              :pi32           (with-gen (g/get-schema :pi32)
                                        (tgen/return (int 123)))
              :i64            (with-gen (g/get-schema :i64)
                                        (tgen/return (long 124)))
              :pi64           (with-gen (g/get-schema :pi64)
                                        (tgen/return (long 124)))
              :byte           (with-gen (g/get-schema :byte)
                                        (tgen/return (byte 64)))}))))

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
