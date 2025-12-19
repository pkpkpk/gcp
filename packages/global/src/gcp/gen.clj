(ns gcp.gen
  (:require [gcp.global :as g]
            [malli.generator :as mg]))

(defn generate
  ([?gen-or-schema]
   (generate ?gen-or-schema nil))
  ([?gen-or-schema opts]
   (if (keyword? ?gen-or-schema)
     (if-let [schema (g/get-schema ?gen-or-schema)]
       (mg/generate schema (merge (g/mopts) opts))
       (throw (Exception. (str "could not find schema '" ?gen-or-schema "'"))))
     (mg/generate ?gen-or-schema (merge (g/mopts) opts)))))

(defn generator
  ([?schema]
   (generator ?schema nil))
  ([?schema opts]
   (if (keyword? ?schema)
     (if-let [schema (g/get-schema ?schema)]
       (mg/generator schema (merge (g/mopts) opts))
       (throw (Exception. (str "could not find schema '" ?schema "'"))))
     (mg/generator ?schema (merge (g/mopts) opts)))))

#_(defn sample [])
#_(defn function-checker [])
#_(defn check [])
