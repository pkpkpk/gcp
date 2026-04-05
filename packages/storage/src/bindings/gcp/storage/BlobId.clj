;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.storage.BlobId
  {:doc
     "Google Storage Object identifier. A {@code BlobId} object includes the name of the containing\nbucket, the blob's name and possibly the blob's generation. If {@link #getGeneration()} is {@code\nnull} the identifier refers to the latest blob's generation."
   :file-git-sha "8f9f5ec4506bde58fbf2351c99f0d67cdcfcd88e"
   :fqcn "com.google.cloud.storage.BlobId"
   :gcp.dev/certification
     {:base-seed 1775175411380
      :manifest "215ec381-0f5f-5884-ab6d-eb0bb246cd16"
      :passed-stages
        {:smoke 1775175411380 :standard 1775175411381 :stress 1775175411382}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-03T00:16:52.693864206Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.storage BlobId]))

(declare from-edn to-edn)

(defn ^BlobId from-edn
  [arg]
  (global/strict! :gcp.storage/BlobId arg)
  (cond (string? arg) (BlobId/fromGsUtilUri arg)
        (get arg :gsUtilUri) (BlobId/fromGsUtilUri (get arg :gsUtilUri))
        (and (get arg :bucket) (get arg :name) (get arg :generation))
          (BlobId/of (get arg :bucket)
                     (get arg :name)
                     (long (get arg :generation)))
        (and (get arg :bucket) (get arg :name)) (BlobId/of (get arg :bucket)
                                                           (get arg :name))
        true (ex-info "failed to match edn for static-factory cond body"
                      {:arg arg, :key :gcp.storage/BlobId})))

(defn to-edn
  [^BlobId arg]
  {:post [(global/strict! :gcp.storage/BlobId %)]}
  (when arg
    (cond (and (.getBucket arg) (.getName arg) (.getGeneration arg))
            {:bucket (.getBucket arg),
             :name (.getName arg),
             :generation (.getGeneration arg)}
          (and (.getBucket arg) (.getName arg)) {:bucket (.getBucket arg),
                                                 :name (.getName arg)}
          :else (throw (clojure.core/ex-info
                         "failed to match edn for static-factory cond body"
                         {:key :gcp.storage/BlobId, :arg arg})))))

(def schema
  [:or
   {:closed true,
    :doc
      "Google Storage Object identifier. A {@code BlobId} object includes the name of the containing\nbucket, the blob's name and possibly the blob's generation. If {@link #getGeneration()} is {@code\nnull} the identifier refers to the latest blob's generation.",
    :gcp/category :static-factory,
    :gcp/key :gcp.storage/BlobId} [:re #"^gs://(.+)/(.+)(?:#(\\d+))?$"]
   [:map
    {:closed true,
     :doc
       "Creates a {@code BlobId} object.\n\n@param gsUtilUri the Storage url to create the blob from"}
    [:gsUtilUri [:re #"^gs://(.+)/(.+)(?:#(\\d+))?$"]]]
   [:map
    {:closed true,
     :doc
       "Creates a blob identifier. Generation is set to {@code null}.\n\n@param bucket the name of the bucket that contains the blob\n@param name the name of the blob"}
    [:bucket {:doc "Returns the name of the bucket containing the blob."}
     [:string {:min 1}]]
    [:name {:doc "Returns the name of the blob."} [:string {:min 1}]]]
   [:map
    {:closed true,
     :doc
       "Creates a {@code BlobId} object.\n\n@param bucket name of the containing bucket\n@param name blob's name\n@param generation blob's data generation, used for versioning. If {@code null} the identifier\n    refers to the latest blob's generation"}
    [:bucket {:doc "Returns the name of the bucket containing the blob."}
     [:string {:min 1}]]
    [:name {:doc "Returns the name of the blob."} [:string {:min 1}]]
    [:generation {:doc "Returns blob's data generation. Used for versioning."}
     :i64]]])

(global/include-schema-registry! (with-meta {:gcp.storage/BlobId schema}
                                   {:gcp.global/name "gcp.storage.BlobId"}))