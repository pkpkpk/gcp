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

(defn- short-java-name [t]
  (let [{:keys [class]} (u/split-fqcn (name t))]
    (string/replace class "." "$")))

(defn- group-imports [fqcns]
  (->> fqcns
       (keep (fn [x] (when x (string/trim (str x)))))
       (map #(string/replace % "$" "."))
       set
       (group-by (fn [fqcn] (:package (u/split-fqcn fqcn))))
       (map (fn [[pkg classes]]
              (let [class-names (->> classes
                                     (map (fn [c] (string/replace (:class (u/split-fqcn c)) "." "$")))
                                     set
                                     sort)]
                (into [(symbol pkg)] (map symbol class-names)))))
       (sort-by (comp str first))))

(defn class-sym [node]
  (let [{class-component :class} (u/split-fqcn (:fqcn node))]
    (if (= class-component (:className node))
      (symbol class-component)
      (symbol (string/replace class-component "." "$")))))

(defn var-name [node postfix]
  (let [{:keys [class nested]} (u/split-fqcn (:fqcn node))]
    (if (nil? nested)
      postfix
      (str (string/join "$" nested) "-" postfix))))

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
  (let [{:keys [class nested]} (u/split-fqcn fqcn)]
    (if nested
      (str (string/join "$" nested) "-from-edn")
      (str (string/replace class "." "$") "-from-edn"))))

(defn nested-to-edn [fqcn]
  (let [{:keys [class nested]} (u/split-fqcn fqcn)]
    (if nested
      (str (string/join "$" nested) "-to-edn")
      (str (string/replace class "." "$") "-to-edn"))))

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

(defn peer-nested-to-edn [binding-ns fqcn]
  (let [{:keys [nested]} (u/split-fqcn fqcn)
        f (str (string/join "$" nested) "-to-edn")]
    (symbol (name (alias binding-ns)) f)))

(defn peer-nested-from-edn [binding-ns fqcn]
  (let [{:keys [nested]} (u/split-fqcn fqcn)
        f (str (string/join "$" nested) "-from-edn")]
    (symbol (name (alias binding-ns)) f)))

(defn custom-to-edn [binding-ns fqcn]
  (let [{:keys [class nested] :as split} (u/split-fqcn fqcn)
        alias-name (name (alias binding-ns))]
    (cond
      (= alias-name class)
      (symbol alias-name "to-edn")

      (nil? nested)
      (symbol alias-name (str class "-to-edn"))

      (some? nested)
      (symbol alias-name (str (string/join "$" nested) "-to-edn")))))

(defn custom-from-edn [binding-ns fqcn]
  (let [{:keys [class nested]} (u/split-fqcn fqcn)
        alias-name (name (alias binding-ns))]
    (cond
      (= alias-name class)
      (symbol alias-name "from-edn")

      (nil? nested)
      (symbol alias-name (str class "-from-edn"))

      (some? nested)
      (symbol alias-name (str (string/join "$" nested) "-from-edn")))))

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

(defn invoke-peer-nested-from-edn
  ([binding-ns fqcn]
   (list (peer-nested-from-edn binding-ns fqcn)))
  ([binding-ns fqcn arg]
   (list (peer-nested-from-edn binding-ns fqcn) arg)))

(defn invoke-peer-nested-to-edn
  ([binding-ns fqcn]
   (list (peer-nested-to-edn binding-ns fqcn)))
  ([binding-ns fqcn arg]
   (list (peer-nested-to-edn binding-ns fqcn) arg)))

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

(defn invoke-native-to-edn
  [t arg]
  (case t
    java.lang.Throwable '(Throwable->map arg)
    java.sql.ResultSet arg
    (throw (ex-info (str "missing invoke-native-to-edn branch for " t) {:type t :arg arg}))))

(defn invoke-static
  ([className method-name]
   (list (symbol (str (symbol className) "/" method-name))))
  ([className method-name arg]
   (list (symbol (str (symbol className) "/" method-name)) arg)))

(defn self-to-edn-sym [fqcn]
  (let [{:keys [nested class]} (u/split-fqcn (str fqcn))]
    (if nested
      (symbol (str (string/replace class "." "$") "-to-edn"))
      'to-edn)))

(defn self-from-edn-sym [fqcn]
  (let [{:keys [nested class]} (u/split-fqcn (str fqcn))]
    (if nested
      (symbol (str (string/replace class "." "$") "-from-edn"))
      'from-edn)))

(defn resolve-from-edn-var
  [deps type-category parameter-type]
  (assert (simple-keyword? type-category))
  (assert (symbol? parameter-type))
  (case type-category
    :self    (self-from-edn-sym parameter-type)
    :enum    (symbol (str (short-java-name parameter-type) "/valueOf"))
    :peer    (peer-from-edn (get-in deps [:peer-mappings parameter-type]))
    :foreign (foreign-from-edn (get (:foreign-mappings deps) parameter-type) parameter-type)
    (:nested :sibling)  (symbol (nested-from-edn parameter-type))
    :custom  (custom-from-edn (get (:custom-mappings deps) parameter-type) parameter-type)))

(defn scalar-from-edn
  [_deps parameter-type arg]
  (case parameter-type
    (byte java.lang.Byte)   (list 'byte arg)
    (int java.lang.Integer) (list 'int arg)
    arg))

(defn convert-param-type-from-edn [deps parameter-type arg]
  (let [type-category (categorize-type deps parameter-type)]
    (match type-category
      :self (list (self-from-edn-sym parameter-type) arg)

      :generic arg

      :scalar (scalar-from-edn deps parameter-type arg)

      :enum (list (symbol (str (short-java-name parameter-type) "/valueOf")) arg)

      [(:or :iterable :list) (:or :generic :scalar)] (list 'seq arg)

      [(:or :array :set :iterator :optional) (:or :generic :scalar)] arg

      :custom (let [binding-ns (get (:custom-mappings deps) parameter-type)]
                (invoke-custom-from-edn binding-ns parameter-type arg))
      :foreign (let [binding-ns (get (:foreign-mappings deps) parameter-type)]
                 (invoke-foreign-from-edn binding-ns parameter-type arg))
      :peer (let [binding-ns (get (:peer-mappings deps) parameter-type)]
              (invoke-peer-from-edn binding-ns arg))
      :peer/nested (let [binding-ns (get (:peer-mappings deps) parameter-type)]
                     (invoke-peer-nested-from-edn binding-ns parameter-type arg))
      (:or :nested :sibling) (invoke-nested-from-edn parameter-type arg)

      [:peer :generic] (let [binding-ns (get (:peer-mappings deps) (first parameter-type))]
                         (invoke-peer-from-edn binding-ns arg))

      [:foreign :generic] (let [binding-ns (get (:foreign-mappings deps) (first parameter-type))]
                            (invoke-foreign-from-edn binding-ns (first parameter-type) arg))

      [(:or :array :list :set :iterable)
       (:or :peer :nested :sibling :foreign :custom :self :enum)] (let [[_ element-type] parameter-type
                                                                        [_ element-category] type-category
                                                                        from-edn (resolve-from-edn-var deps element-category element-type)
                                                                        ls (list 'map from-edn arg)]
                                                                    (if (= :array (first type-category))
                                                                      (list 'into-array (symbol (short-java-name element-type)) ls)
                                                                      ls))

      [:map :scalar
       (:or :scalar :generic)] (let [K (second parameter-type)]
                                 (if (= K 'java.lang.String)
                                   `(~'into {} (~'map (~'fn [[~'k ~'v]] [(~'name ~'k) ~'v])) ~arg)
                                   arg))

      [:map :scalar (:or :peer :foreign :custom :self)] (let [[_ key-type element-type] parameter-type
                                                              [_ _ element-category] type-category
                                                              from-edn (resolve-from-edn-var deps element-category element-type)
                                                              k  (if (= key-type 'java.lang.String)
                                                                   (list 'name 'k)
                                                                   'k)]
                                                          `(~'into {} (~'map (~'fn [[~'k ~'v]] [~k ~(list from-edn 'v)])) ~arg))

      [:map :scalar
       [(:or :array :list :set)
        (:or :peer :foreign :nested :sibling :custom :self)]] (let [[_ key-type [_ element-type]] parameter-type
                                                                    [_ _ [_ element-category]] type-category
                                                                    from-edn (resolve-from-edn-var deps element-category element-type)
                                                                    ls (list 'map from-edn 'v)
                                                                    k  (if (= key-type 'java.lang.String)
                                                                         (list 'name 'k)
                                                                         'k)]
                                                                `(~'into {} (~'map (~'fn [[~'k ~'v]] [~k ~ls])) ~arg))

      [:optional (:or :peer :foreign :nested :sibling :custom)] (let [[_ element-type] parameter-type
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
    [:iterable
     (:or :foreign :custom :peer)] `(g/valid? [:sequential ~(u/schema-key deps (second param-type))] ~arg)
    (:or :foreign :peer :custom :nested :sibling) `(g/valid? ~(u/schema-key deps param-type) ~arg)
    (throw (Exception. (str "could not resolve type predicate: " (pr-str param-type))))))

(defn invoke-static-method-via-map-keys-from-edn
  [deps class-sym {method-name :name :keys [parameters]}]
  `(~(symbol (name class-sym) method-name)
     ~@(map
         (fn [param] (convert-param-type-from-edn deps (:type param) (list 'get 'arg (or (:key param) (keyword (:name param))))))
         parameters)))

(defn invoke-instance-method-from-edn
  [deps {method-name :name :keys [parameters]} instance]
  `(~(symbol (str "." method-name)) ~instance
     ~@(map
         (fn [param]
           (convert-param-type-from-edn deps (:type param) (list 'get 'arg (u/property-key method-name))))
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
     :self (self-to-edn-sym parameter-type)
     :enum '.name
     :peer (peer-to-edn (get-in deps [:peer-mappings parameter-type]))
     :foreign (foreign-to-edn (get (:foreign-mappings deps) parameter-type) parameter-type)
     (:nested :sibling) (symbol (nested-to-edn parameter-type))
     :custom (custom-to-edn (get (:custom-mappings deps) parameter-type) parameter-type))))

(defn emit-invoke-getter-to-edn
  [deps {:keys [name returnType synthetic? field-name private?] :as method} instance]
  (let [extraction (cond
                     synthetic?
                     (list 'global/get-private-field instance field-name)

                     private?
                     (list 'global/invoke-private-method instance name)

                     :else
                     (list (symbol (str "." name)) instance))
        type-category (categorize-type deps returnType)]
    (match type-category
      (:or :scalar :generic) extraction

      :enum (list '.name extraction)

      :native (invoke-native-to-edn returnType extraction)

      [(:or :list :array :iterable) (:or :generic :scalar)] (list 'seq extraction)

      [(:or :set) (:or :generic :scalar)] extraction

      :foreign (let [binding-ns (get (:foreign-mappings deps) returnType)]
                 (invoke-foreign-to-edn binding-ns returnType extraction))
      :peer (let [binding-ns (get (:peer-mappings deps) returnType)]
              (invoke-peer-to-edn binding-ns extraction))
      :peer/nested (let [binding-ns (get (:peer-mappings deps) returnType)]
                     (invoke-peer-nested-to-edn binding-ns returnType extraction))
      :custom (let [binding-ns (get (:custom-mappings deps) returnType)]
                (invoke-custom-to-edn binding-ns returnType extraction))
      :nested (invoke-nested-to-edn returnType extraction)
      :self (list (self-to-edn-sym returnType) extraction)

      [:foreign :generic] (let [binding-ns (get (:foreign-mappings deps) (first returnType))]
                            (invoke-foreign-to-edn binding-ns (first returnType) extraction))

      [(:or :array :list :set :iterable) :enum] (let [to-edn '(fn [e] (.name e))
                                                      ls     (list 'map to-edn extraction)]
                                                  (if (= :set (first type-category))
                                                    (list 'into #{} ls)
                                                    ls))

      [(:or :array :list :set :iterable)
       (:or :custom :peer :nested :sibling :foreign :self)] (let [[_ element-type] returnType
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
       (:or :custom :peer :foreign :nested :sibling :self)] (let [[_ key-type element-type] returnType
                                                                  [_ _ element-category] type-category
                                                                  to-edn (resolve-to-edn-var deps element-category element-type)
                                                                  k      (if (= key-type 'java.lang.String)
                                                                           (list 'keyword 'k)
                                                                           'k)]
                                                              `(~'into {} (~'map (~'fn [[~'k ~'v]] [~k (~to-edn ~'v)])) ~extraction))

      [:map :scalar
       [(:or :array :list :set)
        (:or :custom :peer :foreign :nested :sibling :self)]] (let [[_ key-type [_ element-type]] returnType
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
       (:or :custom :peer :foreign :nested :sibling :self)] (let [[_ element-type] returnType
                                                                  [_ element-category] type-category
                                                                  to-edn (resolve-to-edn-var deps element-category element-type)]
                                                              `(~'when (~'.isPresent ~extraction)
                                                                 (~to-edn (~'.get ~extraction))))

      [:optional (:or :scalar :generic)] `(~'when (~'.isPresent ~extraction) (~'.get ~extraction))

      [:optional                                            ;; TODO the map body could be recursive
       [:map :scalar (:or :scalar :generic)]] (let [K (first (second returnType))]
                                                `(~'when (~'.isPresent ~extraction)
                                                   ~(if (= K 'java.lang.String)
                                                      `(~'into {} (~'map (~'fn [[~'k ~'v]] [(~'keyword ~'k) ~'v])) (~'.get ~extraction))
                                                      (list '.get extraction))))

      [:iterator (:or :scalar :generic)] (list 'iterator-seq extraction)

      [:iterator
       (:or :custom :peer :foreign :nested :sibling :self)] (let [[_ element-type] returnType
                                                                  [_ element-category] type-category
                                                                  to-edn (resolve-to-edn-var deps element-category element-type)]
                                                              (list 'map to-edn (list 'iterator-seq extraction)))

      ['com.google.api.gax.rpc.BidiStream :peer :peer] ::TODO
      ['com.google.cloud.RestorableState :self] ::TODO
      ['com.google.cloud.RestorableState :peer] ::TODO
      ['java.util.concurrent.CompletableFuture :scalar] ::TODO
      ['java.util.concurrent.CompletableFuture [:list :generic]] ::TODO
      ['java.util.concurrent.CompletableFuture [:optional :peer]] ::TODO
      :else (throw (ex-info (str "could not resolve to-edn returnType category " type-category)
                            {:deps deps
                             :type-category type-category
                             :method method})))))

#!----------------------------------------------------------------------------------------------------------------------
#! :static-factory

(defn static-factory-from-edn-body:single-method-single-param
  [{:keys [deps] [{[parameter] :parameters :as method}] :factory-methods} class-sym]
  (let [from-edn (partial convert-param-type-from-edn deps (:type parameter))]
    (list 'if (param-predicate-test deps (:type parameter) 'arg)
          (invoke-static class-sym (:name method) (from-edn 'arg))
          (invoke-static class-sym (:name method) (from-edn (list 'get 'arg (keyword (:name parameter))))))))

(defn _static-factory-single-param-method-from-edn-cond-branches
  [{:keys [deps]} {[parameter] :parameters method-name :name} class-sym]
  (let [from-edn (partial convert-param-type-from-edn deps (:type parameter))]
    [(param-predicate-test deps (:type parameter) 'arg)
     (invoke-static class-sym method-name (from-edn 'arg))
     (list 'get 'arg (keyword (:name parameter)))
     (invoke-static class-sym method-name (from-edn (list 'get 'arg (keyword (:name parameter)))))]))

(defn- static-factory-from-edn-body:intersecting-params
  ([{:keys [factory-methods] :as node} class-sym]
   (let [min-arity (apply min (map (comp count :parameters) factory-methods))]
     (static-factory-from-edn-body:intersecting-params node class-sym (= 1 min-arity))))
  ([{:keys [deps factory-methods] :as node} class-sym sugar?]
   (let [all-params      (map (fn [m] (set (map (comp keyword :name) (:parameters m)))) factory-methods)
         required-params (if (seq all-params)
                           (apply clojure.set/intersection all-params)
                           #{})
         ; optional-params (clojure.set/difference (reduce into all-params) required-params)
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
               (invoke-static class-sym method-name arg-conversion)
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
                    :single-method-no-param (invoke-static class-sym (get-in factory-methods [0 :name]))
                    :single-method-single-param (static-factory-from-edn-body:single-method-single-param node class-sym)
                    :map-keys (invoke-static-method-via-map-keys-from-edn deps class-sym (first factory-methods))
                    :intersecting-with-sugar (static-factory-from-edn-body:intersecting-params node class-sym true)
                    :intersecting-no-sugar (static-factory-from-edn-body:intersecting-params node class-sym false)
                    :cond (static-factory-from-edn-body:cond node class-sym))]
    (defn-from-edn node body)))

(defn- resolve-factory-method-getters [getters-by-key method]
  (let [parameters (:parameters method)
        getters (vals getters-by-key)]
    (map
      (fn [{:keys [name type] :as param}]
        (let [param-key (keyword name)
              exact-getter (get getters-by-key param-key)]
          (if exact-getter
            {:param param, :getter exact-getter, :instance-check? false}
            (let [param-class (try (u/as-class type) (catch Exception _ nil))
                  matching-getters (filter (fn [g]
                                             (let [rt-class (try (u/as-class (:returnType g)) (catch Exception _ nil))]
                                               (and rt-class (.isAssignableFrom rt-class param-class))))
                                           getters)
                  g (first matching-getters)]
              (if g
                {:param param, :getter g, :instance-check? true}
                (throw (ex-info "No matching getter found for factory parameter" {:method method, :param param})))))))
      parameters)))

(defn- static-factory-to-edn-body:cond [node]
  (let [deps (:deps node)
        getters-by-key (:getters-by-key node)
        factory-methods (:factory-methods node)
        [intersected] (sort-by count > (sequence (comp (remove empty?) (filter intersecting-methods?)) (combo/subsets factory-methods)))
        other (remove (set intersected) factory-methods)
        methods (into (vec other) (sort-by (comp count :parameters) > intersected))
        branches (reduce
                   (fn [acc method]
                     (try
                       (let [mappings (resolve-factory-method-getters getters-by-key method)
                             conditions (map
                                          (fn [{:keys [param getter instance-check?]}]
                                            (let [getter-expr (list (symbol (str "." (:name getter))) 'arg)]
                                              (if instance-check?
                                                `(and ~getter-expr (~'instance? ~(symbol (short-java-name (:type param))) ~getter-expr))
                                                getter-expr)))
                                          mappings)
                             condition (if (= 1 (count conditions))
                                         (first conditions)
                                         `(and ~@conditions))
                             result (into {}
                                          (map
                                            (fn [{:keys [param getter]}]
                                              [(keyword (:name param))
                                               (emit-invoke-getter-to-edn deps (assoc getter :returnType (:type param)) 'arg)])
                                            mappings))]
                         (conj acc condition result))
                       (catch Exception _ acc)))
                   []
                   methods)]
    `(~'cond ~@branches :else (throw (ex-info "failed to match edn for static-factory cond body" {:arg ~'arg :key ~(:gcp/key node)})))))

(defn- emit-static-factory-to-edn
  [{:keys [deps getters-by-key strategy] :as node}]
  (if (empty? getters-by-key)
    `(~'defn ~(to-edn-function-name node) [~'arg]
       ~(list 'throw (list 'Exception. (str "to-edn is not supported for type " (:fqcn node)))))
    (if (= strategy :cond)
      (defn-to-edn node (static-factory-to-edn-body:cond node))
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
               ~@(mapcat identity opt-steps))))))))

#!----------------------------------------------------------------------------------------------------------------------
#! :union-abstract

(defn emit-union-abstract-from-edn
  [{:keys [variant-mappings deps] :as node}]
  (let [branches  (mapcat
                    (fn [[t fqcn]]
                      (let [from-edn (resolve-from-edn-var deps (categorize-type deps fqcn) fqcn)]
                        [t (list from-edn 'arg)]))
                    variant-mappings)]
    (defn-from-edn node `(~'case (~'get ~'arg :type) ~@branches))))

(defn emit-union-abstract-to-edn
  [{:keys [discriminator-method deps variant-mappings] :as node}]
  (let [branches (mapcat
                   (fn [[t fqcn]]
                     (let [to-edn (resolve-to-edn-var deps (categorize-type deps fqcn) fqcn)]
                       [t (list to-edn 'arg)]))
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
                          [t (invoke-static class-sym (:name method))])
                        self-variant-mappings)
        peer-branches (mapcat
                        (fn [[t peer]]
                          [t (invoke-peer-from-edn peer 'arg)])
                        peer-variant-mappings)
        case-body `(~'case (~'get ~'arg :type)
                     ~@(concat self-branches peer-branches))]
    (defn-from-edn node
                   (if sugar-factory
                     `(~'if (~'string? ~'arg)
                        ~(invoke-static class-sym (:name sugar-factory) 'arg)
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
                           [tag (invoke-static class-sym name (list 'get 'arg payload-key))])
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
                      (list 'when (list 'some? (list 'get 'arg k))
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
        branches (mapcat
                   (fn [[key method]]
                     (let [test   (invoke-method method 'arg)
                           to-edn (emit-invoke-getter-to-edn deps method 'arg)]
                       [test (list 'assoc key to-edn)]))
                   (apply dissoc getters-by-key (:keys/required node)))
        body `(~'cond-> ~base ~@branches)]
    (defn-to-edn node body)))

#!----------------------------------------------------------------------------------------------------------------------
#! :collection-wrapper

(defn emit-collection-wrapper-from-edn
  [{:keys [of-iterable deps] :as node}]
  (let [class-sym (class-sym node)
        {method-name :name [parameter] :parameters} of-iterable
        from-edn (convert-param-type-from-edn deps (:type parameter) 'arg)
        body (invoke-static class-sym method-name from-edn)]
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

(defn emit-nested-variant-pojo-to-edn
  [{:keys [getters-by-key deps keys/required discriminator] :as node}]
  (let [getters (dissoc getters-by-key :type)
        required-keys (remove #(= % :type) required)
        base-map (assoc (into {}
                              (map
                                (fn [[field-key getter]]
                                  [field-key (emit-invoke-getter-to-edn deps getter 'arg)]))
                              (select-keys getters required-keys))
                   :type discriminator)
        body (if-some [optional-keys (not-empty (apply dissoc getters required-keys))]
               `(~'cond-> ~base-map
                  ~@(mapcat
                      (fn [[k getter]]
                        [(invoke-method getter 'arg) (list 'assoc k (emit-invoke-getter-to-edn deps getter 'arg))])
                      optional-keys))
               base-map)]
    (defn-to-edn node body)))

#!----------------------------------------------------------------------------------------------------------------------
#! variant-accessor

(defn emit-variant-accessor-from-edn
  [{:keys [deps newBuilder] :as node}]
  (let [class-sym (class-sym node)
        body `(~'let [~'builder ~(invoke-static-method-via-map-keys-from-edn deps class-sym newBuilder)]
                ~@(map
                    (fn [[k method]]
                      (list 'when (list 'some? (list 'get 'arg k))
                            (invoke-instance-method-from-edn deps method 'builder)))
                    (:setters-by-key node))
                (~'.build ~'builder))]
    (defn-from-edn node body)))

(defn emit-variant-accessor-to-edn
  [{:keys [getters-by-key deps discriminator] :as node}]
  (let [base (into (sorted-map :type discriminator)
                   (map
                     (fn [[key method]]
                       [key (emit-invoke-getter-to-edn deps method 'arg)]))
                   (select-keys getters-by-key (:keys/required node)))
        branches (mapcat
                   (fn [[key method]]
                     (let [test   (invoke-method method 'arg)
                           to-edn (emit-invoke-getter-to-edn deps method 'arg)]
                       [test (list 'assoc key to-edn)]))
                   (apply dissoc getters-by-key (:keys/required node)))
        body `(~'cond-> ~base ~@branches)]
    (defn-to-edn node body)))

#!----------------------------------------------------------------------------------------------------------------------

(defn emit-mutable-pojo-from-edn
  [{:keys [deps getter-setters-by-key] :as node}]
  (let [class-sym (class-sym node)
        branches (reduce
                   (fn [acc [k {{:as m} :setter}]]
                     (let [; form `(~'when-some [~'val (~'get ~'arg ~k)])
                           ;; TODO (when-some-invoke-method-from-edn ...)
                           form `(~'when (~'some? (~'get ~'arg ~k))
                                   ~(invoke-instance-method-from-edn deps m 'o))]
                       (conj acc form)))
                   []
                   getter-setters-by-key)
        body `(~'let [~'o (~'new ~class-sym)]
                ~@branches
                ~'o)]
    (defn-from-edn node body)))

(defn emit-mutable-pojo-to-edn
  [{:keys [deps getter-setters-by-key] :as node}]
  (let [branches (reduce
                   (fn [acc [k {{:as m} :getter}]]
                     (let [test   (invoke-method m 'arg)
                           to-edn (emit-invoke-getter-to-edn deps m 'arg)]
                       (conj acc test (list 'assoc k to-edn))))
                   []
                   getter-setters-by-key)]
    (defn-to-edn node `(~'cond-> {} ~@branches))))

(defn emit-client-options-from-edn
  [{:keys [key->factory deps] :as node}]
  (let [class-sym (class-sym node)
        func-name-array (symbol (str (var-name node "Array-from-edn") "__TAG__" class-sym "/1"))
        func-name-single (symbol (str (var-name node "from-edn") "__TAG__" class-sym))
        schema-key (:gcp/key node)
        branches-array (mapcat
                         (fn [[k {:keys [parameters] :as method}]]
                           [k (if (seq parameters)
                                (let [param (first parameters)
                                      converted (convert-param-type-from-edn deps (:type param) 'v)]
                                  `(~'conj ~'acc ~(invoke-static class-sym (:name method) converted)))
                                `(if ~'v (conj ~'acc ~(invoke-static class-sym (:name method))) ~'acc))])
                         key->factory)
        branches-single (mapcat
                          (fn [[k {:keys [parameters] :as method}]]
                            [k (if (seq parameters)
                                 (let [param (first parameters)
                                       converted (convert-param-type-from-edn deps (:type param) 'v)]
                                   `(~'reduced ~(invoke-static class-sym (:name method) converted)))
                                 `(if ~'v (~'reduced ~(invoke-static class-sym (:name method))) ~'acc))])
                          key->factory)
        body-array `(~'into-array ~class-sym
                      (~'reduce-kv
                        (~'fn [~'acc ~'k ~'v]
                          (~'case ~'k
                            ~@branches-array
                            ~'acc))
                        []
                        ~'arg))
        body-single `(~'reduce-kv
                       (~'fn [~'acc ~'k ~'v]
                         (~'case ~'k
                           ~@branches-single
                           ~'acc))
                       nil
                       ~'arg)]
    `(~'do
       (~'defn ~func-name-array [~'arg]
         (~'global/strict! ~schema-key ~'arg)
         ~body-array)
       (~'defn ~func-name-single [~'arg]
         (~'global/strict! ~schema-key ~'arg)
         ~body-single))))

(defn emit-client-options-to-edn [node] nil)

#!----------------------------------------------------------------------------------------------------------------------

(defn emit-static-variants-from-edn
  [{:keys [key->factory deps] :as node}]
  (let [class-sym (class-sym node)
        branches (reduce
                   (fn [acc [key {:keys [parameters] :as method}]]
                     (let [test (list 'contains? 'arg key)
                           to-edn (if (seq parameters)
                                    (invoke-static-method-via-map-keys-from-edn deps class-sym method)
                                    (invoke-static class-sym (:name method)))]
                       (conj acc test to-edn)))
                   []
                   key->factory)
        body (if (< 1 (count key->factory))
               `(~'cond ~@branches)
               (second branches))]
    (defn-from-edn node body)))

(defn emit-static-variants-to-edn
  [{:keys [discriminator-method variant->key variant->getter deps] :as node}]
  (let [branches (reduce
                   (fn [acc [variant key]]
                     (let [ret (if-let [method (get variant->getter variant)]
                                 {key (emit-invoke-getter-to-edn deps method 'arg)}
                                 {key nil})]
                       (conj acc variant ret)))
                   []
                   variant->key)
        body (if (< 1 (count variant->key))
               `(~'case ~(emit-invoke-getter-to-edn deps discriminator-method 'arg)
                  ~@branches)
               (second branches))]
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

(defn emit-class-bindings
  [{:keys [category] :as node}]
  (case category
    :nested/builder
    nil

    :client
    nil

    (:enum :string-enum :nested/enum :nested/string-enum)
    nil

    :nested/client-options
    [(emit-client-options-from-edn node)
     (emit-client-options-to-edn node)]

    (:static-variants :nested/static-variants)
    [(emit-static-variants-from-edn node)
     (emit-static-variants-to-edn node)]

    ; (:static-utilities :nested/static-utilities)
    ; nil

    (:mutable-pojo :nested/mutable-pojo)
    [(emit-mutable-pojo-from-edn node)
     (emit-mutable-pojo-to-edn node)]

    :collection-wrapper
    [(emit-collection-wrapper-from-edn node)
     (emit-collection-wrapper-to-edn node)]

    (:accessor-with-builder  :nested/accessor-with-builder)
    [(emit-accessor-with-builder-from-edn node)
     (emit-accessor-with-builder-to-edn node)]

    :variant-accessor
    [(emit-variant-accessor-from-edn node)
     (emit-variant-accessor-to-edn node)]

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

    (:interface :read-only :nested/read-only)
    [(emit-read-only-from-edn node)
     (emit-read-only-to-edn node)]

    (:pojo :nested/pojo)
    [(emit-pojo-from-edn node)
     (emit-pojo-to-edn   node)]

    :nested/variant-pojo
    [(emit-pojo-from-edn node)
     (emit-nested-variant-pojo-to-edn node)]

    (throw (Exception. (str "bindings unimplemented for category " category)))))

(defn- emit-schema [node]
  (when-not (#{:client} (:category node))
    `(~'def ~(var-symbol node "schema") ~(m/->schema node))))

(defn- emit-all-nested-forms
  ([node]
   (emit-all-nested-forms node []))
  ([node acc]
   (reduce
     (fn [acc nested-node]
       (if (= :nested/builder (get nested-node :category))
         acc
         (-> (emit-all-nested-forms nested-node acc)
             (into (emit-class-bindings nested-node))
             (conj (emit-schema nested-node)))))
     acc
     (:nested node))))

(defn collect-declarations
  ([node]
   (collect-declarations node [(var-symbol node "from-edn")
                               (var-symbol node "to-edn")]))
  ([node acc]
   (reduce
     (fn [acc nested-node]
       (if (contains? #{:nested/builder} (:category nested-node))
         acc
         (let [acc (conj acc (var-symbol nested-node "from-edn")
                             (var-symbol nested-node "to-edn"))]
           (collect-declarations nested-node acc))))
     acc
     (:nested node))))

(defn collect-registry-entries
  ([node]
   (collect-registry-entries node (cond-> [] (not= :client (:category node))
                                          (conj [(:gcp/key node) (symbol (var-name node "schema"))]))))
  ([node acc]
   (reduce
     (fn [acc nested-node]
       (if (contains? #{:nested/builder} (:category nested-node))
         acc
         (-> (collect-registry-entries (:nested nested-node) acc)
             (conj [(:gcp/key nested-node) (symbol (var-name nested-node "schema"))]))))
     acc
     (:nested node))))

(defn node->ns-sym [node]
  ; (let [{:keys [class package]} (u/split-fqcn (:fqcn node))])
  (symbol (str (u/package-to-ns (:package node)) "." (:className node))))

(defn emit-ns-form
  ([node] (emit-ns-form node nil))
  ([{:keys [package className nested] :as node
     {:keys [custom-mappings peer-mappings foreign-mappings]} :deps} metadata]
   (when-not (contains? node :require-types)
     (throw (ex-info "Missing :require-types in node during emission"
                     {:fqcn (:fqcn node) :category (:category node)})))
   (let [ns-name        (node->ns-sym node)
         self-fqcn      (str package "." className)
         all-fqcns      (into (set (map str (:import-types node))) (map :fqcn nested))
         all-fqcns      (conj all-fqcns self-fqcn)
         imports        (group-imports all-fqcns)
         metadata       (merge (sorted-map) metadata (select-keys node [:doc :fqcn :file-git-sha]))
         requires       (reduce
                          (fn [acc [t category]]
                            (case category
                              :scalar acc
                              :enum acc
                              :peer (let [target-ns (get peer-mappings t)]
                                      (conj acc [target-ns :as (alias target-ns)]))
                              :peer/nested (let [{:keys [parent-fqcn]} (u/split-fqcn t)
                                                 target-ns (get peer-mappings (symbol parent-fqcn))]
                                             (assert (some? target-ns) (str "expected peer-mapping for " (symbol parent-fqcn)))
                                             (if (some #(= target-ns %) (map first acc))
                                               acc
                                               (conj acc [target-ns :as (alias target-ns)])))
                              :foreign (let [target-ns (get foreign-mappings t)]
                                         (conj acc [target-ns :as (alias target-ns)]))
                              :custom (let [target-ns (get custom-mappings t)]
                                        (conj acc [target-ns :as (alias target-ns)]))
                              (throw (ex-info (str "failed require resolution for " t " : " category) {:deps (:deps node)
                                                                                                       :require-types (:require-types node)}))))
                          (sorted-set '[gcp.global :as global])
                          (:require-types node))]
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
   (let [metadata          (merge (select-keys node [:fqcn :file-git-sha]) metadata)
         ns-form           (emit-ns-form node metadata)
         ;; TODO can remove this if nested is emitted in dependency order
         declare-form      (when-not (= :client category)
                             (when-some [declarations (not-empty (collect-declarations node))]
                               `(~'declare ~@declarations)))
         nested-forms      (when nested
                             (emit-all-nested-forms node))
         [from-edn to-edn] (emit-class-bindings node)
         schema            (emit-schema node)
         registry-entries  (collect-registry-entries node)
         registry-map      (into (sorted-map) registry-entries)
         registry-form     `(~'global/include-schema-registry!
                              (~'with-meta ~registry-map {:gcp.global/name ~(str (node->ns-sym node))}))
         forms             (into [ns-form declare-form] nested-forms)]
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
                         (string/replace #"(defn\s+)([\w:\$\-\.]+)(?:__TAG__)([\w\$\./]+)"
                                         (fn [[_ d1 func type]] (str d1 "^" type " " func)))
                         ;; (defn func__ARGTAG__Type [arg] ...) -> (defn func [^Type arg] ...)
                         (string/replace #"(defn\s+)([\w:\$\-\.]+)(?:__ARGTAG__)([\w\$\./]+)(\s*\[\s*)arg"
                                         (fn [[_ d1 func type d2]] (str d1 func d2 "^" type " arg")))))]     (str preamble
                                                                                                               (string/join "\n\n"
                                                                                                                            (map (comp fix-hints zp/zprint-str) forms))))))
