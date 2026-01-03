(ns gcp.foreign.com.google.rpc
  (:require [gcp.global :as global])
  (:import (com.google.rpc Status)))

(defn Status-from-edn [arg]
  (let [builder (Status/newBuilder)]
    (when-some [v (:code arg)] (.setCode builder v))
    (when-some [v (:message arg)] (.setMessage builder v))
    ;; details are usually Any, skipping for now
    (.build builder)))

(defn Status-to-edn [^Status arg]
  {:code (.getCode arg)
   :message (.getMessage arg)})

(global/include-schema-registry!
 (with-meta
   {::Status [:map
              [:code [:int {:min 0 :max 20}]]
              [:message :string]]}
   {::global/name ::registry}))
