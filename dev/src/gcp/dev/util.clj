(ns gcp.dev.util
  (:require
   [clj-http.client :as http]
   [clojure.java.io :as io]
   [clojure.reflect]
   [clojure.string :as string]
   [edamame.core :as edamame]
   [rewrite-clj.node :as n]
   [rewrite-clj.parser :as p]
   [rewrite-clj.zip :as z]
   [zprint.core :as zp])
  (:import
   (java.io ByteArrayOutputStream)
   (org.objectweb.asm ClassReader ClassVisitor Opcodes)))

(set! *print-namespace-maps* false)

(defn relative-path [base f]
  (let [base-path (.getAbsolutePath (io/file base))
        f-path (.getAbsolutePath (io/file f))]
    (if (string/starts-with? f-path base-path)
      (subs f-path (inc (count base-path)))
      f-path)))

(defn get-googleapis-repos-path []
  (let [root (System/getenv "GOOGLEAPIS_REPOS_PATH")]
    (cond
      (string/blank? root)
      (throw (ex-info "GOOGLEAPIS_REPOS_PATH environment variable is not set." {}))

      (not (.isAbsolute (io/file root)))
      (throw (ex-info "GOOGLEAPIS_REPOS_PATH must be an absolute path." {:path root}))

      :else root)))

(defn get-gcp-repo-root []
  (let [root (System/getenv "GCP_REPO_ROOT")]
    (cond
      (string/blank? root)
      (throw (ex-info "GCP_REPO_ROOT environment variable is not set." {}))

      (not (.isAbsolute (io/file root)))
      (throw (ex-info "GCP_REPO_ROOT must be an absolute path." {:path root}))

      (not (.exists (io/file root)))
      (throw (ex-info "GCP_REPO_ROOT path does not exist." {:path root}))

      :else root)))

(defn class-bytes [^Class cls]
  (with-open [is (.getResourceAsStream cls (str (.getSimpleName cls) ".class"))]
    (let [os (ByteArrayOutputStream.)]
      (loop []
        (let [buf (byte-array 4096)
              len (.read is buf)]
          (when (pos? len)
            (.write os buf 0 len)
            (recur))))
      (.toByteArray os))))

(defn all-method-info [cls]
  (let [acc (atom [])]
    (doto (ClassReader. (class-bytes cls))
      (.accept
        (proxy [ClassVisitor] [Opcodes/ASM9]
          (visitMethod [access name desc signature exceptions]
            (swap! acc conj
                   {:name       name
                    :desc       desc
                    :signature  signature
                    :access     access
                    :exceptions exceptions})
            (proxy-super visitMethod access name desc signature exceptions)))
        0))
    @acc))

(defn clean-doc [doc]
  (when (and doc (not (string/blank? doc)))
    (-> doc
        string/trim
        (string/replace #"\\\S\\" "")
        (string/replace #"\\n" " ")
        (string/replace #"\n" " ")
        (string/replace #"\\\\" "\\")
        (string/replace #"\s+" " "))))

(defn excluded-type-name? [type-name]
  (or (string/ends-with? type-name "Impl")
      (string/ends-with? type-name "Helper")
      (string/ends-with? type-name "OrBuilder")
      (string/ends-with? type-name "Callable")
      (string/ends-with? type-name "Serializer")
      (string/ends-with? type-name "UnusedPrivateParameter")
      (string/ends-with? type-name "Proto")
      (string/ends-with? type-name "AutoCloseable")
      (string/ends-with? type-name "Closeable")
      (string/ends-with? type-name "Serializable")
      (= type-name "BigQueryErrorMessages")))

(defn get-url-bytes [^String url]
  (println (str "fetching url -> " url))
  (let [{:keys [status body] :as response} (http/get url {:redirect-strategy :none :as :byte-array})]
    (if (= 200 status) ; google docs will 301 on missing doc
      body
      (throw (ex-info "expected 200 response" {:url url :response response})))))

(defn get-java-ref [^String url]
  (let [bs (get-url-bytes url)
        s  (String. bs "UTF-8")
        ;; can't be bothered to parse but this is decent 2/3 cut
        header-open-start (string/index-of s "<h1")
        s (subs s (inc header-open-start))
        header-open-close (string/index-of s ">")
        header-open-end (string/index-of s "</h1>")
        header (string/trim (subs s (inc header-open-close) header-open-end))
        _ (println "retrieved reference doc for " (pr-str header))
        article (subs s
                      (string/index-of s "<article>")
                      (string/index-of s "</article>"))
        article (string/trim article)]
    (.getBytes (str header article))))

(defn list-like? [t]
  (or (string/starts-with? t "List")
      (string/starts-with? t "java.util.List")))

(defn array-like? [t]
  (string/ends-with? t "[]"))

(def illegal-native-type
  #{"java.util.Map"
    "java.util.List"})

;; STRICTNESS WARNING:
;; This set defines types that are treated as opaque primitives (no conversion, no bindings).
;; It MUST ONLY contain foundational Java types (java.lang.*, java.util.*) and primitives.
;; DO NOT add Google Cloud types (com.google.cloud.*) to this set to silence "Broken dependency"
;; errors. If a GCP dependency is missing, you must either:
;;   1. Generate the missing package.
;;   2. Add a certified manual binding to gcp.foreign.*.
;;   3. Add a custom mapping in packages.clj.
;; Bypassing architectural gaps by 'fudging' native-type is strictly forbidden.
(def native-type
  #{"java.lang.Boolean"
    "boolean"
    "java.lang.String"
    "java.lang.Integer"
    "int"
    "java.lang.Long"
    "long"
    "java.lang.Object"
    "Map<java.lang.String, java.lang.String>"
    "Map<java.lang.String,java.lang.String>"
    "Map<String,String>"
    "Map<String, String>"
    "java.util.Map<String, String>"
    "java.util.Map<String,String>"
    "java.util.Map<java.lang.String, java.lang.String>"
    "java.util.Map<java.lang.String,java.lang.String>"
    "List<java.lang.String>"
    "List<String>"
    "java.util.List<java.lang.String>"
    "java.util.List<String>"
    "java.math.BigDecimal"
    "java.math.BigInteger"
    "java.sql.ResultSet"
    "java.time.Instant"
    "byte<>"
    "byte"
    "double"
    "float"
    "java.util.Map"
    "java.util.List"
    "java.util.Set"
    "java.util.Iterator"
    "java.util.Optional"
    "java.util.AbstractList"
    "java.lang.Iterable"
    "java.lang.Exception"
    "java.lang.Throwable"
    "java.lang.Error"
    "java.lang.AutoCloseable"
    "com.google.cloud.StringEnumValue"
    "void"})

(defn parse-type [t]
  (assert (string? t))
  (cond

    (string/starts-with? t "java.util.List<")
    (let [start (subs t 15)]
      (subs start 0 (.indexOf start ">")))

    (string/starts-with? t "List<")
    (let [start (subs t 5)]
      (subs start 0 (.indexOf start ">")))

    (string/starts-with? t "java.util.Map<")
    (if (string/starts-with? t "java.util.Map<java.lang.String,")
      (string/trim (subs t 31 (.indexOf t ">")))
      (throw (Exception. (str "unimplemented non-string key for type '" t "'"))))

    (string/starts-with? t "Map<")
    (if (string/starts-with? t "Map<java.lang.String,")
      (string/trim (subs t 21 (.indexOf t ">")))
      (throw (Exception. (str "unimplemented non-string key for type '" t "'"))))

    :else t))

(defn extract-version [doc]
  (when doc
    (when-let [m (re-find #"google\.cloud\.[^.]+\.([^.]+)\.[^.]+" doc)]
      (let [v (second m)]
        (when (re-matches #"v\d+.*" v)
          v)))))

(defn to-vec [v]
  (cond
    (nil? v) []
    (sequential? v) (vec v)
    :else [v]))

(defn property-name [method-name]
  (cond
    (string/starts-with? method-name "get")
    (let [s (subs method-name 3)]
      (if (seq s)
        (str (string/lower-case (subs s 0 1)) (subs s 1))
        ""))
    (string/starts-with? method-name "is")
    (let [s (subs method-name 2)]
      (if (seq s)
        (str (string/lower-case (subs s 0 1)) (subs s 1))
        ""))
    (string/starts-with? method-name "set")
    (let [s (subs method-name 3)]
      (if (seq s)
        (str (string/lower-case (subs s 0 1)) (subs s 1))
        ""))
    (string/starts-with? method-name "addAll")
    (let [s (subs method-name 6)]
      (if (seq s)
        (str (string/lower-case (subs s 0 1)) (subs s 1))
        ""))
    :else method-name))

(def ^:private ns-mapping
  {"artifactregistry" "artifact-registry"})

(defn package-to-ns [package-name]
  (let [pkg-str (str package-name)
        ;; Strip common prefixes: com.google.cloud, com.google.devtools, etc.
        base (if (string/starts-with? pkg-str "com.google.")
               (let [parts (string/split pkg-str #"\.")]
                 (if (> (count parts) 3)
                   (string/join "." (drop 3 parts))
                   pkg-str))
               pkg-str)
        ;; Apply mappings to the first segment of the base
        base-parts (string/split base #"\.")
        mapped-first (get ns-mapping (first base-parts) (first base-parts))
        ;; Construct strict hierarchy: gcp.<service>.bindings.<rest>
        ;; e.g. com.google.cloud.bigquery -> gcp.bigquery.bindings
        ;; e.g. com.google.cloud.bigquery.storage.v1 -> gcp.bigquery.bindings.storage.v1
        ns-str (str "gcp." mapped-first ".bindings"
                    (when (next base-parts)
                      (str "." (string/join "." (rest base-parts)))))]
    (symbol ns-str)))

(defn split-fqcn [fqcn]
  (let [fqcn (str fqcn)
        parts (string/split fqcn #"\.")
        ;; Find index of first part that starts with Uppercase
        idx (first (keep-indexed (fn [i p] (when (re-find #"^[A-Z]" p) i)) parts))]
    (if idx
      {:package (string/join "." (take idx parts))
       :class (string/join "." (drop idx parts))}
      {:package (string/join "." (butlast parts))
       :class (last parts)})))

(defn infer-foreign-ns [fqcn]
  (let [{:keys [package]} (split-fqcn fqcn)]
    (symbol (str "gcp.foreign." package))))

(defn foreign-binding-exists? [ns-sym]
  (boolean (io/resource (str (string/replace (name ns-sym) #"\." "/") ".clj"))))

(defn foreign-vars [ns-sym]
  (let [path (str (string/replace (name ns-sym) #"\." "/") ".clj")]
    (if-let [res (io/resource path)]
      (let [source (slurp res)
            forms (edamame/parse-string-all source {:all true :auto-resolve {:current ns-sym}})]
        (into #{}
              (comp (filter seq?)
                    (filter #(contains? #{'defn 'def} (first %)))
                    (map second))
              forms))
      (throw (ex-info (str "Foreign source missing: " path) {:ns ns-sym})))))

(defn source-ns-meta [source]
  (let [form (edamame/parse-string source {:all true})]
    (when (and (seq? form) (= 'ns (first form)))
      (let [name-sym (second form)
            args (nnext form)
            maybe-map (first args)]
        (merge (meta name-sym)
               (when (map? maybe-map) maybe-map))))))

(defn update-ns-metadata [source metadata-key metadata-val]
  (let [zloc (z/of-string source)
        ;; find (ns ...)
        ns-loc (z/find-value zloc z/next 'ns)
        ;; name is next sibling
        name-loc (z/right ns-loc)
        ;; check if next sibling is a map
        next-loc (z/right name-loc)]
    (if (and next-loc (z/map? next-loc))
      ;; Update existing metadata map
      (let [existing-map (z/sexpr next-loc)
            updated-map-data (assoc existing-map metadata-key metadata-val)
            formatted-map-str (zp/zprint-str updated-map-data {:map {:comma? false}})
            formatted-map-node (p/parse-string formatted-map-str)
            updated-zip (z/replace next-loc formatted-map-node)]
        (z/root-string (if (z/linebreak? (z/left updated-zip))
                         updated-zip
                         (z/insert-left updated-zip (n/newline-node "\n")))))
      ;; Insert new metadata map after name
      (let [new-map-str (zp/zprint-str {metadata-key metadata-val} {:map {:comma? false}})
            new-map-node (p/parse-string new-map-str)
            updated-ns (-> name-loc
                           (z/insert-right new-map-node)
                           (z/insert-newline-right))]
        (z/root-string updated-ns)))))

(defn ns-meta [ns-sym]
  (let [path (str (string/replace (name ns-sym) #"\." "/") ".clj")]
    (when-let [res (io/resource path)]
      (source-ns-meta (slurp res)))))

(defn schema-key [package-name class-name]
  (let [ns-sym (package-to-ns package-name)]
    (keyword (name ns-sym) class-name)))

(declare class-parts)

(defn package-key [package t]
  (keyword "gcp" (string/join "." (into [(:packageName package)] (class-parts t)))))

(defn ->malli-type [package t]
  (assert (string? t))
  (case t
    ; "java.lang.Object"
    "void" :nil
    "java.lang.String" :string
    ("boolean" "java.lang.Boolean") :boolean
    ("int" "java.lang.Integer" "long" "java.lang.Long") :int
    ("java.util.List" "java.util.AbstractList" "java.lang.Iterable" "java.util.Set") [:sequential :any]
    "java.util.Map" [:map-of :any :any]
    "java.util.Iterator" [:sequential :any]
    "java.util.Optional" [:maybe :any]
    ("java.lang.Exception" "java.lang.Throwable" "java.lang.Error" "java.lang.AutoCloseable") :any
    ("Map<java.lang.String, java.lang.String>"
      "Map<java.lang.String,java.lang.String>"
      "Map<String,String>"
      "Map<String, String>"
      "java.util.Map<String, String>"
      "java.util.Map<String,String>"
      "java.util.Map<java.lang.String, java.lang.String>"
      "java.util.Map<java.lang.String,java.lang.String>") [:map-of :string :string]
    (cond

      (string/starts-with? t "java.util.List<")
      [:sequential (->malli-type package (string/trim (subs t 15 (dec (count t)))))]

      (string/starts-with? t "List<")
      [:sequential (->malli-type package (string/trim (subs t 5 (dec (count t)))))]

      (string/starts-with? t "com.google.common.collect.ImmutableList<")
      [:sequential (->malli-type package (string/trim (subs t 40 (dec (count t)))))]

      (contains? (:types/all package) t)
      (package-key package t)

      :else
      (do
        (println "\n\n WARN unknown malli type " t "\n\n")
        t))))

(defn dot-parts [class]
  (let [class (if (class? class)
                (str class)
                (if (or (symbol? class) (keyword? class))
                  (name class)
                  class))]
    (string/split class #"\.")))

(defn package-parts [className]
  (assert (string? className))
  (vec (take 4 (dot-parts className))))

(defn package-part [className]
  (string/join "." (package-parts className)))

(defn class-parts [className]
  {:pre [(string? className)]
   :post [(vector? %) (seq %) (every? string? %)]}
  (vec (nthrest (dot-parts className) 4)))

(defn class-part [className]
  (string/join "." (class-parts className)))

(defn base-part [className]
  ;;   1       2      3         4            5
  ;; com  google  cloud  $package  $base-class
  (string/join "." (take 5 (dot-parts className))))

(defn class-dollar-string [className]
  (string/join "$" (class-parts className)))

(defn dollar-parts [className]
  {:pre [(string? className) (string/includes? className "$")]
   :post [(vector? %) (seq %) (every? string? %)]}
  (let [[dollar :as cp] (class-parts className)]
    (assert (= 1 (count cp)))
    (string/split dollar #"\$")))

(defn as-dot-string [class-like]
  (if (class? class-like)
    (subs (str class-like) 6)
    (let [class-like (name class-like)]
      (if (string? class-like)
        (if (string/includes? class-like "$")
          (let [parts (dot-parts class-like)]
            (string/join "." (into (vec (butlast parts)) (string/split (peek parts) #"\$"))))
          class-like)
        (throw (Exception. (str "cannot make dot string from type " (type class-like))))))))

(defn as-dollar-string [class-like]
  (if (class? class-like)
    (as-dollar-string (as-dot-string class-like))
    (let [class-like    (name class-like)
          package-parts (package-parts class-like)
          class-parts   (class-parts class-like)]
      (string/join "." (conj package-parts (string/join "$" class-parts))))))

(defn _as-class [class-like]
  (if (class? class-like)
    class-like
    (try
      (let [class-like (name class-like)]
        (if (string/includes? class-like "$")
          (or
            (resolve (symbol class-like))
            (let [package (symbol (string/join "." (package-parts class-like)))
                  [dollar] (class-parts class-like)]
              (eval `(import ~[package (symbol dollar)]))))
          (or
            (resolve (symbol class-like))
            (let [package (symbol (string/join "." (package-parts class-like)))
                  dollar  (symbol (string/join "$" (class-parts class-like)))]
              (eval `(import ~[package (symbol dollar)]))))))
      (catch Exception e
        (throw (ex-info "error importing class" {:error e}))))))

(defn as-class [class-like]
  (let [clazz (_as-class class-like)]
    (if (class? clazz)
      clazz
      (throw (Exception. (str "failed to create class from class-like '" class-like "'"))))))

(defn builder-like? [class-like]
  (if (class? class-like)
    (builder-like? (str class-like))
    (string/ends-with? (name class-like) "Builder")))

(defonce reflect (memoize (fn [class-like] (clojure.reflect/reflect (as-class class-like)))))

(defn public-instantiators
  [class-like]
  (into []
        (filter
          (fn [{:keys [flags] :as m}]
            (and (contains? flags :public)
              (or
                (and
                  (instance? clojure.reflect.Method m)
                  (or (#{'of 'newBuilder 'toBuilder} (get m :name))
                      (= (symbol (as-dollar-string class-like)) (get m :return-type))))
                (instance? clojure.reflect.Constructor m)))))
        (sort-by :name (:members (reflect class-like)))))

(defn clean-method
  [{:keys [parameter-types exception-types return-type] :as m}]
  (cond-> (dissoc m :return-type :parameter-types)
          (empty? exception-types) (dissoc m :exception-types)
          (seq parameter-types) (assoc :parameters parameter-types)
          (some? return-type) (assoc :returnType return-type)))

(defn constructors [class-like]
  (into []
        (comp
          (filter #(instance? clojure.reflect.Constructor %))
          (filter #(contains? (:flags %) :public))
          (map clean-method))
        (sort-by :name (:members (reflect class-like)))))

(defn static-methods [class-like]
  (into []
        (comp
          (filter #(instance? clojure.reflect.Method %))
          (filter #(contains? (:flags %) :public))
          (filter #(contains? (:flags %) :static))
          (map clean-method)
          (map #(dissoc % :declaring-class :flags)))
        (sort-by :name (:members (reflect class-like)))))

(defn instance-methods [class-like]
  (into [] ;; includes abstract methods
        (comp
          (filter #(instance? clojure.reflect.Method %))
          (filter #(contains? (:flags %) :public))
          (remove #(contains? (:flags %) :static))
          (remove #(contains? #{'toString 'fromPb 'toPb 'equals 'hashCode 'builder 'toBuilder} (:name %)))
          (map clean-method))
        (sort-by :name (:members (reflect class-like)))))

(defn abstract-methods [class-like]
  (into []
        (comp
          (filter #(instance? clojure.reflect.Method %))
          (filter #(contains? (:flags %) :public))
          (filter #(contains? (:flags %) :abstract))
          (map clean-method))
        (sort-by :name (:members (reflect class-like)))))

(defn concrete-methods [class-like]
  (into []
        (comp
          (filter #(instance? clojure.reflect.Method %))
          (filter #(contains? (:flags %) :public))
          (remove #(contains? (:flags %) :abstract))
          (remove #(contains? #{'toString 'fromPb 'toPb 'equals 'hashCode 'builder 'toBuilder} (:name %)))
          (map clean-method))
        (sort-by :name (:members (reflect class-like)))))

(defn builder-methods [class-like]
  (let [builder-sym (if (builder-like? class-like)
                      (symbol (as-dollar-string class-like))
                      (symbol (as-dollar-string (string/join "." (conj (dot-parts class-like) "Builder")))))]
    (into []
          (comp
            (filter #(instance? clojure.reflect.Method %))
            (filter #(contains? (:flags %) :public))
            (filter #(= builder-sym (:return-type %)))
            (remove #(contains? #{'toString 'fromPb 'toPb 'equals 'hashCode 'builder 'toBuilder} (:name %)))
            (map clean-method))
          (sort-by :name (:members (reflect class-like))))))

(defn static-factory-methods [class-like]
  (let [self-sym (symbol (as-dollar-string class-like))]
    (into []
          (comp
            (filter #(instance? clojure.reflect.Method %))
            (filter #(contains? (:flags %) :public))
            (filter #(= self-sym (:return-type %)))
            (map clean-method))
          (sort-by :name (:members (reflect class-like))))))

(defn abstract-class? [class-like]
  (contains? (:flags (reflect class-like)) :abstract))

(defn enum-values [class-like]
  (let [meth (.getMethod (as-class class-like) "values" (into-array Class []))]
    (->> (.invoke meth nil (into-array Object []))
         (map #(.name %))
         sort
         vec)))

(defn variant? [union-class-like variant-class-like]
  (and (contains? (into #{} (map :name) (:members (reflect union-class-like))) 'getType)
       (contains? (:bases (reflect variant-class-like)) (symbol union-class-like))))

;; signatures only appear for compound types & for classes with actual classfiles
;; ie no nested types/builders
;; ... so the only useful information we can get from asm analysis is type parameters
;; for compound types in top-level accessors. this means for those types, only need LLM to extract docs
(defn- parse-signature [sig] sig)

(defn instance-methods' [classlike]
  (let [by-name (into {}
                      (map
                        (fn [m]
                          (let [m' (cond-> {:desc (get m :desc)}
                                           (get m :signature) (assoc :signature (get m :signature)))]
                            [(:name m) m'])))
                      (all-method-info (as-class classlike)))]
    (into (sorted-map)
          (map
            (fn [m]
              (let [key (name (:name m))
                    m' (merge (dissoc m :flags :declaring-class)
                              (get by-name key))]
                [key m'])))
          (instance-methods classlike))))

(comment

  (instance-methods' "com.google.cloud.bigquery.QueryJobConfiguration")
  (instance-methods' "com.google.cloud.bigquery.ExternalTableDefinition")

  (def sigs
    (frequencies
      (reduce into []
              (into #{}
                    (comp
                      (remove #(contains? (:types/nested bigquery) %))
                      (map instance-methods')
                      (map vals)
                      (map #(map :signature %))
                      (map #(filter some? %)))
                    (clojure.set/union (:types/static-factories bigquery)
                                       (:types/accessors bigquery)))))))
