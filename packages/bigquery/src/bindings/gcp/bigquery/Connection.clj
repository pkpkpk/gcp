;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.bigquery.Connection
  {:doc
     "A Connection is a session between a Java application and BigQuery. SQL statements are executed\nand results are returned within the context of a connection."
   :file-git-sha "abbdde0e7797712d98183ea2d5390671f92d5407"
   :fqcn "com.google.cloud.bigquery.Connection"
   :gcp.dev/certification
     {:base-seed 0
      :manifest "1ac0bbeb-97b3-5784-a294-62e436a43ec4"
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :reason :client
      :skipped true
      :timestamp "2026-05-19T15:31:04.293137811Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery Connection]))

(global/include-schema-registry!
  (with-meta {} {:gcp.global/name "gcp.bigquery.Connection"}))