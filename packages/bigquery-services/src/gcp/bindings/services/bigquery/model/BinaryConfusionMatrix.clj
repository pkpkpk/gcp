;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bindings.services.bigquery.model.BinaryConfusionMatrix
  {:doc
     "Confusion matrix for binary classification models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc."
   :file-git-sha "71853cb52ee53d1c4f9de7baa4b49fe406c6735c"
   :fqcn "com.google.api.services.bigquery.model.BinaryConfusionMatrix"
   :gcp.dev/certification
     {:base-seed 1772390240691
      :manifest "2096f8e8-3cdd-50e2-9b64-67d099f5c3be"
      :passed-stages
        {:smoke 1772390240691 :standard 1772390240692 :stress 1772390240693}
      :protocol-hash
        "f22c161c7a00aa071f6a8b78764e5c684eaa491c76675456f17e0b44b2b8578c"
      :timestamp "2026-03-01T18:37:20.711367399Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.api.services.bigquery.model BinaryConfusionMatrix]))

(defn ^BinaryConfusionMatrix from-edn
  [arg]
  (global/strict! :gcp.bindings.services.bigquery.model/BinaryConfusionMatrix
                  arg)
  (let [o (new BinaryConfusionMatrix)]
    (when (some? (get arg :accuracy)) (.setAccuracy o (get arg :accuracy)))
    (when (some? (get arg :f1Score)) (.setF1Score o (get arg :f1Score)))
    (when (some? (get arg :falseNegatives))
      (.setFalseNegatives o (get arg :falseNegatives)))
    (when (some? (get arg :falsePositives))
      (.setFalsePositives o (get arg :falsePositives)))
    (when (some? (get arg :positiveClassThreshold))
      (.setPositiveClassThreshold o (get arg :positiveClassThreshold)))
    (when (some? (get arg :precision)) (.setPrecision o (get arg :precision)))
    (when (some? (get arg :recall)) (.setRecall o (get arg :recall)))
    (when (some? (get arg :trueNegatives))
      (.setTrueNegatives o (get arg :trueNegatives)))
    (when (some? (get arg :truePositives))
      (.setTruePositives o (get arg :truePositives)))
    o))

(defn to-edn
  [^BinaryConfusionMatrix arg]
  {:post [(global/strict!
            :gcp.bindings.services.bigquery.model/BinaryConfusionMatrix
            %)]}
  (cond-> {}
    (.getAccuracy arg) (assoc :accuracy (.getAccuracy arg))
    (.getF1Score arg) (assoc :f1Score (.getF1Score arg))
    (.getFalseNegatives arg) (assoc :falseNegatives (.getFalseNegatives arg))
    (.getFalsePositives arg) (assoc :falsePositives (.getFalsePositives arg))
    (.getPositiveClassThreshold arg) (assoc :positiveClassThreshold
                                       (.getPositiveClassThreshold arg))
    (.getPrecision arg) (assoc :precision (.getPrecision arg))
    (.getRecall arg) (assoc :recall (.getRecall arg))
    (.getTrueNegatives arg) (assoc :trueNegatives (.getTrueNegatives arg))
    (.getTruePositives arg) (assoc :truePositives (.getTruePositives arg))))

(def schema
  [:map
   {:closed true,
    :doc
      "Confusion matrix for binary classification models.\n\n<p> This is the Java data model class that specifies how to parse/serialize into the JSON that is\ntransmitted over HTTP when working with the BigQuery API. For a detailed explanation see:\n<a href=\"https://developers.google.com/api-client-library/java/google-http-java-client/json\">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>\n</p>\n\n@author Google, Inc.",
    :gcp/category :mutable-pojo,
    :gcp/key :gcp.bindings.services.bigquery.model/BinaryConfusionMatrix}
   [:accuracy
    {:getter-doc
       "The fraction of predictions given the correct label.\n\n@return value or {@code null} for none",
     :setter-doc
       "The fraction of predictions given the correct label.\n\n@param accuracy accuracy or {@code null} for none",
     :optional true} :double]
   [:f1Score
    {:getter-doc
       "The equally weighted average of recall and precision.\n\n@return value or {@code null} for none",
     :setter-doc
       "The equally weighted average of recall and precision.\n\n@param f1Score f1Score or {@code null} for none",
     :optional true} :double]
   [:falseNegatives
    {:getter-doc
       "Number of false samples predicted as false.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of false samples predicted as false.\n\n@param falseNegatives falseNegatives or {@code null} for none",
     :optional true} :int]
   [:falsePositives
    {:getter-doc
       "Number of false samples predicted as true.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of false samples predicted as true.\n\n@param falsePositives falsePositives or {@code null} for none",
     :optional true} :int]
   [:positiveClassThreshold
    {:getter-doc
       "Threshold value used when computing each of the following metric.\n\n@return value or {@code null} for none",
     :setter-doc
       "Threshold value used when computing each of the following metric.\n\n@param positiveClassThreshold positiveClassThreshold or {@code null} for none",
     :optional true} :double]
   [:precision
    {:getter-doc
       "The fraction of actual positive predictions that had positive actual labels.\n\n@return value or {@code null} for none",
     :setter-doc
       "The fraction of actual positive predictions that had positive actual labels.\n\n@param precision precision or {@code null} for none",
     :optional true} :double]
   [:recall
    {:getter-doc
       "The fraction of actual positive labels that were given a positive prediction.\n\n@return value or {@code null} for none",
     :setter-doc
       "The fraction of actual positive labels that were given a positive prediction.\n\n@param recall recall or {@code null} for none",
     :optional true} :double]
   [:trueNegatives
    {:getter-doc
       "Number of true samples predicted as false.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of true samples predicted as false.\n\n@param trueNegatives trueNegatives or {@code null} for none",
     :optional true} :int]
   [:truePositives
    {:getter-doc
       "Number of true samples predicted as true.\n\n@return value or {@code null} for none",
     :setter-doc
       "Number of true samples predicted as true.\n\n@param truePositives truePositives or {@code null} for none",
     :optional true} :int]])

(global/include-schema-registry!
  (with-meta {:gcp.bindings.services.bigquery.model/BinaryConfusionMatrix
                schema}
    {:gcp.global/name
       "gcp.bindings.services.bigquery.model.BinaryConfusionMatrix"}))