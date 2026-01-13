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

(defn ls-remote-tags
  "Lists tags from the remote matching a pattern."
  [repo-path pattern]
  (let [out (run-git repo-path "ls-remote" "--tags" "origin" pattern)]
    (if (string/blank? out)
      []
      (->> (string/split-lines out)
           (map #(second (string/split % #"\s+")))
           (map #(string/replace % "refs/tags/" ""))))))

(defn fetch-tag
  "Fetches a specific tag from the remote."
  [repo-path tag]
  (run-git repo-path "fetch" "origin" (str "refs/tags/" tag ":refs/tags/" tag)))

(defn find-tag-for-artifact
  "Finds the git tag corresponding to the artifact version.
   Tries local tags first, then queries remote and fetches if found."
  [repo-path artifact-id version]
  (let [simple-tag (str "v" version)
        artifact-tag (str artifact-id "-v" version)
        local-check (fn [t]
                      (try
                        (run-git repo-path "rev-parse" "--verify" (str t "^{tag}"))
                        t
                        (catch Exception _ nil)))]
    (or (local-check simple-tag)
        (local-check artifact-tag)
        ;; Try remote
        (let [remote-tags (into #{} (concat (ls-remote-tags repo-path simple-tag)
                                            (ls-remote-tags repo-path artifact-tag)))]
          (cond
            (contains? remote-tags simple-tag)
            (do (fetch-tag repo-path simple-tag) simple-tag)

            (contains? remote-tags artifact-tag)
            (do (fetch-tag repo-path artifact-tag) artifact-tag)

            :else nil)))))
