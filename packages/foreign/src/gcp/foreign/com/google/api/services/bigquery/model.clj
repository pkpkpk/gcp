(ns gcp.foreign.com.google.api.services.bigquery.model
  {:gcp.dev/certification
   {:UserDefinedFunctionResource
    {:protocol-hash "1ec16a37154e80b37dbcfd68e59d7713ceface2ff37cdc88c258cded7134034c"
     :base-seed 1767570500889
     :timestamp "2026-01-04T23:48:20.915556987Z"
     :passed-stages {:smoke 1767570500889
                     :standard 1767570500890
                     :stress 1767570500891}
     :source-hash "9278f8f96710102aaa9c3160d4c77bb6091bf3f6c6848a44dad86e2b567e96c0"}}}
  (:require [gcp.global :as global])
  (:import (com.google.api.services.bigquery.model UserDefinedFunctionResource QueryParameter)))

;https://developers.google.com/resources/api-libraries/documentation/bigquery/v2/java/latest/com/google/api/services/bigquery/model/class-use/QueryParameter.html

(def UserDefinedFunctionResource-schema
  [:map
   [:inline-code {:optional true} :string]
   [:resource-uri {:optional true} :string]])

(defn UserDefinedFunctionResource-to-edn [^UserDefinedFunctionResource obj]
  (cond-> {}
    (.getInlineCode obj) (assoc :inline-code (.getInlineCode obj))
    (.getResourceUri obj) (assoc :resource-uri (.getResourceUri obj))))

(defn ^UserDefinedFunctionResource UserDefinedFunctionResource-from-edn [arg]
  (let [obj (UserDefinedFunctionResource.)]
    (when-some [v (:inline-code arg)] (.setInlineCode obj v))
    (when-some [v (:resource-uri arg)] (.setResourceUri obj v))
    obj))

(defn QueryParameter-from-edn [arg] arg)
(defn QueryParameter-to-edn [arg] arg)

(global/include-schema-registry!
  (with-meta
    {:gcp.foreign.com.google.api.services.bigquery.model/UserDefinedFunctionResource UserDefinedFunctionResource-schema
     :gcp.foreign.com.google.api.services.bigquery.model/QueryParameter [:map]}
    {:gcp.global/name :gcp.foreign.com.google.api.services.bigquery.model/registry}))