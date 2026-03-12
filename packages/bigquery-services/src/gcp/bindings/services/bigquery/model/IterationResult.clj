;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.IterationResult
  {:doc
     "Information about a single iteration of the training run.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.IterationResult"
   :gcp.dev/certification
     {:base-seed 1772390235831
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390235831 :standard 1772390235832 :stress 1772390235833}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:16.087192171Z"}}
  (:require [gcp.bindings.services.bigquery.model.ArimaResult :as ArimaResult]
            [gcp.bindings.services.bigquery.model.ClusterInfo :as ClusterInfo]
            [gcp.bindings.services.bigquery.model.PrincipalComponentInfo :as
             PrincipalComponentInfo]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model IterationResult]))

(defn ^IterationResult from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/IterationResult arg)
  (let [o (new IterationResult)]
    (when (some? (get arg :arimaResult))
      (.setArimaResult o (ArimaResult/from-edn (get arg :arimaResult))))
    (when (some? (get arg :clusterInfos))
      (.setClusterInfos o (map ClusterInfo/from-edn (get arg :clusterInfos))))
    (when (some? (get arg :durationMs))
      (.setDurationMs o (get arg :durationMs)))
    (when (some? (get arg :evalLoss)) (.setEvalLoss o (get arg :evalLoss)))
    (when (some? (get arg :index)) (.setIndex o (int (get arg :index))))
    (when (some? (get arg :learnRate)) (.setLearnRate o (get arg :learnRate)))
    (when (some? (get arg :principalComponentInfos))
      (.setPrincipalComponentInfos o
                                   (map PrincipalComponentInfo/from-edn
                                     (get arg :principalComponentInfos))))
    (when (some? (get arg :trainingLoss))
      (.setTrainingLoss o (get arg :trainingLoss)))
    o))

(defn to-edn
  [^IterationResult arg]
  {:post [(global/strict! :gcp.bindings.services.bigquery.model/IterationResult
                          %)]}
  (cond-> {}
    (.getArimaResult arg) (assoc :arimaResult
                            (ArimaResult/to-edn (.getArimaResult arg)))
    (.getClusterInfos arg) (assoc :clusterInfos
                             (map ClusterInfo/to-edn (.getClusterInfos arg)))
    (.getDurationMs arg) (assoc :durationMs (.getDurationMs arg))
    (.getEvalLoss arg) (assoc :evalLoss (.getEvalLoss arg))
    (.getIndex arg) (assoc :index (.getIndex arg))
    (.getLearnRate arg) (assoc :learnRate (.getLearnRate arg))
    (.getPrincipalComponentInfos arg) (assoc :principalComponentInfos
                                        (map PrincipalComponentInfo/to-edn
                                          (.getPrincipalComponentInfos arg)))
    (.getTrainingLoss arg) (assoc :trainingLoss (.getTrainingLoss arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Information about a single iteration of the training run.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/IterationResult}
   [:arimaResult
    {:getter-doc "Arima result.\n\n@return value or {@code null} for none",
     :setter-doc
       "Arima result.\n\n@param arimaResult arimaResult or {@code null} for none",
     :optional true} :gcp.bindings.services.bigquery.model/ArimaResult]
   [:clusterInfos
    {:getter-doc
       "Information about top clusters for clustering models.\n\n@return value or {@code null} for none",
     :setter-doc
       "Information about top clusters for clustering models.\n\n@param clusterInfos clusterInfos or {@code null} for none",
     :optional true}
    [:sequential {:min 1} :gcp.bindings.services.bigquery.model/ClusterInfo]]
   [:durationMs
    {:getter-doc
       "Time taken to run the iteration in milliseconds.\n\n@return value or {@code null} for none",
     :setter-doc
       "Time taken to run the iteration in milliseconds.\n\n@param durationMs durationMs or {@code null} for none",
     :optional true} :int]
   [:evalLoss
    {:getter-doc
       "Loss computed on the eval data at the end of iteration.\n\n@return value or {@code null} for none",
     :setter-doc
       "Loss computed on the eval data at the end of iteration.\n\n@param evalLoss evalLoss or {@code null} for none",
     :optional true} :double]
   [:index
    {:getter-doc
       "Index of the iteration, 0 based.\n\n@return value or {@code null} for none",
     :setter-doc
       "Index of the iteration, 0 based.\n\n@param index index or {@code null} for none",
     :optional true} [:int {:min -2147483648, :max 2147483647}]]
   [:learnRate
    {:getter-doc
       "Learn rate used for this iteration.\n\n@return value or {@code null} for none",
     :setter-doc
       "Learn rate used for this iteration.\n\n@param learnRate learnRate or {@code null} for none",
     :optional true} :double]
   [:principalComponentInfos
    {:getter-doc
       "The information of the principal components.\n\n@return value or {@code null} for none",
     :setter-doc
       "The information of the principal components.\n\n@param principalComponentInfos principalComponentInfos or {@code null} for none",
     :optional true}
    [:sequential {:min 1}
     :gcp.bindings.services.bigquery.model/PrincipalComponentInfo]]
   [:trainingLoss
    {:getter-doc
       "Loss computed on the training data at the end of iteration.\n\n@return value or {@code null} for none",
     :setter-doc
       "Loss computed on the training data at the end of iteration.\n\n@param trainingLoss trainingLoss or {@code null} for none",
     :optional true} :double]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/IterationResult schema}
    {:gcp.global/name "gcp.bindings.services.bigquery.model.IterationResult"}))