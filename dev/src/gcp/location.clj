(ns gcp.location
  (:require [clojure.string :as string]
            [gcp.global :as g]))

(def ^:const sep "__")

(defn- sanitize [s] (string/replace s #"-" "_"))

(defn name-safe [location]
  (-> location string/lower-case sanitize))

(defn slug
  "dashes will be replaced by underscores."
  [prefixes location suffixes]
  (let [prefixes (if (string? prefixes)
                   [prefixes]
                   (g/coerce [:maybe [:sequential :string]] prefixes))
        suffixes (if (string? suffixes)
                   [suffixes]
                   (g/coerce [:maybe [:sequential :string]] suffixes))
        prefix (when (seq prefixes)
                 (str (string/join sep (map sanitize prefixes)) sep))
        suffix (when (seq suffixes)
                 (str sep (string/join sep (map sanitize suffixes)) ))]
    (str prefix (name-safe location) suffix)))