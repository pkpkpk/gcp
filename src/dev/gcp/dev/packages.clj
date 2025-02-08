(ns gcp.dev.packages
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [gcp.dev.extract :as extract]
            [gcp.dev.models :as models]
            [gcp.dev.store :as store]
            [gcp.dev.util :as util]
            [jsonista.core :as j])
  (:import (com.google.cloud.bigquery BigQueryOptions)))

(defn $package-summary
  ([package-url]
   (let [cfg {:model             (:model models/flash)
              :systemInstruction (str "You are given google cloud java package summary page" "Extract its parts into arrays.")
              :generationConfig  {:responseMimeType "application/json"
                                  :responseSchema   {:type       "OBJECT"
                                                     ;; TODO settings clients etc
                                                     :required   ["version" "classes" "enums" "exceptions" "interfaces"]
                                                     :properties {"version" {:type    "STRING"
                                                                             :example "2.47.0"
                                                                             :pattern "^[0-9]+\\.[0-9]+\\.[0-9]+$"}
                                                                  "settings"
                                                                  {:type  "ARRAY"
                                                                   :items {:type        "STRING"
                                                                           :description "package qualified settings class name"}}
                                                                  "classes"
                                                                  {:type  "ARRAY"
                                                                   :items {:type        "STRING"
                                                                           :description "package qualified class name"}}
                                                                  "enums"
                                                                  {:type  "ARRAY"
                                                                   :items {:type        "STRING"
                                                                           :description "package qualified enum name"}}
                                                                  "exceptions"
                                                                  {:type  "ARRAY"
                                                                   :items {:type        "STRING"
                                                                           :description "package qualified exception name"}}
                                                                  "interfaces"
                                                                  {:type  "ARRAY"
                                                                   :items {:type        "STRING"
                                                                           :description "package qualified interface name"}}}}}}]
     ;; never cache package summary bytes, always get latest
     (extract/extract-from-bytes cfg (util/get-url-bytes package-url)))))

(def package-schema
  [:map
   [:name :string]
   ;TODO settings & clients
   [:classes    [:seqable :string]]
   [:enums      [:seqable :string]]
   [:exceptions [:seqable :string]]
   [:interfaces [:seqable :string]]])

(defn bigquery []
  (let [{:keys [version classes] :as latest} ($package-summary "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery")]
    (if (not= version (.getLibraryVersion (BigQueryOptions/getDefaultInstance)))
      (throw (Exception. "new bigquery version!"))
      (let [discovery          (j/read-value (store/get-url-bytes-aside "discovery" "https://bigquery.googleapis.com/discovery/v1/apis/bigquery/v2/rest") j/keyword-keys-object-mapper)
            class-keys         (into (sorted-set)
                                     (comp (map util/class-parts)
                                           (map first)
                                           (map keyword))
                                     (:classes latest))
            schema-keys        (set (keys (get-in discovery [:schemas])))
            class-intersection (clojure.set/intersection schema-keys class-keys)
            refless-classes    (into (sorted-map)
                                     (map
                                       (fn [key]
                                         (let [schema (get-in discovery [:schemas key])]
                                           (when-not (some some? (map :$ref (vals (get schema :properties))))
                                             [key schema]))))
                                     class-intersection)
            {builders true classes false} (group-by #(string/ends-with? % "Builder") classes)
            {cgc-enums true classes false} (group-by #(contains? (:bases (clojure.reflect/reflect (util/as-class %))) 'com.google.cloud.StringEnumValue) classes)]
        (assoc latest
          :packageRootUrl "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/"
          :overviewUrl "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery"
          :root (io/file util/src "main" "gcp" "bigquery" "v2")
          :rootNs 'gcp.bigquery.v2
          :packageName "bigquery"
          :packageSymbol 'com.google.cloud.bigquery
          :store (str "bigquery_" (:version latest))
          :classes classes
          :enums (into (:enums latest) cgc-enums)
          :builders builders
          :classes-refless refless-classes
          :discovery discovery)))))

;; fetch-all-urls
;; list-missing-urls
;; list-missing-extractions

;(def pubsub-root (io/file src "main" "gcp" "pubsub"))
;(def pubsub-api-doc-base "https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/")