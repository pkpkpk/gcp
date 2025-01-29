(ns gcp.dev.generators.class-binding
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            gcp.bigquery.v2
            gcp.vertexai.v1
            [gcp.global :as g]
            [gcp.vertexai.generativeai :as genai]
            [jsonista.core :as j]
            [taoensso.telemere :as tt])
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

(defn extract-class-details
  ([java-doc-url] (extract-class-details flash java-doc-url))
  ([model java-doc-url]
   (genai/generate-content
     (assoc model :systemInstruction "identity the class & extract its constructors and methods. omit method entries for .hashCode and .equals()"
                  :generationConfig {:responseMimeType "application/json"
                                     :responseSchema   {:type       "OBJECT"
                                                        :properties {"className" {:type "STRING"}
                                                                     "methods"   {:type  "ARRAY"
                                                                                  :items {:type       "OBJECT"
                                                                                          :properties {"doc"       {:type "STRING"}
                                                                                                       "type"      {:type        "STRING"
                                                                                                                    :description "method type, STATIC or INSTANCE"}
                                                                                                       "signature" {:type        "STRING"
                                                                                                                    :description "the method signature"}}}}}}})
     {:parts [{:mimeType "text/html"
               :partData (get-url-bytes java-doc-url)}]})))


(def java-doc-root "https://cloud.google.com/java/docs/reference/")

(defn class-url [sdk class-name]
  (str java-doc-root))


#!----------------------------------------------------------------------------------------------------------------------

(def bigquery-root (io/file src "main" "gcp" "bigquery"))
(def bigquery-api-doc-base "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/")
(def bigquery-discovery-url "https://bigquery.googleapis.com/discovery/v1/apis/bigquery/v2/rest")
(defonce bigquery-discovery (j/read-value (get-url-bytes bigquery-discovery-url) j/keyword-keys-object-mapper))

(def bigquery
  {:src-root bigquery-root
   :sdk-name "google-cloud-bigquery"
   })

(def pubsub-root (io/file src "main" "gcp" "pubsub"))
(def pubsub-api-doc-base "https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/")

#!----------------------------------------------------------------------------------------------------------------------

(defn class-doc-content [base-url class-name]
  (let [class-url (str base-url class-name)
        builder-url (str class-url ".Builder")]
    {:parts ["this is a json description of the read-only class"
             (genai/response-text (extract-class-details class-url))
             "this is  json description of it's builder class"
             (genai/response-text (extract-class-details builder-url))]}))

(defonce class-doc-content-memo (memoize class-doc-content))

#!----------------------------------------------------------------------------------------------------------------------

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
                                                                                          :description "the edn :bigquery qualified keyword for the schema in the registry"}
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
     {key schema})))

(comment
  (do (require :reload 'gcp.dev.generators.class-binding) (in-ns 'gcp.dev.generators.class-binding))

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
