(ns ^{:doc "convert ast nodes into malli schemas"}
  gcp.dev.malli
  (:require [clojure.string :as string]
            [gcp.dev.analyzer :as ana :refer [analyze]]
            [gcp.dev.util :refer :all]
            [gcp.global :as g]))

(defn- malli-accessor
  [package {:keys [className doc fields] :as node}]
  (assert (= :accessor (::ana/type node)))
  (let [?variant-tag (when-let [union (get-in package [:types/variants className])]
                       (g/coerce string? (get-in (analyze package union) [:class->tag className])))
        head [:map {:gcp/key  (:gcp/key node)
                    :gcp/type (g/coerce keyword? (::ana/type node))
                    :closed   true
                    :doc      doc
                    :class    className}]
        head (cond-> head
                     ?variant-tag (conj [:type {:optional true} [:= ?variant-tag]]))]
    (into head
          (comp
            (remove
              (fn [[k _]]
                (and ?variant-tag (= k :type))))
            (map
              (fn [[k v]]
                (let [{:keys [setterDoc getterDoc]} v
                      opts      (dissoc v :setterMethod :setterDoc :getterMethod :getterDoc :getterReturnType :setterArgumentType)
                      getterDoc (clean-doc getterDoc)
                      setterDoc (clean-doc setterDoc)]
                  [k
                   (cond-> opts
                           getterDoc (assoc :getterDoc getterDoc)
                           setterDoc (assoc :setterDoc setterDoc))
                   (->malli-type package (:setterArgumentType v))]))))
          fields)))

(defn- malli-static-factory
  [package {:keys [className doc fields] :as node}]
  (assert (= :static-factory (::ana/type node)))
  (into [:map {:gcp/key  (g/coerce keyword? (:gcp/key node))
               :gcp/type (g/coerce keyword? (::ana/type node))
               :closed   true
               :doc      doc
               :class    className}]
        (map
          (fn [[k v]]
            [k
             (dissoc v :setterMethod :getterMethod :getterReturnType :setterArgumentType)
             (->malli-type package (:getterReturnType v))]))
        fields))

(defn- malli-enum
  [_ {:keys [values doc className] :as node}]
  (assert (= :enum-factory (::ana/type node)))
  (let [doc (clean-doc doc)
        opts (cond-> {:class (as-dot-string className)
                      :gcp/type (g/coerce keyword? (::ana/type node))
                      :gcp/key (g/coerce keyword? (:gcp/key node))}
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

(defn- malli-concrete-union
  [package {:keys [doc class->tag classlessTags className] :as node}]
  (assert (= :concrete-union (::ana/type node)))
  (let [doc (clean-doc doc)
        opts (cond-> {:class    (as-dot-string className)
                      :gcp/type (g/coerce keyword? (::ana/type node))
                      :gcp/key  (g/coerce keyword? (:gcp/key node))}
                     doc (assoc :doc doc))
        with-classes (map (partial package-key package) (sort (keys class->tag)))
        without-classes  (map #(vector :map {:closed true} [:type [:= %]]) classlessTags)]
    (-> [:or opts]
        (into with-classes)
        (into without-classes))))

(defn- malli-abstract-union
  [package {:keys [doc class->tag className] :as node}]
  (assert (= :abstract-union (::ana/type node)))
  (let [doc (clean-doc doc)
        opts (cond-> {:class    (as-dot-string className)
                      :gcp/type (g/coerce keyword? (::ana/type node))
                      :gcp/key  (g/coerce keyword? (:gcp/key node))}
                     doc (assoc :doc doc))
        with-classes (map (partial package-key package) (sort (keys class->tag)))]
    (-> [:or opts]
        (into with-classes))))

(defn malli [package className]
  {:post [(vector? %)
          (map? (second %))
          (keyword? (get (second %) :gcp/key))
          (some? (get (second %) :class))]}
  (let [{type ::ana/type :as t} (analyze package className)]
    (assert (some? (:className t)))
    (case type
      :accessor (malli-accessor package t)
      :abstract-union (malli-abstract-union package t)
      :concrete-union (malli-concrete-union package t)
      :enum (malli-enum package t)
      :static-factory (malli-static-factory package t)
      (throw (Exception. (str "cannot create schema for " className))))))
