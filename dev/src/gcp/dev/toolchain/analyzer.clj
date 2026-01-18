(ns gcp.dev.toolchain.analyzer
  (:require
    [clojure.set :as s]
    [clojure.string :as string]
    [gcp.dev.toolchain.shared :as shared]
    [gcp.dev.util :as u])
  (:import (clojure.lang Reflector)))

(defn- get-nested [node name]
  (first (filter #(= (:name %) name) (:nested node))))

(defn- public? [member]
  (some #{"public"} (:modifiers member)))

(defn- static? [member]
  (:static? member))

(defn- abstract? [member]
  (:abstract? member))

(defn- method-name [m] (:name m))

(defn- getters [node]
  (filter (fn [m]
            (and (not (static? m))
                 (public? m)
                 (empty? (:parameters m))
                 (not (#{"getClass" "toString" "hashCode" "clone" "wait" "notify" "notifyAll" "toPb"} (:name m)))
                 (not (string/starts-with? (:name m) "to")) ;; exclude conversions like toBuilder
                 (not (string/includes? (:name m) "$"))))
          (:methods node)))

(defn- setters
  [{:keys [fqcn] :as node} builder-name]
  (filter (fn [m]
            (and (not (static? m))
                 (public? m)
                 (= 1 (count (:parameters m)))
                 (let [rt (str (:returnType m))]
                   (or (= rt builder-name)
                       (= rt fqcn)
                       (string/ends-with? rt (str "." builder-name))))))
          (:methods node)))

(defn- extract-type-symbols [type-ast]
  (cond
    (and (vector? type-ast) (= :type-parameter (first type-ast)))
    #{}

    (symbol? type-ast) (if (#{'? 'void} type-ast) #{} #{type-ast})
    (sequential? type-ast) (reduce into #{} (map extract-type-symbols type-ast))
    :else #{}))

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
         ignore-extends-categories                    ;; ---------------------------------------------------------------- TODO audit?? make opt-in??
         #{:enum :string-enum :abstract-union :concrete-union :union-variant :union-factory
           :accessor-with-builder :builder :static-factory :factory :pojo :read-only
           :client}
         extends-deps (if (contains? ignore-extends-categories (:category node)) ;; ------------------------------------- TODO audit?? make opt-in??
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

(defn- basic-info
  ([node] (basic-info node nil))
  ([node specific-deps]
   (assert (contains? shared/categories (:category node)))
   (let [prune-set (:prune-dependencies node)
         deps (or specific-deps (extract-all-dependencies node prune-set))
         unresolved (into #{}
                          (filter (fn [dep]
                                    (let [s (str dep)]
                                      (and (not (u/native-type s))
                                           (not (string/starts-with? s "com.google.cloud"))
                                           (not (u/foreign-binding-exists? (u/infer-foreign-ns s)))))))
                          deps)]
     (sorted-map
       :gcp/key          (:gcp/key node)
       :fqcn             (:fqcn node)
       :className        (:name node)
       :package          (:package node)
       :category         (:category node)
       :doc              (:doc node)
       :nested           (:nested node)
       :extends          (:extends node)
       :git-sha          (:file-git-sha node)
       :typeDependencies deps
       :unresolved-deps  unresolved))))

(defn- analyze-accessor-with-builder [node]
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
        props-with-setters (reduce (fn [acc m]
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

        ;; Select simplest newBuilder (fewest params)
        all-new-builders (filter #(= (:name %) "newBuilder") (:methods node))
        newBuilder (first (sort-by #(count (:parameters %)) all-new-builders))

        ;; Identify required properties from newBuilder params
        props (reduce (fn [acc param]
                        (let [pname (:name param)]
                          (if (contains? acc pname)
                            (assoc-in acc [pname :required?] true)
                            acc)))
                      props-with-setters
                      (:parameters newBuilder))

        fields (into (sorted-map)
                     (keep (fn [[k v]]
                             (when (and (:getter v) (or (:setter v) (:required? v)))
                               (let [getter-type (:returnType (:getter v))
                                     setter-type (when (:setter v)
                                                   (:type (first (:parameters (:setter v)))))
                                     ;; Prefer setter type if getter returns a type parameter (e.g. <F extends FormatOptions> F get...)
                                     final-type (if (and setter-type
                                                         (vector? getter-type)
                                                         (= :type-parameter (first getter-type)))
                                                  setter-type
                                                  getter-type)]
                                 [k {:getterMethod (symbol (:name (:getter v)))
                                     :setterMethod (when (:setter v) (symbol (:name (:setter v))))
                                     :type final-type
                                     :getterDoc (:doc (:getter v))
                                     :setterDoc (when (:setter v) (:doc (:setter v)))
                                     :required? (true? (:required? v))}])))
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
                         fields)
        ;; Calculate deps from newBuilder params
        builder-deps (if newBuilder
                       (into #{}
                             (comp (mapcat #(extract-type-symbols (:type %)))
                                   (remove #(u/excluded-type-name? (str %)))
                                   (remove pruned?))
                             (:parameters newBuilder))
                       #{})
        type-deps (s/union field-deps builder-deps)]
    (assoc (basic-info node type-deps)
      :fields fields
      :typeDependencies type-deps
      :newBuilder (when newBuilder {:name "newBuilder" :parameters (:parameters newBuilder)}))))

(defn analyze-read-only [node]
  (let [node-getters (getters node)
        fields (reduce (fn [acc m]
                         (let [pname (u/property-name (:name m))]
                           (assoc acc pname {:getterMethod (symbol (:name m))
                                             :type (:returnType m)
                                             :getterDoc (:doc m)})))
                       (sorted-map)
                       node-getters)
        prune-set (:prune-dependencies node)
        pruned? (if prune-set #(contains? prune-set (str %)) (constantly false))
        field-deps (into #{}
                         (comp (mapcat (fn [[_ f]] (extract-type-symbols (:type f))))
                               (remove #(u/excluded-type-name? (str %)))
                               (remove pruned?))
                         fields)]
    (assoc (basic-info node field-deps)
      :fields fields
      :typeDependencies field-deps)))

(defn analyze-static-factory [node]
  ;; Look for static 'of' methods
  (let [of-methods (filter #(and (static? %) (= (:name %) "of")) (:methods node))
        ;; Determine required parameters (intersection of all 'of' method parameters)
        param-names-seq (map (fn [m] (set (map :name (:parameters m)))) of-methods)
        required-params (if (seq param-names-seq)
                          (apply clojure.set/intersection param-names-seq)
                          #{})
        factory-fields (reduce (fn [acc m]
                                 (let [params (:parameters m)]
                                   ;; Heuristic: params map to fields
                                   (assoc acc (keyword (string/join "-" (map :name params)))
                                              {:parameters params
                                               :doc (:doc m)})))
                               {}
                               of-methods)
        ;; Also extract properties from getters (like read-only)
        node-getters (getters node)
        prop-fields (reduce (fn [acc m]
                              (let [pname (u/property-name (:name m))]
                                (assoc acc pname {:getterMethod (symbol (:name m))
                                                  :type (:returnType m)
                                                  :getterDoc (:doc m)
                                                  :required? (contains? required-params pname)})))
                            (sorted-map)
                            node-getters)
        fields (merge factory-fields prop-fields)
        ;; Calculate deps
        prune-set (:prune-dependencies node)
        pruned? (if prune-set #(contains? prune-set (str %)) (constantly false))
        ;; Deps from factory params
        factory-deps (mapcat (fn [m] (mapcat #(extract-type-symbols (:type %)) (:parameters m))) of-methods)
        ;; Deps from getters
        prop-deps (mapcat (fn [[_ f]] (extract-type-symbols (:type f))) prop-fields)
        type-deps (into #{}
                        (comp (remove #(u/excluded-type-name? (str %)))
                              (remove pruned?))
                        (concat factory-deps prop-deps))]
    (assoc (basic-info node type-deps) :factoryMethods of-methods :fields fields :typeDependencies type-deps)))

(defn analyze-string-enum [node]
  (let [type-name (:name node)
        values (->> (:fields node)
                    (filter (fn [f]
                              (and (static? f)
                                   (public? f)
                                   (let [ft (str (:type f))]
                                     (or (= ft type-name)
                                         (string/ends-with? ft (str "." type-name))
                                         (string/ends-with? ft (str "$" type-name)))))))
                    (map :name))]
    (assoc (basic-info node) :values values)))

(declare _analyze-class-node)

(defn analyze-nested-union-factory [node]
  (let [get-type (first (filter #(= (:name %) "getType") (:methods node)))
        return-type (:returnType get-type)
        self-type (:name node)
        factories (filter #(and (static? %)
                                (public? %)
                                (let [rt (str (:returnType %))]
                                  (or (= rt self-type)
                                      (string/ends-with? rt (str "." self-type))
                                      (string/ends-with? rt (str "$" self-type)))))
                          (:methods node))
        relevant-items (concat [get-type] factories (getters node))
        deps (extract-specific-dependencies relevant-items (:prune-dependencies node))]
    (if get-type
      (assoc (basic-info node deps)
        :category :nested/union-factory
        :getType return-type
        :factories factories
        :getters (getters node))
      (_analyze-class-node (assoc node :category :nested/factory)))))

(defn analyze-union-factory [node]
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
    (if (and (not (abstract? node)) get-type)
      ;; It's actually a concrete-union if it has getType()
      (_analyze-class-node (assoc node :category :concrete-union)) #!<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< TODO THIS SHOULD BE DECIDED AT PARSER, NOT HERE!!
      (assoc (basic-info node deps)
        :getType return-type
        :factories factories
        :getters (getters node)))))

(defn analyze-variant-accessor [node]
  (let [res (_analyze-class-node (assoc node :category :accessor-with-builder))]
    (assoc res
      :discriminator (:discriminator node)
      ;; Explicitly reset the type to :variant-accessor to ensure correct schema generation
      :category :variant-accessor)))

(defn analyze-abstract-union [node]
  (let [get-type (first (filter #(= (:name %) "getType") (:methods node)))
        return-type (:returnType get-type)]
    (assoc (basic-info node) :getType return-type)))

(defn analyze-concrete-union [node]
  (let [get-type (first (filter #(= (:name %) "getType") (:methods node)))
        return-type (:returnType get-type)
        ;; Static factory methods like 'json()', 'csv()' etc return variant types
        factories (filter #(and (static? %)
                                (public? %)
                                (not (= (:name %) "of"))
                                (not (= (:name %) "newBuilder"))
                                (let [rt (str (:returnType %))]
                                  (or (= rt (:name node))
                                      (string/includes? rt "Options"))))
                          (:methods node))
        variants (into {}
                       (keep (fn [f]
                               (try
                                 (let [cls (Class/forName (str (:package node) "." (:name node)))
                                       instance (^[Class String Object/1] Reflector/invokeStaticMethod cls (:name f) (into-array Object []))
                                       type-val (Reflector/invokeInstanceMethod instance "getType" (into-array Object []))
                                       ;; Get the actual runtime class of the returned instance
                                       runtime-class (.getName (type instance))]
                                   [type-val {:factory (:name f)
                                              :returnType runtime-class}])
                                 (catch Exception _ nil))))
                       factories)
        ;; Add all variant return types to dependencies so emitter can find their from-edn/to-edn
        variant-deps (into #{}
                           (map (fn [[_ v]] (symbol (:returnType v))))
                           variants)]
    (assoc (basic-info node variant-deps)
      :getType return-type
      :factories factories
      :variants variants
      :typeDependencies variant-deps
      ;; Auto-discover generated FQCNs from the variants we found
      ::generated-fqcns (set (map :returnType (vals variants))))))

;; -----------------------------------------------------------------------------
;; Analyzer
;; -----------------------------------------------------------------------------

(defn _analyze-class-node
  [{:keys [category] :as node}]
  {:post [(= category (:category %))]}
  (case category
    :accessor-with-builder (analyze-accessor-with-builder node)
    :client                (basic-info node)
    :enum                  (assoc (basic-info node) :values (map :name (:values node)))
    :exception             (basic-info node)
    :error                 (basic-info node)
    :factory               (basic-info node)
    :functional-interface  (basic-info node)
    :interface             (basic-info node)
    :pojo                  (basic-info node)
    :read-only             (analyze-read-only node)
    :resource-extended     (basic-info node)
    :sentinel              (basic-info node)
    :statics               (basic-info node)
    :static-factory        (analyze-static-factory node)
    :string-enum           (analyze-string-enum node)
    :union-abstract        (analyze-abstract-union node)
    :union-concrete        (analyze-concrete-union node)
    :union-factory         (analyze-union-factory node)
    :variant-accessor      (analyze-variant-accessor node)
    #!-----------
    :nested/accessor-with-builder (assoc (analyze-accessor-with-builder node) :category :nested/accessor-with-builder)
    :nested/builder (basic-info node) ;;TODO do we need this?
    :nested/client (assoc (basic-info node) :category :nested/client)
    :nested/factory (assoc (basic-info node) :category :nested/factory)
    :nested/pojo (assoc (_analyze-class-node (assoc node :category :pojo)) :category :nested/pojo)
    :nested/read-only (assoc (_analyze-class-node (assoc node :category :read-only)) :category :nested/read-only)
    :nested/static-factory (assoc (_analyze-class-node (assoc node :category :static-factory)) :category :nested/static-factory)
    :nested/string-enum (analyze-string-enum node)
    :nested/enum (assoc (basic-info node) :values (map :name (:values node)) :category :nested/enum)
    :nested/union-abstract (assoc (_analyze-class-node (assoc node :category :abstract-union)) :category :nested/abstract-union)
    :nested/union-factory (analyze-nested-union-factory node)
    #! << IT IS FORBIDDEN TO CHANGE THIS BRANCH >>
    (throw (Exception. (str "illegal state: missing analysis handler for category " (:category node) " for class " (:fqcn node)))))) #! IT IS FORBIDDEN TO CHANGE THIS LINE

(defn analyze-class-node
  [class-node]
  (assert (contains? shared/categories (:category class-node)) (str "Unsupported category in analyzer input node: '" (pr-str (:category class-node)) "'")) #! IT IS FORBIDDEN TO CHANGE THIS LINE
  (assert (contains? class-node :foreign-mappings) "class-nodes must come with :foreign-mappings entry") #! IT IS FORBIDDEN TO CHANGE THIS LINE
  (let [ana-node (_analyze-class-node class-node)]
    (when-not (contains? shared/categories (:category ana-node))
      (throw (Exception. (str "Unsupported category in analyzer output node '" (pr-str (:category ana-node)) "'"))))
    (cond-> ana-node
            (contains? class-node :opaque-types) (assoc :opaque-types (get class-node :opaque-types))
            (contains? class-node :custom-namespace-mappings) (assoc :custom-namespace-mappings (get class-node :custom-namespace-mappings))
            true (assoc :foreign-mappings (get class-node :foreign-mappings)))))
