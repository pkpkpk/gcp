;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.TableReference
  {:doc
     "Model definition for TableReference.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.TableReference"
   :gcp.dev/certification
     {:base-seed 1772390242087
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390242087 :standard 1772390242088 :stress 1772390242089}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:22.100116503Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model TableReference]))

(defn ^TableReference from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/TableReference arg)
  (let [o (new TableReference)]
    (when (some? (get arg :datasetId)) (.setDatasetId o (get arg :datasetId)))
    (when (some? (get arg :projectId)) (.setProjectId o (get arg :projectId)))
    (when (some? (get arg :tableId)) (.setTableId o (get arg :tableId)))
    o))

(defn to-edn
  [^TableReference arg]
  {:post [(global/strict! :gcp.bindings.services.bigquery.model/TableReference
                          %)]}
  (cond-> {}
    (.getDatasetId arg) (assoc :datasetId (.getDatasetId arg))
    (.getProjectId arg) (assoc :projectId (.getProjectId arg))
    (.getTableId arg) (assoc :tableId (.getTableId arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Model definition for TableReference.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/TableReference}
   [:datasetId
    {:getter-doc
       "Required. The ID of the dataset containing this table.\n\n@return value or {@code null} for none",
     :setter-doc
       "Required. The ID of the dataset containing this table.\n\n@param datasetId datasetId or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:projectId
    {:getter-doc
       "Required. The ID of the project containing this table.\n\n@return value or {@code null} for none",
     :setter-doc
       "Required. The ID of the project containing this table.\n\n@param projectId projectId or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:tableId
    {:getter-doc
       "Required. The ID of the table. The ID can contain Unicode characters in category L (letter), M\n(mark), N (number), Pc (connector, including underscore), Pd (dash), and Zs (space). For more\ninformation, see [General\nCategory](https://wikipedia.org/wiki/Unicode_character_property#General_Category). The maximum\nlength is 1,024 characters. Certain operations allow suffixing of the table ID with a partition\ndecorator, such as `sample_table$20190123`.\n\n@return value or {@code null} for none",
     :setter-doc
       "Required. The ID of the table. The ID can contain Unicode characters in category L (letter), M\n(mark), N (number), Pc (connector, including underscore), Pd (dash), and Zs (space). For more\ninformation, see [General\nCategory](https://wikipedia.org/wiki/Unicode_character_property#General_Category). The maximum\nlength is 1,024 characters. Certain operations allow suffixing of the table ID with a partition\ndecorator, such as `sample_table$20190123`.\n\n@param tableId tableId or {@code null} for none",
     :optional true} [:string {:min 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/TableReference schema}
    {:gcp.global/name "gcp.bindings.services.bigquery.model.TableReference"}))