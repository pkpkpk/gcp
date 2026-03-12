;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.Acl
  {:doc
     "Access Control for a BigQuery Dataset. BigQuery uses ACLs to manage permissions on datasets. ACLs\nare not directly supported on tables. A table inherits its ACL from the dataset that contains it.\nProject roles affect your ability to run jobs or manage the project, while dataset roles affect\nhow you can access or modify the data inside a project.\n\n@see <a href=\"https://cloud.google.com/bigquery/access-control\">Access Control</a>"
   :file-git-sha "a335927e16d0907d62e584f08fa8393daae40354"
   :fqcn "com.google.cloud.bigquery.Acl"
   :gcp.dev/certification
     {:base-seed 1771983412077
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :passed-stages
        {:smoke 1771983412077 :standard 1771983412078 :stress 1771983412079}
      :protocol-hash
        "62616b045d3dd853f6e527d31a44a851f587c87ad57ad3f2927b4519e248d6c9"
      :timestamp "2026-02-25T01:36:52.500948226Z"}}
  (:require [gcp.bindings.bigquery.DatasetId :as DatasetId]
            [gcp.bindings.bigquery.RoutineId :as RoutineId]
            [gcp.bindings.bigquery.TableId :as TableId]
            [gcp.global :as global])
  (:import [com.google.cloud.bigquery Acl Acl$DatasetAclEntity Acl$Domain
            Acl$Entity Acl$Entity$Type Acl$Expr Acl$Group Acl$IamMember Acl$Role
            Acl$Routine Acl$User Acl$View]))

(declare Acl$Role-from-edn
         Acl$Role-to-edn
         Acl$Entity$Type-from-edn
         Acl$Entity$Type-to-edn
         Acl$Entity-from-edn
         Acl$Entity-to-edn
         Acl$DatasetAclEntity-from-edn
         Acl$DatasetAclEntity-to-edn
         Acl$Domain-from-edn
         Acl$Domain-to-edn
         Acl$Group-from-edn
         Acl$Group-to-edn
         Acl$User-from-edn
         Acl$User-to-edn
         Acl$View-from-edn
         Acl$View-to-edn
         Acl$Routine-from-edn
         Acl$Routine-to-edn
         Acl$IamMember-from-edn
         Acl$IamMember-to-edn
         Acl$Expr-from-edn
         Acl$Expr-to-edn)

(defn ^Acl$Role Acl$Role-from-edn [arg] (Acl$Role/valueOf arg))

(defn Acl$Role-to-edn [^Acl$Role arg] (.name arg))

(def Acl$Role-schema
  [:enum
   {:closed true,
    :doc
      "Dataset roles supported by BigQuery.\n\n@see <a href=\"https://cloud.google.com/bigquery/access-control#datasetroles\">Dataset Roles</a>",
    :gcp/category :nested/string-enum,
    :gcp/key :gcp.bindings.bigquery/Acl.Role} "READER" "WRITER" "OWNER"])

(defn ^Acl$Entity$Type Acl$Entity$Type-from-edn
  [arg]
  (Acl$Entity$Type/valueOf arg))

(defn Acl$Entity$Type-to-edn [^Acl$Entity$Type arg] (.name arg))

(def Acl$Entity$Type-schema
  [:enum
   {:closed true,
    :doc "Types of BigQuery entities.",
    :gcp/category :nested/enum,
    :gcp/key :gcp.bindings.bigquery/Acl.Entity.Type} "DOMAIN" "GROUP" "USER"
   "VIEW" "IAM_MEMBER" "ROUTINE" "DATASET"])

(defn ^Acl$Entity Acl$Entity-from-edn
  [arg]
  (case (get arg :type)
    "DOMAIN" (Acl$Domain-from-edn arg)
    "IAM_MEMBER" (Acl$IamMember-from-edn arg)
    "DATASET" (Acl$DatasetAclEntity-from-edn arg)
    "VIEW" (Acl$View-from-edn arg)
    "USER" (Acl$User-from-edn arg)
    "ROUTINE" (Acl$Routine-from-edn arg)
    "GROUP" (Acl$Group-from-edn arg)))

(defn Acl$Entity-to-edn
  [^Acl$Entity arg]
  (case (.name (.getType arg))
    "DOMAIN" (Acl$Domain-to-edn arg)
    "IAM_MEMBER" (Acl$IamMember-to-edn arg)
    "DATASET" (Acl$DatasetAclEntity-to-edn arg)
    "VIEW" (Acl$View-to-edn arg)
    "USER" (Acl$User-to-edn arg)
    "ROUTINE" (Acl$Routine-to-edn arg)
    "GROUP" (Acl$Group-to-edn arg)))

(def Acl$Entity-schema
  [:or
   {:closed true,
    :doc
      "Base class for BigQuery entities that can be grant access to the dataset.",
    :gcp/category :nested/union-abstract,
    :gcp/key :gcp.bindings.bigquery/Acl.Entity}
   :gcp.bindings.bigquery/Acl.Domain :gcp.bindings.bigquery/Acl.IamMember
   :gcp.bindings.bigquery/Acl.DatasetAclEntity :gcp.bindings.bigquery/Acl.View
   :gcp.bindings.bigquery/Acl.User :gcp.bindings.bigquery/Acl.Routine
   :gcp.bindings.bigquery/Acl.Group])

(defn ^Acl$DatasetAclEntity Acl$DatasetAclEntity-from-edn
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

(defn Acl$DatasetAclEntity-to-edn
  [^Acl$DatasetAclEntity arg]
  {:targetTypes (seq (.getTargetTypes arg)),
   :id (DatasetId/to-edn (.getId arg)),
   :type "DATASET"})

(def Acl$DatasetAclEntity-schema
  [:map
   {:closed true,
    :doc
      "Class for a BigQuery DatasetAclEntity ACL entity. Objects of this class represent a\nDatasetAclEntity from a different DatasetAclEntity to grant access to. Only views are supported\nfor now. The role field is not required when this field is set. If that DatasetAclEntity is\ndeleted and re-created, its access needs to be granted again via an update operation.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.bindings.bigquery/Acl.DatasetAclEntity} [:type [:= "DATASET"]]
   [:id {:doc "@return Returns DatasetAclEntity's identity."}
    :gcp.bindings.bigquery/DatasetId]
   [:targetTypes {} [:sequential {:min 1} [:string {:min 1}]]]])

(defn ^Acl$Domain Acl$Domain-from-edn
  [arg]
  (cond
    (contains? arg :domain) (new Acl$Domain (get arg :domain))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.bigquery.Acl.Domain"
          {:arg arg}))))

(defn Acl$Domain-to-edn
  [^Acl$Domain arg]
  {:domain (.getDomain arg), :type "DOMAIN"})

(def Acl$Domain-schema
  [:map
   {:closed true,
    :doc
      "Class for a BigQuery Domain entity. Objects of this class represent a domain to grant access\nto. Any users signed in with the domain specified will be granted the specified access.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.bindings.bigquery/Acl.Domain} [:type [:= "DOMAIN"]]
   [:domain {:doc "@return Returns the domain name."} [:string {:min 1}]]])

(defn ^Acl$Group Acl$Group-from-edn
  [arg]
  (cond
    (contains? arg :identifier) (new Acl$Group (get arg :identifier))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.bigquery.Acl.Group"
          {:arg arg}))))

(defn Acl$Group-to-edn
  [^Acl$Group arg]
  {:identifier (.getIdentifier arg), :type "GROUP"})

(def Acl$Group-schema
  [:map
   {:closed true,
    :doc
      "Class for a BigQuery Group entity. Objects of this class represent a group to granted access\nto. A Group entity can be created given the group's email or can be a special group: {@link\n#ofProjectOwners()}, {@link #ofProjectReaders()}, {@link #ofProjectWriters()} or {@link\n#ofAllAuthenticatedUsers()}.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.bindings.bigquery/Acl.Group} [:type [:= "GROUP"]]
   [:identifier
    {:doc
       "@return Returns group's identifier, can be either a <a\n    href=\"https://cloud.google.com/bigquery/docs/reference/v2/datasets#access.specialGroup\">\n    special group identifier</a> or a group email."}
    [:string {:min 1}]]])

(defn ^Acl$User Acl$User-from-edn
  [arg]
  (cond
    (contains? arg :email) (new Acl$User (get arg :email))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.bigquery.Acl.User"
          {:arg arg}))))

(defn Acl$User-to-edn
  [^Acl$User arg]
  {:email (.getEmail arg), :type "USER"})

(def Acl$User-schema
  [:map
   {:closed true,
    :doc
      "Class for a BigQuery User entity. Objects of this class represent a user to grant access to\ngiven the email address.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.bindings.bigquery/Acl.User} [:type [:= "USER"]]
   [:email {:doc "@return Returns user's email."} [:string {:min 1}]]])

(defn ^Acl$View Acl$View-from-edn
  [arg]
  (cond
    (contains? arg :id) (new Acl$View (TableId/from-edn (get arg :id)))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.bigquery.Acl.View"
          {:arg arg}))))

(defn Acl$View-to-edn
  [^Acl$View arg]
  {:id (TableId/to-edn (.getId arg)), :type "VIEW"})

(def Acl$View-schema
  [:map
   {:closed true,
    :doc
      "Class for a BigQuery View entity. Objects of this class represent a view from a different\ndatasetAclEntity to grant access to. Queries executed against that view will have read access\nto tables in this datasetAclEntity. The role field is not required when this field is set. If\nthat view is updated by any user, access to the view needs to be granted again via an update\noperation.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.bindings.bigquery/Acl.View} [:type [:= "VIEW"]]
   [:id {:doc "@return Returns table's identity."}
    :gcp.bindings.bigquery/TableId]])

(defn ^Acl$Routine Acl$Routine-from-edn
  [arg]
  (cond
    (contains? arg :id) (new Acl$Routine (RoutineId/from-edn (get arg :id)))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.bigquery.Acl.Routine"
          {:arg arg}))))

(defn Acl$Routine-to-edn
  [^Acl$Routine arg]
  {:id (RoutineId/to-edn (.getId arg)), :type "ROUTINE"})

(def Acl$Routine-schema
  [:map
   {:closed true,
    :doc
      "Class for a BigQuery Routine entity. Objects of this class represent a routine from a different\ndatasetAclEntity to grant access to. Queries executed against that routine will have read\naccess to views/tables/routines in this datasetAclEntity. Only UDF is supported for now. The\nrole field is not required when this field is set. If that routine is updated by any user,\naccess to the routine needs to be granted again via an update operation.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.bindings.bigquery/Acl.Routine} [:type [:= "ROUTINE"]]
   [:id {:doc "@return Returns routine's identity."}
    :gcp.bindings.bigquery/RoutineId]])

(defn ^Acl$IamMember Acl$IamMember-from-edn
  [arg]
  (cond
    (contains? arg :iamMember) (new Acl$IamMember (get arg :iamMember))
    :else
      (throw
        (ex-info
          "No matching constructor found for com.google.cloud.bigquery.Acl.IamMember"
          {:arg arg}))))

(defn Acl$IamMember-to-edn
  [^Acl$IamMember arg]
  {:iamMember (.getIamMember arg), :type "IAM_MEMBER"})

(def Acl$IamMember-schema
  [:map
   {:closed true,
    :doc
      "Class for a BigQuery IamMember entity. Objects of this class represent a iamMember to grant\naccess to given the IAM Policy.",
    :gcp/category :nested/variant-pojo,
    :gcp/key :gcp.bindings.bigquery/Acl.IamMember} [:type [:= "IAM_MEMBER"]]
   [:iamMember {:doc "@return Returns iamMember."} [:string {:min 1}]]])

(defn ^Acl$Expr Acl$Expr-from-edn
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

(defn Acl$Expr-to-edn
  [^Acl$Expr arg]
  {:description (global/get-private-field arg "description"),
   :expression (global/get-private-field arg "expression"),
   :title (global/get-private-field arg "title"),
   :location (global/get-private-field arg "location")})

(def Acl$Expr-schema
  [:map
   {:closed true,
    :doc
      "Expr represents the conditional information related to dataset access policies.",
    :gcp/category :nested/pojo,
    :gcp/key :gcp.bindings.bigquery/Acl.Expr}
   [:description {:doc "Synthetic getter for description"} [:string {:min 1}]]
   [:expression {:doc "Synthetic getter for expression"} [:string {:min 1}]]
   [:location {:doc "Synthetic getter for location"} [:string {:min 1}]]
   [:title {:doc "Synthetic getter for title"} [:string {:min 1}]]])

(defn ^Acl from-edn
  [arg]
  (global/strict! :gcp.bindings.bigquery/Acl arg)
  (cond (gcp.global/valid? :gcp.bindings.bigquery/Acl.DatasetAclEntity arg)
          (Acl/of (Acl$DatasetAclEntity-from-edn arg))
        (get arg :datasetAclEntity) (Acl/of (Acl$DatasetAclEntity-from-edn
                                              (get arg :datasetAclEntity)))
        (gcp.global/valid? :gcp.bindings.bigquery/Acl.View arg)
          (Acl/of (Acl$View-from-edn arg))
        (get arg :view) (Acl/of (Acl$View-from-edn (get arg :view)))
        (gcp.global/valid? :gcp.bindings.bigquery/Acl.Routine arg)
          (Acl/of (Acl$Routine-from-edn arg))
        (get arg :routine) (Acl/of (Acl$Routine-from-edn (get arg :routine)))
        (and (get arg :entity) (get arg :role) (get arg :condition))
          (Acl/of (Acl$Entity-from-edn (get arg :entity))
                  (Acl$Role-from-edn (get arg :role))
                  (Acl$Expr-from-edn (get arg :condition)))
        (and (get arg :entity) (get arg :role))
          (Acl/of (Acl$Entity-from-edn (get arg :entity))
                  (Acl$Role-from-edn (get arg :role)))
        true (ex-info "failed to match edn for static-factory cond body"
                      {:arg arg, :key :gcp.bindings.bigquery/Acl})))

(defn to-edn
  [^Acl arg]
  {:post [(global/strict! :gcp.bindings.bigquery/Acl %)]}
  (clojure.core/cond
    (clojure.core/and (.getEntity arg)
                      (clojure.core/instance? Acl$DatasetAclEntity
                                              (.getEntity arg)))
      {:datasetAclEntity (Acl$DatasetAclEntity-to-edn (.getEntity arg))}
    (clojure.core/and (.getEntity arg)
                      (clojure.core/instance? Acl$View (.getEntity arg)))
      {:view (Acl$View-to-edn (.getEntity arg))}
    (clojure.core/and (.getEntity arg)
                      (clojure.core/instance? Acl$Routine (.getEntity arg)))
      {:routine (Acl$Routine-to-edn (.getEntity arg))}
    (clojure.core/and (.getEntity arg) (.getRole arg) (.getCondition arg))
      {:entity (Acl$Entity-to-edn (.getEntity arg)),
       :role (Acl$Role-to-edn (.getRole arg)),
       :condition (Acl$Expr-to-edn (.getCondition arg))}
    (clojure.core/and (.getEntity arg) (.getRole arg))
      {:entity (Acl$Entity-to-edn (.getEntity arg)),
       :role (Acl$Role-to-edn (.getRole arg))}
    :else (throw (clojure.core/ex-info
                   "failed to match edn for static-factory cond body"
                   {:key :gcp.bindings.bigquery/Acl, :arg arg}))))

(def schema
  [:or
   {:closed true,
    :doc
      "Access Control for a BigQuery Dataset. BigQuery uses ACLs to manage permissions on datasets. ACLs\nare not directly supported on tables. A table inherits its ACL from the dataset that contains it.\nProject roles affect your ability to run jobs or manage the project, while dataset roles affect\nhow you can access or modify the data inside a project.\n\n@see <a href=\"https://cloud.google.com/bigquery/access-control\">Access Control</a>",
    :gcp/category :static-factory,
    :gcp/key :gcp.bindings.bigquery/Acl}
   :gcp.bindings.bigquery/Acl.DatasetAclEntity
   [:map
    {:closed true,
     :doc
       "@param datasetAclEntity\n@return Returns an Acl object for a datasetAclEntity."}
    [:datasetAclEntity :gcp.bindings.bigquery/Acl.DatasetAclEntity]]
   :gcp.bindings.bigquery/Acl.View
   [:map
    {:closed true,
     :doc "@param view\n@return Returns an Acl object for a view entity."}
    [:view :gcp.bindings.bigquery/Acl.View]] :gcp.bindings.bigquery/Acl.Routine
   [:map
    {:closed true,
     :doc "@param routine\n@return Returns an Acl object for a routine entity."}
    [:routine :gcp.bindings.bigquery/Acl.Routine]]
   [:map
    {:closed true,
     :doc
       "@return Returns an Acl object.\n@param entity the entity for this ACL object\n@param role the role to associate to the {@code entity} object"}
    [:entity {:doc "@return Returns the entity for this ACL."}
     :gcp.bindings.bigquery/Acl.Entity]
    [:role {:doc "@return Returns the role specified by this ACL."}
     :gcp.bindings.bigquery/Acl.Role]]
   [:map {:closed true}
    [:entity {:doc "@return Returns the entity for this ACL."}
     :gcp.bindings.bigquery/Acl.Entity]
    [:role {:doc "@return Returns the role specified by this ACL."}
     :gcp.bindings.bigquery/Acl.Role]
    [:condition {:doc "@return Returns the condition specified by this ACL."}
     :gcp.bindings.bigquery/Acl.Expr]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/Acl schema,
              :gcp.bindings.bigquery/Acl.DatasetAclEntity
                Acl$DatasetAclEntity-schema,
              :gcp.bindings.bigquery/Acl.Domain Acl$Domain-schema,
              :gcp.bindings.bigquery/Acl.Entity Acl$Entity-schema,
              :gcp.bindings.bigquery/Acl.Entity.Type Acl$Entity$Type-schema,
              :gcp.bindings.bigquery/Acl.Expr Acl$Expr-schema,
              :gcp.bindings.bigquery/Acl.Group Acl$Group-schema,
              :gcp.bindings.bigquery/Acl.IamMember Acl$IamMember-schema,
              :gcp.bindings.bigquery/Acl.Role Acl$Role-schema,
              :gcp.bindings.bigquery/Acl.Routine Acl$Routine-schema,
              :gcp.bindings.bigquery/Acl.User Acl$User-schema,
              :gcp.bindings.bigquery/Acl.View Acl$View-schema}
    {:gcp.global/name "gcp.bindings.bigquery.Acl"}))