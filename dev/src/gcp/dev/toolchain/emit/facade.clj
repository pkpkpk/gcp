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

(defn dwim-commands
  [ns]
  (->> (ns-interns ns)
       vals
       (filter #(-> % meta :gcp.dwim/command))
       (map deref)))

(defn- schema->label [t]
  (cond
    (#{:string 'string?} t) ":string"
    (#{:int 'int? 'integer?} t) ":int"
    (#{:boolean 'boolean?} t) ":boolean"
    (#{:double 'double?} t) ":double"
    (#{:float 'float?} t) ":float"
    (#{:long 'long?} t) ":long"
    (#{:any 'any?} t) ":any"
    (#{:nil 'nil?} t) "nil"
    (vector? t)
    (case (first t)
      :or (string/join " | " (map schema->label (rest t)))
      :and (string/join " & " (map schema->label (rest t)))
      :maybe (if (second t)
               (str "?" (schema->label (second t)))
               "?")
      :sequential (if (second t)
                    (str "[" (schema->label (second t)) " ...]")
                    "[]")
      :set (if (second t)
             (str "#{" (schema->label (second t)) "}")
             "#{}")
      :map ":map"
      :map-of (if (<= 4 (count t))
                (str "{" (schema->label (nth t 2))
                     " " (schema->label (nth t 3)) "}")
                ":map")
      :tuple (str "[" (string/join ", " (map schema->label (rest t))) "]")
      :ref (if (second t)
             (str "[:ref " (schema->label (second t)) "]")
             "[:ref]")
      (if (and (= 2 (count t)) (map? (second t)))
        (schema->label (first t))
        (str t)))
    (keyword? t) (str t)
    (symbol? t) (str t)
    :else (pr-str t)))

(defn- catn->params [branch]
  (letfn [(walk [x]
            (cond
              (and (vector? x) (= :catn (first x)))
              (mapv (fn [entry]
                      (when (and (vector? entry)
                                 (= 2 (count entry))
                                 (keyword? (first entry)))
                        [(name (first entry))
                         (schema->label (second entry))]))
                    (rest x))

              (and (vector? x) (= :altn (first x)))
              (mapcat walk (rest x))

              :else []))]
    (remove nil? (walk branch))))

(defn- branch-doc
  [facade branch]
  (let [params (catn->params branch)]
    (if (seq params)
      (let [arg-names (map first params)]
        (str "  `(" facade " " (string/join " " arg-names) ")`\n"
             "      where:\n"
             (string/join "\n"
                          (map (fn [[k t]]
                                 (str "        " k " --> " t))
                               params))))
      (str "  `(" facade ")`"))))

(defn humanize-arities
  [{:keys [facade cmd arities]}]
  (let [lines (concat
                [(str "Arity dispatch for `" facade "`.")
                 ""]
                (mapcat (fn [[arity schema]]
                          (cons (case arity
                                  0 "no args:"
                                  1 "1 arg:"
                                  (str arity " args:"))
                                (if (and (vector? schema)
                                         (= :altn (first schema)))
                                  (for [child (rest schema)]
                                    (let [branch (if (and (vector? child)
                                                          (= 2 (count child))
                                                          (keyword? (first child)))
                                                   (second child)
                                                   child)]
                                      (branch-doc facade branch)))
                                  [(branch-doc facade schema)])))
                        (sort-by key arities))
                [""]
                ["More Info:"
                 (str "    `(gcp.global/get-schema " (pr-str cmd) ")`")
                 (str "    `(gcp.global/explain " (pr-str cmd) " <args>)`")])]
    (string/join "\n" lines)))

#!----------------------------------------------------------------------------------------------------------------------

(defn extract-docstring-features__request-body
  "semantically extract docstring per method into components
   <header> [<example>/<interstitial> ... <footer>"
  [{:keys [doc parameters name returnType] :as method}]
  (let [sys (string/join \newline ["Given an EDN map describing a java method, examine the :doc entry."
                                   "Decompose the docstring into an array of maps describing it's components"])]
    {:schema [:sequential {:min 1} [:map
                                    [:type [:enum "prose" "code"]]
                                    [:content :string]]]
     :messages [{:role "system" :content sys}]}))

(defn describe-method
  [fqcn method-name]
  (let [{by-param method-name} (client-methods fqcn)
        _ (assert (not-empty by-param))
        body->params (into {} (map (fn [[k v]] [(extract-docstring-features__request-body v) k])) by-param)
        ]
    ;(d/on-realized (completions/post ))
    ))

;(defn complete-clojure-docstring [])
;(defn docstring [])
;(defn emit-function [{:as signature}])
;(defn audit-function [{}])


(comment
  (do (require :reload 'gcp.dev.toolchain.emit.facade) (in-ns 'gcp.dev.toolchain.emit.facade))

  (require :reload '[gcp.bigquery :as bq])
  (dwim-commands 'gcp.bigquery.core)

  GCP_PROJECT_ID
  (client-methods "com.google.cloud.bigquery.BigQuery")
  (get (client-methods "com.google.cloud.bigquery.BigQuery") "writer")
  (generate-clojure-docstring "com.google.cloud.bigquery.BigQuery" "writer")
  )