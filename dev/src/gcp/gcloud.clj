(ns gcp.gcloud
  (:require [clojure.data.json :as json]
            [clojure.java.shell :as sh]
            [clojure.string :as string])
  (:import (java.util Base64)))

(defn dac-auth-token []
  (let [{:keys [exit out err]} (sh/sh "gcloud" "auth" "print-access-token")
        out-trimmed (some-> out string/trim)
        err-trimmed (some-> err string/trim)]
    (cond
      (or (and out-trimmed (re-find #"(?i)reauthentication|password|sign[- ]in|authenticate" out-trimmed))
          (and err-trimmed (re-find #"(?i)reauthentication|password|sign[- ]in|authenticate" err-trimmed)))
      (throw (ex-info "Google Cloud reauthentication required. Please run 'gcloud auth login' in your terminal."
                      {:out out-trimmed :err err-trimmed}))

      (not= 0 exit)
      (throw (ex-info (str "Failed to generate gcloud auth token: " err-trimmed)
                      {:exit exit :out out-trimmed :err err-trimmed}))

      (string/blank? out-trimmed)
      (throw (ex-info "Generated gcloud auth token is empty. Please ensure you are logged in."
                      {:exit exit :out out-trimmed :err err-trimmed}))

      :else
      out-trimmed)))

(defn- encode-registry-auth [username password serveraddress]
  (let [auth-data {"username" username
                   "password" password
                   "serveraddress" serveraddress}]
    (.encodeToString (Base64/getEncoder) (.getBytes (json/write-str auth-data)))))

(defn get-registry-auth-header []
  (let [username "oauth2accesstoken"
        password (dac-auth-token)
        serveraddress "https://us-docker.pkg.dev"]
    (encode-registry-auth username password serveraddress)))

(defn setup-docker-credentials [])