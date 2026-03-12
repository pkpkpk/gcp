(ns gcp.foreign.java.time
  (:require [gcp.global :as global]))

(def registry
  {:gcp.foreign.java.time/LocalDate
   (global/instance-schema java.time.LocalDate)
   :gcp.foreign.java.time/LocalDateTime
   (global/instance-schema java.time.LocalDateTime)})

(global/include-schema-registry! (with-meta registry {:gcp.global/name (str *ns*)}))
