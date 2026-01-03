(ns gcp.dev.packages.package
  (:require
   [clojure.set :as s]
   [clojure.string :as string]
   [malli.core :as m]))

(def pkg-node-schema :map)
(def class-node-schema :map)

(defn named? [class-like] (or (string? class-like) (ident? class-like)))

(defn fqcn? [{:keys [package-name] :as pkg} class-like]
  (and (named? class-like)
       (string/starts-with? (name class-like) package-name)))

(defn lookup-class [pkg class-like]
  (assert (m/validate pkg-node-schema pkg))
  (if (named? class-like)
    (if (fqcn? pkg class-like)
      (get-in pkg [:class/by-fqcn (name class-like)])
      (when-let [fqcn (get-in pkg [:class/name->fqcn (name class-like)])]
        (get-in pkg [:class/by-fqcn fqcn])))
    (throw (Exception. "bad class-like"))))

(def default-ignored-prefixes
  #{"com.google.api.services."})

(defn- ignored-type? [type-sym]
  (let [t-str (str type-sym)]
    (some #(string/starts-with? t-str %) default-ignored-prefixes)))

(defn- extract-type-symbols [type-ast]
  (cond
    (symbol? type-ast) (if (or (#{'? 'void} type-ast) (ignored-type? type-ast)) (sorted-set) (conj (sorted-set) type-ast))
    (sequential? type-ast) (reduce into (sorted-set) (map extract-type-symbols type-ast))
    :else (sorted-set)))

(defn class-user-types
  ([pkg class-like]
   (if-some [node (lookup-class pkg class-like)]
     (class-user-types node)
     (throw (ex-info "class not found" {:pkg pkg :class-like class-like}))))
  ([class-node]
   (let [members      (concat (:constructors class-node) (:methods class-node))
         nested       (:nested class-node)
         param-types  (mapcat :parameters members)
         return-types (keep :returnType members)
         all-types    (concat (map :type param-types) return-types)]
     (into (reduce into (sorted-set) (map extract-type-symbols all-types))
           (mapcat class-user-types nested)))))

(defn user-types [{:keys [class/by-fqcn] :as _pkg}]
  (transduce (map class-user-types) (completing into) (sorted-set) (vals by-fqcn)))

(defn- native-type? [pkg type-sym]
  (let [t-str (str type-sym)
        pkg-name (:package-name pkg)
        _ (assert (some? pkg-name))
        native-prefixes (or (:native-prefixes pkg) #{pkg-name})]
    (some #(string/starts-with? t-str %) native-prefixes)))

(defn class-foreign-user-types [pkg class-like]
  (let [class-node (if (map? class-like)
                     class-like
                     (lookup-class pkg class-like))]
    (if class-node
      (let [all-types (class-user-types class-node)]
        (into (sorted-set)
              (remove (fn [t] (native-type? pkg t)))
              all-types))
      (throw (ex-info "class not found" {:pkg pkg :class-like class-like})))))

(defn class-package-user-types [pkg class-like]
  (let [class-node (if (map? class-like)
                     class-like
                     (lookup-class pkg class-like))]
    (if class-node
      (let [all-types (class-user-types class-node)]
        (into (sorted-set)
              (filter (fn [t] (native-type? pkg t)))
              all-types))
      (throw (ex-info "class not found" {:pkg pkg :class-like class-like})))))

(defn package-user-types [pkg]
  (transduce (map #(class-package-user-types pkg %)) (completing into) (sorted-set) (vals (:class/by-fqcn pkg))))

(defn foreign-user-types [pkg]
  (transduce (map #(class-foreign-user-types pkg %)) (completing into) (sorted-set) (vals (:class/by-fqcn pkg))))

(defn foreign-user-types-by-package
  "Returns a map of package name to a set of foreign types used in the package.
   Primitives are grouped under the :primitive key."
  [pkg]
  (let [foreign-types (foreign-user-types pkg)]
    (reduce (fn [acc type-sym]
              (let [t-str (str type-sym)
                    last-dot (.lastIndexOf t-str ".")
                    pkg-key (if (neg? last-dot)
                              :primitive
                              (subs t-str 0 last-dot))]
                (update acc pkg-key (fnil conj (sorted-set)) type-sym)))
            (sorted-map-by (fn [a b] (compare (str a) (str b))))
            foreign-types)))

;; -------------------------------------------------------------------------
;; Dependency Graph Logic
;; -------------------------------------------------------------------------

(defn- collect-types
  "Walks an arbitrary data structure (AST type representation) and collects all Symbols.
   Excludes common java.lang types if desired, but here we collect everything that looks like a class."
  [x]
  (let [types (atom (sorted-set))]
    (clojure.walk/postwalk
      (fn [form]
        (when (symbol? form)
          (let [s (name form)]
            ;; Heuristic: starts with uppercase or contains dot, likely a class
            (when (or (string/includes? s ".")
                      (re-matches #"^[A-Z].*" s))
              (swap! types conj (name form)))))
        form)
      x)
    @types))

(defn- extract-deps-from-node
  "Extracts all dependencies (as FQCN strings) from a class AST node."
  [node]
  (let [;; 1. Extends
        extends-deps (mapcat collect-types (:extends node))
        ;; 2. Implements
        implements-deps (mapcat collect-types (:implements node))
        ;; 3. Fields (types)
        field-deps (mapcat #(collect-types (:type %)) (:fields node))
        ;; 4. Methods (return types + parameter types)
        method-deps (mapcat (fn [m]
                              (concat (collect-types (:returnType m))
                                      (mapcat #(collect-types (:type %)) (:parameters m))))
                            (:methods node))
        ;; 5. Constructors
        ctor-deps (mapcat (fn [c]
                            (mapcat #(collect-types (:type %)) (:parameters c)))
                          (:constructors node))]
    ;; 6. Nested classes (recurse? No, dependency graph usually treats top-level)
    ;;    But we might want to depend on inner classes.
    (into (sorted-set) (concat extends-deps implements-deps field-deps method-deps ctor-deps))))

(defn build-dependency-graph
  "Builds a dependency graph from the package analysis result.
   Returns a map: {FQCN #{DependencyFQCN ...}}"
  [package-ast]
  (let [fqcn-map (:class/by-fqcn package-ast)
        internal-classes (set (keys fqcn-map))]

    (reduce (fn [graph [fqcn node]]
              (let [raw-deps (extract-deps-from-node node)
                    ;; Filter dependencies to only those within the analyzed package (optional,
                    ;; usually we want to see internal structure)
                    ;; For now, let's keep only edges that point to classes WE know about (internal).
                    internal-deps (s/intersection (set raw-deps) internal-classes)]
                (assoc graph fqcn internal-deps)))
            {}
            fqcn-map)))

(defn dependency-tree
  "Generates a dependency tree (nested map) starting from a root class.
   'package-path' is the file path to the package source (for analysis).
   'root-class-name' is the simple name or FQCN of the root class (e.g. 'Storage').

   Returns a map where keys are class names and values are sub-dependencies.
   Uses a 'seen' set to break cycles."
  [pkg-ast root-class-name]
  (let [graph   (build-dependency-graph pkg-ast)
        ;; Find FQCN for root-class-name if it's simple
        root-fqcn (or (when (contains? graph root-class-name) root-class-name)
                      (first (filter #(string/ends-with? % (str "." root-class-name)) (keys graph))))]

    (when-not root-fqcn
      (throw (ex-info (str "Root class not found: " root-class-name) {:available (take 10 (keys graph))})))

    (letfn [(build-tree [node seen]
              (if (contains? seen node)
                {node :cycle}
                (let [deps (get graph node)
                      new-seen (conj seen node)]
                  {node (into {} (map #(build-tree % new-seen) deps))})))]

      (build-tree root-fqcn #{}))))

(defn dependency-seq
  "Returns a sequence of FQCNs representing the dependency closure of `class-like`
   in depth-first order. Restricted to package-local (native) user types.
   Handles cycles by tracking visited nodes."
  [pkg class-like]
  (let [root-node (if (map? class-like)
                    class-like
                    (lookup-class pkg class-like))]
    (if root-node
      (let [fqcn (str (:package root-node) "." (:name root-node))]
        (loop [stack   (list fqcn)
               visited #{}
               result  []]
          (if (empty? stack)
            result
            (let [current (peek stack)
                  stack   (pop stack)]
              (if (contains? visited current)
                (recur stack visited result)
                (let [deps (try
                             (class-package-user-types pkg current)
                             (catch Exception _ #{}))
                      ;; Sort deps to ensure deterministic order
                      sorted-deps (sort (map str deps))
                      ;; Add deps to stack (reverse to process in order)
                      new-stack (into stack (reverse sorted-deps))]
                  (recur new-stack (conj visited current) (conj result current))))))))
      (throw (ex-info "class not found" {:pkg pkg :class-like class-like})))))

(defn dependency-post-order
  "Returns a sequence of FQCNs representing the dependency closure of `class-like`
   in post-order (leaves first, root last).
   Useful for bottom-up compilation or instantiation."
  [pkg class-like]
  (let [tree (dependency-tree pkg class-like)]
    (letfn [(traverse [node seen]
              (let [[name children] (first node)]
                (if (or (= children :cycle) (contains? seen name))
                  [] ;; Skip cycles or already visited in this path
                  (let [child-seqs (map (fn [[k v]] (traverse {k v} (conj seen name))) children)]
                    (concat (apply concat child-seqs) [name])))))]
      (distinct (traverse tree #{})))))
