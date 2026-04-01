(ns
  ^{:doc "convenience functions that sugar top-level api functions"}
  gcp.bigquery.aux
  (:require [clojure.java.io :as io]
            [gcp.bigquery :as bq]
            [gcp.global :as g])
  (:import (com.google.cloud.bigquery Job)
           (java.nio.channels Channels)))

(defn get-schema [dataset table]
  (when-let [T (bq/get-table dataset table)]
    (get-in T [:definition :schema])))

(defn
  ^{:urls ["https://cloud.google.com/bigquery/docs/exporting-data"
           "https://cloud.google.com/bigquery/docs/reference/standard-sql/export-statements"
           "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.ExtractJobConfiguration"]}
  extract-table
  ([table format compression dst & opts]
   (let [table (if (g/valid? :gcp.bigquery.v2/TableId table)
                 table
                 (if (g/valid? :gcp.bigquery.v2/TableInfo table)
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
                                :sourceTable     (g/coerce :gcp.bigquery.v2/TableId table)
                                :format          format
                                :destinationUris dst}
                               compression (assoc :compression compression))]
     (bq/create-job {:bigquery (:bigquery table) ;; if Table, use same client
                     :jobInfo  {:configuration (g/coerce :gcp.bigquery.v2/ExtractJobConfiguration configuration)}
                     :options  (not-empty opts)}))))

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
   (let [source-tables (if (g/valid? [:sequential :gcp.bigquery.v2/TableId] source)
                         source
                         (if (g/valid? :gcp.bigquery.v2/TableId source)
                           [source]
                           (if (g/valid? :gcp.bigquery.v2/TableInfo source)
                             [(:tableId source)]
                             (if (g/valid? [:sequential :gcp.bigquery.v2/TableInfo] source)
                               (mapv :tableId source)
                               (throw (ex-info "cannot create clone source"
                                               {:source      source
                                                :destination destination}))))))
         destination-table (if (g/valid? :gcp.bigquery.v2/TableId destination)
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
                        :sourceTables     (g/coerce [:sequential :gcp.bigquery.v2/TableId] source-tables)
                        :destinationTable (g/coerce :gcp.bigquery.v2/TableId destination-table)
                        :operationType    "CLONE",
                        :writeDisposition "WRITE_EMPTY"}]
     (bq/create-job {:jobInfo {:configuration (g/coerce :gcp.bigquery.v2/CopyJobConfiguration configuration)}})))
  ([sourceDataset sourceTable destinationDataset]
   (let [source (g/coerce :gcp.bigquery.v2/TableId {:dataset sourceDataset :table sourceTable})]
     (clone-table source destinationDataset)))
  ([sourceDataset sourceTable destinationDataset destinationTable]
   (let [source (g/coerce :gcp.bigquery.v2/TableId {:dataset sourceDataset :table sourceTable})
         destination (g/coerce :gcp.bigquery.v2/TableId {:dataset destinationDataset :table destinationTable})]
     (clone-table source destination))))

(defn- filename-without-ext [f]
  (let [name    (.getName f)
        idx     (.lastIndexOf name ".")]
    (if (pos? idx)
      (subs name 0 idx)
      name)))

(defn load-local-file
  ([dataset table file]
   (load-local-file dataset table file {:type "CSV"}))
  ([dataset table file formatOptions]
   (g/coerce :gcp.bigquery.v2/FormatOptions formatOptions)
   (let [file (io/file file)
         _ (assert (.exists file))
         cfg {:destinationTable {:dataset dataset :table table}
              :autodetect true
              :formatOptions formatOptions}
         jobId {:job (str "load_local_file__" (filename-without-ext file) "__" (random-uuid))}]
     (try
       (let [writer (bq/writer jobId cfg)
             stream (Channels/newOutputStream writer)
             _(io/copy (slurp file) stream)
             _(.close stream)
             ;job (.getJob (bq/client) jobId)
             ;completed (.wait(For Job)
             ]
         ;(if (nil? completed)
         ;  (println "Job DNE")
         ;  completed)
         (bq/get-job jobId))))))
