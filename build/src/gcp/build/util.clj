(ns gcp.build.util
  (:require [clojure.java.io :as io]
            [clojure.tools.build.api :as b]
            [deps-deploy.deps-deploy :as dd]))

(set! *print-namespace-maps* false)

(def home            (io/file (System/getProperty "user.home")))
(def project-root    (io/file home "pkpkpk" "gcp"))
(def package-root    (io/file project-root "gpc"))

(defn pom-template
  [{:keys [version description url]}]
  [[:description description]
   [:url url]
   [:licenses
    [:license
     [:name "MIT"]
     [:url "https://mit-license.org"]]]
   [:scm
    [:url "https://github.com/pkpkpk/gcp"]
    [:connection "scm:git:https://github.com/pkpkpk/gcp.git"]
    [:developerConnection "scm:git:ssh:git@github.com:pkpkpk/gcp.git"]
    [:tag (str "v" version)]]])

(defn jar
  [{:keys [src-dirs class-dir lib version]
    :or {class-dir "target/classes"}
    :as pom}]
  (let [jar-file (format "target/%s-%s.jar" (name lib) version)
        pom (assoc pom :class-dir class-dir)]
    (b/write-pom pom)
    (b/copy-dir {:src-dirs   src-dirs
                 :target-dir class-dir})
    (b/jar {:class-dir class-dir
            :jar-file  jar-file})))

(defn deploy
  [{:keys [lib version] :as pom}]
  (assert (some? (System/getenv "CLOJARS_USERNAME")))
  (assert (some? (System/getenv "CLOJARS_PASSWORD")))
  (let [jar-file (format "target/%s-%s.jar" (name lib) version)]
    (assert (.exists (io/file jar-file)))
    (dd/deploy {:artifact  jar-file
                :installer :remote
                :pom-file  (b/pom-path (assoc pom :class-dir "target/classes"))})))