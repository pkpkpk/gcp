(ns gcp.logging.Severity
  (:require [gcp.global :as g])
  (:import (com.google.cloud.logging Severity)))

(def schema
  (g/schema
    [:or
     [:= {:doc "A person must take an action immediately."} "ALERT"]
     [:= {:doc "Critical events cause more severe problems or brief outages."} "CRITICAL"]
     [:= {:doc "Debug or trace information."} "DEBUG"]
     [:= {:doc "The log entry has no assigned severity level."} "DEFAULT"]
     [:= {:doc "One or more systems are unusable."} "EMERGENCY"]
     [:= {:doc "Error events are likely to cause problems."} "ERROR"]
     [:= {:doc "Routine information, such as ongoing status or performance."} "INFO"]
     [:= {:doc "The None severity level. Should not be used with log entries."} "NONE"]
     [:= {:doc "Normal but significant events, such as start up, shut down, or configuration."} "NOTICE"]
     [:= {:doc "Warning events might cause problems."} "WARNING"]]))

(defn ^Severity from-edn [arg] (Severity/valueOf arg))

(defn ^String to-edn [^Severity arg] (.name arg))