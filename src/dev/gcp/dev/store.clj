(ns gcp.dev.store
  (:require [clojure.java.io :as io]
            [clj-http.client :as http]
            [clojure.string :as string]
            [gcp.dev.util :as util]
            [gcp.vertexai.generativeai :as genai]

            [konserve.core :as k]
            [konserve.filestore :as fs]
            [taoensso.telemere :as tt]))

(def root (io/file (System/getProperty "user.home") "pkpkpk" "gcp" ".konserve"))

(defn evict! [store-name]
  (let [dir (io/file root store-name)]
    (doseq [file (.listFiles dir)]
      (io/delete-file file false))))

(defn list-stores [] (map #(.getName %) (.listFiles root)))

(defonce connect
  (memoize
    (fn [store-name]
      (let [path (.getPath (io/file root store-name))]
        (fs/connect-fs-store path {:opts {:sync? true}})))))

(defn get-bytes-aside [store-name ^String url]
  (or (k/get (connect store-name) url nil {:sync? true})
      (let [bs (util/get-url-bytes url)]
        (k/assoc (connect store-name) url bs {:sync? true})
        bs)))

(defn put! [store-name key item]
  (k/assoc (connect store-name) key item {:sync? true}))

(defn get-java-ref-aside [store-name ^String url]
  (or (k/get (connect store-name) url nil {:sync? true})
      (let [bs (util/get-java-ref url)]
        (k/assoc (connect store-name) url bs {:sync? true})
        bs)))

(defn extract-java-ref-aside
  ([store-name model-cfg url]
   (extract-java-ref-aside store-name model-cfg url identity))
  ([store-name model-cfg url validator!]
   (assert (string? store-name))
   (let [key [model-cfg url]]
     (or (k/get (connect store-name) key nil {:sync? true})
         (let [url-bytes (get-java-ref-aside store-name url)
               response (genai/generate-content model-cfg [{:mimeType "text/html" :partData url-bytes}])
               edn (genai/response-json response)]
           (validator! edn)
           (k/assoc (connect store-name) key edn {:sync? true})
           edn)))))
