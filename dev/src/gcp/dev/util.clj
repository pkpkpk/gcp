(ns gcp.dev.util
  (:require
   [clojure.java.io :as io]
   [clojure.reflect]
   [clojure.string :as string]
   [edamame.core :as edamame]
   [hasch.core :as hasch]
   [rewrite-clj.node :as n]
   [rewrite-clj.parser :as p]
   [rewrite-clj.zip :as z]
   [zprint.core :as zp]))

(set! *print-namespace-maps* false)

(defn hasch [arg] (str (hasch/uuid arg)))

(defn relative-path [base f]
  (let [base-path (.getAbsolutePath (io/file base))
        f-path    (.getAbsolutePath (io/file f))]
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
        (string/ends-with? type-name "BuilderImpl")
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

(defn camel-to-pascal [s]
  (if (empty? s)
    ""
    (str (string/upper-case (subs s 0 1)) (subs s 1))))

(defn screaming-snake->pascal [s]
  (->> (string/split s #"_")
       (map (fn [p] (str (string/upper-case (subs p 0 1)) (string/lower-case (subs p 1)))))
       (string/join "")))

(defn lowercase-first [s]
  (str (string/lower-case (subs s 0 1)) (subs s 1)))

(defn clean-name [p]
  (lowercase-first (string/replace p #"\_" "")))

(defn _property-name [_name]
  (cond
    (string/starts-with? _name "get")
    (let [s (subs _name 3)]
      (if (seq s)
        (str (string/lower-case (subs s 0 1)) (subs s 1))
        ""))
    (string/starts-with? _name "is")
    (let [s (subs _name 2)]
      (if (seq s)
        (str (string/lower-case (subs s 0 1)) (subs s 1))
        ""))
    (string/starts-with? _name "set")
    (let [s (subs _name 3)]
      (if (seq s)
        (str (string/lower-case (subs s 0 1)) (subs s 1))
        ""))
    (string/starts-with? _name "addAll")
    (let [s (subs _name 6)]
      (if (seq s)
        (str (string/lower-case (subs s 0 1)) (subs s 1))
        ""))
    :else _name))

(defn property-name [_name]
  (clean-name (_property-name _name)))

(defn property-key [parameter-or-parameter-name]
  (if (map? parameter-or-parameter-name)
    (property-key (:name parameter-or-parameter-name))
    (do
      (assert (string? parameter-or-parameter-name))
      (keyword (property-name parameter-or-parameter-name)))))

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

(def ^:private ns-mapping
  {"artifactregistry" "artifact-registry"})

;; -- foreign----
; "com.google.cloud.RetryOption"

(def drop-3-re
  (re-pattern (str (string/join "|" ["com.google.cloud.vertexai"
                                     "com.google.cloud.pubsub"
                                     "com.google.cloud.storage"
                                     "com.google.cloud.bigquery"]))))

(def drop-2-re
  (re-pattern (str (string/join "|" ["com.google.pubsub"
                                     "com.google.pubsub.v1"
                                     "com.google.storage.control"
                                     "com.google.api.services.bigquery"
                                     "com.google.api.services.storage"]))))

(defn- fqcn->gcp-prefix [fqcn]
  (let [{:keys [package]} (split-fqcn fqcn)
        pkg-parts (string/split package #"\.")
        gcp-parts (cond
                    (re-find drop-3-re package)
                    (vec (drop 3 pkg-parts))

                    (re-find drop-2-re package)
                    (vec (drop 2 pkg-parts))

                    :else
                    (into ["foreign"] pkg-parts))]
    (str "gcp." (string/join "." gcp-parts))))

(defn fqcn->gcp-key [fqcn]
  (let [{:keys [class]} (split-fqcn fqcn)]
    (keyword (fqcn->gcp-prefix fqcn) class)))

(defn fqcn->gcp-ns [fqcn]
  (let [{:keys [class]} (split-fqcn fqcn)]
    (symbol (str (fqcn->gcp-prefix fqcn) "." class))))

#!----------------------------------------------------------------------------------------------------------------------

(defn infer-foreign-ns
  [fqcn]
  (let [{:keys [package]} (split-fqcn fqcn)]
    (symbol (str "gcp.foreign." package))))

(defn foreign-binding-exists?
  [ns-sym]
  (boolean (io/resource (str (string/replace (name ns-sym) #"\." "/") ".clj"))))

(defn foreign-vars
  [ns-sym]
  (let [path (str (string/replace (name ns-sym) #"\." "/") ".clj")]
    (if-let [res (io/resource path)]
      (let [source (slurp res)
            forms (edamame/parse-string-all source {:all true :regex true :auto-resolve {:current ns-sym}})]
        (into #{}
              (comp (filter seq?)
                    (filter #(contains? #{'defn 'def} (first %)))
                    (map second))
              forms))
      (throw (ex-info (str "Foreign source missing: " path) {:ns ns-sym})))))

(defn source-ns-meta [source]
  (let [form (edamame/parse-string source {:all true :regex true})]
    (when (and (seq? form) (= 'ns (first form)))
      (let [name-sym (second form)
            args (nnext form)
            maybe-map (first args)]
        (merge (meta name-sym)
               (when (map? maybe-map) maybe-map))))))

(defn- indent-str [s indent]
  (let [lines (string/split-lines s)
        spaces (apply str (repeat indent \space))]
    (string/join "\n" (cons (first lines) (map #(str spaces %) (rest lines))))))

(defn update-ns-metadata [source metadata-key metadata-val]
  (let [zloc (z/of-string source {:track-position? true})
        ;; find (ns ...)
        ns-loc (z/find-value zloc z/next 'ns)
        ;; name is next sibling
        name-loc (z/right ns-loc)
        ;; check if next sibling is a map
        next-loc (z/right name-loc)]
    (if (and next-loc (z/map? next-loc))
      ;; Update existing metadata map
      (let [col (dec (second (z/position next-loc)))
            existing-map (z/sexpr next-loc)
            updated-map-data (into (sorted-map) (assoc existing-map metadata-key metadata-val))
            formatted-map-str (zp/zprint-str updated-map-data {:map {:comma? false}})
            indented-map-str (indent-str formatted-map-str col)
            formatted-map-node (p/parse-string indented-map-str)]
        (z/root-string (z/replace next-loc formatted-map-node)))
      ;; Insert new metadata map after name
      (let [indent 2
            new-map-str (zp/zprint-str (sorted-map metadata-key metadata-val) {:map {:comma? false}})
            indented-map-str (indent-str new-map-str indent)
            new-map-node (p/parse-string indented-map-str)]
        (z/root-string
          (-> name-loc
              (z/insert-right new-map-node)
              (z/insert-right (n/whitespace-node "\n  "))))))))

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

(defn fqcn->java-name [fqcn]
  (let [{:keys [package class]} (split-fqcn (name fqcn))]
    (str package "." (string/replace class "." "$"))))

(defn enum? [fqcn]
  (try
    (or (.isEnum (Class/forName (fqcn->java-name fqcn)))
        (= "class com.google.cloud.StringEnumValue"
           (str (.getSuperclass (Class/forName (fqcn->java-name fqcn))))))
    (catch Exception _ false)))

(defn enum-values [fqcn]
  (when-not (enum? fqcn)
    (throw (Exception. (str fqcn " is not an enum"))))
  (let [c      (Class/forName (fqcn->java-name fqcn))
        values (if (.isEnum c)
                 (->> (.getEnumConstants c)
                      (map #(.name %))
                      (remove #{"UNRECOGNIZED"}))
                 (let [values-method (.getMethod c "values" (into-array Class []))
                       values        (.invoke values-method nil (into-array Object []))]
                   (map #(.name %) (seq values))))]
    (if (seq values)
      values
      (->> (.getFields c)
           (filter (fn [f]
                     (let [mods (.getModifiers f)]
                       (and (java.lang.reflect.Modifier/isPublic mods)
                            (java.lang.reflect.Modifier/isStatic mods)
                            (= (.getType f) c)))))
           (map (fn [f] (.name (.get f nil))))
           (distinct)))))
