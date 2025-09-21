(ns gcp.logging.LogEntry
  (:require [gcp.global :as g]
            [gcp.core.MonitoredResource :as MonitoredResource]
            [gcp.logging.HttpRequest :as HttpRequest]
            [gcp.logging.LogDestinationName :as LogDestinationName]
            [gcp.logging.Operation :as Operation]
            [gcp.logging.Payload :as Payload]
            [gcp.logging.Severity :as Severity]
            [gcp.logging.SourceLocation :as SourceLocation])
  (:import (com.google.cloud.logging LogEntry)))

(def schema
  (g/schema
    [:map
     [:payload Payload/schema]
     [:destination {:optional true} LogDestinationName/schema]
     [:httpRequest {:optional true} HttpRequest/schema]
     [:insertId {:optional true} :string]
     [:labels {:optional true} [:map-of :string :string]]
     [:logName {:optional true} :string]
     [:operation {:optional true} Operation/schema]
     [:receiveTimestamp {:optional true} [:or inst? :int]]
     [:resource {:optional true} MonitoredResource/schema]
     [:severity {:optional true} Severity/schema]
     [:sourceLocation {:optional true} SourceLocation/schema]
     [:spanId {:optional true} :any]
     [:timestamp {:optional true} :any]
     [:trace {:optional true} :any]
     [:traceSampled {:optional true} :boolean]]))

(defn ^LogEntry from-edn
  [{:keys [logName resource payload timestamp receiveTimestamp
           severity insertId httpRequest labels operation trace spanId
           traceSampled sourceLocation destination] :as arg}]
  (g/strict! schema arg)
  (let [builder (LogEntry/newBuilder (Payload/from-edn payload))]
    (some->> destination LogDestinationName/from-edn (.setDestination builder))
    (some->> httpRequest HttpRequest/from-edn (.setHttpRequest builder))
    (some->> insertId (.setInsertId builder))
    (some->> labels (.setLabels builder))
    (some->> logName (.setLogName builder))
    (some->> operation Operation/from-edn (.setOperation builder))
    (some->> receiveTimestamp (.setReceiveTimestamp builder))
    (some->> resource MonitoredResource/from-edn (.setResource builder))
    (some->> severity Severity/from-edn (.setSeverity builder))
    (some->> sourceLocation SourceLocation/from-edn (.setSourceLocation builder))
    (some->> spanId (.setSpanId builder))
    (some->> trace (.setTrace builder))
    (some->> timestamp (.setTimestamp builder))
    (some->> traceSampled (.setTraceSampled builder))
    (.build builder)))

(defn to-edn [^LogEntry arg]
  (cond-> {:payload (Payload/to-edn (.getPayload arg))}

          (.getDestination arg)
          (assoc :destination (LogDestinationName/to-edn (.getDestination arg)))

          (.getHttpRequest arg)
          (assoc :httpRequest (HttpRequest/to-edn (.getHttpRequest arg)))

          (.getInsertId arg)
          (assoc :insertId (.getInsertId arg))

          (seq (.getLabels arg))
          (assoc :labels (into {} (.getLabels arg)))

          (.getLogName arg)
          (assoc :logName (.getLogName arg))

          (.getOperation arg)
          (assoc :operation (Operation/to-edn (.getOperation arg)))

          (.getInstantReceiveTimestamp arg)
          (assoc :receiveTimestamp (.getInstantReceiveTimestamp arg))

          (.getResource arg)
          (assoc :resource (MonitoredResource/to-edn (.getResource arg)))

          (some? (.getSeverity arg))
          (assoc :severity (.name (.getSeverity arg)))

          (.getSourceLocation arg)
          (assoc :sourceLocation (SourceLocation/to-edn (.getSourceLocation arg)))

          (.getSpanId arg)
          (assoc :spanId (.getSpanId arg))

          (.getInstantTimestamp arg)
          (assoc :timestamp (.getInstantTimestamp arg))

          (.getTrace arg)
          (assoc :trace (.getTrace arg))

          (some? (.getTraceSampled arg))
          (assoc :traceSampled (.getTraceSampled arg))))