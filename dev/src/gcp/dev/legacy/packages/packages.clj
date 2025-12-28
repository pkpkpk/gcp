(ns gcp.dev.packages.legacy.packages
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [gcp.dev.models :as models]
   [gcp.dev.store :as store]
   [gcp.dev.util :refer :all]
   [gcp.vertexai.generativeai :as genai]
   [jsonista.core :as j]))

(def home            (io/file (System/getProperty "user.home")))
(def project-root    (io/file home "pkpkpk" "gcp"))
(def packages-root   (io/file home "pkpkpk" "gcp" "gcp"))
(def googleapis-root (io/file home "googleapis"))

;; HTML layout is different than class reference.. nested articles, H1 is inside inner article
;; [:article
;;   [:h1 <package ...>]
;;   ...
;;   [:h2 category header]
;;   [:div
;;      [:table <items...>]]
;;   [:h2 category header]
;;   [:div
;;     [:table <items...>]]

(defn ^String get-overview [^String url]
  (let [bs (get-url-bytes url)
        s  (String. bs "UTF-8")
        ;; can't be bothered to parse but this is decent 2/3 cut
        header-open-start (string/index-of s "<h1")
        s (subs s (inc header-open-start))
        header-open-close (string/index-of s ">")
        header-open-end (string/index-of s "</h1>")
        header (string/trim (subs s (inc header-open-close) header-open-end))
        _ (println "retrieved reference doc for " (pr-str header))
        ; article-start (string/index-of s "<article>")
        article-end (string/index-of s "</article>")
        article (string/trim (subs s (+ header-open-end 5) article-end))]
    (str header article)))

(defn extract-from-url
  ([model-cfg url]
   (extract-from-url model-cfg url identity))
  ([model-cfg url validator!]
   ;; never cache package summary bytes, always get latest
   (let [overview (get-overview url)
         response (genai/generate-content model-cfg [{:mimeType "text/html" :partData (.getBytes overview)}])
         edn (genai/response-json response)]
     (validator! edn)
     edn)))

(def package-response-schema
  {:type       "OBJECT"
   ;; TODO settings clients etc
   :required   ["version" "classes" "enums" "exceptions" "interfaces"]
   :properties {"version" {:type    "STRING"
                           :example "2.47.0"
                           :description "the semantic version number of the package"
                           :pattern "^[0-9]+\\.[0-9]+\\.[0-9]+$"}
                "settings"
                {:type  "ARRAY"
                 :items {:type        "STRING"
                         :description "package qualified settings class name"}}
                "classes"
                {:type  "ARRAY"
                 :items {:type        "STRING"
                         :description "package qualified class name"}}
                "enums"
                {:type  "ARRAY"
                 :items {:type        "STRING"
                         :description "package qualified enum name"}}
                "exceptions"
                {:type  "ARRAY"
                 :items {:type        "STRING"
                         :description "package qualified exception name"}}
                "interfaces"
                {:type  "ARRAY"
                 :items {:type        "STRING"
                         :description "package qualified interface name"}}}})

(defn $package-summary
  ([package-url]
   (let [cfg {:generationConfig  {:responseMimeType "application/json"
                                  :responseSchema   package-response-schema}
              :model             (:model models/pro-2)
              :systemInstruction "You are given google cloud java package summary page. Extract its parts into arrays."}]
     ;; unclear how the overviews are generated, but they are different vs javadoc
     ;; and better organized. also reference docs can lag maven/gh releases by few weeks
     ;;
     ;; most likely way this fails seems to be omitting classes
     ;;
     ;; one hang-up w/ validation is versioning... requesting the versioned url of the 'latest' release
     ;; will redirect to the ../latest/.. urls, so caching is not obvious an probably needs versioned stores
     ;; because the urls can be misleading
     ;;
     ;; choosing to not cache and cycle this every repl session, knowing it will blowup if version moves ahead
     (extract-from-url cfg package-url))))

(defonce $package-summary-memo (memoize $package-summary))

(defn slurp-class
  [{:keys [repoRoot]} class-like]
  (assert (some? repoRoot))
  (let [[filename] (class-parts class-like)
        file (io/file repoRoot (str filename ".java"))]
    (if (.exists file)
      (slurp file)
      (throw (Exception. (str "could not find file for " class-like))))))

(defn discovery-properties [package]
  (reduce
    (fn [acc [schema-key {:keys [properties]}]]
      (into acc
            (map
              (fn [[property-key v]]
                ;; this is 100% overwriting repeat properties
                [property-key (assoc v :path [schema-key property-key])]))
            properties))
    (sorted-map)
    (get-in package [:discovery :schemas])))
