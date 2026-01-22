(ns gcp.dev.packages.package
  (:require
   [clojure.set :as s]
   [clojure.string :as string]
   [gcp.dev.util :as u]
   [malli.core :as m]))

(def pkg-node-schema :map)
(def class-node-schema :map)

(defn named? [class-like] (or (string? class-like) (ident? class-like)))

(defn fqcn? [{:keys [package-prefixes] :as pkg} class-like]
  (and (named? class-like)
       (reduce
         (fn [_ prefix]
           (when (string/starts-with? (name class-like) prefix)
             (reduced true)))
         nil package-prefixes)))

(defn lookup-class [pkg class-like]
  (assert (m/validate pkg-node-schema pkg))
  (if (named? class-like)
    (if (fqcn? pkg class-like)
      (get-in pkg [:class/by-fqcn (name class-like)])
      (when-let [fqcn (get-in pkg [:class/name->fqcn (name class-like)])]
        (get-in pkg [:class/by-fqcn fqcn])))
    (if (and (map? class-like)
             (contains? (set (vals (:class/by-fqcn pkg))) class-like))
      class-like ;; already a node
      (throw (Exception. "bad class-like")))))

(def default-ignored-prefixes
  #{"com.google.api.services."})

(defn- ignored-type? [type-sym]
  (let [t-str (str type-sym)]
    (some #(string/starts-with? t-str %) default-ignored-prefixes)))

(defn- extract-type-symbols [type-ast]
  (cond
    (symbol? type-ast) (if (or (#{'? 'void} type-ast) (ignored-type? type-ast)) (sorted-set) (conj (sorted-set) type-ast))
    (sequential? type-ast) (if (= :type-parameter (first type-ast))
                             (sorted-set)
                             (reduce into (sorted-set) (map extract-type-symbols type-ast)))
    :else (sorted-set)))

(defn class-user-types
  ([pkg class-like]
   (if-some [node (lookup-class pkg class-like)]
     (class-user-types node)
     (throw (ex-info "class not found" {:pkg pkg :class-like class-like}))))
  ([class-node]
   (let [members      (concat (:constructors class-node) (:methods class-node))
         nested       (:nested class-node)
         param-types  (mapcat :parameters members)
         return-types (keep :returnType members)
         all-types    (concat (map :type param-types) return-types)]
     (into (reduce into (sorted-set) (map extract-type-symbols all-types))
           (mapcat class-user-types nested)))))

(defn- native-type? [pkg type-sym]
  (let [t-str (str type-sym)
        pkg-name (:package-name pkg)
        _ (assert (some? pkg-name))
        package-prefixes (or (:package-prefixes pkg) #{pkg-name})]
    (some #(string/starts-with? t-str %) package-prefixes)))

(defn class-package-user-types [pkg class-like]
  (let [class-node (if (map? class-like)
                     class-like
                     (lookup-class pkg class-like))]
    (if class-node
      (let [all-types (class-user-types class-node)]
        (into (sorted-set)
              (filter (fn [t] (native-type? pkg t)))
              all-types))
      (throw (ex-info "class not found" {:pkg pkg :class-like class-like})))))

;; -------------------------------------------------------------------------
;; Dependency Graph Logic
;; -------------------------------------------------------------------------

(defn- collect-types
  "Walks an arbitrary data structure (AST type representation) and collects all Symbols.
   Excludes common java.lang types if desired, but here we collect everything that looks like a class."
  [x]
  (let [types (atom (sorted-set))]
    (clojure.walk/postwalk
      (fn [form]
        (when (symbol? form)
          (let [s (name form)]
            ;; Heuristic: starts with uppercase or contains dot, likely a class
            (when (or (string/includes? s ".")
                      (re-matches #"^[A-Z].*" s))
              (swap! types conj (name form)))))
        form)
      x)
    @types))

(def ignore-extends-categories
  #{:enum :string-enum :union-abstract :union-concrete :variant-accessor :union-factory
    :accessor-with-builder :builder :static-factory :factory :pojo :read-only
    :client})

(defn- extract-deps-from-node
  "Extracts all dependencies (as FQCN strings) from a class AST node."
  [node]
  (let [;; 1. Extends (conditional)
        extends-deps (if (contains? ignore-extends-categories (:category node))
                       []
                       (mapcat collect-types (:extends node)))
        ;; 2. Implements
        implements-deps (mapcat collect-types (:implements node))
        ;; 3. Fields (types)
        field-deps (mapcat #(collect-types (:type %)) (:fields node))
        ;; 4. Methods (return types + parameter types)
        method-deps (mapcat (fn [m]
                              (concat (collect-types (:returnType m))
                                      (mapcat #(collect-types (:type %)) (:parameters m))))
                            (:methods node))
        ;; 5. Constructors
        ctor-deps (mapcat (fn [c]
                            (mapcat #(collect-types (:type %)) (:parameters c)))
                          (:constructors node))
        nested-deps (mapcat extract-deps-from-node (:nested node))
        all-deps (concat extends-deps implements-deps field-deps method-deps ctor-deps nested-deps)
        pruned (or (:prune-dependencies node) #{})]
    ;; 6. Nested classes (recurse? No, dependency graph usually treats top-level)
    ;;    But we might want to depend on inner classes.
    (into (sorted-set)
          (remove pruned)
          all-deps)))

(defn build-dependency-graph
  "Builds a dependency graph from the package analysis result.
   Returns a map: {FQCN #{DependencyFQCN ...}}"
  [package-ast]
  (let [fqcn-map (:class/by-fqcn package-ast)
        internal-classes (set (keys fqcn-map))
        base-resolve (fn [dep]
                       (if (contains? internal-classes dep)
                         dep
                         (let [parts (string/split dep #".")]
                           (loop [p parts]
                             (if (empty? p)
                               nil
                               (let [parent (string/join "." p)]
                                 (if (contains? internal-classes parent)
                                   parent
                                   (recur (pop p)))))))))]

    (reduce-kv (fn [graph fqcn node]
                 (let [raw-deps (extract-deps-from-node node)
                       current-pkg (:package node)

                       resolve-dep (fn [dep]
                                     (or (base-resolve dep)
                                         (when current-pkg
                                           (base-resolve (str current-pkg "." dep)))))

                       internal-deps (into #{}
                                           (comp (keep resolve-dep)
                                                 (remove #(= % fqcn)))
                                           raw-deps)]
                   (assoc graph fqcn internal-deps)))
               {}
               fqcn-map)))

(defn dependency-tree
  "Generates a dependency tree (nested map) starting from a root class.
   'package-path' is the file path to the package source (for analysis).
   'root-class-name' is the simple name or FQCN of the root class (e.g. 'Storage').

   Returns a map where keys are class names and values are sub-dependencies.
   Uses a 'seen' set to break cycles."
  [pkg-ast root-class-name]
  (let [graph   (build-dependency-graph pkg-ast)
        ;; Find FQCN for root-class-name if it's simple
        root-fqcn (or (when (contains? graph root-class-name) root-class-name)
                      (first (filter #(string/ends-with? % (str "." root-class-name)) (keys graph))))]

    (when-not root-fqcn
      (throw (ex-info (str "Root class not found: " root-class-name) {:available (take 10 (keys graph))})))

    (letfn [(build-tree [node seen]
              (if (contains? seen node)
                {node :cycle}
                (let [deps (get graph node)
                      new-seen (conj seen node)]
                  {node (into {} (map #(build-tree % new-seen) deps))})))]

      (build-tree root-fqcn #{}))))

(defn transitive-closure
  "Returns the set of all internal classes reachable from the given roots.
   'roots' is a collection of FQCN strings."
  [pkg roots]
  (let [graph (build-dependency-graph pkg)
        roots (set roots)]
    (loop [queue (into clojure.lang.PersistentQueue/EMPTY roots)
           visited roots]
      (if (empty? queue)
        visited
        (let [current (peek queue)
              queue   (pop queue)
              deps    (get graph current)
              new-deps (remove visited deps)]
          (recur (into queue new-deps)
                 (into visited new-deps)))))))

(defn topological-sort
  "Returns a sequence of FQCNs sorted topologically (dependencies first).
   'nodes' is a collection of FQCN strings to sort.
   Throws an exception if a circular dependency is detected."
  [pkg nodes]
  (let [graph (build-dependency-graph pkg)
        ;; We only care about the subgraph defined by 'nodes'
        relevant-graph (select-keys graph nodes)
        nodes-set (set nodes)]
    (letfn [(dfs [node visited visiting path stack]
              (cond
                (contains? visiting node)
                (let [cycle-path (conj (vec (drop-while #(not= % node) path)) node)]
                  (throw (ex-info (str "Circular dependency detected: " (string/join " -> " cycle-path))
                                  {:cycle-node node
                                   :path path
                                   :cycle cycle-path})))

                (contains? visited node)
                [visited stack]

                :else
                (let [new-visiting (conj visiting node)
                      new-path     (conj path node)
                      deps (filter nodes-set (get relevant-graph node))
                      [visited stack] (reduce (fn [[v s] dep]
                                                (dfs dep v new-visiting new-path s))
                                              [visited stack]
                                              deps)]
                  [(conj visited node) (conj stack node)])))]
      (second (reduce (fn [[visited stack] node]
                        (dfs node visited #{} [] stack))
                      [#{} []]
                      nodes)))))

(defn dependency-seq
  "Returns a sequence of FQCNs representing the dependency closure of `class-like`
   in depth-first order. Restricted to package-local (native) user types.
   Handles cycles by tracking visited nodes."
  [pkg class-like]
  (let [{:keys [fqcn] :as root-node} (if (map? class-like)
                                       class-like
                                       (lookup-class pkg class-like))]
    (if root-node
      (loop [stack   (list fqcn)
             visited #{}
             result  []]
        (if (empty? stack)
          result
          (let [current (peek stack)
                stack   (pop stack)]
            (if (contains? visited current)
              (recur stack visited result)
              (let [deps        (try
                                  (class-package-user-types pkg current)
                                  (catch Exception _ #{}))
                    ;; Sort deps to ensure deterministic order
                    sorted-deps (sort (map str deps))
                    ;; Add deps to stack (reverse to process in order)
                    new-stack   (into stack (reverse sorted-deps))]
                (recur new-stack (conj visited current) (conj result current)))))))
      (throw (ex-info "class not found" {:pkg pkg :class-like class-like})))))

(defn dependency-post-order
  "Returns a sequence of FQCNs representing the dependency closure of `class-like`
   in post-order (leaves first, root last).
   Useful for bottom-up compilation or instantiation."
  [pkg class-like]
  (let [tree (dependency-tree pkg class-like)]
    (letfn [(traverse [node seen]
              (let [[name children] (first node)]
                (if (or (= children :cycle) (contains? seen name))
                  [] ;; Skip cycles or already visited in this path
                  (let [child-seqs (map (fn [[k v]] (traverse {k v} (conj seen name))) children)]
                    (concat (apply concat child-seqs) [name])))))]
      (distinct (traverse tree #{})))))

;; TODO some of these may need foreign bindings
;; Need better more robust way of determining user-types
(def ignored-deps '#{com.google.api.core.ApiFunction
                     com.google.api.gax.core.GoogleCredentialsProvider
                     com.google.api.gax.core.InstantiatingExecutorProvider
                     com.google.api.gax.httpjson.InstantiatingHttpJsonChannelProvider
                     com.google.api.gax.grpc.InstantiatingGrpcChannelProvider
                     com.google.api.gax.paging.AbstractFixedSizeCollection
                     com.google.api.gax.paging.AbstractPage
                     com.google.api.gax.paging.AbstractPagedListResponse
                     com.google.api.gax.retrying.RetryingContext
                     com.google.api.gax.retrying.TimedAttemptSettings
                     com.google.api.gax.retrying.TimedRetryAlgorithm
                     com.google.api.gax.rpc.PageContext
                     com.google.api.gax.rpc.ApiClientHeaderProvider
                     com.google.api.gax.rpc.ClientSettings
                     com.google.api.gax.rpc.OperationCallSettings
                     com.google.api.gax.rpc.PagedCallSettings
                     com.google.api.gax.rpc.TransportChannelProvider
                     com.google.api.gax.rpc.UnaryCallSettings
                     com.google.cloud.ServiceFactory
                     com.google.cloud.ServiceOptions
                     com.google.cloud.ServiceOptions.Builder
                     com.google.cloud.ServiceRpc
                     com.google.cloud.TransportOptions
                     com.google.cloud.http.BaseHttpServiceException
                     com.google.cloud.http.HttpTransportOptions
                     com.google.common.collect.ImmutableSet
                     com.google.common.base.Supplier
                     com.google.longrunning.OperationsClient
                     com.google.protobuf.Descriptors
                     com.google.protobuf.Empty
                     com.google.protobuf.FieldMask
                     com.google.protobuf.Internal
                     com.google.protobuf.Internal.EnumLiteMap})

;; STRICTNESS WARNING:
;; This set defines types that are treated as opaque primitives (no conversion, no bindings).
;; It MUST ONLY contain foundational Java types (java.lang.*, java.util.*) and primitives.
;; DO NOT add Google Cloud types (com.google.cloud.*) to this set to silence "Broken dependency"
;; errors. If a GCP dependency is missing, you must either:
;;   1. Generate the missing package.
;;   2. Add a certified manual binding to gcp.foreign.*.
;;   3. Add a custom mapping in packages.clj.
;; Bypassing architectural gaps by 'fudging' native-type is strictly forbidden.
(def native-type
  #{"int"
    "boolean"
    "long"
    "byte<>"
    "byte"
    "double"
    "float"
    "void"
    "java.lang.Boolean"
    "java.lang.String"
    "java.lang.Integer"
    "java.lang.Long"
    "java.lang.Double"
    "java.lang.Object"
    "java.lang.Void"
    "Map<java.lang.String, java.lang.String>"
    "Map<java.lang.String,java.lang.String>"
    "Map<String,String>"
    "Map<String, String>"
    "java.util.Map<String, String>"
    "java.util.Map<String,String>"
    "java.util.Map<java.lang.String, java.lang.String>"
    "java.util.Map<java.lang.String,java.lang.String>"
    "List<java.lang.String>"
    "List<String>"
    "java.util.List<java.lang.String>"
    "java.util.List<String>"
    "java.math.BigDecimal"
    "java.math.BigInteger"
    "java.sql.ResultSet"
    "java.time.Instant"
    "java.util.Map"
    "java.util.List"
    "java.util.Set"
    "java.util.Iterator"
    "java.util.Optional"
    "java.util.AbstractList"
    "java.lang.Iterable"
    "java.lang.Exception"
    "java.lang.Throwable"
    "java.lang.Error"
    "java.lang.AutoCloseable"
    "com.google.cloud.StringEnumValue"
    "java.nio.charset.Charset"
    "java.io.IOException"})

(defn- extract-all-dependencies [node]
  (let [fields          (:fields node)
        methods         (:methods node)
        ctors           (:constructors node)
        extends         (:extends node)
        implements      (:implements node)
        nested          (:nested node)
        field-deps      (mapcat (fn [f] (extract-type-symbols (:type f))) fields)
        method-deps     (mapcat (fn [m]
                                  (concat (extract-type-symbols (:returnType m))
                                          (mapcat #(extract-type-symbols (:type %)) (:parameters m))))
                                methods)
        ctor-deps       (mapcat (fn [c] (mapcat #(extract-type-symbols (:type %)) (:parameters c))) ctors)

        extends-deps    (if (contains? ignore-extends-categories (:category node))
                          []
                          (mapcat extract-type-symbols extends))
        implements-deps (mapcat extract-type-symbols implements)
        nested-deps     (mapcat extract-all-dependencies nested)
        candidates      (concat field-deps method-deps ctor-deps extends-deps implements-deps nested-deps)]
    (into #{}
          (comp
            (remove #(u/excluded-type-name? (str %)))
            ; (remove (comp str u/native-type))
            (remove ignored-deps))
          candidates)))

(def ^:dynamic *strict-foreign-existence?* false)
(def ^:dynamic *strict-peer-existence?* false)

(defn class-deps
  [{:keys [package-prefixes
           exempt-types
           opaque-types
           prune-dependencies
           custom-namespace-mappings] :as pkg
    :or {exempt-types #{}
         opaque-types #{}}} class-like foreign-mappings recursive?]
  (assert (not-empty package-prefixes))
  (let [{:keys [fqcn] :as node} (lookup-class pkg class-like)
        _  (assert (some? node))
        deps (if prune-dependencies
               (let [pred (into #{} (map symbol) (keys prune-dependencies))]
                 (into (sorted-set) (remove pred) (extract-all-dependencies node))
                 (extract-all-dependencies node)))
        unresolved  (into #{}
                          (filter (fn [dep]
                                    (let [s (str dep)]
                                      (and (not (native-type s))
                                           (not (exempt-types s))
                                           (not (opaque-types s))
                                           (not (string/starts-with? s "com.google.cloud"))
                                           (not (string/starts-with? s "com.google.common"))
                                           (not (u/foreign-binding-exists? (u/infer-foreign-ns s)))))))
                          deps)
        _           (when (not (empty? unresolved))
                      (throw (ex-info (str "unresolved deps for " (:fqcn node)) {:node            node
                                                                                 :unresolved-deps unresolved})))
        peer?       (fn [dep]
                      (let [dep-str (str dep)]
                        (some #(string/starts-with? dep-str %) package-prefixes)))
        calc-deps   (fn [node-or-name]
                      (let [node (if (map? node-or-name)
                                   node-or-name
                                   (lookup-class pkg node-or-name))]
                        (if node
                          (let [nested-classes (into (sorted-set) ;; TODO should be recursive?
                                                 (comp (map :className)
                                                       (map symbol))
                                                 (:nested node))]
                            (reduce (fn [acc dep]
                                      (assert (symbol? dep))
                                      (if (peer? dep)
                                        (if (= fqcn dep)
                                          acc
                                          (update acc :peer conj (symbol dep)))
                                        (if (native-type (str dep))
                                          (update acc :native conj (symbol dep))
                                          (update acc :foreign conj (symbol dep)))))
                                    {:peer (sorted-set) :foreign (sorted-set) :native (sorted-set) :nested nested-classes}
                                    deps))
                          {:peer (sorted-set) :foreign (sorted-set) :native (sorted-set) :nested (sorted-set)})))
        res         (if-not recursive?
                      (calc-deps class-like)
                      (let [all-internal (dependency-seq pkg class-like)]
                        (reduce (fn [acc fqcn]
                                  (let [deps (calc-deps fqcn)]
                                    (-> acc
                                        (update :native into (:native deps))
                                        (update :peer into (:peer deps))
                                        (update :foreign into (:foreign deps))
                                        (update :nested into (map symbol) (:nested deps)))))
                                {:peer (sorted-set) :foreign (sorted-set) :native (sorted-set) :nested (sorted-set)}
                                all-internal)))
        foreign-mappings' (into (sorted-map) (select-keys foreign-mappings (get res :foreign)))
        ; _ (println "foreign-mappings'" foreign-mappings')
        _ (when-not (= (count foreign-mappings') (count (:foreign res)))
            (throw (ex-info (str "missing expected foreign mappings for class " fqcn)
                            (let [known (set (keys foreign-mappings))
                                  expected (:foreign res)
                                  found (into (sorted-set) (keys foreign-mappings'))
                                  missing (clojure.set/difference expected found)]
                              {:missing  missing
                               :missing' (clojure.set/difference missing ignored-deps)
                               :ts (map type missing)
                               "(contains? ignored-deps (first missing)" (contains? ignored-deps (first missing))
                               "(contains? deps (first missing))" (contains? deps (first missing))}))))
        peer-mappings (reduce
                        (fn [acc peer]
                          (if (or (exempt-types (str peer)) (opaque-types (str peer)))
                            acc
                            (if-let [custom (get custom-namespace-mappings (str peer))]
                              (assoc acc peer custom)
                              (assoc acc peer (u/binding-ns (:package-name pkg) package-prefixes peer)))))
                        (sorted-map)
                        (:peer res))]
    (sorted-map
      :native (get res :native)
      :nested (get res :nested)
      :exempt (or exempt-types #{})
      :opaque (or opaque-types #{})
      :foreign-mappings foreign-mappings'
      :peer-mappings peer-mappings)))

; peer?
; (when (and *strict-peer-presence?* (not exists?))
;  (throw (ex-info (str "Peer namespace missing: " ns)
;                  {:type :require :dep dep})))
;
; (and (not cloud?) (not custom?))
; (do
;  (when (and *strict-foreign-presence?* (not exists?))
;    (throw (ex-info (str "Foreign namespace missing: " ns)
;                    {:type :require :dep dep})))
;  (check-certification ns fqcn)))
