(ns gcp.dev.generators.class-binding
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            gcp.bigquery.v2
            gcp.vertexai.v1
            [gcp.global :as g]
            [gcp.vertexai.generativeai :as genai]
            [jsonista.core :as j]
            [taoensso.telemere :as tt]
            [clojure.string :as string])
  (:import (java.io ByteArrayOutputStream)))

#_ (do (require :reload 'gcp.dev.generators.class-binding) (in-ns 'gcp.dev.generators.class-binding))

(def home (io/file (System/getProperty "user.home")))
(def src (io/file home "pkpkpk/gcp/src"))

(defonce get-url-bytes
  (let [f (fn [^String url]
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


(def java-doc-root "https://cloud.google.com/java/docs/reference/")

(def bigquery-root (io/file src "main" "gcp" "bigquery"))
(def bigquery-package-url  "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery")
(def bigquery-api-doc-base "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/")
(def bigquery-discovery-url "https://bigquery.googleapis.com/discovery/v1/apis/bigquery/v2/rest")
(defonce bigquery-discovery (j/read-value (get-url-bytes bigquery-discovery-url) j/keyword-keys-object-mapper))

;(def bigquery
;  {:src-root bigquery-root
;   :sdk-name "google-cloud-bigquery"
;   })

(def pubsub-root (io/file src "main" "gcp" "pubsub"))
(def pubsub-api-doc-base "https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/")

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

(def native-type #{"java.lang.Boolean" "java.lang.String" "java.lang.Integer" "java.lang.Long"})

(defn parse-type [t]
  (if (string/starts-with? t "java.util.List<")
    (let [start (subs t 15)]
      (subs start 0 (.indexOf start ">")))
    (if (string/starts-with? t "java.util.Map<")
      (if (string/starts-with? t "java.util.Map<java.lang.String, ")
        (subs t 32 (.indexOf t ">"))
        (throw (Exception. "unimplemented non-string key")))
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

(def package-schema [:map [:classes [:seqable :string]]])

(defn class-parts
  "count=2 w/Builder gets .clj, count=3 put on count=2 .clj"
  [package]
  (g/coerce package-schema package)
  (map #(vec (nthrest (string/split % #"\.") 3)) (:classes package)))

(defn missing-class-bindings [package]
  (let [expected-files (class-parts package)]))

;; TODO there are enum bindings in vertexai at least that can probably be killed

(defn missing-schemas [])

#!----------------------------------------------------------------------------------------------------------------------

(comment
  (do (require :reload 'gcp.dev.generators.class-binding) (in-ns 'gcp.dev.generators.class-binding))
  ($extract-package-summary-memo bigquery-package-url)
  )


#!----------------------------------------------------------------------------------------------------------------------


(defn class-doc-content [base-url class-name]
  (let [class-url (str base-url class-name)
        builder-url (str class-url ".Builder")]
    {:parts ["this is a json description of the read-only class"
             (genai/response-text ($extract-class-details class-url))
             "this is  json description of it's builder class"
             (genai/response-text ($extract-class-details builder-url))]}))

(defonce class-doc-content-memo (memoize class-doc-content))

(defn $class-schema
  "generate a malli schema for a java sdk class.
   (sdk, class-name) -> {key schema}"
  ([api-base class-name]
   ($class-schema pro api-base class-name))
  ([model api-base class-name]
   (let [sys       (str "you are a clojure code authoring tool."
                        "we are converting external documentation for google cloud sdk java classes into malli schemas."
                        "omit docstrings & instead focus on correctness."
                        "if a field is read-only, add :optional true :read-only true to entry's option map.")
         prompt    (str "finish this schema [:map {:class '" class-name)
         model-cfg (assoc model :systemInstruction sys
                                :generationConfig {:responseMimeType "application/json"
                                                   :responseSchema   {:type       "OBJECT"
                                                                      :properties {"key" {:type        "STRING"
                                                                                          :description "the edn :gcp/bigquery.qualified keyword for the schema in the registry"}
                                                                                   "schema" {:type        "STRING"
                                                                                             :description "edn containing the malli schema"}}}})
         context   [{:parts ["(pr-str gcp.bigquery.v2/registry) ;=> " (pr-str gcp.bigquery.v2/registry)]}
                    (class-doc-content-memo api-base class-name)]
         response  (genai/generate-content model-cfg (conj context prompt))
         {:keys [key schema]}
         (try
           (-> (genai/response-json response)
               (update-in [:key] clojure.edn/read-string)
               (update-in [:schema] clojure.edn/read-string))
           (catch Exception e
             (throw (ex-info (str "error extracting edn from response: " (ex-message e))
                             {:response response
                              :cause e}))))]
     [key schema])))

(comment
  (do (require :reload 'gcp.dev.generators.class-binding) (in-ns 'gcp.dev.generators.class-binding))

  (extract-class-details (str bigquery-api-doc-base "com.google.cloud.bigquery.LoadJobConfiguration"))
  ;(extract-class-details "com.google.cloud.bigquery.LoadJobConfiguration.Builder")

  (class-doc-content-memo bigquery-api-doc-base "com.google.cloud.bigquery.LoadJobConfiguration")

  (get-in bigquery-discovery [:schemas :JobConfigurationLoad])

  ($class-schema bigquery-api-doc-base "com.google.cloud.bigquery.LoadJobConfiguration")
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
