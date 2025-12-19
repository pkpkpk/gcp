(ns gcp.logging.Logging
  (:require [gcp.core.MonitoredResource :as MonitoredResource]
            [gcp.global :as g]
            [gcp.logging.LogDestinationName :as LogDestinationName])
  (:import (com.google.cloud.logging
             Logging
             Logging$ListOption
             Logging$EntryListOption
             Logging$SortingField
             Logging$SortingOrder
             Logging$TailOption
             Logging$WriteOption)))

#!-----------------------------------------------------------------------------------

(def ListOptions:schema
  [:map {:closed true
         :url "https://cloud.google.com/java/docs/reference/google-cloud-logging/latest/com.google.cloud.logging.Logging.ListOption"}
   [:pageSize {:optional true} :int]
   [:pageToken {:optional true} :string]])

(defn- ^Logging$ListOption entry->ListOption [[k v]]
  (condp = k
    :pageSize (Logging$ListOption/pageSize v)
    :pageToken (Logging$ListOption/pageToken v)))

(defn ListOptions:from-edn
  [arg]
  (if-not (empty? arg)
    (do
      (g/strict! ListOptions:schema arg)
      (into-array Logging$ListOption (map entry->ListOption arg)))
    (into-array Logging$ListOption [])))

#!-----------------------------------------------------------------------------------

(def EntryListOptions:schema
  [:map {:closed true}
   [:billingAccount {:optional true} :string]
   [:filter {:optional true} :string]
   [:folder {:optional true} :string]
   [:organization {:optional true} :string]
   [:pageSize {:optional true} :int]
   [:pageToken {:optional true} :string]
   [:sortingField {:optional true} [:enum "TIMESTAMP"]]
   [:sortingOrder {:optional true} [:enum "ASCENDING" "DESCENDING"]]])

(defn EntryListOptions:from-edn [arg]
  (if-not (empty? arg)
    (do
      (g/strict! EntryListOptions:schema arg)
      (let [opts (java.util.ArrayList.)]
        (when-let [v (:billingAccount arg)] (.add opts (Logging$EntryListOption/billingAccount v)))
        (when-let [v (:filter arg)] (.add opts (Logging$EntryListOption/filter v)))
        (when-let [v (:folder arg)] (.add opts (Logging$EntryListOption/folder v)))
        (when-let [v (:organization arg)] (.add opts (Logging$EntryListOption/organization v)))
        (when-let [v (:pageSize arg)] (.add opts (Logging$EntryListOption/pageSize v)))
        (when-let [v (:pageToken arg)] (.add opts (Logging$EntryListOption/pageToken v)))
        (when (or (:sortingField arg) (:sortingOrder arg))
          (.add opts (Logging$EntryListOption/sortOrder
                       (Logging$SortingField/valueOf (get arg :sortingField "TIMESTAMP"))
                       (Logging$SortingOrder/valueOf (get arg :sortingOrder "DESCENDING")))))
        (into-array Logging$EntryListOption opts)))
    (into-array Logging$EntryListOption [])))

#!-----------------------------------------------------------------------------------

(def TailOptions:schema
  [:map {:closed true}
   [:billingAccount {:optional true} :string]
   [:bufferWindow {:optional true} :string]
   [:filter {:optional true} :string]
   [:folder {:optional true} :string]
   [:organization {:optional true} :string]
   [:project {:optional true} :string]])

(defn- ^Logging$TailOption entry->TailOption [[k v]]
  (condp = k
    :billingAccount (Logging$TailOption/billingAccount v)
    :bufferWindow (Logging$TailOption/bufferWindow v)
    :filter (Logging$TailOption/filter v)
    :folder (Logging$TailOption/folder v)
    :organization (Logging$TailOption/organization v)
    :project (Logging$TailOption/project v)))

(defn TailOptions:from-edn [arg]
  (if-not (empty? arg)
    (do
      (g/strict! TailOptions:schema arg)
      (into-array Logging$TailOption (map entry->TailOption arg)))
    (into-array Logging$TailOption [])))

#!-----------------------------------------------------------------------------------

(def WriteOptions:schema
  [:map {:closed true}
   [:autoPopulateMetadata :boolean]
   [:destination LogDestinationName/schema]
   [:labels [:map-of :string :string]]
   [:logName :string]
   [:partialSuccess :boolean]
   [:resource MonitoredResource/schema]])

(defn- ^Logging$WriteOption entry->WriteOption [[k v]]
  (condp = k
    :logName
    (Logging$WriteOption/logName v)

    :destination
    (Logging$WriteOption/destination v)

    :resource
    (Logging$WriteOption/resource v)

    :labels
    (Logging$WriteOption/labels v)

    :autoPopulateMetadata
    (Logging$WriteOption/autoPopulateMetadata v)

    :partialSuccess
    (Logging$WriteOption/partialSuccess v)))

(defn WriteOptions:from-edn [arg]
  (if-not (empty? arg)
    (do
      (g/strict! WriteOptions:schema arg)
      (into-array Logging$WriteOption (map entry->WriteOption arg)))
    (into-array Logging$WriteOption [])))
