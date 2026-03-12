;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.PrincipalComponentInfo
  {:doc
     "Principal component infos, used only for eigen decomposition based models, e.g., PCA. Ordered by\nexplained_variance in the descending order.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.PrincipalComponentInfo"
   :gcp.dev/certification
     {:base-seed 1772390235326
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390235326 :standard 1772390235327 :stress 1772390235328}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:15.335575597Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model PrincipalComponentInfo]))

(defn ^PrincipalComponentInfo from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/PrincipalComponentInfo
                  arg)
  (let [o (new PrincipalComponentInfo)]
    (when (some? (get arg :cumulativeExplainedVarianceRatio))
      (.setCumulativeExplainedVarianceRatio
        o
        (get arg :cumulativeExplainedVarianceRatio)))
    (when (some? (get arg :explainedVariance))
      (.setExplainedVariance o (get arg :explainedVariance)))
    (when (some? (get arg :explainedVarianceRatio))
      (.setExplainedVarianceRatio o (get arg :explainedVarianceRatio)))
    (when (some? (get arg :principalComponentId))
      (.setPrincipalComponentId o (get arg :principalComponentId)))
    o))

(defn to-edn
  [^PrincipalComponentInfo arg]
  {:post [(global/strict!
            :gcp.bindings.services.bigquery.model/PrincipalComponentInfo
            %)]}
  (cond-> {}
    (.getCumulativeExplainedVarianceRatio arg)
      (assoc :cumulativeExplainedVarianceRatio
        (.getCumulativeExplainedVarianceRatio arg))
    (.getExplainedVariance arg) (assoc :explainedVariance
                                  (.getExplainedVariance arg))
    (.getExplainedVarianceRatio arg) (assoc :explainedVarianceRatio
                                       (.getExplainedVarianceRatio arg))
    (.getPrincipalComponentId arg) (assoc :principalComponentId
                                     (.getPrincipalComponentId arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Principal component infos, used only for eigen decomposition based models, e.g., PCA. Ordered by\nexplained_variance in the descending order.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/PrincipalComponentInfo}
   [:cumulativeExplainedVarianceRatio
    {:getter-doc
       "The explained_variance is pre-ordered in the descending order to compute the cumulative\nexplained variance ratio.\n\n@return value or {@code null} for none",
     :setter-doc
       "The explained_variance is pre-ordered in the descending order to compute the cumulative\nexplained variance ratio.\n\n@param cumulativeExplainedVarianceRatio cumulativeExplainedVarianceRatio or {@code null} for none",
     :optional true} :double]
   [:explainedVariance
    {:getter-doc
       "Explained variance by this principal component, which is simply the eigenvalue.\n\n@return value or {@code null} for none",
     :setter-doc
       "Explained variance by this principal component, which is simply the eigenvalue.\n\n@param explainedVariance explainedVariance or {@code null} for none",
     :optional true} :double]
   [:explainedVarianceRatio
    {:getter-doc
       "Explained_variance over the total explained variance.\n\n@return value or {@code null} for none",
     :setter-doc
       "Explained_variance over the total explained variance.\n\n@param explainedVarianceRatio explainedVarianceRatio or {@code null} for none",
     :optional true} :double]
   [:principalComponentId
    {:getter-doc
       "Id of the principal component.\n\n@return value or {@code null} for none",
     :setter-doc
       "Id of the principal component.\n\n@param principalComponentId principalComponentId or {@code null} for none",
     :optional true} :int]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/PrincipalComponentInfo
                schema}
    {:gcp.global/name
       "gcp.bindings.services.bigquery.model.PrincipalComponentInfo"}))