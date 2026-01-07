(ns gcp.dev.packages.git
  (:require [clojure.java.io :as io]
            [clojure.string :as string])
  (:import [java.lang ProcessBuilder ProcessBuilder$Redirect]
           [java.io File StringWriter InputStream]))

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
