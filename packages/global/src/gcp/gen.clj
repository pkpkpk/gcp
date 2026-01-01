(ns gcp.gen
  (:require
   [gcp.global :as g]
   [malli.generator :as mg]))

(defn generate
  ([?gen-or-schema]
   (generate ?gen-or-schema nil))
  ([?gen-or-schema opts]
   (if (keyword? ?gen-or-schema)
     (if-let [schema (g/get-schema ?gen-or-schema)]
       (mg/generate schema (merge (g/mopts) opts))
       (throw (Exception. (str "could not find schema '" ?gen-or-schema "'"))))
     (mg/generate ?gen-or-schema (merge (g/mopts) opts)))))

(defn generator
  ([?schema]
   (generator ?schema nil))
  ([?schema opts]
   (if (keyword? ?schema)
     (if-let [schema (g/get-schema ?schema)]
       (mg/generator schema (merge (g/mopts) opts))
       (throw (Exception. (str "could not find schema '" ?schema "'"))))
     (mg/generator ?schema (merge (g/mopts) opts)))))

(defn sample
  "Generates a sequence of sample values for a schema.

   Args:
     ?gen-or-schema: A Malli schema (e.g. [:int]) or a test.check generator.
                     If a keyword is provided, it is looked up in the global GCP registry.
     opts (optional): A map of options to customize generation.
       Common options:
       - :seed  : Seed for deterministic generation.
       - :size  : Number of samples to generate (default 10).

   Returns:
     A lazy sequence of generated values."
  ([?gen-or-schema]
   (sample ?gen-or-schema nil))
  ([?gen-or-schema opts]
   (if (keyword? ?gen-or-schema)
     (if-let [schema (g/get-schema ?gen-or-schema)]
       (mg/sample schema (merge (g/mopts) opts))
       (throw (Exception. (str "could not find schema '" ?gen-or-schema "'"))))
     (mg/sample ?gen-or-schema (merge (g/mopts) opts)))))

(defn function-checker
  "Creates a property-based test checker for a function schema.

   Args:
     ?schema: A function schema (e.g. [:=> [:cat :int] :int]).
              If a keyword is provided, it is looked up in the global GCP registry.
     opts (optional): Configuration options.
       - ::mg/=>iterations : Number of iterations for the check (default 100).

   Returns:
     A function that takes an implementation (function) and verifies it against the schema
     using `clojure.test.check/quick-check`. Returns a result map with details on success or failure."
  ([?schema]
   (function-checker ?schema nil))
  ([?schema opts]
   (if (keyword? ?schema)
     (if-let [schema (g/get-schema ?schema)]
       (mg/function-checker schema (merge (g/mopts) opts))
       (throw (Exception. (str "could not find schema '" ?schema "'"))))
     (mg/function-checker ?schema (merge (g/mopts) opts)))))

(defn check
  "Validates a function implementation against a schema using property-based testing.

   Args:
     ?schema: A function schema. If a keyword is provided, it is looked up in the global GCP registry.
     f: The function implementation to check.
     opts (optional): Configuration options.

   Returns:
     nil if the function passes the checks.
     An explanation map if the check fails."
  ([?schema f]
   (check ?schema f nil))
  ([?schema f opts]
   (if (keyword? ?schema)
     (if-let [schema (g/get-schema ?schema)]
       (mg/check schema f (merge (g/mopts) opts))
       (throw (Exception. (str "could not find schema '" ?schema "'"))))
     (mg/check ?schema f (merge (g/mopts) opts)))))

(comment

  (require '[global.gen :as gg])

  ;; random
  (gg/generate :keyword)
  ; => :?

  ;; using seed
  (gg/generate [:enum "a" "b" "c"] {:seed 42})
  ;; => "a"

  ;; using seed and size
  (gg/generate pos-int? {:seed 10, :size 100})
  ;; => 55740

  ;; regexs work too (only clj and if [com.gfredericks/test.chuck "0.2.10"+] available)
  (gg/generate
    [:re #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$"]
    {:seed 42, :size 10})
  ; => "CaR@MavCk70OHiX.yZ"

  ;; :gen/return (note, not validated)
  (gg/generate
    [:and {:gen/return 42} :int])
  ; => 42

  ;; :gen/elements (note, are not validated)
  (gg/generate
    [:and {:gen/elements ["kikka" "kukka" "kakka"]} :string]
    {:seed 10})
  ; => "kikka"

  ;; :gen/fmap
  (gg/generate
    [:and {:gen/fmap (partial str "kikka_")} :string]
    {:seed 10, :size 10})
  ;; => "kikka_WT3K0yax2"

  ;; portable :gen/fmap (requires `org.babashka/sci` dependency to work)
  (gg/generate
    [:and {:gen/fmap '(partial str "kikka_")} :string]
    {:seed 10, :size 10})
  ;; => "kikka_nWT3K0ya7"

  ;; :gen/schema
  (gg/generate
    [:any {:gen/schema [:int {:min 10, :max 20}]}]
    {:seed 10})
  ; => 19

  ;; :gen/min & :gen/max for numbers and collections
  (gg/generate
    [:vector {:gen/min 4, :gen/max 4} :int]
    {:seed 1})
  ; => [-8522515 -1433 -1 1]

  ;; :gen/min & gen/max works for :+ and :* as well
  (gg/generate
    [:+ {:gen/min 2 :gen/max 10} :int]
    {:seed 10})
  ; => [-109024846 -2 25432]

  ;; When composing sequence schemas, the directives effect the definition they are
  ;; associated with, such that:
  (gg/generate
    [:* {:gen/min 2 :gen/max 3} ; 2 - 3 repetitions of
     [:cat
      [:+ {:gen/min 2 :gen/max 3} :int] ; 2 - 3 repetitions of int
      [:* {:gen/min 1 :gen/max 2} :string]]] ; followed by 1-2 repetitions of string
    {:seed 10})

  ; => (-812 1283 "Q9beps1Yn3c3VP9" "4XHdn1mgudSlNpVyxOrQIiR5pd5ocs" 114 -14284153 "8SSR9033czAO05")

  ;; :gen/infinite? & :gen/NaN? for :double
  (gg/generate
    [:double {:gen/infinite? true, :gen/NaN? true}]
    {:seed 1})
  ; => ##Inf

  (require '[clojure.test.check.generators :as gen])

  ;; gen/gen (note, not serializable)
  (gg/generate
    [:sequential {:gen/gen (gen/list gen/neg-int)} :int]
    {:size 42, :seed 42}))
  ; => (-37 -13 -13 -24 -20 -11 -34 -40 -22 0 -10)
