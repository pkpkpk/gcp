(ns gcp.dev.toolchain.analyzer
  (:require
   [clojure.set :as s]
   [clojure.string :as string]
   [gcp.dev.util :as u]))

;; -----------------------------------------------------------------------------
;; Helpers
;; -----------------------------------------------------------------------------

(defn- get-nested [node name]
  (first (filter #(= (:name %) name) (:nested node))))

(defn- public? [member]
  (some #{"public"} (:modifiers member)))

(defn- static? [member]
  (:static? member))

(defn- abstract? [member]
  (:abstract? member))

(defn- method-name [m] (:name m))

(defn- param-names [m]
  (map :name (:parameters m)))

(defn- getters [node]
  (filter (fn [m]
            (and (not (static? m))
                 (public? m)
                 (empty? (:parameters m))
                 (or (string/starts-with? (:name m) "get")
                     (string/starts-with? (:name m) "is"))
                 (not (#{"getClass"} (:name m)))))
          (:methods node)))

(defn- setters [node builder-name]
  (let [fqcn (str (:package node) "." (:name node))]
    (filter (fn [m]
              (and (not (static? m))
                   (public? m)
                   (= 1 (count (:parameters m)))
                   (let [rt (str (:returnType m))]
                     (or (= rt builder-name)
                         (= rt fqcn)
                         (string/ends-with? rt (str "." builder-name))))))
            (:methods node))))

(defn- extract-type-symbols [type-ast]
  (cond
    (and (vector? type-ast) (= :type-parameter (first type-ast)))
    #{}

    (symbol? type-ast) (if (#{'? 'void} type-ast) #{} #{type-ast})
    (sequential? type-ast) (reduce into #{} (map extract-type-symbols type-ast))
    :else #{}))

(def ^:private ignore-extends-categories
  #{:enum :string-enum :abstract-union :concrete-union :union-variant :union-factory
    :accessor-with-builder :builder :static-factory :factory :pojo :read-only
    :client})

(defn- extract-specific-dependencies
  [items prune-set]
  (let [deps (mapcat (fn [x]
                       (concat
                         (when (:type x) (extract-type-symbols (:type x)))
                         (when (:returnType x) (extract-type-symbols (:returnType x)))
                         (when (:parameters x) (mapcat #(extract-type-symbols (:type %)) (:parameters x)))))
                     items)
        pruned? (if prune-set #(contains? prune-set (str %)) (constantly false))]
    (into #{}
          (comp (remove #(u/excluded-type-name? (str %)))
                (remove pruned?))
          deps)))

(defn- extract-all-dependencies
  ([node] (extract-all-dependencies node nil))
  ([node prune-set]
   (let [fields (:fields node)
         methods (:methods node)
         ctors (:constructors node)
         extends (:extends node)
         implements (:implements node)
         nested (:nested node)
         field-deps (mapcat (fn [f] (extract-type-symbols (:type f))) fields)
         method-deps (mapcat (fn [m]
                               (concat (extract-type-symbols (:returnType m))
                                       (mapcat #(extract-type-symbols (:type %)) (:parameters m))))
                             methods)
         ctor-deps (mapcat (fn [c] (mapcat #(extract-type-symbols (:type %)) (:parameters c))) ctors)
         extends-deps (if (contains? ignore-extends-categories (:category node))
                        []
                        (mapcat extract-type-symbols extends))
         implements-deps (mapcat extract-type-symbols implements)
         nested-deps (mapcat #(extract-all-dependencies % prune-set) nested)

         candidates (concat field-deps method-deps ctor-deps extends-deps implements-deps nested-deps)
         pruned? (if prune-set #(contains? prune-set (str %)) (constantly false))]
     (into #{}
           (comp (remove #(u/excluded-type-name? (str %)))
                 (remove pruned?))
           candidates))))

(defn- package-key [node]
  (keyword "gcp" (str (:package node) "." (:name node))))

(defn- basic-info
  ([node] (basic-info node nil))
  ([node specific-deps]
   (let [prune-set (:prune-dependencies node)
         deps (or specific-deps (extract-all-dependencies node prune-set))
         unresolved (into #{}
                          (filter (fn [dep]
                                    (let [s (str dep)]
                                      (and (not (u/native-type s))
                                           (not (string/starts-with? s "com.google.cloud"))
                                           (not (u/foreign-binding-exists? (u/infer-foreign-ns s)))))))
                          deps)]
     {:gcp/key (package-key node)
      :className (:name node)
      :package (:package node)
      :doc (:doc node)
      :nested (:nested node)
      :extends (:extends node)
      :git-sha (:file-git-sha node)
      :typeDependencies deps
      :unresolved-deps unresolved})))

;; -----------------------------------------------------------------------------
;; Analyzer
;; -----------------------------------------------------------------------------

(defmulti analyze-class-node (fn [{:keys [category]}] category))

;; --- Enums ---

(defmethod analyze-class-node :enum [node]
  (merge (basic-info node)
         {:type :enum
          :values (map :name (:values node))}))

(defmethod analyze-class-node :string-enum [node]
  ;; String enums usually have static fields of the class type
  (let [type-name (:name node)
        values (->> (:fields node)
                    (filter (fn [f]
                              (and (static? f)
                                   (public? f)
                                   (= (str (:type f)) type-name))))
                    (map :name))]
    (merge (basic-info node)
           {:type :string-enum
            :values values})))

;; --- Unions ---

(defmethod analyze-class-node :abstract-union [node]
  (let [get-type (first (filter #(= (:name %) "getType") (:methods node)))
        return-type (:returnType get-type)]
    (merge (basic-info node)
           {:type :abstract-union
            :getType return-type})))

(defmethod analyze-class-node :concrete-union [node]
  (let [get-type (first (filter #(= (:name %) "getType") (:methods node)))
        return-type (:returnType get-type)]
    (merge (basic-info node)
           {:type :concrete-union
            :getType return-type})))

(defmethod analyze-class-node :union-variant [node]
  (merge (basic-info node)
         {:type :union-variant}))

(defmethod analyze-class-node :union-factory [node]
  (let [get-type (first (filter #(= (:name %) "getType") (:methods node)))
        return-type (:returnType get-type)
        self-type (:name node)
        factories (filter #(and (static? %)
                                (public? %)
                                (or (= (str (:returnType %)) self-type)
                                    (string/ends-with? (str (:returnType %)) (str "." self-type))))
                          (:methods node))
        relevant-items (concat [get-type] factories (getters node))
        deps (extract-specific-dependencies relevant-items (:prune-dependencies node))]
    (merge (basic-info node deps)
           {:type :union-factory
            :getType return-type
            :factories factories
            :getters (getters node)})))

;; --- Builders & Accessors ---

(defmethod analyze-class-node :accessor-with-builder [node]
  (let [builder-node (get-nested node "Builder")
        node-getters (getters node)
        builder-setters (if builder-node
                          (setters builder-node (:name builder-node))
                          [])
        ;; Map getters to properties
        props-by-getter (reduce (fn [acc m]
                                  (let [pname (u/property-name (:name m))]
                                    (assoc acc pname {:getter m})))
                                {}
                                node-getters)
        ;; Map setters to properties
        props (reduce (fn [acc m]
                        (let [pname (u/property-name (:name m))]
                          (if (contains? acc pname)
                            (assoc-in acc [pname :setter] m)
                            ;; Handle pluralization/list mapping (e.g. setContents vs getContentsList)
                            (let [plural-pname (str pname "List")]
                              (if (contains? acc plural-pname)
                                (assoc-in acc [plural-pname :setter] m)
                                acc)))))
                      props-by-getter
                      builder-setters)
        fields (into (sorted-map)
                     (keep (fn [[k v]]
                             (when (and (:getter v) (:setter v))
                               [k {:getterMethod (symbol (:name (:getter v)))
                                   :setterMethod (symbol (:name (:setter v)))
                                   :type (:returnType (:getter v))
                                   :doc (:doc (:getter v))}]))
                           props))
        fields (reduce-kv (fn [acc k v]
                            (if (or (and (string/ends-with? k "Bytes")
                                         (contains? fields (subs k 0 (- (count k) 5))))
                                    (and (string/ends-with? k "Value")
                                         (contains? fields (subs k 0 (- (count k) 5)))))
                              acc
                              (assoc acc k v)))
                          (sorted-map)
                          fields)
                prune-set (:prune-dependencies node)
                pruned? (if prune-set #(contains? prune-set (str %)) (constantly false))
                ;; Calculate deps from properties (fields)
                field-deps (into #{}
                             (comp (mapcat (fn [[_ f]] (extract-type-symbols (:type f))))
                                   (remove #(u/excluded-type-name? (str %)))
                                   (remove pruned?))
                             fields)        ;; Select simplest newBuilder (fewest params)
        all-new-builders (filter #(= (:name %) "newBuilder") (:methods node))
        newBuilder (first (sort-by #(count (:parameters %)) all-new-builders))
        ;; Calculate deps from newBuilder params
        builder-deps (if newBuilder
                       (into #{}
                             (comp (mapcat #(extract-type-symbols (:type %)))
                                   (remove #(u/excluded-type-name? (str %)))
                                   (remove pruned?))
                             (:parameters newBuilder))
                       #{})
        type-deps (s/union field-deps builder-deps)]

    (merge (basic-info node type-deps)
           {:type :accessor
            :fields fields
            :typeDependencies type-deps
            :newBuilder (when newBuilder {:name "newBuilder" :parameters (:parameters newBuilder)})})))

(defmethod analyze-class-node :builder [node]
  (merge (basic-info node)
         {:type :builder}))

;; --- Factories ---

(defmethod analyze-class-node :static-factory [node]
  ;; Look for static 'of' methods
  (let [of-methods (filter #(and (static? %) (= (:name %) "of")) (:methods node))
        fields (reduce (fn [acc m]
                         (let [params (:parameters m)]
                           ;; Heuristic: params map to fields
                           ;; This is tricky without more info, but let's just list the factory methods
                           (assoc acc (keyword (string/join "-" (map :name params)))
                                  {:parameters params
                                   :doc (:doc m)})))
                       {}
                       of-methods)]
    (merge (basic-info node)
           {:type :static-factory
            :factoryMethods of-methods})))

(defmethod analyze-class-node :factory [node]
  (merge (basic-info node)
         {:type :factory}))

;; --- Interfaces ---

(defmethod analyze-class-node :functional-interface [node]
  (merge (basic-info node)
         {:type :functional-interface}))

(defmethod analyze-class-node :interface [node]
  (merge (basic-info node)
         {:type :interface}))

;; --- Clients ---

(defmethod analyze-class-node :client [node]
  (merge (basic-info node)
         {:type :client}))

;; --- Others ---

(defmethod analyze-class-node :stub [node]
  (merge (basic-info node)
         {:type :stub}))

(defmethod analyze-class-node :exception [node]
  (merge (basic-info node)
         {:type :exception}))

(defmethod analyze-class-node :error [node]
  (merge (basic-info node)
         {:type :error}))

(defmethod analyze-class-node :resource-extended-class [node]
  (merge (basic-info node)
         {:type :resource-extended-class}))

(defmethod analyze-class-node :sentinel [node]
  (merge (basic-info node)
         {:type :sentinel}))

(defmethod analyze-class-node :statics [node]
  (merge (basic-info node)
         {:type :statics}))

(defmethod analyze-class-node :abstract [node]
  (merge (basic-info node)
         {:type :abstract}))

(defmethod analyze-class-node :pojo [node]
  (merge (basic-info node)
         {:type :pojo}))

(defmethod analyze-class-node :read-only [node]
  (merge (basic-info node)
         {:type :read-only}))

#! << IT IS FORBIDDEN TO CHANGE THIS BRANCH >>
(defmethod analyze-class-node :other [node] (throw (ex-info "illegal state" node))) #! IT IS FORBIDDEN TO CHANGE THIS LINE
