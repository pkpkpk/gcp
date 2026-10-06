(ns gcp.bigquery.tests.iam-tests
  (:require [clojure.test :refer :all]
            [gcp.bigquery :as bq]
            [gcp.bigquery.custom.BigQueryOptions :as BQO]
            [gcp.bigquery.tests.util :as util :refer [wipe-dataset!]]
            [gcp.foreign.com.google.cloud :as cloud]
            [gcp.foreign.com.google.auth.oauth2 :as oauth2]
            [gcp.global :as g])
  (:import (com.google.auth.oauth2 GoogleCredentials)))

(def dataset-id {:dataset "iam_test"})
(def table-id {:dataset "iam_test" :table "iam_test_table"})
(def table-info {:tableId table-id :definition {:type "TABLE"}})

(defn test-principal []
  (or (System/getenv "GCP_BIGQUERY_TEST_PRINCIPAL")
      (throw (ex-info "GCP_BIGQUERY_TEST_PRINCIPAL is required"
                      {}))))

(defn test-principal-client []
  (let [principal (test-principal)
        source-credentials (GoogleCredentials/getApplicationDefault)
        creds (oauth2/ImpersonatedCredentials-from-edn
                {:sourceCredentials source-credentials
                 :targetPrincipal principal
                 :scopes ["https://www.googleapis.com/auth/cloud-platform"]})]
    (BQO/get-service {:credentials creds})))

(def permissions
  ["bigquery.tables.get"
   "bigquery.tables.getData"
   "bigquery.tables.update"])

(defn policy-without-test-principal
  [policy]
  (let [member (str "serviceAccount:" (test-principal))]
    (update policy :bindings
            (fn [bindings]
              (vec (remove #(some #{member} (:members %))
                           bindings))))))

(defn policy-with-test-principal
  [policy]
  (let [member (str "serviceAccount:" (test-principal))]
    (update policy :bindings
            (fn [bindings]
              (conj (vec bindings)
                    {:role "roles/bigquery.dataEditor"
                     :members [member]})))))

(deftest iam-test
  (try
    (bq/create-dataset dataset-id)
    (and
      (testing "table create/get-iam-policy"
        (and
          (is (g/valid? :gcp.bigquery/TableInfo (bq/create-table table-info)))
          (is (g/valid? ::cloud/Policy (bq/get-iam-policy table-info)))))
      (testing "deny/test-iam-permissions"
        (let [policy (bq/get-iam-policy table-id)
              denied-policy (policy-without-test-principal policy)]
          (and
            (is (g/valid? ::cloud/Policy (bq/set-iam-policy table-id denied-policy)))
            (is (empty? (bq/test-iam-permissions (test-principal-client) table-id permissions))))))
      (testing "grant/test-iam-permissions"
        (let [policy (bq/get-iam-policy table-id)
              granted-policy (policy-with-test-principal policy)]
          (and
            (is (g/valid? ::cloud/Policy (bq/set-iam-policy table-id granted-policy)))
            (is (= permissions (bq/test-iam-permissions (test-principal-client) table-id permissions))))))
      (testing "revoke/test-iam-permissions"
        (let [policy (bq/get-iam-policy table-id)
              revoked-policy (policy-without-test-principal policy)]
          (and
            (is (g/valid? ::cloud/Policy (bq/set-iam-policy table-id revoked-policy)))
            (is (empty? (bq/test-iam-permissions (test-principal-client) table-id permissions)))))))
    (finally
      (wipe-dataset! dataset-id))))

(comment
  (do (require :reload 'gcp.bigquery.tests.iam-tests) (in-ns 'gcp.bigquery.tests.iam-tests))
  (wipe-dataset! dataset-id)
  ;(clojure.test/run-test gcp.bigquery.tests.iam-tests/iam-test)
  (def res (util/run-test-capture #'iam-test))
  (def err (->> res :events (filter #(= :error (:type %))) first :actual))
  (g/get-schema ::cloud/Policy)
  (g/get-schema ::bq/BigQuery.IAMOption)
  (g/get-schema ::bq/BigQueryOptions)
  )
