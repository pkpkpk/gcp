(ns gcp.vertexai.v1.generativeai.protocols)

(defprotocol IHistory
  :extend-via-metadata true
  (history-to-contentable [this])
  (history-clone [this] "produce new state container w/ identical content")
  (history-revert [this n] "drop last n contentables from conversation")
  (history-add [this contentable] "add a contentable to conversation")
  (history-count [this] "how many contentables are in context")
  (history-token-count [this] "how many tokens are in context"))
