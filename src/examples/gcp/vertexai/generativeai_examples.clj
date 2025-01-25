(ns gcp.vertexai.generativeai-examples
  (:require [clojure.java.io :as io]
            [gcp.global :as g]
            [gcp.vertexai.v1.VertexAI :as VertexAI]
            [gcp.vertexai.generativeai :as genai]
            [gcp.vertexai.v1.api.GenerateContentResponse :as GenerateContentResponse]
            [clojure.repl :refer :all]
            [malli.core :as m]
            [jsonista.core :as j]
            malli.dev
            [malli.error :as me])
  (:import (java.io ByteArrayOutputStream)))

(comment
  (do
    (require :reload 'gcp.vertexai.v1.generativeai.examples)
    (in-ns 'gcp.vertexai.v1.generativeai.examples))
  )

(defonce get-url-bytes
  (let [f (fn [^String url]
            (with-open [in  (io/input-stream url)
                        out (ByteArrayOutputStream.)]
              (io/copy in out)
              (.toByteArray out)))]
    (memoize f)))

(defonce client (VertexAI/from-edn {:location "us-central1"}))

(def flash {:vertexai client :model "gemini-1.5-flash-001"})

(def french-tutor (assoc flash :systemInstruction "You are a french translator."))

(comment
  (genai/generate-content french-tutor "bonjour")

  ;; same thing, but un-sugared Content
  (genai/generate-content french-tutor {:role "user"
                                        :parts ["bonjour"]})

  ;; same thing again, but un-sugared Part
  (genai/generate-content french-tutor {:role "user"
                                        :parts [{:text "bonjour"}]})

  ;; 'content' can be a blend of text, uris, and maps defining resources w/ mimeTypes
  (genai/generate-content flash {:parts ["please give brief summary of the audio"
                                         {:mimeType "audio/mp3"
                                          :partData "gs://cloud-samples-data/generative-ai/audio/pixel.mp3"}]})

  ;; same thing again, inner vec is just parts
  ;; [[A B]] --> [{:role 'user' :parts [A B]}]
  (genai/generate-content flash [["Can you transcribe this interview, in the format of timecode, speaker, caption. Use speaker A, speaker B, etc. to identify speakers."
                                  {:mimeType "audio/mp3"
                                   :partData "gs://cloud-samples-data/generative-ai/audio/pixel.mp3"}]])

  (genai/generate-content flash ["what's in this photo?"
                                 {:parts [{:mimeType "image/png"
                                           :partData "gs://generativeai-downloads/images/scones.jpg"}]}])
  )

#!-----------------------------------------------------------------------------

;; grounded w/ public data
(def grounded {:vertexai  client
               :model     "gemini-1.5-flash-001"
               :tools     [{:googleSearchRetrieval {}}]})

(comment
  (genai/generate-content grounded "what are the current secondary market price for tickets to Oasis at wembley?")
  )

#!-----------------------------------------------------------------------------

(def clean-spanish {:vertexai          client
                    :model         "gemini-1.5-flash-001"
                    :systemInstruction "Your mission is to translate text in English to es"
                    :safetySettings    [{:category  "HARM_CATEGORY_HATE_SPEECH"
                                         :threshold "BLOCK_MEDIUM_AND_ABOVE"}
                                        {:category  "HARM_CATEGORY_DANGEROUS_CONTENT"
                                         :threshold "BLOCK_MEDIUM_AND_ABOVE"}
                                        {:category  "HARM_CATEGORY_SEXUALLY_EXPLICIT"
                                         :threshold "BLOCK_MEDIUM_AND_ABOVE"}]
                    :generationConfig  {:maxOutputTokens 2048
                                        :temperature     0.4
                                        :topK            32
                                        :topP            1.0}}) ;TODO 0-1 inclusive

(comment
  (genai/generate-content clean-spanish "what are some bad words in spanish")
  )

#!-----------------------------------------------------------------------------

;; TODO can we use malli to generate functionDeclarations from a schema

(def weather-model {:vertexai client
                    :model    "gemini-1.5-flash-001"
                    :library  {"getCurrentWeather" (fn [arg] ;  {"location" "Paris"}
                                                     {:location    "Paris, FR"
                                                      :temperature "28"})}
                    :tools    [{:functionDeclarations [{:name        "getCurrentWeather"
                                                        :description "fn(location) -> STRING weather report"
                                                        :parameters  {:type       "OBJECT"
                                                                      :properties {"location" {:type        "STRING"
                                                                                               :description "location"}}
                                                                      :required   ["location"]}}]}]})


(comment
  (def weather-chat (genai/chat-session weather-model))
  (genai/send-msg (genai/chat-session weather-model) "what's the weather in paris")
  )

#!-----------------------------------------------------------------------------
#! controlling response via mimeType

(def jsonMimeType (assoc flash :generationConfig {:responseMimeType "application/json"}))

(comment
  (genai/generate-content jsonMimeType "List a few popular cookie recipes using this JSON schema: Recipe = {\"recipe_name\": str} Return: list[Recipe]")
  )

#!-----------------------------------------------------------------------------
#! controlling response w/  mimeType & schema

(def json-recipes (assoc flash :generationConfig {:responseMimeType "application/json"
                                                  :responseSchema   {:type  "ARRAY"
                                                                     :items {:type       "OBJECT"
                                                                             :properties {"recipe_name" {:type "STRING"}}
                                                                             :required   ["recipe_name"]}}}))


(comment
  (genai/generate-content json-recipes "list some cookie recipes")
  )

#!-----------------------------------------------------------------------------
#! use output schema for semantic parsing to JSON

(def review-parser (assoc flash :generationConfig {:responseMimeType "application/json"
                                                   :responseSchema   {:type  "ARRAY"
                                                                      :items {:type       "OBJECT"
                                                                              :properties {:flavor {:type "STRING"}
                                                                                           :rating {:type "INTEGER"}}
                                                                              :required   ["rating" "flavor"]}}}))

(comment
  (genai/generate-content review-parser (str "Reviews from our social media:\n"
                                             "\"Absolutely loved it! Best ice cream I've ever had.\" "
                                             "Rating: 4, Flavor: Strawberry Cheesecake\n"
                                             "\"Quite good, but a bit too sweet for my taste.\" "
                                             "Rating: 1, Flavor: Mango Tango"))
  )

#!-----------------------------------------------------------------------------
#! larger semantic parsing to JSON

(def forecaster
  (assoc flash :generationConfig
               {:responseMimeType "application/json"
                :responseSchema   {:type  "ARRAY"
                                   :items {:type       "OBJECT"
                                           :properties {"Day" {:type "STRING"}
                                                        "Forecast" {:type "STRING"}
                                                        "Temperature" {:type "INTEGER"}
                                                        "Humidity" {:type "STRING"}
                                                        "Wind speed" {:type "STRING"}}
                                           :required   ["Day" "Temperature" "Forecast"]}}}))

(def prompt (str "The week ahead brings a mix of weather conditions.\n"
                 "Sunday is expected to be sunny with a temperature of 77°F and a humidity level "
                 "of 50%. Winds will be light at around 10 km/h.\n"
                 "Monday will see partly cloudy skies with a slightly cooler temperature of 72°F "
                 "and humidity increasing to 55%. Winds will pick up slightly to around 15 km/h.\n"
                 "Tuesday brings rain showers, with temperatures dropping to 64°F and humidity"
                 "rising to 70%. Expect stronger winds at 20 km/h.\n"
                 "Wednesday may see thunderstorms, with a temperature of 68°F and high humidity "
                 "of 75%. Winds will be gusty at 25 km/h.\n"
                 "Thursday will be cloudy with a temperature of 66°F and moderate humidity at 60%. "
                 "Winds will ease slightly to 18 km/h.\n"
                 "Friday returns to partly cloudy conditions, with a temperature of 73°F and lower "
                 "humidity at 45%. Winds will be light at 12 km/h.\n"
                 "Finally, Saturday rounds off the week with sunny skies, a temperature of 80°F, "
                 "and a humidity level of 40%. Winds will be gentle at 8 km/h."))

(comment
  (genai/generate-content forecaster prompt)
  )

#!-----------------------------------------------------------------------------

(def package-classifier-response-schema
  {:type "OBJECT"
   :properties {"to_discard" {:type "INTEGER"}
                "subcategory" {:type "STRING"}
                "safe_handling" {:type "INTEGER"}
                "item_category" {:type "STRING"
                                 :enum ["clothing", "winter apparel", "specialized apparel", "furniture",
                                        "decor", "tableware", "cookware", "toys"]}
                "for_resale" {:type "INTEGER"}
                "condition" {:type "STRING"
                             :enum ["new in package", "like new", "gently used", "used", "damaged", "soiled"]}}})

(def package-classifier
  (assoc flash :generationConfig
               {:responseMimeType "application/json"
                :responseSchema   package-classifier-response-schema}))

(comment
  (genai/generate-content
    package-classifier
    (str "Item description:\n"
         "The item is a long winter coat that has many tears all around the seams "
         "and is falling apart.\n"
         "It has large questionable stains on it."))
  )

#!-----------------------------------------------------------------------------
#! image input, json output

(def image-object-classifier
  (assoc flash :generationConfig
               {:responseMimeType "application/json"
                :responseSchema {:type "ARRAY"
                                 :items {:type "OBJECT"
                                         :properties {"object" {:type "STRING"}}}}}))

(comment
  (genai/generate-content image-object-classifier
                          ["generate a list of objects in the images"
                           {:parts [{:mimeType "image/jpeg"
                                     :partData "gs://cloud-samples-data/generative-ai/image/gardening-tools.jpeg"}]}])
  )

#!-----------------------------------------------------------------------------
#! count (multimodal) tokens

(comment
  (genai/count-tokens flash "why is the sky blue?")

  (genai/count-tokens flash ["provide a description of the video"
                             [{:mimeType "video/mp4"
                               :partData "gs://cloud-samples-data/generative-ai/video/pixel8.mp4"}]])
  )

#!-----------------------------------------------------------------------------
#! multimodal input

(comment
  (genai/generate-content flash ["What is in the video?"
                                 [{:mimeType "video/mp4"
                                   :partData "gs://cloud-samples-data/video/animals.mp4"}]])

  (genai/generate-content flash ["Please summarize the given document."
                                 [{:mimeType "application/pdf"
                                   :partData "gs://cloud-samples-data/generative-ai/pdf/2403.05530.pdf"}]])

  (genai/generate-content flash  ["is this video and image correlated?"
                                  [{:mimeType "video/mp4"
                                    :partData "gs://cloud-samples-data/video/animals.mp4"}]
                                  [{:mimeType "image/jpeg"
                                    :partData "gs://cloud-samples-data/generative-ai/image/character.jpg"}]])

  (def stream
    (genai/generate-content-seq flash ["is this video and image correlated?"
                                          [{:mimeType "video/mp4"
                                            :partData "gs://cloud-samples-data/video/animals.mp4"}]
                                          [{:mimeType "image/jpeg"
                                            :partData "gs://cloud-samples-data/generative-ai/image/character.jpg"}]]))

  (doseq [text (map GenerateContentResponse/extract-text stream)]
    (println text))

  (genai/generate-content flash ["Provide a description of the video. The description should also contain anything important which people say in the video."
                                 [{:mimeType "video/mp4"
                                   :partData "gs://cloud-samples-data/generative-ai/video/pixel8.mp4"}]])


  (genai/generate-content flash
                          ["Watch each frame in the video carefully and answer the questions.
   Only base your answers strictly on what information is available in the video attached.
   Do not make up any information that is not part of the video and do not be too verbose.

   Questions:
   - When is the moment in the image happening in the video? Provide a timestamp.
   - What is the context of the moment and what does the narrator say about it?"
                           [{:mimeType "video/mp4"
                             :partData "gs://cloud-samples-data/generative-ai/video/behind_the_scenes_pixel.mp4"}]
                           [{:mimeType "image/png"
                             :partData "gs://cloud-samples-data/generative-ai/image/a-man-and-a-dog.png"}]])

  (genai/generate-content flash
                          [[{:mimeType "image/png"
                             :partData (get-url-bytes "https://storage.googleapis.com/cloud-samples-data/vertex-ai/llm/prompts/landmark1.png")}]
                           "city: Rome, Landmark: the Colosseum"
                           [{:mimeType "image/png"
                             :partData (get-url-bytes "https://storage.googleapis.com/cloud-samples-data/vertex-ai/llm/prompts/landmark2.png")}]
                           "city: Beijing, Landmark: Forbidden City"
                           [{:mimeType "image/png"
                             :partData (get-url-bytes "https://storage.googleapis.com/cloud-samples-data/vertex-ai/llm/prompts/landmark3.png")}]])
  )

#!-----------------------------------------------------------------------------
#! multi-turn

(comment

  (def chat (genai/chat-session (assoc flash :generationConfig {:maxOutputTokens 2048
                                                                :temperature     0.4
                                                                :topK            32
                                                                :topP            (double 1)})))

  (genai/send-msg chat "what is this image"
                       [{:mimeType "image/jpeg"
                         :partData "gs://generativeai-downloads/images/scones.jpg"}])

  (genai/send-msg chat "what did i just show you?")

  )


