(ns gcp.dwim
  "DWIM dispatch with abstract error models and pluggable formatters."
  (:require [clojure.string :as string]
            [gcp.global :as g]
            [malli.core :as m])
  (:import (java.io Writer)
           (java.util IdentityHashMap)))

;; =============================================================================
;; Dynamic configuration

(def ^:dynamic *safe-str-max-chars* 180)
(def ^:dynamic *safe-str-print-length* 12)
(def ^:dynamic *safe-str-print-level* 4)
(def ^:dynamic *safe-str-atom-limit*     128)

;; =============================================================================
;; Bounded printing — never fully realizes lazy seqs

(defn- char-budget-writer [max-chars]
  (let [sb        (StringBuilder.)
        remaining (atom max-chars)]
    (proxy [Writer] []
      (write
        ([x]
         (let [s   (if (string? x) x (str x))
               len (count s)
               avail @remaining]
           (when (pos? avail)
             (let [take-n (min len avail)]
               (.append sb (subs s 0 take-n))
               (swap! remaining - take-n)))))
        ([cbuf off len]
         (.write ^Writer this (^[byte/1 int int] String/new cbuf off len))))
      (flush [])
      (close [])
      (toString [] (.toString sb)))))

(defn- truncate-atom [x]
  (cond
    (string? x)
    (if (> (count x) *safe-str-atom-limit*)
      (str (subs x 0 *safe-str-atom-limit*) "…")
      x)

    (bytes? x)
    (let [n (alength ^bytes x)]
      (if (> n *safe-str-atom-limit*)
        (str "#bytes[" n "]("
             (subs (String. ^bytes x "UTF-8") 0 *safe-str-atom-limit*)
             "…)")
        (str "#bytes[" n "]")))

    :else x))

(defn safe-str
  "Bounded, cycle-aware printer. Never fully realizes lazy sequences."
  [x]
  (let [writer (char-budget-writer *safe-str-max-chars*)
        seen   (IdentityHashMap.)]
    (binding [*out*          writer
              *print-length* *safe-str-print-length*
              *print-level*  *safe-str-print-level*]
      (letfn [(print-bounded [x level]
                (cond
                  ;; Reference types get cycle detection
                  (and (instance? Object x)
                       (not (keyword? x))
                       (not (number? x))
                       (not (string? x))
                       (not (char? x))
                       (not (boolean? x))
                       (not (nil? x)))
                  (if (.containsKey seen x)
                    (print "#ref")
                    (do (.put seen x true)
                        (try (print-truncated x level)
                             (finally (.remove seen x)))))

                  :else
                  (print-truncated x level)))

              (print-truncated [x level]
                (if (>= level *safe-str-print-level*)
                  (print "#")
                  (let [x' (truncate-atom x)]
                    (cond
                      ;; Atoms
                      (or (nil? x') (boolean? x') (char? x')
                          (keyword? x') (symbol? x') (number? x') (string? x'))
                      (pr x')

                      ;; Maps
                      (map? x')
                      (let [s (seq x')]
                        (print "{")
                        (when s
                          (loop [xs s i 0]
                            (when (pos? i) (print ", "))
                            (let [[k v] (first xs)]
                              (print-bounded k (inc level))
                              (print " ")
                              (print-bounded v (inc level)))
                            (let [n (next xs)]
                              (if (and n (< (inc i) *print-length*))
                                (recur n (inc i))
                                (when n (print ", …"))))))
                        (print "}"))

                      ;; Lists / lazy seqs
                      (seq? x')
                      (let [s (seq x')]
                        (print "(")
                        (when s
                          (loop [xs s i 0]
                            (when (pos? i) (print " "))
                            (print-bounded (first xs) (inc level))
                            (let [n (next xs)]
                              (if (and n (< (inc i) *print-length*))
                                (recur n (inc i))
                                (when n (print " …"))))))
                        (print ")"))

                      ;; Vectors, sets, and other seqables
                      (seqable? x')
                      (let [s (seq x')]
                        (print "[")
                        (when s
                          (loop [xs s i 0]
                            (when (pos? i) (print " "))
                            (print-bounded (first xs) (inc level))
                            (let [n (next xs)]
                              (if (and n (< (inc i) *print-length*))
                                (recur n (inc i))
                                (when n (print " …"))))))
                        (print "]"))

                      ;; Fallback for opaque objects
                      :else
                      (print "#<" (.getName (class x')) ">")))))]
        (print-bounded x 0)
        (flush)
        (str writer)))))

;; =============================================================================
;; Schema utilities

(defn gcp-schema-ref? [k]
  (and (keyword? k)
       (namespace k)
       (string/starts-with? (namespace k) "gcp")))

;; =============================================================================
;; Malli EDN introspection

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

(defn- type->placeholder [t]
  (cond
    (= t 'string?)  :string
    (= t 'int?)     :integer
    (= t 'boolean?) :boolean
    (keyword? t)    t
    (symbol? t)     (keyword t)
    :else           :value))

(defn- expected-at-index [arity-edn idx]
  (let [branches (extract-branch-params arity-edn)]
    (into #{} (keep #(get-in % [idx 1]) branches))))

(defn- format-expected [schemas]
  (let [xs (vec schemas)]
    (if (= 1 (count xs))
      (pr-str (first xs))
      (pr-str (into [:or] xs)))))

(defn- schema->label [t]
  (cond
    (#{:string 'string?} t)        ":string"
    (#{:int 'int? 'integer?} t)    ":int"
    (#{:boolean 'boolean?} t)       ":boolean"
    (#{:double 'double?} t)        ":double"
    (#{:float 'float?} t)          ":float"
    (#{:long 'long?} t)             ":long"
    (#{:any 'any?} t)               ":any"
    (#{:nil 'nil?} t)               "nil"
    (vector? t)
    (case (first t)
      :or        (string/join " | " (map schema->label (rest t)))
      :and       (string/join " & " (map schema->label (rest t)))
      :maybe     (str "?" (schema->label (second t)))
      :sequential (str "[" (schema->label (second t)) " ...]")
      :set       (str "#{" (schema->label (second t)) "}")
      :map       ":map"
      :map-of    (str "{" (schema->label (nth t 2)) " " (schema->label (nth t 3)) "}")
      :tuple     (str "[" (string/join ", " (map schema->label (rest t))) "]")
      :ref       (str "[:ref " (schema->label (second t)) "]")
      (when (and (= 2 (count t)) (map? (second t)))
        (schema->label (first t)))
      (str t))
    (keyword? t) (str t)
    (symbol? t)  (str t)
    :else        (pr-str t)))

(defn- catn->params [branch]
  (letfn [(walk [x]
            (cond
              (and (vector? x) (= :catn (first x)))
              (mapv (fn [entry]
                      (when (and (vector? entry) (= 2 (count entry)) (keyword? (first entry)))
                        [(name (first entry)) (schema->label (second entry))]))
                    (rest x))

              (and (vector? x) (= :altn (first x)))
              (mapcat walk (rest x))

              :else []))]
    (remove nil? (walk branch))))

(defn- branch-doc [fn-name branch]
  (let [params (catn->params branch)]
    (if (seq params)
      (let [arg-names (map first params)]
        (str "  `(" fn-name " " (string/join " " arg-names) ")`\n"
             "    where:\n"
             (string/join "\n" (map (fn [[k t]] (str "      " k " --> " t)) params))))
      (str "  `(" fn-name ")`"))))

(defn humanize-arities
  ([fn-name cmd arities]
   (let [lines (concat
                 [(str "Arity dispatch for `" fn-name "`.")
                  ""]
                 (mapcat (fn [[arity schema]]
                           (cons (str arity " args:")
                                 (if (and (vector? schema) (= :altn (first schema)))
                                   (for [child (rest schema)]
                                     (let [branch (if (and (vector? child) (= 2 (count child)) (keyword? (first child)))
                                                    (second child)
                                                    child)]
                                       (branch-doc fn-name branch)))
                                   [(branch-doc fn-name schema)])))
                         (sort-by key arities))
                 [""]
                 ["More Info:"
                  (str "    `(gcp.global/get-schema " (pr-str cmd) ")`")
                  (str "    `(gcp.global/explain " (pr-str cmd) " <args>)`")])]
     (string/join "\n" lines))))

#!----------------------------------------------------------------------------------------------------------------------

(defn- extract-parse-values [parsed]
  (cond
    (and (map? parsed) (:value parsed) (:key parsed))
    (extract-parse-values (:value parsed))

    (and (map? parsed) (:values parsed))
    (:values parsed)

    (map? parsed)
    parsed

    :else parsed))

(defn arity-error
  "Construct an arity error model."
  [{:keys [args supported-arities] :as call}]
  (assoc call ::type             ::arity-error
              :arity             (count args)
              :args              (safe-str args)
              :supported-arities (vec (sort supported-arities))))

(defn parse-error
  "Construct a parse error model."
  [{:keys [edn explain-data cmd] :as call}]
  (let [errors  (:errors explain-data)
        primary (first errors)
        idx     (first (:in primary))
        val     (:value primary)
        expected (when idx (expected-at-index edn idx))]
    (assoc call ::type       ::parse-error
                :args        (safe-str (:value explain-data))
                :branches    (extract-branch-params edn)
                :mismatch    (when (and idx (seq expected))
                               {:index idx
                                :value (safe-str val)
                                :expected (vec expected)})
                :suggestions (vec (filter gcp-schema-ref? expected)))))

(defn- format-arity-error-string
  "this is called with err {::type ::arity-error}
   meaning the user called with an unsupported argument count"
  [call]
  (str (:facade call) " accepts arities "
       (string/join ", " (:supported-arities call))
       ", got " (:arity call) ".\n\n"
       "See: (g/get-schema " (:cmd call) ")"))

(defn- format-parse-error-string
  "{::type ::parse-error}
   user's passed arguments do not conform to any shapes for that given arg count"
  [{:keys [branches facade arity mismatch suggestions] :as call}]
  (let [format-branch-line (fn [fn-name params]
                            (let [placeholders (map (comp type->placeholder second) params)]
                              (if (empty? placeholders)
                                (str "  (" fn-name ")")
                                (if (= 1 (count placeholders))
                                  (str "  (" fn-name " " (first placeholders) ")")
                                  (str "  (" fn-name "\n" (string/join "\n" (map #(str "    " %) placeholders)) ")")))))]
    (str "Invalid arguments to " facade " at arity " arity ".\n\n"
         "Accepted " arity "-arg patterns:\n"
         (string/join "\n\n" (map #(format-branch-line facade %) branches))
         (when-let [{:keys [index value expected]} mismatch]
           (str "\n\n  arg " index " got " value ", expected " (format-expected expected)
                (when-let [refs (seq suggestions)]
                  (str "\n" (string/join "\n" (map #(str "  See: (g/get-schema " (pr-str %) ")") refs))))))
         "\n\n"
         "See: (g/get-schema " (:cmd call) ")")))

(defmulti  format-error (fn [err format-kw] [(::type err) format-kw]))
(defmethod format-error [::arity-error :string] [err _] (format-arity-error-string err))
(defmethod format-error [::parse-error :string] [err _] (format-parse-error-string err))
(defmethod format-error [::arity-error :data]   [err _] err)
(defmethod format-error [::parse-error :data]   [err _] err)

(defn compile-arity-schemas [edn-map]
  (into {} (map (fn [[arity edn]] [arity (g/schema edn)])) edn-map))

(defn- _match-arity
  "{:ok <cmd-map>}  OR  {:error <error-call-map>}"
  [{:keys [facade arity-schemas compiled-schemas cmd args normalize] :as call}]
  (let [arity  (count args)
        schema (get compiled-schemas arity)
        normalize (or normalize identity)]
    (assert (fn? normalize))
    (if-not schema
      {:error (arity-error (assoc call :supported-arities (keys compiled-schemas)))}
      (let [parsed (m/parse schema args)]
        (if (= ::m/invalid parsed)
          {:error (parse-error {:facade       facade
                                :arity        arity
                                :edn          (get arity-schemas arity)
                                :explain-data (m/explain schema args)
                                :cmd          cmd})}
          (let [normalized (normalize (extract-parse-values parsed))]
            (assert (map? normalized))
            (when-not (g/valid? cmd normalized)
              (throw (ex-info (str "normalize function for cmd " cmd " did not return correct shape")
                              {:cmd    cmd
                               :value  normalized
                               :errors (get (g/explain cmd normalized) :errors)
                               :call   call})))
            {:ok normalized}))))))

(defn match-arity
  "Tests user's passed args against the op's arity schema.
   When matched, args will be given names and result map given to normalize

   Normalize is expected to return the canonical cmd map representation"
  [opts & {:keys [format]}]
  (let [{:keys [error ok]} (_match-arity opts)]
    (if error
      (throw (ex-info (format-error error :string) error))
      ok)))

(defmacro defdwim
  [fn-name {:keys [facade cmd arities normalize] :as spec}]
  (assert (qualified-symbol? facade) facade)
  `(let [arity-schemas# ~arities
         compiled#      (compile-arity-schemas arity-schemas#)]
     (defn ~fn-name [args#]
       (match-arity {:facade           (quote ~facade)
                     :cmd              ~cmd
                     :arity-schemas    arity-schemas#
                     :compiled-schemas compiled#
                     :args             args#
                     :normalize        ~normalize}))))
