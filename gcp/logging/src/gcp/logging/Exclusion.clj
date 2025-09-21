(ns gcp.logging.Exclusion
  (:require [gcp.global :as g])
  (:import (com.google.cloud.logging Exclusion)))

(def schema
  [:map
   [:createTime {:optional true} :any]
   [:description {:optional true} :string]
   [:disabled {:optional true} :boolean]
   [:updateTime {:optional true} :any]
   [:name :string]
   [:filter :string]])

(defn ^Exclusion from-edn
  [{:keys [name
           filter
           description
           disabled
           createTime
           updateTime] :as arg}]
  (g/strict! schema arg)
  (let [builder (Exclusion/newBuilder name filter)]
    (some->> description (.setDescription builder))
    (some->> disabled (.setDisabled builder))
    (some->> updateTime (.setUpdateTime builder))
    (some->> createTime (.setCreateTime builder))
    (.build builder)))

(defn to-edn [^Exclusion arg]
  (cond-> {}
          (.getName arg)
          (assoc :name (.getName arg))

          (.getFilter arg)
          (assoc :filter (.getFilter arg))

          (some? (.getDescription arg))
          (assoc :description (.getDescription arg))

          (some? (.getCreateTime arg))
          (assoc :createTime (.getCreateTime arg))

          (some? (.getUpdateTime arg))
          (assoc :updateTime (.getUpdateTime arg))))
