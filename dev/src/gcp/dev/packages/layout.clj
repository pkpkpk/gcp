(ns gcp.dev.packages.layout
  (:require
   [clojure.java.io :as io]
   [gcp.dev.util :as u]))

(defonce googleapis-root (u/get-googleapis-repos-path))
(defonce packages-root (io/file (u/get-gcp-repo-root) "packages"))
(defonce worktrees-root (io/file googleapis-root "__WORKTREE__"))

(defn package-worktree
  "Returns the File pointing to the worktree for a package.
   e.g. .../__WORKTREE__/gcp.bigquery"
  [pkg]
  (assert (map? pkg))
  (io/file worktrees-root (name (:name pkg))))

(defn package-repo
  "Returns the File pointing to the underlying git repository.
   Handles both monorepos (google-cloud-java) and standalone repos."
  [pkg]
  (assert (map? pkg))
  (let [{:keys [googleapis/git-repo]} pkg]
    (if-let [override (:override-root pkg)]
      (io/file override)
      (io/file googleapis-root git-repo))))

(defn package-source-root
  "Returns the root directory where source code should be analyzed.
   STRICTLY uses the worktree. Throws if worktree does not exist.
   Correctly handles monorepo sub-modules by appending git-repo-root."
  [pkg]
  (let [wt (package-worktree pkg)]
    (if (.exists wt)
      (if-let [sub-root (:googleapis/git-repo-root pkg)]
        (io/file wt sub-root)
        wt)
      (throw (ex-info (str "Worktree not found for package " (:name pkg) ". Run sync-to-release first.")
                      {:worktree wt :package (:name pkg)})))))

(defn package-bindings-root
  "Returns the root directory where generated bindings should be written.
   Defaults to (io/file package-root \"src\") if :bindings-target-root is not set."
  [pkg]
  (assert (map? pkg))
  (or (:bindings-target-root pkg)
      (io/file (:package-root pkg) "src")))
