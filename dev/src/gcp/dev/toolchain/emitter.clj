(ns gcp.dev.toolchain.emitter
  "Generates Clojure bindings from analyzed Java AST nodes.

   STRICTNESS WARNING:
   This emitter is designed to fail fast and explicitly when dependencies are missing.
   Agents and developers must NOT bypass dependency checks or 'fudge' environmental conditions
   to make the build pass. If a dependency is missing, throw a descriptive ex-info.
   Do not assume existence. Do not hardcode exemptions for missing systems.
   The integrity of the generated bindings depends on the strict verification of the environment."
  (:require
   [clojure.string :as string]
   [gcp.dev.toolchain.analyzer :as ana]
   [gcp.dev.toolchain.malli :as m]
   [gcp.dev.util :as u]
   [zprint.core :as zp]))

(defn- get-deps
  [node custom-mappings opaque-types generated-fqcns]
  (let [current-class (:className node)
        current-pkg (:package node)
        top-level-class (first (string/split current-class #"\."))]
    (->> (:typeDependencies node)
      (remove #(or (u/native-type (str %))
                   (contains? opaque-types (str %))))
      (map (fn [dep]
             (let [dep-str (str dep)]
               (if-let [custom-ns (get custom-mappings dep-str)]
                 {:fqcn dep-str
                  :ns custom-ns
                  :alias (symbol (last (string/split (name custom-ns) #"\.")))
                  :cls (last (string/split dep-str #"\."))
                  :full-class (last (string/split dep-str #"\."))
                  :local? false
                  :cloud? false
                  :custom? true
                  :exists? (u/foreign-binding-exists? custom-ns)}
                 (let [{:keys [package class]} (u/split-fqcn dep-str)
                       class-parts (string/split class #"\.")
                       top-class (first (string/split (first class-parts) #"\$"))
                       is-local (and (= package current-pkg)
                                     (= top-class top-level-class))]
                   (if is-local
                     {:fqcn dep-str
                      :local? true
                      :full-class class}
                     (let [ns-foreign (u/infer-foreign-ns dep-str)
                           ns-binding (symbol (str (u/package-to-ns package) "." top-class))

                           foreign-exists? (u/foreign-binding-exists? ns-foreign)
                           binding-exists? (u/foreign-binding-exists? ns-binding)
                           is-generated?   (contains? generated-fqcns dep-str)

                           ns-sym (cond foreign-exists? ns-foreign
                                        binding-exists? ns-binding
                                        :else ns-binding)

                           alias (if (and (not foreign-exists?) (or binding-exists? is-generated?))
                                   (symbol top-class)
                                   (symbol (last (string/split (name ns-sym) #"\."))))

                           exists? (or foreign-exists? binding-exists? is-generated?)]

                       {:fqcn dep-str
                        :ns ns-sym
                        :alias alias
                        :cls top-class
                        :full-class class
                        :local? false
                        :cloud? (and (not foreign-exists?) (or binding-exists? is-generated?))
                        :exists? exists?})))))))
      (into #{}))))

(defn- needs-protobuf? [node]
  (if-not (map? node)
    false
    (let [field-types (map :type (vals (:fields node)))
          proto-types #{"com.google.protobuf.ByteString"
                        "com.google.protobuf.Duration"
                        "com.google.protobuf.Timestamp"
                        "com.google.protobuf.Struct"
                        "com.google.protobuf.Value"}]
      (boolean (some #(contains? proto-types (str %)) field-types)))))

(defn- collect-all-nested-fqcns [node]
  (let [pkg (:package node)
        main-class (:className node)]
    (letfn [(traverse [n parent-name]
              (let [current-name (str parent-name "$" (:name n))]
                (cons (str pkg "." current-name)
                      (mapcat #(traverse % current-name) (:nested n)))))]
      (mapcat #(traverse % main-class) (:nested node)))))

(defn- check-certification [ns-sym fqcn]
  (let [ns-meta (u/ns-meta ns-sym)]
    (when-not (:gcp.dev/certification ns-meta)
      (throw (ex-info (str "Foreign namespace NOT CERTIFIED: " ns-sym)
                      {:namespace ns-sym :used-by fqcn})))))

(defn- emit-ns-form [node deps metadata]
  (let [ns-name (symbol (str (u/package-to-ns (:package node)) "." (:className node)))
        main-fqcn (str (:package node) "." (:className node))
        nested-fqcns (collect-all-nested-fqcns node)
        imports (map symbol (cons main-fqcn nested-fqcns))
        requires (->> deps
                      (keep (fn [{:keys [ns alias fqcn cloud? exists? custom? local?] :as dep}]
                              (if local?
                                nil
                                (do
                                  (when (and (not cloud?) (not exists?))
                                    (throw (ex-info (str "Foreign namespace missing: " ns)
                                                    {:type :require :dep dep})))
                                  (when (and (not cloud?) (not custom?))
                                    (check-certification ns fqcn))
                                  (when (or (nil? ns) (nil? alias))
                                    (throw (ex-info (str "bad ns dep: '" fqcn "'") dep)))
                                  [ns :as alias]))))
                      (into #{})
                      (sort-by first))]
    `(~'ns ~ns-name
       ~@(when metadata [metadata])
       (:require [gcp.global :as ~'global]
                 ~@requires)
       (:import ~@imports))))

(defn- resolve-conversion [type deps direction]
  (let [is-list (or (and (sequential? type)
                         (let [base (str (first type))]
                           (or (= base "java.util.List")
                               (= base "com.google.common.collect.ImmutableList")
                               (= base "java.lang.Iterable"))))
                    (let [t (str type)]
                      (or (string/starts-with? t "java.util.List")
                          (string/starts-with? t "com.google.common.collect.ImmutableList"))))
        inner-type (if (sequential? type)
                     (second type)
                     (if is-list
                       (let [m (re-find #"<(.+)>" (str type))]
                         (if m (second m) "Object"))
                       type))
        inner-type-str (str inner-type)
        dep (first (filter #(= (:fqcn %) inner-type-str) deps))]
    (cond
      (= (str type) "com.google.protobuf.ProtocolStringList")
      (if (= direction :from-edn) `(~'mapv ~'clojure.core/str) `(~'into []))

      dep
      (let [is-cloud (:cloud? dep)
            is-foreign (not (or is-cloud (:local? dep)))]
        (when (and is-foreign (not (:exists? dep)))
          (throw (ex-info (str "Foreign namespace missing: " (:ns dep))
                          {:type type :dep dep})))

        (when (and is-foreign (not (:custom? dep)))
          (check-certification (:ns dep) (:fqcn dep)))

        (let [func-name-str (cond
                              (:local? dep)
                              (if (string/includes? (:full-class dep) ".")
                                (str (string/replace (:full-class dep) "." "_") ":" (name direction))
                                (name direction))

                              (:custom? dep)
                              (name direction)

                              is-foreign
                              (str (:cls dep) "-" (name direction))

                              :else ;; is-cloud
                              (if (string/includes? (:full-class dep) ".")
                                (str (string/replace (:full-class dep) "." "_") ":" (name direction))
                                (name direction)))
              func-sym (if (:local? dep)
                         (symbol func-name-str)
                         (symbol (str (:alias dep)) func-name-str))]

          (when is-foreign
            (let [vars (u/foreign-vars (:ns dep))
                  bare-sym (symbol func-name-str)]
              (when-not (contains? vars bare-sym)
                (throw (ex-info (str "Foreign function missing: " bare-sym " in " (:ns dep))
                                {:type type :dep dep :fn bare-sym})))))

          (if is-list
            (if (= direction :from-edn)
              `(~'mapv ~func-sym)
              `(~'map ~func-sym))
            func-sym)))

      (or (= inner-type-str "float") (= inner-type-str "Float"))
      (if (= direction :to-edn) `double `identity)

      :else
      (if is-list
        (if (or (= inner-type-str "java.lang.String")
                (= inner-type-str "String"))
          (if (= direction :from-edn) `(~'mapv ~'clojure.core/str) `(~'into []))
          (if (= direction :from-edn) `vec `vec))
        `identity))))

(defn- emit-accessor-from-edn [node deps version prefix class-sym-name func-name-base]
  (let [class-name (:className node)
        func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'from-edn)
        class-sym (symbol class-sym-name)
        func-name-tagged (symbol (str func-name "__TAG__" class-sym))
        builder-method (symbol (str class-sym "/newBuilder"))
        fields (:fields node)
        builder-calls (for [[field-name {:keys [setterMethod type]}] fields]
                        (let [setter (symbol (str "." setterMethod))
                              conversion (resolve-conversion type deps :from-edn)
                              val-sym (gensym "v")]
                          `(~'when-some [~val-sym (~'get ~'arg ~(keyword field-name))]
                             (~setter ~'builder
                               ~(if (sequential? conversion)
                                  `(~@conversion (~'global/to-vec ~val-sym))
                                  (if (= conversion `identity)
                                    val-sym
                                    `(~conversion ~val-sym)))))))]
    `(~'defn ~func-name-tagged [~'arg]
       ~@(when-not prefix [`(~'global/strict! ~(u/schema-key (:package node) (:className node)) ~'arg)])
       (~'let [~'builder (~builder-method)]
         ~@builder-calls
         (.build ~'builder)))))

(defn- emit-accessor-to-edn [node deps version prefix class-sym-name func-name-base]
  (let [class-name (:className node)
        func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'to-edn)
        class-sym (symbol class-sym-name)
        func-name-tagged (symbol (str func-name "__ARGTAG__" class-sym))
        fields (:fields node)
        assoc-steps (for [[field-name {:keys [getterMethod type]}] fields]
                      (let [getter (symbol (str "." getterMethod))
                            k (keyword field-name)
                            conversion (resolve-conversion type deps :to-edn)]
                        `(~'true
                           (~'assoc ~k
                                    ~(if (= conversion `identity)
                                       `(~getter ~'arg)
                                       (if (sequential? conversion)
                                         `(~@conversion (~getter ~'arg))
                                         `(~conversion (~getter ~'arg))))))))]
    `(~'defn ~func-name-tagged [~'arg]
       ~@(when-not prefix [`{:post [(~'global/strict! ~(u/schema-key (:package node) (:className node)) ~'%)]}])
       (cond-> {}
         ~@(mapcat identity assoc-steps)))))

(defn- emit-enum-from-edn [node version prefix class-sym-name func-name-base]
  (let [class-name (:className node)
        func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'from-edn)
        class-sym (symbol class-sym-name)
        func-name-tagged (symbol (str func-name "__TAG__" class-sym))]
    `(~'defn ~func-name-tagged [~'arg]
       (~(symbol (str class-sym "/valueOf")) (clojure.core/name ~'arg)))))

(defn- emit-enum-to-edn [node version prefix class-sym-name func-name-base]
  (let [class-name (:className node)
        func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'to-edn)
        class-sym (symbol class-sym-name)
        func-name-tagged (symbol (str func-name "__ARGTAG__" class-sym))]
    `(~'defn ~func-name-tagged [~'arg]
       (.name ~'arg))))

(defn- camel-to-screaming-snake [s]
  (-> s
      (string/replace #"([a-z])([A-Z])" "$1_$2")
      string/upper-case))

(defn- emit-union-factory-from-edn [node deps version prefix class-sym-name func-name-base]
  (let [class-name (:className node)
        func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'from-edn)
        class-sym (symbol class-sym-name)
        factories (:factories node)
        getters (:getters node)

        sample-factory (first factories)
        param-name (-> sample-factory :parameters first :name)
        param-type (-> sample-factory :parameters first :type)

        matching-getter (first (filter #(= (str (:returnType %)) (str param-type)) getters))
        prop-name (if matching-getter
                    (u/property-name (:name matching-getter))
                    param-name)

        branches (mapcat (fn [f]
                           (let [enum-val (camel-to-screaming-snake (:name f))
                                 method-sym (symbol (str class-sym "/" (:name f)))]
                             `[(= ~enum-val (~'get ~'arg :type))
                               (~method-sym (~'get ~'arg ~(keyword prop-name)))]))
                         factories)
        func-name-tagged (symbol (str func-name "__TAG__" class-sym))]
    `(~'defn ~func-name-tagged [~'arg]
       ~@(when-not prefix [`(~'global/strict! ~(u/schema-key (:package node) (:className node)) ~'arg)])
       (cond
         ~@branches
         :else (throw (ex-info "Unknown type" {:arg ~'arg}))))))

(defn- emit-union-factory-to-edn [node deps version prefix class-sym-name func-name-base]
  (let [class-name (:className node)
        func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'to-edn)
        class-sym (symbol class-sym-name)
        func-name-tagged (symbol (str func-name "__ARGTAG__" class-sym))
        getters (:getters node)
        getType (symbol ".getType")
        other-getters (remove #(= (:name %) "getType") getters)
        t-enum-sym (gensym "t-enum")
        t-str-sym (gensym "t-str")]

    `(~'defn ~func-name-tagged [~'arg]
       ~@(when-not prefix [`{:post [(~'global/strict! ~(u/schema-key (:package node) (:className node)) ~'%)]}])
       (let [~t-enum-sym (~getType ~'arg)
             ~t-str-sym (when ~t-enum-sym (.name ~t-enum-sym))]
         (cond-> {:type ~t-str-sym}
           ~@(mapcat (fn [m]
                       (let [pname (u/property-name (:name m))
                             msym (symbol (str "." (:name m)))]
                         `[(~msym ~'arg) (~'assoc ~(keyword pname) (~msym ~'arg))]))
                     other-getters))))))

(defn- schema-var-name [class-name]
  (symbol (str (string/replace class-name "." "_") "-schema")))

(defn- emit-schema [node version]
  (let [class-name (:className node)
        key (u/schema-key (:package node) class-name)
        malli-schema (m/->schema node version)
        schema-def `[:and
                     {:doc ~(u/clean-doc (:doc node))
                      :class (quote ~(symbol (str (:package node) "." class-name)))}
                     ~malli-schema]]
    `(~'def ~(schema-var-name class-name) ~schema-def)))

(defn- collect-registry-entries [node parent-class-name]
  (let [ana-node (if (:type node) node (ana/analyze-class-node node))
        full-class-name (if parent-class-name
                          (str parent-class-name "." (:className ana-node))
                          (:className ana-node))
        ana-node (assoc ana-node :className full-class-name)

        entries (if (not= (:type ana-node) :builder)
                  [[ (u/schema-key (:package ana-node) full-class-name)
                     (schema-var-name full-class-name) ]]
                  [])
        nested-entries (mapcat #(collect-registry-entries % full-class-name) (:nested ana-node))]
    (concat entries nested-entries)))

(defn- emit-all-nested-forms [parent-node deps version main-class-name custom-mappings opaque-types generated-fqcns]
  (mapcat (fn [n]
            (let [ana-n (ana/analyze-class-node n)
                  nested-name (:name n)
                  current-full-name (str (:className parent-node) "." nested-name)
                  ana-n (assoc ana-n :className current-full-name)
                  nested-class-sym (string/replace current-full-name "." "$")
                  current-deps (get-deps ana-n custom-mappings opaque-types generated-fqcns)]
              (remove nil?
                (concat
                  (emit-all-nested-forms ana-n current-deps version main-class-name custom-mappings opaque-types generated-fqcns)
                  (case (:type ana-n)
                    :accessor [(emit-accessor-from-edn ana-n current-deps version ":from-edn" nested-class-sym current-full-name)
                               (emit-accessor-to-edn ana-n current-deps version ":to-edn" nested-class-sym current-full-name)]
                    :enum [(emit-enum-from-edn ana-n version ":from-edn" nested-class-sym current-full-name)
                           (emit-enum-to-edn ana-n version ":to-edn" nested-class-sym current-full-name)]
                    :concrete-union [(emit-accessor-from-edn ana-n current-deps version ":from-edn" nested-class-sym current-full-name)
                                     (emit-accessor-to-edn ana-n current-deps version ":to-edn" nested-class-sym current-full-name)]
                    :union-factory [(emit-union-factory-from-edn ana-n current-deps version ":from-edn" nested-class-sym current-full-name)
                                    (emit-union-factory-to-edn ana-n current-deps version ":to-edn" nested-class-sym current-full-name)]
                    nil)
                  (when-not (= (:type ana-n) :builder)
                    [(emit-schema ana-n version)])))))
          (:nested parent-node)))

(defn- get-resource-deps [node custom-mappings opaque-types generated-fqcns]
  (let [extends-syms (into #{}
                           (map (fn [x]
                                  (let [s (str (if (sequential? x) (first x) x))]
                                    (symbol (last (string/split s #"\."))))))
                           (:extends node))
        all-deps (:typeDependencies node)
        ;; Filter deps that match the extends symbols (simple name match)
        relevant-deps (filter (fn [dep-fqcn]
                                (let [simple (last (string/split (str dep-fqcn) #"\."))]
                                  (contains? extends-syms (symbol simple))))
                              all-deps)
        ;; Create a synthetic node with filtered dependencies for get-deps
        synth-node (assoc node :typeDependencies relevant-deps)]
    (get-deps synth-node custom-mappings opaque-types generated-fqcns)))

(defn- emit-resource-delegation [node deps class-name]
  (let [super-dep (first deps)
        super-alias (:alias super-dep)
        class-sym (symbol class-name)]
    (when-not super-alias
      (throw (ex-info "Resource class must have a superclass dependency" {:node node :deps deps})))
    [`(~'defn ~'from-edn {:tag ~class-sym} [~'arg]
        (~(symbol (str super-alias "/from-edn")) ~'arg))

     `(~'defn ~'to-edn [~(with-meta 'arg {:tag class-sym})]
        (~(symbol (str super-alias "/to-edn")) ~'arg))]))

(defn compile-class-forms
  "Compiles a class node into a sequence of Clojure forms.
   Accepts optional metadata map to inject into the namespace declaration."
  ([node] (compile-class-forms node nil))
  ([node metadata]
   (let [custom-mappings (:gcp.dev/custom-mappings metadata)
         opaque-types (set (:gcp.dev/opaque-types metadata))
         generated-fqcns (set (:gcp.dev/generated-fqcns metadata))
         version (u/extract-version (:doc node))
         type (:type node)
         deps (if (= type :resource-extended-class)
                (get-resource-deps node custom-mappings opaque-types generated-fqcns)
                (get-deps node custom-mappings opaque-types generated-fqcns))
         ns-form (emit-ns-form node deps metadata)
         main-class-name (:className node)
         nested-forms (emit-all-nested-forms node deps version main-class-name custom-mappings opaque-types generated-fqcns)
         [from-edn to-edn] (if (= type :resource-extended-class)
                             (emit-resource-delegation node deps main-class-name)
                             [(case type
                                :accessor (emit-accessor-from-edn node deps version nil main-class-name main-class-name)
                                :enum (emit-enum-from-edn node version nil main-class-name main-class-name)
                                :concrete-union (emit-accessor-from-edn node deps version nil main-class-name main-class-name)
                                :abstract-union (emit-accessor-from-edn node deps version nil main-class-name main-class-name)
                                :union-factory (emit-union-factory-from-edn node deps version nil main-class-name main-class-name)
                                nil)
                              (case type
                                :accessor (emit-accessor-to-edn node deps version nil main-class-name main-class-name)
                                :enum (emit-enum-to-edn node version nil main-class-name main-class-name)
                                :concrete-union (emit-accessor-to-edn node deps version nil main-class-name main-class-name)
                                :abstract-union (emit-accessor-to-edn node deps version nil main-class-name main-class-name)
                                :union-factory (emit-union-factory-to-edn node deps version nil main-class-name main-class-name)
                                nil)])
         schema (emit-schema node version)
         registry-entries (collect-registry-entries node nil)
         registry-map (into (sorted-map) registry-entries)
         registry-form `(~'global/include-schema-registry!
                          (~'with-meta ~registry-map {:gcp.global/name (~'str ~'*ns*)}))]
     (remove nil? (concat [ns-form] nested-forms [from-edn to-edn schema registry-form])))))

(defn compile-class
  "Compiles a class node into a formatted string string.
   Accepts optional metadata map."
  ([node] (compile-class node nil))
  ([node metadata]
   (let [preamble ";; THIS FILE IS GENERATED; DO NOT EDIT\n"
         forms (compile-class-forms node metadata)
         class-name (:className node)
         fix-hints (fn [src]
                     (-> src
                         ;; (defn func__TAG__Type ...) -> (defn ^Type func ...)
                         (string/replace #"(defn\s+)([\w:\-\.]+)(?:__TAG__)([\w\$\.]+)"
                                         (fn [[_ d1 func type]] (str d1 "^" type " " func)))
                         ;; (defn func__ARGTAG__Type [arg] ...) -> (defn func [^Type arg] ...)
                         (string/replace #"(defn\s+)([\w:\-\.]+)(?:__ARGTAG__)([\w\$\.]+)(\s*\[\s*)arg"
                                         (fn [[_ d1 func type d2]] (str d1 func d2 "^" type " arg")))))]
     (str preamble
          (string/join "\n\n"
                       (map (comp fix-hints zp/zprint-str) forms))))))
