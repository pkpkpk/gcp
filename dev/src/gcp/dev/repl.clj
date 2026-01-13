(ns gcp.dev.repl
  (:require
   [clojure.java.io :as io]
   [clojure.tools.namespace.repl :as tnr]
   [gcp.dev.util :as u]))

;; Configure tools.namespace to only scan the project source directories.
;; We use the repo root from environment variables to keep this portable.
(let [root (u/get-gcp-repo-root)]
  (tnr/set-refresh-dirs
    (io/file root "dev/src")
    (io/file root "packages")))

(defn refresh
  "Refreshes the project source code using clojure.tools.namespace.
   Clears the tracker state first to ensure no stale/unrelated namespaces (like 'user') are scanned."
  [& args]
  (tnr/clear)
  (let [root (u/get-gcp-repo-root)]
    (tnr/set-refresh-dirs
      (io/file root "dev/src")
      (io/file root "packages")))
  (apply tnr/refresh args))

(defn reset
  "Alias for refresh. Can be extended to stop/start system components."
  []
  (refresh))
