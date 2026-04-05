(ns gcp.dev.packages.git
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string])
  (:import
   (java.io File InputStream StringWriter)
   (java.lang ProcessBuilder ProcessBuilder$Redirect)))

(defn- capture
  "Reads from input-stream until EOF and returns a String (or nil if 0 length)."
  [^InputStream input-stream]
  (let [writer (StringWriter.)]
    (io/copy input-stream writer)
    (let [s (string/trim (.toString writer))]
      (when-not (zero? (.length s))
        s))))

(defmacro background
  [& body]
  `(let [result# (promise)]
     (doto (Thread. (fn [] (deliver result# (do ~@body))))
       (.setDaemon true)
       (.start))
     result#))

(defn- run-git
  [dir & args]
  (let [command-args (cons "git" args)
        proc-builder (doto (ProcessBuilder. ^java.util.List command-args)
                       (.directory (io/file dir))
                       (.redirectError ProcessBuilder$Redirect/INHERIT))
        proc (.start proc-builder)
        out (background (capture (.getInputStream proc)))
        exit (.waitFor proc)]
    (if (zero? exit)
      @out
      (throw (ex-info (str "Git command failed: " (string/join " " command-args))
                      {:dir dir :args args :exit exit})))))

(defn current-branch
  "Returns the name of the current git branch."
  [repo-path]
  (try
    (run-git repo-path "branch" "--show-current")
    (catch Exception _ nil)))

(defn dirty?
  "Returns true if the git repository at repo-path has uncommitted changes
   OR if the current branch is not 'main'.
   Optionally scopes the uncommitted changes check to a specific path within the repo."
  ([repo-path]
   (dirty? repo-path nil))
  ([repo-path path]
   (if (not= "main" (current-branch repo-path))
     true
     (let [args (cond-> ["status" "--porcelain"]
                  path (conj "--" path))
           out (apply run-git repo-path args)]
       (not (string/blank? out))))))

(defn fetch
  "Fetches updates from the remote."
  [repo-path]
  (run-git repo-path "fetch" "--all" "--tags" "--prune"))

(defn pull
  "Pulls updates from the remote."
  [repo-path]
  (run-git repo-path "pull"))

(defn tags
  "Returns a sorted list of tags."
  [repo-path]
  (let [out (run-git repo-path "tag" "--sort=v:refname")]
    (if (string/blank? out)
      []
      (string/split-lines out))))

(defn current-tag
  "Returns the current tag if the HEAD is exactly on a tag, otherwise nil."
  [repo-path]
  (try
    (run-git repo-path "describe" "--tags" "--exact-match" "HEAD")
    (catch Exception _ nil)))

(defn checkout
  "Checks out the specified tag or revision."
  [repo-path rev]
  (run-git repo-path "checkout" rev))

(defn create-worktree
  "Creates a new worktree for the given tag/rev at the target path."
  [repo-path rev target-path]
  (let [abs-target (.getAbsolutePath (io/file target-path))]
    (run-git repo-path "worktree" "add" "--detach" abs-target rev)))

(defn remove-worktree
  "Removes the worktree at the target path. 
   repo-path should be the main repository path."
  [repo-path worktree-path]
  (let [abs-target (.getAbsolutePath (io/file worktree-path))]
    (run-git repo-path "worktree" "remove" "--force" abs-target)))

(defn prune-worktrees
  "Prunes stale worktree information."
  [repo-path]
  (run-git repo-path "worktree" "prune"))

(defn rev-parse
  "Returns the full SHA-1 for the given revision."
  [repo-path rev]
  (run-git repo-path "rev-parse" rev))

(defn- commits-between-impl
  "Returns a list of commit summaries between two revisions, optionally scoped by a path."
  [repo-path old-rev new-rev & [path]]
  (let [args (cond-> ["log" "--format=%h %s" (str old-rev ".." new-rev)]
               path (conj "--" path))
        out (apply run-git repo-path args)]
    (if (string/blank? out)
      []
      (string/split-lines out))))

(def commits-between (memoize commits-between-impl))

(defn- diff-files-impl
  "Returns a map of file changes between two revisions, optionally scoped by a path."
  [repo-path old-rev new-rev & [path]]
  (let [args (cond-> ["diff" "--name-status" (str old-rev ".." new-rev)]
               path (conj "--" path))
        out (apply run-git repo-path args)]
    (if (string/blank? out)
      {:added [] :modified [] :deleted [] :renamed [] :copied []}
      (reduce
        (fn [acc line]
          (let [[status file] (string/split line #"\s+" 2)
                status-key (case (first status)
                             \A :added
                             \M :modified
                             \D :deleted
                             \R :renamed
                             \C :copied
                             :modified)]
            (update acc status-key (fnil conj []) file)))
        {:added [] :modified [] :deleted [] :renamed [] :copied []}
        (string/split-lines out)))))

(def diff-files (memoize diff-files-impl))

(defn diff-patch
  "Returns the unified diff patch between two revisions for the given paths."
  [repo-path old-rev new-rev paths]
  (if (empty? paths)
    ""
    (let [args (concat ["diff" (str old-rev ".." new-rev) "--"] paths)
          out (apply run-git repo-path args)]
      out)))

(defn- ls-remote-tags-impl
  "Lists tags from the remote matching a pattern."
  [repo-path pattern]
  (let [out (run-git repo-path "ls-remote" "--tags" "origin" pattern)]
    (if (string/blank? out)
      []
      (->> (string/split-lines out)
           (map #(second (string/split % #"\s+")))
           (remove #(string/ends-with? % "^{}"))
           (map #(string/replace % "refs/tags/" ""))))))

(def ls-remote-tags (memoize ls-remote-tags-impl))

(defn fetch-tag
  "Fetches a specific tag from the remote."
  [repo-path tag]
  (run-git repo-path "fetch" "origin" (str "refs/tags/" tag ":refs/tags/" tag)))

(defn fetch-all
  "Fetches all updates (branches and tags) from the remote."
  [repo-path]
  (run-git repo-path "fetch" "--all" "--tags" "--prune"))

(defn- find-tag-for-artifact-impl
  "Finds the git tag corresponding to the artifact version.
   Tries local tags first, then queries remote and fetches if found.
   Handles various tagging conventions (e.g. vX.Y.Z, artifact-vX.Y.Z, artifact-X.Y.Z).
   Optionally accepts a tag-pattern (e.g. 'v*') to limit the search scope.
   Fallback: if no specific tag found, tries to find the latest semver tag (vX.Y.Z) from the repo.
   For monorepos (pkg :monorepo? true), we ignore simple 'vX.Y.Z' matches if they don't match the
   module version inside the tag, preferring the latest tag as a safer fallback."
  ([repo-path artifact-id version]
   (find-tag-for-artifact-impl repo-path artifact-id version "v*"))
  ([repo-path artifact-id version tag-pattern]
   (find-tag-for-artifact-impl nil repo-path artifact-id version tag-pattern))
  ([pkg repo-path artifact-id version tag-pattern]
   (let [monorepo? (:monorepo? pkg)
         simple-tag (str "v" version)
         artifact-tag-v (str artifact-id "-v" version)
         artifact-tag (str artifact-id "-" version)
         ;; If it's a monorepo, simple 'vX.Y.Z' is likely a collision with an ancient monorepo-wide tag
         candidates (if monorepo?
                      [artifact-tag-v artifact-tag]
                      [simple-tag artifact-tag-v artifact-tag])
         local-check (fn [t]
                       (try
                         (run-git repo-path "rev-parse" "--verify" (str "refs/tags/" t))
                         t
                         (catch Exception _ nil)))]
     (or (some local-check candidates)
         ;; Try remote with a broader search or specific pattern
         (let [pattern (if tag-pattern tag-pattern (str "*" artifact-id "*" version))
               remote-tags (into #{} (ls-remote-tags repo-path pattern))]
           (cond
             (contains? remote-tags artifact-tag-v)
             (do (fetch-tag repo-path artifact-tag-v) artifact-tag-v)

             (contains? remote-tags artifact-tag)
             (do (fetch-tag repo-path artifact-tag) artifact-tag)

             (and (not monorepo?) (contains? remote-tags simple-tag))
             (do (fetch-tag repo-path simple-tag) simple-tag)

             ;; Fallback: look for any tag ending in the version
             :else
             (if-let [match (first (filter #(string/ends-with? % (str "v" version)) remote-tags))]
               (do (fetch-tag repo-path match) match)
               ;; Final Fallback: if it's likely a monorepo and we found nothing specific, 
               ;; pick the latest 'vX.Y.Z' tag.
               (let [all-tags (ls-remote-tags repo-path "v*")
                     semver-tags (filter #(re-matches #"v\d+\.\d+\.\d+" %) all-tags)
                     sorted-tags (sort-by (fn [t] (mapv #(Integer/parseInt %) (string/split (subs t 1) #"\.")))
                                          (fn [a b] (compare b a)) ;; reverse sort
                                          semver-tags)
                     latest (first sorted-tags)]
                 (when latest
                   (println "WARN: No specific tag found for" artifact-id version ". Falling back to latest monorepo tag:" latest)
                   (fetch-tag repo-path latest)
                   latest)))))))))

(def find-tag-for-artifact (memoize find-tag-for-artifact-impl))
