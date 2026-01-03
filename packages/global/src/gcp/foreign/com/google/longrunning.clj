(ns gcp.foreign.com.google.longrunning
  (:require [gcp.global :as global])
  (:import (com.google.longrunning Operation OperationsClient)))

(defn Operation-from-edn [arg]
  (let [builder (Operation/newBuilder)]
    (when-some [v (:name arg)] (.setName builder v))
    (when-some [v (:done arg)] (.setDone builder v))
    (.build builder)))

(defn Operation-to-edn [^Operation arg]
  {:name (.getName arg)
   :done (.getDone arg)})

(global/include-schema-registry!
 (with-meta
   {::Operation [:map
                 [:name :string]
                 [:done :boolean]]}
   {::global/name ::registry}))
