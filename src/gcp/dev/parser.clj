(ns gcp.dev.parser
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

#_ (do (require :reload 'gcp.dev.parser) (in-ns 'gcp.dev.parser))

;https://github.com/googleapis/java-bigquery
(def home (System/getProperty "user.home"))
(def googleapis (io/file home "googleapis"))
(def bigquery-repo (io/file googleapis "java-bigquery"))

(def bigquery-root (io/file bigquery-repo "google-cloud-bigquery" "src" "main" "java" "com" "google" "cloud" "bigquery"))

(defn public-classes [root]
  (let [files (.listFiles root)]
    (into (sorted-set)
          (comp
            (filter #(.isFile %))
            (filter (fn [file] (some #(string/starts-with? % "public") (string/split-lines (slurp file)))))
            (map #(.getName %)))
          files)))
