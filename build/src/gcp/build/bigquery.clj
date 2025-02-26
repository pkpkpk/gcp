(ns gcp.build.bigquery
  (:require [clojure.java.io :as io]
            [clojure.tools.build.api :as b]
            [gcp.build.global :as global]
            [gcp.build.util :as util :refer [jar deploy]]))

(def lib 'com.github.pkpkpk/gcp.bigquery)
(def bigquery-version "2.47.0")
(def version (str bigquery-version "-0.1.0-SNAPSHOT"))
(def src-root (io/file util/package-root "bigquery" "src"))

(def project
  {:paths [(.getPath src-root)]
   :deps {global/lib {:mvn/version global/version}
          'com.google.cloud/google-cloud-bigquery {:mvn/version bigquery-version}}})

(def basis (b/create-basis {:project project}))

(def pom {:src-dirs  [(.getPath src-root)]
          :lib       lib
          :version   version
          :basis     basis
          :pom-data (util/pom-template {:version version
                                        :description "edn bindings for the google-cloud-bigquery sdk"
                                        :url "https://github.com/pkpkpk/gcp/gcp/bigquery"})})

(comment
  (do (require :reload 'gcp.build.bigquery) (in-ns 'gcp.build.bigquery))
  (jar pom)
  (deploy pom)
  )