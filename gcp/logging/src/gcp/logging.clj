(ns ^{:url "https://cloud.google.com/java/docs/reference/google-cloud-logging/latest/com.google.cloud.logging.Logging"}
  gcp.logging
  (:require [gcp.global :as g]
            [gcp.logging.Exclusion :as Exclusion]
            [gcp.logging.LogDestinationName :as LogDestinationName]
            [gcp.logging.LogEntry :as LogEntry]
            [gcp.logging.Logging :as L]
            [gcp.logging.LoggingOptions :as LO]
            [gcp.logging.Metric :as Metric]
            [gcp.logging.MetricInfo :as MetricInfo]
            [gcp.logging.Sink :as Sink]
            [gcp.logging.SinkInfo :as SinkInfo])
  (:import (com.google.api LabelDescriptor)
           (com.google.cloud MonitoredResourceDescriptor MonitoredResourceDescriptor$LabelDescriptor)
           [com.google.cloud.logging LogEntryServerStream Logging Logging$EntryListOption Logging$ListOption Logging$TailOption]))

;- `createAsync(Exclusion exclusion)` — create a log exclusion. [async]
;- `createAsync(MetricInfo metric)` — create a logs-based metric. [async]
;- `createAsync(SinkInfo sink)` — create a sink. [async]
;- `deleteExclusionAsync(String exclusion)` — delete an exclusion by name. [async]
;- `deleteLogAsync(String log)` — delete a log by name (default destination). [async]
;- `deleteLogAsync(String log, LogDestinationName destination)` — delete a log in a specific destination. [async]
;- `deleteMetricAsync(String metric)` — delete a metric by name. [async]
;- `getExclusionAsync(String exclusion)` — get an exclusion by name. [async]
;- `getMetricAsync(String metric)` — get a metric by name. [async]
;- `deleteSinkAsync(String sink)` — delete a sink by name. [async]
;- `getSinkAsync(String sink)` — get a sink by name. [async]
;- `listExclusionsAsync(Logging.ListOption[] options)` — list exclusions. [async]
;- `updateAsync(Exclusion exclusion)` — update an exclusion. [async]
;- `updateAsync(MetricInfo metric)` — update a metric. [async]
;- `updateAsync(SinkInfo sink)` — update a sink. [async]

(defonce ^:dynamic *client* nil)

(defn ^Logging client
  ([]
   (client nil))
  ([arg]
   (or *client*
       (if (instance? Logging arg)
         arg
         (LO/get-service arg)))))

(defn create-exclusion
  "create a log exclusion."
  ([exclusion]
   (create-exclusion nil exclusion))
  ([clientable exclusion]
   (Exclusion/to-edn (.create (client clientable) (Exclusion/from-edn exclusion)))))

(defn create-metric
  "create a logs-based metric."
  ([metric]
   (create-metric nil metric))
  ([clientable metric]
   (MetricInfo/to-edn (.create (client clientable) (MetricInfo/from-edn metric)))))

(defn create-sink
  "create a sink."
  ([sink]
   (create-sink nil sink))
  ([clientable sink]
   (SinkInfo/to-edn (.create (client clientable) (SinkInfo/from-edn sink)))))

(defn delete-exclusion
  "delete an exclusion by name."
  ([exclusion] (delete-exclusion nil exclusion))
  ([clientable exclusion] (.deleteExclusion (client clientable) exclusion)))

(defn delete-log
  "delete a log by name (default destination)."
  ([log] (delete-log nil log))
  ([clientable log] (.deleteLog (client clientable) log)))

(defn delete-log-in-destination
  "delete a log in a specific destination."
  ([arg0]
   (delete-log-in-destination nil arg0 nil))
  ([arg0 arg1]
   (if (g/valid? LO/clientable-schema arg0)
     (delete-log-in-destination arg0 arg1 nil)
     (delete-log-in-destination nil arg0 arg1)))
  ([clientable log destination]
   (.deleteLog (client clientable) (g/coerce :string log) (LogDestinationName/from-edn destination))))

(defn delete-metric
  "delete a metric by name."
  ([metric] (delete-metric nil metric))
  ([clientable metric] (.deleteMetric (client clientable) metric)))

(defn delete-sink
  "delete a sink by name."
  ([sink] (delete-sink nil sink))
  ([clientable sink] (.deleteSink (client clientable) sink)))

;- `flush()` — force-flush buffered writes.
;- `getExclusion(String exclusion)` — get an exclusion by name. [sync]
;- `getFlushSeverity()` — current flush threshold `Severity`.
;- `getMetric(String metric)` — get a metric by name. [sync]
;- `getSink(String sink)` — get a sink by name. [sync]
;- `getWriteSynchronicity()` — current `Synchronicity` for writes.

(defn list-exclusions [& args]
  (let [xs (if (and (some? (first args)) (g/valid? LO/clientable-schema (first args)))
             (.listExclusions (client (first args)) (into-array Logging$ListOption (map L/ListOption:from-edn (rest args))))
             (.listExclusions (client) (into-array Logging$ListOption (map L/ListOption:from-edn args))))]
    (map Exclusion/to-edn (seq (.iterateAll xs)))))

;- `listLogEntries(Logging.EntryListOption[] options)` — list log entries. [sync]
;- `listLogEntriesAsync(Logging.EntryListOption[] options)` — list log entries. [async]
(defn list-log-entries [& args]
  (let [xs (if (and (some? (first args)) (g/valid? LO/clientable-schema (first args)))
             (.listMetrics (client (first args)) (into-array Logging$EntryListOption (map L/EntryListOption:from-edn (rest args))))
             (.listMetrics (client) (into-array Logging$EntryListOption (map L/EntryListOption:from-edn args))))]
    (map LogEntry/to-edn (seq (.iterateAll xs)))))

;- `listLogs(Logging.ListOption[] options)` — list log names. [sync]
;- `listLogsAsync(Logging.ListOption[] options)` — list log names. [async]
(defn list-logs [& args]
  (if (and (some? (first args)) (g/valid? LO/clientable-schema (first args)))
    (seq (.iterateAll (.listLogs (client (first args)) (into-array Logging$ListOption (map L/ListOption:from-edn (rest args))))))
    (seq (.iterateAll (.listLogs (client) (into-array Logging$ListOption (map L/ListOption:from-edn args)))))))

;- `listMetrics(Logging.ListOption[] options)` — list metrics. [sync]
;- `listMetricsAsync(Logging.ListOption[] options)` — list metrics. [async]
(defn list-metrics [& args]
  (let [xs (if (and (some? (first args)) (g/valid? LO/clientable-schema (first args)))
             (.listMetrics (client (first args)) (into-array Logging$ListOption (map L/ListOption:from-edn (rest args))))
             (.listMetrics (client) (into-array Logging$ListOption (map L/ListOption:from-edn args))))]
    (map Metric/to-edn (seq (.iterateAll xs)))))

;- `listMonitoredResourceDescriptors(Logging.ListOption[] options)` — list MRDs. [sync]
;- `listMonitoredResourceDescriptorsAsync(Logging.ListOption[] options)` — list MRDs. [async]
(defn list-MRDs [& args]
  (let [xs (if (and (some? (first args)) (g/valid? LO/clientable-schema (first args)))
             (.listMonitoredResourceDescriptors (client (first args)) (into-array Logging$ListOption (map L/ListOption:from-edn (rest args))))
             (.listMonitoredResourceDescriptors (client) (into-array Logging$ListOption (map L/ListOption:from-edn args))))]
    (map
      (fn [^MonitoredResourceDescriptor mrd]
        (cond-> {:type (.getType mrd)
                 :displayName (.getDisplayName mrd)
                 :name (.getName mrd)}
                (.getDescription mrd) (assoc :description (.getDescription mrd))
                (.getLabels mrd) (assoc :labels
                                        (map
                                          (fn [^MonitoredResourceDescriptor$LabelDescriptor ld]
                                            {:description (.getDescription ld)
                                             :key         (.getKey ld)
                                             :valueType   (.name (.getValueType ld))})
                                          (.getLabels mrd)))))
      (seq (.iterateAll xs)))))

;- `listSinks(Logging.ListOption[] options)` — list sinks. [sync]
;- `listSinksAsync(Logging.ListOption[] options)` — list sinks. [async]
(defn list-sinks [& args]
  (let [xs (if (and (some? (first args)) (g/valid? LO/clientable-schema (first args)))
             (.listSinks (client (first args)) (into-array Logging$ListOption (map L/ListOption:from-edn (rest args))))
             (.listSinks (client) (into-array Logging$ListOption (map L/ListOption:from-edn args))))]
    (map Sink/to-edn (seq (.iterateAll xs)))))

;- `populateMetadata(Iterable<LogEntry> logEntries, MonitoredResource customResource, String[] exclusionClassPaths)` — enrich entries with resource/labels.
;- `setFlushSeverity(Severity flushSeverity)` — set flush threshold.
;- `setWriteSynchronicity(Synchronicity synchronicity)` — set write mode.

;- `tailLogEntries(Logging.TailOption[] options)` — stream/tail entries.
(defn ^LogEntryServerStream tail-log-entries
  "https://cloud.google.com/java/docs/reference/google-cloud-logging/latest/com.google.cloud.logging.LogEntryServerStream"
  [& args]
  (if (and (some? (first args)) (g/valid? LO/clientable-schema (first args)))
    (.tailLogEntries (client (first args)) (into-array Logging$TailOption (map L/TailOption:from-edn (rest args))))
    (.tailLogEntries (client) (into-array Logging$TailOption (map L/TailOption:from-edn args)))))

(defn update-exclusion
  ([exclusion]
   (update-exclusion nil exclusion))
  ([clientable exclusion]
   (Exclusion/to-edn (.update (client clientable) (Exclusion/from-edn exclusion)))))

(defn update-metric
  ([metric]
   (update-metric nil metric))
  ([clientable metric]
   (MetricInfo/to-edn (.update (client clientable) (MetricInfo/from-edn metric)))))

(defn update-sink
  ([sink] (update-sink nil sink))
  ([clientable sink]
   (SinkInfo/to-edn (.update (client clientable) (SinkInfo/from-edn sink)))))


;- `write(Iterable<LogEntry> logEntries, Logging.WriteOption[] options)` — write entries.

