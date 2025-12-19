(ns gcp.vertexai.v1.api.ExecutableCode
  (:require [gcp.global :as global])
  (:import (com.google.cloud.vertexai.api ExecutableCode ExecutableCode$Language)))

(defn ^ExecutableCode$Language Language-from-edn [arg]
  (if (number? arg)
    (ExecutableCode$Language/forNumber (int arg))
    (ExecutableCode$Language/valueOf ^String arg)))

(defn Language-to-edn [arg]
  (if (int? arg)
    (.name (ExecutableCode$Language/forNumber arg))
    (if (string? arg)
      arg
      (if (instance? ExecutableCode$Language arg)
        (.name arg)
        (throw (ex-info "unsupported arg" {:arg arg}))))))

(defn ^ExecutableCode from-edn [arg]
  (global/strict! :gcp.vertexai.v1.api/ExecutableCode arg)
  (let [builder (ExecutableCode/newBuilder)]
    (some->> (:language arg) Language-from-edn (.setLanguage builder))
    (some->> (:code arg) (.setCode builder))
    (.build builder)))

(defn to-edn [^ExecutableCode arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/ExecutableCode %)]}
  {:language (Language-to-edn (.getLanguage arg))
   :code     (.getCode arg)})

(defn to-edn [^ExecutableCode arg]
  {:post [(global/strict! :gcp.vertexai.v1.api/ExecutableCode %)]}
  {:language (Language-to-edn (.getLanguage arg))
   :code     (.getCode arg)})

(def schema
  [:map
   {:class 'com.google.cloud.vertexai.api.ExecutableCode}
   [:language {:optional true} [:or :string :int]]
   [:code {:optional true} :string]])

(global/register-schema! :gcp.vertexai.v1.api/ExecutableCode schema)
