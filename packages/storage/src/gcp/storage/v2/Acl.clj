(ns gcp.storage.v2.Acl
  (:require [gcp.global :as global]
            [gcp.storage.v2.synth])
  (:import (com.google.cloud.storage Acl Acl$Domain Acl$Entity Acl$Group Acl$Project$ProjectRole Acl$Role Acl$Project Acl$User)))

(defn Entity:from-edn [{t :type :as arg}]
  (case t
    "USER"    (Acl$User. (:email arg))
    "GROUP"   (Acl$Group. (:email arg))
    "PROJECT" (Acl$Project. (Acl$Project$ProjectRole/valueOf (:projectRole t)) (:projectId t))
    "DOMAIN"  (Acl$Domain. (:domain arg))
    (throw (ex-info (str "unsupported entity type '" t "'") {:arg arg}))))

(defn Entity:to-edn [^Acl$Entity arg]
  (let [t (.name (.getType arg))
        base {:type (.name (.getType arg))}]
    (case t
      "USER"    (assoc base :email (.getEmail ^Acl$User arg))
      "GROUP"   (assoc base :email (.getEmail ^Acl$Group arg))
      "DOMAIN"  (assoc base :domain (.getDomain ^Acl$Domain arg))
      "PROJECT" (assoc base :projectId (.getProjectId ^Acl$Project arg)
                            :projectRole (.name (.getProjectRole ^Acl$Project arg)))
      "UNKNOWN" (assoc base :arg arg)
      (throw (ex-info (str "unknown type " t) {:arg arg})))))

(defn from-edn
  [{:keys [role entity]}]
  (Acl/of (Entity:from-edn entity) (Acl$Role/valueOf role)))

(defn to-edn [^Acl arg]
  {:post [(global/strict! :gcp.storage.v2/Acl %)]}
  (cond-> {}
          (some? (.getEtag arg))
          (assoc :etag (.getEtag arg))

          (some? (.getId arg))
          (assoc :id (.getId arg))

          (some? (.getEntity arg))
          (assoc :entity (Entity:to-edn (.getEntity arg)))

          (some? (.getRole arg))
          (assoc :role (.name (.getRole arg)))))

(def schemas
  {:gcp.storage.v2/Acl
   [:map {:closed true
          :urls   ["https://cloud.google.com/storage/docs/access-control#About-Access-Control-Lists"
                   "https://cloud.google.com/java/docs/reference/google-cloud-storage/latest/com.google.cloud.storage.Acl"]}
    [:role [:enum "OWNER" "READER" "WRITER"]]
    [:entity :gcp.storage.v2/Acl.Entity]
    [:id {:optional true :read-only true} :string]
    [:etag {:optional true :read-only true} :string]]

   :gcp.storage.v2/Acl.Entity
   [:and
    [:map [:type [:enum "DOMAIN" "GROUP" "PROJECT" "UNKNOWN" "USER"]]]
    [:or
     :gcp.storage.v2/Acl.Group
     :gcp.storage.v2/Acl.Domain
     :gcp.storage.v2/Acl.Project
     :gcp.storage.v2/Acl.User]]

   :gcp.storage.v2/Acl.Domain
   [:map {:closed true} [:type [:= "DOMAIN"]] [:domain :string]]

   :gcp.storage.v2/Acl.Project
   [:map [:projectRole [:enum "EDITORS" "OWNERS" "VIEWERS"]] [:projectId :string]]

   :gcp.storage.v2/Acl.Group
   [:map {:closed true} [:type [:= "GROUP"]] [:email :string]]

   :gcp.storage.v2/Acl.User
   [:map {:closed true} [:type [:= "USER"]] [:email :string]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))


