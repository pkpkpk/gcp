(ns gcp.build.vertexai
  (:require [clojure.java.io :as io]
            [clojure.tools.build.api :as b]
            [gcp.build.global :as global]
            [gcp.build.util :as util :refer [jar deploy]]))

(def lib 'com.github.pkpkpk/gcp.vertexai)
(def vertexai-version "1.18.0")
(def src-root (io/file util/package-root "vertexai" "src"))

(defn pom []
  (let [version (str vertexai-version "-"
                     (.format (java.time.LocalDate/now)
                              (java.time.format.DateTimeFormatter/ofPattern "yyyy-MM-dd"))
                     "-SNAPSHOT")

        project {:paths [(.getPath src-root)]
                 :deps  {global/lib                              {:mvn/version global/version}
                         'com.google.cloud/google-cloud-vertexai {:mvn/version vertexai-version}}}
        basis (b/create-basis {:project project})]
    {:src-dirs [(.getPath src-root)]
     :lib      lib
     :version  version
     :basis    basis
     :pom-data (util/pom-template {:version     version
                                   :description "edn bindings for the google-cloud-vertexai sdk"
                                   :url         "https://github.com/pkpkpk/gcp/gcp/vertexai"})}))

(comment
  (do (require :reload 'gcp.build.vertexai) (in-ns 'gcp.build.vertexai))
  (jar (pom))
  (deploy (pom))
  )