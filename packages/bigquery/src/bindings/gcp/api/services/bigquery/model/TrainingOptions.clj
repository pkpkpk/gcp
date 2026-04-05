;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.TrainingOptions
  {:doc
     "Options used in model training.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.TrainingOptions"
   :gcp.dev/certification
     {:base-seed 1775131000576
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1775131000576 :standard 1775131000577 :stress 1775131000578}
      :protocol-hash
        "f27f34d24f3d81b3e05f9de655c6ce1de28b53e620c5f9c1978cbce793727f86"
      :timestamp "2026-04-02T11:56:41.928207800Z"}}
  (:require [gcp.api.services.bigquery.model.ArimaOrder :as ArimaOrder]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model TrainingOptions]))

(declare from-edn to-edn)

(defn ^TrainingOptions from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/TrainingOptions arg)
  (let [o (new TrainingOptions)]
    (when (some? (get arg :activationFn))
      (.setActivationFn o (get arg :activationFn)))
    (when (some? (get arg :adjustStepChanges))
      (.setAdjustStepChanges o (get arg :adjustStepChanges)))
    (when (some? (get arg :approxGlobalFeatureContrib))
      (.setApproxGlobalFeatureContrib o (get arg :approxGlobalFeatureContrib)))
    (when (some? (get arg :autoArima)) (.setAutoArima o (get arg :autoArima)))
    (when (some? (get arg :autoArimaMaxOrder))
      (.setAutoArimaMaxOrder o (long (get arg :autoArimaMaxOrder))))
    (when (some? (get arg :autoArimaMinOrder))
      (.setAutoArimaMinOrder o (long (get arg :autoArimaMinOrder))))
    (when (some? (get arg :autoClassWeights))
      (.setAutoClassWeights o (get arg :autoClassWeights)))
    (when (some? (get arg :batchSize))
      (.setBatchSize o (long (get arg :batchSize))))
    (when (some? (get arg :boosterType))
      (.setBoosterType o (get arg :boosterType)))
    (when (some? (get arg :budgetHours))
      (.setBudgetHours o (double (get arg :budgetHours))))
    (when (some? (get arg :calculatePValues))
      (.setCalculatePValues o (get arg :calculatePValues)))
    (when (some? (get arg :categoryEncodingMethod))
      (.setCategoryEncodingMethod o (get arg :categoryEncodingMethod)))
    (when (some? (get arg :cleanSpikesAndDips))
      (.setCleanSpikesAndDips o (get arg :cleanSpikesAndDips)))
    (when (some? (get arg :colorSpace))
      (.setColorSpace o (get arg :colorSpace)))
    (when (some? (get arg :colsampleBylevel))
      (.setColsampleBylevel o (double (get arg :colsampleBylevel))))
    (when (some? (get arg :colsampleBynode))
      (.setColsampleBynode o (double (get arg :colsampleBynode))))
    (when (some? (get arg :colsampleBytree))
      (.setColsampleBytree o (double (get arg :colsampleBytree))))
    (when (some? (get arg :contributionMetric))
      (.setContributionMetric o (get arg :contributionMetric)))
    (when (some? (get arg :dartNormalizeType))
      (.setDartNormalizeType o (get arg :dartNormalizeType)))
    (when (some? (get arg :dataFrequency))
      (.setDataFrequency o (get arg :dataFrequency)))
    (when (some? (get arg :dataSplitColumn))
      (.setDataSplitColumn o (get arg :dataSplitColumn)))
    (when (some? (get arg :dataSplitEvalFraction))
      (.setDataSplitEvalFraction o (double (get arg :dataSplitEvalFraction))))
    (when (some? (get arg :dataSplitMethod))
      (.setDataSplitMethod o (get arg :dataSplitMethod)))
    (when (some? (get arg :decomposeTimeSeries))
      (.setDecomposeTimeSeries o (get arg :decomposeTimeSeries)))
    (when (some? (get arg :dimensionIdColumns))
      (.setDimensionIdColumns o (seq (get arg :dimensionIdColumns))))
    (when (some? (get arg :distanceType))
      (.setDistanceType o (get arg :distanceType)))
    (when (some? (get arg :dropout))
      (.setDropout o (double (get arg :dropout))))
    (when (some? (get arg :earlyStop)) (.setEarlyStop o (get arg :earlyStop)))
    (when (some? (get arg :enableGlobalExplain))
      (.setEnableGlobalExplain o (get arg :enableGlobalExplain)))
    (when (some? (get arg :endpointIdleTtl))
      (.setEndpointIdleTtl o (get arg :endpointIdleTtl)))
    (when (some? (get arg :feedbackType))
      (.setFeedbackType o (get arg :feedbackType)))
    (when (some? (get arg :fitIntercept))
      (.setFitIntercept o (get arg :fitIntercept)))
    (when (some? (get arg :forecastLimitLowerBound))
      (.setForecastLimitLowerBound o
                                   (double (get arg :forecastLimitLowerBound))))
    (when (some? (get arg :forecastLimitUpperBound))
      (.setForecastLimitUpperBound o
                                   (double (get arg :forecastLimitUpperBound))))
    (when (some? (get arg :hiddenUnits))
      (.setHiddenUnits o (seq (get arg :hiddenUnits))))
    (when (some? (get arg :holidayRegion))
      (.setHolidayRegion o (get arg :holidayRegion)))
    (when (some? (get arg :holidayRegions))
      (.setHolidayRegions o (seq (get arg :holidayRegions))))
    (when (some? (get arg :horizon)) (.setHorizon o (long (get arg :horizon))))
    (when (some? (get arg :hparamTuningObjectives))
      (.setHparamTuningObjectives o (seq (get arg :hparamTuningObjectives))))
    (when (some? (get arg :huggingFaceModelId))
      (.setHuggingFaceModelId o (get arg :huggingFaceModelId)))
    (when (some? (get arg :includeDrift))
      (.setIncludeDrift o (get arg :includeDrift)))
    (when (some? (get arg :initialLearnRate))
      (.setInitialLearnRate o (double (get arg :initialLearnRate))))
    (when (some? (get arg :inputLabelColumns))
      (.setInputLabelColumns o (seq (get arg :inputLabelColumns))))
    (when (some? (get arg :instanceWeightColumn))
      (.setInstanceWeightColumn o (get arg :instanceWeightColumn)))
    (when (some? (get arg :integratedGradientsNumSteps))
      (.setIntegratedGradientsNumSteps
        o
        (long (get arg :integratedGradientsNumSteps))))
    (when (some? (get arg :itemColumn))
      (.setItemColumn o (get arg :itemColumn)))
    (when (some? (get arg :kmeansInitializationColumn))
      (.setKmeansInitializationColumn o (get arg :kmeansInitializationColumn)))
    (when (some? (get arg :kmeansInitializationMethod))
      (.setKmeansInitializationMethod o (get arg :kmeansInitializationMethod)))
    (when (some? (get arg :l1RegActivation))
      (.setL1RegActivation o (double (get arg :l1RegActivation))))
    (when (some? (get arg :l1Regularization))
      (.setL1Regularization o (double (get arg :l1Regularization))))
    (when (some? (get arg :l2Regularization))
      (.setL2Regularization o (double (get arg :l2Regularization))))
    (when (some? (get arg :labelClassWeights))
      (.setLabelClassWeights
        o
        (into {} (map (fn [[k v]] [(name k) v])) (get arg :labelClassWeights))))
    (when (some? (get arg :learnRate))
      (.setLearnRate o (double (get arg :learnRate))))
    (when (some? (get arg :learnRateStrategy))
      (.setLearnRateStrategy o (get arg :learnRateStrategy)))
    (when (some? (get arg :lossType)) (.setLossType o (get arg :lossType)))
    (when (some? (get arg :machineType))
      (.setMachineType o (get arg :machineType)))
    (when (some? (get arg :maxIterations))
      (.setMaxIterations o (long (get arg :maxIterations))))
    (when (some? (get arg :maxParallelTrials))
      (.setMaxParallelTrials o (long (get arg :maxParallelTrials))))
    (when (some? (get arg :maxReplicaCount))
      (.setMaxReplicaCount o (long (get arg :maxReplicaCount))))
    (when (some? (get arg :maxTimeSeriesLength))
      (.setMaxTimeSeriesLength o (long (get arg :maxTimeSeriesLength))))
    (when (some? (get arg :maxTreeDepth))
      (.setMaxTreeDepth o (long (get arg :maxTreeDepth))))
    (when (some? (get arg :minAprioriSupport))
      (.setMinAprioriSupport o (double (get arg :minAprioriSupport))))
    (when (some? (get arg :minRelativeProgress))
      (.setMinRelativeProgress o (double (get arg :minRelativeProgress))))
    (when (some? (get arg :minReplicaCount))
      (.setMinReplicaCount o (long (get arg :minReplicaCount))))
    (when (some? (get arg :minSplitLoss))
      (.setMinSplitLoss o (double (get arg :minSplitLoss))))
    (when (some? (get arg :minTimeSeriesLength))
      (.setMinTimeSeriesLength o (long (get arg :minTimeSeriesLength))))
    (when (some? (get arg :minTreeChildWeight))
      (.setMinTreeChildWeight o (long (get arg :minTreeChildWeight))))
    (when (some? (get arg :modelGardenModelName))
      (.setModelGardenModelName o (get arg :modelGardenModelName)))
    (when (some? (get arg :modelRegistry))
      (.setModelRegistry o (get arg :modelRegistry)))
    (when (some? (get arg :modelUri)) (.setModelUri o (get arg :modelUri)))
    (when (some? (get arg :nonSeasonalOrder))
      (.setNonSeasonalOrder o
                            (ArimaOrder/from-edn (get arg :nonSeasonalOrder))))
    (when (some? (get arg :numClusters))
      (.setNumClusters o (long (get arg :numClusters))))
    (when (some? (get arg :numFactors))
      (.setNumFactors o (long (get arg :numFactors))))
    (when (some? (get arg :numParallelTree))
      (.setNumParallelTree o (long (get arg :numParallelTree))))
    (when (some? (get arg :numPrincipalComponents))
      (.setNumPrincipalComponents o (long (get arg :numPrincipalComponents))))
    (when (some? (get arg :numTrials))
      (.setNumTrials o (long (get arg :numTrials))))
    (when (some? (get arg :optimizationStrategy))
      (.setOptimizationStrategy o (get arg :optimizationStrategy)))
    (when (some? (get arg :optimizer)) (.setOptimizer o (get arg :optimizer)))
    (when (some? (get arg :pcaExplainedVarianceRatio))
      (.setPcaExplainedVarianceRatio o
                                     (double (get arg
                                                  :pcaExplainedVarianceRatio))))
    (when (some? (get arg :pcaSolver)) (.setPcaSolver o (get arg :pcaSolver)))
    (when (some? (get arg :reservationAffinityKey))
      (.setReservationAffinityKey o (get arg :reservationAffinityKey)))
    (when (some? (get arg :reservationAffinityType))
      (.setReservationAffinityType o (get arg :reservationAffinityType)))
    (when (some? (get arg :reservationAffinityValues))
      (.setReservationAffinityValues o
                                     (seq (get arg
                                               :reservationAffinityValues))))
    (when (some? (get arg :sampledShapleyNumPaths))
      (.setSampledShapleyNumPaths o (long (get arg :sampledShapleyNumPaths))))
    (when (some? (get arg :scaleFeatures))
      (.setScaleFeatures o (get arg :scaleFeatures)))
    (when (some? (get arg :standardizeFeatures))
      (.setStandardizeFeatures o (get arg :standardizeFeatures)))
    (when (some? (get arg :subsample))
      (.setSubsample o (double (get arg :subsample))))
    (when (some? (get arg :testColumn))
      (.setIsTestColumn o (get arg :isTestColumn)))
    (when (some? (get arg :tfVersion)) (.setTfVersion o (get arg :tfVersion)))
    (when (some? (get arg :timeSeriesDataColumn))
      (.setTimeSeriesDataColumn o (get arg :timeSeriesDataColumn)))
    (when (some? (get arg :timeSeriesIdColumn))
      (.setTimeSeriesIdColumn o (get arg :timeSeriesIdColumn)))
    (when (some? (get arg :timeSeriesIdColumns))
      (.setTimeSeriesIdColumns o (seq (get arg :timeSeriesIdColumns))))
    (when (some? (get arg :timeSeriesLengthFraction))
      (.setTimeSeriesLengthFraction o
                                    (double (get arg
                                                 :timeSeriesLengthFraction))))
    (when (some? (get arg :timeSeriesTimestampColumn))
      (.setTimeSeriesTimestampColumn o (get arg :timeSeriesTimestampColumn)))
    (when (some? (get arg :treeMethod))
      (.setTreeMethod o (get arg :treeMethod)))
    (when (some? (get arg :trendSmoothingWindowSize))
      (.setTrendSmoothingWindowSize o
                                    (long (get arg :trendSmoothingWindowSize))))
    (when (some? (get arg :userColumn))
      (.setUserColumn o (get arg :userColumn)))
    (when (some? (get arg :vertexAiModelVersionAliases))
      (.setVertexAiModelVersionAliases
        o
        (seq (get arg :vertexAiModelVersionAliases))))
    (when (some? (get arg :walsAlpha))
      (.setWalsAlpha o (double (get arg :walsAlpha))))
    (when (some? (get arg :warmStart)) (.setWarmStart o (get arg :warmStart)))
    (when (some? (get arg :xgboostVersion))
      (.setXgboostVersion o (get arg :xgboostVersion)))
    o))

(defn to-edn
  [^TrainingOptions arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/TrainingOptions %)]}
  (when arg
    (cond-> {}
      (some->> (.getActivationFn arg)
               (not= ""))
        (assoc :activationFn (.getActivationFn arg))
      (.getAdjustStepChanges arg) (assoc :adjustStepChanges
                                    (.getAdjustStepChanges arg))
      (.getApproxGlobalFeatureContrib arg)
        (assoc :approxGlobalFeatureContrib (.getApproxGlobalFeatureContrib arg))
      (.getAutoArima arg) (assoc :autoArima (.getAutoArima arg))
      (.getAutoArimaMaxOrder arg) (assoc :autoArimaMaxOrder
                                    (.getAutoArimaMaxOrder arg))
      (.getAutoArimaMinOrder arg) (assoc :autoArimaMinOrder
                                    (.getAutoArimaMinOrder arg))
      (.getAutoClassWeights arg) (assoc :autoClassWeights
                                   (.getAutoClassWeights arg))
      (.getBatchSize arg) (assoc :batchSize (.getBatchSize arg))
      (some->> (.getBoosterType arg)
               (not= ""))
        (assoc :boosterType (.getBoosterType arg))
      (.getBudgetHours arg) (assoc :budgetHours (.getBudgetHours arg))
      (.getCalculatePValues arg) (assoc :calculatePValues
                                   (.getCalculatePValues arg))
      (some->> (.getCategoryEncodingMethod arg)
               (not= ""))
        (assoc :categoryEncodingMethod (.getCategoryEncodingMethod arg))
      (.getCleanSpikesAndDips arg) (assoc :cleanSpikesAndDips
                                     (.getCleanSpikesAndDips arg))
      (some->> (.getColorSpace arg)
               (not= ""))
        (assoc :colorSpace (.getColorSpace arg))
      (.getColsampleBylevel arg) (assoc :colsampleBylevel
                                   (.getColsampleBylevel arg))
      (.getColsampleBynode arg) (assoc :colsampleBynode
                                  (.getColsampleBynode arg))
      (.getColsampleBytree arg) (assoc :colsampleBytree
                                  (.getColsampleBytree arg))
      (some->> (.getContributionMetric arg)
               (not= ""))
        (assoc :contributionMetric (.getContributionMetric arg))
      (some->> (.getDartNormalizeType arg)
               (not= ""))
        (assoc :dartNormalizeType (.getDartNormalizeType arg))
      (some->> (.getDataFrequency arg)
               (not= ""))
        (assoc :dataFrequency (.getDataFrequency arg))
      (some->> (.getDataSplitColumn arg)
               (not= ""))
        (assoc :dataSplitColumn (.getDataSplitColumn arg))
      (.getDataSplitEvalFraction arg) (assoc :dataSplitEvalFraction
                                        (.getDataSplitEvalFraction arg))
      (some->> (.getDataSplitMethod arg)
               (not= ""))
        (assoc :dataSplitMethod (.getDataSplitMethod arg))
      (.getDecomposeTimeSeries arg) (assoc :decomposeTimeSeries
                                      (.getDecomposeTimeSeries arg))
      (seq (.getDimensionIdColumns arg)) (assoc :dimensionIdColumns
                                           (seq (.getDimensionIdColumns arg)))
      (some->> (.getDistanceType arg)
               (not= ""))
        (assoc :distanceType (.getDistanceType arg))
      (.getDropout arg) (assoc :dropout (.getDropout arg))
      (.getEarlyStop arg) (assoc :earlyStop (.getEarlyStop arg))
      (.getEnableGlobalExplain arg) (assoc :enableGlobalExplain
                                      (.getEnableGlobalExplain arg))
      (some->> (.getEndpointIdleTtl arg)
               (not= ""))
        (assoc :endpointIdleTtl (.getEndpointIdleTtl arg))
      (some->> (.getFeedbackType arg)
               (not= ""))
        (assoc :feedbackType (.getFeedbackType arg))
      (.getFitIntercept arg) (assoc :fitIntercept (.getFitIntercept arg))
      (.getForecastLimitLowerBound arg) (assoc :forecastLimitLowerBound
                                          (.getForecastLimitLowerBound arg))
      (.getForecastLimitUpperBound arg) (assoc :forecastLimitUpperBound
                                          (.getForecastLimitUpperBound arg))
      (seq (.getHiddenUnits arg)) (assoc :hiddenUnits
                                    (seq (.getHiddenUnits arg)))
      (some->> (.getHolidayRegion arg)
               (not= ""))
        (assoc :holidayRegion (.getHolidayRegion arg))
      (seq (.getHolidayRegions arg)) (assoc :holidayRegions
                                       (seq (.getHolidayRegions arg)))
      (.getHorizon arg) (assoc :horizon (.getHorizon arg))
      (seq (.getHparamTuningObjectives arg))
        (assoc :hparamTuningObjectives (seq (.getHparamTuningObjectives arg)))
      (some->> (.getHuggingFaceModelId arg)
               (not= ""))
        (assoc :huggingFaceModelId (.getHuggingFaceModelId arg))
      (.getIncludeDrift arg) (assoc :includeDrift (.getIncludeDrift arg))
      (.getInitialLearnRate arg) (assoc :initialLearnRate
                                   (.getInitialLearnRate arg))
      (seq (.getInputLabelColumns arg)) (assoc :inputLabelColumns
                                          (seq (.getInputLabelColumns arg)))
      (some->> (.getInstanceWeightColumn arg)
               (not= ""))
        (assoc :instanceWeightColumn (.getInstanceWeightColumn arg))
      (.getIntegratedGradientsNumSteps arg) (assoc :integratedGradientsNumSteps
                                              (.getIntegratedGradientsNumSteps
                                                arg))
      (some->> (.getItemColumn arg)
               (not= ""))
        (assoc :itemColumn (.getItemColumn arg))
      (some->> (.getKmeansInitializationColumn arg)
               (not= ""))
        (assoc :kmeansInitializationColumn (.getKmeansInitializationColumn arg))
      (some->> (.getKmeansInitializationMethod arg)
               (not= ""))
        (assoc :kmeansInitializationMethod (.getKmeansInitializationMethod arg))
      (.getL1RegActivation arg) (assoc :l1RegActivation
                                  (.getL1RegActivation arg))
      (.getL1Regularization arg) (assoc :l1Regularization
                                   (.getL1Regularization arg))
      (.getL2Regularization arg) (assoc :l2Regularization
                                   (.getL2Regularization arg))
      (seq (.getLabelClassWeights arg)) (assoc :labelClassWeights
                                          (into {}
                                                (map (fn [[k v]] [(keyword k)
                                                                  v]))
                                                (.getLabelClassWeights arg)))
      (.getLearnRate arg) (assoc :learnRate (.getLearnRate arg))
      (some->> (.getLearnRateStrategy arg)
               (not= ""))
        (assoc :learnRateStrategy (.getLearnRateStrategy arg))
      (some->> (.getLossType arg)
               (not= ""))
        (assoc :lossType (.getLossType arg))
      (some->> (.getMachineType arg)
               (not= ""))
        (assoc :machineType (.getMachineType arg))
      (.getMaxIterations arg) (assoc :maxIterations (.getMaxIterations arg))
      (.getMaxParallelTrials arg) (assoc :maxParallelTrials
                                    (.getMaxParallelTrials arg))
      (.getMaxReplicaCount arg) (assoc :maxReplicaCount
                                  (.getMaxReplicaCount arg))
      (.getMaxTimeSeriesLength arg) (assoc :maxTimeSeriesLength
                                      (.getMaxTimeSeriesLength arg))
      (.getMaxTreeDepth arg) (assoc :maxTreeDepth (.getMaxTreeDepth arg))
      (.getMinAprioriSupport arg) (assoc :minAprioriSupport
                                    (.getMinAprioriSupport arg))
      (.getMinRelativeProgress arg) (assoc :minRelativeProgress
                                      (.getMinRelativeProgress arg))
      (.getMinReplicaCount arg) (assoc :minReplicaCount
                                  (.getMinReplicaCount arg))
      (.getMinSplitLoss arg) (assoc :minSplitLoss (.getMinSplitLoss arg))
      (.getMinTimeSeriesLength arg) (assoc :minTimeSeriesLength
                                      (.getMinTimeSeriesLength arg))
      (.getMinTreeChildWeight arg) (assoc :minTreeChildWeight
                                     (.getMinTreeChildWeight arg))
      (some->> (.getModelGardenModelName arg)
               (not= ""))
        (assoc :modelGardenModelName (.getModelGardenModelName arg))
      (some->> (.getModelRegistry arg)
               (not= ""))
        (assoc :modelRegistry (.getModelRegistry arg))
      (some->> (.getModelUri arg)
               (not= ""))
        (assoc :modelUri (.getModelUri arg))
      (.getNonSeasonalOrder arg)
        (assoc :nonSeasonalOrder (ArimaOrder/to-edn (.getNonSeasonalOrder arg)))
      (.getNumClusters arg) (assoc :numClusters (.getNumClusters arg))
      (.getNumFactors arg) (assoc :numFactors (.getNumFactors arg))
      (.getNumParallelTree arg) (assoc :numParallelTree
                                  (.getNumParallelTree arg))
      (.getNumPrincipalComponents arg) (assoc :numPrincipalComponents
                                         (.getNumPrincipalComponents arg))
      (.getNumTrials arg) (assoc :numTrials (.getNumTrials arg))
      (some->> (.getOptimizationStrategy arg)
               (not= ""))
        (assoc :optimizationStrategy (.getOptimizationStrategy arg))
      (some->> (.getOptimizer arg)
               (not= ""))
        (assoc :optimizer (.getOptimizer arg))
      (.getPcaExplainedVarianceRatio arg) (assoc :pcaExplainedVarianceRatio
                                            (.getPcaExplainedVarianceRatio arg))
      (some->> (.getPcaSolver arg)
               (not= ""))
        (assoc :pcaSolver (.getPcaSolver arg))
      (some->> (.getReservationAffinityKey arg)
               (not= ""))
        (assoc :reservationAffinityKey (.getReservationAffinityKey arg))
      (some->> (.getReservationAffinityType arg)
               (not= ""))
        (assoc :reservationAffinityType (.getReservationAffinityType arg))
      (seq (.getReservationAffinityValues arg))
        (assoc :reservationAffinityValues
          (seq (.getReservationAffinityValues arg)))
      (.getSampledShapleyNumPaths arg) (assoc :sampledShapleyNumPaths
                                         (.getSampledShapleyNumPaths arg))
      (.getScaleFeatures arg) (assoc :scaleFeatures (.getScaleFeatures arg))
      (.getStandardizeFeatures arg) (assoc :standardizeFeatures
                                      (.getStandardizeFeatures arg))
      (.getSubsample arg) (assoc :subsample (.getSubsample arg))
      (some->> (.getIsTestColumn arg)
               (not= ""))
        (assoc :testColumn (.getIsTestColumn arg))
      (some->> (.getTfVersion arg)
               (not= ""))
        (assoc :tfVersion (.getTfVersion arg))
      (some->> (.getTimeSeriesDataColumn arg)
               (not= ""))
        (assoc :timeSeriesDataColumn (.getTimeSeriesDataColumn arg))
      (some->> (.getTimeSeriesIdColumn arg)
               (not= ""))
        (assoc :timeSeriesIdColumn (.getTimeSeriesIdColumn arg))
      (seq (.getTimeSeriesIdColumns arg)) (assoc :timeSeriesIdColumns
                                            (seq (.getTimeSeriesIdColumns arg)))
      (.getTimeSeriesLengthFraction arg) (assoc :timeSeriesLengthFraction
                                           (.getTimeSeriesLengthFraction arg))
      (some->> (.getTimeSeriesTimestampColumn arg)
               (not= ""))
        (assoc :timeSeriesTimestampColumn (.getTimeSeriesTimestampColumn arg))
      (some->> (.getTreeMethod arg)
               (not= ""))
        (assoc :treeMethod (.getTreeMethod arg))
      (.getTrendSmoothingWindowSize arg) (assoc :trendSmoothingWindowSize
                                           (.getTrendSmoothingWindowSize arg))
      (some->> (.getUserColumn arg)
               (not= ""))
        (assoc :userColumn (.getUserColumn arg))
      (seq (.getVertexAiModelVersionAliases arg))
        (assoc :vertexAiModelVersionAliases
          (seq (.getVertexAiModelVersionAliases arg)))
      (.getWalsAlpha arg) (assoc :walsAlpha (.getWalsAlpha arg))
      (.getWarmStart arg) (assoc :warmStart (.getWarmStart arg))
      (some->> (.getXgboostVersion arg)
               (not= ""))
        (assoc :xgboostVersion (.getXgboostVersion arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Options used in model training.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/TrainingOptions}
   [:activationFn
    {:getter-doc
       "Activation function of the neural nets.\n\n@return value or {@code null} for none",
     :setter-doc
       "Activation function of the neural nets.\n\n@param activationFn activationFn or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:adjustStepChanges
    {:getter-doc
       "If true, detect step changes and make data adjustment in the input time series.\n\n@return value or {@code null} for none",
     :setter-doc
       "If true, detect step changes and make data adjustment in the input time series.\n\n@param adjustStepChanges adjustStepChanges or {@code null} for none",
     :optional true} :boolean]
   [:approxGlobalFeatureContrib
    {:getter-doc
       "Whether to use approximate feature contribution method in XGBoost model explanation for global\nexplain.\n\n@return value or {@code null} for none",
     :setter-doc
       "Whether to use approximate feature contribution method in XGBoost model explanation for global\nexplain.\n\n@param approxGlobalFeatureContrib approxGlobalFeatureContrib or {@code null} for none",
     :optional true} :boolean]
   [:autoArima
    {:getter-doc
       "Whether to enable auto ARIMA or not.\n\n@return value or {@code null} for none",
     :setter-doc
       "Whether to enable auto ARIMA or not.\n\n@param autoArima autoArima or {@code null} for none",
     :optional true} :boolean]
   [:autoArimaMaxOrder
    {:getter-doc
       "The max value of the sum of non-seasonal p and q.\n\n@return value or {@code null} for none",
     :setter-doc
       "The max value of the sum of non-seasonal p and q.\n\n@param autoArimaMaxOrder autoArimaMaxOrder or {@code null} for none",
     :optional true} :i64]
   [:autoArimaMinOrder
    {:getter-doc
       "The min value of the sum of non-seasonal p and q.\n\n@return value or {@code null} for none",
     :setter-doc
       "The min value of the sum of non-seasonal p and q.\n\n@param autoArimaMinOrder autoArimaMinOrder or {@code null} for none",
     :optional true} :i64]
   [:autoClassWeights
    {:getter-doc
       "Whether to calculate class weights automatically based on the popularity of each label.\n\n@return value or {@code null} for none",
     :setter-doc
       "Whether to calculate class weights automatically based on the popularity of each label.\n\n@param autoClassWeights autoClassWeights or {@code null} for none",
     :optional true} :boolean]
   [:batchSize
    {:getter-doc
       "Batch size for dnn models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Batch size for dnn models.\n\n@param batchSize batchSize or {@code null} for none",
     :optional true} :i64]
   [:boosterType
    {:getter-doc
       "Booster type for boosted tree models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Booster type for boosted tree models.\n\n@param boosterType boosterType or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:budgetHours
    {:getter-doc
       "Budget in hours for AutoML training.\n\n@return value or {@code null} for none",
     :setter-doc
       "Budget in hours for AutoML training.\n\n@param budgetHours budgetHours or {@code null} for none",
     :optional true} :f64]
   [:calculatePValues
    {:getter-doc
       "Whether or not p-value test should be computed for this model. Only available for linear and\nlogistic regression models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Whether or not p-value test should be computed for this model. Only available for linear and\nlogistic regression models.\n\n@param calculatePValues calculatePValues or {@code null} for none",
     :optional true} :boolean]
   [:categoryEncodingMethod
    {:getter-doc
       "Categorical feature encoding method.\n\n@return value or {@code null} for none",
     :setter-doc
       "Categorical feature encoding method.\n\n@param categoryEncodingMethod categoryEncodingMethod or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:cleanSpikesAndDips
    {:getter-doc
       "If true, clean spikes and dips in the input time series.\n\n@return value or {@code null} for none",
     :setter-doc
       "If true, clean spikes and dips in the input time series.\n\n@param cleanSpikesAndDips cleanSpikesAndDips or {@code null} for none",
     :optional true} :boolean]
   [:colorSpace
    {:getter-doc
       "Enums for color space, used for processing images in Object Table. See more details at\nhttps://www.tensorflow.org/io/tutorials/colorspace.\n\n@return value or {@code null} for none",
     :setter-doc
       "Enums for color space, used for processing images in Object Table. See more details at\nhttps://www.tensorflow.org/io/tutorials/colorspace.\n\n@param colorSpace colorSpace or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:colsampleBylevel
    {:getter-doc
       "Subsample ratio of columns for each level for boosted tree models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Subsample ratio of columns for each level for boosted tree models.\n\n@param colsampleBylevel colsampleBylevel or {@code null} for none",
     :optional true} :f64]
   [:colsampleBynode
    {:getter-doc
       "Subsample ratio of columns for each node(split) for boosted tree models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Subsample ratio of columns for each node(split) for boosted tree models.\n\n@param colsampleBynode colsampleBynode or {@code null} for none",
     :optional true} :f64]
   [:colsampleBytree
    {:getter-doc
       "Subsample ratio of columns when constructing each tree for boosted tree models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Subsample ratio of columns when constructing each tree for boosted tree models.\n\n@param colsampleBytree colsampleBytree or {@code null} for none",
     :optional true} :f64]
   [:contributionMetric
    {:getter-doc
       "The contribution metric. Applies to contribution analysis models. Allowed formats supported are\nfor summable and summable ratio contribution metrics. These include expressions such as\n`SUM(x)` or `SUM(x)/SUM(y)`, where x and y are column names from the base table.\n\n@return value or {@code null} for none",
     :setter-doc
       "The contribution metric. Applies to contribution analysis models. Allowed formats supported are\nfor summable and summable ratio contribution metrics. These include expressions such as\n`SUM(x)` or `SUM(x)/SUM(y)`, where x and y are column names from the base table.\n\n@param contributionMetric contributionMetric or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:dartNormalizeType
    {:getter-doc
       "Type of normalization algorithm for boosted tree models using dart booster.\n\n@return value or {@code null} for none",
     :setter-doc
       "Type of normalization algorithm for boosted tree models using dart booster.\n\n@param dartNormalizeType dartNormalizeType or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:dataFrequency
    {:getter-doc
       "The data frequency of a time series.\n\n@return value or {@code null} for none",
     :setter-doc
       "The data frequency of a time series.\n\n@param dataFrequency dataFrequency or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:dataSplitColumn
    {:getter-doc
       "The column to split data with. This column won't be used as a feature. 1. When\ndata_split_method is CUSTOM, the corresponding column should be boolean. The rows with true\nvalue tag are eval data, and the false are training data. 2. When data_split_method is SEQ, the\nfirst DATA_SPLIT_EVAL_FRACTION rows (from smallest to largest) in the corresponding column are\nused as training data, and the rest are eval data. It respects the order in Orderable data\ntypes: https://cloud.google.com/bigquery/docs/reference/standard-sql/data-\ntypes#data_type_properties\n\n@return value or {@code null} for none",
     :setter-doc
       "The column to split data with. This column won't be used as a feature. 1. When\ndata_split_method is CUSTOM, the corresponding column should be boolean. The rows with true\nvalue tag are eval data, and the false are training data. 2. When data_split_method is SEQ, the\nfirst DATA_SPLIT_EVAL_FRACTION rows (from smallest to largest) in the corresponding column are\nused as training data, and the rest are eval data. It respects the order in Orderable data\ntypes: https://cloud.google.com/bigquery/docs/reference/standard-sql/data-\ntypes#data_type_properties\n\n@param dataSplitColumn dataSplitColumn or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:dataSplitEvalFraction
    {:getter-doc
       "The fraction of evaluation data over the whole input data. The rest of data will be used as\ntraining data. The format should be double. Accurate to two decimal places. Default value is\n0.2.\n\n@return value or {@code null} for none",
     :setter-doc
       "The fraction of evaluation data over the whole input data. The rest of data will be used as\ntraining data. The format should be double. Accurate to two decimal places. Default value is\n0.2.\n\n@param dataSplitEvalFraction dataSplitEvalFraction or {@code null} for none",
     :optional true} :f64]
   [:dataSplitMethod
    {:getter-doc
       "The data split type for training and evaluation, e.g. RANDOM.\n\n@return value or {@code null} for none",
     :setter-doc
       "The data split type for training and evaluation, e.g. RANDOM.\n\n@param dataSplitMethod dataSplitMethod or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:decomposeTimeSeries
    {:getter-doc
       "If true, perform decompose time series and save the results.\n\n@return value or {@code null} for none",
     :setter-doc
       "If true, perform decompose time series and save the results.\n\n@param decomposeTimeSeries decomposeTimeSeries or {@code null} for none",
     :optional true} :boolean]
   [:dimensionIdColumns
    {:getter-doc
       "Optional. Names of the columns to slice on. Applies to contribution analysis models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Optional. Names of the columns to slice on. Applies to contribution analysis models.\n\n@param dimensionIdColumns dimensionIdColumns or {@code null} for none",
     :optional true} [:sequential {:min 1} [:string {:min 1}]]]
   [:distanceType
    {:getter-doc
       "Distance type for clustering models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Distance type for clustering models.\n\n@param distanceType distanceType or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:dropout
    {:getter-doc
       "Dropout probability for dnn models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Dropout probability for dnn models.\n\n@param dropout dropout or {@code null} for none",
     :optional true} :f64]
   [:earlyStop
    {:getter-doc
       "Whether to stop early when the loss doesn't improve significantly any more (compared to\nmin_relative_progress). Used only for iterative training algorithms.\n\n@return value or {@code null} for none",
     :setter-doc
       "Whether to stop early when the loss doesn't improve significantly any more (compared to\nmin_relative_progress). Used only for iterative training algorithms.\n\n@param earlyStop earlyStop or {@code null} for none",
     :optional true} :boolean]
   [:enableGlobalExplain
    {:getter-doc
       "If true, enable global explanation during training.\n\n@return value or {@code null} for none",
     :setter-doc
       "If true, enable global explanation during training.\n\n@param enableGlobalExplain enableGlobalExplain or {@code null} for none",
     :optional true} :boolean]
   [:endpointIdleTtl
    {:getter-doc
       "The idle TTL of the endpoint before the resources get destroyed. The default value is 6.5\nhours.\n\n@return value or {@code null} for none",
     :setter-doc
       "The idle TTL of the endpoint before the resources get destroyed. The default value is 6.5\nhours.\n\n@param endpointIdleTtl endpointIdleTtl or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:feedbackType
    {:getter-doc
       "Feedback type that specifies which algorithm to run for matrix factorization.\n\n@return value or {@code null} for none",
     :setter-doc
       "Feedback type that specifies which algorithm to run for matrix factorization.\n\n@param feedbackType feedbackType or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:fitIntercept
    {:getter-doc
       "Whether the model should include intercept during model training.\n\n@return value or {@code null} for none",
     :setter-doc
       "Whether the model should include intercept during model training.\n\n@param fitIntercept fitIntercept or {@code null} for none",
     :optional true} :boolean]
   [:forecastLimitLowerBound
    {:getter-doc
       "The forecast limit lower bound that was used during ARIMA model training with limits. To see\nmore details of the algorithm: https://otexts.com/fpp2/limits.html\n\n@return value or {@code null} for none",
     :setter-doc
       "The forecast limit lower bound that was used during ARIMA model training with limits. To see\nmore details of the algorithm: https://otexts.com/fpp2/limits.html\n\n@param forecastLimitLowerBound forecastLimitLowerBound or {@code null} for none",
     :optional true} :f64]
   [:forecastLimitUpperBound
    {:getter-doc
       "The forecast limit upper bound that was used during ARIMA model training with limits.\n\n@return value or {@code null} for none",
     :setter-doc
       "The forecast limit upper bound that was used during ARIMA model training with limits.\n\n@param forecastLimitUpperBound forecastLimitUpperBound or {@code null} for none",
     :optional true} :f64]
   [:hiddenUnits
    {:getter-doc
       "Hidden units for dnn models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Hidden units for dnn models.\n\n@param hiddenUnits hiddenUnits or {@code null} for none",
     :optional true} [:sequential {:min 1} :i64]]
   [:holidayRegion
    {:getter-doc
       "The geographical region based on which the holidays are considered in time series modeling. If\na valid value is specified, then holiday effects modeling is enabled.\n\n@return value or {@code null} for none",
     :setter-doc
       "The geographical region based on which the holidays are considered in time series modeling. If\na valid value is specified, then holiday effects modeling is enabled.\n\n@param holidayRegion holidayRegion or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:holidayRegions
    {:getter-doc
       "A list of geographical regions that are used for time series modeling.\n\n@return value or {@code null} for none",
     :setter-doc
       "A list of geographical regions that are used for time series modeling.\n\n@param holidayRegions holidayRegions or {@code null} for none",
     :optional true} [:sequential {:min 1} [:string {:min 1}]]]
   [:horizon
    {:getter-doc
       "The number of periods ahead that need to be forecasted.\n\n@return value or {@code null} for none",
     :setter-doc
       "The number of periods ahead that need to be forecasted.\n\n@param horizon horizon or {@code null} for none",
     :optional true} :i64]
   [:hparamTuningObjectives
    {:getter-doc
       "The target evaluation metrics to optimize the hyperparameters for.\n\n@return value or {@code null} for none",
     :setter-doc
       "The target evaluation metrics to optimize the hyperparameters for.\n\n@param hparamTuningObjectives hparamTuningObjectives or {@code null} for none",
     :optional true} [:sequential {:min 1} [:string {:min 1}]]]
   [:huggingFaceModelId
    {:getter-doc
       "The id of a Hugging Face model. For example, `google/gemma-2-2b-it`.\n\n@return value or {@code null} for none",
     :setter-doc
       "The id of a Hugging Face model. For example, `google/gemma-2-2b-it`.\n\n@param huggingFaceModelId huggingFaceModelId or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:includeDrift
    {:getter-doc
       "Include drift when fitting an ARIMA model.\n\n@return value or {@code null} for none",
     :setter-doc
       "Include drift when fitting an ARIMA model.\n\n@param includeDrift includeDrift or {@code null} for none",
     :optional true} :boolean]
   [:initialLearnRate
    {:getter-doc
       "Specifies the initial learning rate for the line search learn rate strategy.\n\n@return value or {@code null} for none",
     :setter-doc
       "Specifies the initial learning rate for the line search learn rate strategy.\n\n@param initialLearnRate initialLearnRate or {@code null} for none",
     :optional true} :f64]
   [:inputLabelColumns
    {:getter-doc
       "Name of input label columns in training data.\n\n@return value or {@code null} for none",
     :setter-doc
       "Name of input label columns in training data.\n\n@param inputLabelColumns inputLabelColumns or {@code null} for none",
     :optional true} [:sequential {:min 1} [:string {:min 1}]]]
   [:instanceWeightColumn
    {:getter-doc
       "Name of the instance weight column for training data. This column isn't be used as a feature.\n\n@return value or {@code null} for none",
     :setter-doc
       "Name of the instance weight column for training data. This column isn't be used as a feature.\n\n@param instanceWeightColumn instanceWeightColumn or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:integratedGradientsNumSteps
    {:getter-doc
       "Number of integral steps for the integrated gradients explain method.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of integral steps for the integrated gradients explain method.\n\n@param integratedGradientsNumSteps integratedGradientsNumSteps or {@code null} for none",
     :optional true} :i64]
   [:itemColumn
    {:getter-doc
       "Item column specified for matrix factorization models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Item column specified for matrix factorization models.\n\n@param itemColumn itemColumn or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:kmeansInitializationColumn
    {:getter-doc
       "The column used to provide the initial centroids for kmeans algorithm when\nkmeans_initialization_method is CUSTOM.\n\n@return value or {@code null} for none",
     :setter-doc
       "The column used to provide the initial centroids for kmeans algorithm when\nkmeans_initialization_method is CUSTOM.\n\n@param kmeansInitializationColumn kmeansInitializationColumn or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:kmeansInitializationMethod
    {:getter-doc
       "The method used to initialize the centroids for kmeans algorithm.\n\n@return value or {@code null} for none",
     :setter-doc
       "The method used to initialize the centroids for kmeans algorithm.\n\n@param kmeansInitializationMethod kmeansInitializationMethod or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:l1RegActivation
    {:getter-doc
       "L1 regularization coefficient to activations.\n\n@return value or {@code null} for none",
     :setter-doc
       "L1 regularization coefficient to activations.\n\n@param l1RegActivation l1RegActivation or {@code null} for none",
     :optional true} :f64]
   [:l1Regularization
    {:getter-doc
       "L1 regularization coefficient.\n\n@return value or {@code null} for none",
     :setter-doc
       "L1 regularization coefficient.\n\n@param l1Regularization l1Regularization or {@code null} for none",
     :optional true} :f64]
   [:l2Regularization
    {:getter-doc
       "L2 regularization coefficient.\n\n@return value or {@code null} for none",
     :setter-doc
       "L2 regularization coefficient.\n\n@param l2Regularization l2Regularization or {@code null} for none",
     :optional true} :f64]
   [:labelClassWeights
    {:getter-doc
       "Weights associated with each label class, for rebalancing the training data. Only applicable\nfor classification models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Weights associated with each label class, for rebalancing the training data. Only applicable\nfor classification models.\n\n@param labelClassWeights labelClassWeights or {@code null} for none",
     :optional true} [:map-of [:or simple-keyword? [:string {:min 1}]] :f64]]
   [:learnRate
    {:getter-doc
       "Learning rate in training. Used only for iterative training algorithms.\n\n@return value or {@code null} for none",
     :setter-doc
       "Learning rate in training. Used only for iterative training algorithms.\n\n@param learnRate learnRate or {@code null} for none",
     :optional true} :f64]
   [:learnRateStrategy
    {:getter-doc
       "The strategy to determine learn rate for the current iteration.\n\n@return value or {@code null} for none",
     :setter-doc
       "The strategy to determine learn rate for the current iteration.\n\n@param learnRateStrategy learnRateStrategy or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:lossType
    {:getter-doc
       "Type of loss function used during training run.\n\n@return value or {@code null} for none",
     :setter-doc
       "Type of loss function used during training run.\n\n@param lossType lossType or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:machineType
    {:getter-doc
       "The type of the machine used to deploy and serve the model.\n\n@return value or {@code null} for none",
     :setter-doc
       "The type of the machine used to deploy and serve the model.\n\n@param machineType machineType or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:maxIterations
    {:getter-doc
       "The maximum number of iterations in training. Used only for iterative training algorithms.\n\n@return value or {@code null} for none",
     :setter-doc
       "The maximum number of iterations in training. Used only for iterative training algorithms.\n\n@param maxIterations maxIterations or {@code null} for none",
     :optional true} :i64]
   [:maxParallelTrials
    {:getter-doc
       "Maximum number of trials to run in parallel.\n\n@return value or {@code null} for none",
     :setter-doc
       "Maximum number of trials to run in parallel.\n\n@param maxParallelTrials maxParallelTrials or {@code null} for none",
     :optional true} :i64]
   [:maxReplicaCount
    {:getter-doc
       "The maximum number of machine replicas that will be deployed on an endpoint. The default value\nis equal to min_replica_count.\n\n@return value or {@code null} for none",
     :setter-doc
       "The maximum number of machine replicas that will be deployed on an endpoint. The default value\nis equal to min_replica_count.\n\n@param maxReplicaCount maxReplicaCount or {@code null} for none",
     :optional true} :i64]
   [:maxTimeSeriesLength
    {:getter-doc
       "The maximum number of time points in a time series that can be used in modeling the trend\ncomponent of the time series. Don't use this option with the `timeSeriesLengthFraction` or\n`minTimeSeriesLength` options.\n\n@return value or {@code null} for none",
     :setter-doc
       "The maximum number of time points in a time series that can be used in modeling the trend\ncomponent of the time series. Don't use this option with the `timeSeriesLengthFraction` or\n`minTimeSeriesLength` options.\n\n@param maxTimeSeriesLength maxTimeSeriesLength or {@code null} for none",
     :optional true} :i64]
   [:maxTreeDepth
    {:getter-doc
       "Maximum depth of a tree for boosted tree models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Maximum depth of a tree for boosted tree models.\n\n@param maxTreeDepth maxTreeDepth or {@code null} for none",
     :optional true} :i64]
   [:minAprioriSupport
    {:getter-doc
       "The apriori support minimum. Applies to contribution analysis models.\n\n@return value or {@code null} for none",
     :setter-doc
       "The apriori support minimum. Applies to contribution analysis models.\n\n@param minAprioriSupport minAprioriSupport or {@code null} for none",
     :optional true} :f64]
   [:minRelativeProgress
    {:getter-doc
       "When early_stop is true, stops training when accuracy improvement is less than\n'min_relative_progress'. Used only for iterative training algorithms.\n\n@return value or {@code null} for none",
     :setter-doc
       "When early_stop is true, stops training when accuracy improvement is less than\n'min_relative_progress'. Used only for iterative training algorithms.\n\n@param minRelativeProgress minRelativeProgress or {@code null} for none",
     :optional true} :f64]
   [:minReplicaCount
    {:getter-doc
       "The minimum number of machine replicas that will be always deployed on an endpoint. This value\nmust be greater than or equal to 1. The default value is 1.\n\n@return value or {@code null} for none",
     :setter-doc
       "The minimum number of machine replicas that will be always deployed on an endpoint. This value\nmust be greater than or equal to 1. The default value is 1.\n\n@param minReplicaCount minReplicaCount or {@code null} for none",
     :optional true} :i64]
   [:minSplitLoss
    {:getter-doc
       "Minimum split loss for boosted tree models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Minimum split loss for boosted tree models.\n\n@param minSplitLoss minSplitLoss or {@code null} for none",
     :optional true} :f64]
   [:minTimeSeriesLength
    {:getter-doc
       "The minimum number of time points in a time series that are used in modeling the trend\ncomponent of the time series. If you use this option you must also set the\n`timeSeriesLengthFraction` option. This training option ensures that enough time points are\navailable when you use `timeSeriesLengthFraction` in trend modeling. This is particularly\nimportant when forecasting multiple time series in a single query using `timeSeriesIdColumn`.\nIf the total number of time points is less than the `minTimeSeriesLength` value, then the query\nuses all available time points.\n\n@return value or {@code null} for none",
     :setter-doc
       "The minimum number of time points in a time series that are used in modeling the trend\ncomponent of the time series. If you use this option you must also set the\n`timeSeriesLengthFraction` option. This training option ensures that enough time points are\navailable when you use `timeSeriesLengthFraction` in trend modeling. This is particularly\nimportant when forecasting multiple time series in a single query using `timeSeriesIdColumn`.\nIf the total number of time points is less than the `minTimeSeriesLength` value, then the query\nuses all available time points.\n\n@param minTimeSeriesLength minTimeSeriesLength or {@code null} for none",
     :optional true} :i64]
   [:minTreeChildWeight
    {:getter-doc
       "Minimum sum of instance weight needed in a child for boosted tree models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Minimum sum of instance weight needed in a child for boosted tree models.\n\n@param minTreeChildWeight minTreeChildWeight or {@code null} for none",
     :optional true} :i64]
   [:modelGardenModelName
    {:getter-doc
       "The name of a Vertex model garden publisher model. Format is\n`publishers/{publisher}/models/{model}@{optional_version_id}`.\n\n@return value or {@code null} for none",
     :setter-doc
       "The name of a Vertex model garden publisher model. Format is\n`publishers/{publisher}/models/{model}@{optional_version_id}`.\n\n@param modelGardenModelName modelGardenModelName or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:modelRegistry
    {:getter-doc
       "The model registry.\n\n@return value or {@code null} for none",
     :setter-doc
       "The model registry.\n\n@param modelRegistry modelRegistry or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:modelUri
    {:getter-doc
       "Google Cloud Storage URI from which the model was imported. Only applicable for imported\nmodels.\n\n@return value or {@code null} for none",
     :setter-doc
       "Google Cloud Storage URI from which the model was imported. Only applicable for imported\nmodels.\n\n@param modelUri modelUri or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:nonSeasonalOrder
    {:getter-doc
       "A specification of the non-seasonal part of the ARIMA model: the three components (p, d, q) are\nthe AR order, the degree of differencing, and the MA order.\n\n@return value or {@code null} for none",
     :setter-doc
       "A specification of the non-seasonal part of the ARIMA model: the three components (p, d, q) are\nthe AR order, the degree of differencing, and the MA order.\n\n@param nonSeasonalOrder nonSeasonalOrder or {@code null} for none",
     :optional true} :gcp.api.services.bigquery.model/ArimaOrder]
   [:numClusters
    {:getter-doc
       "Number of clusters for clustering models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of clusters for clustering models.\n\n@param numClusters numClusters or {@code null} for none",
     :optional true} :i64]
   [:numFactors
    {:getter-doc
       "Num factors specified for matrix factorization models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Num factors specified for matrix factorization models.\n\n@param numFactors numFactors or {@code null} for none",
     :optional true} :i64]
   [:numParallelTree
    {:getter-doc
       "Number of parallel trees constructed during each iteration for boosted tree models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of parallel trees constructed during each iteration for boosted tree models.\n\n@param numParallelTree numParallelTree or {@code null} for none",
     :optional true} :i64]
   [:numPrincipalComponents
    {:getter-doc
       "Number of principal components to keep in the PCA model. Must be <= the number of features.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of principal components to keep in the PCA model. Must be <= the number of features.\n\n@param numPrincipalComponents numPrincipalComponents or {@code null} for none",
     :optional true} :i64]
   [:numTrials
    {:getter-doc
       "Number of trials to run this hyperparameter tuning job.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of trials to run this hyperparameter tuning job.\n\n@param numTrials numTrials or {@code null} for none",
     :optional true} :i64]
   [:optimizationStrategy
    {:getter-doc
       "Optimization strategy for training linear regression models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Optimization strategy for training linear regression models.\n\n@param optimizationStrategy optimizationStrategy or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:optimizer
    {:getter-doc
       "Optimizer used for training the neural nets.\n\n@return value or {@code null} for none",
     :setter-doc
       "Optimizer used for training the neural nets.\n\n@param optimizer optimizer or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:pcaExplainedVarianceRatio
    {:getter-doc
       "The minimum ratio of cumulative explained variance that needs to be given by the PCA model.\n\n@return value or {@code null} for none",
     :setter-doc
       "The minimum ratio of cumulative explained variance that needs to be given by the PCA model.\n\n@param pcaExplainedVarianceRatio pcaExplainedVarianceRatio or {@code null} for none",
     :optional true} :f64]
   [:pcaSolver
    {:getter-doc
       "The solver for PCA.\n\n@return value or {@code null} for none",
     :setter-doc
       "The solver for PCA.\n\n@param pcaSolver pcaSolver or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:reservationAffinityKey
    {:getter-doc
       "Corresponds to the label key of a reservation resource used by Vertex AI. To target a\nSPECIFIC_RESERVATION by name, use `compute.googleapis.com/reservation-name` as the key and\nspecify the name of your reservation as its value.\n\n@return value or {@code null} for none",
     :setter-doc
       "Corresponds to the label key of a reservation resource used by Vertex AI. To target a\nSPECIFIC_RESERVATION by name, use `compute.googleapis.com/reservation-name` as the key and\nspecify the name of your reservation as its value.\n\n@param reservationAffinityKey reservationAffinityKey or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:reservationAffinityType
    {:getter-doc
       "Specifies the reservation affinity type used to configure a Vertex AI resource. The default\nvalue is `NO_RESERVATION`.\n\n@return value or {@code null} for none",
     :setter-doc
       "Specifies the reservation affinity type used to configure a Vertex AI resource. The default\nvalue is `NO_RESERVATION`.\n\n@param reservationAffinityType reservationAffinityType or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:reservationAffinityValues
    {:getter-doc
       "Corresponds to the label values of a reservation resource used by Vertex AI. This must be the\nfull resource name of the reservation or reservation block.\n\n@return value or {@code null} for none",
     :setter-doc
       "Corresponds to the label values of a reservation resource used by Vertex AI. This must be the\nfull resource name of the reservation or reservation block.\n\n@param reservationAffinityValues reservationAffinityValues or {@code null} for none",
     :optional true} [:sequential {:min 1} [:string {:min 1}]]]
   [:sampledShapleyNumPaths
    {:getter-doc
       "Number of paths for the sampled Shapley explain method.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of paths for the sampled Shapley explain method.\n\n@param sampledShapleyNumPaths sampledShapleyNumPaths or {@code null} for none",
     :optional true} :i64]
   [:scaleFeatures
    {:getter-doc
       "If true, scale the feature values by dividing the feature standard deviation. Currently only\napply to PCA.\n\n@return value or {@code null} for none",
     :setter-doc
       "If true, scale the feature values by dividing the feature standard deviation. Currently only\napply to PCA.\n\n@param scaleFeatures scaleFeatures or {@code null} for none",
     :optional true} :boolean]
   [:standardizeFeatures
    {:getter-doc
       "Whether to standardize numerical features. Default to true.\n\n@return value or {@code null} for none",
     :setter-doc
       "Whether to standardize numerical features. Default to true.\n\n@param standardizeFeatures standardizeFeatures or {@code null} for none",
     :optional true} :boolean]
   [:subsample
    {:getter-doc
       "Subsample fraction of the training data to grow tree to prevent overfitting for boosted tree\nmodels.\n\n@return value or {@code null} for none",
     :setter-doc
       "Subsample fraction of the training data to grow tree to prevent overfitting for boosted tree\nmodels.\n\n@param subsample subsample or {@code null} for none",
     :optional true} :f64]
   [:testColumn
    {:getter-doc
       "Name of the column used to determine the rows corresponding to control and test. Applies to\ncontribution analysis models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Name of the column used to determine the rows corresponding to control and test. Applies to\ncontribution analysis models.\n\n@param isTestColumn isTestColumn or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:tfVersion
    {:getter-doc
       "Based on the selected TF version, the corresponding docker image is used to train external\nmodels.\n\n@return value or {@code null} for none",
     :setter-doc
       "Based on the selected TF version, the corresponding docker image is used to train external\nmodels.\n\n@param tfVersion tfVersion or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:timeSeriesDataColumn
    {:getter-doc
       "Column to be designated as time series data for ARIMA model.\n\n@return value or {@code null} for none",
     :setter-doc
       "Column to be designated as time series data for ARIMA model.\n\n@param timeSeriesDataColumn timeSeriesDataColumn or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:timeSeriesIdColumn
    {:getter-doc
       "The time series id column that was used during ARIMA model training.\n\n@return value or {@code null} for none",
     :setter-doc
       "The time series id column that was used during ARIMA model training.\n\n@param timeSeriesIdColumn timeSeriesIdColumn or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:timeSeriesIdColumns
    {:getter-doc
       "The time series id columns that were used during ARIMA model training.\n\n@return value or {@code null} for none",
     :setter-doc
       "The time series id columns that were used during ARIMA model training.\n\n@param timeSeriesIdColumns timeSeriesIdColumns or {@code null} for none",
     :optional true} [:sequential {:min 1} [:string {:min 1}]]]
   [:timeSeriesLengthFraction
    {:getter-doc
       "The fraction of the interpolated length of the time series that's used to model the time series\ntrend component. All of the time points of the time series are used to model the non-trend\ncomponent. This training option accelerates modeling training without sacrificing much\nforecasting accuracy. You can use this option with `minTimeSeriesLength` but not with\n`maxTimeSeriesLength`.\n\n@return value or {@code null} for none",
     :setter-doc
       "The fraction of the interpolated length of the time series that's used to model the time series\ntrend component. All of the time points of the time series are used to model the non-trend\ncomponent. This training option accelerates modeling training without sacrificing much\nforecasting accuracy. You can use this option with `minTimeSeriesLength` but not with\n`maxTimeSeriesLength`.\n\n@param timeSeriesLengthFraction timeSeriesLengthFraction or {@code null} for none",
     :optional true} :f64]
   [:timeSeriesTimestampColumn
    {:getter-doc
       "Column to be designated as time series timestamp for ARIMA model.\n\n@return value or {@code null} for none",
     :setter-doc
       "Column to be designated as time series timestamp for ARIMA model.\n\n@param timeSeriesTimestampColumn timeSeriesTimestampColumn or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:treeMethod
    {:getter-doc
       "Tree construction algorithm for boosted tree models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Tree construction algorithm for boosted tree models.\n\n@param treeMethod treeMethod or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:trendSmoothingWindowSize
    {:getter-doc
       "Smoothing window size for the trend component. When a positive value is specified, a center\nmoving average smoothing is applied on the history trend. When the smoothing window is out of\nthe boundary at the beginning or the end of the trend, the first element or the last element is\npadded to fill the smoothing window before the average is applied.\n\n@return value or {@code null} for none",
     :setter-doc
       "Smoothing window size for the trend component. When a positive value is specified, a center\nmoving average smoothing is applied on the history trend. When the smoothing window is out of\nthe boundary at the beginning or the end of the trend, the first element or the last element is\npadded to fill the smoothing window before the average is applied.\n\n@param trendSmoothingWindowSize trendSmoothingWindowSize or {@code null} for none",
     :optional true} :i64]
   [:userColumn
    {:getter-doc
       "User column specified for matrix factorization models.\n\n@return value or {@code null} for none",
     :setter-doc
       "User column specified for matrix factorization models.\n\n@param userColumn userColumn or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:vertexAiModelVersionAliases
    {:getter-doc
       "The version aliases to apply in Vertex AI model registry. Always overwrite if the version\naliases exists in a existing model.\n\n@return value or {@code null} for none",
     :setter-doc
       "The version aliases to apply in Vertex AI model registry. Always overwrite if the version\naliases exists in a existing model.\n\n@param vertexAiModelVersionAliases vertexAiModelVersionAliases or {@code null} for none",
     :optional true} [:sequential {:min 1} [:string {:min 1}]]]
   [:walsAlpha
    {:getter-doc
       "Hyperparameter for matrix factoration when implicit feedback type is specified.\n\n@return value or {@code null} for none",
     :setter-doc
       "Hyperparameter for matrix factoration when implicit feedback type is specified.\n\n@param walsAlpha walsAlpha or {@code null} for none",
     :optional true} :f64]
   [:warmStart
    {:getter-doc
       "Whether to train a model from the last checkpoint.\n\n@return value or {@code null} for none",
     :setter-doc
       "Whether to train a model from the last checkpoint.\n\n@param warmStart warmStart or {@code null} for none",
     :optional true} :boolean]
   [:xgboostVersion
    {:getter-doc
       "User-selected XGBoost versions for training of XGBoost models.\n\n@return value or {@code null} for none",
     :setter-doc
       "User-selected XGBoost versions for training of XGBoost models.\n\n@param xgboostVersion xgboostVersion or {@code null} for none",
     :optional true} [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/TrainingOptions schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.TrainingOptions"}))