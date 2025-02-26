(ns gcp.build.vertexai
  (:require [clojure.java.io :as io]
            [clojure.tools.build.api :as b]
            [gcp.build.global :as global]
            [gcp.build.util :as util :refer [jar deploy]]))

(def lib 'com.github.pkpkpk/gcp.vertexai)
(def vertexai-version "1.18.0")
(def version (str vertexai-version "-0.1.0-SNAPSHOT"))
(def src-root (io/file util/package-root "vertexai" "src"))

(def project
  {:paths [(.getPath src-root)]
   :deps {global/lib {:mvn/version global/version}
          'com.google.cloud/google-cloud-vertexai {:mvn/version vertexai-version}}})

(def basis (b/create-basis {:project project}))

(def pom {:src-dirs  [(.getPath src-root)]
          :lib       lib
          :version   version
          :basis     basis
          :pom-data (util/pom-template {:version version
                                        :description "edn bindings for the google-cloud-vertexai sdk"
                                        :url "https://github.com/pkpkpk/gcp/gcp/vertexai"})})


(comment
  (do (require :reload 'gcp.build.vertexai) (in-ns 'gcp.build.vertexai))
  (jar pom)
  (deploy pom)
  )