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
  (tt/log! (str "fetching url ->" url))
  (let [{:keys [status body] :as response} (http/get url {:redirect-strategy :none :as :byte-array})]
    (if (= 200 status) ;google docs will 301 on missing doc
      body
      (throw (ex-info "expected 200 response" {:url url
                                               :response response})))))

(def get-url-bytes-memo (memoize get-url-bytes))

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
(def flash-2  {:model "gemini-2.0-flash-exp"})
(def thinking {:model "gemini-2.0-flash-thinking-exp-01-21"})

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
   (tt/log! (str "$extract-package-summary -> " package-url))
   (-> model
       (assoc :systemInstruction (str "You are given google cloud java package summary page"
                                      "Extract its parts into arrays.")
              :generationConfig {:responseMimeType "application/json"
                                 :responseSchema   package-response-schema})
       (genai/generate-content {:parts [{:mimeType "text/html" :partData (get-url-bytes-memo package-url)}]})
       genai/response-json)))

(defonce $extract-package-summary-memo (memoize $extract-package-summary))

(def methods-schema
  {:type        "ARRAY"
   :description "method descriptors"
   :items       {:type        "OBJECT"
                 :required    ["returnType" "methodName"]
                 :description "a description of the method. if the method has 0 parameters, omit it"
                 :properties  {"returnType" {:type        "STRING"
                                             :description "the type returned on method invocation"}
                               "methodName" {:type        "STRING"
                                             :description "the method name"}
                               "required" {:type "BOOLEAN"
                                           :description "if the class is a builder, is this field required to be set?"}
                               "parameters" {:type        "ARRAY"
                                             :description "the name and type of method parameters in order"
                                             :items       {:type       "OBJECT"
                                                           :properties {"type" {:type        "STRING"
                                                                                :description "the fully qualified type descriptor. if a list, use List<$TYPE> syntax"}
                                                                        "name" {:type        "STRING"
                                                                                :description "this name of the parameter"}}}}}}})

(def class-description-schema
  {:type       "OBJECT"
   :required   ["className" "isBuilder" "staticMethods" "instanceMethods"]
   :properties {"className"       {:type "STRING"}
                "isBuilder"       {:type "BOOLEAN" :description "is the class a builder class"}
                "inheritedMethods" (assoc methods-schema :description "descriptions of inherited methods if any")
                "staticMethods"    (assoc methods-schema :description "descriptions of static methods if any")
                "instanceMethods"  (assoc methods-schema :description "descriptions of instance methods if any")}})

(defn $extract-class-details
  ([package className]
   ($extract-class-details flash package className))
  ([model package className]
   (let [java-doc-url (str (g/coerce some? (:packageRootUrl package)) className)]
     (tt/log! (str "$extract-class-details -> " java-doc-url))
     (-> model
         (assoc :systemInstruction (str "identity the class & extract its constructors and methods. omit method entries for .hashCode, and .equals()."
                                        "please use fully qualified package names for all types that reference gcp sdks")
                :generationConfig {:responseMimeType "application/json"
                                   :responseSchema   class-description-schema})
         (genai/generate-content {:parts [{:mimeType "text/html" :partData (get-url-bytes-memo java-doc-url)}]})
         genai/response-json))))

(defonce $extract-class-details-memo (memoize $extract-class-details))

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
         (if (not isBuilder)
           (let [ret (parse-type returnType)]
             (if (native-type ret)
               acc
               (conj acc ret)))
           acc)))
     init
     (into staticMethods instanceMethods))))

(def package-schema
  [:map
   [:name :string]
   [:classes    [:seqable :string]]
   [:enums      [:seqable :string]]
   [:exceptions [:seqable :string]]
   [:interfaces [:seqable :string]]])

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
  (g/coerce package-schema package)
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
#!
#! Class.clj binding
#!

(defn sibling-require
  [{:keys [rootNs] :as package} className]
  (let [req (symbol (str (name rootNs) "." className))
        alias (symbol className)]
    [req :as alias]))

(defn class-binding-skeleton [{:keys [rootNs packageRootUrl packageName] :as package} target-class]
  (assert (string/starts-with? target-class "com.google.cloud"))
  (let [{:keys [className]
         :as cdec} ($extract-class-details-memo package target-class)
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

(defn merge-getters-and-setters
  ([package class]
   (let [readonly  ($extract-class-details-memo package class)
         getters   (into (vec (remove #(= "toString" (:methodName %)) (:inheritedMethods readonly)))
                         (remove #(= "toBuilder" (:methodName %)))
                         (:instanceMethods readonly))
         builder   ($extract-class-details-memo package (str class ".Builder"))
         setters   (into []
                         (comp
                           (remove #(= "build" (:methodName %)))
                           (map #(dissoc % :returnType)))
                         (:instanceMethods builder))
         *setters (atom (into {} (map (fn [{:keys [methodName] :as m}] [(string/lower-case (subs methodName 3)) m])) setters))
         ; for every setter there is a getter, but not every getter has a setter
         fields (reduce (fn [acc {getterMethod :methodName :keys [returnType] :as getter}]
                         (let [key (string/lower-case (cond-> getterMethod
                                                              (string/starts-with? getterMethod "get") (subs 3)))]
                           (if-let [{setterMethod :methodName :keys [parameters required]}
                                    (or (get @*setters key)
                                        (reduce (fn [_ [k m]]
                                                  (when (string/starts-with? k key)
                                                    (reduced m))) nil @*setters))]
                             (do
                               (assert (= 1 (count parameters)))
                               (swap! *setters dissoc (string/lower-case (subs setterMethod 3)))
                               (conj acc {:getter    getterMethod
                                          :type      returnType
                                          :setter    setterMethod
                                          :required  required
                                          :parameter (:name (first parameters))}))
                             (conj acc {:getter getterMethod :type returnType}))))
                       [] getters)]
     (assert (empty? @*setters) (str "expected setters to be exhausted: " @*setters))
     (into [:map {:class (symbol (:className readonly))}]
           (map
             (fn [{:keys [getter setter type parameter required]}]
               (let [t (->malli-type package type)
                     opts (cond-> nil
                                  (nil? setter) (assoc :readOnly true)
                                  (nil? getter) (assoc :writeOnly true)
                                  (true? required) (assoc :optional false))
                     key (if (some? parameter)
                           (keyword parameter)
                           (let [_            (assert (some? getter))
                                 param-capped (subs getter 3)
                                 param        (str (string/lower-case (subs param-capped 0 1)) (subs param-capped 1))]
                             (keyword param)))]
                 (if opts [key opts t] [key t]))))
           fields))))

#!----------------------------------------------------------------------------------------------------------------------

(defn $generate-instance-from-edn [])

(defn $generate-to-edn [])

#!----------------------------------------------------------------------------------------------------------------------

(comment
  (do (require :reload 'gcp.dev.generators.class-binding) (in-ns 'gcp.dev.generators.class-binding))

  (def bigquery
    (let [base {:packageRootUrl "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/"
                :discoveryUrl   "https://bigquery.googleapis.com/discovery/v1/apis/bigquery/v2/rest"
                :overviewUrl    "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery"
                :root           (io/file src "main" "gcp" "bigquery" "v2")
                :rootNs         'gcp.bigquery.v2
                :packageName    "bigquery"}]
      (merge base ($extract-package-summary-memo (:overviewUrl base)))))

  (def ljc ($extract-class-details-memo bigquery "com.google.cloud.bigquery.LoadJobConfiguration"))
  (class-binding-skeleton bigquery  "com.google.cloud.bigquery.LoadJobConfiguration")
  (merge-getters-and-setters bigquery "com.google.cloud.bigquery.LoadJobConfiguration")
  ;($generate-class-schema bigquery "com.google.cloud.bigquery.LoadJobConfiguration")

  ;(defonce bigquery-discovery (j/read-value (get-url-bytes-memo (:discovery-url bigquery)) j/keyword-keys-object-mapper))
  ;(get-in bigquery-discovery [:schemas :JobConfigurationLoad])

  ;;; TODO possible circular deps
  ;;; LoadJobConfiguration produced require for JobInfo because of JobInfo$WriteDisposition
  ;;; --> in QueryJob & CopyJob bindings import JobInfo$WriteDisposition (its just an enum)
  ;;; fix == check if enums and manually import that class$enum?
  ;;; this probably means crawling dependency tree
  ;;;
  ;;; TODO behavior on DNE
  #_(spit-skeleton bigquery "com.google.cloud.bigquery.HivePartitioningOptions")


  (def pubsub-root (io/file src "main" "gcp" "pubsub"))
  (def pubsub-api-doc-base "https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/")
  )

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

#!----------------------------------------------------------------------------------------------------------------------
#!
#! Malli Schema Generation
#!

