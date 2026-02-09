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
        (select-keys node [:gcp/key :doc :fqcn :file-git-sha :package])))

(defn scalar-type [t]
  (case t
    (void java.lang.Void) :nil
    (? Object) :any
    (java.lang.Char char) :char
    java.lang.String :string
    (java.lang.Byte byte) :byte
    (java.lang.Integer int) [:int {:min -2147483648 :max 2147483647}]
    (java.lang.Long long) :int
    (java.lang.Float float) :float
    (java.lang.Double double) :double
    (java.lang.Boolean boolean) :boolean
    java.util.UUID :uuid
    java.util.regex.Pattern :re
    java.time.Instant :inst
    java.math.BigDecimal :bigdec
    java.math.BigInteger :bigint
    (throw (Exception. (str "could not resolve scalar type: " (pr-str t))))))

(defn type-schema
  [deps t]
  (let [type-category (shared/categorize-type deps t)]
    (match type-category
      :scalar (scalar-type t)
      :peer (u/fqcn->schema-key t)
      :foreign (u/fqcn->schema-key t true)
      :nested (u/fqcn->schema-key t)
      [:foreign :generic] (u/fqcn->schema-key t true)
      #!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      [:map :scalar :scalar] (let [[_ K V] t]
                               [:map-of (scalar-type K) (scalar-type V)])
      [:map :scalar :foreign] (let [[_ K V] t]
                                [:map-of (scalar-type K) (u/fqcn->schema-key V true)])
      [:map :scalar (:or :nested :peer)] (let [[_ K V] t]
                                           [:map-of (scalar-type K) (u/fqcn->schema-key V)])
      #!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      [:collection-wrapper [(:or :iterable :list) :self]] [:sequential (u/fqcn->schema-key (:self deps))]
      [:collection-wrapper [(:or :iterable :list) :peer]] [:sequential (u/fqcn->schema-key (get-in deps [:collection-wrappers t 1]))]
      #!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      [(:or :iterable :list :array) :self]               [:sequential (u/fqcn->schema-key (:self deps))]
      [(:or :iterable :list :array) (:or :nested :peer)] [:sequential (u/fqcn->schema-key (second t))]
      [(:or :iterable :list :array) :foreign]            [:sequential (u/fqcn->schema-key (second t) true)]
      [(:or :iterable :list :array) :scalar]             [:sequential (scalar-type (second t))]
      #!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      [:set :scalar] [:set (scalar-type (second t))]
      #!>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
      :custom (u/fqcn->schema-key (get-in deps [:custom-mappings t]))
      [(:or :iterable :list) :custom] [:sequential (u/fqcn->schema-key (get-in deps [:custom-mappings (second t)]))]
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

(defn- accessor-with-builder-schema
  [{:keys [deps] :as node}]
  (let [opts             (common-opts node)
        required-fields  (reduce
                           (fn [acc k]
                             ;; these are fields passed to constructor (no setter)
                             ;; but recovered via getter
                             (let [{:keys [doc returnType] :as getter} (get-in node [:getters-by-key k])
                                   _ (assert (some? getter) (str "missing required :getters-by-key " k))
                                   opts {:getter-doc doc}]
                               (assoc acc k {:opts opts :schema (type-schema deps returnType)})))
                           (sorted-map)
                           (:keys/required node))
        optional-fields  (reduce
                           (fn [acc k]
                             ;; fields set in builder & recoverable w/ getter
                             (let [getter (g/coerce map? (get-in node [:getters-by-key k]))
                                   setter (g/coerce map? (get-in node [:builder-setters-by-key k]))
                                   _ (when (not= (type-schema deps (:returnType getter))
                                                 (type-schema deps (get-in setter [:parameters 0 :type])))
                                       (throw (ex-info "expected return-type to equal parameter type"
                                                       {:returnType          (:returnType getter)
                                                        :returnType-category (categorize-type deps (:returnType getter))
                                                        :returnType-schema   (type-schema deps (:returnType getter))
                                                        :paramType           (get-in setter [:parameters 0 :type])
                                                        :paramType-category  (categorize-type deps (get-in setter [:parameters 0 :type]))
                                                        :paramType-schema    (type-schema deps (get-in setter [:parameters 0 :type]))
                                                        :getter              getter
                                                        :setter              setter})))
                                   opts (cond-> {:optional true}
                                          (get getter :doc) (assoc :getter-doc (get getter :doc))
                                          (get setter :doc) (assoc :setter-doc (get setter :doc)))]
                               (assoc acc k {:opts opts :schema (type-schema deps (:returnType getter))})))
                           (sorted-map)
                           (:keys/optional node))
        read-only-fields (reduce
                           (fn [acc k]
                             ;; unsettable fields only available via getter
                             (let [getter (g/coerce map? (get-in node [:getters-by-key k]))
                                   opts (cond-> {:optional true :read-only? true}
                                          (get getter :doc) (assoc :getter-doc (get getter :doc)))]
                               (assoc acc k {:opts opts :schema (type-schema deps (:returnType getter))})))
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
  (assert (not= name "of"))
  (let [opts (common-opts node)]
    [:maybe opts [:map {:closed true} [(u/property-name (:name method)) :any]]]))

(defn static-factory-cond-schema
  [{:keys [factory-methods deps] :as node}]
  (let [rf (fn [acc {:keys [parameters] :as method}]
             (let [opts (cond-> {:closed true} (:doc method) (assoc :doc (:doc method)))]
               (cond
                 (empty? parameters)
                 (conj acc [:maybe [:map opts [(u/property-key (:name method)) :any]]])

                 (= 1 (count parameters))
                 (let [parameter  (first parameters)
                       schema     (type-schema deps (:type parameter))
                       map-schema [:map opts [(u/property-key (:name parameter)) schema]]]
                   (conj acc schema map-schema))

                 :else
                 (let [schema (into [:map opts]
                                    (map
                                      (fn [parameter]
                                        [(u/property-key (:name parameter))
                                         (type-schema deps (:type parameter))]))
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
  [{:keys [discriminator] :as node}]
  (let [accessor-schema (accessor-with-builder-schema node)
        variant-schema [:map [:type [:= discriminator]]]]
    (m/form (mu/merge accessor-schema variant-schema))))

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

#!----------------------------------------------------------------------------------------------------------------------

(defn ->schema
  [{:keys [category] :as node}]
  {:pre   [(contains? shared/categories category)]
   :post [(contains? (second %) :gcp/key)
          (contains? (second %) :gcp/category)
          (= category (get (second %) :gcp/category))]}
  (case category
    (:enum
     :string-enum
     :nested/enum
     :nested/string-enum) (enum-schema node)
    (:accessor-with-builder
     :nested/accessor-with-builder) (accessor-with-builder-schema node)
    (:static-factory
     :nested/static-factory) (static-factory-schema node)
    (:read-only
     :nested/read-only) (read-only-schema node)
    (:union-abstract
     :nested/union-abstract) (union-abstract-schema node)
    (:union-concrete
      :nested/union-concrete) (union-concrete-schema node)
    (:variant-accessor
     :nested/variant-accessor) (variant-accessor-schema node)
    (:union-tagged
     :nested/union-tagged) (union-tagged-schema node)
    (:pojo :nested/pojo) (pojo-schema node)
    (throw (Exception. (str "->schema unimplemented for category " category)))))
