;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.PlatformLogsSettings
  {:doc "<pre>\nSettings for Platform Logs produced by Pub/Sub.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.PlatformLogsSettings}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.PlatformLogsSettings"
   :gcp.dev/certification {:base-seed 1777050372490
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777050372490 :standard 1777050372491 :stress 1777050372492}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-24T17:06:13.344200325Z"}}
  (:require
   [gcp.global :as global])
  (:import
   (com.google.pubsub.v1 PlatformLogsSettings PlatformLogsSettings$Builder PlatformLogsSettings$Severity)))

(declare from-edn to-edn Severity-from-edn Severity-to-edn)

(def Severity-schema
  [:enum
   {:closed true
    :doc
    "<pre>\nSeverity levels of Platform Logs.\n</pre>\n\nProtobuf enum {@code google.pubsub.v1.PlatformLogsSettings.Severity}"
    :gcp/category :nested/enum
    :gcp/key :gcp.pubsub.v1/PlatformLogsSettings.Severity}
   "SEVERITY_UNSPECIFIED" "DISABLED" "DEBUG" "INFO" "WARNING" "ERROR"])

(defn ^PlatformLogsSettings from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/PlatformLogsSettings arg)
  (let [builder (PlatformLogsSettings/newBuilder)]
    (when (some? (get arg :severity))
      (.setSeverity builder
                    (PlatformLogsSettings$Severity/valueOf (get arg
                                                                :severity))))
    (.build builder)))

(defn to-edn
  [^PlatformLogsSettings arg]
  {:post [(global/strict! :gcp.pubsub.v1/PlatformLogsSettings %)]}
  (when arg
    (cond-> {}
      (.getSeverity arg) (assoc :severity (.name (.getSeverity arg))))))

(def schema
  [:map
   {:closed true
    :doc
    "<pre>\nSettings for Platform Logs produced by Pub/Sub.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.PlatformLogsSettings}"
    :gcp/category :protobuf-message
    :gcp/key :gcp.pubsub.v1/PlatformLogsSettings}
   [:severity
    {:optional true
     :getter-doc
     "<pre>\nOptional. The minimum severity level of Platform Logs that will be written.\n</pre>\n\n<code>\n.google.pubsub.v1.PlatformLogsSettings.Severity severity = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The severity."
     :setter-doc
     "<pre>\nOptional. The minimum severity level of Platform Logs that will be written.\n</pre>\n\n<code>\n.google.pubsub.v1.PlatformLogsSettings.Severity severity = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@param value The severity to set.\n@return This builder for chaining."}
    [:enum {:closed true} "SEVERITY_UNSPECIFIED" "DISABLED" "DEBUG" "INFO"
     "WARNING" "ERROR"]]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/PlatformLogsSettings schema
              :gcp.pubsub.v1/PlatformLogsSettings.Severity Severity-schema}
    {:gcp.global/name "gcp.pubsub.v1.PlatformLogsSettings"}))
