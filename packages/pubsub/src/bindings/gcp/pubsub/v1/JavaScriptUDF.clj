;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.JavaScriptUDF
  {:doc
     "<pre>\nUser-defined JavaScript function that can transform or filter a Pub/Sub\nmessage.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.JavaScriptUDF}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.JavaScriptUDF"
   :gcp.dev/certification
     {:base-seed 1777403448287
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777403448287 :standard 1777403448288 :stress 1777403448289}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-28T19:10:49.135343271Z"}}
  (:require [gcp.global :as global])
  (:import [com.google.pubsub.v1 JavaScriptUDF JavaScriptUDF$Builder]))

(declare from-edn to-edn)

(defn ^JavaScriptUDF from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/JavaScriptUDF arg)
  (let [builder (JavaScriptUDF/newBuilder)]
    (when (some? (get arg :code)) (.setCode builder (get arg :code)))
    (when (some? (get arg :functionName))
      (.setFunctionName builder (get arg :functionName)))
    (.build builder)))

(defn to-edn
  [^JavaScriptUDF arg]
  {:post [(global/strict! :gcp.pubsub.v1/JavaScriptUDF %)]}
  (when arg
    (cond-> {:code (.getCode arg), :functionName (.getFunctionName arg)})))

(def schema
  [:map
   {:closed true,
    :doc
      "<pre>\nUser-defined JavaScript function that can transform or filter a Pub/Sub\nmessage.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.JavaScriptUDF}",
    :gcp/category :protobuf-message,
    :gcp/key :gcp.pubsub.v1/JavaScriptUDF}
   [:code
    {:getter-doc
       "<pre>\nRequired. JavaScript code that contains a function `function_name` with the\nbelow signature:\n\n```\n/&#42;*\n* Transforms a Pub/Sub message.\n\n* &#64;return {(Object&lt;string, (string | Object&lt;string, string&gt;)&gt;|null)} - To\n* filter a message, return `null`. To transform a message return a map\n* with the following keys:\n*   - (required) 'data' : {string}\n*   - (optional) 'attributes' : {Object&lt;string, string&gt;}\n* Returning empty `attributes` will remove all attributes from the\n* message.\n*\n* &#64;param  {(Object&lt;string, (string | Object&lt;string, string&gt;)&gt;} Pub/Sub\n* message. Keys:\n*   - (required) 'data' : {string}\n*   - (required) 'attributes' : {Object&lt;string, string&gt;}\n*\n* &#64;param  {Object&lt;string, any&gt;} metadata - Pub/Sub message metadata.\n* Keys:\n*   - (optional) 'message_id'  : {string}\n*   - (optional) 'publish_time': {string} YYYY-MM-DDTHH:MM:SSZ format\n*   - (optional) 'ordering_key': {string}\n*&#47;\n\nfunction &lt;function_name&gt;(message, metadata) {\n}\n```\n</pre>\n\n<code>string code = 2 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The code.",
     :setter-doc
       "<pre>\nRequired. JavaScript code that contains a function `function_name` with the\nbelow signature:\n\n```\n/&#42;*\n* Transforms a Pub/Sub message.\n\n* &#64;return {(Object&lt;string, (string | Object&lt;string, string&gt;)&gt;|null)} - To\n* filter a message, return `null`. To transform a message return a map\n* with the following keys:\n*   - (required) 'data' : {string}\n*   - (optional) 'attributes' : {Object&lt;string, string&gt;}\n* Returning empty `attributes` will remove all attributes from the\n* message.\n*\n* &#64;param  {(Object&lt;string, (string | Object&lt;string, string&gt;)&gt;} Pub/Sub\n* message. Keys:\n*   - (required) 'data' : {string}\n*   - (required) 'attributes' : {Object&lt;string, string&gt;}\n*\n* &#64;param  {Object&lt;string, any&gt;} metadata - Pub/Sub message metadata.\n* Keys:\n*   - (optional) 'message_id'  : {string}\n*   - (optional) 'publish_time': {string} YYYY-MM-DDTHH:MM:SSZ format\n*   - (optional) 'ordering_key': {string}\n*&#47;\n\nfunction &lt;function_name&gt;(message, metadata) {\n}\n```\n</pre>\n\n<code>string code = 2 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The code to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]
   [:functionName
    {:getter-doc
       "<pre>\nRequired. Name of the JavasScript function that should applied to Pub/Sub\nmessages.\n</pre>\n\n<code>string function_name = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The functionName.",
     :setter-doc
       "<pre>\nRequired. Name of the JavasScript function that should applied to Pub/Sub\nmessages.\n</pre>\n\n<code>string function_name = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The functionName to set.\n@return This builder for chaining."}
    [:string {:min 1, :gen/max 1}]]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/JavaScriptUDF schema}
    {:gcp.global/name "gcp.pubsub.v1.JavaScriptUDF"}))