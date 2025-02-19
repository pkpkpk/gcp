(ns gcp.dev.packages.bigquery
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [gcp.dev.store :as store]
            [gcp.dev.util :refer :all]
            [gcp.dev.packages :refer [$package-summary-memo packages-root googleapis-root]]
            [jsonista.core :as j])
  (:import (com.google.cloud.bigquery BigQueryOptions)))

;:~/googleapis/java-bigquery$ git worktree add ../bigquery-2.48.0 v2.48.0

(defn bigquery []
  (let [{:keys [version classes] :as latest} ($package-summary-memo "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery")]
    (if (not= version (.getLibraryVersion (BigQueryOptions/getDefaultInstance)))
      (throw (ex-info "new bigquery version!" {:current (.getLibraryVersion (BigQueryOptions/getDefaultInstance))
                                               :extracted version}))
      (let [bigquery-repo (io/file googleapis-root (str "bigquery-" version))
            repo-root     (io/file bigquery-repo "google-cloud-bigquery" "src" "main" "java" "com" "google" "cloud" "bigquery")
            target-root   (io/file packages-root "bigquery" "src" "gcp" "bigquery" "v2")

            discovery   (j/read-value (store/get-url-bytes-aside "discovery" "https://bigquery.googleapis.com/discovery/v1/apis/bigquery/v2/rest") j/keyword-keys-object-mapper)

            enums (into (apply sorted-set (:enums latest))
                        (filter #(contains? (:bases (reflect %)) 'com.google.cloud.StringEnumValue))
                        classes)

            classes    (into (sorted-set) (remove enums) classes)
            builders   (into (sorted-set) (filter #(string/ends-with? % "Builder")) classes)

            abstract-unions (reduce
                              (fn [acc className]
                                (if (and (abstract-class? className)
                                         ;; getType() not always an abstract method !
                                         (some #(= 'getType (:name %)) (instance-methods className)))
                                  (if-let [variants (not-empty (into (sorted-set) (filter (partial variant? className)) classes))]
                                    (assoc acc className variants)
                                    acc)
                                  acc))
                              (sorted-map)
                              classes)
            abstract-variants (reduce (fn [acc [k vs]] (into acc (map (fn [v] [v k])) vs)) (sorted-map) abstract-unions)

            concrete-unions (reduce
                              (fn [acc className]
                                (if (and (not (abstract-class? className))
                                         (some #(= 'getType (:name %)) (instance-methods className)))
                                  (if-let [variants (not-empty (into (sorted-set) (filter (partial variant? className)) classes))]
                                    (assoc acc className variants)
                                    acc)
                                  acc))
                              (sorted-map)
                              classes)
            concrete-variants (reduce (fn [acc [k vs]] (into acc (map (fn [v] [v k])) vs)) (sorted-map) concrete-unions)

            service-objects (reduce
                              (fn [acc className]
                                (let [ms (instance-methods className)]
                                  (if (some #(= 'getBigQuery (:name %)) ms)
                                    (conj acc className (str className ".Builder"))
                                    acc)))
                              (sorted-set)
                              classes)
            options (into (sorted-set) (filter #(string/ends-with? % "Option")) classes)

            classes (into (sorted-set)
                          (remove (clojure.set/union options
                                                     service-objects
                                                     (set (keys concrete-unions))
                                                     (set (keys abstract-unions)))) classes)

            by-base     (into (sorted-map) (group-by base-part classes))
            standalone  (reduce (fn [acc [k v]] (if (= [k] v) (conj acc k) acc)) (sorted-set) by-base)
            static-factories (into (sorted-set)
                                   (comp
                                     (remove #(string/ends-with? % "Builder"))
                                     (filter
                                       (fn [className]
                                         (or
                                           (and (contains? standalone className)
                                                (some #(= 'of (:name %)) (static-factory-methods className)))
                                           (and (not (contains? by-base className))
                                                (some #(= 'of (:name %)) (static-factory-methods className)))))))
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
                        classes)
            read-only (into (sorted-set)
                            (filter
                              (fn [className]
                                (and
                                  (not (contains? (:types/builders bigquery) className))
                                  (empty? (public-instantiators className)))))
                            (:classes latest))]
        {:packageRootUrl          "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/"
         :overviewUrl             "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery"
         :repoRoot                repo-root
         :targetRoot              target-root
         :rootNs                  'gcp.bigquery.v2
         :packageName             "bigquery"
         :packageSymbol           'com.google.cloud.bigquery
         :store                   (str "bigquery_" (:version latest))
         :discovery               discovery
         :package/version         (:version latest)
         :types/all               (clojure.set/union (into (sorted-set) (:classes latest)) enums)
         :types/enums             enums
         :types/builders          builders
         :types/settings          (into (sorted-set) (:settings latest))
         :types/interfaces        (into (sorted-set) (:interfaces latest))
         :types/exceptions        (into (sorted-set) (:exceptions latest))
         :types/abstract-unions   abstract-unions
         :types/abstract-variants abstract-variants
         :types/concrete-unions   concrete-unions
         :types/concrete-variants concrete-variants
         :types/variants          (merge abstract-variants concrete-variants)
         :types/unions            (merge abstract-unions concrete-unions)
         :types/service-objects   service-objects
         :types/bigquery-options  options
         :types/static-factories  static-factories
         :types/accessors         accessors
         :types/nested            nested
         :types/by-base           by-base
         :types/standalone        standalone
         :types/read-only         read-only}))))

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

