(ns gcp.dev.toolchain.malli
  "Convert analyzer AST nodes into malli schemas"
  (:require
    [clojure.core.match :refer [match]]
    [clojure.math.combinatorics :as combo]
   [clojure.set :as set]
   [clojure.string :as string]
   [gcp.dev.toolchain.shared :as shared :refer [categorize-type]]
   [gcp.dev.util :as u]
   [gcp.global :as g]
   [malli.core :as m]
   [malli.util :as mu]))

(defn- hoist-props
  [schema]
  (if (and (vector? schema)
           (map? (second schema)))
    (let [props (second schema)
          head (cond-> [(first schema)]
                       (contains? props :closed) (conj {:closed true}))]
      [props (into head (subvec schema 2))])
    [nil schema]))

(defn common-opts [node]
  (into (sorted-map :gcp/category (:category node) :closed true)
        (select-keys node [:gcp/key :doc])))

(defn scalar-type [t]
  (case t
    (void java.lang.Void) :nil
    (? Object) :any
    (java.lang.Char char) :char
    java.lang.String [:string {:min 1}]
    (java.lang.Byte byte) :byte
    (java.lang.Integer int) :i32
    (java.lang.Long long) :i64
    (java.lang.Float float) :f32
    (java.lang.Double double) :f64
    (java.lang.Boolean boolean) :boolean
    java.util.UUID :uuid
    java.util.regex.Pattern :regexp
    java.time.Instant :inst
    java.time.LocalTime :Time
    java.time.LocalDate :Date
    java.time.LocalDateTime :DateTime
    java.time.OffsetDateTime :OffsetDateTime
    java.time.Duration :Duration
    java.math.BigDecimal :bigdec
    java.math.BigInteger :bigint
    java.nio.file.Path :any
    (throw (Exception. (str "could not resolve scalar type: " (pr-str t))))))

(defn- enum-values [fqcn]
  (try
    (let [c (Class/forName (name fqcn))]
      (when (.isEnum c)
        (->> (.getEnumConstants c)
             (map #(.name %))
             (remove #{"UNRECOGNIZED"}))))
    (catch ClassNotFoundException _ nil)))

(defn map-key-schema [K]
  (if (= K 'java.lang.String)
    [:or 'simple-keyword? [:string {:min 1}]]
    (scalar-type K)))

(defn type-schema
  [deps t]
  (let [type-category (shared/categorize-type deps t)]
    (match type-category
      :scalar (scalar-type t)
      :enum (into [:enum {:closed true}] (u/enum-values t))
      :peer (u/fqcn->gcp-key t)
      :peer/nested (u/fqcn->gcp-key t)
      #!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      [:iterable :generic/peer] (let [[A B C] (second t)]
                                  ;; "com.google.cloud.vertexai.api.Content"
                                  ;; [:iterable :generic/peer] for type: [java.lang.Iterable [? :extends com.google.cloud.vertexai.api.Part]]
                                  (assert (= '? A))
                                  (assert (= :extends B))
                                  [:sequential {:min 1} (u/fqcn->gcp-key C)])
      #!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      #! nested - want to wrap in :ref to avoid load order issues
      :nested [:ref (u/fqcn->gcp-key t)]
      :sibling [:ref (u/fqcn->gcp-key t)]
      [(:or :iterable :list) :generic/nested] (let [[A B C] (second t)]
                                                (assert (= '? A))
                                                (assert (= :extends B))
                                                [:sequential {:min 1} [:ref (u/fqcn->gcp-key C)]])
      [(:or :iterable :list :array) :nested]  [:sequential {:min 1} [:ref (u/fqcn->gcp-key (second t))]]
      #!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      #! Foreign
      :foreign (u/fqcn->gcp-key t)
      [:foreign :generic] :any
      [(:or :iterable :list :array) :foreign] [:sequential {:min 1} (u/fqcn->gcp-key (second t))]
      [:map :scalar :foreign]                 (let [[_ K V] t] [:map-of (map-key-schema K) (u/fqcn->gcp-key V)])
      #!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      #! Custom
      :custom (u/fqcn->gcp-key t)
      [(:or :iterable :list) :custom] [:sequential {:min 1} (u/fqcn->gcp-key (second t))]
      [:map :scalar :custom] (let [[_ K V] t] [:map-of (map-key-schema K) (u/fqcn->gcp-key V)])
      #!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      #! Support
      :support (u/fqcn->gcp-key t)
      [(:or :iterable :list) :support] [:sequential (u/fqcn->gcp-key (second t))]
      [:map :scalar :support] (let [[_ K V] t] [:map-of (map-key-schema K) (u/fqcn->gcp-key V)])
      #!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      #! Self
      :self [:ref (u/fqcn->gcp-key (:self deps))]
      [(:or :iterable :list) :self] [:sequential {:min 1} [:ref (u/fqcn->gcp-key (:self deps))]]
      [:map :scalar :self] (let [[_ K V] t] [:map-of (map-key-schema K) [:ref (u/fqcn->gcp-key (:self deps))]])
      [:array :self] (list 'gcp.global/instance-schema (symbol (str (name (second t)) "/1")))
      #!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      :native (list 'gcp.global/instance-schema (symbol (name t)))
      [(:or :iterable :list :array) :native] (list 'gcp.global/instance-schema (symbol (str (name (second t)) "/1")))
      [:array :native] (list 'gcp.global/instance-schema (symbol (str (name (second t)) "/1")))
      #!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      [:map :scalar
       (:or :nested :peer :sibling)] (let [[_ K V] t] [:map-of (map-key-schema K) (u/fqcn->gcp-key V)])
      [:map :scalar :scalar]         (let [[_ K V] t] [:map-of (map-key-schema K) (scalar-type V)])
      [:map :scalar :enum]           (let [[_ K V] t] [:map-of (map-key-schema K) (into [:enum {:closed true}] (u/enum-values V))])
      [:map :scalar [:list :peer]]   (let [[_ K [_ E]] t] [:map-of (map-key-schema K) [:sequential {:min 1} (u/fqcn->gcp-key E)]])
      #!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      [(:or :iterable :list :array) :enum] [:sequential {:min 1} (into [:enum {:closed true}] (u/enum-values (second t)))]
      [(:or :iterable :list :array)
       (:or :nested :peer :sibling)] [:sequential {:min 1} (u/fqcn->gcp-key (second t))]
      [(:or :iterable :list :array) :scalar] [:sequential {:min 1} (scalar-type (second t))]
      #!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      [:set :scalar] [:set (scalar-type (second t))]
      #!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      :else (throw (Exception. (str "could not resolve schema type: " (pr-str t) " with category " type-category))))))

#!----------------------------------------------------------------------------------------------------------------------

(defn- enum-schema
  [{:keys [values] :as node}]
  (let [opts (common-opts node)
        enum-values (->> values
                         (map :name)
                         (remove #{"UNRECOGNIZED"}))]
    (into [:enum opts] enum-values)))

#!----------------------------------------------------------------------------------------------------------------------

(defn assert-getter-setter-agree!
  [deps getter setter parent-category parent-fqcn]
  (let [returnType (:returnType getter)
        setterType (get-in setter [:parameters 0 :type])]
    (when-not (or (= returnType setterType)
                  (= (type-schema deps returnType) (type-schema deps setterType))
                  (if-not (and (ident? returnType) (ident? setterType))
                    false
                    (let [ret (u/as-class returnType)
                          set(u/as-class setterType)]
                      (or (isa? ret set)
                          (isa? set ret)))))
      (if (= :client parent-category)
        (println "WARNING: getter/setter type mismatch for" (:name getter) "in" parent-fqcn
                 "getter-return:" returnType "setter-param:" setterType ". Preferring setter.")
        (throw (ex-info "expected return-type to equal parameter type"
                        {:getter              getter
                         :getter-return-schema (type-schema deps (:returnType getter))
                         :setter              setter
                         :setter-argument-schema (type-schema deps (get-in setter [:parameters 0 :type]))}))))))

(defn- accessor-with-builder-schema
  [{:keys [deps parent-category fqcn] :as node}]
  (let [opts             (common-opts node)
        required-fields  (reduce
                           (fn [acc k]
                             (let [{:keys [doc returnType] :as getter} (get-in node [:getters-by-key k])
                                   {:as setter} (get-in node [:builder-setters-by-key k])
                                   _ (assert (some? getter) (str "missing required :getters-by-key " k))
                                   _ (when setter (assert-getter-setter-agree! deps getter setter parent-category fqcn))
                                   schema-type (if (and (= :client parent-category) setter)
                                                 (get-in setter [:parameters 0 :type])
                                                 returnType)
                                   opts (cond-> {:getter-doc doc}
                                          (some? setter) (assoc :setter-doc (:doc setter)))]
                               (assoc acc k {:opts opts :schema (type-schema deps schema-type)})))
                           (sorted-map)
                           (:keys/required node))
        optional-fields  (reduce
                           (fn [acc k]
                             (let [{:keys [doc returnType] :as getter} (get-in node [:getters-by-key k])
                                   {:as setter} (get-in node [:builder-setters-by-key k])
                                   _ (assert (some? getter) (str "missing optional :getters-by-key " k))
                                   _ (when setter (assert-getter-setter-agree! deps getter setter parent-category fqcn))
                                   schema-type (if (and (= :client parent-category) setter)
                                                 (get-in setter [:parameters 0 :type])
                                                 returnType)
                                   opts (cond-> {:optional true}
                                          doc (assoc :getter-doc doc)
                                          (some? setter) (assoc :setter-doc (:doc setter)))]
                               (assoc acc k {:opts opts :schema (type-schema deps schema-type)})))
                           (sorted-map)
                           (:keys/optional node))
        read-only-fields (reduce
                           (fn [acc k]
                             (let [{:keys [doc returnType] :as getter} (get-in node [:getters-by-key k])
                                   _ (assert (some? getter) (str "missing read-only :getters-by-key " k))
                                   opts (cond-> {:optional true :read-only? true}
                                          doc (assoc :getter-doc doc))]
                               (assoc acc k {:opts opts :schema (type-schema deps returnType)})))
                           (sorted-map)
                           (:keys/read-only node))
        fields           (into (sorted-map) (merge required-fields optional-fields read-only-fields))]
    (into [:map opts]
          (map
            (fn [[k {:keys [opts schema]}]]
              (if opts
                [k opts schema]
                [k schema])))
          fields)))

#!----------------------------------------------------------------------------------------------------------------------
#! :static-factory

(defn static-factory-single-method-no-param-schema
  [{[method] :factory-methods :as node}]
  (assert (not= (:name method) "of"))
  (let [opts (common-opts node)]
    [:maybe opts [:map {:closed true}]]))

(defn- sanitize-regex-for-generator [re-str]
  (-> re-str
      (clojure.string/replace #"\+\?" "+")
      (clojure.string/replace #"\*\?" "*")))

(defn- apply-parameter-constraints [schema parameter]
  (if-let [re (:regex parameter)]
    [:re (re-pattern (sanitize-regex-for-generator re))]
    schema))

(defn static-factory-cond-schema
  [{:keys [factory-methods getters-by-key deps] :as node}]
  (let [rf (fn [acc {:keys [parameters] :as method}]
             (let [opts (cond-> {:closed true} (:doc method) (assoc :doc (:doc method)))]
               (cond
                 (empty? parameters)
                 (conj acc [:maybe [:map opts]])

                 (= 1 (count parameters))
                 (let [parameter (first parameters)
                       k         (u/property-key (:name parameter))
                       getter    (get getters-by-key k)
                       doc       (:doc getter)
                       p-opts    (cond-> {} doc (assoc :doc doc))
                       schema    (apply-parameter-constraints (type-schema deps (:type parameter)) parameter)
                       map-schema (if (seq p-opts)
                                    [:map opts [k p-opts schema]]
                                    [:map opts [k schema]])]
                   (conj acc schema map-schema))

                 :else
                 (let [schema (into [:map opts]
                                    (map
                                      (fn [parameter]
                                        (let [k      (u/property-key (:name parameter))
                                              getter (get getters-by-key k)
                                              doc    (:doc getter)
                                              p-opts (cond-> {} doc (assoc :doc doc))
                                              schema (apply-parameter-constraints (type-schema deps (:type parameter)) parameter)]
                                          (if (seq p-opts)
                                            [k p-opts schema]
                                            [k schema]))))
                                    parameters)]
                   (conj acc schema)))))]
    (reduce rf [:or (common-opts node)] factory-methods)))

(defn static-factory-intersecting-schema
  [{:keys [getters-by-key deps] :as node}]
  (let [opts (common-opts node)]
    (into [:map opts]
          (map
            (fn [[k {:keys [required? returnType doc] :as method}]]
              (let [opts   (not-empty (cond-> {} doc (assoc :doc doc) (not required?) (assoc :optional true)))
                    schema (type-schema deps returnType)]
                (if opts
                  [k opts schema]
                  [k schema]))))
          getters-by-key)))

(defn static-factory-intersecting-sugar-schema
  [{:keys [deps factory-methods] :as node}]
  (let [[method] (filter #(= 1 (count (:parameters %))) factory-methods)
        [props map-schema] (hoist-props (static-factory-intersecting-schema node))]
    [:or props
     (type-schema deps (-> method :parameters first :type))
     map-schema]))

(defn static-factory-single-method-single-param
  [{:keys [deps getters-by-key] :as node}]
  (let [[[k {:keys [doc returnType]}]] (seq getters-by-key)
        schema (type-schema deps returnType)]
    [:or (common-opts node)
     schema
     [:map {:closed true}
      [k schema]]]))

(defn static-factory-map-schema
  [{:keys [getters-by-key deps] :as node}]
  (into [:map (common-opts node)]
        (map
          (fn [[k {:keys [returnType]}]]
            [k (type-schema deps returnType)]))
        getters-by-key))

(defn static-factory-schema
  [{:keys [strategy] :as node}]
  (case strategy
    :single-method-no-param     (static-factory-single-method-no-param-schema node)
    :single-method-single-param (static-factory-single-method-single-param node)
    :map-keys                   (static-factory-map-schema node)
    :intersecting-with-sugar    (static-factory-intersecting-sugar-schema node)
    :intersecting-no-sugar      (static-factory-intersecting-schema node)
    :cond                       (static-factory-cond-schema node)))

#!----------------------------------------------------------------------------------------------------------------------
#! :read-only

(defn read-only-schema
  [{:keys [deps] :as node}]
  (into [:map (common-opts node)]
        (map
          (fn [[k {:keys [returnType doc]}]]
            (let [opts  (cond-> {:read-only? true} doc (assoc :doc doc))
                  schema (type-schema deps returnType)]
              [k opts schema])))
        (:getters-by-key node)))

#!----------------------------------------------------------------------------------------------------------------------
#! union-abstract

(defn union-abstract-schema
  [{:keys [deps variant-mappings] :as node}]
  (into [:or (common-opts node)]
        (map
          (partial type-schema deps)
          (vals variant-mappings))))

#!----------------------------------------------------------------------------------------------------------------------
#! union-concrete

(defn union-concrete-schema
  [{:keys [deps peer-variant-mappings self-variant-mappings discriminator-values] :as node}]
  (into [:or (common-opts node)]
        (map
          (fn [discriminator]
            (if-let [{:keys [doc] :as method} (get self-variant-mappings discriminator)]
              (let [opts  (cond-> {:closed true} doc (assoc :doc doc))]
                (assert (empty? (:parameters method)))
                [:map opts
                 [:type [:= discriminator]]])
              (type-schema deps (get peer-variant-mappings discriminator)))))
        discriminator-values))

#!----------------------------------------------------------------------------------------------------------------------

(defn variant-accessor-schema
  [{:keys [deps discriminator keys/required keys/optional keys/read-only parent-category fqcn] :as node}]
  (let [base [:map (common-opts node)
              [:type [:= discriminator]]]
        required-fields (map
                          (fn [k]
                            (let [{:keys [doc returnType] :as getter} (get-in node [:getters-by-key k])
                                  {:as setter} (get-in node [:setters-by-key k])
                                  _ (assert (some? getter) (str "missing required :getters-by-key " k))
                                  _ (when setter (assert-getter-setter-agree! deps getter setter parent-category fqcn))
                                  schema-type (if (and (= :client parent-category) setter)
                                                (get-in setter [:parameters 0 :type])
                                                returnType)
                                  opts (cond-> {:getter-doc doc}
                                               (some? setter) (assoc :setter-doc (:doc setter)))]
                              [k opts (type-schema deps schema-type)]))
                          required)
        optional-fields (map
                          (fn [k]
                            (let [getter (g/coerce map? (get-in node [:getters-by-key k]))
                                  setter (g/coerce map? (get-in node [:setters-by-key k]))
                                  _ (when setter (assert-getter-setter-agree! deps getter setter parent-category fqcn))
                                  schema-type (if (and (= :client parent-category) setter)
                                                (get-in setter [:parameters 0 :type])
                                                (:returnType getter))
                                  opts (cond-> {:optional true}
                                               (get getter :doc) (assoc :getter-doc (get getter :doc))
                                               (get setter :doc) (assoc :setter-doc (get setter :doc)))]
                              [k opts (type-schema deps schema-type)]))
                          optional)
        read-only-fields (map
                           (fn [k]
                             (let [getter (g/coerce map? (get-in node [:getters-by-key k]))
                                   opts (cond-> {:optional true :read-only? true}
                                                (get getter :doc) (assoc :getter-doc (get getter :doc)))]
                               [k opts (type-schema deps (:returnType getter))]))
                           read-only)]
    (into base (sort-by first (concat required-fields optional-fields read-only-fields)))))

#!----------------------------------------------------------------------------------------------------------------------

(defn union-tagged-schema
  [{:keys [deps getters-by-key] :as node}]
  (into [:map (common-opts node)]
        (map
          (fn [[k {:keys [returnType doc]}]]
            (let [schema (type-schema deps returnType)]
              (if doc
                [k {:doc doc} schema]
                [k schema]))))
        getters-by-key))

(defn- variant-read-only-schema
  [{:keys [deps getters-by-key discriminator] :as node}]
  (into [:map (common-opts node)
         [:type [:= discriminator]]]
        (->> (dissoc getters-by-key :type)
             (map
               (fn [[k {:keys [doc returnType]}]]
                 (let [opts (cond-> {:read-only? true} doc (assoc :doc doc))
                       schema (type-schema deps returnType)]
                   [k opts schema]))))))

#!----------------------------------------------------------------------------------------------------------------------

(defn pojo-schema
  [{:keys [deps getters-by-key keys/required] :as node}]
  (into [:map (common-opts node)]
        (map
          (fn [[k {:keys [doc returnType]}]]
            (let [opts (cond-> {}
                               doc (assoc :doc doc)
                               (not (contains? required k)) (assoc :optional true))
                  schema (type-schema deps returnType)]
              (if opts
                [k opts schema]
                [k schema]))))
        getters-by-key))

(defn variant-pojo-schema
  [{:keys [deps getters-by-key keys/required discriminator] :as node}]
  (into [:map (common-opts node)
         [:type [:= discriminator]]]
        (->> getters-by-key
             (remove (fn [[k _]] (= k :type)))
             (map
               (fn [[k {:keys [doc returnType]}]]
                 (let [opts (cond-> {}
                                    doc (assoc :doc doc)
                                    (not (contains? required k)) (assoc :optional true))
                       schema (type-schema deps returnType)]
                   (if opts
                     [k opts schema]
                     [k schema])))))))

#!----------------------------------------------------------------------------------------------------------------------

(defn mutable-pojo-schema
  [{:keys [getter-setters-by-key deps parent-category fqcn] :as node}]
  (let [opts (common-opts node)
        fields (reduce
                 (fn [acc [k {:keys [getter setter]}]]
                   (let [returnType (get getter :returnType)
                         _ (assert-getter-setter-agree! deps getter setter parent-category fqcn)
                         schema-type (if (and (= :client parent-category) setter)
                                       (get-in setter [:parameters 0 :type])
                                       returnType)
                         schema (type-schema deps schema-type)
                         props {:getter-doc (get getter :doc)
                                :setter-doc (get setter :doc)
                                :optional true}]
                     (conj acc [k props schema])))
                 []
                 getter-setters-by-key)]
    (into [:map opts] (sort-by first fields))))

#!----------------------------------------------------------------------------------------------------------------------

(defn collection-wrapper-schema
  [{:keys [deps element-type] :as node}]
  (let [opts (common-opts node)]
    [:sequential opts (type-schema deps element-type)]))

#!----------------------------------------------------------------------------------------------------------------------

(defn static-variants-schema
  [{:keys [key->factory deps] :as node}]
  (let [opts (common-opts node)
        [v :as vs] (map
                     (fn [[k {:keys [doc parameters] :as factory}]]
                       (let [opts (cond-> {:closed true} doc (assoc :doc doc))
                             ?T (get-in parameters [0 :type])]
                         [:map opts
                          [k (if ?T (type-schema deps ?T) :any)]]))
                     key->factory)]
    (if (= 1 (count vs))
      (update-in v [1] merge opts)
      (into [:or opts] vs))))

#!----------------------------------------------------------------------------------------------------------------------

(defn client-options-schema
  [{:keys [key->factory deps] :as node}]
  (let [opts (common-opts node)
        fields (map
                 (fn [[k {:keys [doc parameters]}]]
                   (let [field-opts (cond-> {:optional true} doc (assoc :doc doc))
                         ?T (get-in parameters [0 :type])]
                     (if ?T
                       [k field-opts (type-schema deps ?T)]
                       [k field-opts :boolean])))
                 key->factory)]
    [:maybe opts (into [:map {:closed true}] fields)]))

#!----------------------------------------------------------------------------------------------------------------------

(defn protobuf-message-schema
  [{:keys [deps] :as node}]
  (let [opts             (common-opts node)
        required-fields  (reduce
                           (fn [acc k]
                             (let [{:keys [doc returnType] :as getter} (get-in node [:getters-by-key k])
                                   _ (assert (map? getter) (str "missing required :getters-by-key " k))
                                   {:as setter} (get-in node [:setters-by-key k])
                                   _ (assert (map? setter) (str "missing required :setters-by-key " k))
                                   opts (cond-> {:getter-doc doc}
                                                (some? setter) (assoc :setter-doc (:doc setter)))]
                               (assoc acc k {:opts opts :schema (type-schema deps returnType)})))
                           (sorted-map)
                           (:keys/required node))
        optional-fields  (reduce
                           (fn [acc k]
                             (let [getter (get-in node [:getters-by-key k])
                                   _ (assert (map? getter) (str "missing required :getters-by-key " k))
                                   setter (get-in node [:setters-by-key k])
                                   ;_ (assert (map? setter) (str "missing required :setters-by-key " k))
                                   opts (cond-> {:optional true}
                                          (get getter :doc) (assoc :getter-doc (get getter :doc))
                                          (get setter :doc) (assoc :setter-doc (get setter :doc)))]
                               (assoc acc k {:opts opts :schema (type-schema deps (:returnType getter))})))
                           (sorted-map)
                           (:keys/optional node))
        read-only-fields (reduce
                           (fn [acc k]
                             (let [{:keys [doc returnType] :as getter} (get-in node [:getters-by-key k])
                                   _ (assert (map? getter) (str "missing required :getters-by-key " k))
                                   opts (cond-> {:optional true :read-only true}
                                                doc (assoc :getter-doc doc))]
                               (assoc acc k {:opts opts :schema (type-schema deps returnType)})))
                           (sorted-map)
                           (:keys/read-only node))
        fields           (merge required-fields optional-fields read-only-fields)
        fields           (reduce-kv (fn [acc k {:keys [opts schema]}]
                                      (conj acc [k opts schema])) [] fields)]
    (into [:map (assoc opts :closed true)] fields)))

#!----------------------------------------------------------------------------------------------------------------------

(defn union-protobuf-oneof-schema
  [{:keys [deps unions] :as node}]
  (let [opts             (common-opts node)
        required-fields  (reduce
                           (fn [acc k]
                             (let [{:keys [doc returnType] :as getter} (get-in node [:getters-by-key k])
                                   _ (assert (some? getter) (str "missing required :getters-by-key " k))
                                   {:as setter} (get-in node [:setters-by-key k])
                                   _ (assert (some? setter) (str "missing required :setters-by-key " k))
                                   opts (cond-> {:getter-doc doc} (some? setter) (assoc :setter-doc (:doc setter)))]
                               (assoc acc k {:opts opts :schema (type-schema deps returnType)})))
                           (sorted-map)
                           (:keys/required node))
        optional-fields  (reduce
                           (fn [acc k]
                             (let [getter (g/coerce map? (get-in node [:getters-by-key k]))
                                   setter (get-in node [:setters-by-key k])
                                   opts (cond-> {:optional true}
                                          (get getter :doc) (assoc :getter-doc (get getter :doc))
                                          (get setter :doc) (assoc :setter-doc (get setter :doc)))]
                               (assoc acc k {:opts opts :schema (type-schema deps (:returnType getter))})))
                           (sorted-map)
                           (:keys/optional node))
        read-only-fields (reduce
                           (fn [acc k]
                             (let [{:keys [doc returnType]} (g/coerce map? (get-in node [:getters-by-key k]))
                                   opts (cond-> {:optional true :read-only true}
                                                doc (assoc :getter-doc doc))]
                               (assoc acc k {:opts opts :schema (type-schema deps returnType)})))
                           (sorted-map)
                           (:keys/read-only node))
        fields           (merge required-fields optional-fields read-only-fields)
        fields-vec       (reduce-kv (fn [acc k {:keys [opts schema]}]
                                      (conj acc [k opts schema])) [] fields)
        map-schema       (into [:map opts]
                               fields-vec)
        union-constraints (map
                            (fn [[union-key {:keys [variants discriminator-method]}]]
                              (let [variant-keys (set (keys variants))]
                                [:fn {:error/message (str "Only one of these keys may be present: " variant-keys)}
                                 (list 'quote
                                       (list 'fn ['m] `(~'<= (~'count (~'filter (~'set (~'keys ~'m)) ~variant-keys)) 1)))]))
                            unions)]
    (if (seq union-constraints)
      (let [[props base-map] (hoist-props map-schema)]
        (into [:and props base-map] union-constraints))
      map-schema)))

#!----------------------------------------------------------------------------------------------------------------------

(defn- factory-schema
  [{:keys [factory-methods deps] :as node}]
  (let [opts (common-opts node)
        methods (map
                  (fn [{:keys [name parameters doc]}]
                    (let [m-opts (cond-> {:closed true} doc (assoc :doc doc))
                          k (keyword name)]
                      (if (empty? parameters)
                        [:map m-opts [k :nil]]
                        (let [p-schemas (mapv (fn [p] (type-schema deps (:type p))) parameters)
                              schema (if (= 1 (count p-schemas))
                                       (first p-schemas)
                                       (into [:vector] p-schemas))]
                          [:map m-opts [k schema]]))))
                  factory-methods)]
    (if (= 1 (count methods))
      (let [[_ m-props & m-body] (first methods)]
        (into [:map (merge opts m-props)] m-body))
      (into [:or opts] methods))))

#!----------------------------------------------------------------------------------------------------------------------

(defn ->schema
  [{:keys [category] :as node}]
  {:pre   [(contains? shared/categories category)]
   :post [(contains? (second %) :gcp/key)
          (contains? (second %) :gcp/category)
          (= category (get (second %) :gcp/category))]}
  (case category
    :nested/client-options (client-options-schema node)
    (:static-variants :nested/static-variants) (static-variants-schema node)
    (:mutable-pojo
      :nested/mutable-pojo) (mutable-pojo-schema node)
    (:enum
      :string-enum
      :nested/enum
      :nested/string-enum) (enum-schema node)
    (:protobuf-message
      :nested/protobuf-message) (protobuf-message-schema node)
    (:union-protobuf-oneof
      :nested/union-protobuf-oneof) (union-protobuf-oneof-schema node)
    (:accessor-with-builder
      :nested/accessor-with-builder) (accessor-with-builder-schema node)
    (:static-factory
      :nested/static-factory) (static-factory-schema node)
    (:read-only
      :nested/read-only
      :interface) (read-only-schema node)
    (:union-abstract :nested/union-abstract) (union-abstract-schema node)
    (:union-concrete
      :nested/union-concrete) (union-concrete-schema node)
    (:variant-accessor
      :nested/variant-accessor) (variant-accessor-schema node)
    (:union-tagged
      :nested/union-tagged) (union-tagged-schema node)
    (:pojo :nested/pojo) (pojo-schema node)
    :nested/variant-pojo (variant-pojo-schema node)
    :nested/variant-read-only (variant-read-only-schema node)
    (:collection-wrapper
      :nested/collection-wrapper) (collection-wrapper-schema node)
    (:factory :nested/factory) (factory-schema node)
    (throw (Exception. (str "->schema unimplemented for category " category)))))
