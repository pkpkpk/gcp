(ns gcp.dev.toolchain.malli
  "Convert analyzer AST nodes into malli schemas"
  (:require
   [clojure.string :as string]
   [gcp.dev.toolchain.shared :as shared]
   [gcp.dev.util :as u]))

(defn- find-nested-enum [node type-name]
  (let [t-str (str type-name)]
    (some (fn [n]
            (when (#{:enum :string-enum} (:category n))
              (let [name (:name n)]
                (when (or (= t-str name)
                          (string/ends-with? t-str (str "." name))
                          (string/ends-with? t-str (str "$" name)))
                  ;; Run analyzer logic to extract values if missing
                  (if (contains? n :values)
                    n
                                          (let [type-name (:name n)
                                                full-class (str (:className node) "." type-name)
                                                values (->> (:fields n)
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
                                            (assoc n :values values :className full-class)))))))          (:nested node))))

(declare malli-enum malli-string-enum)

(defn ->malli-type
  ([package-name t version] (->malli-type package-name t version nil))
  ([package-name t version context-node]
   (let [t-str (str t)]
     (cond
       (nil? t) :nil
       (= t-str "void") :nil
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
           (or (= base-str "java.util.List")
               (= base-str "com.google.common.collect.ImmutableList")
               (= base-str "java.lang.Iterable"))
           [:sequential (->malli-type package-name (first args) version context-node)]
           (or (= base-str "java.util.Map")
               (= base-str "com.google.common.collect.ImmutableMap"))
           [:map-of (->malli-type package-name (first args) version context-node) (->malli-type package-name (second args) version context-node)]
           :else
           (let [{:keys [package class]} (u/split-fqcn base-str)]
             (if-let [nested-enum (and context-node (find-nested-enum context-node base-str))]
               (if (= (:category nested-enum) :enum)
                 (malli-enum nested-enum version)
                 (malli-string-enum nested-enum version))
               (u/schema-key package class)))))
       (or (= t-str "java.util.Map") (string/starts-with? t-str "java.util.Map<"))
       [:map-of :string :any]
       (or (= t-str "java.util.List") (string/starts-with? t-str "java.util.List<"))
       [:sequential :any]
       (and (not (string/starts-with? t-str "com.google.cloud"))
            (not (u/native-type t-str)))
       (let [ns-sym (u/infer-foreign-ns t-str)
             {:keys [class]} (u/split-fqcn t-str)]
         (keyword (name ns-sym) class))
       (string/starts-with? t-str "com.google.cloud")
       (let [clean-t (string/replace t-str #"^(class|interface) " "")
             {:keys [package class]} (u/split-fqcn clean-t)]
         (if-let [nested-enum (and context-node (find-nested-enum context-node clean-t))]
           (if (= (:category nested-enum) :enum)
             (malli-enum nested-enum version)
             (malli-string-enum nested-enum version))
           (u/schema-key package class)))
       :else :any))))

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
                    s-doc     (u/clean-doc setterDoc)]
                (cond-> [(keyword k)]
                  (or (seq opts) g-doc s-doc required?)
                  (conj (cond-> opts
                          g-doc (assoc :doc g-doc)
                          s-doc (assoc :doc (str s-doc (when g-doc (str " " g-doc))))
                          (not required?) (assoc :optional true)))
                  true (conj (->malli-type package type version node)))))
            (remove (fn [[k v]] (string/starts-with? k "has")) fields)))))

(defn- malli-static-factory
  [{:keys [category doc className package] :as node} version]
  ; [:map {:gcp/key  (:gcp/key node)
  ;       :gcp/category category
  ;       :closed   true
  ;       :doc      (u/clean-doc doc)
  ;       :class    className}]
  (throw (Exception. "malli-static-factory unimplemented")))

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
                        s-doc     (u/clean-doc setterDoc)]
                    [(keyword k)
                     (cond-> opts
                             (not required?) (assoc :optional true)
                             g-doc (assoc :getterDoc g-doc)
                             s-doc (assoc :setterDoc s-doc))
                     (->malli-type package type version node)]))
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

(defn- malli-abstract-union
  [{:keys [category doc className package] :as node} version]
  ; [:map {:gcp/key (u/schema-key package className)
  ;       :gcp/category category
  ;       :doc (u/clean-doc doc)
  ;       :class className}]
  (throw (Exception. "malli-abstract-union unimplemented")))

(defn- malli-union-factory
  [{:keys [category doc className package getType getters] :as node} version]
  (let [head [:map {:gcp/key      (:gcp/key node)
                    :gcp/category category
                    :closed       true
                    :doc          (u/clean-doc doc)
                    :class        className}]
        ;; Prop derived from getType
        type-schema (->malli-type package getType version node)
        type-doc (u/clean-doc (:doc getType))
        type-prop (if type-doc
                    [:type {:doc type-doc} type-schema]
                    [:type type-schema])
        ;; Props derived from getters
        other-props (->> getters
                         (remove #(= (:name %) "getType"))
                         (map (fn [m]
                                (let [pname   (u/property-name (:name m))
                                      ptype   (:returnType m)
                                      pdoc    (u/clean-doc (:doc m))
                                      pschema (->malli-type package ptype version node)]
                                  (if pdoc
                                    [(keyword pname) {:doc pdoc} pschema]
                                    [(keyword pname) pschema])))))]
    (into head (cons type-prop other-props))))
(defn ->schema
  "Converts an analyzed node into a malli schema."
  ([node] (->schema node (u/extract-version (:doc node))))
  ([node version]
   (let [category (:category node)
         _        (assert (contains? shared/categories category))
         schema   (case category
                    :accessor-with-builder (malli-accessor node version)
                    :variant-accessor (malli-variant-accessor node version)
                    :enum (malli-enum node version)
                    :string-enum (malli-string-enum node version)
                    :static-factory (malli-static-factory node version)
                    :abstract-union (malli-abstract-union node version)
                    :concrete-union (malli-concrete-union node version)
                    :union-factory (malli-union-factory node version)
                    :read-only (malli-accessor node version)
                    (throw (Exception. (str "->schema unimplemented for category " category))))
         props    (second schema)]
     (assert (contains? props :gcp/key))
     (assert (contains? props :gcp/category))
     (assert (contains? shared/categories (get props :gcp/category)))
     schema)))
