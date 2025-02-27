(ns gcp.vertexai.v1.api.Schema
  (:require [gcp.global :as global]
            [gcp.protobuf :as protobuf]
            [gcp.vertexai.v1.api.Type :as t]
            [clojure.string :as string])
  (:import [com.google.cloud.vertexai.api Schema]
           (com.google.cloud.vertexai.generativeai SchemaMaker)
           (com.google.gson JsonObject)
           (com.google.protobuf Value)))

(defn ^Schema from-edn [arg]
  (global/strict! :gcp/vertexai.api.Schema arg)
  (let [builder (Schema/newBuilder)]
    (.setType builder (t/from-edn (:type arg)))
    (some->> arg :nullable (.setNullable builder))
    (some->> arg :title (.setTitle builder))
    (some->> arg :description (.setDescription builder))
    (some->> arg :example ^Value protobuf/value-from-edn (.setExample builder))
    (some->> arg :default ^Value protobuf/value-from-edn (.setDefault builder))
    (case (:type arg)
      "BOOLEAN" nil
      "STRING" (do
                 (some->> arg :format (.setFormat builder))
                 (some->> arg :pattern (.setPattern builder))
                 (some->> arg :enum (.addAllEnum builder))
                 (some->> arg :minLength (.setMinLength builder))
                 (some->> arg :maxLength (.setMaxLength builder)))
      "ARRAY" (do
                (some->> arg :items from-edn (.setItems builder))
                (some->> arg :minItems (.setMinItems builder))
                (some->> arg :maxItems (.setMaxItems builder)))
      ("INTEGER" "NUMBER") (do
                             (some->> arg :maximum (.setMaximum builder))
                             (some->> arg :minimum (.setMinimum builder)))
      "OBJECT" (let [properties (get arg :properties)]
                 (.putAllProperties builder (into {} (map (fn [[k v]] [(name k) (from-edn v)])) properties))
                 (some->> arg :maxProperties (.setMaxProperties builder))
                 (some->> arg :maxProperties (.setMaxProperties builder))
                 (some->> arg :required (map name) (.addAllRequired builder)))
      (throw (ex-info (str "unknown schema type '" (:type arg) "'")
                      {:type (:type arg) :schema arg})))
    (.build builder)))

(defn ^Schema from-json [arg]
  (cond
    (string? arg)
    (SchemaMaker/fromJsonString arg)
    (instance? JsonObject arg)
    (SchemaMaker/fromJsonObject arg)
    true
    (from-edn arg)))

(defn to-edn [^Schema schema]
  {:post [(global/strict! :gcp/vertexai.api.Schema %)]}
  (let [T    (.name (.getType schema))
        base (cond-> {:nullable (.getNullable schema)}
                     ;;-----------------------------------------------
                     ;; ANY
                     (and (some? (.getTitle schema))
                          (not (string/blank? (.getTitle schema))))
                     (assoc :title (.getTitle schema))
                     ;;-----------------------------------------------
                     (some? T)
                     (assoc :type (t/to-edn (.getType schema)))
                     ;;-----------------------------------------------
                     (and (some? (.getDescription schema))
                          (not (string/blank? (.getDescription schema))))
                     (assoc :description (.getDescription schema))
                     ;;-----------------------------------------------
                     (.hasExample schema)
                     (assoc :example (protobuf/value-to-edn (.getExample schema)))
                     ;;-----------------------------------------------
                     (.hasDefault schema)
                     (assoc :default (protobuf/value-to-edn (.getDefault schema))))]
    (case T
      "STRING" (cond-> base
                       (and (some? (.getFormat schema))
                            (not (string/blank? (.getFormat schema))))
                       (assoc :format (.getFormat schema))
                       (some? (.getPattern schema))
                       (assoc :pattern (.getPattern schema))
                       (pos? (.getEnumCount schema))
                       (assoc :enum (vec (.getEnumList schema)))
                       (some? (.getMinLength schema))
                       (assoc :minLength (.getMinLength schema))
                       (some? (.getMaxLength schema))
                       (assoc :maxLength (.getMaxLength schema)))
      "ARRAY" (cond-> base
                      (.hasItems schema)
                      (assoc :items (to-edn (.getItems schema)))
                      (some? (.getMinItems schema))
                      (assoc :minItems (.getMinItems schema))
                      (some? (.getMaxItems schema))
                      (assoc :maxItems (.getMaxItems schema)))
      ("INTEGER" "NUMBER") (cond-> base
                                   (some? (.getMaximum schema))
                                   (assoc :maximum (.getMaximum schema))
                                   (some? (.getMinimum schema))
                                   (assoc :minimum (.getMinimum schema)))
      "OBJECT" (cond-> base
                       (pos? (.getPropertiesCount schema))
                       (assoc :properties (into {} (map (fn [[k v]] k (to-edn v))) (.getPropertiesMap schema)))
                       (pos? (.getRequiredCount schema))
                       (assoc :required (protobuf/protocolstringlist-to-edn (.getRequiredList schema)))
                       (some? (.getMinProperties schema))
                       (assoc :minProperties (.getMinProperties schema))
                       (some? (.getMaxProperties schema))
                       (assoc :maxProperties (.getMaxProperties schema)))
      (throw (ex-info (str "unknown schema type '" T "'")
                      {:type T :schema schema :base base})))))
