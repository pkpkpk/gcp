(ns
  ^{:doc "convenience functions that sugar top-level api functions"}
  gcp.bigquery.aux
  (:require [gcp.bigquery :as bq]
            [gcp.global :as g]))

(defn
  ^{:urls ["https://cloud.google.com/bigquery/docs/exporting-data"
           "https://cloud.google.com/bigquery/docs/reference/standard-sql/export-statements"
           "https://cloud.google.com/java/docs/reference/google-cloud-bigquery/latest/com.google.cloud.bigquery.ExtractJobConfiguration"]}
  extract-table
  ([table format compression dst & opts]
   (let [table (if (g/valid? :gcp/bigquery.TableId table)
                 table
                 (if (g/valid? :gcp/bigquery.TableInfo table)
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
                                :sourceTable     (g/coerce :gcp/bigquery.TableId table)
                                :format          format
                                :destinationUris dst}
                               compression (assoc :compression compression))]
     (bq/create-job {:bigquery (:bigquery table) ;; if Table, use same client
                     :jobInfo  {:configuration (g/coerce :gcp/bigquery.ExtractJobConfiguration configuration)}
                     :options  (not-empty opts)}))))

(defn clone-table
  ([source destination]
   (let [source-tables (if (g/valid? [:sequential :gcp/bigquery.TableId] source)
                         source
                         (if (g/valid? :gcp/bigquery.TableId source)
                           [source]
                           (if (g/valid? :gcp/bigquery.TableInfo source)
                             [(:tableId source)]
                             (if (g/valid? [:sequential :gcp/bigquery.TableInfo] source)
                               (mapv :tableId source)
                               (throw (ex-info "cannot create clone source"
                                               {:source      source
                                                :destination destination}))))))
         destination-table (if (g/valid? :gcp/bigquery.TableId destination)
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
                        :sourceTables     (g/coerce [:sequential :gcp/bigquery.TableId] source-tables)
                        :destinationTable (g/coerce :gcp/bigquery.TableId destination-table)
                        :operationType    "CLONE",
                        :writeDisposition "WRITE_EMPTY"}]
     (bq/create-job {:jobInfo {:configuration (g/coerce :gcp/bigquery.CopyJobConfiguration configuration)}})))
  ([sourceDataset sourceTable destinationDataset]
   (let [source (g/coerce :gcp/bigquery.TableId {:dataset sourceDataset :table sourceTable})]
     (clone-table source destinationDataset)))
  ([sourceDataset sourceTable destinationDataset destinationTable]
   (let [source (g/coerce :gcp/bigquery.TableId {:dataset sourceDataset :table sourceTable})
         destination (g/coerce :gcp/bigquery.TableId {:dataset destinationDataset :table destinationTable})]
     (clone-table source destination))))
