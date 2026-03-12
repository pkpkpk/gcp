(ns gcp.dev.packages.sync
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.pprint :as pprint]
   [clojure.tools.deps :as deps]
   [gcp.dev.packages.git :as git]
   [gcp.dev.packages.layout :as layout]
   [gcp.dev.packages.maven :as mvn]
   [gcp.dev.packages.package :as pkg]
   [gcp.dev.util :as u]
   [taoensso.telemere :as tel])
  (:import
   (java.io File)))

#!--------------------------------------------------------------------------------------
#! current runtime

(defn ensure-dependency-loaded!
  "Checks if a Maven dependency is loaded in the current runtime at the specified version.
   If not, attempts to load it dynamically using add-lib."
  [group artifact version]
  #_(let [path (format "META-INF/maven/%s/%s/pom.properties" group artifact)
          current-version (when-let [res (io/resource path)]
                            (with-open [r (io/reader res)]
                              (let [p (java.util.Properties.)]
                                (.load p r)
                                (.getProperty p "version"))))]
      (when (not= current-version version)
        (tel/log! :warn ["Runtime dependency mismatch: loaded=" current-version ", desired=" version ". Attempting to load..."])
        (try
          (let [lib-sym (symbol group artifact)]
            (if-let [add-lib (do (require 'clojure.repl.deps) (resolve 'clojure.repl.deps/add-lib))]
              (add-lib lib-sym {:mvn/version version})
              (if-let [add-libs (do (require 'clojure.tools.deps.alpha.repl) (resolve 'clojure.tools.deps.alpha.repl/add-libs))]
                (add-libs {lib-sym {:mvn/version version}})
                (tel/log! :warn ["Neither clojure.repl.deps/add-lib nor add-libs found."]))))
          (catch Exception e
            (tel/log! :error ["Failed to dynamically load dependency:" (.getMessage e)]))))))

#!---------------------------------------------------------------------------------------
#! maven

(defn latest-stable-release
  [pkg]
  (assert (map? pkg))
  (let [{:keys [googleapis/mvn-org googleapis/mvn-artifact]} pkg]
    (mvn/latest-stable-release mvn-org mvn-artifact)))

#!----------------------------------------------------------------------------------------
#! manifest.edn

(defn manifest-path
  "Returns the path to the manifest.edn file for a given package."
  [pkg]
  (assert (map? pkg))
  (io/file (:package-root pkg) "manifest.edn"))

(defn load-manifest
  "Loads the manifest.edn for a package, returning nil if it doesn't exist."
  [pkg]
  (assert (map? pkg))
  (let [f (manifest-path pkg)]
    (when (.exists f)
      (edn/read-string (slurp f)))))

(defn save-manifest
  "Saves the given manifest map to the package's manifest.edn file."
  [pkg-like manifest]
  (let [f (manifest-path pkg-like)]
    (io/make-parents f)
    (spit f (with-out-str (clojure.pprint/pprint manifest)))))

#!----------------------------------------------------------------------------------------
#! deps.edn

(defn deps-dot-edn [pkg] (io/file (:package-root pkg) "deps.edn"))

(defn update-deps-dot-edn
  "Updates or creates deps.edn with the tracked SDK version."
  [pkg manifest]
  (assert (map? pkg))
  (let [deps-file (deps-dot-edn pkg)
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

(defn update-package-deps
  "Updates the deps.edn for a specific package with the latest upstream version."
  [pkg]
  (assert (map? pkg))
  (let [version (latest-stable-release pkg)
        manifest-data {:googleapis/mvn-org      (:googleapis/mvn-org pkg)
                       :googleapis/mvn-artifact (:googleapis/mvn-artifact pkg)
                       :googleapis/mvn-release  version}
        mvn-group (:googleapis/mvn-org pkg)
        mvn-artifact (:googleapis/mvn-artifact pkg)]
    (tel/log! :info ["Updating" (:name pkg) "deps to" version])
    (update-deps-dot-edn pkg manifest-data)
    #_(ensure-dependency-loaded! mvn-group mvn-artifact version)))

#!----------------------------------------------------------------------------------------

(defn sync-coordinates
  "Updates the manifest with the latest upstream coordinates.
   Returns the updated manifest map (unsaved)."
  [pkg]
  (let [manifest (or (load-manifest pkg) {})
        mvn-org      (:googleapis/mvn-org pkg)
        mvn-artifact (:googleapis/mvn-artifact pkg)
        latest-ver   (or (:pinned-version pkg)
                         (mvn/latest-stable-release mvn-org mvn-artifact))
        current-ver  (:googleapis/mvn-release manifest)]
    (if (= latest-ver current-ver)
      (do
        (tel/log! :info ["Package" (:name pkg) "is up to date (" current-ver ")"])
        manifest)
      (let [repo-path (layout/package-repo pkg)
            _ (tel/log! :info ["Syncing" (:name pkg) "to" latest-ver "in" repo-path])
            ;; Always fetch latest tags to ensure we have up-to-date info
            _ (git/fetch-all repo-path)
            tag-pattern (:googleapis/tag-pattern pkg)
            git-tag (or (:pinned-tag pkg)
                        (git/find-tag-for-artifact repo-path mvn-artifact latest-ver tag-pattern))
            _ (when-not git-tag
                (throw (ex-info "Could not find git tag for release" {:package (:name pkg) :version latest-ver :artifact mvn-artifact :pattern tag-pattern})))
            git-sha (git/rev-parse repo-path git-tag)]
        (tel/log! :info ["Found tag:" git-tag "SHA:" git-sha])
        (merge manifest
               {:googleapis/mvn-org      mvn-org
                :googleapis/mvn-artifact mvn-artifact
                :googleapis/mvn-release  latest-ver
                :googleapis/repo         (:googleapis/git-repo pkg)
                :googleapis/git-repo     (:googleapis/git-repo pkg)
                :googleapis/git-tag      git-tag
                :googleapis/git-sha      git-sha
                ;; Bump our release version? Strategy: <mvn-ver>-0 for now
                :gcp/release             (str latest-ver "-0")})))))

#!----------------------------------------------------------------------------------------
#! git

(defn worktree-status ;; => return path, rev, tag
  [pkg]
  (let [w (layout/package-worktree pkg)]
    (if (.exists w)
      (try
        {:exists? true
         :path w
         :sha (git/rev-parse w "HEAD")}
        (catch Exception e
          {:exists? true
           :path w
           :error (.getMessage e)}))
      {:exists? false
       :path w})))

(defn remove-worktree
  [pkg]
  (let [repo-path (layout/package-repo pkg)
        worktree-path (layout/package-worktree pkg)]
    (tel/log! :info ["Removing worktree" (.getPath worktree-path)])
    (git/remove-worktree repo-path worktree-path)))

(defn create-worktree
  [pkg rev]
  (assert (map? pkg))
  (let [repo-path (layout/package-repo pkg)
        w (layout/package-worktree pkg)
        status (worktree-status pkg)
        target-sha (git/rev-parse repo-path rev)]
    (if (:exists? status)
      (if (= (:sha status) target-sha)
        (tel/log! :info ["Worktree already at" rev "(" target-sha ")"])
        (do
          (tel/log! :warn ["Worktree mismatch. Expected" target-sha "got" (:sha status)])
          (remove-worktree pkg)
          (tel/log! :info ["Creating worktree at" w "for" rev])
          (git/create-worktree repo-path rev w)))
      (do
        (tel/log! :info ["Creating worktree at" w "for" rev])
        (git/create-worktree repo-path rev w)))))

#!-------------------------------------------------------------------

(defn status [pkg]
  (let [manifest (load-manifest pkg)
        w-status (worktree-status pkg)
        latest (try (latest-stable-release pkg) (catch Exception _ nil))]
    {:name (:name pkg)
     :release (:googleapis/mvn-release manifest)
     :latest-release latest
     :manifest manifest
     :worktree w-status}))

(defn needs-sync?
  "Returns true if the package needs to be synced (i.e., latest release differs from current)."
  [pkg]
  (let [{:keys [release latest-release]} (status pkg)]
    (not= release latest-release)))

(defn sync-to-release
  [pkg]
  (tel/log! :info ["Starting sync for package:" (:name pkg)])
  (let [manifest (sync-coordinates pkg)
        _ (save-manifest pkg manifest)
        _ (update-deps-dot-edn pkg manifest)
        tag (:googleapis/git-tag manifest)]
    (if tag
      (create-worktree pkg tag)
      (tel/log! :warn ["No tag found for" (:name pkg) "skipping worktree creation."]))
    (tel/log! :info ["Sync complete for" (:name pkg)])))
