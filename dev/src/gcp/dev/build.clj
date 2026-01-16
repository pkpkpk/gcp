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

(defn sync-coordinates
  "Updates the manifest with the latest upstream coordinates.
   Returns the updated manifest map (unsaved)."
  [pkg-key]
  (let [pkg-def (get pkg/packages pkg-key)
        _ (when-not pkg-def (throw (ex-info "Unknown package" {:package pkg-key})))
        manifest (or (pkg/load-manifest pkg-key) {})
        mvn-org      (:googleapis/mvn-org pkg-def)
        mvn-artifact (:googleapis/mvn-artifact pkg-def)
        latest-ver   (or (:pinned-version pkg-def)
                         (mvn/latest-release mvn-org mvn-artifact))
        current-ver  (:googleapis/mvn-release manifest)]
    (if (= latest-ver current-ver)
      (do
        (println "Package" pkg-key "is up to date (" current-ver ")")
        manifest)
      (let [repo-path (pkg/package-root pkg-def)
            _ (println "Syncing" pkg-key "to" latest-ver "in" repo-path)
            ;; Ensure we have the latest tags
            ;; _ (git/fetch repo-path) ;; Optional: make this explicit step in the plan
            git-tag (git/find-tag-for-artifact repo-path mvn-artifact latest-ver)
            _ (when-not git-tag
                (throw (ex-info "Could not find git tag for release"
                                {:package pkg-key :version latest-ver :artifact mvn-artifact})))
            git-sha (git/rev-parse repo-path git-tag)]
        (println "Found tag:" git-tag "SHA:" git-sha)
        (merge manifest
               {:googleapis/mvn-org      mvn-org
                :googleapis/mvn-artifact mvn-artifact
                :googleapis/mvn-release  latest-ver
                :googleapis/repo         (:googleapis/git-repo pkg-def)
                :googleapis/git-repo     (:googleapis/git-repo pkg-def)
                :googleapis/git-tag      git-tag
                :googleapis/git-sha      git-sha
                ;; Bump our release version? Strategy: <mvn-ver>-0 for now
                :gcp/release             (str latest-ver "-0")})))))

;; Phase 2: Analysis & Compilation

(defn- calculate-file-hash [file]
  (digest/sha256 (slurp file)))

(defn- fqcn->file-path
  "Determines the output file path for a given FQCN."
  [pkg-key node]
  (let [pkg-def (get pkg/packages pkg-key)
        target-dir (:package-root pkg-def)
        ns-sym (u/package-to-ns (:package node))
        class-name (:name node)
        ;; ns-sym is like gcp.vertexai.v1.api
        ;; we append the class name to get the full namespace
        full-ns (str ns-sym "." class-name)
        ;; convert to path
        rel-path (str (string/replace full-ns "." "/") ".clj")]
    (io/file target-dir "src" rel-path)))

(defn- create-temp-dir [prefix]
  (.toFile (Files/createTempDirectory prefix (into-array FileAttribute []))))

(defn- with-repo-at-rev
  "Executes f with the repository checked out at the given revision.
   Uses a worktree if necessary.
   f is called with the (potentially temporary) root directory."
  [pkg-def rev f]
  (let [repo-path (pkg/package-root pkg-def)
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

(defn update-deps
  "Updates or creates deps.edn with the tracked SDK version."
  [pkg-key manifest]
  (let [pkg-def (get pkg/packages pkg-key)
        deps-file (io/file (:package-root pkg-def) "deps.edn")
        mvn-coord (symbol (:googleapis/mvn-org manifest)
                          (:googleapis/mvn-artifact manifest))
        version   (:googleapis/mvn-release manifest)]
    (if (.exists deps-file)
      (let [deps-edn (clojure.edn/read-string (slurp deps-file))
            updated (assoc-in deps-edn [:deps mvn-coord :mvn/version] version)]
        (spit deps-file (with-out-str
                          (binding [*print-namespace-maps* false]
                            (pprint/pprint updated)))))
      ;; Create new deps.edn
      (let [new-deps {:paths ["src"]
                      :deps {mvn-coord {:mvn/version version}
                             'gcp/global {:local/root "../global"}
                             'gcp/foreign {:local/root "../foreign"}}}]
        (io/make-parents deps-file)
        (spit deps-file (with-out-str
                          (binding [*print-namespace-maps* false]
                            (pprint/pprint new-deps))))))))

(defn ensure-dependency-loaded!
  "Checks if a Maven dependency is loaded in the current runtime at the specified version.
   If not, attempts to load it dynamically using add-lib."
  [group artifact version]
  (let [path (format "META-INF/maven/%s/%s/pom.properties" group artifact)
        current-version (when-let [res (io/resource path)]
                          (with-open [r (io/reader res)]
                            (let [p (java.util.Properties.)]
                              (.load p r)
                              (.getProperty p "version"))))]
    (when (not= current-version version)
      (println "Runtime dependency mismatch: loaded=" current-version ", desired=" version ". Attempting to load...")
      (try
        (let [lib-sym (symbol group artifact)]
          (if-let [add-lib (do (require 'clojure.repl.deps) (resolve 'clojure.repl.deps/add-lib))]
            (add-lib lib-sym {:mvn/version version})
            (if-let [add-libs (do (require 'clojure.tools.deps.alpha.repl) (resolve 'clojure.tools.deps.alpha.repl/add-libs))]
              (add-libs {lib-sym {:mvn/version version}})
              (println "WARN: Neither clojure.repl.deps/add-lib nor add-libs found."))))
        (catch Exception e
          (println "WARN: Failed to dynamically load dependency:" (.getMessage e)))))))

(defn update-package-deps
  "Updates the deps.edn for a specific package with the latest upstream version."
  [pkg-key]
  (let [pkg-def (get pkg/packages pkg-key)
        _ (when-not pkg-def (throw (ex-info "Unknown package" {:package pkg-key})))
        version (pkg/latest-release pkg-key)
        manifest-data {:googleapis/mvn-org      (:googleapis/mvn-org pkg-def)
                       :googleapis/mvn-artifact (:googleapis/mvn-artifact pkg-def)
                       :googleapis/mvn-release  version}
        mvn-group (:googleapis/mvn-org pkg-def)
        mvn-artifact (:googleapis/mvn-artifact pkg-def)]
    (println "Updating" pkg-key "deps to" version)
    (update-deps pkg-key manifest-data)
    (ensure-dependency-loaded! mvn-group mvn-artifact version)))

(defn update-all-deps
  "Updates deps.edn for all registered packages."
  []
  (doseq [pkg-key (keys pkg/packages)]
    (update-package-deps pkg-key)))

(defn check-package-foreign
  "Checks foreign dependencies for a list of dependency strings."
  [dependencies custom-mappings generated-fqcns opaque]
  (let [find-parent-generator (fn [dep-str]
                                (loop [s dep-str]
                                  (if-let [dot-idx (string/last-index-of s ".")]
                                    (let [parent (subs s 0 dot-idx)]
                                      (if (or (contains? generated-fqcns parent)
                                              (contains? custom-mappings parent))
                                        parent
                                        (recur parent)))
                                    nil)))]
    (reduce (fn [acc dep-str]
              (if (contains? acc dep-str)
                acc
                (let [{:keys [package class]} (u/split-fqcn dep-str)
                      ns-sym (u/package-to-ns package)
                      top-class (first (string/split class #"\."))
                      binding-ns (symbol (str ns-sym "." top-class))
                      foreign-ns (u/infer-foreign-ns dep-str)
                      status
                      (cond
                        ;; Case 0: Opaque Type (Deliberately unmapped)
                        (contains? opaque dep-str)
                        {:status :ok :type :opaque}

                        ;; Case 1: Satisfied by internal generation (exact or parent)
                        (or (contains? generated-fqcns dep-str)
                            (find-parent-generator dep-str))
                        {:status :ok :type :internal}

                        ;; Case 2: Custom Mapping
                        (contains? custom-mappings dep-str)
                        (let [custom-ns (get custom-mappings dep-str)]
                          (if (u/foreign-binding-exists? custom-ns)
                            {:status :ok :type :custom :ns custom-ns}
                            {:status :missing :type :custom :ns custom-ns}))

                        ;; Case 3: Manual Foreign Binding (gcp.foreign.*)
                        (u/foreign-binding-exists? foreign-ns)
                        (let [meta (u/ns-meta foreign-ns)]
                          (if (:gcp.dev/certification meta)
                            {:status :ok :type :foreign :ns foreign-ns}
                            {:status :uncertified :type :foreign :ns foreign-ns}))

                        ;; Case 4: Generated Binding (gcp.<pkg>.bindings.*)
                        (u/foreign-binding-exists? binding-ns)
                        (let [meta (u/ns-meta binding-ns)]
                          (if (:gcp.dev/certification meta)
                            {:status :ok :type :binding :ns binding-ns}
                            {:status :uncertified :type :binding :ns binding-ns}))

                        :else
                        {:status :missing :type :unknown :expected-ns binding-ns})]
                  (assoc acc dep-str status))))
            {}
            dependencies)))

(defn- validate-dependencies! [sorted-nodes exemptions custom-mappings opaque]
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

(defn build-package
  "Orchestrates the build for a package."
  [pkg-key]
  (let [pkg-def  (get pkg/packages pkg-key)
        _ (println "----Phase: Syncing Coordinates...")
        manifest (sync-coordinates pkg-key)
        git-tag  (:googleapis/git-tag manifest)
        _ (println "----Phase: Syncing Coordinates... Done")]
    (when-not git-tag
      (throw (ex-info "Missing git tag in manifest" {:package pkg-key})))
    (with-repo-at-rev pkg-def git-tag
      (fn [worktree-root]
        (let [;; Override root for parsing
              pkg-def-overridden (assoc pkg-def :override-root worktree-root)
              ;; Analyze Package
              _ (println "----Phase: Analyzing Package...")
              pkg-ast (pkg/parse pkg-def-overridden)
              _ (println "----Phase: Analyzing Package... Done")
              ;; Selection Strategy
              _ (println "----Phase: Selecting Classes...")
              api-roots (:api-roots pkg-def)
              _ (when (empty? api-roots)
                  (println "WARN: No :api-roots defined for" pkg-key ". Falling back to all parsed classes."))
              closure (if (seq api-roots)
                        (pkg/transitive-closure pkg-ast api-roots)
                        (keys (:class/by-fqcn pkg-ast)))
              sorted-classes (pkg/topological-sort pkg-ast closure)
              ;; Resolve nodes
              sorted-nodes (keep (fn [fqcn] (get-in pkg-ast [:class/by-fqcn fqcn])) sorted-classes)
              _ (println "    Selected" (count sorted-nodes) "classes for generation (from" (count api-roots) "roots).")
              _ (println "----Phase: Selecting Classes... Done")
              ;; Validate Dependencies
              _ (println "----Phase: Validating Dependencies...")
              custom-mappings (:custom pkg-def)
              opaque (set (:opaque pkg-def))
              exemptions (set (concat (:exempt pkg-def) (:exemptions manifest) (keys custom-mappings)))
              _ (validate-dependencies! sorted-nodes exemptions custom-mappings opaque)
              _ (println "----Phase: Validating Dependencies... Done")
              ;; Calculate FQCNs being generated in this run
              normalize (fn [s] (string/replace (str s) "$" "."))
              generated-fqcns (into #{} (comp (remove #(contains? exemptions (str (:package %) "." (:name %))))
                                              (map #(str (:package %) "." (:name %)))
                                              (map normalize))
                                    sorted-nodes)
              ;; Toolchain Hash
              toolchain (digest/compute-toolchain-hash)
              toolchain-sha (:hash toolchain)
              ;; Process Bindings
              _ (println "----Phase: Generating Bindings...")
              updated-bindings
              (reduce
                (fn [bindings node]
                  (let [fqcn (str (:package node) "." (:name node))
                        version (u/extract-version (:doc node))
                        output-file (fqcn->file-path pkg-key node)
                        rel-path (str (u/relative-path (:package-root pkg-def) output-file))
                        ;; ================================================================
                        ;; Calculate source path relative to worktree root
                        included-roots (mapv #(io/file worktree-root (subs % 1)) (:include pkg-def))
                        candidates (map #(io/file % (str (:name node) ".java")) included-roots)
                        source-file (first (filter #(.exists %) candidates))]
                    ;; REMOVING THIS ASSERTION IS STRICTLY FORBIDDEN =======================
                    (assert (.exists source-file) (str "source file DNE " fqcn " @" (.getPath source-file) " in worktree")) ;; REMOVING THIS ASSERTION IS STRICTLY FORBIDDEN
                    (if (contains? exemptions fqcn)
                      (do (println "Skipping exempt class:" fqcn)
                          bindings)
                      (let [java-input-sha (calculate-file-hash source-file)
                            existing-entry (get bindings rel-path)
                            inputs-changed? (or (not= (:java-input-sha existing-entry) java-input-sha)
                                                (not= (:toolchain-sha existing-entry) toolchain-sha)
                                                (not (.exists output-file)))]
                        (if inputs-changed?
                          (do
                            (println "------ Generating" rel-path)
                            (compiler/compile-and-certify pkg-key fqcn (.getPath output-file) {:generated-fqcns generated-fqcns})
                            (println "------ SUCCESS" rel-path)
                            (let [clj-output-sha (calculate-file-hash output-file)
                                  cert-meta (u/ns-meta (symbol (str (u/package-to-ns (:package node)) "." (:className node))))
                                  certification (:gcp.dev/certification cert-meta)]
                              (assoc bindings rel-path
                                     {:java-input-sha java-input-sha
                                      :clj-output-sha clj-output-sha
                                      :toolchain-sha  toolchain-sha
                                      :certification  certification})))
                          (assoc bindings rel-path existing-entry))))))
                (:bindings manifest)
                sorted-nodes)
              _ (println "----Phase: Generating Bindings... Done")
              new-manifest (assoc manifest :bindings (into (sorted-map) updated-bindings))]
          (update-deps pkg-key new-manifest)
          (pkg/save-manifest pkg-key new-manifest)
          (println "Build complete for" pkg-key)
          new-manifest)))))
