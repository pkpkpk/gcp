(ns gcp.pubsub.v1.PublisherStub
  (:require [gcp.global :as global])
  (:import (com.google.cloud.pubsub.v1.stub PublisherStub)))

(defn to-edn [^PublisherStub arg] (throw (Exception. "unimplemented")))

(defn ^PublisherStub from-edn [edn] (throw (Exception. "unimplemented")))

(global/include-schema-registry! (with-meta {:gcp.pubsub.v1/PublisherStub :any} {:gcp.global/name (str *ns*)}))