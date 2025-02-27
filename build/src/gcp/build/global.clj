(ns gcp.build.global
  (:require [clojure.java.io :as io]
            [clojure.repl :refer :all]
            [clojure.tools.build.api :as b]
            [gcp.build.util :as util :refer [jar deploy]]))

(def lib 'com.github.pkpkpk/gcp.global)
;(def version (format "1.0.%s" (b/git-count-revs nil)))
(def version "1.0.0-SNAPSHOT")
(def package-root (io/file util/package-root "global"))
(def src-root (io/file util/package-root  "global" "src"))

(defn pom []
  (let [basis (b/create-basis {:project (.getPath (io/file package-root "deps.edn"))})]
    {:src-dirs [(.getPath src-root)]
     :lib      lib
     :version  version
     :basis    basis
     :pom-data (util/pom-template {:version     version
                                   :description "gcp global malli registry"
                                   :url         "https://github.com/pkpkpk/gcp/gcp/global"})}))

(comment
  (jar (pom))
  (deploy (pom))
  )


