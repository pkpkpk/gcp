(ns gcp.dev.toolchain.emit.facade
  (:require [clojure.string :as string]
            [gcp.dev.packages :as p]
            gcp.gcloud
            [mendel.alpha.completions :as completions]
            [manifold.deferred :as d]))

(defn clean-method [m]
  (-> (dissoc m :static? :private? :abstract? :beta? :parameter-mappings)
      (update :parameters (fn [ps] (mapv #(dissoc % :varArgs?) ps)))))

(defn client-methods
  "{methodName -> {parameters -> {:returnType, :doc, :parameters, :name}}"
  [client-fqcn]
  (let [{:keys [methods category]} (p/lookup-class client-fqcn)
        _ (assert (= :client category))
        _ (assert (not-empty methods))
        methods (map clean-method methods)]
    (reduce
      (fn [acc {:keys [name parameters] :as meth}]
        (assoc-in acc [name parameters] meth))
      (sorted-map)
      methods)))

(defn extract-docstring-features__request-body
  "semantically extract docstring per method into components
   <header> [<example>/<interstitial> ... <footer>"
  [{:keys [doc parameters name returnType] :as method}]
  (let [sys (string/join \newline ["Given an EDN map describing a java method, examine the :doc entry."
                                   "Decompose the docstring into an array of maps describing it's components"])]
    {:response_format {:type "ARRAY"
                       :minItems 1
                       :items {:type "OBJECT"
                               :properties {"type"    {:type "STRING"
                                                       :enum ["prose" "code"]}
                                            "content" {:type "STRING"
                                                       :nullable false}}}}
     :messages [{:role "system" :content sys}]}))

(defn describe-method
  [fqcn method-name]
  (let [{by-param method-name} (client-methods fqcn)
        _ (assert (not-empty by-param))
        body->params (into {}
                           (map (fn [[k v]] [(extract-docstring-features__request-body v) k]))
                           by-param)


        ]
    ;(d/on-realized (completions/post-many ))
    ))

(defn generate-clojure-docstring [])

(defn docstring
  [])

(defn emit-function
  "methods == "
  [{:as signature}])

(defn audit-function
  [{}])

(comment
  GCP_PROJECT_ID
  (do (require :reload 'gcp.dev.toolchain.emit.facade)
      (in-ns 'gcp.dev.toolchain.emit.facade))
  (describe-methods "com.google.cloud.bigquery.BigQuery")
  (get (describe-methods "com.google.cloud.bigquery.BigQuery") "writer")

  (generate-clojure-docstring "com.google.cloud.bigquery.BigQuery" "writer")
  )