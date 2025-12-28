(ns gcp.dev.packages.legacy.bigquery
  (:require
   [clojure.java.io :as io]
   [clojure.set :as s]
   [clojure.string :as string]
   [gcp.dev.packages.legacy.packages :refer [$package-summary-memo googleapis-root packages-root]]
   [gcp.dev.store :as store]
   [gcp.dev.util :refer :all]
   [jsonista.core :as j])
  (:import
   (com.google.cloud.bigquery BigQueryOptions)))

; :~/googleapis/java-bigquery$ git worktree add ../bigquery-2.48.0 v2.48.0

(defn _bigquery []
  (let [{:keys [version classes] :as latest} ($package-summary-memo "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery")]
    (if (not= version (.getLibraryVersion (BigQueryOptions/getDefaultInstance)))
      (throw (ex-info   "new bigquery version!" {:current (.getLibraryVersion (BigQueryOptions/getDefaultInstance))
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
            simple-accessors (into (sorted-set)
                                   (filter #(and (contains? by-base %)
                                                 (= 2 (count (get by-base %)))))
                                   accessors)
            read-only (into (sorted-set)
                            (filter
                              (fn [className]
                                (and
                                  (not (contains? nested className))
                                  (empty? (public-instantiators className)))))
                            (:classes latest))]
        {:packageRootUrl                "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/"
         :overviewUrl                   "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery"
         :repoRoot                      repo-root
         :targetRoot                    target-root
         :rootNs                        'gcp.bigquery.v2
         :packageName                   "bigquery"
         :packageSymbol                 'com.google.cloud.bigquery
         :store                         (str "bigquery_" (:version latest))
         :discovery                     discovery
         :package/version               (:version latest)
         #!--------------------------------------------------
         :lookup/union->variant         (merge abstract-unions concrete-unions)
         :lookup/variant->union         (merge abstract-variants concrete-variants)
         :lookup/by-base                by-base
         #!------------------------------------------------------
         :pred/accessor                 accessors
         :pred/builder                  builders
         :pred/read-only                read-only
         :pred/nested                   nested
         :pred/abstract-variants       (into (sorted-set) (keys abstract-variants))
         :pred/concrete-variants       (into (sorted-set) (keys concrete-variants))
         #!------------------------------------------------------
         :types/settings                (into (sorted-set) (:settings latest))
         :types/interfaces              (into (sorted-set) (:interfaces latest))
         :types/exceptions              (into (sorted-set) (:exceptions latest))
         :types/service-objects         service-objects
         :types/bigquery-options        options
         #!------------------------------------------------------
         :types/enums                   enums
         :types/abstract-unions         (into (sorted-set) (keys abstract-unions))
         :types/concrete-unions         (into (sorted-set) (keys concrete-unions))
         :types/static-factories        (s/difference static-factories nested)
         :types/nested-static-factories (s/intersection nested static-factories)
         :types/simple-accessors        simple-accessors
         :types/complex-accessors       (s/difference accessors simple-accessors nested)
         :types/nested-accessors        (s/intersection nested accessors)}))))

(defn all-disjoint? [& sets]
  (let [total (reduce + (map count sets))
        united (apply clojure.set/union sets)]
    (= (count united) total)))

(def type-categories
  #{:types/enums
    :types/abstract-unions
    :types/concrete-unions
    :types/static-factories
    :types/nested-static-factories
    :types/simple-accessors
    :types/complex-accessors
    :types/nested-accessors})

(defn bigquery []
  (let [bq (_bigquery)]
    (assert (map? (:lookup/by-base bq)))
    (assert (map? (:lookup/union->variant bq)))
    (assert (map? (:lookup/variant->union bq)))
    (assert (set? (:types/static-factories bq)))
    (assert (set? (:types/nested-static-factories bq)))
    (assert (set? (:types/simple-accessors bq)))
    (assert (set? (:types/complex-accessors bq)))
    (when (seq (s/intersection (:types/static-factories bq) (:types/nested-static-factories bq)))
      (throw (ex-info "nested static factories should be disjoint"
                      {:types/static-factories (:types/static-factories bq)
                       :types/nested-static-factories (:types/nested-static-factories bq)})))
    (when (or (seq (s/intersection (:types/complex-accessors bq)
                                   (:types/simple-accessors bq)))
              (seq (s/intersection (:types/simple-accessors bq)
                                   (:types/nested-accessors bq)))
              (seq (s/intersection (:types/complex-accessors bq)
                                   (:types/nested-accessors bq))))
      (throw (ex-info "simple, complex, & nested accessors should be disjoint"
                      {:types/complex-accessors (:types/complex-accessors bq)
                       :types/simple-accessors (:types/simple-accessors bq)
                       :types/nested-accessors (:types/nested-accessors bq)})))
    (assert (apply all-disjoint? (vals (select-keys bq type-categories))))
    bq))
