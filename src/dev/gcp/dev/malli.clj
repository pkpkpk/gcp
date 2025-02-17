(ns ^{:doc "convert ast nodes into malli schemas"}
  gcp.dev.malli
  (:require [clojure.string :as string]
            [gcp.dev.analyzer :as ana :refer [analyze]]
            [gcp.dev.util :refer :all]))

(defn- malli-accessor
  [package {:keys [className doc fields] :as accessor}]
  (assert (= :accessor (::ana/type accessor)))
  (into [:map {:key (package-key package className)
               :closed true
               :doc    doc
               :class  (symbol className)}]
        (map
          (fn [[k v]]
            [k
             (dissoc v :setterMethod :getterMethod :getterReturnType :setterArgumentType)
             (->malli-type package (:setterArgumentType v))]))
        fields))

(defn- malli-static-factory
  [package {:keys [className doc fields] :as static-factory}]
  (assert (= :static-factory (::ana/type static-factory)))
  (into [:map {:key (package-key package className)
               :closed true
               :doc    doc
               :class  (symbol className)}]
        (map
          (fn [[k v]]
            [k
             (dissoc v :setterMethod :getterMethod :getterReturnType :setterArgumentType)
             (->malli-type package (:getterReturnType v))]))
        fields))

(defn- malli-union
  [package {:keys [doc typeDependencies]}]
  (let []
    [:and
     [:map {:doc doc}
      [:type {:optional false} [:enum]]]
     (into [:or] (map (partial package-key package) typeDependencies))]))

(defn- malli-enum
  [package {:keys [values doc className] :as node}]
  (let [doc (clean-doc doc)
        opts (cond-> {:class (as-dot-string className)
                      :gcp/key (:gcp/key node)}
                     doc (assoc :doc doc))]
    (if (every? string/blank? (vals values))
      (into [:enum opts] (map name (keys values)))
      (into [:or opts]
            (map
              (fn [[val doc]]
                (if-let [doc (clean-doc doc)]
                  [:= {:doc doc} (name val)]
                  [:= (name val)])))
            values))))

(defn malli [package className]
  {:post [(vector? %)
          (map? (second %))
          (keyword? (get (second %) :gcp/key))
          (some? (get (second %) :class))]}
  (let [{type ::ana/type :as t} (analyze package className)]
    (assert (some? (:className t)))
    (case type
      :accessor (malli-accessor package t)
      ;:union (malli-union package t)
      :enum (malli-enum package t)
      :static-factory (malli-static-factory package t)
      (throw (Exception. (str "cannot create schema for " className))))))
