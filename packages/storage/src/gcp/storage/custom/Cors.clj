;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.storage.custom.Cors
  {:doc
     "Cross-Origin Resource Sharing (CORS) configuration for a bucket.\n\n@see <a href=\"https://cloud.google.com/storage/docs/cross-origin\">Cross-Origin Resource Sharing\n    (CORS)</a>"
   :file-git-sha "ca6878974eaf83f0237fa7faacd1e664db93e7a1"
   :fqcn "com.google.cloud.storage.Cors"
   :gcp.dev/certification
     {:base-seed 1774917445713
      :manifest "215ec381-0f5f-5884-ab6d-eb0bb246cd16"
      :passed-stages
        {:smoke 1774917445713 :standard 1774917445714 :stress 1774917445715}
      :protocol-hash
        "51c52e182dafff740cb243c007b50897c605ebe788366788660550307c87e955"
      :timestamp "2026-03-31T00:37:26.808983817Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.cloud.storage Cors Cors$Builder Cors$Origin HttpMethod]))

(declare from-edn to-edn Origin-from-edn Origin-to-edn)

(defn ^Cors$Origin Origin-from-edn
  [arg]
  (cond (empty? arg) (Cors$Origin/any)
        (string? arg) (Cors$Origin/of arg)
        (get arg :value) (Cors$Origin/of (get arg :value))
        (and (get arg :scheme) (get arg :host) (get arg :port))
          (Cors$Origin/of (get arg :scheme)
                          (get arg :host)
                          (int (get arg :port)))
        true (ex-info "failed to match edn for static-factory cond body"
                      {:arg arg, :key :gcp.storage/Cors.Origin})))

(defn Origin-to-edn
  [^Cors$Origin arg]
  (cond (.getValue arg) {:value (.getValue arg)}
        true {}
        :else (throw (clojure.core/ex-info
                       "failed to match edn for static-factory cond body"
                       {:key :gcp.storage/Cors.Origin, :arg arg}))))

(def Origin-schema
  [:or
   {:closed true,
    :doc "Class for a CORS origin.",
    :gcp/category :nested/static-factory,
    :gcp/key :gcp.storage/Cors.Origin}
   [:maybe
    [:map
     {:closed true,
      :doc "Returns an {@code Origin} object for all possible origins."}]]
   [:re {:doc "Full origin string (e.g. http://example.com:80)"}
    #"^https?://[a-zA-Z0-9.-]+(:\d+)?$"]
   [:map
    {:closed true,
     :doc "Creates an {@code Origin} object for the provided value."}
    [:value [:string {:min 1}]]]
   [:map
    {:closed true,
     :doc
       "Returns an {@code Origin} object for the given scheme, host and port."}
    [:scheme [:enum "http" "https"]]
    [:host [:re #"^[a-zA-Z0-9.-]+$"]]
    [:port :i32]]])

(defn ^Cors from-edn
  [arg]
  (global/strict! :gcp.storage/Cors arg)
  (let [builder (Cors/newBuilder)]
    (when (some? (get arg :maxAgeSeconds))
      (.setMaxAgeSeconds builder (int (get arg :maxAgeSeconds))))
    (when (seq (get arg :methods))
      (.setMethods builder (map HttpMethod/valueOf (get arg :methods))))
    (when (seq (get arg :origins))
      (.setOrigins builder (map Origin-from-edn (get arg :origins))))
    (when (seq (get arg :responseHeaders))
      (.setResponseHeaders builder (seq (get arg :responseHeaders))))
    (.build builder)))

(defn to-edn
  [^Cors arg]
  {:post [(global/strict! :gcp.storage/Cors %)]}
  (cond-> {}
    (.getMaxAgeSeconds arg) (assoc :maxAgeSeconds (.getMaxAgeSeconds arg))
    (seq (.getMethods arg)) (assoc :methods
                              (map (fn [e] (.name e)) (.getMethods arg)))
    (seq (.getOrigins arg)) (assoc :origins
                              (map Origin-to-edn (.getOrigins arg)))
    (seq (.getResponseHeaders arg)) (assoc :responseHeaders
                                      (seq (.getResponseHeaders arg)))))

(def schema
  [:map
   {:closed true,
    :doc
      "Cross-Origin Resource Sharing (CORS) configuration for a bucket.\n\n@see <a href=\"https://cloud.google.com/storage/docs/cross-origin\">Cross-Origin Resource Sharing\n    (CORS)</a>",
    :gcp/category :accessor-with-builder,
    :gcp/key :gcp.storage/Cors}
   [:maxAgeSeconds
    {:optional true,
     :getter-doc
       "Returns the max time in seconds in which a client can issue requests before sending a new\npreflight request.",
     :setter-doc
       "Sets the max time in seconds in which a client can issue requests before sending a new\npreflight request."}
    :i32]
   [:methods
    {:optional true,
     :getter-doc
       "Returns the HTTP methods supported by this CORS configuration.",
     :setter-doc "Sets the HTTP methods supported by this CORS configuration."}
    [:sequential {:min 1}
     [:enum {:closed true} "GET" "HEAD" "PUT" "POST" "DELETE" "OPTIONS"]]]
   [:origins
    {:optional true,
     :getter-doc "Returns the origins in this CORS configuration.",
     :setter-doc "Sets the origins for this CORS configuration."}
    [:sequential {:min 1} :gcp.storage/Cors.Origin]]
   [:responseHeaders
    {:optional true,
     :getter-doc
       "Returns the response headers supported by this CORS configuration.",
     :setter-doc
       "Sets the response headers supported by this CORS configuration."}
    [:sequential {:min 1} [:string {:min 1}]]]])

(global/include-schema-registry!
  (with-meta {:gcp.storage/Cors schema, :gcp.storage/Cors.Origin Origin-schema}
    {:gcp.global/name "gcp.storage.Cors"}))