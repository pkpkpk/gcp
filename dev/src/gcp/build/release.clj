(ns gcp.build.release
  (:require [gcp.build.core :as core]
            [gcp.build.global :as global]
            [gcp.build.util :as util]
            [gcp.dev.packages.definitions :as defs]
            [gcp.dev.packages.git :as git]
            [gcp.dev.util :as dev-util]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn deploy-global []
  (let [repo-root (dev-util/get-gcp-repo-root)
        package-root (:package-root defs/global)
        rel-path (dev-util/relative-path repo-root package-root)]
    (if (git/dirty? repo-root rel-path)
      (throw (ex-info "Cannot deploy global: directory is dirty or not on main branch." {:package "global"}))
      (let [version (global/build)
            p (global/pom version)]
        (println "Deploying global version:" version)
        (util/deploy p)
        {:package "gcp.global" :version version}))))

(defn deploy-wrapper [pkg]
  (let [repo-root (dev-util/get-gcp-repo-root)
        package-root (:package-root pkg)
        rel-path (dev-util/relative-path repo-root package-root)]
    (if (git/dirty? repo-root rel-path)
      (throw (ex-info (str "Cannot deploy " (:name pkg) ": directory is dirty or not on main branch.") {:package (:name pkg)}))
      (let [deps-file (io/file package-root "deps.edn")
            deps-map (read-string (slurp deps-file))
            global-version (global/build)
            version (core/build-package pkg)
            p (core/pom pkg version global-version deps-map)]
        (if (string/ends-with? version "-DIRTY")
           (throw (ex-info (str "Refusing to deploy DIRTY version of " (:name pkg)) {:package (:name pkg) :version version}))
           (do
             (println "Deploying" (:name pkg) "version:" version)
             (util/deploy p)
             {:package (name (:name pkg)) :version version}))))))

(defn release-all [packages]
  (let [repo-root (dev-util/get-gcp-repo-root)]
    (println "Starting release orchestration for:" (map :name packages))
    
    ;; 1. Check if the repo root is on main and clean (ignoring dev/state/)
    (if (not= "main" (git/current-branch repo-root))
       (throw (ex-info "Must be on main branch to release." {})))
    
    (let [status-out (or (try (git/run-git repo-root "status" "--porcelain") (catch Exception e "")) "")]
      (let [lines (remove string/blank? (string/split-lines status-out))
            ;; Filter out any lines that are in dev/state/
            relevant-lines (remove #(string/includes? % " dev/state/") lines)]
         (when (seq relevant-lines)
            (throw (ex-info "Working tree is dirty. Commit your changes before releasing." {:status relevant-lines})))))

    ;; 2. Deploy global first
    (let [global-info (deploy-global)
          deployed-info (atom [global-info])]
      
      ;; 3. Deploy requested wrapper packages
      (doseq [pkg packages]
         (swap! deployed-info conj (deploy-wrapper pkg)))
      
      ;; 4. Commit dev/state/
      (let [msg-parts (map (fn [{:keys [package version]}] (str package "-" version)) @deployed-info)
            commit-msg (str "deploy " (string/join ", " msg-parts))]
        (println "Committing state files with message:" commit-msg)
        (git/commit-states repo-root commit-msg)
        
        ;; 5. Tag repository
        (doseq [{:keys [package version]} @deployed-info]
          (let [tag-name (str package "-" version)]
            (println "Creating tag:" tag-name)
            (git/tag repo-root tag-name)))
        
        ;; 6. Push to origin
        (println "Pushing branch and tags to origin...")
        (git/push-release repo-root)
        
        (println "Release complete!")))))
