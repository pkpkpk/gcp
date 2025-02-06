(ns gcp.dev.generators.class-binding
  (:refer-clojure :exclude [memoize])
  (:require
    [clj-http.client :as http]
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.repl :refer :all]
    [clojure.set :as s]
    [clojure.string :as string]
    gcp.bigquery.v2
    gcp.vertexai.v1
    [gcp.global :as g]
    [gcp.vertexai.generativeai :as genai]
    [jsonista.core :as j]
    [taoensso.telemere :as tt]
    [zprint.core :as zp]
    [rewrite-clj.zip :as z])
  (:import (java.io ByteArrayOutputStream)))

#_ (do (require :reload 'gcp.dev.generators.class-binding) (in-ns 'gcp.dev.generators.class-binding))
(def home (io/file (System/getProperty "user.home")))
(def src (io/file home "pkpkpk/gcp/src"))

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

(defn memoize [f] (clojure.core/memoize f)) ;; TODO introduce konserve here

(defn get-url-bytes [^String url]
  (tt/log! (str "fetching url -> " url))
  (let [{:keys [status body] :as response} (http/get url {:redirect-strategy :none :as :byte-array})]
    (if (= 200 status) ;google docs will 301 on missing doc
      body
      (throw (ex-info "expected 200 response" {:url url
                                               :response response})))))

(defonce get-url-bytes-memo (memoize get-url-bytes))

#!----------------------------------------------------------------------------------------------------------------------

;https://cloud.google.com/vertex-ai/generative-ai/docs/thinking-mode
;Gemini 2.0 Flash Thinking Mode
;; Thinking Mode is an experimental model and has the following limitations:
;; 32k token input limit
;; Text and image input only
;; 8k token output limit
;; Text only output
;; No built-in tool usage like Search or code execution

(def flash    {:model "gemini-1.5-flash"})
(def pro      {:model "gemini-1.5-pro"})
(def pro-2    {:model "gemini-2.0-pro-exp-02-05"})
(def flash-2  {:model "gemini-2.0-flash-exp"})
(def thinking {:model "gemini-2.0-flash-thinking-exp-01-21"})

#!----------------------------------------------------------------------------------------------------------------------

(defn as-class [class-like]
  {:post [(class? %)]}
  (if (class? class-like)
    class-like
    (if (string? class-like)
      (if (string/ends-with? class-like ".Builder")
        (let [parts (string/split class-like #"\.")
              sym (symbol (str (string/join "." (butlast parts)) "$Builder"))]
          (resolve sym))
        (if (string/ends-with? class-like "$Builder")
          (resolve (symbol class-like))
          (resolve (symbol class-like))))
      (throw (Exception. "unknown type for class-like")))))

(defn builder-like? [class-like]
  (if (class? class-like)
    (builder-like? (str class-like))
    (string/ends-with? (name class-like) "Builder")))

(defn members [class-like]
  (let [clazz (as-class class-like)]
    (assert (class? clazz) (str "got type " (type clazz) " instead"))
    (into []
          (comp
            (remove
              (fn [{:keys [flags :return-type]}]
                (or (contains? flags :private)
                    (contains? flags :synthetic)
                    (empty? flags)
                    (nil? return-type))))
            (map
              (fn [{:keys [parameter-types exception-types] :as m}]
                (cond-> m
                        (empty? parameter-types) (dissoc m :parameter-types)
                        (empty? exception-types) (dissoc m :exception-types)))))
          (sort-by :name (:members (clojure.reflect/reflect clazz))))))

(defn reflect-readonly [class-like]
  (assert (not (builder-like? class-like)))
  (let [members (members class-like)]
    (reduce
      (fn [acc {:keys [flags return-type name parameter-types]}]
        (if (and (contains? flags :static)
                 (not (#{"fromPb" "builder"} (clojure.core/name name))))
          (assoc-in acc [:staticMethods name] {:parameters parameter-types
                                               :returnType return-type})
          (if (and (not (contains? flags :static))
                   (not (#{"hashCode" "equals" "toBuilder"} (clojure.core/name name))))
            (assoc-in acc [:instanceMethods name]
                      (cond-> {:returnType return-type}
                              (seq parameter-types) (assoc :parameters parameter-types)))
            acc)))
      {:staticMethods (sorted-map)
       :instanceMethods (sorted-map)}
      members)))

#!----------------------------------------------------------------------------------------------------------------------

(def package-response-schema
  {:type "OBJECT"
   :properties {"classes"
                {:type "ARRAY"
                 :items {:type "STRING"
                         :description "package qualified class name"}}
                "enums"
                {:type "ARRAY"
                 :items {:type "STRING"
                         :description "package qualified enum name"}}
                "exceptions"
                {:type "ARRAY"
                 :items {:type "STRING"
                         :description "package qualified exception name"}}
                "interfaces"
                {:type "ARRAY"
                 :items {:type "STRING"
                         :description "package qualified interface name"}}}})

(defn $extract-package-summary
  ([package-url]
   ($extract-package-summary flash package-url))
  ([model package-url]
   (let [bytes (get-url-bytes-memo package-url)]
     (tt/log! (str "$extract-package-summary -> " package-url))
     (-> model
         (assoc :systemInstruction (str "You are given google cloud java package summary page"
                                        "Extract its parts into arrays.")
                :generationConfig {:responseMimeType "application/json"
                                   :responseSchema   package-response-schema})
         (genai/generate-content {:parts [{:mimeType "text/html" :partData bytes}]})
         genai/response-json))))

(defonce $extract-package-summary-memo (memoize $extract-package-summary))

#!----------------------------------------------------------------------------------------------------------------------
#!
#! extract builder setters
#!

(defn builder-setter-members [class-like]
  (let [clazz (as-class class-like)]
    (assert (class? clazz) (str "expected class got type " (type clazz) " instead"))
    (into []
          (comp
            (map :name)
            (map name)
            (filter #(string/starts-with? % "set")))
          (members clazz))))

(defn $extract-builder-setters
  "=> {:setterName {:name 'paramName' :type 'java.lang.Long'}}"
  ([model package className]
   (assert (builder-like? className))
   (let [java-doc-url      (str (g/coerce some? (:packageRootUrl package)) className)
         bytes             (get-url-bytes-memo java-doc-url)
         members           (builder-setter-members className)
         systemInstruction (str "extract the builder setters methods description and parameter type"
                                "please use fully qualified package names for all types that reference gcp sdks")
         method-schema     {:type        "OBJECT"
                            :nullable    true
                            :description "fully qualified setter parameter if any"
                            :properties  {"type" {:type        "STRING"
                                                  :description "the fully qualified type descriptor. if a list, use List<$TYPE> syntax"}
                                          "doc"  {:type "STRING"
                                                  :description "description of the method"}
                                          "name" {:type        "STRING"
                                                  :description "the name of the parameter"}}}
         generationConfig  {:responseMimeType "application/json"
                            :responseSchema   {:type       "OBJECT"
                                               :required   members
                                               :properties (into {} (map #(vector % method-schema)) members)}}
         cfg               (assoc model :systemInstruction systemInstruction
                                        :generationConfig generationConfig)
         response          (genai/generate-content cfg {:parts [{:mimeType "text/html" :partData bytes}]})
         edn               (genai/response-json response)
         actual-set (into (sorted-set) (map name) (keys edn))]
     (if (and (map? edn)
              (= (count members) (count edn))
              (= actual-set (set members)))
       edn
       (throw (ex-info "returned keys did not match" {:actual (into (sorted-set) (map name) (keys edn))
                                                      :members   (into (sorted-set) members)}))))))

#!----------------------------------------------------------------------------------------------------------------------
#!
#! extract builder setters
#!

(defn $extract-readonly
  [model package className]
  (let [bytes (get-url-bytes-memo (str (g/coerce some? (:packageRootUrl package)) className))
        {:keys [instanceMethods] :as reflection} (reflect-readonly className)
        instance-method-names (map name (keys instanceMethods))
        systemInstruction (str "identity the class & extract its constructors and methods. omit method entries for .hashCode, and .equals()."
                               "please use fully qualified package names for all types that reference gcp sdks")
        method-schema {:type        "OBJECT"
                       :description "a description of the method. if the method has 0 parameters, omit it"
                       :required    ["returnType" "doc"]
                       :properties  {"returnType" {:type        "STRING"
                                                   :description "the fully qualified type descriptor. if a list, use List<$TYPE> syntax"}
                                     "doc"        {:type        "STRING"
                                                   :description "description of the method"}
                                     "parameters" {:type     "ARRAY"
                                                   :nullable true
                                                   :items    {:type       "OBJECT"
                                                              :properties {"name" {:type        "STRING"
                                                                                   :description "the name of the parameter"}
                                                                           "type" {:type        "STRING"
                                                                                   :description "the fully qualified type descriptor. if a list, use List<$TYPE> syntax"}}}}}}
        generationConfig  {:responseMimeType "application/json"
                           :responseSchema   {:type       "OBJECT"
                                              :required   instance-method-names
                                              :properties (into {} (map #(vector % method-schema)) instance-method-names)}}
        cfg (assoc model :systemInstruction systemInstruction
                         :generationConfig generationConfig)
        response (genai/generate-content cfg {:parts [{:mimeType "text/html" :partData bytes}]})
        edn (genai/response-json response)]
    (if (and (map? edn)
             (= (count instance-method-names) (count edn))
             (= (set instance-method-names) (set (map name (keys edn)))))
      (assoc reflection :className className
                        :instanceMethods edn)
      (throw (ex-info "returned keys did not match" {:actual (into (sorted-set) (map name) (keys edn))
                                                     :members   (into (sorted-set) instance-method-names)})))))

#!----------------------------------------------------------------------------------------------------------------------

(defn $extract-from-class-doc
  ([package t]
   ($extract-from-class-doc pro package t))
  ([model {:keys [classes] :as package} t]
   (throw (Exception. "unimplemented"))
   #_(if (contains? (set classes) t)
     ($extract-class model package t)
     (let [t' (str (name (get package :packageSymbol)) "." t)]
       (if (contains? (set classes) t')
         ($extract-class model package t')
         (throw (Exception. (str t " is not a class"))))))))

(defonce $extract-from-class-doc-memo (memoize $extract-from-class-doc))

#!----------------------------------------------------------------------------------------------------------------------


(def native-type #{"java.lang.Boolean" "java.lang.String" "java.lang.Integer" "java.lang.Long"
                   "boolean" "int" "java.lang.Object"})

(defn parse-type [t]
  (if (string/starts-with? t "java.util.List<")
    (let [start (subs t 15)]
      (subs start 0 (.indexOf start ">")))
    (if (string/starts-with? t "java.util.Map<")
      (if (string/starts-with? t "java.util.Map<java.lang.String,")
        (string/trim (subs t 31 (.indexOf t ">")))
        (throw (Exception. (str "unimplemented non-string key for type '" t "'"))))
      t)))

(defn type-dependencies
  ([cdesc]
   (type-dependencies #{} cdesc))
  ([init {:keys [staticMethods instanceMethods isBuilder]}]
   (reduce
     (fn [acc {:keys [parameters returnType]}]
       (let [acc (into acc (comp (map :type) (map parse-type) (remove native-type)) parameters)]
         (if (not isBuilder) ;; TODO KILL ME
           (let [ret (parse-type returnType)]
             (if (native-type ret)
               acc
               (conj acc ret)))
           acc)))
     init
     (into staticMethods instanceMethods))))

(defn class-name-components [class]
  (let [class (if (class? class)
                (str class)
                (if (or (symbol? class) (keyword? class))
                  (name class)
                  class))]
    (string/split class #"\.")))

(defn package-parts [className]
  (assert (string? className))
  (vec (nthrest (class-name-components className) 4)))

(defn package-keys
  [{:keys [name] :as package}]
  (let [class-names (into #{} (filter #(= 1 (count %))) (map package-parts (:classes package)))
        class-keys (into (sorted-set) (map #(keyword "gcp" (string/join "." (into [name] %)))) class-names)]
    class-keys))

;; TODO there are enum bindings in vertexai at least that can probably be killed
(defn missing-files [package src-root]
  (let [expected-binding-names (into (sorted-set) (map first) (map package-parts (:classes package)))
        expected-files (map #(io/file src-root (str % ".clj")) expected-binding-names)]
    (remove #(.exists %) expected-files)))

#!----------------------------------------------------------------------------------------------------------------------

(comment
  (do (require :reload 'gcp.dev.generators.class-binding) (in-ns 'gcp.dev.generators.class-binding))
  )


#!----------------------------------------------------------------------------------------------------------------------

(defn ->malli-type [package t]
  (case t
    ;"java.lang.Object"
    "java.lang.String" :string
    ("boolean" "java.lang.Boolean") :boolean
    ("int" "java.lang.Integer" "long" "java.lang.Long") :int
    "java.util.Map<java.lang.String, java.lang.String>" [:map-of :string :string]
    (cond

      (string/starts-with? t "java.util.List<")
      [:sequential (->malli-type package (string/trim (subs t 15 (dec (count t)))))]

      (or (contains? (set (:classes package)) t)
          (contains? (set (:enums package)) t))
      (keyword "gcp" (string/join "." (into [(:packageName package)] (package-parts t))))

      :else t)))

(defn extract-type
  "seq of maps describing a field's type, getters and setters etc
   can be used to generate both schemas and class bindings"
  ([package class]
   (let [readonly  ($extract-from-class-doc-memo package class)
         builder   ($extract-from-class-doc-memo package (str class ".Builder"))
         instance-getters (into []
                                (remove #(or (= "toBuilder" (:methodName %))
                                             (= "toString" (:methodName %))
                                             (= "hashCode" (:methodName %))
                                             (= "equals" (:methodName %))))
                                (:instanceMethods readonly))
         inherited-getters (into []
                                 (remove #(or (= "toBuilder" (:methodName %))
                                              (= "toString" (:methodName %))
                                              (= "hashCode" (:methodName %))
                                              (= "equals" (:methodName %))
                                              ;; we are not interested in inherited static methods,
                                              ;; only inherited instance methods, usually 'getType'
                                              (not (string/starts-with? (:methodName %) "get"))))
                                 (:inheritedMethods readonly))
         getters (concat instance-getters inherited-getters)
         setters   (into []
                         (comp
                           (remove #(= "build" (:methodName %)))
                           (map #(dissoc % :returnType)))
                         (:instanceMethods builder))
         *setters (atom (into {}
                              (map
                                (fn [{:keys [methodName] :as m}]
                                  (let [key (string/lower-case (subs methodName 3))]
                                    [key (assoc m ::key key)])))
                              setters))
         ; for every setter there is a getter, but not every getter has a setter
         fields (reduce (fn [acc {getterMethod :methodName readDoc :doc :keys [returnType] :as getter}]
                         (let [key (string/lower-case (cond-> getterMethod (string/starts-with? getterMethod "get") (subs 3)))]
                           (if-let [{setterMethod :methodName writeDoc :doc :keys [parameters] :as setter}
                                    (or (get @*setters key)
                                        (reduce (fn [_ [k m]]
                                                  (when (string/starts-with? k key)
                                                    (reduced m))) nil @*setters))]
                             (let [opt (if (re-seq #"ptional" writeDoc)
                                         true
                                         (if (re-seq #"equired" writeDoc)
                                           false))]
                               (assert (= 1 (count parameters)))
                               (assert (contains? @*setters (::key setter)))
                               (swap! *setters dissoc (::key setter))
                               (conj acc (cond-> {:getter    getterMethod
                                                  :type      returnType
                                                  :setter    setterMethod
                                                  :readDoc readDoc
                                                  :writeDoc writeDoc
                                                  :parameter (:name (first parameters))}
                                                 (some? opt) (assoc :optional opt))))
                             (conj acc {:getter getterMethod :type returnType}))))
                       [] getters)]
     (assert (empty? @*setters) (str "expected setters to be exhausted: " @*setters))
     fields)))

(defn malli-from-class-doc [package class]
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
                [key opts (->malli-type package type)])))
          (sort-by key properties))))

#!----------------------------------------------------------------------------------------------------------------------

(comment

  (members com.google.cloud.bigquery.LoadJobConfiguration)
  (members com.google.cloud.bigquery.LoadJobConfiguration$Builder)

  (do (require :reload 'gcp.dev.generators.class-binding) (in-ns 'gcp.dev.generators.class-binding))

  (def package-schema
    [:map
     [:name :string]
     ;TODO settings & clients
     [:classes    [:seqable :string]]
     [:enums      [:seqable :string]]
     [:exceptions [:seqable :string]]
     [:interfaces [:seqable :string]]])

  (def bigquery
    (let [discovery (j/read-value (get-url-bytes-memo "https://bigquery.googleapis.com/discovery/v1/apis/bigquery/v2/rest") j/keyword-keys-object-mapper)
          base      {:packageRootUrl "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/"
                     :discovery      discovery
                     :overviewUrl    "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery"
                     :root           (io/file src "main" "gcp" "bigquery" "v2")
                     :rootNs         'gcp.bigquery.v2
                     :packageName    "bigquery"
                     :packageSymbol  'com.google.cloud.bigquery}
          bq (merge base ($extract-package-summary-memo (:overviewUrl base)))
          class-keys (into (sorted-set)
                           (comp (map package-parts)
                                 (map first)
                                 (map keyword))
                           (:classes bq))
          _ (assert (seq class-keys))
          schema-keys (set (keys (get-in discovery [:schemas])))
          _ (assert (seq schema-keys))
          class-intersection (clojure.set/intersection schema-keys  class-keys)
          _ (assert (seq class-intersection))
          ref-classes (into (sorted-map)
                            (map
                              (fn [key]
                                (let [schema (get-in bigquery [:discovery :schemas key])]
                                  (when (some some? (map :$ref (vals (get schema :properties))))
                                    [key schema]))))
                            class-intersection)
          refless-classes (into (sorted-map)
                                (map
                                  (fn [key]
                                    (let [schema (get-in bigquery [:discovery :schemas key])]
                                      (when-not (some some? (map :$ref (vals (get schema :properties))))
                                        [key schema]))))
                                class-intersection)
          classes-difference (into {}
                                   (map
                                     (fn [k]
                                       [k (get-in discovery [:schemas k])]))
                                   (clojure.set/difference schema-keys class-keys))]
      (assoc bq :classes-refless refless-classes
                :classes-ref ref-classes
                :classes-difference classes-difference)))


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
  (let [{:keys [className]
         :as cdec} ($extract-from-class-doc-memo package target-class)
        dependencies (into #{} (remove #(or (string/includes? % target-class)
                                            (string/ends-with? % "Builder")))
                           (type-dependencies cdec))
        [siblings other] (split-with #(string/starts-with? % (str "com.google.cloud." packageName)) dependencies)
        ;; dependencies could be any class, google cloud or threetenbp usually if any
        ;; get siblings by comparing w/ package classes
        sibling-parts (into (sorted-set) (comp (map package-parts) (map first)) siblings)
        _ (when (seq other)
            (throw (ex-info "unimplemented requires for non-sibling dependencies" {:target-class target-class
                                                                                   :other-deps other})))
        parts        (class-name-components target-class)
        requires     (into ['[gcp.global :as g]] (map (partial sibling-require package)) sibling-parts)
        target-ns (symbol (str rootNs "." (peek (package-parts className))))
        ns-forms [`(~'ns ~target-ns
                     (:import [~(symbol (string/join "." (butlast parts))) ~(symbol (last parts))])
                     (:require ~@(sort-by first requires)))
                  (str "(defn ^" (symbol (last parts)) " from-edn [arg] (throw (Exception. \"unimplemented\")))")
                  (str "(defn to-edn [^" (symbol (last parts)) " arg] (throw (Exception. \"unimplemented\")))")]]
    (zp/zprint-str (apply str ns-forms) 80 {:parse-string-all? true :parse {:interpose "\n\n"}})))

(defn spit-skeleton
  [{:keys [root] :as package} target-class]
  (let [class-name (peek (package-parts target-class))
        target-file (io/file root (str class-name ".clj"))]
    (if (.exists target-file)
      (throw (ex-info "already exists" {:file target-file}))
      (let [src (class-binding-skeleton package target-class)]
        (spit target-file src)))))

