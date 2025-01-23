(ns gcp.storage.v2.Acl
  (:require [gcp.global :as g])
  (:import (com.google.cloud.storage Acl Acl$Domain Acl$Entity Acl$Group Acl$Project$ProjectRole Acl$Role Acl$Project Acl$User)))

(defn Entity-from-edn [{t :type :as arg}]
  (case t
    "USER"    (Acl$User. (:email arg))
    "GROUP"   (Acl$Group. (:email arg))
    "PROJECT" (Acl$Project. (Acl$Project$ProjectRole/valueOf (:projectRole t)) (:projectId t))
    "DOMAIN"  (Acl$Domain. (:domain arg))
    (throw (ex-info (str "unsupported entity type '" t "'") {:arg arg}))))

(defn Entity-to-edn [^Acl$Entity arg]
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
  (Acl/of (Entity-from-edn entity) (Acl$Role/valueOf role)))

(defn to-edn [^Acl arg]
  {:post [(g/strict! :storage/Acl %)]}
  (cond-> {}
          (some? (.getEtag arg))
          (assoc :etag (.getEtag arg))

          (some? (.getId arg))
          (assoc :id (.getId arg))

          (some? (.getEntity arg))
          (assoc :entity (Entity-to-edn (.getEntity arg)))

          (some? (.getRole arg))
          (assoc :role (.name (.getRole arg)))))


