(ns gcp.dev.analyzer
  (:require
    [clj-http.client :as http]
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.repl :refer :all]
    [clojure.set :as s]
    [clojure.string :as string]
    [gcp.dev.analyzer.extract :as extract]
    [gcp.dev.models :as models]
    [gcp.dev.packages :as packages]
    [gcp.dev.store :as store]
    [gcp.dev.util :as util :refer :all]
    [gcp.global :as g]
    gcp.vertexai.v1
    [rewrite-clj.zip :as z]
    [zprint.core :as zp :refer [zprint]])
  (:import (com.google.cloud.bigquery BigQueryOptions)
           (java.io ByteArrayOutputStream)
           (java.util.regex Pattern)))

(defn zip-static-params [package static-params getterMethods]
  (let [getters           (g/coerce [:seqable :string] (vec (sort (map name (keys getterMethods)))))
        getter-schema     {:type        "STRING"
                           :nullable    false
                           :enum getters
                           :description "the name of the read only method that corresponds to the param"}
        generationConfig  {:responseMimeType "application/json"
                           :responseSchema   {:type       "OBJECT"
                                              :required   (vec static-params)
                                              :properties (into {} (map #(vector % getter-schema)) static-params)}}
        systemInstruction "for each parameter name, produce the getter method that produces it"
        cfg               (assoc models/flash :systemInstruction systemInstruction
                                              :generationConfig generationConfig)
        validator!        (fn [edn]
                            (if (not= (set static-params) (set (map name (keys edn))))
                              (throw (ex-info "missing static param!"
                                              {:getters getters :static-params static-params :edn edn}))
                              (if (not (every? some? (vals edn)))
                                (throw (ex-info "expected getter for every static param"
                                                {:getters getters :static-params static-params :edn edn}))
                                (if-not (clojure.set/subset? (set (vals edn)) (into #{} getters))
                                  (throw (ex-info "incorrect keys" {:edn     edn
                                                                    :getters getters
                                                                    :static-params static-params}))))))
        res               (store/generate-content-aside (:store package) cfg getters validator!)]
    (into (sorted-map) (map (fn [[k v]] [k (keyword v)])) res)))

(defn analyze-static-factory
  [package className]
  (assert (contains? (:types/static-factories package) className))
  (let [{classDoc :doc :keys [getterMethods staticMethods] :as readonly} (extract/$extract-type-detail package className)
        min-param-names (let [n (apply min (map count (get staticMethods :of)))]
                          (reduce
                            (fn [acc arg-list]
                              (if-not (= n (count arg-list))
                                acc
                                (into acc (map :name) arg-list)))
                            #{}
                            (get staticMethods :of)))
        all-static-params (reduce
                            (fn [acc [_ arg-lists]]
                              (reduce #(into %1 (map :name) %2) acc arg-lists))
                            #{}
                            staticMethods)
        zipped (zip-static-params package all-static-params getterMethods)
        fields (into (sorted-map)
                     (map
                       (fn [[param getter-key]]
                         (let [{:as getterMethod} (get getterMethods getter-key)
                               field (cond-> {:getterMethod (symbol getter-key)
                                              :getterReturnType   (get getterMethod :returnType)
                                              :optional     (not (contains? min-param-names (name param)))}
                                             ;; TODO move doc cleaning upstream, remove newlines & normalize spacing
                                             (and (some? (get getterMethod :doc))
                                                  (not (string/blank? (get getterMethod :doc)))) (assoc :getterDoc (get getterMethod :doc)))]
                           [param field])))
                     zipped)
        static-method-types (reduce
                              (fn [acc [_ arg-lists]]
                                (reduce #(into %1 (map :type) %2) acc arg-lists))
                              #{}
                              staticMethods)
        all-types (into #{} (comp
                              (map parse-type)
                              (remove native-type))
                        (into static-method-types (map :returnType) (vals getterMethods)))]
    {::type             :static-factory
     ::key              (package-key package className)
     :className         className
     :doc               classDoc
     :fields            fields
     :typeDependencies  all-types
     :staticMethods     staticMethods}))

(defn zip-accessors
  ([package getters setters]
   (let [getter-schema     {:type        "STRING"
                            :nullable    false
                            :description "the read only method that corresponds to the setter"}
         generationConfig  {:responseMimeType "application/json"
                            :responseSchema   {:type       "OBJECT"
                                               :required   setters
                                               :properties (into {} (map #(vector % getter-schema)) setters)}}
         systemInstruction (str "given a list of getters, match them to their setter in the response schema."
                                "for every setter there is a getter, but not every getter has a setter")
         cfg               (assoc models/flash-2 :systemInstruction systemInstruction
                                                 :generationConfig generationConfig)
         validator!        (fn [edn]
                             (if (not= (set setters) (set (map name (keys edn))))
                               (throw (ex-info "missing setter method!" {:getters getters
                                                                         :setters setters
                                                                         :edn edn}))
                               (if (not (every? some? (vals edn)))
                                 (throw (ex-info "expected getter for every setter" {:getters getters
                                                                                     :setters setters
                                                                                     :edn edn}))
                                 (if-not (or (set (vals edn)) (set getters)
                                             (clojure.set/subset? (set (vals edn)) (set getters)))
                                   (throw (ex-info "incorrect keys" {:getters getters
                                                                     :setters setters
                                                                     :edn edn}))))))
         res (store/generate-content-aside (:store package) cfg getters validator!)]
     (into (sorted-map) (map (fn [[k v]] [k (keyword v)])) res))))

(defn abstract-variant? [package className]
  (boolean
    (when-let [base (get-in package [:types/variants className])]
      (get-in package [:types/abstract-unions base]))))

(defn abstract-getters [className]
  (let [{:keys [members]} (clojure.reflect/reflect (as-class className))]
    (reduce
      (fn [acc m]
        (if (and (contains? m :return-type)
                 (contains? (:flags m) :public)
                 (contains? (:flags m) :abstract)
                 (not (#{'toBuilder} (:name m))))
          (assoc acc (keyword (name (:name m))) {:returnType (get m :returnType)
                                                 :doc (str "abstract method inherited from " className)})
          acc))
      {}
      members)))

(defn analyze-accessor
   "a readonly class with builder, together as a complete binding unit. no associated types"
  [package className]
  (assert (contains? (:types/accessors package) className))
  (assert (not (builder-like? className)))
  (let [{classDoc :doc :keys [getterMethods staticMethods] :as readonly} (extract/$extract-type-detail package className)
        {:keys [setterMethods] :as builder} (extract/$extract-type-detail package (str className ".Builder"))
        setters  (g/coerce [:seqable :string] (sort (map name (keys setterMethods))))
        getterMethods (cond-> getterMethods
                              (abstract-variant? package className)
                              (merge (abstract-getters (get-in package [:types/variants className]))))
        getters  (g/coerce [:seqable :string] (sort (map name (keys getterMethods))))
        zipped (zip-accessors package getters setters)
        min-param-names (when-let [ms (not-empty (get staticMethods :newBuilder))]
                          (let [n (apply min (map count ms))]
                            (into #{} (comp
                                        (filter #(= n (count %)))
                                        (map :name))
                                  (get staticMethods :newBuilder))))
        fields (into (sorted-map)
                     (map
                       (fn [[setter-key getter-key]]
                         (let [{:as setterMethod} (get setterMethods setter-key)
                               {:as getterMethod} (get getterMethods getter-key)
                               _ (when (nil? getterMethod)
                                   (throw (ex-info (str "expected getter method for getter " getter-key)
                                                   {:setter-key setter-key
                                                    :getter-key getter-key
                                                    :zipped zipped})))
                               _ (assert (nil? (:parameters getterMethod)))
                               _ (when (nil? setterMethod)
                                   (throw (ex-info (str "expected setter method for setter " setter-key)
                                                   {:setter-key         setter-key
                                                    :getter-key         getter-key
                                                    :setter-method-keys (sort (keys setterMethods))
                                                    :zipped             zipped})))
                               optional (if (string/includes? (get setterMethod :doc "") "ptional")
                                          true
                                          (if (string/includes? (get setterMethod :doc "") "equired")
                                            false
                                            ;; as a best guess, the smallest builder params are required
                                            (not (contains? min-param-names (:name setterMethod)))))
                               setter-argument-type (if (= 1 (count (get setterMethod :type)))
                                                      (first (get setterMethod :type))
                                                      (if-let [[native :as natives] (not-empty (filter native-type (get setterMethod :type)))]
                                                        (do
                                                          (when-not (= 1 (count natives))
                                                            (throw (ex-info "polymorphic builder method has weird native types" {:setterMethod setterMethod})))
                                                          native)
                                                        (let [ts (get setterMethod :type)]
                                                          (if (and (= 2 (count ts))
                                                                   (or (and (list-like? (nth ts 0))
                                                                            (array-like? (nth ts 1)))
                                                                       (and (list-like? (nth ts 1))
                                                                            (array-like? (nth ts 0)))))
                                                            (or (and (list-like? (nth ts 0)) (nth ts 0))
                                                                (and (list-like? (nth ts 1)) (nth ts 1)))
                                                            (throw (ex-info "polymorphic builder method has weird non-native types" {:setterMethod setterMethod}))))))
                               field-key (g/coerce keyword? (keyword (:name setterMethod)))
                               field-map (cond-> {:setterMethod       (symbol setter-key)
                                                  :getterMethod       (symbol getter-key)
                                                  :optional           optional
                                                  :getterReturnType   (get getterMethod :returnType)
                                                  :setterArgumentType setter-argument-type}

                                                 ;; TODO move doc cleaning upstream, remove newlines & normalize spacing
                                                 (and (some? (get setterMethod :doc))
                                                      (not (string/blank? (get setterMethod :doc))))
                                                 (assoc :setterDoc (get setterMethod :doc))

                                                 (and (some? (get setterMethod :doc))
                                                      (not (string/blank? (get setterMethod :doc))))
                                                 (assoc :getterDoc (get getterMethod :doc)))]
                           [field-key field-map])))
                     zipped)
        sugar-free-builders (reduce
                              (fn [acc params]
                                (let [names (set (map :name params))]
                                  (if (clojure.set/subset? names (into #{} (map name) (keys fields)))
                                    (conj acc params)
                                    acc)))
                              []
                              (get staticMethods :newBuilder))
        new-builder (when (seq sugar-free-builders)
                      (let [n (apply min (map count sugar-free-builders))]
                        (reduce (fn [_ params]
                                  (if (= n (count params))
                                    (let [params' (into [] (map #(assoc % :field (keyword (:name %)))) params)]
                                      (reduced params'))))
                                nil
                                sugar-free-builders)))
        setter-fields (apply disj (into (sorted-set) (keys fields)) (map :field new-builder))
        static-method-types (reduce
                              (fn [acc [_ arg-lists]]
                                (reduce #(into %1 (map :type) %2) acc arg-lists))
                              #{}
                              staticMethods)
        fields-types (reduce
                       (fn [acc [_ {:keys [setterArgumentType getterReturnType]}]]
                         (conj acc setterArgumentType getterReturnType))
                       #{}
                       fields)
        all-types (reduce (fn [acc item]
                            (into acc (comp
                                        (map parse-type)
                                        (remove native-type))
                                  (if (string? item) [item] item)))
                          #{}
                          (concat static-method-types fields-types))]
    {::type               :accessor
     ::key                (package-key package className)
     :className           className
     :doc                 classDoc
     :fields              fields
     :newBuilder          new-builder
     :builderSetterFields setter-fields
     :typeDependencies    all-types}))


#!--------------------------------------------------------------------------------------------------------

;(defn variant-tags [class-like]
;  (let [reflection (clojure.reflect/reflect (as-class class-like))]
;    (reduce (fn [acc {:keys [flags] :as m}]
;              (if (and (contains? flags :final)
;                       (= (name (:name m))
;                          (string/upper-case (name (:name m)))))
;                (let [fld (.getDeclaredField (as-class class-like) (name (:name m)))]
;                  (.setAccessible fld true)
;                  (conj acc (.get fld nil)))
;                acc))
;            #{}
;            (:members reflection))))

;(defn zip-variant-class-tags
;  [package variant-classes variant-tags]
;  (let [variant-schema    {:type        "STRING"
;                           :nullable    false
;                           :enum       (vec variant-tags)
;                           :description "the tag that corresponds to the class"}
;        generationConfig  {:responseMimeType "application/json"
;                           :responseSchema   {:type       "OBJECT"
;                                              :required   (vec variant-classes)
;                                              :properties (into {} (map #(vector % variant-schema)) variant-classes)}}
;        systemInstruction (str "given a list of class names, match them to a tag")
;        cfg               (assoc models/pro :systemInstruction systemInstruction
;                                            :generationConfig generationConfig)
;        validator! (fn [edn]
;                     (when-not (= (count edn) (count variant-classes))
;                       (throw (ex-info "incorrect count" {:expected (count variant-classes)
;                                                          :actual (count edn)})))
;                     (when-not (= variant-classes (set (map name (keys edn))))
;                       (throw (ex-info "incorrect classes" {:variant-classes variant-classes
;                                                            :edn-keys (set (map name (keys edn)))})))
;                     (when-not (clojure.set/subset? (set (vals edn)) variant-tags)
;                       (throw (ex-info "incorrect tags" {:variant-tags variant-tags
;                                                         :edn edn}))))
;        edn (store/generate-content-aside (:store package) cfg (vec variant-tags) validator!)]
;    (into (sorted-map) (map (fn [[k v]] [(name k) v])) edn)))

;; TODO abstract-unions (get public abstract methods from base class to zip setters)
;; TODO concrete-unions

;(defn analyze-union [package className]
;  (let [{:keys [doc]} (extract/$extract-type-detail package className)
;        variant-classes (get-in package [:types/unions className])
;        tags (variant-tags className)
;        ;zipped (zip-variant-class-tags package variant-classes tags)
;        ;classless-tags (apply disj tags (vals zipped))
;        ]
;    {::type            :union
;     ::key             (util/package-key package className)
;     :doc              doc
;     :typeDependencies variant-classes
;     :variantTags tags
;     ;:class->tag zipped
;     ;:classless-tags classless-tags
;     }))

#!--------------------------------------------------------------------------------------------------------

(defn analyze-abstract-union [package className] (throw (Exception. "analyze-abstract-union unimplemented")))

#!--------------------------------------------------------------------------------------------------------

(defn analyze-concrete-union [package className] (throw (Exception. "analyze-concrete-union unimplemented")))

#!--------------------------------------------------------------------------------------------------------

(defn analyze-enum [package className] (throw (Exception. "analyze-enum unimplemented")))

#!--------------------------------------------------------------------------------------------------------

(defn analyze [package className]
  {:post [(contains? % ::key) (contains? % ::type)]}
  (cond
    (contains? (:types/abstract-unions package) className)
    (analyze-abstract-union package className)

    (contains? (:types/concrete-unions package) className)
    (analyze-concrete-union package className)

    (contains? (:types/static-factories package) className)
    (analyze-static-factory package className)

    (contains? (:types/accessors package) className)
    (analyze-accessor package className)

    (contains? (:types/enums package) className)
    (analyze-enum package className)

    true
    (throw (Exception. (str "cannot analyze unknown type! " className)))))


