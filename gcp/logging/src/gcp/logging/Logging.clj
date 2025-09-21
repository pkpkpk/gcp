(ns gcp.logging.Logging
  (:require [gcp.global :as g])
  (:import (com.google.cloud.logging
             Logging
             Logging$ListOption
             Logging$EntryListOption
             Logging$SortingField
             Logging$SortingOrder
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

(def EntryListOption:schema
  [:or
   [:map {:closed true} [:billingAccount :string]]
   [:map {:closed true} [:filter :string]]
   [:map {:closed true} [:folder :string]]
   [:map {:closed true} [:organization :string]]
   [:map {:closed true} [:pageSize :int]]
   [:map {:closed true} [:pageToken :string]]
   [:map {:closed true}
    [:sortingField [:enum "TIMESTAMP"]]
    [:sortingOrder [:enum "ASCENDING" "DESCENDING"]]]])

(defn ^Logging$EntryListOption EntryListOption:from-edn [arg]
  (g/strict! EntryListOption:schema arg)
  (cond
    (contains? arg :billingAccount)
    (Logging$EntryListOption/billingAccount (:billingAccount arg))

    (contains? arg :filter)
    (Logging$EntryListOption/filter (:filter arg))

    (contains? arg :folder)
    (Logging$EntryListOption/folder (:folder arg))

    (contains? arg :organization)
    (Logging$EntryListOption/organization (:organization arg))

    (contains? arg :pageSize)
    (Logging$EntryListOption/pageSize (:pageSize arg))

    (contains? arg :pageToken)
    (Logging$EntryListOption/pageToken (:pageToken arg))

    true
    (Logging$EntryListOption/sortOrder
      (Logging$SortingField/valueOf (:sortingField arg))
      (Logging$SortingOrder/valueOf (:sortingOrder arg)))))

(defn EntryListOption:to-edn [^Logging$EntryListOption arg]
  (throw (Exception. "unimplemented")))

#!-----------------------------------------------------------------------------------

(def TailOption:schema
  [:or
   [:map {:closed true} [:billingAccount :string]]
   [:map {:closed true} [:bufferWindow :string]]
   [:map {:closed true} [:filter :string]]
   [:map {:closed true} [:folder :string]]
   [:map {:closed true} [:organization :string]]
   [:map {:closed true} [:project :string]]])

(defn ^Logging$TailOption TailOption:from-edn [arg]
  (g/strict! TailOption:schema arg)
  (cond
    (contains? arg :billingAccount)
    (Logging$TailOption/billingAccount (:billingAccount arg))

    (contains? arg :bufferWindow)
    (Logging$TailOption/bufferWindow (:bufferWindow arg))

    (contains? arg :filter)
    (Logging$TailOption/filter (:filter arg))

    (contains? arg :folder)
    (Logging$TailOption/folder (:folder arg))

    (contains? arg :organization)
    (Logging$TailOption/organization (:organization arg))

    (contains? arg :project)
    (Logging$TailOption/project (:project arg))))

(defn TailOption:to-edn [^Logging$TailOption arg]
  (throw (Exception. "unimplemented")))

#!-----------------------------------------------------------------------------------

(defn ^Logging$WriteOption WriteOption:from-edn [arg]
  (throw (Exception. "unimplemented"))
  )

(defn WriteOption:to-edn [^Logging$WriteOption arg]
  (throw (Exception. "unimplemented"))
  )
