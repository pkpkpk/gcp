(ns gcp.bigquery.v2.ViewDefinition
  (:require [gcp.bigquery.v2.Schema :as Schema]
            [gcp.bigquery.v2.UserDefinedFunction :as UserDefinedFunction])
  (:import (com.google.cloud.bigquery ViewDefinition)))

(defn ^ViewDefinition from-edn
  [{:keys [query schema userDefinedFunctions] :as arg}]
  (let [builder (ViewDefinition/newBuilder query)]
    ;; pretty sure this does nothing
    (some->> schema Schema/from-edn (.setSchema builder))
    (when (seq userDefinedFunctions)
      (.setUserDefinedFunctions builder (map UserDefinedFunction/from-edn userDefinedFunctions)))
    (.build builder)))

(defn to-edn [^ViewDefinition arg]
  (cond-> {:query (.getQuery arg)
           :type  (.name (.getType arg))}
          (pos? (count (.getUserDefinedFunctions arg)))
          (assoc :userDefinedFunctions (map UserDefinedFunction/to-edn (.getUserDefinedFunctions arg)))))