(ns gcp.vertexai
  (:require [gcp.vertexai.PredictionService :as PredictionService]
            [jsonista.core :as j]))

(def client PredictionService/client)

(defn generate-content
  "(GenerateContentRequest)
   (client GenerateContentRequest)
   (model-config & contentables)
   (client model-config & contentables)

   Where
     GenerateContentRequest := :gcp.vertexai.api/GenerateContentRequest
     model-config := (dissoc GenerateContentRequest :contents)
     contentable := [:or :string
                         :gcp.vertexai.api/Content
                         [:sequential {:min 1} :gcp.vertexai.api/Part]]

   Returns :gcp.vertexai.api/GenerateContentResponse"
  [& args]
  (PredictionService/execute! (PredictionService/->GenerateContentRequest (vec args))))

(defn content-response-text [response]
  (get-in response [:candidates 0 :content :parts 0 :text]))

(defn content-response-json [response]
  (try
    (some-> response content-response-text (j/read-value j/keyword-keys-object-mapper))
    (catch Exception e
      (throw (ex-info (str "error parsing json from response: " (ex-message e))
                      {:response response
                       :cause e})))))

(comment
  (defn desugar
    "Convert edn form with tolerant Content sugar into strict representation suitable for JSON requests."
    [])

  (defn generate-json [& arg]
    ;; add mimeType
    ;; enforce schema
    ;; return extracted json not full response
    )
  )