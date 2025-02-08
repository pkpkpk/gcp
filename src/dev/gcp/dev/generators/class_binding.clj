(ns gcp.dev.generators.class-binding
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
    [gcp.vertexai.generativeai :as genai]
    [jsonista.core :as j]
    [konserve.core :as k]
    [konserve.filestore :as fs]
    [rewrite-clj.zip :as z]
    [taoensso.telemere :as tt]
    [zprint.core :as zp])
  (:import (com.google.cloud.bigquery BigQueryOptions)
           (java.io ByteArrayOutputStream)))


(comment
  (do (require :reload 'gcp.dev.generators.class-binding) (in-ns 'gcp.dev.generators.class-binding))

  ($extract-type-detail @bigquery "com.google.cloud.bigquery.LoadJobConfiguration") ;=> read-only
  ($extract-type-detail @bigquery "com.google.cloud.bigquery.LoadJobConfiguration.Builder") ;=> builder
  ($extract-type-detail @bigquery "com.google.cloud.bigquery.Acl$Entity$Type") ;=> enum

  ($extract-type-detail-memo @bigquery "com.google.cloud.bigquery.WriteChannelConfiguration")
  ($extract-type-detail-memo @bigquery "com.google.cloud.bigquery.WriteChannelConfiguration.Builder")

  (get-in @bigquery [:discovery :schemas :JobConfigurationQuery :properties :writeDisposition])
  {:description "Optional. Specifies the action that occurs if the destination table already exists. The following values are supported: * WRITE_TRUNCATE: If the table already exists, BigQuery overwrites the data, removes the constraints, and uses the schema from the query result. * WRITE_APPEND: If the table already exists, BigQuery appends the data to the table. * WRITE_EMPTY: If the table already exists and contains data, a 'duplicate' error is returned in the job result. The default value is WRITE_EMPTY. Each action is atomic and only occurs if BigQuery is able to complete the job successfully. Creation, truncation and append actions occur as one atomic update upon job completion.",
   :type "string"}
  )

#!----------------------------------------------------------------------------------------------------------------------

(defn combine-class-accessors
  "combines getter maps from read-only classes with setter maps from builders
   into seq of maps describing the underlying field"
  ([package className]
   (assert (not (builder-like? className)))
   (let [{:keys [instanceMethods staticMethods] :as readonly}  (extract/$extract-type-detail package className)
         {:keys [setterMethods] :as builder} (extract/$extract-type-detail package (str className ".Builder"))
         getters   (remove #(#{"toString" "hashCode" "equals"} (:methodName %)) instanceMethods)
         setters   (into []
                         (comp
                           (remove #(= "build" (:methodName %)))
                           (map #(dissoc % :returnType)))
                         (:instanceMethods builder))
         ;; for every setter there is a getter, but not every getter has a setter

         ;*setters (atom (into {}
         ;                     (map
         ;                       (fn [{:keys [methodName] :as m}]
         ;                         (let [key (string/lower-case (subs methodName 3))]
         ;                           [key (assoc m ::key key)])))
         ;                     setters))

         ;fields (reduce (fn [acc {getterMethod :methodName readDoc :doc :keys [returnType] :as getter}]
         ;                (let [key (string/lower-case (cond-> getterMethod (string/starts-with? getterMethod "get") (subs 3)))]
         ;                  (if-let [{setterMethod :methodName writeDoc :doc :keys [parameters] :as setter}
         ;                           (or (get @*setters key)
         ;                               (reduce (fn [_ [k m]]
         ;                                         (when (string/starts-with? k key)
         ;                                           (reduced m))) nil @*setters))]
         ;                    (let [opt (if (re-seq #"ptional" writeDoc)
         ;                                true
         ;                                (if (re-seq #"equired" writeDoc)
         ;                                  false))]
         ;                      (assert (= 1 (count parameters)))
         ;                      (assert (contains? @*setters (::key setter)))
         ;                      (swap! *setters dissoc (::key setter))
         ;                      (conj acc (cond-> {:getter    getterMethod
         ;                                         :type      returnType
         ;                                         :setter    setterMethod
         ;                                         :readDoc readDoc
         ;                                         :writeDoc writeDoc
         ;                                         :parameter (:name (first parameters))}
         ;                                        (some? opt) (assoc :optional opt))))
         ;                    (conj acc {:getter getterMethod :type returnType}))))
         ;              [] getters)
         ]
     ;(assert (empty? @*setters) (str "expected setters to be exhausted: " @*setters))
     ;fields
     {:getters getters
      :setters setters}
     )))

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

(comment
  (do (require :reload 'gcp.dev.generators.class-binding) (in-ns 'gcp.dev.generators.class-binding))

  (member-methods com.google.cloud.bigquery.LoadJobConfiguration)
  (member-methods com.google.cloud.bigquery.LoadJobConfiguration$Builder)
  (reflect-readonly com.google.cloud.bigquery.LoadJobConfiguration)

  ;com.google.cloud.bigquery.WriteChannelConfiguration

  (def package-schema
    [:map
     [:name :string]
     ;TODO settings & clients
     [:classes    [:seqable :string]]
     [:enums      [:seqable :string]]
     [:exceptions [:seqable :string]]
     [:interfaces [:seqable :string]]])

  (def pubsub-root (io/file src "main" "gcp" "pubsub"))
  (def pubsub-api-doc-base "https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/")
  )

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