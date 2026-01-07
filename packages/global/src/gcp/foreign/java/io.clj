(ns gcp.foreign.java.io
 {:gcp.dev/certification
   {:Closeable
      {:base-seed 1767752423847
       :passed-stages
         {:smoke 1767752423847 :standard 1767752423848 :stress 1767752423849}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "a3d2c81a2a95e0c911719116d4e06adc9967d4cde237e05103c6f61b719fda14"
       :timestamp "2026-01-07T02:20:23.983066540Z"}
    :InputStream
      {:base-seed 1767752423985
       :passed-stages
         {:smoke 1767752423985 :standard 1767752423986 :stress 1767752423987}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "a3d2c81a2a95e0c911719116d4e06adc9967d4cde237e05103c6f61b719fda14"
       :timestamp "2026-01-07T02:20:24.091079513Z"}
    :ObjectInputStream
      {:base-seed 1767752424092
       :passed-stages
         {:smoke 1767752424092 :standard 1767752424093 :stress 1767752424094}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "a3d2c81a2a95e0c911719116d4e06adc9967d4cde237e05103c6f61b719fda14"
       :timestamp "2026-01-07T02:20:24.182310532Z"}
    :OutputStream
      {:base-seed 1767752424183
       :passed-stages
         {:smoke 1767752424183 :standard 1767752424184 :stress 1767752424185}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "a3d2c81a2a95e0c911719116d4e06adc9967d4cde237e05103c6f61b719fda14"
       :timestamp "2026-01-07T02:20:24.238342730Z"}
    :Serializable
      {:base-seed 1767752424239
       :passed-stages
         {:smoke 1767752424239 :standard 1767752424240 :stress 1767752424241}
       :protocol-hash
         "ded1d125f471c4d242e8e29b611cdf56cf0fc0369a2acfe777bcaab506d5d48e"
       :source-hash
         "a3d2c81a2a95e0c911719116d4e06adc9967d4cde237e05103c6f61b719fda14"
       :timestamp "2026-01-07T02:20:24.319950109Z"}}}
  (:require
    [gcp.global :as g]
    [malli.core :as m])
  (:import
    (java.io Closeable InputStream OutputStream ByteArrayInputStream ByteArrayOutputStream Serializable ObjectInputStream ObjectOutputStream)))

(def registry
  ^{:gcp.global/name :gcp.foreign.java.io/registry}
  {:gcp.foreign.java.io/InputStream [:or {:gen/schema :string}
                                     (g/instance-schema java.io.InputStream)
                                     :string]
   :gcp.foreign.java.io/OutputStream [:or {:gen/schema :nil}
                                      (g/instance-schema java.io.OutputStream)
                                      :nil]
   :gcp.foreign.java.io/Closeable [:or {:gen/schema :string}
                                   (g/instance-schema java.io.Closeable)
                                   :string]
   :gcp.foreign.java.io/Serializable [:or {:gen/schema :string}
                                      (g/instance-schema java.io.Serializable)
                                      :string]
   :gcp.foreign.java.io/ObjectInputStream [:or {:gen/schema :string}
                                           (g/instance-schema java.io.ObjectInputStream)
                                           :string]})

(g/include-schema-registry! registry)

(defn ^InputStream InputStream-from-edn [arg]
  (cond
    (instance? InputStream arg) arg
    (string? arg) (ByteArrayInputStream. (.getBytes ^String arg "UTF-8"))
    :else (ByteArrayInputStream. arg)))

(defn InputStream-to-edn [^InputStream arg]
  (if (instance? ByteArrayInputStream arg)
    (let [bytes (.readAllBytes arg)]
      (String. bytes "UTF-8"))
    (str arg)))

(defn ^OutputStream OutputStream-from-edn [arg]
  (if (instance? OutputStream arg)
    arg
    (ByteArrayOutputStream.)))

(defn OutputStream-to-edn [^OutputStream arg]
  nil)

(defn ^Closeable Closeable-from-edn [arg]
  (if (instance? Closeable arg)
    arg
    (InputStream-from-edn arg)))

(defn Closeable-to-edn [^Closeable arg]
  (if (instance? InputStream arg)
    (InputStream-to-edn arg)
    (str arg)))

(defn ^Serializable Serializable-from-edn [arg]
  arg)

(defn Serializable-to-edn [^Serializable arg]
  (str arg))

(defn ^ObjectInputStream ObjectInputStream-from-edn [arg]
  (if (instance? ObjectInputStream arg)
    arg
    (let [baos (ByteArrayOutputStream.)
          oos (ObjectOutputStream. baos)]
      (.writeObject oos arg)
      (.close oos)
      (ObjectInputStream. (ByteArrayInputStream. (.toByteArray baos))))))

(defn ObjectInputStream-to-edn [^ObjectInputStream arg]
  (try
    (let [obj (.readObject arg)]
      (str obj))
    (catch Exception _ nil)))