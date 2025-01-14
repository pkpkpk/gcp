(ns gcp.bigquery.v2.JobStatistics
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery JobStatistics
                                      JobStatistics$ReservationUsage
                                      JobStatistics$ScriptStatistics
                                      JobStatistics$ScriptStatistics$ScriptStackFrame)))

(defn ReservationUsage-to-edn
  [^JobStatistics$ReservationUsage arg]
  {:name (.getName arg)
   :slotMs (.getSlotMs arg)})

(defn ScriptStackFrame-to-edn
  [^JobStatistics$ScriptStatistics$ScriptStackFrame arg]
  {:endColumn (.getEndColumn arg)
   :endLine (.getEndLine arg)
   :procedureId (.getProcedureId arg)
   :startColumn (.getStartColumn arg)
   :startLine (.getStartLine arg)
   :text (.getText arg)})

(defn ScriptStatistics-to-edn
  [^JobStatistics$ScriptStatistics arg]
  {:evaluationKind (.getEvaluationKind arg)
   :stackFrames    (mapv ScriptStackFrame-to-edn (.getStackFrames arg))})

(defn to-edn [^JobStatistics arg]
  {:post [(global/strict! :bigquery/JobStatistics %)]}
  (cond-> {}
          (some? (.getCreationTime arg))
          (assoc :creationTime (.getCreationTime arg))

          (some? (.getEndTime arg))
          (assoc :endTime (.getEndTime arg))

          (some? (.getNumChildJobs arg))
          (assoc :numChildJobs (.getNumChildJobs arg))

          (some? (.getParentJobId arg))
          (assoc :parentJobId (.getParentJobId arg))

          (some? (.getReservationUsage arg))
          (assoc :reservationUsage (map ReservationUsage-to-edn (.getReservationUsage arg)))

          (some? (.getScriptStatistics arg))
          (assoc :scriptStatistics (ScriptStatistics-to-edn (.getScriptStatistics arg)))

          (some? (.getSessionInfo arg))
          (assoc :sessionInfo {:sessionId (.getSessionId (.getSessionInfo arg))})

          (some? (.getStartTime arg))
          (assoc :startTime (.getStartTime arg))

          (some? (.getTotalSlotMs arg))
          (assoc :totalSlotMs (.getTotalSlotMs arg))

          (some? (.getTransactionInfo arg))
          (assoc :transactionInfo {:transactionId (.getTransactionId (.getTransactionInfo arg))})))
