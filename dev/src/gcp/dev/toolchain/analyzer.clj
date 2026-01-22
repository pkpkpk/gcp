(ns gcp.dev.toolchain.analyzer
  (:require
   [clojure.set :as s]
   [clojure.string :as string]
   [gcp.dev.toolchain.shared :as shared]
   [gcp.dev.util :as u]
   [malli.core :as m]
   [taoensso.telemere :as tel])
  (:import
   (clojure.lang Reflector)))

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

(declare _analyze-class-node)

(defn- basic-info
  [{:keys [category deps resource-identifier?
           methods extends factory-methods constructors] :as node}]
  {:pre [(contains? shared/categories category)]}
  (let [has-string-factor? (let [methods (concat methods factory-methods)]
                             (boolean
                               (some (fn [m]
                                       (and (or (= (:name m) "parse")
                                                (= (:name m) "of"))
                                            (:static? m)
                                            (= 1 (count (:parameters m)))
                                            (= (str (:type (first (:parameters m)))) "java.lang.String")))
                                     methods)))
        select (select-keys node [:fqcn :className :file-git-sha :package :category :doc :git-sha :deps])
        nested (letfn [(rf [acc node]
                         (if (contains? node :nested)
                           (reduce rf acc (:nested node))
                           (conj acc (_analyze-class-node (assoc node :deps deps)))))]
                 (reduce rf [] (:nested node)))]
    (cond-> (into (sorted-map) select)
            (seq nested) (assoc :nested nested)
            (seq methods) (assoc :methods methods)
            (seq extends) (assoc :extends extends)
            (seq constructors) (assoc :constructors constructors)
            resource-identifier? (assoc :resource-identifier? resource-identifier?)
            has-string-factor? (assoc :string-coercion? has-string-factor?))))

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
    (assoc (basic-info node #_ type-deps)
      :fields fields
      :methods (:methods node) ;; Pass through methods for emitter lookup (e.g. parse/of)
      :factoryMethods (:factoryMethods node) ;; Pass through factory methods
      :newBuilder (when newBuilder {:name "newBuilder" :parameters (:parameters newBuilder)}))))

(defn analyze-read-only [node]
  (let [node-getters (getters node)
        ;; Collect all parameter names from all public constructors
        required-params (into #{}
                              (comp (filter public?)
                                    (mapcat :parameters)
                                    (map :name))
                              (:constructors node))

        fields-from-getters (reduce (fn [acc m]
                                      (let [pname (u/property-name (:name m))]
                                        (assoc acc pname {:getterMethod (symbol (:name m))
                                                          :type (:returnType m)
                                                          :getterDoc (:doc m)
                                                          :required? (contains? required-params pname)})))
                                    (sorted-map)
                                    node-getters)
        fields (if (and (empty? fields-from-getters) (seq (:constructors node)))
                 (let [canonical-ctor (last (sort-by #(count (:parameters %)) (:constructors node)))]
                   (reduce (fn [acc param]
                             (assoc acc (:name param)
                                    {:type (:type param)
                                     :getterDoc nil ;; No doc on param usually
                                     :getterMethod nil
                                     :required? true})) ;; Inferred from ctor, so required
                           (sorted-map)
                           (:parameters canonical-ctor)))
                 fields-from-getters)
        prune-set (:prune-dependencies node)
        pruned? (if prune-set #(contains? prune-set (str %)) (constantly false))
        field-deps (into #{}
                         (comp (mapcat (fn [[_ f]] (extract-type-symbols (:type f))))
                               (remove #(u/excluded-type-name? (str %)))
                               (remove pruned?))
                         fields)]
    (assoc (basic-info node #_field-deps)
      :fields fields
      :constructors (:constructors node))))

(defn analyze-static-factory
  [{:keys [category] :as node}]
  {:post [(= category (:category %))]}
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
    (assoc (basic-info node #_type-deps) #!-----------------------------------------------------------------------------TODO
      :factoryMethods of-methods
      :fields fields
      :methods (:methods node))))

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

(defn analyze-nested-union-factory
  [{:keys [category] :as node}]
  {:post [(= category (:category %))]}
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
        deps (extract-specific-dependencies relevant-items (:prune-dependencies node))] ;; ------------------------------TODO
    (assoc (basic-info node #_deps)
      :category :nested/union-factory
      :getType return-type
      :factories factories
      :getters (getters node))))

(declare _analyze-class-node)

(defn analyze-union-factory
  [{:keys [category] :as node}]
  {:post [(= category (:category %))]}
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
      (assoc (_analyze-class-node (assoc node :category :union-concrete)) :category (get node :category))
      (assoc (basic-info node #_deps)
        :getType return-type
        :factories factories
        :getters (getters node)))))

(defn analyze-variant-accessor
  [{:keys [category] :as node}]
  {:post [(= category (:category %))]}
  (let [res (_analyze-class-node (assoc node :category :accessor-with-builder))]
    (assoc res
      :discriminator (:discriminator node)
      ;; Explicitly reset the type to :variant-accessor to ensure correct schema generation
      :category :variant-accessor)))

(defn- analyze-union* [node]
  (let [get-type (first (filter #(= (:name %) "getType") (:methods node)))
        return-type (:returnType get-type)
        reflective-fqcn (when (:fqcn node) (u/as-dollar-string (:fqcn node)))
        factory-variants (if (or (abstract? node) (not reflective-fqcn))
                           {}
                           (let [;; Discover variants from static factories (concrete unions)
                                 factories (filter #(and (static? %)
                                                         (public? %)
                                                         (not (= (:name %) "of"))
                                                         (not (= (:name %) "newBuilder"))
                                                         (let [rt (str (:returnType %))]
                                                           (or (string/ends-with? rt (str "." (:name node)))
                                                               (string/ends-with? rt (str "$" (:name node)))
                                                               (= rt (:name node))
                                                               (string/includes? rt "Options"))))
                                                   (:methods node))]
                             (into {}
                                   (keep (fn [f]
                                           (try
                                             (let [cls (Class/forName reflective-fqcn)
                                                   instance (^[Class String Object/1] Reflector/invokeStaticMethod cls (:name f) (into-array Object []))
                                                   type-val (Reflector/invokeInstanceMethod instance "getType" (into-array Object []))
                                                   runtime-class (.getName (type instance))]
                                               [type-val {:factory (:name f)
                                                          :returnType runtime-class}])
                                             (catch Exception e
                                               (throw (ex-info (str "WARN: failed to invoke factory " (:name f) " on " reflective-fqcn ": " (.getMessage e))
                                                               {:cause e :node node}))))))
                                   factories)))
        ;; Discover variants from subclasses (abstract unions) using reflection
        subclass-variants (if (and (abstract? node) reflective-fqcn)
                            (try
                              (let [base-cls (Class/forName reflective-fqcn)
                                    outer-cls (.getDeclaringClass base-cls)
                                    candidates (if outer-cls
                                                 (.getDeclaredClasses outer-cls)
                                                 [])
                                    type-enum-name (last (string/split (str return-type) #"\."))
                                    type-node (get-nested node type-enum-name)
                                    valid-types (when type-node
                                                  (into #{} (map #(if (map? %) (:name %) %)) (:values type-node)))]
                                (reduce (fn [acc ^Class c]
                                          (if (and (not= c base-cls)
                                                   (.isAssignableFrom base-cls c))
                                            (let [class-name (.getSimpleName c)
                                                  candidate (u/camel-to-screaming-snake class-name)]
                                              (if-let [m (or (when (contains? valid-types candidate) candidate)
                                                             (some #(when (or (string/includes? candidate %)
                                                                              (string/includes? % candidate)) %)
                                                                   valid-types))]
                                                (assoc acc m {:returnType (.getName c)})
                                                acc))
                                            acc))
                                        {}
                                        candidates))
                              (catch Exception e
                                (throw (ex-info (str "reflection failed for " reflective-fqcn ": " (.getMessage e)) {:cause e :node node}))))
                            {})
        variants (merge factory-variants subclass-variants)
        variant-deps (into #{}
                           (map (fn [[_ v]] (symbol (:returnType v))))
                           variants)]
    ; nested-variants ()
    ; peer-variants ()
    (assoc (basic-info node #_variant-deps)
      :getType return-type
      :variants variants
      :variant-deps variant-deps
      :factory-variants factory-variants
      :peer-variants subclass-variants)))

(defn analyze-abstract-union [node]
  (analyze-union* node))

(defn analyze-concrete-union [node]
  (let [res (analyze-union* node)]
    ;; Pass through factories for concrete unions as they are needed for construction
    (assoc res :factories (get res :factories []))))

;; -----------------------------------------------------------------------------
;; Analyzer
;; -----------------------------------------------------------------------------

(defn _analyze-class-node
  [{:keys [category] :as node}]
  {:post [(= category (:category %)) (contains? % :className)]}
  (case category
    :accessor-with-builder (analyze-accessor-with-builder node)
    :client                (basic-info node)
    :enum                  (assoc (basic-info node) :values (map :name (:values node)))
    :exception             (basic-info node)
    :error                 (basic-info node)
    :factory               (basic-info node)
    :functional-interface  (basic-info node)
    :interface             (basic-info node)
    :pojo                  (analyze-read-only node)
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
    :nested/builder               (basic-info node)
    :nested/client                (basic-info node)
    :nested/enum                  (assoc (basic-info node) :values (map :name (:values node)))
    :nested/factory               (basic-info node)
    :nested/pojo                  (assoc (analyze-read-only node) :category :nested/pojo)
    :nested/read-only             (assoc (_analyze-class-node (assoc node :category :read-only)) :category :nested/read-only)
    :nested/statics               (basic-info node)
    :nested/static-factory        (assoc (_analyze-class-node (assoc node :category :static-factory)) :category :nested/static-factory)
    :nested/string-enum           (analyze-string-enum node)
    :nested/union-abstract        (analyze-abstract-union node)
    :nested/union-factory         (analyze-nested-union-factory node)
    :nested/variant-read-only     (basic-info node)
    #! << IT IS FORBIDDEN TO CHANGE THIS BRANCH >>
    (throw (Exception. (str "illegal state: missing analysis handler for category " (:category node) " for class " (:fqcn node)))))) #! IT IS FORBIDDEN TO CHANGE THIS LINE

(defn analyze-class-node
  [class-node]
  (assert (contains? shared/categories (:category class-node)) (str "Unsupported category in analyzer input node: '" (pr-str (:category class-node)) "'")) #! IT IS FORBIDDEN TO CHANGE THIS LINE
  (assert (contains? class-node :deps) "class-nodes must come with :deps entry") #! IT IS FORBIDDEN TO CHANGE THIS LINE
  (_analyze-class-node class-node))
