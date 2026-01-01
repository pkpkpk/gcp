(ns gcp.global
  (:require
   [clojure.string :as string]
   [malli.core :as m]
   [malli.edn]
   [malli.error :as me]
   [malli.registry :as mr]
   [malli.util :as mu]
   [sci.core :as sci])
  (:import
   (clojure.lang ExceptionInfo)))

(defonce ^:dynamic *strict-mode* true)
(defonce ^:dynamic *dbg* false)
(defonce ^:dynamic *registry* (mr/composite-registry (m/default-schemas) (mu/schemas)))

(def *classes (atom {'java.lang.reflect.Modifier java.lang.reflect.Modifier
                     'java.lang.reflect.Method   java.lang.reflect.Method
                     'java.nio.ByteBuffer        java.nio.ByteBuffer
                     'java.util.List             java.util.List}))

(def sci-namespaces {'clojure.string      (sci/copy-ns clojure.string (sci/create-ns 'clojure.string {}))
                     'clojure.core        (sci/copy-ns clojure.core (sci/create-ns 'clojure.core {}))})

(defn mopts []
  {:registry       *registry*
   ::m/sci-options {:classes    @*classes
                    :namespaces sci-namespaces
                    :imports    {'ByteBuffer 'java.nio.ByteBuffer
                                 'List       'java.util.List}}})

(defmacro instance-schema
  [class-symbol]
  (assert (symbol? class-symbol))
  (let [fqcn (if (clojure.string/includes? (str class-symbol) ".")
               class-symbol
               (symbol (str "java.lang." class-symbol)))]
    `(do
       (let [sym# (quote ~class-symbol)]
         (swap! gcp.global/*classes assoc sym# (import ~class-symbol)))
       [:fn
        {:error/message (str "not an instance of " (quote ~class-symbol))}
        (list 'fn ['v] (list 'instance? (quote ~class-symbol) 'v))])))

(defn to-vec [v]
  (cond
    (nil? v) []
    (sequential? v) (vec v)
    :else [v]))

(defn get-schema [key] (get (mr/schemas *registry*) key))

(defn schema-key?
  "Returns true if k is a keyword shaped like :gcp.vertexai.v1.api/Schema.
   Rejects pseudo-qualifiers that don't have a namespace."
  [k]
  (and (keyword? k)
       (some? (namespace k))
       (some? (name k))
       (pos? (count (re-seq #"\." (namespace k))))))

(defn assert-schema-key!
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

(defn assert-registry-keys!
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

(defn include-schema-registry! [registry]
  (if-let [{registry-name ::name} (meta registry)]
    (do
      (assert-registry-keys! registry registry-name)
      (let [candidate (merge (mr/schemas *registry*) registry)]
        (when-let [bad-pairs (not-empty
                               (reduce (fn [acc [k schema]]
                                         (let [opts      (assoc (mopts) :registry candidate)
                                               written   (try
                                                           (malli.edn/write-string schema opts)
                                                           (catch Exception e
                                                             (let [error-data (ex-data e)
                                                                   error-type (:type error-data)]
                                                               (cond
                                                                 (and (= ::m/invalid-schema error-type)
                                                                      (get-in error-data [:data :schema]))
                                                                 (let [bad-schema (get-in error-data [:data :schema])]
                                                                   (if-let [body (get candidate bad-schema)]
                                                                     (throw (ex-info (str "invalid schema for key " bad-schema) {:key  bad-schema
                                                                                                                                 :body body}))
                                                                     (throw (ex-info (str "missing schema for key " bad-schema) {:key bad-schema}))))

                                                                 (= ::m/invalid-ref error-type)
                                                                 (throw (ex-info (str "invalid ref in schema for " k ": " (get-in error-data [:data :ref]))
                                                                                 {:schema schema
                                                                                  :data   error-data}))

                                                                 :else
                                                                 (throw (ex-info (str "error serializing schema for " k ": " (ex-message e))
                                                                                 {:schema schema
                                                                                  :data   error-data
                                                                                  :cause  e}))))))
                                               recovered (try
                                                           (malli.edn/read-string written opts)
                                                           (catch Exception e
                                                             (throw (ex-info (str "error recovering serialized schema for " k)
                                                                             {:schema schema
                                                                              :cause  e}))))]
                                           (when-not (= (m/form (m/schema schema opts)) (m/form recovered))
                                             (assoc acc k schema)))) {} registry))]
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

(defn assert-disjoint-keys! [registries]
  (let [all-keys (mapcat keys registries)
        freqs (frequencies all-keys)
        clashes (->> freqs (filter #(> (val %) 1)) (map key) set)]
    (when (seq clashes)
      (throw (ex-info "overlapping keys" {:keys clashes})))))

(defn valid? [?schema value]
  (try
    (m/validate ?schema value (mopts))
    (catch ExceptionInfo ei
      (if (and (keyword? ?schema)
               (= ":malli.core/invalid-schema" (ex-message ei))
               (nil? (get-schema ?schema)))
        (throw (ex-info (str "missing schema for " ?schema " in registry") {:schema ?schema :value value}))
        (throw ei)))))

(defn properties [schema]
  (some-> (get-schema schema) (m/properties (mopts))))

(defn geminize
  "Cleans up a Malli explanation object for better readability/usability.
   - Replaces the full top-level schema definition with its registry key (if available).
   - Removes opaque function/validator objects from errors.
   - Ensures the output is data-preserving enough to be useful (keys, paths, values)
     but concise enough to log."
  [explanation ?schema]
  (cond-> explanation
          (keyword? ?schema) (assoc :schema ?schema)
          true (update :errors (fn [errs]
                                 (mapv (fn [err]
                                         (dissoc err :fn :validator))
                                       errs)))))

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
   (let [gemini (geminize explanation schema)
         human (humanize gemini)
         human-str (pr-str human)
         human-str (if (< (count human-str) 60)
                     human-str
                     (str (subs human-str 0 60) "..."))
         props (properties schema)
         msg   (if-let [clazz (:class props)]
                 (str "schema for class " clazz " failed: " human-str)
                 (str "schema failed: " human-str))]
     (ex-info msg {:explain gemini
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
     (m/schema ?schema (merge (mopts) opts)))))

#!-----------------------------------------------------------------------------

;; TODO some clients are tied to projects, have stateful thread pools + connections
;;  (ie pubsub admins etc) those clients will need .shutdown() .waitForShutdown() .shutdownNow()
;;  etc as part of Lifecycle/GC/TTL systems

;; schema-key -> args -> instance
(defonce *client-cache (atom {}))

(defn get-client [key opts] (get-in @*client-cache [key opts]))

(defn put-client! [key opts client] (swap! *client-cache assoc-in [key opts] client))

(defn client [key opts]
  (or (get-client key opts)
      (if-let [from-edn (:from-edn (properties key))]
        (let [client (if (fn? from-edn)
                       (from-edn opts)
                       (if (qualified-symbol? from-edn)
                         ((requiring-resolve from-edn) opts)
                         (throw (ex-info ":from-edn must be function or qualified symbol" {:key key}))))]
          (put-client! key opts client)
          client)
        (throw (Exception. (str "missing :from-edn in schema for key " key))))))
