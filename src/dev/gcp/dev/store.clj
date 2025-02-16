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

(defonce connect
         (memoize
           (fn [store-name]
             (let [path (.getPath (io/file root store-name))]
               (fs/connect-fs-store path {:opts {:sync? true}})))))

(defn evict! [store-name]
  (let [dir (io/file root store-name)]
    (doseq [file (.listFiles dir)]
      (io/delete-file file false))))

(defn list-stores [] (map #(.getName %) (.listFiles root)))

(defn list-keys [store]
  (k/keys (connect store) {:sync? true}))

(defn delete-store [store]
  (evict! store)
  (io/delete-file (io/file root store)))

;; TODO these need versioned store
(defn get-url-bytes-aside [store-name ^String url]
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

;;
;; TODO serializable/haschable malli validators, store w/value in meta?
;;

(defn generate-content-aside
  ([store-name model-cfg content validator!]
   (assert (fn? validator!))
   (let [key [model-cfg (str (hasch.core/uuid content))]]
     (or (k/get (connect store-name) key nil {:sync? true})
         (let [response (genai/generate-content model-cfg content)
               edn (genai/response-json response)]
           (validator! edn)
           (k/assoc (connect store-name) key edn {:sync? true})
           edn)))))
