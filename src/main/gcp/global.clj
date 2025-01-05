(ns gcp.global
  (:require [malli.core :as m]
            [malli.registry :as r]
            [malli.error :as me])
  (:import (clojure.lang ExceptionInfo)))

(defonce ^:dynamic *strict-mode* true)

(defn instance-schema [class]
  [:fn {:error/message (str "not an instance of " (str class))} (partial instance? class)])

(defn satisfies-schema [protocol]
  [:fn {:error/message (str "must satisfy " (str protocol))} (partial satisfies? protocol)])

(defonce ^:dynamic *registry* (m/default-schemas))

;; TODO cache compiled schemas & explainers?

(defn include! [r]
  (alter-var-root #'*registry* (fn [m] (merge m r))))

(defn valid? [?schema value]
  (m/validate ?schema value {:registry *registry*}))

(defn coerce
  ([?schema value]
   (m/coerce ?schema value nil {:registry *registry*}))
  ([?schema value xf]
   (m/coerce ?schema value xf {:registry *registry*})))

(defn properties [schema]
  (m/properties schema {:registry *registry*}))

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

(defmacro strict! [schema-or-spec value]
  (if-not *strict-mode*
    value
    (do
      (when-let [schema-name (and (symbol? schema-or-spec) (name schema-or-spec))]
        (println "PHASE OUT OLD SCHEMA: " schema-name))
      `(if-let [explanation# (explain ~schema-or-spec ~value)]
         (throw (human-ex-info ~schema-or-spec explanation# ~value))
         ~value))))
