(ns gcp.dev
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [gcp.vertexai.v1]
            [gcp.vertexai.generativeai :as genai]
            [integrant.core :as ig]
            [jsonista.core :as j]
            [malli.dev]))

;; singlefile dst, prompts?

; https://developers.google.com/apis-explorer/
; https://github.com/googleapis/google-api-java-client-services/tree/main/clients/google-api-services-discovery/v1
; https://googleapis.dev/java/google-api-services-discovery/latest/index.html

;#_(io/resource "gcp/vertexai/v1/generativeai/examples.clj")

;; render https://cloud.google.com/docs/samples
;; look for POST https://cloud.google.com/_d/dynamic_content response
(def samples-file (io/file (System/getProperty "user.home") "Downloads/dynamic_content.json"))

(defn get-products
  [sample-vector]
  (->
    (into (sorted-set)
          (comp
            (filter #(string/starts-with? % "product:"))
            (remove #(= "product:googlecloud" %)))
          (get-in sample-vector [20]))))

(defn clean-sample [sample]
  {:title       (nth sample 0)
   :description (string/trim (nth sample 4))
   :url         (nth sample 6)
   :category    (nth sample 7) ;=> this is not a reliable way to group!
   :products    (get-products sample)})

(defn samples []
  (let [all (first (j/read-value (slurp samples-file)))]
    all))

(defn samples-by-products []
  (group-by :products (map clean-sample (samples))))

#_ (keys (samples-by-products)) ;=> 63 count

;; 2025/01/01
#_
([#{"product:identityawareproxy"} 2]
 [#{"product:cloudbuild"} 2]
 [#{"product:cloudcomposer"} 4]
 [#{"product:webrisk"} 4]
 [#{"product:texttospeech"} 5]
 [#{"product:cloudprofiler" "product:googlecloudobservability" "product:unifiedmaintenance"} 6]
 [#{} 6]
 [#{"product:automl" "product:automltranslation" "product:cloudtranslation"} 7]
 [#{"product:documentwarehouse"} 7]
 [#{"product:cloudnetworking" "product:servicedirectory"} 8]
 [#{"product:cloudapplicationintegration" "product:eventarc"} 8]
 [#{"product:enterpriseknowledgegraph"} 9]
 [#{"product:batchforgooglecloud"} 10]
 [#{"product:dataproc"} 11]
 [#{"product:aiplatform" "product:aiplatformdatalabelingservice"} 12]
 [#{"product:cloudtasks"} 14]
 [#{"product:cloudassetinventory"} 15]
 [#{"product:recaptchaenterprise"} 15]
 [#{"product:cloudtranslation"} 16]
 [#{"product:cloudnaturallanguageapi"} 16]
 [#{"product:googlekubernetesengine"} 17]
 [#{"product:cloudcorecompute" "product:googlecloudvmwareengine"} 18]
 [#{"product:transcoderapi"} 18]
 [#{"product:apachekafkaforbigquery"} 20]
 [#{"product:cloudlogging" "product:googlecloudobservability" "product:unifiedmaintenance"} 22]
 [#{"product:cloudapplicationintegration"} 22]
 [#{"product:livestreamapi"} 23]
 [#{"product:dataplex"} 23]
 [#{"product:videointelligenceapi"} 24]
 [#{"product:certificateauthorityservice"} 25]
 [#{"product:bigquery" "product:bigquerydatatransferservice"} 26]
 [#{"product:pubsublite"} 28]
 [#{"product:cloudmonitoring" "product:googlecloudobservability" "product:unifiedmaintenance"} 29]
 [#{"product:identityandaccessmanagement"} 30]
 [#{"product:datacatalog"} 30]
 [#{"product:clouddatabases" "product:cloudsql" "product:cloudsqlforsqlserver"} 31]
 [#{"product:videostitcherapi"} 31]
 [#{"product:vertexaiagentbuilder"} 36]
 [#{"product:documentai"} 38]
 [#{"product:cloudkeymanagementservice"} 41]
 [#{"product:speechtotext"} 47]
 [#{"product:securitycommandcenter"} 52]
 [#{"product:secretmanager"} 53]
 [#{"product:cloudrun"} 53]
 [#{"product:clouddatabases" "product:cloudsql" "product:cloudsqlforpostgresql"} 53]
 [#{"product:clouddatabases" "product:cloudsql" "product:cloudsqlformysql"} 61]
 [#{"product:cloudfunctions"} 61]
 [#{"product:cloudhealthcareapi" "product:cloudlifesciences"} 65]
 [#{"product:dataflow"} 66]
 [#{"product:cloudvision"} 71]
 [#{"product:talentsolution"} 72]
 [#{"product:pubsub"} 74]
 [#{"product:clouddatalossprevention" "product:sensitivedataprotection"} 76]
 [#{"product:clouddatabases" "product:datastore"} 77]
 [#{"product:clouddatabases" "product:cloudspanner"} 92]
 [#{"product:cloudapplicationintegration" "product:workflows"} 111]
 [#{"product:cloudcorecompute" "product:computeengine"} 115]
 [#{"product:bigtable" "product:clouddatabases"} 118]
 [#{"product:clouddatabases" "product:firebase" "product:firestore"} 130]
 [#{"product:cloudstorage"} 130]
 [#{"product:bigquery"} 176]
 [#{"product:vertexai"} 306]
 [#{"product:generativeaionvertexai" "product:vertexai"} 408])

