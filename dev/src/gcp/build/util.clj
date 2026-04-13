(ns gcp.build.util
  (:require
    [clojure.java.io :as io]
    [clojure.tools.build.api :as b]
    [deps-deploy.deps-deploy :as dd])
  (:import (java.security MessageDigest)))

(set! *print-namespace-maps* false)

(defn hash-dir
  "Calculates a hash of the contents of a directory and optional files.
   Uses SHA-256."
  [paths]
  (let [digest (MessageDigest/getInstance "SHA-256")
        files (->> paths
                   (map io/file)
                   (mapcat #(if (.isDirectory %) (file-seq %) [%]))
                   (filter #(.isFile %))
                   (sort-by #(.getPath %)))]
    (doseq [^java.io.File f files]
      (.update digest (.getBytes (.getPath f)))
      (.update digest (java.nio.file.Files/readAllBytes (.toPath f))))
    (->> (.digest digest)
         (map #(format "%02x" %))
         (apply str))))

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
  [{:keys [src-dirs target-root lib version]
    :or {target-root "target"}
    :as pom}]
  (let [class-dir (str target-root "/classes")
        jar-file  (format "%s/%s-%s.jar" target-root (name lib) version)
        pom       (assoc pom :class-dir class-dir)]
    (b/delete {:path target-root})
    (b/write-pom pom)
    (b/copy-dir {:src-dirs   src-dirs
                 :target-dir class-dir})
    (b/jar {:class-dir class-dir
            :jar-file  jar-file})))

(defn install-local
  [{:keys [lib version target-root]
    :or {target-root "target"}
    :as pom}]
  (let [class-dir (str target-root "/classes")
        jar-file  (format "%s/%s-%s.jar" target-root (name lib) version)
        pom-file  (b/pom-path (assoc pom :class-dir class-dir))]
    (b/install {:basis     (:basis pom)
                :lib       lib
                :class-dir class-dir
                :version   version
                :jar-file  jar-file
                :pom-file  pom-file})))

(defn deploy
  [{:keys [lib version target-root]
    :or {target-root "target"}
    :as pom}]
  (assert (some? (System/getenv "CLOJARS_USERNAME")))
  (assert (some? (System/getenv "CLOJARS_PASSWORD")))
  (let [class-dir (str target-root "/classes")
        jar-file  (format "%s/%s-%s.jar" target-root (name lib) version)]
    (assert (.exists (io/file jar-file)))
    (dd/deploy {:artifact  jar-file
                :installer :remote
                :pom-file  (b/pom-path (assoc pom :class-dir class-dir))})))

(comment
  (defn pom [{:keys [description lib sdk-version]}]
    (let [version (str sdk-version "-"
                       (.format (java.time.LocalDate/now)
                                (java.time.format.DateTimeFormatter/ofPattern "yyyy-MM-dd"))
                       "-SNAPSHOT")
          project {:paths [(.getPath src-root)]
                   :deps {global/lib {:mvn/version global/version}
                          'com.google.cloud/google-cloud-bigquery {:mvn/version bigquery-version}}}
          basis (b/create-basis {:project project})]
      {:src-dirs [(.getPath src-root)]
       :lib      lib
       :version  version
       :basis    basis
       :pom-data (util/pom-template {:version     version
                                     :description description
                                     :url         "https://github.com/pkpkpk/gcp"})}))

  (jar (pom))
  (deploy (pom)))
