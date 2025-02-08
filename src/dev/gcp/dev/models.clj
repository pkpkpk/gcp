(ns gcp.dev.models)

;https://cloud.google.com/vertex-ai/generative-ai/docs/learn/models#gemini-2.0-flash-thinking-mode
;https://cloud.google.com/vertex-ai/generative-ai/docs/thinking-mode
;Gemini 2.0 Flash Thinking Mode
;; Thinking Mode is an experimental model and has the following limitations:
;; 32k token input limit
;; Text and image input only
;; 8k token output limit
;; Text only output
;; No built-in tool usage like Search or code execution


(def flash    {:model "gemini-1.5-flash"})
(def pro      {:model "gemini-1.5-pro"})

#! gemini 2.0
(def flash-2    {:model "gemini-2.0-flash-001"})
(def pro-2      {:model "gemini-2.0-pro-exp-02-05"})
(def flash-lite {:model "gemini-2.0-flash-lite-preview-02-05"})
(def thinking   {:model "gemini-2.0-flash-thinking-exp-01-21"})