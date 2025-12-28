(ns gcp.dev.malli
  "Convert analyzer AST nodes into malli schemas"
  (:require
   [clojure.string :as string]
   [gcp.dev.analyzer :as ana]
   [gcp.dev.util :as u]))

(defn- coerce [pred x] (when (pred x) x))

(defn package-key [package-name class-name]
  (let [short-pkg (string/replace (str package-name) #"^com\.google\.cloud\." "gcp.")]
    (keyword short-pkg class-name)))

(defn ->malli-type [package-name t version]
  (cond
    (nil? t) :nil
    (= t "void") :nil
    (= t "java.lang.String") :string
    (#{"boolean" "java.lang.Boolean"} t) :boolean
    (#{"int" "java.lang.Integer" "long" "java.lang.Long"} t) :int
    (= t "com.google.protobuf.ByteString") :gcp.protobuf/ByteString
    (= t "com.google.protobuf.Duration") :gcp.protobuf/Duration
    (= t "com.google.protobuf.Timestamp") :gcp.protobuf/Timestamp
    (= t "com.google.protobuf.Struct") :gcp.protobuf/Struct
    (= t "com.google.protobuf.Value") :gcp.protobuf/Value
    (or (= t "java.util.Map") (string/starts-with? t "java.util.Map<"))
    [:map-of :string :any]
    (or (= t "java.util.List") (string/starts-with? t "java.util.List<"))
    [:sequential :any]

    (and (string/starts-with? (str t) "com.google.cloud")
         (> (count (string/split (str t) #"\.")) 4))
    (let [clean-t (string/replace (str t) #"^(class|interface) " "")
          {:keys [package class]} (u/split-fqcn clean-t)]
      (u/schema-key package class version))
    :else :any))

(defn- malli-accessor
  [{:keys [type doc fields className package] :as node} version]
  (let [head [:map {:gcp/key  (u/schema-key package className version)
                    :gcp/type type
                    :closed   true
                    :doc      (u/clean-doc doc)
                    :class    className}]]
    (into head
          (map
            (fn [[k v]]
              (let [{:keys [setterDoc getterDoc type]} v
                    opts      (dissoc v :setterMethod :setterDoc :getterMethod :getterDoc :type)
                    getterDoc (u/clean-doc getterDoc)
                    setterDoc (u/clean-doc setterDoc)]
                [(keyword k)
                 (cond-> (assoc opts :optional true)
                         getterDoc (assoc :getterDoc getterDoc)
                         setterDoc (assoc :setterDoc setterDoc))
                 (->malli-type package (str type) version)]))
            fields))))

(defn- malli-static-factory
  [{:keys [type doc className package] :as node} version]
  [:map {:gcp/key  (u/schema-key package className version)
         :gcp/type type
         :closed   true
         :doc      (u/clean-doc doc)
         :class    className}])

(defn- malli-enum
  [{:keys [type doc values className package] :as node} version]
  (let [doc (u/clean-doc doc)
        opts (cond-> {:class className
                      :gcp/type type
                      :gcp/key (u/schema-key package className version)}
                     doc (assoc :doc doc))
        enum-values (map (fn [v] (if (map? v) (:name v) v)) values)]
    (into [:enum opts] enum-values)))

(defn- malli-string-enum
  [{:keys [type doc values className package] :as node} version]
  (let [doc (u/clean-doc doc)
        opts (cond-> {:class className
                      :gcp/type type
                      :gcp/key (u/schema-key package className version)}
                     doc (assoc :doc doc))]
    (into [:enum opts] values)))

(defn- malli-concrete-union
  [node version]
  (malli-accessor node version))

(defn- malli-abstract-union
  [{:keys [type doc className package] :as node} version]
  [:map {:gcp/key (u/schema-key package className version)
         :gcp/type type
         :doc (u/clean-doc doc)
         :class className}])

(defn ->schema
  "Converts an analyzed node into a malli schema."
  ([node] (->schema node (u/extract-version (:doc node))))
  ([node version]
   (let [category (:type node)]
     (case category
       :accessor (malli-accessor node version)
       :enum (malli-enum node version)
       :string-enum (malli-string-enum node version)
       :static-factory (malli-static-factory node version)
       :abstract-union (malli-abstract-union node version)
       :concrete-union (malli-concrete-union node version)
       [:map {:gcp/key (u/schema-key (:package node) (:className node) version)
              :gcp/type category
              :doc (u/clean-doc (:doc node))
              :class (:className node)}]))))
