;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.TrainingRun
  {:doc
     "Information about a single training query run for the model.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.TrainingRun"
   :gcp.dev/certification
     {:base-seed 1776499494781
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1776499494781 :standard 1776499494782 :stress 1776499494783}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-18T08:04:56.334615107Z"}}
  (:require
    [gcp.api.services.bigquery.model.DataSplitResult :as DataSplitResult]
    [gcp.api.services.bigquery.model.EvaluationMetrics :as EvaluationMetrics]
    [gcp.api.services.bigquery.model.GlobalExplanation :as GlobalExplanation]
    [gcp.api.services.bigquery.model.IterationResult :as IterationResult]
    [gcp.api.services.bigquery.model.TrainingOptions :as TrainingOptions]
    [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model TrainingRun]))

(declare from-edn to-edn)

(defn ^TrainingRun from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/TrainingRun arg)
  (let [o (new TrainingRun)]
    (when (some? (get arg :classLevelGlobalExplanations))
      (.setClassLevelGlobalExplanations o
                                        (mapv GlobalExplanation/from-edn
                                          (get arg
                                               :classLevelGlobalExplanations))))
    (when (some? (get arg :dataSplitResult))
      (.setDataSplitResult o
                           (DataSplitResult/from-edn (get arg
                                                          :dataSplitResult))))
    (when (some? (get arg :evaluationMetrics))
      (.setEvaluationMetrics o
                             (EvaluationMetrics/from-edn
                               (get arg :evaluationMetrics))))
    (when (some? (get arg :modelLevelGlobalExplanation))
      (.setModelLevelGlobalExplanation
        o
        (GlobalExplanation/from-edn (get arg :modelLevelGlobalExplanation))))
    (when (some? (get arg :results))
      (.setResults o (mapv IterationResult/from-edn (get arg :results))))
    (when (some? (get arg :startTime)) (.setStartTime o (get arg :startTime)))
    (when (some? (get arg :trainingOptions))
      (.setTrainingOptions o
                           (TrainingOptions/from-edn (get arg
                                                          :trainingOptions))))
    (when (some? (get arg :trainingStartTime))
      (.setTrainingStartTime o (long (get arg :trainingStartTime))))
    (when (some? (get arg :vertexAiModelId))
      (.setVertexAiModelId o (get arg :vertexAiModelId)))
    (when (some? (get arg :vertexAiModelVersion))
      (.setVertexAiModelVersion o (get arg :vertexAiModelVersion)))
    o))

(defn to-edn
  [^TrainingRun arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/TrainingRun %)]}
  (when arg
    (cond-> {}
      (seq (.getClassLevelGlobalExplanations arg))
        (assoc :classLevelGlobalExplanations
          (mapv GlobalExplanation/to-edn
            (.getClassLevelGlobalExplanations arg)))
      (.getDataSplitResult arg) (assoc :dataSplitResult
                                  (DataSplitResult/to-edn (.getDataSplitResult
                                                            arg)))
      (.getEvaluationMetrics arg) (assoc :evaluationMetrics
                                    (EvaluationMetrics/to-edn
                                      (.getEvaluationMetrics arg)))
      (.getModelLevelGlobalExplanation arg)
        (assoc :modelLevelGlobalExplanation
          (GlobalExplanation/to-edn (.getModelLevelGlobalExplanation arg)))
      (seq (.getResults arg)) (assoc :results
                                (mapv IterationResult/to-edn (.getResults arg)))
      (some->> (.getStartTime arg)
               (not= ""))
        (assoc :startTime (.getStartTime arg))
      (.getTrainingOptions arg) (assoc :trainingOptions
                                  (TrainingOptions/to-edn (.getTrainingOptions
                                                            arg)))
      (.getTrainingStartTime arg) (assoc :trainingStartTime
                                    (.getTrainingStartTime arg))
      (some->> (.getVertexAiModelId arg)
               (not= ""))
        (assoc :vertexAiModelId (.getVertexAiModelId arg))
      (some->> (.getVertexAiModelVersion arg)
               (not= ""))
        (assoc :vertexAiModelVersion (.getVertexAiModelVersion arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Information about a single training query run for the model.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/TrainingRun}
   [:classLevelGlobalExplanations
    {:getter-doc
       "Output only. Global explanation contains the explanation of top features on the class level.\nApplies to classification models only.\n\n@return value or {@code null} for none",
     :setter-doc
       "Output only. Global explanation contains the explanation of top features on the class level.\nApplies to classification models only.\n\n@param classLevelGlobalExplanations classLevelGlobalExplanations or {@code null} for none",
     :optional true}
    [:sequential {:min 1} :gcp.api.services.bigquery.model/GlobalExplanation]]
   [:dataSplitResult
    {:getter-doc
       "Output only. Data split result of the training run. Only set when the input data is actually\nsplit.\n\n@return value or {@code null} for none",
     :setter-doc
       "Output only. Data split result of the training run. Only set when the input data is actually\nsplit.\n\n@param dataSplitResult dataSplitResult or {@code null} for none",
     :optional true} :gcp.api.services.bigquery.model/DataSplitResult]
   [:evaluationMetrics
    {:getter-doc
       "Output only. The evaluation metrics over training/eval data that were computed at the end of\ntraining.\n\n@return value or {@code null} for none",
     :setter-doc
       "Output only. The evaluation metrics over training/eval data that were computed at the end of\ntraining.\n\n@param evaluationMetrics evaluationMetrics or {@code null} for none",
     :optional true} :gcp.api.services.bigquery.model/EvaluationMetrics]
   [:modelLevelGlobalExplanation
    {:getter-doc
       "Output only. Global explanation contains the explanation of top features on the model level.\nApplies to both regression and classification models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Output only. Global explanation contains the explanation of top features on the model level.\nApplies to both regression and classification models.\n\n@param modelLevelGlobalExplanation modelLevelGlobalExplanation or {@code null} for none",
     :optional true} :gcp.api.services.bigquery.model/GlobalExplanation]
   [:results
    {:getter-doc
       "Output only. Output of each iteration run, results.size() <= max_iterations.\n\n@return value or {@code null} for none",
     :setter-doc
       "Output only. Output of each iteration run, results.size() <= max_iterations.\n\n@param results results or {@code null} for none",
     :optional true}
    [:sequential {:min 1} :gcp.api.services.bigquery.model/IterationResult]]
   [:startTime
    {:getter-doc
       "Output only. The start time of this training run.\n\n@return value or {@code null} for none",
     :setter-doc
       "Output only. The start time of this training run.\n\n@param startTime startTime or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:trainingOptions
    {:getter-doc
       "Output only. Options that were used for this training run, includes user specified and default\noptions that were used.\n\n@return value or {@code null} for none",
     :setter-doc
       "Output only. Options that were used for this training run, includes user specified and default\noptions that were used.\n\n@param trainingOptions trainingOptions or {@code null} for none",
     :optional true} :gcp.api.services.bigquery.model/TrainingOptions]
   [:trainingStartTime
    {:getter-doc
       "Output only. The start time of this training run, in milliseconds since epoch.\n\n@return value or {@code null} for none",
     :setter-doc
       "Output only. The start time of this training run, in milliseconds since epoch.\n\n@param trainingStartTime trainingStartTime or {@code null} for none",
     :optional true} :i64]
   [:vertexAiModelId
    {:getter-doc
       "The model id in the [Vertex AI Model Registry](https://cloud.google.com/vertex-ai/docs/model-\nregistry/introduction) for this training run.\n\n@return value or {@code null} for none",
     :setter-doc
       "The model id in the [Vertex AI Model Registry](https://cloud.google.com/vertex-ai/docs/model-\nregistry/introduction) for this training run.\n\n@param vertexAiModelId vertexAiModelId or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:vertexAiModelVersion
    {:getter-doc
       "Output only. The model version in the [Vertex AI Model\nRegistry](https://cloud.google.com/vertex-ai/docs/model-registry/introduction) for this\ntraining run.\n\n@return value or {@code null} for none",
     :setter-doc
       "Output only. The model version in the [Vertex AI Model\nRegistry](https://cloud.google.com/vertex-ai/docs/model-registry/introduction) for this\ntraining run.\n\n@param vertexAiModelVersion vertexAiModelVersion or {@code null} for none",
     :optional true} [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/TrainingRun schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.TrainingRun"}))