(ns gcp.build
  (:require [clojure.repl :refer :all]
            [clojure.tools.build.api :as b]
            [deps-deploy.deps-deploy :as dd]))

(def lib 'com.github.pkpkpk/gcp.global)

(def version (format "1.0.%s" (b/git-count-revs nil)))

(def basis (b/create-basis {:project "deps.edn"}))
(def jar-file (format "target/%s-%s.jar" (name lib) version))
(def class-dir "target/classes")

(defn clean [] (b/delete {:path "target"}))

(defn- pom-template [version]
  [[:description "gcp global malli registry"]
   [:url "https://github.com/pkpkpk/gcp/gcp/global"]
   [:licenses
    [:license
     [:name "MIT"]
     [:url "https://mit-license.org"]]]
   [:scm
    [:url "https://github.com/pkpkpk/gcp"]
    [:connection "scm:git:https://github.com/pkpkpk/gcp.git"]
    [:developerConnection "scm:git:ssh:git@github.com:pkpkpk/gcp.git"]
    [:tag (str "v" version)]]])

(def pom {:src-dirs  ["src"]
          :class-dir class-dir
          :lib       lib
          :version   version
          :basis     basis
          :pom-data (pom-template version)})

(defn jar [& _args]
  (b/write-pom pom)
  (b/copy-dir {:src-dirs  ["src"]
               :target-dir class-dir})
  (b/jar {:class-dir class-dir
          :jar-file jar-file}))

(defn deploy [& _args]
  (assert (some? (System/getenv "CLOJARS_USERNAME")))
  (assert (some? (System/getenv "CLOJARS_PASSWORD")))
  (dd/deploy {:artifact jar-file
              :installer :remote
              :pom-file (b/pom-path pom)}))
