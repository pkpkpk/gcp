(ns gcp.global
  (:require [sci.core]
            [malli.core :as m]
            malli.edn
            [malli.error :as me]
            [malli.registry :as mr]
            [malli.util :as mu])
  (:import (clojure.lang ExceptionInfo)))

(defonce ^:dynamic *strict-mode* true)

(defonce *classes (atom {}))

(defmacro instance-schema
  [class-symbol]
  (assert (symbol? class-symbol))
  `(do
    (let [sym# (quote ~class-symbol)]
      (swap! gcp.global/*classes assoc sym# (import ~class-symbol)))
    [:fn
      {:error/message (str "not an instance of " (quote ~class-symbol))}
      '(fn [v#] (instance? ~class-symbol v#))]))

(defonce ^:dynamic *registry* (mr/composite-registry (m/default-schemas) (mu/schemas)))

(defn mopts [] {:registry *registry* ::m/sci-options {:classes @*classes}})

(defn get-schema [key] (get (mr/schemas *registry*) key))

(defn include-schema-registry! [registry]
  (if-let [{registry-name ::name} (meta registry)]
    (let [candidate (merge (mr/schemas *registry*) registry)]
      (when-let [bad-pairs (not-empty
                             (reduce (fn [acc [k schema]]
                                       (let [written   (try
                                                         (malli.edn/write-string schema {:registry candidate
                                                                                         ::m/sci-options {:classes @*classes}})
                                                         (catch Exception e
                                                           (let [error-type (:type (ex-data e))]
                                                             (if-let [bad-schema (and (= ::m/invalid-schema error-type)
                                                                                      (keyword? (get-in (ex-data e) [:data :schema]))
                                                                                      (= "gcp" (namespace (get-in (ex-data e) [:data :schema])))
                                                                                      (get-in (ex-data e) [:data :schema]))]
                                                               (if-let [body (get candidate bad-schema)]
                                                                 (throw (ex-info (str "invalid schema for key "  bad-schema) {:key bad-schema
                                                                                                                              :body body}))
                                                                 (throw (Exception. (str "missing schema for key " bad-schema))))
                                                               (throw (ex-info (str "error serializing schema for " k ": " (ex-message e))
                                                                               {:schema schema
                                                                                :cause  e}))))))
                                             recovered (try
                                                         (malli.edn/read-string written {:registry candidate
                                                                                         ::m/sci-options {:classes @*classes}})
                                                         (catch Exception e
                                                           (throw (ex-info (str "error recovering serialized schema for " k)
                                                                           {:schema schema
                                                                            :cause  e}))))]
                                         (when-not (= schema (m/form recovered))
                                           (assoc acc k schema)))) {} registry))]
        (throw (ex-info (str "edn-unsafe schema entries in " registry-name) {:unsafe bad-pairs})))
      (alter-var-root #'*registry* (fn [_extant]
                                     (println "successfully merged registry " registry-name)
                                     ;; TODO should we be cached compiled schemas instead?
                                     ;; TODO check if schema is already present and unchanged before re-compiling
                                     (mr/simple-registry candidate))))
    (throw (Exception. "registry must have metadata with a :gcp.global/name identifier"))))

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
     (m/schema ?schema (merge (mopts) opts)))))

;(defn match [value]) ;=> schema

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
