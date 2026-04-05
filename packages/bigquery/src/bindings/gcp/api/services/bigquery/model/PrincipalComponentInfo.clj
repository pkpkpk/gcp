;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.PrincipalComponentInfo
  {:doc
     "Principal component infos, used only for eigen decomposition based models, e.g., PCA. Ordered by\nexplained_variance in the descending order.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.PrincipalComponentInfo"
   :gcp.dev/certification
     {:base-seed 1775130886143
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1775130886143 :standard 1775130886144 :stress 1775130886145}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:54:47.304462473Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model PrincipalComponentInfo]))

(declare from-edn to-edn)

(defn ^PrincipalComponentInfo from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/PrincipalComponentInfo arg)
  (let [o (new PrincipalComponentInfo)]
    (when (some? (get arg :cumulativeExplainedVarianceRatio))
      (.setCumulativeExplainedVarianceRatio
        o
        (double (get arg :cumulativeExplainedVarianceRatio))))
    (when (some? (get arg :explainedVariance))
      (.setExplainedVariance o (double (get arg :explainedVariance))))
    (when (some? (get arg :explainedVarianceRatio))
      (.setExplainedVarianceRatio o (double (get arg :explainedVarianceRatio))))
    (when (some? (get arg :principalComponentId))
      (.setPrincipalComponentId o (long (get arg :principalComponentId))))
    o))

(defn to-edn
  [^PrincipalComponentInfo arg]
  {:post [(global/strict!
            :gcp.api.services.bigquery.model/PrincipalComponentInfo
            %)]}
  (when arg
    (cond-> {}
      (.getCumulativeExplainedVarianceRatio arg)
        (assoc :cumulativeExplainedVarianceRatio
          (.getCumulativeExplainedVarianceRatio arg))
      (.getExplainedVariance arg) (assoc :explainedVariance
                                    (.getExplainedVariance arg))
      (.getExplainedVarianceRatio arg) (assoc :explainedVarianceRatio
                                         (.getExplainedVarianceRatio arg))
      (.getPrincipalComponentId arg) (assoc :principalComponentId
                                       (.getPrincipalComponentId arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Principal component infos, used only for eigen decomposition based models, e.g., PCA. Ordered by\nexplained_variance in the descending order.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/PrincipalComponentInfo}
   [:cumulativeExplainedVarianceRatio
    {:getter-doc
       "The explained_variance is pre-ordered in the descending order to compute the cumulative\nexplained variance ratio.\n\n@return value or {@code null} for none",
     :setter-doc
       "The explained_variance is pre-ordered in the descending order to compute the cumulative\nexplained variance ratio.\n\n@param cumulativeExplainedVarianceRatio cumulativeExplainedVarianceRatio or {@code null} for none",
     :optional true} :f64]
   [:explainedVariance
    {:getter-doc
       "Explained variance by this principal component, which is simply the eigenvalue.\n\n@return value or {@code null} for none",
     :setter-doc
       "Explained variance by this principal component, which is simply the eigenvalue.\n\n@param explainedVariance explainedVariance or {@code null} for none",
     :optional true} :f64]
   [:explainedVarianceRatio
    {:getter-doc
       "Explained_variance over the total explained variance.\n\n@return value or {@code null} for none",
     :setter-doc
       "Explained_variance over the total explained variance.\n\n@param explainedVarianceRatio explainedVarianceRatio or {@code null} for none",
     :optional true} :f64]
   [:principalComponentId
    {:getter-doc
       "Id of the principal component.\n\n@return value or {@code null} for none",
     :setter-doc
       "Id of the principal component.\n\n@param principalComponentId principalComponentId or {@code null} for none",
     :optional true} :i64]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/PrincipalComponentInfo schema}
    {:gcp.global/name
       "gcp.api.services.bigquery.model.PrincipalComponentInfo"}))