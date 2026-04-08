(ns gcp.vertexai.PredictionService
  (:require [clojure.string :as string]
            [gcp.global :as g]
            [gcp.vertexai.api.GenerateContentRequest :as GenerateContentRequest]
            [gcp.vertexai.api.GenerateContentResponse :as GenerateContentResponse]
            [gcp.vertexai.api.Content :as Content]
            gcp.vertexai.VertexAI
            [jsonista.core :as j]
            [malli.util :as mu])
  (:import [com.google.cloud.vertexai VertexAI]))

(defonce ^:dynamic *client* nil)
(defonce ^:private client-from-edn (memoize gcp.vertexai.VertexAI/from-edn))

(defn ^VertexAI client
  ([]
   (client nil))
  ([arg]
   (or *client*
       (do
         (g/strict! ::clientable arg)
         (if (instance? VertexAI arg)
           arg
           (client-from-edn arg))))))

(def registry {::clientable                 [:maybe
                                             [:or
                                              (g/instance-schema com.google.cloud.vertexai.VertexAI)
                                              :gcp.vertexai/VertexAI]]
               ::contentable                [:or
                                             :gcp.vertexai.api/Content
                                             :string
                                             [:sequential {:min 1} :gcp.vertexai.api/Part]]

               ::model-config               (mu/dissoc :gcp.vertexai.api/GenerateContentRequest :contents (g/mopts))

               ::GenerateContentRequest     [:map
                                             [:op [:= ::GenerateContentRequest]]
                                             [:vertexai ::clientable]
                                             [:request :gcp.vertexai.api/GenerateContentRequest]]})

               (g/include-schema-registry! (with-meta registry {::g/name "gcp.vertexai.PredictionService"}))

#!----------------------------------------------------------------------------------------------------------------------

(defn- extract-parse-values [parsed]
  (cond
    (instance? malli.core.Tag parsed)
    (extract-parse-values (:value parsed))

    (instance? malli.core.Tags parsed)
    (:values parsed)

    (map? parsed)
    parsed

    :else parsed))

(defmulti execute! :op)

#!----------------------------------------------------------------------------------------------------------------------

(defn contentable->Content
  [contentable]
  (if (g/valid? :gcp.vertexai.api/Content contentable)
    contentable
    (if (g/valid? [:sequential {:min 1} :gcp.vertexai.api/Part] contentable)
      {:role  "user"
       :parts contentable}
      (if (string? contentable)
        {:role  "user"
         :parts [{:text contentable}]}
        (throw (ex-info "unsupported contentable shape" (g/explain ::contentable contentable)))))))

(defn- resource-name [^VertexAI client model]
  (let [model-name (let [model-name (name model)]
                     (if (string/starts-with? model-name "gemini")
                       (str "publishers/google/models/" model-name)
                       model-name))]
    (if (string/starts-with? model-name "projects/")
      model-name
      (if (string/starts-with? model-name "publishers/")
        (format "projects/%s/locations/%s/%s"
                (.getProjectId client)
                (.getLocation client)
                model-name)
        (format "projects/%s/locations/%s/publishers/google/models/%s"
                (.getProjectId client)
                (.getLocation client)
                (let [*model-name (atom model-name)]
                  (doseq [prefix #{"projects/" "publishers/" "models/"}]
                    (when (string/starts-with? @*model-name prefix)
                      (swap! *model-name subs (inc (.lastIndexOf ^String @*model-name \/)))))
                  @*model-name))))))

(defn- normalize-model
  [m]
  (if-not (contains? m :model)
    m
    (let [vertexai (:vertexai m)]
      (update m :model #(resource-name (client vertexai) %)))))

(def ^:private generate-content-args-schema
  [:altn
   [:client-model-contents
    [:catn [:clientable ::clientable]
           [:modelConfig ::model-config]
           [:contents [:+ ::contentable]]]]
   [:model-contents
    [:catn [:modelConfig ::model-config]
           [:contents [:+ ::contentable]]]]
   [:client-request
    [:catn [:clientable ::clientable]
           [:request :gcp.vertexai.api/GenerateContentRequest]]]
   [:request
    [:catn [:request :gcp.vertexai.api/GenerateContentRequest]]]])

(defn ->GenerateContentRequest [args]
  (let [schema (g/schema generate-content-args-schema)
        parsed (m/parse schema args)]
    (if (= ::m/invalid parsed)
      (throw (ex-info "Invalid arguments to generate-content" {:args args :explain (g/explain schema args)}))
      (let [{:keys [clientable modelConfig contents request]} (extract-parse-values parsed)
            resolved-request (or request
                                 (assoc modelConfig :contents (mapv contentable->Content contents)))
            normalized-request (-> resolved-request
                                   (assoc :vertexai clientable)
                                   normalize-model
                                   (dissoc :vertexai))]
        {:op        ::GenerateContentRequest
         :vertexai  clientable
         :request   normalized-request}))))

(defmethod execute! ::GenerateContentRequest [{:keys [vertexai request]}]
  (let [client  (.getPredictionServiceClient (client vertexai))
        request (GenerateContentRequest/from-edn request)]
    (GenerateContentResponse/to-edn (.generateContent client request))))

#!----------------------------------------------------------------------------------------------------------------------

