(ns gcp.monitoring-dashboard.ColumnLayout
  (:require [gcp.global :as g]
            [gcp.monitoring-dashboard.Widget :as Widget])
  (:import (com.google.monitoring.dashboard.v1 ColumnLayout ColumnLayout$Column)))

(def ColumnLayout$Column:schema
  (g/schema
   [:map
    [:weight :int]
    [:widgets {:optional true} [:sequential Widget/schema]]]))

(defn ColumnLayout$Column:from-edn
  [{:keys [weight widgets] :as arg}]
  (g/strict! ColumnLayout$Column:schema arg)
  (let [builder (doto (ColumnLayout$Column/newBuilder)
                  (.setWeight weight))]
    (some->> widgets (map Widget/from-edn) (.addAllWidgets builder))
    (.build builder)))

(defn ColumnLayout$Column:to-edn
  [^ColumnLayout$Column arg]
  (cond-> {:weight (.getWeight arg)}
          (not-empty (.getWidgetsList arg)) (assoc :widgets (mapv Widget/to-edn (.getWidgetsList arg)))))

(def schema
  (g/schema
   [:map
    [:columns [:sequential ColumnLayout$Column:schema]]]))

(defn from-edn
  [{:keys [columns] :as arg}]
  (g/strict! schema arg)
  (let [builder (ColumnLayout/newBuilder)]
    (some->> columns (map ColumnLayout$Column:from-edn) (.addAllColumns builder))
    (.build builder)))

(defn to-edn
  [^ColumnLayout arg]
  {:columns (mapv ColumnLayout$Column:to-edn (.getColumnsList arg))})
