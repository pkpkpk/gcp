(ns gcp.dev.util
  (:require [clj-http.client :as http]
            [clojure.java.io :as io]
            clojure.reflect
            [clojure.string :as string]))

(defn clean-doc [doc]
  (when (and doc (not (string/blank? doc)))
    (-> doc
        string/trim
        (string/replace #"\\\S\\" "")
        (string/replace #"\\n" " ")
        (string/replace #"\n" " ")
        (string/replace #"\\\\" "\\")
        (string/replace #"\s+" " "))))

(defn get-url-bytes [^String url]
  (println (str "fetching url -> " url))
  (let [{:keys [status body] :as response} (http/get url {:redirect-strategy :none :as :byte-array})]
    (if (= 200 status) ;google docs will 301 on missing doc
      body
      (throw (ex-info "expected 200 response" {:url url :response response})))))

(defn get-java-ref [^String url]
  (let [bs (get-url-bytes url)
        s  (String. bs "UTF-8")
        ;; can't be bothered to parse but this is decent 2/3 cut
        header-open-start (string/index-of s "<h1")
        s (subs s (inc header-open-start))
        header-open-close (string/index-of s ">")
        header-open-end (string/index-of s "</h1>")
        header (string/trim (subs s (inc header-open-close) header-open-end))
        _ (println "retrieved reference doc for " (pr-str header))
        article (subs s
                      (string/index-of s "<article>")
                      (string/index-of s "</article>"))
        article (string/trim article)]
    (.getBytes (str header article))))

(defn list-like? [t]
  (or (string/starts-with? t "List")
      (string/starts-with? t "java.util.List")))

(defn array-like? [t]
  (string/ends-with? t "[]"))

(def illegal-native-type
  #{"java.util.Map"
    "java.util.List"})

(def native-type
  #{"java.lang.Boolean" "boolean"
    "java.lang.String"
    "java.lang.Integer" "int"
    "java.lang.Long" "long"
    "java.lang.Object"
    "Map<java.lang.String, java.lang.String>"
    "Map<java.lang.String,java.lang.String>"
    "Map<String,String>"
    "Map<String, String>"
    "java.util.Map<String, String>"
    "java.util.Map<String,String>"
    "java.util.Map<java.lang.String, java.lang.String>"
    "java.util.Map<java.lang.String,java.lang.String>"

    "List<java.lang.String>"
    "List<String>"
    "java.util.List<java.lang.String>"
    "java.util.List<String>"})

(defn parse-type [t]
  (assert (string? t))
  (cond

    (string/starts-with? t "java.util.List<")
    (let [start (subs t 15)]
      (subs start 0 (.indexOf start ">")))

    (string/starts-with? t "List<")
    (let [start (subs t 5)]
      (subs start 0 (.indexOf start ">")))

    (string/starts-with? t "java.util.Map<")
    (if (string/starts-with? t "java.util.Map<java.lang.String,")
      (string/trim (subs t 31 (.indexOf t ">")))
      (throw (Exception. (str "unimplemented non-string key for type '" t "'"))))

    (string/starts-with? t "Map<")
    (if (string/starts-with? t "Map<java.lang.String,")
      (string/trim (subs t 21 (.indexOf t ">")))
      (throw (Exception. (str "unimplemented non-string key for type '" t "'"))))

    :else t))

(declare class-parts)

(defn package-key [package t]
  (keyword "gcp" (string/join "." (into [(:packageName package)] (class-parts t)))))

(defn ->malli-type [package t]
  (assert (string? t))
  (case t
    ;"java.lang.Object"
    "java.lang.String" :string
    ("boolean" "java.lang.Boolean") :boolean
    ("int" "java.lang.Integer" "long" "java.lang.Long") :int
    ("Map<java.lang.String, java.lang.String>"
      "Map<java.lang.String,java.lang.String>"
      "Map<String,String>"
      "Map<String, String>"
      "java.util.Map<String, String>"
      "java.util.Map<String,String>"
      "java.util.Map<java.lang.String, java.lang.String>"
      "java.util.Map<java.lang.String,java.lang.String>") [:map-of :string :string]
    (cond

      (string/starts-with? t "java.util.List<")
      [:sequential (->malli-type package (string/trim (subs t 15 (dec (count t)))))]

      (string/starts-with? t "List<")
      [:sequential (->malli-type package (string/trim (subs t 5 (dec (count t)))))]

      (contains? (:types/all package) t)
      (package-key package t)

      :else
      (do
        (println "\n\n WARN unknown malli type " t "\n\n")
        t))))

(defn dot-parts [class]
  (let [class (if (class? class)
                (str class)
                (if (or (symbol? class) (keyword? class))
                  (name class)
                  class))]
    (string/split class #"\.")))

(defn package-parts [className]
  (assert (string? className))
  (vec (take 4 (dot-parts className))))

(defn package-part [className]
  (string/join "." (package-parts className)))

(defn class-parts [className]
  {:pre [(string? className)]
   :post [(vector? %) (seq %) (every? string? %)]}
  (vec (nthrest (dot-parts className) 4)))

(defn class-part [className]
  (string/join "." (class-parts className)))

(defn base-part [className]
  ;;   1       2      3         4            5
  ;; com  google  cloud  $package  $base-class
  (string/join "." (take 5 (dot-parts className))))

(defn class-dollar-string [className]
  (string/join "$" (class-parts className)))

(defn dollar-parts [className]
  {:pre [(string? className) (string/includes? className "$")]
   :post [(vector? %) (seq %) (every? string? %)]}
  (let [[dollar :as cp] (class-parts className)]
    (assert (= 1 (count cp)))
    (string/split dollar #"\$")))

(defn as-dot-string [class-like]
  (if (class? class-like)
    (subs (str class-like) 6)
    (let [class-like (name class-like)]
      (if (string? class-like)
        (if (string/includes? class-like "$")
          (let [parts (dot-parts class-like)]
            (string/join "." (into (vec (butlast parts)) (string/split (peek parts) #"\$"))))
          class-like)
        (throw (Exception. (str "cannot make dot string from type " (type class-like))))))))

(defn as-dollar-string [class-like]
  (if (class? class-like)
    (as-dollar-string (as-dot-string class-like))
    (let [class-like    (name class-like)
          package-parts (package-parts class-like)
          class-parts   (class-parts class-like)]
      (string/join "." (conj package-parts (string/join "$" class-parts))))))

(defn _as-class [class-like]
  (if (class? class-like)
    class-like
    (try
      (let [class-like (name class-like)]
        (if (string/includes? class-like "$")
          (or
            (resolve (symbol class-like))
            (let [package (symbol (string/join "." (package-parts class-like)))
                  [dollar] (class-parts class-like)]
              (eval `(import ~[package (symbol dollar)]))))
          (or
            (resolve (symbol class-like))
            (let [package (symbol (string/join "." (package-parts class-like)))
                  dollar  (symbol (string/join "$" (class-parts class-like)))]
              (eval `(import ~[package (symbol dollar)]))))))
      (catch Exception e
        (throw (ex-info "error importing class" {:error e}))))))

(defn as-class [class-like]
  (let [clazz (_as-class class-like)]
    (if (class? clazz)
      clazz
      (throw (Exception. (str "failed to create class from class-like '" class-like "'"))))))

(defn builder-like? [class-like]
  (if (class? class-like)
    (builder-like? (str class-like))
    (string/ends-with? (name class-like) "Builder")))

(defonce reflect (memoize (fn [class-like] (clojure.reflect/reflect (as-class class-like)))))

;; TODO member ctors, public fields

(defn public-constructors [class-like]
  (into []
        (filter
          (fn [{:keys [flags] :as m}]
            (and (instance? clojure.reflect.Constructor m)
                 (contains? flags :public))))
        (sort-by :name (:members (reflect class-like)))))

(defn public-instantiators
  [class-like]
  (into []
        (filter
          (fn [{:keys [flags] :as m}]
            (and (contains? flags :public)
              (or
                (and
                  (instance? clojure.reflect.Method m)
                  (or (#{'of 'newBuilder 'toBuilder} (get m :name))
                      (= (symbol (as-dollar-string class-like)) (get m :return-type))))
                (instance? clojure.reflect.Constructor m)))))
        (sort-by :name (:members (reflect class-like)))))

(defn member-methods
  "returns public methods we are interested in"
  [class-like]
  (into []
        (comp
          (remove
            (fn [{:keys [flags :return-type name] :as m}]
              (or (contains? flags :private)
                  (contains? flags :synthetic)
                  (empty? flags)
                  (instance? clojure.reflect.Field m)
                  (and (instance? clojure.reflect.Method m) (nil? return-type))
                  (#{'toString 'fromPb 'toPb 'equals 'hashCode} name))))
          (map
            (fn [{:keys [parameter-types exception-types] :as m}]
              (let [t (if (instance? clojure.reflect.Constructor m)
                        :constructor
                        (do
                          (assert (instance? clojure.reflect.Method m))
                          :method))]
                (cond-> (assoc m :type t)
                        (empty? parameter-types) (dissoc m :parameter-types)
                        (empty? exception-types) (dissoc m :exception-types))))))
        (sort-by :name (:members (reflect class-like)))))
