(ns gcp.bigquery.custom.BigQueryOptions
  {:file-git-sha "6dcc90053353422ae766e531413b3ecc65b8b155",
   :fqcn         "com.google.cloud.bigquery.BigQueryOptions"}
  (:require [gcp.bigquery.DataFormatOptions :as DataFormatOptions]
            [gcp.global :as g])
  (:import [com.google.cloud.bigquery BigQuery BigQueryOptions QueryJobConfiguration$JobCreationMode]
           io.opentelemetry.api.trace.Tracer
           com.google.cloud.TransportOptions
           com.google.api.gax.retrying.ResultRetryAlgorithm))

(defn ^BigQueryOptions from-edn
  ([]
   (from-edn nil))
  ([arg]
   (g/strict! :gcp.bigquery/BigQueryOptions arg)
   (if (empty? arg)
     (BigQueryOptions/getDefaultInstance)
     (if (instance? BigQueryOptions arg)
       arg
       (let [builder (BigQueryOptions/newBuilder)]
         (when (some? (get arg :dataFormatOptions))
           (.setDataFormatOptions builder (DataFormatOptions/from-edn (get arg :dataFormatOptions))))
         (when (some? (get arg :enableOpenTelemetryTracing))
           (.setEnableOpenTelemetryTracing builder (get arg :enableOpenTelemetryTracing)))
         (when (some? (get arg :location))
           (.setLocation builder (get arg :location)))
         (when-some [tracer (get arg :openTelemetryTracer)]
           (.setOpenTelemetryTracer builder tracer))
         (when-some [retryAlgorithm (get arg :resultRetryAlgorithm)]
           (.setResultRetryAlgorithm builder retryAlgorithm))
         (when-some [transportOptions (get arg :transportOptions)]
           (.setTransportOptions builder transportOptions))
         (let [instance (.build builder)]
           (when-some [defaultJobCreationMode (get arg :defaultJobCreationMode)]
             (.setDefaultJobCreationMode instance (QueryJobConfiguration$JobCreationMode/valueOf defaultJobCreationMode)))
           instance))))))

(defn ^BigQuery get-service [arg]
  (if (instance? BigQuery arg)
    arg
    (.getService (from-edn arg))))

(defn ^String get-project-id
  ([]
   (get-project-id nil))
  ([arg]
   (.getProjectId (from-edn arg))))

(defn ^String get-library-version
  ([]
   (get-library-version nil))
  ([arg]
   (.getLibraryVersion (from-edn arg))))

(defn to-edn
  [^BigQueryOptions arg]
  {:post [(g/strict! :gcp.bigquery/BigQueryOptions %)]}
  (cond-> {}
    (.getDataFormatOptions arg)          (assoc :dataFormatOptions (DataFormatOptions/to-edn (.getDataFormatOptions arg)))
    (.getDefaultJobCreationMode arg)     (assoc :defaultJobCreationMode (.name (.getDefaultJobCreationMode arg)))
    (.isOpenTelemetryTracingEnabled arg) (assoc :enableOpenTelemetryTracing (.isOpenTelemetryTracingEnabled arg))
    (.getLocation arg)                   (assoc :location (.getLocation arg))
    (.getOpenTelemetryTracer arg)        (assoc :openTelemetryTracer (.getOpenTelemetryTracer arg))
    (.getResultRetryAlgorithm arg)       (assoc :resultRetryAlgorithm (.getResultRetryAlgorithm arg))
    (.getThrowNotFound arg)              (assoc :setThrowNotFound (.getThrowNotFound arg))))

(def schema
  [:or
   {:closed       true,
    :doc          nil,
    :gcp/category :accessor-with-builder,
    :gcp/key      :gcp.bigquery/BigQueryOptions}
   (g/instance-schema com.google.cloud.bigquery.BigQueryOptions)
   [:maybe
    [:map
     [:dataFormatOptions
      {:optional   true,
       :setter-doc "Set the format options for the BigQuery data types\n\n@param dataFormatOptions Configuration of the formatting options"}
      :gcp.bigquery/DataFormatOptions]
     [:defaultJobCreationMode {:optional true, :read-only? true}
      [:enum "JOB_CREATION_MODE_UNSPECIFIED" "JOB_CREATION_REQUIRED" "JOB_CREATION_OPTIONAL"]]
     [:enableOpenTelemetryTracing
      {:optional   true,
       :getter-doc "Returns whether this BigQuery instance has OpenTelemetry tracing enabled\n\n@return true if tracing is enabled, false if not",
       :setter-doc "Enables OpenTelemetry tracing functionality for this BigQuery instance\n\n@param enableOpenTelemetryTracing enables OpenTelemetry tracing if true"}
      :boolean]
     [:location
      {:optional true}
      [:string {:min 1}]]
     [:openTelemetryTracer
      {:optional   true,
       :getter-doc "Returns the OpenTelemetry tracer used by this BigQuery instance\n\n@return OpenTelemetry tracer object or {@code null} if not set",
       :setter-doc "Sets the OpenTelemetry tracer for this BigQuery instance to be tracer. @param tracer OpenTelemetry tracer to be used"}
      (g/instance-schema io.opentelemetry.api.trace.Tracer)]
     [:resultRetryAlgorithm
      {:optional true}
      (g/instance-schema com.google.api.gax.retrying.ResultRetryAlgorithm)]
     [:setThrowNotFound
      {:optional true, :read-only? true} :boolean]
     [:transportOptions
      {:optional true}
      (g/instance-schema com.google.cloud.TransportOptions)]]]])

(g/include-schema-registry!
  (with-meta {:gcp.bigquery/BigQueryOptions schema}
    {:gcp.global/name "gcp.bigquery.BigQueryOptions"}))