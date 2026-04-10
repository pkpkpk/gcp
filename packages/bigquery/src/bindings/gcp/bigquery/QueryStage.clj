;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.QueryStage
  {:doc
     "BigQuery provides diagnostic information about a completed query's execution plan (or query plan\nfor short). The query plan describes a query as a series of stages, with each stage comprising a\nnumber of steps that read from data sources, perform a series of transformations on the input,\nand emit an output to a future stage (or the final result). This class contains information on a\nquery stage.\n\n@see <a href=\"https://cloud.google.com/bigquery/query-plan-explanation\">Query Plan</a>"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.QueryStage"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :reason :read-only
      :skipped true
      :timestamp "2026-04-09T22:56:39.563034165Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery QueryStage QueryStage$Builder
            QueryStage$QueryStep]))

(declare from-edn to-edn QueryStep-from-edn QueryStep-to-edn)

(defn ^QueryStage$QueryStep QueryStep-from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.bigquery.QueryStage.QueryStep is read-only")))

(defn QueryStep-to-edn
  [^QueryStage$QueryStep arg]
  (when arg
    (cond-> {}
      (some->> (.getName arg)
               (not= ""))
        (assoc :name (.getName arg))
      (seq (.getSubsteps arg)) (assoc :substeps (seq (.getSubsteps arg))))))

(def QueryStep-schema
  [:map
   {:closed true,
    :doc
      "Each query stage is made of a number of steps. This class contains information on a query step.\n\n@see <a href=\"https://cloud.google.com/bigquery/query-plan-explanation#steps_metadata\">Steps\n    Metadata</a>",
    :gcp/category :nested/read-only,
    :gcp/key :gcp.bigquery/QueryStage.QueryStep}
   [:name
    {:read-only? true,
     :optional true,
     :doc
       "Returns a machine-readable name for the operation.\n\n@see <a href=\"https://cloud.google.com/bigquery/query-plan-explanation#steps_metadata\">Steps\n    Metadata</a>"}
    [:string {:min 1}]]
   [:substeps
    {:read-only? true,
     :optional true,
     :doc "Returns a list of human-readable stage descriptions."}
    [:sequential {:min 1} [:string {:min 1}]]]])

(defn ^QueryStage from-edn
  [arg]
  (throw (Exception.
           "Class com.google.cloud.bigquery.QueryStage is read-only")))

(defn to-edn
  [^QueryStage arg]
  {:post [(global/strict! :gcp.bigquery/QueryStage %)]}
  (when arg
    (cond-> {}
      (.getCompletedParallelInputs arg) (assoc :completedParallelInputs
                                          (.getCompletedParallelInputs arg))
      (.getComputeMsAvg arg) (assoc :computeMsAvg (.getComputeMsAvg arg))
      (.getComputeMsMax arg) (assoc :computeMsMax (.getComputeMsMax arg))
      (.getComputeRatioAvg arg) (assoc :computeRatioAvg
                                  (.getComputeRatioAvg arg))
      (.getComputeRatioMax arg) (assoc :computeRatioMax
                                  (.getComputeRatioMax arg))
      (.getEndMs arg) (assoc :endMs (.getEndMs arg))
      (.getGeneratedId arg) (assoc :generatedId (.getGeneratedId arg))
      (seq (.getInputStages arg)) (assoc :inputStages
                                    (seq (.getInputStages arg)))
      (some->> (.getName arg)
               (not= ""))
        (assoc :name (.getName arg))
      (.getParallelInputs arg) (assoc :parallelInputs (.getParallelInputs arg))
      (.getReadMsAvg arg) (assoc :readMsAvg (.getReadMsAvg arg))
      (.getReadMsMax arg) (assoc :readMsMax (.getReadMsMax arg))
      (.getReadRatioAvg arg) (assoc :readRatioAvg (.getReadRatioAvg arg))
      (.getReadRatioMax arg) (assoc :readRatioMax (.getReadRatioMax arg))
      (.getRecordsRead arg) (assoc :recordsRead (.getRecordsRead arg))
      (.getRecordsWritten arg) (assoc :recordsWritten (.getRecordsWritten arg))
      (.getShuffleOutputBytes arg) (assoc :shuffleOutputBytes
                                     (.getShuffleOutputBytes arg))
      (.getShuffleOutputBytesSpilled arg) (assoc :shuffleOutputBytesSpilled
                                            (.getShuffleOutputBytesSpilled arg))
      (.getSlotMs arg) (assoc :slotMs (.getSlotMs arg))
      (.getStartMs arg) (assoc :startMs (.getStartMs arg))
      (some->> (.getStatus arg)
               (not= ""))
        (assoc :status (.getStatus arg))
      (seq (.getSteps arg)) (assoc :steps
                              (map QueryStep-to-edn (.getSteps arg)))
      (.getWaitMsAvg arg) (assoc :waitMsAvg (.getWaitMsAvg arg))
      (.getWaitMsMax arg) (assoc :waitMsMax (.getWaitMsMax arg))
      (.getWaitRatioAvg arg) (assoc :waitRatioAvg (.getWaitRatioAvg arg))
      (.getWaitRatioMax arg) (assoc :waitRatioMax (.getWaitRatioMax arg))
      (.getWriteMsAvg arg) (assoc :writeMsAvg (.getWriteMsAvg arg))
      (.getWriteMsMax arg) (assoc :writeMsMax (.getWriteMsMax arg))
      (.getWriteRatioAvg arg) (assoc :writeRatioAvg (.getWriteRatioAvg arg))
      (.getWriteRatioMax arg) (assoc :writeRatioMax (.getWriteRatioMax arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "BigQuery provides diagnostic information about a completed query's execution plan (or query plan\nfor short). The query plan describes a query as a series of stages, with each stage comprising a\nnumber of steps that read from data sources, perform a series of transformations on the input,\nand emit an output to a future stage (or the final result). This class contains information on a\nquery stage.\n\n@see <a href=\"https://cloud.google.com/bigquery/query-plan-explanation\">Query Plan</a>",
    :gcp/category :read-only,
    :gcp/key :gcp.bigquery/QueryStage}
   [:completedParallelInputs
    {:read-only? true,
     :optional true,
     :doc "Returns the number of parallel input segments completed."} :i64]
   [:computeMsAvg
    {:read-only? true,
     :optional true,
     :doc
       "Returns the time in milliseconds the average worker spent on CPU-bound tasks."}
    :i64]
   [:computeMsMax
    {:read-only? true,
     :optional true,
     :doc
       "Returns the time in milliseconds the slowest worker spent on CPU-bound tasks."}
    :i64]
   [:computeRatioAvg
    {:read-only? true,
     :optional true,
     :doc
       "Returns the time the average worker spent CPU-bound, divided by the longest time spent by any\nworker in any segment."}
    :f64]
   [:computeRatioMax
    {:read-only? true,
     :optional true,
     :doc
       "Returns the time the slowest worker spent CPU-bound, divided by the longest time spent by any\nworker in any segment."}
    :f64]
   [:endMs
    {:read-only? true,
     :optional true,
     :doc "Returns the stage end time represented as milliseconds since epoch."}
    :i64]
   [:generatedId
    {:read-only? true,
     :optional true,
     :doc
       "Returns a unique, server-generated ID for the stage within its plan."}
    :i64]
   [:inputStages
    {:read-only? true,
     :optional true,
     :doc "Returns a list of the stage IDs that are inputs to this stage."}
    [:sequential {:min 1} :i64]]
   [:name
    {:read-only? true,
     :optional true,
     :doc "Returns a human-readable name for the stage."} [:string {:min 1}]]
   [:parallelInputs
    {:read-only? true,
     :optional true,
     :doc "Returns the number of parallel input segments to be processed."}
    :i64]
   [:readMsAvg
    {:read-only? true,
     :optional true,
     :doc
       "Returns the time in milliseconds the average worker spent reading input."}
    :i64]
   [:readMsMax
    {:read-only? true,
     :optional true,
     :doc
       "Returns the time in milliseconds the slowest worker spent reading input."}
    :i64]
   [:readRatioAvg
    {:read-only? true,
     :optional true,
     :doc
       "Returns the time the average worker spent reading input data, divided by the longest time spent\nby any worker in any segment."}
    :f64]
   [:readRatioMax
    {:read-only? true,
     :optional true,
     :doc
       "Returns the time the slowest worker spent reading input data, divided by the longest time spent\nby any worker in any segment."}
    :f64]
   [:recordsRead
    {:read-only? true,
     :optional true,
     :doc "Returns the number of rows (top-level records) read by the stage."}
    :i64]
   [:recordsWritten
    {:read-only? true,
     :optional true,
     :doc
       "Returns the number of rows (top-level records) written by the stage."}
    :i64]
   [:shuffleOutputBytes
    {:read-only? true,
     :optional true,
     :doc "Returns the total number of bytes written to shuffle."} :i64]
   [:shuffleOutputBytesSpilled
    {:read-only? true,
     :optional true,
     :doc
       "Returns the total number of bytes writtedn to shuffle and spilled to disk."}
    :i64]
   [:slotMs
    {:read-only? true,
     :optional true,
     :doc "Returns the slot-milliseconds used by the stage."} :i64]
   [:startMs
    {:read-only? true,
     :optional true,
     :doc
       "Returns the stage start time represented as milliseconds since epoch."}
    :i64]
   [:status
    {:read-only? true,
     :optional true,
     :doc "Returns the current status for the stage."} [:string {:min 1}]]
   [:steps
    {:read-only? true,
     :optional true,
     :doc
       "Returns the list of steps within the stage in dependency order (approximately chronological)."}
    [:sequential {:min 1} [:ref :gcp.bigquery/QueryStage.QueryStep]]]
   [:waitMsAvg
    {:read-only? true,
     :optional true,
     :doc
       "Returns the time in milliseconds the average worker spent waiting to be scheduled."}
    :i64]
   [:waitMsMax
    {:read-only? true,
     :optional true,
     :doc
       "Returns the time in milliseconds the slowest worker spent waiting to be scheduled."}
    :i64]
   [:waitRatioAvg
    {:read-only? true,
     :optional true,
     :doc
       "Returns the time the average worker spent waiting to be scheduled, divided by the longest time\nspent by any worker in any segment."}
    :f64]
   [:waitRatioMax
    {:read-only? true,
     :optional true,
     :doc
       "Returns the time the slowest worker spent waiting to be scheduled, divided by the longest time\nspent by any worker in any segment."}
    :f64]
   [:writeMsAvg
    {:read-only? true,
     :optional true,
     :doc
       "Returns the time in milliseconds the average worker spent writing output."}
    :i64]
   [:writeMsMax
    {:read-only? true,
     :optional true,
     :doc
       "Returns the time in milliseconds the slowest worker spent writing output."}
    :i64]
   [:writeRatioAvg
    {:read-only? true,
     :optional true,
     :doc
       "Returns the time the average worker spent writing output data, divided by the longest time\nspent by any worker in any segment."}
    :f64]
   [:writeRatioMax
    {:read-only? true,
     :optional true,
     :doc
       "Returns the time the slowest worker spent writing output data, divided by the longest time\nspent by any worker in any segment."}
    :f64]])

(global/include-schema-registry!
  (with-meta {:gcp.bigquery/QueryStage schema,
              :gcp.bigquery/QueryStage.QueryStep QueryStep-schema}
    {:gcp.global/name "gcp.bigquery.QueryStage"}))