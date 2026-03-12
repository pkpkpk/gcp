;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.bigquery.QueryStage
  {:doc
     "BigQuery provides diagnostic information about a completed query's execution plan (or query plan\nfor short). The query plan describes a query as a series of stages, with each stage comprising a\nnumber of steps that read from data sources, perform a series of transformations on the input,\nand emit an output to a future stage (or the final result). This class contains information on a\nquery stage.\n\n@see <a href=\"https://cloud.google.com/bigquery/query-plan-explanation\">Query Plan</a>"
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.QueryStage"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "32051bbb-16c0-5b08-9f8c-751cde0c9bfb"
      :protocol-hash
        "62616b045d3dd853f6e527d31a44a851f587c87ad57ad3f2927b4519e248d6c9"
      :reason :read-only
      :skipped true
      :timestamp "2026-02-25T16:46:57.497721970Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery QueryStage QueryStage$QueryStep]))

(declare QueryStage$QueryStep-from-edn QueryStage$QueryStep-to-edn)

(defn ^QueryStage$QueryStep QueryStage$QueryStep-from-edn
  [arg]
  (throw
    (Exception.
      "Class com.google.cloud.bigquery.QueryStage.QueryStep is read-only")))

(defn QueryStage$QueryStep-to-edn
  [^QueryStage$QueryStep arg]
  (cond-> {}
    (.getName arg) (assoc :name (.getName arg))
    (.getSubsteps arg) (assoc :substeps (seq (.getSubsteps arg)))))

(def QueryStage$QueryStep-schema
  [:map
   {:closed true,
    :doc
      "Each query stage is made of a number of steps. This class contains information on a query step.\n\n@see <a href=\"https://cloud.google.com/bigquery/query-plan-explanation#steps_metadata\">Steps\n    Metadata</a>",
    :gcp/category :nested/read-only,
    :gcp/key :gcp.bindings.bigquery/QueryStage.QueryStep}
   [:name
    {:read-only? true,
     :doc
       "Returns a machine-readable name for the operation.\n\n@see <a href=\"https://cloud.google.com/bigquery/query-plan-explanation#steps_metadata\">Steps\n    Metadata</a>"}
    [:string {:min 1}]]
   [:substeps
    {:read-only? true,
     :doc "Returns a list of human-readable stage descriptions."}
    [:sequential {:min 1} [:string {:min 1}]]]])

(defn ^QueryStage from-edn
  [arg]
  (throw (Exception.
           "Class com.google.cloud.bigquery.QueryStage is read-only")))

(defn to-edn
  [^QueryStage arg]
  {:post [(global/strict! :gcp.bindings.bigquery/QueryStage %)]}
  (cond-> {}
    (.getCompletedParallelInputs arg) (assoc :completedParallelInputs
                                        (.getCompletedParallelInputs arg))
    (.getComputeMsAvg arg) (assoc :computeMsAvg (.getComputeMsAvg arg))
    (.getComputeMsMax arg) (assoc :computeMsMax (.getComputeMsMax arg))
    (.getComputeRatioAvg arg) (assoc :computeRatioAvg (.getComputeRatioAvg arg))
    (.getComputeRatioMax arg) (assoc :computeRatioMax (.getComputeRatioMax arg))
    (.getEndMs arg) (assoc :endMs (.getEndMs arg))
    (.getGeneratedId arg) (assoc :generatedId (.getGeneratedId arg))
    (.getInputStages arg) (assoc :inputStages (seq (.getInputStages arg)))
    (.getName arg) (assoc :name (.getName arg))
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
    (.getStatus arg) (assoc :status (.getStatus arg))
    (.getSteps arg) (assoc :steps
                      (map QueryStage$QueryStep-to-edn (.getSteps arg)))
    (.getWaitMsAvg arg) (assoc :waitMsAvg (.getWaitMsAvg arg))
    (.getWaitMsMax arg) (assoc :waitMsMax (.getWaitMsMax arg))
    (.getWaitRatioAvg arg) (assoc :waitRatioAvg (.getWaitRatioAvg arg))
    (.getWaitRatioMax arg) (assoc :waitRatioMax (.getWaitRatioMax arg))
    (.getWriteMsAvg arg) (assoc :writeMsAvg (.getWriteMsAvg arg))
    (.getWriteMsMax arg) (assoc :writeMsMax (.getWriteMsMax arg))
    (.getWriteRatioAvg arg) (assoc :writeRatioAvg (.getWriteRatioAvg arg))
    (.getWriteRatioMax arg) (assoc :writeRatioMax (.getWriteRatioMax arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "BigQuery provides diagnostic information about a completed query's execution plan (or query plan\nfor short). The query plan describes a query as a series of stages, with each stage comprising a\nnumber of steps that read from data sources, perform a series of transformations on the input,\nand emit an output to a future stage (or the final result). This class contains information on a\nquery stage.\n\n@see <a href=\"https://cloud.google.com/bigquery/query-plan-explanation\">Query Plan</a>",
    :gcp/category :read-only,
    :gcp/key :gcp.bindings.bigquery/QueryStage}
   [:completedParallelInputs
    {:read-only? true,
     :doc "Returns the number of parallel input segments completed."} :int]
   [:computeMsAvg
    {:read-only? true,
     :doc
       "Returns the time in milliseconds the average worker spent on CPU-bound tasks."}
    :int]
   [:computeMsMax
    {:read-only? true,
     :doc
       "Returns the time in milliseconds the slowest worker spent on CPU-bound tasks."}
    :int]
   [:computeRatioAvg
    {:read-only? true,
     :doc
       "Returns the time the average worker spent CPU-bound, divided by the longest time spent by any\nworker in any segment."}
    :double]
   [:computeRatioMax
    {:read-only? true,
     :doc
       "Returns the time the slowest worker spent CPU-bound, divided by the longest time spent by any\nworker in any segment."}
    :double]
   [:endMs
    {:read-only? true,
     :doc "Returns the stage end time represented as milliseconds since epoch."}
    :int]
   [:generatedId
    {:read-only? true,
     :doc
       "Returns a unique, server-generated ID for the stage within its plan."}
    :int]
   [:inputStages
    {:read-only? true,
     :doc "Returns a list of the stage IDs that are inputs to this stage."}
    [:sequential {:min 1} :int]]
   [:name
    {:read-only? true, :doc "Returns a human-readable name for the stage."}
    [:string {:min 1}]]
   [:parallelInputs
    {:read-only? true,
     :doc "Returns the number of parallel input segments to be processed."}
    :int]
   [:readMsAvg
    {:read-only? true,
     :doc
       "Returns the time in milliseconds the average worker spent reading input."}
    :int]
   [:readMsMax
    {:read-only? true,
     :doc
       "Returns the time in milliseconds the slowest worker spent reading input."}
    :int]
   [:readRatioAvg
    {:read-only? true,
     :doc
       "Returns the time the average worker spent reading input data, divided by the longest time spent\nby any worker in any segment."}
    :double]
   [:readRatioMax
    {:read-only? true,
     :doc
       "Returns the time the slowest worker spent reading input data, divided by the longest time spent\nby any worker in any segment."}
    :double]
   [:recordsRead
    {:read-only? true,
     :doc "Returns the number of rows (top-level records) read by the stage."}
    :int]
   [:recordsWritten
    {:read-only? true,
     :doc
       "Returns the number of rows (top-level records) written by the stage."}
    :int]
   [:shuffleOutputBytes
    {:read-only? true,
     :doc "Returns the total number of bytes written to shuffle."} :int]
   [:shuffleOutputBytesSpilled
    {:read-only? true,
     :doc
       "Returns the total number of bytes writtedn to shuffle and spilled to disk."}
    :int]
   [:slotMs
    {:read-only? true, :doc "Returns the slot-milliseconds used by the stage."}
    :int]
   [:startMs
    {:read-only? true,
     :doc
       "Returns the stage start time represented as milliseconds since epoch."}
    :int]
   [:status {:read-only? true, :doc "Returns the current status for the stage."}
    [:string {:min 1}]]
   [:steps
    {:read-only? true,
     :doc
       "Returns the list of steps within the stage in dependency order (approximately chronological)."}
    [:sequential {:min 1} :gcp.bindings.bigquery/QueryStage.QueryStep]]
   [:waitMsAvg
    {:read-only? true,
     :doc
       "Returns the time in milliseconds the average worker spent waiting to be scheduled."}
    :int]
   [:waitMsMax
    {:read-only? true,
     :doc
       "Returns the time in milliseconds the slowest worker spent waiting to be scheduled."}
    :int]
   [:waitRatioAvg
    {:read-only? true,
     :doc
       "Returns the time the average worker spent waiting to be scheduled, divided by the longest time\nspent by any worker in any segment."}
    :double]
   [:waitRatioMax
    {:read-only? true,
     :doc
       "Returns the time the slowest worker spent waiting to be scheduled, divided by the longest time\nspent by any worker in any segment."}
    :double]
   [:writeMsAvg
    {:read-only? true,
     :doc
       "Returns the time in milliseconds the average worker spent writing output."}
    :int]
   [:writeMsMax
    {:read-only? true,
     :doc
       "Returns the time in milliseconds the slowest worker spent writing output."}
    :int]
   [:writeRatioAvg
    {:read-only? true,
     :doc
       "Returns the time the average worker spent writing output data, divided by the longest time\nspent by any worker in any segment."}
    :double]
   [:writeRatioMax
    {:read-only? true,
     :doc
       "Returns the time the slowest worker spent writing output data, divided by the longest time\nspent by any worker in any segment."}
    :double]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.bigquery/QueryStage schema,
              :gcp.bindings.bigquery/QueryStage.QueryStep
                QueryStage$QueryStep-schema}
    {:gcp.global/name "gcp.bindings.bigquery.QueryStage"}))