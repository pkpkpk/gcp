
 
 lib | snapshot| notes
-----|---------|------
`gcp.vertexai` | [![Clojars Project](https://img.shields.io/clojars/v/com.github.pkpkpk/gcp.vertexai.svg?include_prereleases)](https://clojars.org/com.github.pkpkpk/gcp.vertexai) | [`gcp.vertexai.generativeai`](https://github.com/pkpkpk/gcp/blob/main/gcp/vertexai/src/gcp/vertexai/generativeai.clj) :warning: mostly ok, the rest of vertexai :construction:
`gcp.bigquery` | [![Clojars Project](https://img.shields.io/clojars/v/com.github.pkpkpk/gcp.bigquery.svg?include_prereleases)](https://clojars.org/com.github.pkpkpk/gcp.bigquery) | (datasets, tables, queries) :white_check_mark:, (jobs, routines, models) :construction:
`gcp.storage` | :x: | WIP :construction:
`gcp.pubsub` | :x: | WIP :construction:
`gcp.run` | :x: | WIP :construction:


### gemini models

As of Feb 2025 here is a small list of models you may be interested in.
See [stable gemini models](https://ai.google.dev/gemini-api/docs/models/gemini#model-variations)
and [experimental models](https://ai.google.dev/gemini-api/docs/models/experimental-models) for the complete list.

model | note
------|-----
`"gemini-2.0-flash-thinking-exp-01-21"` | text-only thinking model, no tool use or structured output. thinking tokens not exposed via vertexai (..yet)
`"gemini-2.0-flash-lite-preview-02-05"` | cheap, fast and dumb. supports structured output, good at cleaning output from thinking model
`"gemini-2.0-pro-exp-02-05"` | gemini-pro-2 preview. multimodal very good at semantic parsing, follows schema rules and supports more complex json output.
`"gemini-2.0-flash-001"` | gemini-flash-2. multimodal, cheaper and faster than pro but has higher hallucination rate







