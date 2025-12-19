(ns gcp.bigquery.v2.ViewDefinition
  (:require [gcp.bigquery.v2.Schema :as Schema]
            [gcp.bigquery.v2.UserDefinedFunction :as UserDefinedFunction]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery ViewDefinition)))

(defn ^ViewDefinition from-edn
  [{:keys [query schema userDefinedFunctions] :as arg}]
  (global/strict! :gcp.bigquery.v2/ViewDefinition arg)
  (let [builder (ViewDefinition/newBuilder query)]
    ;; pretty sure this does nothing
    (some->> schema Schema/from-edn (.setSchema builder))
    (when (seq userDefinedFunctions)
      (.setUserDefinedFunctions builder (map UserDefinedFunction/from-edn userDefinedFunctions)))
    (.build builder)))

(defn to-edn [^ViewDefinition arg]
  {:post [(global/strict! :gcp.bigquery.v2/ViewDefinition %)]}
  (cond-> {:query (.getQuery arg)
           :type  (.name (.getType arg))}
          (pos? (count (.getUserDefinedFunctions arg)))
          (assoc :userDefinedFunctions (map UserDefinedFunction/to-edn (.getUserDefinedFunctions arg)))))

(def schemas
  {:gcp.bigquery.v2/ViewDefinition
   [:map {:closed true}
    [:type [:= "VIEW"]]
    [:query [:maybe :string]]
    [:userDefinedFunctions {:optional true} [:seqable :gcp.bigquery.v2/UserDefinedFunction]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))