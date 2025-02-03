(ns gcp.dev.generators.class-binding
  (:require #_[clj.http :as http]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.set :as s]
            gcp.bigquery.v2
            gcp.vertexai.v1
            [gcp.global :as g]
            [gcp.vertexai.generativeai :as genai]
            [jsonista.core :as j]
            [taoensso.telemere :as tt]
            [clojure.string :as string]
            [zprint.core :as zp])
  (:import (java.io ByteArrayOutputStream)))

#_ (do (require :reload 'gcp.dev.generators.class-binding) (in-ns 'gcp.dev.generators.class-binding))

(def home (io/file (System/getProperty "user.home")))
(def src (io/file home "pkpkpk/gcp/src"))

;; TODO google docs will redirect on missing doc, catch that as error here
#_ (http/get url {:redirect-strategy :none :as :stream})

(defonce get-url-bytes
  (let [f (fn [^String url]
            (tt/log! (str "fetching url ->" url))
            (with-open [in  (io/input-stream url)
                        out (ByteArrayOutputStream.)]
              (io/copy in out)
              (.toByteArray out)))]
    (memoize f)))

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
       (genai/generate-content {:parts [{:mimeType "text/html" :partData (get-url-bytes package-url)}]})
       genai/response-json)))

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
                "staticMethods"   (assoc methods-schema :description "descriptions of static methods if any")
                "instanceMethods" (assoc methods-schema :description "descriptions of instance methods if any")}})

(defn $extract-class-details
  ([java-doc-url]
   ($extract-class-details flash java-doc-url))
  ([model java-doc-url]
   (tt/log! (str "$extract-class-details -> " java-doc-url))
   (-> model
       (assoc :systemInstruction (str "identity the class & extract its constructors and methods. omit method entries for .hashCode, and .equals()."
                                      "please use fully qualified package names for all types that reference gcp sdks")
              :generationConfig {:responseMimeType "application/json"
                                 :responseSchema class-description-schema})
       (genai/generate-content {:parts [{:mimeType "text/html" :partData (get-url-bytes java-doc-url)}]})
       genai/response-json)))

#!----------------------------------------------------------------------------------------------------------------------

(defonce $extract-package-summary-memo (memoize $extract-package-summary))
(defonce $extract-class-details-memo (memoize $extract-class-details))

(defn instance-methods [cdesc]
  (into [] (comp
             (filter #(#{"INSTANCE"} (:methodType %)))
             (remove #(re-find #"uilder" (:methodName %)))
             (map #(dissoc % :doc :methodType)))
        (:methods cdesc)))

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
         :as cdec} ($extract-class-details-memo (str packageRootUrl target-class))
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

  (class-binding-skeleton bigquery "com.google.cloud.bigquery.LoadJobConfiguration")

  (defonce bigquery-discovery (j/read-value (get-url-bytes (:discovery-url bigquery)) j/keyword-keys-object-mapper))
  (class-doc-content-memo bigquery-api-doc-base "com.google.cloud.bigquery.LoadJobConfiguration")
  (get-in bigquery-discovery [:schemas :JobConfigurationLoad])

  ;;; TODO produces circular dep w/ JobInfo
  ;;; TODO behavior on DNE
  #_(spit-skeleton bigquery "com.google.cloud.bigquery.HivePartitioningOptions")

  (def ljc ($extract-class-details-memo (str (:packageRootUrl bigquery) "com.google.cloud.bigquery.LoadJobConfiguration")))


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

;(defn class-doc-content [base-url class-name]
;  (let [class-url (str base-url class-name)
;        builder-url (str class-url ".Builder")]
;    {:parts ["this is a json description of the read-only class"
;             (genai/response-text ($extract-class-details-memo class-url))
;             "this is  json description of it's builder class"
;             (genai/response-text ($extract-class-details-memo builder-url))]}))
;
;(defonce class-doc-content-memo (memoize class-doc-content))
;
;(defn $class-schema
;  "generate a malli schema for a java sdk class.
;   (sdk, class-name) -> {key schema}"
;  ([api-base class-name]
;   ($class-schema pro api-base class-name))
;  ([model api-base class-name]
;   (let [sys       (str "you are a clojure code authoring tool."
;                        "we are converting external documentation for google cloud sdk java classes into malli schemas."
;                        "omit docstrings & instead focus on correctness."
;                        "if a field is read-only, add :optional true :read-only true to entry's option map.")
;         prompt    (str "finish this schema [:map {:class '" class-name)
;         model-cfg (assoc model :systemInstruction sys
;                                :generationConfig {:responseMimeType "application/json"
;                                                   :responseSchema   {:type       "OBJECT"
;                                                                      :properties {"key" {:type        "STRING"
;                                                                                          :description "the edn :gcp/bigquery.qualified keyword for the schema in the registry"}
;                                                                                   "schema" {:type        "STRING"
;                                                                                             :description "edn containing the malli schema"}}}})
;         context   [{:parts ["(pr-str gcp.bigquery.v2/registry) ;=> " (pr-str gcp.bigquery.v2/registry)]}
;                    (class-doc-content-memo api-base class-name)]
;         response  (genai/generate-content model-cfg (conj context prompt))
;         {:keys [key schema]}
;         (try
;           (-> (genai/response-json response)
;               (update-in [:key] clojure.edn/read-string)
;               (update-in [:schema] clojure.edn/read-string))
;           (catch Exception e
;             (throw (ex-info (str "error extracting edn from response: " (ex-message e))
;                             {:response response
;                              :cause e}))))]
;     [key schema])))