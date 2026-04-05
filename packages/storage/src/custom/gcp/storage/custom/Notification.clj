(ns gcp.storage.custom.Notification
  (:require [gcp.storage.NotificationInfo :as NotificationInfo])
  (:import (com.google.cloud.storage Notification)))

(defn to-edn [^Notification arg]
  (when arg
    (assoc (NotificationInfo/to-edn arg) :storage (.getStorage arg))))
