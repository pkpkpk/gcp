(ns gcp.dev.toolchain.shared
  (:require
   [clojure.core.match :refer [match]]
   [clojure.set :as set]
   [gcp.dev.util :as u]))

(def primitive-types
  #{'byte 'short 'int 'long 'float 'double 'boolean 'char})

(defn primitive? [t]
  (contains? primitive-types t))

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
    :mutable-pojo
    :pojo
    :read-only
    :resource-extended
    :sentinel
    :static-factory
    :static-utilities
    :static-variants
    :string-enum
    :union-abstract
    :union-concrete
    :union-tagged
    :union-protobuf-oneof
    :protobuf-message
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
    :nested/mutable-pojo
    :nested/variant-pojo
    :nested/read-only
    :nested/static-utilities
    :nested/static-variants
    :nested/client-options
    :nested/static-factory
    :nested/string-enum
    :nested/union-abstract
    :nested/union-tagged
    :nested/union-protobuf-oneof
    :nested/protobuf-message
    :nested/variant-read-only})

(defn categorize-type [deps t]
  (assert (some? t))
  (if (vector? t)
    (match t
           [:type-parameter _] :generic
           [:array E]                                           [:array (categorize-type deps E)]
           [(:or 'java.util.List
                 'com.google.common.collect.ImmutableList) E]   [:list (categorize-type deps E)]
           [(:or 'java.util.Set
                 'com.google.common.collect.ImmutableSet) E]    [:set (categorize-type deps E)]
           [(:or 'java.util.Map
                 'com.google.common.collect.ImmutableMap) K V]  [:map (categorize-type deps K) (categorize-type deps V)]
           [(:or 'java.lang.Iterable 'java.util.Collection) E]  [:iterable (categorize-type deps E)]
           ['java.util.Iterator E]                              [:iterator (categorize-type deps E)]
           ['java.util.Optional E]                              [:optional (categorize-type deps E)]
           ['? :extends T]                                      (keyword "generic" (name (categorize-type deps T)))
           [T E]                                                [(categorize-type deps T) (categorize-type deps E)]
           [T A B]                                              [(categorize-type deps T) (categorize-type deps A) (categorize-type deps B)]
           :else (throw (ex-info (str "unknown vector type representation: " t) {:deps deps :param-type t})))
    (cond
      (contains? (:scalars deps) t) :scalar

      (contains? (:native deps) t) :native

      ('#{?} t)
      :generic

      (contains? (:custom-mappings deps) t)
      :custom

      (contains? (:support-mappings deps) t)
      :support

      (u/enum? t)
      :enum

      (contains? (:foreign-mappings deps) t)
      :foreign
      (get (:peer-mappings deps) t)
      (if (u/nested-fqcn? t)
        :peer/nested
        :peer)
      (contains? (:sibling deps) t)
      :sibling
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
