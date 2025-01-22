(ns gcp.bigquery.v2.Field
  (:require [gcp.bigquery.v2.FieldElementType :as FieldElementType]
            [gcp.bigquery.v2.PolicyTags :as PolicyTags]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery Field Field$Mode StandardSQLTypeName)))

(defn ^Field from-edn
  [{:keys [subFields
           name
           type
           collation
           defaultValueExpression
           description
           maxLength
           mode
           policyTags
           precision
           rangeElementType
           scale] :as arg}]
  (global/strict! :bigquery/Field arg)
  (let [builder (Field/newBuilder ^String name (StandardSQLTypeName/valueOf type) ^Field/1 (into-array Field (map from-edn subFields)))]
    (when collation
      (.setCollation builder collation))
    (when defaultValueExpression
      (.setDefaultValueExpression builder defaultValueExpression))
    (when description
      (.setDescription builder description))
    (when maxLength
      (.setMaxLength builder (long maxLength)))
    (when mode
      (.setMode builder (Field$Mode/valueOf mode)))
    (when policyTags
      (.setPolicyTags builder (PolicyTags/from-edn policyTags)))
    (when precision
      (.setPrecision builder (long precision)))
    (when rangeElementType
      (.setRangeElementType (FieldElementType/from-edn rangeElementType)))
    (when scale
      (.setScale builder (long scale)))
    (.build builder)))

(defn to-edn [^Field arg]
  (cond-> {:name (.getName arg)
           :type (.name (.getType arg))}

          (some? (.getMode arg))
          (assoc :mode (.name (.getMode arg)))

          (.getCollation arg)
          (assoc :collation (.getCollation arg))

          (.getDefaultValueExpression arg)
          (assoc :defaultValueExpression (.getDefaultValueExpression arg))

          (.getDescription arg)
          (assoc :description (.getDescription arg))

          (.getMaxLength arg)
          (assoc :maxLength (.getMaxLength arg))

          (.getPrecision arg)
          (assoc :precision (.getPrecision arg))

          (.getScale arg)
          (assoc :scale (.getScale arg))

          (.getPolicyTags arg)
          (assoc :policyTags (PolicyTags/to-edn (.getPolicyTags arg)))

          (.getRangeElementType arg)
          (assoc :rangeElementType (FieldElementType/to-edn (.getRangeElementType arg)))

          (pos? (count (.getSubFields arg)))
          (assoc :subFields (mapv to-edn (.getSubFields arg)))))