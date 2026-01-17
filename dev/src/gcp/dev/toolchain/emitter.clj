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
   [gcp.dev.toolchain.shared :as shared]
   [gcp.dev.util :as u]
   [zprint.core :as zp]))

(def ^:dynamic *strict-foreign-presence?*
  "If true, throws an exception when a foreign (external library) dependency is missing."
  true)

(def ^:dynamic *strict-peer-presence?*
  "If true, throws an exception when a peer (same library, different file) dependency is missing."
  false)

(defn get-deps
  [node custom-mappings opaque-types generated-fqcns]
  (let [current-class (:className node)
        current-pkg (:package node)
        foreign-mappings (:foreign-mappings node)
        top-level-class (first (string/split current-class #"\."))
        nested-enums (into {}
                           (keep (fn [n]
                                   (when (#{:nested/enum :nested/string-enum} (:category n))
                                     [(:fqcn n) n])))
                           (:nested node))]
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
                  :class-local? false
                  :peer? false
                  :cloud? false
                  :custom? true
                  :exists? (u/foreign-binding-exists? custom-ns)}
                 (let [foreign-ns (get foreign-mappings dep-str)
                       {:keys [package class]} (u/split-fqcn dep-str)
                       class-parts (string/split class #"\.")
                       top-class (first (string/split (first class-parts) #"\$"))
                       is-class-local (and (= package current-pkg)
                                           (= top-class top-level-class))
                       is-peer (and (= package current-pkg) (not is-class-local))]
                   (if is-class-local
                     {:fqcn dep-str
                      :class-local? true
                      :peer? false
                      :full-class class
                      :nested-enum? (contains? nested-enums dep-str)
                      :nested-enum-node (get nested-enums dep-str)}
                     (let [ns-foreign (get foreign-mappings dep-str)
                           _ (when (and (nil? ns-foreign)
                                        (not (string/starts-with? dep-str "com.google.cloud")))
                               (throw (ex-info (str "Foreign type not found in mappings: " dep-str)
                                               {:type dep-str})))

                           ns-binding (symbol (str (u/package-to-ns package) "." top-class))

                           foreign-exists? (and ns-foreign (u/foreign-binding-exists? ns-foreign))
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
                        :class-local? false
                        :peer? is-peer
                        :cloud? (and (not foreign-exists?) (or binding-exists? is-generated?))
                        :exists? exists?})))))))
      (into #{}))))

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

(defn- emit-ns-form
  [node deps metadata]
  (let [ns-name (symbol (str (u/package-to-ns (:package node)) "." (:className node)))
        main-fqcn (str (:package node) "." (:className node))
        nested-fqcns (collect-all-nested-fqcns node)
        imports (map symbol (cons main-fqcn nested-fqcns))
        requires (->> deps
                      (keep (fn [{:keys [ns alias fqcn cloud? exists? custom? class-local? peer?] :as dep}]
                              (if class-local?
                                nil
                                (do
                                  (cond
                                    peer?
                                    (when (and *strict-peer-presence?* (not exists?))
                                      (throw (ex-info (str "Peer namespace missing: " ns)
                                                      {:type :require :dep dep})))

                                    (and (not cloud?) (not custom?))
                                    (do
                                      (when (and *strict-foreign-presence?* (not exists?))
                                        (throw (ex-info (str "Foreign namespace missing: " ns)
                                                        {:type :require :dep dep})))
                                      (check-certification ns fqcn)))

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

(defn- resolve-conversion
  [type deps direction]
  (let [is-list (or (and (sequential? type)
                         (let [base (str (first type))]
                           (or (= base "java.util.List")
                               (= base "com.google.common.collect.ImmutableList")
                               (= base "java.lang.Iterable"))))
                    (let [t (str type)]
                      (or (string/starts-with? t "java.util.List")
                          (string/starts-with? t "com.google.common.collect.ImmutableList"))))
        is-map (or (and (sequential? type)
                        (let [base (str (first type))]
                          (or (= base "java.util.Map")
                              (= base "com.google.common.collect.ImmutableMap"))))
                   (let [t (str type)]
                     (or (string/starts-with? t "java.util.Map")
                         (string/starts-with? t "com.google.common.collect.ImmutableMap"))))
        inner-type (if (sequential? type)
                     (if is-map (last type) (second type))
                     (cond
                       is-list
                       (let [m (re-find #"<(.+)>" (str type))]
                         (if m (second m) "Object"))
                       is-map
                       (let [m (re-find #",\s*(.+)>" (str type))]
                         (if m (second m) "Object"))
                       :else type))
        inner-type-str (str inner-type)
        dep (first (filter #(= (:fqcn %) inner-type-str) deps))]
    (cond
      (= (str type) "com.google.protobuf.ProtocolStringList")
      (if (= direction :from-edn) `(~'mapv ~'clojure.core/str) `(~'into []))
      dep
      (let [is-cloud (:cloud? dep)
            is-peer (:peer? dep)
            is-nested-enum (:nested-enum? dep)
            is-foreign (not (or is-cloud (:class-local? dep) is-peer))]
        (if is-nested-enum
          (let [cls-sym (symbol (string/replace (:full-class dep) "." "$"))]
            (if (= direction :from-edn)
              `(~(symbol (str cls-sym "/valueOf")))
              `(.name)))
          (do
            (cond
              is-peer
              (when (and *strict-peer-presence?* (not (:exists? dep)))
                (throw (ex-info (str "Peer namespace missing: " (:ns dep))
                                {:type type :dep dep})))
              is-foreign
              (when (and *strict-foreign-presence?* (not (:exists? dep)))
                (throw (ex-info (str "Foreign namespace missing: " (:ns dep))
                                {:type type :dep dep}))))
            (when (and is-foreign (not (:custom? dep)))
              (check-certification (:ns dep) (:fqcn dep)))
            (let [func-name-str (cond
                                  (:class-local? dep)
                                  (if (string/includes? (:full-class dep) ".")
                                    (str (string/replace (:full-class dep) "." "_") ":" (name direction))
                                    (name direction))

                                  (:custom? dep)
                                  (name direction)

                                  is-foreign
                                  (str (:cls dep) "-" (name direction))

                                  :else ;; is-cloud or peer
                                  (if (string/includes? (:full-class dep) ".")
                                    (str (string/replace (:full-class dep) "." "_") ":" (name direction))
                                    (name direction)))
                  func-sym (if (:class-local? dep)
                             (symbol func-name-str)
                             (symbol (str (:alias dep)) func-name-str))]

              (when (and is-foreign *strict-foreign-presence?*)
                (let [vars (u/foreign-vars (:ns dep))
                      bare-sym (symbol func-name-str)]
                  (when-not (contains? vars bare-sym)
                    (throw (ex-info (str "Foreign function missing: " bare-sym " in " (:ns dep))
                                    {:type type :dep dep :fn bare-sym})))))
              (cond
                is-list
                (if (= direction :from-edn)
                  `(~'mapv ~func-sym)
                  `(~'map ~func-sym))

                is-map
                (if (= direction :from-edn)
                  `((~'fn [v#] (clojure.core/update-vals v# ~func-sym)))
                  `((~'fn [v#] (clojure.core/update-vals v# ~func-sym))))

                :else
                func-sym)))))
      (or (= inner-type-str "float") (= inner-type-str "Float"))
      (if (= direction :to-edn) `double `identity)
      :else
      (cond
        is-list
        (let [inner-conv (resolve-conversion inner-type deps direction)]
          (if (= inner-conv `identity)
            (if (or (= inner-type-str "java.lang.String")
                    (= inner-type-str "String"))
              (if (= direction :from-edn) `(~'mapv ~'clojure.core/str) `(~'into []))
              (if (= direction :from-edn) `vec `vec))
            (if (= direction :from-edn)
              `(~'mapv ~inner-conv)
              `(~'map ~inner-conv))))

        is-map
        (let [inner-conv (resolve-conversion inner-type deps direction)]
          (if (= inner-conv `identity)
            (if (= direction :from-edn) `identity `(~'into {}))
            (if (= direction :from-edn)
              `((~'fn [v#] (clojure.core/update-vals v# ~inner-conv)))
              `((~'fn [v#] (clojure.core/update-vals v# ~inner-conv))))))

        :else
        `identity))))

(defn- pair-guards
  [fields]
  (let [guards (filter (fn [[k v]] (string/starts-with? k "has")) fields)
        targets (into {} (remove (fn [[k v]] (string/starts-with? k "has")) fields))]
    (reduce (fn [acc [g-name g-info]]
              (let [suffix (subs g-name 3)
                    matches (filter (fn [[t-name t-info]]
                                      (string/ends-with? (string/lower-case t-name) (string/lower-case suffix)))
                                    targets)]
                (cond
                  (empty? matches) (throw (ex-info (str "Orphan guard: " g-name) {:guard g-name :fields (keys fields)}))
                  (> (count matches) 1) (throw (ex-info (str "Ambiguous guard: " g-name) {:guard g-name :matches (keys matches)}))
                  :else (assoc acc (first (first matches)) g-name))))
            {}
            guards)))

(defn- emit-read-only-from-edn
  [node version prefix class-sym-name func-name-base]
  (let [func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'from-edn)
        class-sym (symbol class-sym-name)
        func-name-tagged (symbol (str func-name "__TAG__" class-sym))]
    `(~'defn ~func-name-tagged [~'arg]
       (throw (ex-info (str "Class " ~class-sym-name " is read-only") {:class ~class-sym-name})))))

(defn- emit-accessor-from-edn
  [node deps version prefix class-sym-name func-name-base]
  (let [func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'from-edn)
        class-sym (symbol class-sym-name)
        func-name-tagged (symbol (str func-name "__TAG__" class-sym))
        fields (:fields node)
        newBuilder (:newBuilder node)
        nb-params (:parameters newBuilder)
        nb-param-names (mapv :name nb-params)
        nb-param-set (set nb-param-names)
        ;; Arguments for the newBuilder call
        nb-args (for [p nb-params]
                  (let [pname (:name p)
                        ptype (:type p)
                        conversion (resolve-conversion ptype deps :from-edn)
                        val-form `(~'get ~'arg ~(keyword pname))]
                    (if (= conversion `identity)
                      val-form
                      (if (sequential? conversion)
                        `(~@conversion ~val-form)
                        `(~conversion ~val-form)))))
        builder-method (symbol (str class-sym "/newBuilder"))
        ;; Setter calls for fields NOT in newBuilder params
        builder-calls (for [[field-name {:keys [setterMethod type]}] fields
                            :when (and setterMethod (not (contains? nb-param-set (name field-name))))]
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
       (~'let [~'builder (~builder-method ~@nb-args)]
         ~@builder-calls
         (.build ~'builder)))))

(defn- emit-accessor-to-edn
  [node deps version prefix class-sym-name func-name-base]
  (let [func-name        (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'to-edn)
        class-sym        (symbol class-sym-name)
        func-name-tagged (symbol (str func-name "__ARGTAG__" class-sym))
        fields           (:fields node)
        guard-map        (pair-guards fields)
        assoc-steps      (for [[field-name {:keys [getterMethod type]}] fields]
                           (if (string/starts-with? field-name "has")
                             nil
                             (let [getter     (symbol (str "." getterMethod))
                                   k          (keyword field-name)
                                   conversion (resolve-conversion type deps :to-edn)
                                   guard-name (get guard-map field-name)
                                   check-form (if guard-name
                                                `(~(symbol (str "." (get-in fields [guard-name :getterMethod]))) ~'arg)
                                                `(~getter ~'arg))]
                               [check-form
                                `(~'assoc ~k
                                   ~(if (= conversion `identity)
                                      `(~getter ~'arg)
                                      (if (sequential? conversion)
                                        `(~@conversion (~getter ~'arg))
                                        `(~conversion (~getter ~'arg)))))])))]
    `(~'defn ~func-name-tagged [~'arg]
       ~@(when-not prefix [`{:post [(~'global/strict! ~(u/schema-key (:package node) (:className node)) ~'%)]}])
       (cond-> {}
         ~@(mapcat identity assoc-steps)))))

(defn- emit-concrete-union-from-edn
  [node deps version prefix class-sym-name func-name-base]
  (let [class-name       (:className node)
        current-fqcn     (:fqcn node)
        func-name        (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'from-edn)
        class-sym        (symbol class-sym-name)
        func-name-tagged (symbol (str func-name "__TAG__" class-sym))
        variants         (:variants node)
        branches         (reduce (fn [acc [t-val {:keys [factory returnType]}]]
                                   (let [is-complex (not= returnType current-fqcn)
                                         {:keys [package class]} (u/split-fqcn returnType)]
                                     (if is-complex
                                       (conj acc t-val `(~(symbol class "from-edn") ~'arg))
                                       (conj acc t-val `(~(symbol (str class-sym "/" factory)))))))
                                 []
                                 (into (sorted-map) (:variants node)))]
    `(~'defn ~func-name-tagged [~'arg]
       ~@(when-not prefix [`(~'global/strict! ~(u/schema-key (:package node) (:className node)) ~'arg)])
       (~'case (get ~'arg :type)
         ~@branches))))

(defn- emit-concrete-union-to-edn
  [node deps version prefix class-sym-name func-name-base]
  (let [current-fqcn     (:fqcn node)
        func-name        (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'to-edn)
        class-sym        (symbol class-sym-name)
        func-name-tagged (symbol (str func-name "__ARGTAG__" class-sym))
        branches         (reduce (fn [acc [t-val {:keys [returnType]}]]
                                   (let [{:keys [class]} (u/split-fqcn returnType)]
                                     (if (not= returnType current-fqcn)
                                       (conj acc t-val `(~(symbol class "to-edn") ~'arg))
                                       (conj acc t-val {:type t-val}))))
                                 []
                                 (into (sorted-map) (:variants node)))]
    `(~'defn ~func-name-tagged [~'arg]
       ~@(when-not prefix [`{:post [(~'global/strict! ~(u/schema-key (:package node) (:className node)) ~'%)]}])
       (~'case (.getType ~'arg)
         ~@branches))))

(defn- emit-enum-from-edn
  [node version prefix class-sym-name func-name-base]
  (let [func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'from-edn)
        class-sym (symbol class-sym-name)
        func-name-tagged (symbol (str func-name "__TAG__" class-sym))]
    `(~'defn ~func-name-tagged [~'arg]
       (~(symbol (str class-sym "/valueOf")) (clojure.core/name ~'arg)))))

(defn- emit-enum-to-edn
  [node version prefix class-sym-name func-name-base]
  (let [func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'to-edn)
        class-sym (symbol class-sym-name)
        func-name-tagged (symbol (str func-name "__ARGTAG__" class-sym))]
    `(~'defn ~func-name-tagged [~'arg]
       (.name ~'arg))))

(defn- camel-to-screaming-snake [s]
  (-> s
      (string/replace #"([a-z])([A-Z])" "$1_$2")
      string/upper-case))

(defn- emit-union-factory-from-edn
  [node deps version prefix class-sym-name func-name-base]
  (let [func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'from-edn)
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
                             `[~enum-val (~method-sym (~'get ~'arg ~(keyword prop-name)))]))
                         factories)
        func-name-tagged (symbol (str func-name "__TAG__" class-sym))]
    `(~'defn ~func-name-tagged [~'arg]
       ~@(when-not prefix [`(~'global/strict! ~(u/schema-key (:package node) (:className node)) ~'arg)])
       (case (~'get ~'arg :type)
         ~@branches
         (throw (ex-info "Unknown type" {:arg ~'arg}))))))

(defn- emit-union-factory-to-edn
  [node deps version prefix class-sym-name func-name-base]
  (let [func-name        (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'to-edn)
        class-sym        (symbol class-sym-name)
        func-name-tagged (symbol (str func-name "__ARGTAG__" class-sym))
        getters          (:getters node)
        getType          (symbol ".getType")
        other-getters    (remove #(= (:name %) "getType") getters)

        nullable?        (fn [m] (some #(let [n (:name %)]
                                          (or (= n "Nullable")
                                              (string/ends-with? n ".Nullable")))
                                       (:annotations m)))

        [optional-getters required-getters]
        (reduce (fn [[opt req] m]
                  (if (nullable? m)
                    [(conj opt m) req]
                    [opt (conj req m)]))
                [[] []]
                other-getters)

        base-map (assoc (into {}
                              (map (fn [m]
                                     [(keyword (u/property-name (:name m)))
                                      `(~(symbol (str "." (:name m))) ~'arg)]))
                              required-getters)
                        :type `(.name (~getType ~'arg)))]
    `(~'defn ~func-name-tagged [~'arg]
       ~@(when-not prefix [`{:post [(~'global/strict! ~(u/schema-key (:package node) (:className node)) ~'%)]}])
       ~(if (empty? optional-getters)
          base-map
          `(cond-> ~base-map
             ~@(mapcat (fn [m]
                         (let [pname (u/property-name (:name m))
                               msym (symbol (str "." (:name m)))]
                           `[(~msym ~'arg) (~'assoc ~(keyword pname) (~msym ~'arg))]))
                       optional-getters))))))

(defn- emit-static-factory-from-edn
  [node deps version prefix class-sym-name func-name-base]
  (let [func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'from-edn)
        class-sym (symbol class-sym-name)
        func-name-tagged (symbol (str func-name "__TAG__" class-sym))

        ;; Use factory methods identified by analyzer
        factory-methods (:factoryMethods node)
        ;; Prefer 'of' methods
        of-methods (filter #(= (:name %) "of") factory-methods)
        target-methods (if (seq of-methods) of-methods factory-methods)

        ;; Determine required params (intersection)
        param-names-seq (map (fn [m] (set (map :name (:parameters m)))) target-methods)
        required-params (if (seq param-names-seq)
                          (apply clojure.set/intersection param-names-seq)
                          #{})

        ;; Sort descending by param count to match most specific first
        sorted-methods (sort-by #(count (:parameters %)) > target-methods)]

    `(~'defn ~func-name-tagged [~'arg]
       ~@(when-not prefix [`(~'global/strict! ~(u/schema-key (:package node) (:className node)) ~'arg)])
       (cond
         ~@(mapcat (fn [method]
                     (let [params (:parameters method)
                           ;; Identify params that are NOT required (the discriminators)
                           discriminators (remove #(contains? required-params (:name %)) params)
                           ;; Condition checks only discriminators
                           condition (cond
                                       (empty? discriminators) true
                                       (= 1 (count discriminators)) `(~'get ~'arg ~(keyword (:name (first discriminators))))
                                       :else `(and ~@(map (fn [p] `(~'get ~'arg ~(keyword (:name p)))) discriminators)))
                           call `(~(symbol (str class-sym "/" (:name method)))
                                   ~@(map (fn [p]
                                            (let [pname (:name p)
                                                  ptype (:type p)
                                                  conversion (resolve-conversion ptype deps :from-edn)
                                                  val-form `(~'get ~'arg ~(keyword pname))]
                                              (if (= conversion `identity)
                                                val-form
                                                (if (sequential? conversion)
                                                  `(~@conversion ~val-form)
                                                  `(~conversion ~val-form)))))
                                          params))]
                       [condition call]))
                   sorted-methods)))))

(defn- emit-static-factory-to-edn
  [node deps version prefix class-sym-name func-name-base]
  (let [func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'to-edn)
        class-sym (symbol class-sym-name)
        func-name-tagged (symbol (str func-name "__ARGTAG__" class-sym))
        fields (:fields node)
        ;; Split into required and optional fields
        [required-fields optional-fields] (reduce (fn [[req opt] [k v]]
                                                    (if (and (:getterMethod v) (:required? v))
                                                      [(conj req [k v]) opt]
                                                      (if (:getterMethod v)
                                                        [req (conj opt [k v])]
                                                        [req opt])))
                                                  [[] []]
                                                  fields)
        base-map (into {}
                       (for [[field-name {:keys [getterMethod type]}] required-fields]
                         (let [getter (symbol (str "." getterMethod))
                               k (keyword field-name)
                               conversion (resolve-conversion type deps :to-edn)]
                           [k (if (= conversion `identity)
                                `(~getter ~'arg)
                                (if (sequential? conversion)
                                  `(~@conversion (~getter ~'arg))
                                  `(~conversion (~getter ~'arg))))])))
        opt-steps (for [[field-name {:keys [getterMethod type]}] optional-fields]
                    (let [getter (symbol (str "." getterMethod))
                          k (keyword field-name)
                          conversion (resolve-conversion type deps :to-edn)]
                      [`(~getter ~'arg)
                       `(~'assoc ~k
                          ~(if (= conversion `identity)
                             `(~getter ~'arg)
                             (if (sequential? conversion)
                               `(~@conversion (~getter ~'arg))
                               `(~conversion (~getter ~'arg)))))]))]
    `(~'defn ~func-name-tagged [~'arg]
       ~@(when-not prefix [`{:post [(~'global/strict! ~(u/schema-key (:package node) (:className node)) ~'%)]}])
       ~(if (empty? optional-fields)
          base-map
          `(cond-> ~base-map
             ~@(mapcat identity opt-steps))))))

(defn- schema-var-name [class-name]
  (symbol (str (string/replace class-name "." "_") "-schema")))

(defn- emit-schema [node version]
  (let [class-name (:className node)
        key (u/schema-key (:package node) class-name)
        malli-schema (m/->schema node version)]
    `(~'def ~(schema-var-name class-name) ~malli-schema)))

(defn- collect-registry-entries
  [node parent-class-name]
  (let [full-class-name (if parent-class-name
                          (str parent-class-name "." (:name node))
                          (:className node))
        ana-node (assoc node :className full-class-name)
        entries (if (not (or (= (:category ana-node) :builder)
                             (= (:category ana-node) :nested/enum)
                             (= (:category ana-node) :nested/string-enum)
                             (= (:category ana-node) :nested/builder)
                             (= (:category ana-node) :nested/factory)
                             (= (:category ana-node) :nested/client)
                             (= (:category ana-node) :factory)
                             (= (:category ana-node) :client)))
                  [[(u/schema-key (:package ana-node) full-class-name)
                    (schema-var-name full-class-name)]]
                  [])
        nested-entries (mapcat #(collect-registry-entries % full-class-name) (:nested ana-node))]
    (concat entries nested-entries)))

(defn- emit-all-nested-forms [parent-node deps version main-class-name custom-mappings opaque-types generated-fqcns]
  (let [foreign-mappings (:foreign-mappings parent-node)]
    (mapcat (fn [n]
              (let [n (assoc n :foreign-mappings foreign-mappings)
                    ana-n (ana/analyze-class-node n)
                    nested-name (:name n)
                    current-full-name (str (:className parent-node) "." nested-name)
                    ana-n (assoc ana-n :className current-full-name)
                    nested-class-sym (string/replace current-full-name "." "$")
                    current-deps (get-deps ana-n custom-mappings opaque-types generated-fqcns)]
                (remove nil?
                  (concat
                    (emit-all-nested-forms ana-n current-deps version main-class-name custom-mappings opaque-types generated-fqcns)
                    (case (:category ana-n)
                      :nested/accessor-with-builder
                      [(emit-accessor-from-edn ana-n current-deps version ":from-edn" nested-class-sym current-full-name)
                       (emit-accessor-to-edn ana-n current-deps version ":to-edn" nested-class-sym current-full-name)]

                      (:nested/enum :nested/string-enum :nested/builder :nested/factory :nested/client)
                      nil

                      :concrete-union
                      [(emit-concrete-union-from-edn ana-n current-deps version ":from-edn" nested-class-sym current-full-name)
                       (emit-concrete-union-to-edn ana-n current-deps version ":to-edn" nested-class-sym current-full-name)]

                      :nested/union-factory
                      [(emit-union-factory-from-edn ana-n current-deps version ":from-edn" nested-class-sym current-full-name)
                       (emit-union-factory-to-edn ana-n current-deps version ":to-edn" nested-class-sym current-full-name)]

                      :nested/static-factory
                      [(emit-static-factory-from-edn ana-n current-deps version ":from-edn" nested-class-sym current-full-name)
                       (emit-static-factory-to-edn ana-n current-deps version ":to-edn" nested-class-sym current-full-name)]

                      :nested/read-only
                      [(emit-read-only-from-edn ana-n version ":from-edn" nested-class-sym current-full-name)
                       (emit-accessor-to-edn ana-n current-deps version ":to-edn" nested-class-sym current-full-name)]

                      :nested/pojo
                      [(emit-accessor-from-edn ana-n current-deps version ":from-edn" nested-class-sym current-full-name)
                       (emit-accessor-to-edn ana-n current-deps version ":to-edn" nested-class-sym current-full-name)]
                      nil)
                    (when-not (or (= (:category ana-n) :builder)
                                  (= (:category ana-n) :nested/enum)
                                  (= (:category ana-n) :nested/string-enum)
                                  (= (:category ana-n) :nested/builder)
                                  (= (:category ana-n) :nested/factory)
                                  (= (:category ana-n) :nested/client)
                                  (= (:category ana-n) :factory)
                                  (= (:category ana-n) :client))
                      [(emit-schema ana-n version)])))))
      (:nested parent-node))))

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
  ([{:keys [category] :as node} metadata]
   (assert (contains? shared/categories (:category node)))
   (let [custom-mappings (:custom-namespace-mappings node)
         opaque-types (set (:opaque-types node))
         generated-fqcns (set (::ana/generated-fqcns node))
         version (u/extract-version (:doc node))
         deps (if (= category :resource-extended-class)
                (get-resource-deps node custom-mappings opaque-types generated-fqcns)
                (get-deps node custom-mappings opaque-types generated-fqcns))
         ns-form (emit-ns-form node deps metadata)
         main-class-name (:className node)
         nested-forms (emit-all-nested-forms node deps version main-class-name custom-mappings opaque-types generated-fqcns)
         [from-edn to-edn] (if (= category :resource-extended-class)
                             (emit-resource-delegation node deps main-class-name)
                             [(case category
                                :abstract (throw (Exception. "emit-abstract-from-edn unimplemented"))
                                :abstract-union (emit-accessor-from-edn node deps version nil main-class-name main-class-name)
                                :accessor-with-builder (emit-accessor-from-edn node deps version nil main-class-name main-class-name)
                                :concrete-union (emit-concrete-union-from-edn node deps version nil main-class-name main-class-name)
                                :enum (emit-enum-from-edn node version nil main-class-name main-class-name)
                                :read-only (emit-read-only-from-edn node version nil main-class-name main-class-name)
                                :static-factory (emit-static-factory-from-edn node deps version nil main-class-name main-class-name)
                                :union-factory (emit-union-factory-from-edn node deps version nil main-class-name main-class-name)
                                :variant-accessor (emit-accessor-from-edn node deps version nil main-class-name main-class-name)
                                (throw (Exception. (str "unimplemented from-edn for category " category))))
                              (case category
                                :abstract (throw (Exception. "emit-abstract-to-edn unimplemented"))
                                :abstract-union (emit-accessor-to-edn node deps version nil main-class-name main-class-name)
                                :accessor-with-builder (emit-accessor-to-edn node deps version nil main-class-name main-class-name)
                                :concrete-union (emit-concrete-union-to-edn node deps version nil main-class-name main-class-name)
                                :enum (emit-enum-to-edn node version nil main-class-name main-class-name)
                                :read-only (emit-accessor-to-edn node deps version nil main-class-name main-class-name)
                                :static-factory (emit-static-factory-to-edn node deps version nil main-class-name main-class-name)
                                :union-factory (emit-union-factory-to-edn node deps version nil main-class-name main-class-name)
                                :variant-accessor (emit-accessor-to-edn node deps version nil main-class-name main-class-name)
                                (throw (Exception. (str "unimplemented to-edn for category " category))))])
         schema (emit-schema node version)
         registry-entries (collect-registry-entries node nil)
         registry-map (into (sorted-map) registry-entries)
         registry-form `(~'global/include-schema-registry!
                          (~'with-meta ~registry-map {:gcp.global/name (~'str ~'*ns*)}))]
     (remove nil? (concat [ns-form] nested-forms [from-edn to-edn schema registry-form])))))

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
                         (string/replace #"(defn\s+)([\w:\-\.]+)(?:__TAG__)([\w\$\.]+)"
                                         (fn [[_ d1 func type]] (str d1 "^" type " " func)))
                         ;; (defn func__ARGTAG__Type [arg] ...) -> (defn func [^Type arg] ...)
                         (string/replace #"(defn\s+)([\w:\-\.]+)(?:__ARGTAG__)([\w\$\.]+)(\s*\[\s*)arg"
                                         (fn [[_ d1 func type d2]] (str d1 func d2 "^" type " arg")))))]
     (str preamble
          (string/join "\n\n"
                       (map (comp fix-hints zp/zprint-str) forms))))))
