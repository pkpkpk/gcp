(ns gcp.global
  (:require [malli.core :as m]
            [malli.error :as me]))

(defonce ^:dynamic *strict-mode* true)

(defn instance-schema [class]
  [:fn {:error/message (str "not an instance of " (str class))} (partial instance? class)])

(defn satisfies-schema [protocol]
  [:fn {:error/message (str "must satisfy " (str protocol))} (partial satisfies? protocol)])

(defmacro strict! [schema-or-spec value]
  (let [{schema-class :class
         schema-name :name} (when (symbol? schema-or-spec)
                              (assoc (meta (resolve schema-or-spec))
                              :name (name schema-or-spec)))]
    (if-not *strict-mode*
      ~value
      `(let [schema# (if (m/schema? ~schema-or-spec)
                       ~schema-or-spec
                       (m/schema ~schema-or-spec))]
         (if-let [explanation# (m/explain schema# ~value)]
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
           ~value)))))
