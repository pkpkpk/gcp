(ns gcp.monitoring-dashboard
  (:require [gcp.global :as g]
            [gcp.monitoring-dashboard.Dashboard :as Dashboard]
            [gcp.monitoring-dashboard.DashboardsServiceClientSettings :as Settings])
  (:import [com.google.cloud.monitoring.dashboard.v1 DashboardsServiceClient]
           (com.google.monitoring.dashboard.v1 ProjectName)))

#_(do (require :reload 'gcp.monitoring-dashboard) (in-ns 'gcp.monitoring-dashboard))
(defonce ^:dynamic *client* nil)

(defn ^DashboardsServiceClient client
  ([]
   (client nil))
  ([arg]
   (or *client*
       (if (instance? DashboardsServiceClient arg)
         arg
         (Settings/get-service arg)))))


;- createDashboard(CreateDashboardRequest request)
;- createDashboard(ProjectName parent, Dashboard dashboard)
;- createDashboard(String parent, Dashboard dashboard)

;- deleteDashboard(DashboardName name)
;- deleteDashboard(DeleteDashboardRequest request)
;- deleteDashboard(String name)

;- getDashboard(DashboardName name)
;- getDashboard(GetDashboardRequest request)
;- getDashboard(String name)

;- listDashboards(ListDashboardsRequest request)
(defn list-dashboards
  ([project-or-request]
   (list-dashboards nil project-or-request))
  ([clientable project-or-request]
   (let [client (client clientable)
         res (if (string? project-or-request)
               (.listDashboards client (ProjectName/of project-or-request))
               (throw (Exception. "unimplemented")))]
     (when-let [pages (not-empty (.iterateAll res))]
       (reduce
         (fn [acc page]
           (into acc (map Dashboard/to-edn) (.getDashboardList page)))
         []
         pages)))))

;- updateDashboard(UpdateDashboardRequest request)
