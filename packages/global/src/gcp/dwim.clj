(ns gcp.dwim
  "DWIM dispatch with abstract error models and pluggable formatters."
  (:require [clojure.string :as string]
            [gcp.global :as g]
            [malli.core :as m])
  (:import (java.io StringWriter Writer)
           (java.util IdentityHashMap)))

(def ^:dynamic *safe-str-max-chars* 180)
(def ^:dynamic *safe-str-print-length* 12)
(def ^:dynamic *safe-str-print-level* 4)
(def ^:dynamic *safe-str-segment-limit* 16)

(defn- truncate
  [x]
  (cond
    (string? x)
    (if (> (count x) *safe-str-segment-limit*)
      (str (subs x 0 *safe-str-segment-limit*) "…")
      x)

    (bytes? x)
    (let [n (alength ^bytes x)]
      (if (> n *safe-str-segment-limit*)
        (str "#bytes[" n "]("
             (subs (String. ^bytes x "UTF-8") 0 *safe-str-segment-limit*)
             "…)")
        (str "#bytes[" n "]")))

    :else
    x))

(defn safe-str
  "Bounded, cycle-aware printer. Never fully realizes lazy sequences."
  [x]
  (let [writer (StringWriter.)
        seen   (IdentityHashMap.)]
    (binding [*out*          writer
              *print-length* *safe-str-print-length*
              *print-level*  *safe-str-print-level*]
      (letfn [(print-bounded [x level]
                (cond
                  ;; Reference types get cycle detection.
                  (and (instance? Object x)
                       (not (keyword? x))
                       (not (number? x))
                       (not (string? x))
                       (not (char? x))
                       (not (boolean? x))
                       (not (nil? x)))
                  (if (.containsKey seen x)
                    (print "#ref")
                    (do
                      (.put seen x true)
                      (try
                        (print-truncated x level)
                        (finally
                          (.remove seen x)))))

                  :else
                  (print-truncated x level)))

              (print-truncated [x level]
                (if (>= level *safe-str-print-level*)
                  (print "#")
                  (let [x' (truncate x)]
                    (cond
                      (or (nil? x')
                          (boolean? x')
                          (char? x')
                          (keyword? x')
                          (symbol? x')
                          (number? x')
                          (string? x'))
                      (pr x')

                      (map? x')
                      (let [s (seq x')]
                        (print "{")
                        (when s
                          (loop [xs s
                                 i  0]
                            (when (pos? i)
                              (print ", "))
                            (let [[k v] (first xs)]
                              (print-bounded k (inc level))
                              (print " ")
                              (print-bounded v (inc level)))
                            (let [n (next xs)]
                              (if (and n
                                       (< (inc i) *safe-str-print-length*))
                                (recur n (inc i))
                                (when n
                                  (print ", …"))))))
                        (print "}"))

                      ;; Lists / lazy seqs.
                      (seq? x')
                      (let [s (seq x')]
                        (print "(")
                        (when s
                          (loop [xs s
                                 i  0]
                            (when (pos? i)
                              (print " "))
                            (print-bounded (first xs) (inc level))
                            (let [n (next xs)]
                              (if (and n
                                       (< (inc i) *safe-str-print-length*))
                                (recur n (inc i))
                                (when n
                                  (print " …"))))))
                        (print ")"))

                      ;; Vectors, sets, and other seqables.
                      (seqable? x')
                      (let [s (seq x')]
                        (print "[")
                        (when s
                          (loop [xs s
                                 i  0]
                            (when (pos? i)
                              (print " "))
                            (print-bounded (first xs) (inc level))
                            (let [n (next xs)]
                              (if (and n
                                       (< (inc i) *safe-str-print-length*))
                                (recur n (inc i))
                                (when n
                                  (print " …"))))))
                        (print "]"))

                      :else
                      (print "#<" (.getName (class x')) ">")))))]
        (print-bounded x 0)
        (flush)
        (let [s (.toString writer)]
          (if (> (count s) *safe-str-max-chars*)
            (str (subs s 0 *safe-str-max-chars*) "…")
            s))))))

(defn gcp-schema-ref? [k]
  (and (keyword? k)
       (namespace k)
       (string/starts-with? (namespace k) "gcp")))

(defn- extract-parse-values [parsed]
  (cond
    (and (map? parsed) (:value parsed) (:key parsed))
    (extract-parse-values (:value parsed))

    (and (map? parsed) (:values parsed))
    (:values parsed)

    (map? parsed)
    parsed

    :else parsed))

(defn- format-arity-error-string
  "the user called with an unsupported argument count"
  [call]
  (str (:facade call) " accepts arities "
       (string/join ", " (:supported-arities call))
       ", got " (:arity call) ".\n\n"
       "More Info:" \newline
       "  (gcp.global/get-schema " (:cmd call) ")" \newline
       "  (clojure.repl/doc " (:facade call) ")"))

(defn compile-arity-schemas [edn-map]
  (into {} (map (fn [[arity edn]] [arity (g/schema edn)])) edn-map))

(defn- extract-branch-params [edn]
  (letfn [(walk [x]
            (cond
              (and (vector? x) (= :altn (first x)))
              (mapcat (fn [child]
                        (if (and (vector? child) (keyword? (first child)) (= 2 (count child)))
                          (walk (second child))
                          (walk child)))
                      (rest x))

              (and (vector? x) (= :catn (first x)))
              [(vec (rest x))]

              :else []))]
    (walk edn)))

(defn- flatten-or-schema
  [schema]
  (if (and (vector? schema)
           (= :or (first schema)))
    (mapcat flatten-or-schema (rest schema))
    [schema]))

(defn- reduce-or-schema [schema]
  (if (and (vector? schema)
           (= :or (first schema)))
    (into [:or]
          (mapcat (fn [schema]
                    (let [reduced (reduce-or-schema schema)]
                      (if (and (vector? reduced)
                               (= :or (first reduced)))
                        (rest reduced)
                        [reduced])))
                  (rest schema)))
    schema))

(defn- normalize-position-schema [schema]
  (let [schemas (->> (flatten-or-schema schema)
                     distinct
                     vec)]
    (if (= 1 (count schemas))
      (first schemas)
      (into [:or] schemas))))

(defn branch-position-schemas [branches]
  (->> branches
       (mapcat (fn [params]
                 (map-indexed (fn [idx [_ schema]]
                                [idx schema])
                              params)))
       (group-by first)
       (reduce-kv (fn [result idx entries]
                    (assoc result idx
                                  (normalize-position-schema
                                    (map second entries))))
                  {})))

(defn branch-error
  [{:keys [arity-schemas compiled-schemas argv] :as call}]
  (let [arity            (count argv)
        schema           (get compiled-schemas arity)
        {:keys [errors] :as explanation} (g/explain schema argv)
        arity-schema     (get arity-schemas arity)
        branches         (extract-branch-params arity-schema)
        position-schemas (branch-position-schemas branches)
        branch-positions (mapv (fn [branch]
                                 (mapv second branch))
                               branches)
        mismatches       (->> (range arity)
                              (keep (fn [index]
                                      (let [value   (nth argv index)
                                            schemas (keep #(nth % index nil)
                                                          branch-positions)]
                                        (when-not (some #(g/valid? % value)
                                                        schemas)
                                          {:index    index
                                           :value    (safe-str value)
                                           :expected (reduce-or-schema
                                                       (into [:or]
                                                             (get position-schemas index)))}))))
                              vec)
        suggestions      (into (sorted-set)
                               (comp (map :expected)
                                     (mapcat flatten)
                                     (filter gcp-schema-ref?))
                               mismatches)
        repl-suggestions (into {}
                               (map (fn [{:keys [index expected]}]
                                      [index
                                       (mapv #(list 'g/explain % (nth argv index))
                                             (rest expected))]))
                               mismatches)]
    (assoc call
      ::type             ::parse-error
      :arity             arity
      :argv              (safe-str (:value explanation))
      :branches          branches
      :position-schemas  position-schemas
      :mismatches        mismatches
      :suggested-keys    suggestions
      :suggestions-forms  repl-suggestions)))

(defn- type->placeholder [t]
  (cond
    (= t 'string?) :string
    (= t 'int?) :integer
    (= t 'boolean?) :boolean
    (keyword? t) t
    (symbol? t) (keyword t)
    :else t))

(defn- format-mismatch
  [{:keys [index value expected]}]
  (str "  arg " index "\n"
       "    expected: "
       (if (= 1 (count expected))
         (pr-str (first expected))
         (if (and (= 2 (count expected))
                  (#{:and :or} (first expected)))
           (pr-str (second expected))
           (pr-str expected)))
       \newline
       "    actual: " value))

(defn format-parse-error-string
  [{:keys [facade arity mismatches]}]
  (let [args       (mapv #(str "<arg" % ">") (range arity))
        invocation (str "(" facade
                        (when (seq args)
                          (str " " (string/join " " args)))
                        ")")
        start      (count (str "(" facade " "))
        positions  (reductions +
                               start
                               (map #(inc (count %)) (butlast args)))
        bad-args   (set (map :index mismatches))
        markers    (loop [positions positions
                          index     0
                          column    0
                          out       ""]
                     (if-let [position (first positions)]
                       (recur (next positions)
                              (inc index)
                              (inc position)
                              (str out
                                   (apply str
                                          (repeat (- position column) " "))
                                   (if (bad-args index) "✗" "✓")))
                       out))]
    (str "Invalid arguments to " facade " for arity " arity "."
         "\n\n"
         invocation "\n"
         markers
         (when (seq mismatches)
           (str "\n\n"
                (string/join "\n" (map format-mismatch mismatches))))
         "\n\nMore Info:\n"
         "  (clojure.repl/doc " facade ")")))

(defn _match-arity
  "Tests user's passed argv against the op's arity schema.
   When matched, argv will be given names and result map given to normalize
   Normalize is expected to return the canonical cmd map representation
   {:ok <cmd-map>}  OR  {:error <error-call-map>}"
  [{:keys [facade arity-schemas compiled-schemas cmd argv normalize] :as call}]
  (let [arity  (count argv)
        schema (get compiled-schemas arity)
        normalize (or normalize identity)]
    (assert (fn? normalize))
    (if-not schema
      {:error (assoc call ::type             ::arity-error
                          :arity             (count argv)
                          :argv              (safe-str argv)
                          :supported-arities (vec (sort (keys compiled-schemas))))}
      (let [parsed (m/parse schema argv)]
        (if (= ::m/invalid parsed)
          {:error (branch-error call)}
          (let [parse-values (extract-parse-values parsed)
                normalized (normalize parse-values)]
            (assert (map? normalized))
            (assert (contains? normalized :op))
            (when-some [explanation (g/explain cmd normalized)]
              (throw (ex-info (str "normalize function for cmd " cmd " did not return correct shape")
                              {:cmd        cmd
                               :normalized normalized
                               :errors     (get explanation :errors)})))
            {:ok normalized}))))))

(defmulti  format-error ::type)
(defmethod format-error ::arity-error [err] (format-arity-error-string err))
(defmethod format-error ::parse-error [err] (format-parse-error-string err))

(defmacro defdwim
  [fn-name {:keys [facade cmd arities normalize] :as spec}]
  (assert (qualified-symbol? facade) facade)
  `(let [arity-schemas# ~arities
         compiled#      (compile-arity-schemas arity-schemas#)
         call-base# {:facade           (quote ~facade)
                     :cmd              ~cmd
                     :arity-schemas    arity-schemas#
                     :compiled-schemas compiled#
                     :normalize        ~normalize}]
     (defn ~fn-name [argv#]
       (let [res# (_match-arity (assoc call-base# :argv argv#))]
         (if-let [err# (:error res#)]
           (throw (ex-info (format-error err#) (select-keys err# [::type :arity :argv :mismatches :suggested-keys :suggested-forms])))
           (:ok res#))))))
