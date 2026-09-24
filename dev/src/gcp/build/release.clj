(ns gcp.build.release
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [gcp.build.core :as core]
   [gcp.build.global :as global]
   [gcp.build.util :as util]
   [gcp.dev.packages.definitions :as defs]
   [gcp.dev.packages.git :as git]
   [gcp.dev.util :as dev-util]))

(defn deploy-global [snapshot?]
  (let [repo-root (dev-util/get-gcp-repo-root)
        package-root (:package-root defs/global)
        rel-path (dev-util/relative-path repo-root package-root)]
    (if (git/dirty? repo-root rel-path)
      (throw (ex-info "Cannot deploy global: directory is dirty or not on main branch." {:package "global"}))
      (let [needs-deploy? (global/needs-deploy?)
            published-version (global/build snapshot?)
            version (string/replace published-version #"-SNAPSHOT$" "")
            p (global/pom published-version)]
        (if (or needs-deploy? snapshot?)
          (do
            (println "Deploying global version:" published-version)
            (util/deploy p))
          (println "Global package up to date, skipping Clojars deploy:" published-version))
        {:package "gcp.global"
         :version version
         :published-version published-version
         :deployed? needs-deploy?}))))

(defn deploy-wrapper [pkg global-version snapshot?]
  (let [repo-root (dev-util/get-gcp-repo-root)
        package-root (:package-root pkg)
        rel-path (dev-util/relative-path repo-root package-root)]
    (if (git/dirty? repo-root rel-path)
      (throw (ex-info (str "Cannot deploy " (:name pkg) ": directory is dirty or not on main branch.") {:package (:name pkg)}))
      (let [deps-file (io/file package-root "deps.edn")
            deps-map (read-string (slurp deps-file))
            sdk-dep (symbol (str (:googleapis/mvn-org pkg) "/" (:googleapis/mvn-artifact pkg)))
            sdk-version (get-in deps-map [:deps sdk-dep :mvn/version])
            pkg-hash (core/current-hash pkg deps-map)
            state (core/current-state pkg)
            version-info (core/determine-version state sdk-version pkg-hash global-version false)
            needs-deploy? (:needs-deploy? version-info)
            published-version (core/build-package pkg global-version snapshot?)
            version (string/replace published-version #"-SNAPSHOT$" "")
            p (core/pom pkg published-version global-version deps-map)]
        (cond
          (string/ends-with? version "-DIRTY")
          (throw (ex-info (str "Refusing to deploy DIRTY version of " (:name pkg)) {:package (:name pkg) :version published-version}))

          (or needs-deploy? snapshot?)
          (do
            (println "Deploying" (:name pkg) "version:" published-version)
            (util/deploy p))

          :else
          (println "Package" (:name pkg) "up to date, skipping Clojars deploy:" published-version))
        {:package (name (:name pkg))
         :version version
         :published-version published-version
         :deployed? needs-deploy?}))))

(defn release-many
  [packages & {:keys [snapshot?]}]
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
    (let [global-info (deploy-global snapshot?)
          published-global-version (:published-version global-info)
          initial-info (if (:deployed? global-info) [global-info] [])
          deployed-info (atom initial-info)]
      ;; 3. Deploy requested wrapper packages
      (doseq [pkg packages]
        (let [info (deploy-wrapper pkg published-global-version snapshot?)]
          (when (:deployed? info)
            (swap! deployed-info conj info))))
      (if (empty? @deployed-info)
        (println "Nothing new to deploy. All packages are up to date.")
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
          (println "Release complete!"))))))
