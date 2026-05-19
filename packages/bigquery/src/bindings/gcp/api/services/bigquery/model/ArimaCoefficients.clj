;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.api.services.bigquery.model.ArimaCoefficients
  {:doc
     "Arima coefficients.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.ArimaCoefficients"
   :gcp.dev/certification
     {:base-seed 1779204651458
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1779204651458 :standard 1779204651459 :stress 1779204651460}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-05-19T15:30:52.298144267Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model ArimaCoefficients]))

(declare from-edn to-edn)

(defn ^ArimaCoefficients from-edn
  [arg]
  (global/strict! :gcp.api.services.bigquery.model/ArimaCoefficients arg)
  (let [o (new ArimaCoefficients)]
    (when (some? (get arg :autoRegressiveCoefficients))
      (.setAutoRegressiveCoefficients o
                                      (seq (get arg
                                                :autoRegressiveCoefficients))))
    (when (some? (get arg :interceptCoefficient))
      (.setInterceptCoefficient o (double (get arg :interceptCoefficient))))
    (when (some? (get arg :movingAverageCoefficients))
      (.setMovingAverageCoefficients o
                                     (seq (get arg
                                               :movingAverageCoefficients))))
    o))

(defn to-edn
  [^ArimaCoefficients arg]
  {:post [(global/strict! :gcp.api.services.bigquery.model/ArimaCoefficients
                          %)]}
  (when arg
    (cond-> {}
      (seq (.getAutoRegressiveCoefficients arg))
        (assoc :autoRegressiveCoefficients
          (seq (.getAutoRegressiveCoefficients arg)))
      (.getInterceptCoefficient arg) (assoc :interceptCoefficient
                                       (.getInterceptCoefficient arg))
      (seq (.getMovingAverageCoefficients arg))
        (assoc :movingAverageCoefficients
          (seq (.getMovingAverageCoefficients arg))))))

(def schema
  [:map
   {:closed true,
    :doc
      "Arima coefficients.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.api.services.bigquery.model/ArimaCoefficients}
   [:autoRegressiveCoefficients
    {:getter-doc
       "Auto-regressive coefficients, an array of double.\n\n@return value or {@code null} for none",
     :setter-doc
       "Auto-regressive coefficients, an array of double.\n\n@param autoRegressiveCoefficients autoRegressiveCoefficients or {@code null} for none",
     :optional true} [:sequential {:min 1, :gen/max 2} :f64]]
   [:interceptCoefficient
    {:getter-doc
       "Intercept coefficient, just a double not an array.\n\n@return value or {@code null} for none",
     :setter-doc
       "Intercept coefficient, just a double not an array.\n\n@param interceptCoefficient interceptCoefficient or {@code null} for none",
     :optional true} :f64]
   [:movingAverageCoefficients
    {:getter-doc
       "Moving-average coefficients, an array of double.\n\n@return value or {@code null} for none",
     :setter-doc
       "Moving-average coefficients, an array of double.\n\n@param movingAverageCoefficients movingAverageCoefficients or {@code null} for none",
     :optional true} [:sequential {:min 1, :gen/max 2} :f64]]])

(global/include-schema-registry!
  (with-meta {:gcp.api.services.bigquery.model/ArimaCoefficients schema}
    {:gcp.global/name "gcp.api.services.bigquery.model.ArimaCoefficients"}))