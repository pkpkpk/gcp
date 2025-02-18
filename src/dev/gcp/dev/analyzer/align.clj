(ns ^{:doc "match getters/setters/parameters together semantically.
            naming conventions are sometimes inconsistent ie easier to ask a gemini model than to parse"}
  gcp.dev.analyzer.align
  (:require [gcp.dev.store :as store]
            [gcp.dev.models :as models]
            [gcp.global :as g]))

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
         cfg               (assoc models/flash-2 :systemInstruction systemInstruction
                                                 :generationConfig generationConfig)
         validator!        (fn [edn]
                             (if (not= (set setters) (set (map name (keys edn))))
                               (throw (ex-info "missing setter method!" {:getters getters
                                                                         :setters setters
                                                                         :edn edn}))
                               (if (not (every? some? (vals edn)))
                                 (throw (ex-info "expected getter for every setter" {:getters getters
                                                                                     :setters setters
                                                                                     :edn edn}))
                                 (if-not (or (set (vals edn)) (set getters)
                                             (clojure.set/subset? (set (vals edn)) (set getters)))
                                   (throw (ex-info "incorrect keys" {:getters getters
                                                                     :setters setters
                                                                     :edn edn}))))))
         res (store/generate-content-aside (:store package) cfg getters validator!)]
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
        validator!        (fn [edn]
                            (if (not= (set static-params) (set (map name (keys edn))))
                              (throw (ex-info "missing static param!"
                                              {:getters getters :static-params static-params :edn edn}))
                              (if (not (every? some? (vals edn)))
                                (throw (ex-info "expected getter for every static param"
                                                {:getters getters :static-params static-params :edn edn}))
                                (if-not (clojure.set/subset? (set (vals edn)) (into #{} getters))
                                  (throw (ex-info "incorrect keys" {:edn     edn
                                                                    :getters getters
                                                                    :static-params static-params}))))))
        res               (store/generate-content-aside (:store package) cfg getters validator!)]
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
        systemInstruction (str "given a list of class names, match them to a tag")
        cfg               (assoc models/pro :systemInstruction systemInstruction
                                            :generationConfig generationConfig)
        validator! (fn [edn]
                     (when-not (= (count edn) (count variant-classes))
                       (throw (ex-info "incorrect count" {:expected (count variant-classes)
                                                          :actual (count edn)})))
                     (when-not (= variant-classes (set (map name (keys edn))))
                       (throw (ex-info "incorrect classes" {:variant-classes variant-classes
                                                            :edn-keys (set (map name (keys edn)))})))
                     (when-not (or (= (set (vals edn)) (set variant-tags))
                                   (clojure.set/subset? (set (vals edn)) (set variant-tags)))
                       (throw (ex-info "incorrect tags" {:variant-tags variant-tags
                                                         :edn edn}))))
        edn (store/generate-content-aside (:store package) cfg (vec variant-tags) validator!)]
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
        validator! (fn [edn]
                     (when-not (= variant-tags (set (map name (keys edn))))
                       (throw (ex-info "incorrect tags" {:expected-tags (set variant-tags)
                                                         :actual-tags (set (map name (keys edn)))})))
                     (when-not (clojure.set/subset? (set (vals edn)) (set static-methods))
                       (throw (ex-info "incorrect methods" {:expected-superset static-methods
                                                            :actual-values (vals edn)}))))
        edn (store/generate-content-aside (:store package) cfg (vec variant-tags) validator!)]
    (into (sorted-map) (map (fn [[k v]] [(name k) (symbol v)])) edn)))

