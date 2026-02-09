(ns gcp.dev.util
  (:require
   [clojure.java.io :as io]
   [clojure.reflect]
   [clojure.string :as string]
   [edamame.core :as edamame]
   [rewrite-clj.node :as n]
   [rewrite-clj.parser :as p]
   [rewrite-clj.zip :as z]
   [zprint.core :as zp]))

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

(defn clean-doc
  [doc]
  (when (and doc (not (string/blank? doc)))
    (-> doc
        string/trim
        (string/replace #"\\\S\\" "")
        (string/replace #"\\n" " ")
        (string/replace #"\n" " ")
        (string/replace #"\\\\" "\\")
        (string/replace #"\s+" " "))))

(defn excluded-type-name?
  [type-name]
  (let [type-name (str type-name)]
    (or (string/ends-with? type-name "OrBuilder")
        (string/ends-with? type-name "Callable")
        (string/ends-with? type-name "Serializer")
        (string/ends-with? type-name "UnusedPrivateParameter")
        (string/ends-with? type-name "Proto")
        (string/ends-with? type-name "AutoCloseable")
        (string/ends-with? type-name "Closeable")
        (string/ends-with? type-name "Serializable")
        (= type-name "BigQueryErrorMessages"))))

(defn extract-version
  [doc]
  (when doc
    (when-let [m (re-find #"google\.cloud\.[^.]+\.([^.]+)\.[^.]+" doc)]
      (let [v (second m)]
        (when (re-matches #"v\d+.*" v)
          v)))))

(defn camel-to-screaming-snake [s]
  (-> s
      (string/replace #"([a-z])([A-Z])" "$1_$2")
      string/upper-case))

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

(defn property-key [parameter-or-parameter-name]
  (if (map? parameter-or-parameter-name)
    (property-key (:name parameter-or-parameter-name))
    (do
      (assert (string? parameter-or-parameter-name))
      (keyword (property-name parameter-or-parameter-name)))))

(def ^:private ns-mapping
  {"artifactregistry" "artifact-registry"})

(defn package-to-ns
  ([package-name]
   (package-to-ns package-name false))
  ([package-name foreign?]
   (let [pkg-str      (str package-name)
         ;; Strip common prefixes: com.google.cloud, com.google.devtools, etc.
         base         (if (string/starts-with? pkg-str "com.google.")
                        (let [parts (string/split pkg-str #"\.")]
                          (if (> (count parts) 3)
                            (string/join "." (drop 3 parts))
                            pkg-str))
                        pkg-str)
         ;; Apply mappings to the first segment of the base
         base-parts   (string/split base #"\.")
         mapped-first (get ns-mapping (first base-parts) (first base-parts))
         ;; Construct strict hierarchy: gcp.bindings.<service>.<rest>
         ;; e.g. com.google.cloud.bigquery -> gcp.bindings.bigquery
         ;; e.g. com.google.cloud.bigquery.storage.v1 -> gcp.bindings.bigquery.storage.v1
         root (if foreign? "gcp.foreign." "gcp.bindings.")
         ns-str       (str root mapped-first
                           (when (next base-parts)
                             (str "." (string/join "." (rest base-parts)))))]
     (symbol ns-str))))

(defn binding-ns
  [package-name package-prefixes fqcn]
  (let [package-base (package-to-ns package-name)
        [prefix] (filter #(string/starts-with? fqcn %) (reverse (sort-by count package-prefixes)))
        _ (assert (some? prefix))
        postfix (subs (name fqcn) (count prefix))]
    (symbol (str (name package-base) postfix))))

(defn split-fqcn [fqcn]
  (let [fqcn (str fqcn)
        parts (string/split fqcn #"\.")
        ;; Find index of first part that starts with Uppercase
        idx (first (keep-indexed (fn [i p] (when (re-find #"^[A-Z]" p) i)) parts))]
    (if idx
      (let [package (string/join "." (take idx parts))
            nested (vec (rest (drop idx parts)))]
        (cond-> {:package     package
                 :class       (string/join "." (drop idx parts))}
                (seq nested) (assoc :nested nested
                                    :parent-fqcn (str package "." (first (drop idx parts))))))
      {:package (string/join "." (butlast parts))
       :class   (last parts)})))

(defn nested-fqcn?
  [fqcn]
  (contains? (split-fqcn fqcn) :nested))

(defn fqcn->schema-key
  ([fqcn]
   (fqcn->schema-key fqcn false))
  ([fqcn foreign?]
   (let [{:keys [package class]} (split-fqcn fqcn)]
     (keyword (name (package-to-ns package foreign?)) class))))

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

(defn ns-meta
  [ns-sym]
  (let [path (str (string/replace (name ns-sym) #"\." "/") ".clj")]
    (when-let [res (io/resource path)]
      (source-ns-meta (slurp res)))))

(declare class-parts)

(defn dot-parts
  [class]
  (let [class (if (class? class)
                (str class)
                (if (or (symbol? class) (keyword? class))
                  (name class)
                  class))]
    (string/split class #"\.")))

(defn package-parts
  [className]
  (assert (string? className))
  (vec (take 4 (dot-parts className))))

(defn package-part
  [className]
  (string/join "." (package-parts className)))

(defn class-parts
  [className]
  {:pre [(string? className)]
   :post [(vector? %) (seq %) (every? string? %)]}
  (vec (nthrest (dot-parts className) 4)))

(defn class-part
  [className]
  (string/join "." (class-parts className)))

(defn class-dollar-string
  [className]
  (string/join "$" (class-parts className)))

(defn dollar-parts
  [className]
  {:pre [(string? className) (string/includes? className "$")]
   :post [(vector? %) (seq %) (every? string? %)]}
  (let [[dollar :as cp] (class-parts className)]
    (assert (= 1 (count cp)))
    (string/split dollar #"\$")))

(defn as-dot-string
  [class-like]
  (if (class? class-like)
    (subs (str class-like) 6)
    (let [class-like (name class-like)]
      (if (string? class-like)
        (if (string/includes? class-like "$")
          (let [parts (dot-parts class-like)]
            (string/join "." (into (vec (butlast parts)) (string/split (peek parts) #"\$"))))
          class-like)
        (throw (Exception. (str "cannot make dot string from type " (type class-like))))))))

(defn as-dollar-string
  [class-like]
  (if (class? class-like)
    (as-dollar-string (as-dot-string class-like))
    (let [class-like    (name class-like)
          package-parts (package-parts class-like)
          class-parts   (class-parts class-like)]
      (string/join "." (conj package-parts (string/join "$" class-parts))))))

(defn _as-class
  [class-like]
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

(defn as-class
  [class-like]
  (let [clazz (_as-class class-like)]
    (if (class? clazz)
      clazz
      (throw (Exception. (str "failed to create class from class-like '" class-like "'"))))))

(defn fish->clj-array
  "Converts fish-style array notation (Type<>) to modern Clojure 1.12 array syntax (Type/depth).
   Handles multi-dimensional arrays (Type<><> -> Type/2).
   Returns a symbol."
  [sym-or-str]
  (let [s (str sym-or-str)]
    (loop [curr s depth 0]
      (if (string/ends-with? curr "<>")
        (recur (subs curr 0 (- (count curr) 2)) (inc depth))
        (if (pos? depth)
          (symbol (str curr "/" depth))
          (symbol curr))))))

(defn fish->ast-array
  "Converts fish-style array notation (Type<>) to the AST array representation ([:array Type]).
   Handles multi-dimensional arrays (Type<><> -> [:array [:array Type]]).
   Returns a symbol or vector."
  [sym-or-str]
  (let [s (str sym-or-str)]
    (if (string/ends-with? s "<>")
      (loop [curr s layers 0]
        (if (string/ends-with? curr "<>")
          (recur (subs curr 0 (- (count curr) 2)) (inc layers))
          (reduce (fn [acc _] [:array acc]) (symbol curr) (range layers))))
      (symbol s))))

(defn ast-array->clj-array
  "Converts AST-style array representation ([:array Type]) to modern Clojure 1.12 array syntax (Type/depth).
   Handles multi-dimensional arrays ([:array [:array Type]] -> Type/2).
   Returns a symbol."
  [ast-type]
  (if (and (vector? ast-type) (= :array (first ast-type)))
    (loop [curr ast-type depth 0]
      (if (and (vector? curr) (= :array (first curr)))
        (recur (second curr) (inc depth))
        (symbol (str curr "/" depth))))
    (symbol (str ast-type))))

(defn array-dimension
  "Returns the array dimension (>= 1) for a given type representation.
   Accepts:
   - Java Class: byte/1, String/2 (checks .isArray)
   - Fish style: Type<>, Type<><>
   - Clojure style: Type/1, Type/2
   - AST style: [:array Type], [:array [:array Type]]
   Returns 0 if not an array."
  [type-rep]
  (cond
    ;; Java Class
    (class? type-rep)
    (if (.isArray type-rep)
      (count (take-while #(= \[ %) (.getName type-rep)))
      0)

    ;; AST style: [:array ...]
    (and (vector? type-rep) (= :array (first type-rep)))
    (loop [curr type-rep depth 0]
      (if (and (vector? curr) (= :array (first curr)))
        (recur (second curr) (inc depth))
        depth))

    ;; Fish style: Type<> or Type<><> (string or symbol)
    (let [s (str type-rep)]
      (string/ends-with? s "<>"))
    (loop [curr (str type-rep) depth 0]
      (if (string/ends-with? curr "<>")
        (recur (subs curr 0 (- (count curr) 2)) (inc depth))
        depth))

    ;; Clojure style: Type/N
    :else
    (let [s (str type-rep)]
      (if-let [[_ n] (re-matches #".+/(\d+)$" s)]
        (try
          (Integer/parseInt n)
          (catch NumberFormatException _ 0))
        0))))

(defonce reflect (memoize (fn [class-like] (clojure.reflect/reflect (as-class class-like)))))
