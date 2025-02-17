(ns ^{:doc "match getters/setters/parameters together semantically.
            naming conventions are sometimes inconsistent ie easier to ask a gemini model than to parse"}
  gcp.dev.analyzer.align
  (:require [gcp.dev.store :as store]
            [gcp.dev.models :as models]
            [gcp.global :as g]))

(defn align-accessors
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

(defn align-static-params
  "static-params ==  Foo.of(A a, B b) => ['a', 'b']
   getters == foo.getA() , foo.B() etc => ['getA', 'B']"
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

(defn align-variant-class-tags
  [package variant-classes variant-tags]
  (let [variant-schema    {:type        "STRING"
                           :nullable    false
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
                     (when-not (clojure.set/subset? (set (vals edn)) variant-tags)
                       (throw (ex-info "incorrect tags" {:variant-tags variant-tags
                                                         :edn edn}))))
        edn (store/generate-content-aside (:store package) cfg (vec variant-tags) validator!)]
    (into (sorted-map) (map (fn [[k v]] [(name k) v])) edn)))