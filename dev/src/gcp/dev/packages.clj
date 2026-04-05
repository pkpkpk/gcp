(ns gcp.dev.packages
  (:require
   [babashka.fs :as fs]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as string]
   [edamame.core :as eda]
   [gcp.dev.packages.definitions :as defs]
   [gcp.dev.packages.layout :as layout]
   [gcp.dev.packages.maven :as mvn]
   [gcp.dev.packages.package :as pkg]
   [gcp.dev.packages.sync :as sync]
   [gcp.dev.toolchain.analyzer :as analyzer]
   [gcp.dev.toolchain.parser :as parser]
   [gcp.dev.toolchain.shared :as shared]
   [gcp.dev.util :as u]
   [malli.core :as m])
  (:import
   (java.io File)))

(def global defs/global)
(def foreign defs/foreign)
(defn packages [] defs/pkg-key->package)

(def vertexai defs/vertexai)
(def bigquery defs/bigquery)
(def pubsub defs/pubsub)
(def storage defs/storage)
(def storage-control defs/storage-control)
(def logging defs/logging)
(def genai defs/genai)
(def monitoring defs/monitoring)
(def artifact-registry defs/artifact-registry)

#!----------------------------------------------------------------------------------------------------------------------

(defn as-pkg [pkg-like]
  (if (keyword? pkg-like)
    (or (get (packages) pkg-like)
        (throw (Exception. (str "unknown package key " pkg-like))))
    pkg-like))

(defn update-package-deps [pkg-like]
  (sync/update-package-deps (as-pkg pkg-like)))

(defn update-all-deps
  "Updates deps.edn for all registered packages."
  []
  (doseq [pkg-key (keys (packages))]
    (update-package-deps pkg-key)))

(defn delete-bindings
  "Deletes the entire contents of the package's bindings-target-root directory."
  [pkg-like]
  (let [pkg (as-pkg pkg-like)
        root (io/file (layout/package-bindings-root pkg) "gcp")]
    (when (fs/exists? root)
      (println "Deleting bindings root:" (str root))
      (fs/delete-tree root))))

(defn status [pkg-like]
  (sync/status (as-pkg pkg-like)))

(defn fetch-all-upstream [pkg-like]
  (sync/fetch-all-upstream (as-pkg pkg-like)))

(defn needs-sync? [pkg-like]
  (sync/needs-sync? (as-pkg pkg-like)))

(defn sync-to-release [pkg-like]
  (sync/sync-to-release (as-pkg pkg-like)))

(defn delta
  ([pkg-like] (sync/delta (as-pkg pkg-like)))
  ([pkg-like new-rev] (sync/delta (as-pkg pkg-like) new-rev))
  ([pkg-like old-rev new-rev] (sync/delta (as-pkg pkg-like) old-rev new-rev)))

(defn summary
  ([pkg-like] (sync/summary (as-pkg pkg-like)))
  ([pkg-like new-rev] (sync/summary (as-pkg pkg-like) new-rev))
  ([pkg-like old-rev new-rev] (sync/summary (as-pkg pkg-like) old-rev new-rev)))

(defn superficial-file?
  ([path] (sync/superficial-file? path))
  ([pkg-like path] (sync/superficial-file? (as-pkg pkg-like) path)))

(defn needs-interpretation? [pkg-like delta-map]
  (sync/needs-interpretation? (as-pkg pkg-like) delta-map))

(defn patch
  "Returns a unified diff patch string for the non-superficial files in the summary-map.
   Wraps sync/patch."
  [pkg-like summary-map]
  (sync/patch (as-pkg pkg-like) summary-map))

#!----------------------------------------------------------------------------------------------------------------------

(defn clear-cache [] (parser/clear-cache))

(defn package-root [pkg-like]
  (:package-root (as-pkg pkg-like)))

(defn manifest
  "Reads the manifest.edn file for the given package.
   Returns the manifest map or nil if not found."
  [pkg-like]
  (let [root (:state-root (as-pkg pkg-like))
        manifest-file (io/file root "manifest.edn")]
    (when (.exists manifest-file)
      (edn/read-string (slurp manifest-file)))))

(defn parse
  "Analyzes a package specified by keyword (e.g. :bigquery), static definition map, or path string.
   Returns the parsed package AST with :type :parsed."
  [pkg-like]
  (cond
    (keyword? pkg-like)
    (if-let [pkg-def (get (packages) pkg-like)]
      (parse pkg-def)
      (throw (ex-info "Unknown package keyword" {:package pkg-like :available (keys (packages))})))
    (map? pkg-like)
    (pkg/parse pkg-like)
    :else
    (throw (ex-info "Invalid argument to parse" {:arg pkg-like}))))

(defn lookup-pkg-key [class-like]
  (let [fqcn (if (map? class-like)
               (:fqcn class-like)
               class-like)]
    (loop [pkgs (packages)]
      (when-let [[pkg-key {:keys [package-prefixes]}] (first pkgs)]
        (if (some #(string/starts-with? (str fqcn) %) package-prefixes)
          pkg-key
          (recur (rest pkgs)))))))

(defn lookup-class
  ([class-like]
   (if (symbol? class-like)
     (lookup-class (str class-like))
     (when-let [pkg-key (lookup-pkg-key class-like)]
       (lookup-class pkg-key class-like))))
  ([pkg-like class-like]
   (let [pkg (parse pkg-like)]
     (when-let [node (pkg/lookup-class pkg class-like)]
       (into (sorted-map) node)))))

(defn target-file
  ([class-like]
   (if (symbol? class-like)
     (target-file (str class-like))
     (when-let [pkg-key (lookup-pkg-key class-like)]
       (target-file pkg-key class-like))))
  ([pkg-like class-like]
   (let [pkg (parse pkg-like)]
     (when-let [node (pkg/lookup-class pkg class-like)]
       (pkg/fqcn->target-file pkg (:fqcn node))))))

(defn target-file->fqcn
  ([file]
   (let [path (if (instance? File file)
                (.getPath file)
                (str file))
         pkg-key (cond
                   (and (string/includes? path "bigquery")
                        (not (string/includes? path "services"))) :bigquery
                   (and (string/includes? path "bigquery")
                        (string/includes? path "services")) :bigquery-services
                   (and (string/includes? path "storage")
                        (not (string/includes? path "services"))) :storage
                   (and (string/includes? path "storage")
                        (string/includes? path "services")) :storage-services
                   (string/includes? path "vertexai") :vertexai
                   (string/includes? path "pubsub") :pubsub
                   true
                   (throw (Exception. "TODO")))]
     (target-file->fqcn pkg-key file)))
  ([pkg-like file]
   (let [pkg (parse pkg-like)]
     (pkg/target-file->fqcn pkg file))))

(defn fqcn->target-file [fqcn]
  (let [pkg-key (lookup-pkg-key fqcn)
        pkg (parse pkg-key)]
    (pkg/fqcn->target-file pkg fqcn)))

(defn package-api-types
  "returns sorted list of all binding targets for the given package"
  [pkg-like]
  (let [{:keys [custom-namespace-mappings exempt-types] :as pkg} (parse pkg-like)
        pred (into (or exempt-types #{}) (map #(str %)) (keys custom-namespace-mappings))]
    (sort (remove (fn [fqcn]
                    (or (pred fqcn)
                        (:private? (get-in pkg [:class/by-fqcn fqcn]))))
                  (keys (:class/by-fqcn pkg))))))

(defn class-deps
  "Analyzes the dependencies of a node and returns a map separating them into
   :internal (same package/service) and :foreign (external, e.g. java.*, protobuf)."
  ([class-like]
   (class-deps (lookup-pkg-key class-like) class-like false))
  ([pkg-like class-like]
   (class-deps pkg-like class-like false))
  ([pkg-like class-like recursive?]
   (let [pkg  (parse pkg-like)
         support-pkgs (mapv #(parse %) (:support-packages pkg))]
     (pkg/class-deps pkg class-like (get foreign :mappings) support-pkgs recursive?))))

(defn _analyze-class
  ([class-like]
   (_analyze-class (lookup-pkg-key class-like) class-like))
  ([pkg-like class-like]
   (assert (some? pkg-like))
   (if-let [{:keys [custom-namespace-mappings exempt-types] :as pkg} (parse pkg-like)]
     (if-let [{:keys [fqcn] :as class-node} (lookup-class pkg class-like)]
       (if (contains? custom-namespace-mappings (symbol fqcn))
         (throw (Exception. (str "Class " fqcn " is listed as custom override and is exempt from analysis")))
         (if (contains? exempt-types fqcn)
           (throw (Exception. (str "Class " fqcn " is listed as exempt-type and is exempt from analysis")))
           (let [deps (class-deps pkg class-node)]
             (analyzer/analyze-class-node (assoc class-node :deps deps)))))
       (throw (ex-info (str "failed to find node for class-like '" class-like "'")
                       {:pkg-like pkg-like :class-like class-like})))
     (throw (Exception. (str "failed to parse package " pkg-like))))))

(def analyze-class
  (let [*cache (atom {})
        *ana-timestamp (atom analyzer/LOADED)]
    (fn analyze
      ([class-like]
       (analyze (lookup-pkg-key class-like) class-like))
      ([pkg-like class-like]
       (if (= @*ana-timestamp analyzer/LOADED)
         (do
           ;; read-aside
           (if-let [node (get @*cache class-like)]
             node
             (let [node (_analyze-class pkg-like class-like)]
               (swap! *cache assoc class-like node)
               node)))
         (do
           (reset! *cache {})
           (reset! *ana-timestamp analyzer/LOADED)
           (let [node (_analyze-class pkg-like class-like)]
             (swap! *cache assoc class-like node)
             node)))))))

(defn api-types-by-category [pkg-like]
  (let [pkg (parse pkg-like)]
    (reduce
      (fn [acc fqcn]
        (if (u/nested-fqcn? fqcn)
          acc
          (let [{:keys [category]} (lookup-class pkg fqcn)]
            (if (contains? acc category)
              (update-in acc [category] conj fqcn)
              (assoc acc category (sorted-set fqcn))))))
      (sorted-map)
      (package-api-types pkg))))

(defn global-api-types-by-category []
  (reduce
    (partial merge-with into)
    (map api-types-by-category (keys (packages)))))

(defn require-graph-many [fqcns]
  (let [visited (atom {})
        foreign (atom (sorted-set))
        pkg-cache (atom {})]
    (letfn [(get-parsed-pkg [pkg-key]
              (if-let [pkg (get @pkg-cache pkg-key)]
                pkg
                (let [pkg (parse pkg-key)
                      ns->fqcn (pkg/package-ns->fqcn pkg)
                      pkg (assoc pkg :ns->fqcn ns->fqcn)]
                  (swap! pkg-cache assoc pkg-key pkg)
                  pkg)))
            (visit [curr]
              (let [curr-fqcn (str curr)
                    pkg-key (lookup-pkg-key curr-fqcn)]
                (when-not (contains? @visited curr-fqcn)
                  (if-not (contains? (packages) pkg-key)
                    (do (swap! visited assoc curr-fqcn #{})
                        (swap! foreign conj curr-fqcn))
                    (let [pkg (get-parsed-pkg pkg-key)
                          custom-ns (get (:custom-namespace-mappings pkg) (symbol curr-fqcn))
                          exempt? (contains? (:exempt-types pkg) curr-fqcn)
                          requires (cond
                                     exempt? #{}
                                     custom-ns
                                     ;; For custom handwritten bindings, we STRICTLY rely on the parsed Clojure :require graph.
                                     ;; Including Java-level class-deps here would re-introduce the circular dependencies
                                     ;; (e.g., Field <-> FieldList) that the handwritten files are specifically designed to sever.
                                     (let [rel-path (str (string/replace (name custom-ns) "." "/") ".clj")
                                           ;; use io/resource to seamlessly find the file whether it's in src/ or src/custom/
                                           res (io/resource rel-path)
                                           clj-deps (if res
                                                      (let [content (slurp res)
                                                            ns-form (eda/parse-string content)
                                                            reqs (when (and (seq? ns-form) (= 'ns (first ns-form)))
                                                                   (->> ns-form
                                                                        (drop 2)
                                                                        (filter #(and (seq? %) (= :require (first %))))
                                                                        first
                                                                        rest))
                                                            ns-syms (map #(if (coll? %) (first %) %) reqs)
                                                            ;; Check all packages (including support packages like bigquery-services)
                                                            ;; so we can correctly resolve transitive targets (like QueryParameter).
                                                            all-pkgs (map get-parsed-pkg (keys (packages)))]
                                                        (->> ns-syms
                                                             (keep (fn [ns-sym]
                                                                     (some (fn [p] (get (:ns->fqcn p) ns-sym))
                                                                           all-pkgs)))
                                                             (into (sorted-set))))
                                                      #{})]
                                       clj-deps)
                                     :else
                                     (let [node (analyze-class curr-fqcn)]
                                       (doseq [[dep-fqcn type] (:require-types node)]
                                         (when (= :foreign type)
                                           (swap! foreign conj (str dep-fqcn))))
                                       (into (sorted-set) (map str) (keys (:require-types node)))))]
                      (swap! visited assoc curr-fqcn requires)
                      (doseq [dep requires]
                        (visit dep)))))))]
      (doseq [fqcn fqcns]
        (visit fqcn))
      {:graph @visited
       :foreign @foreign})))

(defn require-graph [fqcn]
  (:graph (require-graph-many [fqcn])))

(defn topological-order-many
  [fqcns]
  (let [fqcns (m/coerce [:seqable [:or :string symbol?]] fqcns)
        {:keys [graph]} (require-graph-many fqcns)
        visited (atom #{})
        visiting (atom #{})
        stack (atom [])]
    (letfn [(visit [node]
              (if (contains? @visiting node)
                (throw (ex-info "Circular dependency detected" {:node node :visiting @visiting}))
                (when-not (contains? @visited node)
                  (swap! visiting conj node)
                  (doseq [dep (get graph node)]
                    (visit dep))
                  (swap! visiting disj node)
                  (swap! visited conj node)
                  (swap! stack conj node))))]
      (doseq [node (keys graph)]
        (visit node))
      (->> @stack
           (map #(if (u/nested-fqcn? %)
                   (:parent-fqcn (u/split-fqcn %))
                   %))
           distinct
           (filterv (fn [node]
                      (let [pkg-key (lookup-pkg-key node)]
                        (if (contains? (packages) pkg-key)
                          (let [pkg (parse pkg-key)]
                            (not (or (contains? (:custom-namespace-mappings pkg) (symbol node))
                                     (contains? (:exempt-types pkg) node))))
                          false))))))))

(defn topological-order [fqcn]
  (topological-order-many [fqcn]))
