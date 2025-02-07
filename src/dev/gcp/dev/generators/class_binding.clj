(ns gcp.dev.generators.class-binding
  (:require
    [clj-http.client :as http]
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.repl :refer :all]
    [clojure.set :as s]
    [clojure.string :as string]
    gcp.bigquery.v2
    [gcp.global :as g]
    gcp.vertexai.v1
    [gcp.vertexai.generativeai :as genai]
    [jsonista.core :as j]
    [konserve.core :as k]
    [hasch.core :as hasch]
    [konserve.filestore :as fs]
    [rewrite-clj.zip :as z]
    [taoensso.telemere :as tt]
    [zprint.core :as zp])
  (:import (java.io ByteArrayOutputStream)))

#_ (do (require :reload 'gcp.dev.generators.class-binding) (in-ns 'gcp.dev.generators.class-binding))
(def home (io/file (System/getProperty "user.home")))
(def pkpkpk (io/file home "pkpkpk"))
(def root (io/file pkpkpk "gcp"))
(def src  (io/file root "src"))
(defonce store (fs/connect-fs-store (.getPath (io/file root ".konserve")) {:opts {:sync? true}}))

#!----------------------------------------------------------------------------------------------------------------------

(defn get-url-bytes [^String url]
  (tt/log! (str "fetching url -> " url))
  (let [{:keys [status body] :as response} (http/get url {:redirect-strategy :none :as :byte-array})]
    (if (= 200 status) ;google docs will 301 on missing doc
      body
      (throw (ex-info "expected 200 response" {:url url
                                               :response response})))))

(defn get-url-bytes-aside [^String url]
  ;; TODO sweep <package>/latest version to invalidate
  (or (k/get store url nil {:sync? true})
      (let [bs (get-url-bytes url)]
        (k/assoc store url bs {:sync? true})
        bs)))

(defn extract-url-aside
  ([model-cfg url]
   (extract-url-aside model-cfg url identity))
  ([model-cfg url validator!]
   (let [key [model-cfg url]]
     ;; TODO this is caching latest urls, would be better to use permalinks to versions
     (or (k/get store key nil {:sync? true})
         (let [url-bytes (get-url-bytes-aside url)
               response (genai/generate-content model-cfg [{:mimeType "text/html" :partData url-bytes}])
               edn (genai/response-json response)]
           (validator! edn)
           (k/assoc store key edn {:sync? true})
           edn)))))

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

(defn enum-values [clazz]
  (assert (class? clazz))
  (let [meth (.getMethod clazz "values" (into-array Class []))]
    (->> (.invoke meth nil (into-array Object []))
         (map #(.name %))
         sort
         vec)))

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

(defn dot-parts [class]
  (let [class (if (class? class)
                (str class)
                (if (or (symbol? class) (keyword? class))
                  (name class)
                  class))]
    (string/split class #"\.")))

(defn package-parts [className]
  (assert (string? className))
  (vec (take 4 (dot-parts className))))

(defn class-parts [className]
  {:pre [(string? className)]
   :post [(vector? %) (seq %) (every? string? %)]}
  (vec (nthrest (dot-parts className) 4)))

#_(into (pop (class-parts className)) (string/split (peek ps) #"\$"))

(defn package-keys
  [{:keys [name] :as package}]
  (let [class-names (into #{} (filter #(= 1 (count %))) (map class-parts (:classes package)))
        class-keys (into (sorted-set) (map #(keyword "gcp" (string/join "." (into [name] %)))) class-names)]
    class-keys))

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
      (keyword "gcp" (string/join "." (into [(:packageName package)] (class-parts t))))

      :else t)))

(defn as-dot-string [class-like]
  (if (class? class-like)
    (subs (str class-like) 6)
    (let [class-like (name class-like)]
      (if (string? class-like)
        (if (string/includes? class-like "$")
          (let [parts (dot-parts class-like)]
            (string/join "." (into (vec (butlast parts)) (string/split (peek parts) #"\$"))))
          class-like)
        (throw (Exception. (str "cannot make dot string from type " (type class-like))))))))

(defn as-dollar-string [class-like]
  (if (class? class-like)
    (as-dollar-string (as-dot-string class-like))
    (let [class-like    (name class-like)
          package-parts (package-parts class-like)
          class-parts   (class-parts class-like)]
      (string/join "." (conj package-parts (string/join "$" class-parts))))))

;(defn as-gcp-key [class-like])

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
      (throw (Exception. (str "unknown type for class-like '" (type class-like) "'"))))))

(defn builder-like? [class-like]
  (if (class? class-like)
    (builder-like? (str class-like))
    (string/ends-with? (name class-like) "Builder")))

(defn member-methods [class-like]
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
  (let [members (member-methods class-like)]
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
      {:className       (as-dot-string class-like)
       :staticMethods   (sorted-map)
       :instanceMethods (sorted-map)}
      members)))

(defn reflect-builder [class-like]
  (assert (builder-like? class-like))
  (let [clazz (as-class class-like)
        _(assert (class? clazz) (str "expected class got type " (type clazz) " instead"))
        methods (member-methods clazz)
        members (into []
                      (comp (map :name)
                            (map name)
                            (filter #(string/starts-with? % "set")))
                      methods)]
    {:className (as-dot-string class-like)
     :setterMethods members}))

#!----------------------------------------------------------------------------------------------------------------------
#!
#! extract package
#!

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
   (let [cfg (assoc model
               :systemInstruction (str "You are given google cloud java package summary page"
                                       "Extract its parts into arrays.")
               :generationConfig {:responseMimeType "application/json"
                                  :responseSchema   package-response-schema})]
     (tt/log! (str "$extract-package-summary -> " package-url))
     (extract-url-aside cfg package-url))))

#!----------------------------------------------------------------------------------------------------------------------
#!
#! extract type
#!

(defn $extract-builder-setters
  "=> {:setterName {:name 'paramName' :type 'java.lang.Long'}}"
  ([model package builder-like]
   (assert (builder-like? builder-like))
   (assert ((set (:builders package)) (as-dot-string builder-like)))
   (let [url               (str (g/coerce some? (:packageRootUrl package)) builder-like)
         {setters :setterMethods :as reflection} (reflect-builder builder-like)
         _                 (g/coerce [:seqable :string] setters)
         systemInstruction (str "extract the builder setters methods description and parameter type"
                                "please use fully qualified package names for all types that reference gcp sdks")
         method-schema     {:type        "OBJECT"
                            :nullable    true
                            :description "fully qualified setter parameter if any"
                            :properties  {"type" {:type        "STRING"
                                                  :description "the fully qualified type descriptor. if a list, use List<$TYPE> syntax"}
                                          "doc"  {:type        "STRING"
                                                  :description "description of the method"}
                                          "name" {:type        "STRING"
                                                  :description "the name of the parameter"}}}
         generationConfig  {:responseMimeType "application/json"
                            :responseSchema   {:type       "OBJECT"
                                               :required   setters
                                               :properties (into {} (map #(vector % method-schema)) setters)}}
         validator         (fn [edn]
                             (when-not
                               (and (map? edn)
                                    (= (count setters) (count edn))
                                    (= (into (sorted-set) (map name) (keys edn)) (set setters)))
                               (throw (ex-info "returned keys did not match" {:actual  (into (sorted-set) (map name) (keys edn))
                                                                              :members (into (sorted-set) setters)}))))
         cfg               (assoc model :systemInstruction systemInstruction
                                        :generationConfig generationConfig)
         edn               (extract-url-aside cfg url validator)]
     (assoc reflection :setters edn))))

(defn $extract-readonly
  "{:className ''
    :doc ''
    :staticMethods {sym {:parameters [..] :returnType ''}
    :instanceMethods {:getterName -> {:returnType '', :doc ''}}}"
  [model package class-like]
  (assert ((set (:classes package)) (as-dot-string class-like)))
  (let [url (str (g/coerce some? (:packageRootUrl package)) class-like)
        {:keys [instanceMethods] :as reflection} (reflect-readonly class-like)
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
        generationConfig {:responseMimeType "application/json"
                          :responseSchema   {:type       "OBJECT"
                                             :required   ["doc" "methods"]
                                             :properties {"doc" {:type "STRING"
                                                                 :description "description of the class"}
                                                          "methods" {:type       "OBJECT"
                                                                     :required   instance-method-names
                                                                     :properties (into {} (map #(vector % method-schema)) instance-method-names)}}}}
        cfg (assoc model :systemInstruction systemInstruction
                         :generationConfig generationConfig)
        validator (fn [{:keys [methods doc] :as edn}]
                    (when-not (and (map? methods)
                                   (= (count instance-method-names) (count methods))
                                   (= (set instance-method-names) (set (map name (keys methods)))))
                      (throw (ex-info "returned keys did not match" {:actual (into (sorted-set) (map name) (keys methods))
                                                                     :members (into (sorted-set) instance-method-names)}))))
        edn (extract-url-aside cfg url validator)]
    (assoc reflection :className class-like
                      :doc (:doc edn)
                      :instanceMethods (:methods edn))))

(defn $extract-enum-detail
  [model package enum-like]
  (assert ((set (:enums package)) (as-dot-string enum-like)))
  (let [values (enum-values (g/coerce some? (resolve (symbol (as-dollar-string enum-like)))))
        url (str (g/coerce some? (:packageRootUrl package)) (as-dot-string enum-like))
        systemInstruction (str "identity the description of the enum class & each of individual value")
        doc-schema {:type "STRING"
                    :nullable true
                    :description "description of enum value"}
        generationConfig {:responseMimeType "application/json"
                          :responseSchema   {:type       "OBJECT"
                                             :properties {"doc" {:type "STRING"
                                                                 :description "description of the enum class"}
                                                          "values" {:type       "OBJECT"
                                                                    :properties {"doc"    {:type        "STRING"
                                                                                           :description "enum class description"}
                                                                                 "values" {:type       "OBJECT"
                                                                                           :required   values
                                                                                           :properties (into {} (map #(vector % doc-schema)) values)}}}}}}
        cfg (assoc model :systemInstruction systemInstruction
                         :generationConfig generationConfig)]
    (extract-url-aside cfg url)))

#!----------------------------------------------------------------------------------------------------------------------

(defn $extract-type-detail
  ([package class-like]
   ($extract-type-detail pro-2 package class-like))
  ([model {:keys [classes] :as package} class-like]
   (cond
     ;; TODO exceptions! settings! clients!
     ((set (:enums package)) (as-dot-string class-like))
     ($extract-readonly model package class-like)

     (builder-like? class-like)
     ($extract-builder-setters model package class-like)

     true
     ($extract-readonly model package class-like))))

#!----------------------------------------------------------------------------------------------------------------------

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
   (let [{:keys [instanceMethods staticMethods] :as readonly}  ($extract-type-detail package className)
         {:keys [setterMethods] :as builder} ($extract-type-detail package (str className ".Builder"))
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
                [key opts (->malli-type package type)])))
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

(defonce bigquery
  (delay
    (let [discovery          (j/read-value (get-url-bytes-memo "https://bigquery.googleapis.com/discovery/v1/apis/bigquery/v2/rest") j/keyword-keys-object-mapper)
          base               {:packageRootUrl "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/"
                              :discovery      discovery
                              :overviewUrl    "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery"
                              :root           (io/file src "main" "gcp" "bigquery" "v2")
                              :rootNs         'gcp.bigquery.v2
                              :packageName    "bigquery"
                              :packageSymbol  'com.google.cloud.bigquery}
          bq                 (merge base ($extract-package-summary (:overviewUrl base)))
          class-keys         (into (sorted-set)
                                   (comp (map class-parts)
                                         (map first)
                                         (map keyword))
                                   (:classes bq))
          _                  (assert (seq class-keys))
          schema-keys        (set (keys (get-in discovery [:schemas])))
          _                  (assert (seq schema-keys))
          class-intersection (clojure.set/intersection schema-keys class-keys)
          _                  (assert (seq class-intersection))
          ref-classes        (into (sorted-map)
                                   (map
                                     (fn [key]
                                       (let [schema (get-in bigquery [:discovery :schemas key])]
                                         (when (some some? (map :$ref (vals (get schema :properties))))
                                           [key schema]))))
                                   class-intersection)
          refless-classes    (into (sorted-map)
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
                :classes-difference classes-difference))))

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
  (let [class-name (peek (class-parts target-class))
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