(ns gcp.vertexai.resources
  (:require [clojure.string :as string]))

;; TODO buckets, artifacts, BQ, firestore etc...

(def datastore-resource-id-schema
  [:and
   :string
   [:fn
    {:error/message "datastore-resource-id must conform to format 'projects/{project}/locations/{location}/collections/{collection}/dataStores/{dataStore}'"}
    (fn [s]
      (let [parts (string/split s #"/")]
        (and
          (= "projects" (nth parts 0))
          (some? (nth parts 1))
          (= "locations" (nth parts 2))
          (some? (nth parts 3))
          (= "collections" (nth parts 4))
          (some? (nth parts 5))
          (= "dataStores" (nth parts 6))
          (some? (nth parts 7)))))]])
