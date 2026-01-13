(ns gcp.dev.packages.maven
  (:require
   [clojure.string :as string]
   [clojure.tools.deps.util.maven :as mvn])
  (:import
   (org.eclipse.aether.artifact DefaultArtifact)
   (org.eclipse.aether.resolution VersionRangeRequest)))

(defn- version-range-request
  [group artifact]
  (let [system (mvn/make-system)
        session (mvn/make-session system (mvn/get-settings) @mvn/cached-local-repo)
        repos (mvn/remote-repos mvn/standard-repos)
        artifact (DefaultArtifact. group artifact "jar" "[0,)")
        req (doto (VersionRangeRequest.)
              (.setArtifact artifact)
              (.setRepositories repos))]
    {:system system
     :session session
     :request req}))

(defn- resolve-versions
  [group artifact]
  (let [{:keys [system session request]} (version-range-request group artifact)
        result (.resolveVersionRange system session request)]
    (mapv #(.toString %) (.getVersions result))))

(defn- stable? [v]
  (not (re-find #"(?i)(snapshot|rc|beta|alpha|milestone)" v)))

(defn latest-release
  "Returns the latest stable release version string for the given group and artifact."
  [group artifact]
  (->> (resolve-versions group artifact)
       (filter stable?)
       last))

(defn latest-candidate
  "Returns the latest snapshot or release candidate version string for the given group and artifact."
  [group artifact]
  (->> (resolve-versions group artifact)
       (remove stable?)
       last))
