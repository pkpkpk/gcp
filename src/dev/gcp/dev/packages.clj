(ns gcp.dev.packages
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [gcp.dev.extract :as extract]
            [gcp.dev.models :as models]
            [gcp.dev.store :as store]
            [gcp.dev.util :refer :all]
            [jsonista.core :as j])
  (:import (com.google.cloud.bigquery BigQueryOptions)))

(def home   (io/file (System/getProperty "user.home")))
(def root   (io/file home "pkpkpk" "gcp"))
(def src    (io/file root "src"))

(defn $package-summary
  ([package-url]
   (let [cfg {:model             (:model models/flash-2)
              :systemInstruction (str "You are given google cloud java package summary page" "Extract its parts into arrays.")
              :generationConfig  {:responseMimeType "application/json"
                                  :responseSchema   {:type       "OBJECT"
                                                     ;; TODO settings clients etc
                                                     :required   ["version" "classes" "enums" "exceptions" "interfaces"]
                                                     :properties {"version" {:type    "STRING"
                                                                             :example "2.47.0"
                                                                             :description "the semantic version number of the package"
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
     (println  "fetching package summary")
     (extract/extract-from-bytes cfg (get-url-bytes package-url)))))

(defonce $package-summary-memo (memoize $package-summary))

(defn variant? [union class-like]
  (and (contains? (into #{} (map :name) (:members (reflect (as-class union)))) 'getType)
       (contains? (:bases (reflect class-like)) (symbol union))))

(defn bigquery []
  (let [{:keys [version classes] :as latest} ($package-summary-memo "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery")]
    (if (not= version (.getLibraryVersion (BigQueryOptions/getDefaultInstance)))
      (throw (ex-info "new bigquery version!" {:current (.getLibraryVersion (BigQueryOptions/getDefaultInstance))
                                               :extracted version}))
      (let [discovery   (j/read-value (store/get-url-bytes-aside "discovery" "https://bigquery.googleapis.com/discovery/v1/apis/bigquery/v2/rest") j/keyword-keys-object-mapper)

            enums (into (apply sorted-set (:enums latest))
                        (filter #(contains? (:bases (reflect %)) 'com.google.cloud.StringEnumValue))
                        classes)

            classes    (into (sorted-set) (remove enums) classes)
            builders   (into (sorted-set) (filter #(string/ends-with? % "Builder")) classes)

            abstract-unions (reduce
                              (fn [acc className]
                                (let [{:keys [flags]} (reflect className)
                                      members (member-methods className)]
                                  (if (and (contains? flags :abstract)
                                           (some #(= 'getType (:name %)) members))
                                    (if-let [variants (not-empty (into (sorted-set) (filter (partial variant? className)) classes))]
                                      (assoc acc className variants)
                                      acc)
                                    acc)))
                              (sorted-map)
                              classes)
            abstract-variants (reduce (fn [acc [k vs]] (into acc (map (fn [v] [v k])) vs)) (sorted-map) abstract-unions)

            classes (remove (set (keys abstract-unions)) classes)

            concrete-unions (reduce
                              (fn [acc className]
                                (let [{:keys [flags]} (reflect className)
                                      members (member-methods className)]
                                  (if (and (not (contains? flags :abstract))
                                           (some #(= 'getType (:name %)) members))
                                    (if-let [variants (not-empty (into (sorted-set) (filter (partial variant? className)) classes))]
                                      (assoc acc className variants)
                                      acc)
                                    acc)))
                              (sorted-map)
                              classes)
            concrete-variants (reduce (fn [acc [k vs]] (into acc (map (fn [v] [v k])) vs)) (sorted-map) concrete-unions)


            classes (remove (set (keys concrete-unions)) classes)

            service-objects (reduce
                              (fn [acc className]
                                (let [ms (member-methods className)]
                                  (if (some #(= 'getBigQuery (:name %)) ms)
                                    (conj acc className (str className ".Builder"))
                                    acc)))
                              (sorted-set)
                              classes)
            classes (into (sorted-set) (remove service-objects) classes)
            by-base     (into (sorted-map) (group-by base-part classes))
            standalone  (reduce (fn [acc [k v]] (if (= [k] v) (conj acc k) acc)) (sorted-set) by-base)
            static-factories (into (sorted-set)
                                   (comp
                                     (remove #(string/ends-with? % "Builder"))
                                     (filter
                                       (fn [className]
                                         (or
                                           (and (contains? standalone className)
                                                (some #(= 'of (:name %)) (member-methods className)))
                                           (and (not (contains? by-base className))
                                                (some #(= 'of (:name %)) (member-methods className)))))))
                                   classes)
            nested (into (sorted-set)
                         (comp
                           (remove #(string/ends-with? % "Builder"))
                           (filter #(not (contains? by-base %))))
                         classes)
            accessors (reduce
                        (fn [acc className]
                          (if (contains? classes (str className ".Builder"))
                            (conj acc className)
                            acc))
                        (sorted-set)
                        classes)]

        ;; TODO classes without solutions
        ;;  - bigquery.BigQuery.<$>Option
        ;;    --> mostly static methods that wrap a value  ie (<$>Option/pageSize 43) etc
        ;;  - bigquery.Acl.<$>
        ;;    -- normal constructor calls
        ;;    -- bigquery.Acl.Entity is abstract, wraps Acl.Entity.Type ENUM, read-only?
        ;;  - bigquery.JobStatistics.<$>Statistics
        ;;    -- generally read-only, from-edn does not apply here
        ;;  - many others that are also read only ie "com.google.cloud.bigquery.InsertAllResponse"
        ;;    --> are constructors private/protected? might be good way to sort into to-edn-only

        #_(clojure.set/difference
            (:classes bigquery)
            (:types/accessors bigquery)
            (:types/static-factories bigquery)
            (:types/enums bigquery)
            (:types/builders bigquery)
            (:types/service-objects bigquery)
            (set (keys (:types/abstract-unions bigquery)))
            (set (keys (:types/concrete-unions bigquery)))
            (set (keys (:types/abstract-variants bigquery)))
            (set (keys (:types/concrete-variants bigquery))))

        ;;(filter #(empty? (public-constructors %)) misfits) ;=> to-edn only?
        ;;

        {:packageRootUrl "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/"
         :overviewUrl "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery"
         :root (io/file src "main" "gcp" "bigquery" "v2")
         :rootNs 'gcp.bigquery.v2
         :packageName "bigquery"
         :packageSymbol 'com.google.cloud.bigquery
         :store (str "bigquery_" (:version latest))
         :discovery discovery

         #!----------------------------
         :classes (into (sorted-set) (:classes latest))
         #!----------------------------
         :package/version (:version latest)
         :types/enums enums
         :types/builders builders
         :types/settings (into (sorted-set) (:settings latest))
         :types/interfaces (into (sorted-set) (:interfaces latest))
         :types/exceptions (into (sorted-set) (:exceptions latest))
         :types/abstract-unions abstract-unions
         :types/abstract-variants abstract-variants
         :types/concrete-unions concrete-unions
         :types/concrete-variants concrete-variants
         :types/service-objects service-objects
         :types/static-factories static-factories
         :types/accessors accessors
         :types/nested nested
         :types/by-base by-base
         :types/standalone standalone}))))

;(def pubsub-root (io/file src "main" "gcp" "pubsub"))
;(def pubsub-api-doc-base "https://cloud.google.com/java/docs/reference/google-cloud-pubsub/latest/")