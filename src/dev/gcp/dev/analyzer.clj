(ns gcp.dev.analyzer
  (:require
    [clj-http.client :as http]
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.repl :refer :all]
    [clojure.set :as s]
    [clojure.string :as string]
    gcp.bigquery.v2
    [gcp.dev.extract :as extract]
    [gcp.dev.models :as models]
    [gcp.dev.packages :as packages]
    [gcp.dev.store :as store]
    [gcp.dev.util :as util :refer [as-dot-string as-class as-dollar-string builder-like?]]
    [gcp.global :as g]
    gcp.vertexai.v1
    [rewrite-clj.zip :as z]
    [zprint.core :as zp :refer [zprint]])
  (:import (com.google.cloud.bigquery BigQueryOptions)
           (java.io ByteArrayOutputStream)))

(defn zip-static-params [package static-params getterMethods]
  (let [getters           (g/coerce [:seqable :string] (vec (sort (map name (keys getterMethods)))))
        getter-schema     {:type        "STRING"
                           :nullable    false
                           :enum getters
                           :description "the name of the read only method that corresponds to the param"}
        generationConfig  {:responseMimeType "application/json"
                           :responseSchema   {:type       "OBJECT"
                                              :required   (vec static-params)
                                              :properties (into {} (map #(vector % getter-schema)) static-params)}}
        systemInstruction "for each parameter name, produce the getter method that produces it"
        cfg               (assoc models/flash :systemInstruction systemInstruction
                                              :generationConfig generationConfig)
        validator!        (fn [edn]
                            (if (not= (set static-params) (set (map name (keys edn))))
                              (throw (ex-info "missing static param!"
                                              {:getters getters :static-params static-params :edn edn}))
                              (if (not (every? some? (vals edn)))
                                (throw (ex-info "expected getter for every static param"
                                                {:getters getters :static-params static-params :edn edn}))
                                (if-not (clojure.set/subset? (set (vals edn)) (into #{} getters))
                                  (throw (ex-info "incorrect keys" {:edn     edn
                                                                    :getters getters
                                                                    :static-params static-params}))))))
        res               (store/generate-content-aside (:store package) cfg getters validator!)]
    (into (sorted-map) (map (fn [[k v]] [k (keyword v)])) res)))

(defn analyze-static-factory
  [package className]
  (assert (contains? (:types/static-factories package) className))
  (let [{classDoc :doc :keys [getterMethods staticMethods] :as readonly} (extract/$extract-type-detail package className)
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
        zipped (zip-static-params package all-static-params getterMethods)
        fields (into (sorted-map)
                     (map
                       (fn [[param getter-key]]
                         (let [{:as getterMethod} (get getterMethods getter-key)]
                           [param
                            {:getterDoc    (get getterMethod :doc)
                             :getterMethod (symbol getter-key)
                             :returnType   (get getterMethod :returnType)
                             :optional     (not (contains? min-param-names (name param)))}])))
                     zipped)
        static-method-types (reduce
                              (fn [acc [_ arg-lists]]
                                (reduce #(into %1 (map :type) %2) acc arg-lists))
                              #{}
                              staticMethods)
        all-types (into #{} (comp
                              (map util/parse-type)
                              (remove util/native-type))
                        (into static-method-types (map :returnType) (vals getterMethods) ))]
    {::type             :static-factory
     :className         className
     :doc               classDoc
     :fields            fields
     :type-dependencies all-types
     :staticMethods     staticMethods}))

(defn zip-accessors
  ([package getterMethods setterMethods]
   (let [setters           (g/coerce [:seqable :string] (sort (map name (keys setterMethods))))
         getters           (g/coerce [:seqable :string] (sort (map name (keys getterMethods))))
         getter-schema     {:type        "STRING"
                            :nullable    false
                            :description "the read only method that corresponds to the setter"}
         generationConfig  {:responseMimeType "application/json"
                            :responseSchema   {:type       "OBJECT"
                                               :required   setters
                                               :properties (into {} (map #(vector % getter-schema)) setters)}}
         systemInstruction (str "given a list of getters, match them to their setter in the response schema."
                                "for every setter there is a getter, but not every getter has a setter")
         cfg               (assoc models/flash :systemInstruction systemInstruction
                                               :generationConfig generationConfig)
         validator!        (fn [edn]
                             (if (not= (set setters) (set (map name (keys edn))))
                               (throw (ex-info "missing setter method!" {:getters getters
                                                                         :setters setters
                                                                         :edn edn}))
                               (if (not (every? some? (vals edn)))
                                 (throw (ex-info "expected getter for every setter" {:getters getters
                                                                                     :setters setters
                                                                                     :edn edn}))
                                 (if-not (clojure.set/subset? (set (vals edn)) (into #{} getters))
                                   (throw (ex-info "incorrect keys" {:getters getters
                                                                     :setters setters
                                                                     :edn edn}))))))
         res (store/generate-content-aside (:store package) cfg getters validator!)]
     (into (sorted-map) (map (fn [[k v]] [k (keyword v)])) res))))

(defn analyze-accessor
   "a readonly class with builder, together as a complete binding unit. no associated types"
  [package className]
  (assert (contains? (:types/accessors package) className))
  (let [{classDoc :doc :keys [getterMethods staticMethods] :as readonly} (extract/$extract-type-detail package className)
        {:keys [setterMethods] :as builder} (extract/$extract-type-detail package (str className ".Builder"))
        zipped (zip-accessors package getterMethods setterMethods)
        min-param-names (let [n (apply min (map count (get staticMethods :newBuilder)))]
                          (into #{} (comp
                                      (filter #(= n (count %)))
                                      (map :name))
                                (get staticMethods :newBuilder)))
        fields (into (sorted-map)
                     (map
                       (fn [[setter-key getter-key]]
                         (let [{:as setterMethod} (get setterMethods setter-key)
                               {:as getterMethod} (get getterMethods getter-key)]
                           (assert (some? setterMethod))
                           (assert (some? getterMethod))
                           (assert (nil? (:parameters getterMethod)))
                           [(keyword (:name setterMethod))
                            {:setterDoc    (get setterMethod :doc)
                             :setterMethod (symbol setter-key)
                             :getterDoc    (get getterMethod :doc)
                             :getterMethod (symbol getter-key)
                             :optional     (if (string/includes? (get setterMethod :doc "") "ptional")
                                             true
                                             (if (string/includes? (get setterMethod :doc "") "equired")
                                               false
                                               ;; as a best guess, the smallest builder params are required
                                               ;; todo fuzzing to be more empirical
                                               (not (contains? min-param-names (:name setterMethod)))))
                             :returnType   (get getterMethod :returnType)
                             :type         (get setterMethod :type)}])))
                     zipped)
        sugar-free-builders (reduce
                              (fn [acc params]
                                (let [names (set (map :name params))]
                                  (if (clojure.set/subset? names (into #{} (map name) (keys fields)))
                                    (conj acc params)
                                    acc)))
                              []
                              (get staticMethods :newBuilder))
        new-builder (let [n (apply min (map count sugar-free-builders))]
                      (reduce (fn [_ params]
                                (if (= n (count params))
                                  (let [params' (into [] (map #(assoc % :field (keyword (:name %)))) params)]
                                    (reduced params'))))
                              nil
                              sugar-free-builders))
        setter-fields (apply disj (into (sorted-set) (keys fields)) (map :field new-builder))
        static-method-types (reduce
                              (fn [acc [_ arg-lists]]
                                (reduce #(into %1 (map :type) %2) acc arg-lists))
                              #{}
                              staticMethods)
        fields-types (reduce (fn [acc [_ {:keys [type returnType]}]] (conj acc type returnType)) #{} fields)
        all-types (into #{} (comp
                              (map util/parse-type)
                              (remove util/native-type))
                        (into static-method-types fields-types))]
    {::type               :accessor
     :className           className
     :doc                 classDoc
     :fields              fields
     :newBuilder          new-builder
     :builderSetterFields setter-fields
     :typeDependencies    all-types}))

(defn analyze-enum [package className] (throw (Exception. "unimplemented")))

(defn analyze [package className]
  (cond
    (contains? (:types/static-factories package) className)
    (analyze-static-factory package className)

    (contains? (:types/accessors package) className)
    (analyze-accessor package className)

    (contains? (:types/enums package) className)
    (analyze-enum package className)))

(defn malli-accessor
  [package {:keys [className doc fields] :as accessor}]
  (assert (= :accessor (::type accessor)))
  (into [:map {:key (util/package-key package className)
               :closed true
               :doc    doc
               :class  (symbol className)}]
        (map
          (fn [[k v]]
            [k
             (dissoc v :returnType :type :setterMethod :getterMethod)
             (util/->malli-type package (:type v))]))
        fields))

(defn malli-static-factory
  [package {:keys [className doc fields] :as static-factory}]
  (assert (= :static-factory (::type static-factory)))
  (into [:map {:key (util/package-key package className)
               :closed true
               :doc    doc
               :class  (symbol className)}]
        (map
          (fn [[k v]]
            [k
             (dissoc v :returnType :type :setterMethod :getterMethod)
             (util/->malli-type package (:returnType v))]))
        fields))

(defn malli-enum [package enum] (throw (Exception. "unimplemented")))

(defn malli [package class]
  (let [{type ::type :as t} (analyze package class)]
    (case type
      :accessor (malli-accessor package t)
      :enum (malli-enum package t)
      :static-factory (malli-static-factory package t))))

(comment
  (do (require :reload 'gcp.dev.analyzer) (in-ns 'gcp.dev.analyzer))
  (analyze bigquery "com.google.cloud.bigquery.LoadJobConfiguration") ;=> accessor
  (malli bigquery "com.google.cloud.bigquery.LoadJobConfiguration")
  (analyze bigquery "com.google.cloud.bigquery.WriteChannelConfiguration") ;=> accessor
  (malli bigquery "com.google.cloud.bigquery.WriteChannelConfiguration")
  (analyze bigquery "com.google.cloud.bigquery.FormatOptions") ;=> static-factory
  (malli bigquery "com.google.cloud.bigquery.FormatOptions")
  (analyze bigquery "com.google.cloud.bigquery.Acl.Entity.Type")
  (malli bigquery "com.google.cloud.bigquery.Acl.Entity.Type")
  ;(get-in @bigquery [:discovery :schemas :JobConfigurationQuery :properties :writeDisposition])
  ;{:type "string" :description "Optional. Specifies the action that occurs if the destination table already exists. The following values are supported: * WRITE_TRUNCATE: If the table already exists, BigQuery overwrites the data, removes the constraints, and uses the schema from the query result. * WRITE_APPEND: If the table already exists, BigQuery appends the data to the table. * WRITE_EMPTY: If the table already exists and contains data, a 'duplicate' error is returned in the job result. The default value is WRITE_EMPTY. Each action is atomic and only occurs if BigQuery is able to complete the job successfully. Creation, truncation and append actions occur as one atomic update upon job completion."}
  )

(defn sibling-require
  [{:keys [rootNs] :as package} className]
  (assert (some? rootNs))
  (let [req (symbol (str (name rootNs) "." className))
        alias (symbol className)]
    [req :as alias]))

(defn emit-ns-form
  [{:keys [rootNs packageName] :as package} target]
  (assert (string/starts-with? target "com.google.cloud"))
  (assert (contains? (set (:classes package)) target))
  (let [{:keys [typeDependencies]} (analyze package target)
        [siblings other] (split-with #(string/starts-with? % (str "com.google.cloud." packageName)) typeDependencies)
        {siblings             false
         target-package-enums true} (group-by #(contains? (:types/enums package) %) siblings)
        sibling-parts   (into (sorted-set) (comp (map util/class-parts) (map first)) siblings)
        _               (when (seq other)
                          ;; TODO
                          ;; dependencies could be any class, google cloud or threetenbp usually if any
                          ;; get siblings by comparing w/ package classes
                          (throw (ex-info "unimplemented requires for non-sibling dependencies" {:target-class target
                                                                                                 :other-deps   other})))
        [target-package target-class] ((juxt #(symbol (string/join "." (butlast %))) #(symbol (last %))) (util/dot-parts target))
        requires        (into ['[gcp.global :as g]] (map (partial sibling-require package)) sibling-parts)
        package-imports (cond-> [target-package target-class]
                                (seq target-package-enums) (into (comp (map util/class-dollar-string) (map symbol)) target-package-enums))
        imports         [package-imports]]
    `(~'ns ~(symbol (str rootNs "." (peek (util/class-parts target))))
       (:import ~@(sort-by first imports))
       (:require ~@(sort-by first requires)))))

(defn emit-enum-from-edn [package t] (throw (Exception. "unimplemented")))
(defn emit-static-factory-from-edn [package t] (throw (Exception. "unimplemented")))

(defn emit-from-edn-call
  [package key {:keys [type] :as field}]
  (cond
    (boolean (util/native-type type))
    `(~'get ~'arg ~key)

    (contains? (:types/enums package) type)
    (let [from-edn-call (symbol (util/class-dollar-string type) "valueOf")]
      `(~from-edn-call (~'get ~'arg ~key)))

    (contains? (:classes package) type)
    (let [from-edn-call (symbol (first (util/class-parts type)) "from-edn")]
      `(~from-edn-call (~'get ~'arg ~key)))

    (string/starts-with? type "List")
    (let [type (util/parse-type type)]
      (if (contains? (:types/enums package) type)
        (let [from-edn-call (symbol (util/class-dollar-string type) "valueOf")]
          `(~'map ~from-edn-call (~'get ~'arg ~key)))
        (if (contains? (:classes package) type)
          (let [alias (first (util/class-parts type))
                from-edn-call (symbol alias "from-edn")]
            `(~'map ~from-edn-call (~'get ~'arg ~key)))
          (throw (ex-info "unknown list type" {:key key :field field})))))

    true
    (throw (ex-info "unknown field type for setter" {:key key :field field}))))

(defn emit-setter-call
  [package key {:keys [setterMethod type] :as field}]
  (let [method-call (symbol (str "." setterMethod))
        value (emit-from-edn-call package key field)]
    `(~'when (~'get ~'arg ~key)
       (~method-call ~'builder ~value))))

(defn emit-accessor-from-edn
  [package className]
  (let [{:keys [builderSetterFields fields] t ::type :as node} (analyze package className)
        _ (assert (= :accessor t))
        target-class (-> (util/class-parts className) first symbol)
        setter-fields (into (sorted-map) (select-keys fields builderSetterFields))

        new-builder (symbol (name target-class) "newBuilder")
        new-builder-params (map
                             (fn [{:keys [name type field] :as m}]
                               (emit-from-edn-call package field m))
                             (:newBuilder node))
        let-body `(~'let [~'builder (~new-builder ~@new-builder-params)]
                    ~@(for [[key field] setter-fields]
                        (emit-setter-call package key field))
                    (.build ~'builder))]
    ;; TODO global/strict!
    `(~'defn ~(vary-meta 'from-edn assoc :tag (symbol className)) [~'arg] ~let-body)))

;(str "(defn ^" (symbol (last parts)) " from-edn [arg] (throw (Exception. \"unimplemented\")))")

(defn emit-from-edn
  [package className]
  (let [{t ::type} (analyze package className)]
    (case t
      :accessor (emit-accessor-from-edn package className)
      :enum (emit-enum-from-edn package className)
      :static-factory (emit-static-factory-from-edn package className))))

;(zp/zprint-str (apply str ns-forms) 80 {:parse-string-all? true :parse {:interpose "\n\n"}})
;(str "(defn to-edn [^" (symbol (last parts)) " arg] (throw (Exception. \"unimplemented\")))")

;; TODO
;; Validity Testing
;; -- valid edn
;; -- top-level from-edn to-edn with annotations
;; -- namespace skeleton should eval ok
;; -- ...clj-kondo?
;;
;; => com.google.cloud.ServiceOptions

; TODO there are enum bindings in vertexai at least that can probably be killed
;(defn missing-files [package src-root]
;  (let [expected-binding-names (into (sorted-set) (map first) (map util/class-parts (:classes package)))
;        expected-files (map #(io/file src-root (str % ".clj")) expected-binding-names)]
;    (remove #(.exists %) expected-files)))

;(defn spit-skeleton
;  [{:keys [root] :as package} target-class]
;  (let [class-name (peek (util/class-parts target-class))
;        target-file (io/file root (str class-name ".clj"))]
;    (if (.exists target-file)
;      (throw (ex-info "already exists" {:file target-file}))
;      (let [src (class-binding-skeleton package target-class)]
;        (spit target-file src)))))

