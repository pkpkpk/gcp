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
    [taoensso.telemere :as tt]
    [zprint.core :as zp])
  (:import (com.google.cloud.bigquery BigQueryOptions)
           (java.io ByteArrayOutputStream)))

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

;(defn type-dependencies
;  ([node]
;   (type-dependencies #{} node))
;  ([init {:keys [staticMethods instanceMethods isBuilder]}]
;   (reduce
;     (fn [acc {:keys [parameters returnType]}]
;       #_(let [acc (into acc (comp (map :type) (map util/parse-type) (remove util/native-type)) parameters)
;             ret (util/parse-type returnType)]
;         (if (util/native-type ret)
;           acc
;           (conj acc ret))))
;     init
;     (into staticMethods instanceMethods))))

(defn analyze-accessor
   "a complete binding unit, no associated types"
  [package className]
  (assert (contains? (set (:simple-accessor package)) className))
  (let [{classDoc :doc :keys [getterMethods staticMethods] :as readonly} (extract/$extract-type-detail package className)
        {:keys [setterMethods] :as builder} (extract/$extract-type-detail package (str className ".Builder"))
        zipped (zip-accessors package getterMethods setterMethods)
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
                             :returnType   (get getterMethod :returnType)
                             :type         (get setterMethod :type )}])))
                     zipped)
        static-method-types (reduce
                              (fn [acc [_ arg-lists]]
                                (reduce #(into %1 (map :type) %2) acc arg-lists))
                              #{}
                              staticMethods)
        fields-types (into #{}
                           (comp
                             (map util/parse-type)
                             (remove util/native-type))
                           (reduce (fn [acc [_ {:keys [type returnType]}]] (conj acc type returnType)) #{} fields))]
    ;; TODO dependencies, replace types w/ malli ident
    {::type :simple-accessor
     :className className
     :doc classDoc
     :fields fields
     :staticMethods staticMethods
     :type-dependencies (into static-method-types fields-types)}))

(defn analyze-enum [package className] (throw (Exception. "unimplemented")))

(defn analyze-type [package className]
  (cond
    (contains? (:simple-accessor package) className)
    (analyze-accessor package className)

    (contains? (:enums package) className)
    (analyze-enum package className)))

(comment
  (do (require :reload 'gcp.dev.analyzer) (in-ns 'gcp.dev.analyzer))
  (analyze-type bigquery "com.google.cloud.bigquery.LoadJobConfiguration")
  (analyze-type bigquery "com.google.cloud.bigquery.WriteChannelConfiguration")
  (analyze-type bigquery "com.google.cloud.bigquery.Acl.Entity.Type")
  ;(get-in @bigquery [:discovery :schemas :JobConfigurationQuery :properties :writeDisposition])
  ;{:type "string" :description "Optional. Specifies the action that occurs if the destination table already exists. The following values are supported: * WRITE_TRUNCATE: If the table already exists, BigQuery overwrites the data, removes the constraints, and uses the schema from the query result. * WRITE_APPEND: If the table already exists, BigQuery appends the data to the table. * WRITE_EMPTY: If the table already exists and contains data, a 'duplicate' error is returned in the job result. The default value is WRITE_EMPTY. Each action is atomic and only occurs if BigQuery is able to complete the job successfully. Creation, truncation and append actions occur as one atomic update upon job completion."}
  )

#!----------------------------------------------------------------------------------------------------------------------

(defn malli-from-class-doc [package class]
  #_
  (let [readonly ($extract-from-class-doc-memo package class)
        fields   (extract-type package class)]
    (into [:map {:closed true
                 :class (symbol (:className readonly))}]
          (map
            (fn [{:keys [getter setter type optional parameter readDoc writeDoc] :as field}]
              (let [t (->malli-type package type)
                    opts (cond-> nil
                                 (nil? setter)    (assoc :readOnly true)
                                 (nil? getter)    (assoc :writeOnly true)
                                 (some? readDoc)  (assoc :readDoc readDoc)
                                 (some? writeDoc) (assoc :writeDoc writeDoc)
                                 (some? optional) (assoc :optional optional))
                    key (if (some? parameter)
                          (keyword parameter)
                          (let [_ (when (or (nil? getter) (string/blank? getter)) (throw (ex-info "bad getter!" {:field field})))
                                param-capped (subs getter 3)
                                param        (str (string/lower-case (subs param-capped 0 1)) (subs param-capped 1))]
                            (keyword param)))]
                (if opts [key opts t] [key t]))))
          fields)))

(defn ?discovery-schema->malli
  [package class-key]
  (when-let [{:keys [description properties id]} (or (get-in package [:discovery :schemas class-key])
                                                     (get-in package [:discovery :schemas (keyword class-key)]))]
    (into [:map {:closed true
                 :doc    description
                 :id     id}]
          (map
            (fn [[key {:keys [type description] :as prop}]]
              (let [opts (-> prop
                             (dissoc :description :type)
                             (assoc :doc description))
                    opts (cond-> opts
                                (string/starts-with? description "Optional")
                                (assoc :optional true))]
                [key opts (util/->malli-type package type)])))
          (sort-by key properties))))





#!----------------------------------------------------------------------------------------------------------------------



;; TODO
;; context, siblings classes as reference
;; registry as content
;; style guide
;; generate namespace skeleton: imports. from-edn to-edn outline
;; sibling files
;; similar namespaces
;; target ns
;; class name
;; class members
;; Validity Testing
;; -- valid edn
;; -- top-level from-edn to-edn with annotations
;; -- namespace skeleton should eval ok
;; -- ...clj-kondo?
;; cache by model-cfg & content hasch
;; TODO there are enum bindings in vertexai at least that can probably be killed
;(defn missing-files [package src-root]
;  (let [expected-binding-names (into (sorted-set) (map first) (map package-parts (:classes package)))
;        expected-files (map #(io/file src-root (str % ".clj")) expected-binding-names)]
;    (remove #(.exists %) expected-files)))

;; => com.google.cloud.ServiceOptions


(defn sibling-require
  [{:keys [rootNs] :as package} className]
  (let [req (symbol (str (name rootNs) "." className))
        alias (symbol className)]
    [req :as alias]))

;;; TODO possible circular deps
;;; LoadJobConfiguration produced require for JobInfo because of JobInfo$WriteDisposition
;;; --> in QueryJob & CopyJob bindings import JobInfo$WriteDisposition (its just an enum)
;;; fix == check if enums and manually import that class$enum?
;;; this probably means crawling dependency tree
(defn class-binding-skeleton
  [{:keys [rootNs packageRootUrl packageName] :as package} target-class]
  (assert (string/starts-with? target-class "com.google.cloud"))
  (assert (contains? (set (:classes package)) target-class))
  #_(let [{:keys [className]
         :as cdec} ($extract-from-class-doc-memo package target-class)
        dependencies (into #{} (remove #(or (string/includes? % target-class)
                                            (string/ends-with? % "Builder")))
                           (type-dependencies cdec))
        [siblings other] (split-with #(string/starts-with? % (str "com.google.cloud." packageName)) dependencies)
        ;; dependencies could be any class, google cloud or threetenbp usually if any
        ;; get siblings by comparing w/ package classes
        sibling-parts (into (sorted-set) (comp (map class-parts) (map first)) siblings)
        _ (when (seq other)
            (throw (ex-info "unimplemented requires for non-sibling dependencies" {:target-class target-class
                                                                                   :other-deps other})))
        parts        (dot-parts target-class)
        requires     (into ['[gcp.global :as g]] (map (partial sibling-require package)) sibling-parts)
        target-ns (symbol (str rootNs "." (peek (class-parts className))))
        ns-forms [`(~'ns ~target-ns
                     (:import [~(symbol (string/join "." (butlast parts))) ~(symbol (last parts))])
                     (:require ~@(sort-by first requires)))
                  (str "(defn ^" (symbol (last parts)) " from-edn [arg] (throw (Exception. \"unimplemented\")))")
                  (str "(defn to-edn [^" (symbol (last parts)) " arg] (throw (Exception. \"unimplemented\")))")]]
    (zp/zprint-str (apply str ns-forms) 80 {:parse-string-all? true :parse {:interpose "\n\n"}})))

(defn spit-skeleton
  [{:keys [root] :as package} target-class]
  (let [class-name (peek (util/class-parts target-class))
        target-file (io/file root (str class-name ".clj"))]
    (if (.exists target-file)
      (throw (ex-info "already exists" {:file target-file}))
      (let [src (class-binding-skeleton package target-class)]
        (spit target-file src)))))

(defn var-editor [v]
  ;; TODO validate, reload, test etc
  ;; TODO getText, setText, undo/redo/tx/history
  (if-not (var? v)
    (throw (ex-info "must pass var" {:v v}))
    (let [{:keys [file line column name]} (meta v)
          state (atom {:file file
                       :var  name
                       :zloc (z/of-file file {:track-position? true})})]
      (fn [new-src]
        (let [zloc (:zloc @state)
              found-zloc (z/find zloc z/next
                                 #(when-let [pos (z/position %)]
                                    (= [line column] pos)))
              new-node (z/node (z/of-string new-src))
              updated-zloc (some-> found-zloc
                                   (z/replace new-node)
                                   z/up)]
          (when updated-zloc
            (spit file (z/root-string updated-zloc))
            (swap! state assoc :zloc updated-zloc)))))))