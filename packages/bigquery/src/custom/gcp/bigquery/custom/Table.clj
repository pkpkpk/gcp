(ns gcp.bigquery.custom.Table
  (:require
   [gcp.bigquery.TableId :as TableId]
   [gcp.bigquery.TableInfo :as TableInfo])
  (:import
   (com.google.cloud.bigquery Table)))

(defn to-edn [^Table arg]
  (when arg
    (assoc (TableInfo/to-edn arg) :bigquery (.getBigQuery arg))))

(defn keywordize-map [m]
  (into {}
        (map
          (fn [[k v]]
            [(keyword k) v]))
        m))

(defn Lite-to-edn [^Table arg]
  (cond-> {:bigquery (.getBigQuery arg)
           :definition {:type (.name (.getType (.getDefinition arg)))}
           :generatedId (.getGeneratedId arg)
           :creationTime (.getCreationTime arg)
           :tableId (TableId/to-edn (.getTableId arg))}
          (seq (.getLabels arg))
          (assoc :labels (keywordize-map (.getLabels arg)))
          (seq (.getResourceTags arg))
          (assoc :resourceTags (keywordize-map (.getResourceTags arg)))))
