(ns gcp.dev.legacy.analyzer)

(comment

  #!--------------------------------------------------------------------------------------------------------
  #!
  #! :types/accessors
  #!

  (defn abstract-variant? [package className]
    (boolean
      (when-let [base (get-in package [:types/variants className])]
        (get-in package [:types/abstract-unions base]))))

  (defn abstract-getters [className]
    (let [{:keys [members]} (reflect (as-class className))]
      (reduce
        (fn [acc m]
          (if (and (contains? m :return-type)
                   (contains? (:flags m) :public)
                   (contains? (:flags m) :abstract)
                   (not (#{'toBuilder} (:name m))))
            (assoc acc (keyword (name (:name m))) {:returnType (get m :returnType)
                                                   :doc (str "abstract method inherited from " className)})
            acc))
        {}
        members)))

  (defn desugar-builders [builders fields]
    (let [field-names (into #{} (map name) (keys fields))]
      ; (println " field-names--> " field-names)
      (reduce
        (fn [acc params]
          (let [param-names (set (map :name params))]
            ; (println " param-names--> " param-names)
            (if (clojure.set/subset? param-names field-names)
              (conj acc params)
              acc)))
        []
        (g/coerce [:vector [:vector [:map [:name :string] [:type :string]]]] builders))))

  (defn select-builder [builders fields]
    (assert (seq builders))
    ;; TODO if theres a no arg builder pick it here
    (or

      ;; desugar & select by name
      (when-let [builders (seq (desugar-builders builders fields))]
        (let [n (apply min (map count builders))]
          (reduce (fn [_ params]
                    (if (= n (count params))
                      (let [params' (into [] (map #(assoc % :field (keyword (:name %)))) params)]
                        (reduced params'))))
                  nil
                  builders)))
      ;; if desugaring fails, invert and select-by-type?
      (let []
        (println "failed!"))))

  (defn analyze-accessor
    "a readonly class with builder, together as a complete binding unit. no associated types"
    [package className]
    (assert (contains? (:types/accessors package) className))
    (assert (not (builder-like? className)))
    (let [{classDoc :doc :keys [getterMethods staticMethods]} (extract/from-src package className)
          setterMethods (extract/from-src package (str className ".Builder"))
          setters  (g/coerce [:seqable :string] (sort (map name (keys setterMethods))))
          getterMethods (cond-> getterMethods
                                (abstract-variant? package className)
                                (merge (abstract-getters (get-in package [:types/variants className]))))
          getters  (g/coerce [:seqable :string] (sort (map name (keys getterMethods))))
          zipped (align/align-accessor-methods package getters setters)
          ; _ (clojure.pprint/pprint zipped)
          min-param-names (when-let [ms (not-empty (get staticMethods :newBuilder))]
                            (let [n (apply min (map count ms))]
                              (into #{} (comp
                                          (filter #(= n (count %)))
                                          (map :name))
                                    (get staticMethods :newBuilder))))
          fields (into (sorted-map)
                       (map
                         (fn [[setter-key getter-key]]
                           (let [{:as setterMethod} (get setterMethods setter-key)
                                 {:as getterMethod} (get getterMethods getter-key)
                                 _ (when (nil? getterMethod)
                                     (throw (ex-info (str "expected getter method for getter " getter-key)
                                                     {:className className
                                                      :setter-key setter-key
                                                      :getter-key getter-key
                                                      :zipped zipped
                                                      :getterMethods getterMethods})))
                                 _ (assert (nil? (:parameters getterMethod)))
                                 _ (when (nil? setterMethod)
                                     (throw (ex-info (str "expected setter method for setter " setter-key)
                                                     {:setter-key         setter-key
                                                      :getter-key         getter-key
                                                      :setter-method-keys (sort (keys setterMethods))
                                                      :zipped             zipped})))
                                 optional (if (string/includes? (get setterMethod :doc "") "ptional")
                                            true
                                            (if (string/includes? (get setterMethod :doc "") "equired")
                                              false
                                              ;; as a best guess, the smallest builder params are required
                                              (not (contains? min-param-names (:name setterMethod)))))
                                 setter-argument-type (if (= 1 (count (get setterMethod :type)))
                                                        (first (get setterMethod :type))
                                                        (if-let [[native :as natives] (not-empty (filter native-type (get setterMethod :type)))]
                                                          (do
                                                            (when-not (= 1 (count natives))
                                                              (throw (ex-info "polymorphic builder method has weird native types" {:setterMethod setterMethod})))
                                                            native)
                                                          (let [ts (get setterMethod :type)]
                                                            (if (and (= 2 (count ts))
                                                                     (or (and (list-like? (nth ts 0))
                                                                              (array-like? (nth ts 1)))
                                                                         (and (list-like? (nth ts 1))
                                                                              (array-like? (nth ts 0)))))
                                                              (or (and (list-like? (nth ts 0)) (nth ts 0))
                                                                  (and (list-like? (nth ts 1)) (nth ts 1)))
                                                              (throw (ex-info "polymorphic builder method has weird non-native types" {:setterMethod setterMethod}))))))
                                 field-key (g/coerce keyword? (keyword (:name setterMethod)))
                                 field-map (cond-> {:setterMethod       (symbol setter-key)
                                                    :getterMethod       (symbol getter-key)
                                                    :optional           optional
                                                    :getterReturnType   (get getterMethod :returnType)
                                                    :setterArgumentType setter-argument-type}

                                                   ;; TODO move doc cleaning upstream, remove newlines & normalize spacing
                                                   (and (some? (get setterMethod :doc))
                                                        (not (string/blank? (get setterMethod :doc))))
                                                   (assoc :setterDoc (get setterMethod :doc))

                                                   (and (some? (get setterMethod :doc))
                                                        (not (string/blank? (get setterMethod :doc))))
                                                   (assoc :getterDoc (get getterMethod :doc)))]
                             [field-key field-map])))
                       zipped)
          new-builder (select-builder (:newBuilder staticMethods) fields)
          _ (println "selected builder:  ")
          _ (pprint new-builder)
          setter-fields (apply disj (into (sorted-set) (keys fields)) (map :field new-builder))
          static-method-types (reduce
                                (fn [acc [_ arg-lists]]
                                  (reduce #(into %1 (map :type) %2) acc arg-lists))
                                #{}
                                staticMethods)
          fields-types (reduce
                         (fn [acc [_ {:keys [setterArgumentType getterReturnType]}]]
                           (conj acc setterArgumentType getterReturnType))
                         #{}
                         fields)
          all-types (reduce (fn [acc item]
                              (into acc (comp
                                          (map parse-type)
                                          (remove native-type))
                                    (if (string? item) [item] item)))
                            #{}
                            (concat static-method-types fields-types))]
      ;;
      ;; TODO still getting nil getter return types
      ;; TODO newBuilder is wrong!
      ;;
      ;; getType returning nil for returnType?????
      ;; typeDependencies OFF
      ;; :type {:setterMethod setType,
      ;;        :getterMethod getType,
      ;;        :optional true,
      ;;        :getterReturnType nil,
      ;;        :setterArgumentType "com.google.cloud.bigquery.TableDefinition.Type"}}
      {::type               :accessor
       :gcp/key             (package-key package className)
       :className           className
       :doc                 classDoc
       :fields              fields
       :newBuilder          new-builder
       :builderSetterFields setter-fields
       :typeDependencies    all-types}))

  #!--------------------------------------------------------------------------------------------------------
  #!
  #! :types/static-factories
  #!

  (defn analyze-static-factory
    [package className]
    (assert (contains? (:types/static-factories package) className))
    (let [{classDoc :doc :keys [getterMethods staticMethods] :as readonly} (extract/from-src package className)
          min-param-names (let [n (apply min (map count (get staticMethods :of)))]
                            (reduce
                              (fn [acc arg-list]
                                (if-not (= n (count arg-list))
                                  acc
                                  (into acc (map :name) arg-list)))
                              #{}
                              (get staticMethods :of)))
          all-static-params (reduce
                              (fn [acc [_ arg-lists]]
                                (reduce #(into %1 (map :name) %2) acc arg-lists))
                              #{}
                              staticMethods)
          zipped (align/align-static-params-to-getters package all-static-params getterMethods)
          fields (into (sorted-map)
                       (map
                         (fn [[param getter-key]]
                           (let [{:as getterMethod} (get getterMethods getter-key)
                                 field (cond-> {:getterMethod (symbol getter-key)
                                                :getterReturnType   (get getterMethod :returnType)
                                                :optional     (not (contains? min-param-names (name param)))}
                                               ;; TODO move doc cleaning upstream, remove newlines & normalize spacing
                                               (and (some? (get getterMethod :doc))
                                                    (not (string/blank? (get getterMethod :doc)))) (assoc :getterDoc (get getterMethod :doc)))]
                             [param field])))
                       zipped)
          static-method-types (reduce
                                (fn [acc [_ arg-lists]]
                                  (reduce #(into %1 (map :type) %2) acc arg-lists))
                                #{}
                                staticMethods)
          all-types (into #{} (comp
                                (map parse-type)
                                (remove native-type))
                          (into static-method-types (map :returnType) (vals getterMethods)))]
      {::type             :static-factory
       :gcp/key           (package-key package className)
       :className         className
       :doc               classDoc
       :fields            fields
       :typeDependencies  all-types
       :staticMethods     staticMethods}))

  #!--------------------------------------------------------------------------------------------------------
  #!
  #! :types/enums
  #!

  (defn analyze-enum [package className]
    (let [{:as detail} (extract/from-src package className)]
      (merge {::type :enum
              :gcp/key (package-key package className)
              :className className}
             detail)))

  #!--------------------------------------------------------------------------------------------------------
  #!
  #! :types/concrete-unions
  #!

  (defn- variant-tags [class-like]
    (let [reflection (reflect (as-class class-like))]
      (reduce (fn [acc {:keys [flags] :as m}]
                (if (and (contains? flags :final)
                         (= (name (:name m))
                            (string/upper-case (name (:name m)))))
                  (let [fld (.getDeclaredField (as-class class-like) (name (:name m)))]
                    (.setAccessible fld true)
                    (conj acc (.get fld nil)))
                  acc))
              #{}
              (:members reflection))))

  (defn- analyze-concrete-union
    "concrete unions have static methods for variants that do no have their own subclass"
    [package className]
    (let [{:keys [doc getterMethods]} (extract/from-src package className)
          variant-classes (g/coerce [:seqable :string] (get-in package [:types/concrete-unions className]))
          tags (variant-tags className)
          zipped (align/align-variant-class-to-variant-tags package variant-classes tags)
          classless-tags (apply disj tags (vals zipped))
          static-method-names (map (comp name :name) (public-instantiators className))
          classless-methods (align/align-variant-tags-to-static-methods package classless-tags static-method-names)]
      {::type            :concrete-union
       :gcp/key          (package-key package className)
       :className        className
       :doc              doc
       :typeDependencies variant-classes
       :variantTags      tags
       :class->tag       zipped
       :tag->class       (clojure.set/map-invert zipped)
       :tag->method classless-methods
       :classlessTags    classless-tags
       :getType (g/coerce some? (get getterMethods :getType))}))

  #!--------------------------------------------------------------------------------------------------------
  #!
  #! :types/abstract-unions
  #!

  (defn- analyze-abstract-union
    "abstract unions have subclasses for all variants"
    [package className]
    (let [{:keys [getterMethods doc]} (extract/from-src package className)
          variant-classes (g/coerce [:seqable :string] (get-in package [:types/abstract-unions className]))
          {:keys [returnType] :as getType} (g/coerce some? (get getterMethods :getType))
          _               (assert (contains? (:types/enums package) returnType) "expected enum type as returnType for abstract union .getType()")
          tags            (set (enum-values returnType))
          zipped          (align/align-variant-class-to-variant-tags package variant-classes tags)]
      {::type          :abstract-union
       :gcp/key        (package-key package className)
       :className      className
       :doc            (clean-doc doc)
       :variantClasses variant-classes
       :typeDependencies variant-classes
       :variantTags    tags
       :class->tag     zipped
       :tag->class     (clojure.set/map-invert zipped)
       :getType        getType}))

  #!--------------------------------------------------------------------------------------------------------

  (defn analyze [package className]
    {:post [(contains? % :gcp/key)
            (contains? % ::type)
            (contains? % :className)
            (string? (get % :className))]}
    (cond
      (contains? (:types/abstract-unions package) className)
      (analyze-abstract-union package className)

      (contains? (:types/concrete-unions package) className)
      (analyze-concrete-union package className)

      (contains? (:types/enums package) className)
      (analyze-enum package className)

      (contains? (:types/static-factories package) className)
      (analyze-static-factory package className)

      ; (contains? (:types/accessors package) className)
      ; (analyze-accessor package className)

      true
      (throw (Exception. (str "cannot analyze unknown type! " className))))))
