(ns gcp.global
  (:require [malli.core :as m]
            [malli.edn :as edn]
            [malli.error :as me]
            [malli.registry :as mr]
            [malli.util :as mu])
  (:import (clojure.lang ExceptionInfo)))

(defonce ^:dynamic *strict-mode* true)

(defmacro instance-schema [class-symbol]
  (assert (symbol? class-symbol))
  `[:fn {:error/message (str "not an instance of " (str ~class-symbol))}
    '(fn [v#] (instance? (import ~class-symbol) v#))])

(defmacro satisfies-schema [protocol]
  (assert (qualified-symbol? protocol))
  (let [ns (symbol (namespace protocol))
        sym (symbol (name protocol))]
    (println " ns " ns " sym" sym)
    ()
    `[:fn
      #_{:error/message (str "must satisfy " (str protocol))}
      '(fn [v#]
         (satisfies? (requiring-resolve ~protocol) v#))]))

(defonce ^:dynamic *registry* (mr/composite-registry (m/default-schemas) (mu/schemas)))

(defn edn-safe-schema?
  ([schema registry]
   (= schema (m/form (edn/read-string (edn/write-string schema {:registry registry}) {:registry registry})
                     {:registry registry}))))

(defn include-schema-registry! [registry]
  (if-let [{registry-name ::name} (meta registry)]
    (let [candidate (mr/composite-registry *registry* registry)]
      (when-let [bad-pairs (not-empty
                             (reduce (fn [acc [k schema]]
                                       (let [written   (try
                                                         (malli.edn/write-string schema {:registry candidate})
                                                         (catch Exception e
                                                           (throw (ex-info (str "error serializing schema for " k ": " (ex-message e))
                                                                           {:schema schema
                                                                            :cause e}))))
                                             recovered (try
                                                         (malli.edn/read-string written {:registry candidate})
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
                                     ;; everything built in 'recovered' above can be reused
                                     candidate)))
    (throw (Exception. "registries must have metadata"))))

(defn valid? [?schema value] (m/validate ?schema value {:registry *registry*}))

(defn properties [schema]
  (some-> (get *registry* schema) (m/properties {:registry *registry*})))

(defn explain [?schema value]
  (try
    (m/explain ?schema value {:registry *registry*})
    (catch ExceptionInfo ei
      (if (and (keyword? ?schema)
               (= ":malli.core/invalid-schema" (ex-message ei))
               (nil? (get *registry* ?schema)))
        (throw (ex-info (str "missing schema for " ?schema " in registry") {:schema ?schema :value value}))
        (if-let [bad-schema (get-in (ex-data ei) [:data :schema])]
          (if (and (keyword? bad-schema)
                   (nil? (get *registry* bad-schema)))
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
         props (properties schema)
         msg   (if-let [clazz (:class props)]
                 (str "schema for class " clazz " failed.")
                 (str "schema failed."))]
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
   (m/coerce ?schema value xf {:registry *registry*})))

(defmacro strict! [schema-or-spec value]
  `(if-not *strict-mode* ~value (coerce ~schema-or-spec ~value)))

(defn schema
  ([?schema]
   (schema ?schema nil))
  ([?schema opts]
   (if (keyword? ?schema)
     (m/schema (get *registry* ?schema) {:registry *registry*})
     (m/schema ?schema (merge {:registry *registry*} opts)))))

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
