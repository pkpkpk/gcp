(ns gcp.vertexai.v1.api.Schema
  (:require [gcp.global :as g]
            [gcp.protobuf :as protobuf]
            [gcp.vertexai.v1.api.Type :as Type]
            [clojure.string :as string])
  (:import [com.google.cloud.vertexai.api Schema]
           (com.google.cloud.vertexai.generativeai SchemaMaker)
           (com.google.gson JsonObject)
           (com.google.protobuf Value)))

(defn ^Schema from-edn [arg]
  (g/strict! :gcp.vertexai.v1.api/Schema arg)
  (let [builder (Schema/newBuilder)]
    (.setType builder (Type/from-edn (:type arg)))
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
                 (some->> arg :minProperties (.setMinProperties builder))
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
  {:post [(g/strict! :gcp.vertexai.v1.api/Schema %)]}
  (let [T    (.name (.getType schema))
        base (cond-> {:nullable (.getNullable schema)}
                     ;;-----------------------------------------------
                     ;; ANY
                     (and (some? (.getTitle schema))
                          (not (string/blank? (.getTitle schema))))
                     (assoc :title (.getTitle schema))
                     ;;-----------------------------------------------
                     (some? T)
                     (assoc :type (Type/to-edn (.getType schema)))
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
                       (and (some? (.getPattern schema))
                            (not (string/blank? (.getPattern schema))))
                       (assoc :pattern (.getPattern schema))
                       (pos? (.getEnumCount schema))
                       (assoc :enum (vec (.getEnumList schema)))
                       (some? (.getMinLength schema))
                       (assoc :minLength (.getMinLength schema))
                       (and (some? (.getMaxLength schema))
                            (pos? (.getMaxLength schema)))
                       (assoc :maxLength (.getMaxLength schema)))
      "ARRAY" (cond-> base
                      (.hasItems schema)
                      (assoc :items (to-edn (.getItems schema)))
                      (some? (.getMinItems schema))
                      (assoc :minItems (.getMinItems schema))
                      (and (some? (.getMaxItems schema))
                           (pos? (.getMaxItems schema)))
                      (assoc :maxItems (.getMaxItems schema)))
      ("INTEGER" "NUMBER") (cond-> base
                                   (some? (.getMaximum schema))
                                   (assoc :maximum (.getMaximum schema))
                                   (some? (.getMinimum schema))
                                   (assoc :minimum (.getMinimum schema)))
      "OBJECT" (cond-> base
                       (pos? (.getPropertiesCount schema))
                       (assoc :properties (into {} (map (fn [[k v]] [k (to-edn v)])) (.getPropertiesMap schema)))
                       (pos? (.getRequiredCount schema))
                       (assoc :required (protobuf/protocolstringlist-to-edn (.getRequiredList schema)))
                       (some? (.getMinProperties schema))
                       (assoc :minProperties (.getMinProperties schema))
                       (some? (.getMaxProperties schema))
                       (assoc :maxProperties (.getMaxProperties schema)))
      (throw (ex-info (str "unknown schema type '" T "'")
                      {:type T :schema schema :base base})))))

(def schema
  [:map
   {:closed           true
    :doc              "Represents a select subset of an OpenAPI 3.0 schema object"
    :ns               'gcp.vertexai.v1.api.Schema
    :from-edn         'gcp.vertexai.v1.api.Schema/from-edn
    :to-edn           'gcp.vertexai.v1.api.Schema/to-edn
    :generativeai/url "https://ai.google.dev/api/caching#Schema"
    :protobuf/type    "google.cloud.vertexai.v1.Schema"
    :class            'com.google.cloud.vertexai.api.Schema
    :class/url        "https://cloud.google.com/vertex-ai/generative-ai/docs/reference/java/latest/com.google.cloud.vertexai.api.Schema"}

   [:type {:optional false} :gcp.vertexai.v1.api/Type]

   [:description
    {:optional true
     :doc      "A brief description of the parameter. This could contain examples of use. Parameter description may be formatted as Markdown."} :string]

   [:example {:optional true
              :doc      "example of an object"}
    :gcp.protobuf/Value]

   [:nullable {:optional true} :boolean]

   [:title {:optional true} :string]
   ;; ENUM -----------------------
   [:enum {:optional true
           :doc      "TYPE=STRING"} [:seqable :string]]
   ;; STRING -----------------------
   [:format
    {:optional true
     :doc      "The format of the data. This is used only for primitive datatypes. Supported formats: for NUMBER type: float, double for INTEGER type: int32, int64 for STRING type: enum"}
    :string]
   [:pattern {:optional true} :string]
   [:minLength {:optional true} :int]
   [:maxLength {:optional true} :int]
   ;; NUMBER -----------------------
   [:minimum {:optional true} :double]
   [:maximum {:optional true} :double]
   ;; ARRAY -----------------------
   [:items {:optional true} [:ref :gcp.vertexai.v1.api/Schema]]
   [:minItems {:optional true} :int]
   [:maxItems {:optional true} :int]
   ;; OBJECT -----------------------
   [:properties
    [:map-of {:optional true
              :doc      "string->schema. An object containing a list of \"key\": value pairs. Example: { \"name\": \"wrench\", \"mass\": \"1.3kg\", \"count\": \"3\" }. "}
             [:or :string :keyword]
     [:ref :gcp.vertexai.v1.api/Schema]]]
   [:minProperties {:optional true} :int]
   [:maxProperties {:optional true} :int]
   [:required {:optional true} [:sequential [:or :string :keyword]]]])

(g/include-schema-registry!
  ^{::g/name ::registry}
  {:gcp.vertexai.v1.api/Schema schema})
