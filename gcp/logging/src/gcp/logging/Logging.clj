(ns gcp.logging.Logging
  (:require [gcp.global :as g])
  (:import (com.google.cloud.logging
             Logging
             Logging$ListOption
             Logging$EntryListOption
             Logging$TailOption
             Logging$WriteOption)))

#!-----------------------------------------------------------------------------------

(def ListOption:schema
  (g/schema
    [:and
     {:url "https://cloud.google.com/java/docs/reference/google-cloud-logging/latest/com.google.cloud.logging.Logging.ListOption"}
     [:map
      [:pageToken {:optional true} :string]
      [:pageSize {:optional true} :int]]
     [:fn
      {:error/message "pageSize & pageToken are exclusive"}
      '(fn [m]
         (if (contains? m :pageSize)
           (not (contains? m :pageToken))
           (contains? m :pageToken)))]]))

(defn ^Logging$ListOption ListOption:from-edn [arg]
  (g/strict! ListOption:schema arg)
  (if-let [pageSize (get arg :pageSize)]
    (Logging$ListOption/pageSize pageSize)
    (Logging$ListOption/pageToken (get arg :pageToken))))

(defn ListOption:to-edn [^Logging$ListOption arg]
  (throw (Exception. "unimplemented"))
  )

#!-----------------------------------------------------------------------------------

(defn ^Logging$EntryListOption EntryListOption:from-edn [arg]
  (throw (Exception. "unimplemented"))
  )

(defn EntryListOption:to-edn [^Logging$EntryListOption arg]
  (throw (Exception. "unimplemented"))
  )

(defn ^Logging$TailOption TailOption:from-edn [arg]
  (throw (Exception. "unimplemented"))
  )

(defn TailOption:to-edn [^Logging$TailOption arg]
  (throw (Exception. "unimplemented"))
  )

(defn ^Logging$WriteOption WriteOption:from-edn [arg]
  (throw (Exception. "unimplemented"))
  )

(defn WriteOption:to-edn [^Logging$WriteOption arg]
  (throw (Exception. "unimplemented"))
  )
