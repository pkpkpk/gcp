(ns gcp.foreign.com.google.api.core
  (:import (com.google.api.core AbstractApiService ApiFuture ApiService)))

(defn AbstractApiService-from-edn [_] (throw (Exception. "unimplemented")))
(defn AbstractApiService-to-edn [_] (throw (Exception. "unimplemented")))
(defn ApiFuture-from-edn [_] (throw (Exception. "unimplemented")))
(defn ApiFuture-to-edn [_] (throw (Exception. "unimplemented")))
(defn ApiService-from-edn [_] (throw (Exception. "unimplemented")))
(defn ApiService-to-edn [_] (throw (Exception. "unimplemented")))
