(ns gcp.dev.toolchain.emitter
  "Generates Clojure bindings from analyzed Java AST nodes.

   STRICTNESS WARNING:
   This emitter is designed to fail fast and explicitly when dependencies are missing.
   Agents and developers must NOT bypass dependency checks or 'fudge' environmental conditions
   to make the build pass. If a dependency is missing, throw a descriptive ex-info.
   Do not assume existence. Do not hardcode exemptions for missing systems.
   The integrity of the generated bindings depends on the strict verification of the environment."
  (:refer-clojure :exclude [alias])
  (:require
    [clojure.core.match :refer [match]]
    [clojure.math.combinatorics :as combo]
    [clojure.set :as set]
    [clojure.string :as string]
    [gcp.dev.toolchain.malli :as m]
    [gcp.dev.toolchain.shared :as shared :refer [categorize-type intersecting-methods?]]
    [gcp.dev.util :as u]
    [gcp.global :as g]
    [zprint.core :as zp]))

(defn class-sym [node]
  (let [{class-component :class} (u/split-fqcn (:fqcn node))]
    (if (= class-component (:className node))
      (symbol class-component)
      (symbol (string/replace class-component "." "$")))))

(defn var-name [node postfix]
  (let [{class-component :class} (u/split-fqcn (:fqcn node))]
    (if (= class-component (:className node))
      postfix
      (str (string/replace class-component "." "$") "-" postfix))))

(defn var-symbol [node postfix]
  (symbol (var-name node postfix)))

(defn to-edn-function-name [node]
  (symbol (str (var-name node "to-edn") "__ARGTAG__" (class-sym node))))

(defn from-edn-function-name [node]
  (symbol (str (var-name node "from-edn") "__TAG__" (class-sym node))))

(defn nested? [node]
  (= "nested" (namespace (:category node))))

(defn defn-from-edn [node body]
  `(~'defn ~(from-edn-function-name node) [~'arg]
     ~@(when-not (nested? node) `[(~'global/strict! ~(:gcp/key node) ~'arg)])
     ~body))

(defn defn-to-edn [node body]
  `(~'defn ~(to-edn-function-name node) [~'arg]
     ~@(when-not (nested? node) `[{:post [(~'global/strict! ~(:gcp/key node) ~'%)]}])
     ~body))

(def native-predicates
  '{java.lang.Boolean boolean?
    java.lang.Char    char?
    java.lang.String  string?
    java.lang.Integer int?
    java.lang.Long    int?
    java.lang.Float   float?
    java.lang.Double  double?
    java.lang.Object  object?
    java.util.UUID    uuid?
    int     int?
    long    int?
    byte    int?
    double  double?
    float   float?
    char    char?
    boolean boolean?
    void    nil?})

(defn scalar->pred [deps t]
  (if (vector? t)
    (if (= t '[java.util.Map java.lang.String java.lang.String])
      'map?
      (throw (Exception. (str "no predicate for native vector-type: " (pr-str t)))))
    (or (get native-predicates (g/coerce symbol? t))
        (throw (Exception. (str "no predicate for native type: " (pr-str t)))))))

(defn alias [target-ns]
  (symbol (last (string/split (name target-ns) #"\."))))

(defn nested-from-edn [fqcn]
  (let [{:keys [class]} (u/split-fqcn fqcn)]
    (str (string/replace class "." "$") "-from-edn")))

(defn nested-to-edn [fqcn]
  (let [{:keys [class]} (u/split-fqcn fqcn)]
    (str (string/replace class "." "$") "-to-edn")))

(defn foreign-to-edn
  [binding-ns fqcn]
  (let [alias     (alias binding-ns)
        className (last (u/dot-parts fqcn))]
    (symbol (name alias) (str className "-to-edn"))))

(defn foreign-from-edn
  [binding-ns fqcn]
  (let [alias     (alias binding-ns)
        className (last (u/dot-parts fqcn))]
    (symbol (name alias) (str className "-from-edn"))))

(defn peer-to-edn [binding-ns]
  (symbol (name (alias binding-ns)) "to-edn"))

(defn peer-from-edn [binding-ns]
  (symbol (name (alias binding-ns)) "from-edn"))

(defn custom-to-edn [binding-ns _fqcn]
  (symbol (name (alias binding-ns)) "to-edn"))

(defn custom-from-edn [binding-ns _fqcn]
  (symbol (name (alias binding-ns)) "from-edn"))

(defn invoke-custom-from-edn
  ([binding-ns fqcn]
   (list (custom-from-edn binding-ns fqcn)))
  ([binding-ns fqcn arg]
   (list (custom-from-edn binding-ns fqcn) arg)))

(defn invoke-custom-to-edn
  ([binding-ns fqcn]
   (list (custom-to-edn binding-ns fqcn)))
  ([binding-ns fqcn arg]
   (list (custom-to-edn binding-ns fqcn) arg)))

(defn invoke-peer-from-edn
  ([binding-ns]
   (list (peer-from-edn binding-ns)))
  ([binding-ns arg]
   (list (peer-from-edn binding-ns) arg)))

(defn invoke-peer-to-edn
  ([binding-ns]
   (list (peer-to-edn binding-ns)))
  ([binding-ns arg]
   (list (peer-to-edn binding-ns) arg)))

(defn invoke-foreign-from-edn
  ([binding-ns fqcn]
   (list (foreign-from-edn binding-ns fqcn)))
  ([binding-ns fqcn arg]
   (list (foreign-from-edn binding-ns fqcn) arg)))

(defn invoke-foreign-to-edn
  ([binding-ns fqcn]
   (list (foreign-to-edn binding-ns fqcn)))
  ([binding-ns fqcn arg]
   (list (foreign-to-edn binding-ns fqcn) arg)))

(defn invoke-nested-from-edn
  ([fqcn]
   (list (symbol (nested-from-edn fqcn))))
  ([fqcn arg]
   (list (symbol (nested-from-edn fqcn)) arg)))

(defn invoke-nested-to-edn
  ([fqcn]
   (list (symbol (nested-to-edn fqcn))))
  ([fqcn arg]
   (list (symbol (nested-to-edn fqcn)) arg)))

(defn static-invoke
  ([className method-name]
   (list (symbol (str (symbol className) "/" method-name))))
  ([className method-name arg]
   (list (symbol (str (symbol className) "/" method-name)) arg)))

(defn resolve-from-edn-var
  [deps type-category parameter-type]
  (assert (simple-keyword? type-category))
  (assert (symbol? parameter-type))
  (case type-category
    :self    'from-edn
    :peer    (peer-from-edn (get-in deps [:peer-mappings parameter-type]))
    :foreign (foreign-from-edn (get (:foreign-mappings deps) parameter-type) parameter-type)
    :nested  (nested-from-edn parameter-type)
    :custom  (custom-from-edn (get (:custom-mappings deps) parameter-type) parameter-type)))

(defn convert-param-type-from-edn [deps parameter-type arg]
  (let [type-category (categorize-type deps parameter-type)]
    (match type-category
      :self '(from-edn arg)
      (:or :scalar :generic
           [(:or :list :array :set :iterator :optional :iterable) (:or :generic :scalar)]) arg
      :custom (let [binding-ns (get (:custom-mappings deps) parameter-type)]
                (invoke-custom-from-edn binding-ns parameter-type arg))
      :foreign (let [binding-ns (get (:foreign-mappings deps) parameter-type)]
                 (invoke-foreign-from-edn binding-ns parameter-type arg))
      :peer (let [binding-ns (get (:peer-mappings deps) parameter-type)]
              (invoke-peer-from-edn binding-ns arg))
      :nested (invoke-nested-from-edn parameter-type arg)

      [:peer :generic] (let [binding-ns (get (:peer-mappings deps) (first parameter-type))]
                         (invoke-peer-from-edn binding-ns arg))

      [:foreign :generic] (let [binding-ns (get (:foreign-mappings deps) (first parameter-type))]
                            (invoke-foreign-from-edn binding-ns (first parameter-type) arg))

      [:collection-wrapper
       [(:or :array :list :set :iterable)
        (:or :peer :nested :foreign :custom :self)]]
      (let [[_ element-type] (get-in deps [:collection-wrappers parameter-type])
            [_ [coll-key element-category]] type-category
            from-edn (resolve-from-edn-var deps element-category element-type)
            ls       (list 'map from-edn arg)]
        (if (= :array coll-key)
          (list 'into-array element-type ls)
          ls))

      [(:or :array :list :set :iterable)
       (:or :peer :nested :foreign :custom :self)] (let [[_ element-type] parameter-type
                                                         [_ element-category] type-category
                                                         from-edn (resolve-from-edn-var deps element-category element-type)
                                                         ls (list 'map from-edn arg)]
                                                     (if (= :array (first type-category))
                                                       (list 'into-array element-type ls)
                                                       ls))

      [:map :scalar
       (:or :scalar :generic)] (let [K (second parameter-type)]
                                 (if (= K 'java.lang.String)
                                   `(~'into {} (~'map (~'fn [[~'k ~'v]] [(~'name ~'k) ~'v])) ~arg)
                                   arg))

      [:map :scalar (:or :peer :foreign :custom)] (let [[_ key-type element-type] parameter-type
                                                           [_ _ element-category] type-category
                                                           from-edn (resolve-from-edn-var deps element-category element-type)
                                                           k  (if (= key-type 'java.lang.String)
                                                                (list 'name 'k)
                                                                'k)]
                                                       `(~'into {} (~'map (~'fn [[~'k ~'v]] [~k ~(list from-edn 'v)])) ~arg))

      [:map :scalar
       [(:or :array :list :set)
        (:or :peer :foreign :nested :custom)]] (let [[_ key-type [_ element-type]] parameter-type
                                                     [_ _ [_ element-category]] type-category
                                                     from-edn (resolve-from-edn-var deps element-category element-type)
                                                     ls (list 'map from-edn 'v)
                                                     k  (if (= key-type 'java.lang.String)
                                                          (list 'name 'k)
                                                          'k)]
                                                 `(~'into {} (~'map (~'fn [[~'k ~'v]] [~k ~ls])) ~arg))

      [:optional (:or :peer :foreign :nested :custom)] (let [[_ element-type] parameter-type
                                                             [_ element-category] type-category
                                                             from-edn (resolve-from-edn-var deps element-category element-type)]
                                                         `(~'when ~arg
                                                            (~'java.util.Optional/of (~from-edn ~arg))))

      [:optional (:or :scalar :generic)] `(~'java.util.Optional/ofNullable ~arg)

      :else (throw (ex-info (str "could not resolve from-edn type-category: " type-category)
                            {:type-category type-category
                             :deps deps
                             :parameter-type parameter-type
                             :arg arg})))))

(defn param-predicate-test [deps param-type arg]
  (match (categorize-type deps param-type)
    (:or :scalar [:map :scalar :scalar]) (list (scalar->pred deps param-type) arg)
    [:iterable :peer] `(g/valid? [:sequential ~(u/fqcn->schema-key (second param-type))] ~arg)
    :foreign `(g/valid? ~(u/fqcn->schema-key param-type true) ~arg)
    :peer    `(g/valid? ~(u/fqcn->schema-key param-type) ~arg)
    :nested  `(g/valid? ~(u/fqcn->schema-key param-type) ~arg)
    (throw (Exception. (str "could not resolve type predicate: " (pr-str param-type))))))

(defn invoke-static-method-via-map-keys-from-edn
  [deps class-sym {method-name :name :keys [parameters]}]
  `(~(symbol (name class-sym) method-name)
     ~@(map
         (fn [param] (convert-param-type-from-edn deps (:type param) (list 'get 'arg (keyword (:name param)))))
         parameters)))

(defn invoke-instance-method-from-edn
  [deps {method-name :name :keys [parameters]} instance]
  `(~(symbol (str "." method-name)) ~instance
     ~@(map
         (fn [param] (convert-param-type-from-edn deps (:type param) (list 'get 'arg (keyword (:name param)))))
         parameters)))

(defn invoke-method
  ([{:keys [name]} instance & args]
   `(~(symbol (str "." name)) ~instance ~@args)))

(defn resolve-to-edn-var
  ([deps parameter-type]
   (resolve-to-edn-var deps (categorize-type deps parameter-type) parameter-type))
  ([deps type-category parameter-type]
   (assert (simple-keyword? type-category))
   (assert (symbol? parameter-type))
   (case type-category
     :self 'to-edn
     :peer (peer-to-edn (get-in deps [:peer-mappings parameter-type]))
     :foreign (foreign-to-edn (get (:foreign-mappings deps) parameter-type) parameter-type)
     :nested (nested-to-edn parameter-type)
     :custom (custom-to-edn (get (:custom-mappings deps) parameter-type) parameter-type))))

;; TODO this needs to call enum.name() for native-enums + goog-string-enums
(defn emit-invoke-getter-to-edn
  [deps {:keys [name returnType synthetic? field-name] :as method} instance]
  (let [extraction (if synthetic?
                     (list 'global/get-private-field instance field-name)
                     (list (symbol (str "." name)) instance))
        type-category (categorize-type deps returnType)]
    (match type-category
      (:or
        :scalar
        :generic
        [(:or :list :array :set :iterable) (:or :generic :scalar)]) extraction

      :foreign (let [binding-ns (get (:foreign-mappings deps) returnType)]
                 (invoke-foreign-to-edn binding-ns returnType extraction))
      :peer (let [binding-ns (get (:peer-mappings deps) returnType)]
              (invoke-peer-to-edn binding-ns extraction))
      :custom (let [binding-ns (get (:custom-mappings deps) returnType)]
               (invoke-custom-to-edn binding-ns returnType extraction))
      :nested (invoke-nested-to-edn returnType extraction)

      [:foreign :generic] (let [binding-ns (get (:foreign-mappings deps) (first returnType))]
                             (invoke-foreign-to-edn binding-ns (first returnType) extraction))

      [(:or :array :list :set :iterable)
       (:or :custom :peer :nested :foreign :self)] (let [[_ element-type] returnType
                                                         [_ element-category] type-category
                                                         to-edn (resolve-to-edn-var deps element-category element-type)
                                                         ls     (list 'map to-edn extraction)]
                                                     (if (= :set (first type-category))
                                                       (list 'into #{} ls)
                                                       ls))

      [:map :scalar (:or :scalar :generic)] (if (= (second returnType) 'java.lang.String)
                                                            `(~'into {} (~'map (~'fn [[~'k ~'v]] [(~'keyword ~'k) ~'v])) ~extraction)
                                                            ~extraction)

      [:map :scalar
       (:or :custom :peer :foreign :nested)] (let [[_ key-type element-type] returnType
                                                   [_ _ element-category] type-category
                                                   to-edn (resolve-to-edn-var deps element-category element-type)
                                                   k      (if (= key-type 'java.lang.String)
                                                            (list 'keyword 'k)
                                                            'k)]
                                                `(~'into {} (~'map (~'fn [[~'k ~'v]] [~k (~to-edn ~'v)])) ~extraction))

      [:map :scalar
       [(:or :array :list :set)
        (:or :custom :peer :foreign :nested)]] (let [[_ key-type [_ element-type]] returnType
                                                     [_ _ [_ element-category]] type-category
                                                     to-edn (resolve-to-edn-var deps element-category element-type)
                                                     ls     (if (= :set (get-in type-category [2 0]))
                                                              (list 'into #{} (list 'map to-edn 'v))
                                                              (list 'map to-edn 'v))
                                                     k      (if (= key-type 'java.lang.String)
                                                              (list 'keyword 'k)
                                                              'k)]
                                                  `(~'into {} (~'map (~'fn [[~'k ~'v]] [~k ~ls])) ~extraction))

      [:optional
       (:or :custom :peer :foreign :nested)] (let [[_ element-type] returnType
                                                   [_ element-category] type-category
                                                   to-edn (resolve-to-edn-var deps element-category element-type)]
                                               `(~'when (~'.isPresent ~extraction)
                                                  (~to-edn (~'.get ~extraction))))

      [:optional (:or :scalar :generic)] `(~'when (~'.isPresent ~extraction) (~'.get ~extraction))

      [:optional                                            ;;TODO the map body could be recursive
       [:map :scalar (:or :scalar :generic)]] (let [K (first (second returnType))]
                                                `(~'when (~'.isPresent ~extraction)
                                                   ~(if (= K 'java.lang.String)
                                                      `(~'into {} (~'map (~'fn [[~'k ~'v]] [(~'keyword ~'k) ~'v])) (~'.get ~extraction))
                                                      (list '.get extraction))))

      [:iterator (:or :scalar :generic)] (list 'iterator-seq extraction)

      [:iterator
       (:or :custom :peer :foreign :nested)] (let [[_ element-type] returnType
                                                   [_ element-category] type-category
                                                   to-edn (resolve-to-edn-var deps element-category element-type)]
                                               (list 'map to-edn (list 'iterator-seq extraction)))

      [:collection-wrapper
       [(:or :array :list :set :iterable)
        (:or :custom :peer :nested :foreign :self)]]
      (let [[_ element-type] (get-in deps [:collection-wrappers returnType])
            [_ [coll-key element-category]] type-category
            to-edn (resolve-to-edn-var deps element-category element-type)
            ls     (list 'map to-edn extraction)]
        (if (= :set coll-key)
          (list 'into #{} ls)
          ls))

      ['com.google.api.gax.rpc.BidiStream :peer :peer] ::TODO
      ['com.google.cloud.RestorableState :self] ::TODO
      ['com.google.cloud.RestorableState :peer] ::TODO
      ['java.util.concurrent.CompletableFuture :scalar] ::TODO
      ['java.util.concurrent.CompletableFuture [:list :generic]] ::TODO
      ['java.util.concurrent.CompletableFuture [:optional :peer]] ::TODO
      :else (throw (ex-info (str "could not resolve to-edn returnType category " type-category)
                            {:deps deps
                             :type-category type-category
                             :returnType returnType})))))

#!----------------------------------------------------------------------------------------------------------------------
#! :static-factory

(defn static-factory-from-edn-body:single-method-single-param
  [{:keys [deps] [{[parameter] :parameters :as method}] :factory-methods} class-sym]
  (let [from-edn (partial convert-param-type-from-edn deps (:type parameter))]
    (list 'if (param-predicate-test deps (:type parameter) 'arg)
          (static-invoke class-sym (:name method) (from-edn 'arg))
          (static-invoke class-sym (:name method) (from-edn (list 'get 'arg (keyword (:name parameter))))))))

(defn _static-factory-single-param-method-from-edn-cond-branches
  [{:keys [deps]} {[parameter] :parameters method-name :name} class-sym]
  (let [from-edn (partial convert-param-type-from-edn deps (:type parameter))]
    [(param-predicate-test deps (:type parameter) 'arg)
     (static-invoke class-sym method-name (from-edn 'arg))
     (list 'get 'arg (keyword (:name parameter)))
     (static-invoke class-sym method-name (from-edn (list 'get 'arg (keyword (:name parameter)))))]))

(defn- static-factory-from-edn-body:intersecting-params
  ([{:keys [factory-methods] :as node} class-sym]
   (let [min-arity (apply min (map (comp count :parameters) factory-methods))]
     (static-factory-from-edn-body:intersecting-params node class-sym (= 1 min-arity))))
  ([{:keys [deps factory-methods] :as node} class-sym sugar?]
   (let [all-params      (map (fn [m] (set (map (comp keyword :name) (:parameters m)))) factory-methods)
         required-params (if (seq all-params)
                           (apply clojure.set/intersection all-params)
                           #{})
         ;optional-params (clojure.set/difference (reduce into all-params) required-params)
         ;; factory-methods is sorted least-params first
         ;; because params intersect we want to test optional/most first
         body            (if (= 2 (count factory-methods))
                           (let [[largest smallest] (reverse factory-methods)
                                 [discriminator] (remove #(contains? required-params (:name %)) (:parameters largest))
                                 kw (keyword (:name discriminator))]
                             (list 'if (list 'get 'arg kw)
                                   (invoke-static-method-via-map-keys-from-edn deps class-sym largest)
                                   (invoke-static-method-via-map-keys-from-edn deps class-sym smallest)))
                           (throw (Exception. "intersections larger than 2 not supported at this time")))]
     (if sugar?
       (let [{[{param-type :type}] :parameters method-name :name} (first factory-methods)
             arg-conversion (convert-param-type-from-edn deps param-type 'arg)]
         (list 'if
               (param-predicate-test deps param-type 'arg)
               (static-invoke class-sym method-name arg-conversion)
               body))
       body))))

(defn- static-factory-from-edn-body:cond
  [{:keys [deps factory-methods gcp/key] :as node} class-sym]
  ;; a typical pattern is several unique .a(A), .b(B) with intersecting .of(x) .of(x,y) .of(x,y,z)
  (let [[intersected] (sort-by count > (sequence (comp (remove empty?) (filter intersecting-methods?)) (combo/subsets factory-methods)))
        other (remove (set intersected) factory-methods)
        _ (assert (= (+ (count other) (count intersected)) (count factory-methods)))
        ;; more specific intersectional conditionals must come before less specific conditionals
        methods (into (vec other) (sort-by (comp count :parameters) > intersected))
        branch-rf (fn [acc {:keys [parameters] :as method}]
                    (cond
                      (empty? parameters)
                      (conj acc '(empty? arg)
                                (list (symbol (str class-sym "/" (:name method)))))

                      (= 1 (count parameters))
                      (into acc (_static-factory-single-param-method-from-edn-cond-branches node method class-sym))

                      :else
                      (let [condition `(~'and ~@(map #(list 'get 'arg (keyword (:name %))) parameters))
                            call (invoke-static-method-via-map-keys-from-edn deps class-sym method)]
                        (conj acc condition call))))
        failure (list 'ex-info "failed to match edn for static-factory cond body" {:arg 'arg :key key})]
    `(~'cond ~@(conj (reduce branch-rf [] methods) true failure))))

(defn- emit-static-factory-from-edn
  [{:keys [deps factory-methods strategy] :as node}]
  (let [class-sym (class-sym node)
        body      (case strategy
                    :single-method-no-param (static-invoke class-sym (get-in factory-methods [0 :name]))
                    :single-method-single-param (static-factory-from-edn-body:single-method-single-param node class-sym)
                    :map-keys (invoke-static-method-via-map-keys-from-edn deps class-sym (first factory-methods))
                    :intersecting-with-sugar (static-factory-from-edn-body:intersecting-params node class-sym true)
                    :intersecting-no-sugar (static-factory-from-edn-body:intersecting-params node class-sym false)
                    :cond (static-factory-from-edn-body:cond node class-sym))]
    (defn-from-edn node body)))

(defn- emit-static-factory-to-edn
  [{:keys [deps getters-by-key] :as node}]
  (if (empty? getters-by-key)
    `(~'defn ~(to-edn-function-name node) [~'arg]
       ~(list 'throw (list 'Exception. (str "to-edn is not supported for type " (:fqcn node)))))
    (let [[required-fields optional-fields] (reduce (fn [[req opt] [k v]]
                                                      (if (:required? v)
                                                        [(conj req [k v]) opt]
                                                        [req (conj opt [k v])]))
                                                    [[] []]
                                                    getters-by-key)
          base-map  (into {}
                          (for [[field-key getter] required-fields]
                            [field-key (emit-invoke-getter-to-edn deps getter 'arg)]))
          opt-steps (for [[field-key getter] optional-fields]
                      [(invoke-method getter 'arg) (list 'assoc field-key (emit-invoke-getter-to-edn deps getter 'arg))])]
      (defn-to-edn node
        (if (empty? optional-fields)
          base-map
          `(~'cond-> ~base-map
             ~@(mapcat identity opt-steps)))))))

#!----------------------------------------------------------------------------------------------------------------------
#! :union-abstract

(defn emit-union-abstract-from-edn
  [{:keys [variant-mappings deps] :as node}]
  (let [branches  (mapcat
                    (fn [[t fqcn]]
                      [t (invoke-peer-from-edn (get-in deps [:peer-mappings fqcn]) 'arg)])
                    variant-mappings)]
    (defn-from-edn node `(~'case (~'get ~'arg :type) ~@branches))))

(defn emit-union-abstract-to-edn
  [{:keys [discriminator-method deps variant-mappings] :as node}]
  (let [branches (mapcat
                   (fn [[t fqcn]]
                     [t (invoke-peer-to-edn (get-in deps [:peer-mappings fqcn]) 'arg)])
                   variant-mappings)]
    (defn-to-edn node `(~'case (~'.name ~(invoke-method discriminator-method 'arg)) ~@branches))))

#!----------------------------------------------------------------------------------------------------------------------
#! :union-concrete

(defn emit-union-concrete-from-edn
  [{:keys [peer-variant-mappings self-variant-mappings sugar-factory] :as node}]
  (let [class-sym (class-sym node)
        self-branches (mapcat
                        (fn [[t method]]
                          (assert (empty? (:parameters method)))
                          [t (static-invoke class-sym (:name method))])
                        self-variant-mappings)
        peer-branches (mapcat
                        (fn [[t peer]]
                          [t (invoke-peer-to-edn peer 'arg)])
                        peer-variant-mappings)
        case-body `(~'case (~'get ~'arg :type)
                     ~@(concat self-branches peer-branches))]
    (defn-from-edn node
                   (if sugar-factory
                     `(~'if (~'string? ~'arg)
                        ~(static-invoke class-sym (:name sugar-factory) 'arg)
                        ~case-body)
                     case-body))))

(defn emit-union-concrete-to-edn
  [{:keys [discriminator-method peer-variant-mappings self-variant-mappings] :as node}]
  (let [self-branches (mapcat
                        (fn [[t _]]
                          [t {:type t}])
                        self-variant-mappings)
        peer-branches (mapcat
                        (fn [[t peer]]
                          [t (invoke-peer-to-edn peer 'arg)])
                        peer-variant-mappings)]
    (defn-to-edn node
                 `(~'case ~(invoke-method discriminator-method 'arg)
                   ~@(concat self-branches peer-branches)))))

#!----------------------------------------------------------------------------------------------------------------------
#! :union-tagged

(defn emit-union-tagged-from-edn
  [{:keys [tag-key payload-key factories-by-tag] :as node}]
  (let [class-sym (class-sym node)
        branches (mapcat (fn [[tag {:keys [name] :as method}]]
                           [tag (static-invoke class-sym name (list 'get 'arg payload-key))])
                         factories-by-tag)]
    (defn-from-edn node `(~'case (~'get ~'arg ~tag-key) ~@branches))))

(defn emit-union-tagged-to-edn
  [{:keys [tag-key payload-key getters-by-key deps] :as node}]
  (let [tag-extract (let [method (get getters-by-key tag-key)]
                      (if (= 'java.lang.String (:returnType method))
                        (invoke-method method 'arg)
                        (list '.name (invoke-method method 'arg))))]
    (defn-to-edn node
                 {tag-key tag-extract
                  payload-key (emit-invoke-getter-to-edn deps (get getters-by-key payload-key) 'arg)})))

#!----------------------------------------------------------------------------------------------------------------------
#! :accessor-with-builder

(defn emit-accessor-with-builder-from-edn
  [{:keys [deps newBuilder] :as node}]
  (let [class-sym (class-sym node)
        body `(~'let [~'builder ~(invoke-static-method-via-map-keys-from-edn deps class-sym newBuilder)]
                ~@(map
                   (fn [[k method]]
                     (list 'when (list 'get 'arg k)
                           (invoke-instance-method-from-edn deps method 'builder)))
                   (:builder-setters-by-key node))
                (~'.build ~'builder))]
    (defn-from-edn node body)))

(defn emit-accessor-with-builder-to-edn
  [{:keys [getters-by-key deps] :as node}]
  (let [base (into (sorted-map)
                   (map
                     (fn [[key method]]
                       [key (emit-invoke-getter-to-edn deps method 'arg)]))
                   (select-keys getters-by-key (:keys/required node)))
        base (if (= :variant-accessor (:category node))
               (assoc base :type '(.name (.getType arg)))
               base)
        branches (mapcat
                   (fn [[key method]]
                     (let [test   (invoke-method method 'arg)
                           to-edn (emit-invoke-getter-to-edn deps method 'arg)]
                       [test (list 'assoc key to-edn)]))
                   (cond-> (apply dissoc getters-by-key (:keys/required node))
                           (= :variant-accessor (:category node)) (dissoc :type)))
        body `(~'cond-> ~base ~@branches)]
    (defn-to-edn node body)))

#!----------------------------------------------------------------------------------------------------------------------
#! :collection-wrapper

(defn emit-collection-wrapper-from-edn
  [{:keys [of-iterable deps] :as node}]
  (let [class-sym (class-sym node)
        {method-name :name [parameter] :parameters} of-iterable
        from-edn (convert-param-type-from-edn deps (:type parameter) 'arg)
        body (static-invoke class-sym method-name from-edn)]
    (defn-from-edn node body)))

(defn emit-collection-wrapper-to-edn
  [{:keys [deps element-type unwrap] :as node}]
  (let [body (if unwrap
               (emit-invoke-getter-to-edn deps unwrap 'arg)
               (let [to-edn (resolve-to-edn-var deps element-type)]
                 (list 'map to-edn 'arg)))]
    (defn-to-edn node body)))

#!----------------------------------------------------------------------------------------------------------------------
#! :enum

(defn emit-enum-from-edn [node]
  (defn-from-edn node (list (symbol (str (class-sym node) "/valueOf")) 'arg)))

(defn emit-enum-to-edn
  [node]
  (defn-to-edn node (list '.name 'arg)))

#!----------------------------------------------------------------------------------------------------------------------
#! :read-only

(defn- emit-read-only-from-edn [node]
  `(~'defn ~(from-edn-function-name node) [~'arg]
     (throw (~'Exception. ~(str "Class " (:fqcn node) " is read-only")))))

(defn- emit-read-only-to-edn
  [{:keys [deps getters-by-key] :as node}]
  (defn-to-edn node
               `(~'cond-> {}
                 ~@(mapcat
                     (fn [[k getter]]
                       [(invoke-method getter 'arg) (list 'assoc k (emit-invoke-getter-to-edn deps getter 'arg))])
                     getters-by-key))))

#!----------------------------------------------------------------------------------------------------------------------
#! :pojo

(defn emit-pojo-from-edn
  [{:keys [constructors-by-keys deps] :as node}]
  (let [class-sym (class-sym node)
        sorted-ctors (sort-by (comp count key) > constructors-by-keys)
        branches (mapcat
                   (fn [[keyset {:keys [parameters]}]]
                     (let [condition (if (empty? keyset)
                                       true
                                       (if (= 1 (count keyset))
                                         `(~'contains? ~'arg ~(first keyset))
                                         `(~'and ~@(map (fn [k] `(~'contains? ~'arg ~k)) keyset))))
                           args (map (fn [{:keys [name type]}]
                                       (convert-param-type-from-edn deps type `(~'get ~'arg ~(keyword name))))
                                     parameters)
                           body `(~'new ~class-sym ~@args)]
                       [condition body]))
                   sorted-ctors)
        has-fallback? (true? (first (take-last 2 branches)))
        final-branches (if has-fallback?
                         branches
                         (concat branches [:else `(~'throw (~'ex-info ~(str "No matching constructor found for " (:fqcn node))
                                                             {:arg ~'arg}))]))]
    (defn-from-edn node `(~'cond ~@final-branches))))

(defn emit-pojo-to-edn
  [{:keys [getters-by-key deps keys/required] :as node}]
  (let [base-map  (into {}
                        (map
                          (fn [[field-key getter]]
                            [field-key (emit-invoke-getter-to-edn deps getter 'arg)]))
                        (select-keys getters-by-key required))
        body (if-some [optional-keys (not-empty (apply dissoc getters-by-key required))]
               `(~'cond-> ~base-map
                  ~@(mapcat
                      (fn [[k getter]]
                        [(invoke-method getter 'arg) (list 'assoc k (emit-invoke-getter-to-edn deps getter 'arg))])
                      optional-keys))
               base-map)]
    (defn-to-edn node body)))

#!----------------------------------------------------------------------------------------------------------------------

; (defn- emit-resource-delegation [node deps class-name]
;  (let [super-dep (first deps)
;        super-alias (:alias super-dep)
;        class-sym (symbol class-name)]
;    (when-not super-alias
;      (throw (ex-info "Resource class must have a superclass dependency" {:node node :deps deps})))
;    [`(~'defn ~'from-edn {:tag ~class-sym} [~'arg]
;        (~(symbol (str super-alias "/from-edn")) ~'arg))
;     `(~'defn ~'to-edn [~(with-meta 'arg {:tag class-sym})]
;        (~(symbol (str super-alias "/to-edn")) ~'arg))]))

#!----------------------------------------------------------------------------------------------------------------------

(defn- emit-schema [node]
  `(~'def ~(var-symbol node "schema") ~(m/->schema node)))

(defn- collect-registry-entries
  [{:keys [category nested] :as node}]
  (let [entries (if (contains? #{:builder :nested/builder} category)
                  []
                  [[(:gcp/key node) (symbol (var-name node "schema"))]])
        nested-entries (mapcat collect-registry-entries nested)]
    (concat entries nested-entries)))

(defn emit-class-bindings
  [{:keys [category] :as node}]
  (case category
    :nested/builder
    nil

    :collection-wrapper
    [(emit-collection-wrapper-from-edn node)
     (emit-collection-wrapper-to-edn node)]

    (:accessor-with-builder :variant-accessor :nested/accessor-with-builder)
    [(emit-accessor-with-builder-from-edn node)
     (emit-accessor-with-builder-to-edn node)]

    (:enum :string-enum :nested/enum :nested/string-enum)   ;; TODO inline nested
    [(emit-enum-from-edn node)
     (emit-enum-to-edn node)]

    (:union-tagged :nested/union-tagged)
    [(emit-union-tagged-from-edn node)
     (emit-union-tagged-to-edn node)]

    (:static-factory :nested/static-factory)
    [(emit-static-factory-from-edn node)
     (emit-static-factory-to-edn node)]

    (:union-abstract :nested/union-abstract)
    [(emit-union-abstract-from-edn node)
     (emit-union-abstract-to-edn node)]

    (:union-concrete :nested/union-concrete)
    [(emit-union-concrete-from-edn node)
     (emit-union-concrete-to-edn node)]

    (:read-only :nested/read-only)
    [(emit-read-only-from-edn node)
     (emit-read-only-to-edn node)]

    (:pojo :nested/pojo)
    [(emit-pojo-from-edn node)
     (emit-pojo-to-edn   node)]
    (throw (Exception. (str "bindings unimplemented for category " category)))))

(defn- emit-all-nested-forms
  ([node]
   (emit-all-nested-forms node []))
  ([node acc]
   (reduce
     (fn [acc nested-node]
       (-> (emit-all-nested-forms nested-node acc)
           (into (emit-class-bindings nested-node))
           (conj (emit-schema nested-node))))
     acc
     (:nested node))))

(defn collect-declarations
  ([node]
   (collect-declarations node []))
  ([node acc]
   (reduce
     (fn [acc nested-node]
       (if (contains? #{:nested/builder} (:category nested-node))
         acc
         (conj acc (var-symbol nested-node "from-edn")
                   (var-symbol nested-node "to-edn"))))
     acc
     (:nested node))))

(defn emit-ns-form
  ([node] (emit-ns-form node nil))
  ([{:keys [package className nested] :as node
     {:keys [peer-mappings foreign-mappings collection-wrappers]} :deps} metadata]
   (let [ns-name        (symbol (str (u/package-to-ns (:package node)) "." (:className node)))
         ;; TODO this might not be handling recursive nested classes right
         primary-import (into [(symbol package) (symbol className)] (map #(symbol (str (:className node) "$" (:className %))) nested))
         imports        [primary-import]
         metadata       (merge (sorted-map) metadata (select-keys node [:doc :fqcn :file-git-sha]))
         peer-mappings  (apply dissoc peer-mappings (keys collection-wrappers))
         requires       (reduce
                          (fn [acc [_ target-ns]]
                            (conj acc [target-ns :as (alias target-ns)]))
                          (sorted-set '[gcp.global :as global])
                          (into peer-mappings foreign-mappings))]
     `(~'ns ~ns-name
        ~metadata
        (:require ~@requires)
        (:import ~@imports)))))

(defn- compile-class-forms
  "Compiles a class node into a sequence of Clojure forms.
   Accepts optional metadata map to inject into the namespace declaration."
  ([node] (compile-class-forms node nil))
  ([{:keys [category nested] :as node} metadata]
   (assert (contains? shared/categories category))
   (let [ns-form           (emit-ns-form node metadata)
         ;; TODO can remove this if nested is emitted in dependency order
         declare-form      (when-some [declarations (not-empty (collect-declarations node))]
                             `(~'declare ~@declarations))
         nested-forms      (when nested
                             (emit-all-nested-forms node))
         [from-edn to-edn] (emit-class-bindings node)
          schema           (emit-schema node)
          registry-entries (collect-registry-entries node)
          registry-map     (into (sorted-map) registry-entries)
          registry-form    `(~'global/include-schema-registry! (~'with-meta ~registry-map {:gcp.global/name (~'str ~'*ns*)}))
         forms (into [ns-form declare-form] nested-forms)]
     (remove nil? (conj forms from-edn to-edn schema registry-form)))))

(defn emit-to-string
  "Compiles a class node into a formatted string.
   Accepts optional metadata map."
  ([node] (emit-to-string node nil))
  ([node metadata]
   (assert (contains? shared/categories (:category node)))
   (let [preamble ";; THIS FILE IS GENERATED; DO NOT EDIT\n"
         forms (compile-class-forms node metadata)
         fix-hints (fn [src]
                     (-> src
                         ;; (defn func__TAG__Type ...) -> (defn ^Type func ...)
                         (string/replace #"(defn\s+)([\w:\$\-\.]+)(?:__TAG__)([\w\$\.]+)"
                                         (fn [[_ d1 func type]] (str d1 "^" type " " func)))
                         ;; (defn func__ARGTAG__Type [arg] ...) -> (defn func [^Type arg] ...)
                         (string/replace #"(defn\s+)([\w:\$\-\.]+)(?:__ARGTAG__)([\w\$\.]+)(\s*\[\s*)arg"
                                         (fn [[_ d1 func type d2]] (str d1 func d2 "^" type " arg")))))]
     (str preamble
          (string/join "\n\n"
                       (map (comp fix-hints zp/zprint-str) forms))))))
