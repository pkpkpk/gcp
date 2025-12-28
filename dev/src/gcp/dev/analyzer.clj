(ns gcp.dev.analyzer
  (:require
    [clojure.set :as s]
    [clojure.string :as string]
    [gcp.dev.packages :as packages]
    [gcp.dev.util :as u]
    [taoensso.telemere :as tel]))

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

(defn- property-name [method-name]
  (cond
    (string/starts-with? method-name "get")
    (let [s (subs method-name 3)]
      (if (seq s)
        (str (string/lower-case (subs s 0 1)) (subs s 1))
        ""))
    (string/starts-with? method-name "is")
    (let [s (subs method-name 2)]
      (if (seq s)
        (str (string/lower-case (subs s 0 1)) (subs s 1))
        ""))
    (string/starts-with? method-name "set")
    (let [s (subs method-name 3)]
      (if (seq s)
        (str (string/lower-case (subs s 0 1)) (subs s 1))
        ""))
    :else method-name))

(defn- package-key [node]
  (keyword "gcp" (str (:package node) "." (:name node))))

(defn- basic-info [node]
  {:gcp/key (package-key node)
   :className (:name node)
   :package (:package node)
   :doc (:doc node)
   :nested (:nested node)})

(defn- extract-type-symbols [type-ast]
  (cond
    (symbol? type-ast) (if (#{'? 'void} type-ast) #{} #{type-ast})
    (sequential? type-ast) (reduce into #{} (map extract-type-symbols type-ast))
    :else #{}))

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

;; --- Builders & Accessors ---

(defmethod analyze-class-node :accessor-with-builder [node]
  (let [builder-node (get-nested node "Builder")
        node-getters (getters node)
        builder-setters (if builder-node 
                          (setters builder-node (:name builder-node)) 
                          [])
        
        ;; Map getters to properties
        props-by-getter (reduce (fn [acc m]
                                  (let [pname (property-name (:name m))]
                                    (assoc acc pname {:getter m})))
                                {}
                                node-getters)
        
        ;; Map setters to properties
        props (reduce (fn [acc m]
                        (let [pname (property-name (:name m))]
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
        
        type-deps (into #{} (mapcat (fn [[_ f]] (extract-type-symbols (:type f)))) fields)
        newBuilder (first (filter #(= (:name %) "newBuilder") (:methods node)))]

    (merge (basic-info node)
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

(defn analyze-class
  [pkg-like class-like]
  (let [class-node (packages/lookup-class pkg-like class-like)]
    (analyze-class-node class-node)))

(defn analyze-dependencies
  "Analyzes the dependencies of a node and returns a map separating them into
   :internal (same package/service) and :foreign (external, e.g. java.*, protobuf)."
  [node]
  (let [analyzed (analyze-class-node node)
        deps (:typeDependencies analyzed)
        package (:package node)
        ;; Heuristic: Internal if starts with same package prefix (up to service level)
        ;; e.g. com.google.cloud.vertexai
        service-pkg (if (string/starts-with? package "com.google.cloud.")
                      (let [parts (string/split package #"\.")]
                        (string/join "." (take 5 parts))) ;; com.google.cloud.service.vX
                      package)]
    (reduce (fn [acc dep]
              (let [dep-str (str dep)]
                (if (string/starts-with? dep-str service-pkg)
                  (update acc :internal conj dep)
                  (update acc :foreign conj dep))))
            {:internal #{} :foreign #{}}
            deps)))