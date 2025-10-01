(ns gcp.monitoring-dashboard.DashboardsServiceClientSettings
  (:require [gcp.global :as g])
  (:import (com.google.cloud.monitoring.dashboard.v1 DashboardsServiceClient
                                                     DashboardsServiceSettings)))

;https://cloud.google.com/java/docs/reference/google-cloud-monitoring-dashboard/latest/com.google.cloud.monitoring.dashboard.v1.DashboardsServiceSettings.Builder

(defn ^DashboardsServiceSettings from-edn [arg]
  (let [builder (DashboardsServiceSettings/newBuilder)]
    (.build builder)))

(defn ^DashboardsServiceClient get-service
  ([]
   (get-service nil))
  ([arg]
   (if (instance? DashboardsServiceClient arg)
     arg
     (or (g/get-client ::client arg)
       (let [client (DashboardsServiceClient/create (from-edn arg))]
         (g/put-client! ::client arg client)
         client)))))

;- ### Static Methods
;- create()
;- create(DashboardsServiceSettings settings)
;- #### Constructors
;- DashboardsServiceClient(DashboardsServiceSettings settings)
;- DashboardsServiceClient(DashboardsServiceStub stub)
;- #### Methods
;- awaitTermination(long duration, TimeUnit unit)
;- close()
;- shutdown()
;- shutdownNow()
;- isShutdown()
;- isTerminated()
;- getSettings()