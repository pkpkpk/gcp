(ns gcp.build.global
  (:require [clojure.java.io :as io]
            [clojure.tools.build.api :as b]
            [gcp.build.util :as util]
            [gcp.dev.packages.definitions :as defs]))

(def lib (:lib defs/global))
(def package-root (:package-root defs/global))
(def src-root (:src-root defs/global))
(def state-file (io/file (:state-root defs/global) "deployment.edn"))

(defn current-state []
  (if (.exists state-file)
    (read-string (slurp state-file))
    {}))

(defn- current-hash []
  (util/hash-dir [src-root (io/file package-root "deps.edn")]))

(defn needs-deploy? []
  (let [state (current-state)
        new-hash (current-hash)]
    (not= (:hash state) new-hash)))

(defn- next-version []
  (str (.format (java.time.LocalDateTime/now)
                (java.time.format.DateTimeFormatter/ofPattern "yyyy.MM.dd.HHmmss"))))

(defn pom [version]
  (let [basis (b/create-basis {:project (.getPath (io/file package-root "deps.edn"))})]
    {:src-dirs [(.getPath src-root)]
     :lib      lib
     :version  version
     :basis    basis
     :pom-data (util/pom-template {:version     version
                                   :description (:description defs/global)
                                   :url         "https://github.com/pkpkpk/gcp"})}))

(defn build []
  (let [new-hash (current-hash)
        state (current-state)]
    (if (needs-deploy?)
      (let [version (next-version)
            p (pom version)]
        (println "Building global package version:" version)
        (util/jar p)
        (util/install-local p)
        (io/make-parents state-file)
        (spit state-file (pr-str {:hash    new-hash
                                  :version version}))
        version)
      (do
        (println "Global package up to date:" (:version state))
        (:version state)))))

(comment
  (current-hash)
  (needs-deploy?)
  (build)
  )
