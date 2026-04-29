;; THIS FILE IS GENERATED; DO NOT EDIT
(ns gcp.pubsub.v1.AIInference
  {:doc
     "<pre>\nConfiguration for making inference requests against Vertex AI models.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.AIInference}"
   :file-git-sha "a7edd5b705557bcf72ca47dc8219677ba8595f8c"
   :fqcn "com.google.pubsub.v1.AIInference"
   :gcp.dev/certification
     {:base-seed 1777403446707
      :manifest "1278e47a-581c-5be4-ab42-9f07d3602a33"
      :passed-stages
        {:smoke 1777403446707 :standard 1777403446708 :stress 1777403446709}
      :protocol-hash
        "75d3372fb35f1e40bc5550be4e402bfd0b7a7edb8010ca96440bb4161b829c72"
      :timestamp "2026-04-28T19:10:47.794465970Z"}}
  (:require [gcp.foreign.com.google.protobuf :as protobuf]
            [gcp.global :as global])
  (:import [com.google.protobuf Struct]
           [com.google.pubsub.v1 AIInference AIInference$Builder
            AIInference$InferenceModeCase AIInference$UnstructuredInference
            AIInference$UnstructuredInference$Builder]))

(declare from-edn
         to-edn
         UnstructuredInference-from-edn
         UnstructuredInference-to-edn
         InferenceModeCase-from-edn
         InferenceModeCase-to-edn)

(defn ^AIInference$UnstructuredInference UnstructuredInference-from-edn
  [arg]
  (let [builder (AIInference$UnstructuredInference/newBuilder)]
    (when (some? (get arg :parameters))
      (.setParameters builder (protobuf/Struct-from-edn (get arg :parameters))))
    (.build builder)))

(defn UnstructuredInference-to-edn
  [^AIInference$UnstructuredInference arg]
  (when arg
    (cond-> {}
      (.hasParameters arg) (assoc :parameters
                             (protobuf/Struct-to-edn (.getParameters arg))))))

(def UnstructuredInference-schema
  [:map
   {:closed true,
    :doc
      "<pre>\nConfiguration for making inferences using arbitrary JSON payloads.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.AIInference.UnstructuredInference}",
    :gcp/category :nested/protobuf-message,
    :gcp/key :gcp.pubsub.v1/AIInference.UnstructuredInference}
   [:parameters
    {:optional true,
     :getter-doc
       "<pre>\nOptional. A parameters object to be included in each inference request.\nThe parameters object is combined with the data field of the Pub/Sub\nmessage to form the inference request.\n</pre>\n\n<code>.google.protobuf.Struct parameters = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The parameters.",
     :setter-doc
       "<pre>\nOptional. A parameters object to be included in each inference request.\nThe parameters object is combined with the data field of the Pub/Sub\nmessage to form the inference request.\n</pre>\n\n<code>.google.protobuf.Struct parameters = 1 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
    :gcp.foreign.com.google.protobuf/Struct]])

(def InferenceModeCase-schema
  [:enum
   {:closed true,
    :doc nil,
    :gcp/category :nested/enum,
    :gcp/key :gcp.pubsub.v1/AIInference.InferenceModeCase}
   "UNSTRUCTURED_INFERENCE" "INFERENCEMODE_NOT_SET"])

(defn ^AIInference from-edn
  [arg]
  (global/strict! :gcp.pubsub.v1/AIInference arg)
  (let [builder (AIInference/newBuilder)]
    (when (some? (get arg :endpoint))
      (.setEndpoint builder (get arg :endpoint)))
    (when (some? (get arg :serviceAccountEmail))
      (.setServiceAccountEmail builder (get arg :serviceAccountEmail)))
    (cond (contains? arg :unstructuredInference)
            (.setUnstructuredInference builder
                                       (UnstructuredInference-from-edn
                                         (get arg :unstructuredInference))))
    (.build builder)))

(defn to-edn
  [^AIInference arg]
  {:post [(global/strict! :gcp.pubsub.v1/AIInference %)]}
  (when arg
    (let [res (cond-> {:endpoint (.getEndpoint arg)}
                (some->> (.getServiceAccountEmail arg)
                         (not= ""))
                  (assoc :serviceAccountEmail (.getServiceAccountEmail arg)))
          res (case (.name (.getInferenceModeCase arg))
                "UNSTRUCTURED_INFERENCE" (assoc res
                                           :unstructuredInference
                                             (UnstructuredInference-to-edn
                                               (.getUnstructuredInference arg)))
                res)]
      res)))

(def schema
  [:and
   {:closed true,
    :doc
      "<pre>\nConfiguration for making inference requests against Vertex AI models.\n</pre>\n\nProtobuf type {@code google.pubsub.v1.AIInference}",
    :gcp/category :union-protobuf-oneof,
    :gcp/key :gcp.pubsub.v1/AIInference}
   [:map {:closed true}
    [:endpoint
     {:getter-doc
        "<pre>\nRequired. An endpoint to a Vertex AI model of the form\n`projects/{project}/locations/{location}/endpoints/{endpoint}` or\n`projects/{project}/locations/{location}/publishers/{publisher}/models/{model}`.\nVertex AI API requests will be sent to this endpoint.\n</pre>\n\n<code>string endpoint = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@return The endpoint.",
      :setter-doc
        "<pre>\nRequired. An endpoint to a Vertex AI model of the form\n`projects/{project}/locations/{location}/endpoints/{endpoint}` or\n`projects/{project}/locations/{location}/publishers/{publisher}/models/{model}`.\nVertex AI API requests will be sent to this endpoint.\n</pre>\n\n<code>string endpoint = 1 [(.google.api.field_behavior) = REQUIRED];</code>\n\n@param value The endpoint to set.\n@return This builder for chaining."}
     [:string {:min 1, :gen/max 1}]]
    [:serviceAccountEmail
     {:optional true,
      :getter-doc
        "<pre>\nOptional. The service account to use to make prediction requests against\nendpoints. The resource creator or updater that specifies this field must\nhave `iam.serviceAccounts.actAs` permission on the service account. If not\nspecified, the Pub/Sub [service\nagent]({$universe.dns_names.final_documentation_domain}/iam/docs/service-agents),\nservice-{project_number}&#64;gcp-sa-pubsub.iam.gserviceaccount.com, is used.\n</pre>\n\n<code>string service_account_email = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@return The serviceAccountEmail.",
      :setter-doc
        "<pre>\nOptional. The service account to use to make prediction requests against\nendpoints. The resource creator or updater that specifies this field must\nhave `iam.serviceAccounts.actAs` permission on the service account. If not\nspecified, the Pub/Sub [service\nagent]({$universe.dns_names.final_documentation_domain}/iam/docs/service-agents),\nservice-{project_number}&#64;gcp-sa-pubsub.iam.gserviceaccount.com, is used.\n</pre>\n\n<code>string service_account_email = 3 [(.google.api.field_behavior) = OPTIONAL];</code>\n\n@param value The serviceAccountEmail to set.\n@return This builder for chaining."}
     [:string {:min 1, :gen/max 1}]]
    [:unstructuredInference
     {:optional true,
      :getter-doc
        "<pre>\nOptional. Requests and responses can be any arbitrary JSON object.\n</pre>\n\n<code>\n.google.pubsub.v1.AIInference.UnstructuredInference unstructured_inference = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>\n\n@return The unstructuredInference.",
      :setter-doc
        "<pre>\nOptional. Requests and responses can be any arbitrary JSON object.\n</pre>\n\n<code>\n.google.pubsub.v1.AIInference.UnstructuredInference unstructured_inference = 2 [(.google.api.field_behavior) = OPTIONAL];\n</code>"}
     [:ref :gcp.pubsub.v1/AIInference.UnstructuredInference]]]
   [:fn
    {:error/message
       "Only one of these keys may be present: #{:unstructuredInference}"}
    (quote (fn [m]
             (<= (count (filter (set (keys m)) #{:unstructuredInference}))
                 1)))]])

(global/include-schema-registry!
  (with-meta {:gcp.pubsub.v1/AIInference schema,
              :gcp.pubsub.v1/AIInference.InferenceModeCase
                InferenceModeCase-schema,
              :gcp.pubsub.v1/AIInference.UnstructuredInference
                UnstructuredInference-schema}
    {:gcp.global/name "gcp.pubsub.v1.AIInference"}))