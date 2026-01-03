(ns gcp.dev.toolchain.emitter
  (:require
   [clojure.string :as string]
   [gcp.dev.toolchain.analyzer :as ana]
   [gcp.dev.toolchain.malli :as m]
   [gcp.dev.util :as u]
   [zprint.core :as zp]))

(defn- get-deps [node version]
  (let [current-class (:className node)
        top-level-class (first (string/split current-class #"\."))]
    (->> (:typeDependencies node)
      (remove #(u/native-type (str %)))
      (map (fn [dep]
             (let [dep-str (str dep)
                   {:keys [package class]} (u/split-fqcn dep-str)
                   class-parts (string/split class #"\.")
                   top-class (first (string/split (first class-parts) #"\$"))
                   is-local (and (string/starts-with? dep-str "com.google.cloud")
                                 (= top-class top-level-class))]
               (if is-local
                 {:fqcn dep-str
                  :local? true
                  :full-class class}
                 (let [is-cloud (string/starts-with? dep-str "com.google.cloud")
                       ns-sym (if is-cloud
                                (symbol (str (u/package-to-ns package version) "." top-class))
                                (u/infer-foreign-ns dep-str))
                       alias (if is-cloud
                               (symbol top-class)
                               (symbol (last (string/split (name ns-sym) #"\."))))
                       exists? (if is-cloud true (u/foreign-binding-exists? ns-sym))]
                   {:fqcn dep-str
                    :ns ns-sym
                    :alias alias
                    :cls top-class
                    :full-class class
                    :local? false
                    :exists? exists?})))))
      (into #{}))))

(defn- needs-protobuf? [node]
  (if-not (map? node)
    false
    (let [field-types (map :type (vals (:fields node)))
          proto-types #{"com.google.protobuf.ByteString"
                        "com.google.protobuf.Duration"
                        "com.google.protobuf.Timestamp"
                        "com.google.protobuf.Struct"
                        "com.google.protobuf.Value"}]
      (boolean (some #(contains? proto-types (str %)) field-types)))))

(defn- collect-all-nested-fqcns [node]
  (let [pkg (:package node)
        main-class (:className node)]
    (letfn [(traverse [n parent-name]
              (let [current-name (str parent-name "$" (:name n))]
                (cons (str pkg "." current-name)
                      (mapcat #(traverse % current-name) (:nested n)))))]
      (mapcat #(traverse % main-class) (:nested node)))))

(defn- emit-ns-form [node deps version metadata]
  (let [ns-name (symbol (str (u/package-to-ns (:package node) version) "." (:className node)))
        main-fqcn (str (:package node) "." (:className node))
        nested-fqcns (collect-all-nested-fqcns node)
        imports (map symbol (cons main-fqcn nested-fqcns))
        requires (->> deps
                      (remove :local?)
                      (filter :exists?)
                      (map (fn [{:keys [ns alias]}]
                             [ns :as alias]))
                      (into #{})
                      (sort-by first))]
    `(~'ns ~ns-name
       ~@(when metadata [metadata])
       (:require [gcp.global :as ~'global]
                 ~@requires)
       (:import ~@imports))))

(defn- resolve-conversion [type deps direction]
  (let [is-list (or (and (sequential? type)
                         (let [base (str (first type))]
                           (or (= base "java.util.List")
                               (= base "com.google.common.collect.ImmutableList")
                               (= base "java.lang.Iterable"))))
                    (let [t (str type)]
                      (or (string/starts-with? t "java.util.List")
                          (string/starts-with? t "com.google.common.collect.ImmutableList"))))
        inner-type (if (sequential? type)
                     (second type)
                     (if is-list
                       (let [m (re-find #"<(.+)>" (str type))]
                         (if m (second m) "Object"))
                       type))
        inner-type-str (str inner-type)
        dep (first (filter #(= (:fqcn %) inner-type-str) deps))]
    (cond
      (= (str type) "com.google.protobuf.ProtocolStringList")
      (if (= direction :from-edn) `(~'mapv ~'clojure.core/str) `(~'into []))

      (and dep (:exists? dep))
      (let [func (cond
                   (:local? dep)
                   (if (string/includes? (:full-class dep) ".")
                     (symbol (str (string/replace (:full-class dep) "." "_") ":" (name direction)))
                     (symbol (name direction)))

                   (not (string/starts-with? (:fqcn dep) "com.google.cloud"))
                   (symbol (str (:alias dep)) (str (:cls dep) "-" (name direction)))

                   :else
                   (if (string/includes? (:full-class dep) ".")
                     (symbol (str (:alias dep)) (str (string/replace (:full-class dep) "." "_") ":" (name direction)))
                     (symbol (str (:alias dep)) (name direction))))]
        (if is-list
          (if (= direction :from-edn)
            `(~'mapv ~func)
            `(~'map ~func))
          func))

      (or (= inner-type-str "float") (= inner-type-str "Float"))
      (if (= direction :to-edn) `double `identity)

      :else
      (if is-list
        (if (or (= inner-type-str "java.lang.String")
                (= inner-type-str "String"))
          (if (= direction :from-edn) `(~'mapv ~'clojure.core/str) `(~'into []))
          (if (= direction :from-edn) `vec `vec))
        `identity))))

(defn- emit-accessor-from-edn [node deps version prefix class-sym-name func-name-base]
  (let [class-name (:className node)
        func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'from-edn)
        class-sym (symbol class-sym-name)
        builder-method (symbol (str class-sym "/newBuilder"))
        fields (:fields node)
        builder-calls (for [[field-name {:keys [setterMethod type]}] fields]
                        (let [setter (symbol (str "." setterMethod))
                              conversion (resolve-conversion type deps :from-edn)
                              val-sym (gensym "v")]
                          `(~'when-some [~val-sym (~'get ~'arg ~(keyword field-name))]
                             (~setter ~'builder
                               ~(if (sequential? conversion)
                                  `(~@conversion (~'global/to-vec ~val-sym))
                                  (if (= conversion `identity)
                                    val-sym
                                    `(~conversion ~val-sym)))))))]
    `(~'defn ~func-name {:tag ~class-sym} [~'arg]
       ~@(when-not prefix [`(~'global/strict! ~(u/schema-key (:package node) (:className node) version) ~'arg)])
       (~'let [~'builder (~builder-method)]
         ~@builder-calls
         (.build ~'builder)))))

(defn- emit-accessor-to-edn [node deps version prefix class-sym-name func-name-base]
  (let [class-name (:className node)
        func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'to-edn)
        class-sym (symbol class-sym-name)
        fields (:fields node)
        assoc-steps (for [[field-name {:keys [getterMethod type]}] fields]
                      (let [getter (symbol (str "." getterMethod))
                            k (keyword field-name)
                            conversion (resolve-conversion type deps :to-edn)]
                        `(~'true
                           (~'assoc ~k
                                    ~(if (= conversion `identity)
                                       `(~getter ~'arg)
                                       (if (sequential? conversion)
                                         `(~@conversion (~getter ~'arg))
                                         `(~conversion (~getter ~'arg))))))))]
    `(~'defn ~func-name [~(with-meta 'arg {:tag class-sym})]
       ~@(when-not prefix [`{:post [(~'global/strict! ~(u/schema-key (:package node) (:className node) version) ~'%)]}])
       (cond-> {}
         ~@(mapcat identity assoc-steps)))))

(defn- emit-enum-from-edn [node version prefix class-sym-name func-name-base]
  (let [class-name (:className node)
        func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'from-edn)
        class-sym (symbol class-sym-name)]
    `(~'defn ~(with-meta func-name {:tag class-sym}) [~'arg]
       (~'when ~'arg
         (~(symbol (str class-sym "/valueOf")) (clojure.core/name ~'arg))))))

(defn- emit-enum-to-edn [node version prefix class-sym-name func-name-base]
  (let [class-name (:className node)
        func-name (if prefix (symbol (str (string/replace func-name-base "." "_") prefix)) 'to-edn)
        class-sym (symbol class-sym-name)]
    `(~'defn ~func-name [~(with-meta 'arg {:tag class-sym})]
       (~'when ~'arg
         (.name ~'arg)))))

(defn- schema-var-name [class-name]
  (symbol (str (string/replace class-name "." "_") "-schema")))

(defn- emit-schema [node version]
  (let [class-name (:className node)
        key (u/schema-key (:package node) class-name version)
        malli-schema (m/->schema node version)
        schema-def `[:and
                     {:doc ~(u/clean-doc (:doc node))
                      :class (quote ~(symbol (str (:package node) "." class-name)))}
                     ~malli-schema]]
    `(~'def ~(schema-var-name class-name) ~schema-def)))
(defn- emit-register [node version]
  (let [class-name (:className node)
        key (u/schema-key (:package node) class-name version)]
    `(~'global/register-schema! ~key ~(schema-var-name class-name))))

(defn- emit-all-nested-forms [parent-node deps version main-class-name]
  (mapcat (fn [n]
            (let [ana-n (ana/analyze-class-node n)
                  nested-name (:name n)
                  current-full-name (str (:className parent-node) "." nested-name)
                  ana-n (assoc ana-n :className current-full-name)
                  nested-class-sym (string/replace current-full-name "." "$")
                  current-deps (get-deps ana-n version)]
              (remove nil?
                (concat
                  (emit-all-nested-forms ana-n current-deps version main-class-name)
                  (case (:type ana-n)
                    :accessor [(emit-accessor-from-edn ana-n current-deps version ":from-edn" nested-class-sym current-full-name)
                               (emit-accessor-to-edn ana-n current-deps version ":to-edn" nested-class-sym current-full-name)]
                    :enum [(emit-enum-from-edn ana-n version ":from-edn" nested-class-sym current-full-name)
                           (emit-enum-to-edn ana-n version ":to-edn" nested-class-sym current-full-name)]
                    :concrete-union [(emit-accessor-from-edn ana-n current-deps version ":from-edn" nested-class-sym current-full-name)
                                     (emit-accessor-to-edn ana-n current-deps version ":to-edn" nested-class-sym current-full-name)]
                    nil)
                  (when-not (= (:type ana-n) :builder)
                    [(emit-schema ana-n version)
                     (emit-register ana-n version)])))))
          (:nested parent-node)))

(defn compile-class-forms
  "Compiles a class node into a sequence of Clojure forms.
   Accepts optional metadata map to inject into the namespace declaration."
  ([node] (compile-class-forms node nil))
  ([node metadata]
   (let [version (u/extract-version (:doc node))
         deps (get-deps node version)
         ns-form (emit-ns-form node deps version metadata)
         type (:type node)
         main-class-name (:className node)
         nested-fnames (letfn [(collect [prefix n]
                                 (let [ana-n (ana/analyze-class-node n)
                                       full (str prefix "_" (:name n))
                                       supported? (contains? #{:accessor :enum :concrete-union :abstract-union} (:type ana-n))]
                                   (concat (when supported?
                                             [(symbol (str full ":from-edn"))
                                              (symbol (str full ":to-edn"))])
                                           (mapcat #(collect (str prefix "_" (:name n)) %) (:nested n)))))]
                         (mapcat #(collect main-class-name %) (:nested node)))
         declare-form (when (seq nested-fnames) `(~'declare ~@nested-fnames))
         nested-forms (emit-all-nested-forms node deps version main-class-name)
         from-edn (case type
                    :accessor (emit-accessor-from-edn node deps version nil main-class-name main-class-name)
                    :enum (emit-enum-from-edn node version nil main-class-name main-class-name)
                    :concrete-union (emit-accessor-from-edn node deps version nil main-class-name main-class-name)
                    :abstract-union (emit-accessor-from-edn node deps version nil main-class-name main-class-name)
                    nil)
         to-edn (case type
                  :accessor (emit-accessor-to-edn node deps version nil main-class-name main-class-name)
                  :enum (emit-enum-to-edn node version nil main-class-name main-class-name)
                  :concrete-union (emit-accessor-to-edn node deps version nil main-class-name main-class-name)
                  :abstract-union (emit-accessor-to-edn node deps version nil main-class-name main-class-name)
                  nil)
         schema (emit-schema node version)
         register (emit-register node version)]
     (remove nil? (concat [ns-form declare-form] nested-forms [from-edn to-edn schema register])))))

(defn compile-class
  "Compiles a class node into a formatted string string.
   Accepts optional metadata map."
  ([node] (compile-class node nil))
  ([node metadata]
   (let [preamble ";; THIS FILE IS GENERATED; DO NOT EDIT\n"
         forms (compile-class-forms node metadata)
         class-name (:className node)
         fix-hints (fn [src]
                     (-> src
                         ;; (defn from-edn {:tag Class} [arg] ...) -> (defn ^Class from-edn [arg] ...)
                         (string/replace #"(defn\s+)(?:from-edn|([\w:]+from-edn))\s+\{:tag ([\w\$]+)\}"
                                         (fn [[_ d1 n1 t1]] (str d1 "^" (or t1 "") " " (or n1 "from-edn"))))
                         ;; (defn to-edn [arg] ...) -> (defn to-edn [^Class arg] ...)
                         (string/replace #"(defn (?:[\w:]+)?to-edn\s+)\n?\s*\[arg\]"
                                         (str "$1 [^" class-name " arg]"))))]
     (str preamble
          (string/join "\n\n"
                       (map (comp fix-hints zp/zprint-str) forms))))))
