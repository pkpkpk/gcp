(ns gcp.dev.analyzer.extract
  (:require
   [clojure.data :refer [diff]]
   [clojure.string :as string]
   [gcp.dev.models :as models]
   [gcp.dev.packages :as packages]
   [gcp.dev.store :as store]
   [gcp.dev.util :refer :all]
   [gcp.global :as g]
   [gcp.vertexai.generativeai :as genai]))

(defn- extract-from-class [package validate! cfg classlike]
  (let [src (packages/slurp-class package classlike)
        f  #(genai/response-json (genai/generate-content cfg src))]
    (try
      (let [result (f)]
        (validate! result)
        result)
      (catch Exception e
        (throw (ex-info "validation failed" (Throwable->map e)))))))

(defn- gv-from-src!
  [package validate! cfg classlike]
  (assert (fn? validate!))
  (extract-from-class package validate! cfg classlike))

(defn- gvc-from-src!
  [package validate! cfg classlike]
  (assert (fn? validate!))
  (let [key [cfg classlike]
        store (:store package)]
    (or (store/get-key store key)
        (let [edn (extract-from-class package validate! cfg classlike)]
          (store/put! store key edn)
          edn))))

(defn setter-methods [class-like]
  (assert (builder-like? class-like))
  (let [methods (instance-methods class-like)]
    (into (sorted-set)
          (comp (map :name)
                (map name)
                (filter #(string/starts-with? % "set")))
          methods)))

(def version-schema {:type        "STRING"
                     :example     "2.47.0"
                     :description "package version"
                     :pattern     "^[0-9]+\\.[0-9]+\\.[0-9]+$"})

(defn- setters-from-src
  "=> {:setterName {:name 'paramName' :type 'java.lang.Long'}}"
  [model package builder-like]
  (assert (builder-like? builder-like))
  (let [setters           (g/coerce [:seqable :string] (setter-methods builder-like))
        systemInstruction (str "Extract the builder setter methods' description and parameter types."
                               " Please use fully qualified package names for all types that reference gcp sdks. "
                               " If the type is a generic type parameter with a type bound, return the fully qualified type bound as the type.")
        method-schema     {:type       "OBJECT"
                           :required   ["type" "doc" "name"]
                           :properties {"type" {:type        "ARRAY"
                                                :description "if there are polymorphic parameter types, include them all here"
                                                :items       {:type        "STRING"
                                                              :description "**FULLY QUALIFIED TYPE PARAMETER**. if a list, use List<$TYPE> syntax"}}
                                        "doc"  {:type        "STRING"
                                                :description "description of the method"}
                                        "name" {:type        "STRING"
                                                :description "the name of the parameter"}}}
        generationConfig  {:responseMimeType "application/json"
                           :responseSchema   {:type       "OBJECT"
                                              :required   (vec setters)
                                              :properties (into {} (map #(vector % method-schema)) setters)}}
        validator         (fn [edn]
                            (when-not (= (into (sorted-set) (map name) (keys edn)) (set setters))
                              (let [data {:extracted (into (sorted-set) (map name) (keys edn))
                                          :expected  (into (sorted-set) setters)
                                          :same?     (= (into (sorted-set) (map name) (keys edn))
                                                        (set setters))
                                          :diff      (diff (into (sorted-set) setters)
                                                           (into (sorted-set) (map name) (keys edn)))}]
                                (throw (ex-info "returned keys did not match" data))))
                            (let [types (apply concat (map :type (vals edn)))]
                              (when (some illegal-native-type types)
                                (throw (ex-info "illegal native types" {:types types})))))
        cfg               (assoc model :systemInstruction systemInstruction
                                       :generationConfig generationConfig)
        base-class        (string/join "." (pop (dot-parts builder-like)))]
    (gvc-from-src! package validator cfg base-class)))

(def parameter-schema
  {:type     "ARRAY"
   :nullable true
   :items    {:type       "OBJECT"
              :properties {"name" {:type        "STRING"
                                   :description "the name of the parameter"}
                           "type" {:type        "STRING"
                                   :description "fully qualified type descriptor"}}}})

(def method-schema
  {:type        "OBJECT"
   :description "a description of the method. if the method has 0 parameters, omit it"
   :required    ["doc" "parameters" "returnType"]
   :properties  {"doc"        {:type        "STRING"
                               :nullable true
                               :description "description of the method"}
                 "parameters" parameter-schema
                 "returnType" {:type        "STRING"
                               :description "fully qualified type descriptor"}}})

(defn- acceptable-type? [expected actual]
  (or (string/starts-with? actual (name expected))
      (and (string/starts-with? actual "List<")
           (= expected "java.util.List"))))

(defn- build-getter-method-validator [expected-methods]
  (g/coerce [:seqable [:map [:returnType symbol?] [:name symbol?]]] expected-methods)
  (let [expected (into {} (map #(vector (keyword (:name %)) %)) expected-methods)
        expected-keys (g/coerce [:set :keyword] (set (keys expected)))]
    (fn [edn]
      (when-not (= expected-keys (set (keys edn)))
        (throw (ex-info "returned keys did not match"
                        {:expected (set (keys expected))
                         :actual   (set (keys edn))})))
      (let [bad-types      (reduce
                             (fn [acc [k {t :returnType}]]
                               ;; TODO look for param types too?
                               (let [expected-type (as-dot-string (get-in expected [k :returnType]))]
                                 (if (acceptable-type? expected-type (name t))
                                   acc
                                   (assoc acc k {:actual t
                                                 :expected expected-type}))))
                             {}
                             edn)]
        (when-not (empty? bad-types)
          (throw (ex-info "returned bad types" bad-types)))))))

(defn- instance-methods-from-src
  [model package classlike]
  (let [instance-methods' (g/coerce [:seqable map?] (instance-methods classlike))
        method-names (sort (map (comp name :name) instance-methods'))
        systemInstruction (str "Extract the docstrings and types for the listed methods."
                               "Please use fully qualified package names for all types that reference gcp sdks."
                               "If the type is a list, use fully qualified List<$TYPE> syntax."
                               "If the type is a generic type parameter with a type bound, return the fully qualified type bound as the type")
        generationConfig {:responseMimeType "application/json"
                          :responseSchema {:type       "OBJECT"
                                           :required   method-names
                                           :properties (into {} (map #(vector % method-schema)) method-names)}}
        cfg (assoc model :systemInstruction systemInstruction
                         :generationConfig generationConfig)
        validator! (build-getter-method-validator instance-methods')
        res (gvc-from-src! package validator! cfg classlike)]
    (into (sorted-map)
          (map
            (fn [[k {:keys [returnType doc parameters]}]]
              (let [doc (clean-doc doc)]
                [k (cond-> {:returnType returnType}
                           (not-empty parameters) (assoc :parameters parameters)
                           (some? doc) (assoc :doc doc))])))
          res)))

(defn- build-static-methods-validator
  [package className]
  (let []
    ;; just checking types...
    ;; disambiguating polymorphic arities does not seem worth the hassle
    (fn [res]
      (let [ts (reduce #(into %1 (map :type) (apply concat %2)) #{} (vals res))]
        (doseq [t ts]
          (when-not (or (known-types t)
                        (contains? (:types/all package) t))
            (throw (ex-info (str "bad static method param type '" t "'") {:extracted-types ts
                                                                          :extracted-methods res
                                                                          :className className}))))))))

(defn- static-methods-from-src
  [model package classlike]
  (let [static-methods' (g/coerce [:seqable map?] (static-methods classlike))
        static-method-names (into #{} (map :name) static-methods')
        systemInstruction (str "Extract the docstrings and types for the listed methods."
                               "Please use fully qualified package names for all types that reference gcp sdks."
                               "If the type is a list, use fully qualified List<$TYPE> syntax."
                               "If the type is a generic type parameter with a type bound, return the fully qualified type bound as the type")
        static-method-schema {:type       "OBJECT"
                              :required   (mapv name static-method-names)
                              :properties (into (sorted-map)
                                                (map
                                                  (fn [method-name]
                                                    (assert (symbol? method-name))
                                                    [(name method-name)
                                                     {:type        "ARRAY"
                                                      :minLength   (count (filter #(= method-name (:name %)) static-methods'))
                                                      :maxLength   (count (filter #(= method-name (:name %)) static-methods'))
                                                      :description "each polymorphic parameter list for the static method"
                                                      :items       parameter-schema}]))
                                                static-method-names)}
        generationConfig {:responseMimeType "application/json"
                          :responseSchema static-method-schema}
        cfg (assoc model :systemInstruction systemInstruction
                         :generationConfig generationConfig)
        validator! (build-static-methods-validator package classlike)]
    (gvc-from-src! package validator! cfg classlike)))

(defn- readonly-from-src
  [model package classlike]
  (assert (not (contains? (:types/builders package) classlike)))
  (let [getters (instance-methods-from-src model package classlike)
        statics (static-methods-from-src model package classlike)
        doc (when-let [[comment] (not-empty (re-seq #"(?ms)^/\*\*.*?\*/" (packages/slurp-class package classlike)))]
              (-> comment
                  (string/replace #"^/\*\*" "")
                  (string/replace #"\*/$" "")
                  (string/replace #"(?m)^\s*\*\s?" "")
                  clean-doc))]
    (cond-> {:className (as-dot-string classlike)
             :type :readonly
             :getterMethods getters
             :staticMethods statics}
            doc (assoc :doc doc))))

(defn- enum-from-src [model package enum-like]
  (assert ((set (:types/enums package)) (as-dot-string enum-like)))
  (assert (string? (:store package)))
  (let [values (enum-values (g/coerce some? (resolve (symbol (as-dollar-string enum-like)))))
        doc-schema {:type "STRING"
                    :nullable true
                    :description "description of enum value"}
        schema {:type     "OBJECT"
                :required ["doc" "values"]
                :properties {"doc"     {:type        "STRING"
                                        :nullable    true
                                        :description "enum class description"}
                             "values"  {:type       "OBJECT"
                                        :required values
                                        :properties (into {} (map #(vector % doc-schema)) values)}}}
        cfg (assoc model :systemInstruction "describe the enum class & each of individual value"
                         :generationConfig {:responseMimeType "application/json"
                                            :responseSchema   schema})
        res (gvc-from-src! package identity cfg enum-like)]
    (assoc res :className (as-dot-string enum-like))))

(defn from-src
  ([package classlike]
   (from-src models/pro-2 package classlike))
  ([model {:as package} class-like]
   (cond
     ;; TODO exceptions! settings! clients!

     ((set (:types/enums package)) (as-dot-string class-like))
     (enum-from-src model package class-like)

     (builder-like? class-like)
     (setters-from-src model package class-like)

     true
     (readonly-from-src model package class-like))))
