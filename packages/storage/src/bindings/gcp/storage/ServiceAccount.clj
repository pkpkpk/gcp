;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.storage.ServiceAccount
  {:doc
     "A service account, with its specified scopes, authorized for this instance.\n\n@see <a href=\"https://cloud.google.com/storage/docs/authentication\">Authenticating from Google\n    Cloud Storage</a>"
   :file-git-sha "ca6878974eaf83f0237fa7faacd1e664db93e7a1"
   :fqcn "com.google.cloud.storage.ServiceAccount"
   :gcp.dev/certification
     {:base-seed 1775342707476
      :manifest "215ec381-0f5f-5884-ab6d-eb0bb246cd16"
      :passed-stages
        {:smoke 1775342707476 :standard 1775342707477 :stress 1775342707478}
      :protocol-hash
        "4c8153e592bbd21aa5ceea5ac76bb3400f5daf613bb57ad03e7e373f401ca3ad"
      :timestamp "2026-04-04T22:45:08.664992049Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.storage ServiceAccount]))

(declare from-edn to-edn)

(defn ^ServiceAccount from-edn
  [arg]
  (global/strict! :gcp.storage/ServiceAccount arg)
  (if (string? arg)
    (ServiceAccount/of arg)
    (ServiceAccount/of (get arg :email))))

(defn to-edn
  [^ServiceAccount arg]
  {:post [(global/strict! :gcp.storage/ServiceAccount %)]}
  (when arg {:email (.getEmail arg)}))

(def schema
  [:or
   {:closed true,
    :doc
      "A service account, with its specified scopes, authorized for this instance.\n\n@see <a href=\"https://cloud.google.com/storage/docs/authentication\">Authenticating from Google\n    Cloud Storage</a>",
    :gcp/category :static-factory,
    :gcp/key :gcp.storage/ServiceAccount} [:string {:min 1}]
   [:map {:closed true} [:email [:string {:min 1}]]]])

(global/include-schema-registry! (with-meta {:gcp.storage/ServiceAccount schema}
                                   {:gcp.global/name
                                      "gcp.storage.ServiceAccount"}))