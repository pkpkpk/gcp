(ns ^{:doc "match getters/setters/parameters together semantically.
            naming conventions are sometimes inconsistent ie easier to ask a gemini model than to parse"}
  gcp.dev.analyzer.align
  (:require [clojure.data :refer [diff]]
            [gcp.dev.models :as models]
            [gcp.dev.store :as store]
            [gcp.global :as g]))

(defn- validate-alignment!
  "expected-keys must be exact.
   expected-values can be exact or a superset of extracted values"
  [expected-keys expected-values]
  (g/coerce [:seqable :string] expected-keys)
  (g/coerce [:seqable :string] expected-values)
  (fn [edn]
    (when (not= (set expected-keys) (set (map name (keys edn))))
      (throw (ex-info "returned alignment keys are incorrect"
                      {:expected-keys expected-keys
                       :actual-keys   (set (map name (keys edn)))
                       :edn           edn})))
    (let [expected-values (set expected-values)
          aligned-values  (into (sorted-set) (vals edn))]
      (if-not (or (= expected-values aligned-values)
                  (clojure.set/subset? aligned-values expected-values))
        (let [[unused bad] (diff expected-values aligned-values)]
          (throw (ex-info "incorrect values" {:expected-values expected-values
                                              :aligned-values  aligned-values
                                              :unused          unused
                                              :hallucinated    bad})))))))

(defn align-accessor-methods
  "setters == Foo.builder.setBar<param> -> ['setBar', 'setBaz', ...]
   getters == Foo.getBar() , Foo.baz() ->  ['getBar', 'baz', ...]"
  ([package getters setters]
   (let [getter-schema     {:type        "STRING"
                            :nullable    false
                            :description "the read only method that corresponds to the setter"}
         generationConfig  {:responseMimeType "application/json"
                            :responseSchema   {:type       "OBJECT"
                                               :required   setters
                                               :properties (into {} (map #(vector % getter-schema)) setters)}}
         systemInstruction (str "given a list of getters, match them to their setter in the response schema."
                                "for every setter there is a getter, but not every getter has a setter")
         cfg               (assoc models/pro-2 :systemInstruction systemInstruction
                                               :generationConfig generationConfig)
         res (store/generate-content-aside (:store package) cfg getters (validate-alignment! setters getters))]
     (into (sorted-map) (map (fn [[k v]] [k (keyword v)])) res))))

(defn align-static-params-to-getters
  "static-params ==  Foo.of(A a, B b) => ['a', 'b']
   getters == foo.getA() , foo.B() etc => ['getA', 'B']
   |-alignment-> {:a 'getA', :b 'B'} "
  [package static-params getterMethods]
  (let [getters           (g/coerce [:seqable :string] (vec (sort (map name (keys getterMethods)))))
        getter-schema     {:type        "STRING"
                           :nullable    false
                           :enum getters
                           :description "the name of the read only method that corresponds to the param"}
        generationConfig  {:responseMimeType "application/json"
                           :responseSchema   {:type       "OBJECT"
                                              :required   (vec static-params)
                                              :properties (into {} (map #(vector % getter-schema)) static-params)}}
        systemInstruction "for each parameter name, produce the getter method that produces it"
        cfg               (assoc models/flash :systemInstruction systemInstruction
                                              :generationConfig generationConfig)
        res               (store/generate-content-aside (:store package) cfg getters (validate-alignment! static-params getters))]
    (into (sorted-map) (map (fn [[k v]] [k (keyword v)])) res)))

(defn align-variant-class-to-variant-tags
  "concrete-unions are base classes that can accept subclasses with own settings or tags
   that provide identity ie FormatOptions is a base class; CsvOptions, ParquetOptions etc are instances
   of FormatOptions with their own per format configuration, but FormatOptions can also create
   empty instances for JSON via static methods.

   This alignment call marries the subclasses to their :type tag, so we know what to do with edn args"
  [package variant-classes variant-tags]
  (let [variant-schema    {:type       "STRING"
                           :nullable   false
                           :enum       (vec variant-tags)
                           :description "the tag that corresponds to the class"}
        generationConfig  {:responseMimeType "application/json"
                           :responseSchema   {:type       "OBJECT"
                                              :required   (vec variant-classes)
                                              :properties (into {} (map #(vector % variant-schema)) variant-classes)}}
        systemInstruction "given a list of class names, match them to a tag"
        cfg               (assoc models/pro :systemInstruction systemInstruction
                                            :generationConfig generationConfig)
        edn (store/generate-content-aside (:store package) cfg (vec variant-tags) (validate-alignment! variant-classes variant-tags))]
    (into (sorted-map) (map (fn [[k v]] [(name k) v])) edn)))

(defn align-variant-tags-to-static-methods
  "For static-unions:
      given tags that do not have subclasses, choose the static method that instantiates the base
      class correctly for that tag"
  [package variant-tags static-methods]
  (let [method-schema     {:type        "STRING"
                           :nullable    false
                           :enum        (vec static-methods)
                           :description "method name corresponding to the tag"}
        generationConfig  {:responseMimeType "application/json"
                           :responseSchema   {:type       "OBJECT"
                                              :required   (vec variant-tags)
                                              :properties (into {} (map #(vector % method-schema)) variant-tags)}}
        systemInstruction (str "for each tag, match it with the correct method name")
        cfg               (assoc models/pro-2 :systemInstruction systemInstruction
                                              :generationConfig generationConfig)
        edn (store/generate-content-aside (:store package) cfg (vec variant-tags) (validate-alignment! variant-tags static-methods))]
    (into (sorted-map) (map (fn [[k v]] [(name k) (symbol v)])) edn)))

