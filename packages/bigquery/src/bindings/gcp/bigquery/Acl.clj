;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.Acl
  {:doc
     "Access Control for a BigQuery Dataset. BigQuery uses ACLs to manage permissions on datasets. ACLs\nare not directly supported on tables. A table inherits its ACL from the dataset that contains it.\nProject roles affect your ability to run jobs or manage the project, while dataset roles affect\nhow you can access or modify the data inside a project.\n\n@see <a href=\"https://cloud.google.com/bigquery/access-control\">Access Control</a>"
   :file-git-sha "a335927e16d0907d62e584f08fa8393daae40354"
   :fqcn "com.google.cloud.bigquery.Acl"
   :gcp.dev/certification
     {:base-seed 1776499498650
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :passed-stages
        {:smoke 1776499498650 :standard 1776499498651 :stress 1776499498652}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:04:59.999434829Z"}}
  (:require [gcp.bigquery.DatasetId :as DatasetId]
            [gcp.bigquery.RoutineId :as RoutineId]
            [gcp.bigquery.TableId :as TableId]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery Acl Acl$DatasetAclEntity Acl$Domain
            Acl$Entity Acl$Entity$Type Acl$Expr Acl$Expr$Builder Acl$Group
            Acl$IamMember Acl$Role Acl$Routine Acl$User Acl$View]))

(declare from-edn
         to-edn
         Role-from-edn
         Role-to-edn
         Entity$Type-from-edn
         Entity$Type-to-edn
         Entity-from-edn
         Entity-to-edn
         DatasetAclEntity-from-edn
         DatasetAclEntity-to-edn
         Domain-from-edn
         Domain-to-edn
         Group-from-edn
         Group-to-edn
         User-from-edn
         User-to-edn
         View-from-edn
         View-to-edn
         Routine-from-edn
         Routine-to-edn
         IamMember-from-edn
         IamMember-to-edn
         Expr-from-edn
         Expr-to-edn)

(def Role-schema
  [:enum
   {:closed true,
    :doc
      "Dataset roles supported by BigQuery.\n\n@see <a href=\"https://cloud.google.com/bigquery/access-control#datasetroles\">Dataset Roles</a>",
    :gcp/category :nested/string-enum,
    :gcp/key :gcp.bigquery/Acl.Role} "READER" "WRITER" "OWNER"])

(def Entity$Type-schema
  [:enum
   {:closed true,
    :doc "Types of BigQuery entities.",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bigquery/Acl.Entity.Type} "DOMAIN" "GROUP" "USER" "VIEW"
   "IAM_MEMBER" "ROUTINE" "DATASET"])

(defn ^Acl$Entity Entity-from-edn
  [arg]
  (case (get arg :type)
    "DOMAIN" (Domain-from-edn arg)
    "IAM_MEMBER" (IamMember-from-edn arg)
    "DATASET" (DatasetAclEntity-from-edn arg)
    "VIEW" (View-from-edn arg)
    "USER" (User-from-edn arg)
    "ROUTINE" (Routine-from-edn arg)
    "GROUP" (Group-from-edn arg)))

(defn Entity-to-edn
  [^Acl$Entity arg]
  (when arg
    (case (.name (.getType arg))
      "DOMAIN" (Domain-to-edn arg)
      "IAM_MEMBER" (IamMember-to-edn arg)
      "DATASET" (DatasetAclEntity-to-edn arg)
      "VIEW" (View-to-edn arg)
      "USER" (User-to-edn arg)
      "ROUTINE" (Routine-to-edn arg)
      "GROUP" (Group-to-edn arg))))

(def Entity-schema
  [:or
   {:closed true,
    :doc
      "Base class for BigQuery entities that can be grant access to the dataset.",
    :gcp/category :nested/union-abstract,
    :gcp/key :gcp.bigquery/Acl.Entity} [:ref :gcp.bigquery/Acl.Domain]
   [:ref :gcp.bigquery/Acl.IamMember] [:ref :gcp.bigquery/Acl.DatasetAclEntity]
   [:ref :gcp.bigquery/Acl.View] [:ref :gcp.bigquery/Acl.User]
   [:ref :gcp.bigquery/Acl.Routine] [:ref :gcp.bigquery/Acl.Group]])

(defn ^Acl$DatasetAclEntity DatasetAclEntity-from-edn
  [arg]
  (cond
    (and (contains? arg :targetTypes) (contains? arg :id))
      (new Acl$DatasetAclEntity
           (DatasetId/from-edn (get arg :id))
           (seq (get arg :targetTypes)))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.bigquery.Acl.DatasetAclEntity"
          {:arg arg}))))

(defn DatasetAclEntity-to-edn
  [^Acl$DatasetAclEntity arg]
  (when arg
    {:targetTypes (seq (.getTargetTypes arg)),
     :id (DatasetId/to-edn (.getId arg)),
     :type "DATASET"}))

(def DatasetAclEntity-schema
  [:map
   {:closed true,
    :doc
      "Class for a BigQuery DatasetAclEntity ACL entity. Objects of this class represent a\nDatasetAclEntity from a different DatasetAclEntity to grant access to. Only views are supported\nfor now. The role field is not required when this field is set. If that DatasetAclEntity is\ndeleted and re-created, its access needs to be granted again via an update operation.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.bigquery/Acl.DatasetAclEntity} [:type [:= "DATASET"]]
   [:id {:doc "@return Returns DatasetAclEntity's identity."}
    :gcp.bigquery/DatasetId]
   [:targetTypes {} [:sequential {:min 1} [:string {:min 1}]]]])

(defn ^Acl$Domain Domain-from-edn
  [arg]
  (cond
    (contains? arg :domain) (new Acl$Domain (get arg :domain))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.bigquery.Acl.Domain"
          {:arg arg}))))

(defn Domain-to-edn
  [^Acl$Domain arg]
  (when arg {:domain (.getDomain arg), :type "DOMAIN"}))

(def Domain-schema
  [:map
   {:closed true,
    :doc
      "Class for a BigQuery Domain entity. Objects of this class represent a domain to grant access\nto. Any users signed in with the domain specified will be granted the specified access.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.bigquery/Acl.Domain} [:type [:= "DOMAIN"]]
   [:domain {:doc "@return Returns the domain name."} [:string {:min 1}]]])

(defn ^Acl$Group Group-from-edn
  [arg]
  (cond
    (contains? arg :identifier) (new Acl$Group (get arg :identifier))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.bigquery.Acl.Group"
          {:arg arg}))))

(defn Group-to-edn
  [^Acl$Group arg]
  (when arg {:identifier (.getIdentifier arg), :type "GROUP"}))

(def Group-schema
  [:map
   {:closed true,
    :doc
      "Class for a BigQuery Group entity. Objects of this class represent a group to granted access\nto. A Group entity can be created given the group's email or can be a special group: {@link\n#ofProjectOwners()}, {@link #ofProjectReaders()}, {@link #ofProjectWriters()} or {@link\n#ofAllAuthenticatedUsers()}.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.bigquery/Acl.Group} [:type [:= "GROUP"]]
   [:identifier
    {:doc
       "@return Returns group's identifier, can be either a <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/datasets#access.specialGroup\">\n    special group identifier</a> or a group email."}
    [:string {:min 1}]]])

(defn ^Acl$User User-from-edn
  [arg]
  (cond
    (contains? arg :email) (new Acl$User (get arg :email))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.bigquery.Acl.User"
          {:arg arg}))))

(defn User-to-edn
  [^Acl$User arg]
  (when arg {:email (.getEmail arg), :type "USER"}))

(def User-schema
  [:map
   {:closed true,
    :doc
      "Class for a BigQuery User entity. Objects of this class represent a user to grant access to\ngiven the email address.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.bigquery/Acl.User} [:type [:= "USER"]]
   [:email {:doc "@return Returns user's email."} [:string {:min 1}]]])

(defn ^Acl$View View-from-edn
  [arg]
  (cond
    (contains? arg :id) (new Acl$View (TableId/from-edn (get arg :id)))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.bigquery.Acl.View"
          {:arg arg}))))

(defn View-to-edn
  [^Acl$View arg]
  (when arg {:id (TableId/to-edn (.getId arg)), :type "VIEW"}))

(def View-schema
  [:map
   {:closed true,
    :doc
      "Class for a BigQuery View entity. Objects of this class represent a view from a different\ndatasetAclEntity to grant access to. Queries executed against that view will have read access\nto tables in this datasetAclEntity. The role field is not required when this field is set. If\nthat view is updated by any user, access to the view needs to be granted again via an update\noperation.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.bigquery/Acl.View} [:type [:= "VIEW"]]
   [:id {:doc "@return Returns table's identity."} :gcp.bigquery/TableId]])

(defn ^Acl$Routine Routine-from-edn
  [arg]
  (cond
    (contains? arg :id) (new Acl$Routine (RoutineId/from-edn (get arg :id)))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.bigquery.Acl.Routine"
          {:arg arg}))))

(defn Routine-to-edn
  [^Acl$Routine arg]
  (when arg {:id (RoutineId/to-edn (.getId arg)), :type "ROUTINE"}))

(def Routine-schema
  [:map
   {:closed true,
    :doc
      "Class for a BigQuery Routine entity. Objects of this class represent a routine from a different\ndatasetAclEntity to grant access to. Queries executed against that routine will have read\naccess to views/tables/routines in this datasetAclEntity. Only UDF is supported for now. The\nrole field is not required when this field is set. If that routine is updated by any user,\naccess to the routine needs to be granted again via an update operation.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.bigquery/Acl.Routine} [:type [:= "ROUTINE"]]
   [:id {:doc "@return Returns routine's identity."} :gcp.bigquery/RoutineId]])

(defn ^Acl$IamMember IamMember-from-edn
  [arg]
  (cond
    (contains? arg :iamMember) (new Acl$IamMember (get arg :iamMember))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.bigquery.Acl.IamMember"
          {:arg arg}))))

(defn IamMember-to-edn
  [^Acl$IamMember arg]
  (when arg {:iamMember (.getIamMember arg), :type "IAM_MEMBER"}))

(def IamMember-schema
  [:map
   {:closed true,
    :doc
      "Class for a BigQuery IamMember entity. Objects of this class represent a iamMember to grant\naccess to given the IAM Policy.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.bigquery/Acl.IamMember} [:type [:= "IAM_MEMBER"]]
   [:iamMember {:doc "@return Returns iamMember."} [:string {:min 1}]]])

(defn ^Acl$Expr Expr-from-edn
  [arg]
  (cond
    (and (contains? arg :description)
         (contains? arg :expression)
         (contains? arg :title)
         (contains? arg :location))
      (new Acl$Expr
           (get arg :expression)
           (get arg :title)
           (get arg :description)
           (get arg :location))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.bigquery.Acl.Expr"
          {:arg arg}))))

(defn Expr-to-edn
  [^Acl$Expr arg]
  (when arg
    {:description (global/get-private-field arg "description"),
     :expression (global/get-private-field arg "expression"),
     :title (global/get-private-field arg "title"),
     :location (global/get-private-field arg "location")}))

(def Expr-schema
  [:map
   {:closed true,
    :doc
      "Expr represents the conditional information related to dataset access policies.",
    :gcp/category :nested/pojo,
    :gcp/key :gcp.bigquery/Acl.Expr}
   [:description {:doc "Synthetic getter for description"} [:string {:min 1}]]
   [:expression {:doc "Synthetic getter for expression"} [:string {:min 1}]]
   [:location {:doc "Synthetic getter for location"} [:string {:min 1}]]
   [:title {:doc "Synthetic getter for title"} [:string {:min 1}]]])

(defn ^Acl from-edn
  [arg]
  (global/strict! :gcp.bigquery/Acl arg)
  (cond (gcp.global/valid? :gcp.bigquery/Acl.DatasetAclEntity arg)
          (Acl/of (DatasetAclEntity-from-edn arg))
        (get arg :datasetAclEntity) (Acl/of (DatasetAclEntity-from-edn
                                              (get arg :datasetAclEntity)))
        (gcp.global/valid? :gcp.bigquery/Acl.View arg) (Acl/of (View-from-edn
                                                                 arg))
        (get arg :view) (Acl/of (View-from-edn (get arg :view)))
        (gcp.global/valid? :gcp.bigquery/Acl.Routine arg)
          (Acl/of (Routine-from-edn arg))
        (get arg :routine) (Acl/of (Routine-from-edn (get arg :routine)))
        (and (get arg :entity) (get arg :role) (get arg :condition))
          (Acl/of (Entity-from-edn (get arg :entity))
                  (Acl$Role/valueOf (get arg :role))
                  (Expr-from-edn (get arg :condition)))
        (and (get arg :entity) (get arg :role))
          (Acl/of (Entity-from-edn (get arg :entity))
                  (Acl$Role/valueOf (get arg :role)))
        true (ex-info "failed to match edn for static-factory cond body"
                      {:arg arg, :key :gcp.bigquery/Acl})))

(defn to-edn
  [^Acl arg]
  {:post [(global/strict! :gcp.bigquery/Acl %)]}
  (when arg
    (cond (and (.getEntity arg) (.getRole arg) (.getCondition arg))
            {:entity (Entity-to-edn (.getEntity arg)),
             :role (.name (.getRole arg)),
             :condition (Expr-to-edn (.getCondition arg))}
          (and (.getEntity arg) (.getRole arg)) {:entity (Entity-to-edn
                                                           (.getEntity arg)),
                                                 :role (.name (.getRole arg))}
          (and (.getEntity arg)
               (instance? Acl$DatasetAclEntity (.getEntity arg)))
            {:datasetAclEntity (DatasetAclEntity-to-edn (.getEntity arg))}
          (and (.getEntity arg) (instance? Acl$View (.getEntity arg)))
            {:view (View-to-edn (.getEntity arg))}
          (and (.getEntity arg) (instance? Acl$Routine (.getEntity arg)))
            {:routine (Routine-to-edn (.getEntity arg))}
          :else (throw (clojure.core/ex-info
                         "failed to match edn for static-factory cond body"
                         {:key :gcp.bigquery/Acl, :arg arg})))))

(def schema
  [:or
   {:closed true,
    :doc
      "Access Control for a BigQuery Dataset. BigQuery uses ACLs to manage permissions on datasets. ACLs\nare not directly supported on tables. A table inherits its ACL from the dataset that contains it.\nProject roles affect your ability to run jobs or manage the project, while dataset roles affect\nhow you can access or modify the data inside a project.\n\n@see <a href=\"https://cloud.google.com/bigquery/access-control\">Access Control</a>",
    :gcp/category :static-factory,
    :gcp/key :gcp.bigquery/Acl} [:ref :gcp.bigquery/Acl.DatasetAclEntity]
   [:map
    {:closed true,
     :doc
       "@param datasetAclEntity\n@return Returns an Acl object for a datasetAclEntity."}
    [:datasetAclEntity [:ref :gcp.bigquery/Acl.DatasetAclEntity]]]
   [:ref :gcp.bigquery/Acl.View]
   [:map
    {:closed true,
     :doc "@param view\n@return Returns an Acl object for a view entity."}
    [:view [:ref :gcp.bigquery/Acl.View]]] [:ref :gcp.bigquery/Acl.Routine]
   [:map
    {:closed true,
     :doc "@param routine\n@return Returns an Acl object for a routine entity."}
    [:routine [:ref :gcp.bigquery/Acl.Routine]]]
   [:map
    {:closed true,
     :doc
       "@return Returns an Acl object.\n@param entity the entity for this ACL object\n@param role the role to associate to the {@code entity} object"}
    [:entity {:doc "@return Returns the entity for this ACL."}
     [:ref :gcp.bigquery/Acl.Entity]]
    [:role {:doc "@return Returns the role specified by this ACL."}
     [:enum {:closed true} "READER" "WRITER" "OWNER"]]]
   [:map {:closed true}
    [:entity {:doc "@return Returns the entity for this ACL."}
     [:ref :gcp.bigquery/Acl.Entity]]
    [:role {:doc "@return Returns the role specified by this ACL."}
     [:enum {:closed true} "READER" "WRITER" "OWNER"]]
    [:condition {:doc "@return Returns the condition specified by this ACL."}
     [:ref :gcp.bigquery/Acl.Expr]]]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/Acl schema,
              :gcp.bigquery/Acl.DatasetAclEntity DatasetAclEntity-schema,
              :gcp.bigquery/Acl.Domain Domain-schema,
              :gcp.bigquery/Acl.Entity Entity-schema,
              :gcp.bigquery/Acl.Entity.Type Entity$Type-schema,
              :gcp.bigquery/Acl.Expr Expr-schema,
              :gcp.bigquery/Acl.Group Group-schema,
              :gcp.bigquery/Acl.IamMember IamMember-schema,
              :gcp.bigquery/Acl.Role Role-schema,
              :gcp.bigquery/Acl.Routine Routine-schema,
              :gcp.bigquery/Acl.User User-schema,
              :gcp.bigquery/Acl.View View-schema}
    {:gcp.global/name "gcp.bigquery.Acl"}))