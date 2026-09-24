(ns gcp.global.manifest
  (:require [clojure.java.io :as io]
            [clojure.string :as string])
  (:import (java.net JarURLConnection)
           (java.util.jar JarEntry JarFile)))

(defn- namespace-class [ns-sym]
  (Class/forName (str (string/replace (str ns-sym) "-" "_") "__init")))

(defn- jar-file
  [ns]
  (when-let [resource (io/resource (str (string/replace (str (ns-name ns)) "." "/") ".clj"))]
    (when (= "jar" (.getProtocol resource))
      (let [connection (.openConnection resource)]
        (when (instance? java.net.JarURLConnection connection)
          (.getJarFile ^java.net.JarURLConnection connection))))))

(defn manifest
  [ns]
  (when-let [jar (jar-file ns)]
    (.getManifest jar)))

(defn resources
  [ns]
  (when-let [jar (jar-file ns)]
    (try
      (->> (.entries jar)
           enumeration-seq
           (remove #(.isDirectory ^java.util.jar.JarEntry %))
           (mapv #(.getName ^java.util.jar.JarEntry %)))
      (finally
        (.close jar)))))

(defn manifest-info
  [ns]
  (when-let [attrs (manifest ns)]
    (into {}
          (map (fn [[k v]]
                 [(keyword (str (ns-name ns))
                           (string/lower-case
                             (string/replace (str k) "_" "-")))
                  v]))
          attrs)))

(defn build-info
  [ns]
  (let [prefix (str (ns-name ns))]
    {(keyword prefix "manifest") (manifest-info ns)
     (keyword prefix "resources") (resources ns)}))
