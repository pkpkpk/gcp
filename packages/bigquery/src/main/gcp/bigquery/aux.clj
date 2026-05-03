(ns gcp.bigquery.aux
  {:doc "convenience functions that sugar top-level api functions"}
  (:require
    [clojure.java.io :as io]
    [gcp.bigquery :as bq]
   [gcp.global :as g])
  (:import
   (java.nio.channels Channels)))

(defn get-schema [dataset table]
  (when-let [T (bq/get-table dataset table)]
    (get-in T [:definition :schema])))

(defn export-query
  "Exports the result of a SQL query directly to a GCS bucket using EXPORT DATA.
   `format` should be 'JSON' (for JSONL) or 'CSV'.
   If the URI does not contain a wildcard '*', one is automatically inserted before the extension."
  [query uri format]
  (let [uri (if (clojure.string/includes? uri "*")
              uri
              (let [idx (.lastIndexOf uri ".")]
                (if (pos? idx)
                  (str (subs uri 0 idx) "-*" (subs uri idx))
                  (str uri "-*"))))]
    (bq/q (str "EXPORT DATA OPTIONS(ok uri= ?, format= ?, overwrite=true) AS " query)
          [uri format])))
(defn
  ^{:urls ["https://cloud.google.com/bigquery/docs/exporting-data"
           "https://cloud.google.com/bigquery/docs/reference/standard-sql/export-statements"
           "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.ExtractJobConfiguration"]}
  extract-table
  ([table format compression dst]
   (extract-table table format compression dst nil))
  ([table format compression dst opts]
   (let [table (if (g/valid? :gcp.bigquery/TableId table)
                 table
                 (if (g/valid? :gcp.bigquery/TableInfo table)
                   (get table :tableId)
                   (throw (ex-info "must provide valid tableId" {:table table
                                                                 :format format
                                                                 :dst dst
                                                                 :opts opts}))))
         dst (if (string? dst)
               [dst]
               (if (g/valid? [:sequential :string] dst)
                 dst
                 (throw (ex-info "destination should be string uris" {:table table
                                                                      :format format
                                                                      :dst dst
                                                                      :opts opts}))))
         configuration (cond-> {:type            "EXTRACT"
                                :sourceTable     (g/coerce :gcp.bigquery/TableId table)
                                :format          format
                                :destinationUris dst}
                               compression (assoc :compression compression))
         jobInfo {:configuration (g/coerce :gcp.bigquery/ExtractJobConfiguration configuration)}]
     (bq/create-job (:bigquery table) jobInfo opts))))

(defn export-from-temp
  "Executes a query into a temporary table, then uses the Extract API to export it.
   This bypasses the wildcard requirement of EXPORT DATA and the table-only limitation of EXTRACT.
   `dataset` is required to determine where to create the temporary table.
   `format` should be 'NEWLINE_DELIMITED_JSON' or 'CSV'.
   `params` is an optional sequence of positional SQL parameters."
  ([dataset uri format query]
   (export-from-temp dataset uri format query nil))
  ([dataset uri format query params]
   (when-not dataset
     (throw (ex-info "dataset is required for export-from-temp" {:query query :uri uri})))
   (let [temp-table   (str "tmp_export_" (clojure.string/replace (str (random-uuid)) "-" ""))
         table-id     {:dataset dataset :table temp-table}
         query-config (cond-> {:type "QUERY"
                               :query query
                               :destinationTable table-id
                               :writeDisposition "WRITE_TRUNCATE"}
                              (seq params) (assoc :useLegacySql false
                                                  :parameterMode "POSITIONAL"
                                                  :queryParameters params))]
     (try
       (bq/wait-for (bq/create-job {:configuration query-config}))
       (bq/wait-for (extract-table table-id format nil uri))
       (finally
         (try (bq/q (str "DROP TABLE IF EXISTS `" dataset "." temp-table "`"))
              (catch Exception _)))))))

(defn extract-parquet
  ([dataset table bucket]
   (extract-table {:dataset dataset :table table} "PARQUET" "GZIP" (str "gs://" bucket "/" table ".parquet"))))

(defn extract-jsonl
  ([dataset table bucket]
   (extract-jsonl dataset table bucket table))
  ([dataset table bucket filename]
   (extract-table {:dataset dataset :table table} "NEWLINE_DELIMITED_JSON" "GZIP" (str "gs://" bucket "/" filename ".jsonl"))))

(defn clone-table
  ([source destination]
   (let [source-tables (if (g/valid? [:sequential :gcp.bigquery/TableId] source)
                         source
                         (if (g/valid? :gcp.bigquery/TableId source)
                           [source]
                           (if (g/valid? :gcp.bigquery/TableInfo source)
                             [(:tableId source)]
                             (if (g/valid? [:sequential :gcp.bigquery/TableInfo] source)
                               (mapv :tableId source)
                               (throw (ex-info "cannot create clone source"
                                               {:source      source
                                                :destination destination}))))))
         destination-table (if (g/valid? :gcp.bigquery/TableId destination)
                             destination
                             (if (string? destination)
                               (if (= 1 (count source-tables))
                                 {:dataset destination
                                  :table   (get (first source-tables) :table)}
                                 (throw (ex-info "must provide name for composite destination table"
                                                 {:source source
                                                  :destination destination})))
                               (throw (ex-info "cannot create clone destination"
                                               {:source source
                                                :destination destination}))))
         configuration {:type "COPY"
                        :sourceTables     (g/coerce [:sequential :gcp.bigquery/TableId] source-tables)
                        :destinationTable (g/coerce :gcp.bigquery/TableId destination-table)
                        :operationType    "CLONE"
                        :writeDisposition "WRITE_EMPTY"}]
     (bq/create-job {:jobInfo {:configuration (g/coerce :gcp.bigquery/CopyJobConfiguration configuration)}})))
  ([sourceDataset sourceTable destinationDataset]
   (let [source (g/coerce :gcp.bigquery/TableId {:dataset sourceDataset :table sourceTable})]
     (clone-table source destinationDataset)))
  ([sourceDataset sourceTable destinationDataset destinationTable]
   (let [source (g/coerce :gcp.bigquery/TableId {:dataset sourceDataset :table sourceTable})
         destination (g/coerce :gcp.bigquery/TableId {:dataset destinationDataset :table destinationTable})]
     (clone-table source destination))))

(defn- filename-without-ext [f]
  (let [name    (.getName f)
        idx     (.lastIndexOf name ".")]
    (if (pos? idx)
      (subs name 0 idx)
      name)))

(defn open-csv-writer
  "Returns a java.io.Writer that streams directly to BigQuery as CSV.
   The caller must close the writer (e.g. using with-open) to trigger the job.
   Once closed, use `gcp.bigquery/wait-for` on the `jobId` to await completion.

   Its recommended to use the otherwise optional location field of the jobId such that
   it can be retrieved using bq/get-job"
  ([tableId jobId]
   (open-csv-writer tableId jobId nil))
  ([tableId jobId cfg]
   (let [cfg     (merge {:destinationTable tableId
                         :formatOptions    {:type "CSV"}} cfg)
         channel (bq/writer jobId cfg)
         stream  (Channels/newOutputStream channel)]
     (io/writer stream))))

(defn load-local-file
  ([dataset table file]
   (load-local-file dataset table file {:type "CSV"}))
  ([dataset table file formatOptions]
   (g/coerce :gcp.bigquery/FormatOptions formatOptions)
   (let [file (io/file file)
         _ (assert (.exists file))
         cfg {:destinationTable {:dataset dataset :table table}
              :autodetect true
              :formatOptions formatOptions}
         jobId {:job (str "load_local_file__" (filename-without-ext file) "__" (random-uuid))}]
     (try
       (let [writer (bq/writer jobId cfg)
             stream (Channels/newOutputStream writer)
             _ (io/copy (slurp file) stream)
             _ (.close stream)]
             ; job (.getJob (bq/client) jobId)
             ; completed (.wait(For Job)
         ; (if (nil? completed)
         ;  (println "Job DNE")
         ;  completed)
         (bq/get-job jobId))))))
