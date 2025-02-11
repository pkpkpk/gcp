(ns gcp.dev.extract
  (:require [clojure.string :as string]
            [gcp.dev.models :as models]
            [gcp.dev.store :as store]
            [gcp.dev.util :refer [as-dot-string as-dollar-string as-class builder-like?]]
            [gcp.global :as g]
            [gcp.vertexai.generativeai :as genai]
            [taoensso.telemere :as tt]))

;; TODO vertexai schemas should accept named in properties slots, automatically transform them to string

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
          (update-in acc [:staticMethods name] conj {:parameters parameter-types :returnType return-type})
          (if (and (not (contains? flags :static))
                   (not (#{"hashCode" "equals" "toBuilder"} (clojure.core/name name))))
            (assoc-in acc [:instanceMethods name]
                      (cond-> {:returnType return-type}
                              (seq parameter-types) (assoc :parameters parameter-types)))
            acc)))
      {:className       (as-dot-string class-like)
       ;; TODO this might be dropping polymorphic newBuilder(..)
       ;; we want all of them, get them special? group-by :name?
       :staticMethods   (sorted-map)
       :instanceMethods (sorted-map)}
      members)))

(defn enum-values [clazz]
  (assert (class? clazz))
  (let [meth (.getMethod clazz "values" (into-array Class []))]
    (->> (.invoke meth nil (into-array Object []))
         (map #(.name %))
         sort
         vec)))

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

(defn extract-from-bytes
  ([model-cfg bytes]
   (extract-from-bytes model-cfg bytes identity))
  ([model-cfg bytes validator!]
   (let [response (genai/generate-content model-cfg [{:mimeType "text/html" :partData bytes}])
         edn (genai/response-json response)]
     (validator! edn)
     edn)))

(def version-schema {:type        "STRING"
                     :example     "2.47.0"
                     :description "package version"
                     :pattern     "^[0-9]+\\.[0-9]+\\.[0-9]+$"})

(defn- $_extract-builder-setters
  "=> {:setterName {:name 'paramName' :type 'java.lang.Long'}}"
  ([model package builder-like]
   (assert (builder-like? builder-like))
   (assert (string? (:store package)))
   (let [url               (str (g/coerce some? (:packageRootUrl package)) builder-like)
         {setters :setterMethods :as reflection} (reflect-builder builder-like)
         _                 (g/coerce [:seqable :string] setters)
         systemInstruction (str "extract the builder setters methods description and parameter type."
                                " please use fully qualified package names for all types that reference gcp sdks")
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
         edn               (store/extract-java-ref-aside (:store package) cfg url validator)]
     {:type :builder
      :className (as-dot-string builder-like)
      :setterMethods edn})))

(defn- $_extract-readonly
  "{:className ''
    :doc ''
    :staticMethods {sym {:parameters [..] :returnType ''}
    :instanceMethods {:getterName -> {:returnType '', :doc ''}}}"
  [model package class-like]
  (assert ((set (:classes package)) (as-dot-string class-like)))
  (assert (string? (:store package)))
  (let [url (str (g/coerce some? (:packageRootUrl package)) class-like)
        {:keys [instanceMethods staticMethods] :as reflection} (reflect-readonly class-like)
        getter-method-names (map name (keys instanceMethods))
        systemInstruction (str "identity the class & extract its constructors and methods. omit method entries for .hashCode, and .equals()."
                               "please use fully qualified package names for all types that reference gcp sdks")
        parameters-schema {:type     "ARRAY"
                           :nullable true
                           :items    {:type       "OBJECT"
                                      :properties {"name" {:type        "STRING"
                                                           :description "the name of the parameter"}
                                                   "type" {:type        "STRING"
                                                           :description "the fully qualified type descriptor. if a list, use List<$TYPE> syntax"}}}}
        method-schema {:type        "OBJECT"
                       :description "a description of the method. if the method has 0 parameters, omit it"
                       :required    ["returnType" "doc"]
                       :properties  {"returnType" {:type        "STRING"
                                                   :description "the fully qualified type descriptor. if a list, use List<$TYPE> syntax"}
                                     "doc"        {:type        "STRING"
                                                   :description "description of the method"}
                                     "parameters" parameters-schema}}
        static-methods-schema {:type       "OBJECT"
                               :required   (map name (keys staticMethods))
                               :properties (into {}
                                                 (map
                                                   (fn [static-method]
                                                     [(name static-method)
                                                      {:type "ARRAY"
                                                       :minLength (count (get staticMethods static-method))
                                                       :maxLength (count (get staticMethods static-method))
                                                       :description "each polymorphic parameter list for the static method"
                                                       :items parameters-schema}]))
                                                 (keys staticMethods))}
        generationConfig {:responseMimeType "application/json"
                          :responseSchema   {:type       "OBJECT"
                                             :required   ["version" "doc" "staticMethods" "getterMethods"]
                                             :properties {"version" version-schema
                                                          "doc" {:type "STRING"
                                                                 :description "description of the class"}
                                                          "staticMethods" static-methods-schema
                                                          "getterMethods" {:type       "OBJECT"
                                                                           :required   getter-method-names
                                                                           :properties (into {} (map #(vector % method-schema)) getter-method-names)}}}}
        cfg (assoc model :systemInstruction systemInstruction
                         :generationConfig generationConfig)
        validator (fn [{:keys [version getterMethods]}]
                    (when (not= version (:version package))
                      (throw (ex-info (str "found different package version for class '" class-like "'")
                                      {:extracted-version version
                                       :package-version (:version package)
                                       :class-like      class-like})))
                    (when-not (and (map? getterMethods)
                                   (= (count getter-method-names) (count getterMethods))
                                   (= (set getter-method-names) (set (map name (keys getterMethods)))))
                      (throw (ex-info "returned keys did not match" {:actual (into (sorted-set) (map name) (keys getterMethods))
                                                                     :members (into (sorted-set) getter-method-names)}))))
        edn (store/extract-java-ref-aside (:store package) cfg url validator)]
    (assoc reflection :className class-like
                      :type :readonly
                      :doc (:doc edn)
                      :getterMethods (:getterMethods edn)
                      :staticMethods (:staticMethods edn))))

(defn- $_extract-enum-detail
  [model package enum-like]
  (assert ((set (:enums package)) (as-dot-string enum-like)))
  (assert (string? (:store package)))
  (let [values (enum-values (g/coerce some? (resolve (symbol (as-dollar-string enum-like)))))
        url (str (g/coerce some? (:packageRootUrl package)) (as-dot-string enum-like))
        systemInstruction (str "identity the description of the enum class & each of individual value")
        doc-schema {:type "STRING"
                    :nullable true
                    :description "description of enum value"}
        generationConfig {:responseMimeType "application/json"
                          :responseSchema   {:type       "OBJECT"
                                             :required   ["version" "doc" "values"]
                                             :properties {"version" version-schema
                                                          "doc"     {:type        "STRING"
                                                                     :description "description of the enum class"}
                                                          "values"  {:type       "OBJECT"
                                                                     :required   ["doc" "values"]
                                                                     :properties {"doc"    {:type        "STRING"
                                                                                            :nullable    true
                                                                                            :description "enum class description"}
                                                                                  "values" {:type       "OBJECT"
                                                                                            :required   values
                                                                                            :properties (into {} (map #(vector % doc-schema)) values)}}}}}}
        cfg (assoc model :systemInstruction systemInstruction
                         :generationConfig generationConfig)
        validator (fn [{:keys [version] :as edn}]
                    (when (not= version (:version package))
                      (throw (ex-info (str "found different package version for enum '" enum-like "'")
                                      {:edn             edn
                                       :package-version (:version package)
                                       :class-like      enum-like}))))
        res (store/extract-java-ref-aside (:store package) cfg url validator)]
    (assoc res :type :enum)))

(defn $extract-type-detail
  ([package class-like]
   ($extract-type-detail models/pro-2 package class-like))
  ([model {:keys [classes] :as package} class-like]
   (cond
     ;; TODO exceptions! settings! clients!
     ((set (:enums package)) (as-dot-string class-like))
     ($_extract-enum-detail model package class-like)

     (builder-like? class-like)
     ($_extract-builder-setters model package class-like)

     true
     ($_extract-readonly model package class-like))))

(comment

  ($extract-type-detail bigquery "com.google.cloud.bigquery.LoadJobConfiguration") ;=> read-only
  ($extract-type-detail bigquery "com.google.cloud.bigquery.LoadJobConfiguration.Builder") ;=> builder
  ($extract-type-detail bigquery "com.google.cloud.bigquery.Acl.Entity.Type") ;=> enum
  ($extract-type-detail bigquery "com.google.cloud.bigquery.WriteChannelConfiguration")
  ($extract-type-detail bigquery "com.google.cloud.bigquery.WriteChannelConfiguration.Builder")
  )