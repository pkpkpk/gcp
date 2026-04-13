(ns gcp.build.core
  "
  ======================================================================
  VERSIONING SCHEMA & DEPLOYMENT LIFECYCLE
  ======================================================================
  
  This namespace coordinates the building of wrapper packages (like bigquery 
  and storage) while decoupling them from the internal `global` package 
  versioning.

  We adopt a clean, independent schema mapping to the Clojure ecosystem 
  standard for wrapper libraries:
  
    <sdk-version>.<wrapper-revision> (e.g., 2.62.0.0, 2.62.0.1)

  State Management:
  Each package maintains its own `deployment.edn` state file, storing:
    1. The Hash of its `src/` directory and `deps.edn`.
    2. The SDK Version it was last built against (e.g., \"2.62.0\").
    3. The Revision Integer (e.g., 0, 1, 2).

  Lifecycle & Bumping Strategy:
  When `build` is called on a package:
    1. Check Git State: We first check if the package directory is dirty
       (has uncommitted changes).
       - If DIRTY: We build a transient `<sdk-version>.<revision>-DIRTY` 
         version. We DO NOT increment the official revision or update 
         `deployment.edn`. This prevents polluting the state with uncommitted 
         experiments.
       - If CLEAN: We proceed to hash and evaluate.
    2. Detect Changes: Hash the package's `src/` and `deps.edn`. Extract 
       the current underlying `sdk-version` from its `deps.edn`.
    3. Evaluate State:
       - No changes: If the hash matches the persisted state, `needs-deploy?` 
         is false. We do nothing and return the existing version.
       - SDK Bumped: If the `sdk-version` has changed (e.g., upgraded from 
         2.62.0 to 2.63.0), we trigger a build, set the revision to 0, 
         and the new artifact becomes `2.63.0.0`.
       - Wrapper Updated: If the `sdk-version` remains the same but the 
         hash differs (e.g., custom helper added), we trigger a build, 
         increment the existing revision by 1, and the artifact becomes 
         `2.62.0.1`.
         
  This provides zero noise for consumers, allows independent releases of 
  sibling packages, and preserves traceability to the underlying Google SDK.
  ======================================================================
  "
  (:require [clojure.java.io :as io]
            [clojure.tools.build.api :as b]
            [gcp.build.global :as global]
            [gcp.build.util :as util]
            [gcp.dev.packages.definitions :as defs]
            [gcp.dev.packages.git :as git]
            [gcp.dev.util :as dev-util]))

(defn package-state-file [pkg]
  (io/file (:state-root pkg) "deployment.edn"))

(defn current-state [pkg]
  (let [f (package-state-file pkg)]
    (if (.exists f)
      (read-string (slurp f))
      {:revision 0 :hash nil :sdk-version nil})))

(defn current-hash [pkg deps-map]
  (let [root (:package-root pkg)
        paths (map #(io/file root %) (:paths deps-map))
        deps-file (io/file root "deps.edn")]
    (util/hash-dir (conj paths deps-file))))

(defn determine-version [state current-sdk-version current-hash is-dirty?]
  (let [revision (or (:revision state) 0)]
    (if is-dirty?
      {:needs-deploy? true
       :dirty? true
       :version (str current-sdk-version "." revision "-DIRTY")}
      (if (= (:hash state) current-hash)
        {:needs-deploy? false
         :dirty? false
         :version (str current-sdk-version "." revision)}
        (let [new-revision (if (= (:sdk-version state) current-sdk-version)
                             (inc revision)
                             0)]
          {:needs-deploy? true
           :dirty? false
           :revision new-revision
           :version (str current-sdk-version "." new-revision)})))))

(defn pom [pkg version global-version deps-map]
  (let [package-root (:package-root pkg)
        project-deps (-> (:deps deps-map)
                         (dissoc 'gcp/global)
                         (assoc (:lib defs/global) {:mvn/version global-version}))
        project (assoc deps-map :deps project-deps)
        basis (b/create-basis {:project project :dir (.getPath package-root)})
        src-dirs (mapv #(.getPath (io/file package-root %)) (:paths project))
        target-root (.getPath (io/file package-root "target"))]
    {:src-dirs    src-dirs
     :lib         (:lib pkg)
     :version     version
     :basis       basis
     :target-root target-root
     :pom-data    (util/pom-template {:version     version
                                      :description (:description pkg)
                                      :url         "https://github.com/pkpkpk/gcp"})}))

(defn build-package [pkg]
  (let [global-version (global/build)
        package-root (:package-root pkg)
        deps-file (io/file package-root "deps.edn")
        deps-map (read-string (slurp deps-file))
        sdk-dep (symbol (str (:googleapis/mvn-org pkg) "/" (:googleapis/mvn-artifact pkg)))
        sdk-version (get-in deps-map [:deps sdk-dep :mvn/version])
        
        repo-root (dev-util/get-gcp-repo-root)
        rel-path (dev-util/relative-path repo-root package-root)
        is-dirty? (git/dirty? repo-root rel-path)
        
        pkg-hash (current-hash pkg deps-map)
        state (current-state pkg)
        {:keys [needs-deploy? dirty? version revision]} (determine-version state sdk-version pkg-hash is-dirty?)]
    
    (if-not needs-deploy?
      (do
        (println "Package" (:name pkg) "up to date:" version)
        version)
      (let [p (pom pkg version global-version deps-map)]
        (if dirty?
          (println "Building DIRTY package version:" version "(state will not be updated)")
          (println "Building" (:name pkg) "package version:" version))
        
        (util/jar p)
        (util/install-local p)
        
        (when-not dirty?
          (let [f (package-state-file pkg)]
            (io/make-parents f)
            (spit f (pr-str {:hash pkg-hash
                             :sdk-version sdk-version
                             :revision revision}))))
        version))))
