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
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :reason :client
      :skipped true
      :timestamp "2026-04-18T08:03:03.373335072Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.bigquery Connection]))

(global/include-schema-registry!
  (with-meta {} {:gcp.global/name "gcp.bigquery.Connection"}))