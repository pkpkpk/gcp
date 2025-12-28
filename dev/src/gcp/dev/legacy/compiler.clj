(ns ^{:doc "emit clojure code for roundtripping edn <-> sdk instances"}
  gcp.dev.legacy.compiler
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [gcp.dev.legacy.analyzer :as ana :refer [analyze]]
            [gcp.dev.util :refer :all]
            [gcp.global :as g]
            [zprint.core :as zp]))

#!----------------------------------------------------------------------------------------------------------------------
#!
#! ns-form
#!

;; TODO label as machine generated do not edit
;; TODO ns doc string, versioning, producing code hash

(defn sibling-require
  [{:keys [rootNs] :as package} className]
  (assert (some? rootNs))
  (let [req (symbol (str (name rootNs) "." className))
        alias (symbol className)]
    [req :as alias]))

(defn emit-ns-form
  [{:keys [rootNs packageName] :as package} node-or-className]
  (let [{:keys [typeDependencies className]} (if (string? node-or-className)
                                               (ana/analyze package node-or-className)
                                               (g/coerce [:map [:className :string]] node-or-className))
        [siblings other] (split-with #(string/starts-with? % (str "com.google.cloud." packageName)) typeDependencies)
        {siblings             false
         target-package-enums true} (group-by #(contains? (:types/enums package) %) siblings)
        sibling-parts   (into (sorted-set) (comp (map class-parts) (map first)) siblings)
        _               (when (seq other)
                          ;; TODO
                          ;; dependencies could be any class, google cloud or threetenbp usually if any
                          ;; get siblings by comparing w/ package classes
                          (throw (ex-info "unimplemented requires for non-sibling dependencies" {:target-class className
                                                                                                 :other-deps   other})))
        [target-package target-class] ((juxt #(symbol (string/join "." (butlast %))) #(symbol (last %))) (dot-parts className))
        requires        (into ['gcp.global] (map (partial sibling-require package)) sibling-parts)
        package-imports (cond-> [target-package target-class]
                                (seq target-package-enums) (into (comp (map class-dollar-string) (map symbol)) target-package-enums))
        imports         [package-imports]
        form `(~'ns ~(symbol (str rootNs "." (peek (class-parts className))))
                (:import ~@(sort-by first imports))
                (:require ~@(sort-by #(if (vector? %) (first %) %) requires)))]
    (zp/zprint-str form)))

#!----------------------------------------------------------------------------------------------------------------------
#!
#! :types/accessors
#!

(defn emit-from-edn-call
  [package key {:keys [setterArgumentType] :as field}]
  (assert (some? setterArgumentType))
  (cond
    (boolean (native-type setterArgumentType))
    `(~'get ~'arg ~key)

    (contains? (:types/enums package) setterArgumentType)
    (let [from-edn-call (symbol (class-dollar-string setterArgumentType) "valueOf")]
      `(~from-edn-call (~'get ~'arg ~key)))

    (contains? (:classes package) setterArgumentType)
    (let [from-edn-call (symbol (first (class-parts setterArgumentType)) "from-edn")]
      `(~from-edn-call (~'get ~'arg ~key)))

    (or (string/starts-with? setterArgumentType "List")
        (string/starts-with? setterArgumentType "java.util.List"))
    (let [type (parse-type setterArgumentType)]
      (if (contains? (:types/enums package) type)
        (let [from-edn-call (symbol (class-dollar-string type) "valueOf")]
          `(~'map ~from-edn-call (~'get ~'arg ~key)))
        (if (contains? (:classes package) type)
          (let [alias (first (class-parts type))
                from-edn-call (symbol alias "from-edn")]
            `(~'map ~from-edn-call (~'get ~'arg ~key)))
          (throw (ex-info "unknown list type" {:key key :field field})))))

    true
    (throw (ex-info (str "unknown field type for setter:" (pr-str setterArgumentType)) {:key key :field field}))))

(defn emit-setter-call
  [package key {:keys [setterMethod] :as field}]
  (let [method-call (symbol (str "." setterMethod))
        value (emit-from-edn-call package key field)]
    `(~'when (~'get ~'arg ~key)
       (~method-call ~'builder ~value))))

(defn emit-accessor-from-edn [package className]
  (let [{:keys [builderSetterFields fields] t ::ana/type :as node} (analyze package className)
        _ (assert (= :accessor t))
        target-class (-> (class-parts className) first symbol)
        setter-fields (into (sorted-map) (select-keys fields builderSetterFields))
        new-builder (symbol (name target-class) "newBuilder")
        new-builder-params (map
                             (fn [{:keys [field] :as m}]
                               (emit-from-edn-call package field m))
                             (:newBuilder node))
        let-body `(~'let [~'builder (~new-builder ~@new-builder-params)]
                    ~@(for [[key field] setter-fields]
                        (emit-setter-call package key field))
                    (.build ~'builder))
        form `(~'defn ~(vary-meta 'from-edn assoc :tag (symbol className))
                [~'arg]
                (gcp.global/strict! ~(:gcp/key node) ~'arg)
                ~let-body)
        src (zp/zprint-str form)]
    (str (subs src 0 5) " ^" target-class (subs src 5))))

(defn emit-to-edn-call
  [package key {:keys [getterMethod getterReturnType] :as field}]
  (let [method-call (symbol (str "." getterMethod))]
    (cond
      (native-type getterReturnType)
      `(~method-call ~'arg)

      (contains? (:types/enums package) getterReturnType)
      `(~(symbol (class-dollar-string getterReturnType) ".name") (~method-call ~'arg))

      (contains? (:classes package) getterReturnType)
      (let [to-edn (symbol (first (class-parts getterReturnType)) "to-edn")]
        `(~to-edn (~method-call ~'arg)))

      (or (string/starts-with? getterReturnType "List")
          (string/starts-with? getterReturnType "java.util.List"))
      (let [t (parse-type getterReturnType)
            to-edn (if (contains? (:types/enums package) t)
                     (symbol (class-dollar-string t) ".name")
                     (if (contains? (:types/accessors package) t)
                       (symbol (first (class-parts t)) "to-edn")
                       (throw (ex-info (str "unimplemented " (pr-str t)) {:field field}))))]
        `(~'mapv ~to-edn (~method-call ~'arg)))

      true
      (throw (ex-info (str "unimplemented type " (pr-str getterReturnType)) {:key key :field field})))))

(defn emit-accessor-to-edn
  [package {:keys [className fields] :as node}]
  (assert (= :accessor (::ana/type node)))
  (let [target-class (-> (class-parts className) first symbol)
        required-keys (map :field (:newBuilder node))
        required-fields (into (sorted-map) (select-keys fields required-keys))
        optional-fields (into (sorted-map) (apply dissoc fields required-keys))
        base-map (into (sorted-map)
                       (map
                         (fn [[key field]]
                           [key (emit-to-edn-call package key field)]))
                       required-fields)
        body `(~'cond-> ~base-map
                ~@(reduce
                    (fn [acc [key field]]
                      (let [add `(~'assoc ~key ~(emit-to-edn-call package key field))]
                        (conj acc `(~'get ~'arg ~key) add)))
                    [] optional-fields))
        form `(~'defn ~'to-edn [~'arg]
                {:post [(gcp.global/strict! ~(:gcp/key node) ~'%)]}
                ~body)]
    (string/replace (zp/zprint-str form) "[arg]" (str "[^" target-class  " arg]"))))

#!----------------------------------------------------------------------------------------------------------------------
#!
#! :types/concrete-unions
#!

(defn emit-concrete-union-from-edn
  [package {:keys [className variantTags] :as node}]
  (let [variantTags (g/coerce [:set :string] variantTags)
        target-class (-> (class-parts className) first symbol)
        body (reduce
               (fn [acc tag]
                 (if-let [variant (get-in node [:tag->class tag])]
                   (let [gcp-key (package-key package variant)
                         alias (first (class-parts variant))
                         from-edn-call (symbol alias "from-edn")]
                     (conj acc `(~'and (~'or (~'= ~tag (~'get ~'arg :type))
                                             (gcp.global/valid? ~gcp-key ~'arg))
                                  (~from-edn-call ~'arg))))
                   (let [method (g/coerce some? (get-in node [:tag->method tag]))
                         meth (symbol (name target-class) (name method))]
                     (conj acc `(~'and (~'= ~tag (~'get ~'arg :type))  ~(list meth))))))
               ['or]
               (sort variantTags))
        body (conj body `(throw (ex-info ~(str "failed to match variant for union " className) {:arg ~'arg :expected ~variantTags})))
        form `(~'defn ~(vary-meta 'from-edn assoc :tag (symbol className))
                [~'arg]
                (gcp.global/strict! ~(:gcp/key node) ~'arg)
                ~(list* body))
        src (zp/zprint-str form)]
    (str (subs src 0 5) " ^" target-class (subs src 5))))

(defn emit-concrete-union-to-edn
  [_ {:keys [className variantTags] :as node}]
  (g/coerce [:map [::ana/type [:= :concrete-union]]] node)
  (let [target-class (-> (class-parts className) first symbol)
        case-body (reduce
                    (fn [acc tag]
                      (if-let [variant (get-in node [:tag->class tag])]
                        (let [alias       (first (class-parts variant))
                              to-edn-call (symbol alias "to-edn")]
                          (conj acc tag `(~'assoc (~to-edn-call ~'arg) :type ~tag)))
                        (conj acc tag {:type tag})))
                    []
                    variantTags)
        get-type (if (= "java.lang.String" (get-in node [:getType :returnType]))
                   `(.getType ~'arg)
                   `(.name (.getType ~'arg)))
        form `(~'defn ~'to-edn [~'arg]
                {:post [(gcp.global/strict! ~(:gcp/key node) ~'%)]}
                (~'case ~get-type ~@case-body))]
    (string/replace (zp/zprint-str form) "[arg]" (str "[^" target-class  " arg]"))))

#!----------------------------------------------------------------------------------------------------------------------
#!
#! :types/abstract-unions
#!

(defn emit-abstract-union-from-edn
  [package {:keys [className variantTags] :as node}]
  (g/coerce [:map [::ana/type [:= :abstract-union]]] node)
  (let [variantTags (g/coerce [:set :string] variantTags)
        target-class (-> (class-parts className) first symbol)
        body (reduce
               (fn [acc tag]
                 (let [variant       (get-in node [:tag->class tag])
                       gcp-key       (package-key package variant)
                       alias         (first (class-parts variant))
                       from-edn-call (symbol alias "from-edn")]
                   (conj acc `(~'and (~'or (~'= ~tag (~'get ~'arg :type))
                                       (gcp.global/valid? ~gcp-key ~'arg))
                                (~from-edn-call ~'arg)))))
               ['or]
               (sort variantTags))
        body (conj body `(throw (ex-info ~(str "failed to match variant for union " className) {:arg ~'arg :expected ~variantTags})))
        form `(~'defn ~(vary-meta 'from-edn assoc :tag (symbol className))
                [~'arg]
                (gcp.global/strict! ~(:gcp/key node) ~'arg)
                ~(list* body))
        src (zp/zprint-str form)]
    (str (subs src 0 5) " ^" target-class (subs src 5))))

(defn emit-abstract-union-to-edn
  [_ {:keys [className variantTags] :as node}]
  (g/coerce [:map [::ana/type [:= :abstract-union]]] node)
  (let [target-class (-> (class-parts className) first symbol)
        case-body (reduce
                    (fn [acc tag]
                      (let [variant (get-in node [:tag->class tag])
                            alias       (first (class-parts variant))
                            to-edn-call (symbol alias "to-edn")]
                        (conj acc tag `(~'assoc (~to-edn-call ~'arg) :type ~tag))))
                    []
                    variantTags)
        form `(~'defn ~'to-edn [~'arg]
                {:post [(gcp.global/strict! ~(:gcp/key node) ~'%)]}
                (~'case ~(if (= "java.lang.String" (get-in node [:getType :returnType]))
                           `(.getType ~'arg)
                           `(.name (.getType ~'arg)))
                  ~@case-body))]
    (string/replace (zp/zprint-str form) "[arg]" (str "[^" target-class  " arg]"))))

#!----------------------------------------------------------------------------------------------------------------------
#! #! :types/static-factories
#!

(defn emit-static-factory-to-edn [package t] (throw (Exception. "unimplemented")))
(defn emit-static-factory-from-edn [package t] (throw (Exception. "unimplemented")))

#!----------------------------------------------------------------------------------------------------------------------

(defn emit-to-edn
  [package className]
  (let [{t ::ana/type :as node} (analyze package className)]
    (case t
      :accessor (emit-accessor-to-edn package node)
      :abstract-union (emit-abstract-union-to-edn package node)
      :concrete-union (emit-concrete-union-to-edn package node)
      :static-factory (emit-static-factory-to-edn package node)
      (throw (Exception. (str "cannot emit from-edn for className '" className "' with type '" t "'"))))))

(defn emit-from-edn
  [package className]
  (let [{t ::ana/type :as node} (analyze package className)]
    (case t
      :accessor (emit-accessor-from-edn package node)
      :abstract-union (emit-abstract-union-from-edn package node)
      :concrete-union (emit-concrete-union-from-edn package node)
      :static-factory (emit-static-factory-from-edn package node)
      (throw (Exception. (str "cannot emit from-edn for className '" className "' with type '" t "'"))))))

#!----------------------------------------------------------------------------------------------------------------------
#!
#! complete binding
#!

(defn emit-binding
  [package className]
  (when (contains? (:types/nested package) className)
    (throw (Exception. (str "cannot emit binding for nested type '" className "'"))))
  (let [ns-form (emit-ns-form package className)
        to-edn (emit-to-edn package className)
        forms (cond-> [ns-form to-edn]
                      (not (contains? (:types/read-only package) className))
                      (conj (emit-from-edn package className)))]
    (string/join "\n\n" forms)))

(defn spit-binding
  ([package target-class]
   (spit-binding package target-class false))
  ([{:keys [root] :as package} target-class overwrite?]
   (let [class-name  (peek (class-parts target-class))
         target-file (io/file root (str class-name ".clj"))]
     (if (and (not overwrite?) (.exists target-file))
       (throw (ex-info "already exists" {:file target-file}))
       (let [src (emit-binding package target-class)]
         (spit target-file src))))))

