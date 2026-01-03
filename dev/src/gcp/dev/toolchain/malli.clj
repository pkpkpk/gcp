(ns gcp.dev.toolchain.malli
  "Convert analyzer AST nodes into malli schemas"
  (:require
   [clojure.string :as string]
   [gcp.dev.toolchain.analyzer :as ana]
   [gcp.dev.util :as u]))

(defn- coerce [pred x] (when (pred x) x))

(defn package-key [package-name class-name]
  (let [short-pkg (string/replace (str package-name) #"^com\.google\.cloud\." "gcp.")]
    (keyword short-pkg class-name)))

(defn ->malli-type [package-name t version]

  (let [t-str (str t)]

    (cond

      (nil? t) :nil

      (= t-str "void") :nil

      (= t-str "java.lang.String") :string

      (#{"boolean" "java.lang.Boolean"} t-str) :boolean

      (#{"int" "java.lang.Integer"} t-str) [:int {:min -2147483648 :max 2147483647}]

      (#{"long" "java.lang.Long"} t-str) :int

      (#{"float" "java.lang.Float"} t-str) [:double {:min -3.4028235E38 :max 3.4028235E38}]

      (#{"double" "java.lang.Double"} t-str) :double



      (= t-str "com.google.protobuf.ProtocolStringList") [:sequential :string]



      (sequential? t)

      (let [[base & args] t

            base-str (str base)]

        (cond

          (or (= base-str "java.util.List")

              (= base-str "com.google.common.collect.ImmutableList")

              (= base-str "java.lang.Iterable"))

          [:sequential (->malli-type package-name (first args) version)]



          (or (= base-str "java.util.Map")

              (= base-str "com.google.common.collect.ImmutableMap"))

          [:map-of (->malli-type package-name (first args) version) (->malli-type package-name (second args) version)]



          :else

          (let [{:keys [package class]} (u/split-fqcn base-str)]

            (u/schema-key package class version))))



      (or (= t-str "java.util.Map") (string/starts-with? t-str "java.util.Map<"))

      [:map-of :string :any]

      (or (= t-str "java.util.List") (string/starts-with? t-str "java.util.List<"))

      [:sequential :any]



      (and (not (string/starts-with? t-str "com.google.cloud"))

           (not (u/native-type t-str)))

      (let [ns-sym (u/infer-foreign-ns t-str)

            {:keys [class]} (u/split-fqcn t-str)]

        (keyword (name ns-sym) class))



      (string/starts-with? t-str "com.google.cloud")

      (let [clean-t (string/replace t-str #"^(class|interface) " "")

            {:keys [package class]} (u/split-fqcn clean-t)]

        (u/schema-key package class version))

      :else :any)))

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
                 (->malli-type package type version)]))
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
        enum-values (->> values
                         (map (fn [v] (if (map? v) (:name v) v)))
                         (remove #{"UNRECOGNIZED"}))]
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
