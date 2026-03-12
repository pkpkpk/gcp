(ns gcp.dev.packages
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as string]
   [gcp.dev.packages.definitions :as defs]
   [gcp.dev.packages.maven :as mvn]
   [gcp.dev.packages.package :as pkg]
   [gcp.dev.packages.sync :as sync]
   [gcp.dev.toolchain.analyzer :as analyzer]
   [gcp.dev.toolchain.parser :as parser]
   [gcp.dev.toolchain.shared :as shared]
   [gcp.dev.util :as u])
  (:import
   (java.io File)))

(def global defs/global)
(def foreign defs/foreign)
(def packages defs/packages)

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
    (or (get packages pkg-like)
        (throw (Exception. (str "unknown package key " pkg-like))))
    pkg-like))

(defn update-package-deps [pkg-like]
  (sync/update-package-deps (as-pkg pkg-like)))

(defn update-all-deps
  "Updates deps.edn for all registered packages."
  []
  (doseq [pkg-key (keys packages)]
    (update-package-deps pkg-key)))

(defn status [pkg-like]
  (sync/status (as-pkg pkg-like)))

(defn needs-sync? [pkg-like]
  (sync/needs-sync? (as-pkg pkg-like)))

(defn sync-to-release [pkg-like]
  (sync/sync-to-release (as-pkg pkg-like)))

#!----------------------------------------------------------------------------------------------------------------------

(defn clear-cache [] (parser/clear-cache))

(defn package-root [pkg-like]
  (:package-root (as-pkg pkg-like)))

(defn manifest
  "Reads the manifest.edn file for the given package.
   Returns the manifest map or nil if not found."
  [pkg-like]
  (let [root (package-root pkg-like)
        manifest-file (io/file root "manifest.edn")]
    (when (.exists manifest-file)
      (edn/read-string (slurp manifest-file)))))

(defn parse
  "Analyzes a package specified by keyword (e.g. :bigquery), static definition map, or path string.
   Returns the parsed package AST with :type :parsed."
  [pkg-like]
  (cond
    (keyword? pkg-like)
    (if-let [pkg-def (get packages pkg-like)]
      (parse pkg-def)
      (throw (ex-info "Unknown package keyword" {:package pkg-like :available (keys packages)})))
    (map? pkg-like)
    (pkg/parse pkg-like)
    :else
    (throw (ex-info "Invalid argument to parse" {:arg pkg-like}))))

(defn lookup-pkg-key [class-like]
  (let [fqcn (if (map? class-like)
               (:fqcn class-like)
               class-like)]
    (loop [packages packages]
      (when-let [[pkg-key {:keys [package-prefixes]}] (first packages)]
        (if (some #(string/starts-with? (str fqcn) %) package-prefixes)
          pkg-key
          (recur (rest packages)))))))

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
                   (string/includes? path "vertexai") :vertexai
                   (string/includes? path "storage") :storage
                   true
                   (throw (Exception. "TODO")))]
     (target-file->fqcn pkg-key file)))
  ([pkg-like file]
   (let [pkg (parse pkg-like)]
     (pkg/target-file->fqcn pkg file))))

(defn package-api-types
  "returns sorted list of all binding targets for the given package"
  [pkg-like]
  (let [{:keys [custom-namespace-mappings exempt-types] :as pkg} (parse pkg-like)
        pred (into (or exempt-types #{}) (map #(str %)) (keys custom-namespace-mappings))]
    (sort (remove pred (keys (:class/by-fqcn pkg))))))

(defn dependency-post-order
  [pkg-like class-like]
  (let [pkg (parse pkg-like)]
    (pkg/dependency-post-order pkg class-like)))

(defn transitive-closure
  [pkg-like roots]
  (pkg/transitive-closure (parse pkg-like) roots))

(defn topological-sort
  [pkg-like nodes]
  (pkg/topological-sort (parse pkg-like) nodes))

(defn class-deps
  "Analyzes the dependencies of a node and returns a map separating them into
   :internal (same package/service) and :foreign (external, e.g. java.*, protobuf)."
  ([class-like]
   (class-deps (lookup-pkg-key class-like) class-like false))
  ([pkg-like class-like]
   (class-deps pkg-like class-like false))
  ([pkg-like class-like recursive?]
   (let [pkg  (parse pkg-like)]
     (pkg/class-deps pkg class-like (get foreign :mappings)  recursive?))))

(defn analyze-class
  ([class-like]
   (analyze-class (lookup-pkg-key class-like) class-like))
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

(defn api-types-for-category
  [pkg-like target-category]
  (assert (contains? shared/categories target-category))
  (let [pkg (parse pkg-like)]
    (reduce
      (fn [acc fqcn]
        (let [{:keys [category]} (lookup-class pkg fqcn)]
          (if (= target-category category)
            (conj acc fqcn)
            acc)))
      (sorted-set)
      (package-api-types pkg))))

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
    (map api-types-by-category (keys packages))))

(defn require-graph [fqcn]
  (let [visited (atom {})]
    (letfn [(visit [curr]
              (let [curr-fqcn (str curr)]
                (when-not (contains? @visited curr-fqcn)
                  (let [node (analyze-class curr-fqcn)
                        requires (into (sorted-set) (map str) (keys (:require-types node)))]
                    (swap! visited assoc curr-fqcn requires)
                    (doseq [dep requires]
                      (visit dep))))))]
      (visit fqcn)
      @visited)))

(defn topological-order [fqcn]
  (let [graph (require-graph fqcn)
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
      @stack)))
