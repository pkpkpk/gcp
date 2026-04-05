;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.storage.Acl
  {:doc
     "Access Control List for buckets or blobs.\n\n@see <a href=\"https://cloud.google.com/storage/docs/access-control#About-Access-Control-Lists\">\n    About Access Control Lists</a>"
   :file-git-sha "ca6878974eaf83f0237fa7faacd1e664db93e7a1"
   :fqcn "com.google.cloud.storage.Acl"
   :gcp.dev/certification
     {:base-seed 1775175512170
      :manifest "215ec381-0f5f-5884-ab6d-eb0bb246cd16"
      :passed-stages
        {:smoke 1775175512170 :standard 1775175512171 :stress 1775175512172}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-03T00:18:33.556793946Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.storage Acl Acl$Builder Acl$Domain Acl$Entity
            Acl$Entity$Type Acl$Group Acl$Project Acl$Project$ProjectRole
            Acl$RawEntity Acl$Role Acl$User]))

(declare from-edn
         to-edn
         Role-from-edn
         Role-to-edn
         Entity$Type-from-edn
         Entity$Type-to-edn
         Entity-from-edn
         Entity-to-edn
         Domain-from-edn
         Domain-to-edn
         Group-from-edn
         Group-to-edn
         User-from-edn
         User-to-edn
         Project$ProjectRole-from-edn
         Project$ProjectRole-to-edn
         Project-from-edn
         Project-to-edn
         Project$ProjectRole-from-edn
         Project$ProjectRole-to-edn
         RawEntity-from-edn
         RawEntity-to-edn)

(def Role-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/string-enum,
    :gcp/key :gcp.storage/Acl.Role} "OWNER" "READER" "WRITER"])

(def Entity$Type-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.storage/Acl.Entity.Type} "DOMAIN" "GROUP" "USER" "PROJECT"
   "UNKNOWN"])

(defn ^Acl$Entity Entity-from-edn
  [arg]
  (case (get arg :type)
    "DOMAIN" (Domain-from-edn arg)
    "UNKNOWN" (RawEntity-from-edn arg)
    "PROJECT" (Project-from-edn arg)
    "GROUP" (Group-from-edn arg)
    "USER" (User-from-edn arg)))

(defn Entity-to-edn
  [^Acl$Entity arg]
  (when arg
    (case (.name (.getType arg))
      "DOMAIN" (Domain-to-edn arg)
      "UNKNOWN" (RawEntity-to-edn arg)
      "PROJECT" (Project-to-edn arg)
      "GROUP" (Group-to-edn arg)
      "USER" (User-to-edn arg))))

(def Entity-schema
  [:or
   {:closed true,
    :doc "Base class for Access Control List entities.",
    :gcp/category :nested/union-abstract,
    :gcp/key :gcp.storage/Acl.Entity} [:ref :gcp.storage/Acl.Domain]
   [:ref :gcp.storage/Acl.RawEntity] [:ref :gcp.storage/Acl.Project]
   [:ref :gcp.storage/Acl.Group] [:ref :gcp.storage/Acl.User]])

(defn ^Acl$Domain Domain-from-edn
  [arg]
  (cond
    (contains? arg :domain) (new Acl$Domain (get arg :domain))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.storage.Acl.Domain"
          {:arg arg}))))

(defn Domain-to-edn
  [^Acl$Domain arg]
  (when arg {:domain (.getDomain arg), :type "DOMAIN"}))

(def Domain-schema
  [:map
   {:closed true,
    :doc "Class for ACL Domain entities.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.storage/Acl.Domain} [:type [:= "DOMAIN"]]
   [:domain {:doc "Returns the domain associated to this entity."}
    [:string {:min 1}]]])

(defn ^Acl$Group Group-from-edn
  [arg]
  (cond
    (contains? arg :email) (new Acl$Group (get arg :email))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.storage.Acl.Group"
          {:arg arg}))))

(defn Group-to-edn
  [^Acl$Group arg]
  (when arg {:email (.getEmail arg), :type "GROUP"}))

(def Group-schema
  [:map
   {:closed true,
    :doc "Class for ACL Group entities.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.storage/Acl.Group} [:type [:= "GROUP"]]
   [:email {:doc "Returns the group email."} [:string {:min 1}]]])

(defn ^Acl$User User-from-edn
  [arg]
  (cond
    (contains? arg :email) (new Acl$User (get arg :email))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.storage.Acl.User"
          {:arg arg}))))

(defn User-to-edn
  [^Acl$User arg]
  (when arg {:email (.getEmail arg), :type "USER"}))

(def User-schema
  [:map
   {:closed true,
    :doc "Class for ACL User entities.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.storage/Acl.User} [:type [:= "USER"]]
   [:email {:doc "Returns the user email."} [:string {:min 1}]]])

(def Project$ProjectRole-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/string-enum,
    :gcp/key :gcp.storage/Acl.Project.ProjectRole} "OWNERS" "EDITORS"
   "VIEWERS"])

(def Project$ProjectRole-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/string-enum,
    :gcp/key :gcp.storage/Acl.Project.ProjectRole} "OWNERS" "EDITORS"
   "VIEWERS"])

(defn ^Acl$Project Project-from-edn
  [arg]
  (cond
    (and (contains? arg :projectId) (contains? arg :projectRole))
      (new Acl$Project
           (Acl$Project$ProjectRole/valueOf (get arg :projectRole))
           (get arg :projectId))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.storage.Acl.Project"
          {:arg arg}))))

(defn Project-to-edn
  [^Acl$Project arg]
  (when arg
    {:projectId (.getProjectId arg),
     :projectRole (.name (.getProjectRole arg)),
     :type "PROJECT"}))

(def Project-schema
  [:map
   {:closed true,
    :doc "Class for ACL Project entities.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.storage/Acl.Project} [:type [:= "PROJECT"]]
   [:projectId {:doc "Returns the project id for this entity."}
    [:string {:min 1}]]
   [:projectRole {:doc "Returns the role in the project for this entity."}
    [:enum {:closed true} "OWNERS" "EDITORS" "VIEWERS"]]])

(defn ^Acl$RawEntity RawEntity-from-edn
  [arg]
  (throw (Exception.
           "Class com.google.cloud.storage.Acl.RawEntity is read-only")))

(defn RawEntity-to-edn
  [^Acl$RawEntity arg]
  (when arg {:type "UNKNOWN"}))

(def RawEntity-schema
  [:map
   {:closed true,
    :doc nil,
    :gcp/category :nested/variant-read-only,
    :gcp/key :gcp.storage/Acl.RawEntity} [:type [:= "UNKNOWN"]]])

(defn ^Acl from-edn
  [arg]
  (global/strict! :gcp.storage/Acl arg)
  (let [builder (Acl/newBuilder (Entity-from-edn (get arg :entity))
                                (Acl$Role/valueOf (get arg :role)))]
    (.build builder)))

(defn to-edn
  [^Acl arg]
  {:post [(global/strict! :gcp.storage/Acl %)]}
  (when arg
    (cond-> {:entity (Entity-to-edn (.getEntity arg)),
             :role (.name (.getRole arg))}
      (some->> (.getEtag arg)
               (not= ""))
        (assoc :etag (.getEtag arg))
      (some->> (.getId arg)
               (not= ""))
        (assoc :id (.getId arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Access Control List for buckets or blobs.\n\n@see <a href=\"https://cloud.google.com/storage/docs/access-control#About-Access-Control-Lists\">\n    About Access Control Lists</a>",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.storage/Acl}
   [:entity {:getter-doc "Returns the entity for this ACL object."}
    [:ref :gcp.storage/Acl.Entity]]
   [:etag
    {:optional true,
     :read-only? true,
     :getter-doc
       "Returns HTTP 1.1 Entity tag for the ACL entry.\n\n@see <a href=\"http://tools.ietf.org/html/rfc2616#section-3.11\">Entity Tags</a>"}
    [:string {:min 1}]]
   [:id
    {:optional true,
     :read-only? true,
     :getter-doc "Returns the ID of the ACL entry."} [:string {:min 1}]]
   [:role
    {:getter-doc
       "Returns the role associated to the entity in this ACL object."}
    [:enum {:closed true} "OWNER" "READER" "WRITER"]]])

(global/include-schema-registry!
  (with-meta {:gcp.storage/Acl schema,
              :gcp.storage/Acl.Domain Domain-schema,
              :gcp.storage/Acl.Entity Entity-schema,
              :gcp.storage/Acl.Entity.Type Entity$Type-schema,
              :gcp.storage/Acl.Group Group-schema,
              :gcp.storage/Acl.Project Project-schema,
              :gcp.storage/Acl.Project.ProjectRole Project$ProjectRole-schema,
              :gcp.storage/Acl.RawEntity RawEntity-schema,
              :gcp.storage/Acl.Role Role-schema,
              :gcp.storage/Acl.User User-schema}
    {:gcp.global/name "gcp.storage.Acl"}))