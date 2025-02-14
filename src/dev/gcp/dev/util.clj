(ns gcp.dev.util
  (:require [clj-http.client :as http]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [taoensso.telemere :as tt]))

(def home (io/file (System/getProperty "user.home")))
(def pkpkpk (io/file home "pkpkpk"))
(def root (io/file pkpkpk "gcp"))
(def src  (io/file root "src"))

(defn get-url-bytes [^String url]
  (tt/log! (str "fetching url -> " url))
  (let [{:keys [status body] :as response} (http/get url {:redirect-strategy :none :as :byte-array})]
    (if (= 200 status) ;google docs will 301 on missing doc
      body
      (throw (ex-info "expected 200 response" {:url url :response response})))))

(defn get-java-ref [^String url]
  (let [bs (get-url-bytes url)
        s  (String. bs "UTF-8")
        ;; can't be bothered to parse but this is decent 2/3 cut
        head (subs s
                   (string/index-of s "<h1")
                   (string/index-of s "</h1>"))
        head (string/trim head)
        article (subs s
                      (string/index-of s "<article>")
                      (string/index-of s "</article>"))
        article (string/trim article)]
    (.getBytes (str head article))))

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
    "List<String>"})

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

      (or (contains? (set (:classes package)) t)
          (contains? (set (:enums package)) t))
      (package-key package t)

      :else
      (do
        (tt/log! :warn (str "WARN unknown malli type " t))
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

(defn class-parts [className]
  {:pre [(string? className)]
   :post [(vector? %) (seq %) (every? string? %)]}
  (vec (nthrest (dot-parts className) 4)))

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