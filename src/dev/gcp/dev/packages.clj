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
                                                                             :example "2.47.0"}
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
     (extract/extract-from-bytes cfg (util/get-url-bytes package-url)))))

(defn bigquery []
  (let [latest ($package-summary "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery")]
    (if (not= (:version latest) (.getLibraryVersion (BigQueryOptions/getDefaultInstance)))
      (throw (Exception. "new bigquery version!"))
      (let [bq (assoc latest :packageRootUrl "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/"
                             :overviewUrl    "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery"
                             :root           (io/file util/src "main" "gcp" "bigquery" "v2")
                             :rootNs         'gcp.bigquery.v2
                             :packageName    "bigquery"
                             :packageSymbol  'com.google.cloud.bigquery
                             :store          (str "bigquery_" (:version latest)))
            discovery          (j/read-value (store/get-bytes-aside "discovery" "https://bigquery.googleapis.com/discovery/v1/apis/bigquery/v2/rest") j/keyword-keys-object-mapper)
            class-keys         (into (sorted-set)
                                     (comp (map util/class-parts)
                                           (map first)
                                           (map keyword))
                                     (:classes bq))
            schema-keys        (set (keys (get-in discovery [:schemas])))
            class-intersection (clojure.set/intersection schema-keys class-keys)
            refless-classes    (into (sorted-map)
                                     (map
                                       (fn [key]
                                         (let [schema (get-in discovery [:schemas key])]
                                           (when-not (some some? (map :$ref (vals (get schema :properties))))
                                             [key schema]))))
                                     class-intersection)
            {builders true classes false} (group-by #(string/ends-with? % "Builder") (:classes bq))
            {cgc-enums true classes false} (group-by #(contains? (:bases (clojure.reflect/reflect (util/as-class %))) 'com.google.cloud.StringEnumValue) classes)]
        (assoc bq :classes classes
                  :enums (into (:enums bq) cgc-enums)
                  :builders builders
                  :classes-refless refless-classes
                  :discovery discovery)))))