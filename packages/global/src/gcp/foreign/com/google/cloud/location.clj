(ns gcp.foreign.com.google.cloud.location
  {:gcp.dev/certification
   {:GetLocationRequest
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767557933124
       :timestamp "2026-01-04T20:18:53.127942660Z"
       :passed-stages
         {:smoke 1767557933124 :standard 1767557933125 :stress 1767557933126}
       :source-hash
         "be68aede2b05192c490bc9f244f0ad09e8bc83c0c5e95e4ef8a04e5fdbe0aaa2"}
    :ListLocationsRequest
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767557933128
       :timestamp "2026-01-04T20:18:53.134574064Z"
       :passed-stages
         {:smoke 1767557933128 :standard 1767557933129 :stress 1767557933130}
       :source-hash
         "be68aede2b05192c490bc9f244f0ad09e8bc83c0c5e95e4ef8a04e5fdbe0aaa2"}
    :ListLocationsResponse
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767557933135
       :timestamp "2026-01-04T20:18:54.809992133Z"
       :passed-stages
         {:smoke 1767557933135 :standard 1767557933136 :stress 1767557933137}
       :source-hash
         "be68aede2b05192c490bc9f244f0ad09e8bc83c0c5e95e4ef8a04e5fdbe0aaa2"}
    :Location
      {:protocol-hash
         "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
       :base-seed 1767557934810
       :timestamp "2026-01-04T20:18:54.885015400Z"
       :passed-stages
         {:smoke 1767557934810 :standard 1767557934811 :stress 1767557934812}
       :source-hash
         "be68aede2b05192c490bc9f244f0ad09e8bc83c0c5e95e4ef8a04e5fdbe0aaa2"}}}
  (:require [gcp.global :as global])
  (:import (com.google.cloud.location GetLocationRequest ListLocationsRequest ListLocationsResponse Location)
           (com.google.protobuf Any)))

;; Location
(defn Location-from-edn [arg]
  (let [builder (Location/newBuilder)]
    (when-some [v (:name arg)] (.setName builder v))
    (when-some [v (:locationId arg)] (.setLocationId builder v))
    (when-some [v (:displayName arg)] (.setDisplayName builder v))
    (when-some [v (:labels arg)] (.putAllLabels builder v))
    ;; metadata is Any, we assume user passes Any object or we skip complex conversion for now
    (when-some [v (:metadata arg)] 
      (if (instance? Any v)
        (.setMetadata builder v)
        ;; If it's a map representing Any, we might need a way to construct it.
        ;; For now, assuming it's ignored or must be Any instance.
        nil))
    (.build builder)))

(defn Location-to-edn [^Location arg]
  {:name (.getName arg)
   :locationId (.getLocationId arg)
   :displayName (.getDisplayName arg)
   :labels (into {} (.getLabelsMap arg))
   ;; metadata: just returning the object if present
   :metadata (when (.hasMetadata arg) (.getMetadata arg))})

;; GetLocationRequest
(defn GetLocationRequest-from-edn [arg]
  (let [builder (GetLocationRequest/newBuilder)]
    (when-some [v (:name arg)] (.setName builder v))
    (.build builder)))

(defn GetLocationRequest-to-edn [^GetLocationRequest arg]
  {:name (.getName arg)})

;; ListLocationsRequest
(defn ListLocationsRequest-from-edn [arg]
  (let [builder (ListLocationsRequest/newBuilder)]
    (when-some [v (:name arg)] (.setName builder v))
    (when-some [v (:filter arg)] (.setFilter builder v))
    (when-some [v (:pageSize arg)] (.setPageSize builder (int v)))
    (when-some [v (:pageToken arg)] (.setPageToken builder v))
    (.build builder)))

(defn ListLocationsRequest-to-edn [^ListLocationsRequest arg]
  {:name (.getName arg)
   :filter (.getFilter arg)
   :pageSize (.getPageSize arg)
   :pageToken (.getPageToken arg)})

;; ListLocationsResponse
(defn ListLocationsResponse-from-edn [arg]
  (let [builder (ListLocationsResponse/newBuilder)]
    (when-some [v (:nextPageToken arg)] (.setNextPageToken builder v))
    (when-some [v (:locations arg)] (.addAllLocations builder (map Location-from-edn v)))
    (.build builder)))

(defn ListLocationsResponse-to-edn [^ListLocationsResponse arg]
  {:nextPageToken (.getNextPageToken arg)
   :locations (mapv Location-to-edn (.getLocationsList arg))})

(global/include-schema-registry!
 (with-meta
   {::Location [:map
                [:name :string]
                [:locationId :string]
                [:displayName :string]
                [:labels [:map-of :string :string]]
                [:metadata {:optional true} :any]]
    ::GetLocationRequest [:map
                          [:name :string]]
    ::ListLocationsRequest [:map
                            [:name :string]
                            [:filter :string]
                            [:pageSize [:int {:min 0 :max 2147483647}]]
                            [:pageToken :string]]
    ::ListLocationsResponse [:map
                             [:nextPageToken :string]
                             [:locations [:sequential ::Location]]]}
   {::global/name ::registry}))
