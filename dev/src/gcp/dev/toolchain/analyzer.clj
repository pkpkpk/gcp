(ns gcp.dev.toolchain.analyzer
  (:require
    [clojure.core.match :refer [match]]
    [clojure.set :as set]
    [clojure.string :as string]
    [gcp.dev.toolchain.shared :as shared]
    [gcp.dev.util :as u]
    [malli.core :as m]
    [taoensso.telemere :as tel]))

(defn- get-nested [node name]
  (first (filter #(= (:className %) name) (:nested node))))

(defn- public? [member]
  (not (:private? member)))

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
  [{:keys [fqcn className] :as node}]
  (filter (fn [m]
            (and (not (static? m))
                 (public? m)
                 (= 1 (count (:parameters m)))
                 (let [rt (str (:returnType m))]
                   (or (= rt className)
                       (= rt fqcn)
                       (string/ends-with? rt (str "." className))))))
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

#!----------------------------------------------------------------------------------------------------------------------

(declare _analyze-class-node)

(defn- basic-info
  [{:keys [category deps] :as node}]
  {:pre [(contains? shared/categories category)]}
  (let [select (select-keys node [:fqcn :className :file-git-sha :package :category :doc :resource-identifier? :deps :gcp/key])
        nested (letfn [(rf [acc node]
                         (if (contains? node :nested)
                           (reduce rf acc (:nested node))
                           (conj acc (_analyze-class-node (assoc node :deps deps)))))]
                 (reduce rf [] (:nested node)))]
    (cond-> (into (sorted-map) select)
            (seq nested) (assoc :nested nested))))

(defn analyze-static-factory [node]
  (let [static-methods (sequence (comp (remove #(:varArgs? (last (:parameters %))))
                                       (filter :static?))
                                 (:methods node))
        factory-methods (vec (sort-by #(count (:parameters %)) < static-methods))
        _ (let [param-types (mapv #(map :type (:parameters %)) factory-methods)]
            (assert (= (count param-types) (count (into #{} param-types)))
                    (str "found ambiguous static factory methods for type " (:fqcn node) "; they need to be unique by arity count and type")))
        ;; Determine required parameters (intersection of all 'of' method parameters)
        required-params (if-some [param-names-seq (not-empty (map (fn [m] (set (map (comp keyword :name) (:parameters m)))) factory-methods))]
                          (apply clojure.set/intersection param-names-seq)
                          #{})
        getters-by-key (into (sorted-map)
                             (map
                               (fn [m]
                                 (let [pname (u/property-key (:name m))]
                                   [pname (-> m
                                              (dissoc :parameters :abstract? :static?)
                                              (assoc :required? (contains? required-params pname)))])))
                             (getters node))
        method-count  (count factory-methods)
        min-arity     (apply min (map (comp count :parameters) factory-methods))
        max-arity     (apply max (map (comp count :parameters) factory-methods))
        intersect?    (shared/intersecting-methods? factory-methods)
        strategy      (match [method-count min-arity max-arity intersect?]
                             [1 0 0 false] :single-method-no-param
                             [1 1 1 false] :single-method-single-param
                             [1 _ _ false] :map-keys
                             [_ 1 _ true]  :intersecting-with-sugar
                             [_ _ _ true]  :intersecting-no-sugar
                             [_ _ _ false] :cond)]
    (cond-> (assoc (basic-info node) :factory-methods factory-methods
                                     :strategy strategy)
            (seq getters-by-key) (assoc :getters-by-key getters-by-key))))

(defn analyze-union-abstract
  [{:keys [nested methods variant-mappings] :as node}]
  (assert (seq variant-mappings))
  (let [base                     (dissoc (basic-info node) :nested)
        methods-by-name          (into {} (map (fn [{:keys [name] :as m}] [name m])) methods)
        _                        (assert (contains? methods-by-name "getType"))
        nested-by-name           (into {} (map (fn [{:keys [className] :as c}] [className c])) nested)
        _                        (assert (contains? nested-by-name "Type"))
        discriminator-enum       (get nested-by-name "Type")
        _ (assert (some? discriminator-enum))
        discriminator-values     (into #{} (map :name)
                                       (if (= :enum (:category discriminator-enum))
                                         (get discriminator-enum :values)
                                         (filter #(and
                                                   (not (:private? %))
                                                   (= (symbol (:fqcn discriminator-enum)) (:type %)))
                                                 (get discriminator-enum :fields))))]
    (assoc base :discriminator-values discriminator-values
                :discriminator-enum discriminator-enum
                :variant-mappings variant-mappings
                :discriminator-method (get methods-by-name "getType"))))

(defn analyze-union-concrete
  [{:keys [variant-mappings methods] :as node}]
  (assert (= 1 (count (filter #(= "of" (:name %)) methods))))
  (let [base (basic-info node)
        methods-by-name          (into {} (map (fn [{:keys [name] :as m}] [name m])) methods)
        _                        (assert (contains? methods-by-name "getType") "missing getType discriminator method")
        discriminator-method (get methods-by-name "getType")
        _ (assert (empty? (:parameters discriminator-method)))
        _ (assert (= 'java.lang.String (:returnType discriminator-method)))
        discriminator-values (into #{}
                                   (comp
                                     (remove :private?)
                                     (map :value))
                                   (:fields node))
        peer-variant-values (into (sorted-set) (keys variant-mappings))
        self-variant-values (set/difference discriminator-values peer-variant-values)
        self-variant-mappings (loop [acc {} variants self-variant-values]
                                (if-let [t (first variants)]
                                  (let [method (reduce
                                                 (fn [_ {:keys [doc] :as method}]
                                                   (when (boolean (re-find (re-pattern (str "(?i)" t)) doc))
                                                     (reduced method)))
                                                 nil
                                                 (filter #(= (symbol (:fqcn node)) (:returnType %)) methods))]
                                    (assert (some? method))
                                    (recur (assoc acc t method) (disj variants t)))
                                  acc))]
    (assert (= (count discriminator-values) (+ (count variant-mappings) (count self-variant-mappings))))
    (cond->
      (assoc base :discriminator-values discriminator-values
                  :peer-variant-mappings variant-mappings
                  :self-variant-mappings self-variant-mappings
                  :discriminator-method discriminator-method)
      (= 'java.lang.String (get-in methods-by-name ["of" :parameters :type]))
      (assoc :sugar-factory (get methods-by-name "of")))))

(defn analyze-union-tagged
  [{:keys [methods nested tag-field payload-field] :as node}]
  (let [base (basic-info node)
        methods-by-name          (into {} (map (fn [{:keys [name] :as m}] [name m])) methods)
        _                        (assert (contains? methods-by-name "getType"))
        nested-by-name           (into {} (map (fn [{:keys [className] :as c}] [className c])) nested)
        _                        (assert (contains? nested-by-name "Type"))
        discriminator-enum       (get nested-by-name "Type")
        discriminator-values     (into (sorted-set) (map :name (:values discriminator-enum)))
        discriminator-method     (get methods-by-name "getType")
        _ (assert (empty? (:parameters discriminator-method)))
        _ (assert (= (symbol (:fqcn discriminator-enum)) (:returnType discriminator-method)))
        getters-by-key (into (sorted-map)
                               (map
                                 (fn [m]
                                   [(keyword (u/property-name (:name m))) (dissoc m :parameters :abstract? :static?)]))
                               (getters node))
        _ (assert (= #{(keyword tag-field) (keyword payload-field)} (set (keys getters-by-key))))
        factory-by-tag (into {}
                             (map
                               (fn [{:keys [variant] :as method}]
                                 [variant method]))
                             (filter :variant methods))]
    (assert (= (set (keys factory-by-tag)) discriminator-values))
    (assoc base
      :tag-key (keyword tag-field)
      :payload-key (keyword payload-field)
      :getters-by-key getters-by-key
      :discriminator-values discriminator-values
      :discriminator-enum discriminator-enum
      :discriminator-method discriminator-method
      :factories-by-tag factory-by-tag)))

(defn- analyze-accessor-with-builder
  [{:keys [methods] :as node}]
  (let [base (basic-info node)
        builder-node (get-nested node "Builder")
        fields-by-key (into (sorted-map)
                            (map
                              (fn [f]
                                [(keyword (:name f)) f]))
                            (:fields node))
        getters-by-key (into (sorted-map)
                             (map
                               (fn [m]
                                 (let [k (keyword (or (:field-name m) (u/property-name (:name m))))
                                       m (dissoc m :parameters :abstract? :static?)]
                                   [k m])))
                             (getters node))
        builder-setters-by-key (into (sorted-map)
                                     (map
                                       (fn [m]
                                         (let [k (keyword (u/property-name (:name m)))
                                               m (dissoc m :returnType :abstract? :static?)
                                               _(assert (= 1 (count (:parameters m))))
                                               [{param-type :type :as parameter}] (:parameters m)
                                               m (if (and (vector? param-type)
                                                          (= (first param-type) :type-parameter))
                                                   (let [actual (get-in fields-by-key [k :type])]
                                                     (if-not (or (get-in node [:deps :foreign-mappings actual])
                                                                 (get-in node [:deps :custom-mappings actual])
                                                                 (get-in node [:deps :peer-mappings actual]))
                                                       (do
                                                         (tel/log! :warn ["unresolved generic returnType for setter" (str (:className node) "." (:name m) "()")])
                                                         m)
                                                       (assoc m :parameters [(assoc parameter :type actual)])))
                                                   m)]
                                           [k m])))
                                     (setters builder-node))
        getters-by-key (reduce-kv
                         (fn [acc k {:keys [returnType name] :as m}]
                           (if (and (vector? returnType)
                                    (= (first returnType) :type-parameter))
                             (let [setter-type (get-in builder-setters-by-key [k :parameters 0 :type])
                                   field-type (get-in fields-by-key [k :type])
                                   actual (or setter-type field-type)]
                               (if-not (or (get-in node [:deps :foreign-mappings actual])
                                           (get-in node [:deps :custom-mappings actual])
                                           (get-in node [:deps :peer-mappings actual]))
                                 (do
                                   (tel/log! :warn ["unresolved generic returnType for getter" (str (:className node) "." name "()")])
                                   (assoc acc k m))
                                 (assoc acc k (assoc m :returnType actual))))
                             (assoc acc k m)))
                         (sorted-map)
                         getters-by-key)
        candidates (sequence
                     (comp
                       (remove #(:varArgs? (last (get % :parameters))))
                       (filter #(= (:name %) "newBuilder")))
                     (:methods node))
        candidate-stats (map (fn [m]
                               (let [mappings (:parameter-mappings m)
                                     param->key (fn [p-name]
                                                  (if-let [setter (get mappings p-name)]
                                                    (keyword (u/property-name setter))
                                                    (keyword (u/property-name p-name))))
                                     params (map (comp param->key :name) (:parameters m))
                                     param-set (set params)
                                     getter-keys (set (keys getters-by-key))
                                     matches (count (set/intersection param-set getter-keys))
                                     mismatches (count (set/difference param-set getter-keys))]
                                 {:method m
                                  :matches matches
                                  :mismatches mismatches
                                  :arity (count params)}))
                             candidates)
        fully-valid (filter #(zero? (:mismatches %)) candidate-stats)
        best-candidate (if (empty? fully-valid)
                         (do
                           (tel/log! :warn (str "newBuilder params does not match getter fields for type " (:fqcn node)))
                           (:method (first (sort-by (juxt (comp - :matches) :arity) candidate-stats))))
                         (if (= 1 (count fully-valid))
                           (:method (first fully-valid))
                           (if (apply = (map :arity fully-valid))
                             ;; Disambiguation Strategy:
                             ;; If multiple newBuilders have the same arity and match all keys,
                             ;; prefer the one whose parameter types match the *getter* return types.
                             ;; This resolves cases like BigQuery Field where newBuilder accepts
                             ;; either StandardSQLTypeName or LegacySQLTypeName, but .getType()
                             ;; returns LegacySQLTypeName. We want the one that matches .getType().
                             (let [getter-return-types (into {}
                                                             (map (fn [[k m]] [k (:returnType m)]))
                                                             getters-by-key)
                                   scored-candidates
                                   (map (fn [candidate]
                                          (let [method (:method candidate)
                                                score (reduce
                                                        (fn [acc param]
                                                          (let [param-name (:name param)
                                                                ;; map param name back to property key
                                                                mappings (:parameter-mappings method)
                                                                k (if-let [setter (get mappings param-name)]
                                                                    (keyword (u/property-name setter))
                                                                    (keyword (u/property-name param-name)))
                                                                getter-type (get getter-return-types k)]
                                                            (if (= (:type param) getter-type)
                                                              (inc acc)
                                                              acc)))
                                                        0
                                                        (:parameters method))]
                                            (assoc candidate :type-matches score)))
                                        fully-valid)
                                   winner (first (sort-by (comp - :type-matches) scored-candidates))]
                                (:method winner))
                              (:method (first (sort-by :arity fully-valid))))))
        ;; If absolutely no candidates (shouldn't happen if methods exist), fall back to original sort
        [newBuilder] (if best-candidate
                       [best-candidate]
                       (sort-by #(count (:parameters %)) candidates))
        _(assert (some? newBuilder))
        param->key (fn [p-name]
                     (if-let [setter (get (:parameter-mappings newBuilder) p-name)]
                       (keyword (u/property-name setter))
                       (keyword (u/property-name p-name))))
        required-keys (mapv (comp param->key :name) (:parameters newBuilder))
        required-newBuilder-params-by-key (into {}
                                                (map (fn [{:keys [name] :as parameter}]
                                                       [(param->key name) parameter]))
                                                (:parameters newBuilder))
        ;_(assert (= (into (sorted-set) (keys fields-by-key))
        ;            (set (keys getters-by-key)))
        ;         "this might be too strict, only need fields to resolve generics for getter")
        ;required-getters-by-key (into (sorted-map) (select-keys getters-by-key required-keys))
        ;optional-getters-by-key (into (sorted-map) (select-keys getters-by-key optional-keys))
        builder-setters-by-key (apply dissoc builder-setters-by-key required-keys)
        read-only-keys (set/difference (set (keys getters-by-key)) (set required-keys) (set (keys builder-setters-by-key)))
        optional-keys  (set/intersection (set (keys getters-by-key)) (set (keys builder-setters-by-key)))]
    (when (not= (set required-keys) (set/intersection (set required-keys) (set (keys getters-by-key))))
      (throw (ex-info "expected a getter for each required param" {:fqcn (:fqcn node)
                                                                   :required-keys required-keys
                                                                   :getters (set (keys getters-by-key))
                                                                   :missing (set/difference (set required-keys) (set (keys getters-by-key)))})))
    (assert (empty? (set/intersection (set required-keys) optional-keys)))
    (assert (empty? (set/intersection (set required-keys) read-only-keys)))
    (assert (empty? (set/intersection read-only-keys optional-keys)))
    ;; TODO .hasX() and .XCount() methods for getter tests
    (assoc base
      :newBuilder newBuilder
      :builder builder-node
      :getters-by-key getters-by-key
      :builder-setters-by-key builder-setters-by-key
      ;:fields-by-key fields-by-key
      ;:keys/fields   field-keys
      :keys/required required-keys
      :keys/optional (into (sorted-set) optional-keys)
      :keys/read-only (into (sorted-set) read-only-keys)
      :required-newBuilder-params-by-key required-newBuilder-params-by-key)))

(defn analyze-variant-accessor
  [node]
  (let [res (analyze-accessor-with-builder node)]
    (assoc res
      :discriminator (:discriminator node)
      :category :variant-accessor)))

(defn analyze-collection-wrapper
  [node]
  (let [base          (basic-info node)
        of-iterable   (reduce
                        (fn [_ method]
                          (when (and (= "of" (:name method))
                                     (= 'java.lang.Iterable (get-in method [:parameters 0 :type 0])))
                            (reduced method)))
                        nil
                        (filter :static? (:methods node)))
        _             (assert (some? of-iterable))
        element-type  (get-in of-iterable [:parameters 0 :type 1])]
    (cond-> (assoc base :of-iterable of-iterable
                        :element-type element-type))))

(defn analyze-read-only [node]
  (let [getters-by-key (into (sorted-map)
                               (map
                                 (fn [m]
                                   [(u/property-key (:name m)) (dissoc m :parameters :abstract? :static?)]))
                               (getters node))]
    (assoc (basic-info node) :getters-by-key getters-by-key)))

(defn analyze-pojo
  [{:keys [constructors] :as node}]
  {:pre [(seq constructors)]}
  (let [base (basic-info node)
        constructors-by-keys (into {}
                                   (map
                                     (fn [{:keys [parameters] :as ctor}]
                                       [(set (map (comp keyword :name) parameters)) ctor]))
                                   constructors)
        getters-by-key (into (sorted-map)
                             (map
                               (fn [m]
                                 (let [key (u/property-key (:name m))]
                                   (when (contains? m :field-name)
                                     [key (dissoc m :parameters :abstract? :static?)]))))
                             (getters node))
        constructor-keys (apply set/union (keys constructors-by-keys))
        required-keys    (apply set/intersection (keys constructors-by-keys))
        read-only-keys   (set/difference (set (keys getters-by-key)) constructor-keys)]
    (assert (= (count constructors) (count constructors-by-keys)))
    (cond-> (assoc base :constructors-by-keys constructors-by-keys
                        :keys/constructor constructor-keys
                        :keys/required required-keys
                        :keys/read-only read-only-keys)
            (seq getters-by-key) (assoc :getters-by-key getters-by-key))))

(defn analyze-enum [node]
  (let [base (basic-info node)
        values (if (= :string-enum (:category node))
                 (reduce
                   (fn [acc {:keys [] :as f}]
                     (if (and (static? f)
                              (public? f)
                              (= (string/upper-case (:name f)) (:name f)))
                       (conj acc {:name (:name f) :doc (:doc f)})
                       acc))
                   []
                   (:fields node))
                 (:values node))]
    (assert (every? #(and (map? %) (contains? % :doc)) values))
    (assoc base :values values)))

;; -----------------------------------------------------------------------------
;; Analyzer
;; -----------------------------------------------------------------------------

(defn _analyze-class-node
  [{:keys [category] :as node}]
  {:post [(= category (:category %)) (contains? % :className)]}
  (case category
    :accessor-with-builder (analyze-accessor-with-builder node)
    :client                (basic-info node)
    :collection-wrapper    (analyze-collection-wrapper node)
    :factory               (basic-info node)
    :functional-interface  (basic-info node)
    :interface             (basic-info node)
    :pojo                  (analyze-pojo node)
    :read-only             (analyze-read-only node)
    :resource-extended     (basic-info node)
    :sentinel              (basic-info node)
    :statics               (basic-info node)
    :static-factory        (analyze-static-factory node)
    (:enum :string-enum)   (analyze-enum node)
    :union-abstract        (analyze-union-abstract node)
    :union-concrete        (analyze-union-concrete node)
    :union-tagged          (analyze-union-tagged node)
    :variant-accessor      (analyze-variant-accessor node)
    #!-----------
    :nested/accessor-with-builder      (analyze-accessor-with-builder node)
    :nested/builder                    (basic-info node)
    :nested/client                     (basic-info node)
    :nested/collection-wrapper         (analyze-collection-wrapper node)
    (:nested/enum :nested/string-enum) (analyze-enum node)
    :nested/factory                    (basic-info node)
    :nested/pojo                       (analyze-pojo node)
    :nested/read-only                  (analyze-read-only node)
    :nested/statics                    (basic-info node)
    :nested/static-factory             (analyze-static-factory node)
    :nested/union-abstract             (analyze-union-abstract node)
    :nested/union-tagged               (analyze-union-tagged node)
    :nested/variant-read-only          (basic-info node)
    #! << IT IS FORBIDDEN TO CHANGE THIS BRANCH >>
    (throw (Exception. (str "illegal state: missing analysis handler for category " (:category node) " for class " (:fqcn node)))))) #! IT IS FORBIDDEN TO CHANGE THIS LINE

(defn analyze-class-node
  [class-node]
  (assert (contains? shared/categories (:category class-node)) (str "Unsupported category in analyzer input node: '" (pr-str (:category class-node)) "'")) #! IT IS FORBIDDEN TO CHANGE THIS LINE
  (assert (contains? class-node :deps) "class-nodes must come with :deps entry") #! IT IS FORBIDDEN TO CHANGE THIS LINE
  (_analyze-class-node class-node))
