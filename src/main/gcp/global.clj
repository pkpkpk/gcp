(ns gcp.global
  (:require [malli.core :as m]
            [malli.registry :as r]
            [malli.error :as me]))

(defonce ^:dynamic *strict-mode* true)

(defn instance-schema [class]
  [:fn {:error/message (str "not an instance of " (str class))} (partial instance? class)])

(defn satisfies-schema [protocol]
  [:fn {:error/message (str "must satisfy " (str protocol))} (partial satisfies? protocol)])

(defonce ^:dynamic *registry* (m/default-schemas))

(defn include! [r]
  (alter-var-root #'*registry* (fn [m] (merge m r))))

(defn valid? [?schema value]
  (m/validate ?schema value {:registry *registry*}))

(defn coerce
  ([?schema value]
   (m/coerce ?schema value nil {:registry *registry*}))
  ([?schema value xf]
   (m/coerce ?schema value xf {:registry *registry*})))

(defn explain [?schema value]
  (m/explain ?schema value {:registry *registry*}))

(defn humanize [explanation]
  (me/humanize explanation))

(defmacro strict! [schema-or-spec value]
  (if-not *strict-mode*
    value
    (if (keyword? schema-or-spec)
      (do
        ;(m/properties schema)
        ;(println "NOT CHECKING!")
        value)
      (let [{schema-class :class
             schema-name :name} (when (symbol? schema-or-spec)
                                  (assoc (meta (resolve schema-or-spec))
                                    :name (name schema-or-spec)))]
        (when schema-name
          (println "PHASE OUT OLD SCHEMA: " schema-name))
        `(let [schema# (if (m/schema? ~schema-or-spec)
                         ~schema-or-spec
                         (m/schema ~schema-or-spec))]
           (if-let [explanation# (m/explain schema# ~value {:registry *registry*})]
             (let [human# (me/humanize explanation#)
                   human# (if (not= (count human#) (count (:errors explanation#)))
                            (mapv me/error-message (:errors explanation#))
                            human#)
                   msg#   (if ~schema-class
                            (str ~schema-class " schema failed : " human#)
                            (str "Schema failed : " human#))]
               (throw (ex-info msg# {:human   human#
                                     :value   ~value
                                     :explain explanation#})))
             ~value))))))
