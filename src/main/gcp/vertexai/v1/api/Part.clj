(ns gcp.vertexai.v1.api.Part
  (:require [gcp.vertexai.v1.api.Blob :as blob]
            [gcp.vertexai.v1.api.FileData :as fd]
            [gcp.vertexai.v1.api.FunctionCall :as fnc]
            [gcp.vertexai.v1.api.FunctionResponse :as fnr]
            [gcp.global :as global]
            [gcp.protobuf :as protobuf])
  (:import [com.google.cloud.vertexai.generativeai PartMaker]
           [com.google.cloud.vertexai.api Part]
           [java.net URI]))

(def ^{:class Part} schema
  [:or
   :string
   [:map {:closed true} ;; TODO this might not be JSON safe
    [:mimeType {:optional false} :string]
    [:partData
     {:doc ":partData can be:
             - a String representing the uri of the data. Resulting Part will have fileData field set.
             - a GCS URI object. Resulting Part will have fileData field set.
             - byte arrays that represents the actual data. Resulting Part will have inlineData field set.
             - com.google.protobuf.ByteString that represents the actual data. Resulting Part will have inlineData field set."
      :optional false}
     [:or bytes? string? (global/instance-schema URI) protobuf/bytestring-schema]]]
   [:map {:closed true} [:text {:optional false} :string]]
   [:map {:closed true}
    [:inlineData {:optional false} blob/schema]]
   [:map {:closed true}
    [:functionCall {:optional false} fnc/schema]]
   [:map {:closed true}
    [:functionResponse {:optional false} fnr/schema]]
   [:map {:closed true}
    [:fileData {:optional false :node true} fd/schema]]])

(defn ^Part from-edn [arg]
  (global/strict! schema arg)
  (if (string? arg)
    (from-edn {:text arg})
    (if (contains? arg :mimeType)
      ;;TODO to make this json safe, recur this as inline blob
      (PartMaker/fromMimeTypeAndData (:mimeType arg) (:partData arg))
      (let [builder (Part/newBuilder)]
        (when-let [text (:text arg)]
          (.setText builder text))
        (when-let [inline (:inlineData arg)]
          (.setInlineData builder (blob/from-edn inline)))
        (when-let [functionCall (:functionCall arg)]
          (.setFunctionCall builder (fnc/from-edn functionCall)))
        (when-let [functionResponse (:functionResponse arg)]
          (.setFunctionResponse builder (fnr/from-edn functionResponse)))
        (when-let [fileData (:fileData arg)]
          (.setFileData builder (fd/from-edn fileData)))
        (.build builder)))))

(defn ->edn [^Part part]
  (cond-> {}
          (.hasText part)
          (assoc :text (.getText part))
          (.hasFileData part)
          (assoc :fileData (fd/to-edn (.getFileData part)))
          (.hasFunctionCall part)
          (assoc :functionCall (fnc/to-edn (.getFunctionCall part)))
          (.hasFunctionResponse part)
          (assoc :functionResponse (fnr/to-edn (.getFunctionResponse part)))
          (.hasInlineData part)
          (assoc :inlineData (blob/to-edn (.getInlineData part)))))