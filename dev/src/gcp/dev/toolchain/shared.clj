(ns gcp.dev.toolchain.shared
  (:require [clojure.core.match :refer [match]]
            [clojure.set :as set]))

#! IT IS FORBIDDEN TO MODIFY THIS SET WITHOUT ELICITING USER FOR APPROVAL
(def categories
  #{:accessor-with-builder
    :client
    :collection-wrapper
    :enum
    :error
    :exception
    :factory
    :functional-interface
    :interface
    :pojo
    :read-only
    :resource-extended
    :sentinel
    :static-factory
    :statics
    :string-enum
    :union-abstract
    :union-concrete
    :union-tagged
    :variant-accessor

    :other
    :nested/other

    ;; Nested Categories
    :nested/accessor-with-builder
    :nested/builder
    :nested/client
    :nested/collection-wrapper
    :nested/enum
    :nested/factory
    :nested/pojo
    :nested/read-only
    :nested/statics
    :nested/static-factory
    :nested/string-enum
    :nested/union-abstract
    :nested/union-tagged
    :nested/variant-read-only})

(defn categorize-type [deps t]
  (assert (some? t))
  (if (vector? t)
    (match t
           [:type-parameter _] :generic
           [:array E]                                        [:array (categorize-type deps E)]
           [(:or 'java.util.List
              'com.google.common.collect.ImmutableList) E]   [:list (categorize-type deps E)]
           [(:or 'java.util.Set
              'com.google.common.collect.ImmutableSet) E]    [:set (categorize-type deps E)]
           [(:or 'java.util.Map
              'com.google.common.collect.ImmutableMap) K V]  [:map (categorize-type deps K) (categorize-type deps V)]
           ['java.lang.Iterable E]                           [:iterable (categorize-type deps E)]
           ['java.util.Iterator E]                           [:iterator (categorize-type deps E)]
           ['java.util.Optional E]                           [:optional (categorize-type deps E)]
           [T E]                                             [(categorize-type deps T) (categorize-type deps E)]
           [T A B]                                           [(categorize-type deps T) (categorize-type deps A) (categorize-type deps B)]
           :else (throw (ex-info (str "unknown vector type representation: " t) {:deps deps :param-type t})))
    (cond
      ('#{java.lang.String
          java.util.UUID
          java.util.regex.Pattern
          void java.lang.Void
          double java.lang.Double
          long java.lang.Long
          int java.lang.Integer
          byte java.lang.Byte
          char java.lang.Char
          float java.lang.Float
          java.time.Instant
          java.lang.Object
          boolean java.lang.Boolean
          java.math.BigInteger
          java.math.BigDecimal} t) :scalar

      ('#{?} t)
      :generic

      (contains? (:collection-wrappers deps) t)
      [:collection-wrapper (categorize-type deps (get-in deps [:collection-wrappers t]))]

      (contains? (:custom-mappings deps) t)
      :custom
      (contains? (:foreign-mappings deps) t)
      :foreign
      (get (:peer-mappings deps) t)
      :peer
      (contains? (:nested deps) t)
      :nested
      (= t (:self deps))
      :self
      true
      (throw (ex-info (str "could not categorize type: " t) {:deps deps :param-type t})))))

(defn intersecting-methods?
  [methods]
  (if-not (apply = (map :name methods))
    false
    (let [params (mapv (comp set :parameters) methods)]
      (if (= 1 (count params))
        false
        (loop [i 0]
          (if (= i (dec (count params)))
            true
            (if (empty? (set/intersection (nth params i) (nth params (inc i))))
              false
              (recur (inc i)))))))))