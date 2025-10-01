(ns gcp.monitoring-dashboard.MosaicLayout
  (:require [gcp.global :as g]
            [gcp.monitoring-dashboard.Widget :as Widget])
  (:import (com.google.monitoring.dashboard.v1 MosaicLayout MosaicLayout$Tile)))

(def MosaicLayout$Tile:schema
  (g/schema
   [:map
    [:height :int]
    [:widget {:optional true} Widget/schema]
    [:width :int]
    [:xPos :int]
    [:yPos :int]]))

(defn MosaicLayout$Tile:from-edn
  [{:keys [height widget width xPos yPos] :as arg}]
  (g/strict! MosaicLayout$Tile:schema arg)
  (let [builder (doto (MosaicLayout$Tile/newBuilder)
                  (.setHeight height)
                  (.setWidth width)
                  (.setXPos xPos)
                  (.setYPos yPos))]
    (some->> widget Widget/from-edn (.setWidget builder))
    (.build builder)))

(defn MosaicLayout$Tile:to-edn
  [^MosaicLayout$Tile arg]
  (cond-> {:height (.getHeight arg)
           :width (.getWidth arg)
           :xPos (.getXPos arg)
           :yPos (.getYPos arg)}
          (.hasWidget arg) (assoc :widget (Widget/to-edn (.getWidget arg)))))

(def schema
  (g/schema
   [:map
    [:columns :int]
    [:tiles [:sequential MosaicLayout$Tile:schema]]]))

(defn from-edn
  [{:keys [columns tiles] :as arg}]
  (g/strict! schema arg)
  (let [builder (doto (MosaicLayout/newBuilder)
                  (.setColumns columns))]
    (some->> tiles (map MosaicLayout$Tile:from-edn) (.addAllTiles builder))
    (.build builder)))

(defn to-edn
  [^MosaicLayout arg]
  {:columns (.getColumns arg)
   :tiles (mapv MosaicLayout$Tile:to-edn (.getTilesList arg))})
