(ns gcp.dev.build
  (:require
   [clojure.java.io :as io]
   [clojure.pprint :as pprint]
   [clojure.string :as string]
   [gcp.dev.compiler :as compiler]
   [gcp.dev.digest :as digest]
   [gcp.dev.packages :as pkg]
   [gcp.dev.packages.git :as git]
   [gcp.dev.packages.maven :as mvn]
   [gcp.dev.toolchain.analyzer :as ana]
   [gcp.dev.toolchain.fuzz :as fuzz]
   [gcp.dev.toolchain.parser :as parser]
   [gcp.dev.util :as u])
  (:import
   (java.nio.file Files)
   (java.nio.file.attribute FileAttribute)))

(set! *print-namespace-maps* false)

(defn clear-cache! []
  (parser/clear-cache))

;; Phase 2: Analysis & Compilation

(defn- calculate-file-hash [file]
  (digest/sha256 (slurp file)))

(defn- create-temp-dir [prefix]
  (.toFile (Files/createTempDirectory prefix (into-array FileAttribute []))))

(defn- with-repo-at-rev
  "Executes f with the repository checked out at the given revision.
   Uses a worktree if necessary.
   f is called with the (potentially temporary) root directory."
  [pkg-def rev f]
  #_(let [repo-path (pkg/package-root pkg-def)
          current-sha (try (git/rev-parse repo-path "HEAD") (catch Exception _ nil))
          target-sha (try (git/rev-parse repo-path rev) (catch Exception _ nil))]
      (if (and current-sha target-sha (= current-sha target-sha))
        (f repo-path)
        (let [worktree-path (create-temp-dir (str "gcp-build-" rev "-"))]
          (try
            (println "Creating worktree at" worktree-path "for" rev)
            (git/create-worktree repo-path rev worktree-path)
            (f worktree-path)
            (finally
              (println "Removing worktree" worktree-path)
              (git/remove-worktree repo-path worktree-path)))))))

; (defn check-package-foreign
;  "Checks foreign dependencies for a list of dependency strings."
;  [dependencies custom-mappings generated-fqcns opaque]
;  (let [find-parent-generator (fn [dep-str]
;                                (loop [s dep-str]
;                                  (if-let [dot-idx (string/last-index-of s ".")]
;                                    (let [parent (subs s 0 dot-idx)]
;                                      (if (or (contains? generated-fqcns parent)
;                                              (contains? custom-mappings parent))
;                                        parent
;                                        (recur parent)))
;                                    nil)))]
;    (reduce (fn [acc dep-str]
;              (if (contains? acc dep-str)
;                acc
;                (let [{:keys [package class]} (u/split-fqcn dep-str)
;                      ns-sym (u/package-to-ns package)
;                      top-class (first (string/split class #"\."))
;                      binding-ns (symbol (str ns-sym "." top-class))
;                      foreign-ns (u/infer-foreign-ns dep-str)
;                      status
;                      (cond
;                        ;; Case 0: Opaque Type (Deliberately unmapped)
;                        (contains? opaque dep-str)
;                        {:status :ok :type :opaque}
;
;                        ;; Case 1: Satisfied by internal generation (exact or parent)
;                        (or (contains? generated-fqcns dep-str)
;                            (find-parent-generator dep-str))
;                        {:status :ok :type :internal}
;
;                        ;; Case 2: Custom Mapping
;                        (contains? custom-mappings dep-str)
;                        (let [custom-ns (get custom-mappings dep-str)]
;                          (if (u/foreign-binding-exists? custom-ns)
;                            {:status :ok :type :custom :ns custom-ns}
;                            {:status :missing :type :custom :ns custom-ns}))
;
;                        ;; Case 3: Manual Foreign Binding (gcp.foreign.*)
;                        (u/foreign-binding-exists? foreign-ns)
;                        (let [meta (u/ns-meta foreign-ns)]
;                          (if (:gcp.dev/certification meta)
;                            {:status :ok :type :foreign :ns foreign-ns}
;                            {:status :uncertified :type :foreign :ns foreign-ns}))
;
;                        ;; Case 4: Generated Binding (gcp.<pkg>.bindings.*)
;                        (u/foreign-binding-exists? binding-ns)
;                        (let [meta (u/ns-meta binding-ns)]
;                          (if (:gcp.dev/certification meta)
;                            {:status :ok :type :binding :ns binding-ns}
;                            {:status :uncertified :type :binding :ns binding-ns}))
;
;                        :else
;                        {:status :missing :type :unknown :expected-ns binding-ns})]
;                  (assoc acc dep-str status))))
;            {}
;            dependencies)))

(defn- validate-dependencies! [sorted-nodes exemptions custom-mappings opaque]
  #_
  (let [normalize (fn [s] (string/replace (str s) "$" "."))
        generated-fqcns (into #{} (comp (remove #(contains? exemptions (str (:package %) "." (:name %))))
                                        (map #(str (:package %) "." (:name %)))
                                        (map normalize))
                              sorted-nodes)
        all-deps (into #{}
                       (comp (mapcat (fn [node]
                                       (let [analyzed (ana/analyze-class-node node)]
                                         (:typeDependencies analyzed))))
                             (map normalize)
                             (filter #(and (string/starts-with? % "com.google.cloud")
                                           (not (u/native-type %)))))
                       sorted-nodes)
        results (check-package-foreign all-deps custom-mappings generated-fqcns opaque)]
    (doseq [[dep {:keys [status type ns expected-ns]}] results]
      (case status
        :ok nil
        :missing (if (= type :custom)
                   (throw (ex-info (str "Missing custom binding: " dep " mapped to " ns)
                                   {:missing dep :mapped-to ns}))
                   (throw (ex-info (str "Broken dependency: " dep ". No generation target or foreign binding found at " expected-ns
                                        " (Missing Core Library?)")
                                   {:missing dep :expected-ns expected-ns})))
        :uncertified (throw (ex-info (str "Uncertified foreign dependency: " dep " in " ns)
                                     {:missing dep :ns ns}))))))

; (defn build-package
;  "Orchestrates the build for a package."
;  [pkg-key]
;  (let [pkg-def  (get pkg/packages pkg-key)
;        _ (println "----Phase: Syncing Coordinates...")
;        manifest (sync-coordinates pkg-key)
;        git-tag  (:googleapis/git-tag manifest)
;        _ (println "----Phase: Syncing Coordinates... Done")]
;    (when-not git-tag
;      (throw (ex-info "Missing git tag in manifest" {:package pkg-key})))
;    (with-repo-at-rev pkg-def git-tag
;      (fn [worktree-root]
;        (let [;; Override root for parsing
;              pkg-def-overridden (assoc pkg-def :override-root worktree-root)
;              ;; Analyze Package
;              _ (println "----Phase: Analyzing Package...")
;              pkg-ast (pkg/parse pkg-def-overridden)
;              _ (println "----Phase: Analyzing Package... Done")
;              ;; Selection Strategy
;              _ (println "----Phase: Selecting Classes...")
;              api-roots (:api-roots pkg-def)
;              _ (when (empty? api-roots)
;                  (println "WARN: No :api-roots defined for" pkg-key ". Falling back to all parsed classes."))
;              closure (if (seq api-roots)
;                        (pkg/transitive-closure pkg-ast api-roots)
;                        (keys (:class/by-fqcn pkg-ast)))
;              sorted-classes (pkg/topological-sort pkg-ast closure)
;              ;; Resolve nodes
;              sorted-nodes (keep (fn [fqcn] (get-in pkg-ast [:class/by-fqcn fqcn])) sorted-classes)
;              _ (println "    Selected" (count sorted-nodes) "classes for generation (from" (count api-roots) "roots).")
;              _ (println "----Phase: Selecting Classes... Done")
;              ;; Validate Dependencies
;              _ (println "----Phase: Validating Dependencies...")
;              custom-mappings (:custom pkg-def)
;              opaque (set (:opaque pkg-def))
;              exemptions (set (concat (:exempt pkg-def) (:exemptions manifest) (keys custom-mappings)))
;              _ (validate-dependencies! sorted-nodes exemptions custom-mappings opaque)
;              _ (println "----Phase: Validating Dependencies... Done")
;              ;; Calculate FQCNs being generated in this run
;              normalize (fn [s] (string/replace (str s) "$" "."))
;              generated-fqcns (into #{} (comp (remove #(contains? exemptions (str (:package %) "." (:name %))))
;                                              (map #(str (:package %) "." (:name %)))
;                                              (map normalize))
;                                    sorted-nodes)
;              ;; Toolchain Hash
;              toolchain (digest/compute-toolchain-hash)
;              toolchain-sha (:hash toolchain)
;              ;; Process Bindings
;              _ (println "----Phase: Generating Bindings...")
;              updated-bindings
;              (reduce
;                (fn [bindings node]
;                  (let [fqcn (str (:package node) "." (:name node))
;                        version (u/extract-version (:doc node))
;                        output-file (fqcn->file-path pkg-key node)
;                        rel-path (str (u/relative-path (:package-root pkg-def) output-file))
;                        ;; ================================================================
;                        ;; Calculate source path relative to worktree root
;                        included-roots (mapv #(io/file worktree-root (subs % 1)) (:include pkg-def))
;                        candidates (map #(io/file % (str (:name node) ".java")) included-roots)
;                        source-file (first (filter #(.exists %) candidates))]
;                    ;; REMOVING THIS ASSERTION IS STRICTLY FORBIDDEN =======================
;                    (assert (.exists source-file) (str "source file DNE " fqcn " @" (.getPath source-file) " in worktree")) ;; REMOVING THIS ASSERTION IS STRICTLY FORBIDDEN
;                    (if (contains? exemptions fqcn)
;                      (do (println "Skipping exempt class:" fqcn)
;                          bindings)
;                      (let [java-input-sha (calculate-file-hash source-file)
;                            existing-entry (get bindings rel-path)
;                            inputs-changed? (or (not= (:java-input-sha existing-entry) java-input-sha)
;                                                (not= (:toolchain-sha existing-entry) toolchain-sha)
;                                                (not (.exists output-file)))]
;                        (if inputs-changed?
;                          (do
;                            (println "------ Generating" rel-path)
;                            (compiler/compile-and-certify pkg-key fqcn (.getPath output-file) {:generated-fqcns generated-fqcns})
;                            (println "------ SUCCESS" rel-path)
;                            (let [clj-output-sha (calculate-file-hash output-file)
;                                  cert-meta (u/ns-meta (symbol (str (u/package-to-ns (:package node)) "." (:className node))))
;                                  certification (:gcp.dev/certification cert-meta)]
;                              (assoc bindings rel-path
;                                     {:java-input-sha java-input-sha
;                                      :clj-output-sha clj-output-sha
;                                      :toolchain-sha  toolchain-sha
;                                      :certification  certification})))
;                          (assoc bindings rel-path existing-entry))))))
;                (:bindings manifest)
;                sorted-nodes)
;              _ (println "----Phase: Generating Bindings... Done")
;              new-manifest (assoc manifest :bindings (into (sorted-map) updated-bindings))]
;          (update-deps pkg-key new-manifest)
;          (pkg/save-manifest pkg-key new-manifest)
;          (println "Build complete for" pkg-key)
;          new-manifest)))))
