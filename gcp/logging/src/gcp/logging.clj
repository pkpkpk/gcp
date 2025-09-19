(ns ^{:url "https://cloud.google.com/java/docs/reference/google-cloud-logging/latest/com.google.cloud.logging.Logging"}
  gcp.logging
  (:require [gcp.global :as g]
            [gcp.logging.Logging :as L]
            [gcp.logging.LoggingOptions :as LO]
            [gcp.logging.Sink :as Sink])
  (:import [com.google.cloud.logging Logging Logging$ListOption]))

(defonce ^:dynamic *client* nil)

(defn ^Logging client
  ([]
   (client nil))
  ([arg]
   (or *client*
       (if (instance? Logging arg)
         arg
         (LO/get-service arg)))))

;- `create(Exclusion exclusion)` — create a log exclusion. [sync]
;- `create(MetricInfo metric)` — create a logs-based metric. [sync]
;- `create(SinkInfo sink)` — create a sink. [sync]
;- `createAsync(Exclusion exclusion)` — create a log exclusion. [async]
;- `createAsync(MetricInfo metric)` — create a logs-based metric. [async]
;- `createAsync(SinkInfo sink)` — create a sink. [async]
;- `deleteExclusion(String exclusion)` — delete an exclusion by name. [sync]
;- `deleteExclusionAsync(String exclusion)` — delete an exclusion by name. [async]
;- `deleteLog(String log)` — delete a log by name (default destination). [sync]
;- `deleteLog(String log, LogDestinationName destination)` — delete a log in a specific destination. [sync]
;- `deleteLogAsync(String log)` — delete a log by name (default destination). [async]
;- `deleteLogAsync(String log, LogDestinationName destination)` — delete a log in a specific destination. [async]
;- `deleteMetric(String metric)` — delete a metric by name. [sync]
;- `deleteMetricAsync(String metric)` — delete a metric by name. [async]
;- `deleteSink(String sink)` — delete a sink by name. [sync]
;- `deleteSinkAsync(String sink)` — delete a sink by name. [async]
;- `flush()` — force-flush buffered writes.
;- `getExclusion(String exclusion)` — get an exclusion by name. [sync]
;- `getExclusionAsync(String exclusion)` — get an exclusion by name. [async]
;- `getFlushSeverity()` — current flush threshold `Severity`.
;- `getMetric(String metric)` — get a metric by name. [sync]
;- `getMetricAsync(String metric)` — get a metric by name. [async]
;- `getSink(String sink)` — get a sink by name. [sync]
;- `getSinkAsync(String sink)` — get a sink by name. [async]
;- `getWriteSynchronicity()` — current `Synchronicity` for writes.
;- `listExclusions(Logging.ListOption[] options)` — list exclusions. [sync]
;- `listExclusionsAsync(Logging.ListOption[] options)` — list exclusions. [async]
;- `listLogEntries(Logging.EntryListOption[] options)` — list log entries. [sync]
;- `listLogEntriesAsync(Logging.EntryListOption[] options)` — list log entries. [async]

;- `listLogs(Logging.ListOption[] options)` — list log names. [sync]
;- `listLogsAsync(Logging.ListOption[] options)` — list log names. [async]
(defn list-logs [& args]
  (if (and (some? (first args)) (g/valid? LO/clientable-schema (first args)))
    (seq (.iterateAll (.listLogs (client (first args)) (into-array Logging$ListOption (map L/ListOption:from-edn (rest args))))))
    (seq (.iterateAll (.listLogs (client) (into-array Logging$ListOption (map L/ListOption:from-edn args)))))))

;- `listMetrics(Logging.ListOption[] options)` — list metrics. [sync]
;- `listMetricsAsync(Logging.ListOption[] options)` — list metrics. [async]
;- `listMonitoredResourceDescriptors(Logging.ListOption[] options)` — list MRDs. [sync]
;- `listMonitoredResourceDescriptorsAsync(Logging.ListOption[] options)` — list MRDs. [async]

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
;- `update(Exclusion exclusion)` — update an exclusion. [sync]
;- `update(MetricInfo metric)` — update a metric. [sync]
;- `update(SinkInfo sink)` — update a sink. [sync]
;- `updateAsync(Exclusion exclusion)` — update an exclusion. [async]
;- `updateAsync(MetricInfo metric)` — update a metric. [async]
;- `updateAsync(SinkInfo sink)` — update a sink. [async]
;- `write(Iterable<LogEntry> logEntries, Logging.WriteOption[] options)` — write entries.

