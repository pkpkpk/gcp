(ns gcp.bigquery.v2.Field
  (:require [gcp.bigquery.v2.FieldElement :as FieldElement]
            [gcp.bigquery.v2.PolicyTags :as PolicyTags])
  (:import (com.google.cloud.bigquery Field
                                      Field$Mode)))

(def Field$Mode-mapping
  {Field$Mode/NULLABLE :nullable
   Field$Mode/REQUIRED :required
   Field$Mode/REPEATED :repeated
   "NULLABLE" Field$Mode/NULLABLE
   "REQUIRED" Field$Mode/REQUIRED
   "REPEATED" Field$Mode/REPEATED
   :nullable Field$Mode/NULLABLE
   :required Field$Mode/REQUIRED
   :repeated Field$Mode/REPEATED})

(defn from-edn [arg] (throw (Exception. "unimplemented")))

(defn to-edn [^Field arg]
  (cond-> {:name (.getName arg)
           :type (.name (.getType arg))
           :mode (.name (.getMode arg))}

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
          (assoc :rangeElementType (FieldElement/to-edn (.getRangeElementType arg)))

          (pos? (count (.getSubFields arg)))
          (assoc :subFields (mapv to-edn (.getSubFields arg)))))