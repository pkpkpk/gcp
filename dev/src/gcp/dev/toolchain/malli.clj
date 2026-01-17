(ns gcp.dev.toolchain.malli
  "Convert analyzer AST nodes into malli schemas"
  (:require
   [clojure.string :as string]
   [gcp.dev.toolchain.shared :as shared]
   [gcp.dev.util :as u]))

(defn- find-nested-enum [node type-name]
  (let [t-str (str type-name)]
    (some (fn [n]
            (when (#{:enum :string-enum :nested/enum :nested/string-enum} (:category n))
              (let [name (:name n)]
                (when (or (= t-str name)
                          (string/ends-with? t-str (str "." name))
                          (string/ends-with? t-str (str "$" name)))
                  (let [type-name (:name n)
                        full-class (str (:className node) "." type-name)]
                    (if (contains? n :values)
                      (assoc n :className full-class)
                      (let [values (->> (:fields n)
                                        (filter (fn [f]
                                                  (let [ft (str (:type f))
                                                        static? (some #{"static"} (:modifiers f))
                                                        public? (some #{"public"} (:modifiers f))]
                                                    (and static?
                                                         public?
                                                         (or (= ft type-name)
                                                             (string/ends-with? ft (str "." type-name))
                                                             (string/ends-with? ft (str "$" type-name)))))))
                                        (map :name))]
                        (assoc n :values values :className full-class))))))))          (:nested node))))

(declare malli-enum malli-string-enum malli-inline-enum malli-inline-string-enum)

(defn- resolve-type-key [package-name t-str context-node]
  (let [foreign-mappings (:foreign-mappings context-node)
        {:keys [package class]} (u/split-fqcn t-str)]
    (if-let [foreign-ns (get foreign-mappings t-str)]
      (keyword (name foreign-ns) class)
      (if (string/starts-with? t-str "com.google.cloud")
        (u/schema-key package class)
        (throw (ex-info (str "Foreign type not found in mappings: " t-str)
                        {:type t-str :package package-name}))))))

(defn ->malli-type
  ([package-name t version] (->malli-type package-name t version nil))
  ([package-name t version context-node]
   (let [t-str (str t)]
     (cond
       (nil? t) :nil
       (= t-str "void") :nil
       (= t-str "java.lang.Object") :any
       (= t-str "?") :any
       (= t-str "java.lang.String") :string
       (#{"boolean" "java.lang.Boolean"} t-str) :boolean
       (#{"int" "java.lang.Integer"} t-str) [:int {:min -2147483648 :max 2147483647}]
       (#{"long" "java.lang.Long"} t-str) :int
       (#{"float" "java.lang.Float"} t-str) [:double {:min -3.4028235E38 :max 3.4028235E38}]
       (#{"double" "java.lang.Double"} t-str) :double
       (= t-str "com.google.protobuf.ProtocolStringList") [:sequential :string]
       (sequential? t)
       (let [[base & args] t
             base-str (str base)]
         (cond
           (= base :type-parameter) :any
           (or (= base-str "java.util.List")
               (= base-str "com.google.common.collect.ImmutableList")
               (= base-str "java.lang.Iterable"))
           [:sequential (->malli-type package-name (first args) version context-node)]
           (or (= base-str "java.util.Map")
               (= base-str "com.google.common.collect.ImmutableMap"))
           [:map-of (->malli-type package-name (first args) version context-node) (->malli-type package-name (second args) version context-node)]
           :else
           (if-let [nested-enum (and context-node (find-nested-enum context-node base-str))]
             (case (:category nested-enum)
               :enum (malli-enum nested-enum version)
               :nested/enum (malli-inline-enum nested-enum version)
               :string-enum (malli-string-enum nested-enum version)
               :nested/string-enum (malli-inline-string-enum nested-enum version))
             (resolve-type-key package-name base-str context-node))))
       (or (= t-str "java.util.Map") (string/starts-with? t-str "java.util.Map<"))
       [:map-of :string :any]
       (or (= t-str "java.util.List") (string/starts-with? t-str "java.util.List<"))
       [:sequential :any]
       :else
       (let [clean-t (string/replace t-str #"^(class|interface) " "")]
         (if-let [nested-enum (and context-node (find-nested-enum context-node clean-t))]
           (case (:category nested-enum)
             :enum (malli-enum nested-enum version)
             :nested/enum (malli-inline-enum nested-enum version)
             :string-enum (malli-string-enum nested-enum version)
             :nested/string-enum (malli-inline-string-enum nested-enum version))
           (resolve-type-key package-name clean-t context-node)))))))

(defn- hoist-schema-properties
  "If the schema is an inlined enum ([:enum {props} ...]), splits it into [props [:enum ...]].
   Returns [props bare-schema] or [nil schema]."
  [schema]
  (if (and (vector? schema)
           (= (first schema) :enum)
           (map? (second schema))
           (#{:nested/enum :nested/string-enum} (:gcp/category (second schema))))
    [(second schema) (into [:enum] (drop 2 schema))]
    [nil schema]))

(defn- malli-accessor
  [{:keys [category doc fields className package] :as node} version]
  (let [head [:map {:gcp/key  (:gcp/key node)
                    :gcp/category category
                    :closed   true
                    :doc      (u/clean-doc doc)
                    :class    className}]]
    (into head
          (map
            (fn [[k v]]
              (let [{:keys [type getterDoc setterDoc required?]} v
                    opts      (dissoc v :setterMethod :getterMethod :type :getterDoc :setterDoc :required?)
                    g-doc     (u/clean-doc getterDoc)
                    s-doc     (u/clean-doc setterDoc)
                    raw-schema (->malli-type package type version node)
                    [hoisted-opts schema] (hoist-schema-properties raw-schema)
                    opts (merge opts hoisted-opts)]
                (cond-> [(keyword k)]
                  (or (seq opts) g-doc s-doc required?)
                  (conj (cond-> opts
                          g-doc (assoc :doc g-doc)
                          s-doc (assoc :doc (str s-doc (when g-doc (str " " g-doc))))
                          (not required?) (assoc :optional true)))
                  true (conj schema))))
            (remove (fn [[k v]] (string/starts-with? k "has")) fields)))))

(defn- malli-static-factory
  [{:keys [category doc fields className package] :as node} version]
  (let [head [:map {:gcp/key  (:gcp/key node)
                    :gcp/category category
                    :closed   true
                    :doc      (u/clean-doc doc)
                    :class    className}]
        ;; Filter only property fields (those with :type), ignoring factory method entries
        prop-fields (filter (fn [[k v]] (:type v)) fields)]
    (into head
          (map
            (fn [[k v]]
              (let [{:keys [type getterDoc setterDoc required?]} v
                    opts      (dissoc v :setterMethod :getterMethod :type :getterDoc :setterDoc :required? :parameters)
                    g-doc     (u/clean-doc getterDoc)
                    s-doc     (u/clean-doc setterDoc)
                    raw-schema (->malli-type package type version node)
                    [hoisted-opts schema] (hoist-schema-properties raw-schema)
                    opts (merge opts hoisted-opts)]
                (cond-> [(keyword k)]
                  (or (seq opts) g-doc s-doc required?)
                  (conj (cond-> opts
                          g-doc (assoc :doc g-doc)
                          s-doc (assoc :doc (str s-doc (when g-doc (str " " g-doc))))
                          (not required?) (assoc :optional true)))
                  true (conj schema))))
            prop-fields))))

(defn- malli-enum
  [{:keys [category doc values className package] :as node} version]
  (let [doc (u/clean-doc doc)
        opts (cond-> {:class className
                      :gcp/category category
                      :gcp/key (:gcp/key node)}
                     doc (assoc :doc doc))
        enum-values (->> values
                         (map (fn [v] (if (map? v) (:name v) v)))
                         (remove #{"UNRECOGNIZED"}))]
    (into [:enum opts] enum-values)))

(defn- malli-inline-enum
  [{:keys [category doc values className package] :as node} version]
  (let [doc (u/clean-doc doc)
        opts (cond-> {:gcp/category category}
                     className (assoc :class className)
                     doc (assoc :doc doc))
        enum-values (->> values
                         (map (fn [v] (if (map? v) (:name v) v)))
                         (remove #{"UNRECOGNIZED"}))]
    (into [:enum opts] enum-values)))

(defn- malli-inline-string-enum
  [{:keys [category doc values className package] :as node} version]
  (let [doc (u/clean-doc doc)
        opts (cond-> {:gcp/category category}
                     className (assoc :class className)
                     doc (assoc :doc doc))
        enum-values (if (seq values)
                      values
                      [])]
    (into [:enum opts] enum-values)))

(defn- malli-string-enum
  [{:keys [category doc values className package] :as node} version]
  (let [doc (u/clean-doc doc)
        opts (cond-> {:class className
                      :gcp/category category
                      :gcp/key (:gcp/key node)}
                     doc (assoc :doc doc))
        enum-values (if (seq values)
                      values
                      ;; Fallback if analyzer failed to extract values (e.g. before my fix)
                      [])]
    (into [:enum opts] enum-values)))

(defn- malli-variant-accessor
  [{:keys [category doc fields className package discriminator] :as node} version]
  (let [head [:map {:gcp/key      (:gcp/key node)
                    :gcp/category category
                    :closed       true
                    :doc          (u/clean-doc doc)
                    :class        className}
              [:type [:= discriminator]]]]
    (-> head
        (into (map
                (fn [[k v]]
                  (let [{:keys [type getterDoc setterDoc required?]} v
                        opts      (dissoc v :setterMethod :getterMethod :type :getterDoc :setterDoc :required?)
                        g-doc     (u/clean-doc getterDoc)
                        s-doc     (u/clean-doc setterDoc)
                        raw-schema (->malli-type package type version node)
                        [hoisted-opts schema] (hoist-schema-properties raw-schema)
                        opts (merge opts hoisted-opts)]
                    [(keyword k)
                     (cond-> opts
                             (not required?) (assoc :optional true)
                             g-doc (assoc :getterDoc g-doc)
                             s-doc (assoc :setterDoc s-doc))
                     schema]))
                fields)))))

(defn- malli-concrete-union
  [{:keys [category doc className package variants] :as node} version]
  (let [current-fqcn (str package "." className)
        opts         {:gcp/key      (:gcp/key node)
                      :gcp/category category
                      :doc          (u/clean-doc doc)
                      :class        className}
        ;; Complex variants (return a specialized subtype)
        complex-variants (filter (fn [[_ v]] (not= (:returnType v) current-fqcn)) variants)
        ;; Simple variants (return the union type itself)
        simple-variants (remove (fn [[_ v]] (not= (:returnType v) current-fqcn)) variants)
        complex-schemas (map (fn [[_ v]]
                               (let [{:keys [package class]} (u/split-fqcn (:returnType v))]
                                 (u/schema-key package class)))
                             complex-variants)
        simple-schemas (map (fn [[t _]] [:map [:type [:= t]]]) simple-variants)]
    (into [:or opts] (concat complex-schemas simple-schemas))))

(defn- malli-union-factory
  [{:keys [category doc className package getType getters] :as node} version]
  (let [head [:map {:gcp/key      (:gcp/key node)
                    :gcp/category category
                    :closed       true
                    :doc          (u/clean-doc doc)
                    :class        className}]
        ;; Prop derived from getType
        raw-type-schema (->malli-type package getType version node)
        [type-hoisted-opts type-schema] (hoist-schema-properties raw-type-schema)
        type-doc (u/clean-doc (:doc getType))
        type-opts (cond-> (or type-hoisted-opts {})
                    type-doc (assoc :doc type-doc))
        type-prop (if (seq type-opts)
                    [:type type-opts type-schema]
                    [:type type-schema])
        ;; Props derived from getters
        other-props (->> getters
                         (remove #(= (:name %) "getType"))
                         (map (fn [m]
                                (let [pname   (u/property-name (:name m))
                                      ptype   (:returnType m)
                                      pdoc    (u/clean-doc (:doc m))
                                      raw-schema (->malli-type package ptype version node)
                                      [hoisted-opts pschema] (hoist-schema-properties raw-schema)
                                      nullable? (some #(let [n (:name %)]
                                                         (or (= n "Nullable")
                                                             (string/ends-with? n ".Nullable")))
                                                      (:annotations m))
                                      opts (cond-> (or hoisted-opts {})
                                             pdoc (assoc :doc pdoc)
                                             nullable? (assoc :optional true))]
                                  (cond-> [(keyword pname)]
                                    (seq opts) (conj opts)
                                    true (conj pschema))))))]
    (into head (cons type-prop other-props))))

(defn ->schema
  "Converts an analyzed node into a malli schema."
  ([node] (->schema node (u/extract-version (:doc node))))
  ([node version]
   (let [category (:category node)
         _        (assert (contains? shared/categories category))
         schema   (case category
                    :abstract (throw (Exception. "malli schema unimplemented for type :abstract"))
                    :abstract-union (throw (Exception. "malli-abstract-union unimplemented"))
                    :accessor-with-builder (malli-accessor node version)
                    :concrete-union (malli-concrete-union node version)
                    :enum (malli-enum node version)
                    :read-only (malli-accessor node version)
                    :static-factory (malli-static-factory node version)
                    :string-enum (malli-string-enum node version)
                    :union-factory (malli-union-factory node version)
                    :variant-accessor (malli-variant-accessor node version)
                    ;; ------------
                    :nested/static-factory (malli-static-factory node version)
                    :nested/union-factory (malli-union-factory node version)
                    :nested/accessor-with-builder (malli-accessor node version)
                    :nested/read-only (malli-accessor node version)
                    :nested/pojo (malli-accessor node version)
                    (throw (Exception. (str "->schema unimplemented for category " category))))
         props    (second schema)]
     (assert (contains? props :gcp/key))
     (assert (contains? props :gcp/category))
     (assert (contains? shared/categories (get props :gcp/category)))
     schema)))
