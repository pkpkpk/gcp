(ns gcp.bigquery.v2.JobStatistics
  (:require [gcp.global :as global])
  (:import (com.google.cloud.bigquery JobStatistics
                                      JobStatistics$ReservationUsage
                                      JobStatistics$ScriptStatistics
                                      JobStatistics$ScriptStatistics$ScriptStackFrame)))

(defn ReservationUsage:to-edn
  [^JobStatistics$ReservationUsage arg]
  {:name (.getName arg)
   :slotMs (.getSlotMs arg)})

(defn ScriptStackFrame:to-edn
  [^JobStatistics$ScriptStatistics$ScriptStackFrame arg]
  {:endColumn (.getEndColumn arg)
   :endLine (.getEndLine arg)
   :procedureId (.getProcedureId arg)
   :startColumn (.getStartColumn arg)
   :startLine (.getStartLine arg)
   :text (.getText arg)})

(defn ScriptStatistics:to-edn
  [^JobStatistics$ScriptStatistics arg]
  {:evaluationKind (.getEvaluationKind arg)
   :stackFrames    (mapv ScriptStackFrame:to-edn (.getStackFrames arg))})

(defn to-edn [^JobStatistics arg]
  {:post [(global/strict! :gcp.bigquery.v2/JobStatistics %)]}
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
          (assoc :reservationUsage (map ReservationUsage:to-edn (.getReservationUsage arg)))

          (some? (.getScriptStatistics arg))
          (assoc :scriptStatistics (ScriptStatistics:to-edn (.getScriptStatistics arg)))

          (some? (.getSessionInfo arg))
          (assoc :sessionInfo {:sessionId (.getSessionId (.getSessionInfo arg))})

          (some? (.getStartTime arg))
          (assoc :startTime (.getStartTime arg))

          (some? (.getTotalSlotMs arg))
          (assoc :totalSlotMs (.getTotalSlotMs arg))

          (some? (.getTransactionInfo arg))
          (assoc :transactionInfo {:transactionId (.getTransactionId (.getTransactionInfo arg))})))

(def schemas
  {:gcp.bigquery.v2/JobStatistics
   [:map
    [:creationTime {:optional true} :int] ; Typically a timestamp in ms
    [:endTime {:optional true} :int] ; Also a timestamp in ms
    [:numChildJobs {:optional true} :int]
    [:parentJobId {:optional true} :string]
    [:reservationUsage
     {:optional true
      :doc      "ReservationUsage contains information about a job's usage of a single reservation."}
     [:map
      [:name :string]
      [:slotMs :int]]]
    [:scriptStatistics {:optional true}
     [:map {:closed true}
      [:evaluationKind {:doc "child job was statement or expression"} :string]
      [:stackFrames {:doc "Stack trace showing the line/column/procedure name of each frame on the stack at the point where the current evaluation happened. The leaf frame is first, the primary script is last. Never empty."}
       [:sequential
        [:map {:closed true}
         [:endColumn :int]
         [:endLine :int]
         [:procedureId :string]
         [:startColumn :int]
         [:startLine :int]
         [:text :string]]]]]]
    [:sessionInfo {:optional true
                   :doc      "SessionInfo contains information about the session if this job is part of one."}
     [:map {:closed true} [:sessionId :string]]]
    [:startTime {:optional true} :int] ; Timestamp in ms
    [:totalSlotMs {:optional true} :int]
    [:transactionInfo
     {:optional true
      :doc      "TransactionInfo contains information about a multi-statement transaction that may have associated with a job."}
     [:map {:closed true} [:transactionId :string]]]]})

(global/include-schema-registry! (with-meta schemas {:gcp.global/name (str *ns*)}))
