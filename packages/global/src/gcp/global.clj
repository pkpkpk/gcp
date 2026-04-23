(ns gcp.global
  (:refer-clojure :exclude [merge])
  (:require
    clojure.string
    [malli.core :as m]
    [malli.edn]
    [malli.error :as me]
    [malli.registry :as mr]
    [malli.util :as mu]
    [sci.core :as sci])
  (:import
    (clojure.lang ExceptionInfo)
    (java.time LocalTime LocalDate LocalDateTime OffsetDateTime Duration)
    (org.threeten.extra PeriodDuration)))

(def *classes (atom {}))

(defn- parse-class-symbol [class-symbol]
  (if (simple-symbol? class-symbol)
    [class-symbol nil]
    (let [nest (name class-symbol)]
      (assert (and (= 1 (count nest))
                   (some? (re-find #"\d" nest))) "qualified symbols representing arrays must have singular digit name")
      [(symbol (namespace class-symbol)) (Integer/parseInt nest)])))

(defmacro instance-schema
  "Given fully qualified class symbol, create a instance predicate schema.
   This will register the class into the global sci environment"
  ([class-symbol]
   `(instance-schema nil ~class-symbol))
  ([properties class-symbol]
   (assert (symbol? class-symbol))
   (when properties (assert (map? properties)))
   (let [[scalar-sym array-nest] (parse-class-symbol class-symbol)
         scalar-class (eval `(import ~scalar-sym))
         array-class  (when array-nest
                        (loop [i     array-nest
                               class scalar-class]
                          (if (zero? i)
                            class
                            (recur (dec i) (.arrayType class)))))
         props (clojure.core/merge
                 {:error/message (str "not an instance of " class-symbol)}
                 properties)]
     `(do
        (swap! *classes assoc '~scalar-sym ~scalar-sym)
        ~@(when array-class
            `((swap! *classes assoc '~class-symbol (Class/forName ~(.getName ^Class array-class)))))
        [:fn ~props (quote (~'fn [~'v] (~'instance? ~class-symbol ~'v)))]))))

(def built-in-schemas
  {:bigint         (instance-schema java.math.BigInteger)
   :bigdec         'decimal?
   :regexp         (instance-schema java.util.regex.Pattern)
   :inst           'inst?
   :i32            [:int {:min -2147483648 :max 2147483647}]
   :pi32           [:int {:min 0 :max 2147483647}]
   :i64            :int
   :pi64           [:int {:min 0}]
   :f32            [:or (instance-schema java.lang.Float) [:double {:min -3.4028235E38 :max 3.4028235E38}]]
   :f64            'float?
   :byte           (instance-schema java.lang.Byte)
   :char           'char?
   :Timestamp      'inst?
   :Time           (instance-schema java.time.LocalTime)
   :Date           (instance-schema java.time.LocalDate)
   :DateTime       (instance-schema java.time.LocalDateTime)
   :OffsetDateTime (instance-schema java.time.OffsetDateTime)
   :Duration       (instance-schema java.time.Duration)
   :PeriodDuration (instance-schema org.threeten.extra.PeriodDuration)})

(defonce ^:dynamic *strict-mode* true)
(defonce ^:dynamic *dbg* false)
(defonce ^:dynamic *registry* (mr/composite-registry
                                built-in-schemas
                                (m/default-schemas)
                                (mu/schemas)))

(def sci-namespaces {'clojure.string      (sci/copy-ns clojure.string (sci/create-ns 'clojure.string {}))
                     'clojure.core        (sci/copy-ns clojure.core (sci/create-ns 'clojure.core {}))})

(defn mopts []
  {:registry       *registry*
   ::m/sci-options {:classes    @*classes
                    :namespaces sci-namespaces}})

(defn get-all-schemas [] (mr/schemas *registry*))

(defn get-schema [key] (get (mr/schemas *registry*) key))

(defn- schema-key?
  "Returns true if k is a keyword shaped like :gcp.vertexai.v1.api/Schema.
   Rejects pseudo-qualifiers that don't have a namespace."
  [k]
  (and (keyword? k)
       (some? (namespace k))
       (some? (name k))
       (pos? (count (re-seq #"\." (namespace k))))))

(defn- assert-schema-key!
  "Throws a helpful error when a schema registry key is not a qualified keyword.
   This catches dotted keys like :gcp.vertexai.v1.api.Schema early."
  [k]
  (when (keyword? k)
    (when-not (schema-key? k)
      (throw (ex-info (str "invalid schema registry key " k
                           " (expected qualified keyword like :gcp.vertexai.v1.api/Schema; "
                           "Clojure keyword namespaces use '/', not '.')")
                      {:key          k
                       :expected     :qualified-keyword
                       :example      :gcp.vertexai.v1.api/Schema
                       :hint         "rename the key and update [:ref ...] sites accordingly"}))))
  k)

(defn- assert-registry-keys!
  "Validates registry keys before attempting Malli EDN roundtrip/compile.
   Strings are allowed (Malli refs can be strings), but keyword keys must be qualified."
  [registry registry-name]
  (let [bad (not-empty
              (reduce (fn [acc [k _v]]
                        (try
                          (cond
                            (keyword? k) (do (assert-schema-key! k) acc)
                            (string? k)  acc
                            :else        (conj acc k))
                          (catch ExceptionInfo ei
                            (conj acc {:key k :error (ex-data ei)}))))
                      [] registry))]
    (when bad
      (throw (ex-info (str "invalid schema registry keys in " registry-name)
                      {:bad bad
                       :registry-name registry-name})))))

(defn- schemas-equivalent? [form1 form2]
  (cond
    (and (instance? java.util.regex.Pattern form1)
         (instance? java.util.regex.Pattern form2))
    (= (str form1) (str form2))

    (and (sequential? form1) (sequential? form2))
    (and (= (count form1) (count form2))
         (every? true? (map schemas-equivalent? form1 form2)))

    (and (map? form1) (map? form2))
    (and (= (count form1) (count form2))
         (every? true? (map (fn [[k1 v1]]
                              (schemas-equivalent? v1 (get form2 k1)))
                            form1)))

    :else
    (= form1 form2)))

(defn- written-res [schema opts]
  (try
    [nil (malli.edn/write-string schema opts)]
    (catch Exception err
      [err])))

(defn- read-res [written opts]
  (try
    [nil (malli.edn/read-string written opts)]
    (catch Exception e
      [e])))

(defn- safety-check-schema [schema opts]
  (let [[err written :as res] (written-res schema opts)]
    (if err
      res
      (let [[err recovered :as res] (read-res written opts)]
        (if err
          res
          (let [same? (schemas-equivalent? (m/form (m/schema schema opts)) (m/form recovered))]
            (if same?
              [nil true]
              [nil false])))))))

(defn include-schema-registry! [registry]
  (if-let [{registry-name ::name} (meta registry)]
    (do
      (assert-registry-keys! registry registry-name)
      (let [candidate (clojure.core/merge (mr/schemas *registry*) registry)]
        (when-let [bad-pairs (not-empty
                               (reduce
                                 (fn [acc [k schema]]
                                   (let [opts (assoc (mopts) :registry candidate)
                                         [err same?] (safety-check-schema schema opts)]
                                     (if (or err (false? same?))
                                       (assoc acc k {:schema schema :err err :same? same?})
                                       acc)))
                                 {}
                                 registry))]
          (throw (ex-info (str "edn-unsafe schema entries in " registry-name) {:unsafe bad-pairs})))
        (alter-var-root #'*registry* (fn [_extant]
                                       (when (or *dbg* (System/getenv "GCP_DEBUG"))
                                         (println "successfully merged registry " registry-name))
                                       ;; TODO should we be cached compiled schemas instead?
                                       ;; TODO check if schema is already present and unchanged before re-compiling
                                       (mr/simple-registry candidate)))))
    (throw (Exception. "registry must have metadata with a :gcp.global/name identifier"))))

(defn register-schema! [key schema]
  (assert-schema-key! key)
  (include-schema-registry! (with-meta {key schema} {::name (str key)})))

(defn valid? [?schema value]
  (try
    (m/validate ?schema value (mopts))
    (catch ExceptionInfo ei
      (if (and (keyword? ?schema)
               (= ":malli.core/invalid-schema" (ex-message ei))
               (nil? (get-schema ?schema)))
        (throw (ex-info (str "missing schema for " ?schema " in registry") {:schema ?schema :value value}))
        (throw ei)))))

(defn properties [schema-or-key]
  (if (keyword? schema-or-key)
    (some-> (get-schema schema-or-key) (m/properties (mopts)))
    (m/properties schema-or-key (mopts))))

(defn explain [?schema value]
  (try
    (m/explain ?schema value (mopts))
    (catch ExceptionInfo ei
      (if (and (keyword? ?schema)
               (= ":malli.core/invalid-schema" (ex-message ei))
               (nil? (get-schema ?schema)))
        (throw (ex-info (str "missing schema for " ?schema " in registry") {:schema ?schema :value value}))
        (if-let [bad-schema (get-in (ex-data ei) [:data :schema])]
          (if (and (keyword? bad-schema)
                   (nil? (get-schema bad-schema)))
            (throw (ex-info (str "missing nested schema " bad-schema)
                            {:schema ?schema
                             :data   (ex-data ei)
                             :value  value}))
            (throw (ex-info (str "bad schema: " bad-schema) {:schema ?schema
                                                             :data   (ex-data ei)
                                                             :value  value})))
          (throw ei))))))

(defn humanize [explanation]
  #_(let [human (me/humanize explanation)]
      (if (not= (count human) (count (:errors explanation)))
        (mapv me/error-message (:errors explanation))
        human))
  (me/humanize explanation))

(defn human-ex-info
  ([schema value]
   (human-ex-info schema (explain schema value) value))
  ([schema explanation value]
   (let [human (humanize explanation)
         human-str (pr-str human)
         human-str (if (< (count human-str) 60)
                     human-str
                     (str (subs human-str 0 60) "..."))
         props (properties schema)
         msg   (if-let [clazz (:class props)]
                 (str "schema for class " clazz " failed: " human-str)
                 (str "schema failed: " human-str))]
     (ex-info msg {:explain explanation
                   :human   human
                   :props   props
                   :value   value}))))

(defn coerce
  ([schema value]
   (if-let [explanation (explain schema value)]
     (throw (human-ex-info schema explanation value))
     value))
  ([?schema value xf]
   (m/coerce ?schema value xf (mopts))))

(defmacro strict! [schema-or-spec value]
  `(if-not *strict-mode* ~value (coerce ~schema-or-spec ~value)))

(defn get-schema [key]
  (get (mr/schemas *registry*) key))

(defn schema
  ([?schema]
   (schema ?schema nil))
  ([?schema opts]
   (if-let [?schema (and (keyword? ?schema) (get-schema ?schema))]
     (m/schema (get *registry* ?schema) (mopts))
     (m/schema ?schema (clojure.core/merge (mopts) opts)))))

(defn form [schema]
  (m/form schema mopts))

(defn merge
  [schema1 schema2]
  (mu/merge schema1 schema2 (mopts)))

#!----------------------------------------------------------------------------------------------------------------------

(defn to-vec [v]
  (cond
    (nil? v) []
    (sequential? v) (vec v)
    :else [v]))

(defn get-private-field
  [obj field-name]
  (loop [clazz (class obj)]
    (if-not clazz
      (throw (NoSuchFieldException. (str "Field " field-name " not found in class hierarchy.")))
      (if-let [f (try
                   (.getDeclaredField clazz field-name)
                   (catch NoSuchFieldException _ nil))]
        (do
          (.setAccessible f true)
          (.get f obj))
        (recur (.getSuperclass clazz))))))

(defn invoke-private-method
  "Invokes a no-arg private or package-private method on obj."
  [obj method-name]
  (loop [clazz (class obj)]
    (if-not clazz
      (throw (NoSuchMethodException. (str "Method " method-name " not found in class hierarchy.")))
      (if-let [m (try
                   (.getDeclaredMethod clazz method-name (into-array Class []))
                   (catch NoSuchMethodException _ nil))]
        (do
          (.setAccessible m true)
          (.invoke m obj (object-array 0)))
        (recur (.getSuperclass clazz))))))