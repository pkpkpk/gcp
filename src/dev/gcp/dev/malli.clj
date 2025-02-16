(ns gcp.dev.malli
  (:require [gcp.dev.analyzer :as ana :refer [analyze]]
            [gcp.dev.util :refer :all]))


(defn- malli-accessor
  [package {:keys [className doc fields] :as accessor}]
  (assert (= :accessor (::type accessor)))
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
  (assert (= :static-factory (::type static-factory)))
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

(defn- malli-enum [package node]
  (throw (Exception. "malli enum unimplemented")))

(defn malli [package class]
  (let [{type ::type :as t} (analyze package class)]
    (case type
      :accessor (malli-accessor package t)
      :union (malli-union package t)
      :enum (malli-enum package t)
      :static-factory (malli-static-factory package t))))
