;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.GlobalExplanation
  {:doc
     "Global explanations containing the top most important features after training.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.GlobalExplanation"
   :gcp.dev/certification
     {:base-seed 1772390264144
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390264144 :standard 1772390264145 :stress 1772390264146}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:44.199139997Z"}}
  (:require [gcp.bindings.services.bigquery.model.Explanation :as Explanation]
            [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model GlobalExplanation]))

(defn ^GlobalExplanation from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/GlobalExplanation arg)
  (let [o (new GlobalExplanation)]
    (when (some? (get arg :classLabel))
      (.setClassLabel o (get arg :classLabel)))
    (when (some? (get arg :explanations))
      (.setExplanations o (map Explanation/from-edn (get arg :explanations))))
    o))

(defn to-edn
  [^GlobalExplanation arg]
  {:post [(global/strict!
            :gcp.bindings.services.bigquery.model/GlobalExplanation
            %)]}
  (cond-> {}
    (.getClassLabel arg) (assoc :classLabel (.getClassLabel arg))
    (.getExplanations arg) (assoc :explanations
                             (map Explanation/to-edn (.getExplanations arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Global explanations containing the top most important features after training.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/GlobalExplanation}
   [:classLabel
    {:getter-doc
       "Class label for this set of global explanations. Will be empty/null for binary logistic and\nlinear regression models. Sorted alphabetically in descending order.\n\n@return value or {@code null} for none",
     :setter-doc
       "Class label for this set of global explanations. Will be empty/null for binary logistic and\nlinear regression models. Sorted alphabetically in descending order.\n\n@param classLabel classLabel or {@code null} for none",
     :optional true} [:string {:min 1}]]
   [:explanations
    {:getter-doc
       "A list of the top global explanations. Sorted by absolute value of attribution in descending\norder.\n\n@return value or {@code null} for none",
     :setter-doc
       "A list of the top global explanations. Sorted by absolute value of attribution in descending\norder.\n\n@param explanations explanations or {@code null} for none",
     :optional true}
    [:sequential {:min 1} :gcp.bindings.services.bigquery.model/Explanation]]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/GlobalExplanation schema}
    {:gcp.global/name
       "gcp.bindings.services.bigquery.model.GlobalExplanation"}))