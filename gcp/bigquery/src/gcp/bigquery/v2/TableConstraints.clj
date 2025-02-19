(ns gcp.bigquery.v2.TableConstraints
  (:require [gcp.bigquery.v2.ForeignKey :as ForeignKey]
            [gcp.bigquery.v2.PrimaryKey :as PrimaryKey]
            [gcp.global :as global])
  (:import (com.google.cloud.bigquery TableConstraints)))

(defn ^TableConstraints from-edn
  [{:keys [primaryKey foreignKeys] :as arg}]
  (global/strict! :gcp/bigquery.TableConstraints arg)
  (let [builder (TableConstraints/newBuilder)]
    (when primaryKey
      (.setPrimaryKey builder (PrimaryKey/from-edn primaryKey)))
    (when (seq foreignKeys)
      (.setForeignKeys builder (map ForeignKey/from-edn foreignKeys)))
    (.build builder)))

(defn to-edn [^TableConstraints arg]
  {:post [(global/strict! :gcp/bigquery.TableConstraints %)]}
  (cond-> {}
          (pos? (count (.getForeignKeys arg)))
          (assoc :foreignKeys (mapv ForeignKey/to-edn (.getForeignKeys arg)))
          (some? (.getPrimaryKey arg))
          (assoc :primaryKey (PrimaryKey/to-edn (.getPrimaryKey arg)))))