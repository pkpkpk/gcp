(ns gcp.global
  (:require [malli.core :as m]
            [malli.error :as me]
            [malli.registry :as r]
            [malli.util :as mu])
  (:import (clojure.lang ExceptionInfo)))

(defonce ^:dynamic *strict-mode* true)

(defn instance-schema [class]
  [:fn {:error/message (str "not an instance of " (str class))} (partial instance? class)])

(defn satisfies-schema [protocol]
  [:fn {:error/message (str "must satisfy " (str protocol))} (partial satisfies? protocol)])

(defonce ^:dynamic *registry* (merge (m/default-schemas) (mu/schemas)))

;; TODO cache compiled schemas & explainers?

(defn include! [r] (alter-var-root #'*registry* (fn [m] (merge m r))))

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
  (let [human (me/humanize explanation)]
    (if (not= (count human) (count (:errors explanation)))
      (mapv me/error-message (:errors explanation))
      human)))

(defn human-ex-info [schema explanation value]
  (let [human (humanize explanation)
        props (properties schema)
        msg   (if-let [clazz (:class props)]
                (str "schema for class " clazz " failed.")
                (str "schema failed."))]
     (ex-info msg {:explain explanation
                   :human   human
                   :props   props
                   :value   value})))

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
